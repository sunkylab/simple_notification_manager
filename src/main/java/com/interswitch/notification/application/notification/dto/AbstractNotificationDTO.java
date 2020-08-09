package com.interswitch.notification.application.notification.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class AbstractNotificationDTO {

    @NotEmpty
    private String requestRef;
    @NotEmpty
    private String message;

}
