package com.example.CouponService.commons;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class IpUtilsUnitTest {

    @Test
    void getClientIpFromXForwardedFor_singleIp() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("X-Forwarded-For")).thenReturn("192.168.1.100");

        String ip = IpUtils.getClientIp(request);

        assertEquals("192.168.1.100", ip);
    }

    @Test
    void getClientIpFromXForwardedFor_multipleIps() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("X-Forwarded-For")).thenReturn("192.168.1.100, 10.0.0.1, 127.0.0.1");

        String ip = IpUtils.getClientIp(request);

        assertEquals("192.168.1.100", ip);
    }

    @Test
    void getClientIp_shouldSkipUnknownInXForwardedFor() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("X-Forwarded-For")).thenReturn("unknown, 203.0.113.5");

        String ip = IpUtils.getClientIp(request);

        assertEquals("203.0.113.5", ip);
    }

    @Test
    void getClientIp_shouldReturnIpFromXRealIpIfPresent() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("X-Forwarded-For")).thenReturn(null);
        when(request.getHeader("X-Real-IP")).thenReturn("203.0.113.77");

        String ip = IpUtils.getClientIp(request);

        assertEquals("203.0.113.77", ip);
    }

    @Test
    void getClientIp_shouldReturnIpFromForwardedHeader() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("X-Forwarded-For")).thenReturn(null);
        when(request.getHeader("X-Real-IP")).thenReturn(null);
        when(request.getHeader("Forwarded")).thenReturn("198.51.100.22");

        String ip = IpUtils.getClientIp(request);

        assertEquals("198.51.100.22", ip);
    }

    @Test
    void getClientIp_shouldReturnIpFromRemoteAddrIfNoHeaders() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader(anyString())).thenReturn(null);
        when(request.getRemoteAddr()).thenReturn("127.0.0.1");

        String ip = IpUtils.getClientIp(request);

        assertEquals("127.0.0.1", ip);
    }

    @Test
    void getClientIp_shouldReturnEmptyStringIfNothingAvailable() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader(anyString())).thenReturn(null);
        when(request.getRemoteAddr()).thenReturn(null);

        String ip = IpUtils.getClientIp(request);

        assertEquals("", ip);
    }
}

