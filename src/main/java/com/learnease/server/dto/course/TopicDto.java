package com.learnease.server.dto.course;


import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TopicDto {
    private UUID tId;
    private String topicName;
    private String topicDesc;
    private String topicVideo;
    private String topicNotes;
    private LocalDateTime createdAt;
}
