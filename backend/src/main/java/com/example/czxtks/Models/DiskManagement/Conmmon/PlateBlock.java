package com.example.czxtks.Models.DiskManagement.Conmmon;


import java.util.ArrayList;

public class PlateBlock {
    public int id;//盘块号
    public int diskBlockStatus=0;//当前盘块号的状态 : 未使用为0号，普通数据为1号，索引表1级为2号，索引表2级为3号，目录表为4号
    //采用交换区为另外的数组表示，所以交换区不需要判断目录和索引，只有1号和0号
    public ArrayList<Integer> IndexNode = new ArrayList<>();//若为索引块则为其创建索引数组，存取索引的下一节点指针
    public String content1 = "";//当前地址块存放的字符
    public String content = "";//存储文件内容
    public int  FileSize = 0;//文件大小
    public PlateBlock(int id){
        this.id = id;
        this.diskBlockStatus = 0;
    }
    public PlateBlock(int id , int a){
        this.id = id;
        this.diskBlockStatus = 0;
    }
}
