//package com.example.czxtks.Models.MemoryManagement.model;
package com.example.czxtks.Models.MemoryManagement.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
//@AllArgsConstructor
@NoArgsConstructor
public class Block {
    public int pageNum;
    public int timeForDie;
    public int asyncId; //-1 代表当前未被分配
    public String content = "";

    public Block(int pageNum, int timeForDie , int asyncId ,String content){
        this.timeForDie = timeForDie;
        this.asyncId = asyncId;
        this.content = content;
        this.pageNum = pageNum;
    }
}

/*申请一块256KB的内存空间模拟内存，按逻辑划分为64块，每块4KB。
将目录中选中的文件读入内存，显示文件中信息。
内存可以同时存放多个文件信息，每个文件固定分配8个内存块，
如果8个内存块不能存放文件全部信息，采用页面置换策略，将满足置换策略条件的页换出内存，
可以选择的置换策略有，全局置换、局部置换、FIFO、LRU。内存管理需要支持：
（1）分配内存块：为线程分配内存块，每个线程默认分配8块。
（2）回收内存：线程结束后回收其内存。
（3）空闲内存块管理：为进入内存的数据寻找空闲内存块。没有空闲内存时，应给出提示。
（4）页表管理：记录页面在内存块的对应关系，提供数据块进入模拟内存的访问、修改情况，为页面置换算法提供支持。
*/