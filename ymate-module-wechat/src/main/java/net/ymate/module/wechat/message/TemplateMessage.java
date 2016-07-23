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
package net.ymate.module.wechat.message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 刘镇 (suninformation@163.com) on 16/6/22 下午2:00
 * @version 1.0
 */
public class TemplateMessage {

    @JSONField(name = "touser")
    private String toUser;

    @JSONField(name = "template_id")
    private String templateId;

    private String url;

    @JSONField(name = "topcolor")
    private String topColor;

    private Map<String, TemplateDataItem> data;

    public TemplateMessage(String toUser, String templateId) {
        this.toUser = toUser;
        this.templateId = templateId;
        this.data = new HashMap<String, TemplateDataItem>();
    }

    public TemplateMessage(String toUser, String templateId, String url) {
        this(toUser, templateId);
        this.url = url;
    }

    public TemplateMessage(String toUser, String templateId, String url, String topColor) {
        this.toUser = toUser;
        this.templateId = templateId;
        this.url = url;
        this.topColor = topColor;
    }

    public Map<String, TemplateDataItem> getData() {
        return data;
    }

    public TemplateMessage putDataItem(String key, String value) {
        this.data.put(key, new TemplateDataItem(value));
        return this;
    }

    public TemplateMessage putDataItem(String key, String value, String color) {
        this.data.put(key, new TemplateDataItem(value, color));
        return this;
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTopColor() {
        return topColor;
    }

    public void setTopColor(String topColor) {
        this.topColor = topColor;
    }

    public JSONObject toJSON() throws Exception {
        return (JSONObject) JSON.toJSON(this);
    }

    public class TemplateDataItem {

        private String value;

        private String color = "#000000";

        public TemplateDataItem(String value) {
            this.value = value;
        }

        public TemplateDataItem(String value, String color) {
            this.value = value;
            this.color = color;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }
    }
}
