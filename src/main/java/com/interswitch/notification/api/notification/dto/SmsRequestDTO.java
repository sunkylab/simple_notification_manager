package com.interswitch.notification.api.notification.dto;

import com.interswitch.notification.application.notification.dto.AbstractNotificationDTO;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class SmsRequestDTO extends AbstractNotificationDTO {

    @NotEmpty
    private String phoneNumber;

}
