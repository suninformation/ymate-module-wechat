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
package net.ymate.module.wechat.web;

import net.ymate.module.wechat.message.InMessage;
import net.ymate.platform.core.event.EventContext;
import net.ymate.platform.core.event.IEvent;

/**
 * 微信模块事件对象
 *
 * @author 刘镇 (suninformation@163.com) on 16/5/22 上午2:15
 * @version 1.0
 */
public class WechatEvent extends EventContext<InMessage, WechatEvent.EVENT> implements IEvent {

    /**
     * 微信模块事件枚举
     */
    public enum EVENT {
        SUBSCRIBE,
        UNSUBSCRIBE,
        MENU,
        LOCATION,
        SCAN,
        MASS_SEND_JOB_FINISH,
        TEMPLATE_SEND_JOB_FINISH,
        MSG_TEXT,
        MSG_IMAGE,
        MSG_LINK,
        MSG_VOICE,
        MSG_VIDEO,
        MSG_SHORT_VIDEO,
        MSG_LOCATION
    }

    public WechatEvent(InMessage owner, WechatEvent.EVENT eventName) {
        super(owner, WechatEvent.class, eventName);
    }
}
