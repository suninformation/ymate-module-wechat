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

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * WxMenuItem
 * </p>
 * <p>
 * <p/>
 * </p>
 *
 * @author 刘镇(suninformation@163.com)
 * @version 0.0.0
 *          <table style="border:1px solid gray;">
 *          <tr>
 *          <th width="100px">版本号</th><th width="100px">动作</th><th
 *          width="100px">修改人</th><th width="100px">修改时间</th>
 *          </tr>
 *          <!-- 以 Table 方式书写修改历史 -->
 *          <tr>
 *          <td>0.0.0</td>
 *          <td>创建类</td>
 *          <td>刘镇</td>
 *          <td>2014年3月20日下午3:46:35</td>
 *          </tr>
 *          </table>
 */
public class WxMenuItem {

    public static final String TYPE_CLICK = "click";

    public static final String TYPE_VIEW = "view";

    public final static String TYPE_MEDIA_ID = "media_id";

    public final static String TYPE_VIEW_LIMITED = "view_limited";

    private String name;

    @JSONField(name = "sub_button")
    private List<WxMenuItem> subItems = new ArrayList<WxMenuItem>();

    private String type;

    private String key;

    private String url;

    @JSONField(name = "media_id")
    private String mediaId;

    public static WxMenuItem create() {
        return new WxMenuItem();
    }

    public WxMenuItem addItem(WxMenuItem item) {
        subItems.add(item);
        return this;
    }

    public String getName() {
        return name;
    }

    public WxMenuItem setName(String name) {
        this.name = name;
        return this;
    }

    public List<WxMenuItem> getSubItems() {
        return subItems;
    }

    public void setSubItems(List<WxMenuItem> subItems) {
        this.subItems = subItems;
    }

    public String getType() {
        return type;
    }

    public WxMenuItem setType(String type) {
        this.type = type;
        return this;
    }

    public String getKey() {
        return key;
    }

    public WxMenuItem setKey(String key) {
        this.key = key;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public WxMenuItem setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }
}
