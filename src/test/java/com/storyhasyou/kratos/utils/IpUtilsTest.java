package com.storyhasyou.kratos.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * IpUtils单元测试类
 * 
 * 【强制】测试IP地址获取功能，包括各种HTTP头部处理
 * 【强制】使用Mockito模拟HttpServletRequest对象
 * 【强制】测试方法命名遵循规范：should_ReturnExpected_When_GivenInput()
 * 
 * @author fangxi
 */
@ExtendWith(MockitoExtension.class)
public class IpUtilsTest {

    @Mock
    private HttpServletRequest mockRequest;

    // ==================== X-Forwarded-For头部测试 ====================

    @Test
    void should_ReturnIpFromXForwardedFor_When_HeaderExists() {
        // Given
        String expectedIp = "192.168.1.100";
        when(mockRequest.getHeader("x-forwarded-for")).thenReturn(expectedIp);
        
        // When
        String actualIp = IpUtils.getIp(mockRequest);
        
        // Then
        assertThat(actualIp).isEqualTo(expectedIp);
        verify(mockRequest).getHeader("x-forwarded-for");
        verifyNoMoreInteractions(mockRequest);
    }

    @Test
    void should_ReturnFirstValidIp_When_XForwardedForHasMultipleIps() {
        // Given
        String forwardedFor = "192.168.1.100, 10.0.0.1, 172.16.0.1";
        when(mockRequest.getHeader("x-forwarded-for")).thenReturn(forwardedFor);
        
        // When
        String actualIp = IpUtils.getIp(mockRequest);
        
        // Then
        assertThat(actualIp).isEqualTo(forwardedFor);
    }

    @Test
    void should_FallbackToProxyClientIp_When_XForwardedForIsNull() {
        // Given
        String expectedIp = "192.168.1.101";
        when(mockRequest.getHeader("x-forwarded-for")).thenReturn(null);
        when(mockRequest.getHeader("Proxy-Client-IP")).thenReturn(expectedIp);
        
        // When
        String actualIp = IpUtils.getIp(mockRequest);
        
        // Then
        assertThat(actualIp).isEqualTo(expectedIp);
        verify(mockRequest).getHeader("x-forwarded-for");
        verify(mockRequest).getHeader("Proxy-Client-IP");
    }

    @Test
    void should_FallbackToProxyClientIp_When_XForwardedForIsEmpty() {
        // Given
        String expectedIp = "192.168.1.102";
        when(mockRequest.getHeader("x-forwarded-for")).thenReturn("");
        when(mockRequest.getHeader("Proxy-Client-IP")).thenReturn(expectedIp);
        
        // When
        String actualIp = IpUtils.getIp(mockRequest);
        
        // Then
        assertThat(actualIp).isEqualTo(expectedIp);
        verify(mockRequest).getHeader("x-forwarded-for");
        verify(mockRequest).getHeader("Proxy-Client-IP");
    }

    @Test
    void should_FallbackToProxyClientIp_When_XForwardedForIsUnknown() {
        // Given
        String expectedIp = "192.168.1.103";
        when(mockRequest.getHeader("x-forwarded-for")).thenReturn("unknown");
        when(mockRequest.getHeader("Proxy-Client-IP")).thenReturn(expectedIp);
        
        // When
        String actualIp = IpUtils.getIp(mockRequest);
        
        // Then
        assertThat(actualIp).isEqualTo(expectedIp);
        verify(mockRequest).getHeader("x-forwarded-for");
        verify(mockRequest).getHeader("Proxy-Client-IP");
    }

    // ==================== Proxy-Client-IP头部测试 ====================

    @Test
    void should_FallbackToWLProxyClientIp_When_ProxyClientIpIsNull() {
        // Given
        String expectedIp = "192.168.1.104";
        when(mockRequest.getHeader("x-forwarded-for")).thenReturn(null);
        when(mockRequest.getHeader("Proxy-Client-IP")).thenReturn(null);
        when(mockRequest.getHeader("WL-Proxy-Client-IP")).thenReturn(expectedIp);
        
        // When
        String actualIp = IpUtils.getIp(mockRequest);
        
        // Then
        assertThat(actualIp).isEqualTo(expectedIp);
        verify(mockRequest).getHeader("x-forwarded-for");
        verify(mockRequest).getHeader("Proxy-Client-IP");
        verify(mockRequest).getHeader("WL-Proxy-Client-IP");
    }

