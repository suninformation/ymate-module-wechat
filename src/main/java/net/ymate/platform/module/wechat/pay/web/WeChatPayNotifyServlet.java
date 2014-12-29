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
package net.ymate.platform.module.wechat.pay.web;

import net.ymate.platform.commons.util.ClassUtils;
import net.ymate.platform.commons.util.RuntimeUtils;
import net.ymate.platform.module.wechat.WeChat;
import net.ymate.platform.module.wechat.pay.IWxPayNotifyData;
import net.ymate.platform.module.wechat.pay.base.IWxPayNotifyEventHandler;
import net.ymate.platform.module.wechat.pay.base.WxPayProtocol;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 通用通知消息数据HttpServlet接收入口
 *
 * @author 刘镇 (suninformation@163.com) on 14/12/29 上午12:42
 * @version 1.0
 */
public class WeChatPayNotifyServlet extends HttpServlet {

    private static final Log _LOG = LogFactory.getLog(WeChatPayNotifyServlet.class);

    private IWxPayNotifyEventHandler __eventHandler;

    @Override
    public void init() throws ServletException {
        __eventHandler = ClassUtils.impl(getServletConfig().getInitParameter("event_handler_impl"), IWxPayNotifyEventHandler.class, this.getClass());
        if (__eventHandler == null) {
            throw new NullArgumentException("event_handler_impl");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/xml");
        String _protocol = IOUtils.toString(request.getInputStream(), "UTF-8");
        _LOG.debug("WeChatPayNotify Received: [" + _protocol + "]");
        String _returnStr = null;
        try {
            IWxPayNotifyData _data = WxPayProtocol.fromXML(IWxPayNotifyData.class, _protocol);
            _returnStr = __eventHandler.onNotifyDataReceived(_data).toXML();
        } catch (Throwable e) {
            try {
                __eventHandler.onExceptionCaught(e);
            } catch (Throwable e1) {
                // Nothing...
            }
            _LOG.debug("WeChatPayNotify Error: ", RuntimeUtils.unwrapThrow(e));
        }
        if (_returnStr == null) {
            _returnStr = new WxPayProtocol(WeChat.WX_PAY.CODE_FAIL).toXML();
        }
        _LOG.debug("WeChatPayNotify Response: [" + _returnStr + "]");
        response.getWriter().write(_returnStr);
    }

}
