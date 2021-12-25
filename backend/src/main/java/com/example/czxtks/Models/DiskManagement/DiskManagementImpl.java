package com.example.czxtks.Models.DiskManagement;

import com.example.czxtks.Models.DiskManagement.Conmmon.*;
import com.example.czxtks.Models.MemoryManagement.dao.IBlockDao;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

/*
需要前端判定，如果大于256K，直接弹出超过文件最大大小

 */

@Service
public class DiskManagementImpl implements DiskManagement{

    public static ArrayList<PlateBlock> OrdBlockArrayList = new ArrayList<>();//简单普通文件盘块
    public static ArrayList<PlateBlock> ExBlockArrayList = new ArrayList<>();

    public DiskManagementImpl(){
        DiskSpaceInit();
    }
    public String DiskSpaceInit() {//磁盘空间初始化

        OrdBlockArrayList = new ArrayList<>();//新申请内存
        for (int i = 0; i < 900; i++) {
            PlateBlock plateBlock = new PlateBlock(i);
            OrdBlockArrayList.add(plateBlock);//将新建的对象放入数组中
        }
        OrdBlockArrayList.get(0).diskBlockStatus = 4;
        ExBlockArrayList = new ArrayList<>();//新申请内存
        for (int i = 900; i < 1024; i++) {
            PlateBlock plateBlock = new PlateBlock(i);
            ExBlockArrayList.add(plateBlock);//将新建的对象放入数组中
        }
        String DiskState = DiskState();

        DynamicPartition.allocLinkedList();//对最佳适应算法进行初始化
        DynamicPartition.linkedList_dy = new LinkedList<>();
        DynamicFragmentation dy = new DynamicFragmentation(0, 124);
        DynamicPartition.linkedList_dy.add(dy);
        DynamicPartition.start = new ArrayList<>();
        DynamicPartition.end = new ArrayList<>();
        DynamicPartition.ID = new ArrayList<>();

        IBlockDao.initialMemory();

        return DiskState;//向前端返回当前已经初始化后状态
    }

