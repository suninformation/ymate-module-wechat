/*
 * Copyright 2007-2107 the original author or authors.
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
package net.ymate.platform.module.wechat.base;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * @author 刘镇 (suninformation@163.com) on 15/5/18 下午2:22
 * @version 1.0
 */
public class WxMedia {

    @JSONField(name = "media_id")
    private String mediaId;

    private String name;

    private WxNewsItem content;

    @JSONField(name = "update_time")
    private Long updateTime;

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public WxNewsItem getContent() {
        return content;
    }

    public void setContent(WxNewsItem content) {
        this.content = content;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }
}
