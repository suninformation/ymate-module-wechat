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

import net.ymate.module.wechat.payment.base.WxPayBaseResponse;
import net.ymate.platform.core.lang.BlurObject;

/**
 * @author 刘镇 (suninformation@163.com) on 16/6/26 下午9:19
 * @version 1.0
 */
public class WxPayUnifiedOrderResponse extends WxPayBaseResponse {

    /**
     * 公众账号ID
     */
    private String appId;

    /**
     * 设备号
     */
    private String deviceInfo;

    /**
     * 交易类型
     */
    private String tradeType;

    /**
     * 预支付交易会话标识
     */
    private String prepayId;

    /**
     * 二维码链接
     */
    private String codeUrl;

    public static WxPayUnifiedOrderResponse bind(String protocol) throws Exception {
        return new WxPayUnifiedOrderResponse(protocol);
    }

    public WxPayUnifiedOrderResponse(String protocol) throws Exception {
        super(protocol);
        this.appId = BlurObject.bind(this.getResponseParams().get("appid")).toStringValue();
        this.deviceInfo = BlurObject.bind(this.getResponseParams().get("device_info")).toStringValue();
        this.tradeType = BlurObject.bind(this.getResponseParams().get("trade_type")).toStringValue();
        this.prepayId = BlurObject.bind(this.getResponseParams().get("prepay_id")).toStringValue();
        this.codeUrl = BlurObject.bind(this.getResponseParams().get("code_url")).toStringValue();
    }

    public String appId() {
        return appId;
    }

    public String deviceInfo() {
        return deviceInfo;
    }

    public String tradeType() {
        return tradeType;
    }

    public String prepayId() {
        return prepayId;
    }

    public String codeUrl() {
        return codeUrl;
    }
}
