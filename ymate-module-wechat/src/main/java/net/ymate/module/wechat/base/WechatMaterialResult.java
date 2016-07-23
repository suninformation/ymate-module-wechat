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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 刘镇 (suninformation@163.com) on 16/6/23 下午5:05
 * @version 1.0
 */
public class WechatMaterialResult extends WechatResult {

    private int totalCount;

    private int itemCount;

    private List<WechatMediaItem> items;

    public WechatMaterialResult(JSONObject result) {
        super(result);
        this.items = new ArrayList<WechatMediaItem>();
        if (this.getErrCode() == 0) {
            this.totalCount = result.getIntValue("total_count");
            this.itemCount = result.getIntValue("item_count");
            //
            JSONArray _jsonArr = result.getJSONArray("item");
            if (_jsonArr != null && !_jsonArr.isEmpty()) {
                for (int _idx = 0; _idx < _jsonArr.size(); _idx++) {
                    WechatMediaItem _item = _jsonArr.getObject(_idx, WechatMediaItem.class);
                    if (_item != null) {
                        this.items.add(_item);
                    }
                }
            }
        }
    }

    public int getTotalCount() {
        return totalCount;
    }

    public int getItemCount() {
        return itemCount;
    }

    public List<WechatMediaItem> getItems() {
        return items;
    }
}
