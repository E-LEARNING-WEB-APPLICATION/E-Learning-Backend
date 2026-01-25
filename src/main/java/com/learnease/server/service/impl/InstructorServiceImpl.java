package com.learnease.server.service.impl;

import com.learnease.server.dto.ApiResponse;
import com.learnease.server.dto.CoursesDto;
import com.learnease.server.dto.JWTDTO;
import com.learnease.server.exception.custom_exception.ResourceNotFoundException;
import com.learnease.server.exception.custom_exception.UserNotFoundException;
import com.learnease.server.model.Category;
import com.learnease.server.model.Course;
import com.learnease.server.model.Instructor;
import com.learnease.server.model.UserDetails;
import com.learnease.server.repository.CategoryRepository;
import com.learnease.server.repository.CourseRepository;
import com.learnease.server.repository.InstructorRepository;
import com.learnease.server.repository.UserDetailRepository;
import com.learnease.server.service.InstructorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InstructorServiceImpl implements InstructorService {

    private final CourseRepository courseRepository;
    private final InstructorRepository instructorRepository;
    private final UserDetailRepository userDetailRepository;
    private final CategoryRepository categoryRepository;
    private final S3Service s3Service;

    @Override
    public ApiResponse addCourse(String courseName, String courseDesc, double fees, int discountPercentage, int hour, UUID categoryId, MultipartFile image, MultipartFile video, JWTDTO user) {


        String videoPath=null;
        String imagePath =null;
        UUID userId= user.getUserId();
        UserDetails userDetails = userDetailRepository.findByUserAuth_Id(userId)
                .orElseThrow(()->new UserNotFoundException("No such user exist"));
        Instructor instructor = instructorRepository.findByUserDetails_Id(userDetails.getId()).orElseThrow();
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()->new ResourceNotFoundException("No such category exist"));

        Course course = new Course();
        course.setTitle(courseName);
        course.setDescription(courseDesc);
        course.setFees(fees);
        course.setDiscount(discountPercentage);
        course.setHour(hour);
        course.setCategory(category);
        course.setInstructor(instructor);

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

    @Override
    public List<CoursesDto> getAllCourses(JWTDTO user) {
        UUID userId= user.getUserId();
        UserDetails userDetails = userDetailRepository.findByUserAuth_Id(userId)
                .orElseThrow();
        Instructor instructor = instructorRepository.findByUserDetails_Id(userDetails.getId()).orElseThrow();

        List<CoursesDto> courses = courseRepository.findByInstructor(instructor.getId());

        return courses;
    }
}
