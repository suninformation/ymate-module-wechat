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
 * @author 刘镇 (suninformation@163.com) on 16/6/27 上午12:53
 * @version 1.0
 */
public class WxPayMchPayResponse extends WxPayBaseResponse {

    /**
     * 公众账号appid
     */
    private String mchAppId;

    /**
     * 设备号
     */
    private String deviceInfo;

    /**
     * 商户订单号
     */
    private String partnerTradeNo;

    /**
     * 微信订单号
     */
    private String paymentNo;

    /**
     * 微信支付成功时间
     */
    private String paymentTime;

    public static WxPayMchPayResponse bind(String protocol) throws Exception {
        return new WxPayMchPayResponse(protocol);
    }

    public WxPayMchPayResponse(String protocol) throws Exception {
        super(protocol);
        this.mchAppId = BlurObject.bind(this.getResponseParams().get("mch_appid")).toStringValue();
        this.deviceInfo = BlurObject.bind(this.getResponseParams().get("device_info")).toStringValue();
        this.partnerTradeNo = BlurObject.bind(this.getResponseParams().get("partner_trade_no")).toStringValue();
        this.paymentNo = BlurObject.bind(this.getResponseParams().get("payment_no")).toStringValue();
        this.paymentTime = BlurObject.bind(this.getResponseParams().get("payment_time")).toStringValue();
    }

    public String mchAppId() {
        return mchAppId;
    }

    public String deviceInfo() {
        return deviceInfo;
    }

    public String partnerTradeNo() {
        return partnerTradeNo;
    }

    public String paymentNo() {
        return paymentNo;
    }

    public String paymentTime() {
        return paymentTime;
    }
}
