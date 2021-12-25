package com.example.czxtks.Models.Threads;

import com.example.czxtks.Models.DirectoryManagement.DireService;
import com.example.czxtks.Models.MemoryManagement.service.IBlockService;

public class DirectoryDeleteThread implements Runnable{
    private int threadId;
    private DireService directoryManagement;
    String thisFolderName ;
    int theLevelOfDirectory;
    @Override
    public void run() {
        directoryManagement.deleteFoder(thisFolderName,theLevelOfDirectory);
    }
    public DirectoryDeleteThread(int id , DireService directoryManagement, String thisFolderName, int num){
        this.threadId = id;
        this.directoryManagement = directoryManagement;
        this.thisFolderName = thisFolderName;
        this.theLevelOfDirectory = num;
    }
}