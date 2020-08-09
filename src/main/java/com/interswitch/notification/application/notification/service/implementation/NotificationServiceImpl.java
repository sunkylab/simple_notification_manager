package com.interswitch.notification.application.notification.service.implementation;

import com.interswitch.notification.api.notification.dto.NotificationFilterDTO;
import com.interswitch.notification.application.email.dto.MailDTO;
import com.interswitch.notification.application.email.service.MailService;
import com.interswitch.notification.application.notification.dto.NotificationDTO;
import com.interswitch.notification.application.notification.entity.MessageStatus;
import com.interswitch.notification.application.notification.entity.Notification;
import com.interswitch.notification.application.notification.repository.NotificationRepo;
import com.interswitch.notification.application.notification.service.NotificationService;
import com.interswitch.notification.application.sms.dto.SmsRequestDTO;
import com.interswitch.notification.application.sms.dto.SmsResponseDTO;
import com.interswitch.notification.application.sms.service.SmsService;
import com.interswitch.notification.core.exceptions.AppBaseException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class NotificationServiceImpl implements NotificationService {


    @Autowired
    NotificationRepo notificationRepo;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    EntityManager entityManager;

    @Autowired
    MailService mailService;

    @Autowired
    SmsService smsService;



    private static final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);

    @Override
    public NotificationDTO sendNotification(NotificationDTO dto) {

        Notification notification = notificationRepo.findByRequestRef(dto.getRequestRef());
        if(notification !=null ){
            throw new AppBaseException("Duplicate Request");
        }


        notification = modelMapper.map(dto, Notification.class);
        notification.setStatus(MessageStatus.PENDING);
        notification.setRecipient(dto.getRecipient());

        logger.info("Notification is :: {}",notification);

        notification = notificationRepo.save(notification);

        dto.setId(notification.getId());
        dto.setCreatedOn(notification.getCreatedOn());
        dto.setStatus(notification.getStatus().name());


        pushAsyncProcess(dto);


        return dto;
    }

    @Override
    public NotificationDTO getNotificationByRef(String requestRef) {

        Notification notification = notificationRepo.findByRequestRef(requestRef);

        if(notification == null ){
            throw new AppBaseException("Notification not found");
        }
        NotificationDTO dto = modelMapper.map(notification, NotificationDTO.class);

        return dto;
    }

    @Override
    public NotificationDTO resendNotification(String requestRef) throws AppBaseException {

        Notification notification = notificationRepo.findByRequestRef(requestRef);
        if(notification == null ){
            throw new AppBaseException("Invalid Reference Id");
        }

        NotificationDTO dto = modelMapper.map(notification, NotificationDTO.class);

        if(notification.getStatus().equals(MessageStatus.FAILED)){
            pushAsyncProcess(dto);
        }

        return dto;
    }

    @Override
    public void updateNotificationStatus(String requestRef,String status) throws AppBaseException {

        Notification notification = notificationRepo.findByRequestRef(requestRef);


        if(notification==null){
            throw new AppBaseException("Notification record not found");
        }else{
            notification.setStatus(MessageStatus.valueOf(status));
            notificationRepo.save(notification);
        }
    }

    @Override
    public Page<NotificationDTO> getNotifications(NotificationFilterDTO filterDTO) {


        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Notification> q = cb.createQuery(Notification.class);
        Root<Notification> c = q.from(Notification.class);
        List<Predicate> predicates = new ArrayList<>();

        Date startDate;
        Date endDate;

        try {

            if (filterDTO.getDeliveryStatus() != null && filterDTO.getDeliveryStatus()!="") {
                predicates.add((cb.equal(c.get("status"), MessageStatus.valueOf(filterDTO.getDeliveryStatus()))) );
            }

            if (filterDTO.getStartDate()!=null && filterDTO.getEndDate()!= null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                startDate = dateFormat.parse(filterDTO.getStartDate());
                endDate = dateFormat.parse(filterDTO.getEndDate());
                predicates.add(cb.between(c.get("createdOn"), startDate, endDate) );
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new AppBaseException("System Error Occurred");
        }

        CriteriaQuery<Notification> baseQuery = null;
        CriteriaQuery<Long> qc = cb.createQuery(Long.class).orderBy();
        CriteriaQuery<Long> countQuery;
        if (predicates.size() > 0) {
            Predicate and = cb.and(predicates.toArray(new Predicate[0]));
            baseQuery = q.select(c).where(and).orderBy(cb.desc(c.get("id")));
            countQuery = qc.select(cb.count(qc.from(Notification.class))).where(and);
        } else {
            baseQuery = q.select(c);
            countQuery = qc.select(cb.count(qc.from(Notification.class)));
        }

        Pageable pageable;
        if(filterDTO.getSize() == 0 || filterDTO.getPage() < 0){
            pageable = PageRequest.of(0,10);
        }else{
            pageable = PageRequest.of(filterDTO.getPage(),filterDTO.getSize());
        }


        TypedQuery<Notification> query = entityManager.createQuery(baseQuery);
        Long count = entityManager.createQuery(countQuery).getSingleResult();
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<Notification> notifications = query.getResultList();

        List<NotificationDTO> notificationDTOS = new ArrayList<>();

        notifications.forEach(notification -> {
            notificationDTOS.add(modelMapper.map(notification, NotificationDTO.class));
        });

        return new PageImpl<>(notificationDTOS,pageable,count);

    }

    private void pushAsyncProcess(NotificationDTO notificationDTO){

        Boolean isSuccessful = false;


        if (notificationDTO.getType().equals("SMS")){

            SmsRequestDTO smsRequestDTO = new SmsRequestDTO();
            smsRequestDTO.setMobileNumber(notificationDTO.getRecipient());
            smsRequestDTO.setMessage(notificationDTO.getMessage());
            SmsResponseDTO smsResponseDTO = smsService.sendSMS(smsRequestDTO);
            if(smsResponseDTO.getResponseCode().equals("00")){
                isSuccessful = true;
            }
        }else{
            MailDTO mailDTO = new MailDTO();
            mailDTO.setRecipient(notificationDTO.getRecipient());
            mailDTO.setMailSubject(notificationDTO.getTitle());
            mailDTO.setMailContent(notificationDTO.getMessage());
            try{
                isSuccessful = mailService.sendMail(mailDTO);
            }catch(AppBaseException e){
                isSuccessful=false;
            }
        }


        if(isSuccessful){
            updateNotificationStatus(notificationDTO.getRequestRef(), MessageStatus.DELIVERED.name());
        }else{
            updateNotificationStatus(notificationDTO.getRequestRef(), MessageStatus.FAILED.name());
        }


    }

}
