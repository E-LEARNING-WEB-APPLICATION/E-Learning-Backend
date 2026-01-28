package com.learnease.server.dto.course;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShowSectionsResDto {
   private UUID sectionId;
   private int sectionNumber;
   private String sectionTitle;
   private String sectionDesc;
}
