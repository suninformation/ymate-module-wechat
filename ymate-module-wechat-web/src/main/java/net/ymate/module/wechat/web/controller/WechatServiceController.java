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
package net.ymate.module.wechat.web.controller;

import com.qq.weixin.mp.aes.WXBizMsgCrypt;
import net.ymate.framework.commons.ParamUtils;
import net.ymate.framework.commons.XPathHelper;
import net.ymate.module.wechat.IWechat;
import net.ymate.module.wechat.IWechatMessageHandler;
import net.ymate.module.wechat.Wechat;
import net.ymate.module.wechat.base.WechatAccountMeta;
import net.ymate.module.wechat.message.OutMessage;
import net.ymate.module.wechat.message.event.*;
import net.ymate.module.wechat.message.in.*;
import net.ymate.module.wechat.web.support.WechatRequestProcessor;
import net.ymate.platform.core.util.DateTimeUtils;
import net.ymate.platform.webmvc.annotation.*;
import net.ymate.platform.webmvc.base.Type;
import net.ymate.platform.webmvc.view.IView;
import net.ymate.platform.webmvc.view.impl.HttpStatusView;
import net.ymate.platform.webmvc.view.impl.TextView;
import org.apache.commons.lang.StringUtils;

/**
 * 微信服务接入统一入口
 *
 * @author 刘镇 (suninformation@163.com) on 16/5/22 上午3:56
 * @version 1.0
 */
@Controller
@RequestMapping("/wechat/service")
public class WechatServiceController {

    private IWechat.MessageType __doParseMsgType(String msgType) {
        if ("text".equals(msgType)) {
            return IWechat.MessageType.TEXT;
        }
        if ("image".equals(msgType)) {
            return IWechat.MessageType.IMAGE;
        }
        if ("voice".equals(msgType)) {
            return IWechat.MessageType.VOICE;
        }
        if ("video".equals(msgType)) {
            return IWechat.MessageType.VIDEO;
        }
        if ("shortvideo".equals(msgType)) {
            return IWechat.MessageType.SHORT_VIDEO;
        }
        if ("location".equals(msgType)) {
            return IWechat.MessageType.LOCATION;
        }
        if ("link".equals(msgType)) {
            return IWechat.MessageType.LINK;
        }
        if ("event".equals(msgType)) {
            return IWechat.MessageType.EVENT;
        }
        return null;
    }

    private IWechat.EventType __doParseEventType(String eventType) {
        if ("view".equals(eventType)) {
            return IWechat.EventType.MENU_VIEW;
        }
        if ("click".equals(eventType)) {
            return IWechat.EventType.MENU_CLICK;
        }
        if ("subscribe".equals(eventType)) {
            return IWechat.EventType.SUBSCRIBE;
        }
        if ("unsubscribe".equals(eventType)) {
            return IWechat.EventType.UNSUBSCRIBE;
        }
        if ("scancode_push".equals(eventType)) {
            return IWechat.EventType.MENU_SCANCODE_PUSH;
        }
        if ("scancode_waitmsg".equals(eventType)) {
            return IWechat.EventType.MENU_SCANCODE_WAITMSG;
        }
        if ("pic_sysphoto".equals(eventType)) {
            return IWechat.EventType.MENU_PIC_SYSPHOTO;
        }
        if ("pic_photo_or_album".equals(eventType)) {
            return IWechat.EventType.MENU_PIC_PHOTO_OR_ALBUM;
        }
        if ("pic_weixin".equals(eventType)) {
            return IWechat.EventType.MENU_PIC_WEIXIN;
        }
        if ("location_select".equals(eventType)) {
            return IWechat.EventType.MENU_LOCATION_SELECT;
        }
        if ("media_id".equals(eventType)) {
            return IWechat.EventType.MENU_MEDIA_ID;
        }
        if ("LOCATION".equals(eventType)) {
            return IWechat.EventType.LOCATION;
        }
        if ("SCAN".equals(eventType)) {
            return IWechat.EventType.SCAN;
        }
        if ("TEMPLATESENDJOBFINISH".equals(eventType)) {
            return IWechat.EventType.TEMPLATE_SEND_JOB_FINISH;
        }
        if ("MASSSENDJOBFINISH".equals(eventType)) {
            return IWechat.EventType.MASS_SEND_JOB_FINISH;
        }
        return null;
    }

