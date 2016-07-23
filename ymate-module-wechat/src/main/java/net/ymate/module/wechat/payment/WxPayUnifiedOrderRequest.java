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
 * @author 刘镇 (suninformation@163.com) on 16/6/26 上午12:44
 * @version 1.0
 */
public class WxPayUnifiedOrderRequest extends WxPayBaseRequest<WxPayUnifiedOrderResponse> {

    /**
     * 公众账号ID
     */
    private String appId;

    /**
     * 设备号
     */
    private String deviceInfo;

    /**
     * 商品描述
     */
    private String body;

    /**
     * 商品详情
     */
    private String detail;

    /**
     * 附加数据
     */
    private String attach;

    /**
     * 商户订单号
     */
    private String outTradeNo;

    /**
     * 货币类型
     */
    private String feeType;

    /**
     * 总金额
     */
    private Integer totalFee;

    /**
     * 终端IP
     */
    private String spbillCreateIp;

    /**
     * 交易起始时间
     */
    private String timeStart;

    /**
     * 交易结束时间
     */
    private String timeExpire;

    /**
     * 商品标记
     */
    private String goodsTag;

    /**
     * 通知地址
     */
    private String notifyUrl;

    /**
     * 交易类型
     */
    private String tradeType;

    /**
     * 商品ID
     */
    private String productId;

    /**
     * 指定支付方式
     */
    private String limitPay;

    /**
     * 用户标识
     */
    private String openId;

    public static WxPayUnifiedOrderRequest create(WechatAccountMeta accountMeta) {
        return new WxPayUnifiedOrderRequest(accountMeta);
    }

    public WxPayUnifiedOrderRequest(WechatAccountMeta accountMeta) {
        super(accountMeta);
        this.appId = accountMeta.getAppId();
    }

    public String appId() {
        return appId;
    }

    public WxPayUnifiedOrderRequest appId(String appId) {
        this.appId = appId;
        return this;
    }

    public String deviceInfo() {
        return deviceInfo;
    }

    public WxPayUnifiedOrderRequest deviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
        return this;
    }

    public String body() {
        return body;
    }

    public WxPayUnifiedOrderRequest body(String body) {
        this.body = body;
        return this;
    }

    public String detail() {
        return detail;
    }

    public WxPayUnifiedOrderRequest detail(String detail) {
        this.detail = detail;
        return this;
    }

    public String attach() {
        return attach;
    }

    public WxPayUnifiedOrderRequest attach(String attach) {
        this.attach = attach;
        return this;
    }

    public String outTradeNo() {
        return outTradeNo;
    }

    public WxPayUnifiedOrderRequest outTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
        return this;
    }

    public String feeType() {
        return feeType;
    }

    public WxPayUnifiedOrderRequest feeType(String feeType) {
        this.feeType = feeType;
        return this;
    }

    public Integer totalFee() {
        return totalFee;
    }

    public WxPayUnifiedOrderRequest totalFee(Integer totalFee) {
        this.totalFee = totalFee;
        return this;
    }

    public String spbillCreateIp() {
        return spbillCreateIp;
    }

    public WxPayUnifiedOrderRequest spbillCreateIp(String spbillCreateIp) {
        this.spbillCreateIp = spbillCreateIp;
        return this;
    }

    public String timeStart() {
        return timeStart;
    }

    public WxPayUnifiedOrderRequest timeStart(String timeStart) {
        this.timeStart = timeStart;
        return this;
    }

    public String timeExpire() {
        return timeExpire;
    }

    public WxPayUnifiedOrderRequest timeExpire(String timeExpire) {
        this.timeExpire = timeExpire;
        return this;
    }

    public String goodsTag() {
        return goodsTag;
    }

    public WxPayUnifiedOrderRequest goodsTag(String goodsTag) {
        this.goodsTag = goodsTag;
        return this;
    }

    public String notifyUrl() {
        return notifyUrl;
    }

    public WxPayUnifiedOrderRequest notifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
        return this;
    }

    public String tradeType() {
        return tradeType;
    }

    public WxPayUnifiedOrderRequest tradeType(String tradeType) {
        this.tradeType = tradeType;
        return this;
    }

    public String productId() {
        return productId;
    }

    public WxPayUnifiedOrderRequest productId(String productId) {
        this.productId = productId;
        return this;
    }

    public String limitPay() {
        return limitPay;
    }

    public WxPayUnifiedOrderRequest limitPay(String limitPay) {
        this.limitPay = limitPay;
        return this;
    }

    public String openId() {
        return openId;
    }

    public WxPayUnifiedOrderRequest openId(String openId) {
        this.openId = openId;
        return this;
    }

    @Override
    public Map<String, Object> buildSignatureParams() {
        if (StringUtils.isBlank(this.appId)) {
            throw new NullArgumentException("appId");
        }
        if (StringUtils.isBlank(this.body)) {
            throw new NullArgumentException("body");
        }
        if (StringUtils.isBlank(this.outTradeNo)) {
            throw new NullArgumentException("outTradeNo");
        }
        if (this.totalFee == null || this.totalFee <= 0) {
            throw new NullArgumentException("totalFee");
        }
        if (StringUtils.isBlank(this.spbillCreateIp)) {
            throw new NullArgumentException("spbillCreateIp");
        }
        if (StringUtils.isBlank(this.notifyUrl)) {
            throw new NullArgumentException("notifyUrl");
        }
        if (StringUtils.isBlank(this.tradeType)) {
            throw new NullArgumentException("tradeType");
        }
        //
        Map<String, Object> _params = super.buildSignatureParams();
        _params.put("appid", appId);
        _params.put("device_info", deviceInfo);
        _params.put("body", body);
        _params.put("detail", detail);
        _params.put("attach", attach);
        _params.put("out_trade_no", outTradeNo);
        _params.put("fee_type", feeType);
        _params.put("total_fee", totalFee);
        _params.put("spbill_create_id", spbillCreateIp);
        _params.put("time_start", timeStart);
        _params.put("time_expire", timeExpire);
        _params.put("goods_tag", goodsTag);
        _params.put("notify_url", notifyUrl);
        _params.put("trade_type", tradeType);
        _params.put("product_id", productId);
        _params.put("limit_pay", limitPay);
        _params.put("openid", openId);
        return _params;
    }

    protected String __doGetRequestURL() {
        return IWechat.WX_PAY_API.PAY_UNIFIED_ORDER;
    }

    protected WxPayUnifiedOrderResponse __doParseResponse(IHttpResponse httpResponse) throws Exception {
        return WxPayUnifiedOrderResponse.bind(httpResponse.getContent());
    }
}
