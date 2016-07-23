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
package net.ymate.module.wechat.base;

import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;

import javax.net.ssl.SSLContext;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 刘镇 (suninformation@163.com) on 16/5/23 下午6:44
 * @version 1.0
 */
public class WechatAccountMeta implements Serializable {

    private static final Log _LOG = LogFactory.getLog(WechatAccountMeta.class);

    // 微信公众帐号原始ID
    private String accountId;

    // 第三方应用唯一凭证
    private String appId;

    // 第三方应用唯一凭证密钥
    private String appSecret;

    // 消息加密密钥由43位字符组成，可随机修改，字符范围为A-Z，a-z，0-9
    private String appAesKey;

    // 上一次使用的消息加密密钥（备用）
    private String lastAppAesKey;

    // 是否采用消息加密
    private boolean isMsgEncrypted;

    // OAuth授权后重定向的URL地址
    private String redirectUri;

    // 公众号类型
    private int type;

    // 是否已认证
    private boolean isVerified;

    /**
     * 微信接入唯一标识
     */
    private String token;

    // 支付相关参数------

    // 商户号
    private String mchId;

    // 商号密钥
    private String mchKey;

    // 证书文件路径
    private String certFilePath;

    private SSLConnectionSocketFactory connectionSocketFactory;

    // 异步通知URL
    private String notifyUrl;

    // 自定义扩展属性映射
    private Map<String, String> attributes = new HashMap<String, String>();

    public WechatAccountMeta(String accountId, String appId, String appSecret, String appAesKey, String redirectUri, int type, boolean isVerfied, String token) {
        if (StringUtils.isBlank(accountId)) {
            throw new NullArgumentException("accountId");
        }
        if (StringUtils.isBlank(appId)) {
            throw new NullArgumentException("appId");
        }
        if (StringUtils.isBlank(appSecret)) {
            throw new NullArgumentException("appSecret");
        }
        if (StringUtils.isBlank(token)) {
            throw new NullArgumentException("token");
        }
        //
        this.accountId = accountId;
        this.appId = appId;
        this.appSecret = appSecret;
        this.appAesKey = appAesKey;
        this.redirectUri = redirectUri;
        this.type = type;
        this.isVerified = isVerfied;
        this.token = token;
    }

    public WechatAccountMeta(String accountId, String appId, String appSecret, String appAesKey, String redirectUri, int type, boolean isVerfied, String token, boolean isMsgEncrypted) {
        this(accountId, appId, appSecret, appAesKey, redirectUri, type, isVerfied, token);
        this.isMsgEncrypted = isMsgEncrypted;
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

    public String getLastAppAesKey() {
        return lastAppAesKey;
    }

    public void setLastAppAesKey(String lastAppAesKey) {
        this.lastAppAesKey = lastAppAesKey;
    }

    public boolean isMsgEncrypted() {
        return isMsgEncrypted;
    }

    public void setMsgEncrypted(boolean msgEncrypted) {
        isMsgEncrypted = msgEncrypted;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getMchKey() {
        return mchKey;
    }

    public void setMchKey(String mchKey) {
        this.mchKey = mchKey;
    }

    public String getCertFilePath() {
        return certFilePath;
    }

    public void setCertFilePath(String certFilePath) {
        this.certFilePath = certFilePath;
    }

    public SSLConnectionSocketFactory getConnectionSocketFactory() {
        if (StringUtils.isNotBlank(this.mchId)
                && StringUtils.isNotBlank(this.certFilePath) && this.connectionSocketFactory == null) {
            try {
                // 指定读取证书格式为PKCS12
                KeyStore _keyStore = KeyStore.getInstance("PKCS12");
                // 读取PKCS12证书文件流
                InputStream _certFileStream = new URL(this.certFilePath).openStream();
                //
                char[] _mchIdChars = this.mchId.toCharArray();
                try {
                    // 指定PKCS12的密码(商户ID)
                    _keyStore.load(_certFileStream, _mchIdChars);
                } finally {
                    _certFileStream.close();
                }
                SSLContext _sslContext = SSLContexts.custom().loadKeyMaterial(_keyStore, _mchIdChars).build();
                // 指定TLS版本
                connectionSocketFactory = new SSLConnectionSocketFactory(
                        _sslContext, new String[]{"TLSv1"}, null,
                        SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
            } catch (Exception e) {
                _LOG.warn("", e);
            }
        }
        return connectionSocketFactory;
    }

    public void setConnectionSocketFactory(SSLConnectionSocketFactory connectionSocketFactory) {
        this.connectionSocketFactory = connectionSocketFactory;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void addAttribute(String key, String value) {
        attributes.put(key, value);
    }

    public String getAttribute(String key) {
        return attributes.get(key);
    }
}
