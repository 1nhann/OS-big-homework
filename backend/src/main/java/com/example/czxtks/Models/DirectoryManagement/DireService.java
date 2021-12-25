package com.example.czxtks.Models.DirectoryManagement;

import com.example.czxtks.Models.DirectoryManagement.entity.FILE;
import com.example.czxtks.Models.DirectoryManagement.entity.FOLDER;
import com.example.czxtks.Models.DirectoryManagement.vo.File;
import com.example.czxtks.Models.DirectoryManagement.vo.FileAndFolder;
import com.example.czxtks.Models.DiskManagement.DiskManagementImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class DireService {
    private FOLDER root;//根目录
    //FOLDER root;//根目录
    int count=0;//控制输出格式
    int flagD=0;//删除标记
    int choice=0;

    @Autowired
    private DiskManagementImpl diskManagementImpl;

    public DireService() {
        this.root=initRootFolder();
    }

    FOLDER findCurrentFolder(FOLDER currentFolder,String name,int num) {
        //查找指定目录
        FOLDER folder;
        if(currentFolder==null)
            return null;		//没找到
        if(currentFolder.name.equals(name)&&currentFolder.num==num)
            //System.out.println("当前目录："+currentFolder.name+" 指定目录："+name);
            return currentFolder;		//查找目录为当前目录
        folder=findCurrentFolder(currentFolder.firstChildFolder,name,num);
        if(folder!=null)
            return folder;		//	查找目录在子目录中
        folder=findCurrentFolder(currentFolder.nextFolder,name,num);
        if(folder!=null)
            return folder;		//查找目录在同级其他目录中
        return null;		//没找到
    }

    public FILE findCurrentFile(FOLDER currentFolder, String name) {
        //查找指定文件
        FILE tempFile;
        if(currentFolder==null)
            return null;		//没找到
        tempFile=currentFolder.firstChildFile;
        while(tempFile!=null) {//	遍历当前目录子文件
            System.out.println(tempFile.name+" "+name);
            if(tempFile.name.equals(name))
                return tempFile;		//找到了
            tempFile=tempFile.nextFile;
        }

        tempFile=findCurrentFile(currentFolder.firstChildFolder,name);
        if(tempFile!=null)
            return tempFile;		//查找文件在子目录中
        tempFile=findCurrentFile(currentFolder.nextFolder,name);
        if(tempFile!=null)
            return tempFile;		//查找文件在同级其他目录中

        return null;		//没找到
    }

    public FOLDER prepareWorkBeforeCreate(String current_name,int num){
        //Scanner in=new Scanner(System.in);
        FOLDER currentFolder=null;
        //String name=new String();
        //System.out.println("输入当前目录名称：");
        //name=in.nextLine();

        currentFolder=findCurrentFolder(root,current_name,num);
        if(currentFolder==null) {
            System.out.println("目录不存在！");

            return null;
        }
        if(currentFolder.canWrite==0) {
            System.out.println("权限不够，无法创建！");

            return null;
        }

        return currentFolder;
    }

    public int  createFolder(String current_name,String name,int num) {
        //当前目录中创建新目录
        //Scanner in=new Scanner(System.in);
        FOLDER currentFolder=prepareWorkBeforeCreate(current_name, num);

        if(currentFolder==null) {		//目标目录不存在
            return 0;
        }
        //String name=new String();
        //System.out.println("输入新目录名称：");
        //name=in.nextLine();



        FOLDER newFolder;
        newFolder=new FOLDER();
        newFolder.name=name;
        newFolder.firstChildFolder=null;		//初始化新目录
        newFolder.firstChildFile=null;
        newFolder.nextFolder=null;
        newFolder.parentFolder=null;
        newFolder.frontFolder=null;
        newFolder.canRead=1;
        newFolder.canWrite=1;
        newFolder.num=currentFolder.num+1;//目录级数+1

        if(currentFolder.firstChildFolder==null) {		//当前目录下无子目录
            currentFolder.firstChildFolder=newFolder;//设置当前目录的子目录为新目录
            System.out.println(currentFolder.firstChildFolder);
            newFolder.parentFolder=currentFolder;//新目录的父目录为当前目录
        }
        else {		//当前目录下有子目录
            FOLDER tempFolder=currentFolder.firstChildFolder;//保存当前目录的子目录
            FOLDER lastFolder=new FOLDER();		//保存当前currentFolder下最后一个子folder
            while(tempFolder!=null) {		//同级目录下不得有相同的目录
                lastFolder=tempFolder;
                if(tempFolder.name==newFolder.name) {
                    System.out.println("目录下已有同名目录: "+currentFolder.name);
                    return 2;
                }
                tempFolder=tempFolder.nextFolder;//往下走，遍历当前目录下的所有子目录
            }
            lastFolder.nextFolder=newFolder;		//将新目录与同级子目录建立连接
            newFolder.frontFolder=lastFolder;
            //System.out.println(lastFolder);

        }
        return 1;
    }

    //一级索引创建文件
    public int  create_Single_File(String current_folder_name,String file_name,String user,String content,int num) {
        //当前目录中创建新文件
        //Scanner in=new Scanner(System.in);
        FOLDER currentFolder=prepareWorkBeforeCreate(current_folder_name,num);
        if(currentFolder==null) {//当前目标不存在
            return 2;
        }
        //String name=new String();
        //System.out.println("请输入新文件名称：");
        //name=in.nextLine();

        //String user =new String();
        //System.out.println("请输入文件所有者：");
        //user=in.nextLine();

        FILE newFile;
        newFile=new FILE();
        newFile.name=file_name;
        newFile.user=user;
        newFile.address=diskManagementImpl.FileMake();
        Date date = new Date();    // 调用无参数构造函数
        // SimpleDateFormat sdf = new SimpleDateFormat();
        // 给定模式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        String s = sdf.format(date);
        //System.out.println(date.toString());    // 输出：Wed May 18 21:24:40 CST 2016
        newFile.date=s;
        //System.out.println("是否输入文件内容？是，请输入y：");
        //String ans=in.nextLine();
        //System.out.println("ans:"+ans);
        newFile.content=content;
        newFile.nextFile=null;		//初始化新文件信息
        newFile.frontFile=null;
        newFile.parentFolder=null;
        newFile.canRead=1;
        newFile.canWrite=1;
        newFile.filesize=content.length();

        //调用磁盘函数
        diskManagementImpl.FileEdit_Singleindex(Integer.valueOf(newFile.address).intValue(), newFile.filesize, newFile.content);

        if(currentFolder.firstChildFile==null) {		//当前目录下无子文件
            currentFolder.firstChildFile=newFile;
            newFile.parentFolder=currentFolder;
        }
        else {		//当前目录下有子文件
            FILE tempFile=currentFolder.firstChildFile;
            FILE lastFile=new FILE();		//保存当前currentFolder下最后一个子文件

            while(tempFile!=null) {		//同级目录下不得有相同文件
                lastFile=tempFile;
                if(tempFile.name==newFile.name) {
                    //System.out.println("目录下已有同名文件："+currentFolder.name);
                    return 3;
                }
                tempFile=tempFile.nextFile;
            }
            lastFile.nextFile=newFile;		//将新文件与同级文件建立连接
            newFile.frontFile=lastFile;

        }
        System.out.println("创建成功！");
        return 1;
    }
    public int  createFile(String current_folder_name,String file_name,String user,String content,int num) {
        //当前目录中创建新文件
        //Scanner in=new Scanner(System.in);
        FOLDER currentFolder=prepareWorkBeforeCreate(current_folder_name,num);
        if(currentFolder==null) {//当前目标不存在
            return 2;
        }
        //String name=new String();
        //System.out.println("请输入新文件名称：");
        //name=in.nextLine();

        //String user =new String();
        //System.out.println("请输入文件所有者：");
        //user=in.nextLine();

        FILE newFile;
        newFile=new FILE();
        newFile.name=file_name;
        newFile.user=user;
        newFile.address=diskManagementImpl.FileMake();
        Date date = new Date();    // 调用无参数构造函数
        // SimpleDateFormat sdf = new SimpleDateFormat();
        // 给定模式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        String s = sdf.format(date);
        //System.out.println(date.toString());    // 输出：Wed May 18 21:24:40 CST 2016
        newFile.date=s;
        //System.out.println("是否输入文件内容？是，请输入y：");
        //String ans=in.nextLine();
        //System.out.println("ans:"+ans);
        newFile.content=content;
        newFile.nextFile=null;		//初始化新文件信息
        newFile.frontFile=null;
        newFile.parentFolder=null;
        newFile.canRead=1;
        newFile.canWrite=1;
        newFile.filesize=content.length();

        //调用磁盘函数
        diskManagementImpl.FileEdit(Integer.valueOf(newFile.address).intValue(), newFile.filesize, newFile.content);

        if(currentFolder.firstChildFile==null) {		//当前目录下无子文件
            currentFolder.firstChildFile=newFile;
            newFile.parentFolder=currentFolder;
        }
        else {		//当前目录下有子文件
            FILE tempFile=currentFolder.firstChildFile;
            FILE lastFile=new FILE();		//保存当前currentFolder下最后一个子文件

            while(tempFile!=null) {		//同级目录下不得有相同文件
                lastFile=tempFile;
                if(tempFile.name==newFile.name) {
                    //System.out.println("目录下已有同名文件："+currentFolder.name);
                    return 3;
                }
                tempFile=tempFile.nextFile;
            }
            lastFile.nextFile=newFile;		//将新文件与同级文件建立连接
            newFile.frontFile=lastFile;

        }
        System.out.println("创建成功！");
        return 1;
    }

    public void deleteAllChild(FOLDER currentFolder) {
        //删除该目录下所有内容
        FILE tempFile,dFile;
        if(currentFolder==null)
            return;
        if(flagD==0)
            deleteAllChild(currentFolder.nextFolder);
        flagD=1;
        deleteAllChild(currentFolder.firstChildFolder);//遍历子目录
        tempFile=currentFolder.firstChildFile;
        while(tempFile!=null) {//删除该目录子文件
            dFile=tempFile;
            tempFile=tempFile.nextFile;
        }
    }

    public int deleteFoder(String delete_name,int num) {
        //删除目录所有内容
        //Scanner in=new Scanner(System.in);
        //String name=new String();
        //name=in.nextLine();
        if(delete_name=="root") {
            System.out.println("根目录不能删除！");
            return 2;
        }
        FOLDER currentFolder=findCurrentFolder(root, delete_name,num);//先在根目录中找到当前目录
        if(currentFolder==null) {
            return 3;
        }
        if(currentFolder.canWrite==0) {
            return 4;
        }
        if (currentFolder.firstChildFolder!=null||currentFolder.firstChildFile!=null)
        {
            System.out.println("目录不是空目录，无法删除！");
            return 5;
        }

        if(currentFolder.frontFolder==null) {//如果当前目录是父级目录的第一个目录
            currentFolder.parentFolder.firstChildFolder=currentFolder.nextFolder;//断开连接
            if(currentFolder.nextFolder!=null)
                currentFolder.nextFolder.frontFolder=null;//设置当前目录的下一个目录为父级目录的第一个目录
        }
        else
            currentFolder.frontFolder.nextFolder=currentFolder.nextFolder;//连接当前目录的上一个目录和下一个目录
        deleteAllChild(currentFolder);//释放空间


        System.out.println("删除成功！");
        return 1;
    }

    //一级索引删除文件
    public int delete_Single_File(String delete_name) {
            //删除文件
            //String name=new String();
            //Scanner in=new Scanner(System.in);
            System.out.println("请输入要删除的文件名称：");
            //name=in.nextLine();
            FILE currentFile=findCurrentFile(root, delete_name);
            if(currentFile==null) {
                System.out.println("文件不存在！");
                return 2;
            }
            //删除文件
            while (currentFile.isReading){// 如果当前文件正在读，就阻塞

            }
            currentFile.isWrting = true;

            diskManagementImpl.DeleteFile(Integer.valueOf(currentFile.address).intValue());
            if(currentFile.frontFile==null) {
                currentFile.parentFolder.firstChildFile=currentFile.nextFile;//如果当前文件的前面没有文件，那么设置当前文件的父目录下的第一个文件为当前文件的下一个文件
                if(currentFile.nextFile!=null)
                    currentFile.nextFile.frontFile=null;//当前文件的下一个文件没有上一个文件，因为当前文件要被删除了
            }
            else
                currentFile.frontFile.nextFile=currentFile.nextFile;//将当前文件的上一个文件和下一个文件连接起来
            currentFile.isWrting = false;
            return 1;
    }


    public List<FileAndFolder> displayFileSystemStructure(String folderName, int num) {
        //输出目录结构
        List<FileAndFolder> list=new ArrayList<>();
        FOLDER folder= findCurrentFolder(root,folderName,num);//找到要遍历的目录

        FOLDER next=folder.firstChildFolder;//第一个子目录
        while (next!=null){
            FileAndFolder fileAndFolder=new FileAndFolder();
            fileAndFolder.folderName=next.name;
            list.add(fileAndFolder);
            next=next.nextFolder;
        }

        FILE file=folder.firstChildFile;//第一个文件
        while (file!=null){
            FileAndFolder fileAndFolder=new FileAndFolder();
            fileAndFolder.filename=file.name;
            fileAndFolder.canRead=file.canRead;
            fileAndFolder.canWrite=file.canWrite;
            fileAndFolder.user=file.user;
            fileAndFolder.date=file.date;
            fileAndFolder.address=file.address;
            list.add(fileAndFolder);
            file=file.nextFile;
        }
        return list;
    }

    public FILE showFileContent(String file_name) throws UnsupportedEncodingException {
        //显示文件内容
        //Scanner in=new Scanner(System.in);
        FILE currentFile=null;

//        File file=new File();
        FILE file = new FILE();

        currentFile=findCurrentFile(root, file_name);
        if(currentFile==null) {
            System.out.println("文件不存在！");
            return null;
        }
        if(currentFile.canRead==0) {
            System.out.println("权限不够，无法读取！");
            return null;
        }

        while (currentFile.isWrting) {// 如果文件正在写，那就不能读，阻塞
        }
        currentFile.isReading = true;
        file.setContent(currentFile.content);
        file.setDate(currentFile.date);
        file.setUser(currentFile.user);
        file.setAddress(currentFile.address);
        file.setFilesize(currentFile.filesize);

        currentFile.isReading = false;
        return file;

    }

    public int changeFileContent(String content,String change_file_name) {
//        System.out.println(content);
        //更改文件内容
        //Scanner inScanner=new Scanner(System.in);
        FILE currentFile=null;
        //String name=new String();
        System.out.println("请输入文件名：");
        //name=inScanner.nextLine();
        //inScanner.nextLine();
        currentFile=findCurrentFile(root, change_file_name);
        if(currentFile==null) {
            System.out.println("文件不存在！");
            return 2;
        }
        if(currentFile.canWrite==0) {
            System.out.println("权限不够，不能修改！");
            return 3;
        }

        while (currentFile.isReading) {//如果正在读，就阻塞
        }
        currentFile.isWrting = true;
        System.out.println("请输入文件内容：");
        currentFile.content=content;

        currentFile.filesize=content.length();

        diskManagementImpl.FileEdit(Integer.valueOf(currentFile.address).intValue(), currentFile.filesize, currentFile.content);

        //inScanner.nextLine();
        System.out.println("更改成功！");
        currentFile.isWrting = false;
        return 1;
    }

    public void changeAllChildPermission(FOLDER currentFolder,int canRead,int canWrite) {
        //更改子目录下所有目录和文件权限
        FILE tempFile;
        if(currentFolder!=null) {
            currentFolder.canRead=canRead;
            currentFolder.canWrite=canWrite;
        }
        else if(currentFolder==null)
            return;
        tempFile=currentFolder.firstChildFile;
        while(tempFile!=null) {//遍历子文件
            tempFile.canRead=canRead;
            tempFile.canWrite=canWrite;
            tempFile=tempFile.nextFile;
        }
        changeAllChildPermission(currentFolder.firstChildFolder, canRead, canWrite);
        if(currentFolder.firstChildFolder!=null)
            changeAllChildPermission(currentFolder.firstChildFolder.nextFolder, canRead, canWrite);
    }

    public int changeFolderPermission(String permission_name,String permission,int num) {
        //更改目录权限
        //Scanner in=new Scanner(System.in);
        //String name=new String();

        System.out.println("请输入要更改权限的目录名称：");
        //name=in.nextLine();
        if(permission_name=="root") {
            System.out.println("根目录不准修改");

            return 2;
        }
        FOLDER currentFolder=null;
        currentFolder=findCurrentFolder(root, permission_name,num);
        if(currentFolder==null) {
            System.out.println("目录不存在");
            return 3;
        }
        System.out.println("输入目录权限（读和写）：");
        //String tempInput=in.nextLine();

        //currentFolder.canRead=tempInput.charAt(0);
        currentFolder.canRead= Integer.valueOf(permission.charAt(0)).intValue() - 48;
        //currentFolder.canWrite=tempInput.charAt(1);
        currentFolder.canWrite= Integer.valueOf(permission.charAt(1)).intValue() - 48;
        changeAllChildPermission(currentFolder, currentFolder.canRead, currentFolder.canWrite);//更改当前目录权限时，同时更改目录下所有文件和目录权限
        return 1;
    }

    public int changeFilePermission(String file_name,String permission) {
        //更改文件权限
        //Scanner in=new Scanner(System.in);
        //String name=new String();
        System.out.println("请输入要更改权限的的文件名称：");
        //name=in.nextLine();
        FILE currentFile=null;
        currentFile=findCurrentFile(root, file_name);
        if(currentFile==null) {
            System.out.println("文件不存在！");
            return 2;
        }
        System.out.println("输入文件权限（读和写）：");
        //String tempInput=in.nextLine();

        //currentFile.canRead=tempInput.charAt(0);
        currentFile.canRead=Integer.valueOf(permission.charAt(0)).intValue() - 48;
        //currentFile.canWrite=tempInput.charAt(1);
        currentFile.canWrite=Integer.valueOf(permission.charAt(1)).intValue() - 48;
        System.out.println("权限更改成功！");

        return 1;
    }

    public int fileNameIsDuplication(FILE currentFile,String name) {
        //判断新重命名的文件是否重名
        FILE tempFile=currentFile.frontFile;
        while(tempFile!=null) {
            if(tempFile.name.equals(name)) {
                System.out.println("文件重名！");
                return 1;
            }
            tempFile=tempFile.nextFile;
        }
        tempFile=currentFile.nextFile;
        while(tempFile!=null) {
            if(tempFile.name.equals(name)) {
                System.out.println("文件重名！");
                return 1;
            }
            tempFile=tempFile.nextFile;
        }
        return 0;
    }

    public int folderNameIsDuplication(FOLDER currentFolder,String name) {
        //判断新重命名的目录是否重名
        FOLDER tempFolder=currentFolder.frontFolder;
        while(tempFolder!=null) {
            if(tempFolder.name.equals(name)) {
                System.out.println("目录重名！");
                return 1;
            }
            tempFolder=tempFolder.frontFolder;
        }
        tempFolder=currentFolder.nextFolder;
        while(tempFolder!=null) {
            if(tempFolder.name.equals(name)) {
                System.out.println("目录重名！");
                return 1;
            }
            tempFolder=tempFolder.nextFolder;
        }
        return 0;
    }

    public int renameFolder(String old_name,String new_name,int num) {
        //重命名目录
        //Scanner in=new Scanner(System.in);
        //String name=new String();
        System.out.println("请输入要重命名的目录名称：");
        //name=in.nextLine();
        if(old_name=="root") {
            System.out.println("根目录不准修改");

            return 2;
        }
        FOLDER currentFolder=findCurrentFolder(root, old_name,num);
        if(currentFolder==null) {
            System.out.println("目录不存在！");

            return 3;
        }
        //System.out.println("输入新目录名称：");
        //name=in.nextLine();


        if(folderNameIsDuplication(currentFolder, new_name)==1)//重名
            return 4;
        currentFolder.name=new_name;
        return 1;
    }

    public int renameFile(String old_file_name,String new_file_name) {
        //重命名文件
        //Scanner in=new Scanner(System.in);
        //String name=new String();

        //name=in.nextLine();
        FILE currentFile=findCurrentFile(root, old_file_name);
        if(currentFile==null) {

            return 2;
        }
        System.out.println("输入新名称：");
        //name=in.nextLine();

        if(fileNameIsDuplication(currentFile, new_file_name)==1)//重名是1，为0则不重名
            return 3;
        currentFile.name=new_file_name;
        System.out.println("重命名成功！");
        return 1;

    }

    public FOLDER initRootFolder() {
        //初始化根目录信息
        FOLDER root=new FOLDER();
        root.frontFolder=null;
        root.nextFolder=null;
        root.parentFolder=null;
        root.firstChildFile=null;
        root.firstChildFolder=null;
        root.canRead=1;
        root.canWrite=1;
        root.name="root";

        return root;
    }

}
