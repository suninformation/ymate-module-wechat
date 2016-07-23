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

import net.ymate.framework.commons.ReentrantLockHelper;
import net.ymate.module.wechat.AbstractWechatTokenCacheAdapter;
import net.ymate.module.wechat.base.WechatAccessToken;
import net.ymate.module.wechat.base.WechatAccountMeta;
import net.ymate.module.wechat.base.WechatTicket;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author 刘镇 (suninformation@163.com) on 16/5/26 上午3:32
 * @version 1.0
 */
public class DefaultWechatTokenCacheAdapter extends AbstractWechatTokenCacheAdapter {

    private static final Log _LOG = LogFactory.getLog(DefaultWechatTokenCacheAdapter.class);

    private Map<String, WechatAccessToken> __TOKEN_CACHES = new ConcurrentHashMap<String, WechatAccessToken>();

    private Map<String, WechatTicket> __TICKET_CACHES = new ConcurrentHashMap<String, WechatTicket>();

    private static ReentrantLockHelper __LOCKS = new ReentrantLockHelper();

    public WechatAccessToken getAccessToken(WechatAccountMeta accountMeta) {
        WechatAccessToken _token = __TOKEN_CACHES.get(accountMeta.getAppId());
        if (_token == null || _token.isExpired()) {
            ReentrantLock _locker = __LOCKS.getLocker(accountMeta.getAppId().concat("_token"));
            _locker.lock();
            try {
                _token = __doGetAccessToken(accountMeta.getAppId(), accountMeta.getAppSecret());
                if (_token != null) {
                    __TOKEN_CACHES.put(accountMeta.getAppId(), _token);
                }
            } catch (Exception e) {
                try {
                    _token = __doGetAccessToken(accountMeta.getAppId(), accountMeta.getAppSecret());
                    if (_token != null) {
                        __TOKEN_CACHES.put(accountMeta.getAppId(), _token);
                    }
                } catch (Exception ex) {
                    _LOG.warn("", ex);
                }
            } finally {
                _locker.unlock();
            }
        }
        return _token;
    }

    public WechatTicket getJsTicket(WechatAccountMeta accountMeta) {
        WechatTicket _ticket = __TICKET_CACHES.get(accountMeta.getAppId());
        if (_ticket == null || _ticket.isExpired()) {
            ReentrantLock _locker = __LOCKS.getLocker(accountMeta.getAppId().concat("_ticket"));
            _locker.lock();
            String _accessToken = getAccessToken(accountMeta).getToken();
            try {
                _ticket = __doGetJsTicket(_accessToken);
                if (_ticket != null) {
                    __TICKET_CACHES.put(accountMeta.getAppId(), _ticket);
                }
            } catch (Exception e) {
                try {
                    _ticket = __doGetJsTicket(_accessToken);
                    if (_ticket != null) {
                        __TICKET_CACHES.put(accountMeta.getAppId(), _ticket);
                    }
                } catch (Exception ex) {
                    _LOG.warn("", ex);
                }
            } finally {
                _locker.unlock();
            }
        }
        return _ticket;
    }
}
