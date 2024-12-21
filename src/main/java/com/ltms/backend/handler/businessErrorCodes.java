package com.ltms.backend.handler;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

public enum businessErrorCodes {
    NO_CODE(0, NOT_IMPLEMENTED, "No code"),
    INCORRECT_CURRENT_PASSWORD(300, BAD_REQUEST, "La contraseña ingresada es incorrecta"),
    NEW_PASSWORD_DOES_NOT_MATCH(301, BAD_REQUEST, "La nueva contraseña no coincide"),
    ACCOUNT_LOCKED(302, FORBIDDEN, "User account is locked"),
    ACCOUNT_DISABLED(303, FORBIDDEN, "La cuenta del usuario esta inhabilitada"),
    BAD_CREDENTIALS(304, FORBIDDEN, "El login y/o la contraseña es incorrecto"),
    ;
    @Getter
    private final int code;
    @Getter
    private final String description;
    @Getter
    private final HttpStatus httpStatusCode;


    businessErrorCodes(int code, HttpStatus httpStatusCode, String description) {
        this.code = code;
        this.description = description;
        this.httpStatusCode = httpStatusCode;
    }
}
