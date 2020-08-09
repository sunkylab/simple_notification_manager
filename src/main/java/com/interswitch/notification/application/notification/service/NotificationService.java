package com.interswitch.notification.application.notification.service;

import com.interswitch.notification.api.notification.dto.NotificationFilterDTO;
import com.interswitch.notification.application.notification.dto.NotificationDTO;
import com.interswitch.notification.core.exceptions.AppBaseException;
import org.springframework.data.domain.Page;


public interface NotificationService {

    NotificationDTO sendNotification(NotificationDTO dto) throws AppBaseException;

    NotificationDTO getNotificationByRef(String requestRef) throws AppBaseException;

    NotificationDTO resendNotification(String requestRef) throws AppBaseException;

    void updateNotificationStatus(String requestRef, String status) throws AppBaseException;

    Page<NotificationDTO> getNotifications(NotificationFilterDTO filterDTO) throws AppBaseException;


}
