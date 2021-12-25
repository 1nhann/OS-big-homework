package com.example.czxtks.Models.MemoryManagement;

public interface MemoryManagement {
    public int malloc(int num );
//    public int getThreadNextId();
    public boolean free(int threadId);
}
