package com.example.CouponService.commons;

import jakarta.servlet.http.HttpServletRequest;

public final class IpUtils {

    private IpUtils() {}

    public static String getClientIp(HttpServletRequest request) {
        String[] headerCandidates = {
                "X-Forwarded-For",
                "X-Real-IP",
                "Forwarded",
                "CF-Connecting-IP"
        };

        for (String header : headerCandidates) {
            String value = request.getHeader(header);
            if (value != null && !value.isBlank()) {
                if ("X-Forwarded-For".equalsIgnoreCase(header) || value.contains(",")) {
                    String[] parts = value.split(",");
                    for (String part : parts) {
                        String ip = part.trim();
                        if (!ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
                            return ip;
                        }
                    }
                } else {
                    return value.trim();
                }
            }
        }

        String remoteAddr = request.getRemoteAddr();
        return remoteAddr == null ? "" : remoteAddr;
    }
}

