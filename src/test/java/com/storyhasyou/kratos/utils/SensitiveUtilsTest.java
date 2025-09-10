package com.storyhasyou.kratos.utils;

import com.storyhasyou.kratos.enums.SensitiveTypeEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.assertj.core.api.Assertions.*;

/**
 * SensitiveUtils单元测试类
 * 
 * 【强制】测试敏感数据脱敏功能，确保数据安全性
 * 【强制】测试各种输入边界条件和异常情况
 * 【强制】测试方法命名遵循规范：should_ReturnExpected_When_GivenInput()
 * 
 * @author fangxi
 */
public class SensitiveUtilsTest {

    // ==================== 真实姓名脱敏测试 ====================

    @Test
    void should_DesensitizeRealName_When_GivenValidName() {
        // Given
        String realName = "张三";
        
        // When
        String result = SensitiveUtils.realName(realName);
        
        // Then
        assertThat(result).isEqualTo("张");
    }

    @Test
    void should_DesensitizeLongRealName_When_GivenLongName() {
        // Given
        String realName = "欧阳修斌";
        
        // When
        String result = SensitiveUtils.realName(realName);
        
        // Then
        assertThat(result).isEqualTo("欧");
    }

    @Test
    void should_DesensitizeSingleCharName_When_GivenSingleChar() {
        // Given
        String realName = "王";
        
        // When
        String result = SensitiveUtils.realName(realName);
        
        // Then
        assertThat(result).isEqualTo("王");
    }

    @ParameterizedTest
    @NullAndEmptySource
    void should_ReturnEmpty_When_RealNameIsNullOrEmpty(String input) {
        // When
        String result = SensitiveUtils.realName(input);
        
        // Then
        assertThat(result).isEmpty();
    }

    // ==================== 身份证号脱敏测试 ====================

    @Test
    void should_DesensitizeIdCard_When_Given18DigitIdCard() {
        // Given
        String idCard = "110101199003076512";
        
        // When
        String result = SensitiveUtils.idCard(idCard);
        
        // Then
        assertThat(result).isEqualTo("110**************6512");
    }

    @Test
    void should_DesensitizeIdCard_When_Given15DigitIdCard() {
        // Given
        String idCard = "110101900307651";
        
        // When
        String result = SensitiveUtils.idCard(idCard);
        
        // Then
        assertThat(result).isEqualTo("110***********7651");
    }

