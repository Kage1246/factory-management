package edu.hust.hespi.factorymanagement.model.response;

import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CustomResponse<T> implements Serializable {
    private String timestamp;
    private int code;
    private String message;
    private T data;

    public CustomResponse() {
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
    }

    public CustomResponse<T> success(T data) {
        this.code = HttpStatus.OK.value();
        this.message = "Successful!";
        this.data = data;
        return this;
    }
    public CustomResponse<T> success() {
        this.code = HttpStatus.OK.value();
        this.message = "Successful!";
        return this;
    }
    public CustomResponse<T> error(int code, String message) {
        this.code = code;
        this.message = message;
        return this;
    }
    public CustomResponse<T> error(int code, String message, T data) {
        this.data = data;
        this.code = code;
        this.message = message;
        return this;
    }
    public CustomResponse<T> data(T data) {
        this.data = data;
        return this;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
