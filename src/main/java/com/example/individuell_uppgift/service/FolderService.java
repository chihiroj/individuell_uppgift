package com.example.individuell_uppgift.service;

import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class FolderService {

    public boolean createFolder(String folderName){
        String directory = "/Users/schih/Documents/" + folderName;
        File f = new File(directory);
        return f.mkdirs();
    }
}
