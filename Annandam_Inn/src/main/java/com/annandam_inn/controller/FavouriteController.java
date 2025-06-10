package com.annandam_inn.controller;

import com.annandam_inn.entity.Favourite;
import com.annandam_inn.entity.PropertyUser;
import com.annandam_inn.repository.FavouriteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/favourites")
public class FavouriteController {

    private final FavouriteRepository favouriteRepository;

    public FavouriteController(FavouriteRepository favouriteRepository) {
        this.favouriteRepository = favouriteRepository;
    }

    //http://localhost:8080/api/v1/favourites
    // Create
    @PostMapping
    public ResponseEntity<Favourite> addFavourite(
            @RequestBody Favourite favourite,
            @AuthenticationPrincipal PropertyUser user) {
        favourite.setPropertyUser(user);
        Favourite savedFavourite = favouriteRepository.save(favourite);
        return new ResponseEntity<>(savedFavourite, HttpStatus.CREATED);
    }

    //http://localhost:8080/api/v1/favourites
    // Read all favourites for the authenticated user
    @GetMapping
    public ResponseEntity<List<Favourite>> getAllFavourites(@AuthenticationPrincipal PropertyUser user) {
        List<Favourite> favourites = favouriteRepository.findByPropertyUser(user);
        return new ResponseEntity<>(favourites, HttpStatus.OK);
    }

    //http://localhost:8080/api/v1/favourites/{id}
    // Read a single favourite by ID
    @GetMapping("/{id}")
    public ResponseEntity<Favourite> getFavouriteById(@PathVariable Long id, @AuthenticationPrincipal PropertyUser user) {
        Optional<Favourite> favourite = favouriteRepository.findByIdAndPropertyUser(id, user);
        return favourite.map(f -> new ResponseEntity<>(f, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    //http://localhost:8080/api/v1/favourites/{id}
    // Update
    @PutMapping("/{id}")
    public ResponseEntity<Favourite> updateFavourite(@PathVariable Long id, @RequestBody Favourite updatedFavourite, @AuthenticationPrincipal PropertyUser user) {
        Optional<Favourite> optionalFavourite = favouriteRepository.findByIdAndPropertyUser(id, user);
        if (optionalFavourite.isPresent()) {
            Favourite existingFavourite = optionalFavourite.get();
            existingFavourite.setIsFavourite(updatedFavourite.getIsFavourite());
            existingFavourite.setProperty(updatedFavourite.getProperty());
            Favourite savedFavourite = favouriteRepository.save(existingFavourite);
            return new ResponseEntity<>(savedFavourite, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //http://localhost:8080/api/v1/favourites/{id}
    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFavourite(@PathVariable Long id, @AuthenticationPrincipal PropertyUser user) {
        Optional<Favourite> favourite = favouriteRepository.findByIdAndPropertyUser(id, user);
        if (favourite.isPresent()) {
            favouriteRepository.delete(favourite.get());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
