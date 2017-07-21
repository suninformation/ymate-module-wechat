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

import net.ymate.framework.commons.IFileHandler;
import net.ymate.module.wechat.base.*;
import net.ymate.module.wechat.message.OutMessage;
import net.ymate.module.wechat.message.TemplateMessage;
import net.ymate.platform.core.YMP;
import org.apache.http.entity.mime.content.ContentBody;

import java.io.File;
import java.util.List;

/**
 * @author 刘镇 (suninformation@163.com) on 16/5/22 上午1:50
 * @version 1.0
 */
public interface IWechat {

    String MODULE_NAME = "module.wechat";

    YMP getOwner();

    IWechatCfg getModuleCfg();

    WechatAccountMeta getAccountByToken(String token);

    WechatAccountMeta getAccountById(String accountId);

    WechatAccountMeta getAccountByAppId(String appId);

    // ----------

    String wxGetAccessToken(WechatAccountMeta accountMeta);

    String wxGetJsTicket(WechatAccountMeta accountMeta);

    String[] wxGetCallbackIp(WechatAccountMeta accountMeta);

    // ----------

    WechatQRCodeResult wxCreateQRCode(WechatAccountMeta accountMeta, int sceneId, int expireSeconds);

    WechatQRCodeResult wxCreateQRCodeLimit(WechatAccountMeta accountMeta, int sceneId);

    WechatQRCodeResult wxCreateQRCodeLimitStr(WechatAccountMeta accountMeta, String sceneStr);

    String wxShowQRCode(String ticket);

    WechatShortUrlResult wxShortUrl(WechatAccountMeta accountMeta, String longUrl);

    WechatAutoreplyResult wxAutoreplyGetInfo(WechatAccountMeta accountMeta);

    // ----------

    String wxOAuthGetCodeUrl(WechatAccountMeta accountMeta, boolean baseScope, String state, String redirectUri);

    WechatSnsAccessTokenResult wxOAuthGetToken(WechatAccountMeta accountMeta, String code);

    WechatSnsAccessTokenResult wxOAuthRefreshToken(WechatAccountMeta accountMeta, String refreshToken);

    WechatSnsUserResult wxOAuthGetUserInfo(String oauthAccessToken, String openid);

    WechatSnsUserResult wxOAuthGetUserInfo(String oauthAccessToken, String openid, WxLangType lang);

    WechatResult wxOAuthAuthAccessToken(String oauthAccessToken, String openid);

    // ----------

    WechatUserListResult wxUserGetList(WechatAccountMeta accountMeta, String nextOpenId);

    WechatUserResult wxUserGetInfo(WechatAccountMeta accountMeta, String openid);

    WechatUserResult wxUserGetInfo(WechatAccountMeta accountMeta, String openid, WxLangType lang);

    WechatUserResult wxUserGetInfoBatch(WechatAccountMeta accountMeta, List<String> openids);

    WechatUserResult wxUserGetInfoBatch(WechatAccountMeta accountMeta, List<String> openids, WxLangType lang);

    WechatResult wxUserUpdateRemark(WechatAccountMeta accountMeta, String openid, String remark);

    WechatUserTagsResult wxUserGetTags(WechatAccountMeta accountMeta, String openid);

    // ----------

    WechatTagResult wxTagsCreate(WechatAccountMeta accountMeta, String tagName);

    WechatTagResult[] wxTagsGetList(WechatAccountMeta accountMeta);

    WechatResult wxTagsUpdate(WechatAccountMeta accountMeta, int id, String tagName);

    WechatResult wxTagsDelete(WechatAccountMeta accountMeta, int id);

    WechatUserListResult wxTagsGetUserList(WechatAccountMeta accountMeta, int tagId, String nextOpenId);

    WechatResult wxTagsUpdateUser(WechatAccountMeta accountMeta, int tagId, List<String> openids);

    WechatResult wxTagsDeleteUser(WechatAccountMeta accountMeta, int tagId, List<String> openids);

    // ----------

    WechatTagResult wxGroupCreate(WechatAccountMeta accountMeta, String groupName);

    WechatTagResult[] wxGroupGetList(WechatAccountMeta accountMeta);

    WechatUserTagsResult wxGroupGetId(WechatAccountMeta accountMeta, String openid);

    WechatResult wxGroupUpdate(WechatAccountMeta accountMeta, String id, String name);

    WechatResult wxGroupsDelete(WechatAccountMeta accountMeta, int groupId);

    WechatResult wxGroupMembersUpdate(WechatAccountMeta accountMeta, String openid, String to_groupid);

