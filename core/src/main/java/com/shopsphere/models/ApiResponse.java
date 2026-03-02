package com.shopsphere.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private int statusCode;
    private String errorCode;
}
