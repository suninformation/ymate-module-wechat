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
package net.ymate.module.wechat.payment.base;

import net.ymate.framework.commons.HttpClientHelper;
import net.ymate.framework.commons.IHttpResponse;
import net.ymate.module.wechat.base.WechatAccountMeta;
import net.ymate.platform.core.util.UUIDUtils;
import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 刘镇 (suninformation@163.com) on 16/6/28 下午2:50
 * @version 1.0
 */
public abstract class WxPayBaseRequest<RESPONSE extends WxPayBaseResponse> extends WxPayBaseData {

    private static final Log _LOG = LogFactory.getLog(WxPayBaseRequest.class);

    private WechatAccountMeta __accountMeta;

    public WxPayBaseRequest(WechatAccountMeta accountMeta) {
        if (StringUtils.isBlank(accountMeta.getMchId())) {
            throw new NullArgumentException("mchId");
        }
        if (StringUtils.isBlank(accountMeta.getMchKey())) {
            throw new NullArgumentException("mchKey");
        }
        __accountMeta = accountMeta;
        this.mchId(__accountMeta.getMchId());
        this.nonceStr(__doCreateNonceStr());
    }

    /**
     * @return 返回用于签名的请求参数映射
     */
    protected Map<String, Object> buildSignatureParams() {
        Map<String, Object> _params = new HashMap<String, Object>();
        _params.put("mch_id", this.mchId());
        _params.put("nonce_str", this.nonceStr());
        return _params;
    }

    /**
     * @return 产生随机字符串，长度为6到32位不等
     */
    private String __doCreateNonceStr() {
        return UUIDUtils.randomStr(UUIDUtils.randomInt(6, 32), false).toLowerCase();
    }

    private String __doBuildXML(Map<String, Object> paramMap) {
        StringBuilder _xmlSB = new StringBuilder("<xml>");
        for (Map.Entry<String, Object> _entry : paramMap.entrySet()) {
            if (_entry.getValue() != null) {
                _xmlSB.append("<").append(_entry.getKey()).append(">");
                if (_entry.getValue() instanceof String) {
                    _xmlSB.append("<![CDATA[").append(_entry.getValue().toString()).append("]]>");
                } else {
                    _xmlSB.append(_entry.getValue().toString());
                }
                _xmlSB.append("</").append(_entry.getKey()).append(">");
            }
        }
        return _xmlSB.append("</xml>").toString();
    }

    /**
     * @return 返回API请求地址
     */
    protected abstract String __doGetRequestURL();

    /**
     * @param httpResponse HTTP请求回应对象
     * @return 分析请求回应结果
     * @throws Exception 可能产生的任何异常
     */
    protected abstract RESPONSE __doParseResponse(IHttpResponse httpResponse) throws Exception;

    /**
     * @return 提交请求并返回回应结果
     */
    public RESPONSE send() {
        Map<String, Object> _params = buildSignatureParams();
        _params.put("sign", __doCreateSignature(_params, __accountMeta.getMchKey()));
        //
        try {
            IHttpResponse _response = HttpClientHelper.create()
                    .customSSL(__accountMeta.getConnectionSocketFactory())
                    .post(__doGetRequestURL(), __doBuildXML(_params));
            return __doParseResponse(_response);
        } catch (Exception e) {
            _LOG.warn("", e);
        }
        return null;
    }
}
