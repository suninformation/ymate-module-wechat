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
package net.ymate.platform.module.wechat.message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.HashMap;
import java.util.Map;

/**
 * 模板消息
 *
 * @author 刘镇 (suninformation@163.com) on 14/12/22 上午1:52
 * @version 1.0
 */
public class TemplateOutMessage {

    private String toUser;

    private String templateId;

    private String url;

    private String topColor;

    private Map<String, TemplateDataItem> datas = new HashMap<String, TemplateDataItem>();

    public TemplateOutMessage(String toUser, String templateId) {
        this.toUser = toUser;
        this.templateId = templateId;
    }

    public TemplateOutMessage(String toUser, String templateId, String url) {
        this.toUser = toUser;
        this.templateId = templateId;
        this.url = url;
    }

    public TemplateOutMessage(String toUser, String templateId, String url, String topColor) {
        this.toUser = toUser;
        this.templateId = templateId;
        this.url = url;
        this.topColor = topColor;
    }

    public TemplateOutMessage putDataItem(String key, String value) {
        datas.put(key, new TemplateDataItem(value));
        return this;
    }

    public TemplateOutMessage putDataItem(String key, String value, String color) {
        datas.put(key, new TemplateDataItem(value, color));
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

    public String toJSON() throws Exception {
        JSONObject _jsonObj = new JSONObject();
        _jsonObj.put("touser", toUser);
        _jsonObj.put("template_id", templateId);
        _jsonObj.put("url", url);
        _jsonObj.put("topcolor", topColor);
        _jsonObj.put("data", JSON.toJSON(datas));
        return _jsonObj.toString();
    }

    /**
     * @author 刘镇 (suninformation@163.com) on 14/12/22 上午1:58
     * @version 1.0
     */
    private class TemplateDataItem {

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
