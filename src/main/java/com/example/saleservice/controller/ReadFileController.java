package com.example.saleservice.controller;

import com.example.saleservice.model.ResponseObject;
import com.example.saleservice.model.ResponseUtils;
import com.example.saleservice.service.FTPClient.FTPReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/Bitel_Project/readFileController/v1")
public class ReadFileController {
    @Autowired
    private FTPReaderService ftpReaderService;
    @GetMapping("/readFile/{serial}/{urlKey}")
//    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ResponseObject> readFile(@PathVariable String serial, @PathVariable String urlKey) throws IOException {
        String base64String = ftpReaderService.convertToBase64(serial, urlKey);

        return base64String != null ?
                ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject(200, "Convert PNG Successfully", new ResponseUtils(base64String))
                ):
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ResponseObject(404, "Convert PNG Failed", "")
                );
    }
}