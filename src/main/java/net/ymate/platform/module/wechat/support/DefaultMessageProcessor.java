/*
 * Copyright 2007-2107 the original author or authors.
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
package net.ymate.platform.module.wechat.support;

import net.ymate.platform.module.wechat.IMessageHandler;
import net.ymate.platform.module.wechat.IMessageProcessor;
import net.ymate.platform.module.wechat.WeChat;
import net.ymate.platform.module.wechat.message.OutMessage;
import net.ymate.platform.module.wechat.message.in.InMessage;

/**
 * <p>
 * DefaultMessageProcessor
 * </p>
 * <p>
 * 
 * </p>
 * 
 * @author 刘镇(suninformation@163.com)
 * @version 0.0.0
 *          <table style="border:1px solid gray;">
 *          <tr>
 *          <th width="100px">版本号</th><th width="100px">动作</th><th
 *          width="100px">修改人</th><th width="100px">修改时间</th>
 *          </tr>
 *          <!-- 以 Table 方式书写修改历史 -->
 *          <tr>
 *          <td>0.0.0</td>
 *          <td>创建类</td>
 *          <td>刘镇</td>
 *          <td>2014年3月15日下午3:26:48</td>
 *          </tr>
 *          </table>
 */
public class DefaultMessageProcessor implements IMessageProcessor {

	private IMessageHandler __handler;

	public DefaultMessageProcessor(IMessageHandler handler) {
		__handler = handler;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.module.wechat.IMessageProcessor#onMessageReceived(java.lang.String)
	 */
	@SuppressWarnings("deprecation")
	public String onMessageReceived(String protocol) throws Exception {
        InMessage _message = MessageHelper.parsingInMessage(protocol);
		OutMessage _returnMsg = null;
		if (WeChat.getConfig().isCheckAccountValid() && !WeChat.getAccountDataProvider().checkAccountValid(_message.getToUserName())) {
			_returnMsg = __handler.onInvalidMessage(_message);
		} else {
            //
            if (_message.isText()) {
                _returnMsg = __handler.onTextMessage(_message);
            } else if (_message.isImage()) {
                _returnMsg = __handler.onImageMessage(_message);
            } else if (_message.isVoice()) {
                _returnMsg = __handler.onVoiceMessage(_message);
            } else if (_message.isVideo()) {
                _returnMsg = __handler.onVideoMessage(_message);
            } else if (_message.isLocation()) {
                _returnMsg = __handler.onLocationMessage(_message);
            } else if (_message.isLink()) {
                _returnMsg = __handler.onLinkMessage(_message);
            } else if (_message.isEvent()) {
                if (_message.getEvent().equalsIgnoreCase(WeChat.WX_MESSAGE.EVENT_SUBSCRIBE)) {
                    _returnMsg = __handler.onEventSubscribe(_message);
                } else if (_message.getEvent().equalsIgnoreCase(WeChat.WX_MESSAGE.EVENT_UNSUBSCRIBE)) {
                    // 取消关注的帐号就别回复消息了，人家都不理你了~
                    __handler.onEventUnsubscribe(_message);
                } else if (_message.getEvent().equalsIgnoreCase(WeChat.WX_MESSAGE.EVENT_LOCATION)) {
                    _returnMsg = __handler.onEventLocation(_message);
                } else if (_message.getEvent().equalsIgnoreCase(WeChat.WX_MESSAGE.EVENT_CLICK)) {
                    _returnMsg = __handler.onEventClick(_message);
                } else if (_message.getEvent().equalsIgnoreCase(WeChat.WX_MESSAGE.EVENT_SCAN)) {
                    _returnMsg = __handler.onEventScan(_message);
                } else if (_message.getEvent().equalsIgnoreCase(WeChat.WX_MESSAGE.EVENT_VIEW)) {
                    _returnMsg = __handler.onEventView(_message);
                } else if (_message.getEvent().equalsIgnoreCase(WeChat.WX_MESSAGE.EVENT_MASS_SEND_JOB_FINISH)) {
                    __handler.onEventMassSendJobFinish(_message);
                } else {
                    _returnMsg = __handler.onUnknownMessage(_message);
                }
            } else {
                _returnMsg = __handler.onUnknownMessage(_message);
            }
        }
		return _returnMsg != null ? MessageHelper.parsingOutMessage(_returnMsg) : null;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.module.wechat.IMessageProcessor#onExceptionCaught(java.lang.Throwable)
	 */
	public void onExceptionCaught(Throwable cause) throws Exception {
	}

}
