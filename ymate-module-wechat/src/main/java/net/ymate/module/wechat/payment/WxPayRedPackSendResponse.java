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
 * @author 刘镇 (suninformation@163.com) on 16/6/27 上午12:59
 * @version 1.0
 */
public class WxPayRedPackSendResponse extends WxPayBaseResponse {

    /**
     * 商户订单号
     */
    private String mchBillNo;

    /**
     * 公众账号appid
     */
    private String wxAppId;

    /**
     * 接受收红包的用户openid
     */
    private String reOpenId;

    /**
     * 总金额
     */
    private Integer totalAmount;

    /**
     * 发放成功时间
     */
    private String sendTime;

    /**
     * 微信红包订单号
     */
    private String sendListId;

    public static WxPayRedPackSendResponse bind(String protocol) throws Exception {
        return new WxPayRedPackSendResponse(protocol);
    }

    public WxPayRedPackSendResponse(String protocol) throws Exception {
        super(protocol);
        this.mchBillNo = BlurObject.bind(this.getResponseParams().get("mch_billno")).toStringValue();
        this.wxAppId = BlurObject.bind(this.getResponseParams().get("wxappid")).toStringValue();
        this.reOpenId = BlurObject.bind(this.getResponseParams().get("re_openid")).toStringValue();
        this.totalAmount = BlurObject.bind(this.getResponseParams().get("total_amount")).toIntValue();
        this.sendTime = BlurObject.bind(this.getResponseParams().get("send_time")).toStringValue();
        this.sendListId = BlurObject.bind(this.getResponseParams().get("send_listid")).toStringValue();
    }

    public String mchBillNo() {
        return mchBillNo;
    }

    public String wxAppId() {
        return wxAppId;
    }

    public String reOpenId() {
        return reOpenId;
    }

    public Integer totalAmount() {
        return totalAmount;
    }

    public String sendTime() {
        return sendTime;
    }

    public String sendListId() {
        return sendListId;
    }
}
