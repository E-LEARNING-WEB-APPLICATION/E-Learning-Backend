package com.learnease.server.service;

import com.learnease.server.dto.ApiResponse;
import com.learnease.server.dto.JWTDTO;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


public interface InstructorService {

    ApiResponse addCourse(String courseName,String courseDesc,double fees,int discountPercentage,int hour,MultipartFile image,
                          MultipartFile video,
                          JWTDTO user);

}
