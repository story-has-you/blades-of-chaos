package com.storyhasyou.kratos.utils;

import com.storyhasyou.kratos.handler.TranslationHandler;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

/**
 * @author 方曦 created by 2020/12/8
 */
@NoArgsConstructor
@Data
public class TranslationHandlerTest {

    @Test
    public void translation() {
        TranslationHandler translationHandler = TranslationHandler.builder().build();
        String translation = translationHandler.translation("hello world");
        System.out.println(translation);
    }
}