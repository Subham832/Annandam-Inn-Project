package com.annandam_inn.repository;

import com.annandam_inn.entity.Favourite;
import com.annandam_inn.entity.PropertyUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavouriteRepository extends JpaRepository<Favourite, Long> {

    List<Favourite> findByPropertyUser(PropertyUser propertyUser);
    Optional<Favourite> findByIdAndPropertyUser(Long id, PropertyUser propertyUser);

}