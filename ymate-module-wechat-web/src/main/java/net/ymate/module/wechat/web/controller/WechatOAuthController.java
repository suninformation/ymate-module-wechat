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
package net.ymate.module.wechat.web.controller;

import net.ymate.framework.core.util.WebUtils;
import net.ymate.framework.webmvc.support.UserSessionBean;
import net.ymate.module.wechat.IWechat;
import net.ymate.module.wechat.Wechat;
import net.ymate.module.wechat.base.WechatAccountMeta;
import net.ymate.module.wechat.base.WechatSnsAccessTokenResult;
import net.ymate.module.wechat.base.WechatSnsUserResult;
import net.ymate.module.wechat.model.WechatUser;
import net.ymate.module.wechat.web.base.WechatUserSession;
import net.ymate.platform.persistence.Fields;
import net.ymate.platform.persistence.jdbc.annotation.Transaction;
import net.ymate.platform.persistence.jdbc.query.IDBLocker;
import net.ymate.platform.webmvc.annotation.Controller;
import net.ymate.platform.webmvc.annotation.PathVariable;
import net.ymate.platform.webmvc.annotation.RequestMapping;
import net.ymate.platform.webmvc.annotation.RequestParam;
import net.ymate.platform.webmvc.context.WebContext;
import net.ymate.platform.webmvc.view.IView;
import net.ymate.platform.webmvc.view.View;
import net.ymate.platform.webmvc.view.impl.HttpStatusView;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;

/**
 * @author 刘镇 (suninformation@163.com) on 16/5/23 上午4:19
 * @version 1.0
 */
@Controller
@RequestMapping("/wechat")
@Transaction
public class WechatOAuthController {

    private String __doBuildOauthScope(String currOauthScope, String needOauthScope) {
        if (StringUtils.isNotBlank(needOauthScope)) {
            if (StringUtils.isBlank(currOauthScope) || StringUtils.equalsIgnoreCase(currOauthScope, needOauthScope)) {
                return needOauthScope;
            } else if (StringUtils.equalsIgnoreCase(currOauthScope, IWechat.OAuthScope.SNSAPI_USERINFO)
                    || StringUtils.equalsIgnoreCase(needOauthScope, IWechat.OAuthScope.SNSAPI_USERINFO)) {
                return IWechat.OAuthScope.SNSAPI_USERINFO;
            }
        }
        return IWechat.OAuthScope.SNSAPI_BASE;
    }

    @Transaction
    private boolean __doRefreshIfNeed(IWechat wechat, WechatAccountMeta accountMeta, WechatUserSession wxSession, String needOauthScope) throws Exception {
        // 从数据库加载微信用户Token数据
        WechatUser _wxUser = WechatUser.builder().id(wxSession.getId()).build()
                .load(Fields.create(
                        WechatUser.FIELDS.OAUTH_ACCESS_TOKEN,
                        WechatUser.FIELDS.OAUTH_EXPIRED_TIME,
                        WechatUser.FIELDS.OAUTH_REFRESH_TOKEN,
                        WechatUser.FIELDS.OAUTH_SCOPE,
                        WechatUser.FIELDS.LAST_MODIFY_TIME), IDBLocker.MYSQL);
        if (_wxUser != null) {
            // 如果数据库中的Token数据比Session会话中的数据新而且数据未过期, 则更新会话数据
            if (_wxUser.getLastModifyTime() != null && wxSession.getLastModifyTime() < _wxUser.getLastModifyTime().intValue()) {
                wxSession.setOauthAccessToken(_wxUser.getOauthAccessToken());
                wxSession.setOauthExpiredTime(_wxUser.getOauthExpiredTime());
                wxSession.setOauthScope(_wxUser.getOauthScope());
            }
            // 重新检查Session会话状态
            if (!wxSession.checkStatus() || !wxSession.checkOauthScope(needOauthScope)) {
                if (StringUtils.isNotBlank(_wxUser.getOauthRefreshToken()) && wxSession.checkOauthScope(needOauthScope)) {
                    // 尝试刷新Token
                    WechatSnsAccessTokenResult _token = wechat.wxOAuthRefreshToken(accountMeta, _wxUser.getOauthRefreshToken());
                    if (_token != null && _token.isOK()) {
                        wxSession.setOauthAccessToken(_wxUser.getOauthAccessToken());
                        wxSession.setOauthExpiredTime(_wxUser.getOauthExpiredTime());
                        wxSession.setOauthScope(_wxUser.getOauthScope());
                        // 存储数据
                        WechatUser.builder().id(wxSession.getId())
                                .oauthAccessToken(_token.getAccessToken())
                                .oauthExpiredTime(_token.getExpiredTime())
                                .oauthRefreshToken(_token.getRefreshToken())
                                .oauthScope(_token.getScope())
                                .unionId(_token.getUnionId())
                                .lastModifyTime(System.currentTimeMillis()).build()
                                .update(Fields.create(
                                        WechatUser.FIELDS.OAUTH_ACCESS_TOKEN,
                                        WechatUser.FIELDS.OAUTH_EXPIRED_TIME,
                                        WechatUser.FIELDS.OAUTH_REFRESH_TOKEN,
                                        WechatUser.FIELDS.OAUTH_SCOPE,
                                        WechatUser.FIELDS.UNION_ID,
                                        WechatUser.FIELDS.LAST_MODIFY_TIME));
                        return true;
                    }
                }
            } else {
                return true;
            }
        }
        return false;
    }

