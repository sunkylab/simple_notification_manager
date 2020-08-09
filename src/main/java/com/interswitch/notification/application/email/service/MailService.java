package com.interswitch.notification.application.email.service;

import com.interswitch.notification.application.email.dto.MailDTO;
import com.interswitch.notification.core.exceptions.AppBaseException;


public interface MailService {


    Boolean  sendMail(MailDTO mailDTO) throws AppBaseException;

}
