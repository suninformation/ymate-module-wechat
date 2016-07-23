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
package net.ymate.module.wechat;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.ymate.framework.commons.HttpClientHelper;
import net.ymate.framework.commons.IFileHandler;
import net.ymate.framework.commons.IHttpResponse;
import net.ymate.framework.commons.ParamUtils;
import net.ymate.module.wechat.base.*;
import net.ymate.module.wechat.impl.DefaultWechatCfg;
import net.ymate.module.wechat.message.OutMessage;
import net.ymate.module.wechat.message.TemplateMessage;
import net.ymate.platform.core.Version;
import net.ymate.platform.core.YMP;
import net.ymate.platform.core.module.IModule;
import net.ymate.platform.core.module.annotation.Module;
import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 刘镇 (suninformation@163.com) on 16/5/22 上午1:50
 * @version 1.0
 */
@Module
public class Wechat implements IModule, IWechat {

    private static final Log _LOG = LogFactory.getLog(Wechat.class);

    public static final Version VERSION = new Version(2, 0, 0, Wechat.class.getPackage().getImplementationVersion(), Version.VersionType.Alphal);

    private static volatile IWechat __instance;

    private YMP __owner;

    private IWechatCfg __moduleCfg;

    private boolean __inited;

    public static IWechat get() {
        if (__instance == null) {
            synchronized (VERSION) {
                if (__instance == null) {
                    __instance = YMP.get().getModule(Wechat.class);
                }
            }
        }
        return __instance;
    }

    public String getName() {
        return IWechat.MODULE_NAME;
    }

    public void init(YMP owner) throws Exception {
        if (!__inited) {
            //
            _LOG.info("Initializing ymatecms-wechat-" + VERSION);
            //
            __owner = owner;
            __moduleCfg = new DefaultWechatCfg(owner);
            //
            __moduleCfg.getAccountProvider().init(this);
            __moduleCfg.getTokenCacheAdapter().init(this);
            if (__moduleCfg.getMessageHandler() != null) {
                __moduleCfg.getMessageHandler().init(this);
            }
            if (__moduleCfg.getAutoreplyHandler() != null) {
                __moduleCfg.getAutoreplyHandler().init(this);
            }
            //
            __inited = true;
        }
    }

    public boolean isInited() {
        return __inited;
    }

    public void destroy() throws Exception {
        if (__inited) {
            __inited = false;
            //
            __moduleCfg = null;
            __owner = null;
        }
    }

    public YMP getOwner() {
        return __owner;
    }

    public IWechatCfg getModuleCfg() {
        return __moduleCfg;
    }

    public WechatAccountMeta getAccountByToken(String token) {
        return __moduleCfg.getAccountProvider().getAccountMetaByToken(token);
    }

    public WechatAccountMeta getAccountById(String accountId) {
        return __moduleCfg.getAccountProvider().getAccountMetaByAccountId(accountId);
    }

    // ----------

    public String wxGetAccessToken(WechatAccountMeta accountMeta) {
        WechatAccessToken _token = __moduleCfg.getTokenCacheAdapter().getAccessToken(accountMeta);
        return _token == null ? null : _token.getToken();
    }

    public String wxGetJsTicket(WechatAccountMeta accountMeta) {
        WechatTicket _ticket = __moduleCfg.getTokenCacheAdapter().getJsTicket(accountMeta);
        return _ticket == null ? null : _ticket.getTicket();
    }

    public String[] wxGetCallbackIp(WechatAccountMeta accountMeta) {
        try {
            IHttpResponse _response = HttpClientHelper.create().get(WX_API.WX_GET_CALLBACK_IP.concat(wxGetAccessToken(accountMeta)));
            WechatResult _result = new WechatResult(JSON.parseObject(_response.getContent()));
            if (_result.getErrCode() == 0) {
                JSONArray _array = _result.getOriginalResult().getJSONArray("ip_list");
                if (_array != null && !_array.isEmpty()) {
                    return _array.toArray(new String[_array.size()]);
                }
            }
        } catch (Exception e) {
            _LOG.warn("", e);
        }
        return null;
    }

    // ----------

    public WechatQRCodeResult wxCreateQRCode(WechatAccountMeta accountMeta, int sceneId, int expireSeconds) {
        JSONObject _paramJSON = new JSONObject();
        if (expireSeconds >= 0) {
            _paramJSON.put("action_name", "QR_SCENE");
            _paramJSON.put("expire_seconds", expireSeconds);
        } else {
            _paramJSON.put("action_name", "QR_LIMIT_SCENE");
        }
        JSONObject _sceneJSON = new JSONObject();
        _sceneJSON.put("scene_id", sceneId);
        JSONObject _infoJSON = new JSONObject();
        _infoJSON.put("scene", _sceneJSON);
        _paramJSON.put("action_info", _infoJSON);
        try {
            IHttpResponse _response = HttpClientHelper.create().post(WX_API.QRCODE_CREATE.concat(wxGetAccessToken(accountMeta)), _paramJSON.toString());
            return new WechatQRCodeResult(JSON.parseObject(_response.getContent()));
        } catch (Exception e) {
            _LOG.warn("", e);
        }
        return null;
    }

    public WechatQRCodeResult wxCreateQRCodeLimit(WechatAccountMeta accountMeta, int sceneId) {
        return wxCreateQRCode(accountMeta, sceneId, -1);
    }

