package com.hat.baserver.services;

import com.hat.baserver.bean.VerifyLoginMobile;
import com.hat.baserver.constant.ResultEnum;
import com.hat.baserver.pojo.ReqVerifyLoginMobileInfo;
import com.hat.baserver.utils.ResultUtil;
import com.hat.baserver.utils.Tools;
import org.springframework.stereotype.Service;

/**
 * description:
 * author: DuzhenTong
 * date: 2018/9/8 下午2:42
 */

@Service
public class LoginService {
    //根据图形验证码验证手机
    public Object verifyLoginMobile(ReqVerifyLoginMobileInfo loginMobileInfo) {
        //手机号格式错误
        if(!Tools.checkMobileNumber(loginMobileInfo.getPhoneNumber()))
            return ResultUtil.onError(ResultEnum.ERROR_PHONE_NUMBER_FORMAT);

        VerifyLoginMobile verifyLoginMobile = new VerifyLoginMobile();
        verifyLoginMobile.setHasLoginPwd(1);
        verifyLoginMobile.setIsRealName(0);
        verifyLoginMobile.setIsRegistered(1);
        verifyLoginMobile.setIsSilentRegistered(0);
        return ResultUtil.onSuccess(verifyLoginMobile);
    }
}
