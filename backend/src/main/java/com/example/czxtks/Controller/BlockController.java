package com.example.czxtks.Controller;

import com.example.czxtks.Models.MemoryManagement.util.JsonObject;
import com.example.czxtks.Models.MemoryManagement.service.IBlockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/block")
public class BlockController {

    @Autowired
    private IBlockService iBlockService;

    @GetMapping("/returnTemp")
    public @ResponseBody
    int[] returnTemp(){                    //向前端返回内存使用情况
        return iBlockService.returnTemp();
    }
}