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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import net.ymate.module.wechat.IWechat;
import net.ymate.module.wechat.message.OutMessage;
import net.ymate.module.wechat.message.out.*;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 刘镇 (suninformation@163.com) on 16/7/9 上午4:46
 * @version 1.0
 */
public class WechatAutoreplyResult extends WechatResult {

    private String id;

    @JSONField(name = "is_autoreply_open")
    private boolean autoreplyOpen;

    @JSONField(name = "is_add_friend_reply_open")
    private boolean subscribeReplyOpen;

    @JSONField(name = "add_friend_autoreply_info")
    private ContentInfo subscribeAutoreplyInfo;

    @JSONField(name = "message_default_autoreply_info")
    private ContentInfo defaultAutoreplyInfo;

    @JSONField(name = "keyword_autoreply_info")
    private KeywordAutoreplyInfo keywordAutoreplyInfo;

    @JSONField(name = "last_modify_time")
    private long lastModifyTime;

    public static AutoreplyBuilder builder() {
        return new AutoreplyBuilder(null);
    }

    public static AutoreplyBuilder builder(WechatAutoreplyResult target) {
        return new AutoreplyBuilder(target);
    }

    public WechatAutoreplyResult(JSONObject result) {
        super(result);
        if (this.getErrCode() == 0) {
            this.autoreplyOpen = result.getBooleanValue("is_autoreply_open");
            this.subscribeReplyOpen = result.getBooleanValue("is_add_friend_reply_open");
            this.subscribeAutoreplyInfo = result.getObject("add_friend_autoreply_info", ContentInfo.class);
            this.defaultAutoreplyInfo = result.getObject("message_default_autoreply_info", ContentInfo.class);
            this.keywordAutoreplyInfo = result.getObject("keyword_autoreply_info", KeywordAutoreplyInfo.class);
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public ContentInfo getSubscribeAutoreplyInfo() {
        return subscribeAutoreplyInfo;
    }

    public void setSubscribeAutoreplyInfo(ContentInfo subscribeAutoreplyInfo) {
        this.subscribeAutoreplyInfo = subscribeAutoreplyInfo;
    }

    public ContentInfo getDefaultAutoreplyInfo() {
        return defaultAutoreplyInfo;
    }

    public void setDefaultAutoreplyInfo(ContentInfo defaultAutoreplyInfo) {
        this.defaultAutoreplyInfo = defaultAutoreplyInfo;
    }

    public KeywordAutoreplyInfo getKeywordAutoreplyInfo() {
        return keywordAutoreplyInfo;
    }

    public void setKeywordAutoreplyInfo(KeywordAutoreplyInfo keywordAutoreplyInfo) {
        this.keywordAutoreplyInfo = keywordAutoreplyInfo;
    }

    public long getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(long lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    public WechatAutoreplyCfgMeta toAutoreplyCfgMeta() {
        WechatAutoreplyCfgMeta _confMeta = new WechatAutoreplyCfgMeta();
        _confMeta.setAutoreplyOpen(this.isAutoreplyOpen());
        _confMeta.setSubscribeReplyOpen(this.subscribeReplyOpen);
        _confMeta.setSubscribeAutoreplyInfo(this.subscribeAutoreplyInfo);
        _confMeta.setDefaultAutoreplyInfo(this.defaultAutoreplyInfo);
        if (this.keywordAutoreplyInfo != null && this.keywordAutoreplyInfo.getItems() != null) {
            List<WechatAutoreplyCfgMeta.AutoreplyRuleMeta> _replyRules = new ArrayList<WechatAutoreplyCfgMeta.AutoreplyRuleMeta>();
            for (KeywordAutoreplyItem _item : this.keywordAutoreplyInfo.getItems()) {
                WechatAutoreplyCfgMeta.AutoreplyRuleMeta _meta = new WechatAutoreplyCfgMeta.AutoreplyRuleMeta();
                _meta.setReplyAll(StringUtils.equalsIgnoreCase(_item.getReplyMode(), "reply_all"));
                _meta.setKeywordInfos(_item.getKeywordInfos());
                _meta.setReplyInfos(_item.getReplyInfos());
                _replyRules.add(_meta);
            }
            _confMeta.setReplyRules(_replyRules);
        }
        return _confMeta;
    }

    public static class KeywordAutoreplyInfo {
        @JSONField(name = "list")
        private List<KeywordAutoreplyItem> items;

        public List<KeywordAutoreplyItem> getItems() {
            return items;
        }

        public void setItems(List<KeywordAutoreplyItem> items) {
            this.items = items;
        }
    }

    public static class KeywordAutoreplyItem {

        private String id;

        @JSONField(name = "rule_name")
        private String ruleName;

        @JSONField(name = "create_time")
        private long createTime;

        @JSONField(name = "reply_mode")
        private String replyMode;

        @JSONField(name = "keyword_list_info")
        private List<KeywordInfo> keywordInfos;

        @JSONField(name = "reply_list_info")
        private List<ContentInfo> replyInfos;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getRuleName() {
            return ruleName;
        }

        public void setRuleName(String ruleName) {
            this.ruleName = ruleName;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public String getReplyMode() {
            return replyMode;
        }

        public void setReplyMode(String replyMode) {
            this.replyMode = replyMode;
        }

        public List<KeywordInfo> getKeywordInfos() {
            return keywordInfos;
        }

        public void setKeywordInfos(List<KeywordInfo> keywordInfos) {
            this.keywordInfos = keywordInfos;
        }

        public List<ContentInfo> getReplyInfos() {
            return replyInfos;
        }

        public void setReplyInfos(List<ContentInfo> replyInfos) {
            this.replyInfos = replyInfos;
        }
    }

    public static class KeywordInfo extends ContentInfo {
        @JSONField(name = "match_mode")
        private String matchMode;

        public String getMatchMode() {
            return matchMode;
        }

        public void setMatchMode(String matchMode) {
            this.matchMode = matchMode;
        }
    }

    public static class ContentInfo {

        private String type;
        private String content;

        @JSONField(name = "news_info")
        private NewsInfo newsInfo;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public NewsInfo getNewsInfo() {
            return newsInfo;
        }

        public void setNewsInfo(NewsInfo newsInfo) {
            this.newsInfo = newsInfo;
        }

        public OutMessage toOutMessage(String fromUserName, String toUserName) {
            IWechat.MessageType _type = IWechat.MessageType.valueOf(type);
            OutMessage _out = null;
            switch (_type) {
                case TEXT:
                    _out = new OutTextMessage(fromUserName, toUserName, content);
                    break;
                case IMAGE:
                    _out = new OutImageMessage(fromUserName, toUserName, JSON.parseObject(content).getString("MediaId"));
                    break;
                case VIDEO:
                    JSONObject _json = JSONObject.parseObject(content);
                    OutVideoMessage _videoMsg = new OutVideoMessage(fromUserName, toUserName, _json.getString("MediaId"));
                    if (_json.containsKey("Title")) {
                        _videoMsg.setTitle(_json.getString("Title"));
                    }
                    if (_json.containsKey("Description")) {
                        _videoMsg.setDescription(_json.getString("Description"));
                    }
                    _out = _videoMsg;
                    break;
                case VOICE:
                    _out = new OutVoiceMessage(fromUserName, toUserName, JSON.parseObject(content).getString("MediaId"));
                    break;
                case NEWS:
                    List<Article> _articles = JSONArray.parseArray(JSONObject.parseObject(content).getJSONArray("Articles").toJSONString(), Article.class);
                    _out = new OutNewsMessage(fromUserName, toUserName, _articles);
                    break;
            }
            return _out;
        }
    }

    public static class NewsInfo {
        @JSONField(name = "list")
        private List<NewsItem> items;

        public List<NewsItem> getItems() {
            return items;
        }

        public void setItems(List<NewsItem> items) {
            this.items = items;
        }
    }

    public static class NewsItem {
        private String title;
        private String digest;
        private String author;
        @JSONField(name = "show_cover")
        private boolean showCover;
        @JSONField(name = "cover_url")
        private String coverUrl;
        @JSONField(name = "content_url")
        private String contentUrl;
        @JSONField(name = "source_url")
        private String sourceUrl;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDigest() {
            return digest;
        }

        public void setDigest(String digest) {
            this.digest = digest;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public boolean isShowCover() {
            return showCover;
        }

        public void setShowCover(boolean showCover) {
            this.showCover = showCover;
        }

        public String getCoverUrl() {
            return coverUrl;
        }

        public void setCoverUrl(String coverUrl) {
            this.coverUrl = coverUrl;
        }

        public String getContentUrl() {
            return contentUrl;
        }

        public void setContentUrl(String contentUrl) {
            this.contentUrl = contentUrl;
        }

        public String getSourceUrl() {
            return sourceUrl;
        }

        public void setSourceUrl(String sourceUrl) {
            this.sourceUrl = sourceUrl;
        }
    }

    // ------

    public static class AutoreplyBuilder {

        private WechatAutoreplyResult __target;

        public static AutoreplyBuilder create() {
            return new AutoreplyBuilder(null);
        }

        public static AutoreplyBuilder create(WechatAutoreplyResult target) {
            return new AutoreplyBuilder(target);
        }

        public AutoreplyBuilder(WechatAutoreplyResult target) {
            if (target != null) {
                __target = target;
            } else {
                __target = new WechatAutoreplyResult(new JSONObject());
            }
        }

        public AutoreplyBuilder autoreplyOpen(boolean open) {
            __target.setAutoreplyOpen(open);
            return this;
        }

        public AutoreplyBuilder subscribeReplyOpen(boolean open) {
            __target.setSubscribeReplyOpen(open);
            return this;
        }

        public AutoreplyBuilder subscribeAutoreplyInfo(ContentInfo content) {
            __target.setSubscribeAutoreplyInfo(content);
            return this;
        }

        public AutoreplyBuilder defaultAutoreplyInfo(ContentInfo content) {
            __target.setDefaultAutoreplyInfo(content);
            return this;
        }

        public AutoreplyBuilder keywordAutoreplyInfo(KeywordAutoreplyInfo info) {
            __target.setKeywordAutoreplyInfo(info);
            return this;
        }

        public AutoreplyBuilder id(String id) {
            __target.setId(id);
            return this;
        }

        public AutoreplyBuilder lastModifyTime(long lastModifyTime) {
            __target.setLastModifyTime(lastModifyTime);
            return this;
        }

        public WechatAutoreplyResult build() {
            return __target;
        }
    }

    public static class ContentBuilder {

        private ContentInfo __target;

        public static ContentBuilder create() {
            return new ContentBuilder(null);
        }

        public static ContentBuilder create(ContentInfo target) {
            return new ContentBuilder(target);
        }

        private ContentBuilder(ContentInfo target) {
            if (target == null) {
                __target = new ContentInfo();
            } else {
                __target = target;
            }
        }

        public ContentInfo build() {
            return __target;
        }

        public ContentBuilder bind(WechatAutoreplyResult.ContentInfo content) {
            __target = content;
            return this;
        }

        public ContentBuilder bind(String type, String content) {
            __target.setType(IWechat.MessageType.valueOf(type.toUpperCase()).getType());
            __target.setContent(content);
            return this;
        }

        public ContentBuilder text(String content) {
            __target.setType("text");
            __target.setContent(content);
            return this;
        }

        public ContentBuilder image(String mediaId) {
            return image(mediaId, "");
        }

        public ContentBuilder image(String mediaId, String picUrl) {
            __target.setType("image");
            JSONObject _json = new JSONObject();
            _json.put("MediaId", mediaId);
            _json.put("Url", picUrl);
            __target.setContent(_json.toJSONString());
            return this;
        }

        public ContentBuilder voice(String mediaId) {
            return voice(mediaId, "");
        }

        public ContentBuilder voice(String mediaId, String resUrl) {
            __target.setType("voice");
            JSONObject _json = new JSONObject();
            _json.put("MediaId", mediaId);
            _json.put("Url", resUrl);
            __target.setContent(_json.toJSONString());
            return this;
        }

        public ContentBuilder video(String mediaId, String title, String description) {
            return video(mediaId, title, description, "");
        }

        public ContentBuilder video(String mediaId, String title, String description, String resUrl) {
            __target.setType("video");
            JSONObject _json = new JSONObject();
            _json.put("MediaId", mediaId);
            _json.put("Title", title);
            _json.put("Description", description);
            _json.put("Url", resUrl);
            __target.setContent(_json.toJSONString());
            return this;
        }

        public ContentBuilder news(String id, List<Article> articles) {
            __target.setType("news");
            JSONObject _json = new JSONObject();
            _json.put("Id", id);
            _json.put("Url", articles.get(0).getPicUrl());
            _json.put("Articles", JSONArray.toJSONString(articles));
            __target.setContent(_json.toJSONString());
            return this;
        }
    }

    public static class KeywordAutoreplyBuilder {

        private KeywordAutoreplyItem __target;

        public static KeywordAutoreplyBuilder create() {
            return new KeywordAutoreplyBuilder(null);
        }

        public static KeywordAutoreplyBuilder create(KeywordAutoreplyItem target) {
            return new KeywordAutoreplyBuilder(target);
        }

        public KeywordAutoreplyBuilder(KeywordAutoreplyItem target) {
            if (target == null) {
                __target = new KeywordAutoreplyItem();
            } else {
                __target = target;
            }
        }

        public KeywordAutoreplyItem build() {
            return __target;
        }

        public KeywordAutoreplyBuilder id(String id) {
            __target.setId(id);
            return this;
        }

        public KeywordAutoreplyBuilder ruleName(String ruleName) {
            __target.setRuleName(ruleName);
            return this;
        }

        public KeywordAutoreplyBuilder replyMode(String replyMode) {
            __target.setReplyMode(replyMode);
            return this;
        }

        public KeywordAutoreplyBuilder createTime(long createTime) {
            __target.setCreateTime(createTime);
            return this;
        }

        public KeywordAutoreplyBuilder addKeywordInfo(KeywordInfo info) {
            List<KeywordInfo> _infos = __target.getKeywordInfos();
            if (_infos == null) {
                _infos = new ArrayList<KeywordInfo>();
                __target.setKeywordInfos(_infos);
            }
            _infos.add(info);
            return this;
        }

        public KeywordAutoreplyBuilder addKeywordInfo(List<KeywordInfo> infos) {
            List<KeywordInfo> _infos = __target.getKeywordInfos();
            if (_infos == null) {
                _infos = new ArrayList<KeywordInfo>();
                __target.setKeywordInfos(_infos);
            }
            _infos.addAll(infos);
            return this;
        }

        public KeywordAutoreplyBuilder addReplyInfo(ContentInfo info) {
            List<ContentInfo> _infos = __target.getReplyInfos();
            if (_infos == null) {
                _infos = new ArrayList<ContentInfo>();
                __target.setReplyInfos(_infos);
            }
            _infos.add(info);
            return this;
        }

        public KeywordAutoreplyBuilder addReplyInfo(List<ContentInfo> infos) {
            List<ContentInfo> _infos = __target.getReplyInfos();
            if (_infos == null) {
                _infos = new ArrayList<ContentInfo>();
                __target.setReplyInfos(_infos);
            }
            _infos.addAll(infos);
            return this;
        }
    }
}
