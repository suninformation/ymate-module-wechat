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

/**
 * @author 刘镇 (suninformation@163.com) on 16/5/22 上午1:52
 * @version 1.0
 */
public interface IWechatCfg {

    /**
     * @return 微信公众账号信息提供者接口实现类, 若未提供则使用默认配置
     */
    IWechatAccountProvider getAccountProvider();

    /**
     * @return Token缓存配置器接口实现类, 若未提供则使用默认配置
     */
    IWechatTokenCacheAdapter getTokenCacheAdapter();

    /**
     * @return 消息处理器, 可选参数, 默认值为null
     */
    IWechatMessageHandler getMessageHandler();

    /**
     * @return 消息自动回复处理器接口实现类, 可选参数, 默认值为null
     */
    IWechatAutoreplyHandler getAutoreplyHandler();
}
