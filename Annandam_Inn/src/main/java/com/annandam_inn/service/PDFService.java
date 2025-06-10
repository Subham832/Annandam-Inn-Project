package com.annandam_inn.service;

import com.annandam_inn.dto.BookingDto;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;

@Service
public class PDFService {

    private static final String PDF_DIRECTORY= "/path/to/your/pdf/directory/";

    public boolean generatePDF(String fileName, BookingDto dto){
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(fileName)); //Change that path to the String fileName for fixing the path in a dynamic way.

            document.open();
            Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
//            Chunk chunk = new Chunk("Hello World", font);
            Chunk bookingConfirmation = new Chunk("Booking Confirmation", font);
            Chunk guestName = new Chunk("Guest Name: "+dto.getGuestName(), font); //adding the line in Word Document.
            Chunk price = new Chunk("Price Per Night: "+dto.getPrice(), font);
            Chunk totalPrice = new Chunk("Total Price: "+dto.getTotalPrice(), font);

//          document.add(chunk);//By this it will add the particular line of the code in your Word Document.
            document.add(bookingConfirmation);
            document.add(new Paragraph("\n")); //For Next Line.
            document.add(guestName);
            document.add(new Paragraph("\n")); //For Next Line.
            document.add(price);
            document.add(new Paragraph("\n")); //For Next Line.
            document.add(totalPrice);

            document.close();//When we close it then the document will be save.
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
//            return null;
        }
        return false;
    }

}
