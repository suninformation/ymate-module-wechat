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
 * @author 刘镇 (suninformation@163.com) on 15/5/18 上午11:41
 * @version 1.0
 */
public class WxMaterialResult {

    @JSONField(name = "total_count")
    private int totalCount;

    @JSONField(name = "item_count")
    private int itemCount;

    private List<WxMedia> item;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public List<WxMedia> getItem() {
        return item;
    }

    public void setItem(List<WxMedia> item) {
        this.item = item;
    }
}
