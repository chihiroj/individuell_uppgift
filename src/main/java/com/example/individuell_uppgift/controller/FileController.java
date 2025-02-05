package com.example.individuell_uppgift.controller;

import com.example.individuell_uppgift.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
public class FileController {

    private final FileService fileService;

    @PostMapping("/file")
    public ResponseEntity<?> uploadFile(@RequestParam String folderName,@RequestParam MultipartFile file){
        return fileService.uploadFile(file,folderName);
    }

    @DeleteMapping("/file")
    public ResponseEntity<?> deleteFile(@RequestParam String folderName, @RequestParam String fileName){
        return fileService.deleteFile(folderName, fileName);
    }
    @GetMapping("/file")
    public ResponseEntity<?> downloadFile(@RequestParam String folderName, @RequestParam String fileName) {
        return fileService.downloadFile(folderName, fileName);
    }
}
