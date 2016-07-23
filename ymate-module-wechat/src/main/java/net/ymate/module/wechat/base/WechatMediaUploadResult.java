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
import net.ymate.module.wechat.IWechat;

/**
 * @author 刘镇 (suninformation@163.com) on 16/5/31 下午11:48
 * @version 1.0
 */
public class WechatMediaUploadResult extends WechatResult {

    private IWechat.MediaType type;

    private String mediaId;

    private String thumbMediaId;

    private long createAt;

    private String url;

    public WechatMediaUploadResult(IWechat.MediaType type, JSONObject result) {
        super(result);
        if (this.getErrCode() == 0) {
            this.type = type;
            this.mediaId = result.getString("media_id");
            this.thumbMediaId = result.getString("thumb_media_id");
            this.createAt = result.getLongValue("created_at");
            this.url = result.getString("url");
        }
    }

    public IWechat.MediaType getType() {
        return type;
    }

    public String getMediaId() {
        return mediaId;
    }

    public String getThumbMediaId() {
        return thumbMediaId;
    }

    public long getCreateAt() {
        return createAt;
    }

    public String getUrl() {
        return url;
    }
}
