package com.link8.tw.service.impl;

import com.link8.tw.controller.errorCode.ActionErrorCode;
import com.link8.tw.exception.ActionFailException;
import com.link8.tw.exception.file.FileNotFoundException;
import com.link8.tw.exception.file.ImageNotFoundException;
import com.link8.tw.model.FileInfo;
import com.link8.tw.repository.FileRepository;
import com.link8.tw.service.FileService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    FileRepository fileRepository;

    @Value("${imageService.imageBase}")
    private String imageBase;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int uploadImage(InputStream inputStream) throws ActionFailException {
        String uuid = UUID.randomUUID().toString();
        Path path = Paths.get(imageBase, uuid);
        try {
            Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new ActionFailException(ActionErrorCode.IMAGE_UPLOAD_FAIL);
        }
        FileInfo file = new FileInfo();
        file.setUuid(uuid);
        fileRepository.save(file);
        return file.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public byte[] getImage(int id) throws ImageNotFoundException {
        Optional<FileInfo> file = fileRepository.findById(id);
        if (!file.isPresent()) {
            throw new ImageNotFoundException(ActionErrorCode.IMAGE_GET_FAIL);
        }
        String path = imageBase + File.separator + file.get().getUuid();
        File img = new File(path);
        try {
            return FileUtils.readFileToByteArray(img);
        } catch (IOException e) {
            return new byte[0];
        }
    }

    @Override
    public FileInfo getFileInfo(int id) throws FileNotFoundException {
        Optional<FileInfo> file = fileRepository.findById(id);
        if (!file.isPresent()) {
            throw new FileNotFoundException(ActionErrorCode.IMAGE_GET_FAIL);
        }
        return file.get();

    }
}
