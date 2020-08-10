package com.interswitch.notification.api;

import lombok.Data;

@Data
public class APIResponse {

    private String code;
    private String message;
    private Object data;

}
