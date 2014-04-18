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

/**
 * <p>
 * AccountDataMeta
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
 *          <td>2014年4月17日下午3:10:36</td>
 *          </tr>
 *          </table>
 */
public class AccountDataMeta {

	private String accountId;

	private String appId;

	private String appSecret;

	private String redirectURI;

	/**
	 * 构造器
	 * 
	 * @param accountId 微信公众帐号原始ID
	 * @param appId 第三方应用唯一凭证
	 * @param appSecret 第三方应用唯一凭证密钥
	 * @param redirectURI OAuth授权后重定向的URL地址
	 */
	public AccountDataMeta(String accountId, String appId, String appSecret, String redirectURI) {
		if (StringUtils.isBlank(accountId)) {
			throw new NullArgumentException("accountId");
		}
		//
		this.accountId = accountId;
		this.appId = appId;
		this.appSecret = appSecret;
		this.redirectURI = redirectURI;
	}

	public String getAccountId() {
		return accountId;
	}

	public String getAppId() {
		return appId;
	}

	public String getAppSecret() {
		return appSecret;
	}

	public String getRedirectURI() {
		return redirectURI;
	}

}
