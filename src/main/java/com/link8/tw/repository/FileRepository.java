package com.link8.tw.repository;

import com.link8.tw.model.FileInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<FileInfo,Integer> {
}
