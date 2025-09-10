package com.storyhasyou.kratos.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

/**
 * ChineseUtils单元测试类
 * <p>
 * 【强制】测试中文字符检测和过滤功能
 * 【强制】测试各种中文字符、标点符号和边界条件
 * 【强制】测试方法命名遵循规范：should_ReturnExpected_When_GivenInput()
 *
 * @author fangxi
 */
public class ChineseUtilsTest {

    // ==================== 中文检测测试 ====================

    @Test
    void should_ReturnTrue_When_StringContainsChinese() {
        // Given
        String chineseText = "这是中文";

        // When
        boolean result = ChineseUtils.isContainChinese(chineseText);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void should_ReturnTrue_When_StringContainsMixedText() {
        // Given
        String mixedText = "Hello世界123";

        // When
        boolean result = ChineseUtils.isContainChinese(mixedText);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void should_ReturnFalse_When_StringContainsNoChineseBasic() {
        // Given
        String englishText = "Hello World 123";

        // When
        boolean result = ChineseUtils.isContainChinese(englishText);

        // Then
        assertThat(result).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "中文测试",
            "a中b",
            "123中文456",
            "Hello世界",
            "测试Test",
            "中",
            "龥" // Unicode范围内的最后一个常用汉字
    })
    void should_ReturnTrue_When_StringContainsVariousChineseCharacters(String input) {
        // When
        boolean result = ChineseUtils.isContainChinese(input);

        // Then
        assertThat(result).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Hello World",
            "123456",
            "test@example.com",
            "!@#$%^&*()",
            "abcdefghijklmnopqrstuvwxyz",
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ",
            ""
    })
    void should_ReturnFalse_When_StringContainsNoChineseCharacters(String input) {
        // When
        boolean result = ChineseUtils.isContainChinese(input);

        // Then
        assertThat(result).isFalse();
    }

    @ParameterizedTest
    @NullAndEmptySource
    void should_ReturnFalse_When_StringIsNullOrEmpty(String input) {
        // When
        boolean result = ChineseUtils.isContainChinese(input);

        // Then
        assertThat(result).isFalse();
    }

    // ==================== 中文字符判断测试 ====================