    @RequestMapping("/oauth/{account_id}")
    public IView __doOAuthGET(@PathVariable("account_id") String accountId,
                              @RequestParam(defaultValue = IWechat.OAuthScope.SNSAPI_BASE) String scope,
                              @RequestParam("redirect_uri") String redirectUri) throws Exception {
        WechatAccountMeta _accountMeta = Wechat.get().getAccountById(accountId);
        if (_accountMeta == null || StringUtils.isBlank(redirectUri)) {
            // 关键参数无效则直接404
            return HttpStatusView.NOT_FOUND;
        }
        IView _returnView = null;
        boolean _tokenGetFlag = false;
        String _currScope = null;
        String _state = null;
        // 尝试提取Session会话中的微信用户数据
        UserSessionBean _sessionBean = UserSessionBean.current();
        if (_sessionBean != null) {
            WechatUserSession _wxSession = _sessionBean.getAttribute(WechatUserSession.class.getName());
            if (_wxSession != null) {
                if (!_wxSession.checkStatus()) {
                    _tokenGetFlag = !__doRefreshIfNeed(Wechat.get(), _accountMeta, _wxSession, scope);
                }
                _currScope = _wxSession.getOauthScope();
                _state = _wxSession.getId();
            }
        }
        if (!_tokenGetFlag) {
            // 重新获取Token
            String _redirectUri = _accountMeta.getRedirectUri();
            if (StringUtils.isBlank(_redirectUri)) {
                _redirectUri = WebUtils.buildURL(WebContext.getRequest(), "/wechat/redirect/".concat(accountId), true).concat("?redirect_uri=".concat(redirectUri));
            }
            _returnView = View.redirectView(Wechat.get().wxOAuthGetCodeUrl(_accountMeta, __doBuildOauthScope(_currScope, scope).equals(IWechat.OAuthScope.SNSAPI_BASE), _state, _redirectUri));
        }
        return _returnView == null ? View.redirectView(redirectUri) : _returnView;
    }

