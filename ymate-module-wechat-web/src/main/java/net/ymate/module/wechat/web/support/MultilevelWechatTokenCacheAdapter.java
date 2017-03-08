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
package net.ymate.module.wechat.web.support;

import net.ymate.framework.commons.ReentrantLockHelper;
import net.ymate.module.wechat.AbstractWechatTokenCacheAdapter;
import net.ymate.module.wechat.IWechat;
import net.ymate.module.wechat.base.WechatAccessToken;
import net.ymate.module.wechat.base.WechatAccountMeta;
import net.ymate.module.wechat.base.WechatTicket;
import net.ymate.module.wechat.model.WechatToken;
import net.ymate.platform.cache.Caches;
import net.ymate.platform.cache.ICaches;
import net.ymate.platform.core.util.RuntimeUtils;
import net.ymate.platform.persistence.Fields;
import net.ymate.platform.persistence.jdbc.query.IDBLocker;
import net.ymate.platform.persistence.jdbc.transaction.Trade;
import net.ymate.platform.persistence.jdbc.transaction.Transactions;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author 刘镇 (suninformation@163.com) on 16/7/12 上午1:32
 * @version 1.0
 */
public class MultilevelWechatTokenCacheAdapter extends AbstractWechatTokenCacheAdapter {

    private static final Log _LOG = LogFactory.getLog(MultilevelWechatTokenCacheAdapter.class);

    private ICaches __caches;

    private String __TOKEN_CACHE_NAME;
    private String __TICKET_CACHE_NAME;

    private static ReentrantLockHelper __LOCKS = new ReentrantLockHelper();

    @Override
    public void init(IWechat owner) throws Exception {
        super.init(owner);
        __caches = Caches.get(owner.getOwner());
        //
        String _cacheNamePrefix = StringUtils.trimToEmpty(owner.getOwner().getConfig().getParam(IWechat.MODULE_NAME.concat(".cache_name_prefix")));
        __TOKEN_CACHE_NAME = _cacheNamePrefix.toUpperCase().concat("_WECHAT_TOKEN_CACHE");
        __TICKET_CACHE_NAME = _cacheNamePrefix.toUpperCase().concat("_WECHAT_TICKET_CACHE");
    }

    public WechatAccessToken getAccessToken(WechatAccountMeta accountMeta) {
        String _cacheKey = accountMeta.getAppId().concat("_token");
        WechatAccessToken _token = (WechatAccessToken) __caches.get(__TOKEN_CACHE_NAME, _cacheKey);
        if (_token == null || _token.isExpired()) {
            ReentrantLock _locker = __LOCKS.getLocker(_cacheKey);
            _locker.lock();
            try {
                _token = __doGetAccessTokenFromDB(_cacheKey, accountMeta);
            } catch (Exception e) {
                try {
                    _token = __doGetAccessTokenFromDB(_cacheKey, accountMeta);
                } catch (Exception ex) {
                    _LOG.warn("", RuntimeUtils.unwrapThrow(ex));
                }
            } finally {
                _locker.unlock();
            }
        }
        return _token;
    }

