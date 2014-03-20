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

import com.alibaba.fastjson.JSONObject;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * <p>
 * OutMessage
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
 *          <td>2014年3月15日下午2:45:46</td>
 *          </tr>
 *          </table>
 */
public abstract class OutMessage extends AbstractMessage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2030629509962007932L;

	/**
	 * 星标刚收到的消息
	 */
	@XStreamAlias("FuncFlag")
	private int funcFlag = 0;

	/**
	 * 构造器
	 * 
	 * @param toUserName 
	 * @param msgType
	 */
	public OutMessage(String toUserName, String msgType) {
		this.setToUserName(toUserName);
		this.SetMsgType(msgType);
	}

	public int getFuncFlag() {
		return funcFlag;
	}

	public void setFuncFlag(int funcFlag) {
		this.funcFlag = funcFlag;
	}

	public String toJSON() throws Exception {
		JSONObject _json = new JSONObject();
		_json.put("touser", this.getToUserName());
		_json.put("msgtype", this.getMsgType());
		__doSetJsonContent(_json);
		return _json.toString();
	}

	protected abstract void __doSetJsonContent(JSONObject parent) throws Exception;

}
