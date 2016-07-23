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
package net.ymate.module.wechat;

import com.alibaba.fastjson.JSON;
import net.ymate.framework.commons.HttpClientHelper;
import net.ymate.framework.commons.IHttpResponse;
import net.ymate.module.wechat.base.WechatAccessToken;
import net.ymate.module.wechat.base.WechatAccessTokenResult;
import net.ymate.module.wechat.base.WechatJsTicketResult;
import net.ymate.module.wechat.base.WechatTicket;

/**
 * @author 刘镇 (suninformation@163.com) on 16/7/11 下午6:36
 * @version 1.0
 */
public abstract class AbstractWechatTokenCacheAdapter implements IWechatTokenCacheAdapter {

    private IWechat __owner;

    public void init(IWechat owner) throws Exception {
        __owner = owner;
    }

    public IWechat getOwner() {
        return __owner;
    }

    protected WechatAccessToken __doGetAccessToken(String appId, String appSecret) throws Exception {
        IHttpResponse _response = HttpClientHelper.create().get(IWechat.WX_API.WX_ACCESS_TOKEN.concat("&appid=").concat(appId).concat("&secret=").concat(appSecret));
        WechatAccessTokenResult _result = new WechatAccessTokenResult(JSON.parseObject(_response.getContent()));
        if (_response.getStatusCode() == 200 && _result.getErrCode() == 0) {
            return new WechatAccessToken(_result.getAccessToken(), _result.getExpiredTime());
        }
        return null;
    }

    protected WechatTicket __doGetJsTicket(String accessToken) throws Exception {
        IHttpResponse _response = HttpClientHelper.create().get(IWechat.WX_API.WX_JSAPI_TICKET.concat(accessToken));
        WechatJsTicketResult _result = new WechatJsTicketResult(JSON.parseObject(_response.getContent()));
        if (_response.getStatusCode() == 200 && _result.getErrCode() == 0) {
            return new WechatTicket(_result.getTicket(), _result.getExpiredTime());
        }
        return null;
    }
}
