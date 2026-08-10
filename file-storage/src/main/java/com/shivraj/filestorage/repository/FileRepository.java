package com.shivraj.filestorage.repository;

import com.shivraj.filestorage.model.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, String> {
}
