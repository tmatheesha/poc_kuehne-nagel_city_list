package com.kuehne_nagel.city_list.application.controller;

import com.kuehne_nagel.city_list.application.config.YAMLConfig;
import com.kuehne_nagel.city_list.application.transport.request.RequestHeader;
import jakarta.servlet.http.HttpServletRequest;
import org.jboss.logging.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;
import java.util.UUID;

public class BaseController {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private YAMLConfig yamlConfig;

    public void setLogIdentifier(HttpServletRequest request) {
        String logIdentifier = request.getHeader(yamlConfig.getLogIdentifierKey());
        if (logIdentifier != null) {
            MDC.put(yamlConfig.getLogIdentifierKey(), logIdentifier);
        } else {
            MDC.put(yamlConfig.getLogIdentifierKey(), UUID.randomUUID().toString());
        }
    }

    /**
     * Set RequestId to MDC.
     *
     * @param requestHeader
     */
    public void setRequestId(RequestHeader requestHeader) {
        if (Objects.nonNull(requestHeader)) {
            MDC.put(yamlConfig.getRequestIdKey(), requestHeader.getRequestId());

        }
    }
}
