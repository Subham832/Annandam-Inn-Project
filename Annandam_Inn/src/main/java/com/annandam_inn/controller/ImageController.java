package com.annandam_inn.controller;

import com.annandam_inn.entity.Images;
import com.annandam_inn.entity.Property;
import com.annandam_inn.entity.PropertyUser;
import com.annandam_inn.repository.ImagesRepository;
import com.annandam_inn.repository.PropertyRepository;
import com.annandam_inn.service.BucketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/images")
public class ImageController {

    private ImagesRepository imagesRepository;
    private PropertyRepository propertyRepository;
    private BucketService bucketService;

    public ImageController(ImagesRepository imagesRepository, PropertyRepository propertyRepository, BucketService bucketService) {
        this.imagesRepository = imagesRepository;
        this.propertyRepository = propertyRepository;
        this.bucketService = bucketService;
    }

    //http://localhost:8080/api/v1/images/upload/file/annandaminn/property/1
    @PostMapping(path = "/upload/file/{bucketName}/property/{propertyId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addImage
                    (
                    @RequestParam MultipartFile file,
                    @PathVariable String bucketName,
                    @PathVariable long propertyId,
                    @AuthenticationPrincipal PropertyUser propertyUser
                    )
    {
        String imageUrl = bucketService.uploadFile(file, bucketName);
        Property property = propertyRepository.findById(propertyId).get();

        Images img = new Images();
        img.setImageUrl(imageUrl);
        img.setProperty(property);
        img.setPropertyUser(propertyUser);

        Images savedImage = imagesRepository.save(img);

        return new ResponseEntity<>(savedImage, HttpStatus.OK);
    }

}
