package com.learnease.server.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse <T> {
    private boolean success;
    private String message;
    T data;

    public ApiResponse(boolean success, String message){
        this.success = success;
        this.message = message;
    }

    public ApiResponse(boolean success, T data){
        this.success = success;
        this.data = data;
    }
}
