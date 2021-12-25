package com.example.czxtks.Models;

public interface Kernel {
    //增
    public String FileMake(String fileName , String owner  , String directoryName , String content , int num);
    public String DirectoryCreate(String parentFolderName ,  int theLevelOfParentFolder , String thisFolderName );
    //删
    public String FileDelete(String fileName);
    public String DirectoryDelete(String thisFolderName , int theLevelOfThisFolder);
    //改
     public String EditFileContent(String fileName, String content);
     public String RenameFile(String oldName , String newName);
     public String RenameDirectory(String oldName , String newName, int num);
    //查
    // 或者叫做 DiskToMemory
    public String ReadFileContent(String fileName);
}