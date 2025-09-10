package com.storyhasyou.kratos.utils;

import com.storyhasyou.kratos.enums.SensitiveTypeEnum;
import com.storyhasyou.kratos.toolkit.StringPool;
import org.apache.commons.lang3.StringUtils;

/**
 * The type Sensitive utils.
 *
 * @author fangxi
 */
public class SensitiveUtils {

    /**
     * [真实姓名] 显示最后四位，其他隐藏。共计18位或者15位。<例子：*************5762>
     *
     * @param realName the real name
     * @return the string
     */
    public static String realName(final String realName) {
        if (StringUtils.isBlank(realName)) {
            return StringPool.EMPTY;
        }
        return dealString(realName, 1, 0);
    }

    /**
     * [身份证号] 显示最后四位，其他隐藏。共计18位或者15位。<例子：*************5762>
     *
     * @param idCard the id card
     * @return the string
     */
    public static String idCard(final String idCard) {
        if (StringUtils.isBlank(idCard)) {
            return StringPool.EMPTY;
        }
        return dealString(idCard, 3, 4);
    }

    /**
     * [手机号] 显示最后四位，其他隐藏。共计18位或者15位。<例子：*************5762>
     *
     * @param idCard the id card
     * @return the string
     */
    public static String mobilePhone(final String idCard) {
        if (StringUtils.isBlank(idCard)) {
            return StringPool.EMPTY;
        }
        return dealString(idCard, 3, 4);
    }

    /**
     * [邮箱] 显示最后四位，其他隐藏。共计18位或者15位。<例子：*************5762>
     *
     * @param email the email
     * @return the string
     */
    public static String email(final String email) {
        if (StringUtils.isBlank(email)) {
            return StringPool.EMPTY;
        }
        int index = email.indexOf("@");
        return dealString(email, 3, email.length() - index);
    }

    /**
     * [账号] 显示最后四位，其他隐藏。共计18位或者15位。<例子：*************5762>
     *
     * @param idCard the id card
     * @return the string
     */
    public static String acctNo(final String idCard) {
        if (StringUtils.isBlank(idCard)) {
            return StringPool.EMPTY;
        }
        final String name = StringUtils.left(idCard, 1);
        return StringUtils.rightPad(name, StringUtils.length(idCard), StringPool.EMPTY);
    }

    /**
     * [密码] 隐藏。<例子：*************>
     *
     * @param password the password
     * @return the string
     */
    public static String password(final String password) {
        if (StringUtils.isBlank(password)) {
            return StringPool.EMPTY;
        }
        return StringPool.ASTERISK;
    }


    private static String dealString(String str, int headOff, int tailOff) {
        int length = str.length();
        StringBuilder sb = new StringBuilder();
        final String head = StringUtils.left(str, headOff);
        String tail = StringUtils.right(str, tailOff);
        sb.append(head);
        int size = length - (headOff + tailOff);
        if (size > 0) {
            while (size > 0) {
                sb.append(StringPool.EMPTY);
                size--;
            }
        }
        sb.append(tail);
        return sb.toString();
    }


    /**
     * 提供给外部进行直接脱敏处理
     *
     * @param type  the type
     * @param value the value
     * @return string
     */
    public static String sensitiveValue(SensitiveTypeEnum type, String value) {
        return switch (type) {
            case NAME -> realName(String.valueOf(value));
            case ID_CARD -> idCard(String.valueOf(value));
            case MOBILE_PHONE -> mobilePhone(String.valueOf(value));
            case EMAIL -> email(String.valueOf(value));
            case ACCOUNT_NO -> acctNo(String.valueOf(value));
            case PASSWORD -> password(String.valueOf(value));
        };

    }


}
