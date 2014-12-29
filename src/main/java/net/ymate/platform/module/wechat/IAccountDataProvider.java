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
     * 初始化
     *
     * @throws Exception
     */
    public void initialize() throws Exception;

    /**
     * 销毁
     *
     * @throws Exception
     */
    public void destroy() throws Exception;

    /**
     * 注册公众帐号
     *
     * @param account 微信帐号数据对象
     * @throws Exception
     */
    public void registerAccount(AccountDataMeta account) throws Exception;

    /**
     * @param accountId 微信帐号原始ID
     * @return 移除并返回公众帐号数据对象，若不存在则返回空
     */
    public AccountDataMeta unregisterAccount(String accountId);

    /**
     * @return 返回当前维护的微信帐号原始ID集合
     */
    public Set<String> getAccountIds();

    /**
     * @param accountId 微信帐号原始ID
     * @return 检测微信帐号accountId是否有效(常规实现方式为判断是否存在于当前服务的原始ID集合中)
     */
    public boolean checkAccountValid(String accountId);

    /**
     * @param accountId 微信帐号原始ID
     * @return 通过微信帐号获取AccessToken，在有效期内将被缓存，过期后会重新获取新的Token
     * @throws Exception
     */
    public String getAccessToken(String accountId) throws Exception;

    /**
     * @param accountId 微信帐号原始ID
     * @return 返回第三方应用唯一凭证
     */
    public String getAppId(String accountId);

    /**
     * @param accountId 微信帐号原始ID
     * @return 返回第三方应用唯一凭证密钥
     */
    public String getAppSecret(String accountId);

    /**
     * @param accountId 微信帐号原始ID
     * @return 返回消息加密密钥
     */
    public String getAppAesKey(String accountId);

    /**
     * @param accountId 微信帐号原始ID
     * @return 返回上一次使用的消息加密密钥（备用）
     */
    public String getLastAppAesKey(String accountId);

    /**
     * @param accountId 微信帐号原始ID
     * @return 是否采用消息加密
     */
    public int isMsgEncrypted(String accountId);

    /**
     * @param accountId 微信帐号原始ID
     * @return 返回微信支付分配的商户ID
     */
    public String getMchId(String accountId);

    /**
     * @param accountId 微信帐号原始ID
     * @return 返回商户支付密钥
     */
    public String getMchKey(String accountId);

    /**
     * @param accountId 微信帐号原始ID
     * @return 返回微支付通用通知回调URL地址
     */
    public String getNofityUrl(String accountId);

    /**
     * @param accountId 微信帐号原始ID
     * @return OAuth授权后重定向的URL地址
     */
    public String getRedirectURI(String accountId);

    /**
     * @param accountId 微信帐号原始ID
     * @return 公众号类型
     */
    public int getType(String accountId);

    /**
     * @param accountId 微信帐号原始ID
     * @return 公众号是否已认证
     */
    public boolean isVerified(String accountId);

    /**
     * @param accountId 微信帐号原始ID
     * @param attrKey   自定义属性KEY
     * @return 获取公从号自定义属性
     */
    public String getAttribute(String accountId, String attrKey);

}
