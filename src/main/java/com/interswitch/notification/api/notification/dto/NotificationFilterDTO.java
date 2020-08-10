package com.interswitch.notification.api.notification.dto;

import lombok.Data;

@Data
public class NotificationFilterDTO {

    private int size;
    private int page;
    private String deliveryStatus;

    /**Expected Date format is "dd-MM-yyyy"
     * **/
    private String startDate;
    private String endDate;
}

