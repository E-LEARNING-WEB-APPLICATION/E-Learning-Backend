package com.learnease.server.service.impl;

import com.learnease.server.dto.wishlist.WishlistResponseDto;
import com.learnease.server.exception.custom_exception.CourseNotFoundException;
import com.learnease.server.exception.custom_exception.UserNotFoundException;
import com.learnease.server.model.Course;
import com.learnease.server.model.Student;
import com.learnease.server.model.Wishlist;
import com.learnease.server.repository.CourseRepository;
import com.learnease.server.repository.StudentRepository;
import com.learnease.server.repository.WishlistRepository;
import com.learnease.server.service.WishlistService;
import com.learnease.server.util.mappers.WishlistMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository wishlistRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final WishlistMapper wishlistMapper;

    private Student getStudentFromAuthId(UUID authId) {
        return studentRepository.findByUserDetails_UserAuth_Id(authId)
                .orElseThrow(() -> new UserNotFoundException("Student not found for authenticated user"));
    }

    @Override
    public void addToWishlist(UUID authId, UUID courseId) {
        Student student = getStudentFromAuthId(authId);

        Course course = courseRepository.findById(courseId)
                .orElseThrow(()-> new CourseNotFoundException(courseId));

        boolean alreadyExists =
                wishlistRepository.existsByStudentAndCourse(student , course);

        //// Idempotent
        if (alreadyExists) {
            return;
        }

        Wishlist wishlist = new Wishlist();
        wishlist.setStudent(student);
        wishlist.setCourse(course);

        wishlistRepository.save(wishlist);
    }

    @Override
    public void removeFromWishlist(UUID authId, UUID courseId) {

        Student student = getStudentFromAuthId(authId);

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException(courseId));

        // Idempotent remove
        wishlistRepository.deleteByStudentAndCourse(student, course);
    }

    @Override
    public void markCoursePurchased(UUID authId, UUID courseId){
        Student student = getStudentFromAuthId(authId);

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException(courseId));

        Wishlist item = wishlistRepository.findByStudentAndCourse(student, course);
        if(item!=null) item.setPurchased(true);
    }

    @Override
    public List<WishlistResponseDto> getWishlist(UUID authId) {

        Student student = getStudentFromAuthId(authId);
        return wishlistRepository.findByStudentAndIsPurchasedFalse(student)
                .stream()
                .map(wishlistMapper::toResponseDto)
                .toList();
    }

    @Override
    public long getWishlistCount(UUID authId) {

        Student student = getStudentFromAuthId(authId);
        return wishlistRepository.countByStudentAndIsPurchasedFalse(student);
    }
}
