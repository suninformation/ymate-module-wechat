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
import net.ymate.module.wechat.IWechat;
import net.ymate.module.wechat.message.OutMessage;
import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang.StringUtils;

/**
 * @author 刘镇 (suninformation@163.com) on 16/5/23 上午2:38
 * @version 1.0
 */
public class OutVideoMessage extends OutMessage {

    private String mediaId;

    private String thumbMediaId;

    private String title;

    private String description;

    public OutVideoMessage(String fromUserName, String toUserName, String mediaId) {
        super(fromUserName, toUserName, IWechat.MessageType.VIDEO);
        this.mediaId = mediaId;
    }

    @Override
    protected void __buildXML(StringBuilder builder) {
        if (StringUtils.isBlank(mediaId)) {
            throw new NullArgumentException("mediaId");
        }
        builder.append("<Video>\n");
        builder.append("<MediaId><![CDATA[").append(mediaId).append("]]></MediaId>\n");
        builder.append("<Title><![CDATA[").append(StringUtils.isNotBlank(title)).append("]]></Title>\n");
        builder.append("<Description><![CDATA[").append(StringUtils.isNotBlank(description)).append("]]></Description>\n");
        builder.append("</Video>\n");
    }

    @Override
    public JSONObject toJSON() {
        JSONObject _json = super.toJSON();
        _json.put("msgtype", "video");
        JSONObject _video = new JSONObject();
        _video.put("media_id", this.mediaId);
        _video.put("thumb_media_id", this.thumbMediaId);
        _json.put("video", _video);
        return _json;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getThumbMediaId() {
        return thumbMediaId;
    }

    public void setThumbMediaId(String thumbMediaId) {
        this.thumbMediaId = thumbMediaId;
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
}
