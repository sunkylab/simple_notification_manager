package com.interswitch.notification.application.sms.service;

import com.interswitch.notification.application.sms.dto.SmsRequestDTO;
import com.interswitch.notification.application.sms.dto.SmsResponseDTO;
import com.interswitch.notification.core.exceptions.AppBaseException;

public interface SmsService {


    SmsResponseDTO sendSMS(SmsRequestDTO requestDTO) throws AppBaseException;

}