    public String DiskState() {//打印当前的磁盘使用状态
        DiskState diskState = new DiskState();

        Iterator<PlateBlock> orditerator = OrdBlockArrayList.iterator();
        for (; orditerator.hasNext(); ) {
            diskState.ordblock.add(orditerator.next().diskBlockStatus);
        }
        Iterator<PlateBlock> exiterator = ExBlockArrayList.iterator();
        for (; exiterator.hasNext(); ) {
            diskState.exblock.add(exiterator.next().diskBlockStatus);
        }
        ObjectMapper objectMapper = new ObjectMapper();

        String DiskState = null;
        try {

            DiskState = objectMapper.writeValueAsString(diskState);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return DiskState;//向前端返回当前的磁盘状态，可用作位示图

    }

    public boolean DeleteFile(int IndexNode) {
        OrdBlockArrayList.get(IndexNode).diskBlockStatus = 0;
        for (int i : OrdBlockArrayList.get(IndexNode).IndexNode) {
            OrdBlockArrayList.get(i).diskBlockStatus = 0;
            for (int j : OrdBlockArrayList.get(i).IndexNode) {
                OrdBlockArrayList.get(j).diskBlockStatus = 0;
                OrdBlockArrayList.get(j).content1="";
            }
            OrdBlockArrayList.get(i).IndexNode = new ArrayList<>();
        }
        OrdBlockArrayList.get(IndexNode).IndexNode = new ArrayList<>();
        return true;
    }

    public String FileMake() {
        //传入数据 FileSize 文件大小
        //假设规定 索引块 可以存放8条地址,即文件的最大Size为8*8*4KB = 256 KB
        int Indexblock = FindFree();//找到空闲磁盘块,Indexblock为主索引块
        OrdBlockArrayList.get(Indexblock).diskBlockStatus = 2;//进行标记

        ObjectMapper objectMapper =new ObjectMapper();//使用objectmapper转化为json
        String string = null;
        try {
            string = objectMapper.writeValueAsString(Indexblock);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return string;
    }

    public int FindFree() {
        int id = 0;
        for (PlateBlock plateBlock : OrdBlockArrayList) {
            int status = plateBlock.diskBlockStatus;
            id = plateBlock.id;
            if (status == 0) {

                break;
            }
        }
        return id;
    }

    public String ExchangeAreaADD(int Length, int ID) {
        DynamicPartition.start = new ArrayList<>();
        DynamicPartition.end = new ArrayList<>();
        DynamicPartition.ID = new ArrayList<>();
        DynamicFragmentation.BestAdaptation(Length, ID);
        //将最佳适应算法的全部结果在真正的磁盘文件中进行更改和实现
        for (int i = 0; i < DynamicPartition.start.size(); i++) {
            for (int j = DynamicPartition.start.get(i); j < DynamicPartition.end.get(i); j++) {
                if (DynamicPartition.ID.get(i) == 0)
                    ExBlockArrayList.get(j - 900).diskBlockStatus = 0;
                else
                    ExBlockArrayList.get(j - 900).diskBlockStatus = ID;
            }
        }

        ExchangeAreaStructure exchangeAreaStructure = new ExchangeAreaStructure();
        exchangeAreaStructure.start = DynamicPartition.start;
        exchangeAreaStructure.end = DynamicPartition.end;
        exchangeAreaStructure.ID = DynamicPartition.ID;
        ObjectMapper objectMapper = new ObjectMapper();
        String s = null;
        try {
            s = objectMapper.writeValueAsString(exchangeAreaStructure);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return s;
    }

    public String ExchangeAreaDelete(int Length, int ID) {
        DynamicPartition.start = new ArrayList<>();
        DynamicPartition.end = new ArrayList<>();
        DynamicPartition.ID = new ArrayList<>();
        DynamicFragmentation.BestAdaptationFree(Length, ID);
        //将最佳适应算法的全部结果在真正的磁盘文件中进行更改和实现
        for (int i = 0; i < DynamicPartition.start.size(); i++) {
            for (int j = DynamicPartition.start.get(i); j < DynamicPartition.end.get(i); j++) {
                if (DynamicPartition.ID.get(i) == 0)
                    ExBlockArrayList.get(j - 900).diskBlockStatus = 0;
                else
                    ExBlockArrayList.get(j - 900).diskBlockStatus = ID;
            }
        }
        ExchangeAreaStructure exchangeAreaStructure = new ExchangeAreaStructure();
        exchangeAreaStructure.start = DynamicPartition.start;
        exchangeAreaStructure.end = DynamicPartition.end;
        exchangeAreaStructure.ID = DynamicPartition.ID;
        ObjectMapper objectMapper = new ObjectMapper();
        String s = null;
        try {
            s = objectMapper.writeValueAsString(exchangeAreaStructure);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return s;
    }

    @Override
    public boolean DeleteFile_SingleIndex(int IndexNode) {
        OrdBlockArrayList.get(IndexNode).diskBlockStatus = 0;
        for(int i : OrdBlockArrayList.get(IndexNode).IndexNode){
            OrdBlockArrayList.get(i).diskBlockStatus = 0;
            OrdBlockArrayList.get(i).IndexNode = new ArrayList<>();
        }
        OrdBlockArrayList.get(IndexNode).IndexNode = new ArrayList<>();
        return  true;
    }

    @Override
    public String FileSizeView(int IndexNode) {
        int size = OrdBlockArrayList.get(IndexNode).FileSize;
        String s = null;
        try {
            s = new ObjectMapper().writeValueAsString(size);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return  s;
    }

    @Override
    public String FileView(int IndexNode) {
        String s = null;
        try {
            s = new ObjectMapper().writeValueAsString(OrdBlockArrayList.get(IndexNode).content);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return s;
    }

    @Override
    public  String FileEdit_Singleindex(int Indexblock,int FileSize,String content) { //传入数据 FileSize 文件大小
            for(int i : OrdBlockArrayList.get(Indexblock).IndexNode){
                OrdBlockArrayList.get(i).diskBlockStatus = 0;
                OrdBlockArrayList.get(i).IndexNode = new ArrayList<>();
            }
            OrdBlockArrayList.get(Indexblock).IndexNode = new ArrayList<>();


            OrdBlockArrayList.get(Indexblock).content = content;
            OrdBlockArrayList.get(Indexblock).FileSize = FileSize;
            int DisksNum ;//盘块所需要的数目
            if(FileSize%4>0)//进行文件所需要的块数的计算
            {
                DisksNum = FileSize/4+1;
            }
            else
                DisksNum = FileSize/4;
            int  PrimaryIndex = 0;
            int  SecondaryIndex;
//        SecondaryIndex = DisksNum%8;
//        if(SecondaryIndex > 0 )
//        {
//            PrimaryIndex = DisksNum/8+1;
//        }
//        else
//            PrimaryIndex = DisksNum/8;//判断一级索引表大小
//        int Indexblock = FindFree();//找到空闲磁盘块,Indexblock为主索引块
            OrdBlockArrayList.get(Indexblock).diskBlockStatus = 2;//进行标记
            for(int i=0;i<DisksNum;i++)
            {
                int index = FindFree();
                OrdBlockArrayList.get(index).diskBlockStatus = 1;
                OrdBlockArrayList.get(Indexblock).IndexNode.add(index);//添加一级索引块内的直接地址号

            }
            //IndexNode为文件属性的索引块变量
            ArrayList<Integer> IndexNodeArray = OrdBlockArrayList.get(Indexblock).IndexNode;//一级索引结点数组

            FileIndexResult fileIndexResult = new FileIndexResult();
            fileIndexResult.IndexNode = Indexblock;
            fileIndexResult.IndexNodeArray = IndexNodeArray;

            fileIndexResult.content = content;
            ObjectMapper objectMapper =new ObjectMapper();//使用objectmapper转化为json
            String string = null;
            try {
                string = objectMapper.writeValueAsString(fileIndexResult);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return string;
    }

    @Override
//    @PostMapping("/FileEdit")
//    @ResponseBody
        public  String FileEdit(int Indexblock,int FileSize,String content) { //传入数据 FileSize 文件大小
        FileSize = content.length();
        int FileNode = 0;
        for(int i : OrdBlockArrayList.get(Indexblock).IndexNode){
            OrdBlockArrayList.get(i).diskBlockStatus = 0;
            for (int j : OrdBlockArrayList.get(i).IndexNode) {
                OrdBlockArrayList.get(j).diskBlockStatus = 0;
                OrdBlockArrayList.get(j).content1 = "";
            }
            OrdBlockArrayList.get(i).IndexNode = new ArrayList<>();
        }
        OrdBlockArrayList.get(Indexblock).IndexNode = new ArrayList<>();


        OrdBlockArrayList.get(Indexblock).content = content;
        OrdBlockArrayList.get(Indexblock).FileSize = FileSize;
        int DisksNum ;//盘块所需要的数目
        if(FileSize%4>0)//进行文件所需要的块数的计算
        {
            DisksNum = FileSize/4+1;
        }
        else
            DisksNum = FileSize/4;
        //假设规定 索引块 可以存放8条地址,即文件的最大Size为8*8*4KB = 256 KB
        int  PrimaryIndex = 0;
        int  SecondaryIndex;
        SecondaryIndex = DisksNum%8;
        if(SecondaryIndex > 0 )
        {
            PrimaryIndex = DisksNum/8+1;
        }
        else
            PrimaryIndex = DisksNum/8;//判断一级索引表大小
//        int Indexblock = FindFree();//找到空闲磁盘块,Indexblock为主索引块
        OrdBlockArrayList.get(Indexblock).diskBlockStatus = 2;//进行标记
        for(int i=0;i<PrimaryIndex;i++)
        {
            if(i<PrimaryIndex-1)
            {
                int free = FindFree();
                OrdBlockArrayList.get(free).diskBlockStatus = 3;
                OrdBlockArrayList.get(Indexblock).IndexNode.add(free);
                for (int j = 0; j < 8; j++) {
                    int free1 = FindFree();
                    OrdBlockArrayList.get(free1).diskBlockStatus = 1;//找到二级索引下的地址文件，写为普通文件
                    OrdBlockArrayList.get(free).IndexNode.add(free1);//将地址加入二级索引表
                    for (int k = 0; k < 4; k++) {
                        OrdBlockArrayList.get(free1).content1+=content.charAt(FileNode); //将字符写入盘块，每个盘块最多写4个字节
                        FileNode++;
                    }


                }


            }
            else
            {
                int free = FindFree(),flag;
                if(SecondaryIndex==0)
                    flag = 8;
                else
                    flag = SecondaryIndex;
                OrdBlockArrayList.get(Indexblock).IndexNode.add(free);
                OrdBlockArrayList.get(free).diskBlockStatus = 3;
                for (int j = 0; j < flag; j++) {
                    int free1 = FindFree();
                    OrdBlockArrayList.get(free1).diskBlockStatus = 1;//找到二级索引下的地址文件，写为普通文件
                    OrdBlockArrayList.get(free).IndexNode.add(free1);   //将地址加入二级索引表
                    if(FileSize-FileNode>=4)
                        for (int k = 0; k < 4; k++) {
                            OrdBlockArrayList.get(free1).content1+=content.charAt(FileNode); //将字符写入盘块，每个盘块最多写4个字节
                            FileNode++;
                        }
                    else
                        for (; FileNode < FileSize; FileNode++) {
                            OrdBlockArrayList.get(free1).content1+=content.charAt(FileNode); //将字符写入盘块，每个盘块最多写4个字节

                        }
                }


            }

        }
        //IndexNode为文件属性的索引块变量
        ArrayList<Integer> IndexNodeArray = OrdBlockArrayList.get(Indexblock).IndexNode;//一级索引结点数组
        ArrayList<ArrayList> SecondNodeArray = new ArrayList();//二级索引节点数组
        for(int node:OrdBlockArrayList.get(Indexblock).IndexNode)
        {
            SecondNodeArray.add(OrdBlockArrayList.get(node).IndexNode);
        }
        FileIndexResult fileIndexResult = new FileIndexResult();
        fileIndexResult.IndexNode = Indexblock;
        fileIndexResult.IndexNodeArray = IndexNodeArray;
        fileIndexResult.SecondNodeArray = SecondNodeArray;
        fileIndexResult.content = content;
        ObjectMapper objectMapper =new ObjectMapper();//使用objectmapper转化为json
        String string = null;
        try {
            string = objectMapper.writeValueAsString(fileIndexResult);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return string;
    }

//    public static void main(String[] args) {//测试接口正确性的main函数
//        DiskSpaceInit();
//       String s =  FileMake(100);
//       System.out.println(s);
//       s = DiskState();
//       System.out.println(s);
//        s =  FileMake(200);
//        System.out.println(s);
//       DeleteFile(0);
//       s = DiskState();
//       System.out.println(s);
//       s = ExchangeAreaADD(100,101);
//       System.out.println(s);
//       s = DiskState();
//       System.out.println(s);
//    }

//    @ResponseBody
//    @PostMapping("/getNewPage")
//    @CrossOrigin(origins = "*",maxAge = 3600)
    public  DiskToMem getNewPage(int blocknum){
        DiskToMem diskToMem = new DiskToMem();
        diskToMem.content = OrdBlockArrayList.get(blocknum).content1;
        diskToMem.indexnode = OrdBlockArrayList.get(blocknum).IndexNode;
        return diskToMem;
    }
}


//以下为兑换区的部分，使用的是最佳适应算法

class DynamicFragmentation extends DynamicPartition {
    public int LogoNumberv = 0;
    public int FragmentLength = 0;
    DynamicFragmentation(int LogoNumberv,int FragmentLength){
        this.LogoNumberv = LogoNumberv;
        this.FragmentLength = FragmentLength;
    }
}

class DynamicPartition {
    public static  ArrayList<Integer> start = new ArrayList<>();
    public static ArrayList<Integer> end = new ArrayList<>();
    public static ArrayList<Integer> ID = new ArrayList<>();
    public static LinkedList<DynamicPartition> linkedList_dy = new LinkedList<>();
    public static LinkedList<Integer> linkedList = new LinkedList<>();
    public static void allocLinkedList() {
        linkedList = new LinkedList<>();
        for (int i = 900; i < 1024; i++) {
            linkedList.add(0);
        }
    }
    public static void BestAdaptation(int Length, int SerialNumber){

        int small = 999;
        int index  = 0;
        for (int i = 0; i <linkedList_dy.size() ; i++) {
            if(((DynamicFragmentation)linkedList_dy.get(i)).FragmentLength>=Length&&(((DynamicFragmentation)linkedList_dy.get(i)).LogoNumberv==0)){
                if(((DynamicFragmentation)linkedList_dy.get(i)).FragmentLength<small) {
                    small = ((DynamicFragmentation) linkedList_dy.get(i)).FragmentLength;
                    index = i;
                }

            }
        }
        DynamicFragmentation dy_temp = new DynamicFragmentation(SerialNumber,Length);
        linkedList_dy.set(index,dy_temp);
        DynamicFragmentation dy_temp1 = new DynamicFragmentation(0,small-Length);
        linkedList_dy.add(index+1,dy_temp1);
        int sum = 900;
        for (int i = 0; i < linkedList_dy.size(); i++) {


            start.add(sum);
            end.add((sum+((DynamicFragmentation)linkedList_dy.get(i)).FragmentLength)-1);
            ID.add(((DynamicFragmentation)linkedList_dy.get(i)).LogoNumberv);
            sum+=((DynamicFragmentation)linkedList_dy.get(i)).FragmentLength;
        }


    }
    public static void BestAdaptationFree( int Length,int SerialNumber){

        int index  = 0;
        for (int i = 0; i <linkedList_dy.size() ; i++) {
            if(((DynamicFragmentation)linkedList_dy.get(i)).LogoNumberv==SerialNumber)
                index = i;

        }
        DynamicFragmentation dy2 = new DynamicFragmentation(0,Length);
        linkedList_dy.set(index,dy2);
        int previous = 1;
        for (int i = 0; i <linkedList_dy.size() ; i++) {

            if(previous==0&&((DynamicFragmentation)linkedList_dy.get(i)).LogoNumberv==0)
            {
                int num = ((DynamicFragmentation)linkedList_dy.get(i-1)).FragmentLength+((DynamicFragmentation)linkedList_dy.get(i)).FragmentLength;
                linkedList_dy.remove(i-1);
                DynamicFragmentation dy_temp1 = new DynamicFragmentation(0,num);
                linkedList_dy.set(i-1,dy_temp1);
//                previous = ((com.example.czxtks.Models.DiskManagement.DynamicFragmentation)linkedList_dy.get(i)).LogoNumberv;
                if(i>=linkedList_dy.size() )
                    previous = ((DynamicFragmentation)linkedList_dy.get(i-1)).LogoNumberv;
                else
                    previous = ((DynamicFragmentation)linkedList_dy.get(i)).LogoNumberv;
            }
            else
                previous = ((DynamicFragmentation)linkedList_dy.get(i)).LogoNumberv;
            if(i+1>=linkedList_dy.size())
                break;
        }
        int sum = 900;
        for (int i = 0; i < linkedList_dy.size(); i++) {
            System.out.println ("\t\t"+sum+"\t\t\t\t"+(sum+((DynamicFragmentation)linkedList_dy.get(i)).FragmentLength)+"\t\t\t\t\t"+((DynamicFragmentation)linkedList_dy.get(i)).LogoNumberv);
            start.add(sum);
            end.add((sum+((DynamicFragmentation)linkedList_dy.get(i)).FragmentLength)-1);
            ID.add(((DynamicFragmentation)linkedList_dy.get(i)).LogoNumberv);
            sum+=((DynamicFragmentation)linkedList_dy.get(i)).FragmentLength;
        }

    }

}

class ExchangeAreaStructure{//兑换区结构体
    public ArrayList<Integer> start = new ArrayList<>();
    public ArrayList<Integer> end = new ArrayList<>();
    public ArrayList<Integer> ID = new ArrayList<>();

}

class FileSizeViewStruct{
    public int FileSize;
    public ArrayList<Integer> IndexNodeArray = new ArrayList<>();
    public ArrayList<ArrayList> SecondNodeArray = new ArrayList<>();

}