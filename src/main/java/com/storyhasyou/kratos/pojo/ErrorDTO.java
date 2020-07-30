package com.storyhasyou.kratos.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

/**
 * @author fangxi
 */
@Data
public class ErrorDTO {

    private String message;
    private Integer code;
    private String path;
    private LocalDateTime timestamp;
    private HttpStatus httpStatus;

    @JsonIgnore
    public boolean isOk() {
        return httpStatus == null || httpStatus.is2xxSuccessful();
    }

    @JsonIgnore
    public boolean isError() {
        return httpStatus.isError();
    }

}
