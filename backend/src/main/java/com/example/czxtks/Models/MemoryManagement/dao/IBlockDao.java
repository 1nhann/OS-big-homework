package com.example.czxtks.Models.MemoryManagement.dao;
import com.example.czxtks.Models.DiskManagement.DiskManagementImpl;
import com.example.czxtks.Models.MemoryManagement.model.Block;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.example.czxtks.Models.DiskManagement.DiskManagement;

import java.net.ServerSocket;
import java.util.concurrent.ThreadLocalRandom;

@Repository
public class IBlockDao {
    public static int SIZE = 64;          //内存块数
    static int CmdCode = -1;             //命令代码(由于模拟原因,将命令代码直接默认为页面)
    static int[] temp = new int[10];    //初始生成伪随机数
    static Block[] blocks = new Block[SIZE];               //内存块
    static int[] blockTemp = new int[SIZE];
    static int[] IDTemp = {1,2,3,4,5,6,7,8};

    public static int[][] pageControl = new int[8][10];//其中二维数组的第一个变量 用来存放threadID便于管理页表

    @Autowired
    private DiskManagement diskManagement;

    public static void initialMemory(){       //初始化内存块
        blocks = new Block[SIZE];
        CmdCode = 0;
        for (int i = 0; i < SIZE; i++) {
            Block block = new Block(-1,0,-1,"");
            blocks[i] = block;
        }
        for (int i = 0; i < 8 ; i++){
            for (int j = 0 ; j < 9 ; j++){
                pageControl[i][j] = -1;//-1表示未分配
            }
            pageControl[i][9] = 0;
            pageControl[i][0] = -1;
        }
    }

    static int k=1;
    public static int getID(){
        int i=k;
        while (true){
            if (k==9){
                k=1;
            }
            k++;
            return IDTemp[i];
        }
    }

    public static void getRandom() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        for (int i= 0; i<10;i++){
            temp[i] = random.nextInt(10);
        }
    }

    public static int isEmpty(){       //判断是否有空内存块
        for (int i = 0; i<SIZE;i++){
            if (blocks[i].pageNum == -1){
                return i;
            }
        }
        return -1;
    }

    public static int isExist(int page){//判断该页面是否在内存块内
        for (int i = 0;i<SIZE;i++){
            if (blocks[i].pageNum == page){
                return i;
            }
        }
        return -1;
    }

    public static int pageSelect(){     //选择置换页面
        int selection = 0;
        int MAX_=0;
        for (int i = 0; i < SIZE; i++) {
            if (MAX_<blocks[i].timeForDie){
                MAX_ = blocks[i].timeForDie;
                selection = i;
            }
        }
        return selection;
    }
