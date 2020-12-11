package com.storyhasyou.kratos.result;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author 方曦 created by 2020/12/11
 */
public class ResultTest {

    @Test
    public void data() {

        UserDTO userDTO = new UserDTO();
        userDTO.setAge(1);
        userDTO.setAmount("111");
        userDTO.setUsername("hehe");
        Result<UserDTO> userDTOResult = Result.ok(userDTO);
        User user1 = userDTOResult.data(dto -> {
            User user = new User();
            user.setAge(dto.getAge());
            return user;
        });
        System.out.println(user1);

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