    WechatResult wxGroupMembersUpdateBatch(WechatAccountMeta accountMeta, List<String> openids, String toGroupid);

    // ----------

    WechatResult wxMenuCreate(WechatAccountMeta accountMeta, WechatMenu menu);

    WechatMenu wxMenuGet(WechatAccountMeta accountMeta);

    WechatResult wxMenuDelete(WechatAccountMeta accountMeta);

    // --------

    void wxMediaGetFile(WechatAccountMeta accountMeta, String mediaId, IFileHandler fileHandler);

    WechatMediaUploadResult wxMediaUploadFile(WechatAccountMeta accountMeta, MediaType type, File file);

    WechatMediaUploadResult wxMediaUploadFile(WechatAccountMeta accountMeta, MediaType type, ContentBody file);

    WechatMediaUploadResult wxMediaUploadImage(WechatAccountMeta accountMeta, File file);

    WechatMediaUploadResult wxMediaUploadImage(WechatAccountMeta accountMeta, ContentBody file);

    WechatMediaUploadResult wxMediaUploadNews(WechatAccountMeta accountMeta, List<WechatMassArticle> articles);

    WechatMediaUploadResult wxMediaUploadVideo(WechatAccountMeta accountMeta, WechatMassVideo video);

    // --------

    WechatResult wxMessageCustomSend(WechatAccountMeta accountMeta, OutMessage message);

    WechatMsgSendResult wxMessageTemplateSend(WechatAccountMeta accountMeta, TemplateMessage message);

    // --------

    WechatMsgSendResult wxMassSendAll(WechatAccountMeta accountMeta, String groupOrTagId, boolean isTagId, boolean isToAll, MessageType msgType, String mediaIdOrContent);

    WechatMsgSendResult wxMassSend(WechatAccountMeta accountMeta, List<String> openIds, MessageType msgType, String mediaIdOrContent);

    WechatMsgSendResult wxMassPreview(WechatAccountMeta accountMeta, String openId, MessageType msgType, String mediaIdOrContent);

    WechatResult wxMassDelete(WechatAccountMeta accountMeta, String msgId);

    WechatMsgSendResult wxMassGet(WechatAccountMeta accountMeta, String msgId);

    // ------

    WechatMediaUploadResult wxMaterialAddNews(WechatAccountMeta accountMeta, List<WechatMassArticle> articles);

    WechatMediaUploadResult wxMaterialAddNews(WechatAccountMeta accountMeta, MediaType type, File file);

    WechatMediaUploadResult wxMaterialAddNews(WechatAccountMeta accountMeta, MediaType type, ContentBody file);

    WechatMediaUploadResult wxMaterialAddVideo(WechatAccountMeta accountMeta, String title, String introduction, File file);

    WechatMediaUploadResult wxMaterialAddVideo(WechatAccountMeta accountMeta, String title, String introduction, ContentBody file);

    WechatResult wxMaterialDelete(WechatAccountMeta accountMeta, String mediaId);

    WechatMaterialResult wxMaterialBatchGet(WechatAccountMeta accountMeta, MediaType type, int offset, int count);

    WechatMaterialDetailResult wxMaterialNewsAndVideoGet(WechatAccountMeta accountMeta, String mediaId);

    void wxMaterialFileGet(WechatAccountMeta accountMeta, String mediaId, IFileHandler fileHandler);

    WechatResult wxMaterialUpdateNews(WechatAccountMeta accountMeta, String mediaId, int index, WechatMassArticle article);

    WechatMaterialCountResult wxMaterialCount(WechatAccountMeta accountMeta);

    // ------


