package com.example.czxtks.Models.Threads;

import com.example.czxtks.Models.DirectoryManagement.DireService;

public class EditFileContentThread implements Runnable{
    private int threadId;
    private DireService directoryManagement;
    //    private IBlockService memoryManagement;
    String fileName ;
    String owner ;
    String directoryName ;
    String content;
    int theLevelOfDirectory;
    @Override
    public void run() {
//        directoryManagement.deleteFile(fileName);
//        directoryManagement.createFile(directoryName , fileName , owner ,content, theLevelOfDirectory);
//            memoryManagement.freeMemory(threadId);
        directoryManagement.changeFileContent(content,fileName);
    }
    public EditFileContentThread(int id ,DireService directoryManagement,  String fileName , String content){
        this.directoryManagement = directoryManagement;
        this.threadId = id;
//        this.memoryManagement = memoryManagement;
        this.owner = owner;
        this.fileName = fileName;
        this.directoryName = directoryName;
//        this.theLevelOfDirectory = num;
        this.content = content;
    }
}