package com.link8.tw.controller;

import com.link8.tw.controller.advice.ErrorCodeException;
import com.link8.tw.controller.errorCode.ActionErrorCode;
import com.link8.tw.exception.ActionFailException;
import com.link8.tw.exception.file.ImageNotFoundException;
import com.link8.tw.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class FileController {

    @Autowired
    FileService fileService;


    @PostMapping(value = "/image/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public int uploadImage(@RequestParam("file") MultipartFile file) throws ErrorCodeException, IOException {

        try {
            return fileService.uploadImage(file.getInputStream());
        } catch (ActionFailException e) {
            throw new ErrorCodeException(e.getErrorCode());
        }
    }

    @GetMapping(value = "/image/get/{id}", produces = "image/jpg")
    public ResponseEntity<byte[]> uploadImage(@PathVariable int id) throws ErrorCodeException {
        try {
            return ResponseEntity.ok().contentType(MediaType.IMAGE_GIF).body(fileService.getImage(id));
        } catch (ImageNotFoundException e) {
           throw new ErrorCodeException(e.getErrorCode());
        }
    }

//    @PostMapping("/file/upload")
//    public int uploadImage() {
//
//    }
//
//    @GetMapping("/file/get")
//    public int uploadImage() {
//
//    }
}
