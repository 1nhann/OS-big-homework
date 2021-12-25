package com.example.czxtks.Controller;

import com.example.czxtks.Models.DirectoryManagement.DirectoryManagement;
import com.example.czxtks.Models.DirectoryManagement.DirectoryManagementImpl;
import com.example.czxtks.Models.DiskManagement.DiskManagement;
import com.example.czxtks.Models.DiskManagement.DiskManagementImpl;
import com.example.czxtks.Models.MemoryManagement.MemoryManagement;
import com.example.czxtks.Models.MemoryManagement.MemoryManagementImpl;
import com.sun.org.apache.bcel.internal.generic.RETURN;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
//import sun.security.mscapi.CPublicKey;
import sun.security.x509.SerialNumber;

import java.sql.Struct;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ArrayList;

/*
需要前端判定，如果大于256K，直接弹出超过文件最大大小

 */
@RestController
public class DiskManagementController{

    @Autowired
    private DiskManagementImpl DiskManager;

    @ResponseBody
    @ApiOperation(value = "磁盘初始化", notes = "运行结束 返回当前的1024个块的状态")
    @PostMapping("/DiskSpaceInit")//磁盘初始化
    public String DiskSpaceInit() {//磁盘空间初始化

        return DiskManager.DiskSpaceInit();
    }
    @ResponseBody
    @PostMapping("/DiskState")//磁盘当前状态打印
    @ApiOperation(value = "磁盘状态打印", notes = "磁盘的1024块的状态进行打印")
    public String DiskState() {//打印当前的磁盘使用状态

        return DiskManager.DiskState();
    }

    @ResponseBody
    @PostMapping("/DeleteFile")//删除文件
    @ApiOperation(value = "删除文件", notes = "删除索引块IndexNode所对应的全部文件，并返回是否成功")
    @ApiImplicitParam(name = "IndexNode", value = "删除文件的主索引块的值", dataType = "int", example = "0")
    public boolean DeleteFile(int IndexNode) {
        DirectoryManagement DirectoryManager = new DirectoryManagementImpl();
        MemoryManagement MemoryManager = new MemoryManagementImpl();
        DirectoryManager.DeleteDirectory(IndexNode);
        MemoryManager.malloc(8);
        return DiskManager.DeleteFile(IndexNode);
    }

    @ResponseBody
    @PostMapping("/fileMake")//文件创建
    @ApiOperation(value = "文件创建函数",notes = "返回对应的主索引块号，一级索引块中的二级索引块号，二级索引块中的全部普通文件地址块号")
    public  String FileMake( ) { //返回文件对应的主索引块

        return DiskManager.FileMake();
    }

    @ResponseBody
    @PostMapping("/ExchangeAreaADD")//向兑换区加入文件
    @ApiOperation(value = " 向兑换区加入文件", notes = "输入文件的ID标识号 文件的长度 返回当前的兑换区全部的地址块分配情况")
    @ApiImplicitParams(value = {@ApiImplicitParam(name = "Length", value = "文件长度", dataType = "int", example = "100"), @ApiImplicitParam(name = "ID", dataType = "int", value = "文件所对应的ID号", example = "2084")})
    public String ExchangeAreaADD(int Length, int ID) {

        return DiskManager.ExchangeAreaADD(Length , ID);
    }

    @ResponseBody
    @PostMapping("/ExchangeAreaDelete")//向兑换区删除文件
    @ApiOperation(value = " 兑换区删除文件", notes = "输入文件的ID标识号 文件的长度 返回当前的兑换区全部的地址块分配情况")
    @ApiImplicitParams(value = {@ApiImplicitParam(name = "Length", value = "文件长度", dataType = "int", example = "100"), @ApiImplicitParam(name = "ID", dataType = "int", value = "文件所对应的ID号", example = "2084")})
    public String ExchangeAreaDelete(int Length, int ID) {
        return DiskManagement.ExchangeAreaDelete(Length,ID);
    }
    @ResponseBody
    @PostMapping("/DeleteFile_SingleIndex")//删除文件
    @ApiOperation(value = "删除文件",notes = "删除索引块IndexNode所对应的全部文件，并返回是否成功")
    @ApiImplicitParam(name ="IndexNode",value = "删除文件的主索引块的值",dataType = "int",example = "0")
    public  boolean DeleteFile_SingleIndex(int IndexNode){

        return DiskManager.DeleteFile_SingleIndex(IndexNode);
    }

    @ResponseBody
    @PostMapping("/FileSizeView")//查找文件大小
    @ApiOperation(value = "查找文件大小",notes = "查找索引块IndexNode所对应的全部文件，并返回文件的大小")
    @ApiImplicitParam(name ="IndexNode",value = "获得文件的主索引块的值,返回文件大小",dataType = "int",example = "0")
    @CrossOrigin(origins = "*",maxAge = 3600)
    public  String FileSizeView(int IndexNode){

        return DiskManager.FileSizeView(IndexNode);
    }

    @ResponseBody
    @PostMapping("/FileView")//查找文件内容
    @ApiOperation(value = "查找文件内容",notes = "查找索引块IndexNode所对应的全部文件，并返回文件的内容")
    public String FileView(int IndexNode) {
        return DiskManager.FileView(IndexNode);
    }

}


