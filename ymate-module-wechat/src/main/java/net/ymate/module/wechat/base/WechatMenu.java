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

import com.alibaba.fastjson.annotation.JSONField;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 刘镇 (suninformation@163.com) on 16/5/31 下午11:15
 * @version 1.0
 */
public class WechatMenu {

    @JSONField(name = "button")
    private List<WechatMenuItem> items = new ArrayList<WechatMenuItem>();

    public static WechatMenu create() {
        return new WechatMenu();
    }

    public WechatMenu addItem(WechatMenuItem item) {
        items.add(item);
        return this;
    }

    public List<WechatMenuItem> getItems() {
        return items;
    }

    public void setItems(List<WechatMenuItem> items) {
        this.items = items;
    }
}
