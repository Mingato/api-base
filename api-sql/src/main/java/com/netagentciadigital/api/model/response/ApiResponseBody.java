package com.netagentciadigital.api.model.response;

import lombok.Builder;
import lombok.Data;

import java.util.HashMap;

@Data
@Builder
public class ApiResponseBody extends HashMap<String, Object> {

    private String status;
    private String message;
    private Object result;

    public ApiResponseBody(String status, String message, Object result) {
        this.status = status;
        this.message = message;
        this.result = result;


        this.put("status", this.status);
        if (this.message != null) {
            this.put("message", this.message);
        }
        if (this.result != null) {
            this.put("result", this.result);
        }

    }
}
