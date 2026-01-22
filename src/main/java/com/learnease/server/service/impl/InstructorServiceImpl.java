package com.learnease.server.service.impl;

import com.learnease.server.dto.ApiResponse;
import com.learnease.server.dto.JWTDTO;
import com.learnease.server.model.Course;
import com.learnease.server.model.Instructor;
import com.learnease.server.repository.CourseRepository;
import com.learnease.server.repository.InstructorRepository;
import com.learnease.server.service.InstructorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InstructorServiceImpl implements InstructorService {

    private final CourseRepository courseRepository;
    private final InstructorRepository instructorRepository;
    private final S3Service s3Service;

    @Override
    public ApiResponse addCourse(String courseName, String courseDesc, double fees, int discountPercentage, int hour, MultipartFile image, MultipartFile video, JWTDTO user) {


        String videoPath=null;
        String imagePath =null;
        UUID userId= user.getUserId();
        Optional<Instructor> instructor = instructorRepository.findById(userId);

        Course course = new Course();
        course.setTitle(courseName);
        course.setDescription(courseDesc);
        course.setFees(fees);
        course.setDiscount(discountPercentage);
        course.setHour(hour);
        course.setInstructor(instructor.orElseThrow());

        try {
            videoPath =s3Service.uploadFile(video,"course/courseIntroVideo");
            imagePath = s3Service.uploadFile(image,"course/courseThumbnails");
        }
        catch (IOException e)
        {
            return new ApiResponse(false,"Error While Uploading Image and Video");
        }

        course.setIntroVideo(videoPath);
        course.setThumbnail(imagePath);
       Course res = courseRepository.save(course);
       if(res == null)
       {
           return new ApiResponse(false,"Unable to save course in the database");
       }

       return new ApiResponse(true,"Course Added Successfully");


    }
}
