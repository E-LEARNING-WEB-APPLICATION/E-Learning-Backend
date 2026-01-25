package com.learnease.server.dto.course;


import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SectionDto {

    private UUID sId;
    private String sectionName;
    private String sectionDesc;
    private LocalDateTime createdAt;

    private List<TopicDto> topics;
}
