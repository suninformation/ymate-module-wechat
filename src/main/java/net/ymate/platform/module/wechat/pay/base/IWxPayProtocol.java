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

import java.io.Serializable;
import java.util.Map;

/**
 * 微支付协议基础接口
 *
 * @author 刘镇 (suninformation@163.com) on 14/12/27 下午2:43
 * @version 1.0
 */
public interface IWxPayProtocol extends Serializable {

    public String getAppId();

    public void setAppId(String appId);

    public String getMchId();

    public void setMchId(String mchId);

    public String getNonceStr();

    public void setNonceStr(String nonceStr);

    public String getSign();

    public void setSign(String sign);

    /**
     * @return 输出XML格式协议文本
     */
    public String toXML();

    /**
     * @return 生成用于签名的请求参数映射
     */
    public Map<String, Object> buildQueryParamMap();

}
