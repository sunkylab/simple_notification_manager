package com.interswitch.notification.application.sms.dto;


import lombok.Data;

@Data
public class SmsResponseDTO {

    private String responseCode;
    private String responseDescription;

    public SmsResponseDTO(String responseCode, String responseDescription) {
        this.responseCode = responseCode;
        this.responseDescription = responseDescription;
    }
}
