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
package net.ymate.module.wechat.message.out;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.ymate.module.wechat.IWechat;
import net.ymate.module.wechat.message.OutMessage;
import org.apache.commons.lang.NullArgumentException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 刘镇 (suninformation@163.com) on 16/5/23 上午3:01
 * @version 1.0
 */
public class OutNewsMessage extends OutMessage {

    private List<Article> articles = new ArrayList<Article>();

    public OutNewsMessage(String fromUserName, String toUserName, Article article) {
        super(fromUserName, toUserName, IWechat.MessageType.NEWS);
        this.articles.add(article);
    }

    public OutNewsMessage(String fromUserName, String toUserName, List<Article> articles) {
        super(fromUserName, toUserName, IWechat.MessageType.NEWS);
        this.articles.addAll(articles);
    }

    @Override
    protected void __buildXML(StringBuilder builder) {
        if (articles.isEmpty()) {
            throw new NullArgumentException("articles");
        }
        builder.append("<ArticleCount>").append(articles.size()).append("</ArticleCount>\n");
        builder.append("<Articles>\n");
        for (Article _news : articles) {
            _news.toXML(builder);
        }
        builder.append("</Articles>\n");
    }

    @Override
    public JSONObject toJSON() {
        JSONObject _json = super.toJSON();
        _json.put("msgtype", "news");
        JSONObject _news = new JSONObject(true);
        JSONArray _articles = new JSONArray();
        for (Article _item : this.articles) {
            _articles.add(_item.toJSON());
        }
        _news.put("articles", _articles);
        _json.put("news", _news);
        return _json;
    }

    public List<Article> getArticles() {
        return articles;
    }
}
