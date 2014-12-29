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
package net.ymate.platform.module.wechat.support;

import java.io.Writer;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.naming.NameCoder;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;

/**
 * <p>
 * XStreamHelper
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
 *          <td>2014年3月15日上午10:38:26</td>
 *          </tr>
 *          </table>
 */
public class XStreamHelper {

	public static class WeChatXppDriver extends XppDriver {

		public WeChatXppDriver(NameCoder nameCoder) {
			super(nameCoder);
		}

		public HierarchicalStreamWriter createWriter(Writer out) {
			return new PrettyPrintWriter(out, this.getNameCoder()) {
				boolean cdata = false;

				@Override
				@SuppressWarnings("rawtypes")
				public void startNode(String name, Class clazz) {
					super.startNode(name, clazz);
					cdata = String.class.equals(clazz);
				}

				@Override
				public void setValue(String text) {
					super.setValue(text);
				}

				@Override
				protected void writeText(QuickWriter writer, String text) {
					if (cdata) {
						writer.write("<![CDATA[");
						writer.write(text);
						writer.write("]]>");
					} else {
						writer.write(text);
					}
				}
			};
		};
	}

	/**
	 * 初始化XStream可支持String类型字段加入CDATA标签"<![CDATA["和结尾处加上"]]>"， 以供XStream输出时进行识别
	 * 
	 * @param isAddCDATA 是否支持CDATA标签
	 * @return
	 */
	public static XStream createXStream(boolean isAddCDATA) {
		XStream xstream = null;
		if (isAddCDATA) {
			xstream = new XStream(new WeChatXppDriver(new XmlFriendlyNameCoder("_-", "_")));
		} else {
			xstream = new XStream(new XppDriver(new XmlFriendlyNameCoder("_-", "_")));
		}
		return xstream;
	}

}
