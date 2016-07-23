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
 * @author 刘镇 (suninformation@163.com) on 16/5/23 上午2:06
 * @version 1.0
 */
public class OutTextMessage extends OutMessage {

    private String content;

    public OutTextMessage(String fromUserName, String toUserName, String content) {
        super(fromUserName, toUserName, IWechat.MessageType.TEXT);
        this.content = content;
    }

    @Override
    protected void __buildXML(StringBuilder builder) {
        if (StringUtils.isBlank(content)) {
            throw new NullArgumentException("content");
        }
        builder.append("<Content><![CDATA[").append(content).append("]]></Content>\n");
    }

    @Override
    public JSONObject toJSON() {
        JSONObject _json = super.toJSON();
        _json.put("msgtype", "text");
        JSONObject _text = new JSONObject();
        _text.put("content", this.content);
        _json.put("text", _text);
        return _json;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
