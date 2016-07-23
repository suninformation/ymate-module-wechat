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
 * @author 刘镇 (suninformation@163.com) on 16/5/23 上午2:44
 * @version 1.0
 */
public class OutMusicMessage extends OutMessage {

    private String title;

    private String description;

    private String musicUrl;

    private String hqMusicUrl;

    private String thumbMediaId;

    public OutMusicMessage(String fromUserName, String toUserName, String thumbMediaId) {
        super(fromUserName, toUserName, IWechat.MessageType.MUSIC);
        this.thumbMediaId = thumbMediaId;
    }

    @Override
    protected void __buildXML(StringBuilder builder) {
        if (StringUtils.isBlank(thumbMediaId)) {
            throw new NullArgumentException("thumbMediaId");
        }
        builder.append("<Music>\n");
        builder.append("<Title><![CDATA[").append(StringUtils.isNotBlank(title)).append("]]></Title>\n");
        builder.append("<Description><![CDATA[").append(StringUtils.isNotBlank(description)).append("]]></Description>\n");
        builder.append("<MusicUrl><![CDATA[").append(StringUtils.isNotBlank(musicUrl)).append("]]></MusicUrl>\n");
        builder.append("<HQMusicUrl><![CDATA[").append(StringUtils.isNotBlank(hqMusicUrl)).append("]]></HQMusicUrl>\n");
        builder.append("<ThumbMediaId><![CDATA[").append(StringUtils.isNotBlank(thumbMediaId)).append("]]></ThumbMediaId>\n");
        builder.append("</Music>\n");
    }

    @Override
    public JSONObject toJSON() {
        JSONObject _json = super.toJSON();
        _json.put("msgtype", "music");
        JSONObject _music = new JSONObject();
        _music.put("title", this.title);
        _music.put("description", this.description);
        _music.put("musicurl", this.musicUrl);
        _music.put("hqmusicurl", this.hqMusicUrl);
        _music.put("thumb_media_id", this.thumbMediaId);
        _json.put("music", _music);
        return _json;
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

    public String getMusicUrl() {
        return musicUrl;
    }

    public void setMusicUrl(String musicUrl) {
        this.musicUrl = musicUrl;
    }

    public String getHqMusicUrl() {
        return hqMusicUrl;
    }

    public void setHqMusicUrl(String hqMusicUrl) {
        this.hqMusicUrl = hqMusicUrl;
    }

    public String getThumbMediaId() {
        return thumbMediaId;
    }

    public void setThumbMediaId(String thumbMediaId) {
        this.thumbMediaId = thumbMediaId;
    }
}
