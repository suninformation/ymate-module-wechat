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

import net.ymate.framework.commons.IHttpResponse;
import net.ymate.module.wechat.IWechat;
import net.ymate.module.wechat.base.WechatAccountMeta;
import net.ymate.module.wechat.payment.base.WxPayBaseRequest;
import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang.StringUtils;

import java.util.Map;

/**
 * @author 刘镇 (suninformation@163.com) on 16/6/26 下午10:12
 * @version 1.0
 */
public class WxPayRefundRequest extends WxPayBaseRequest<WxPayRefundResponse> {

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
     * 商户退款单号
     */
    private String outRefundNo;

    /**
     * 总金额
     */
    private Integer totalFee;

    /**
     * 退款金额
     */
    private Integer refundFee;

    /**
     * 货币种类
     */
    private String refundFeeType;

    /**
     * 操作员
     */
    private String opUserId;

    public static WxPayRefundRequest create(WechatAccountMeta accountMeta) {
        return new WxPayRefundRequest(accountMeta);
    }

    public WxPayRefundRequest(WechatAccountMeta accountMeta) {
        super(accountMeta);
        this.appId = accountMeta.getAppId();
    }

    public String appId() {
        return appId;
    }

    public WxPayRefundRequest appId(String appId) {
        this.appId = appId;
        return this;
    }

    public String deviceInfo() {
        return deviceInfo;
    }

    public WxPayRefundRequest deviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
        return this;
    }

    public String transactionId() {
        return transactionId;
    }

    public WxPayRefundRequest transactionId(String transactionId) {
        this.transactionId = transactionId;
        return this;
    }

    public String outTradeNo() {
        return outTradeNo;
    }

    public WxPayRefundRequest outTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
        return this;
    }

    public String outRefundNo() {
        return outRefundNo;
    }

    public WxPayRefundRequest outRefundNo(String outRefundNo) {
        this.outRefundNo = outRefundNo;
        return this;
    }

    public Integer totalFee() {
        return totalFee;
    }

    public WxPayRefundRequest totalFee(Integer totalFee) {
        this.totalFee = totalFee;
        return this;
    }

    public Integer refundFee() {
        return refundFee;
    }

    public WxPayRefundRequest refundFee(Integer refundFee) {
        this.refundFee = refundFee;
        return this;
    }

    public String refundFeeType() {
        return refundFeeType;
    }

    public WxPayRefundRequest refundFeeType(String refundFeeType) {
        this.refundFeeType = refundFeeType;
        return this;
    }

    public String opUserId() {
        return opUserId;
    }

    public WxPayRefundRequest opUserId(String opUserId) {
        this.opUserId = opUserId;
        return this;
    }

    @Override
    public Map<String, Object> buildSignatureParams() {
        if (StringUtils.isBlank(this.appId)) {
            throw new NullArgumentException("appId");
        }
        if (StringUtils.isBlank(this.transactionId)
                && StringUtils.isBlank(this.outTradeNo)) {
            throw new NullArgumentException("transactionId or outTradeNo");
        }
        if (StringUtils.isBlank(this.outRefundNo)) {
            throw new NullArgumentException("outRefundNo");
        }
        if (this.totalFee == null || this.totalFee <= 0) {
            throw new NullArgumentException("totalFee");
        }
        if (this.refundFee == null || this.refundFee <= 0) {
            throw new NullArgumentException("refundFee");
        }
        if (StringUtils.isBlank(this.opUserId)) {
            throw new NullArgumentException("opUserId");
        }
        //
        Map<String, Object> _params = super.buildSignatureParams();
        _params.put("appid", appId);
        _params.put("device_info", deviceInfo);
        _params.put("transaction_id", transactionId);
        _params.put("out_trade_no", outTradeNo);
        _params.put("out_refund_no", outRefundNo);
        _params.put("total_fee", totalFee);
        _params.put("refund_fee", refundFee);
        _params.put("refund_fee_type", refundFeeType);
        _params.put("op_user_id", opUserId);
        return _params;
    }

    protected String __doGetRequestURL() {
        return IWechat.WX_PAY_API.PAY_REFUND;
    }

    protected WxPayRefundResponse __doParseResponse(IHttpResponse httpResponse) throws Exception {
        return WxPayRefundResponse.bind(httpResponse.getContent());
    }
}
