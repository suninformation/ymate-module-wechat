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
import net.ymate.module.wechat.payment.base.WxPayRefundData;
import net.ymate.platform.core.lang.BlurObject;

/**
 * @author 刘镇 (suninformation@163.com) on 16/6/26 下午10:43
 * @version 1.0
 */
public class WxPayRefundResponse extends WxPayBaseResponse {

    /**
     * 公众账号ID
     */
    private String appId;

    /**
     * 设备号
     */
    private String deviceInfo;

    /**
     * 微信订单号
     */
    private String transactionId;

    /**
     * 商户订单号
     */
    private String outTradeNo;

    /**
     * 订单金额
     */
    private Integer totalFee;

    /**
     * 应结订单金额
     */
    private Integer settlementTotalFee;

    /**
     * 订单金额货币种类
     */
    private String feeType;

    /**
     * 现金支付金额
     */
    private Integer cashFee;

    /**
     * 现金退款金额
     */
    private Integer cashRefundFee;

    private WxPayRefundData refundData;

    public static WxPayRefundResponse bind(String protocol) throws Exception {
        return new WxPayRefundResponse(protocol);
    }

    public WxPayRefundResponse(String protocol) throws Exception {
        super(protocol);
        this.appId = BlurObject.bind(this.getResponseParams().get("appid")).toStringValue();
        this.deviceInfo = BlurObject.bind(this.getResponseParams().get("device_info")).toStringValue();
        this.transactionId = BlurObject.bind(this.getResponseParams().get("transaction_id")).toStringValue();
        this.outTradeNo = BlurObject.bind(this.getResponseParams().get("out_trade_no")).toStringValue();
        this.totalFee = BlurObject.bind(this.getResponseParams().get("total_fee")).toIntValue();
        this.settlementTotalFee = BlurObject.bind(this.getResponseParams().get("settlement_total_fee")).toIntValue();
        this.feeType = BlurObject.bind(this.getResponseParams().get("fee_type")).toStringValue();
        this.cashFee = BlurObject.bind(this.getResponseParams().get("cash_type")).toIntValue();
        this.cashRefundFee = BlurObject.bind(this.getResponseParams().get("cash_refund_fee")).toIntValue();
        //
        int i = 0;
        this.refundData = new WxPayRefundData();
        this.refundData.setOutRefundNo(BlurObject.bind(this.getResponseParams().get("out_refund_no_" + i)).toStringValue());
        this.refundData.setRefundId(BlurObject.bind(this.getResponseParams().get("refund_id_" + i)).toStringValue());
        this.refundData.setRefundChannel(BlurObject.bind(this.getResponseParams().get("refund_channel_" + i)).toStringValue());
        this.refundData.setRefundFee(BlurObject.bind(this.getResponseParams().get("refund_fee_" + i)).toIntValue());
        this.refundData.setSettlementRefundFee(BlurObject.bind(this.getResponseParams().get("settlement_refund_fee_" + i)).toIntValue());
        this.refundData.setCouponType(BlurObject.bind(this.getResponseParams().get("coupon_type_" + i)).toIntValue());
        this.refundData.setCouponRefundCount(BlurObject.bind(this.getResponseParams().get("coupon_refund_count_" + i)).toIntValue());
        this.refundData.setCouponRefundFee(BlurObject.bind(this.getResponseParams().get("coupon_refund_fee_" + i)).toIntValue());
        this.refundData.setRefundStatus(BlurObject.bind(this.getResponseParams().get("refund_status_" + i)).toStringValue());
        this.refundData.setRefundRecvAccount(BlurObject.bind(this.getResponseParams().get("refund_recv_accout_" + i)).toStringValue());
        //
        for (int y = 0; y < this.refundData.getCouponRefundCount(); y++) {
            WxPayCouponData _couponData = new WxPayCouponData();
            _couponData.setBatchId(BlurObject.bind(this.getResponseParams().get("coupon_batch_id_" + i + "_" + y)).toStringValue());
            _couponData.setType(this.refundData.getCouponType());
            _couponData.setCouponId(BlurObject.bind(this.getResponseParams().get("coupon_refund_id_" + i + "_" + y)).toStringValue());
            _couponData.setCouponFee(BlurObject.bind(this.getResponseParams().get("coupon_refund_fee_" + i + "_" + y)).toIntValue());
            this.refundData.getCouponDatas().add(_couponData);
        }
    }

    public String appId() {
        return appId;
    }

    public String deviceInfo() {
        return deviceInfo;
    }

    public String transactionId() {
        return transactionId;
    }

    public String outTradeNo() {
        return outTradeNo;
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

    public Integer cashRefundFee() {
        return cashRefundFee;
    }

    public WxPayRefundData refundData() {
        return refundData;
    }
}
