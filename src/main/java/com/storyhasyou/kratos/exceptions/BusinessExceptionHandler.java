package com.storyhasyou.kratos.exceptions;

import com.storyhasyou.kratos.result.Result;
import com.storyhasyou.kratos.result.ResultCode;
import com.storyhasyou.kratos.toolkit.StringPool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * The type Business exception handler.
 *
 * @author fangxi created by 2020/6/17
 */
@Slf4j
@RestControllerAdvice
public class BusinessExceptionHandler {


    /**
     * Business exception result.
     *
     * @param ex the ex
     * @return the result
     */
    @ExceptionHandler(BusinessException.class)
    public Result<?> businessException(BusinessException ex) {
        log.error(ex.getMessage(), ex);
        return Result.fail(ex.getCode(), ex.getMessage());
    }

    /**
     * 参数错误
     *
     * @param ex
     * @return
     */
    @ExceptionHandler({IllegalStateException.class, IllegalArgumentException.class})
    public Result<?> illegalException(RuntimeException ex) {
        log.error(ex.getMessage(), ex);
        return Result.fail(ResultCode.FAILURE, ex.getMessage());
    }

    /**
     * RuntimeException result.
     *
     * @param ex the ex
     * @return the result
     */
    @ExceptionHandler(RuntimeException.class)
    public Result<?> runtimeException(RuntimeException ex) {
        log.error(ex.getMessage(), ex);
        return Result.fail(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
    }

    /**
     * Method argument not valid exception result.
     *
     * @param ex the ex
     * @return the result
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        String msg = bindingResult
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .distinct()
                .collect(Collectors.joining(StringPool.COMMA));
        return Result.fail(HttpStatus.BAD_REQUEST.value(), msg);
    }

    /**
     * Bind exception result.
     *
     * @param ex the ex
     * @return the result
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> bindException(BindException ex) {
        String msg = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .distinct()
                .collect(Collectors.joining(StringPool.COMMA));
        return Result.fail(HttpStatus.BAD_REQUEST.value(), msg);
    }


    /**
     * Handler exception result.
     *
     * @param ex the ex
     * @return the result
     */
    @ExceptionHandler(Exception.class)
    public Result<?> handlerException(Exception ex) {
        log.error(ex.getMessage(), ex);
        return Result.fail(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
    }

}
