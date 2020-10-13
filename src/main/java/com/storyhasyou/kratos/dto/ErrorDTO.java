package com.storyhasyou.kratos.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * The type Error dto.
 *
 * @author fangxi
 */
@Data
public class ErrorDTO implements Serializable {

    private String message;
    private Integer code;
    private String path;
    private LocalDateTime timestamp;
    private HttpStatus httpStatus;

    /**
     * Is ok boolean.
     *
     * @return the boolean
     */
    @JsonIgnore
    public boolean isOk() {
        return httpStatus == null || httpStatus.is2xxSuccessful();
    }

    /**
     * Is error boolean.
     *
     * @return the boolean
     */
    @JsonIgnore
    public boolean isError() {
        return httpStatus.isError();
    }

}
