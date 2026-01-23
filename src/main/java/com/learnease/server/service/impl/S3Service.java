package com.learnease.server.service.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    @Autowired
    final private S3Client s3Client;

    @Value("${aws.bucket.name}")
    private String bucketName;

    public String uploadFile(MultipartFile file, String folder) throws IOException {

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        String key = folder + "/" + fileName;   // S3 path

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(file.getContentType())
                .build();

        s3Client.putObject(request, RequestBody.fromBytes(file.getBytes()));

        return "https://" + bucketName + ".s3.amazonaws.com/" + key;
    }

    public String getFileUrl(String fileName) {
        return "https://" + bucketName + ".s3.amazonaws.com/" + fileName;
    }

    // Delete file
    public void deleteFile(String fileName) {
        DeleteObjectRequest request = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();

        s3Client.deleteObject(request);
    }

}
