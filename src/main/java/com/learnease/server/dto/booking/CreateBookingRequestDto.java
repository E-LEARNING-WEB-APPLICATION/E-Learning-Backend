package com.learnease.server.dto.booking;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateBookingRequestDto {

    @NotNull
    private UUID courseId;

    @NotNull
    private UUID instructorId;
}