    @RequestMapping("/{account_id}")
    public IView __doServiceGET(@PathVariable("account_id") String accountId,
                                @RequestParam String signature,
                                @RequestParam String timestamp,
                                @RequestParam String nonce,
                                @RequestParam String echostr,
                                @RequestParam String token) throws Exception {

        if (StringUtils.isBlank(accountId)
                || StringUtils.isBlank(signature)
                || StringUtils.isBlank(timestamp)
                || StringUtils.isBlank(nonce)
                || StringUtils.isBlank(echostr)
                || StringUtils.isBlank(token)) {
            echostr = "";
        } else {
            WechatAccountMeta _meta = Wechat.get().getAccountById(accountId);
            if (_meta == null || !WechatRequestProcessor.__doCheckSignature(token, signature, timestamp, nonce) || !token.equals(_meta.getToken())) {
                echostr = "";
            }
        }
        return new TextView(echostr);
    }

    @RequestMapping(value = "/{account_id}", method = Type.HttpMethod.POST)
    @RequestProcessor(WechatRequestProcessor.class)
    public IView __doServicePOST(String accountId, String protocol, String openId) throws Exception {
        //
        if (StringUtils.isNotBlank(accountId) && StringUtils.isNotBlank(protocol)) {
            WechatAccountMeta _accountMeta = Wechat.get().getAccountById(accountId);
            if (_accountMeta != null) {
                // 解析消息报文
                XPathHelper _helper = new XPathHelper(protocol);
                if (_accountMeta.isMsgEncrypted()) {
                    WXBizMsgCrypt _msgCrypt = new WXBizMsgCrypt(_accountMeta.getToken(), _accountMeta.getAppAesKey(), _accountMeta.getAppId());
                    protocol = _msgCrypt.decryptMsg(_helper.getStringValue("//MsgSignature"), _helper.getStringValue("//TimeStamp"), _helper.getStringValue("//Nonce"), _helper.getStringValue("//Encrypt"));
                    _helper = new XPathHelper(protocol);
                }
                //
                IWechatMessageHandler _messageHandler = Wechat.get().getModuleCfg().getMessageHandler();
                OutMessage _outMessage = null;
                try {
                    _outMessage = _messageHandler.onMessageReceived(protocol);
                    if (_outMessage == null) {
                        String _toUserName = _helper.getStringValue("//ToUserName");
                        String _fromUserName = _helper.getStringValue("//FromUserName");
                        if (StringUtils.equals(accountId, _toUserName) && StringUtils.equals(openId, _fromUserName)) {
                            Integer _createTime = _helper.getNumberValue("//CreateTime").intValue();
                            String _msgType = _helper.getStringValue("//MsgType");
                            //
                            IWechat.MessageType _messageType = __doParseMsgType(_msgType);
                            if (_messageType != null) {
                                if (IWechat.MessageType.EVENT.equals(_messageType)) {
                                    String _event = _helper.getStringValue("//Event");
                                    IWechat.EventType _eventType = __doParseEventType(_event);
                                    if (_eventType != null) {
                                        switch (_eventType) {
                                            case SUBSCRIBE:
                                                _outMessage = _messageHandler.onSubscribeEvent(new SubscribeEvent(_helper, _toUserName, _fromUserName, _createTime, _messageType, _eventType));
                                                break;
                                            case UNSUBSCRIBE:
                                                _messageHandler.onUnsubscribeEvent(new UnsubscribeEvent(_helper, _toUserName, _fromUserName, _createTime, _messageType, _eventType));
                                                break;
                                            case MENU_CLICK:
                                            case MENU_VIEW:
                                            case MENU_LOCATION_SELECT:
                                            case MENU_MEDIA_ID:
                                            case MENU_PIC_PHOTO_OR_ALBUM:
                                            case MENU_PIC_SYSPHOTO:
                                            case MENU_PIC_WEIXIN:
                                            case MENU_SCANCODE_PUSH:
                                            case MENU_SCANCODE_WAITMSG:
                                                _outMessage = _messageHandler.onMenuEvent(new MenuEvent(_helper, _toUserName, _fromUserName, _createTime, _messageType, _eventType));
                                                break;
                                            case SCAN:
                                                _outMessage = _messageHandler.onScanEvent(new ScanEvent(_helper, _toUserName, _fromUserName, _createTime, _messageType, _eventType));
                                                break;
                                            case LOCATION:
                                                _outMessage = _messageHandler.onLocationEvent(new LocationEvent(_helper, _toUserName, _fromUserName, _createTime, _messageType, _eventType));
                                                break;
                                            case MASS_SEND_JOB_FINISH:
                                                _messageHandler.onMassJobEvent(new MassJobEvent(_helper, _toUserName, _fromUserName, _createTime, _messageType, _eventType));
                                                break;
                                            case TEMPLATE_SEND_JOB_FINISH:
                                                _messageHandler.onTemplateJobEvent(new TemplateJobEvent(_helper, _toUserName, _fromUserName, _createTime, _messageType, _eventType));
                                            default:
                                                _outMessage = _messageHandler.onUnknownMessage(protocol);
                                        }
                                    }
                                } else {
                                    String _msgId = _helper.getStringValue("//MsgId");
                                    switch (_messageType) {
                                        case TEXT:
                                            _outMessage = _messageHandler.onTextMessage(new InTextMessage(_helper, _toUserName, _fromUserName, _createTime, _messageType, _msgId));
                                            break;
                                        case IMAGE:
                                            _outMessage = _messageHandler.onImageMessage(new InImageMessage(_helper, _toUserName, _fromUserName, _createTime, _messageType, _msgId));
                                            break;
                                        case VOICE:
                                            _outMessage = _messageHandler.onVoiceMessage(new InVoiceMessage(_helper, _toUserName, _fromUserName, _createTime, _messageType, _msgId));
                                            break;
                                        case VIDEO:
                                            _outMessage = _messageHandler.onVideoMessage(new InVideoMessage(_helper, _toUserName, _fromUserName, _createTime, _messageType, _msgId));
                                            break;
                                        case SHORT_VIDEO:
                                            _outMessage = _messageHandler.onShortVideoMessage(new InShortVideoMessage(_helper, _toUserName, _fromUserName, _createTime, _messageType, _msgId));
                                            break;
                                        case LOCATION:
                                            _outMessage = _messageHandler.onLocationMessage(new InLocationMessage(_helper, _toUserName, _fromUserName, _createTime, _messageType, _msgId));
                                            break;
                                        case LINK:
                                            _outMessage = _messageHandler.onLinkMessage(new InLinkMessage(_helper, _toUserName, _fromUserName, _createTime, _messageType, _msgId));
                                        default:
                                            _outMessage = _messageHandler.onUnknownMessage(protocol);
                                    }
                                }
                            } else {
                                _outMessage = _messageHandler.onUnknownMessage(protocol);
                            }
                        }
                    }
                } catch (Throwable e) {
                    _messageHandler.onExceptionCaught(e);
                }
                String _msgText = _outMessage != null ? _outMessage.toXML() : null;
                if (_msgText != null && _accountMeta.isMsgEncrypted()) {
                    _msgText = new WXBizMsgCrypt(_accountMeta.getToken(), _accountMeta.getAppAesKey(), _accountMeta.getAppId()).encryptMsg(_msgText, DateTimeUtils.currentTimeUTC() + "", ParamUtils.createNonceStr());
                }
                return new TextView(StringUtils.trimToEmpty(_msgText), "text/xml");
            }
        }
        return HttpStatusView.NOT_FOUND;
    }
}
