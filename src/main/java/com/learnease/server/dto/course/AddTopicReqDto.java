package com.learnease.server.dto.course;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddTopicReqDto {

   private UUID sectionId;
   private int topicNumber;
   private String topicName;
   private String topicDesc;
   private int hour;
   private int  min;

   @Schema(type = "string", format = "binary")
   private MultipartFile video;
}
