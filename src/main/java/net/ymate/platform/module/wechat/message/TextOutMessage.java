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
package net.ymate.platform.module.wechat.message;

import net.ymate.platform.module.wechat.WeChat;

import org.json.JSONObject;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * <p>
 * TextOutMessage
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
 *          <td>2014年3月15日上午2:09:12</td>
 *          </tr>
 *          </table>
 */
@XStreamAlias("xml")
public class TextOutMessage extends OutMessage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3649536452800726206L;

	@XStreamAlias("Content")
	private String content;

	/**
	 * 构造器
	 * 
	 * @param toUserName
	 */
	public TextOutMessage(String toUserName) {
		super(toUserName, WeChat.WX_MESSAGE.TYPE_TEXT);
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	protected void __doSetJsonContent(JSONObject parent) throws Exception {
		JSONObject _text = new JSONObject();
		_text.put("content", this.getContent());
		parent.put("text", _text);
	}

}
