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
import net.ymate.framework.commons.XPathHelper;
import net.ymate.module.wechat.IWechat;
import net.ymate.module.wechat.base.WechatAccountMeta;
import net.ymate.module.wechat.payment.base.WxPayBaseRequest;
import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang.StringUtils;

import java.util.Map;

/**
 * @author 刘镇 (suninformation@163.com) on 16/6/26 下午11:54
 * @version 1.0
 */
public class WxPayDownloadBillRequest extends WxPayBaseRequest<WxPayDownloadBillResponse> {

    /**
     * 公众账号ID
     */
    private String appId;

    /**
     * 设备号
     */
    private String deviceInfo;

    /**
     * 对账单日期
     */
    private String billData;

    /**
     * 账单类型
     */
    private String billType;

    public static WxPayDownloadBillRequest create(WechatAccountMeta accountMeta) {
        return new WxPayDownloadBillRequest(accountMeta);
    }

    public WxPayDownloadBillRequest(WechatAccountMeta accountMeta) {
        super(accountMeta);
        this.appId = accountMeta.getAppId();
    }

    public String appId() {
        return appId;
    }

    public WxPayDownloadBillRequest appId(String appId) {
        this.appId = appId;
        return this;
    }

    public String deviceInfo() {
        return deviceInfo;
    }

    public WxPayDownloadBillRequest deviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
        return this;
    }

    public String billData() {
        return billData;
    }

    public WxPayDownloadBillRequest billData(String billData) {
        this.billData = billData;
        return this;
    }

    public String billType() {
        return billType;
    }

    public WxPayDownloadBillRequest billType(String billType) {
        this.billType = billType;
        return this;
    }

    @Override
    public Map<String, Object> buildSignatureParams() {
        if (StringUtils.isBlank(this.appId)) {
            throw new NullArgumentException("appId");
        }
        if (StringUtils.isBlank(this.billData)) {
            throw new NullArgumentException("billData");
        }
        Map<String, Object> _params = super.buildSignatureParams();
        _params.put("appid", appId);
        _params.put("device_info", deviceInfo);
        _params.put("bill_date", billData);
        _params.put("bill_type", billType);
        return _params;
    }

    protected String __doGetRequestURL() {
        return IWechat.WX_PAY_API.PAY_DOWNLOAD_BILL;
    }

    protected WxPayDownloadBillResponse __doParseResponse(IHttpResponse httpResponse) {
        try {
            XPathHelper _xpath = new XPathHelper(httpResponse.getContent());
            return new WxPayDownloadBillResponse(null, _xpath.getStringValue("//return_code"), _xpath.getStringValue("//return_msg"));
        } catch (Exception e) {
            // Nothing..
        }
        return new WxPayDownloadBillResponse(httpResponse.getContent(), "SUCCESS", null);
    }
}
