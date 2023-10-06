package com.kuehne_nagel.city_list.application.aop;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kuehne_nagel.city_list.application.config.YAMLConfig;
import com.kuehne_nagel.city_list.domain.util.Constants;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.jboss.logging.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Objects;

@Aspect
@Configuration
public class ApplicationAopAspect {

    private Logger logger = LoggerFactory.getLogger(ApplicationAopAspect.class);

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private YAMLConfig yamlConfig;


    /**
     * log around
     *
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("execution(* com.kuehne_nagel.city_list.application.controller.**.*(..))")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Long inTime = System.currentTimeMillis();

        Object[] argList = joinPoint.getArgs();

        HttpServletRequest httpServletRequest = getHttpServletRequest(argList);

        String function = joinPoint.getSignature().getName();
        String api = httpServletRequest != null ? httpServletRequest.getRequestURI() : "N/A";
        try {
            if (logger.isInfoEnabled()) {
                logger.info("{}:{}:{}:{}:{}", Constants.CITY_MGT, Constants.REQUEST_RECEIVED, function, api, mapper.writeValueAsString(argList[0]));
            }
        } catch (JsonProcessingException e) {
            logger.error(Constants.ERROR_PARSING_PARAMS_IN_CLASS, joinPoint.getTarget().getClass().getSimpleName());
        }
        Object proceed = joinPoint.proceed();

        ResponseEntity<HashMap> response = (ResponseEntity<HashMap>) proceed;

        // extract response code from response
        String responseCode = String.valueOf(response.getStatusCode().value());

        Long responseTime = System.currentTimeMillis() - inTime;
        if (Boolean.TRUE.toString().equals(yamlConfig.getLoggingResponseShouldLogInfo()) && logger.isInfoEnabled()) {
            logger.info("sending response with requestId : {}", MDC.get(yamlConfig.getRequestIdKey()));
            logger.info(Constants.LOG_STRING, Constants.CITY_MGT, Constants.REQUEST_TERMINATED, function, api, responseCode, responseTime, response.getBody());
        } else {
            logger.info("sending response with requestId : {}", MDC.get(yamlConfig.getRequestIdKey()));
            if (logger.isDebugEnabled()) {
                logger.debug(Constants.LOG_STRING, Constants.CITY_MGT, Constants.REQUEST_TERMINATED, function, api, responseCode, responseTime, response.getBody());
            }
        }

        logger.info(Constants.LOG_STRING, Constants.CITY_MGT, Constants.REQUEST_TERMINATED, function, api, responseCode, responseTime, response.getBody());
        return proceed;
    }

    /**
     * log around
     *
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("execution(* org.springframework.data.jpa.repository.JpaRepository+.*(..))))")
    public Object logAroundRepositories(ProceedingJoinPoint joinPoint) throws Throwable {
        Long inTime = System.currentTimeMillis();
        Object[] argList = joinPoint.getArgs();
        String function = joinPoint.getSignature().getName();
        String repositoryClass = ((MethodInvocationProceedingJoinPoint) joinPoint).getTarget().toString();
        try {
            if (logger.isInfoEnabled()) {
                logger.info(Constants.DB_CALL_INIT, repositoryClass, function, mapper.writeValueAsString(argList));
            }
        } catch (JsonProcessingException e) {
            logger.error(Constants.ERROR_PARSING_PARAMS_IN_CLASS, repositoryClass, joinPoint.getTarget().getClass().getSimpleName());
        }
        Object proceed = joinPoint.proceed();
        Long responseTime = System.currentTimeMillis() - inTime;
        logger.info(Constants.DB_CALL_TERMINATED, function, responseTime, proceed);
        return proceed;
    }

    /**
     * Add logging in method init in Domain package.
     *
     * @param pjp
     * @param request
     */
    @Before("( execution(* com.kuehne_nagel.city_list.domain.**.*.*(..)) || " +
            "execution(* com.kuehne_nagel.city_list.domain.entities.assemblers.impl.*.*(..))) && args(request,..)")
    public void beforeDomainMethod(JoinPoint pjp, Object request) {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        String className = pjp.getTarget().getClass().getSimpleName();
        Method method = signature.getMethod();
        try {
            if (logger.isDebugEnabled()) {
                logger.debug(Constants.METHOD_START, Constants.CITY_MGT, className + ":" + method.getName(), Objects.nonNull(request) ? mapper.writeValueAsString(request) : "null");
            }
        } catch (JsonProcessingException e) {
            logger.error(Constants.ERROR_PARSING_PARAMS_IN_CLASS, pjp.getTarget().getClass().getSimpleName());
        }
    }

    /**
     * Add logging in method end in domain package.
     *
     * @param pjp
     * @param response
     */
    @AfterReturning(value = "execution(* com.kuehne_nagel.city_list.domain.**.*.*(..))",
            returning = "response")
    public void afterDomainMethod(JoinPoint pjp, Object response) {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        String className = pjp.getTarget().getClass().getSimpleName();
        Method method = signature.getMethod();
        try {
            if (logger.isDebugEnabled()) {
                logger.debug(Constants.METHOD_OUT, Constants.CITY_MGT, className + ":" + method.getName(), Objects.nonNull(response) ? mapper.writeValueAsString(response) : "null");
            }
        } catch (JsonProcessingException e) {
            logger.error(Constants.ERROR_PARSING_PARAMS_IN_CLASS, pjp.getTarget().getClass().getSimpleName());
        }
    }

    /**
     * get HttpServletRequest from argList
     *
     * @param argList
     * @return
     */
    private HttpServletRequest getHttpServletRequest(Object[] argList) {
        HttpServletRequest httpServletRequest = null;

        try {
            if (HttpServletRequest.class.isInstance(argList[1])) {
                httpServletRequest = (HttpServletRequest) argList[1];
            }
        } catch (Exception ex) {
            logger.error("Error occurred while parsing to HttpServletRequest", ex);
        }

        return httpServletRequest;
    }
}
