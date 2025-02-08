package com.example.individuell_uppgift.service;

import com.example.individuell_uppgift.model.MessageResponseDTO;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;

/**
 *Handle business logic for file operations.
 */
@Service
public class FileService {
    /**
     *Upload a new file.
     * @param file The file to upload.
     * @param folderName The folder to upload the file to.
     * @return Success or error message
     */
    public ResponseEntity<MessageResponseDTO> uploadFile(MultipartFile file, String folderName){
        String userHome = System.getProperty("user.home");
        String directory = getDirectory(folderName, userHome);

        //Get location of folder provided by user in request
        File folder = new File(directory);
        if(!folder.exists()){
            //Return error if folder does not exist
            return ResponseEntity.badRequest().body(new MessageResponseDTO("The folder does not exist."));
        }

        //Get filename
        String originalFilename = file.getOriginalFilename();
        if(originalFilename == null || originalFilename.isEmpty()){
            return ResponseEntity.badRequest().body(new MessageResponseDTO("Something wrong with the file. Try again."));
        }

        //Where to save file
        File fileToUpload = new File(folder, originalFilename);
        try{
            file.transferTo(fileToUpload);
            return ResponseEntity.ok(new MessageResponseDTO("Success"));
        }catch (IOException exception){
            exception.printStackTrace();
            return ResponseEntity.internalServerError().body(new MessageResponseDTO("Something went wrong. Try again."));
        }
    }

    /**
     *Delete a file.
     * @param folderName The folder to delete the file from.
     * @param fileName The file to delete.
     * @return Success or error message.
     */
    public ResponseEntity<MessageResponseDTO>deleteFile(String folderName, String fileName){
        String userHome = System.getProperty("user.home");
        String directory = getDirectory(folderName, userHome);

        File folder = new File(directory);
        if(!folder.exists()){
            //Return error if folder does not exist
            return ResponseEntity.badRequest().body(new MessageResponseDTO("The folder does not exist."));
        }

        String fileLocation = getFileLocation(folderName, fileName, userHome);

        File file = new File(fileLocation);
        if(!file.exists()){
            return ResponseEntity.badRequest().body(new MessageResponseDTO("The file does not exist."));
        }
        boolean deleted = file.delete();
        if(deleted) {
            return ResponseEntity.ok(new MessageResponseDTO("Success"));
        }

        return ResponseEntity.badRequest().body(new MessageResponseDTO("Could not delete the file."));
    }

    /**
     *Get the path to a file.
     * @param folderName The last folder in the path.
     * @param fileName The file in the path.
     * @param userHome User directory on the current computer.
     * @return location for the file.
     */
    private static String getFileLocation(String folderName, String fileName, String userHome) {
        return userHome + File.separator + "Documents" + File.separator + folderName + File.separator + fileName;
    }

    /**
     *Get the path to a directory.
     * @param folderName The last folder in the path.
     * @param userHome User directory on the current computer.
     * @return The path to the directory.
     */
    private static String getDirectory(String folderName, String userHome) {
        return userHome + File.separator + "Documents" + File.separator + folderName;
    }

    /**
     *Download a selected file.
     * @param folderName The folder to download the file from.
     * @param fileName  The file to download.
     * @return The file which is found.
     */
    public ResponseEntity<?> downloadFile(String folderName, String fileName) {
        String userHome = System.getProperty("user.home");
        String directory = getDirectory(folderName, userHome);

        File folder = new File(directory);
        if(!folder.exists()){
            //Return error if folder does not exist
            return ResponseEntity.badRequest().body(new MessageResponseDTO("The folder does not exist."));
        }

        String fileLocation = getFileLocation(folderName, fileName, userHome);

        File file = new File(fileLocation);
        if(!file.exists()){
            return ResponseEntity.badRequest().body(new MessageResponseDTO("The file does not exist."));
        }

        try {
            //Determine the content type
            String contentType = Files.probeContentType(file.toPath());
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            //Create an InputStreamResource for the file
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

            //Set headers for the response
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"");
            headers.add(HttpHeaders.CONTENT_TYPE, contentType);
            headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(file.length()));

            //Return the file content as a ResponseEntity
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(resource);

        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(new MessageResponseDTO("Error downloading file."));
        }
    }
}
