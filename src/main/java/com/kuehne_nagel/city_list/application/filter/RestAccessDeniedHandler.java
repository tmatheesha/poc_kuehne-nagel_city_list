package com.kuehne_nagel.city_list.application.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class RestAccessDeniedHandler implements AccessDeniedHandler {

    private static final String MESSAGE_TEXT = "message";
    private static final String CODE_TEXT = "code";
    private static final String TYPE_TEXT = "type";

    /**
     * Handle access denied exception
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @param e
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                       AccessDeniedException e) throws IOException, ServletException {

        Map<String, Object> errorDetails = new LinkedHashMap<>();
        errorDetails.put(CODE_TEXT, HttpStatus.UNAUTHORIZED.value());
        errorDetails.put(TYPE_TEXT, e.getClass().getSimpleName());
        errorDetails.put(MESSAGE_TEXT, "Access Denied");
        httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        OutputStream out = httpServletResponse.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(out, errorDetails);
        out.flush();
    }
}
