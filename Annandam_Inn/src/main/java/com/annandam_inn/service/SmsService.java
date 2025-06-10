package com.annandam_inn.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SmsService {

    @Value("${twilio.account_sid}")
    private String accountSid; //TWILIO_ACCOUNT_SID
    @Value("${twilio.auth_token}")
    private String authToken; //TWILIO_TOKEN

    @Value("${twilio.phone_number}")
    private String fromPhoneNumber; //TWILIO_PHONE

    public  void sendSms(String toPhoneNumber, String messageBody){

        Twilio.init(accountSid, authToken); //It will perform Login Operation to TWILIO Account.

        Message message = Message.creator
                (
                        new PhoneNumber(toPhoneNumber),
                        new PhoneNumber(fromPhoneNumber),
                        messageBody
                ).create();
        System.out.println("Message sent! SID: " + message.getSid());
    }
}
