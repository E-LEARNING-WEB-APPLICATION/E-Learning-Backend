package com.learnease.server.dto.Profile;

import com.learnease.server.model.Skill;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class SkillRequestDto {
    List<Skill> skills;
}
