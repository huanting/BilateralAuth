package com.hat.baserver.utils;

import com.alibaba.fastjson.JSON;
import com.hat.baserver.bean.Result;
import com.hat.baserver.constant.ResultEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Description:
 * User: Administrator
 * Date: 2018/5/19 15:13
 */
public class ResultUtil {
    private static Logger logger=LoggerFactory.getLogger(ResultUtil.class);
    private static final int TOKEN_LEN = 8;

    public static String onSuccess(Result result) {
        result.setResult(ResultEnum.SUCCESS_CODE);

        result.setToken(Tools.getRandomString(TOKEN_LEN));
        return JSON.toJSONString(result);
    }

    public static String onError(ResultEnum resultEnum) {
        Result result = new Result();
        result.setResult(resultEnum);
        result.setTraceNo("111111");
        result.setToken(Tools.getRandomString(TOKEN_LEN));
        return JSON.toJSONString(result);
    }

    public static String onError(int errorCode, String errMsg) {
        Result result = new Result();
        result.setResultCode(errorCode+"");
        result.setResultMsg(errMsg);
        result.setTraceNo("111111");
        result.setToken(Tools.getRandomString(TOKEN_LEN));
        return JSON.toJSONString(result);
    }
}
