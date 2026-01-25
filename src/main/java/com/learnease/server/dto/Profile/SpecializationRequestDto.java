package com.learnease.server.dto.Profile;

import com.learnease.server.model.Specialization;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class SpecializationRequestDto {
    Set<Specialization> specializations;
}