    @Test
    void should_FallbackToWLProxyClientIp_When_ProxyClientIpIsEmpty() {
        // Given
        String expectedIp = "192.168.1.105";
        when(mockRequest.getHeader("x-forwarded-for")).thenReturn("");
        when(mockRequest.getHeader("Proxy-Client-IP")).thenReturn("");
        when(mockRequest.getHeader("WL-Proxy-Client-IP")).thenReturn(expectedIp);
        
        // When
        String actualIp = IpUtils.getIp(mockRequest);
        
        // Then
        assertThat(actualIp).isEqualTo(expectedIp);
    }

    @Test
    void should_FallbackToWLProxyClientIp_When_ProxyClientIpIsUnknown() {
        // Given
        String expectedIp = "192.168.1.106";
        when(mockRequest.getHeader("x-forwarded-for")).thenReturn("UNKNOWN");
        when(mockRequest.getHeader("Proxy-Client-IP")).thenReturn("Unknown");
        when(mockRequest.getHeader("WL-Proxy-Client-IP")).thenReturn(expectedIp);
        
        // When
        String actualIp = IpUtils.getIp(mockRequest);
        
        // Then
        assertThat(actualIp).isEqualTo(expectedIp);
    }

    // ==================== HTTP_CLIENT_IP头部测试 ====================

    @Test
    void should_FallbackToHttpClientIp_When_WLProxyClientIpIsNull() {
        // Given
        String expectedIp = "192.168.1.107";
        when(mockRequest.getHeader("x-forwarded-for")).thenReturn(null);
        when(mockRequest.getHeader("Proxy-Client-IP")).thenReturn(null);
        when(mockRequest.getHeader("WL-Proxy-Client-IP")).thenReturn(null);
        when(mockRequest.getHeader("HTTP_CLIENT_IP")).thenReturn(expectedIp);
        
        // When
        String actualIp = IpUtils.getIp(mockRequest);
        
        // Then
        assertThat(actualIp).isEqualTo(expectedIp);
        verify(mockRequest).getHeader("x-forwarded-for");
        verify(mockRequest).getHeader("Proxy-Client-IP");
        verify(mockRequest).getHeader("WL-Proxy-Client-IP");
        verify(mockRequest).getHeader("HTTP_CLIENT_IP");
    }

    // ==================== HTTP_X_FORWARDED_FOR头部测试 ====================

    @Test
    void should_FallbackToHttpXForwardedFor_When_HttpClientIpIsNull() {
        // Given
        String expectedIp = "192.168.1.108";
        when(mockRequest.getHeader("x-forwarded-for")).thenReturn(null);
        when(mockRequest.getHeader("Proxy-Client-IP")).thenReturn(null);
        when(mockRequest.getHeader("WL-Proxy-Client-IP")).thenReturn(null);
        when(mockRequest.getHeader("HTTP_CLIENT_IP")).thenReturn(null);
        when(mockRequest.getHeader("HTTP_X_FORWARDED_FOR")).thenReturn(expectedIp);
        
        // When
        String actualIp = IpUtils.getIp(mockRequest);
        
        // Then
        assertThat(actualIp).isEqualTo(expectedIp);
        verify(mockRequest).getHeader("HTTP_X_FORWARDED_FOR");
    }

    // ==================== getRemoteAddr()回退测试 ====================

    @Test
    void should_FallbackToRemoteAddr_When_AllHeadersAreNull() {
        // Given
        String expectedIp = "127.0.0.1";
        when(mockRequest.getHeader("x-forwarded-for")).thenReturn(null);
        when(mockRequest.getHeader("Proxy-Client-IP")).thenReturn(null);
        when(mockRequest.getHeader("WL-Proxy-Client-IP")).thenReturn(null);
        when(mockRequest.getHeader("HTTP_CLIENT_IP")).thenReturn(null);
        when(mockRequest.getHeader("HTTP_X_FORWARDED_FOR")).thenReturn(null);
        when(mockRequest.getRemoteAddr()).thenReturn(expectedIp);
        
        // When
        String actualIp = IpUtils.getIp(mockRequest);
        
        // Then
        assertThat(actualIp).isEqualTo(expectedIp);
        verify(mockRequest).getRemoteAddr();
    }

    @Test
    void should_FallbackToRemoteAddr_When_AllHeadersAreEmpty() {
        // Given
        String expectedIp = "192.168.1.1";
        when(mockRequest.getHeader("x-forwarded-for")).thenReturn("");
        when(mockRequest.getHeader("Proxy-Client-IP")).thenReturn("");
        when(mockRequest.getHeader("WL-Proxy-Client-IP")).thenReturn("");
        when(mockRequest.getHeader("HTTP_CLIENT_IP")).thenReturn("");
        when(mockRequest.getHeader("HTTP_X_FORWARDED_FOR")).thenReturn("");
        when(mockRequest.getRemoteAddr()).thenReturn(expectedIp);
        
        // When
        String actualIp = IpUtils.getIp(mockRequest);
        
        // Then
        assertThat(actualIp).isEqualTo(expectedIp);
        verify(mockRequest).getRemoteAddr();
    }

