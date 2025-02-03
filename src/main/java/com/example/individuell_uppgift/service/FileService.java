package com.example.individuell_uppgift.service;

import com.example.individuell_uppgift.model.MessageResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class FileService {
    public ResponseEntity<MessageResponseDTO> uploadFile(MultipartFile file, String folderName){
        String userHome = System.getProperty("user.home");
        String directory = userHome + File.separator + "Documents" + File.separator + folderName;

        //Get location of folder provided by user in request
        File folder = new File(directory);
        if(!folder.exists()){
            //Return error if folder does not exist
            System.out.println(folder.getAbsolutePath());
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
}
