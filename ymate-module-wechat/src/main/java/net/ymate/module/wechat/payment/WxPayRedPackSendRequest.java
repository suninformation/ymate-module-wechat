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
 * @author 刘镇 (suninformation@163.com) on 16/6/27 上午12:34
 * @version 1.0
 */
public class WxPayRedPackSendRequest extends WxPayBaseRequest<WxPayRedPackSendResponse> {

    /**
     * 公众账号ID
     */
    private String wxAppId;

    /**
     * 商户订单号
     */
    private String mchBillNo;

    /**
     * 商户名称
     */
    private String sendName;

    /**
     * 用户openid
     */
    private String reOpenId;

    /**
     * 总金额
     */
    private Integer totalAmount;

    /**
     * 红包发放总人数
     */
    private Integer totalNum;

    /**
     * 红包祝福语
     */
    private String wishing;

    /**
     * Ip地址
     */
    private String clientIp;

    /**
     * 活动名称
     */
    private String actName;

    /**
     * 备注
     */
    private String remark;

    public static WxPayRedPackSendRequest create(WechatAccountMeta accountMeta) {
        return new WxPayRedPackSendRequest(accountMeta);
    }

    public WxPayRedPackSendRequest(WechatAccountMeta accountMeta) {
        super(accountMeta);
        this.wxAppId = accountMeta.getAppId();
    }

    public String wxAppId() {
        return wxAppId;
    }

    public WxPayRedPackSendRequest wxAppId(String wxAppId) {
        this.wxAppId = wxAppId;
        return this;
    }

    public String mchBillNo() {
        return mchBillNo;
    }

    public WxPayRedPackSendRequest mchBillNo(String mchBillNo) {
        this.mchBillNo = mchBillNo;
        return this;
    }

    public String sendName() {
        return sendName;
    }

    public WxPayRedPackSendRequest sendName(String sendName) {
        this.sendName = sendName;
        return this;
    }

    public String reOpenId() {
        return reOpenId;
    }

    public WxPayRedPackSendRequest reOpenId(String reOpenId) {
        this.reOpenId = reOpenId;
        return this;
    }

    public Integer totalAmount() {
        return totalAmount;
    }

    public WxPayRedPackSendRequest totalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
        return this;
    }

    public Integer totalNum() {
        return totalNum;
    }

    public WxPayRedPackSendRequest totalNum(Integer totalNum) {
        this.totalNum = totalNum;
        return this;
    }

    public String wishing() {
        return wishing;
    }

    public WxPayRedPackSendRequest wishing(String wishing) {
        this.wishing = wishing;
        return this;
    }

    public String clientIp() {
        return clientIp;
    }

    public WxPayRedPackSendRequest clientIp(String clientIp) {
        this.clientIp = clientIp;
        return this;
    }

    public String actName() {
        return actName;
    }

    public WxPayRedPackSendRequest actName(String actName) {
        this.actName = actName;
        return this;
    }

    public String remark() {
        return remark;
    }

    public WxPayRedPackSendRequest remark(String remark) {
        this.remark = remark;
        return this;
    }

    @Override
    public Map<String, Object> buildSignatureParams() {
        if (StringUtils.isBlank(this.wxAppId)) {
            throw new NullArgumentException("wxAppId");
        }
        if (StringUtils.isBlank(this.mchBillNo)) {
            throw new NullArgumentException("mchBillNo");
        }
        if (StringUtils.isBlank(this.sendName)) {
            throw new NullArgumentException("sendName");
        }
        if (StringUtils.isBlank(this.reOpenId)) {
            throw new NullArgumentException("reOpenId");
        }
        if (this.totalAmount == null || this.totalAmount <= 0) {
            throw new NullArgumentException("totalAmount");
        }
        if (this.totalNum == null || this.totalNum <= 0) {
            throw new NullArgumentException("totalNum");
        }
        if (StringUtils.isBlank(this.wishing)) {
            throw new NullArgumentException("wishing");
        }
        if (StringUtils.isBlank(this.clientIp)) {
            throw new NullArgumentException("clientIp");
        }
        if (StringUtils.isBlank(this.actName)) {
            throw new NullArgumentException("actName");
        }
        if (StringUtils.isBlank(this.remark)) {
            throw new NullArgumentException("remark");
        }
        //
        Map<String, Object> _params = super.buildSignatureParams();
        _params.put("wxappid", wxAppId);
        _params.put("mch_billno", mchBillNo);
        _params.put("send_name", sendName);
        _params.put("re_openid", reOpenId);
        _params.put("total_amount", totalAmount);
        _params.put("total_num", totalNum);
        _params.put("wishing", wishing);
        _params.put("client_ip", clientIp);
        _params.put("act_name", actName);
        _params.put("remark", remark);
        return _params;
    }

    protected String __doGetRequestURL() {
        return IWechat.WX_PAY_API.REDPACK_SEND_URL;
    }

    protected WxPayRedPackSendResponse __doParseResponse(IHttpResponse httpResponse) throws Exception {
        return WxPayRedPackSendResponse.bind(httpResponse.getContent());
    }
}
