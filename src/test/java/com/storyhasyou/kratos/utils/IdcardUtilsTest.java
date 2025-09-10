package com.storyhasyou.kratos.utils;

import cn.hutool.core.util.IdcardUtil;
import org.junit.jupiter.api.Test;

/**
 * @author fangxi created by 2020/7/31
 */
public class IdcardUtilsTest {

    private static final String ID_CARD = "32012119950722391X";

    @Test
    public void convert15To18() throws Exception {
        System.out.println(IdcardUtil.convert15To18(ID_CARD));
    }

    @Test
    public void isValidCard() throws Exception {
        System.out.println(IdcardUtil.isValidCard(ID_CARD));
    }


    @Test
    public void getBirthDate() throws Exception {
        System.out.println(IdcardUtil.getBirthDate(ID_CARD));
    }

    @Test
    public void getAge() throws Exception {
        System.out.println(IdcardUtil.getAgeByIdCard(ID_CARD));
    }

    @Test
    public void testGetAge() throws Exception {
    }

    @Test
    public void getYear() throws Exception {
    }

    @Test
    public void getMonth() throws Exception {
    }

    @Test
    public void getDay() throws Exception {
    }

    @Test
    public void getGender() throws Exception {
        System.out.println(IdcardUtil.getGenderByIdCard(ID_CARD));
    }

    @Test
    public void getProvince() throws Exception {
        System.out.println(IdcardUtil.getProvinceByIdCard(ID_CARD));
    }
}
