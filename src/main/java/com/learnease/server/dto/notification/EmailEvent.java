package com.learnease.server.dto.notification;

import com.learnease.server.model.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailEvent {

    private NotificationType eventType;

    private List<String> to;

    private String subject;

    private Map<String, Object> data;

    private Map<String, String> attachments;

    private Map<String, Object> meta;
}