    interface WX_API {
        // base
        String WX_ACCESS_TOKEN = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential";
        String WX_JSAPI_TICKET = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?type=jsapi&access_token=";
        String WX_GET_CALLBACK_IP = "https://api.weixin.qq.com/cgi-bin/getcallbackip?access_token=";
        // qrcode
        String QRCODE_CREATE = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=";
        String QRCODE_SHOW = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=";
        //
        String SHORT_URL = "https://api.weixin.qq.com/cgi-bin/shorturl?access_token=";
        //
        String AUTOREPLY_GET_INFO = "https://api.weixin.qq.com/cgi-bin/get_current_autoreply_info?access_token=";
        // oauth
        String OAUTH_GET_CODE = "https://open.weixin.qq.com/connect/oauth2/authorize?";
        String OAUTH_ACCESS_TOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token";
        String OAUTH_REFRESH_TOKEN = "https://api.weixin.qq.com/sns/oauth2/refresh_token";
        String OAUTH_USER_INFO = "https://api.weixin.qq.com/sns/userinfo?access_token=";
        String OAUTH_AUTH_ACCESS_TOKEN = "https://api.weixin.qq.com/sns/auth?access_token=";
        // user
        String USER_GET = "https://api.weixin.qq.com/cgi-bin/user/get";
        String USER_INFO = "https://api.weixin.qq.com/cgi-bin/user/info";
        String USER_INFO_BATCH = "https://api.weixin.qq.com/cgi-bin/user/info/batchget?access_token=";
        String USER_UPDATE_REMARK = "https://api.weixin.qq.com/cgi-bin/user/info/updateremark?access_token=";
        // tags
        String TAGS_CREATE = "https://api.weixin.qq.com/cgi-bin/tags/create?access_token=";
        String TAGS_GET = "https://api.weixin.qq.com/cgi-bin/tags/get?access_token=";
        String TAGS_GET_ID = "https://api.weixin.qq.com/cgi-bin/tags/getidlist?access_token=";
        String TAGS_UPDATE = "https://api.weixin.qq.com/cgi-bin/tags/update?access_token=";
        String TAGS_DELETE = "https://api.weixin.qq.com/cgi-bin/tags/delete?access_token=";
        String TAGS_GET_USER = "https://api.weixin.qq.com/cgi-bin/user/tag/get?access_token=";
        String TAGS_UPDATE_USER = "https://api.weixin.qq.com/cgi-bin/tags/members/batchtagging?access_token=";
        String TAGS_DELETE_USER = "https://api.weixin.qq.com/cgi-bin/tags/members/batchuntagging?access_token=";
        // groups
        String GROUP_CREATE = "https://api.weixin.qq.com/cgi-bin/groups/create?access_token=";
        String GROUP_GET = "https://api.weixin.qq.com/cgi-bin/groups/get?access_token=";
        String GROUP_GET_ID = "https://api.weixin.qq.com/cgi-bin/groups/getid?access_token=";
        String GROUP_UPDATE = "https://api.weixin.qq.com/cgi-bin/groups/update?access_token=";
        String GROUP_DELETE = "https://api.weixin.qq.com/cgi-bin/groups/delete?access_token=";
        String GROUP_MEMBERS_UPDATE = "https://api.weixin.qq.com/cgi-bin/groups/members/update?access_token=";
        String GROUP_MEMBERS_UPDATE_BATCH = "https://api.weixin.qq.com/cgi-bin/groups/members/batchupdate?access_token=";
        // menus
        String MENU_CREATE = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=";
        String MENU_GET = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=";
        String MENU_DELETE = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=";
        String MENU_SELF_INFO = "https://api.weixin.qq.com/cgi-bin/get_current_selfmenu_info?access_token=";
        String MENU_ADD_CONDITIONAL = "https://api.weixin.qq.com/cgi-bin/menu/addconditional?access_token=";
        String MENU_DEL_CONDITIONAL = "https://api.weixin.qq.com/cgi-bin/menu/delconditional?access_token=";
        String MENU_TRY_MATCH = "https://api.weixin.qq.com/cgi-bin/menu/trymatch?access_token=";
        // media
        String MEDIA_GET = "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=";
        String MEDIA_UPLOAD = "http://file.api.weixin.qq.com/cgi-bin/media/upload?access_token=";
        String MEDIA_UPLOAD_IMAGE = "https://api.weixin.qq.com/cgi-bin/media/uploadimg?access_token=";
        String MEDIA_UPLOAD_NEWS = "https://api.weixin.qq.com/cgi-bin/media/uploadnews?access_token=";
        String MEDIA_UPLOAD_VIDEO = "http://file.api.weixin.qq.com/cgi-bin/media/uploadvideo?access_token=";
        // message
        String MESSAGE_CUSTOM_SEND = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=";
        String MESSAGE_TEMPLATE_SEND = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";
        // mass
        String MASS_SEND_ALL = "https://api.weixin.qq.com/cgi-bin/message/mass/sendall?access_token=";
        String MASS_SEND_BY_OPENID = "https://api.weixin.qq.com/cgi-bin/message/mass/send?access_token=";
        String MASS_PREVIEW = "https://api.weixin.qq.com/cgi-bin/message/mass/preview?access_token=";
        String MASS_DELETE = "https://api.weixin.qq.com//cgi-bin/message/mass/delete?access_token=";
        String MASS_GET = "https://api.weixin.qq.com/cgi-bin/message/mass/get?access_token=";
        // material
        String MATERIAL_ADD_NEWS = "https://api.weixin.qq.com/cgi-bin/material/add_news?access_token=";
        String MATERIAL_ADD_MATERIAL = "https://api.weixin.qq.com/cgi-bin/material/add_material?access_token=";
        String MATERIAL_GET_MATERIAL = "https://api.weixin.qq.com/cgi-bin/material/get_material?access_token=";
        String MATERIAL_DEL_MATERIAL = "https://api.weixin.qq.com/cgi-bin/material/del_material?access_token=";
        String MATERIAL_UPDATE_NEWS = "https://api.weixin.qq.com/cgi-bin/material/update_news?access_token=";
        String MATERIAL_GET_COUNT = "https://api.weixin.qq.com/cgi-bin/material/get_materialcount?access_token=";
        String MATERIAL_BATCH_GET = "https://api.weixin.qq.com/cgi-bin/material/batchget_material?access_token=";
    }

