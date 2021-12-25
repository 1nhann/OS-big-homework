//package com.example.czxtks.Models.MemoryManagement.service;
package com.example.czxtks.Models.MemoryManagement.service;
import com.example.czxtks.Models.MemoryManagement.util.*;
import org.springframework.stereotype.Service;

//@Service
public interface IBlockService {

    void startMemory();
    //初始化内存空间
    //前端

    JsonObject requestMemory();
    //全局LRU

    JsonObject freeMemory(int asyncId);
    //释放内存空间

    int[] returnTemp();

    public boolean Malloc(int threadID);

    public String access  (int threadID, int blockNum);
    public void free(int asyncId);
}