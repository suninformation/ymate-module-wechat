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
package net.ymate.module.wechat.message.in;

import net.ymate.framework.commons.XPathHelper;
import net.ymate.module.wechat.IWechat;
import net.ymate.module.wechat.message.InMessage;

import javax.xml.xpath.XPathExpressionException;

/**
 * @author 刘镇 (suninformation@163.com) on 16/5/22 上午7:14
 * @version 1.0
 */
public class InVoiceMessage extends InMessage {

    private String mediaId;

    private String format;

    private String recognition;

    public InVoiceMessage(XPathHelper xPathHelper, String toUserName, String fromUserName, Integer createTime, IWechat.MessageType msgType, String msgId) throws XPathExpressionException {
        super(toUserName, fromUserName, createTime, msgType, msgId);
        this.mediaId = xPathHelper.getStringValue("//MediaId");
        this.format = xPathHelper.getStringValue("//Format");
        this.recognition = xPathHelper.getStringValue("//Recognition");
    }

    public String getMediaId() {
        return mediaId;
    }

    public String getFormat() {
        return format;
    }

    public String getRecognition() {
        return recognition;
    }
}
