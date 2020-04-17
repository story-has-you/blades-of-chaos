package com.fxipp.kratos.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

/**
 * @author fangxi
 * @date 2020/4/17
 * @since 1.0.0
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

}
