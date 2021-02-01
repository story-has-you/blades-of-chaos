package com.storyhasyou.kratos.utils;

import com.storyhasyou.kratos.toolkit.StringPool;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;


/**
 * The type Cookie utils.
 *
 * @author fangxi
 */
@Slf4j
public final class CookieUtils {

    /**
     * Gets cookie value.
     *
     * @param request    the request
     * @param cookieName the cookie name
     * @return cookie value
     */
    public static String getCookieValue(HttpServletRequest request, String cookieName) {
        return getCookieValue(request, cookieName, false);
    }

    /**
     * Gets cookie value.
     *
     * @param request    the request
     * @param cookieName the cookie name
     * @param isDecoder  the is decoder
     * @return cookie value
     */
    public static String getCookieValue(HttpServletRequest request, String cookieName, boolean isDecoder) {
        Cookie[] cookieList = request.getCookies();
        if (cookieList == null || cookieName == null) {
            return null;
        }
        String retValue = null;
        try {
            for (Cookie cookie : cookieList) {
                if (cookie.getName().equals(cookieName)) {
                    if (isDecoder) {
                        retValue = URLDecoder.decode(cookie.getValue(), "UTF-8");
                    } else {
                        retValue = cookie.getValue();
                    }
                    break;
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return retValue;
    }

    /**
     * Gets cookie value.
     *
     * @param request      the request
     * @param cookieName   the cookie name
     * @param encodeString the encode string
     * @return cookie value
     */
    public static String getCookieValue(HttpServletRequest request, String cookieName, String encodeString) {
        Cookie[] cookieList = request.getCookies();
        if (cookieList == null || cookieName == null) {
            return null;
        }
        String retValue = null;
        try {
            for (Cookie cookie : cookieList) {
                if (cookie.getName().equals(cookieName)) {
                    retValue = URLDecoder.decode(cookie.getValue(), encodeString);
                    break;
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return retValue;
    }

    /**
     * Sets cookie.
     *
     * @param request     the request
     * @param response    the response
     * @param cookieName  the cookie name
     * @param cookieValue the cookie value
     */
    public static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName,
                                 String cookieValue) {
        setCookie(request, response, cookieName, cookieValue, -1);
    }

    /**
     * Sets cookie.
     *
     * @param request      the request
     * @param response     the response
     * @param cookieName   the cookie name
     * @param cookieValue  the cookie value
     * @param cookieMaxAge the cookie maxage
     */
    public static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName,
                                 String cookieValue, int cookieMaxAge) {
        setCookie(request, response, cookieName, cookieValue, cookieMaxAge, false);
    }

    /**
     * Sets cookie.
     *
     * @param request     the request
     * @param response    the response
     * @param cookieName  the cookie name
     * @param cookieValue the cookie value
     * @param isEncode    the is encode                    设置Cookie的值 不设置生效时间,但编码 在服务器被创建，返回给客户端，并且保存客户端 如果设置了SETMAXAGE
     *                    (int seconds)，会把cookie保存在客户端的硬盘中                    如果没有设置，会默认把cookie保存在浏览器的内存中 一旦设置setPath()
     *                    ：只能通过设置的路径才能获取到当前的cookie信息
     */
    public static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName,
                                 String cookieValue, boolean isEncode) {
        setCookie(request, response, cookieName, cookieValue, -1, isEncode);
    }

    /**
     * Sets cookie.
     *
     * @param request      the request
     * @param response     the response
     * @param cookieName   the cookie name
     * @param cookieValue  the cookie value
     * @param cookieMaxAge the cookie maxage
     * @param isEncode     the is encode
     */
    public static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName,
                                 String cookieValue, int cookieMaxAge, boolean isEncode) {
        doSetCookie(request, response, cookieName, cookieValue, cookieMaxAge, isEncode);
    }

    /**
     * Sets cookie.
     *
     * @param request      the request
     * @param response     the response
     * @param cookieName   the cookie name
     * @param cookieValue  the cookie value
     * @param cookieMaxAge the cookie maxage
     * @param encodeString the encode string
     */
    public static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName,
                                 String cookieValue, int cookieMaxAge, String encodeString) {
        doSetCookie(request, response, cookieName, cookieValue, cookieMaxAge, encodeString);
    }

    /**
     * Delete cookie.
     *
     * @param request    the request
     * @param response   the response
     * @param cookieName the cookie name
     */
    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response,
                                    String cookieName) {
        doSetCookie(request, response, cookieName, null, -1, false);
    }


