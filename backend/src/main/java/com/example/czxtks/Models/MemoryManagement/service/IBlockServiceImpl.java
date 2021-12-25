package com.example.czxtks.Models.MemoryManagement.service;

import com.example.czxtks.Models.MemoryManagement.util.JsonObject;
import com.example.czxtks.Models.MemoryManagement.dao.IBlockDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IBlockServiceImpl implements IBlockService{

    @Autowired
    private IBlockDao iBlockDao;

    @Autowired
    public void startMemory() {      //初始化内存空间
        iBlockDao.initialMemory();
    }

    @Autowired
    public JsonObject requestMemory() {        //全局LRU实现
        if (iBlockDao.isOK()){
            iBlockDao.getRandom();
            iBlockDao.LRU();
            return JsonObject.success(iBlockDao.getID());
        } else {
            return JsonObject.deFault(iBlockDao.getID());
        }
    }

//    @Autowired
    public JsonObject freeMemory(int asyncId) {
        iBlockDao.freeBlockByAsyncId(asyncId);
        return JsonObject.successFree(asyncId);
    }

    @Override
    public int[] returnTemp() {
        return iBlockDao.getTemp();
    }

//    @Autowired
    @Override
    public boolean Malloc(int threadID) {
        return iBlockDao.Malloc(threadID);
    }

//    @Autowired
    @Override
    public String access(int threadID, int blockNum) {
        return iBlockDao.access(threadID,blockNum);
    }

//    @Autowired
    @Override
    public void free(int asyncId) {
        iBlockDao.free(asyncId);
    }
}
