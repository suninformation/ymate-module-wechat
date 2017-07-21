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
package net.ymate.module.wechat;

import net.ymate.module.wechat.base.WechatAccountMeta;

import java.util.Collection;

/**
 * @author 刘镇 (suninformation@163.com) on 16/5/23 下午5:58
 * @version 1.0
 */
public interface IWechatAccountProvider {

    void init(IWechat owner) throws Exception;

    void registerAccount(WechatAccountMeta accountMeta);

    WechatAccountMeta unregisterAccount(String accountId);

    Collection<String> getAccountIds();

    boolean hasAccountId(String accountId);

    WechatAccountMeta getAccountMetaByToken(String token);

    WechatAccountMeta getAccountMetaByAccountId(String accountId);

    WechatAccountMeta getAccountMetaByAppId(String appId);
}
