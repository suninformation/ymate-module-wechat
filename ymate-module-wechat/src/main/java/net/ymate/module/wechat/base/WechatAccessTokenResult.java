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
 * @author 刘镇 (suninformation@163.com) on 16/5/28 上午2:39
 * @version 1.0
 */
public class WechatAccessTokenResult extends WechatResult {

    private String accessToken;

    private long expiredTime;

    public WechatAccessTokenResult(JSONObject result) {
        super(result);
        if (this.getErrCode() == 0) {
            this.accessToken = result.getString("access_token");
            this.expiredTime = System.currentTimeMillis() + (result.getIntValue("expires_in") - 10) * 1000;
        }
    }

    public String getAccessToken() {
        return accessToken;
    }

    public long getExpiredTime() {
        return expiredTime;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() < expiredTime;
    }
}
