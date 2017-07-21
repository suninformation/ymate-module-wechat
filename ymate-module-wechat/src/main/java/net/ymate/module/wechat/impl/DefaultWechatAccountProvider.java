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
package net.ymate.module.wechat.impl;

import net.ymate.module.wechat.IWechat;
import net.ymate.module.wechat.IWechatAccountProvider;
import net.ymate.module.wechat.base.WechatAccountMeta;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 刘镇 (suninformation@163.com) on 16/5/24 上午1:18
 * @version 1.0
 */
public class DefaultWechatAccountProvider implements IWechatAccountProvider {

    private static Map<String, WechatAccountMeta> __CACHES = new ConcurrentHashMap<String, WechatAccountMeta>();

    private static Map<String, WechatAccountMeta> __APPID_CACHES = new ConcurrentHashMap<String, WechatAccountMeta>();

    private static Map<String, WechatAccountMeta> __TOKEN_CACHES = new ConcurrentHashMap<String, WechatAccountMeta>();

    public void init(IWechat owner) throws Exception {
    }

    public void registerAccount(WechatAccountMeta accountMeta) {
        __CACHES.put(accountMeta.getAccountId(), accountMeta);
        __APPID_CACHES.put(accountMeta.getAppId(), accountMeta);
        __TOKEN_CACHES.put(accountMeta.getToken(), accountMeta);
    }

    public WechatAccountMeta unregisterAccount(String accountId) {
        WechatAccountMeta _meta = __CACHES.remove(accountId);
        __APPID_CACHES.remove(_meta.getAppId());
        __TOKEN_CACHES.remove(_meta.getToken());
        return _meta;
    }

    public Collection<String> getAccountIds() {
        return __CACHES.keySet();
    }

    public boolean hasAccountId(String accountId) {
        return __CACHES.containsKey(accountId);
    }

    public WechatAccountMeta getAccountMetaByToken(String token) {
        return __TOKEN_CACHES.get(token);
    }

    public WechatAccountMeta getAccountMetaByAccountId(String accountId) {
        return __CACHES.get(accountId);
    }

    public WechatAccountMeta getAccountMetaByAppId(String appId) {
        return __APPID_CACHES.get(appId);
    }
}
