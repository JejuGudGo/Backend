package com.example.jejugudgo.global.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {
    public Cookie getCookie(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookieName)) return cookie;
        }

        return null;
    }

    public void setCookie(String key, String value, HttpServletResponse response) {
        ResponseCookie responseCookie = ResponseCookie.from(key, value)
                .path("/")
                .secure(true)
                .sameSite("None")
                .httpOnly(true)
                .maxAge(Integer.MAX_VALUE)
                .build();

        response.addHeader("Set-Cookie", responseCookie.toString());
    }

    public void deleteCookie(String key, HttpServletResponse response) {
        ResponseCookie responseCookie = ResponseCookie.from(key, "")
                .path("/")
                .secure(true)
                .sameSite("None")
                .httpOnly(true)
                .maxAge(0)
                .build();

        response.addHeader("Set-Cookie", responseCookie.toString());
    }
}