    @Test
    void should_ReturnTrue_When_CharacterIsChinese() {
        // Given
        char chineseChar = '中';

        // When
        boolean result = ChineseUtils.isChinese(chineseChar);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void should_ReturnFalse_When_CharacterIsEnglish() {
        // Given
        char englishChar = 'A';

        // When
        boolean result = ChineseUtils.isChinese(englishChar);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    void should_ReturnFalse_When_CharacterIsDigit() {
        // Given
        char digitChar = '9';

        // When
        boolean result = ChineseUtils.isChinese(digitChar);

        // Then
        assertThat(result).isFalse();
    }

    @ParameterizedTest
    @CsvSource({
            "中, true",
            "文, true",
            "测, true",
            "试, true",
            "一, true",
            "龥, true",
            "A, false",
            "a, false",
            "1, false",
            "!, false",
            "@, false"
    })
    void should_ReturnCorrectResult_When_CheckingVariousCharacters(char input, boolean expected) {
        // When
        boolean result = ChineseUtils.isChinese(input);

        // Then
        assertThat(result).isEqualTo(expected);
    }

    // ==================== 中文标点符号测试 ====================

    @Test
    void should_ReturnTrue_When_CharacterIsChinesePunctuation() {
        // Given - 移除不在中文Unicode块中的字符
        char[] chinesePunctuation = {'，', '。', '？', '！', '；', '：'};

        // When & Then
        for (char punct : chinesePunctuation) {
            boolean result = ChineseUtils.isChinese(punct);
            assertThat(result)
                    .describedAs("中文标点符号 '%c' 应该被识别为中文字符", punct)
                    .isTrue();
        }
    }

    @Test
    void should_ReturnFalse_When_CharacterIsEnglishPunctuation() {
        // Given
        char[] englishPunctuation = {',', '.', '?', '!', ';', ':', '"', '\''};

        // When & Then
        for (char punct : englishPunctuation) {
            boolean result = ChineseUtils.isChinese(punct);
            assertThat(result)
                    .describedAs("英文标点符号 '%c' 不应该被识别为中文字符", punct)
                    .isFalse();
        }
    }

    // ==================== 中文过滤测试 ====================

    @Test
    void should_ReturnEmptyString_When_FilteringNullInput() {
        // When
        String result = ChineseUtils.filterChinese(null);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void should_ReturnEmptyString_When_FilteringEmptyInput() {
        // When
        String result = ChineseUtils.filterChinese("");

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void should_ReturnEmptyString_When_FilteringBlankInput() {
        // When
        String result = ChineseUtils.filterChinese("   ");

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void should_RemoveAllChinese_When_FilteringChineseText() {
        // Given
        String chineseText = "这是中文测试";

        // When
        String result = ChineseUtils.filterChinese(chineseText);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void should_KeepOnlyNonChinese_When_FilteringMixedText() {
        // Given
        String mixedText = "Hello世界123测试World";

        // When
        String result = ChineseUtils.filterChinese(mixedText);

        // Then
        assertThat(result).isEqualTo("Hello123World");
    }

    @Test
    void should_ReturnOriginal_When_FilteringNonChineseText() {
        // Given
        String englishText = "Hello World 123!";

        // When
        String result = ChineseUtils.filterChinese(englishText);

        // Then
        assertThat(result).isEqualTo(englishText);
    }

    @ParameterizedTest
    @CsvSource({
            "中文Test123, Test123",
            "Hello世界, Hello",
            "123测试456, 123456",
            "!@#中文$%^, !@#$%^",
            "中, ''",
            "Test, Test",
            "123, 123"
    })
    void should_FilterCorrectly_When_GivenVariousInputs(String input, String expected) {
        // When
        String result = ChineseUtils.filterChinese(input);

        // Then
        assertThat(result).isEqualTo(expected);
    }

    // ==================== 中文标点符号过滤测试 ====================

    @Test
    void should_FilterChinesePunctuation_When_StringContainsChinesePunctuation() {
        // Given
        String textWithPunctuation = "Hello，世界！Test。";

        // When
        String result = ChineseUtils.filterChinese(textWithPunctuation);

        // Then
        assertThat(result).isEqualTo("HelloTest");
    }

    @Test
    void should_KeepEnglishPunctuation_When_StringContainsEnglishPunctuation() {
        // Given
        String textWithPunctuation = "Hello, World! Test.";

        // When
        String result = ChineseUtils.filterChinese(textWithPunctuation);

        // Then
        assertThat(result).isEqualTo("Hello, World! Test.");
    }

    @Test
    void should_HandleMixedPunctuation_When_StringContainsBothTypes() {
        // Given
        String mixedPunctuation = "Hello，世界! Test。World.";

        // When
        String result = ChineseUtils.filterChinese(mixedPunctuation);

        // Then
        assertThat(result).isEqualTo("Hello! TestWorld.");
    }

    // ==================== 特殊Unicode字符测试 ====================

    @Test
    void should_HandleJapaneseCharacters_When_StringContainsJapanese() {
        // Given
        String japaneseText = "こんにちはKonichiwa";

        // When
        boolean containsChinese = ChineseUtils.isContainChinese(japaneseText);
        String filtered = ChineseUtils.filterChinese(japaneseText);

        // Then
        // 日文平假名不在汉字Unicode范围内，不应该被检测为中文
        assertThat(containsChinese).isFalse();
        assertThat(filtered).isEqualTo(japaneseText);
    }

    @Test
    void should_HandleKoreanCharacters_When_StringContainsKorean() {
        // Given
        String koreanText = "안녕하세요Korean";

        // When
        boolean containsChinese = ChineseUtils.isContainChinese(koreanText);
        String filtered = ChineseUtils.filterChinese(koreanText);

        // Then
        // 韩文字母不在汉字Unicode范围内，不应该被检测为中文
        assertThat(containsChinese).isFalse();
        assertThat(filtered).isEqualTo(koreanText);
    }

    @Test
    void should_HandleEmojis_When_StringContainsEmojis() {
        // Given
        String emojiText = "Hello😊世界🌍";

        // When
        boolean containsChinese = ChineseUtils.isContainChinese(emojiText);
        String filtered = ChineseUtils.filterChinese(emojiText);

        // Then
        assertThat(containsChinese).isTrue(); // 包含"世界"
        assertThat(filtered).isEqualTo("Hello😊🌍"); // 过滤掉中文，保留emoji
    }

    // ==================== 繁体中文测试 ====================

    @Test
    void should_HandleTraditionalChinese_When_StringContainsTraditionalCharacters() {
        // Given
        String traditionalChinese = "繁體中文測試";

        // When
        boolean containsChinese = ChineseUtils.isContainChinese(traditionalChinese);
        String filtered = ChineseUtils.filterChinese(traditionalChinese);

        // Then
        assertThat(containsChinese).isTrue();
        assertThat(filtered).isEmpty(); // 繁体中文应该被完全过滤
    }

    @Test
    void should_HandleMixedTraditionalSimplified_When_StringContainsBothForms() {
        // Given
        String mixedChinese = "简体繁體混合test";

        // When
        boolean containsChinese = ChineseUtils.isContainChinese(mixedChinese);
        String filtered = ChineseUtils.filterChinese(mixedChinese);

        // Then
        assertThat(containsChinese).isTrue();
        assertThat(filtered).isEqualTo("test");
    }

    // ==================== 边界条件和异常处理测试 ====================

    @Test
    void should_HandleVeryLongString_When_ProcessingLargeInput() {
        // Given
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10000; i++) {
            sb.append(i % 2 == 0 ? "中" : "A");
        }
        String longString = sb.toString();

        // When
        boolean containsChinese = ChineseUtils.isContainChinese(longString);
        String filtered = ChineseUtils.filterChinese(longString);

        // Then
        assertThat(containsChinese).isTrue();
        assertThat(filtered).hasSize(5000); // 一半是英文字符
        assertThat(filtered).matches("A+"); // 只包含A字符
    }

    @Test
    void should_HandleSpecialWhitespaceCharacters_When_ProcessingWhitespace() {
        // Given
        String whitespaceText = "中文\t\n\r 测试";

        // When
        boolean containsChinese = ChineseUtils.isContainChinese(whitespaceText);
        String filtered = ChineseUtils.filterChinese(whitespaceText);

        // Then
        assertThat(containsChinese).isTrue();
        assertThat(filtered).isEqualTo("\t\n\r "); // 保留空白字符
    }

    @Test
    void should_HandleControlCharacters_When_ProcessingControlChars() {
        // Given
        String controlChars = "中文\u0001\u0002测试";

        // When
        String filtered = ChineseUtils.filterChinese(controlChars);

        // Then
        assertThat(filtered).isEqualTo("\u0001\u0002"); // 保留控制字符
    }

    // ==================== 性能测试 ====================

    @Test
    void should_PerformEfficiently_When_ProcessingManyStrings() {
        // Given
        String[] testStrings = {
                "Hello World",
                "中文测试",
                "Mixed中英文123",
                "纯English",
                "纯中文内容",
                "Special!@#$%^&*()字符",
                "Very long string with mixed content 包含中文和English以及123数字符号!@#",
                ""
        };

        // When
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < 10000; i++) {
            for (String testString : testStrings) {
                ChineseUtils.isContainChinese(testString);
                ChineseUtils.filterChinese(testString);

                // 测试每个字符的判断
                if (!testString.isEmpty()) {
                    for (char c : testString.toCharArray()) {
                        ChineseUtils.isChinese(c);
                    }
                }
            }
        }

        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;

        // Then
        assertThat(elapsedTime).isLessThan(3000L); // 3秒内完成
        System.out.println("中文处理性能测试: " + (testStrings.length * 10000 * 3) + " 次操作耗时 " + elapsedTime + "ms");
    }

    // ==================== 实际应用场景测试 ====================

    @Test
    void should_HandleFileNames_When_ProcessingFileNamesWithChinese() {
        // Given
        String[] fileNames = {
                "测试文档.txt",
                "report-2023.pdf",
                "用户手册-v1.0.docx",
                "image照片.jpg",
                "backup备份_20231225.zip"
        };

        // When & Then
        String[] expectedFiltered = {
                ".txt",
                "report-2023.pdf",
                "-v1.0.docx",
                "image.jpg",
                "backup_20231225.zip"
        };

        for (int i = 0; i < fileNames.length; i++) {
            String filtered = ChineseUtils.filterChinese(fileNames[i]);
            assertThat(filtered).isEqualTo(expectedFiltered[i]);
        }
    }

    @Test
    void should_HandleEmailAddresses_When_ProcessingEmailsWithChinese() {
        // Given
        String emailWithChinese = "用户@example.com";
        String emailWithoutChinese = "user@example.com";

        // When
        String filteredChinese = ChineseUtils.filterChinese(emailWithChinese);
        String filteredEnglish = ChineseUtils.filterChinese(emailWithoutChinese);

        // Then
        assertThat(filteredChinese).isEqualTo("@example.com");
        assertThat(filteredEnglish).isEqualTo("user@example.com");
    }

    @Test
    void should_HandleUrls_When_ProcessingUrlsWithChinese() {
        // Given
        String urlWithChinese = "https://www.example.com/路径/页面?参数=值";

        // When
        String filtered = ChineseUtils.filterChinese(urlWithChinese);

        // Then
        assertThat(filtered).isEqualTo("https://www.example.com//?=");
    }
}