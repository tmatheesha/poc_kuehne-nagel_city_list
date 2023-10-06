package com.kuehne_nagel.city_list.application.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kuehne_nagel.city_list.application.transport.response.ResponseHeader;
import com.kuehne_nagel.city_list.domain.services.impl.JwtService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    public static final String AUTH_HEADER_TEXT = "authorization";

    private static final String RESPONSE_HEADER_TEXT = "responseHeader";

    private JwtService jwtService;

    private static final String TOKEN_EXPIRED_TEXT = "LGN2001";
    private static final String PREVIOUS_LOGIN_EXPIRED_TEXT = "LGN2003";
    private static final String AUTHENTICATION_REQUIRED_TEXT = "LGN2002";


    public RestAuthenticationEntryPoint(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    /**
     * Handle all authentication exceptions
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @param e
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                         AuthenticationException e) throws IOException, ServletException {

        Map<String, Object> errorDetails = new LinkedHashMap<>();

        ResponseHeader response = new ResponseHeader();
        response.setRequestId("12345");
        response.setResponseCode("401");

        String token = httpServletRequest.getHeader(AUTH_HEADER_TEXT);


        response.setTimestamp(LocalDateTime.now().toString());

        errorDetails.put(RESPONSE_HEADER_TEXT, response);

        httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        OutputStream out = httpServletResponse.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(out, errorDetails);
        out.flush();

    }
}
