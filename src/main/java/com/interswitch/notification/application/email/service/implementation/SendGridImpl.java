package com.interswitch.notification.application.email.service.implementation;


import com.interswitch.notification.application.email.dto.MailDTO;
import com.interswitch.notification.application.email.service.MailService;
import com.interswitch.notification.core.exceptions.AppBaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;


@Service
public class SendGridImpl  implements MailService {


    @Value("${mail.from}")
    private String mailFrom ;

    @Autowired
    public JavaMailSender javaMailSender;


    private Logger logger = LoggerFactory.getLogger(this.getClass());



    @Override
    public Boolean sendMail(MailDTO mailDTO) throws AppBaseException {

        boolean isSuccessful;
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom(mailFrom);
            messageHelper.setTo(mailDTO.getRecipient());
            messageHelper.setSubject(mailDTO.getMailSubject());
            messageHelper.setText(mailDTO.getMailContent(),false);
        };

        try {
            javaMailSender.send(messagePreparator);
            logger.info(" mail sent to -> {} ",mailDTO.getRecipient());
            isSuccessful = true;
        } catch (MailException e) {
            logger.error("Error sending email", e);
            isSuccessful = false;
        }

        return isSuccessful;
    }

}
