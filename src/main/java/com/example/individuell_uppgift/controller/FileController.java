package com.example.individuell_uppgift.controller;

import com.example.individuell_uppgift.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
public class FileController {

    private final FileService fileService;

    @PostMapping("/file")
    public ResponseEntity<?> uploadFile(@RequestParam String folderName,@RequestParam MultipartFile file){
        return fileService.uploadFile(file,folderName);
    }
}
