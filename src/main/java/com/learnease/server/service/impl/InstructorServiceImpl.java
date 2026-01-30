package com.learnease.server.service.impl;

import com.learnease.server.dto.ApiResponse;
import com.learnease.server.dto.CourseInstructorResponseDto;
import com.learnease.server.dto.CoursesDto;
import com.learnease.server.dto.JWTDTO;
import com.learnease.server.dto.course.*;
import com.learnease.server.dto.instructor.DashboardInstructorResponseDto;
import com.learnease.server.dto.notification.SendNotificationDTO;
import com.learnease.server.exception.custom_exception.FileStorageException;
import com.learnease.server.exception.custom_exception.ResourceNotFoundException;
import com.learnease.server.exception.custom_exception.UserNotFoundException;
import com.learnease.server.model.*;
import com.learnease.server.repository.*;
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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.embedded.netty.NettyWebServer;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class InstructorServiceImpl implements InstructorService {

    private final CourseRepository courseRepository;
    private final InstructorRepository instructorRepository;
    private final UserDetailRepository userDetailRepository;
    private final CategoryRepository categoryRepository;
    private final S3Service s3Service;
    private final InstructorMapper instructorMapper;
    private final SectionRepository sectionRepository;
    private final TopicRepository topicRepository;
    private final TopicServiceImpl topicService;
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
    public ApiResponse addSection(UUID userId, AddSectionReqDto reqDto) {
        UserDetails userDetails = userDetailRepository.findByUserAuth_Id(userId)
                .orElseThrow(()-> new ResourceNotFoundException("Instructor Not Found"));
        Instructor instructor = instructorRepository.findByUserDetails_Id(userDetails.getId()).orElseThrow(() -> new ResourceNotFoundException("Instructor Not Found"));
        Course course = courseRepository.findById(reqDto.getCourseId()).orElseThrow(() -> new ResourceNotFoundException("Course Not Found"));
        Section section = new Section();
        section.setSectionNumber(reqDto.getSectionNumber());
        section.setTitle(reqDto.getSectionTitle());
        section.setDescription(reqDto.getSectionDesc());
        section.setCourse(course);
        Section s = sectionRepository.save(section);
        return new ApiResponse(true,s.getId());
    }

    @Override
    public List<ShowSectionsResDto> getAllSections(UUID courseId , UUID userId) {

        UserDetails userDetails = userDetailRepository.findByUserAuth_Id(userId)
                .orElseThrow(()-> new ResourceNotFoundException("Instructor Not Found"));
        Instructor instructor = instructorRepository.findByUserDetails_Id(userDetails.getId()).orElseThrow(() -> new ResourceNotFoundException("Instructor Not Found"));
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new ResourceNotFoundException("Course Not Found"));
        if(!course.getInstructor().getId().equals(instructor.getId()))
        {
            throw new AuthorizationDeniedException("Can't access Another Instructor Course Sections");
        }
        List<ShowSectionsResDto> allSections = sectionRepository.getAllSections(courseId);
        return allSections;
    }

   @Override
    public ApiResponse addTopic(UUID userId, AddTopicReqDto reqDto) {

        String videoUrl;
        try {
            videoUrl = s3Service.uploadFile(
                    reqDto.getVideo(), "topics/videos"
            );
        } catch (IOException e) {
            throw new FileStorageException("Failed to upload video", e);
        }

        return topicService.saveTopic(userId, reqDto, videoUrl);
    }

    @Override
    public ApiResponse updateTopic(UUID userId, UpdateTopicReqDto reqDto) {

        String videoUrl = null;

        if (reqDto.getVideo() != null && !reqDto.getVideo().isEmpty()) {
            try {
                videoUrl = s3Service.uploadFile(
                        reqDto.getVideo(), "topics/videos"
                );
            } catch (IOException e) {
                throw new FileStorageException("Failed to upload video", e);
            }
        }

        return topicService.updateTopic(userId, reqDto, videoUrl);
    }


    @Override
    public List<TopicResponseDto> getTopics(UUID sectionId, UUID userId) {

        UserDetails userDetails = userDetailRepository.findByUserAuth_Id(userId)
                .orElseThrow(()-> new ResourceNotFoundException("Instructor Not Found"));
        Instructor instructor = instructorRepository.findByUserDetails_Id(userDetails.getId()).orElseThrow(() -> new ResourceNotFoundException("Instructor Not Found"));
        Section section = sectionRepository.findById(sectionId).orElseThrow(()-> new ResourceNotFoundException("Section Not Found"));
        if(!section.getCourse().getInstructor().getId().equals(instructor.getId()))
        {
            throw new AuthorizationDeniedException("Can't access Another Instructor Course Sections");
        }

        List<TopicResponseDto> topicResponseDtos = topicRepository.getAllTopics(sectionId);
        return topicResponseDtos;
    }

    @Override
    public ApiResponse updateCourse(
            UUID courseId,
            String courseName,
            String courseDesc,
            double fees,
            int discountPercentage,
            int hour,
            UUID categoryId,
            MultipartFile image,
            MultipartFile video,
            JWTDTO user
    ) {

        UUID userId = user.getUserId();

        UserDetails userDetails = userDetailRepository
                .findByUserAuth_Id(userId)
                .orElseThrow(() -> new UserNotFoundException("No such user exist"));

        Instructor instructor = instructorRepository
                .findByUserDetails_Id(userDetails.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Instructor not found"));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));


        if (!course.getInstructor().getId().equals(instructor.getId())) {
            return new ApiResponse(false, "You are not authorized to update this course");
        }

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("No such category exist"));


        course.setTitle(courseName);
        course.setDescription(courseDesc);
        course.setFees(fees);
        course.setDiscount(discountPercentage);
        course.setHour(hour);
        course.setCategory(category);

        try {

            if (image != null && !image.isEmpty()) {
                String imagePath = s3Service.uploadFile(image, "course/courseThumbnails");
                course.setThumbnail(imagePath);
            }


            if (video != null && !video.isEmpty()) {
                String videoPath = s3Service.uploadFile(video, "course/courseIntroVideo");
                course.setIntroVideo(videoPath);
            }

        } catch (IOException e) {
            return new ApiResponse(false, "Error while uploading image/video");
        }

        Course updatedCourse = courseRepository.save(course);

        if (updatedCourse == null) {
            return new ApiResponse(false, "Unable to update course");
        }

        return new ApiResponse(true, "Course Updated Successfully");
    }

    @Override
    public SectionDto getSection(UUID sectionId) {

        Section section = sectionRepository.findById(sectionId).orElseThrow(()-> new ResourceNotFoundException("Section Not Found"));
        SectionDto dto = new SectionDto();
        dto.setSectionName(section.getTitle());
        dto.setSectionDesc(section.getDescription());
        dto.setSectionNumber(section.getSectionNumber());
        return dto;

    }

    @Transactional
    @Override
    public ApiResponse updateSection(UUID userId, UUID sectionId, AddSectionReqDto reqDto) {

        UserDetails userDetails = userDetailRepository
                .findByUserAuth_Id(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Instructor Not Found"));

        Instructor instructor = instructorRepository
                .findByUserDetails_Id(userDetails.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Instructor Not Found"));

        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new ResourceNotFoundException("Section Not Found"));


        if (!section.getCourse().getInstructor().getId().equals(instructor.getId())) {
            return new ApiResponse(false, "You are not authorized to update this section");
        }

        Course course = courseRepository.findById(section.getCourse().getId()).orElseThrow(() -> new ResourceNotFoundException("Course Not Found"));


        section.setTitle(reqDto.getSectionTitle());
        section.setDescription(reqDto.getSectionDesc());
        section.setSectionNumber(reqDto.getSectionNumber());
        sectionRepository.save(section);

        return new ApiResponse(true, "Section Updated Successfully");
    }

    @Override
    public TopicResponseDto getTopic(UUID topicId) {
        Topic topic = topicRepository.findById(topicId).orElseThrow(()-> new ResourceNotFoundException("Topic Not Found"));
        TopicResponseDto dto = new TopicResponseDto();
        dto.setTopicId(topicId);
        dto.setMin(topic.getMin());
        dto.setNotes(topic.getNotes());
        dto.setTopicNumber(topic.getTopicNumber());
        dto.setDescription(topic.getDescription());
        dto.setVideoUrl(topic.getVideo());
        dto.setTitle(topic.getTitle());
        return dto;
    }

    @Override
    public List<CourseStudentDto> getCoursesData(UUID userId) {
        List<CourseStudentDto> dtos = new ArrayList<>();

        UserDetails userDetails = userDetailRepository
                .findByUserAuth_Id(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Instructor Not Found"));

        Instructor instructor = instructorRepository
                .findByUserDetails_Id(userDetails.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Instructor Not Found"));

        List<Course> courses = instructor.getCourses();

        for(Course c : courses)
        {
            CourseStudentDto dto = new CourseStudentDto();
            dto.setId(c.getId());
            dto.setCourseName(c.getTitle());
            dto.setImageUrl(c.getThumbnail());
            dto.setStudents(c.getStudents().size());
            dto.setDescription("Best "+ c.getCategory().getTitle() +" Course");
            dtos.add(dto);
        }

        return dtos;

    }

    @Override
    public List<StudentListDto> getStudentsList(UUID userId,UUID courseId) {
        List<StudentListDto> dtos = new ArrayList<>();

        UserDetails userDetails = userDetailRepository
                .findByUserAuth_Id(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Instructor Not Found"));

        Instructor instructor = instructorRepository
                .findByUserDetails_Id(userDetails.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Instructor Not Found"));

        Course course = courseRepository.findById(courseId).orElseThrow(() -> new ResourceNotFoundException("Course Not Found"));

        Set<Student> students = course.getStudents();

        for(Student student : students )
        {
            StudentListDto dto = new StudentListDto();
            dto.setId(student.getId());
            if(student.getUserDetails().getGender() != null)
            dto.setGender(student.getUserDetails().getGender().name());
            if(student.getUserDetails().getDob() != null)
            dto.setDob(student.getUserDetails().getDob().toString());
            if(student.getUserDetails().getAddress() != null)
            dto.setAddress(student.getUserDetails().getAddress().getCity());
            dto.setFirstName(student.getUserDetails().getFirstName());
            dto.setLastName(student.getUserDetails().getLastName());
            dto.setPhoneNo(student.getUserDetails().getPhoneNo());
            dto.setProfilePic(student.getUserDetails().getProfilePic());
            Booking booking =  bookingRepository.findByStudentAndPurchasedCourse(student,course).orElseThrow(() -> new ResourceNotFoundException("Booking Not Found"));
            dto.setCoursePurchasedOn(booking.getPaidAt().toLocalDate().toString());
            dtos.add(dto);
        }

        return dtos;

    }

    @Override
    public ApiResponse withdrawMoney(UUID authId) {
        Instructor instructor = instructorRepository.findByUserDetails_UserAuth_Id(authId)
                .orElseThrow(()->new UserNotFoundException("No such Instructor Exist"));
        BigDecimal availableBalance =
                walletTransactionRepository.getAvailableBalance(instructor.getId());

        if (availableBalance.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalStateException("No available balance to withdraw");
        }

        // 🔹 call payout gateway here
        // payoutService.transfer(instructorId, availableBalance);

        int rowAffected = walletTransactionRepository.withdrawAvailableAmount(
                instructor.getId(),
                LocalDateTime.now()
        );

        if (rowAffected == 0){
            throw new IllegalStateException("Something went on our side the if money is not deposited in next 2 to 3 working days please contact admin");
        }
        availableBalance =
                walletTransactionRepository.getAvailableBalance(instructor.getId());

        return new ApiResponse(true,"All the money is withdrawn");
    }


    @Override
    public CourseInstructorResponseDto getInstructorById(UUID instructorId) {

        Instructor instructor = instructorRepository.findById(instructorId)
                .orElseThrow(()-> new UserNotFoundException("Instructor Not Found"));


        return instructorMapper.toResponse(instructor);
    }
}