    @RequestMapping("/redirect/{account_id}")
    @Transaction
    public IView __doRedirectGET(@PathVariable("account_id") String accountId,
                                 @RequestParam String code,
                                 @RequestParam String state,
                                 @RequestParam("redirect_uri") String redirectUri) throws Exception {
        WechatAccountMeta _accountMeta = Wechat.get().getAccountById(accountId);
        if (_accountMeta != null && StringUtils.isNotBlank(code) && StringUtils.isNotBlank(redirectUri)) {
            WechatSnsAccessTokenResult _token = Wechat.get().wxOAuthGetToken(_accountMeta, code);
            if (_token != null && _token.isOK()) {
                if (StringUtils.isBlank(state)) {
                    state = DigestUtils.md5Hex(_token.getOpenId());
                }
                boolean _needInfo = _token.getScope().equals(IWechat.OAuthScope.SNSAPI_USERINFO);
                long _currentTime = System.currentTimeMillis();
                boolean _isNew = false;
                WechatUser _wxUser = WechatUser.builder().id(state).build().load(Fields.create(WechatUser.FIELDS.ID, WechatUser.FIELDS.OAUTH_SCOPE), IDBLocker.MYSQL);
                Fields _fields = Fields.create();
                if (_wxUser != null) {
                    // 未订阅的用户且当前授权为snsapi_base时才去获取用户资料
                    _needInfo = _needInfo && (_wxUser.getIsSubscribe() != null && _wxUser.getIsSubscribe() != 1) && StringUtils.trimToEmpty(_wxUser.getOauthScope()).equals(IWechat.OAuthScope.SNSAPI_BASE);
                } else {
                    _wxUser = new WechatUser();
                    _wxUser.setId(state);
                    _wxUser.setAccountId(_accountMeta.getAccountId());
                    _wxUser.setOpenId(_token.getOpenId());
                    _wxUser.setSiteId(_accountMeta.getAttribute("site_id"));
                    _fields.add(WechatUser.FIELDS.ID).add(WechatUser.FIELDS.ACCOUNT_ID).add(WechatUser.FIELDS.OPEN_ID).add(WechatUser.FIELDS.SITE_ID);
                    _isNew = true;
                }
                if (_needInfo) {
                    WechatSnsUserResult _snsUser = Wechat.get().wxOAuthGetUserInfo(_token.getAccessToken(), _token.getOpenId());
                    if (_snsUser != null && _snsUser.isOK()) {
                        _wxUser.setNickName(_snsUser.getNickname());
                        _wxUser.setHeadImgUrl(_snsUser.getHeadImgUrl());
                        _wxUser.setGender(_snsUser.getSex());
                        _wxUser.setCountry(_snsUser.getCountry());
                        _wxUser.setProvince(_snsUser.getProvince());
                        _wxUser.setCity(_snsUser.getCity());
                        //
                        _fields.add(Fields.create(
                                WechatUser.FIELDS.NICK_NAME,
                                WechatUser.FIELDS.HEAD_IMG_URL,
                                WechatUser.FIELDS.GENDER,
                                WechatUser.FIELDS.COUNTRY,
                                WechatUser.FIELDS.PROVINCE,
                                WechatUser.FIELDS.CITY));
                    }
                }
                _wxUser.setOauthAccessToken(_token.getAccessToken());
                _wxUser.setOauthRefreshToken(_token.getRefreshToken());
                _wxUser.setOauthExpiredTime(_token.getExpiredTime());
                _wxUser.setOauthScope(_token.getScope());
                _wxUser.setUnionId(_token.getUnionId());
                _wxUser.setLastModifyTime(_currentTime);
                //
                _fields.add(Fields.create(
                        WechatUser.FIELDS.OAUTH_ACCESS_TOKEN,
                        WechatUser.FIELDS.OAUTH_REFRESH_TOKEN,
                        WechatUser.FIELDS.OAUTH_EXPIRED_TIME,
                        WechatUser.FIELDS.OAUTH_SCOPE,
                        WechatUser.FIELDS.UNION_ID,
                        WechatUser.FIELDS.LAST_MODIFY_TIME));
                if (_isNew) {
                    _wxUser.setCreateTime(_currentTime);
                    _wxUser.save(_fields.add(WechatUser.FIELDS.CREATE_TIME));
                } else {
                    _wxUser.update(_fields);
                }
                //
                UserSessionBean _sessionBean = UserSessionBean.createIfNeed();
                WechatUserSession _wxSession = _sessionBean.getAttribute(WechatUserSession.class.getName());
                if (_wxSession == null) {
                    _wxSession = new WechatUserSession();
                }
                _wxSession.setId(state);
                _wxSession.setOpenId(_token.getOpenId());
                _wxSession.setAccountId(accountId);
                _wxSession.setOauthAccessToken(_token.getAccessToken());
                _wxSession.setOauthExpiredTime(_token.getExpiredTime());
                _wxSession.setOauthScope(_token.getScope());
                //
                _sessionBean.addAttribute(WechatUserSession.class.getName(), _wxSession).saveIfNeed();
                //
                return View.redirectView(redirectUri);
            }
        }
        return HttpStatusView.NOT_FOUND;
    }
}
