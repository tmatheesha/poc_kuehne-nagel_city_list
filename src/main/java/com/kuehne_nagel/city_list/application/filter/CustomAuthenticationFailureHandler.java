package com.kuehne_nagel.city_list.application.filter;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException  {
        httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());

        String jsonPayload = "{\"message\" : \"%s\", \"timestamp\" : \"%s\" }";
        httpServletResponse.getOutputStream().println(String.format(jsonPayload, e.getMessage(), Calendar.getInstance().getTime()));
    }

}
