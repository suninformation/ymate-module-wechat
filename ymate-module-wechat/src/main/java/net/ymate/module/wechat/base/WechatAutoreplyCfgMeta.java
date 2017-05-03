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
package net.ymate.module.wechat.base;

import net.ymate.module.wechat.message.OutMessage;
import net.ymate.platform.core.util.UUIDUtils;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 刘镇 (suninformation@163.com) on 16/7/19 下午1:19
 * @version 1.0
 */
public class WechatAutoreplyCfgMeta implements Serializable {

    private boolean autoreplyOpen;
    private boolean subscribeReplyOpen;
    private WechatAutoreplyResult.ContentInfo subscribeAutoreplyInfo;
    private WechatAutoreplyResult.ContentInfo defaultAutoreplyInfo;

    private List<AutoreplyRuleMeta> replyRules;

    public List<OutMessage> matchKeywords(String fromUserName, String toUserName, String keywords) throws Exception {
        List<OutMessage> _returnValues = new ArrayList<OutMessage>();
        if (replyRules != null && !replyRules.isEmpty()) {
            for (AutoreplyRuleMeta _meta : replyRules) {
                for (WechatAutoreplyResult.ContentInfo _info : _meta.matchKeywords(keywords)) {
                    _returnValues.add(_info.toOutMessage(fromUserName, toUserName));
                }
            }
        }
        return _returnValues;
    }

    public boolean isAutoreplyOpen() {
        return autoreplyOpen;
    }

    public void setAutoreplyOpen(boolean autoreplyOpen) {
        this.autoreplyOpen = autoreplyOpen;
    }

    public boolean isSubscribeReplyOpen() {
        return subscribeReplyOpen;
    }

    public void setSubscribeReplyOpen(boolean subscribeReplyOpen) {
        this.subscribeReplyOpen = subscribeReplyOpen;
    }

    public WechatAutoreplyResult.ContentInfo getSubscribeAutoreplyInfo() {
        return subscribeAutoreplyInfo;
    }

    public void setSubscribeAutoreplyInfo(WechatAutoreplyResult.ContentInfo subscribeAutoreplyInfo) {
        this.subscribeAutoreplyInfo = subscribeAutoreplyInfo;
    }

    public WechatAutoreplyResult.ContentInfo getDefaultAutoreplyInfo() {
        return defaultAutoreplyInfo;
    }

    public void setDefaultAutoreplyInfo(WechatAutoreplyResult.ContentInfo defaultAutoreplyInfo) {
        this.defaultAutoreplyInfo = defaultAutoreplyInfo;
    }

    public List<AutoreplyRuleMeta> getReplyRules() {
        return replyRules;
    }

    public void setReplyRules(List<AutoreplyRuleMeta> replyRules) {
        this.replyRules = replyRules;
    }

    /**
     * 关键字自动回复规则描述
     */
    public static class AutoreplyRuleMeta implements Serializable {

        private boolean replyAll;

        private List<WechatAutoreplyResult.KeywordInfo> keywordInfos;

        private List<WechatAutoreplyResult.ContentInfo> replyInfos;

        public List<WechatAutoreplyResult.ContentInfo> matchKeywords(String keywords) throws Exception {
            List<WechatAutoreplyResult.ContentInfo> _returnValues = new ArrayList<WechatAutoreplyResult.ContentInfo>();
            for (WechatAutoreplyResult.KeywordInfo _info : keywordInfos) {
                String _content = StringUtils.trimToEmpty(_info.getContent());
                boolean _flag = StringUtils.equalsIgnoreCase(_content, keywords);
                if (!_flag && "contain".equalsIgnoreCase(_info.getMatchMode())) {
                    _flag = StringUtils.containsIgnoreCase(_content, keywords);
                }
                if (_flag) {
                    if (replyAll) {
                        _returnValues.addAll(replyInfos);
                    } else if (!replyInfos.isEmpty()) {
                        if (replyInfos.size() > 1) {
                            _returnValues.add(replyInfos.get(UUIDUtils.randomInt(0, replyInfos.size() - 1)));
                        } else {
                            _returnValues.add(replyInfos.get(0));
                        }
                    }
                }
            }
            return _returnValues;
        }

        public boolean isReplyAll() {
            return replyAll;
        }

        public void setReplyAll(boolean replyAll) {
            this.replyAll = replyAll;
        }

        public List<WechatAutoreplyResult.KeywordInfo> getKeywordInfos() {
            return keywordInfos;
        }

        public void setKeywordInfos(List<WechatAutoreplyResult.KeywordInfo> keywordInfos) {
            this.keywordInfos = keywordInfos;
        }

        public List<WechatAutoreplyResult.ContentInfo> getReplyInfos() {
            return replyInfos;
        }

        public void setReplyInfos(List<WechatAutoreplyResult.ContentInfo> replyInfos) {
            this.replyInfos = replyInfos;
        }
    }
}
