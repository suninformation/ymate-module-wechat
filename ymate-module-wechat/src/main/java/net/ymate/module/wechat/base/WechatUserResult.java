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
 * @author 刘镇 (suninformation@163.com) on 16/5/31 上午1:08
 * @version 1.0
 */
public class WechatUserResult extends WechatSnsUserResult {

    private int subscribe;

    private int subscribeTime;

    private int groupId;

    private String language;

    private String remark;

    private Integer[] tagidList;

    public WechatUserResult(JSONObject result) {
        super(result);
        if (this.getErrCode() == 0) {
            this.subscribe = result.getIntValue("subscribe");
            this.subscribeTime = result.getIntValue("subscribe_time");
            this.groupId = result.getIntValue("groupid");
            this.language = result.getString("language");
            this.remark = result.getString("remark");
            this.tagidList = result.getJSONArray("tagid_list").toArray(new Integer[0]);
        }
    }

    public int getSubscribe() {
        return subscribe;
    }

    public int getSubscribeTime() {
        return subscribeTime;
    }

    public int getGroupId() {
        return groupId;
    }

    public String getLanguage() {
        return language;
    }

    public String getRemark() {
        return remark;
    }

    public Integer[] getTagidList() {
        return tagidList;
    }
}
