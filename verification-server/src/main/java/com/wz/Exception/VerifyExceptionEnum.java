package com.wz.Exception;

public enum VerifyExceptionEnum {
    PIC_SIZE_OUT(100000, "图片单向尺寸不能超过200"),
    PIC_READ_FAIL(100001, "图片读取失败,确保存储资源都属于有效Image资源"),

    //URL读取异常
    PATH_READ_FAIL(100100, "无法读取路径"),
    PATH_RESOURCE_NOT_EFFECTIVE(100101, "目录下无资源或并不是一个有效目录,请保证格式为:/(分类文件夹)/(图片资源)"),

    //Obj异常
    OBJ_READ_FAIL(100200, "图形资源数量小于创建验证码图形总数"),

    //运算类异常
    OPERRA_CAN_NOT_DO(100300, "运算公式出现错误,无法计算");

    int code;
    String msg;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    VerifyExceptionEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
