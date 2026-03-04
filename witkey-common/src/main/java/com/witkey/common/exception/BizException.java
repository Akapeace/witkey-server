package com.witkey.common.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * @author peace
 * @date 2026/3/4 14:28
 * @description:
 */
@Getter
@Setter
public class BizException extends RuntimeException{
    // 异常码
    private String errorCode;
    // 错误信息
    private String errorMessage;

    public BizException(BaseExceptionInterface baseExceptionInterface) {
        this.errorCode = baseExceptionInterface.getErrorCode();
        this.errorMessage = baseExceptionInterface.getErrorMessage();
    }

}
