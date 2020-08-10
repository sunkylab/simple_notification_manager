package com.interswitch.notification.application.notification.entity;


import com.interswitch.notification.core.entity.AbstractEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Date;

@Entity
@Data
public class Notification extends AbstractEntity {

    private String message;
    private String title;
    private String sender;
    private String recipient;
    private Date deliveredOn;

    //this would also make querying faster
    @Column(unique = true)
    private String requestRef;

    @Enumerated(EnumType.STRING)
    private MessageStatus status;

    @Enumerated(EnumType.STRING)
    private MessageType type;


}
