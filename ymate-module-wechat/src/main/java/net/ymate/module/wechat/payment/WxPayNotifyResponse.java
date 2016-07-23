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
import net.ymate.module.wechat.payment.base.WxPayCouponData;
import net.ymate.platform.core.lang.BlurObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 刘镇 (suninformation@163.com) on 16/6/27 上午12:08
 * @version 1.0
 */
public class WxPayNotifyResponse extends WxPayBaseResponse {

    /**
     * 公众账号ID
     */
    private String appId;

    /**
     * 设备号
     */
    private String deviceInfo;

    /**
     * 用户标识
     */
    private String openId;

    /**
     * 是否关注公众账号
     */
    private String isSubscribe;

    /**
     * 交易类型
     */
    private String tradeType;

    /**
     * 付款银行
     */
    private String bankType;

    /**
     * 订单金额
     */
    private Integer totalFee;

    /**
     * 应结订单金额
     */
    private Integer settlementTotalFee;

    /**
     * 货币种类
     */
    private String feeType;

    /**
     * 现金支付金额
     */
    private Integer cashFee;

    /**
     * 现金支付货币类型
     */
    private String cashFeeType;

    /**
     * 代金券金额
     */
    private Integer couponFee;

    /**
     * 代金券使用数量
     */
    private Integer couponCount;

    /**
     * 代金券信息
     */
    private List<WxPayCouponData> couponDatas;

    /**
     * 微信支付订单号
     */
    private String transactionId;

    /**
     * 商户订单号
     */
    private String outTradeNo;

    /**
     * 附加数据
     */
    private String attach;

    /**
     * 支付完成时间
     */
    private String timeEnd;

    public static WxPayNotifyResponse bind(String protocol) throws Exception {
        return new WxPayNotifyResponse(protocol);
    }

    public WxPayNotifyResponse(String protocol) throws Exception {
        super(protocol);
        this.couponDatas = new ArrayList<WxPayCouponData>();
        this.appId = BlurObject.bind(this.getResponseParams().get("appid")).toStringValue();
        this.deviceInfo = BlurObject.bind(this.getResponseParams().get("device_info")).toStringValue();
        this.openId = BlurObject.bind(this.getResponseParams().get("openid")).toStringValue();
        this.isSubscribe = BlurObject.bind(this.getResponseParams().get("is_subscribe")).toStringValue();
        this.tradeType = BlurObject.bind(this.getResponseParams().get("trade_type")).toStringValue();
        this.bankType = BlurObject.bind(this.getResponseParams().get("bank_type")).toStringValue();
        this.totalFee = BlurObject.bind(this.getResponseParams().get("total_fee")).toIntValue();
        this.settlementTotalFee = BlurObject.bind(this.getResponseParams().get("settlement_total_fee")).toIntValue();
        this.feeType = BlurObject.bind(this.getResponseParams().get("fee_type")).toStringValue();
        this.cashFee = BlurObject.bind(this.getResponseParams().get("cash_fee")).toIntValue();
        this.cashFeeType = BlurObject.bind(this.getResponseParams().get("cash_fee_type")).toStringValue();
        this.transactionId = BlurObject.bind(this.getResponseParams().get("transaction_id")).toStringValue();
        this.outTradeNo = BlurObject.bind(this.getResponseParams().get("out_trade_no")).toStringValue();
        this.attach = BlurObject.bind(this.getResponseParams().get("attach")).toStringValue();
        this.timeEnd = BlurObject.bind(this.getResponseParams().get("time_end")).toStringValue();
        //
        this.couponFee = BlurObject.bind(this.getResponseParams().get("coupon_fee")).toIntValue();
        this.couponCount = BlurObject.bind(this.getResponseParams().get("coupon_count")).toIntValue();
        //
        for (int i = 0; i < this.couponCount; i++) {
            WxPayCouponData _data = new WxPayCouponData();
            _data.setBatchId(BlurObject.bind(this.getResponseParams().get("coupon_batch_id_" + i)).toStringValue());
            _data.setType(BlurObject.bind(this.getResponseParams().get("coupon_type_" + i)).toIntValue());
            _data.setCouponId(BlurObject.bind(this.getResponseParams().get("coupon_id_" + i)).toStringValue());
            _data.setCouponFee(BlurObject.bind(this.getResponseParams().get("coupon_fee_" + i)).toIntValue());
            this.couponDatas.add(_data);
        }
    }

    public String appId() {
        return appId;
    }

    public String deviceInfo() {
        return deviceInfo;
    }

    public String openId() {
        return openId;
    }

    public String isSubscribe() {
        return isSubscribe;
    }

    public String tradeType() {
        return tradeType;
    }

    public String bankType() {
        return bankType;
    }

    public Integer totalFee() {
        return totalFee;
    }

    public Integer settlementTotalFee() {
        return settlementTotalFee;
    }

    public String feeType() {
        return feeType;
    }

    public Integer cashFee() {
        return cashFee;
    }

    public String cashFeeType() {
        return cashFeeType;
    }

    public Integer couponFee() {
        return couponFee;
    }

    public Integer couponCount() {
        return couponCount;
    }

    public List<WxPayCouponData> couponDatas() {
        return couponDatas;
    }

    public String transactionId() {
        return transactionId;
    }

    public String outTradeNo() {
        return outTradeNo;
    }

    public String attach() {
        return attach;
    }

    public String timeEnd() {
        return timeEnd;
    }
}
