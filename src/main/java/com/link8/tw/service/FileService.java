package com.link8.tw.service;

import com.link8.tw.exception.ActionFailException;
import com.link8.tw.exception.file.FileNotFoundException;
import com.link8.tw.exception.file.ImageNotFoundException;
import com.link8.tw.model.FileInfo;

import java.io.InputStream;

public interface FileService {

    int uploadImage(InputStream inputStream) throws ActionFailException;

    byte[] getImage(int id) throws  ImageNotFoundException;

    FileInfo getFileInfo(int id) throws FileNotFoundException;
}
