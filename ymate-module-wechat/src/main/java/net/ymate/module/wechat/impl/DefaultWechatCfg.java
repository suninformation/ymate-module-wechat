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

import net.ymate.module.wechat.*;
import net.ymate.module.wechat.base.WechatAccountMeta;
import net.ymate.platform.core.YMP;
import net.ymate.platform.core.lang.BlurObject;
import net.ymate.platform.core.util.ClassUtils;
import org.apache.commons.lang.StringUtils;

import java.util.Map;

/**
 * @author 刘镇 (suninformation@163.com) on 16/5/22 上午1:56
 * @version 1.0
 */
public class DefaultWechatCfg implements IWechatCfg {

    private IWechatAccountProvider __accountProvider;

    private IWechatTokenCacheAdapter __tokenCacheAdapter;

    private IWechatMessageHandler __messageHandler;

    private IWechatAutoreplyHandler __autoreplyHandler;

    public DefaultWechatCfg(YMP owner) {
        Map<String, String> _moduleCfgs = owner.getConfig().getModuleConfigs(IWechat.MODULE_NAME);
        //
        __accountProvider = ClassUtils.impl(_moduleCfgs.get("account_provider_class"), IWechatAccountProvider.class, getClass());
        if (__accountProvider == null) {
            __accountProvider = new DefaultWechatAccountProvider();
            WechatAccountMeta _meta = new WechatAccountMeta(
                    _moduleCfgs.get("account_id"),
                    _moduleCfgs.get("app_id"),
                    _moduleCfgs.get("app_secret"),
                    _moduleCfgs.get("app_aes_key"),
                    _moduleCfgs.get("redirect_uri"),
                    new BlurObject(_moduleCfgs.get("type")).toIntValue(),
                    new BlurObject(_moduleCfgs.get("is_verfied")).toBooleanValue(),
                    _moduleCfgs.get("token"),
                    new BlurObject(_moduleCfgs.get("is_msg_encrypted")).toBooleanValue());
            _meta.setLastAppAesKey(_moduleCfgs.get("last_app_aes_key"));
            _meta.setMchId(_moduleCfgs.get("mch_id"));
            _meta.setMchKey(_moduleCfgs.get("mch_key"));
            _meta.setCertFilePath(_moduleCfgs.get("cert_file_path"));
            _meta.setNotifyUrl(_moduleCfgs.get("notify_url"));
            //
            for (Map.Entry<String, String> _item : _moduleCfgs.entrySet()) {
                if (_item.getKey().startsWith("params.")) {
                    String _key = StringUtils.substringAfter(_item.getKey(), "params.");
                    _meta.addAttribute(_key, _item.getValue());
                }
            }
            //
            __accountProvider.registerAccount(_meta);
        }
        //
        __tokenCacheAdapter = ClassUtils.impl(_moduleCfgs.get("token_cache_adapter_class"), IWechatTokenCacheAdapter.class, getClass());
        if (__tokenCacheAdapter == null) {
            __tokenCacheAdapter = new DefaultWechatTokenCacheAdapter();
        }
        //
        __messageHandler = ClassUtils.impl(_moduleCfgs.get("message_handler_class"), IWechatMessageHandler.class, getClass());
        //
        __autoreplyHandler = ClassUtils.impl(_moduleCfgs.get("autoreply_handler_class"), IWechatAutoreplyHandler.class, getClass());
    }

    public IWechatAccountProvider getAccountProvider() {
        return __accountProvider;
    }

    public IWechatTokenCacheAdapter getTokenCacheAdapter() {
        return __tokenCacheAdapter;
    }

    public IWechatMessageHandler getMessageHandler() {
        return __messageHandler;
    }

    public IWechatAutoreplyHandler getAutoreplyHandler() {
        return __autoreplyHandler;
    }
}
