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
package net.ymate.platform.module.wechat.support;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import net.ymate.platform.module.wechat.IAccountDataProvider;
import net.ymate.platform.module.wechat.WeChat;
import net.ymate.platform.module.wechat.WeChat.WX_API;

import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSONObject;

/**
 * <p>
 * DefaultAccountDataProvider
 * </p>
 * <p>
 * 
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
 *          <td>2014年3月28日上午11:02:10</td>
 *          </tr>
 *          </table>
 */
public class DefaultAccountDataProvider implements IAccountDataProvider {

	private static final Log _LOG = LogFactory.getLog(DefaultAccountDataProvider.class);

	private static Map<String, Object> __CACHES = new ConcurrentHashMap<String, Object>();

	private static Map<String, AccountDataMeta> __accountCahces = new ConcurrentHashMap<String, AccountDataMeta>();

	private static Object __LOCK = new Object();

	/**
	 * 构造器
	 * 
	 * @param accountId 微信公众帐号ID
	 * @param appId 第三方应用唯一凭证
	 * @param appSecret 第三方应用唯一凭证密钥
	 * @param redirectURI OAuth授权后重定向的URL地址
	 */
	public DefaultAccountDataProvider(String accountId, String appId, String appSecret, String redirectURI) {
		if (StringUtils.isBlank(accountId)) {
			throw new NullArgumentException("accountId");
		}
		if (StringUtils.isBlank(appId)) {
			throw new NullArgumentException("appId");
		}
		if (StringUtils.isBlank(appSecret)) {
			throw new NullArgumentException("appSecret");
		}
		if (StringUtils.isBlank(redirectURI)) {
			throw new NullArgumentException("redirectURI");
		}
		//
		__accountCahces.put(accountId, new AccountDataMeta(accountId, appId, appSecret, redirectURI));
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.module.wechat.IAccountDataProvider#getAccountIds()
	 */
	public Set<String> getAccountIds() {
		return Collections.unmodifiableSet(__accountCahces.keySet());
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.module.wechat.IAccountDataProvider#getAccessToken(java.lang.String)
	 */
	public String getAccessToken(String accountId) throws Exception {
		// {"access_token":"ACCESS_TOKEN","expires_in":7200}
		JSONObject _accessToken = null;
		synchronized (__LOCK) {
			_accessToken = (JSONObject) __CACHES.get(accountId);
			//
			long _currentTime = System.currentTimeMillis();
			if (_accessToken == null || (_currentTime >= _accessToken.getLong("expires_time"))) {
				_accessToken = WeChat.__doCheckJsonResult(HttpClientHelper.doGet(WX_API.WX_ACCESS_TOKEN.concat("&appid=") + getAppId(accountId) + "&secret=" + getAppSecret(accountId), true));
				_accessToken.put("expires_time", _currentTime + _accessToken.getIntValue("expires_in") * 1000);
				__CACHES.put("WX_ACCESS_TOKEN", _accessToken);
				//
				_LOG.debug("AccessToken Has Expired, Get From Remote: " + _accessToken);
//			} else {
//				_LOG.debug("Get AccessToken From Cache: " + _accessToken);
			}
		}
		return _accessToken.getString("access_token");
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.module.wechat.IAccountDataProvider#getAppId(java.lang.String)
	 */
	public String getAppId(String accountId) {
		return __accountCahces.get(accountId).getAppId();
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.module.wechat.IAccountDataProvider#getAppSecret(java.lang.String)
	 */
	public String getAppSecret(String accountId) {
		return __accountCahces.get(accountId).getAppSecret();
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.module.wechat.IAccountDataProvider#getRedirectURI(java.lang.String)
	 */
	public String getRedirectURI(String accountId) {
		return __accountCahces.get(accountId).getRedirectURI();
	}

}

class AccountDataMeta {
	private String accountId;

	private String appId;

	private String appSecret;

	private String redirectURI;

	public AccountDataMeta(String accountId, String appId, String appSecret, String redirectURI) {
		this.accountId = accountId;
		this.appId = appId;
		this.appSecret = appSecret;
		this.redirectURI = redirectURI;
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

	public String getRedirectURI() {
		return redirectURI;
	}

	public void setRedirectURI(String redirectURI) {
		this.redirectURI = redirectURI;
	}

}
