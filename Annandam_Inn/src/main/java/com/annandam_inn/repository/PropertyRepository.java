package com.annandam_inn.repository;

import com.annandam_inn.entity.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PropertyRepository extends JpaRepository<Property, Long> {

                 //2 Tables
  // @Query("select p from Property p JOIN Location l on p.location = l.id where l.locationName=:locationName") //2 Tables

                //3 Tables
    @Query("select p from Property p JOIN Location l on p.location = l.id JOIN Country c on p.country = c.id where l.locationName=:locationName or c.countryName=:locationName") //3 Tables
    List<Property> findPropertyByLocation(@Param("locationName") String locationName);
}