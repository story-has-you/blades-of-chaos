package com.storyhasyou.kratos.exceptions;

import com.storyhasyou.kratos.result.Result;
import com.storyhasyou.kratos.result.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * @author fangxi created by 2020/6/17
 */
@Slf4j
@RestControllerAdvice
public class BusinessExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Result<?> handlerException(Exception ex) {
        Result<?> result = new Result<>();
        // 业务异常
        if (ex instanceof BusinessException) {
            result.setStatus(((BusinessException) ex).getCode());
            result.setMessage(ex.getMessage());
            log.warn("[全局业务异常]\r\n业务编码：{}\r\n异常记录：{}", result.getStatus(), result.getMessage());
        } else if (ex instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException methodArgumentNotValidException = (MethodArgumentNotValidException) ex;
            BindingResult bindingResult = methodArgumentNotValidException.getBindingResult();
            String msg = bindingResult
                    .getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .distinct()
                    .collect(Collectors.joining(","));
            result.setStatus(HttpStatus.BAD_REQUEST.value());
            result.setMessage(msg);
        } else if (ex instanceof BindException) {
            BindException bindException = (BindException) ex;
            String msg = bindException.getBindingResult()
                    .getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .distinct()
                    .collect(Collectors.joining(","));
            result.setStatus(HttpStatus.BAD_REQUEST.value());
            result.setMessage(msg);
        }
        // 未知错误
        else {
            result.setStatus(ResultCode.FAILED.getCode());
            result.setMessage(ex.getMessage());
            log.error("", ex);
        }

        return result;
    }

}