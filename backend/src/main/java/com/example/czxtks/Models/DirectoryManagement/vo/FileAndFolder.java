package com.example.czxtks.Models.DirectoryManagement.vo;

public class FileAndFolder {
    public String filename=new String();//文件名
    public int canRead;
    public int canWrite;
    public String user=new String();//文件所有者
    public int filesize;//文件大小
    public String date = new String();    // 文件时间，调用无参数构造函数
    public String address = new String();//在磁盘中存放的地址

    //目录名
    public String folderName;

}
