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
package net.ymate.platform.module;

import java.util.Map;

import net.ymate.platform.base.AbstractModule;
import net.ymate.platform.commons.util.ClassUtils;
import net.ymate.platform.module.wechat.IMessageHandler;
import net.ymate.platform.module.wechat.IMessageProcessor;
import net.ymate.platform.module.wechat.IWeChatConfig;
import net.ymate.platform.module.wechat.WeChat;

/**
 * <p>
 * WeChatModule
 * </p>
 * <p>
 * 微信公众平台服务接入框架模块加载器接口实现类；
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
 *          <td>2014年3月14日下午9:36:43</td>
 *          </tr>
 *          </table>
 */
public class WeChatModule extends AbstractModule {

	/* (non-Javadoc)
	 * @see net.ymate.platform.module.base.IModule#initialize(java.util.Map)
	 */
	public void initialize(final Map<String, String> moduleCfgs) throws Exception {
		WeChat.initialize(new IWeChatConfig() {

			public String getAppId() {
				return moduleCfgs.get("app_id");
			}

			public String getAppSecret() {
				return moduleCfgs.get("app_secret");
			}

			public String getRedirectURI() {
				return moduleCfgs.get("redirect_uri");
			}

			public IMessageProcessor getMessageProcessorImpl() {
				return ClassUtils.impl(moduleCfgs.get("message_processor_impl"), IMessageProcessor.class, WeChatModule.class);
			}

			public IMessageHandler getMessageHandlerImpl() {
				return ClassUtils.impl(moduleCfgs.get("message_handler_impl"), IMessageHandler.class, WeChatModule.class);
			}

		});
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.module.base.IModule#destroy()
	 */
	public void destroy() throws Exception {
		WeChat.destroy();
	}

}
