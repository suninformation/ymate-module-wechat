/*
 * Copyright 2007-2107 the original author or authors.
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
package net.ymate.platform.module.wechat.pay.base;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import net.ymate.platform.module.wechat.WeChat;
import net.ymate.platform.module.wechat.pay.*;
import net.ymate.platform.module.wechat.support.XStreamHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * 微支付协议接口实现类
 *
 * @author 刘镇 (suninformation@163.com) on 14/12/22 上午11:01
 * @version 1.0
 */
@XStreamAlias("xml")
public class WxPayProtocol implements IWxPayResultData, IWxPayUnifiedOrderRequest, IWxPayUnifiedOrderData,
        IWxPayCloseOrderRequest, IWxPayCloseOrderData, IWxPayNotifyData,
        IWxPayOrderQueryRequest, IWxPayOrderQueryData, IWxPayRefundRequest, IWxPayRefundData,
        IWxPayShortUrlRequest, IWxPayShortUrlData {

    /**
     * 公众帐号ID，微信分配的公众账号ID
     */
    @XStreamAlias("appid")
    private String appId;

    /**
     * 商户号，微信支付分配的商户号
     */
    @XStreamAlias("mch_id")
    private String mchId;

    /**
     * 随机字符串，随机字符串,不长于 32 位
     */
    @XStreamAlias("nonce_str")
    private String nonceStr;

    /**
     * 签名
     */
    private String sign;

    /**
     * 交易类型，JSAPI、NATIVE、APP
     */
    @XStreamAlias("trade_type")
    private String tradeType;

    /**
     * 设备号，微信支付分配的终端设备号
     */
    @XStreamAlias("device_info")
    private String deviceInfo;

    /**
     * 用户标识，用户在商户 appid 下的唯一标识,trade_type 为 JSAPI 时,此参数必传
     */
    @XStreamAlias("openid")
    private String openId;

    /**
     * 通知地址，接收微信支付成功通知
     */
    @XStreamAlias("notify_url")
    private String notifyUrl;

    /**
     * 商品描述，商品描述
     */
    private String body;

    /**
     * 附加数据，附加数据,原样返回
     */
    private String attach;

    /**
     * 商户订单号，商户系统内部的订单号,32 个字符内、可包含字母,确保在商户系统唯一
     */
    @XStreamAlias("out_trade_no")
    private String outTradeNo;

    /**
     * 总金额，订单总金额,单位为分,不能带小数点
     */
    @XStreamAlias("total_fee")
    private String totalFee;

    /**
     * 终端IP，订单生成的机器IP
     */
    @XStreamAlias("spbill_create_ip")
    private String spbillCreateIp;

    /**
     * 订单生成时间,格式为 yyyyMMddHHmmss,如 2009 年 12月25日9点10分10秒表 示为 20091225091010。时区 为 GMT+8 beijing。该时间取 自商户服务器
     */
    @XStreamAlias("time_start")
    private String timeStart;

    /**
     * 订单失效时间,格式为 yyyyMMddHHmmss,如 2009 年 12月27日9点10分10秒表 示为 20091227091010。时区 为 GMT+8 beijing。该时间取 自商户服务器
     */
    @XStreamAlias("time_expire")
    private String timeExpire;

    /**
     * 商品ID，只在 trade_type 为 NATIVE 时需要填写。此 id 为二维码 中包含的商品 ID,商户自行 维护
     */
    @XStreamAlias("product_id")
    private String productId;

    /**
     * 商品标记，商品标记,该字段不能随便 填,不使用请填空
     */
    @XStreamAlias("goods_tag")
    private String goodsTag;

    // 预支付ID
    @XStreamAlias("prepay_id")
    private String prepayId;

    // 二维码链接
    @XStreamAlias("code_url")
    private String codeUrl;

    /**
     * 用户是否关注公众账号,Y-关注,N-未关注,仅在公众账号类型支付有效
     */
    @XStreamAlias("is_subscribe")
    private String isSubscribe;

    /**
     * 付款银行，银行类型,采用字符串类型的银行标识
     */
    @XStreamAlias("bank_type")
    private String bankType;

    /**
     * 现金券金额，现金券支付金额<=订单总金额,订单总金额-现金券金额为现金支付金额
     */
    @XStreamAlias("coupon_fee")
    private String couponFee;

    /**
     * 货币种类，货币类型,符合ISO4217标准的三位字母代码,默认人民币:CNY
     */
    @XStreamAlias("fee_type")
    private String feeType;

    /**
     * 微信支付订单号
     */
    @XStreamAlias("transaction_id")
    private String transactionId;

    /**
     * 支付完成时间，支付完成时间,格式为yyyyMMddhhmmss,如2009年12月27日9点10分10秒表示为20091227091010。时区为 GMT+8 beijing。该时间取自微信支付服务器
     */
    @XStreamAlias("time_end")
    private String timeEnd;

    /**
     * 交易状态
     * <p>
     * SUCCESS—支付成功
     * REFUND—转入退款
     * NOTPAY—未支付
     * CLOSED—已关闭
     * REVOKED—已撤销
     * USERPAYING--用户支付中
     * NOPAY--未支付(输入密码或确认支付超时)
     * PAYERROR--支付失败(其他 原因,如银行返回失败)
     * </p>
     */
    @XStreamAlias("trade_state")
    private String tradeState;

    /**
     * 操作员帐号, 默认为商户号
     */
    @XStreamAlias("op_user_id")
    private String opUserId;

    /**
     * 商户退款单号
     */
    @XStreamAlias("out_refund_no")
    private String outRefundNo;

    /**
     * 微信退款单号
     */
    @XStreamAlias("refund_id")
    private String refundId;

    /**
     * 退款渠道，ORIGINAL—原路退款,默认 BALANCE—退回到余额
     */
    @XStreamAlias("refund_channel")
    private String refundChannel;

    /**
     * 退款金额，退款总金额,单位为分,可以做部分退款
     */
    @XStreamAlias("refund_fee")
    private String refundFee;

    /**
     * 现金券退款金额
     */
    @XStreamAlias("coupon_refund_fee")
    private String couponRefundFee;

    /**
     * 需要转换的URL,签名用原串,传输需URLencode
     */
    @XStreamAlias("long_url")
    private String longUrl;

    @XStreamAlias("short_url")
    private String shortUrl;

    /////----------

    /**
     * 返回状态码，SUCCESS/FAIL
     */
    @XStreamAlias("return_code")
    private String returnCode;

    /**
     * 返回信息，返回信息,如非空,为错误原因、签名失败、参数格式校验错误
     */
    @XStreamAlias("return_msg")
    private String returnMsg;

    /**
     * 业务结果，SUCCESS/FAIL
     */
    @XStreamAlias("result_code")
    private String resultCode;

    /**
     * 错误代码
     */
    @XStreamAlias("err_code")
    private String errCode;

    /**
     * 错误代码描述
     */
    @XStreamAlias("err_code_des")
    private String errCodeDes;

    /**
     * 构造方法
     */
    public WxPayProtocol() {
    }

    /**
     * 构造方法
     *
     * @param appId
     * @param mchId
     * @param nonceStr
     */
    public WxPayProtocol(String appId, String mchId, String nonceStr) {
        this.appId = appId;
        this.mchId = mchId;
        this.nonceStr = nonceStr;
    }

    /**
     * 构造方法
     *
     * @param returnCode
     */
    public WxPayProtocol(String returnCode) {
        this.returnCode = returnCode;
    }

    /**
     * @return 输出XML格式协议文本
     */
    public String toXML() {
        XStream _xStream = XStreamHelper.createXStream(true);
        _xStream.processAnnotations(this.getClass());
        _xStream.ignoreUnknownElements();
        return _xStream.toXML(this);
    }

    /**
     * @param clazz       对象接口转换类型
     * @param protocolStr XML协议报文
     * @param <T>
     * @return 解析XML协议报文并返回clazz指定的类对象
     */
    public static <T> T fromXML(Class<? extends IWxPayProtocol> clazz, String protocolStr) {
        XStream _xStream = XStreamHelper.createXStream(true);
        _xStream.ignoreUnknownElements();
        _xStream.processAnnotations(WxPayProtocol.class);
        return (T) _xStream.fromXML(protocolStr);
    }

    /**
     * @param accountId      微信帐号原始ID
     * @param nonceStr       随机字符串
     * @param body           商品描述
     * @param outTradeNo     商户订单号
     * @param totalFee       订单总金额
     * @param spbillCreateIp 终端IP
     * @param tradeType      交易类型
     * @param productId      商品ID
     * @return 创建统一支付接口请求对象
     * @throws Exception
     */
    public static IWxPayUnifiedOrderRequest createWxPayUnifiedOrderRequest(String accountId,
                                                                           String nonceStr,
                                                                           String body,
                                                                           String outTradeNo,
                                                                           String totalFee,
                                                                           String spbillCreateIp,
                                                                           String tradeType,
                                                                           String productId) throws Exception {
        IWxPayUnifiedOrderRequest _req = new WxPayProtocol(
                WeChat.getAccountDataProvider().getAppId(accountId),
                WeChat.getAccountDataProvider().getMchId(accountId), nonceStr);
        _req.setNotifyUrl(WeChat.getAccountDataProvider().getNofityUrl(accountId));
        _req.setBody(body);
        _req.setOutTradeNo(outTradeNo);
        _req.setTotalFee(totalFee);
        _req.setSpbillCreateIp(spbillCreateIp);
        _req.setTradeType(tradeType);
        _req.setProductId(productId);
        return _req;
    }

    /**
     * @param accountId  微信帐号原始ID
     * @param nonceStr   随机字符串
     * @param outTradeNo 商户订单号
     * @return 创建订单查询接口请求对象
     * @throws Exception
     */
    public static IWxPayOrderQueryRequest createWxPayOrderQueryRequest(String accountId, String nonceStr, String outTradeNo) throws Exception {
        IWxPayOrderQueryRequest _req = new WxPayProtocol(
                WeChat.getAccountDataProvider().getAppId(accountId),
                WeChat.getAccountDataProvider().getMchId(accountId), nonceStr);
        _req.setOutTradeNo(outTradeNo);
        return _req;
    }

    /**
     * @param accountId  微信帐号原始ID
     * @param nonceStr   随机字符串
     * @param outTradeNo 商户订单号
     * @return 创建关闭订单接口请求对象
     * @throws Exception
     */
    public static IWxPayCloseOrderRequest createWxPayCloseOrderRequest(String accountId, String nonceStr, String outTradeNo) throws Exception {
        IWxPayCloseOrderRequest _req = new WxPayProtocol(
                WeChat.getAccountDataProvider().getAppId(accountId),
                WeChat.getAccountDataProvider().getMchId(accountId), nonceStr);
        _req.setOutTradeNo(outTradeNo);
        return _req;
    }

    /**
     * @param accountId   微信帐号原始ID
     * @param nonceStr    随机字符串
     * @param outTradeNo  商户订单号
     * @param outRefundNo 商户退款单号
     * @param totalFee    订单总金额
     * @param refundFee   退款金额
     * @param opUserId    操作员帐号, 默认为商户号
     * @return 创建申请退款接口请求对象
     * @throws Exception
     */
    public static IWxPayRefundRequest createWxPayRefundRequest(String accountId,
                                                               String nonceStr,
                                                               String outTradeNo,
                                                               String outRefundNo,
                                                               String totalFee,
                                                               String refundFee,
                                                               String opUserId) throws Exception {
        IWxPayRefundRequest _req = new WxPayProtocol(
                WeChat.getAccountDataProvider().getAppId(accountId),
                WeChat.getAccountDataProvider().getMchId(accountId), nonceStr);
        _req.setOutTradeNo(outTradeNo);
        _req.setOutRefundNo(outRefundNo);
        _req.setTotalFee(totalFee);
        _req.setRefundFee(refundFee);
        _req.setOpUserId(opUserId);
        return _req;
    }

    /**
     * @param accountId 微信帐号原始ID
     * @param nonceStr  随机字符串
     * @param longUrl   需要转换的URL
     * @return 创建短链接转换接口请求对象
     * @throws Exception
     */
    public static IWxPayShortUrlRequest createWxPayShortUrlRequest(String accountId, String nonceStr, String longUrl) throws Exception {
        IWxPayShortUrlRequest _req = new WxPayProtocol(
                WeChat.getAccountDataProvider().getAppId(accountId),
                WeChat.getAccountDataProvider().getMchId(accountId), nonceStr);
        _req.setLongUrl(longUrl);
        return _req;
    }

    /**
     * @return 生成用于签名的请求参数映射
     */
    public Map<String, Object> buildQueryParamMap() {
        Map<String, Object> _params = new HashMap<String, Object>();
        _params.put("appid", appId);
        _params.put("mch_id", mchId);
        _params.put("nonce_str", nonceStr);
//        if (!useToVerify) {
//            _params.put("sign", sign);
//        }
        _params.put("device_info", deviceInfo);
        _params.put("openid", openId);
        _params.put("notify_url", notifyUrl);
        _params.put("body", body);
        _params.put("attach", attach);
        _params.put("out_trade_no", outTradeNo);
        _params.put("total_fee", totalFee);
        _params.put("spbill_create_ip", spbillCreateIp);
        _params.put("time_start", timeStart);
        _params.put("time_expire", timeExpire);
        _params.put("product_id", productId);
        _params.put("goods_tag", goodsTag);
        _params.put("prepay_id", prepayId);
        _params.put("is_subscribe", isSubscribe);
        _params.put("bank_type", bankType);
        _params.put("coupon_fee", couponFee);
        _params.put("fee_type", feeType);
        _params.put("transaction_id", transactionId);
        _params.put("time_end", timeEnd);
        _params.put("trade_state", tradeState);
        _params.put("op_user_id", opUserId);
        _params.put("out_refund_no", outRefundNo);
        _params.put("refund_id", refundId);
        _params.put("refund_channel", refundChannel);
        _params.put("refund_fee", refundFee);
        _params.put("coupon_refund_fee", couponRefundFee);
        _params.put("long_url", longUrl);
        _params.put("short_url", shortUrl);
        //
        _params.put("return_code", returnCode);
        _params.put("return_msg", returnMsg);
        _params.put("result_code", resultCode);
        _params.put("err_code", errCode);
        _params.put("err_code_des", errCodeDes);
        return _params;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(String totalFee) {
        this.totalFee = totalFee;
    }

    public String getSpbillCreateIp() {
        return spbillCreateIp;
    }

    public void setSpbillCreateIp(String spbillCreateIp) {
        this.spbillCreateIp = spbillCreateIp;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeExpire() {
        return timeExpire;
    }

    public void setTimeExpire(String timeExpire) {
        this.timeExpire = timeExpire;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getGoodsTag() {
        return goodsTag;
    }

    public void setGoodsTag(String goodsTag) {
        this.goodsTag = goodsTag;
    }

    public String getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }

    public String getCodeUrl() {
        return codeUrl;
    }

    public void setCodeUrl(String codeUrl) {
        this.codeUrl = codeUrl;
    }

    public String getIsSubscribe() {
        return isSubscribe;
    }

    public void setIsSubscribe(String isSubscribe) {
        this.isSubscribe = isSubscribe;
    }

    public String getBankType() {
        return bankType;
    }

    public void setBankType(String bankType) {
        this.bankType = bankType;
    }

    public String getCouponFee() {
        return couponFee;
    }

    public void setCouponFee(String couponFee) {
        this.couponFee = couponFee;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public String getTradeState() {
        return tradeState;
    }

    public void setTradeState(String tradeState) {
        this.tradeState = tradeState;
    }

    public String getOpUserId() {
        return opUserId;
    }

    public void setOpUserId(String opUserId) {
        this.opUserId = opUserId;
    }

    public String getOutRefundNo() {
        return outRefundNo;
    }

    public void setOutRefundNo(String outRefundNo) {
        this.outRefundNo = outRefundNo;
    }

    public String getRefundId() {
        return refundId;
    }

    public void setRefundId(String refundId) {
        this.refundId = refundId;
    }

    public String getRefundChannel() {
        return refundChannel;
    }

    public void setRefundChannel(String refundChannel) {
        this.refundChannel = refundChannel;
    }

    public String getRefundFee() {
        return refundFee;
    }

    public void setRefundFee(String refundFee) {
        this.refundFee = refundFee;
    }

    public String getCouponRefundFee() {
        return couponRefundFee;
    }

    public void setCouponRefundFee(String couponRefundFee) {
        this.couponRefundFee = couponRefundFee;
    }

    public String getLongUrl() {
        return longUrl;
    }

    public void setLongUrl(String longUrl) {
        this.longUrl = longUrl;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getReturnMsg() {
        return returnMsg;
    }

    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrCodeDes() {
        return errCodeDes;
    }

    public void setErrCodeDes(String errCodeDes) {
        this.errCodeDes = errCodeDes;
    }

}
