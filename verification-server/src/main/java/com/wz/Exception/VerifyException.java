package com.wz.Exception;

/**
 * 验证工具异常类
 *
 * @author AlanLee
 * @version 2016/11/26
 */
public class VerifyException extends RuntimeException {


    private static final long serialVersionUID = 1L;

    /**
     * 错误编码
     */
    private String errorCode;

    /**
     * 消息是否为属性文件中的Key
     */
    private boolean propertiesKey = true;

    /**
     * 构造一个基本异常.
     *
     * @param message 信息描述
     */
    public VerifyException(String message) {
        super(message);
    }

    /**
     * 构造一个基本异常.
     *
     * @param errorCode 错误编码
     * @param message   信息描述
     */
    public VerifyException(String errorCode, String message) {
        this(errorCode, message, true);
    }

    /**
     * 构造一个枚举异常.
     *
     * @param exceptionEnum 验证异常枚举
     */
    public VerifyException(VerifyExceptionEnum exceptionEnum) {
        this(String.valueOf(exceptionEnum.getCode()), exceptionEnum.getMsg(), true);
    }

    /**
     * 构造一个枚举异常.扩充信息
     *
     * @param exceptionEnum 验证异常枚举
     * @param message       扩充信息
     */
    public VerifyException(VerifyExceptionEnum exceptionEnum, String message) {
        this(String.valueOf(exceptionEnum.getCode()), exceptionEnum.getMsg() + ":" + message, true);
    }

    /**
     * 构造一个基本异常.
     *
     * @param errorCode 错误编码
     * @param message   信息描述
     */
    public VerifyException(String errorCode, String message, Throwable cause) {
        this(errorCode, message, cause, true);
    }

    /**
     * 构造一个基本异常.
     *
     * @param errorCode     错误编码
     * @param message       信息描述
     * @param propertiesKey 消息是否为属性文件中的Key
     */
    public VerifyException(String errorCode, String message, boolean propertiesKey) {
        super(message);
        this.setErrorCode(errorCode);
        this.setPropertiesKey(propertiesKey);
    }

    /**
     * 构造一个基本异常.
     *
     * @param errorCode 错误编码
     * @param message   信息描述
     */
    public VerifyException(String errorCode, String message, Throwable cause, boolean propertiesKey) {
        super(message, cause);
        this.setErrorCode(errorCode);
        this.setPropertiesKey(propertiesKey);
    }

    /**
     * 构造一个基本异常.
     *
     * @param message 信息描述
     * @param cause   根异常类（可以存入任何异常）
     */
    public VerifyException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public boolean isPropertiesKey() {
        return propertiesKey;
    }

    public void setPropertiesKey(boolean propertiesKey) {
        this.propertiesKey = propertiesKey;
    }
}
