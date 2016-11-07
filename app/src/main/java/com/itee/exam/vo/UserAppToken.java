package com.itee.exam.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by rkcoe on 2016/3/17.
 */
public class UserAppToken implements Serializable{
    private static final long serialVersionUID = 1L;

    private int userId;
    private String appKey;
    private String appToken;
    private Date createTime;
    private long valideTime;//有效时间

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppToken() {
        return appToken;
    }

    public void setAppToken(String appToken) {
        this.appToken = appToken;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public long getValideTime() {
        return valideTime;
    }

    public void setValideTime(long valideTime) {
        this.valideTime = valideTime;
    }
}
