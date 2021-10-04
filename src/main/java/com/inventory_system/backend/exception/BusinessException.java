package com.inventory_system.backend.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusinessException extends Exception {
    int errorCode;
    String message;

    public BusinessException(int errorCode, String message){
        super();
        this.errorCode= errorCode;
        this.message= message;
    }
}
