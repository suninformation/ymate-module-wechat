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
package net.ymate.platform.module.wechat.message.event;

/**
 * @author 刘镇 (suninformation@163.com) on 14-11-28 下午5:22
 * @version 1.0
 */
public interface ITemplateSendJobFinishEvent {

    /**
     * 消息ID
     *
     * @return
     */
    public Long getMsgID();

    public String getToUserName();

    public String getFromUserName();

    public Long getCreateTime();

    /**
     * @return 返回值：success|failed:user block|failed: system failed
     */
    public String getStatus();

}
