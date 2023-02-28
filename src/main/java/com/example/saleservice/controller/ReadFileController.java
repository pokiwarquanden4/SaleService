//package com.example.saleservice.controller;
//
//import com.example.saleservice.model.ResponseUtils;
//import com.example.saleservice.repository.ConfigCacheRepo;
//import com.example.saleservice.service.FTPClient.FTPReaderService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.io.IOException;
//
//@RestController
//@RequestMapping("/test")
//public class ReadFileController {
//    @Autowired
//    ConfigCacheRepo configCacheRepo;
//
//    @GetMapping("/cache/")
//    public String readFile(@PathVariable String serial) throws IOException {
//        String base64String = configCacheRepo.findConfig()
//
//        return base64String != null ?
//                ResponseEntity.status(HttpStatus.OK).body(
//                        new ResponseObject(200, "Convert PNG Successfully", new ResponseUtils(base64String))
//                ):
//                ResponseEntity.status(HttpStatus.NOT_FOUND).body(
//                        new ResponseObject(404, "Convert PNG Failed", "")
//                );
//    }
//}