    @Test
    void should_FallbackToRemoteAddr_When_AllHeadersAreUnknown() {
        // Given
        String expectedIp = "10.0.0.1";
        when(mockRequest.getHeader("x-forwarded-for")).thenReturn("unknown");
        when(mockRequest.getHeader("Proxy-Client-IP")).thenReturn("UNKNOWN");
        when(mockRequest.getHeader("WL-Proxy-Client-IP")).thenReturn("Unknown");
        when(mockRequest.getHeader("HTTP_CLIENT_IP")).thenReturn("unknown");
        when(mockRequest.getHeader("HTTP_X_FORWARDED_FOR")).thenReturn("UNKNOWN");
        when(mockRequest.getRemoteAddr()).thenReturn(expectedIp);
        
        // When
        String actualIp = IpUtils.getIp(mockRequest);
        
        // Then
        assertThat(actualIp).isEqualTo(expectedIp);
        verify(mockRequest).getRemoteAddr();
    }

    // ==================== 大小写不敏感测试 ====================

    @ParameterizedTest
    @CsvSource({
        "unknown, true",
        "UNKNOWN, true", 
        "Unknown, true",
        "UnKnOwN, true",
        "192.168.1.100, false",
        "not-unknown, false",
        "unknownip, false"
    })
    void should_HandleCaseInsensitiveUnknown_When_CheckingUnknownValue(String headerValue, boolean shouldFallback) {
        // Given
        String fallbackIp = "127.0.0.1";
        lenient().when(mockRequest.getHeader("x-forwarded-for")).thenReturn(headerValue);
        lenient().when(mockRequest.getHeader("Proxy-Client-IP")).thenReturn(fallbackIp);
        
        // When
        String actualIp = IpUtils.getIp(mockRequest);
        
        // Then
        if (shouldFallback) {
            assertThat(actualIp).isEqualTo(fallbackIp);
            verify(mockRequest).getHeader("Proxy-Client-IP");
        } else {
            assertThat(actualIp).isEqualTo(headerValue);
            verify(mockRequest, never()).getHeader("Proxy-Client-IP");
        }
    }

    // ==================== IPv4地址测试 ====================

    @Test
    void should_ReturnValidIPv4_When_HeaderContainsIPv4() {
        // Given
        String ipv4Address = "203.0.113.1";
        when(mockRequest.getHeader("x-forwarded-for")).thenReturn(ipv4Address);
        
        // When
        String actualIp = IpUtils.getIp(mockRequest);
        
        // Then
        assertThat(actualIp).isEqualTo(ipv4Address);
        assertThat(actualIp).matches("^\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}$");
    }

    @ParameterizedTest
    @CsvSource({
        "127.0.0.1",        // 本地回环
        "192.168.1.100",    // 私有网络
        "10.0.0.1",         // 私有网络
        "172.16.0.1",       // 私有网络
        "203.0.113.1",      // 公网IP
        "8.8.8.8",          // Google DNS
        "1.1.1.1"           // Cloudflare DNS
    })
    void should_ReturnValidIPs_When_GivenVariousIPv4Formats(String ipAddress) {
        // Given
        when(mockRequest.getHeader("x-forwarded-for")).thenReturn(ipAddress);
        
        // When
        String actualIp = IpUtils.getIp(mockRequest);
        
        // Then
        assertThat(actualIp).isEqualTo(ipAddress);
    }

    // ==================== IPv6地址测试 ====================

    @Test
    void should_ReturnValidIPv6_When_HeaderContainsIPv6() {
        // Given
        String ipv6Address = "2001:db8::1";
        when(mockRequest.getHeader("x-forwarded-for")).thenReturn(ipv6Address);
        
        // When
        String actualIp = IpUtils.getIp(mockRequest);
        
        // Then
        assertThat(actualIp).isEqualTo(ipv6Address);
    }

    @ParameterizedTest
    @CsvSource({
        "::1",                      // IPv6本地回环
        "2001:db8::1",             // IPv6地址
        "2001:0db8:85a3::8a2e:0370:7334", // 完整IPv6
        "::ffff:192.0.2.1"         // IPv4映射的IPv6
    })
    void should_ReturnValidIPs_When_GivenVariousIPv6Formats(String ipAddress) {
        // Given
        when(mockRequest.getHeader("x-forwarded-for")).thenReturn(ipAddress);
        
        // When
        String actualIp = IpUtils.getIp(mockRequest);
        
        // Then
        assertThat(actualIp).isEqualTo(ipAddress);
    }

    // ==================== 复杂场景测试 ====================

