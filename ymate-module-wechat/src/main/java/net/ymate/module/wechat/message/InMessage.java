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

import net.ymate.module.wechat.IWechat;

import java.io.Serializable;

/**
 * 接收到的微信消息
 *
 * @author 刘镇 (suninformation@163.com) on 16/5/22 上午4:31
 * @version 1.0
 */
public abstract class InMessage implements Serializable {

    private String toUserName;

    private String fromUserName;

    private Integer createTime;

    private IWechat.MessageType msgType;

    private String msgId;

    public InMessage(String toUserName, String fromUserName, Integer createTime, IWechat.MessageType msgType, String msgId) {
        this.toUserName = toUserName;
        this.fromUserName = fromUserName;
        this.createTime = createTime;
        this.msgType = msgType;
        this.msgId = msgId;
    }

    public String getToUserName() {
        return toUserName;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public IWechat.MessageType getMsgType() {
        return msgType;
    }

    public String getMsgId() {
        return msgId;
    }
}
