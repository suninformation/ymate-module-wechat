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

import com.alibaba.fastjson.JSONArray;
import net.ymate.framework.commons.ReentrantLockHelper;
import net.ymate.module.wechat.IWechat;
import net.ymate.module.wechat.IWechatAutoreplyHandler;
import net.ymate.module.wechat.base.WechatAutoreplyCfgMeta;
import net.ymate.module.wechat.base.WechatAutoreplyResult;
import net.ymate.module.wechat.message.OutMessage;
import net.ymate.module.wechat.message.event.MenuEvent;
import net.ymate.module.wechat.message.event.SubscribeEvent;
import net.ymate.module.wechat.message.in.InLocationMessage;
import net.ymate.module.wechat.model.WechatAutoreply;
import net.ymate.module.wechat.model.WechatAutoreplyRule;
import net.ymate.platform.cache.Caches;
import net.ymate.platform.cache.ICaches;
import net.ymate.platform.core.lang.BlurObject;
import net.ymate.platform.persistence.IResultSet;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author 刘镇 (suninformation@163.com) on 16/7/18 上午2:03
 * @version 1.0
 */
public class DefaultWechatAutoreplyHandler implements IWechatAutoreplyHandler {

    private IWechat __owner;

    private ICaches __caches;

    private String __AUTOREPLY_CACHE_NAME;

    private static ReentrantLockHelper __LOCKS = new ReentrantLockHelper();

    public void init(IWechat owner) throws Exception {
        __owner = owner;
        __caches = Caches.get(owner.getOwner());
        __AUTOREPLY_CACHE_NAME = StringUtils.trimToEmpty(owner.getOwner().getConfig().getParam(IWechat.MODULE_NAME.concat(".cache_name_prefix"))).concat("_WECHAT_AUTOREPLY_CACHE");
    }

    public OutMessage onSubscribe(SubscribeEvent event) throws Exception {
        WechatAutoreplyCfgMeta _cfgMeta = __doGetAutoreplyCfgMeta(event.getToUserName());
        if (_cfgMeta != null && _cfgMeta.isAutoreplyOpen()) {
            if (_cfgMeta.getSubscribeAutoreplyInfo() != null) {
                return _cfgMeta.getSubscribeAutoreplyInfo().toOutMessage(event.getToUserName(), event.getFromUserName());
            }
        }
        return null;
    }

    public OutMessage onMenu(MenuEvent event) throws Exception {
        OutMessage _outMsg = null;
        WechatAutoreplyCfgMeta _cfgMeta = __doGetAutoreplyCfgMeta(event.getToUserName());
        if (_cfgMeta != null && _cfgMeta.isAutoreplyOpen()) {
            List<OutMessage> _outMsgs = _cfgMeta.matchKeywords(event.getToUserName(), event.getFromUserName(), event.getEventKey());
            if (_outMsgs != null && !_outMsgs.isEmpty()) {
                _outMsg = _outMsgs.remove(0);
                for (OutMessage _msg : _outMsgs) {
                    __owner.wxMessageCustomSend(__owner.getAccountById(event.getToUserName()), _msg);
                }
            }
        }
        return _outMsg;
    }

    public OutMessage onLocation(InLocationMessage location) throws Exception {
        return null;
    }

    public OutMessage onKeywords(String accountId, String toUserName, String keywords) throws Exception {
        OutMessage _outMsg = null;
        WechatAutoreplyCfgMeta _cfgMeta = __doGetAutoreplyCfgMeta(accountId);
        if (_cfgMeta != null && _cfgMeta.isAutoreplyOpen()) {
            List<OutMessage> _outMsgs = _cfgMeta.matchKeywords(accountId, toUserName, keywords);
            if (_outMsgs != null && !_outMsgs.isEmpty()) {
                _outMsg = _outMsgs.remove(0);
                for (OutMessage _msg : _outMsgs) {
                    __owner.wxMessageCustomSend(__owner.getAccountById(accountId), _msg);
                }
            }
        }
        return _outMsg;
    }

    private WechatAutoreplyCfgMeta __doGetAutoreplyCfgMeta(String accountId) throws Exception {
        String _cacheKey = accountId.concat("_autoreply");
        WechatAutoreplyCfgMeta _cfgMeta = (WechatAutoreplyCfgMeta) __caches.get(__AUTOREPLY_CACHE_NAME, _cacheKey);
        if (_cfgMeta == null) {
            ReentrantLock _locker = __LOCKS.getLocker(_cacheKey);
            _locker.lock();
            try {
                _cfgMeta = __doGetAutoreplyCfgMetaFromDB(accountId);
                if (_cfgMeta != null) {
                    __caches.put(__AUTOREPLY_CACHE_NAME, _cacheKey, _cfgMeta);
                }
            } finally {
                _locker.unlock();
            }
        }
        return _cfgMeta;
    }

    private WechatAutoreplyCfgMeta __doGetAutoreplyCfgMetaFromDB(String accountId) throws Exception {
        String _id = DigestUtils.md5Hex(accountId);
        WechatAutoreply _autoreply = WechatAutoreply.builder().id(_id).build().load();
        if (_autoreply != null) {
            WechatAutoreplyResult.AutoreplyBuilder _builder = WechatAutoreplyResult.builder()
                    .autoreplyOpen(BlurObject.bind(_autoreply.getIsAutoreplyOpen()).toBooleanValue())
                    .defaultAutoreplyInfo(WechatAutoreplyResult.ContentBuilder.create().bind(_autoreply.getDefaultAutoreplyType(), _autoreply.getDefaultAutoreplyContent()).build())
                    .subscribeReplyOpen(BlurObject.bind(_autoreply.getIsSubscribeReplyOpen()).toBooleanValue())
                    .subscribeAutoreplyInfo(WechatAutoreplyResult.ContentBuilder.create().bind(_autoreply.getSubscribeAutoreplyType(), _autoreply.getSubscribeautoreplyContent()).build());
            //
            IResultSet<WechatAutoreplyRule> _rules = WechatAutoreplyRule.builder().accountId(_id).build().find();
            if (_rules.isResultsAvailable()) {
                WechatAutoreplyResult.KeywordAutoreplyInfo _info = new WechatAutoreplyResult.KeywordAutoreplyInfo();
                _info.setItems(new ArrayList<WechatAutoreplyResult.KeywordAutoreplyItem>());
                //
                for (WechatAutoreplyRule _rule : _rules.getResultData()) {
                    WechatAutoreplyResult.KeywordAutoreplyBuilder _autoreplyBuilder = WechatAutoreplyResult.KeywordAutoreplyBuilder.create().replyMode(_rule.getReplyMode());
                    List<WechatAutoreplyResult.KeywordInfo> _keywordInfos = JSONArray.parseArray(_rule.getKeywordInfo(), WechatAutoreplyResult.KeywordInfo.class);
                    if (_keywordInfos != null) {
                        _autoreplyBuilder.addKeywordInfo(_keywordInfos);
                    }
                    List<WechatAutoreplyResult.ContentInfo> _replyInfos = JSONArray.parseArray(_rule.getReplyInfo(), WechatAutoreplyResult.ContentInfo.class);
                    if (_replyInfos != null) {
                        _autoreplyBuilder.addReplyInfo(_replyInfos);
                    }
                    _info.getItems().add(_autoreplyBuilder.build());
                }
                //
                _builder.keywordAutoreplyInfo(_info);
            }
            //
            return _builder.build().toAutoreplyCfgMeta();
        }
        return null;
    }
}
