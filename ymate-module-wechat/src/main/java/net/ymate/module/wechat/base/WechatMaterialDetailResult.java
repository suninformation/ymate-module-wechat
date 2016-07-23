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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * @author 刘镇 (suninformation@163.com) on 16/6/24 下午4:31
 * @version 1.0
 */
public class WechatMaterialDetailResult extends WechatResult {

    private WechatMediaItem.MediaContent content;

    public WechatMaterialDetailResult(JSONObject result) {
        super(result);
        if (this.getErrCode() == 0) {
            this.content = JSON.toJavaObject(result, WechatMediaItem.MediaContent.class);
        }
    }

    public WechatMediaItem.MediaContent getContent() {
        return content;
    }
}
