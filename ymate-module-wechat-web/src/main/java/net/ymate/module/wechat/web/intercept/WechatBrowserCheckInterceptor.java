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
package net.ymate.module.wechat.web.intercept;

import net.ymate.platform.core.beans.intercept.IInterceptor;
import net.ymate.platform.core.beans.intercept.InterceptContext;
import net.ymate.platform.core.lang.BlurObject;
import net.ymate.platform.webmvc.context.WebContext;
import net.ymate.platform.webmvc.view.View;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 检查当前浏览器是否为微信客户端内置浏览器及其版本号, 不符合将回应HTTP状态406
 *
 * @author 刘镇 (suninformation@163.com) on 16/7/8 下午1:56
 * @version 1.0
 */
public class WechatBrowserCheckInterceptor implements IInterceptor {

    public Object intercept(InterceptContext context) throws Exception {
        String _agent = WebContext.getRequest().getHeader("User-Agent");
        if (StringUtils.isNotBlank(_agent)) {
            Pattern _p = Pattern.compile("MicroMessenger/(\\d+).+");
            Matcher _m = _p.matcher(_agent);
            String _version = null;
            if (_m.find()) {
                _version = _m.group(1);
            }
            if (_version == null) {
                return View.httpStatusView(HttpServletResponse.SC_NOT_ACCEPTABLE);
            } else {
                int _needVersion = BlurObject.bind(context.getContextParams().get("wechat_version")).toIntValue();
                if (_needVersion > 0 && NumberUtils.toInt(_version) < _needVersion) {
                    return View.httpStatusView(HttpServletResponse.SC_NOT_ACCEPTABLE);
                }
            }
        }
        return null;
    }
}
