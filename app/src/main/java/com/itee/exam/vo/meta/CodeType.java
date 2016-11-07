package com.itee.exam.vo.meta;

/**
 * 验证码的用途类型
 * Created by moxin on 2015-08-23 0023.
 */
public enum CodeType {

    /**
     * 注册
     */
    REGISTER,
    /**
     * 找回密码
     */
    FIND_PASSWORD,
    /**
     * 更换手机号
     */
    CHANGE_MOBILE,
    /**
     * 添加银行卡
     */
    ADD_BANKCARD,
    /**
     * 更改支付密码
     */
    CHANGE_PAY_PASSWORD
}
