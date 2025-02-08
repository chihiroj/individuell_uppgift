package com.example.individuell_uppgift.controller;

import com.example.individuell_uppgift.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * This class is for managing file operations.
 */
@RequiredArgsConstructor
@RestController
public class FileController {

    private final FileService fileService;

    /**
     * This endpoint is for uploading a new file.
     * @param folderName The folder to upload the file to.
     * @param file The file to upload.
     * @return Status message.
     */
    @PostMapping("/file")
    public ResponseEntity<?> uploadFile(@RequestParam String folderName,@RequestParam MultipartFile file){
        return fileService.uploadFile(file,folderName);
    }

    /**
     * This endpoint is for deleting a selected file.
     * @param folderName The folder to delete the file from.
     * @param fileName The file to delete.
     * @return Status message.
     */
    @DeleteMapping("/file")
    public ResponseEntity<?> deleteFile(@RequestParam String folderName, @RequestParam String fileName){
        return fileService.deleteFile(folderName, fileName);
    }

    /**
     * This endpoint is for downloading a file.
     * @param folderName The folder to download the file from.
     * @param fileName The file to download.
     * @return Status message.
     */
    @GetMapping("/file")
    public ResponseEntity<?> downloadFile(@RequestParam String folderName, @RequestParam String fileName) {
        return fileService.downloadFile(folderName, fileName);
    }
}
