package com.kuehne_nagel.city_list.domain.util;

/**
 * Utility Class for Constants
 */
public class Constants {

    private Constants(){
        //This class should not be instantiated
    }

    public static final String DUPLICATE_CITY_NAME = "duplicateCityName";

    public static final String USER_NAME = "userName";

    public static final String USER_ID = "userId";

    public static final String CRUD_METHOD_INIT_LOG_MSG_CREATE = "create method started";

    public static final String CRUD_METHOD_INIT_LOG_MSG_IS_NULL= "{} is null";

    public static final String CRUD_METHOD_INIT_MSG_IS_NULL= "%s is null";


    public static final String CITY_MGT = "citymgt";

    public static final String LOG_STRING = "{}:{}:{}:{}:{}:{}:{}";
    public static final String DB_CALL_INIT = "citymgt:db request initiated:{}:{}:{}";
    public static final String DB_CALL_TERMINATED = "citymgt:db request terminated:{}:{}:{}:{}";
    public static final String REQUEST_TERMINATED = "REQUEST TERMINATED";

    public static final String REQUEST_RECEIVED = "REQUEST RECEIVED";
    public static final String ERROR_PARSING_PARAMS_IN_CLASS = "error parsing params in class {}";
    public static final String METHOD_START = "{}:method start:{}:{}";
    public static final String METHOD_OUT = "{}:method out:{}:{}";

    public static final String CRUD_METHOD_INIT_LOG_MSG_UPDATE = "update method started | id: {}";
    public static final String AUTH_HEADER_STRING = "Authorization";
    public static final String BEARER_STRING = "Bearer ";

    public static final String NAME = "name";
    public static final String EMAIL = "email";

    public static final String TOKEN_TYPE = "jwtTokenType";

}
