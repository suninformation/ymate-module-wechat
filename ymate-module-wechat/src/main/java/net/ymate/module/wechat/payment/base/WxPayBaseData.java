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

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Map;

/**
 * @author 刘镇 (suninformation@163.com) on 16/6/26 上午12:51
 * @version 1.0
 */
public abstract class WxPayBaseData {

    /**
     * 商户号
     */
    private String mchId;

    /**
     * 随机字符串
     */
    private String nonceStr;

    /**
     * 签名
     */
    private String sign;

    public String mchId() {
        return mchId;
    }

    protected WxPayBaseData mchId(String mchId) {
        this.mchId = mchId;
        return this;
    }

    public String nonceStr() {
        return nonceStr;
    }

    protected WxPayBaseData nonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
        return this;
    }

    public String sign() {
        return sign;
    }

    protected WxPayBaseData sign(String sign) {
        this.sign = sign;
        return this;
    }

    /**
     * @param paramMap 请求协议参数对象映射
     * @param mchKey   商户密钥
     * @return 返回最终生成的签名
     */
    protected String __doCreateSignature(Map<String, Object> paramMap, String mchKey) {
        String _queryParamStr = __doBuildQueryParamStr(paramMap, false, null);
        _queryParamStr += "&key=" + mchKey;
        return DigestUtils.md5Hex(_queryParamStr).toUpperCase();
    }

    /**
     * @param params  参数映射
     * @param encode  是否为参数值编码
     * @param charset 字符集名称
     * @return 对参数进行ASCII正序排列并生成请求参数串
     */
    protected String __doBuildQueryParamStr(Map<String, Object> params, boolean encode, String charset) {
        String[] _keys = params.keySet().toArray(new String[0]);
        Arrays.sort(_keys);
        StringBuilder _paramSB = new StringBuilder();
        boolean _flag = true;
        for (String _key : _keys) {
            Object _value = params.get(_key);
            if (_value != null) {
                if (_flag) {
                    _flag = false;
                } else {
                    _paramSB.append("&");
                }
                String _valueStr = _value.toString();
                if (encode) {
                    try {
                        _paramSB.append(_key).append("=").append(URLEncoder.encode(_valueStr, StringUtils.defaultIfBlank(charset, "UTF-8")));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                } else {
                    _paramSB.append(_key).append("=").append(_valueStr);
                }
            }
        }
        return _paramSB.toString();
    }
}
