package com.interswitch.notification.application.notification.dto;

import com.interswitch.notification.api.notification.dto.EmailRequestDTO;
import com.interswitch.notification.api.notification.dto.SmsRequestDTO;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Data
public class NotificationDTO extends AbstractNotificationDTO {

    private long id;
    private String title;
    @NotEmpty
    private String recipient;
    private Date createdOn = null;
    private Date deliveredOn = null;
    private String  status;
    private String  type;

    public NotificationDTO() {
    }

    public NotificationDTO(SmsRequestDTO smsRequestDTO) {

        this.setRequestRef(smsRequestDTO.getRequestRef());
        this.setMessage(smsRequestDTO.getMessage());
        this.setType("SMS");
        this.setRecipient(smsRequestDTO.getPhoneNumber());

    }

    public NotificationDTO(EmailRequestDTO emailRequestDTO) {

        this.setRequestRef(emailRequestDTO.getRequestRef());
        this.setMessage(emailRequestDTO.getMessage());
        this.setType("EMAIL");
        this.setRecipient(emailRequestDTO.getEmail());
        this.setTitle(emailRequestDTO.getTitle());

    }
}
