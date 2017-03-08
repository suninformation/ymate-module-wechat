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

import com.alibaba.fastjson.JSONObject;
import net.ymate.module.wechat.message.in.*;
import net.ymate.module.wechat.model.WechatMessage;
import net.ymate.module.wechat.web.WechatEvent;
import net.ymate.platform.core.event.IEventListener;
import net.ymate.platform.core.util.RuntimeUtils;
import net.ymate.platform.persistence.Fields;
import net.ymate.platform.persistence.jdbc.transaction.ITrade;
import net.ymate.platform.persistence.jdbc.transaction.Transactions;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author 刘镇 (suninformation@163.com) on 16/7/15 上午11:13
 * @version 1.0
 */
public class MessageEventListener implements IEventListener<WechatEvent> {

    private static final Log _LOG = LogFactory.getLog(MessageEventListener.class);

    public boolean handle(WechatEvent context) {
        String _siteId = (String) context.getParamExtend("site_id");
        String _content = null;
        JSONObject _jsonObj = null;
        switch (context.getEventName()) {
            case MSG_TEXT:
                _content = ((InTextMessage) context.getSource()).getContent();
                break;
            case MSG_IMAGE:
                InImageMessage _imageMsg = (InImageMessage) context.getSource();
                _jsonObj = new JSONObject();
                _jsonObj.put("PicUrl", _imageMsg.getPicUrl());
                _jsonObj.put("MediaId", _imageMsg.getMediaId());
                _content = _jsonObj.toJSONString();
                break;
            case MSG_LINK:
                InLinkMessage _linkMsg = (InLinkMessage) context.getSource();
                _jsonObj = new JSONObject();
                _jsonObj.put("Title", _linkMsg.getTitle());
                _jsonObj.put("Description", _linkMsg.getDescription());
                _jsonObj.put("Url", _linkMsg.getUrl());
                _content = _jsonObj.toJSONString();
                break;
            case MSG_LOCATION:
                break;
            case MSG_SHORT_VIDEO:
                InShortVideoMessage _svkMsg = (InShortVideoMessage) context.getSource();
                _jsonObj = new JSONObject();
                _jsonObj.put("MediaId", _svkMsg.getMediaId());
                _jsonObj.put("ThumbMediaId", _svkMsg.getThumbMediaId());
                _content = _jsonObj.toJSONString();
                break;
            case MSG_VIDEO:
                InVideoMessage _videoMsg = (InVideoMessage) context.getSource();
                _jsonObj = new JSONObject();
                _jsonObj.put("MediaId", _videoMsg.getMediaId());
                _jsonObj.put("ThumbMediaId", _videoMsg.getThumbMediaId());
                _content = _jsonObj.toJSONString();
                break;
            case MSG_VOICE:
                InVoiceMessage _voiceMsg = (InVoiceMessage) context.getSource();
                _jsonObj = new JSONObject();
                _jsonObj.put("MediaId", _voiceMsg.getMediaId());
                _jsonObj.put("Format", _voiceMsg.getFormat());
                _jsonObj.put("Recognition", _voiceMsg.getRecognition());
                _content = _jsonObj.toJSONString();
                break;
        }
        if (_content != null) {
            __doSaveMessage(context.getSource().getMsgId(), context.getSource().getFromUserName(), context.getSource().getToUserName(), context.getSource().getMsgType().ordinal(), _content, context.getSource().getCreateTime(), _siteId);
        }
        return false;
    }

    private void __doSaveMessage(final String msgId,
                                 final String fromUserName,
                                 final String toUserName,
                                 final int msgType,
                                 final String content,
                                 final int createTime, final String siteId) {
        try {
            Transactions.execute(new ITrade() {
                @Override
                public void deal() throws Throwable {
                    WechatMessage.builder().id(DigestUtils.md5Hex(msgId + toUserName + createTime))
                            .fromUid(fromUserName)
                            .toUid(toUserName)
                            .content(content)
                            .type(msgType)
                            .createTime(System.currentTimeMillis())
                            .sessionFlag(fromUserName)
                            .siteId(siteId).build()
                            .save(Fields.create(WechatMessage.FIELDS.ID,
                                    WechatMessage.FIELDS.FROM_UID,
                                    WechatMessage.FIELDS.TO_UID,
                                    WechatMessage.FIELDS.CONTENT,
                                    WechatMessage.FIELDS.TYPE,
                                    WechatMessage.FIELDS.CREATE_TIME,
                                    WechatMessage.FIELDS.SESSION_FLAG,
                                    WechatMessage.FIELDS.SITE_ID));
                }
            });
        } catch (Exception e) {
            _LOG.warn("", RuntimeUtils.unwrapThrow(e));
        }
    }
}
