package com.storyhasyou.kratos.result;

import com.storyhasyou.kratos.utils.JacksonUtils;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author 方曦 created by 2020/12/11
 */
public class ResultTest {

    @Test
    public void data() {
        Result<Map<String, Object>> result = Result.body()
                .put("name", "fangxi")
                .put("age", 1)
                .build();
        System.out.println(JacksonUtils.serialize(result));
        
    }

    @Data
    static class User {
        private String username;
        private Integer age;
        private BigDecimal amount;
        private LocalDateTime createTime;
    }

    @Data
    static class UserDTO {
        private String username;
        private Integer age;
        private String amount;
        private String createTime;
    }
}