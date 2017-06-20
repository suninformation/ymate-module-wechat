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
package net.ymate.platform.module.wechat;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.ymate.platform.commons.util.DateTimeUtils;
import net.ymate.platform.commons.util.UUIDUtils;
import net.ymate.platform.module.wechat.base.*;
import net.ymate.platform.module.wechat.message.OutMessage;
import net.ymate.platform.module.wechat.message.TemplateOutMessage;
import net.ymate.platform.module.wechat.support.DefaultMessageProcessor;
import net.ymate.platform.module.wechat.support.HttpClientHelper;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * <p>
 * WeChat
 * </p>
 * <p>
 * 微信公众平台服务接入框架管理器；
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
 *          <td>2014年3月13日上午1:14:17</td>
 *          </tr>
 *          </table>
 */
public class WeChat {

//	private static final Log _LOG = LogFactory.getLog(WeChat.class);

    /**
     * 当前微信公众平台服务接入框架初始化配置对象
     */
    private static IWeChatConfig __CFG_CONFIG;

    private static boolean __IS_INITED;

    private static IAccountDataProvider __dataProvider;

    private static IMessageProcessor __messageProcessor;

    /**
     * 初始化微信公众平台服务接入框架管理器
     *
     * @param config
     * @throws Exception
     */
    public static void initialize(IWeChatConfig config) throws Exception {
        if (!__IS_INITED) {
            if (config == null) {
                throw new NullArgumentException("config");
            }
            if ((__dataProvider = config.getAccountDataProviderImpl()) == null) {
                throw new NullArgumentException("AccountDataProviderImpl");
            }
            __dataProvider.initialize();
            //
            if ((__messageProcessor = config.getMessageProcessorImpl()) == null) {
//				_LOG.debug("Default Message Processor Used");
                if (config.getMessageHandlerImpl() == null) {
                    throw new NullArgumentException("MessageHandlerImpl");
                }
                __messageProcessor = new DefaultMessageProcessor(config.getMessageHandlerImpl());
            }
            __CFG_CONFIG = config;
            __IS_INITED = true;
        }
    }

    /**
     * @return 获取微信开放平台服务接入框架初始化配置对象
     */
    public static IWeChatConfig getConfig() throws Exception {
        __doCheckModuleInited();
        return __CFG_CONFIG;
    }

    /**
     * 销毁模块
     */
    public static void destroy() throws Exception {
        if (__IS_INITED) {
            __IS_INITED = false;
            __dataProvider.destroy();
        }
    }

    /**
     * @return 返回微信多帐号数据提供者
     * @throws Exception
     */
    public static IAccountDataProvider getAccountDataProvider() throws Exception {
        __doCheckModuleInited();
        return __dataProvider;
    }

    /**
     * @return 返回消息处理器
     * @throws Exception
     */
    public static IMessageProcessor getMessageProcessor() throws Exception {
        __doCheckModuleInited();
        return __messageProcessor;
    }

    private static void __doCheckModuleInited() throws Exception {
        if (!__IS_INITED) {
            throw new Exception("YMP Module WeChat was not Inited");
        }
    }

    public static JSONObject __doCheckJsonResult(String jsonStr) throws Exception {
        if (StringUtils.isBlank(jsonStr)) {
            return null;
        }
        JSONObject _result = JSON.parseObject(jsonStr);
        if (_result.containsKey("errcode") && _result.getIntValue("errcode") != 0) {
            throw new Exception("[" + _result.getIntValue("errcode") + "]" + _result.getString("errmsg"));
        }
        return _result;
    }

