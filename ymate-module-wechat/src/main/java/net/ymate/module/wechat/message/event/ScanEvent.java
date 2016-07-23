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
package net.ymate.module.wechat.message.event;

import net.ymate.framework.commons.XPathHelper;
import net.ymate.module.wechat.IWechat;
import net.ymate.module.wechat.message.InEvent;

import javax.xml.xpath.XPathExpressionException;

/**
 * @author 刘镇 (suninformation@163.com) on 16/5/22 上午8:00
 * @version 1.0
 */
public class ScanEvent extends InEvent {

    private String ticket;

    public ScanEvent(XPathHelper xPathHelper, String toUserName, String fromUserName, Integer createTime, IWechat.MessageType msgType, IWechat.EventType event) throws XPathExpressionException {
        super(xPathHelper, toUserName, fromUserName, createTime, msgType, event);
        this.ticket = xPathHelper.getStringValue("//Ticket");
    }

    public String getTicket() {
        return ticket;
    }
}