    /**
     * Do set cookie.
     *
     * @param request      the request
     * @param response     the response
     * @param cookieName   the cookie name
     * @param cookieValue  the cookie value
     * @param cookieMaxAge cookie生效的最大秒数
     * @param isEncode     the is encode
     */
    private static void doSetCookie(HttpServletRequest request, HttpServletResponse response,
                                    String cookieName, String cookieValue, int cookieMaxAge, boolean isEncode) {
        try {
            if (cookieValue == null) {
                cookieValue = StringPool.EMPTY;
            } else if (isEncode) {
                cookieValue = URLEncoder.encode(cookieValue, "utf-8");
            }
            Cookie cookie = new Cookie(cookieName, cookieValue);
            if (cookieMaxAge > 0) {
                cookie.setMaxAge(cookieMaxAge);
            }
            if (null != request) {
                String domainName = getDomainName(request);
                log.info("========== domainName: {} ==========", domainName);
                if (!"localhost".equals(domainName)) {
                    cookie.setDomain(domainName);
                }
            }
            cookie.setPath(StringPool.SLASH);
            response.addCookie(cookie);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Do set cookie.
     *
     * @param request      the request
     * @param response     the response
     * @param cookieName   the cookie name
     * @param cookieValue  the cookie value
     * @param cookieMaxAge cookie生效的最大秒数
     * @param encodeString the encode string
     */
    private static void doSetCookie(HttpServletRequest request, HttpServletResponse response,
                                    String cookieName, String cookieValue, int cookieMaxAge, String encodeString) {
        try {
            if (cookieValue == null) {
                cookieValue = StringPool.EMPTY;
            } else {
                cookieValue = URLEncoder.encode(cookieValue, encodeString);
            }
            Cookie cookie = new Cookie(cookieName, cookieValue);
            if (cookieMaxAge > 0) {
                cookie.setMaxAge(cookieMaxAge);
            }
            if (null != request) {
                String domainName = getDomainName(request);
                log.info("========== domainName: {} ==========", domainName);
                if (!"localhost".equals(domainName)) {
                    cookie.setDomain(domainName);
                }
            }
            cookie.setPath(StringPool.SLASH);
            response.addCookie(cookie);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets domain name.
     *
     * @param request the request
     * @return domain name
     */
    private static String getDomainName(HttpServletRequest request) {
        String domainName = null;

        String serverName = request.getRequestURL().toString();
        if (StringPool.EMPTY.equals(serverName)) {
            domainName = StringPool.EMPTY;
        } else {
            serverName = serverName.toLowerCase();
            serverName = serverName.substring(7);
            final int end = serverName.indexOf(StringPool.SLASH);
            serverName = serverName.substring(0, end);
            if (serverName.indexOf(StringPool.COLON) > 0) {
                String[] ary = serverName.split(StringPool.COLON);
                serverName = ary[0];
            }

            final String[] domains = serverName.split("\\.");
            int len = domains.length;
            if (len > 3 && !isIp(serverName)) {
                // www.xxx.com.cn
                domainName = StringPool.DOT + domains[len - 3] + StringPool.DOT + domains[len - 2] + StringPool.DOT + domains[len - 1];
            } else if (len <= 3 && len > 1) {
                // xxx.com or xxx.cn
                domainName = StringPool.DOT + domains[len - 2] + StringPool.DOT + domains[len - 1];
            } else {
                domainName = serverName;
            }
        }
        return domainName;
    }

    /**
     * Trim spaces string.
     *
     * @param IP the ip
     * @return the string
     */
    public static String trimSpaces(String IP) {//去掉IP字符串前后所有的空格
        while (IP.startsWith(StringPool.SPACE)) {
            IP = IP.substring(1).trim();
        }
        while (IP.endsWith(StringPool.SPACE)) {
            IP = IP.substring(0, IP.length() - 1).trim();
        }
        return IP;
    }

    /**
     * Is ip boolean.
     *
     * @param IP the ip
     * @return the boolean
     */
    public static boolean isIp(String IP) {//判断是否是一个IP
        boolean b = false;
        IP = trimSpaces(IP);
        if (IP.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}")) {
            String s[] = IP.split("\\.");
            if (Integer.parseInt(s[0]) < 255) {
                if (Integer.parseInt(s[1]) < 255) {
                    if (Integer.parseInt(s[2]) < 255) {
                        if (Integer.parseInt(s[3]) < 255) {
                            b = true;
                        }
                    }
                }
            }
        }
        return b;
    }

}
