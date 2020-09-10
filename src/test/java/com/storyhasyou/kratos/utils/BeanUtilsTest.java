package com.storyhasyou.kratos.utils;

import cn.hutool.core.lang.Console;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author fangxi
 * @date 2020/3/2
 */
public class BeanUtilsTest {

    @Test
    public void copyProperties() {
        User user = new User();
        user.setUsername("fangxi");
        user.setAmount(BigDecimal.TEN);
        user.setAge(25);
        user.setCreateTime(LocalDateTime.now());
        UserDTO userDTO = BeanUtils.copyProperties(user, UserDTO.class);
        System.out.println(userDTO);
    }

    @Test
    public void test() {
        System.out.println(TimeUnit.DAYS.toMillis(1));
    }

    @Test
    public void describe() {
        User user = new User();
        user.setUsername("fangxi");
        user.setAge(25);
        Map<String, Object> describe = BeanUtils.describe(user);
        Console.log(describe);
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
