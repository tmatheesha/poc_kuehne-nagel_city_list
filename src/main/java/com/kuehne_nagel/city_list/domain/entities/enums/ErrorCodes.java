package com.kuehne_nagel.city_list.domain.entities.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCodes {
    CITY_ERROR_MAPPING_ERROR("CTMGT_4001", "MappingError", "Mapping exception : %s "),
    CITY_ERROR_EXT_FILE_TYPE_NULL("CTMGT_4002", "externalFileTypes is null", "externalFileTypes is null"),
    CITY_ERROR_EXT_FILE_TYPE_NOT_IMPL("CTMGT_4003", "externalFileTypes is not implemented: {}", "externalFileTypes is not implemented: %s"),
    CITY_ERROR_OBJ_IS_NULL("CTMGT_4004", "object is null", "%s is null"),
    CITY_ERROR_ID_NOT_EXIST_FOR_OBJ("CTMGT_4005", "id doesn't exist for object", "id: %s doesn't exist for %s"),
    CITY_ERROR_CITY_NAME_ALREADY_EXISTS("CTMGT_4006", "City Name already exists", "City Name {} already exists"),
    CITY_ERROR_MULTIPART_FILE_CONVERT("CTMGT_4007", "Error occurred in converting multipart file to a file, {}", "Please check the file and try again"),
    CITY_ERROR_READING_FILE("CTMGT_4008", "Error occurred in reading the file {}", "Please check the file and try again, error occurred: %s"),
    CITY_ERROR_FILE_IS_NULL("CTMGT_4009", "file is null", "file is null"),
    CITY_ERROR_FILE_TYPE_NOT_SUPPORTED("CTMGT_4010", "file type {} is not supported, use {} file type", "file type %s is not supported, use %s file type"),
    CITY_ERROR_CREATING_SECRET_KEY("CTMGT_4011", "Error occurred while creating secret key: {}", "Error occurred while creating secret key: %s"),
    CITY_ERROR_ENCRYPTING("CTMGT_4012", "Error occurred while encrypting: {}", "Error occurred while encrypting: %s"),
    CITY_ERROR_DECRYPTING("CTMGT_4013", "Error occurred while decrypting: {}", "Error occurred while decrypting: %s"),
    CITY_ERROR_NO_USER_ID("CTMGT_4014", "There is no User for given Id {}", "There is no User for given Id %l"),
    CITY_ERROR_NO_USER_FOR_EMAIL("CTMGT_4015", "There is no User for given email {}", "There is no User for given email %s"),
    CITY_ERROR_USERNAME_PWD_NOT_MATCHED("CTMGT_4016", "Password not matched for user name : {}", "Invalid Credentials"),
    CITY_ERROR_INVALID_TOKEN("CTMGT_4016", "Invalid or Expired token: {}", "Invalid or Expired token"),

    ;

    private final String code;
    private final String description;

    private final String message;

}
