package com.interswitch.notification.api.notification.dto;

import com.interswitch.notification.application.notification.dto.AbstractNotificationDTO;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class EmailRequestDTO extends AbstractNotificationDTO {

    @NotEmpty
    private String title;
    @NotEmpty
    private String email;

}
