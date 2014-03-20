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

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * <p>
 * WxOAuthUser
 * </p>
 * <p>
 * 
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
 *          <td>2014年3月20日下午8:27:10</td>
 *          </tr>
 *          </table>
 */
public class WxOAuthUser {

	@JSONField(name = "openid")
	private String openId;

	private String nickname;

	private Integer sex;

	private String city;

	private String province;

	private String country;

	@JSONField(name = "headimgurl")
	private String headImgUrl;

	private List<String> privilege;

	public WxOAuthUser(String openId, String nickname, Integer sex,
			String city, String province, String country, String headImgUrl,
			List<String> privilege) {
		this.openId = openId;
		this.nickname = nickname;
		this.sex = sex;
		this.city = city;
		this.province = province;
		this.country = country;
		this.headImgUrl = headImgUrl;
		this.privilege = privilege;
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

	public List<String> getPrivilege() {
		return privilege;
	}

}
