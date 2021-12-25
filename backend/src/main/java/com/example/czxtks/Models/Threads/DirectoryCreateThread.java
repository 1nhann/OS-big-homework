package com.example.czxtks.Models.Threads;

import com.example.czxtks.Models.DirectoryManagement.DireService;
import com.example.czxtks.Models.MemoryManagement.service.IBlockService;

public class DirectoryCreateThread implements Runnable{
    private int threadId;
    private DireService directoryManagement;
    String parentFolderName ;
    String thisFolderName ;
    int theLevelOfDirectory;
    @Override
    public void run() {
        directoryManagement.createFolder(parentFolderName,thisFolderName,theLevelOfDirectory);
    }
    public DirectoryCreateThread(int id , DireService directoryManagement,  String parentFolderName, int num, String thisFolderName){
        this.threadId = id;
        this.directoryManagement = directoryManagement;
        this.theLevelOfDirectory = num;
        this.parentFolderName = parentFolderName;
        this.thisFolderName = thisFolderName;
        this.theLevelOfDirectory = num;
    }
}
