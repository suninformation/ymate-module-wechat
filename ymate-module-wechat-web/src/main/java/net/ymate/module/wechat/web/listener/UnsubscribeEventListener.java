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
package net.ymate.module.wechat.web.listener;

import net.ymate.module.wechat.message.event.UnsubscribeEvent;
import net.ymate.module.wechat.model.WechatUser;
import net.ymate.module.wechat.web.WechatEvent;
import net.ymate.platform.core.event.IEventListener;
import net.ymate.platform.persistence.Fields;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author 刘镇 (suninformation@163.com) on 16/7/11 上午12:08
 * @version 1.0
 */
public class UnsubscribeEventListener implements IEventListener<WechatEvent> {

    private static final Log _LOG = LogFactory.getLog(UnsubscribeEventListener.class);

    public boolean handle(WechatEvent context) {
        switch (context.getEventName()) {
            case UNSUBSCRIBE:
                UnsubscribeEvent _unsubscribe = (UnsubscribeEvent) context.getSource();
                try {
                    WechatUser.builder()
                            .id(DigestUtils.md5Hex(_unsubscribe.getFromUserName()))
                            .isSubscribe(0)
                            .unsubscribeTime(_unsubscribe.getCreateTime().longValue())
                            .lastModifyTime(System.currentTimeMillis())
                            .build().update(Fields.create(WechatUser.FIELDS.IS_SUBSCRIBE, WechatUser.FIELDS.UNSUBSCRIBE_TIME, WechatUser.FIELDS.LAST_MODIFY_TIME));
                } catch (Exception e) {
                    _LOG.warn("", e);
                }
                break;
        }
        return false;
    }
}
