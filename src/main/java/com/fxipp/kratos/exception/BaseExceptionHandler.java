package com.fxipp.kratos.exception;

import com.fxipp.kratos.pojo.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;

/**
 * @author fangxi
 */
@RestControllerAdvice
public class BaseExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(BaseExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public Result<String> handler(Exception e, HttpServletRequest request) {
        return getError(e, request);
    }


    @ExceptionHandler(BaseException.class)
    public Result<String> handler(BaseException e, HttpServletRequest request) {
        Result<String> error = getError(e, request);
        error.setStatus(e.getStatus());
        return error;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<String> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException methodArgumentNotValidException, HttpServletRequest request) {
        return getError(methodArgumentNotValidException, request);
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<String> bindException(BindException bindException, HttpServletRequest request) {
        return getError(bindException, request);
    }

    private Result<String> getError(Exception e, HttpServletRequest request) {
        log.error("", e);
        Result<String> result = new Result<>();
        String message = e.getMessage();
        result.setMsg(message == null ? "系统异常，请联系管理员" : message);
        result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        if (e instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException methodArgumentNotValidException = (MethodArgumentNotValidException) e;
            BindingResult bindingResult = methodArgumentNotValidException.getBindingResult();
            String msg = bindingResult
                    .getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .distinct()
                    .collect(Collectors.joining(","));
            result.setMsg(msg);
            result.setStatus(HttpStatus.BAD_REQUEST.value());

        } else if (e instanceof BindException) {
            BindException bindException = (BindException) e;
            String msg = bindException.getBindingResult()
                    .getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .distinct()
                    .collect(Collectors.joining(","));
            result.setMsg(msg);
            result.setStatus(HttpStatus.BAD_REQUEST.value());
        }

        return result;
    }
}
