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
 * @author 刘镇 (suninformation@163.com) on 16/5/30 上午11:00
 * @version 1.0
 */
public class WechatSnsUserResult extends WechatResult {

    private String openId;

    private String nickname;

    private Integer sex;

    private String city;

    private String province;

    private String country;

    private String headImgUrl;

    private String[] privilege;

    private String unionId;

    public WechatSnsUserResult(JSONObject result) {
        super(result);
        if (this.getErrCode() == 0) {
            this.openId = result.getString("openid");
            this.nickname = result.getString("nickname");
            this.sex = result.getInteger("sex");
            this.city = result.getString("city");
            this.province = result.getString("province");
            this.country = result.getString("country");
            this.headImgUrl = result.getString("headimgurl");
            this.privilege = result.getJSONArray("privilege").toArray(new String[0]);
            this.unionId = result.getString("unionid");
        }
    }

    public String getOpenId() {
        return openId;
    }

    public String getNickname() {
        return nickname;
    }

    public Integer getSex() {
        return sex;
    }

    public String getCity() {
        return city;
    }

    public String getProvince() {
        return province;
    }

    public String getCountry() {
        return country;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public String[] getPrivilege() {
        return privilege;
    }

    public String getUnionId() {
        return unionId;
    }
}
