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
import net.ymate.module.wechat.message.event.MenuEvent;
import net.ymate.module.wechat.message.event.SubscribeEvent;
import net.ymate.module.wechat.message.in.InLocationMessage;

/**
 * @author 刘镇 (suninformation@163.com) on 16/7/18 上午1:54
 * @version 1.0
 */
public interface IWechatAutoreplyHandler {

    /**
     * 初始化
     *
     * @param owner 所属模块对象
     * @throws Exception 可能产生的任何异常
     */
    void init(IWechat owner) throws Exception;

    /**
     * @param event 关注事件对象
     * @return 返回被关注消息对象
     * @throws Exception 可能产生的任何异常
     */
    OutMessage onSubscribe(SubscribeEvent event) throws Exception;

    /**
     * @param event 菜单事件对象
     * @return 返回菜单相关消息对象
     * @throws Exception 可能产生的任何异常
     */
    OutMessage onMenu(MenuEvent event) throws Exception;

    /**
     * @param location 位置消息对象
     * @return 返回相关消息对象
     * @throws Exception 可能产生的任何异常
     */
    OutMessage onLocation(InLocationMessage location) throws Exception;

    /**
     * @param accountId  微信账户ID
     * @param toUserName 发送给用户ID
     * @param keywords   关键字
     * @return 返回符合条件的消息对象
     * @throws Exception 可能产生的异常
     */
    OutMessage onKeywords(String accountId, String toUserName, String keywords) throws Exception;
}