    @ParameterizedTest
    @CsvSource({
        "123456789012345, 123***********345",
        "1234567890123456789, 123***************6789",
        "1234, 123*",
        "12, 12*",
        "1, 1**"
    })
    void should_DesensitizeIdCard_When_GivenVariousLengths(String input, String expected) {
        // When
        String result = SensitiveUtils.idCard(input);
        
        // Then
        assertThat(result).isEqualTo(expected);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void should_ReturnEmpty_When_IdCardIsNullOrEmpty(String input) {
        // When
        String result = SensitiveUtils.idCard(input);
        
        // Then
        assertThat(result).isEmpty();
    }

    // ==================== 手机号脱敏测试 ====================

    @Test
    void should_DesensitizeMobilePhone_When_Given11DigitPhone() {
        // Given
        String mobilePhone = "13812345678";
        
        // When
        String result = SensitiveUtils.mobilePhone(mobilePhone);
        
        // Then
        assertThat(result).isEqualTo("138****5678");
    }

    @Test
    void should_DesensitizeMobilePhone_When_GivenPhoneWithCountryCode() {
        // Given
        String mobilePhone = "+8613812345678";
        
        // When
        String result = SensitiveUtils.mobilePhone(mobilePhone);
        
        // Then
        assertThat(result).isEqualTo("+86*******5678");
    }

    @ParameterizedTest
    @CsvSource({
        "1381234567, 138****567",
        "138123456789, 138*****6789",
        "1381, 138*",
        "138, 138*",
        "13, 13*",
        "1, 1**"
    })
    void should_DesensitizeMobilePhone_When_GivenVariousFormats(String input, String expected) {
        // When
        String result = SensitiveUtils.mobilePhone(input);
        
        // Then
        assertThat(result).isEqualTo(expected);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void should_ReturnEmpty_When_MobilePhoneIsNullOrEmpty(String input) {
        // When
        String result = SensitiveUtils.mobilePhone(input);
        
        // Then
        assertThat(result).isEmpty();
    }

    // ==================== 邮箱脱敏测试 ====================

    @Test
    void should_DesensitizeEmail_When_GivenNormalEmail() {
        // Given
        String email = "zhangsan@example.com";
        
        // When
        String result = SensitiveUtils.email(email);
        
        // Then
        assertThat(result).isEqualTo("zha**@example.com");
    }

    @Test
    void should_DesensitizeEmail_When_GivenShortEmail() {
        // Given
        String email = "a@b.com";
        
        // When
        String result = SensitiveUtils.email(email);
        
        // Then
        assertThat(result).isEqualTo("a**@b.com");
    }

    @Test
    void should_DesensitizeEmail_When_GivenLongEmail() {
        // Given
        String email = "verylongusername@example.com";
        
        // When
        String result = SensitiveUtils.email(email);
        
        // Then
        assertThat(result).isEqualTo("ver***********@example.com");
    }

    @ParameterizedTest
    @CsvSource({
        "test@gmail.com, tes*@gmail.com",
        "user123@company.co.uk, use**********@company.co.uk",
        "a@b.c, a**@b.c"
    })
    void should_DesensitizeEmail_When_GivenVariousEmails(String input, String expected) {
        // When
        String result = SensitiveUtils.email(input);
        
        // Then
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void should_HandleEmailWithoutAtSymbol_When_InvalidEmail() {
        // Given
        String invalidEmail = "notanemail";
        
        // When
        String result = SensitiveUtils.email(invalidEmail);
        
        // Then
        // 没有@符号时，index为-1，会显示完整长度
        assertThat(result).hasSize(invalidEmail.length());
        assertThat(result).startsWith("not");
    }

    @ParameterizedTest
    @NullAndEmptySource
    void should_ReturnEmpty_When_EmailIsNullOrEmpty(String input) {
        // When
        String result = SensitiveUtils.email(input);
        
        // Then
        assertThat(result).isEmpty();
    }

    // ==================== 账号脱敏测试 ====================

    @Test
    void should_DesensitizeAcctNo_When_GivenBankAccount() {
        // Given
        String acctNo = "6212345678901234567";
        
        // When
        String result = SensitiveUtils.acctNo(acctNo);
        
        // Then
        assertThat(result).isEqualTo("6******************");
        assertThat(result).hasSize(acctNo.length());
    }

    @Test
    void should_DesensitizeAcctNo_When_GivenShortAccount() {
        // Given
        String acctNo = "12345";
        
        // When
        String result = SensitiveUtils.acctNo(acctNo);
        
        // Then
        assertThat(result).isEqualTo("1****");
        assertThat(result).hasSize(acctNo.length());
    }

    @Test
    void should_DesensitizeAcctNo_When_GivenSingleChar() {
        // Given
        String acctNo = "1";
        
        // When
        String result = SensitiveUtils.acctNo(acctNo);
        
        // Then
        assertThat(result).isEqualTo("1");
    }

    @ParameterizedTest
    @NullAndEmptySource
    void should_ReturnEmpty_When_AcctNoIsNullOrEmpty(String input) {
        // When
        String result = SensitiveUtils.acctNo(input);
        
        // Then
        assertThat(result).isEmpty();
    }

    // ==================== 密码脱敏测试 ====================

    @Test
    void should_ReturnAsterisk_When_GivenPassword() {
        // Given
        String password = "myPassword123!";
        
        // When
        String result = SensitiveUtils.password(password);
        
        // Then
        assertThat(result).isEqualTo("*");
    }

    @Test
    void should_ReturnAsterisk_When_GivenSimplePassword() {
        // Given
        String password = "123";
        
        // When
        String result = SensitiveUtils.password(password);
        
        // Then
        assertThat(result).isEqualTo("*");
    }

    @Test
    void should_ReturnAsterisk_When_GivenComplexPassword() {
        // Given
        String password = "V3ryC0mpl3x!P@ssw0rd#2023$%^&*()";
        
        // When
        String result = SensitiveUtils.password(password);
        
        // Then
        assertThat(result).isEqualTo("*");
    }

    @ParameterizedTest
    @NullAndEmptySource
    void should_ReturnEmpty_When_PasswordIsNullOrEmpty(String input) {
        // When
        String result = SensitiveUtils.password(input);
        
        // Then
        assertThat(result).isEmpty();
    }

    // ==================== 通用脱敏方法测试 ====================

    @ParameterizedTest
    @EnumSource(SensitiveTypeEnum.class)
    void should_HandleAllSensitiveTypes_When_GivenValidValue(SensitiveTypeEnum type) {
        // Given
        String testValue = "testValue123@example.com";
        
        // When
        String result = SensitiveUtils.sensitiveValue(type, testValue);
        
        // Then
        assertThat(result).isNotNull();
        
        // 验证每种类型都有相应的处理逻辑
        switch (type) {
            case NAME -> assertThat(result).isEqualTo("t");
            case ID_CARD -> assertThat(result).isEqualTo("tes****************.com");
            case MOBILE_PHONE -> assertThat(result).isEqualTo("tes*************e.com");
            case EMAIL -> assertThat(result).isEqualTo("tes**********@example.com");
            case ACCOUNT_NO -> {
                assertThat(result).startsWith("t");
                assertThat(result).hasSize(testValue.length());
            }
            case PASSWORD -> assertThat(result).isEqualTo("*");
        }
    }

    @Test
    void should_DesensitizeName_When_UsingSensitiveValueMethod() {
        // Given
        String name = "李四";
        
        // When
        String result = SensitiveUtils.sensitiveValue(SensitiveTypeEnum.NAME, name);
        
        // Then
        assertThat(result).isEqualTo("李");
    }

    @Test
    void should_DesensitizeIdCard_When_UsingSensitiveValueMethod() {
        // Given
        String idCard = "110101199003076512";
        
        // When
        String result = SensitiveUtils.sensitiveValue(SensitiveTypeEnum.ID_CARD, idCard);
        
        // Then
        assertThat(result).isEqualTo("110**************6512");
    }

    @Test
    void should_DesensitizeMobile_When_UsingSensitiveValueMethod() {
        // Given
        String mobile = "13812345678";
        
        // When
        String result = SensitiveUtils.sensitiveValue(SensitiveTypeEnum.MOBILE_PHONE, mobile);
        
        // Then
        assertThat(result).isEqualTo("138****5678");
    }

    @Test
    void should_DesensitizeEmail_When_UsingSensitiveValueMethod() {
        // Given
        String email = "user@domain.com";
        
        // When
        String result = SensitiveUtils.sensitiveValue(SensitiveTypeEnum.EMAIL, email);
        
        // Then
        assertThat(result).isEqualTo("use*@domain.com");
    }

    @Test
    void should_DesensitizeAccount_When_UsingSensitiveValueMethod() {
        // Given
        String account = "1234567890";
        
        // When
        String result = SensitiveUtils.sensitiveValue(SensitiveTypeEnum.ACCOUNT_NO, account);
        
        // Then
        assertThat(result).isEqualTo("1*********");
        assertThat(result).hasSize(account.length());
    }

    @Test
    void should_DesensitizePassword_When_UsingSensitiveValueMethod() {
        // Given
        String password = "secretPassword";
        
        // When
        String result = SensitiveUtils.sensitiveValue(SensitiveTypeEnum.PASSWORD, password);
        
        // Then
        assertThat(result).isEqualTo("*");
    }

    // ==================== 边界条件和特殊字符测试 ====================

    @Test
    void should_HandleSpecialCharacters_When_GivenSpecialInput() {
        // Given
        String specialName = "张-三";
        String specialIdCard = "110101-1990-03-07-6512";
        String specialPhone = "138-1234-5678";
        
        // When
        String nameResult = SensitiveUtils.realName(specialName);
        String idResult = SensitiveUtils.idCard(specialIdCard);
        String phoneResult = SensitiveUtils.mobilePhone(specialPhone);
        
        // Then
        assertThat(nameResult).isEqualTo("张");
        assertThat(idResult).isEqualTo("110******************6512");
        assertThat(phoneResult).isEqualTo("138***********5678");
    }

    @Test
    void should_HandleUnicodeCharacters_When_GivenUnicodeInput() {
        // Given
        String unicodeName = "🙂张三😊";
        String unicodeEmail = "user🌟@domain.com";
        
        // When
        String nameResult = SensitiveUtils.realName(unicodeName);
        String emailResult = SensitiveUtils.email(unicodeEmail);
        
        // Then
        assertThat(nameResult).isEqualTo("🙂");
        assertThat(emailResult).isEqualTo("use***@domain.com");
    }

    @Test
    void should_HandleEmptyAndWhitespace_When_GivenWhitespaceInput() {
        // Given
        String whitespace = "   ";
        String tab = "\t";
        String newline = "\n";
        
        // When & Then
        // 空白字符应该被视为有效输入而不是空字符串
        assertThat(SensitiveUtils.realName(whitespace)).isEqualTo(" ");
        assertThat(SensitiveUtils.idCard(tab)).isEqualTo("\t**");
        assertThat(SensitiveUtils.mobilePhone(newline)).isEqualTo("\n**");
    }

    // ==================== 性能测试 ====================

    @Test
    void should_PerformEfficiently_When_ProcessingManyInputs() {
        // Given
        String[] testInputs = {
            "张三", "李四", "王五", "赵六",
            "110101199003076512", "110101199003076513", "110101199003076514",
            "13812345678", "13812345679", "13812345680",
            "user@domain.com", "test@example.org", "admin@company.net"
        };
        
        // When
        long startTime = System.currentTimeMillis();
        
        for (int i = 0; i < 10000; i++) {
            for (String input : testInputs) {
                SensitiveUtils.realName(input);
                SensitiveUtils.idCard(input);
                SensitiveUtils.mobilePhone(input);
                SensitiveUtils.email(input);
                SensitiveUtils.acctNo(input);
                SensitiveUtils.password(input);
            }
        }
        
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        
        // Then
        // 10万次脱敏操作应该在合理时间内完成
        assertThat(elapsedTime).isLessThan(5000L); // 5秒内完成
        System.out.println("脱敏性能测试: " + (testInputs.length * 60000) + " 次操作耗时 " + elapsedTime + "ms");
    }
}