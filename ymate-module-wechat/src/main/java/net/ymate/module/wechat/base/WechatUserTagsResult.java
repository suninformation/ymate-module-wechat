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
 * @author 刘镇 (suninformation@163.com) on 16/6/25 上午1:44
 * @version 1.0
 */
public class WechatUserTagsResult extends WechatResult {

    private Integer[] tagIds;

    public WechatUserTagsResult(JSONObject result) {
        super(result);
        if (this.getErrCode() == 0) {
            if (result.containsKey("tagid_list")) {
                tagIds = result.getJSONArray("tagid_list").toArray(new Integer[0]);
            } else {
                tagIds = new Integer[]{result.getIntValue("groupid")};
            }
        }
    }

    public Integer[] getTagIds() {
        return tagIds;
    }
}
