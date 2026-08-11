package com.shivraj.filestorage.service;

import com.shivraj.filestorage.exc.GenericErrorResponse;
import com.shivraj.filestorage.model.File;
import com.shivraj.filestorage.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StorageService {
    private final FileRepository fileRepository;

    @Value("${file.storage.path}")
    private String FOLDER_PATH;

    @PostConstruct
    public void init() {
       
        java.io.File targetFolder = new java.io.File(FOLDER_PATH);

        if (!targetFolder.exists()) {
            boolean directoriesCreated = targetFolder.mkdirs();
            if (!directoriesCreated) {
                throw GenericErrorResponse.builder()
                        .message("Unable to create directories at: " +FOLDER_PATH)
                        .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                        .build();
            }
        }
    }

    public String uploadImageToFileSystem(MultipartFile file) {
        String uuid = UUID.randomUUID().toString();
        Path filePath = Paths.get(FOLDER_PATH, uuid);

        try {
            file.transferTo(filePath.toFile());
        } catch (IOException e) {
            throw GenericErrorResponse.builder()
                    .message("Unable to save file to storage")
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }

        fileRepository.save(File.builder()
                .id(uuid)
                .type(file.getContentType())
                .filePath(filePath.toString()).build());
        return uuid;
    }

    public byte[] downloadImageFromFileSystem(String id) {
        try {
            return Files.readAllBytes(Paths.get(findFileById(id).getFilePath()));
        } catch (IOException e) {
            throw GenericErrorResponse.builder()
                    .message("Unable to read file from storage")
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    public void deleteImageFromFileSystem(String id) {
        java.io.File file = new java.io.File(findFileById(id).getFilePath());

        boolean deletionResult = file.delete();

        if (deletionResult) fileRepository.deleteById(id);

        else throw GenericErrorResponse.builder()
                .message("Unable to delete file from storage")
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
    }


    protected File findFileById(String id) {
        return fileRepository.findById(id)
                .orElseThrow(() -> GenericErrorResponse.builder()
                        .message("File not found")
                        .httpStatus(HttpStatus.NOT_FOUND)
                        .build());
    }
}