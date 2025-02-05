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

@Service
public class FileService {
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

    private static String getFileLocation(String folderName, String fileName, String userHome) {
        return userHome + File.separator + "Documents" + File.separator + folderName + File.separator + fileName;
    }

    private static String getDirectory(String folderName, String userHome) {
        return userHome + File.separator + "Documents" + File.separator + folderName;
    }

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
