package com.inventory_system.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StandardResponse implements Serializable {

    private static final long serialVersionUID = 8589020361320897371L;
    private Object info;
    private String token;
    private Integer code;
    private String error;

    public static StandardResponse createResponse(Object info, String token) {
        return new StandardResponse(info, token, null, null);
    }
}
