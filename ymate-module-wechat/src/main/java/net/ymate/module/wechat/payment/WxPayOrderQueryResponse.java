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
package net.ymate.module.wechat.payment;

import net.ymate.platform.core.lang.BlurObject;

/**
 * @author 刘镇 (suninformation@163.com) on 16/6/26 上午1:07
 * @version 1.0
 */
public class WxPayOrderQueryResponse extends WxPayNotifyResponse {

    /**
     * 交易状态
     */
    private String tradeState;

    /**
     * 交易状态描述
     */
    private String tradeStateDesc;

    public static WxPayOrderQueryResponse bind(String protocol) throws Exception {
        return new WxPayOrderQueryResponse(protocol);
    }

    public WxPayOrderQueryResponse(String protocol) throws Exception {
        super(protocol);
        this.tradeState = BlurObject.bind(this.getResponseParams().get("trade_state")).toStringValue();
        this.tradeStateDesc = BlurObject.bind(this.getResponseParams().get("trade_state_desc")).toStringValue();
    }

    public String tradeState() {
        return tradeState;
    }

    public String tradeStateDesc() {
        return tradeStateDesc;
    }
}
