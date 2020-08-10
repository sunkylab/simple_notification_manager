package com.interswitch.notification;


import com.interswitch.notification.application.notification.dto.NotificationDTO;
import com.interswitch.notification.application.notification.entity.MessageStatus;
import com.interswitch.notification.application.notification.service.NotificationService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class ApplicationTests {

    @Autowired
    NotificationService notificationService;

    @Test
    public void whenSmsNotificationIsSent() {
        // given
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setMessage("Hi Ola");
        notificationDTO.setRecipient("2348169141091");
        notificationDTO.setType("SMS");
        notificationDTO.setRequestRef("r-001");
        // when
        notificationDTO = notificationService.sendNotification(notificationDTO);
        // then
        Assert.assertTrue("Notification would has been sent ",notificationDTO.getStatus().equals(MessageStatus.DELIVERED));

    }

    @Test
    public void whenEmailNotificationIsSent() {
        // given

        // when

        // then

    }

    @Test
    public void whenNotificationFails() {
        // given

        // when

        // then

    }

    @Test
    public void whenFailedNotificationIsRePushed() {
        // given

        // when

        // then

    }

    @Test
    public void whenDeliveredNotificationIsRePushed() {
        // given

        // when

        // then

    }

    @Test
    public void whenInvalidRequestRefIsUsed() {
        // given

        // when

        // then

    }

    @Test
    public void whenValidRequestRefIsUsed() {
        // given

        // when

        // then
    }


    @Test
    public void whenNotificationIsFetchedWithFilter() {
        // given

        // when

        // then
    }

    @Test
    public void whenNotificationIsFetchedWithoutFilter() {
        // given

        // when

        // then

    }

}
