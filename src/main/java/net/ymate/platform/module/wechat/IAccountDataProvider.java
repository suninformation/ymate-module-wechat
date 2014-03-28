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

import java.util.Set;

/**
 * <p>
 * IAccountDataProvider
 * </p>
 * <p>
 * 微信多帐号数据提供者接口定义；
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
 *          <td>2014年3月28日上午10:46:48</td>
 *          </tr>
 *          </table>
 */
public interface IAccountDataProvider {

	/**
	 * @return 返回当前维护的微信帐号ID集合
	 */
	public Set<String> getAccountIds();

	/**
	 * @param accountId 微信帐号
	 * @return 通过微信帐号获取AccessToken，在有效期内将被缓存，过期后会重新获取新的Token
	 * @throws Exception
	 */
	public String getAccessToken(String accountId) throws Exception;

	/**
	 * @param accountId 微信帐号
	 * @return 返回第三方应用唯一凭证
	 */
	public String getAppId(String accountId);

	/**
	 * @param accountId 微信帐号
	 * @return 返回第三方应用唯一凭证密钥
	 */
	public String getAppSecret(String accountId);

	/**
	 * @param accountId 微信帐号
	 * @return OAuth授权后重定向的URL地址
	 */
	public String getRedirectURI(String accountId);

}
