package com.example.czxtks.Models.DirectoryManagement.entity;

public class FILE {
    public String name=new String();//文件名
    public String content=new String();//文件内容
    public int canRead;
    public int canWrite;
    public int filesize;
    public FILE frontFile;//同级目录上一文件
    public FILE nextFile;//同级目录下一文件
    public FOLDER parentFolder;//父目录
    public String user=new String();//文件所有者
    public String date = new String();    // 文件时间，调用无参数构造函数
    public String address = new String();//在磁盘中存放的地址

    public boolean isReading;
    public boolean isWrting;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public FILE getFrontFile() {
        return frontFile;
    }

    public void setFrontFile(FILE frontFile) {
        this.frontFile = frontFile;
    }

    public FILE getNextFile() {
        return nextFile;
    }

    public void setNextFile(FILE nextFile) {
        this.nextFile = nextFile;
    }

    public FOLDER getParentFolder() {
        return parentFolder;
    }

    public void setParentFolder(FOLDER parentFolder) {
        this.parentFolder = parentFolder;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getFilesize(){return filesize;}
    public void setFilesize(int filesize){this.filesize = filesize;}

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "FILE{" +
                "name:'" + name  +
                ", content:'" + content +
                ", canRead:" + canRead +
                ", canWrite:" + canWrite +
                ", frontFile:" + frontFile +
                ", nextFile:" + nextFile +
                ", parentFolder:" + parentFolder +
                ", user:'" + user  +
                ", date:'" + date  +
                ", address:'" + address  +
                '}';
    }
}
