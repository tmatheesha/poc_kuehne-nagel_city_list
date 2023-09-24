package com.kuehne_nagel.city_list.domain.services;

import com.kuehne_nagel.city_list.application.transport.request.AuthenticateRequest;
import com.kuehne_nagel.city_list.application.transport.request.RefreshTokenRequest;
import com.kuehne_nagel.city_list.application.transport.response.AuthResponse;
import com.kuehne_nagel.city_list.domain.exception.AuthorizationException;
import com.kuehne_nagel.city_list.domain.exception.DomainException;

public interface AuthService extends CommonService{

    AuthResponse authenticate(AuthenticateRequest authenticateRequest) throws  AuthorizationException;

    AuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest) throws DomainException;
}
