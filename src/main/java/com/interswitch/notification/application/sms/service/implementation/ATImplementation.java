package com.interswitch.notification.application.sms.service.implementation;


import com.interswitch.notification.application.sms.dto.SmsRequestDTO;
import com.interswitch.notification.application.sms.dto.SmsResponseDTO;
import com.interswitch.notification.application.sms.service.SmsService;
import com.interswitch.notification.core.utility.HttpCustomClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


@Service
public class ATImplementation implements SmsService {

    private static final Logger logger = LoggerFactory.getLogger(ATImplementation.class);



    @Value("${sms.host}")
    private String host;

    @Value("${sms.path}")
    private String path;

    @Value("${sms.user}")
    private String user;

    @Value("${sms.password}")
    private String password;

    @Value("${sms.smsFrom}")
    private String smsFrom;

    HttpCustomClient customClient;



    @Override
    public SmsResponseDTO sendSMS(SmsRequestDTO requestDTO) {

        String message = "";
        try {
            message =URLEncoder.encode(requestDTO.getMessage(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String fullUrl= host+"/"+path+"?"+String.format("u=%s&p=%s&s=%s&r=t&f=f&d=%s&t=%s",user,
                password,smsFrom,
                requestDTO.getMobileNumber(),
                message);


        customClient = new HttpCustomClient(fullUrl,null,"GET",null);

        String smsResponse = customClient.makeHttpRequest();

        if(smsResponse.contains("POSTED:FALSE")){
            return new SmsResponseDTO("96","failed");
        }else{
            return new SmsResponseDTO("00","success");
        }


    }


}
