package com.example.individuell_uppgift.controller;

import com.example.individuell_uppgift.model.MessageResponseDTO;
import com.example.individuell_uppgift.service.FolderService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.message.Message;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@RestController
@RequiredArgsConstructor
public class FolderController {

    private final FolderService folderService;

    @PostMapping("/folder")
    public ResponseEntity<?> createFolder (@RequestParam String folderName){

        boolean success = folderService.createFolder(folderName);

        if(success){
            return ResponseEntity.ok(new MessageResponseDTO("Success"));
        }else return ResponseEntity.internalServerError().body(new MessageResponseDTO("Something went wrong"));


    }
}