    private static String __doParamSignatureSort(Map<String, Object> params, boolean encode) {
        StringBuilder _paramSB = new StringBuilder();
        String[] _keys = params.keySet().toArray(new String[0]);
        Arrays.sort(_keys);
        boolean _flag = true;
        for (String _key : _keys) {
            String _value = (String) params.get(_key);
            if (StringUtils.isNotEmpty(_value)) {
                if (_flag) {
                    _flag = false;
                } else {
                    _paramSB.append("&");
                }
                _paramSB.append(_key).append("=");
                if (encode) {
                    try {
                        _paramSB.append(URLEncoder.encode(_value, HttpClientHelper.DEFAULT_CHARSET));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                } else {
                    _paramSB.append(_value);
                }
            }
        }
        return _paramSB.toString();
    }

    /**
     * @param token
     * @param signature
     * @param timestamp
     * @param nonce
     * @return 返回签名检查结果
     */
    public static boolean checkSignature(String token, String signature, String timestamp, String nonce) {
        List<String> _params = new ArrayList<String>();
        _params.add(token);
        _params.add(timestamp);
        _params.add(nonce);
        Collections.sort(_params, new Comparator<String>() {
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        return DigestUtils.shaHex(_params.get(0) + _params.get(1) + _params.get(2)).equals(signature);
    }

    /**
     * 生成JSAPI配置对象
     *
     * @param accountId 微信公众帐号ID
     * @param url       使用JSAPI的页面URL地址
     * @return 返回wx.config对象
     * @throws Exception
     */
    public static JSONObject wxCreateJsApiConfig(String accountId, String url) throws Exception {
        String _jsapiTicket = __dataProvider.getJsApiTicket(accountId);
        String _timestamp = DateTimeUtils.currentTimeMillisUTC() + "";
        String _noncestr = UUIDUtils.uuid();
        //
        StringBuilder _signSB = new StringBuilder()
                .append("jsapi_ticket=").append(_jsapiTicket).append("&")
                .append("noncestr=").append(_noncestr).append("&")
                .append("timestamp=").append(_timestamp).append("&")
                .append("url=").append(StringUtils.substringBefore(url, "#"));
        //
        JSONObject _json = new JSONObject();
        _json.put("jsapi_ticket", _jsapiTicket);
        _json.put("timestamp", _timestamp);
        _json.put("nonceStr", _noncestr);
        _json.put("url", url);
        _json.put("signature", DigestUtils.shaHex(_signSB.toString()));
        _json.put("appId", __dataProvider.getAppId(accountId));
        return _json;
    }

    /**
     * @param accountId 微信公众帐号ID
     * @return 获取AccessToken，在有效期内将被缓存，过期后会重新获取新的Token
     * @throws Exception
     */
    public static String wxGetAccessToken(String accountId) throws Exception {
        __doCheckModuleInited();
        if (StringUtils.isBlank(accountId)) {
            throw new NullArgumentException("accountId");
        }
        return __dataProvider.getAccessToken(accountId);
    }

    /**
     * @param accountId 微信公众帐号ID
     * @return 基于安全等考虑，需要获知微信服务器的IP地址列表，以便进行相关限制，可以通过该接口获得微信服务器IP地址列表
     * @throws Exception
     */
    public static String[] wxGetCallbackIP(String accountId) throws Exception {
        __doCheckModuleInited();
        if (StringUtils.isBlank(accountId)) {
            throw new NullArgumentException("accountId");
        }
        JSONObject _json = __doCheckJsonResult(HttpClientHelper.create().doGet(WX_API.WX_GET_CALLBACK_IP.concat(wxGetAccessToken(accountId))));
        return _json.getJSONArray("ip_list").toArray(new String[0]);
    }

    /**
     * @param accountId 微信公众帐号ID
     * @param mediaId
     * @return 获取媒体资源
     * @throws Exception
     */
    public static IMediaFileWrapper wxMediaGetFile(String accountId, String mediaId) throws Exception {
        __doCheckModuleInited();
        IMediaFileWrapper _wrapper = HttpClientHelper.create().doDownload(WX_API.MEDIA_GET + wxGetAccessToken(accountId) + "&media_id=" + mediaId);
        if (_wrapper.getErrorMsg() != null && StringUtils.isNotEmpty(_wrapper.getErrorMsg())) {
            __doCheckJsonResult(_wrapper.getErrorMsg());
        }
        return _wrapper;
    }

    /**
     * 上传的多媒体文件有格式和大小限制，如下：
     * 图片（image）: 128K，支持JPG格式
     * 语音（voice）：256K，播放长度不超过60s，支持AMR\MP3格式
     * 视频（video）：1MB，支持MP4格式
     * 缩略图（thumb）：64KB，支持JPG格式
     * 媒体文件在后台保存时间为3天，即3天后media_id失效
     *
     * @param accountId 微信公众帐号ID
     * @param type
     * @param file
     * @return 上传媒体文件
     * @throws Exception
     */
    public static WxMediaUploadResult wxMediaUploadFile(String accountId, WxMediaType type, File file) throws Exception {
        __doCheckModuleInited();
        if (WxMediaType.NEWS.equals(type)) {
            throw new UnsupportedOperationException("News media type need use wxMediaUploadNews method.");
        }
        // {"type":"TYPE","media_id":"MEDIA_ID","created_at":123456789}
        // {"type":"TYPE","thubm_media_id":"MEDIA_ID","created_at":123456789}
        JSONObject _json = __doCheckJsonResult(HttpClientHelper.create().doUpload(WX_API.MEDIA_UPLOAD + wxGetAccessToken(accountId) + "&type=" + type.toString().toLowerCase(), file));
        return new WxMediaUploadResult(type, _json.getString("media_id"), _json.getString("thumb_media_id"), _json.getLong("created_at"));
    }

    /**
     * @param accountId 微信公众帐号ID
     * @param articles  图文消息对象集合
     * @return 上传图文消息素材
     * @throws Exception
     */
    public static WxMediaUploadResult wxMediaUploadNews(String accountId, List<WxMassArticle> articles) throws Exception {
        __doCheckModuleInited();
        if (articles == null || articles.isEmpty()) {
            throw new NullArgumentException("articles");
        }
        StringBuilder _paramSB = new StringBuilder("{ \"articles\": [");
        boolean _flag = false;
        for (WxMassArticle article : articles) {
            if (_flag) {
                _paramSB.append(",");
            }
            _paramSB.append(article.toJSON());
            _flag = true;
        }
        _paramSB.append("]}");
        JSONObject _json = __doCheckJsonResult(HttpClientHelper.create().doPost(WX_API.MEDIA_UPLOAD_NEWS + wxGetAccessToken(accountId), _paramSB.toString()));
        return new WxMediaUploadResult(WxMediaType.NEWS, _json.getString("media_id"), _json.getString("thumb_media_id"), _json.getLong("created_at"));
    }

    /**
     * @param accountId
     * @param video
     * @return 上传群发用的视频
     * @throws Exception
     */
    public static WxMediaUploadResult wxMediaUploadVideo(String accountId, WxMassVideo video) throws Exception {
        __doCheckModuleInited();
        JSONObject _json = __doCheckJsonResult(HttpClientHelper.create().doPost(WX_API.MEDIA_UPLOAD_VIDEO + wxGetAccessToken(accountId), video.toJSON()));
        return new WxMediaUploadResult(WxMediaType.VIDEO, _json.getString("media_id"), _json.getString("thumb_media_id"), _json.getLong("created_at"));
    }

    /**
     * @param accountId
     * @param groupId
     * @param msgType
     * @param mediaIdOrContent
     * @return 根据分组进行群发并返回消息ID
     * @throws Exception
     */
    public static Long wxMassSendByGroupId(String accountId, String groupId, String msgType, String mediaIdOrContent) throws Exception {
        __doCheckModuleInited();
        StringBuilder _paramSB = new StringBuilder("{");
        _paramSB.append("\"filter\": {").append("\"group_id\":").append("\"").append(groupId).append("\"},");
        //
        String _msgType = WX_MESSAGE.TYPE_MP_NEWS;
        String _bodyAttr = "media_id";
        if (WX_MESSAGE.TYPE_MP_NEWS.equals(msgType)) {
            // default..
        } else if (WX_MESSAGE.TYPE_TEXT.equals(msgType)) {
            _msgType = WX_MESSAGE.TYPE_TEXT;
            _bodyAttr = "content";
        } else if (WX_MESSAGE.TYPE_VOICE.equals(msgType)) {
            _msgType = WX_MESSAGE.TYPE_VOICE;
        } else if (WX_MESSAGE.TYPE_IMAGE.equals(msgType)) {
            _msgType = WX_MESSAGE.TYPE_IMAGE;
        } else if (WX_MESSAGE.TYPE_MP_VIDEO.equals(msgType)) {
            _msgType = WX_MESSAGE.TYPE_MP_VIDEO;
        } else {
            throw new UnsupportedOperationException("Unsupport Message Type \"" + msgType + "\".");
        }
        _paramSB.append("\"").append(_msgType).append("\": {").append("\"").append(_bodyAttr).append("\":\"").append(mediaIdOrContent).append("\"},");
        _paramSB.append("\"msgtype\": \"").append(_msgType).append("\"}");
        JSONObject _json = __doCheckJsonResult(HttpClientHelper.create().doPost(WX_API.MASS_SEND_BY_GROUP + wxGetAccessToken(accountId), _paramSB.toString()));
        return _json.getLong("msg_id");
    }

    /**
     * @param accountId
     * @param openIds
     * @param mediaIdOrContent
     * @return 根据OpenID列表群发并返回消息ID
     * @throws Exception
     */
    public static Long wxMassSendByOpenId(String accountId, List<String> openIds, String msgType, String mediaIdOrContent) throws Exception {
        __doCheckModuleInited();
        if (openIds == null || openIds.isEmpty()) {
            throw new NullArgumentException("openIds");
        }
        StringBuilder _paramSB = new StringBuilder("{");
        _paramSB.append("\"touser\": ").append(JSON.toJSONString(openIds)).append(",");
        //
        String _msgType = WX_MESSAGE.TYPE_MP_NEWS;
        String _bodyAttr = "media_id";
        if (WX_MESSAGE.TYPE_MP_NEWS.equals(msgType)) {
            // default..
        } else if (WX_MESSAGE.TYPE_TEXT.equals(msgType)) {
            _msgType = WX_MESSAGE.TYPE_TEXT;
            _bodyAttr = "content";
        } else if (WX_MESSAGE.TYPE_VOICE.equals(msgType)) {
            _msgType = WX_MESSAGE.TYPE_VOICE;
        } else if (WX_MESSAGE.TYPE_IMAGE.equals(msgType)) {
            _msgType = WX_MESSAGE.TYPE_IMAGE;
        } else if (WX_MESSAGE.TYPE_MP_VIDEO.equals(msgType)) {
            _msgType = WX_MESSAGE.TYPE_MP_VIDEO;
        } else {
            throw new UnsupportedOperationException("Unsupport Message Type \"" + msgType + "\".");
        }
        _paramSB.append("\"").append(_msgType).append("\": {").append("\"").append(_bodyAttr).append("\":\"").append(mediaIdOrContent).append("\"},");
        _paramSB.append("\"msgtype\": \"").append(_msgType).append("\"}");
        JSONObject _json = __doCheckJsonResult(HttpClientHelper.create().doPost(WX_API.MASS_SEND_BY_OPENID + wxGetAccessToken(accountId), _paramSB.toString()));
        return _json.getLong("msg_id");
    }

    /**
     * @param accountId
     * @param msgId
     * @return 删除群发(只是将消息的图文详情页失效)
     * @throws Exception
     */
    public static boolean wxMassDelete(String accountId, Long msgId) throws Exception {
        __doCheckModuleInited();
        JSONObject _result = __doCheckJsonResult(HttpClientHelper.create().doPost(WX_API.MASS_DELETE.concat(wxGetAccessToken(accountId)), "{\"msgid\":" + msgId + "}"));
        return 0 == _result.getIntValue("errcode");
    }

    /**
     * @param accountId 微信公众帐号ID
     * @param message
     * @return 发送客服消息
     * @throws Exception
     */
    public static String wxMessageSendCustom(String accountId, OutMessage message) throws Exception {
        __doCheckModuleInited();
        return HttpClientHelper.create().doPost(WX_API.MESSAGE_SEND.concat(wxGetAccessToken(accountId)), message.toJSON());
    }

    /**
     * @param accountId 微信公众帐号ID
     * @param message   模板消息对象
     * @return 发送模板消息，若成功则返回msgid
     * @throws Exception
     */
    public static String wxMessageSendTemplate(String accountId, TemplateOutMessage message) throws Exception {
        __doCheckModuleInited();
        JSONObject _result = __doCheckJsonResult(HttpClientHelper.create().doPost(WX_API.MESSAGE_TEMPLATE_SEND.concat(wxGetAccessToken(accountId)), message.toJSON()));
        return _result.getString("msgid");
    }

    /**
     * @param accountId 微信公众帐号ID
     * @param openid
     * @param lang      语言(可选)
     * @return 获取用户基本信息
     * @throws Exception
     */
    public static WxUser wxUserGetInfo(String accountId, String openid, WxLangType lang) throws Exception {
        __doCheckModuleInited();
        Map<String, String> _params = new HashMap<String, String>();
        _params.put("access_token", wxGetAccessToken(accountId));
        _params.put("openid", openid);
        if (lang != null) {
            _params.put("lang", lang.toString());
        }
        JSONObject _json = __doCheckJsonResult(HttpClientHelper.create().doGet(WX_API.USER_INFO, _params));
        return new WxUser(_json.getString("openid"),
                _json.getString("nickname"), _json.getInteger("sex"),
                _json.getString("city"), _json.getString("province"),
                _json.getString("country"), _json.getString("headimgurl"),
                _json.getInteger("subscribe"), _json.getLong("subscribe_time"));
    }

    /**
     * @param accountId   微信公众帐号ID
     * @param next_openid
     * @return 获取关注者列表
     * @throws Exception
     */
    public static WxFollwersResult wxUserGetList(String accountId, String next_openid) throws Exception {
        __doCheckModuleInited();
        Map<String, String> params = new HashMap<String, String>();
        params.put("access_token", wxGetAccessToken(accountId));
        params.put("next_openid", next_openid);
        JSONObject _json = __doCheckJsonResult(HttpClientHelper.create().doGet(WX_API.USER_GET, params));
        List<String> _datas = null;
        if (_json.containsKey("data") && _json.getJSONObject("data").containsKey("openid")) {
            _datas = JSON.parseArray(_json.getJSONObject("data").getJSONArray("openid").toJSONString(), String.class);
        }
        if (_datas == null) {
            _datas = Collections.emptyList();
        }
        return new WxFollwersResult(_json.getLongValue("total"), _json.getIntValue("count"), _datas, _json.getString("next_openid"));
    }

    /**
     * @param accountId
     * @param openid    用户标识
     * @param remark    新的备注名，长度必须小于30字符
     * @return 对指定用户设置备注名
     * @throws Exception
     */
    public static boolean wxUserUpdateRemark(String accountId, String openid, String remark) throws Exception {
        __doCheckModuleInited();
        Map<String, String> _params = new HashMap<String, String>();
        _params.put("access_token", wxGetAccessToken(accountId));
        _params.put("openid", openid);
        _params.put("remark", remark);
        JSONObject _json = __doCheckJsonResult(HttpClientHelper.create().doGet(WX_API.USER_UPDATE_REMARK, _params));
        return 0 == _json.getIntValue("errcode");
    }

    /**
     * @param accountId 微信公众帐号ID
     * @param name
     * @return 创建分组
     * @throws Exception
     */
    public static WxGroup wxGroupCreate(String accountId, String name) throws Exception {
        __doCheckModuleInited();
        JSONObject _groupJSON = new JSONObject();
        JSONObject _nameJSON = new JSONObject();
        _nameJSON.put("name", name);
        _groupJSON.put("group", _nameJSON);
        return JSON.toJavaObject(__doCheckJsonResult(HttpClientHelper.create().doPost(WX_API.GROUP_CREATE.concat(wxGetAccessToken(accountId)), _groupJSON.toString())), WxGroup.class);
    }

    /**
     * @param accountId 微信公众帐号ID
     * @return 查询所有分组
     * @throws Exception
     */
    public static List<WxGroup> wxGroupGetList(String accountId) throws Exception {
        __doCheckModuleInited();
        JSONObject _json = __doCheckJsonResult(HttpClientHelper.create().doGet(WX_API.GROUP_GET.concat(wxGetAccessToken(accountId))));
        if (_json.containsKey("groups")) {
            return JSON.parseArray(_json.getJSONArray("groups").toJSONString(), WxGroup.class);
        }
        return Collections.emptyList();
    }

    /**
     * @param accountId 微信公众帐号ID
     * @param openid
     * @return 查询用户所在分组ID
     * @throws Exception
     */
    public static int wxGroupGetId(String accountId, String openid) throws Exception {
        __doCheckModuleInited();
        JSONObject _openidJSON = new JSONObject();
        _openidJSON.put("openid", openid);
        JSONObject _json = __doCheckJsonResult(HttpClientHelper.create().doPost(WX_API.GROUP_GET_ID.concat(wxGetAccessToken(accountId)), _openidJSON.toString()));
        return _json.getIntValue("groupid");
    }

    /**
     * @param accountId 微信公众帐号ID
     * @param id
     * @param name
     * @return 修改分组名
     * @throws Exception
     */
    public static boolean wxGroupUpdate(String accountId, String id, String name) throws Exception {
        __doCheckModuleInited();
        JSONObject _groupJSON = new JSONObject();
        _groupJSON.put("id", id);
        _groupJSON.put("name", name);
        JSONObject _paramJSON = new JSONObject();
        _paramJSON.put("group", _groupJSON);
        JSONObject _json = __doCheckJsonResult(HttpClientHelper.create().doPost(WX_API.GROUP_UPDATE.concat(wxGetAccessToken(accountId)), _paramJSON.toString()));
        return 0 == _json.getIntValue("errcode");
    }

    /**
     * @param accountId  微信公众帐号ID
     * @param openid
     * @param to_groupid
     * @return 移动用户分组
     * @throws Exception
     */
    public static boolean wxGroupMembersMove(String accountId, String openid, String to_groupid) throws Exception {
        __doCheckModuleInited();
        JSONObject _paramJSON = new JSONObject();
        _paramJSON.put("openid", openid);
        _paramJSON.put("to_groupid", to_groupid);
        JSONObject _json = __doCheckJsonResult(HttpClientHelper.create().doPost(WX_API.GROUP_MEMBERS_UPDATE.concat(wxGetAccessToken(accountId)), _paramJSON.toString()));
        return 0 == _json.getIntValue("errcode");
    }

    /**
     * @param accountId 微信公众帐号ID
     * @param menu
     * @return 创建菜单
     * @throws Exception
     */
    public static boolean wxMenuCreate(String accountId, WxMenu menu) throws Exception {
        __doCheckModuleInited();
        JSONObject _result = __doCheckJsonResult(HttpClientHelper.create().doPost(WX_API.MENU_CREATE.concat(wxGetAccessToken(accountId)), JSON.toJSONString(menu)));
        return 0 == _result.getIntValue("errcode");
    }

    /**
     * @param accountId 微信公众帐号ID
     * @return 查询菜单
     * @throws Exception
     */
    public static WxMenu wxMenuGet(String accountId) throws Exception {
        __doCheckModuleInited();
        JSONObject _json = __doCheckJsonResult(HttpClientHelper.create().doGet(WX_API.MENU_GET.concat(wxGetAccessToken(accountId))));
        return JSON.toJavaObject(_json.getJSONObject("menu"), WxMenu.class);
    }

    /**
     * @param accountId 微信公众帐号ID
     * @return 删除自定义菜单
     * @throws Exception
     */
    public static boolean wxMenuDelete(String accountId) throws Exception {
        __doCheckModuleInited();
        JSONObject _result = __doCheckJsonResult(HttpClientHelper.create().doGet(WX_API.MENU_DELETE.concat(wxGetAccessToken(accountId))));
        return 0 == _result.getIntValue("errcode");
    }

    /**
     * @param accountId      微信公众帐号ID
     * @param scene_id       场景ID，场景ID若大于100000时自动转换成为临时二维码
     * @param expire_seconds 二维码有效时间，0表示永久保存，最大1800，单位：秒
     * @return 创建二维码Ticket
     * @throws Exception
     */
    public static WxQRCode wxQRCodeCreate(String accountId, int scene_id, int expire_seconds) throws Exception {
        __doCheckModuleInited();
        JSONObject _paramJSON = new JSONObject();
        if (expire_seconds > 0 || scene_id > 100000) {
            _paramJSON.put("action_name", "QR_SCENE");
            _paramJSON.put("expire_seconds", expire_seconds <= 0 ? 1800 : expire_seconds);
        }
        JSONObject _sceneJSON = new JSONObject();
        _sceneJSON.put("scene_id", scene_id);
        JSONObject _infoJSON = new JSONObject();
        _infoJSON.put("scene", _sceneJSON);
        _paramJSON.put("action_info", _infoJSON);
        // {"ticket":"gQG28DoAAAAAAAAAASxodHRwOi8vd2VpeGluLnFxLmNvbS9xL0FuWC1DNmZuVEhvMVp4NDNMRnNRAAIEesLvUQMECAcAAA==","expire_seconds":1800}
        JSONObject _json = __doCheckJsonResult(HttpClientHelper.create().doPost(WX_API.QRCODE_CREATE.concat(wxGetAccessToken(accountId)), _paramJSON.toString()));
        return new WxQRCode(scene_id, _json.getString("ticket"), _json.getIntValue("expire_seconds"));
    }

    /**
     * @param ticket
     * @return 返回二维码访问URL地址
     */
    public static String wxQRCodeShowURL(String ticket) {
        try {
            return WX_API.QRCODE_SHOW.concat(URLEncoder.encode(ticket, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param accountId    微信公众帐号ID
     * @param needUserInfo 是否弹出授权页面
     * @param state        自定义参数(a-zA-Z0-9)
     * @return 返回微信用户授权URL地址
     * @throws Exception
     */
    public static String wxOAuthGetCodeURL(String accountId, boolean needUserInfo, String state) throws Exception {
        String _appId = __dataProvider.getAppId(accountId);
        if (StringUtils.isBlank(_appId)) {
            throw new NullArgumentException("appId");
        }
        return wxOAuthGetCodeURL(accountId, needUserInfo, state, __dataProvider.getRedirectURI(accountId));
    }

    /**
     * @param accountId    微信公众帐号ID
     * @param needUserInfo 是否弹出授权页面
     * @param state        自定义参数(a-zA-Z0-9)
     * @param redirectURI  自定义重定向地址
     * @return 返回微信用户授权URL地址
     * @throws Exception
     */
    public static String wxOAuthGetCodeURL(String accountId, boolean needUserInfo, String state, String redirectURI) throws Exception {
        __doCheckModuleInited();
        String _appId = __dataProvider.getAppId(accountId);
        if (StringUtils.isBlank(_appId)) {
            throw new NullArgumentException("appId");
        }
        if (StringUtils.isBlank(redirectURI)) {
            throw new NullArgumentException("redirectURI");
        }
        Map<String, Object> _params = new HashMap<String, Object>();
        _params.put("appid", _appId);
        _params.put("response_type", "code");
        _params.put("redirect_uri", URLEncoder.encode(redirectURI, HttpClientHelper.DEFAULT_CHARSET));
        _params.put("scope", needUserInfo ? "snsapi_userinfo" : "snsapi_base");
        _params.put("state", StringUtils.defaultIfEmpty(state, "") + "#wechat_redirect");
        return WX_API.OAUTH_GET_CODE.concat(__doParamSignatureSort(_params, false));
    }

    /**
     * { "access_token":"ACCESS_TOKEN", "expires_in":7200, "refresh_token":"REFRESH_TOKEN", "openid":"OPENID", "scope":"SCOPE" }
     *
     * @param accountId 微信公众帐号ID
     * @param code
     * @return 通过Code换取网页授权的AccessToken
     * @throws Exception
     */
    public static WxOAuthToken wxOAuthGetToken(String accountId, String code) throws Exception {
        __doCheckModuleInited();
        String _appId = __dataProvider.getAppId(accountId);
        if (StringUtils.isBlank(_appId)) {
            throw new NullArgumentException("appId");
        }
        String _appSecret = __dataProvider.getAppSecret(accountId);
        if (StringUtils.isBlank(_appSecret)) {
            throw new NullArgumentException("appSecret");
        }
        if (StringUtils.isBlank(code)) {
            throw new NullArgumentException("code");
        }
        Map<String, String> _params = new HashMap<String, String>();
        _params.put("appid", _appId);
        _params.put("secret", _appSecret);
        _params.put("code", code);
        _params.put("grant_type", "authorization_code");
        JSONObject _json = __doCheckJsonResult(HttpClientHelper.create().doGet(WX_API.OAUTH_ACCESS_TOKEN, _params));
        return new WxOAuthToken(_json.getString("access_token"), _json.getIntValue("expires_in"), _json.getString("refresh_token"), _json.getString("openid"), _json.getString("scope"));
    }

    /**
     * @param accountId    微信公众帐号ID
     * @param refreshToken 哪个想刷就刷哪个~
     * @return 刷新AccessToken
     * @throws Exception
     */
    public static WxOAuthToken wxOAuthRefreshToken(String accountId, String refreshToken) throws Exception {
        __doCheckModuleInited();
        String _appId = __dataProvider.getAppId(accountId);
        if (StringUtils.isBlank(_appId)) {
            throw new NullArgumentException("appId");
        }
        if (StringUtils.isBlank(refreshToken)) {
            throw new NullArgumentException("refreshToken");
        }
        Map<String, String> _params = new HashMap<String, String>();
        _params.put("appid", _appId);
        _params.put("grant_type", "refresh_token");
        _params.put("refresh_token", refreshToken);
        JSONObject _json = __doCheckJsonResult(HttpClientHelper.create().doGet(WX_API.OAUTH_REFRESH_TOKEN, _params));
        return new WxOAuthToken(_json.getString("access_token"), _json.getIntValue("expires_in"), _json.getString("refresh_token"), _json.getString("openid"), _json.getString("scope"));
    }

    /**
     * @param openid
     * @param lang   语言(可选)
     * @return 拉取用户信息
     * @throws Exception
     */
    public static WxOAuthUser wxOAuthUserGetInfo(String oauthAccessToken, String openid, WxLangType lang) throws Exception {
        __doCheckModuleInited();
        if (StringUtils.isBlank(oauthAccessToken)) {
            throw new NullArgumentException("oauthAccessToken");
        }
        if (StringUtils.isBlank(openid)) {
            throw new NullArgumentException("openid");
        }
        Map<String, String> _params = new HashMap<String, String>();
        _params.put("openid", openid);
        if (lang != null) {
            _params.put("lang", lang.toString());
        }
        JSONObject _json = __doCheckJsonResult(HttpClientHelper.create().doGet(WX_API.OAUTH_USER_INFO.concat(oauthAccessToken), _params));
        return new WxOAuthUser(_json.getString("openid"),
                _json.getString("unionid"),
                _json.getString("nickname"), _json.getInteger("sex"),
                _json.getString("province"), _json.getString("city"),
                _json.getString("country"), _json.getString("headimgurl"),
                JSON.parseArray(_json.getJSONArray("privilege").toJSONString(), String.class));
    }

    /**
     * 检验授权凭证（access_token）是否有效
     *
     * @param oauthAccessToken 网页授权接口调用凭证
     * @param openid           用户的唯一标识
     * @return true / false
     * @throws Exception
     */
    public static boolean wxOAuthAuthAccessToken(String oauthAccessToken, String openid) throws Exception {
        __doCheckModuleInited();
        if (StringUtils.isBlank(oauthAccessToken)) {
            throw new NullArgumentException("oauthAccessToken");
        }
        if (StringUtils.isBlank(openid)) {
            throw new NullArgumentException("openid");
        }
        Map<String, String> _params = new HashMap<String, String>();
        _params.put("openid", openid);
        try {
            __doCheckJsonResult(HttpClientHelper.create().doGet(WX_API.OAUTH_AUTH_ACCESS_TOKEN.concat(oauthAccessToken), _params));
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * @param accountId
     * @param longUrl   需要转换的长链接
     * @return 长链接转成短链接
     * @throws Exception
     */
    public static String wxShortUrl(String accountId, String longUrl) throws Exception {
        __doCheckModuleInited();
        JSONObject _json = __doCheckJsonResult(HttpClientHelper.create().doGet(WX_API.SHORT_URL.concat(wxGetAccessToken(accountId))));
        return _json.getString("short_url");
    }

    /**
     * 上传永久图文素材
     *
     * @param accountId
     * @param articles
     * @return
     * @throws Exception
     */
    public static String wxMaterialAddNews(String accountId, List<WxMassArticle> articles) throws Exception {
        __doCheckModuleInited();
        if (articles == null || articles.isEmpty()) {
            throw new NullArgumentException("articles");
        }
        StringBuilder _paramSB = new StringBuilder("{ \"articles\": [");
        boolean _flag = false;
        for (WxMassArticle article : articles) {
            if (_flag) {
                _paramSB.append(",");
            }
            _paramSB.append(article.toJSON());
            _flag = true;
        }
        _paramSB.append("]}");
        JSONObject _json = __doCheckJsonResult(HttpClientHelper.create().doPost(WX_API.MATERIAL_ADD_NEWS + wxGetAccessToken(accountId), _paramSB.toString()));
        return _json.getString("media_id");
    }

    /**
     * 上传永久媒体素材(不包括视频)
     *
     * @param accountId
     * @param type
     * @param file
     * @return
     * @throws Exception
     */
    public static String wxMaterialAddNews(String accountId, WxMediaType type, File file) throws Exception {
        __doCheckModuleInited();
        if (WxMediaType.NEWS.equals(type)) {
            throw new UnsupportedOperationException("News media type need use wxMediaUploadNews method.");
        } else if (WxMediaType.VIDEO.equals(type) || WxMediaType.SHORT_VIDEO.equals(type)) {
            throw new UnsupportedOperationException("Unsupport Video and Short Video");
        }
        JSONObject _json = __doCheckJsonResult(HttpClientHelper.create().doUpload(WX_API.MATERIAL_ADD_MATERIAL + wxGetAccessToken(accountId) + "&type=" + type.toString().toLowerCase(), file));
        return _json.getString("media_id");
    }

    /**
     * 上传永久视频素材
     *
     * @param accountId
     * @param title
     * @param introduction
     * @param file
     * @return
     * @throws Exception
     */
    public static String wxMaterialAddNews(String accountId, String title, String introduction, File file) throws Exception {
        __doCheckModuleInited();
        String _description = URLEncoder.encode("{\"title\":\"" + title + "\", \"introduction\":\"" + introduction + "\"}", "UTF-8");
        JSONObject _json = __doCheckJsonResult(HttpClientHelper.create().doUpload(WX_API.MATERIAL_ADD_MATERIAL + wxGetAccessToken(accountId) + "&description=" + _description + "&type=" + WxMediaType.VIDEO.toString().toLowerCase(), file));
        return _json.getString("media_id");
    }

    /**
     * 删除永久素材
     *
     * @param accountId
     * @param mediaId
     * @return
     * @throws Exception
     */
    public static boolean wxMaterialDelete(String accountId, String mediaId) throws Exception {
        __doCheckModuleInited();
        JSONObject _result = __doCheckJsonResult(HttpClientHelper.create().doPost(WX_API.MATERIAL_DEL_MATERIAL.concat(wxGetAccessToken(accountId)), "{\"media_id\":\"" + mediaId + "\"}"));
        return 0 == _result.getIntValue("errcode");
    }

    /**
     * 获取素材列表
     *
     * @param accountId
     * @param type
     * @param offset
     * @param count
     * @return
     * @throws Exception
     */
    public static WxMaterialResult wxMaterialGetNews(String accountId, WxMediaType type, int offset, int count) throws Exception {
        __doCheckModuleInited();
        String _params = "{\"type\":\"" + type.toString().toLowerCase() + "\", \"offset\":" + offset + ", \"count\":" + count + "}";
        JSONObject _resultJSON = __doCheckJsonResult(HttpClientHelper.create().doPost(WX_API.MATERIAL_BATCH_GET.concat(wxGetAccessToken(accountId)), _params));
        if (_resultJSON != null) {
            WxMaterialResult _result = JSON.toJavaObject(_resultJSON, WxMaterialResult.class);
            return _result;
        }
        return null;
    }

    /**
     * 获取永久素材(图文和视频)
     *
     * @param accountId
     * @param mediaId
     * @return
     * @throws Exception
     */
    public static WxNewsItem wxMaterialNewOrVideoGet(String accountId, String mediaId) throws Exception {
        __doCheckModuleInited();
        JSONObject _resultJSON = __doCheckJsonResult(HttpClientHelper.create().doPost(WX_API.MATERIAL_GET_MATERIAL.concat(wxGetAccessToken(accountId)), "{\"media_id\":\"" + mediaId + "\"}"));
        if (_resultJSON != null) {
            WxNewsItem _result = JSON.toJavaObject(_resultJSON, WxNewsItem.class);
            return _result;
        }
        return null;
    }

    /**
     * 获取永久素材（非图文和非视频）
     *
     * @param accountId
     * @param mediaId
     * @return
     * @throws Exception
     */
    public static IMediaFileWrapper wxMaterialFileGet(String accountId, String mediaId) throws Exception {
        __doCheckModuleInited();
        return HttpClientHelper.create().doDownload(WX_API.MATERIAL_GET_MATERIAL.concat(wxGetAccessToken(accountId)), "{\"media_id\":\"" + mediaId + "\"}");
    }

    /**
     * 修改永久图文素材
     *
     * @param accountId
     * @param mediaId
     * @param index
     * @param article
     * @return
     * @throws Exception
     */
    public static boolean wxMaterialUpdate(String accountId, String mediaId, int index, WxMassArticle article) throws Exception {
        __doCheckModuleInited();
        JSONObject _params = new JSONObject();
        _params.put("media_id", mediaId);
        _params.put("index", index);
        _params.put("articles", JSON.toJSON(article));
        JSONObject _result = __doCheckJsonResult(HttpClientHelper.create().doPost(WX_API.MATERIAL_UPDATE_NEWS.concat(wxGetAccessToken(accountId)), _params.toJSONString()));
        return 0 == _result.getIntValue("errcode");
    }

    /**
     * 获取素材总数
     *
     * @param accountId
     * @return
     * @throws Exception
     */
    public static WxMaterialCount wxMaterialCount(String accountId) throws Exception {
        __doCheckModuleInited();
        JSONObject _resultJSON = __doCheckJsonResult(HttpClientHelper.create().doPost(WX_API.MATERIAL_GET_COUNT.concat(wxGetAccessToken(accountId)), ""));
        if (_resultJSON != null) {
            WxMaterialCount _result = JSON.toJavaObject(_resultJSON, WxMaterialCount.class);
            return _result;
        }
        return null;
    }

    /**
     * @param request  Http请求对象
     * @param needVer5 是否需要判断版本大于5.0
     * @return 判断当前是否在微信环境
     */
    public static boolean isInWeChat(HttpServletRequest request, boolean needVer5) {
        String userAgent = request.getHeader("User-Agent");
        if (StringUtils.isNotBlank(userAgent)) {
            Pattern p = Pattern.compile("MicroMessenger/(\\d+).+");
            Matcher m = p.matcher(userAgent);
            String version = null;
            if (m.find()) {
                version = m.group(1);
            }
            if (null != version) {
                if (needVer5) {
                    return NumberUtils.toInt(version) >= 5;
                }
                return true;
            }
        }
        return false;
    }

    /**
     * <p>
     * WX_MESSAGE
     * </p>
     * <p>
     * 微信消息类型
     * </p>
     */
    public static class WX_MESSAGE {
        public final static String TYPE_TEXT = "text";
        public final static String TYPE_LOCATION = "location";
        public final static String TYPE_IMAGE = "image";
        public final static String TYPE_LINK = "link";
        public final static String TYPE_VOICE = "voice";
        public final static String TYPE_EVENT = "event";
        public final static String TYPE_VIDEO = "video";
        public final static String TYPE_SHORT_VIDEO = "shortvideo";

        public final static String TYPE_NEWS = "news";
        public final static String TYPE_MP_NEWS = "mpnews"; // 此类型仅用于群发图文
        public final static String TYPE_MP_VIDEO = "mpvideo"; // 此类型仅用于群发视频
        public final static String TYPE_MUSIC = "music";

        public final static String EVENT_LOCATION = "LOCATION";
        public final static String EVENT_SCAN = "SCAN";
        public final static String EVENT_SUBSCRIBE = "subscribe";
        public final static String EVENT_UNSUBSCRIBE = "unsubscribe";

        public final static String EVENT_CLICK = "click";
        public final static String EVENT_VIEW = "view";

        public final static String EVENT_MASS_SEND_JOB_FINISH = "MASSSENDJOBFINISH";
        public final static String EVENT_TEMPLATE_SEND_JOB_FINISH = "TEMPLATESENDJOBFINISH";
    }

    /**
     * <p>
     * WxMediaType
     * </p>
     * <p>
     * 微信媒体资源类型
     * </p>
     */
    public static enum WxMediaType {
        IMAGE, VOICE, VIDEO, SHORT_VIDEO, THUMB, NEWS
    }

    /**
     * <p>
     * WxLangType
     * </p>
     * <p>
     * 微信支持的语言
     * </p>
     */
    public static enum WxLangType {
        zh_CN, zh_TW, en
    }

    /**
     * <p>
     * WX_API
     * </p>
     * <p>
     * 微信API地址
     * </p>
     */
    public static class WX_API {
        public static final String WX_ACCESS_TOKEN = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential";
        public static final String WX_GET_CALLBACK_IP = "https://api.weixin.qq.com/cgi-bin/getcallbackip?access_token=";

        public static final String MEDIA_GET = "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=";
        public static final String MEDIA_UPLOAD = "http://file.api.weixin.qq.com/cgi-bin/media/upload?access_token=";

        public static final String MEDIA_UPLOAD_NEWS = "https://api.weixin.qq.com/cgi-bin/media/uploadnews?access_token=";
        public static final String MEDIA_UPLOAD_VIDEO = "http://file.api.weixin.qq.com/cgi-bin/media/uploadvideo?access_token=";

        public static final String GROUP_CREATE = "https://api.weixin.qq.com/cgi-bin/groups/create?access_token=";
        public static final String GROUP_GET = "https://api.weixin.qq.com/cgi-bin/groups/get?access_token=";
        public static final String GROUP_GET_ID = "https://api.weixin.qq.com/cgi-bin/groups/getid?access_token=";
        public static final String GROUP_UPDATE = "https://api.weixin.qq.com/cgi-bin/groups/update?access_token=";
        public static final String GROUP_MEMBERS_UPDATE = "https://api.weixin.qq.com/cgi-bin/groups/members/update?access_token=";

        public static final String USER_INFO = "https://api.weixin.qq.com/cgi-bin/user/info";
        public static final String USER_GET = "https://api.weixin.qq.com/cgi-bin/user/get";
        public static final String USER_UPDATE_REMARK = "https://api.weixin.qq.com/cgi-bin/user/info/updateremark?access_token=";

        public static final String MENU_CREATE = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=";
        public static final String MENU_GET = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=";
        public static final String MENU_DELETE = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=";

        public static final String QRCODE_CREATE = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=";
        public static final String QRCODE_SHOW = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=";

        public static final String MESSAGE_SEND = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=";

        public static final String MESSAGE_TEMPLATE_SEND = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";

        public static final String MASS_SEND_BY_GROUP = "https://api.weixin.qq.com/cgi-bin/message/mass/sendall?access_token=";
        public static final String MASS_SEND_BY_OPENID = "https://api.weixin.qq.com/cgi-bin/message/mass/send?access_token=";
        public static final String MASS_DELETE = "https://api.weixin.qq.com//cgi-bin/message/mass/delete?access_token=";

        public static final String OAUTH_GET_CODE = "https://open.weixin.qq.com/connect/oauth2/authorize?";
        public static final String OAUTH_ACCESS_TOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token";
        public static final String OAUTH_REFRESH_TOKEN = "https://api.weixin.qq.com/sns/oauth2/refresh_token";
        public static final String OAUTH_USER_INFO = "https://api.weixin.qq.com/sns/userinfo?access_token=";
        public static final String OAUTH_AUTH_ACCESS_TOKEN = "https://api.weixin.qq.com/sns/auth?access_token=";

        public static final String OAUTH_JSAPI_TICKET = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?type=jsapi&access_token=";

        public static final String SHORT_URL = "https://api.weixin.qq.com/cgi-bin/shorturl?action=long2short&access_token=";


        // 新增永久图文素材
        public static final String MATERIAL_ADD_NEWS = "https://api.weixin.qq.com/cgi-bin/material/add_news?access_token=";
        // 新增其他类型永久素材
        public static final String MATERIAL_ADD_MATERIAL = "https://api.weixin.qq.com/cgi-bin/material/add_material?access_token=";
        // 获取永久素材
        public static final String MATERIAL_GET_MATERIAL = "https://api.weixin.qq.com/cgi-bin/material/get_material?access_token=";
        // 删除永久素材
        public static final String MATERIAL_DEL_MATERIAL = "https://api.weixin.qq.com/cgi-bin/material/del_material?access_token=";
        // 修改永久图文素材
        public static final String MATERIAL_UPDATE_NEWS = "https://api.weixin.qq.com/cgi-bin/material/update_news?access_token=";
        // 获取素材总数
        public static final String MATERIAL_GET_COUNT = "https://api.weixin.qq.com/cgi-bin/material/get_materialcount?access_token=";
        // 获取素材列表
        public static final String MATERIAL_BATCH_GET = "https://api.weixin.qq.com/cgi-bin/material/batchget_material?access_token=";
    }

}
