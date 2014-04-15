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
import net.ymate.platform.module.wechat.message.event.IViewEvent;
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
	 * @throws Exception
	 */
	public OutMessage onTextMessage(ITextMessage message) throws Exception;

	/**
	 * @param message
	 * @return 图片类型的消息处理
	 * @throws Exception
	 */
	public OutMessage onImageMessage(IImageMessage message) throws Exception;

	/**
	 * @param message
	 * @return 语音类型的消息处理
	 * @throws Exception
	 */
	public OutMessage onVoiceMessage(IVoiceMessage message) throws Exception;

	/**
	 * @param message
	 * @return 视频类型的消息处理
	 * @throws Exception
	 */
	public OutMessage onVideoMessage(IVideoMessage message) throws Exception;

	/**
	 * @param message
	 * @return 地理位置类型的消息处理
	 * @throws Exception
	 */
	public OutMessage onLocationMessage(ILocationMessage message) throws Exception;

	/**
	 * @param message
	 * @return 链接类型的消息处理
	 * @throws Exception
	 */
	public OutMessage onLinkMessage(ILinkMessage message) throws Exception;

	/**
	 * @param message
	 * @return 无效(由IAccountDataProvider的checkAccountValid接口方法验证)类型的消息处理
	 * @throws Exception
	 */
	public OutMessage onInvalidMessage(InMessage message) throws Exception;

	/**
	 * @param message
	 * @return 未知类型的消息处理(一般情况下不会发生这种事吧)
	 * @throws Exception
	 */
	@Deprecated
	public OutMessage onUnknownMessage(InMessage message) throws Exception;

	/**
	 * @param event
	 * @return 关注事件处理
	 * @throws Exception
	 */
	public OutMessage onEventSubscribe(ISubscribeEvent event) throws Exception;

	/**
	 * 取消关注事件处理
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onEventUnsubscribe(IUnsubscribeEvent event) throws Exception;

	/**
	 * @param event
	 * @return 菜单KEY点击事件处理
	 * @throws Exception
	 */
	public OutMessage onEventClick(IClickEvent event) throws Exception;

	/**
	 * @param event
	 * @return 菜单URL地址被访问事件处理
	 * @throws Exception
	 */
	public OutMessage onEventView(IViewEvent event) throws Exception;

	/**
	 * @param event
	 * @return 二维码扫描事件处理
	 * @throws Exception
	 */
	public OutMessage onEventScan(IScanEvent event) throws Exception;

	/**
	 * @param event
	 * @return 接收上报位置事件处理
	 * @throws Exception
	 */
	public OutMessage onEventLocation(ILocationEvent event) throws Exception;

}
