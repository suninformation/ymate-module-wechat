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

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author 刘镇 (suninformation@163.com) on 16/6/1 上午12:25
 * @version 1.0
 */
public class WechatMassArticle {

    @JSONField(name = "thumb_media_id")
    private String thumbMediaId;

    private String author;

    private String title;

    @JSONField(name = "content_source_url")
    private String contentSourceUrl;

    private String content;

    private String digest;

    @JSONField(name = "show_cover_pic")
    private boolean showCoverPic;

    public String getThumbMediaId() {
        return thumbMediaId;
    }

    public void setThumbMediaId(String thumbMediaId) {
        this.thumbMediaId = thumbMediaId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContentSourceUrl() {
        return contentSourceUrl;
    }

    public void setContentSourceUrl(String contentSourceUrl) {
        this.contentSourceUrl = contentSourceUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public boolean isShowCoverPic() {
        return showCoverPic;
    }

    public void setShowCoverPic(boolean showCoverPic) {
        this.showCoverPic = showCoverPic;
    }

    public String toJSON() {
        JSONObject _json = new JSONObject();
        _json.put("thumb_media_id", this.getThumbMediaId());
        _json.put("author", this.getAuthor());
        _json.put("title", this.getTitle());
        _json.put("content_source_url", this.getContentSourceUrl());
        _json.put("content", this.getContent());
        _json.put("digest", this.getDigest());
        _json.put("show_cover_pic", this.isShowCoverPic() ? "1" : "0");
        return _json.toString();
    }
}