    interface WX_PAY_API {
        // 统一支付接口
        String PAY_UNIFIED_ORDER = "https://api.mch.weixin.qq.com/pay/unifiedorder";

        // 订单查询接口
        String PAY_ORDER_QUERY = "https://api.mch.weixin.qq.com/pay/orderquery";

        // 关闭订单接口
        String PAY_CLOSE_ORDER = "https://api.mch.weixin.qq.com/pay/closeorder";

        // 退款申请接口
        String PAY_REFUND = "https://api.mch.weixin.qq.com/secapi/pay/refund";

        // 退款查询接口
        String PAY_REFUND_QUERY = "https://api.mch.weixin.qq.com/pay/refundquery";

        // 对帐单接口
        String PAY_DOWNLOAD_BILL = "https://api.mch.weixin.qq.com/pay/downloadbill";

        // 短链接转换接口
        String PAY_SHORT_URL = "https://api.mch.weixin.qq.com/tools/shorturl";

        //////////

        // 现金红包发放接口
        String REDPACK_SEND_URL = "https://api.mch.weixin.qq.com/mmpaymkttransfers/sendredpack";

        // 裂变红包发放接口
        String REDPACK_SEND_GROUP_URL = " https://api.mch.weixin.qq.com/mmpaymkttransfers/sendgroupredpack";

        // 微信红包查询接口
        String REDPACK_GET_URL = "https://api.mch.weixin.qq.com/mmpaymkttransfers/gethbinfo";

        //////////

        // 企业付款接口
        String MCH_PAY_URL = "https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers";

        // 企业付款查询接口
        String MCH_PAY_GET_URL = "https://api.mch.weixin.qq.com/mmpaymkttransfers/gettransferinfo";
    }

    enum WxLangType {
        zh_CN, zh_TW, en
    }

    /**
     * 微信事件枚举
     */
    enum EventType {
        SUBSCRIBE("subscribe"),
        UNSUBSCRIBE("unsubscribe"),
        MENU_CLICK("click"),
        MENU_VIEW("view"),
        MENU_SCANCODE_PUSH("scancode_push"),
        MENU_SCANCODE_WAITMSG("scancode_waitmsg"),
        MENU_PIC_SYSPHOTO("pic_sysphoto"),
        MENU_PIC_PHOTO_OR_ALBUM("pic_photo_or_album"),
        MENU_PIC_WEIXIN("pic_weixin"),
        MENU_LOCATION_SELECT("location_select"),
        MENU_MEDIA_ID("media_id"),
        LOCATION("LOCATION"),
        SCAN("SCAN"),
        MASS_SEND_JOB_FINISH("MASSSENDJOBFINISH"),
        TEMPLATE_SEND_JOB_FINISH("TEMPLATESENDJOBFINISH");

        private String type;

        EventType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }

        @Override
        public String toString() {
            return type;
        }
    }

    /**
     * 微信消息类型枚举
     */
    enum MessageType {
        TEXT("text"),
        IMAGE("image"),
        LINK("link"),
        VOICE("voice"),
        VIDEO("video"),
        SHORT_VIDEO("shortvideo"),
        MUSIC("music"),
        NEWS("news"),
        LOCATION("location"),
        EVENT("event");

        private String type;

        MessageType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }

        @Override
        public String toString() {
            return type;
        }
    }

    enum MediaType {
        IMAGE("image"),
        VOICE("voice"),
        VIDEO("video"),
        SHORT_VIDEO("shortvideo"),
        THUMB("thumb"),
        NEWS("news");

        private String type;

        MediaType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }

        @Override
        public String toString() {
            return type;
        }
    }
}
