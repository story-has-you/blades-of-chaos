package com.storyhasyou.kratos.utils;

import com.baomidou.mybatisplus.annotation.TableField;
import com.storyhasyou.kratos.base.BaseEntity;
import lombok.Data;
import org.junit.jupiter.api.Test;

/**
 * @author fangxi created by 2020/8/11
 */
public class OkHttpUtilsTest {

    @Test
    public void get() {
        Dict dict = new Dict();
        dict.setCode("1");
        String response = OkHttpUtils.get("http://47.101.214.214:8001//cms-api/dict/page", dict);
        System.out.println(response);
    }

    @Data
    public static class Dict extends BaseEntity {
        /**
         * 编码
         */
        @TableField(value = "`code`")
        private String code;

        /**
         * 名称
         */
        @TableField(value = "`name`")
        private String name;
    }

}
