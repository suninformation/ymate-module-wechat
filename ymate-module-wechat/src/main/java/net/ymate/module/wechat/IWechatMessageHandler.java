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
package net.ymate.module.wechat;

import net.ymate.module.wechat.message.OutMessage;
import net.ymate.module.wechat.message.event.*;
import net.ymate.module.wechat.message.in.*;

/**
 * @author 刘镇 (suninformation@163.com) on 16/5/22 上午4:52
 * @version 1.0
 */
public interface IWechatMessageHandler {

    /**
     * 初始化
     *
     * @param owner 所属模块对象
     * @throws Exception 可能产生的任何异常
     */
    void init(IWechat owner) throws Exception;

    OutMessage onMessageReceived(String protocol) throws Exception;

    void onExceptionCaught(Throwable cause) throws Exception;

    /**
     * 收到无法解析或未知其类型的报文
     *
     * @param protocol 收到的原始报文内容
     * @return 预回应的消息对象
     * @throws Exception 可能产生的任何异常
     */
    OutMessage onUnknownMessage(String protocol) throws Exception;

    // ------

    OutMessage onTextMessage(InTextMessage message) throws Exception;

    OutMessage onImageMessage(InImageMessage message) throws Exception;

    OutMessage onVoiceMessage(InVoiceMessage message) throws Exception;

    OutMessage onVideoMessage(InVideoMessage message) throws Exception;

    OutMessage onShortVideoMessage(InShortVideoMessage message) throws Exception;

    OutMessage onLocationMessage(InLocationMessage message) throws Exception;

    OutMessage onLinkMessage(InLinkMessage message) throws Exception;

    OutMessage onSubscribeEvent(SubscribeEvent event) throws Exception;

    void onUnsubscribeEvent(UnsubscribeEvent event) throws Exception;

    OutMessage onMenuEvent(MenuEvent event) throws Exception;

    OutMessage onScanEvent(ScanEvent event) throws Exception;

    OutMessage onLocationEvent(LocationEvent event) throws Exception;

    void onMassJobEvent(MassJobEvent event) throws Exception;

    void onTemplateJobEvent(TemplateJobEvent event) throws Exception;
}
