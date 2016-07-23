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
 * @author 刘镇 (suninformation@163.com) on 16/5/31 上午12:55
 * @version 1.0
 */
public class WechatUserListResult extends WechatResult {

    private long total;

    private int count;

    private String nextOpenId;

    private String[] data;

    public WechatUserListResult(JSONObject result) {
        super(result);
        if (this.getErrCode() == 0) {
            this.total = result.getLongValue("total");
            this.count = result.getIntValue("count");
            this.nextOpenId = result.getString("next_openid");
            if (result.containsKey("total")) {
                this.data = result.getJSONArray("data").toArray(new String[0]);
            } else {
                this.data = result.getJSONObject("data").getJSONArray("openid").toArray(new String[0]);
            }
        }
    }

    public long getTotal() {
        return total;
    }

    public int getCount() {
        return count;
    }

    public String getNextOpenId() {
        return nextOpenId;
    }

    public String[] getData() {
        return data;
    }
}
