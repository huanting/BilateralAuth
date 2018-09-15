package com.hat.baserver.bean;

/**
 * Description:
 * User: Administrator
 * Date: 2018/5/19 15:50
 */
public class VerifyLoginMobile extends Result {
    //是否有登录密码
    private int hasLoginPwd;
    //是否实名
    private int isRealName;
    //是否已经注册
    private int isRegistered;
    //是否是静默注册
    private int isSilentRegistered;

    public int getHasLoginPwd() {
        return hasLoginPwd;
    }

    public void setHasLoginPwd(int hasLoginPwd) {
        this.hasLoginPwd = hasLoginPwd;
    }

    public int getIsRealName() {
        return isRealName;
    }

    public void setIsRealName(int isRealName) {
        this.isRealName = isRealName;
    }

    public int getIsRegistered() {
        return isRegistered;
    }

    public void setIsRegistered(int isRegistered) {
        this.isRegistered = isRegistered;
    }

    public int getIsSilentRegistered() {
        return isSilentRegistered;
    }

    public void setIsSilentRegistered(int isSilentRegistered) {
        this.isSilentRegistered = isSilentRegistered;
    }

}
