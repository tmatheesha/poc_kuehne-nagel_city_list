package com.kuehne_nagel.city_list.application.filter;

import com.kuehne_nagel.city_list.domain.entities.dto.UserDetailDto;
import com.kuehne_nagel.city_list.domain.exception.DomainException;
import com.kuehne_nagel.city_list.domain.services.impl.JwtService;
import com.kuehne_nagel.city_list.domain.util.Constants;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * This is the filter class for user authentication
 */
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtRequestFilter.class);

    @Autowired
    private JwtService jwtService;

    public JwtRequestFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    /**
     * Main filter method. All requests comes here before go to controller method.
     * Authentication is set if jwt token is valid
     *
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwtToken = null;
        String userName = null;

        // get token with bearer from authorization header
        final String authorizationHeader = request.getHeader(Constants.AUTH_HEADER_STRING);

        if (Objects.nonNull(authorizationHeader) && authorizationHeader.startsWith(Constants.BEARER_STRING)) {
            jwtToken = authorizationHeader.substring(7);
            LOGGER.info("JWT filter. Token received.");

            try {
                userName = jwtService.getUsernameFromToken(jwtToken);
            } catch (IllegalArgumentException e) {
                System.out.println("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                System.out.println("JWT Token has expired");
            }
        } else {
            logger.warn("JWT Token does not begin with Bearer String");
        }

        // Once we get the token validate it.
        if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetailDto userDetails = null;
            try {
                userDetails = jwtService.loadUserByUsername(userName);
            } catch (DomainException e) {
                logger.error("loadUserByUsername error occurred: ", e);
            }

            // if token is valid configure Spring Security to manually set
            // authentication
            if (Objects.nonNull(userDetails) && jwtService.validateToken(jwtToken, userDetails)) {

                List<String> authorities = new ArrayList<>();
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // After setting the Authentication in the context, we specify
                // that the current user is authenticated. So it passes the
                // Spring Security Configurations successfully.
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
