package com.storyhasyou.kratos.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 错误响应数据传输对象
 * <p>
 * 使用 JDK 21 Record 特性重构，提供不可变的错误信息封装。
 * Record 类自动生成 constructor、getter、equals、hashCode 和 toString 方法，
 * 相比传统类具有更简洁的语法和更好的性能表现。
 * </p>
 *
 * @param message    错误消息
 * @param code       错误代码
 * @param path       请求路径
 * @param timestamp  时间戳
 * @param httpStatus HTTP 状态
 * @author fangxi
 * @since JDK 21
 */
public record ErrorDTO(
        String message,
        Integer code,
        String path,
        LocalDateTime timestamp,
        HttpStatus httpStatus
) implements Serializable {

    /**
     * 判断是否为成功状态
     * <p>
     * 当 HTTP 状态为 null 或者是 2xx 成功状态时返回 true
     * </p>
     *
     * @return 是否成功
     */
    @JsonIgnore
    public boolean isOk() {
        return httpStatus == null || httpStatus.is2xxSuccessful();
    }

    /**
     * 判断是否为错误状态
     * <p>
     * 当 HTTP 状态为错误状态（4xx 或 5xx）时返回 true
     * </p>
     *
     * @return 是否错误
     */
    @JsonIgnore
    public boolean isError() {
        return httpStatus != null && httpStatus.isError();
    }
}
