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
package net.ymate.module.wechat.web.support;

import net.ymate.module.wechat.IWechat;
import net.ymate.module.wechat.IWechatMessageHandler;
import net.ymate.module.wechat.base.WechatAccountMeta;
import net.ymate.module.wechat.message.InMessage;
import net.ymate.module.wechat.message.OutMessage;
import net.ymate.module.wechat.message.event.*;
import net.ymate.module.wechat.message.in.*;
import net.ymate.module.wechat.web.WechatEvent;
import net.ymate.module.wechat.web.listener.LocationEventListener;
import net.ymate.module.wechat.web.listener.MessageEventListener;
import net.ymate.module.wechat.web.listener.SubscribeEventListener;
import net.ymate.module.wechat.web.listener.UnsubscribeEventListener;
import net.ymate.platform.core.event.Events;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author 刘镇 (suninformation@163.com) on 16/5/22 下午11:23
 * @version 1.0
 */
public class DefaultWebchatMessageHandler implements IWechatMessageHandler {

    private static final Log _LOG = LogFactory.getLog(DefaultWebchatMessageHandler.class);

    private IWechat __owner;

    public void init(IWechat owner) throws Exception {
        __owner = owner;
        __owner.getOwner().getEvents().registerEvent(WechatEvent.class);
        __owner.getOwner().getEvents().registerListener(WechatEvent.class, new MessageEventListener());
        __owner.getOwner().getEvents().registerListener(WechatEvent.class, new LocationEventListener());
        __owner.getOwner().getEvents().registerListener(WechatEvent.class, new SubscribeEventListener());
        __owner.getOwner().getEvents().registerListener(WechatEvent.class, new UnsubscribeEventListener());
    }

    public OutMessage onMessageReceived(String protocol) throws Exception {
        if (__owner.getOwner().getConfig().isDevelopMode()) {
            _LOG.info(protocol);
        }
        return null;
    }

    public void onExceptionCaught(Throwable cause) throws Exception {
        _LOG.warn("", cause);
    }

    public OutMessage onUnknownMessage(String protocol) throws Exception {
        _LOG.info(protocol);
        return null;
    }

    protected OutMessage __doWriteLog(InMessage message) {
        return null;
    }

    protected WechatEvent __doBuildEvent(WechatEvent.EVENT event, InMessage message) {
        WechatAccountMeta _meta = __owner.getAccountById(message.getToUserName());
        WechatEvent _event = new WechatEvent(message, event);
        _event.addParamExtend(IWechat.class.getName(), __owner)
                .addParamExtend("site_id", _meta.getAttribute("site_id"));
        return _event;
    }

    public OutMessage onTextMessage(InTextMessage message) throws Exception {
        __owner.getOwner().getEvents().fireEvent(Events.MODE.ASYNC, __doBuildEvent(WechatEvent.EVENT.MSG_TEXT, message));
        if (__owner.getModuleCfg().getAutoreplyHandler() != null) {
            return __owner.getModuleCfg().getAutoreplyHandler().onKeywords(message.getToUserName(), message.getFromUserName(), message.getContent());
        }
        return __doWriteLog(message);
    }

    public OutMessage onImageMessage(InImageMessage message) throws Exception {
        __owner.getOwner().getEvents().fireEvent(Events.MODE.ASYNC, __doBuildEvent(WechatEvent.EVENT.MSG_IMAGE, message));
        return __doWriteLog(message);
    }

    public OutMessage onVoiceMessage(InVoiceMessage message) throws Exception {
        __owner.getOwner().getEvents().fireEvent(Events.MODE.ASYNC, __doBuildEvent(WechatEvent.EVENT.MSG_VOICE, message));
        if (__owner.getModuleCfg().getAutoreplyHandler() != null && StringUtils.isNotBlank(message.getRecognition())) {
            return __owner.getModuleCfg().getAutoreplyHandler().onKeywords(message.getToUserName(), message.getFromUserName(), message.getRecognition());
        }
        return __doWriteLog(message);
    }

    public OutMessage onVideoMessage(InVideoMessage message) throws Exception {
        __owner.getOwner().getEvents().fireEvent(Events.MODE.ASYNC, __doBuildEvent(WechatEvent.EVENT.MSG_VIDEO, message));
        return __doWriteLog(message);
    }

    public OutMessage onShortVideoMessage(InShortVideoMessage message) throws Exception {
        __owner.getOwner().getEvents().fireEvent(Events.MODE.ASYNC, __doBuildEvent(WechatEvent.EVENT.MSG_SHORT_VIDEO, message));
        return __doWriteLog(message);
    }

    public OutMessage onLocationMessage(InLocationMessage message) throws Exception {
        __owner.getOwner().getEvents().fireEvent(Events.MODE.ASYNC, __doBuildEvent(WechatEvent.EVENT.MSG_LOCATION, message));
        if (__owner.getModuleCfg().getAutoreplyHandler() != null) {
            return __owner.getModuleCfg().getAutoreplyHandler().onLocation(message);
        }
        return __doWriteLog(message);
    }

    public OutMessage onLinkMessage(InLinkMessage message) throws Exception {
        __owner.getOwner().getEvents().fireEvent(Events.MODE.ASYNC, __doBuildEvent(WechatEvent.EVENT.MSG_LINK, message));
        return __doWriteLog(message);
    }

    public OutMessage onSubscribeEvent(SubscribeEvent event) throws Exception {
        __owner.getOwner().getEvents().fireEvent(Events.MODE.ASYNC, __doBuildEvent(WechatEvent.EVENT.SUBSCRIBE, event));
        if (__owner.getModuleCfg().getAutoreplyHandler() != null) {
            return __owner.getModuleCfg().getAutoreplyHandler().onSubscribe(event);
        }
        return __doWriteLog(event);
    }

    public void onUnsubscribeEvent(UnsubscribeEvent event) throws Exception {
        __owner.getOwner().getEvents().fireEvent(Events.MODE.ASYNC, __doBuildEvent(WechatEvent.EVENT.UNSUBSCRIBE, event));
        __doWriteLog(event);
    }

    public OutMessage onMenuEvent(MenuEvent event) throws Exception {
        __owner.getOwner().getEvents().fireEvent(Events.MODE.ASYNC, __doBuildEvent(WechatEvent.EVENT.MENU, event));
        if (__owner.getModuleCfg().getAutoreplyHandler() != null) {
            return __owner.getModuleCfg().getAutoreplyHandler().onMenu(event);
        }
        return __doWriteLog(event);
    }

    public OutMessage onScanEvent(ScanEvent event) throws Exception {
        __owner.getOwner().getEvents().fireEvent(Events.MODE.ASYNC, __doBuildEvent(WechatEvent.EVENT.SCAN, event));
        return __doWriteLog(event);
    }

    public OutMessage onLocationEvent(LocationEvent event) throws Exception {
        __owner.getOwner().getEvents().fireEvent(Events.MODE.ASYNC, __doBuildEvent(WechatEvent.EVENT.LOCATION, event));
        return __doWriteLog(event);
    }

    public void onMassJobEvent(MassJobEvent event) throws Exception {
        __owner.getOwner().getEvents().fireEvent(Events.MODE.ASYNC, __doBuildEvent(WechatEvent.EVENT.MASS_SEND_JOB_FINISH, event));
        __doWriteLog(event);
    }

    public void onTemplateJobEvent(TemplateJobEvent event) throws Exception {
        __owner.getOwner().getEvents().fireEvent(Events.MODE.ASYNC, __doBuildEvent(WechatEvent.EVENT.TEMPLATE_SEND_JOB_FINISH, event));
        __doWriteLog(event);
    }
}
