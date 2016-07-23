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

import java.util.ArrayList;
import java.util.List;

/**
 * @author 刘镇 (suninformation@163.com) on 16/5/31 下午10:52
 * @version 1.0
 */
public class WechatMenuItem {

    private String name;

    @JSONField(name = "sub_button")
    private List<WechatMenuItem> subItems = new ArrayList<WechatMenuItem>();

    private String type;

    private String key;

    private String url;

    @JSONField(name = "media_id")
    private String mediaId;

    public static WechatMenuItem create() {
        return new WechatMenuItem();
    }

    public WechatMenuItem addItem(WechatMenuItem item) {
        subItems.add(item);
        return this;
    }

    public String getName() {
        return name;
    }

    public WechatMenuItem setName(String name) {
        this.name = name;
        return this;
    }

    public List<WechatMenuItem> getSubItems() {
        return subItems;
    }

    public WechatMenuItem setSubItems(List<WechatMenuItem> subItems) {
        this.subItems = subItems;
        return this;
    }

    public String getType() {
        return type;
    }

    public WechatMenuItem setType(String type) {
        this.type = type;
        return this;
    }

    public String getKey() {
        return key;
    }

    public WechatMenuItem setKey(String key) {
        this.key = key;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public WechatMenuItem setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getMediaId() {
        return mediaId;
    }

    public WechatMenuItem setMediaId(String mediaId) {
        this.mediaId = mediaId;
        return this;
    }
}