    public WechatQRCodeResult wxCreateQRCodeLimitStr(WechatAccountMeta accountMeta, String sceneStr) {
        JSONObject _paramJSON = new JSONObject();
        _paramJSON.put("action_name", "QR_LIMIT_STR_SCENE");
        JSONObject _sceneJSON = new JSONObject();
        _sceneJSON.put("scene_str", sceneStr);
        JSONObject _infoJSON = new JSONObject();
        _infoJSON.put("scene", _sceneJSON);
        _paramJSON.put("action_info", _infoJSON);
        try {
            IHttpResponse _response = HttpClientHelper.create().post(WX_API.QRCODE_CREATE.concat(wxGetAccessToken(accountMeta)), _paramJSON.toString());
            return new WechatQRCodeResult(JSON.parseObject(_response.getContent()));
        } catch (Exception e) {
            _LOG.warn("", e);
        }
        return null;
    }

    public String wxShowQRCode(String ticket) {
        try {
            return WX_API.QRCODE_SHOW.concat(URLEncoder.encode(ticket, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            _LOG.warn("", e);
        }
        return null;
    }

    public WechatShortUrlResult wxShortUrl(WechatAccountMeta accountMeta, String longUrl) {
        JSONObject _paramJSON = new JSONObject();
        _paramJSON.put("action", "long2short");
        _paramJSON.put("long_url", longUrl);
        try {
            IHttpResponse _response = HttpClientHelper.create().post(WX_API.SHORT_URL.concat(wxGetAccessToken(accountMeta)), _paramJSON.toString());
            return new WechatShortUrlResult(JSON.parseObject(_response.getContent()));
        } catch (Exception e) {
            _LOG.warn("", e);
        }
        return null;
    }

    public WechatAutoreplyResult wxAutoreplyGetInfo(WechatAccountMeta accountMeta) {
        try {
            IHttpResponse _response = HttpClientHelper.create().get(WX_API.AUTOREPLY_GET_INFO.concat(wxGetAccessToken(accountMeta)));
            return new WechatAutoreplyResult(JSON.parseObject(_response.getContent()));
        } catch (Exception e) {
            _LOG.warn("", e);
        }
        return null;
    }

    // ----------

    public String wxOAuthGetCodeUrl(WechatAccountMeta accountMeta, boolean baseScope, String state, String redirectUri) {
        if (StringUtils.isBlank(redirectUri)) {
            throw new NullArgumentException("redirectUri");
        }
        try {
            Map<String, Object> _params = new HashMap<String, Object>();
            _params.put("appid", accountMeta.getAppId());
            _params.put("response_type", "code");
            _params.put("redirect_uri", URLEncoder.encode(redirectUri, HttpClientHelper.DEFAULT_CHARSET));
            _params.put("scope", baseScope ? "snsapi_base" : "snsapi_userinfo");
            _params.put("state", StringUtils.defaultIfEmpty(state, "") + "#wechat_redirect");
            return WX_API.OAUTH_GET_CODE.concat(ParamUtils.buildQueryParamStr(_params, false, HttpClientHelper.DEFAULT_CHARSET));
        } catch (Exception e) {
            _LOG.warn("", e);
        }
        return null;
    }

    public WechatSnsAccessTokenResult wxOAuthGetToken(WechatAccountMeta accountMeta, String code) {
        if (StringUtils.isBlank(code)) {
            throw new NullArgumentException("code");
        }
        Map<String, String> _params = new HashMap<String, String>();
        _params.put("appid", accountMeta.getAppId());
        _params.put("secret", accountMeta.getAppSecret());
        _params.put("code", code);
        _params.put("grant_type", "authorization_code");
        try {
            IHttpResponse _response = HttpClientHelper.create().get(WX_API.OAUTH_ACCESS_TOKEN, _params);
            return new WechatSnsAccessTokenResult(JSON.parseObject(_response.getContent()));
        } catch (Exception e) {
            _LOG.warn("", e);
        }
        return null;
    }

    public WechatSnsAccessTokenResult wxOAuthRefreshToken(WechatAccountMeta accountMeta, String refreshToken) {
        if (StringUtils.isBlank(refreshToken)) {
            throw new NullArgumentException("refreshToken");
        }
        Map<String, String> _params = new HashMap<String, String>();
        _params.put("appid", accountMeta.getAppId());
        _params.put("grant_type", "refresh_token");
        _params.put("refresh_token", refreshToken);
        try {
            IHttpResponse _response = HttpClientHelper.create().get(WX_API.OAUTH_REFRESH_TOKEN, _params);
            return new WechatSnsAccessTokenResult(JSON.parseObject(_response.getContent()));
        } catch (Exception e) {
            _LOG.warn("", e);
        }
        return null;
    }

    public WechatSnsUserResult wxOAuthGetUserInfo(String oauthAccessToken, String openid) {
        return wxOAuthGetUserInfo(oauthAccessToken, openid, WxLangType.zh_CN);
    }

    public WechatSnsUserResult wxOAuthGetUserInfo(String oauthAccessToken, String openid, WxLangType lang) {
        if (StringUtils.isBlank(oauthAccessToken)) {
            throw new NullArgumentException("oauthAccessToken");
        }
        if (StringUtils.isBlank(openid)) {
            throw new NullArgumentException("openid");
        }
        Map<String, String> _params = new HashMap<String, String>();
        _params.put("openid", openid);
        _params.put("lang", lang == null ? WxLangType.zh_CN.toString() : lang.toString());
        try {
            IHttpResponse _response = HttpClientHelper.create().get(WX_API.OAUTH_USER_INFO.concat(oauthAccessToken), _params);
            return new WechatSnsUserResult(JSON.parseObject(_response.getContent()));
        } catch (Exception e) {
            _LOG.warn("", e);
        }
        return null;
    }

    public WechatResult wxOAuthAuthAccessToken(String oauthAccessToken, String openid) {
        if (StringUtils.isBlank(oauthAccessToken)) {
            throw new NullArgumentException("oauthAccessToken");
        }
        if (StringUtils.isBlank(openid)) {
            throw new NullArgumentException("openid");
        }
        Map<String, String> _params = new HashMap<String, String>();
        _params.put("openid", openid);
        try {
            IHttpResponse _response = HttpClientHelper.create().get(WX_API.OAUTH_AUTH_ACCESS_TOKEN.concat(oauthAccessToken), _params);
            return new WechatResult(JSON.parseObject(_response.getContent()));
        } catch (Exception e) {
            _LOG.warn("", e);
        }
        return null;
    }

    // ----------

    public WechatUserListResult wxUserGetList(WechatAccountMeta accountMeta, String nextOpenId) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("access_token", wxGetAccessToken(accountMeta));
        if (StringUtils.isNotBlank(nextOpenId)) {
            params.put("next_openid", nextOpenId);
        }
        try {
            IHttpResponse _response = HttpClientHelper.create().get(WX_API.USER_GET, params);
            return new WechatUserListResult(JSON.parseObject(_response.getContent()));
        } catch (Exception e) {
            _LOG.warn("", e);
        }
        return null;
    }

    public WechatUserResult wxUserGetInfo(WechatAccountMeta accountMeta, String openid) {
        return wxUserGetInfo(accountMeta, openid, WxLangType.zh_CN);
    }

    public WechatUserResult wxUserGetInfo(WechatAccountMeta accountMeta, String openid, WxLangType lang) {
        if (StringUtils.isBlank(openid)) {
            throw new NullArgumentException("openid");
        }
        Map<String, String> _params = new HashMap<String, String>();
        _params.put("access_token", wxGetAccessToken(accountMeta));
        _params.put("openid", openid);
        _params.put("lang", lang == null ? WxLangType.zh_CN.toString() : lang.toString());
        try {
            IHttpResponse _response = HttpClientHelper.create().get(WX_API.USER_INFO, _params);
            return new WechatUserResult(JSON.parseObject(_response.getContent()));
        } catch (Exception e) {
            _LOG.warn("", e);
        }
        return null;
    }

    public WechatUserResult wxUserGetInfoBatch(WechatAccountMeta accountMeta, List<String> openids) {
        return wxUserGetInfoBatch(accountMeta, openids, WxLangType.zh_CN);
    }

    public WechatUserResult wxUserGetInfoBatch(WechatAccountMeta accountMeta, List<String> openids, WxLangType lang) {
        if (openids == null || openids.isEmpty()) {
            throw new NullArgumentException("openids");
        }
        String _lang = (lang == null ? WxLangType.zh_CN.toString() : lang.toString());
        JSONArray _userList = new JSONArray();
        for (String openid : openids) {
            JSONObject _item = new JSONObject();
            _item.put("openid", openid);
            _item.put("lang", _lang);
            _userList.add(_item);
        }
        JSONObject _paramJSON = new JSONObject();
        _paramJSON.put("user_list", _userList);
        try {
            IHttpResponse _response = HttpClientHelper.create().post(WX_API.USER_INFO_BATCH.concat(wxGetAccessToken(accountMeta)), _paramJSON.toString());
            return new WechatUserResult(JSON.parseObject(_response.getContent()));
        } catch (Exception e) {
            _LOG.warn("", e);
        }
        return null;
    }

    public WechatResult wxUserUpdateRemark(WechatAccountMeta accountMeta, String openid, String remark) {
        if (StringUtils.isBlank(openid)) {
            throw new NullArgumentException("openid");
        }
        JSONObject _paramJSON = new JSONObject();
        _paramJSON.put("openid", openid);
        _paramJSON.put("remark", StringUtils.trimToEmpty(remark));
        try {
            IHttpResponse _response = HttpClientHelper.create().post(WX_API.USER_UPDATE_REMARK.concat(wxGetAccessToken(accountMeta)), _paramJSON.toString());
            return new WechatResult(JSON.parseObject(_response.getContent()));
        } catch (Exception e) {
            _LOG.warn("", e);
        }
        return null;
    }

    public WechatUserTagsResult wxUserGetTags(WechatAccountMeta accountMeta, String openid) {
        if (StringUtils.isBlank(openid)) {
            throw new NullArgumentException("openid");
        }
        JSONObject _openidJSON = new JSONObject();
        _openidJSON.put("openid", openid);
        try {
            IHttpResponse _response = HttpClientHelper.create().post(WX_API.TAGS_GET_ID.concat(wxGetAccessToken(accountMeta)), _openidJSON.toString());
            return new WechatUserTagsResult(JSON.parseObject(_response.getContent()));
        } catch (Exception e) {
            _LOG.warn("", e);
        }
        return null;
    }

    // ----------

    public WechatTagResult wxTagsCreate(WechatAccountMeta accountMeta, String tagName) {
        if (StringUtils.isBlank(tagName)) {
            throw new NullArgumentException("tagName");
        }
        JSONObject _paramJSON = new JSONObject();
        JSONObject _item = new JSONObject();
        _item.put("name", tagName);
        _paramJSON.put("tag", _item);
        try {
            IHttpResponse _response = HttpClientHelper.create().post(WX_API.TAGS_CREATE.concat(wxGetAccessToken(accountMeta)), _paramJSON.toString());
            return new WechatTagResult(JSON.parseObject(_response.getContent()));
        } catch (Exception e) {
            _LOG.warn("", e);
        }
        return null;
    }

    public WechatTagResult[] wxTagsGetList(WechatAccountMeta accountMeta) {
        try {
            IHttpResponse _response = HttpClientHelper.create().get(WX_API.TAGS_GET.concat(wxGetAccessToken(accountMeta)));
            WechatResult _result = new WechatResult(JSON.parseObject(_response.getContent()));
            if (_result.getErrCode() == 0) {
                List<WechatTagResult> _results = new ArrayList<WechatTagResult>();
                JSONArray _tags = _result.getOriginalResult().getJSONArray("tags");
                for (Object _item : _tags) {
                    JSONObject _tag = (JSONObject) _item;
                    _results.add(new WechatTagResult(_tag.getIntValue("id"), _tag.getString("name"), _tag.getLongValue("count")));
                }
                if (!_results.isEmpty()) {
                    return _results.toArray(new WechatTagResult[0]);
                }
            }
        } catch (Exception e) {
            _LOG.warn("", e);
        }
        return null;
    }

    public WechatResult wxTagsUpdate(WechatAccountMeta accountMeta, int tagId, String tagName) {
        if (StringUtils.isBlank(tagName)) {
            throw new NullArgumentException("tagName");
        }
        JSONObject _paramJSON = new JSONObject();
        JSONObject _item = new JSONObject();
        _item.put("id", tagId);
        _item.put("name", tagName);
        _paramJSON.put("tag", _item);
        try {
            IHttpResponse _response = HttpClientHelper.create().post(WX_API.TAGS_UPDATE.concat(wxGetAccessToken(accountMeta)), _paramJSON.toString());
            return new WechatResult(JSON.parseObject(_response.getContent()));
        } catch (Exception e) {
            _LOG.warn("", e);
        }
        return null;
    }

    public WechatResult wxTagsDelete(WechatAccountMeta accountMeta, int tagId) {
        JSONObject _paramJSON = new JSONObject();
        JSONObject _item = new JSONObject();
        _item.put("id", tagId);
        _paramJSON.put("tag", _item);
        try {
            IHttpResponse _response = HttpClientHelper.create().post(WX_API.TAGS_DELETE.concat(wxGetAccessToken(accountMeta)), _paramJSON.toString());
            return new WechatResult(JSON.parseObject(_response.getContent()));
        } catch (Exception e) {
            _LOG.warn("", e);
        }
        return null;
    }

    public WechatUserListResult wxTagsGetUserList(WechatAccountMeta accountMeta, int tagId, String nextOpenId) {
        JSONObject _paramJSON = new JSONObject();
        _paramJSON.put("tagid", tagId);
        if (StringUtils.isNotBlank(nextOpenId)) {
            _paramJSON.put("next_openid", nextOpenId);
        }
        try {
            IHttpResponse _response = HttpClientHelper.create().post(WX_API.TAGS_GET_USER.concat(wxGetAccessToken(accountMeta)), _paramJSON.toString());
            return new WechatUserListResult(JSON.parseObject(_response.getContent()));
        } catch (Exception e) {
            _LOG.warn("", e);
        }
        return null;
    }

    private WechatResult __doTagsBatchOpt(WechatAccountMeta accountMeta, int tagId, List<String> openids, String apiUrl) {
        if (openids == null || openids.isEmpty()) {
            throw new NullArgumentException("openids");
        }
        JSONObject _paramJSON = new JSONObject();
        _paramJSON.put("tagid", tagId);
        JSONArray _item = new JSONArray();
        _item.addAll(openids);
        _paramJSON.put("openid_list", _item);
        try {
            IHttpResponse _response = HttpClientHelper.create().post(apiUrl.concat(wxGetAccessToken(accountMeta)), _paramJSON.toString());
            return new WechatResult(JSON.parseObject(_response.getContent()));
        } catch (Exception e) {
            _LOG.warn("", e);
        }
        return null;
    }

    public WechatResult wxTagsUpdateUser(WechatAccountMeta accountMeta, int tagId, List<String> openids) {
        return __doTagsBatchOpt(accountMeta, tagId, openids, WX_API.TAGS_UPDATE_USER);
    }

    public WechatResult wxTagsDeleteUser(WechatAccountMeta accountMeta, int tagId, List<String> openids) {
        return __doTagsBatchOpt(accountMeta, tagId, openids, WX_API.TAGS_DELETE_USER);
    }

    // ----------

    public WechatTagResult wxGroupCreate(WechatAccountMeta accountMeta, String groupName) {
        JSONObject _groupJSON = new JSONObject();
        JSONObject _nameJSON = new JSONObject();
        _nameJSON.put("name", groupName);
        _groupJSON.put("group", _nameJSON);
        try {
            IHttpResponse _response = HttpClientHelper.create().post(WX_API.GROUP_CREATE.concat(wxGetAccessToken(accountMeta)), _groupJSON.toString());
            return new WechatTagResult(JSON.parseObject(_response.getContent()));
        } catch (Exception e) {
            _LOG.warn("", e);
        }
        return null;
    }

    public WechatTagResult[] wxGroupGetList(WechatAccountMeta accountMeta) {
        try {
            IHttpResponse _response = HttpClientHelper.create().get(WX_API.GROUP_GET.concat(wxGetAccessToken(accountMeta)));
            WechatResult _result = new WechatResult(JSON.parseObject(_response.getContent()));
            if (_result.getErrCode() == 0) {
                List<WechatTagResult> _results = new ArrayList<WechatTagResult>();
                JSONArray _tags = _result.getOriginalResult().getJSONArray("groups");
                for (Object _item : _tags) {
                    JSONObject _tag = (JSONObject) _item;
                    _results.add(new WechatTagResult(_tag.getIntValue("id"), _tag.getString("name"), _tag.getLongValue("count")));
                }
                if (!_results.isEmpty()) {
                    return _results.toArray(new WechatTagResult[0]);
                }
            }
        } catch (Exception e) {
            _LOG.warn("", e);
        }
        return null;
    }

    public WechatUserTagsResult wxGroupGetId(WechatAccountMeta accountMeta, String openid) {
        JSONObject _openidJSON = new JSONObject();
        _openidJSON.put("openid", openid);
        try {
            IHttpResponse _response = HttpClientHelper.create().post(WX_API.GROUP_GET_ID.concat(wxGetAccessToken(accountMeta)), _openidJSON.toString());
            return new WechatUserTagsResult(JSON.parseObject(_response.getContent()));
        } catch (Exception e) {
            _LOG.warn("", e);
        }
        return null;
    }

    public WechatResult wxGroupUpdate(WechatAccountMeta accountMeta, String groupId, String groupName) {
        JSONObject _groupJSON = new JSONObject();
        _groupJSON.put("id", groupId);
        _groupJSON.put("name", groupName);
        JSONObject _paramJSON = new JSONObject();
        _paramJSON.put("group", _groupJSON);
        try {
            IHttpResponse _response = HttpClientHelper.create().post(WX_API.GROUP_UPDATE.concat(wxGetAccessToken(accountMeta)), _paramJSON.toString());
            return new WechatResult(JSON.parseObject(_response.getContent()));
        } catch (Exception e) {
            _LOG.warn("", e);
        }
        return null;
    }

    public WechatResult wxGroupsDelete(WechatAccountMeta accountMeta, int groupId) {
        JSONObject _paramJSON = new JSONObject();
        JSONObject _item = new JSONObject();
        _item.put("id", groupId);
        _paramJSON.put("group", _item);
        try {
            IHttpResponse _response = HttpClientHelper.create().post(WX_API.GROUP_DELETE.concat(wxGetAccessToken(accountMeta)), _paramJSON.toString());
            return new WechatResult(JSON.parseObject(_response.getContent()));
        } catch (Exception e) {
            _LOG.warn("", e);
        }
        return null;
    }

    public WechatResult wxGroupMembersUpdate(WechatAccountMeta accountMeta, String openid, String toGroupid) {
        JSONObject _paramJSON = new JSONObject();
        _paramJSON.put("openid", openid);
        _paramJSON.put("to_groupid", toGroupid);
        try {
            IHttpResponse _response = HttpClientHelper.create().post(WX_API.GROUP_MEMBERS_UPDATE.concat(wxGetAccessToken(accountMeta)), _paramJSON.toString());
            return new WechatResult(JSON.parseObject(_response.getContent()));
        } catch (Exception e) {
            _LOG.warn("", e);
        }
        return null;
    }

    public WechatResult wxGroupMembersUpdateBatch(WechatAccountMeta accountMeta, List<String> openids, String toGroupid) {
        if (openids == null || openids.isEmpty()) {
            throw new NullArgumentException("openids");
        }
        if (StringUtils.isBlank(toGroupid)) {
            throw new NullArgumentException("toGroupid");
        }
        JSONObject _paramJSON = new JSONObject();
        _paramJSON.put("to_groupid", toGroupid);
        JSONArray _item = new JSONArray();
        _item.addAll(openids);
        _paramJSON.put("openid_list", _item);
        try {
            IHttpResponse _response = HttpClientHelper.create().post(WX_API.GROUP_MEMBERS_UPDATE_BATCH.concat(wxGetAccessToken(accountMeta)), _paramJSON.toString());
            return new WechatResult(JSON.parseObject(_response.getContent()));
        } catch (Exception e) {
            _LOG.warn("", e);
        }
        return null;
    }

    // ----------

    public WechatResult wxMenuCreate(WechatAccountMeta accountMeta, WechatMenu menu) {
        try {
            IHttpResponse _response = HttpClientHelper.create().post(WX_API.MENU_CREATE.concat(wxGetAccessToken(accountMeta)), JSON.toJSONString(menu));
            return new WechatResult(JSON.parseObject(_response.getContent()));
        } catch (Exception e) {
            _LOG.warn("", e);
        }
        return null;
    }

    public WechatMenu wxMenuGet(WechatAccountMeta accountMeta) {
        try {
            IHttpResponse _response = HttpClientHelper.create().get(WX_API.MENU_GET.concat(wxGetAccessToken(accountMeta)));
            WechatResult _result = new WechatResult(JSON.parseObject(_response.getContent()));
            if (_result.getErrCode() == 0) {
                return JSON.toJavaObject(_result.getOriginalResult().getJSONObject("menu"), WechatMenu.class);
            }
        } catch (Exception e) {
            _LOG.warn("", e);
        }
        return null;
    }

    public WechatResult wxMenuDelete(WechatAccountMeta accountMeta) {
        try {
            IHttpResponse _response = HttpClientHelper.create().get(WX_API.MENU_DELETE.concat(wxGetAccessToken(accountMeta)));
            return new WechatResult(JSON.parseObject(_response.getContent()));
        } catch (Exception e) {
            _LOG.warn("", e);
        }
        return null;
    }

    // ----------

    public void wxMediaGetFile(WechatAccountMeta accountMeta, String mediaId, IFileHandler fileHandler) {
        try {
            HttpClientHelper.create().download(WX_API.MEDIA_GET.concat(wxGetAccessToken(accountMeta)).concat("&media_id=").concat(mediaId), fileHandler);
        } catch (Exception e) {
            _LOG.warn("", e);
        }
    }

    public WechatMediaUploadResult wxMediaUploadFile(WechatAccountMeta accountMeta, MediaType type, File file) {
        return wxMediaUploadFile(accountMeta, type, new FileBody(file));
    }

    public WechatMediaUploadResult wxMediaUploadFile(WechatAccountMeta accountMeta, MediaType type, ContentBody file) {
        if (MediaType.NEWS.equals(type) || MediaType.SHORT_VIDEO.equals(type)) {
            throw new UnsupportedOperationException("Unsupport News and Short Video");
        }
        try {
            IHttpResponse _response = HttpClientHelper.create().upload(WX_API.MEDIA_UPLOAD.concat(wxGetAccessToken(accountMeta)).concat("&type=").concat(type.toString().toLowerCase()), "media", file, null);
            return new WechatMediaUploadResult(type, JSON.parseObject(_response.getContent()));
        } catch (Exception e) {
            _LOG.warn("", e);
        }
        return null;
    }

    public WechatMediaUploadResult wxMediaUploadImage(WechatAccountMeta accountMeta, File file) {
        return wxMediaUploadImage(accountMeta, new FileBody(file));
    }

    public WechatMediaUploadResult wxMediaUploadImage(WechatAccountMeta accountMeta, ContentBody file) {
        try {
            IHttpResponse _response = HttpClientHelper.create().upload(WX_API.MEDIA_UPLOAD_IMAGE.concat(wxGetAccessToken(accountMeta)), "media", file, null);
            return new WechatMediaUploadResult(MediaType.IMAGE, JSON.parseObject(_response.getContent()));
        } catch (Exception e) {
            _LOG.warn("", e);
        }
        return null;
    }

    public WechatMediaUploadResult wxMediaUploadNews(WechatAccountMeta accountMeta, List<WechatMassArticle> articles) {
        if (articles == null || articles.isEmpty()) {
            throw new NullArgumentException("articles");
        }
        JSONObject _json = new JSONObject();
        JSONArray _articleJSON = new JSONArray();
        for (WechatMassArticle article : articles) {
            _articleJSON.add(article.toJSON());
        }
        _json.put("articles", _articleJSON);
        try {
            IHttpResponse _response = HttpClientHelper.create().post(WX_API.MEDIA_UPLOAD_NEWS.concat(wxGetAccessToken(accountMeta)), _json.toString());
            return new WechatMediaUploadResult(MediaType.NEWS, JSON.parseObject(_response.getContent()));
        } catch (Exception e) {
            _LOG.warn("", e);
        }
        return null;
    }

    public WechatMediaUploadResult wxMediaUploadVideo(WechatAccountMeta accountMeta, WechatMassVideo video) {
        try {
            IHttpResponse _response = HttpClientHelper.create().post(WX_API.MEDIA_UPLOAD_VIDEO + wxGetAccessToken(accountMeta), video.toJSON());
            return new WechatMediaUploadResult(MediaType.VIDEO, JSON.parseObject(_response.getContent()));
        } catch (Exception e) {
            _LOG.warn("", e);
        }
        return null;
    }

    public WechatResult wxMessageCustomSend(WechatAccountMeta accountMeta, OutMessage message) {
        try {
            IHttpResponse _response = HttpClientHelper.create().post(WX_API.MESSAGE_CUSTOM_SEND.concat(wxGetAccessToken(accountMeta)), message.toJSON().toJSONString());
            return new WechatResult(JSON.parseObject(_response.getContent()));
        } catch (Exception e) {
            _LOG.warn("", e);
        }
        return null;
    }

    public WechatMsgSendResult wxMessageTemplateSend(WechatAccountMeta accountMeta, TemplateMessage message) {
        try {
            IHttpResponse _response = HttpClientHelper.create().post(WX_API.MESSAGE_TEMPLATE_SEND.concat(wxGetAccessToken(accountMeta)), message.toJSON().toJSONString());
            return new WechatMsgSendResult(JSON.parseObject(_response.getContent()));
        } catch (Exception e) {
            _LOG.warn("", e);
        }
        return null;
    }

    // --------

    private WechatMsgSendResult __doWxMassSend(JSONObject json, String toURL, MessageType msgType, String mediaIdOrContent) {
        //
        String _msgType = "mpnews";
        String _bodyAttr = "media_id";
        //
        switch (msgType) {
            case NEWS:
                break;
            case TEXT:
                _msgType = "text";
                _bodyAttr = "content";
                break;
            case VOICE:
                _msgType = "voice";
                break;
            case IMAGE:
                _msgType = "image";
                break;
            case VIDEO:
                _msgType = "mpvideo";
                break;
            default:
                throw new UnsupportedOperationException("Unsupport MessageType \"" + msgType + "\".");
        }
        //
        JSONObject _bodyJSON = new JSONObject();
        _bodyJSON.put(_bodyAttr, mediaIdOrContent);
        json.put(_msgType, _bodyJSON);
        //
        json.put("msgtype", _msgType);
        //
        try {
            IHttpResponse _response = HttpClientHelper.create().post(toURL, json.toJSONString());
            return new WechatMsgSendResult(JSON.parseObject(_response.getContent()));
        } catch (Exception e) {
            _LOG.warn("", e);
        }
        return null;
    }

    public WechatMsgSendResult wxMassSendAll(WechatAccountMeta accountMeta, String groupOrTagId, boolean isTagId, boolean isToAll, MessageType msgType, String mediaIdOrContent) {
        JSONObject _json = new JSONObject();
        //
        JSONObject _filterJSON = new JSONObject();
        _filterJSON.put("is_to_all", isToAll);
        _filterJSON.put(isTagId ? "tag_id" : "group_id", groupOrTagId);
        _json.put("filter", _filterJSON);
        //
        return __doWxMassSend(_json, WX_API.MASS_SEND_ALL.concat(wxGetAccessToken(accountMeta)), msgType, mediaIdOrContent);
    }

    public WechatMsgSendResult wxMassSend(WechatAccountMeta accountMeta, List<String> openIds, MessageType msgType, String mediaIdOrContent) {
        if (openIds == null || openIds.isEmpty()) {
            throw new NullArgumentException("openIds");
        }
        JSONObject _json = new JSONObject();
        _json.put("touser", openIds);
        //
        return __doWxMassSend(_json, WX_API.MASS_SEND_BY_OPENID.concat(wxGetAccessToken(accountMeta)), msgType, mediaIdOrContent);
    }

    public WechatMsgSendResult wxMassPreview(WechatAccountMeta accountMeta, String openId, MessageType msgType, String mediaIdOrContent) {
        if (StringUtils.isBlank(openId)) {
            throw new NullArgumentException("openId");
        }
        JSONObject _json = new JSONObject();
        _json.put("touser", openId);
        //
        return __doWxMassSend(_json, WX_API.MASS_PREVIEW.concat(wxGetAccessToken(accountMeta)), msgType, mediaIdOrContent);
    }

    public WechatResult wxMassDelete(WechatAccountMeta accountMeta, String msgId) {
        try {
            IHttpResponse _response = HttpClientHelper.create().post(WX_API.MASS_DELETE.concat(wxGetAccessToken(accountMeta)), "{\"msg_id\":" + msgId + "}");
            return new WechatResult(JSON.parseObject(_response.getContent()));
        } catch (Exception e) {
            _LOG.warn("", e);
        }
        return null;
    }

    public WechatMsgSendResult wxMassGet(WechatAccountMeta accountMeta, String msgId) {
        try {
            IHttpResponse _response = HttpClientHelper.create().post(WX_API.MASS_GET.concat(wxGetAccessToken(accountMeta)), "{\"msg_id\":\"" + msgId + "\"}");
            return new WechatMsgSendResult(JSON.parseObject(_response.getContent()));
        } catch (Exception e) {
            _LOG.warn("", e);
        }
        return null;
    }

    // ------

    public WechatMediaUploadResult wxMaterialAddNews(WechatAccountMeta accountMeta, List<WechatMassArticle> articles) {
        if (articles == null || articles.isEmpty()) {
            throw new NullArgumentException("articles");
        }
        JSONObject _json = new JSONObject();
        JSONArray _articleJSON = new JSONArray();
        for (WechatMassArticle article : articles) {
            _articleJSON.add(article.toJSON());
        }
        _json.put("articles", _articleJSON);
        try {
            IHttpResponse _response = HttpClientHelper.create().post(WX_API.MATERIAL_ADD_NEWS.concat(wxGetAccessToken(accountMeta)), _json.toString());
            return new WechatMediaUploadResult(MediaType.NEWS, JSON.parseObject(_response.getContent()));
        } catch (Exception e) {
            _LOG.warn("", e);
        }
        return null;
    }

    public WechatMediaUploadResult wxMaterialAddNews(WechatAccountMeta accountMeta, MediaType type, File file) {
        return wxMaterialAddNews(accountMeta, type, new FileBody(file));
    }

    public WechatMediaUploadResult wxMaterialAddNews(WechatAccountMeta accountMeta, MediaType type, ContentBody file) {
        if (MediaType.NEWS.equals(type)) {
            throw new UnsupportedOperationException("News media type need use wxMediaUploadNews method.");
        } else if (MediaType.VIDEO.equals(type) || MediaType.SHORT_VIDEO.equals(type)) {
            throw new UnsupportedOperationException("Unsupport Video and Short Video");
        }
        try {
            IHttpResponse _response = HttpClientHelper.create().upload(WX_API.MATERIAL_ADD_MATERIAL.concat(wxGetAccessToken(accountMeta)).concat("&type=").concat(type.toString().toLowerCase()), "media", file, null);
            return new WechatMediaUploadResult(MediaType.NEWS, JSON.parseObject(_response.getContent()));
        } catch (Exception e) {
            _LOG.warn("", e);
        }
        return null;
    }

    public WechatMediaUploadResult wxMaterialAddVideo(WechatAccountMeta accountMeta, String title, String introduction, File file) {
        return wxMaterialAddVideo(accountMeta, title, introduction, new FileBody(file));
    }

    public WechatMediaUploadResult wxMaterialAddVideo(WechatAccountMeta accountMeta, String title, String introduction, ContentBody file) {
        try {
            String _description = URLEncoder.encode("{\"title\":\"" + StringUtils.trimToEmpty(title) + "\", \"introduction\":\"" + StringUtils.trimToEmpty(introduction) + "\"}", "UTF-8");
            IHttpResponse _response = HttpClientHelper.create().upload(WX_API.MATERIAL_ADD_MATERIAL + wxGetAccessToken(accountMeta) + "&description=" + _description + "&type=video", "media", file, null);
            return new WechatMediaUploadResult(MediaType.VIDEO, JSON.parseObject(_response.getContent()));
        } catch (Exception e) {
            _LOG.warn("", e);
        }
        return null;
    }

    public WechatResult wxMaterialDelete(WechatAccountMeta accountMeta, String mediaId) {
        try {
            IHttpResponse _response = HttpClientHelper.create().post(WX_API.MATERIAL_DEL_MATERIAL.concat(wxGetAccessToken(accountMeta)), "{\"media_id\":\"" + mediaId + "\"}");
            return new WechatResult(JSON.parseObject(_response.getContent()));
        } catch (Exception e) {
            _LOG.warn("", e);
        }
        return null;
    }

    public WechatMaterialResult wxMaterialBatchGet(WechatAccountMeta accountMeta, MediaType type, int offset, int count) {
        try {
            String _params = "{\"type\":\"" + type.toString().toLowerCase() + "\", \"offset\":" + offset + ", \"count\":" + count + "}";
            IHttpResponse _response = HttpClientHelper.create().post(WX_API.MATERIAL_BATCH_GET.concat(wxGetAccessToken(accountMeta)), _params);
            return new WechatMaterialResult(JSON.parseObject(_response.getContent()));
        } catch (Exception e) {
            _LOG.warn("", e);
        }
        return null;
    }

    public WechatMaterialDetailResult wxMaterialNewsAndVideoGet(WechatAccountMeta accountMeta, String mediaId) {
        try {
            IHttpResponse _response = HttpClientHelper.create().post(WX_API.MATERIAL_GET_MATERIAL.concat(wxGetAccessToken(accountMeta)), "{\"media_id\":\"" + mediaId + "\"}");
            return new WechatMaterialDetailResult(JSON.parseObject(_response.getContent()));
        } catch (Exception e) {
            _LOG.warn("", e);
        }
        return null;
    }

    public void wxMaterialFileGet(WechatAccountMeta accountMeta, String mediaId, IFileHandler fileHandler) {
        try {
            HttpClientHelper.create().download(WX_API.MATERIAL_GET_MATERIAL.concat(wxGetAccessToken(accountMeta)), "{\"media_id\":\"" + mediaId + "\"}", fileHandler);
        } catch (Exception e) {
            _LOG.warn("", e);
        }
    }

    public WechatResult wxMaterialUpdateNews(WechatAccountMeta accountMeta, String mediaId, int index, WechatMassArticle article) {
        JSONObject _params = new JSONObject();
        _params.put("media_id", mediaId);
        _params.put("index", index);
        _params.put("articles", article.toJSON());
        try {
            IHttpResponse _response = HttpClientHelper.create().post(WX_API.MATERIAL_UPDATE_NEWS.concat(wxGetAccessToken(accountMeta)), _params.toJSONString());
            return new WechatResult(JSON.parseObject(_response.getContent()));
        } catch (Exception e) {
            _LOG.warn("", e);
        }
        return null;
    }

    public WechatMaterialCountResult wxMaterialCount(WechatAccountMeta accountMeta) {
        try {
            IHttpResponse _response = HttpClientHelper.create().post(WX_API.MATERIAL_GET_COUNT.concat(wxGetAccessToken(accountMeta)), "");
            return new WechatMaterialCountResult(JSON.parseObject(_response.getContent()));
        } catch (Exception e) {
            _LOG.warn("", e);
        }
        return null;
    }
}
