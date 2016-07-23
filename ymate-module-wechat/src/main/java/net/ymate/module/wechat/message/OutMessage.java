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

import com.alibaba.fastjson.JSONObject;
import net.ymate.module.wechat.IWechat;
import net.ymate.platform.core.util.DateTimeUtils;

import java.io.Serializable;

/**
 * @author 刘镇 (suninformation@163.com) on 16/5/22 上午5:02
 * @version 1.0
 */
public abstract class OutMessage implements Serializable {

    protected String toUserName;

    protected String fromUserName;

    protected Integer createTime;

    protected String msgType;

    public OutMessage(String fromUserName, String toUserName, IWechat.MessageType msgType) {
        this.fromUserName = fromUserName;
        this.toUserName = toUserName;
        this.msgType = msgType.getType();
        this.createTime = DateTimeUtils.systemTimeUTC();
    }

    protected abstract void __buildXML(StringBuilder builder);

    public String toXML() {
        StringBuilder _contentSB = new StringBuilder();
        _contentSB.append("<xml>\n");
        _contentSB.append("<ToUserName><![CDATA[").append(toUserName).append("]]></ToUserName>\n");
        _contentSB.append("<FromUserName><![CDATA[").append(fromUserName).append("]]></FromUserName>\n");
        _contentSB.append("<CreateTime>").append(createTime).append("</CreateTime>\n");
        _contentSB.append("<MsgType><![CDATA[").append(msgType).append("]]></MsgType>\n");
        __buildXML(_contentSB);
        _contentSB.append("</xml>");
        return _contentSB.toString();
    }

    public JSONObject toJSON() {
        JSONObject _json = new JSONObject();
        _json.put("touser", toUserName);
        return _json;
    }

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }

    public String getMsgType() {
        return msgType;
    }
}
