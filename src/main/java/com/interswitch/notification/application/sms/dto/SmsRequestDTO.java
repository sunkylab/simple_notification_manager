package com.interswitch.notification.application.sms.dto;


import lombok.Data;

@Data
public class SmsRequestDTO {

    private String mobileNumber;
    private String message;

    @Override
    public String toString() {
        return "SendSmsRequest{" +
                "mobileNumber='" + mobileNumber + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