    private WechatAccessToken __doGetAccessTokenFromDB(final String cacheKey, final WechatAccountMeta accountMeta) throws Exception {
        return Transactions.execute(new Trade<WechatAccessToken>() {
            @Override
            public void deal() throws Throwable {
                String _id = DigestUtils.md5Hex(accountMeta.getAccountId());
                WechatToken _token = WechatToken.builder().id(_id)
                        .build().load(Fields.create(WechatToken.FIELDS.ID, WechatToken.FIELDS.ACCESS_TOKEN, WechatToken.FIELDS.ACCESS_TOKEN_EXPIRED_TIME), IDBLocker.MYSQL);
                WechatAccessToken _accessToken = null;
                Fields _fields = Fields.create();
                boolean _isNew = _token == null;
                if (_isNew) {
                    _token = WechatToken.builder()
                            .id(_id)
                            .accountId(accountMeta.getAccountId())
                            .siteId(accountMeta.getAttribute("site_id"))
                            .build();
                    _fields.add(Fields.create(WechatToken.FIELDS.ID, WechatToken.FIELDS.ACCOUNT_ID, WechatToken.FIELDS.SITE_ID));
                } else {
                    _accessToken = new WechatAccessToken(_token.getAccessToken(), _token.getAccessTokenExpiredTime());
                }
                if (_isNew || _accessToken.isExpired()) {
                    _accessToken = __doGetAccessToken(accountMeta.getAppId(), accountMeta.getAppSecret());
                    if (_accessToken != null) {
                        _token.bind().accessToken(_accessToken.getToken())
                                .accessTokenExpiredTime(_accessToken.getExpiredTime())
                                .lastModifyTime(System.currentTimeMillis())
                                .build();
                        _fields.add(Fields.create(WechatToken.FIELDS.ACCESS_TOKEN, WechatToken.FIELDS.ACCESS_TOKEN_EXPIRED_TIME, WechatToken.FIELDS.LAST_MODIFY_TIME));
                        if (_isNew) {
                            _token.save(_fields);
                        } else {
                            _token.update(_fields);
                        }
                    }
                }
                if (_accessToken != null) {
                    __caches.put(__TOKEN_CACHE_NAME, cacheKey, _accessToken);
                    this.setReturns(_accessToken);
                }
            }
        });
    }

    public WechatTicket getJsTicket(WechatAccountMeta accountMeta) {
        String _cacheKey = accountMeta.getAppId().concat("_ticket");
        WechatTicket _ticket = (WechatTicket) __caches.get(__TICKET_CACHE_NAME, _cacheKey);
        if (_ticket == null || _ticket.isExpired()) {
            ReentrantLock _locker = __LOCKS.getLocker(_cacheKey);
            _locker.lock();
            try {
                _ticket = __doGetTicketFromDB(_cacheKey, accountMeta);
            } catch (Exception e) {
                try {
                    _ticket = __doGetTicketFromDB(_cacheKey, accountMeta);
                } catch (Exception ex) {
                    _LOG.warn("", RuntimeUtils.unwrapThrow(ex));
                }
            } finally {
                _locker.unlock();
            }
        }
        return _ticket;
    }

    private WechatTicket __doGetTicketFromDB(final String cacheKey, final WechatAccountMeta accountMeta) throws Exception {
        return Transactions.execute(new Trade<WechatTicket>() {
            @Override
            public void deal() throws Throwable {
                String _id = DigestUtils.md5Hex(accountMeta.getAccountId());
                WechatToken _token = WechatToken.builder().id(_id)
                        .build().findFirst(Fields.create(WechatToken.FIELDS.ID, WechatToken.FIELDS.JS_TICKET, WechatToken.FIELDS.JS_TICKET_EXPIRED_TIME), IDBLocker.MYSQL);
                WechatTicket _ticket = null;
                Fields _fields = Fields.create();
                boolean _isNew = _token == null;
                if (_isNew) {
                    _token = WechatToken.builder()
                            .id(_id)
                            .accountId(accountMeta.getAccountId())
                            .siteId(accountMeta.getAttribute("site_id"))
                            .build();
                    _fields.add(WechatToken.FIELDS.ID, WechatToken.FIELDS.ACCOUNT_ID, WechatToken.FIELDS.SITE_ID);
                } else {
                    _ticket = new WechatTicket(_token.getJsTicket(), _token.getJsTicketExpiredTime());
                }
                if (_isNew || _ticket.isExpired()) {
                    _ticket = __doGetJsTicket(getAccessToken(accountMeta).getToken());
                    if (_ticket != null) {
                        _token.bind().jsTicket(_ticket.getTicket())
                                .jsTicketExpiredTime(_ticket.getExpiredTime())
                                .lastModifyTime(System.currentTimeMillis())
                                .build();
                        _fields.add(Fields.create(WechatToken.FIELDS.JS_TICKET, WechatToken.FIELDS.JS_TICKET_EXPIRED_TIME, WechatToken.FIELDS.LAST_MODIFY_TIME));
                        if (_isNew) {
                            _token.save(_fields);
                        } else {
                            _token.update(_fields);
                        }
                    }
                }
                if (_ticket != null) {
                    __caches.put(__TICKET_CACHE_NAME, cacheKey, _ticket);
                    this.setReturns(_ticket);
                }
            }
        });
    }
}
