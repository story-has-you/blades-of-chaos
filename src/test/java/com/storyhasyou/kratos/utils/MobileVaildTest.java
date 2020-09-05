package com.storyhasyou.kratos.utils;

import com.storyhasyou.kratos.annotation.Mobile;
import com.storyhasyou.kratos.annotation.Sensitive;
import com.storyhasyou.kratos.enums.SensitiveTypeEnum;
import com.storyhasyou.kratos.valid.BeanValidator;
import lombok.Data;
import org.junit.jupiter.api.Test;

import javax.validation.constraints.NotBlank;

/**
 * @author fangxi created by 2020/7/31
 */
public class MobileVaildTest {


    @Test
    public void mobile() {
        Student student = new Student();
        student.setUsername("fangxi");
        student.setMobile("15605192570");
        BeanValidator.validate(student);
        System.out.println(JsonUtils.serialize(student));
    }

    @Data
    static class Student {
        @NotBlank(message = "用户名不能为空")
        @Sensitive(SensitiveTypeEnum.NAME)
        private String username;
        @Mobile(message = "手机号码格式不正确")
        @Sensitive(SensitiveTypeEnum.MOBILE_PHONE)
        private String mobile;
    }
}
