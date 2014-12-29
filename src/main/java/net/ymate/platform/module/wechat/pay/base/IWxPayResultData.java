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

/**
 * @author 刘镇 (suninformation@163.com) on 14/12/27 下午4:16
 * @version 1.0
 */
public interface IWxPayResultData extends IWxPayProtocol {

    public String getReturnCode();

    public void setReturnCode(String returnCode);

    public String getReturnMsg();

    public void setReturnMsg(String returnMsg);

    public String getResultCode();

    public void setResultCode(String resultCode);

    public String getErrCode();

    public void setErrCode(String errCode);

    public String getErrCodeDes();

    public void setErrCodeDes(String errCodeDes);

}
