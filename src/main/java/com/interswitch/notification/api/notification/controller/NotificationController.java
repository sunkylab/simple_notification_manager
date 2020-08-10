package com.interswitch.notification.api.notification.controller;


import com.interswitch.notification.api.APIResponse;
import com.interswitch.notification.api.notification.dto.EmailRequestDTO;
import com.interswitch.notification.api.notification.dto.NotificationFilterDTO;
import com.interswitch.notification.api.notification.dto.NotificationFilterResponseDTO;
import com.interswitch.notification.api.notification.dto.SmsRequestDTO;
import com.interswitch.notification.application.notification.dto.NotificationDTO;
import com.interswitch.notification.application.notification.service.NotificationService;
import com.interswitch.notification.core.exceptions.AppBaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RequestMapping("/api/v1/notifications")
@RestController
public class NotificationController {

    private static Logger logger = LoggerFactory.getLogger(NotificationController.class);

    @Autowired
    NotificationService notificationService;



    @PostMapping("/send/sms")
    public ResponseEntity<APIResponse> sendEmailNotification(@Valid @RequestBody SmsRequestDTO smsRequestDTO) {

        APIResponse apiResponse = new APIResponse();
        ResponseEntity<APIResponse> responseEntity;

        try{

            NotificationDTO notificationDTO = new NotificationDTO(smsRequestDTO);

            notificationDTO = notificationService.sendNotification(notificationDTO);

            apiResponse.setData(notificationDTO);
            apiResponse.setCode("00");
            apiResponse.setMessage("success");
            responseEntity = new ResponseEntity(apiResponse, HttpStatus.OK);

        }catch (AppBaseException e){
            logger.error("application exception:{}",e.getMessage());
            apiResponse.setMessage(e.getMessage());
            apiResponse.setCode("96");
            responseEntity =  new ResponseEntity(apiResponse, HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            e.printStackTrace();
            apiResponse.setMessage("System Error Occurred");
            apiResponse.setCode("96");
            responseEntity =  new ResponseEntity(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return responseEntity;

    }

    @PostMapping("/send/email")
    public ResponseEntity<APIResponse> sendNotification(@Valid @RequestBody EmailRequestDTO emailRequestDTO) {

        APIResponse apiResponse = new APIResponse();
        ResponseEntity<APIResponse> responseEntity;

        try{

            NotificationDTO dto = new NotificationDTO(emailRequestDTO);

            dto = notificationService.sendNotification(dto);

            apiResponse.setData(dto);
            apiResponse.setMessage("success");
            apiResponse.setCode("00");
            responseEntity = new ResponseEntity(apiResponse, HttpStatus.OK);

        }catch (AppBaseException e){
            logger.error("application exception:{}",e.getMessage());
            apiResponse.setMessage(e.getMessage());
            apiResponse.setCode("96");
            responseEntity =  new ResponseEntity(apiResponse, HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            e.printStackTrace();
            apiResponse.setMessage("System Error Occurred");
            apiResponse.setCode("96");
            responseEntity =  new ResponseEntity(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return responseEntity;

    }

    @GetMapping("/{requestRef}")
    public ResponseEntity<APIResponse> getNotification(@PathVariable("requestRef") String requestRef) {

        APIResponse apiResponse = new APIResponse();
        ResponseEntity<APIResponse> responseEntity;

        try{
            NotificationDTO dto = notificationService.getNotificationByRef(requestRef);
            apiResponse.setData(dto);
            apiResponse.setCode("00");
            apiResponse.setMessage("success");
            responseEntity = new ResponseEntity(apiResponse, HttpStatus.OK);

        }catch (AppBaseException e){
            logger.error("application exception:{}",e.getMessage());
            apiResponse.setMessage(e.getMessage());
            apiResponse.setCode("96");
            responseEntity =  new ResponseEntity(apiResponse, HttpStatus.BAD_REQUEST);
        }catch (Exception e){

            e.printStackTrace();
            apiResponse.setMessage("System Error Occurred");
            apiResponse.setCode("96");
            responseEntity =  new ResponseEntity(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return responseEntity;

    }

    @PostMapping("/{requestRef}/re_push")
    public ResponseEntity<APIResponse> rePushNotification(@PathVariable("requestRef") String requestRef) {

        APIResponse apiResponse = new APIResponse();
        ResponseEntity<APIResponse> responseEntity;

        try{
            NotificationDTO dto = notificationService.resendNotification(requestRef);
            apiResponse.setData(dto);
            apiResponse.setCode("00");
            apiResponse.setMessage("success");
            responseEntity = new ResponseEntity(apiResponse, HttpStatus.OK);

        }catch (AppBaseException e){
            logger.error("application exception:{}",e.getMessage());
            apiResponse.setMessage(e.getMessage());
            apiResponse.setCode("96");
            responseEntity =  new ResponseEntity(apiResponse, HttpStatus.BAD_REQUEST);
        }catch (Exception e){

            e.printStackTrace();
            apiResponse.setMessage("System Error Occurred");
            apiResponse.setCode("96");
            responseEntity =  new ResponseEntity(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return responseEntity;

    }

    @PostMapping("/stats")
    public ResponseEntity<APIResponse> getReport(@RequestBody NotificationFilterDTO filterDTO) {

        APIResponse apiResponse = new APIResponse();
        ResponseEntity<APIResponse> responseEntity;

        try{

            Page<NotificationDTO> notificationDTOS = notificationService.getNotifications(filterDTO);

            NotificationFilterResponseDTO filterResponseDTO = new NotificationFilterResponseDTO(notificationDTOS);

            apiResponse.setData(filterResponseDTO);
            apiResponse.setCode("00");
            apiResponse.setMessage("success");

            responseEntity = new ResponseEntity(apiResponse, HttpStatus.OK);

        }catch (AppBaseException e){
            logger.error("application exception:{}",e.getMessage());
            apiResponse.setMessage(e.getMessage());
            apiResponse.setCode("96");
            responseEntity =  new ResponseEntity(apiResponse, HttpStatus.BAD_REQUEST);
        }catch (Exception e){

            e.printStackTrace();
            apiResponse.setMessage("System Error Occurred");
            apiResponse.setCode("96");
            responseEntity =  new ResponseEntity(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return responseEntity;

    }


}
