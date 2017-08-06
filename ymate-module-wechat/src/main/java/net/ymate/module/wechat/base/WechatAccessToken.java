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

import java.io.Serializable;

/**
 * @author 刘镇 (suninformation@163.com) on 16/7/11 下午6:06
 * @version 1.0
 */
public class WechatAccessToken implements Serializable {

    private String token;

    private long expiredTime;

    public WechatAccessToken(String token, long expiredTime) {
        this.token = token;
        this.expiredTime = expiredTime;
    }

    public String getToken() {
        return token;
    }

    public long getExpiredTime() {
        return expiredTime;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() >= expiredTime;
    }
}
