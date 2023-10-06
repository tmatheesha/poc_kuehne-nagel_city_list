package com.kuehne_nagel.city_list.application.controller;

import com.kuehne_nagel.city_list.application.transport.request.AuthenticateRequest;
import com.kuehne_nagel.city_list.application.transport.request.RefreshTokenRequest;
import com.kuehne_nagel.city_list.application.transport.response.AuthResponse;
import com.kuehne_nagel.city_list.domain.exception.AuthorizationException;
import com.kuehne_nagel.city_list.domain.exception.DomainException;
import com.kuehne_nagel.city_list.domain.services.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Controller implementation of Authentication management.
 */
@RestController
@RequestMapping("${base-url.context}/auth")
@CrossOrigin(origins = "*",
        allowedHeaders = "*")
public class AuthController extends BaseController {

    @Autowired
    private AuthService authService;

    @PostMapping(value = "/login",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthResponse> authenticate(
            @Validated
            @RequestBody(required = true)
            AuthenticateRequest authenticateRequest, HttpServletRequest request) throws AuthorizationException {
        setLogIdentifier(request);
        setRequestId(authenticateRequest.getRequestHeader());
        AuthResponse authResponse = authService.authenticate(authenticateRequest);
        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

    /**
     * refresh token
     *
     * @param httpServletRequest
     * @param refreshTokenRequest
     * @return
     * @throws DomainException
     */
    @PostMapping(value = "/refresh/token", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest,
                                          HttpServletRequest httpServletRequest) throws DomainException {
        setLogIdentifier(httpServletRequest);
        AuthResponse authResponse = authService.refreshToken(refreshTokenRequest);
        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }
}
