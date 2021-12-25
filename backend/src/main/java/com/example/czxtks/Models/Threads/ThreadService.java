package com.example.czxtks.Models.Threads;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Repository
public class ThreadService {
    public int getNextId(){
//        return (int)(System.currentTimeMillis()/1000);
        return new Random().nextInt(9 ) + 1;
    }

}
