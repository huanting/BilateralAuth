package com.hat.baserver.bean;

import com.hat.baserver.constant.ResultEnum;

/**
 * Description: http返回最外层的结果
 * User: Administrator
 * Date: 2018/5/19 15:10
 */
public class Result {
    //错误码
    private String resultCode;
    //错误信息
    private String resultMsg;
    //当前消息id
    private String traceNo;

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setResult(ResultEnum resultEnum) {
        resultCode = resultEnum.getCode()+"";
        resultMsg = resultEnum.getMessage();
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public String getTraceNo() {
        return traceNo;
    }

    public void setTraceNo(String traceNo) {
        this.traceNo = traceNo;
    }
}
