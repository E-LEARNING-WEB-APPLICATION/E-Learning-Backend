package com.learnease.server.service.impl;

import com.learnease.server.dto.ApiResponse;
import com.learnease.server.dto.CourseInstructorResponseDto;
import com.learnease.server.dto.CoursesDto;
import com.learnease.server.dto.JWTDTO;
import com.learnease.server.dto.instructor.DashboardInstructorResponseDto;
import com.learnease.server.dto.notification.SendNotificationDTO;
import com.learnease.server.exception.custom_exception.ResourceNotFoundException;
import com.learnease.server.exception.custom_exception.UserNotFoundException;
import com.learnease.server.model.Category;
import com.learnease.server.model.Course;
import com.learnease.server.model.Instructor;
import com.learnease.server.model.UserDetails;
import com.learnease.server.model.enums.NotificationPriority;
import com.learnease.server.model.enums.NotificationSubjectType;
import com.learnease.server.model.enums.NotificationType;
import com.learnease.server.model.enums.Role;
import com.learnease.server.repository.CategoryRepository;
import com.learnease.server.repository.CourseRepository;
import com.learnease.server.repository.InstructorRepository;
import com.learnease.server.repository.UserDetailRepository;
import com.learnease.server.service.InstructorService;
import com.learnease.server.service.NotificationService;
import com.learnease.server.util.mappers.InstructorMapper;
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
    private final InstructorMapper instructorMapper;
    private final NotificationService notificationService;

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

       notificationService.sendNotification(
               SendNotificationDTO.builder()
                       .title("new course arrived ")
                       .message("check this new course " + course.getTitle())
                       .type(NotificationType.COURSE_PUBLISHED)
                       .priority(NotificationPriority.LOW)
                       .subjectId(course.getId())
                       .subjectType(NotificationSubjectType.COURSE)
                       .role(Role.STUDENT)
                       .build()

       );
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

    @Override
    public List<DashboardInstructorResponseDto> getAllInstructors() {
        return instructorRepository.findAll()
                .stream()
                .map(i-> new DashboardInstructorResponseDto(
                        i.getId(),
                        i.getUserDetails().getFirstName() +" "+ i.getUserDetails().getLastName(),
                        i.getBio(),
                        i.getSpecializations().stream()
                                .map(s-> s.getTitle())
                                .toList(),
                        i.getCourses().size(),
                        i.getCourses().stream()
                                .mapToInt(c -> c.getStudents().size())
                                .sum(),
                        i.getUserDetails().getProfilePic(),
                        i.getUserDetails()
                                .getUserAuth()
                                .getEmail(),
                        i.getGitHubUrl(),
                        i.getLinkedInUrl(),
                        i.getTwitterUrl()

                )).toList();
    }

    @Override
    public CourseInstructorResponseDto getInstructorById(UUID instructorId) {

        Instructor instructor = instructorRepository.findById(instructorId)
                .orElseThrow(()-> new UserNotFoundException("Instructor Not Found"));


        return instructorMapper.toResponse(instructor);
    }
}
