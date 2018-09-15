package com.hat.baserver.constant;

/**
 * description:
 * Author: anting.hu
 * date: 2018/5/30 22:46
 */
public enum ResultEnum {
    SUCCESS_CODE(1000, "成功"),
    ERROR_PHONE_NUMBER_FORMAT(1001, "手机号码格式错误"),
    ERROR_CAPTCHA(1002, "验证码错误"),
    ERROR_SMS_CODE(1003, "短信验证码错误"),
    ERROR_SECURITY(1004, "秘钥错误"),
    ERROR_DB(1005, "数据库错误"),
    ERROR_KICKOUT(1006, "您已经在其他地方登录，请重新登录！"),
    ERROR_PASSWORD(1007, "密码错误"),
    ERROR_TRY_TOOMANY(1008,"尝试次数过多"),
    ERROR_UNKNOWN_PHONE(1009,"不存在的手机号码"),
    ERROR_SMS_SEND(1010,"无法发送短信验证码"),
    ERROR_LOGIN(1011,"您目前未登录"),
    ERROR_RENTUSER(1012,"租客未注册"),
    ERROR_UNKNOWN(-1, "未知错误"),
    ERROR_VERIFY(02,"验证不通过"),
    ERROR_NAME(204,"姓名错误"),
    ERROR_ID(205,"身份证错误"),
    ERROR_PHONE_NUMBER(206,"手机号错误");


    private int code;
    private String message;

    ResultEnum(int code, String msg) {
        this.code = code;
        this.message = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
