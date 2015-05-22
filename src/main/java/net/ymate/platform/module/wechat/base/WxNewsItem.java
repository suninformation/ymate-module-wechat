/*
 * Copyright 2007-2107 the original author or authors.
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
/*
 * Copyright (c) 2007-2107, the original author or authors. All rights reserved.
 *
 * This program licensed under the terms of the GNU Lesser General Public License version 3.0
 * as published by the Free Software Foundation.
 */
package net.ymate.platform.module.wechat.base;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * @author 刘镇 (suninformation@163.com) on 15/5/20 下午5:35
 * @version 1.0
 */
public class WxNewsItem {

    private String title;

    private String description;

    @JSONField(name = "down_url")
    private String downUrl;

    @JSONField(name = "news_item")
    private List<WxMassArticle> newsItem;

    public List<WxMassArticle> getNewsItem() {
        return newsItem;
    }

    public void setNewsItem(List<WxMassArticle> newsItem) {
        this.newsItem = newsItem;
    }

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
}
