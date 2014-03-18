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

import java.util.ArrayList;
import java.util.List;

import net.ymate.platform.module.wechat.WeChat;

import org.json.JSONArray;
import org.json.JSONObject;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * <p>
 * NewsOutMessage
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
 *          <td>2014年3月15日下午1:11:15</td>
 *          </tr>
 *          </table>
 */
@XStreamAlias("xml")
public class NewsOutMessage extends OutMessage {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2297334979044913232L;

	@XStreamAlias("ArticleCount")
	private Integer articleCount;

	@XStreamAlias("Articles")
	private List<Article> articles = new ArrayList<Article>();

	/**
	 * 构造器
	 * 
	 * @param toUserName
	 */
	public NewsOutMessage(String toUserName) {
		super(toUserName, WeChat.WX_MESSAGE.TYPE_NEWS);
	}

	public Integer getArticleCount() {
		return articleCount;
	}

	public void setArticleCount(Integer articleCount) {
		this.articleCount = articleCount;
	}

	public List<Article> getArticles() {
		return articles;
	}

	public void setArticles(List<Article> articles) {
		this.articles = articles;
	}

	@Override
	protected void __doSetJsonContent(JSONObject parent) throws Exception {
		JSONObject _news = new JSONObject();
		if (!this.getArticles().isEmpty()) {
			JSONArray _articleArr = new JSONArray();
			for (Article _a : this.getArticles()) {
				JSONObject _article = new JSONObject();
				_article.put("title", _a.getTitle());
				_article.put("description", _a.getDescription());
				_article.put("url", _a.getUrl());
				_article.put("picurl", _a.getPicUrl());
				_articleArr.put(_article);
			}
			_news.put("articles", _articleArr);
		}
		parent.put("news", _news);
	}

}
