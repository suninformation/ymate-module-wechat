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
package net.ymate.platform.module.wechat.web;

import net.ymate.platform.commons.util.RuntimeUtils;
import net.ymate.platform.module.wechat.WeChat;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>
 * WeChatFilter
 * </p>
 * <p>
 * 微信公众平台服务请求过滤器；
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
 *          <td>2014年3月14日下午10:51:23</td>
 *          </tr>
 *          </table>
 */
public class WeChatFilter implements Filter {

	private static final Log _LOG = LogFactory.getLog(WeChatFilter.class);

	/**
	 * 默认Token设置, 默认值: wechat
	 */
	private String token;

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	public void init(FilterConfig filterConfig) throws ServletException {
		_LOG.debug("YMP Module WeChat Started");
		token = StringUtils.defaultIfEmpty(filterConfig.getInitParameter("token"), "wechat");
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest _request = (HttpServletRequest) request;
        HttpServletResponse _response = (HttpServletResponse) response;
        if (_request.getMethod().equalsIgnoreCase("GET")) {
        	__doGetProcess(_request, _response);
        } else {
        	__doPostProcess(_request, _response);
        }
	}

	private void __doPostProcess(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/xml");
        String _protocol = IOUtils.toString(request.getInputStream(), "UTF-8");
        _LOG.debug("Message Received: [" + _protocol + "]");
        String _returnStr = null;
        try {
            _returnStr = WeChat.getMessageProcessor().onMessageReceived(_protocol);
		} catch (Throwable e) {
			try {
				WeChat.getMessageProcessor().onExceptionCaught(e);
			} catch (Throwable e1) {
				// Nothing...
			}
			_LOG.debug("Message Process Error: ", RuntimeUtils.unwrapThrow(e));
		}
        _LOG.debug("Message Response: [" + _returnStr + "]");
        response.getWriter().write(_returnStr != null ? _returnStr : "");
    }

    private void __doGetProcess(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String _echoStr = "";
		String _signature = request.getParameter("signature");
		String _timestamp = request.getParameter("timestamp");
		String _nonce = request.getParameter("nonce");
		String _echostr = request.getParameter("echostr");
		// 尝试从请求的参数中获取Token, 若未获取到则采用框架默认值
		String _token = StringUtils.defaultIfEmpty(request.getParameter("_t"), token);
		//
		if (WeChat.checkSignature(_token, _signature, _timestamp, _nonce)) {
			_echoStr = _echostr;
		}
		response.getWriter().write(_echoStr);
    }

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#destroy()
	 */
	public void destroy() {
		_LOG.debug("YMP Module WeChat Stoped");
	}

}