//
//    public static boolean isOK(){      //判断内存空间是否存在至少8个内存块
//        int emptyIsOk=0;
//        for (int i = 0 ; i< SIZE; i++){
//            if (isEmpty()!=-1){
//                emptyIsOk++;
//            }
//            if (emptyIsOk>=8){
//                return true;
//            }
//        }
//        return false;
//    }
    public static boolean isOK(){
        int count = 0;
        for (int i = 0 ; i < SIZE; i++) {
            if (blocks[i].asyncId == -1) count++;
            if(count >= 8) return  true;
        }
        return false;
    }
    public boolean Malloc(int threadID){
        if(isOK()){
            int location = 0;
            for (int i = 0; i < 8; i++){
                if (pageControl[i][0] == -1){
                    location = i;       //代表pageControl这个二维表的第location行记录的是threadID的信息
                    pageControl[i][0] = threadID;
                    break;
                }
            }


            int j = 1;
            for (int i = 0; i < SIZE; i++){
                if(blocks[i].asyncId == -1){
                    blocks[i].asyncId = threadID;
                    pageControl[location][j] = i;
                    j++;
                }
                if(j == 9) break;
            }
            pageControl[location][9] = 0; //已占内存块个数
            return true;
        }
        return false;
    }

    public static void LRU(){
        for (int i = 0; i<10; i++){
            CmdCode = temp[i];
            if (isExist(CmdCode) == -1){
                for (int j =0 ;j <SIZE;j++){
                    if (blocks[j].pageNum != -1){
                        blocks[j].timeForDie++;
                    }
                }
                if (isEmpty() != -1){
                    blocks[isEmpty()].pageNum = CmdCode;
                }else {
                    blocks[pageSelect()] = new Block(CmdCode,0,getID(),"");
                    DiskManagement.ExchangeAreaDelete(100+ThreadLocalRandom.current().nextInt(100), 1);
                }
            }else {
                for (int j2 = 0;j2<SIZE;j2++){
                    if (blocks[j2].pageNum!=-1&&blocks[j2].pageNum!=CmdCode){
                        blocks[j2].timeForDie++;
                    }
                }
            }
        }
    }
    public static int LRU11(int threadID, int blockNum , int location){  //返回max_num
        int max_num = 1;
        for(int i = 2; i < 9; i++){
            if (blocks[pageControl[location][max_num]].timeForDie < blocks[pageControl[location][i]].timeForDie) max_num = i;
        }   //得出timeForDie最大的页
        //调出磁盘中相应的块,修改对应的信息
        //return max_num的块号
        //writeToSwap(blocks[max_num].BlockInDiskNum,max_num);
        //pageIn(blockNum,max_num);
        return pageControl[location][max_num];
    }
    public  int LRU1(int threadID, int blockNum , int location){  //返回max_num
        int max_num = 1;
        for(int i = 2; i < 9; i++){
            if (blocks[pageControl[location][max_num]].timeForDie < blocks[pageControl[location][i]].timeForDie) max_num = i;
        }   //得出timeForDie最大的页
        //调出磁盘中相应的块,修改对应的信息
        //return max_num的块号
        //writeToSwap(blocks[max_num].BlockInDiskNum,max_num);
        //pageIn(blockNum,max_num);
        System.out.println(pageControl[location][max_num]);
        diskManagement.ExchangeAreaADD(4,threadID);
        return pageControl[location][max_num];
    }


    public String access  (int threadID, int blockNum){  //线程访问内存,应该要返回其中相应的content
        int location = 0;
        for (int i = 0; i < 8; i++ ){
            if (pageControl[i][0] == threadID) {
                location = i;
                break;
            }
        }
        int j = 0;
        int judge = 0;
        int result = 0;
        for (int i = 1; i <= pageControl[location][9]; i++){
            //if(pageControl[location][i] != -1) j = pageControl[location][i];

            j = pageControl[location][i];
            if(blocks[j].pageNum == blockNum){
                blocks[j].timeForDie = 0;   //清0
                judge = 1;
                result = j;
                //System.out.println(result);
            }
            else{
                blocks[j].timeForDie++;
            }
        }
        if(judge == 0){
            //result = LRU1(threadID, blockNum, location);
            if(pageControl[location][9] == 8){ //置换+调页
                result = LRU1(threadID, blockNum, location);
            }
            else { //调页
                pageControl[location][9]++;
                result = pageControl[location][9]+8*location-1;
            }
            blocks[result].content = diskManagement.getNewPage(blockNum).content; //调入页
            blocks[result].asyncId = 1;
            blocks[result].timeForDie = 0;
            blocks[result].pageNum = blockNum;
        }

        String s = diskManagement.getNewPage(blockNum).content;
        System.out.println(blocks[result].content);
        System.out.println(result);
        return blocks[result].content;



    }

    public static void freeBlockByAsyncId(int asyncId){   //内存空间释放（根据线程标记）
        for (int i = 0;i<SIZE ; i++){
            if (blocks[i].asyncId == asyncId){
                
                blocks[i] = new Block(-1,0,-1,"");
            }
        }
    }
    public void free(int asyncId){   //内存空间释放（根据线程标记）
//        for (int i = 0;i<SIZE ; i++){
//            if (blocks[i].asyncId == asyncId){
//                //blocks[i] = new Block(-1,0,-1);
//                blocks[i].asyncId = -1;
//                blocks[i].timeForDie = 0;
//                //写到兑换区
//            }
//        }
        int location = 0;                                   //问题：如何返回string类型
        for (int i = 0; i < 8; i++ ){
            if (pageControl[i][0] == asyncId) {
                location = i;
                break;
            }
        }
        int j;
        for (int i = 1; i < 9; i++){
            j = pageControl[location][i];
            blocks[j].asyncId = -1; //释放内存块
            blocks[j].timeForDie = 0;
            // block 中的 content 需要写到 兑换去
            //相应的磁盘接口 ...
        }
      //释放页表
        for(int i = 0; i <= 8; i++){
            pageControl[location][i] = -1;
        }
        pageControl[location][9] = 0;

    }

    public static int[] getTemp(){
        for (int i = 0; i<SIZE ; i++){
            blockTemp[i] = blocks[i].asyncId;
        }
        return blockTemp;
    }
//    public static void main(String[] args) {
//        diskManagement = new DiskManagementImpl();
//        diskManagement.DiskSpaceInit();
//        System.out.println(diskManagement.FileMake());
//        System.out.println(diskManagement.FileEdit(1,0,"aaaabbbbccccddddeeeeffffgggghhhheeee"));
//
//        IBlockDao iBlockDao = new IBlockDao();
//        IBlockDao.initialMemory();
//        iBlockDao.Malloc(1);
//        iBlockDao.access(1,3);
//        iBlockDao.access(1,4);
//        iBlockDao.access(1,5);
//        iBlockDao.access(1,6);
//        iBlockDao.access(1,7);
//        iBlockDao.access(1,8);
//        iBlockDao.access(1,9);
//        iBlockDao.access(1,10);
//        iBlockDao.access(1,3);
//        iBlockDao.access(1,12);
//        for(int i = 0; i <= 9; i++){
//            System.out.print(iBlockDao.pageControl[0][i]+" ");
//        }
//        System.out.println("");
//
//        for(int i = 0; i <= 7; i++){
//            System.out.print(iBlockDao.blocks[i].timeForDie+" ");
//        }
//        System.out.println("");
//        iBlockDao.freeBlockByAsyncId(1);
//        for(int i = 0; i <= 9; i++){
//            System.out.print(iBlockDao.pageControl[0][i]+" ");
//        }
//        System.out.println("");
//        iBlockDao.Malloc(1);
//        for(int i = 0; i <= 9; i++){
//            System.out.print(iBlockDao.pageControl[0][i]+" ");
//        }
//        System.out.println("");
//        iBlockDao.access(1,3);
//        iBlockDao.access(1,4);
//        iBlockDao.access(1,5);
//        iBlockDao.access(1,6);
//        iBlockDao.access(1,7);
//        iBlockDao.access(1,8);
//        iBlockDao.access(1,9);
//        iBlockDao.access(1,10);
//        iBlockDao.access(1,3);
//        for(int i = 0; i <= 7; i++){
//            System.out.print(iBlockDao.blocks[i].timeForDie+" ");
//        }
//        System.out.println("");
//        iBlockDao.access(1,12);
//        for(int i = 0; i <= 7; i++){
//            System.out.print(iBlockDao.blocks[i].timeForDie+" ");
//        }
//        System.out.println("");
//    }

}
