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
 * @author 刘镇 (suninformation@163.com) on 16/6/26 下午10:02
 * @version 1.0
 */
public class WxPayCloseOrderRequest extends WxPayBaseRequest<WxPayCloseOrderResponse> {

    /**
     * 公众账号ID
     */
    private String appId;

    /**
     * 商户订单号
     */
    private String outTradeNo;

    public static WxPayCloseOrderRequest create(WechatAccountMeta accountMeta) {
        return new WxPayCloseOrderRequest(accountMeta);
    }

    public WxPayCloseOrderRequest(WechatAccountMeta accountMeta) {
        super(accountMeta);
        this.appId = accountMeta.getAppId();
    }

    public String appId() {
        return appId;
    }

    public WxPayCloseOrderRequest appId(String appId) {
        this.appId = appId;
        return this;
    }

    public String outTradeNo() {
        return outTradeNo;
    }

    public WxPayCloseOrderRequest outTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
        return this;
    }

    @Override
    protected Map<String, Object> buildSignatureParams() {
        if (StringUtils.isBlank(this.appId)) {
            throw new NullArgumentException("appId");
        }
        if (StringUtils.isBlank(this.outTradeNo)) {
            throw new NullArgumentException("outTradeNo");
        }
        Map<String, Object> _params = super.buildSignatureParams();
        _params.put("appid", appId);
        _params.put("out_trade_no", outTradeNo);
        return _params;
    }

    protected String __doGetRequestURL() {
        return IWechat.WX_PAY_API.PAY_UNIFIED_ORDER;
    }

    protected WxPayCloseOrderResponse __doParseResponse(IHttpResponse httpResponse) throws Exception {
        return WxPayCloseOrderResponse.bind(httpResponse.getContent());
    }
}
