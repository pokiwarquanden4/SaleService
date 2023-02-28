package com.example.saleservice.controller;

import com.example.saleservice.repository.ConfigCacheRepo;
import com.example.saleservice.service.Cache.ApParamDALCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/test")
public class ReadFileController {
    @Autowired
    ApParamDALCache apParamDALCache;

    @GetMapping("/{parName}/{parType}")
    public Object readFile(@PathVariable String parName, @PathVariable String parType) throws IOException {
        return apParamDALCache.getApParam(null, parName, parType);
    }
}