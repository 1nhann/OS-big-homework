package com.example.czxtks.Models.Threads;

import com.example.czxtks.Models.DirectoryManagement.DireService;

public class FileMakeThread implements Runnable{
    private int threadId;
    private DireService directoryManagement;
    String fileName ;
    String owner ;
    String directoryName ;
    String content;
    int theLevelOfDirectory;
    @Override
    public void run() {
            directoryManagement.createFile(directoryName , fileName , owner ,content, theLevelOfDirectory);
    }
    public FileMakeThread(int id ,DireService directoryManagement,  String fileName , String owner , String directoryName , String content,int num){
        this.directoryManagement = directoryManagement;
        this.threadId = id;
        this.owner = owner;
        this.fileName = fileName;
        this.directoryName = directoryName;
        this.theLevelOfDirectory = num;
        this.content = content;
    }
}
