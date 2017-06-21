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
import net.ymate.module.wechat.IWechat;
import net.ymate.module.wechat.IWechatAccountProvider;
import net.ymate.module.wechat.base.WechatAccountMeta;
import net.ymate.module.wechat.model.WechatAccount;
import net.ymate.platform.cache.Caches;
import net.ymate.platform.cache.ICaches;
import net.ymate.platform.core.lang.BlurObject;
import net.ymate.platform.core.util.RuntimeUtils;
import net.ymate.platform.persistence.Fields;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Collection;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author 刘镇 (suninformation@163.com) on 16/7/12 下午3:07
 * @version 1.0
 */
public class MultilevelWechatAccountProvider implements IWechatAccountProvider {

    private static final Log _LOG = LogFactory.getLog(MultilevelWechatAccountProvider.class);

    private ICaches __caches;

    private String __ACCOUNT_CACHE_NAME;

    private static ReentrantLockHelper __LOCKS = new ReentrantLockHelper();

    public void init(IWechat owner) throws Exception {
        __caches = Caches.get(owner.getOwner());
        __ACCOUNT_CACHE_NAME = StringUtils.trimToEmpty(owner.getOwner().getConfig().getParam(IWechat.MODULE_NAME.concat(".cache_name_prefix"))).concat("_WECHAT_ACCOUNT_CACHE");
    }

    public void registerAccount(WechatAccountMeta accountMeta) {
        __caches.put(__ACCOUNT_CACHE_NAME, accountMeta.getAccountId(), accountMeta);
        __caches.put(__ACCOUNT_CACHE_NAME.concat("_TOKEN"), accountMeta.getToken(), accountMeta.getAccountId());
    }

    public WechatAccountMeta unregisterAccount(String accountId) {
        WechatAccountMeta _meta = (WechatAccountMeta) __caches.get(__ACCOUNT_CACHE_NAME, accountId);
        __caches.remove(__ACCOUNT_CACHE_NAME, accountId);
        __caches.remove(__ACCOUNT_CACHE_NAME.concat("_TOKEN"), _meta.getToken());
        return _meta;
    }

    private WechatAccountMeta __doBuildAccountMeta(WechatAccount _account) {
        if (_account != null) {
            WechatAccountMeta _meta = new WechatAccountMeta(_account.getAccountId(),
                    _account.getAppId(),
                    _account.getAppSecret(),
                    _account.getAppAesKey(),
                    _account.getRedirectUri(),
                    _account.getType(),
                    BlurObject.bind(_account.getIsVerified()).toBooleanValue(),
                    _account.getToken(),
                    BlurObject.bind(_account.getIsMsgEncrypted()).toBooleanValue());
            _meta.setLastAppAesKey(_account.getLastAppAesKey());
            _meta.addAttribute("site_id", _account.getSiteId());
            //
            registerAccount(_meta);
            //
            return _meta;
        }
        return null;
    }

    private WechatAccountMeta __doLoadAccountByIdIfNeed(String accountId) {
        WechatAccountMeta _meta = (WechatAccountMeta) __caches.get(__ACCOUNT_CACHE_NAME, accountId);
        if (_meta == null) {
            ReentrantLock _locker = __LOCKS.getLocker(accountId);
            _locker.lock();
            try {
                _meta = (WechatAccountMeta) __caches.get(__ACCOUNT_CACHE_NAME, accountId);
                if (_meta == null) {
                    WechatAccount _account = WechatAccount.builder().id(DigestUtils.md5Hex(accountId)).build()
                            .load(Fields.create(WechatAccount.FIELDS.ACCOUNT_ID,
                                    WechatAccount.FIELDS.APP_ID,
                                    WechatAccount.FIELDS.APP_SECRET,
                                    WechatAccount.FIELDS.APP_AES_KEY,
                                    WechatAccount.FIELDS.LAST_APP_AES_KEY,
                                    WechatAccount.FIELDS.REDIRECT_URI,
                                    WechatAccount.FIELDS.TYPE,
                                    WechatAccount.FIELDS.IS_VERIFIED,
                                    WechatAccount.FIELDS.TOKEN,
                                    WechatAccount.FIELDS.IS_MSG_ENCRYPTED,
                                    WechatAccount.FIELDS.MCH_ID,
                                    WechatAccount.FIELDS.MCH_KEY,
                                    WechatAccount.FIELDS.CERT_FILE_PATH,
                                    WechatAccount.FIELDS.NOTIFY_URL, WechatAccount.FIELDS.SITE_ID));
                    _meta = __doBuildAccountMeta(_account);
                }
            } catch (Exception e) {
                _LOG.warn("", RuntimeUtils.unwrapThrow(e));
            } finally {
                _locker.unlock();
            }
        }
        return _meta;
    }

    private WechatAccountMeta __doLoadAccountByTokenIfNeed(String token) {
        String _accountId = (String) __caches.get(__ACCOUNT_CACHE_NAME.concat("_TOKEN"), token);
        if (_accountId != null) {
            return __doLoadAccountByIdIfNeed(_accountId);
        }
        ReentrantLock _locker = __LOCKS.getLocker(token);
        _locker.lock();
        try {
            _accountId = (String) __caches.get(__ACCOUNT_CACHE_NAME.concat("_TOKEN"), token);
            if (_accountId != null) {
                return __doLoadAccountByIdIfNeed(_accountId);
            }
            WechatAccount _account = WechatAccount.builder().token(token).build()
                    .findFirst(Fields.create(WechatAccount.FIELDS.ACCOUNT_ID,
                            WechatAccount.FIELDS.APP_ID,
                            WechatAccount.FIELDS.APP_SECRET,
                            WechatAccount.FIELDS.APP_AES_KEY,
                            WechatAccount.FIELDS.LAST_APP_AES_KEY,
                            WechatAccount.FIELDS.REDIRECT_URI,
                            WechatAccount.FIELDS.TYPE,
                            WechatAccount.FIELDS.IS_VERIFIED,
                            WechatAccount.FIELDS.TOKEN,
                            WechatAccount.FIELDS.IS_MSG_ENCRYPTED,
                            WechatAccount.FIELDS.MCH_ID,
                            WechatAccount.FIELDS.MCH_KEY,
                            WechatAccount.FIELDS.CERT_FILE_PATH,
                            WechatAccount.FIELDS.NOTIFY_URL, WechatAccount.FIELDS.SITE_ID));
            return __doBuildAccountMeta(_account);
        } catch (Exception e) {
            _LOG.warn("", RuntimeUtils.unwrapThrow(e));
        } finally {
            _locker.unlock();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public Collection<String> getAccountIds() {
        return __caches.keys(__ACCOUNT_CACHE_NAME);
    }

    public boolean hasAccountId(String accountId) {
        return __doLoadAccountByIdIfNeed(accountId) != null;
    }

    public WechatAccountMeta getAccountMetaByToken(String token) {
        return __doLoadAccountByTokenIfNeed(token);
    }

    public WechatAccountMeta getAccountMetaByAccountId(String accountId) {
        return __doLoadAccountByIdIfNeed(accountId);
    }
}
