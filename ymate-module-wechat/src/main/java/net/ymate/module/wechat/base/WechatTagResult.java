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
 * @author 刘镇 (suninformation@163.com) on 16/5/31 上午1:50
 * @version 1.0
 */
public class WechatTagResult extends WechatResult {

    private int id;

    private String name;

    private long count;

    public WechatTagResult(JSONObject result) {
        super(result);
        if (this.getErrCode() == 0) {
            JSONObject _tag = result.getJSONObject("tag");
            if (_tag == null) {
                _tag = result.getJSONObject("group");
            }
            if (_tag != null) {
                this.id = _tag.getIntValue("id");
                this.name = _tag.getString("name");
            }
        }
    }

    public WechatTagResult(int id, String name, long count) {
        super(new JSONObject());
        this.id = id;
        this.name = name;
        this.count = count;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getCount() {
        return count;
    }
}
