package com.kuehne_nagel.city_list.domain.services.impl;

import com.kuehne_nagel.city_list.application.config.YAMLConfig;
import com.kuehne_nagel.city_list.domain.entities.dto.UserDetailDto;
import com.kuehne_nagel.city_list.domain.exception.DomainException;
import com.kuehne_nagel.city_list.domain.services.CommonService;
import com.kuehne_nagel.city_list.domain.services.UserMgtService;
import com.kuehne_nagel.city_list.domain.util.Constants;
import com.kuehne_nagel.city_list.domain.entities.enums.JwtTokenType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService implements CommonService {

    @Autowired
    private YAMLConfig yamlConfig;

    @Autowired
    private UserMgtService userMgtService;


    //retrieve username from jwt token
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    //retrieve expiration date from jwt token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    //retrieve details from token
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    //for retrieving any information from token we will need the secret key
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(yamlConfig.getJwtSignKey()).parseClaimsJws(token).getBody();
    }

    //check if the token has expired
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }


    //while creating the token -
    //1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID
    //2. Sign the JWT using the HS512 algorithm and secret key.
    //   compaction of the JWT to a URL-safe string
    public String doGenerateToken(Map<String, Object> claims, String subject,Date expireDate ) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, yamlConfig.getJwtSignKey()).compact();
    }

    public String generateJwtToken(UserDetailDto userDetail, JwtTokenType jwtTokenType) {
        Date expireDate;
                if (JwtTokenType.ACCESS_TOKEN.equals(jwtTokenType)) {
                    expireDate = new Date(System.currentTimeMillis() + yamlConfig.getAccessTokenExpireTime());
                } else {
                    expireDate = new Date(System.currentTimeMillis() + yamlConfig.getRefreshTokenExpireTime());
                }

        return doGenerateToken(getUserDetailMap(userDetail, jwtTokenType),userDetail.getEmail(),expireDate);
    }

    public Map<String, Object> getClaimsFromTokenAsMap(String token) {

        logger.info("Extracting claims");

        Claims claims = getAllClaimsFromToken(token);

        logger.info("Claims extracted");
        return new HashMap<>(claims);
    }

    private Map<String, Object> getUserDetailMap(UserDetailDto userDetail, JwtTokenType jwtTokenType) {

        Map<String, Object> userDetails = new HashMap<>();

        userDetails.put(Constants.NAME, userDetail.getName());
        userDetails.put(Constants.EMAIL, userDetail.getEmail());
        userDetails.put(Constants.TOKEN_TYPE, jwtTokenType);

        return userDetails;
    }

    //validate token
    public Boolean validateToken(String token, UserDetailDto userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getEmail()) && !isTokenExpired(token));
    }



    /**
     * TODO - remove this one
     *
     * @param userName
     * @return
     */
    public UserDetailDto loadUserByUsername(String userName) throws DomainException {
        return userMgtService.getUserByEmail(userName);
    }
}
