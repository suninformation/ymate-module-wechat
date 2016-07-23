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
public class WxPayMchPayQueryResponse extends WxPayBaseResponse {

    /**
     * 商户订单号
     */
    private String partnerTradeNo;

    /**
     * 付款单号
     */
    private String detailId;

    /**
     * 转账状态
     */
    private String status;

    /**
     * 失败原因
     */
    private String reason;

    /**
     * 收款用户openid
     */
    private String openId;

    /**
     * 收款用户姓名
     */
    private String transferName;

    /**
     * 付款金额
     */
    private Integer paymentAmount;

    /**
     * 转账时间
     */
    private String transferTime;

    /**
     * 付款描述
     */
    private String desc;

    public static WxPayMchPayQueryResponse bind(String protocol) throws Exception {
        return new WxPayMchPayQueryResponse(protocol);
    }

    public WxPayMchPayQueryResponse(String protocol) throws Exception {
        super(protocol);
        this.partnerTradeNo = BlurObject.bind(this.getResponseParams().get("partner_trade_no")).toStringValue();
        this.detailId = BlurObject.bind(this.getResponseParams().get("detail_id")).toStringValue();
        this.status = BlurObject.bind(this.getResponseParams().get("status")).toStringValue();
        this.reason = BlurObject.bind(this.getResponseParams().get("reason")).toStringValue();
        this.openId = BlurObject.bind(this.getResponseParams().get("openid")).toStringValue();
        this.transferName = BlurObject.bind(this.getResponseParams().get("transfer_name")).toStringValue();
        this.paymentAmount = BlurObject.bind(this.getResponseParams().get("payment_amount")).toIntValue();
        this.transferTime = BlurObject.bind(this.getResponseParams().get("transfer_time")).toStringValue();
        this.desc = BlurObject.bind(this.getResponseParams().get("desc")).toStringValue();
    }

    public String partnerTradeNo() {
        return partnerTradeNo;
    }

    public String detailId() {
        return detailId;
    }

    public String status() {
        return status;
    }

    public String reason() {
        return reason;
    }

    public String openId() {
        return openId;
    }

    public String transferName() {
        return transferName;
    }

    public Integer paymentAmount() {
        return paymentAmount;
    }

    public String transferTime() {
        return transferTime;
    }

    public String desc() {
        return desc;
    }
}
