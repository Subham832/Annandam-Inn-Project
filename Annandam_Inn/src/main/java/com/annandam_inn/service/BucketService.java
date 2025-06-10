package com.annandam_inn.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Service
public class BucketService {

    @Autowired
    private AmazonS3 amazonS3;

    //Uploading the file.
    public String uploadFile(MultipartFile file, String bucketName){
        if (file.isEmpty()){
            throw new IllegalStateException("Cannot Upload Empty File");
        }
        try {
            File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
            file.transferTo(convFile);
            try {
                amazonS3.putObject(bucketName, convFile.getName(), convFile);
                return amazonS3.getUrl(bucketName, file.getOriginalFilename()).toString();
            }
            catch (AmazonS3Exception s3Exception){
                return "Unable To Upload File :" +s3Exception.getMessage();
            }
        }
        catch (Exception e){
            throw new IllegalStateException("Failed To Upload File", e);
        }
    }

    //Delete
//    public String deleteBucket(String bucketName){
//        amazonS3.deleteBucket(bucketName);
//        return "File Is Deleted";
//    }

}