    @Test
    void should_HandleComplexForwardedHeader_When_MultipleProxies() {
        // Given
        String complexHeader = "203.0.113.195, 70.41.3.18, 150.172.238.178";
        when(mockRequest.getHeader("x-forwarded-for")).thenReturn(complexHeader);
        
        // When
        String actualIp = IpUtils.getIp(mockRequest);
        
        // Then
        assertThat(actualIp).isEqualTo(complexHeader);
    }

    @Test
    void should_HandleForwardedHeaderWithSpaces_When_ContainsExtraSpaces() {
        // Given
        String headerWithSpaces = " 192.168.1.100 , 10.0.0.1 ";
        when(mockRequest.getHeader("x-forwarded-for")).thenReturn(headerWithSpaces);
        
        // When
        String actualIp = IpUtils.getIp(mockRequest);
        
        // Then
        assertThat(actualIp).isEqualTo(headerWithSpaces);
    }

    // ==================== 边界条件测试 ====================

    @Test
    void should_HandleNullRequest_When_RequestIsNull() {
        // When & Then
        assertThatThrownBy(() -> IpUtils.getIp(null))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    void should_HandleAllNullValues_When_AllMethodsReturnNull() {
        // Given
        when(mockRequest.getHeader(anyString())).thenReturn(null);
        when(mockRequest.getRemoteAddr()).thenReturn(null);
        
        // When
        String actualIp = IpUtils.getIp(mockRequest);
        
        // Then
        assertThat(actualIp).isNull();
        verify(mockRequest).getRemoteAddr();
    }

    // ==================== 实际应用场景测试 ====================

    @Test
    void should_HandleNginxProxy_When_BehindNginx() {
        // Given - 模拟Nginx反向代理场景
        String realClientIp = "203.0.113.1";
        when(mockRequest.getHeader("x-forwarded-for")).thenReturn(realClientIp);
        
        // When
        String actualIp = IpUtils.getIp(mockRequest);
        
        // Then
        assertThat(actualIp).isEqualTo(realClientIp);
    }

    @Test
    void should_HandleCloudFlareProxy_When_BehindCloudFlare() {
        // Given - 模拟CloudFlare CDN场景
        String cloudFlareHeader = "203.0.113.1";
        when(mockRequest.getHeader("x-forwarded-for")).thenReturn(cloudFlareHeader);
        
        // When
        String actualIp = IpUtils.getIp(mockRequest);
        
        // Then
        assertThat(actualIp).isEqualTo(cloudFlareHeader);
    }

    @Test
    void should_HandleLoadBalancer_When_BehindLoadBalancer() {
        // Given - 模拟负载均衡器场景
        String lbForwardedFor = "192.168.1.100, 10.0.0.1";
        when(mockRequest.getHeader("x-forwarded-for")).thenReturn(lbForwardedFor);
        
        // When
        String actualIp = IpUtils.getIp(mockRequest);
        
        // Then
        assertThat(actualIp).isEqualTo(lbForwardedFor);
    }

    // ==================== 安全性测试 ====================

    @Test
    void should_ReturnValueAsIs_When_HeaderContainsMaliciousContent() {
        // Given
        String maliciousHeader = "<script>alert('xss')</script>";
        when(mockRequest.getHeader("x-forwarded-for")).thenReturn(maliciousHeader);
        
        // When
        String actualIp = IpUtils.getIp(mockRequest);
        
        // Then
        // 方法不进行内容过滤，原样返回（调用方需要自行验证）
        assertThat(actualIp).isEqualTo(maliciousHeader);
    }

    @Test
    void should_ReturnValueAsIs_When_HeaderContainsSpecialCharacters() {
        // Given
        String specialChars = "192.168.1.100; DROP TABLE users;";
        when(mockRequest.getHeader("x-forwarded-for")).thenReturn(specialChars);
        
        // When
        String actualIp = IpUtils.getIp(mockRequest);
        
        // Then
        assertThat(actualIp).isEqualTo(specialChars);
    }

    // ==================== 性能测试 ====================

    @Test
    void should_PerformEfficiently_When_CalledManyTimes() {
        // Given
        String testIp = "192.168.1.100";
        when(mockRequest.getHeader("x-forwarded-for")).thenReturn(testIp);
        
        // When
        long startTime = System.currentTimeMillis();
        
        for (int i = 0; i < 100000; i++) {
            String ip = IpUtils.getIp(mockRequest);
            assertThat(ip).isNotNull();
        }
        
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        
        // Then
        assertThat(elapsedTime).isLessThan(1000L); // 1秒内完成10万次调用
        System.out.println("IP获取性能测试: 100,000 次调用耗时 " + elapsedTime + "ms");
    }
}