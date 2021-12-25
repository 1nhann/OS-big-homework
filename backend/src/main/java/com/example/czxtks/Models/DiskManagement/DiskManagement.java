package com.example.czxtks.Models.DiskManagement;

import com.example.czxtks.Models.DiskManagement.Conmmon.DiskToMem;

public interface DiskManagement {

    public String DiskSpaceInit();
    public String DiskState();
    public boolean DeleteFile(int IndexNode);
    public String FileMake();
    public int FindFree();
    public String ExchangeAreaADD(int Length, int ID);
    public static String ExchangeAreaDelete(int Length, int ID) {
        return null;
    }
    public  boolean DeleteFile_SingleIndex(int IndexNode);
    public  String FileSizeView(int IndexNode);
    public String FileView(int IndexNode);
    public  String FileEdit_Singleindex(int Indexblock,int FileSize,String content);
    public  String FileEdit(int Indexblock,int FileSize,String content);
    public DiskToMem getNewPage(int blocknum);
}
