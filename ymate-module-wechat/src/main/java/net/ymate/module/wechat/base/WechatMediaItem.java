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

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * @author 刘镇 (suninformation@163.com) on 16/6/23 下午5:54
 * @version 1.0
 */
public class WechatMediaItem {

    @JSONField(name = "media_id")
    private String mediaId;

    private String name;

    private MediaContent content;

    @JSONField(name = "update_time")
    private Long updateTime;

    private String url;

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MediaContent getContent() {
        return content;
    }

    public void setContent(MediaContent content) {
        this.content = content;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public static class MediaContent {

        private String title;

        private String description;

        @JSONField(name = "down_url")
        private String downUrl;

        @JSONField(name = "news_item")
        private List<WechatMassArticle> newsItem;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getDownUrl() {
            return downUrl;
        }

        public void setDownUrl(String downUrl) {
            this.downUrl = downUrl;
        }

        public List<WechatMassArticle> getNewsItem() {
            return newsItem;
        }

        public void setNewsItem(List<WechatMassArticle> newsItem) {
            this.newsItem = newsItem;
        }
    }
}
