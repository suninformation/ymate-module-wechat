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
 * @author 刘镇 (suninformation@163.com) on 16/6/24 下午5:27
 * @version 1.0
 */
public class WechatMaterialCountResult extends WechatResult {

    private int voiceCount;

    private int videoCount;

    private int imageCount;

    private int newsCount;

    public WechatMaterialCountResult(JSONObject result) {
        super(result);
        if (this.getErrCode() == 0) {
            this.voiceCount = result.getIntValue("voice_count");
            this.videoCount = result.getIntValue("video_count");
            this.imageCount = result.getIntValue("image_count");
            this.newsCount = result.getIntValue("news_count");
        }
    }

    public int getVoiceCount() {
        return voiceCount;
    }

    public int getVideoCount() {
        return videoCount;
    }

    public int getImageCount() {
        return imageCount;
    }

    public int getNewsCount() {
        return newsCount;
    }
}
