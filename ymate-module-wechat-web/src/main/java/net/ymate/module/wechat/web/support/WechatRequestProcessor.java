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
package net.ymate.module.wechat.web.support;

import net.ymate.platform.webmvc.IRequestProcessor;
import net.ymate.platform.webmvc.IWebMvc;
import net.ymate.platform.webmvc.RequestMeta;
import net.ymate.platform.webmvc.context.WebContext;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 刘镇 (suninformation@163.com) on 16/5/22 上午4:25
 * @version 1.0
 */
public class WechatRequestProcessor implements IRequestProcessor {

    public Map<String, Object> processRequestParams(IWebMvc owner, RequestMeta requestMeta) throws Exception {
        String _content = IOUtils.toString(WebContext.getRequest().getInputStream(), owner.getModuleCfg().getDefaultCharsetEncoding());
        Map<String, Object> _params = new HashMap<String, Object>();
        _params.put("protocol", StringUtils.trimToNull(_content));
        _params.put("accountId", WebContext.getContext().getAttribute("account_id"));
        //
        return _params;
    }
}
