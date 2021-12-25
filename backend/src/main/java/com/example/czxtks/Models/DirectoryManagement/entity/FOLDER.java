package com.example.czxtks.Models.DirectoryManagement.entity;

public class FOLDER {//目录结构
    public String name=new String();//目录名
    public int canRead;//是否可读
    public int canWrite;//是否可写
    public FOLDER nextFolder;//同级下一目录
    public FOLDER frontFolder;//同级上一目录
    public FOLDER parentFolder;//父目录
    public FOLDER firstChildFolder;//子目录
    public FILE firstChildFile;//子文件
    public int num;//第几级目录

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCanRead() {
        return canRead;
    }

    public void setCanRead(int canRead) {
        this.canRead = canRead;
    }

    public int getCanWrite() {
        return canWrite;
    }

    public void setCanWrite(int canWrite) {
        this.canWrite = canWrite;
    }

    public FOLDER getNextFolder() {
        return nextFolder;
    }

    public void setNextFolder(FOLDER nextFolder) {
        this.nextFolder = nextFolder;
    }

    public FOLDER getFrontFolder() {
        return frontFolder;
    }

    public void setFrontFolder(FOLDER frontFolder) {
        this.frontFolder = frontFolder;
    }

    public FOLDER getParentFolder() {
        return parentFolder;
    }

    public void setParentFolder(FOLDER parentFolder) {
        this.parentFolder = parentFolder;
    }

    public FOLDER getFirstChildFolder() {
        return firstChildFolder;
    }

    public void setFirstChildFolder(FOLDER firstChildFolder) {
        this.firstChildFolder = firstChildFolder;
    }

    public FILE getFirstChildFile() {
        return firstChildFile;
    }

    public void setFirstChildFile(FILE firstChildFile) {
        this.firstChildFile = firstChildFile;
    }

    @Override
    public String toString() {
        return "FOLDER{" +
                "name:'" + name+
                ", canRead:" + canRead +
                ", canWrite:" + canWrite +
                ", nextFolder:" + nextFolder +
                ", frontFolder:" + frontFolder +
                ", parentFolder:" + parentFolder +
                ", firstChildFolder:" + firstChildFolder +
                ", firstChildFile:" + firstChildFile +
                '}';
    }
}
