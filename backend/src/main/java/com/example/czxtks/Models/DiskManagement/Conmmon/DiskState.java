package com.example.czxtks.Models.DiskManagement.Conmmon;

import java.util.ArrayList;

public class DiskState{
    public ArrayList<Integer> ordblock = new ArrayList<Integer>();//磁盘的普通文件盘块状态
    public ArrayList<Integer> exblock = new ArrayList<Integer>();//磁盘的交换区盘块状态

}