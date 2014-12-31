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
package net.ymate.platform.module.wechat.pay;

import net.ymate.platform.module.wechat.pay.base.IWxPayProtocol;

/**
 * 统一支付接口请求对象
 *
 * @author 刘镇 (suninformation@163.com) on 14/12/27 下午2:58
 * @version 1.0
 */
public interface IWxPayUnifiedOrderRequest extends IWxPayProtocol {

    public String getTradeType();

    public void setTradeType(String tradeType);

    public String getDeviceInfo();

    public void setDeviceInfo(String deviceInfo);

    public String getOpenId();

    public void setOpenId(String openId);

    public String getNotifyUrl();

    public void setNotifyUrl(String notifyUrl);

    public String getBody();

    public void setBody(String body);

    public String getAttach();

    public void setAttach(String attach);

    public String getOutTradeNo();

    public void setOutTradeNo(String outTradeNo);

    public Integer getTotalFee();

    public void setTotalFee(Integer totalFee);

    public String getSpbillCreateIp();

    public void setSpbillCreateIp(String spbillCreateIp);

    public String getTimeStart();

    public void setTimeStart(String timeStart);

    public String getTimeExpire();

    public void setTimeExpire(String timeExpire);

    public String getProductId();

    public void setProductId(String productId);

    public String getGoodsTag();

    public void setGoodsTag(String goodsTag);

}
