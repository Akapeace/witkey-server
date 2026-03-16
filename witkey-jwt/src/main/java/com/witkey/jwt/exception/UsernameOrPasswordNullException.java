package com.witkey.jwt.exception;


import org.springframework.security.core.AuthenticationException;

/**
 * @author peace
 * @date 2026/3/16 17:27
 * @description:
 */
public class UsernameOrPasswordNullException extends AuthenticationException {
    public UsernameOrPasswordNullException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public UsernameOrPasswordNullException(String msg) {
        super(msg);
    }
}
