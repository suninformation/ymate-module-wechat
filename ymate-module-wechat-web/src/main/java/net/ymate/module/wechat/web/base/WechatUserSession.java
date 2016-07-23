/*
 * Copyright 2007-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.ymate.module.wechat.web.base;

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

/**
 * @author 刘镇 (suninformation@163.com) on 16/7/5 下午5:15
 * @version 1.0
 */
public class WechatUserSession implements Serializable {

    /**
     * 微信用户记录ID
     */
    private String id;

    /**
     * 微信用户openid
     */
    private String openId;

    /**
     * 微信账户id
     */
    private String accountId;

    //
    private String oauthAccessToken;
    private long oauthExpiredTime;
    private String oauthScope;

    private long lastModifyTime;

    public String getId() {
        return id;
    }

    public WechatUserSession setId(String id) {
        this.id = id;
        return this;
    }

    public String getOpenId() {
        return openId;
    }

    public WechatUserSession setOpenId(String openId) {
        this.openId = openId;
        return this;
    }

    public String getAccountId() {
        return accountId;
    }

    public WechatUserSession setAccountId(String accountId) {
        this.accountId = accountId;
        return this;
    }

    public String getOauthAccessToken() {
        return oauthAccessToken;
    }

    public WechatUserSession setOauthAccessToken(String oauthAccessToken) {
        this.oauthAccessToken = oauthAccessToken;
        return this;
    }

    public long getOauthExpiredTime() {
        return oauthExpiredTime;
    }

    public WechatUserSession setOauthExpiredTime(long oauthExpiredTime) {
        this.oauthExpiredTime = oauthExpiredTime;
        return this;
    }

    public String getOauthScope() {
        return oauthScope;
    }

    public WechatUserSession setOauthScope(String oauthScope) {
        this.oauthScope = oauthScope;
        return this;
    }

    public long getLastModifyTime() {
        return lastModifyTime;
    }

    public WechatUserSession setLastModifyTime(long lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
        return this;
    }

    public boolean checkStatus() {
        return StringUtils.isNotBlank(this.oauthAccessToken) && System.currentTimeMillis() < this.oauthExpiredTime;
    }

    public boolean checkOauthScope(String needOauthScope) {
        if (this.oauthScope.equals(needOauthScope)) {
            return true;
        } else if ((this.oauthScope.equals("snsapi_userinfo") && needOauthScope.equals("snsapi_base"))) {
            return true;
        }
        return false;
    }
}
