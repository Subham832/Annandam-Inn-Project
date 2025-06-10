package com.annandam_inn.controller;

import com.annandam_inn.dto.ReviewDto;
import com.annandam_inn.entity.Property;
import com.annandam_inn.entity.PropertyUser;
import com.annandam_inn.entity.Review;
import com.annandam_inn.repository.PropertyRepository;
import com.annandam_inn.repository.ReviewRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {

    private ReviewRepository reviewRepository;
    private PropertyRepository propertyRepository;

    public ReviewController(ReviewRepository reviewRepository, PropertyRepository propertyRepository) {
        this.reviewRepository = reviewRepository;
        this.propertyRepository = propertyRepository;
    }

    //http://localhost:8080/api/v1/reviews/addReview/ooty
    //http://localhost:8080/api/v1/reviews/addReview/India
    //http://localhost:8080/api/v1/reviews/addReview/{propertyId}
    @PostMapping("/addReview/{propertyId}")
    public ResponseEntity<String> addReview
            (
                    @PathVariable long propertyId, //User is giving a review for a particular property. The Property ID will come from URL
                    @RequestBody Review review, //Review will come as Review DTO //For now I will directly take the content in review objects For now we are not using DTO layer.
                    @AuthenticationPrincipal PropertyUser propertyUser //this will give the reivew only who is logged in.
            ){

        Optional<Property> opProperty = propertyRepository.findById(propertyId);
        Property property = opProperty.get();

//        Review r = reviewRepository.findReviewByUserIdAndPropertyId(propertyId, propertyUser.getId());
//        // System.out.println(r.getContent());
//        if (r!=null){
//            return new ResponseEntity<>("You have already added a review for this property", HttpStatus.BAD_REQUEST);
//        }

        Review r = reviewRepository.findReviewByUser(property, propertyUser);
        if (r != null){

            return new ResponseEntity<>("You have already added a review for this property", HttpStatus.BAD_REQUEST);
        }

        review.setProperty(property); //we done a mapping and this object address is a Foreign Key
        review.setPropertyUser(propertyUser);// It is getting with @AuthenticationPrincipal because this will give us current user details logged In Automatically. I need to just supply the JWT Token while accessing the this url that Automatically gives us user Details.

        reviewRepository.save(review);
        return new ResponseEntity<>("Review Added Successfully", HttpStatus.CREATED);
    }

    //http://localhost:8080/api/v1/reviews/userReviews
    @GetMapping("/userReviews")
    public ResponseEntity<List<Review>> getUserReviews(@AuthenticationPrincipal PropertyUser propertyUser){

        List<Review> reviews = reviewRepository.findByPropertyUser(propertyUser);
        return new ResponseEntity<>(reviews, HttpStatus.OK);

    }

}
