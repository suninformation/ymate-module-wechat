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
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import java.util.*;

/**
 * @author 刘镇 (suninformation@163.com) on 16/5/22 上午4:25
 * @version 1.0
 */
public class WechatRequestProcessor implements IRequestProcessor {

    public static boolean __doCheckSignature(String token, String signature, String timestamp, String nonce) {
        List<String> _params = new ArrayList<String>();
        _params.add(token);
        _params.add(timestamp);
        _params.add(nonce);
        Collections.sort(_params, new Comparator<String>() {
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        return DigestUtils.sha1Hex(_params.get(0) + _params.get(1) + _params.get(2)).equals(signature);
    }

    public Map<String, Object> processRequestParams(IWebMvc owner, RequestMeta requestMeta) throws Exception {
        String _token = WebContext.getContext().getParameterToString("token");
        String _timestamp = WebContext.getContext().getParameterToString("timestamp");
        String _nonce = WebContext.getContext().getParameterToString("nonce");
        String _signature = WebContext.getContext().getParameterToString("signature");
        if (__doCheckSignature(_token, _signature, _timestamp, _nonce)) {

            String _content = IOUtils.toString(WebContext.getRequest().getInputStream(), owner.getModuleCfg().getDefaultCharsetEncoding());
            Map<String, Object> _params = new HashMap<String, Object>();
            _params.put("protocol", StringUtils.trimToNull(_content));
            String _accountId = StringUtils.substringAfterLast(WebContext.getRequestContext().getRequestMapping(), "/");
            if (StringUtils.isNotBlank(WebContext.getRequestContext().getSuffix()) && StringUtils.endsWith(_accountId, WebContext.getRequestContext().getSuffix())) {
                _accountId = StringUtils.substringBefore(_accountId, WebContext.getRequestContext().getSuffix());
            }
            _params.put("accountId", _accountId);
            _params.put("openId", WebContext.getContext().getParameterToString("openid"));
            //
            return _params;
        }
        return Collections.emptyMap();
    }
}
