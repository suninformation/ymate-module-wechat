/*
 * Copyright 2007-2107 the original author or authors.
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
package net.ymate.platform.module.wechat;

import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * AccountDataMeta
 * </p>
 * <p>
 * <p/>
 * </p>
 *
 * @author 刘镇(suninformation@163.com)
 * @version 0.0.0
 *          <table style="border:1px solid gray;">
 *          <tr>
 *          <th width="100px">版本号</th><th width="100px">动作</th><th
 *          width="100px">修改人</th><th width="100px">修改时间</th>
 *          </tr>
 *          <!-- 以 Table 方式书写修改历史 -->
 *          <tr>
 *          <td>0.0.0</td>
 *          <td>创建类</td>
 *          <td>刘镇</td>
 *          <td>2014年4月17日下午3:10:36</td>
 *          </tr>
 *          </table>
 */
public class AccountDataMeta {

    private String accountId;

    private String appId;

    private String appSecret;

    private String appAesKey;

    private String redirectUri;

    private String accessToken;

    private long accessTokenExpires;

    private int type;

    private int isVerified;

    private Map<String, String> attributes = new ConcurrentHashMap<String, String>();;

    /**
     * 构造器
     */
    public AccountDataMeta() {
    }

    /**
     * 构造器
     *
     * @param accountId   微信公众帐号原始ID
     * @param appId       第三方应用唯一凭证
     * @param appAesKey
     * @param appSecret   第三方应用唯一凭证密钥
     * @param redirectUri OAuth授权后重定向的URL地址
     * @param type
     * @param isVerfied
     */
    public AccountDataMeta(String accountId, String appId, String appSecret, String appAesKey, String redirectUri, int type, int isVerfied) {
        if (StringUtils.isBlank(accountId)) {
            throw new NullArgumentException("accountId");
        }
        if (StringUtils.isBlank(appId)) {
            throw new NullArgumentException("appId");
        }
        if (StringUtils.isBlank(appSecret)) {
            throw new NullArgumentException("appSecret");
        }
        //
        this.accountId = accountId;
        this.appId = appId;
        this.appSecret = appSecret;
        this.appAesKey = appAesKey;
        this.redirectUri = redirectUri;
        this.type = type;
        this.isVerified = isVerfied;
    }

    public AccountDataMeta(String accountId, String appId, String appSecret, String appAesKey, String redirectUri, int type, int isVerfied, String accessToken, long accessTokenExpires) {
        this(accountId, appId, appSecret, appAesKey, redirectUri, type, isVerfied);
        this.accessToken = accessToken;
        this.accessTokenExpires = accessTokenExpires;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getAppAesKey() {
        return appAesKey;
    }

    public void setAppAesKey(String appAesKey) {
        this.appAesKey = appAesKey;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public long getAccessTokenExpires() {
        return accessTokenExpires;
    }

    public void setAccessTokenExpires(long accessTokenExpires) {
        this.accessTokenExpires = accessTokenExpires;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(int isVerified) {
        this.isVerified = isVerified;
    }

    public void addAttribute(String key, String value) {
        attributes.put(key, value);
    }

    public String getAttribute(String key) {
        return attributes.get(key);
    }
}
