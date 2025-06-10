package com.annandam_inn.repository;

import com.annandam_inn.entity.Property;
import com.annandam_inn.entity.PropertyUser;
import com.annandam_inn.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

//    @Query("select r from Review r where r.property=:propertyId and r.propertyUser=:userId")
//    Review findReviewByUserIdAndPropertyId(@Param("propertyId")Property property, @Param("userId") Long propertyId);


    //Whether the user has given review or not.
    @Query("select r from Review r where r.property=:property and r.propertyUser=:user")
    Review findReviewByUser(@Param("property") Property property, @Param("user") PropertyUser user);


    //All the Review for a particular user.
    List<Review> findByPropertyUser(PropertyUser propertyUser);
}