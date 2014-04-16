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
package net.ymate.platform.module.wechat.message.event;

/**
 * <p>
 * IMassSendJobFinishEvent
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
 *          <td>2014年4月16日下午3:05:11</td>
 *          </tr>
 *          </table>
 */
public interface IMassSendJobFinishEvent {

	/**
	 * 群发的消息ID
	 * 
	 * @return
	 */
	public Long getMsgID();

	public String getToUserName();

	public String getFromUserName();

	public Long getCreateTime();

	/**
	 * 群发的结构，为“send success”或“send fail”或“err(num)”。
	 * 但send success时，也有可能因用户拒收公众号的消息、系统错误等原因造成少量用户接收失败。err(num)是审核失败的具体原因，可能的情况如下：
	 * err(10001), //涉嫌广告 err(20001), //涉嫌政治 err(20004), //涉嫌社会 err(20002),
	 * //涉嫌色情 err(20006), //涉嫌违法犯罪 err(20008), //涉嫌欺诈 err(20013), //涉嫌版权
	 * err(22000), //涉嫌互推(互相宣传) err(21000), //涉嫌其他
	 * 
	 * @return
	 */
	public String getStatus();

	/**
	 * group_id下粉丝数；或者openid_list中的粉丝数
	 * 
	 * @return
	 */
	public Integer getTotalCount();

	/**
	 * 过滤（过滤是指，有些用户在微信设置不接收该公众号的消息）后，准备发送的粉丝数，原则上，FilterCount = SentCount + ErrorCount
	 * 
	 * @return
	 */
	public Integer getFilterCount();

	/**
	 * 发送成功的粉丝数
	 * 
	 * @return
	 */
	public Integer getSendCount();

	/**
	 * 发送失败的粉丝数
	 * 
	 * @return
	 */
	public Integer getErrorCount();

}
