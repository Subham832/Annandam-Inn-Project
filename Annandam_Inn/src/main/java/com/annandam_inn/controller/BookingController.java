package com.annandam_inn.controller;

import com.annandam_inn.dto.BookingDto;
import com.annandam_inn.entity.Booking;
import com.annandam_inn.entity.Property;
import com.annandam_inn.entity.PropertyUser;
import com.annandam_inn.repository.BookingRepository;
import com.annandam_inn.repository.PropertyRepository;
import com.annandam_inn.service.BucketService;
import com.annandam_inn.service.PDFService;
import com.annandam_inn.service.SmsService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

@RestController
@RequestMapping("/api/v1/booking")
public class BookingController {

    private BookingRepository bookingRepository;
    private PropertyRepository propertyRepository;
    private PDFService pdfService;
    private BucketService bucketService;
    private SmsService smsService;

    public BookingController(BookingRepository bookingRepository, PropertyRepository propertyRepository, PDFService pdfService, BucketService bucketService, SmsService smsService) {
        this.bookingRepository = bookingRepository;
        this.propertyRepository = propertyRepository;
        this.pdfService = pdfService;
        this.bucketService = bucketService;
        this.smsService = smsService;
    }

    //http://localhost:8080/api/v1/booking/createBooking/1
    @PostMapping("/createBooking/{propertyId}")
    public ResponseEntity<?> createBooking
    (
            @RequestBody Booking booking,
            @AuthenticationPrincipal PropertyUser propertyUser,
            @PathVariable long propertyId
    ) throws IOException {
        booking.setPropertyUser(propertyUser);
//        Property property = booking.getProperty(); //For getting the Property Id by booking.
//        Long propertyId = property.getId();
//        Property completePropertyInfo = propertyRepository.findById(propertyId).get();
        //       Booking createdBooking = bookingRepository.save(booking);

        Property property = propertyRepository.findById(propertyId).get();
        int propertyPrice = property.getNightlyPrice(); //Formula to give the totalPrice for Booking
        int totalNights = booking.getTotalNights();
        int totalPrice = propertyPrice * totalNights;
        booking.setProperty(property);
        booking.setTotalPrice(totalPrice);
        Booking createdBooking = bookingRepository.save(booking);

        BookingDto dto = new BookingDto();
        dto.setBookingId(createdBooking.getId());
        dto.setGuestName(createdBooking.getGuestName());
        dto.setPrice(propertyPrice);
        dto.setTotalPrice(createdBooking.getTotalPrice());
        //Create PDF with Booking Confirmation
        boolean b = pdfService.generatePDF("D://PROJECT//Intellij Idea Project//" + "booking-confirmation-id" + createdBooking.getId() + ".pdf", dto);//In this i am fixing the Path before (+) symbol path is there.
        if (b)
        {
            //Upload Our File Into Bucket.
            MultipartFile file = BookingController.convert("D://PROJECT//Intellij Idea Project//" + "booking-confirmation-id" + createdBooking.getId() + ".pdf"); //In this we have to apply the Class name then only the Convert() will run.
            String uploadFileUrl = bucketService.uploadFile(file, "annandaminn");
//            System.out.println(uploadFileUrl); //It will give the url that AWS s3 bucket has taken the Information of the data which we send form here, It will show only URL.
            smsService.sendSms("+918002983919","Your Booking Is Confirmed. Click For More Information:"+uploadFileUrl); //In this it will send the sms to the number by twilio number, first the file will go to the bucket then the file url with message will go the number by sms.
        }
        else
        {
            return new ResponseEntity<>("Something Went Worng", HttpStatus.INTERNAL_SERVER_ERROR);

        }

        return new ResponseEntity<>(createdBooking, HttpStatus.CREATED);
    }

    //http://localhost:8080/api/v1/booking/createPdf
//    @PostMapping("/createPdf")
//    public void createPdf()
//    {
//        pdfService.generatePDF();
//
//    }

    //This code is for Converting String file to the MultipartFile.
    public static MultipartFile convert(String filePath) throws IOException {

        //Load the file from the specified path
        File file = new File(filePath);

        //Read the file content into a byte array
        byte[] fileContent = Files.readAllBytes(file.toPath());

        //Convert byte array to a Resource (ByteArrayResource)
        Resource resource = new ByteArrayResource(fileContent);

        //Create MultipartFile from Resource
        MultipartFile multipartFile = new MultipartFile() {
            @Override
            public String getName() {
                return file.getName();
            }

            @Override
            public String getOriginalFilename() {
                return file.getName();
            }

            @Override
            public String getContentType() {
                return null;  //You can set appropriate content type here
            }

            @Override
            public boolean isEmpty() {
                return fileContent.length == 0;
            }

            @Override
            public long getSize() {
                return fileContent.length;
            }

            @Override
            public byte[] getBytes() throws IOException {
                return fileContent;
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return resource.getInputStream();
            }

            @Override
            public void transferTo(File dest) throws IOException, IllegalStateException {

                Files.write(dest.toPath(), fileContent);

            }
        };
        return multipartFile;
    }
}
