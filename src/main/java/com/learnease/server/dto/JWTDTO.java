package com.learnease.server.dto;


import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class JWTDTO {
    private UUID userId;
    private String email;
    private String role;
}
