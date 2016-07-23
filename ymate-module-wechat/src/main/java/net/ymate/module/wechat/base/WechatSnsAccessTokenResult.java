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
package net.ymate.module.wechat.base;

import com.alibaba.fastjson.JSONObject;

/**
 * @author 刘镇 (suninformation@163.com) on 16/5/30 上午10:34
 * @version 1.0
 */
public class WechatSnsAccessTokenResult extends WechatAccessTokenResult {

    private String refreshToken;

    private String openId;

    private String scope;

    private String unionId;

    public WechatSnsAccessTokenResult(JSONObject result) {
        super(result);
        if (this.getErrCode() == 0) {
            this.refreshToken = result.getString("refresh_token");
            this.openId = result.getString("openid");
            this.scope = result.getString("scope");
            this.unionId = result.getString("unionid");
        }
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getOpenId() {
        return openId;
    }

    public String getScope() {
        return scope;
    }

    public String getUnionId() {
        return unionId;
    }
}
