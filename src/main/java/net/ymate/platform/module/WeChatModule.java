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

import net.ymate.platform.base.AbstractModule;
import net.ymate.platform.commons.lang.BlurObject;
import net.ymate.platform.commons.util.ClassUtils;
import net.ymate.platform.commons.util.RuntimeUtils;
import net.ymate.platform.module.wechat.*;
import net.ymate.platform.module.wechat.support.DefaultAccountDataProvider;

import java.util.Map;

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

            private IAccountDataProvider __dataProvider;

            private IMessageProcessor __messageProcessor;

            private IMessageHandler __messageHandler;

            private Boolean __checkAccountValid;

            public IAccountDataProvider getAccountDataProviderImpl() {
                if (__dataProvider == null) {
                    __dataProvider = ClassUtils.impl(moduleCfgs.get("account_data_provider_impl"), IAccountDataProvider.class, WeChatModule.class);
                    if (__dataProvider == null) {
                        __dataProvider = new DefaultAccountDataProvider();
                        try {
                            AccountDataMeta _meta = new AccountDataMeta(
                                    moduleCfgs.get("account_id"),
                                    moduleCfgs.get("app_id"),
                                    moduleCfgs.get("app_secret"),
                                    moduleCfgs.get("app_aes_key"),
                                    moduleCfgs.get("redirect_uri"),
                                    new BlurObject(moduleCfgs.get("type")).toIntValue(),
                                    new BlurObject(moduleCfgs.get("is_verfied")).toIntValue(),
                                    new BlurObject(moduleCfgs.get("is_msg_encrypted")).toIntValue());
                            _meta.setLastAppAesKey(moduleCfgs.get("last_app_aes_key"));
                            _meta.setMchId(moduleCfgs.get("mch_id"));
                            _meta.setMchKey(moduleCfgs.get("mch_key"));
                            _meta.setNotifyUrl(moduleCfgs.get("notify_url"));
                            //
                            __dataProvider.registerAccount(_meta);
                        } catch (Exception e) {
                            throw new Error(RuntimeUtils.unwrapThrow(e));
                        }
                    }
                }
                return __dataProvider;
            }

            public IMessageProcessor getMessageProcessorImpl() {
                if (__messageProcessor == null) {
                    __messageProcessor = ClassUtils.impl(moduleCfgs.get("message_processor_impl"), IMessageProcessor.class, WeChatModule.class);
                }
                return __messageProcessor;
            }

            public IMessageHandler getMessageHandlerImpl() {
                if (__messageHandler == null) {
                    __messageHandler = ClassUtils.impl(moduleCfgs.get("message_handler_impl"), IMessageHandler.class, WeChatModule.class);
                }
                return __messageHandler;
            }

            public boolean isCheckAccountValid() {
                if (__checkAccountValid == null) {
                    if (moduleCfgs.containsKey("check_account_valid")) {
                        __checkAccountValid = new BlurObject(moduleCfgs.get("check_account_valid")).toBooleanValue();
                    } else {
                        __checkAccountValid = true;
                    }
                }
                return __checkAccountValid;
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
