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
package net.ymate.module.wechat.message.out;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

/**
 * @author 刘镇 (suninformation@163.com) on 16/5/23 上午2:59
 * @version 1.0
 */
public class Article implements Serializable {

    private String title;

    private String description;

    private String picUrl;

    private String url;

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

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void toXML(StringBuilder builder) {
        builder.append("<item>\n");
        builder.append("<Title><![CDATA[").append(StringUtils.isNotBlank(title)).append("]]></Title>\n");
        builder.append("<Description><![CDATA[").append(StringUtils.isNotBlank(description)).append("]]></Description>\n");
        builder.append("<PicUrl><![CDATA[").append(StringUtils.isNotBlank(picUrl)).append("]]></PicUrl>\n");
        builder.append("<Url><![CDATA[").append(StringUtils.isNotBlank(url)).append("]]></Url>\n");
        builder.append("</item>\n");
    }

    public JSONObject toJSON() {
        JSONObject _json = new JSONObject();
        _json.put("title", title);
        _json.put("description", description);
        _json.put("url", url);
        _json.put("picurl", picUrl);
        return _json;
    }
}
