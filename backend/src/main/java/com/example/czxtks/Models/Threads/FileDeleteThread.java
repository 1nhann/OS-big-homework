package com.example.czxtks.Models.Threads;
import com.example.czxtks.Models.DirectoryManagement.DireService;
import com.example.czxtks.Models.MemoryManagement.service.IBlockService;

public class FileDeleteThread implements Runnable{
    private int threadId;
    private DireService directoryManagement;
    String fileName ;
    @Override
    public void run() {
        directoryManagement.delete_Single_File(fileName);
    }
    public FileDeleteThread(int id , DireService directoryManagement, String fileName){
        this.threadId = id;
        this.directoryManagement = directoryManagement;
        this.fileName = fileName;
    }
}