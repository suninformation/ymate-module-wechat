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
 * @author 刘镇 (suninformation@163.com) on 16/6/27 上午12:51
 * @version 1.0
 */
public class WxPayMchPayRequest extends WxPayBaseRequest<WxPayMchPayResponse> {

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
     * 用户openid
     */
    private String openId;

    /**
     * 校验用户姓名选项
     */
    private String checkName;

    /**
     * 收款用户姓名
     */
    private String reUserName;

    /**
     * 金额
     */
    private Integer amount;

    /**
     * 企业付款描述信息
     */
    private String desc;

    /**
     * Ip地址
     */
    private String spbillCreateIp;

    public static WxPayMchPayRequest create(WechatAccountMeta accountMeta) {
        return new WxPayMchPayRequest(accountMeta);
    }

    public WxPayMchPayRequest(WechatAccountMeta accountMeta) {
        super(accountMeta);
        this.mchAppId = accountMeta.getAppId();
    }

    public String mchAppId() {
        return mchAppId;
    }

    public WxPayMchPayRequest mchAppId(String mchAppId) {
        this.mchAppId = mchAppId;
        return this;
    }

    public String deviceInfo() {
        return deviceInfo;
    }

    public WxPayMchPayRequest deviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
        return this;
    }

    public String partnerTradeNo() {
        return partnerTradeNo;
    }

    public WxPayMchPayRequest partnerTradeNo(String partnerTradeNo) {
        this.partnerTradeNo = partnerTradeNo;
        return this;
    }

    public String openId() {
        return openId;
    }

    public WxPayMchPayRequest openId(String openId) {
        this.openId = openId;
        return this;
    }

    public String checkName() {
        return checkName;
    }

    public WxPayMchPayRequest checkName(String checkName) {
        this.checkName = checkName;
        return this;
    }

    public String reUserName() {
        return reUserName;
    }

    public WxPayMchPayRequest reUserName(String reUserName) {
        this.reUserName = reUserName;
        return this;
    }

    public Integer amount() {
        return amount;
    }

    public WxPayMchPayRequest amount(Integer amount) {
        this.amount = amount;
        return this;
    }

    public String desc() {
        return desc;
    }

    public WxPayMchPayRequest desc(String desc) {
        this.desc = desc;
        return this;
    }

    public String spbillCreateIp() {
        return spbillCreateIp;
    }

    public WxPayMchPayRequest spbillCreateIp(String spbillCreateIp) {
        this.spbillCreateIp = spbillCreateIp;
        return this;
    }

    @Override
    public Map<String, Object> buildSignatureParams() {
        if (StringUtils.isBlank(this.mchAppId)) {
            throw new NullArgumentException("mchAppId");
        }
        if (StringUtils.isBlank(this.partnerTradeNo)) {
            throw new NullArgumentException("partnerTradeNo");
        }
        if (StringUtils.isBlank(this.openId)) {
            throw new NullArgumentException("openId");
        }
        if (StringUtils.isBlank(this.checkName)) {
            throw new NullArgumentException("checkName");
        }
        if (this.amount == null || this.amount <= 0) {
            throw new NullArgumentException("amount");
        }
        if (StringUtils.isBlank(this.desc)) {
            throw new NullArgumentException("desc");
        }
        if (StringUtils.isBlank(this.spbillCreateIp)) {
            throw new NullArgumentException("spbillCreateIp");
        }
        //
        Map<String, Object> _params = super.buildSignatureParams();
        _params.put("mch_appid", mchAppId);
        _params.put("device_info", deviceInfo);
        _params.put("partner_trade_no", partnerTradeNo);
        _params.put("openid", openId);
        _params.put("check_name", checkName);
        _params.put("re_user_name", reUserName);
        _params.put("amount", amount);
        _params.put("desc", desc);
        _params.put("spbill_create_ip", spbillCreateIp);
        return _params;
    }

    protected String __doGetRequestURL() {
        return IWechat.WX_PAY_API.MCH_PAY_URL;
    }

    protected WxPayMchPayResponse __doParseResponse(IHttpResponse httpResponse) throws Exception {
        return WxPayMchPayResponse.bind(httpResponse.getContent());
    }
}
