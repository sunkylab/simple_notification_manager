package com.interswitch.notification;


import com.interswitch.notification.api.notification.dto.NotificationFilterDTO;
import com.interswitch.notification.application.notification.dto.NotificationDTO;
import com.interswitch.notification.application.notification.entity.MessageStatus;
import com.interswitch.notification.application.notification.entity.MessageType;
import com.interswitch.notification.application.notification.entity.Notification;
import com.interswitch.notification.application.notification.repository.NotificationRepo;
import com.interswitch.notification.application.notification.service.NotificationService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Locale;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class ApplicationTests {

    @Autowired
    NotificationService notificationService;

    //do not do this at home
    @Autowired
    NotificationRepo notificationRepo;

    @Autowired
    private MessageSource messageSource;

    private Locale locale = LocaleContextHolder.getLocale();

    private String phoneRecipient = "2348169141091";
    private String emailRecipient = "owolabi.sunday@yahoo.com";

    @Test
    public void whenSmsNotificationIsSent() {
        // given
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setMessage("Hi Ola");
        notificationDTO.setRecipient(phoneRecipient);
        notificationDTO.setType("SMS");
        notificationDTO.setRequestRef("r-001");
        // when
        notificationDTO = notificationService.sendNotification(notificationDTO);
        // then
        Assert.assertTrue("SMS Notification would have status DELIVERED",notificationDTO.getStatus().equals(MessageStatus.PENDING.name()));

    }

    @Test
    public void whenEmailNotificationIsSent() {
        // given
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setTitle("Test Email");
        notificationDTO.setMessage("Hi Ola");
        notificationDTO.setRecipient(emailRecipient);
        notificationDTO.setType("EMAIL");
        notificationDTO.setRequestRef("r-002");
        // when
        notificationDTO = notificationService.sendNotification(notificationDTO);
        // then
        Assert.assertTrue("Notification would have status PENDING ",notificationDTO.getStatus().equals(MessageStatus.PENDING.name()));
        notificationDTO = notificationService.getNotificationByRef("r-002");
        Assert.assertTrue("Email Notification would have status DELIVERED",notificationDTO.getStatus().equals(MessageStatus.DELIVERED.name()));


    }

    @Test
    public void whenNotificationFails() {
        // given
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setMessage("Hi Ola");
        //set invalid phone number
        notificationDTO.setRecipient("");
        notificationDTO.setType("SMS");
        notificationDTO.setRequestRef("r-003");
        // when
        notificationService.sendNotification(notificationDTO);
        notificationDTO = notificationService.getNotificationByRef("r-003");
        // then
        Assert.assertTrue("Notification would have status FAILED ",notificationDTO.getStatus().equals(MessageStatus.FAILED.name()));
    }

    @Test
    public void whenFailedNotificationIsRePushed() {
        // given
        Notification notification = new Notification();
        notification.setRecipient(phoneRecipient);
        notification.setMessage("Welcome man");
        notification.setRequestRef("r-0909");
        notification.setStatus(MessageStatus.FAILED);
        notification.setType(MessageType.SMS);
        notificationRepo.save(notification);
        // when
        notificationService.resendNotification("r-0909");
        NotificationDTO dto = notificationService.getNotificationByRef("r-0909");
        // then
        Assert.assertTrue("Notification would have status DELIVERED ",dto.getStatus().equals(MessageStatus.DELIVERED.name()));
    }

    @Test
    public void whenInvalidNotificationTypeIsUsed() {
        // given
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setType("TELEGRAM");
        notificationDTO.setRequestRef("r-004");
        // when
        try{
            notificationService.sendNotification(notificationDTO);
        }catch(Exception e){
            String err = messageSource.getMessage("notification.invalid.type", null, locale);
            // then
            Assert.assertTrue("Notification would throw exception",err.equals(e.getMessage()));
        }
    }

    @Test
    public void whenDuplicateRefIsUsed() {
        // given
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setRequestRef("r-001");
        // when
        notificationDTO = notificationService.sendNotification(notificationDTO);
        // then
        Assert.assertTrue("Notification would have been sent ",notificationDTO.getStatus().equals(MessageStatus.DELIVERED));
    }

    @Test
    public void whenInvalidRequestRefIsFetched() {
        // given
        String requestRef = "r-0000001";
        // when
        NotificationDTO notificationDTO = notificationService.getNotificationByRef(requestRef);
        // then
        Assert.assertTrue("Notification would have been sent ",notificationDTO.getStatus().equals(MessageStatus.DELIVERED));
    }

    @Test
    public void whenValidRequestRefIsFetched() {
        // given
        String requestRef = "r-001";
        // when
        NotificationDTO notificationDTO = notificationService.getNotificationByRef(requestRef);
        // then
        Assert.assertTrue("Notification would have been sent ",notificationDTO.getStatus().equals(MessageStatus.DELIVERED));
    }


    @Test
    public void whenNotificationIsFetchedWithFilter() {
        // given
        NotificationFilterDTO filterDTO = new NotificationFilterDTO();
        filterDTO.setPage(0);
        filterDTO.setPage(5);
        // when
        Page<NotificationDTO> notificationDTOS =  notificationService.getNotifications(filterDTO);
        // then
        Assert.assertTrue("Page size is always less than or equal to specified size",notificationDTOS.getContent().size() <= 5);
    }

    @Test
    public void whenNotificationIsFetchedWithoutFilter() {
        // given
        NotificationFilterDTO filterDTO = new NotificationFilterDTO();
        // when
        Page<NotificationDTO> notificationDTOS =  notificationService.getNotifications(filterDTO);
        // then
        Assert.assertTrue("Default Page size is 10 ",notificationDTOS.getPageable().getPageSize() == 10);

    }

}
