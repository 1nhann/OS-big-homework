package com.example.czxtks.Models.Threads;
import com.example.czxtks.Models.DirectoryManagement.DireService;
import com.example.czxtks.Models.DirectoryManagement.entity.FILE;
import com.example.czxtks.Models.DiskManagement.Conmmon.DiskToMem;
import com.example.czxtks.Models.DiskManagement.DiskManagement;
import com.example.czxtks.Models.MemoryManagement.MemoryManagement;
import com.example.czxtks.Models.MemoryManagement.service.IBlockService;
import com.example.czxtks.Models.DirectoryManagement.vo.File;
import com.example.czxtks.Models.MemoryManagement.util.JsonObject;

import org.springframework.beans.factory.annotation.Autowired;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Random;

public class ReadFileContentThread implements Runnable{
    private int threadId;
    private DireService directoryManagement;
    private IBlockService memoryManagement;
    private String fileName ;
    public File returnFile;
    private DiskManagement diskManagement;

//    private ThreadService threadService;
    public  static int mutex = 1;
    @Override
    public void run() {
        try {
            memoryManagement.Malloc(threadId);

            FILE file = directoryManagement.showFileContent(fileName);

            String address = file.getAddress();

            returnFile = new File();
            returnFile.setUser(file.getUser());
            returnFile.setDate(file.getDate());
            returnFile.setFilesize(file.getFilesize());

            ArrayList<Integer> nums = new ArrayList<>();

            DiskToMem index1 = diskManagement.getNewPage(Integer.valueOf(address));
            for(Integer i : index1.indexnode){
                ArrayList<Integer> indexes = diskManagement.getNewPage(i).indexnode;
                nums.addAll(indexes);
            }

            String content = new String();

            for (int i : nums){
                while(ReadFileContentThread.mutex == 0){;}
                ReadFileContentThread.mutex = 0;
                content +=  memoryManagement.access(threadId,i);
                ReadFileContentThread.mutex = 1;
                Thread.sleep(2000);
            }
            returnFile.setContent(content);
            System.out.println(content);
            memoryManagement.free(threadId);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public ReadFileContentThread(int id , DireService directoryManagement, IBlockService memoryManagement, DiskManagement diskManagement, ThreadService threadService,String fileName){
        this.threadId = id;
        this.directoryManagement = directoryManagement;
        this.memoryManagement = memoryManagement;
        this.fileName = fileName;
        this.diskManagement = diskManagement;
    }
}