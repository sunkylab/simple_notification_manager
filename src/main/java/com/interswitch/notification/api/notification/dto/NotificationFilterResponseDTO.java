package com.interswitch.notification.api.notification.dto;

import com.interswitch.notification.application.notification.dto.NotificationDTO;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
public class NotificationFilterResponseDTO {

    private int size;
    private int page;
    private boolean hasNextPage;
    private long total;
    Object data;

    public NotificationFilterResponseDTO(Page<NotificationDTO> dto) {
        this.setSize(dto.getPageable().getPageSize());
        this.setPage(dto.getPageable().getPageNumber());
        this.setTotal(dto.getTotalElements());
        this.setHasNextPage(dto.hasNext());
        this.setData(dto.getContent());
    }
}

