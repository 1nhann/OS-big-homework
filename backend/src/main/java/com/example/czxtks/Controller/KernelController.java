package com.example.czxtks.Controller;
import com.example.czxtks.Models.DirectoryManagement.DireService;
import com.example.czxtks.Models.DirectoryManagement.result.Result;
import com.example.czxtks.Models.DiskManagement.DiskManagement;
import com.example.czxtks.Models.Kernel;
import com.example.czxtks.Models.MemoryManagement.service.IBlockService;
import com.example.czxtks.Models.Threads.*;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/*
需要前端判定，如果大于256K，直接弹出超过文件最大大小

 */
@RestController
public class KernelController implements Kernel {

    @Autowired
    private DiskManagement diskManagement;

     @Autowired
     private IBlockService iBlockService;

    @Autowired
    private DireService dirService;

    @Autowired
    private ThreadService threadService;

    @ResponseBody
    @GetMapping("/FileMake")//文件创建
    @ApiOperation(value = "文件创建函数")
    @ApiImplicitParams({
    @ApiImplicitParam(name = "fileName", value = "文件名", dataType = "String", example = "test.txt"),
    @ApiImplicitParam(name = "owner", value = "文件所有者", dataType = "String", example = "root"),
    @ApiImplicitParam(name = "directoryName", value = "存储目录", dataType = "String", example = "/var/www/html/"),
    @ApiImplicitParam(name = "content", value = "数据内容", dataType = "String", example = "hello world"),
    @ApiImplicitParam(name = "num", value = "父目录所在级数", dataType = "int", example = "1")})
    public String FileMake(String fileName , String owner  , String directoryName , String content , int num) {
        int nextThreadId = threadService.getNextId();
        Thread t = new Thread(new FileMakeThread(nextThreadId,dirService,fileName,owner,directoryName,content,num));
        t.run();
        return "1";
    }

    @ResponseBody
    @GetMapping("/DirectoryCreate")//文件创建
    @ApiOperation(value = "目录创建函数")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "parentFolderName", value = "父目录名字", dataType = "String", example = "www"),
            @ApiImplicitParam(name = "thisFolderName", value = "所创建的目录名", dataType = "String", example = "/var/www/html/"),
            @ApiImplicitParam(name = "num", value = "父目录所在级数", dataType = "int", example = "1")})
    public String DirectoryCreate(String parentFolderName, int num, String thisFolderName) {
        int nextThreadId = threadService.getNextId();
        Thread t = new Thread(
                new DirectoryCreateThread(nextThreadId,dirService,parentFolderName,num,thisFolderName)
        );
        t.run();
        return "1";
    }
    @ResponseBody
    @GetMapping("/FileDelete")//文件删除
    @ApiOperation(value = "文件删除函数")
    @ApiImplicitParam(name = "fileName", value = "文件名", dataType = "String", example = "passwd.txt")
    public String FileDelete(String fileName) {
        int nextThreadId = threadService.getNextId();
        Thread t = new Thread(
                new FileDeleteThread(nextThreadId,dirService,fileName)
        );
        t.run();
        return "1";
    }

    @ResponseBody
    @GetMapping("/DirectoryDelete")//文件创建
    @ApiOperation(value = "目录删除函数")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "folderName", value = "所删除的目录的名字", dataType = "String", example = "www"),
            @ApiImplicitParam(name = "num", value = "目录所在级数", dataType = "int", example = "1")})
    public String DirectoryDelete(String folderName, int num) {

        int nextThreadId = threadService.getNextId();
        Thread t = new Thread(
                new DirectoryDeleteThread(nextThreadId,dirService,folderName,num)
        );
        t.run();
        return "1";
    }

    @ResponseBody
    @GetMapping("/EditFileContent")//文件编辑
    @ApiOperation(value = "文件内容函数")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "fileName", value = "文件名", dataType = "String", example = "test.txt"),
            @ApiImplicitParam(name = "content", value = "数据内容", dataType = "String", example = "hello world"),

    })
    @Override
    public String EditFileContent(String fileName, String content) {
        int nextThreadId = threadService.getNextId();
        Thread t = new Thread(
                new EditFileContentThread(nextThreadId,dirService,fileName,content)
        );
        t.run();
        return "1";
    }

    @ResponseBody
    @PostMapping("/RenameFile")//文件创建
    @ApiOperation(value = "文件创建函数")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "oldName", value = "原文件名", dataType = "String", example = "test.txt"),
            @ApiImplicitParam(name = "newName", value = "新文件名", dataType = "String", example = "hello world"),
    })
    @Override
    public String RenameFile(String oldName, String newName) {
        try {
            dirService.renameFile(oldName,newName);
        }
        catch (Exception e){
            return e.getMessage();
        }
        return "1";
    }
    @ResponseBody
    @PostMapping("/RenameDirectory")//文件创建
    @ApiOperation(value = "文件创建函数")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "oldName", value = "原文件名", dataType = "String", example = "test.txt"),
            @ApiImplicitParam(name = "newName", value = "新文件名", dataType = "String", example = "hello world"),
            @ApiImplicitParam(name = "num", value = "父目录所在级数", dataType = "int", example = "1")
    })
    @Override
    public String RenameDirectory(String oldName, String newName, int num) {
        try {
            dirService.renameFolder(oldName,newName,num);
        }
        catch (Exception e){
            return e.getMessage();
        }
        return "1";
    }

    @ResponseBody
    @GetMapping("/ReadFileContent")//文件创建
    @ApiOperation(value = "文件读，要把文件从磁盘移动到内存")
    @ApiImplicitParam(name = "fileName", value = "文件名", dataType = "String", example = "passwd.txt")
    public String ReadFileContent(String fileName) {
        int nextThreadId = threadService.getNextId();
        ReadFileContentThread r = new ReadFileContentThread(nextThreadId,dirService,iBlockService,diskManagement,threadService,fileName);
        Thread t = new Thread(r);
        t.run();
        return r.returnFile.toString();
    }
    @GetMapping("/DisplayFileSystemStructure")
    @ApiOperation(value = "查询显示目录结构")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "folderName", value = "查找的目录的名字", dataType = "String", example = "www"),
            @ApiImplicitParam(name = "num", value = "目录所在级数", dataType = "int", example = "1")})
    public Result DisplayFileSystemStructure(String foldername , int num) {
        Result result=new Result();
        List list=dirService.displayFileSystemStructure(foldername,num);
        if(list==null){
            result.setCode(401);
            result.setMsg("目录中没有文件");
        }else {
            result.setCode(201);
            result.setMsg("查询成功");
            result.setData(list);
        }
        return result;
    }
}

