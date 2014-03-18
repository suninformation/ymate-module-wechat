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
package net.ymate.platform.module.wechat;

import net.ymate.platform.module.wechat.message.OutMessage;
import net.ymate.platform.module.wechat.message.event.IClickEvent;
import net.ymate.platform.module.wechat.message.event.ILocationEvent;
import net.ymate.platform.module.wechat.message.event.IScanEvent;
import net.ymate.platform.module.wechat.message.event.ISubscribeEvent;
import net.ymate.platform.module.wechat.message.event.IUnsubscribeEvent;
import net.ymate.platform.module.wechat.message.in.IImageMessage;
import net.ymate.platform.module.wechat.message.in.ILinkMessage;
import net.ymate.platform.module.wechat.message.in.ILocationMessage;
import net.ymate.platform.module.wechat.message.in.ITextMessage;
import net.ymate.platform.module.wechat.message.in.IVideoMessage;
import net.ymate.platform.module.wechat.message.in.IVoiceMessage;
import net.ymate.platform.module.wechat.message.in.InMessage;

/**
 * <p>
 * IMessageHandler
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
 *          <td>2014年3月15日下午3:33:37</td>
 *          </tr>
 *          </table>
 */
public interface IMessageHandler {

	/**
	 * @param message
	 * @return 文字内容的消息处理
	 */
	public OutMessage onTextMessage(ITextMessage message);

	/**
	 * @param message
	 * @return 图片类型的消息处理
	 */
	public OutMessage onImageMessage(IImageMessage message);

	/**
	 * @param message
	 * @return 语音类型的消息处理
	 */
	public OutMessage onVoiceMessage(IVoiceMessage message);

	/**
	 * @param message
	 * @return 视频类型的消息处理
	 */
	public OutMessage onVideoMessage(IVideoMessage message);

	/**
	 * @param message
	 * @return 地理位置类型的消息处理
	 */
	public OutMessage onLocationMessage(ILocationMessage message);

	/**
	 * @param message
	 * @return 链接类型的消息处理
	 */
	public OutMessage onLinkMessage(ILinkMessage message);

	/**
	 * @param message
	 * @return 事件类型的消息处理
	 */
	@Deprecated
	public OutMessage onEventMessage(InMessage message);

	public OutMessage onEventSubscribe(ISubscribeEvent event);

	public OutMessage onEventUnsubscribe(IUnsubscribeEvent event);

	public OutMessage onEventClick(IClickEvent event);

	public OutMessage onEventScan(IScanEvent event);

	public OutMessage onEventLocation(ILocationEvent event);

}
