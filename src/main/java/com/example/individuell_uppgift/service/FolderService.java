package com.example.individuell_uppgift.service;

import org.springframework.stereotype.Service;

import java.io.File;

/**
 * This class is for business logic for folder operations.
 */

@Service
public class FolderService {
    /**
     * Create a new folder.
     * @param folderName The name of the folder to create.
     * @return true if created, false if not created.
     */
    public boolean createFolder(String folderName){
        String directory = "/Users/schih/Documents/" + folderName;
        File f = new File(directory);
        return f.mkdirs();
    }
}
