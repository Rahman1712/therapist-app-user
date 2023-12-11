package com.ar.therapist.user.vendor.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.ar.therapist.user.dto.Review;
import com.ar.therapist.user.vendor.service.TherapistReviewService;

@RestController
@RequestMapping("/api/v1/reviews")
public class TherapistReviewController {

    @Autowired
    private TherapistReviewService reviewService;

    @GetMapping("/user/{userId}")
    public List<Review> getReviewsByUserId(@PathVariable Long userId) {
        return reviewService.getReviewsByUserId(userId);
    }

    @GetMapping("/user/{userId}/therapist/{therapistId}")
    public List<Review> getReviewsByUserIdAndTherapistId(@PathVariable Long userId, @PathVariable Long therapistId) {
        return reviewService.getReviewsByUserIdAndTherapistId(userId, therapistId);
    }

    @GetMapping("/therapist/{therapistId}")
    public List<Review> getReviewsByTherapistId(@PathVariable Long therapistId) {
        return reviewService.getReviewsByTherapistId(therapistId);
    }

    @GetMapping("/booking/{bookingId}")
    public Review getReviewsByBookingId(@PathVariable String bookingId) {
        return reviewService.getReviewByBookingId(bookingId);
    }
    
    @PostMapping("/create")
    public Review createReview(@RequestBody Review review) {
        return reviewService.createReview(review);
    }

    @PutMapping("/update/{id}")
    public Review updateReview(@PathVariable Long id, @RequestBody Review updatedReview) {
        return reviewService.updateReview(id, updatedReview);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
    }
}
