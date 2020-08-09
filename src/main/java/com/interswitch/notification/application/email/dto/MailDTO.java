package com.interswitch.notification.application.email.dto;


import lombok.Data;

@Data
public class MailDTO {

    String mailSubject;
    String recipient;
    String mailContent;

}
