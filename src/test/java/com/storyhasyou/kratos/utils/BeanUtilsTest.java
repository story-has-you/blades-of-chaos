package com.storyhasyou.kratos.utils;

import cn.hutool.core.lang.Console;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.util.StopWatch;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author fangxi
 * @date 2020/3/2
 */
public class BeanUtilsTest {

    @Test
    public void copyProperties() {
        List<User> users = IntStream.range(1, 1000000)
                .mapToObj(operand -> new User("fangxi" + operand, operand, BigDecimal.TEN, DateUtils.now()))
                .collect(Collectors.toList());

        StopWatch stopWatch = new StopWatch();
        stopWatch.start("1");
        for (User user : users) {
            BeanUtils.copyProperties(user, UserDTO.class);
        }
        stopWatch.stop();

        stopWatch.start("2");
        for (User user : users) {
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(user, userDTO);
        }
        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());
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
    @AllArgsConstructor
    @NoArgsConstructor
    static class User {
        private String username;
        private Integer age;
        private BigDecimal amount;
        private LocalDateTime createTime;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class UserDTO {
        private String username;
        private Integer age;
        private BigDecimal amount;
        private LocalDateTime createTime;
    }

}
