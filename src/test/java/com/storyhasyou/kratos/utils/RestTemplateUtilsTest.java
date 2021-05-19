package com.storyhasyou.kratos.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.storyhasyou.kratos.dto.ArticleListResponseVO;
import com.storyhasyou.kratos.dto.BusinessConfigRequestVO;
import com.storyhasyou.kratos.dto.BusinessConfigResponseVO;
import com.storyhasyou.kratos.dto.PageRequest;
import com.storyhasyou.kratos.dto.PageResponse;
import com.storyhasyou.kratos.dto.ResponseResult;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * @author fangxi created by 2021/5/19
 */
public class RestTemplateUtilsTest {

    @Test
    public void get() {
        PageRequest pageRequest = new PageRequest(1, 10);
        String result = RestTemplateUtils.get("http://172.29.30.154:8002/article/page", pageRequest);
        ResponseResult<PageResponse<ArticleListResponseVO>> responseResult = JacksonUtils.parse(result, new TypeReference<ResponseResult<PageResponse<ArticleListResponseVO>>>() {
        });
        System.out.println(responseResult);
    }

    @Test
    public void testGet() {
    }

    @Test
    public void testGet1() {
    }

    @Test
    public void post() {
        String requestBody = "[\n" +
                "    {\n" +
                "        \"terminalId\": 20171226009,\n" +
                "        \"appVersion\": \"4.12G\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"terminalId\": 92034760486,\n" +
                "        \"appVersion\": \"4.15G\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"terminalId\": 14838944569,\n" +
                "        \"mpu\": \"1\",\n" +
                "        \"mcu\": \"2\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"terminalId\": 14838944635,\n" +
                "        \"mpu\": \"1\",\n" +
                "        \"mcu\": \"2\"\n" +
                "    }\n" +
                "]";
        List<BusinessConfigRequestVO> requestVOList = JacksonUtils.parseList(requestBody, BusinessConfigRequestVO.class);
        String result = RestTemplateUtils.post("http://172.29.30.154:8001/business/config/verfiy", requestVOList);
        System.out.println(result);
    }

    @Test
    public void testPost() {
    }

    @Test
    public void testPost1() {
    }

    @Test
    public void exchange() {
    }
}