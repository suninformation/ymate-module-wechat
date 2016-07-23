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
package net.ymate.module.wechat.base;

import com.alibaba.fastjson.JSONObject;

/**
 * @author 刘镇 (suninformation@163.com) on 16/6/25 上午2:07
 * @version 1.0
 */
public class WechatMsgSendResult extends WechatResult {

    private long msgId;

    private long msgDataId;

    private String msgStatus;

    public WechatMsgSendResult(JSONObject result) {
        super(result);
        if (this.getErrCode() == 0) {
            this.msgId = result.getLongValue("msgid");
            if (result.containsKey("msg_data_id")) {
                this.msgDataId = result.getLongValue("msg_data_id");
            }
            if (result.containsKey("msg_status")) {
                this.msgStatus = result.getString("msg_status");
            }
        }
    }

    public long getMsgId() {
        return msgId;
    }

    public long getMsgDataId() {
        return msgDataId;
    }

    public String getMsgStatus() {
        return msgStatus;
    }
}
