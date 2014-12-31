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
 * 退款查询接口请求对象
 *
 * @author 刘镇 (suninformation@163.com) on 14/12/30 下午7:31
 * @version 1.0
 */
public interface IWxPayRefundQueryRequest extends IWxPayProtocol {

    public String getTransactionId();

    public void setTransactionId(String transactionId);

    public String getOutTradeNo();

    public void setOutTradeNo(String outTradeNo);

    public String getOutRefundNo();

    public void setOutRefundNo(String outRefundNo);

    public String getRefundId();

    public void setRefundId(String refundId);

}
