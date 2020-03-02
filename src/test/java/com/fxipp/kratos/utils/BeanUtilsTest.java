package com.fxipp.kratos.utils;

import com.fxipp.kratos.converter.Converters;
import lombok.Data;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
        user.setCreateTime(DateUtils.now());
        UserDTO userDTO = BeanUtils.copyProperties(user, UserDTO.class, Converters.STRING_CONVERTERS);
        System.out.println(userDTO);
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
