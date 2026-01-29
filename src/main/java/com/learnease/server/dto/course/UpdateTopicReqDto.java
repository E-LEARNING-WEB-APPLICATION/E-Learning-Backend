package com.learnease.server.dto.course;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Data
public class UpdateTopicReqDto {

    private UUID topicId;

    private Integer topicNumber;

    private String topicName;

    private String topicDesc;

    private Integer hour;

    private Integer min;

    private MultipartFile video;
}

