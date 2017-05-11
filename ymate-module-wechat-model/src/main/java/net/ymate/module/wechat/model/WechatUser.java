package net.ymate.module.wechat.model;

import net.ymate.platform.core.beans.annotation.PropertyState;
import net.ymate.platform.persistence.annotation.Default;
import net.ymate.platform.persistence.annotation.Entity;
import net.ymate.platform.persistence.annotation.Id;
import net.ymate.platform.persistence.annotation.Property;
import net.ymate.platform.persistence.jdbc.support.BaseEntity;

/**
 * WechatUser generated By EntityGenerator on 2016/07/05 上午 02:06:09
 *
 * @author YMP
 * @version 1.0
 */
@Entity("wechat_user")
public class WechatUser extends BaseEntity<WechatUser, java.lang.String> {

    private static final long serialVersionUID = 1L;

    @Id
    @Property(name = "id", nullable = false, length = 32)
    @PropertyState(propertyName = "id")
    private java.lang.String id;

    @Property(name = "account_id", nullable = false, length = 32)
    @PropertyState(propertyName = "account_id")
    private java.lang.String accountId;

    @Property(name = "open_id", nullable = false, length = 256)
    @PropertyState(propertyName = "open_id")
    private java.lang.String openId;

    @Property(name = "union_id", length = 32)
    @PropertyState(propertyName = "union_id")
    private java.lang.String unionId;

    @Property(name = "nick_name", length = 32)
    @PropertyState(propertyName = "nick_name")
    private java.lang.String nickName;

    @Property(name = "gender", unsigned = true, length = 1)
    @Default("0")
    @PropertyState(propertyName = "gender")
    private java.lang.Integer gender;

    @Property(name = "head_img_url", length = 255)
    @PropertyState(propertyName = "head_img_url")
    private java.lang.String headImgUrl;

    @Property(name = "signature", length = 100)
    @PropertyState(propertyName = "signature")
    private java.lang.String signature;

    @Property(name = "country", length = 32)
    @PropertyState(propertyName = "country")
    private java.lang.String country;

    @Property(name = "province", length = 32)
    @PropertyState(propertyName = "province")
    private java.lang.String province;

    @Property(name = "city", length = 32)
    @PropertyState(propertyName = "city")
    private java.lang.String city;

    @Property(name = "language", length = 10)
    @PropertyState(propertyName = "language")
    private java.lang.String language;

    @Property(name = "remark", length = 100)
    @PropertyState(propertyName = "remark")
    private java.lang.String remark;

    @Property(name = "is_subscribe", unsigned = true, length = 1)
    @Default("0")
    @PropertyState(propertyName = "is_subscribe")
    private java.lang.Integer isSubscribe;

    @Property(name = "subscribe_time", length = 13)
    @Default("0")
    @PropertyState(propertyName = "subscribe_time")
    private java.lang.Long subscribeTime;

    @Property(name = "unsubscribe_time", length = 13)
    @Default("0")
    @PropertyState(propertyName = "unsubscribe_time")
    private java.lang.Long unsubscribeTime;

    @Property(name = "tag_id_list", length = 32)
    @PropertyState(propertyName = "tag_id_list")
    private java.lang.String tagIdList;

    @Property(name = "oauth_access_token", length = 512)
    @PropertyState(propertyName = "oauth_access_token")
    private java.lang.String oauthAccessToken;

    @Property(name = "oauth_refresh_token", length = 512)
    @PropertyState(propertyName = "oauth_refresh_token")
    private java.lang.String oauthRefreshToken;

    @Property(name = "oauth_expired_time", length = 13)
    @Default("0")
    @PropertyState(propertyName = "oauth_expired_time")
    private java.lang.Long oauthExpiredTime;

    @Property(name = "status", unsigned = true, length = 2)
    @Default("0")
    @PropertyState(propertyName = "status")
    private java.lang.Integer status;

    @Property(name = "site_id", nullable = false, length = 32)
    @PropertyState(propertyName = "site_id")
    private java.lang.String siteId;

    @Property(name = "oauth_scope", length = 100)
    @PropertyState(propertyName = "oauth_scope")
    private java.lang.String oauthScope;

    @Property(name = "last_active_time", length = 13)
    @Default("0")
    @PropertyState(propertyName = "last_active_time")
    private java.lang.Long lastActiveTime;

    @Property(name = "create_time", nullable = false, length = 13)
    @PropertyState(propertyName = "create_time")
    private java.lang.Long createTime;

    @Property(name = "last_modify_time", length = 13)
    @Default("0")
    @PropertyState(propertyName = "last_modify_time")
    private java.lang.Long lastModifyTime;

    /**
     * 构造器
     */
    public WechatUser() {
    }

    /**
     * 构造器
     *
     * @param id
     * @param accountId
     * @param openId
     * @param siteId
     * @param createTime
     */
    public WechatUser(java.lang.String id, java.lang.String accountId, java.lang.String openId, java.lang.String siteId, java.lang.Long createTime) {
        this.id = id;
        this.accountId = accountId;
        this.openId = openId;
        this.siteId = siteId;
        this.createTime = createTime;
    }

    /**
     * 构造器
     *
     * @param id
     * @param accountId
     * @param openId
     * @param unionId
     * @param nickName
     * @param gender
     * @param headImgUrl
     * @param signature
     * @param country
     * @param province
     * @param city
     * @param language
     * @param remark
     * @param isSubscribe
     * @param subscribeTime
     * @param unsubscribeTime
     * @param tagIdList
     * @param oauthAccessToken
     * @param oauthRefreshToken
     * @param oauthExpiredTime
     * @param status
     * @param siteId
     * @param oauthScope
     * @param lastActiveTime
     * @param createTime
     * @param lastModifyTime
     */
    public WechatUser(java.lang.String id, java.lang.String accountId, java.lang.String openId, java.lang.String unionId, java.lang.String nickName, java.lang.Integer gender, java.lang.String headImgUrl, java.lang.String signature, java.lang.String country, java.lang.String province, java.lang.String city, java.lang.String language, java.lang.String remark, java.lang.Integer isSubscribe, java.lang.Long subscribeTime, java.lang.Long unsubscribeTime, java.lang.String tagIdList, java.lang.String oauthAccessToken, java.lang.String oauthRefreshToken, java.lang.Long oauthExpiredTime, java.lang.Integer status, java.lang.String siteId, java.lang.String oauthScope, java.lang.Long lastActiveTime, java.lang.Long createTime, java.lang.Long lastModifyTime) {
        this.id = id;
        this.accountId = accountId;
        this.openId = openId;
        this.unionId = unionId;
        this.nickName = nickName;
        this.gender = gender;
        this.headImgUrl = headImgUrl;
        this.signature = signature;
        this.country = country;
        this.province = province;
        this.city = city;
        this.language = language;
        this.remark = remark;
        this.isSubscribe = isSubscribe;
        this.subscribeTime = subscribeTime;
        this.unsubscribeTime = unsubscribeTime;
        this.tagIdList = tagIdList;
        this.oauthAccessToken = oauthAccessToken;
        this.oauthRefreshToken = oauthRefreshToken;
        this.oauthExpiredTime = oauthExpiredTime;
        this.status = status;
        this.siteId = siteId;
        this.oauthScope = oauthScope;
        this.lastActiveTime = lastActiveTime;
        this.createTime = createTime;
        this.lastModifyTime = lastModifyTime;
    }

    public java.lang.String getId() {
        return id;
    }

    public void setId(java.lang.String id) {
        this.id = id;
    }


    /**
     * @return the accountId
     */
    public java.lang.String getAccountId() {
        return accountId;
    }

    /**
     * @param accountId the accountId to set
     */
    public void setAccountId(java.lang.String accountId) {
        this.accountId = accountId;
    }

    /**
     * @return the openId
     */
    public java.lang.String getOpenId() {
        return openId;
    }

    /**
     * @param openId the openId to set
     */
    public void setOpenId(java.lang.String openId) {
        this.openId = openId;
    }

    /**
     * @return the unionId
     */
    public java.lang.String getUnionId() {
        return unionId;
    }

    /**
     * @param unionId the unionId to set
     */
    public void setUnionId(java.lang.String unionId) {
        this.unionId = unionId;
    }

    /**
     * @return the nickName
     */
    public java.lang.String getNickName() {
        return nickName;
    }

    /**
     * @param nickName the nickName to set
     */
    public void setNickName(java.lang.String nickName) {
        this.nickName = nickName;
    }

    /**
     * @return the gender
     */
    public java.lang.Integer getGender() {
        return gender;
    }

    /**
     * @param gender the gender to set
     */
    public void setGender(java.lang.Integer gender) {
        this.gender = gender;
    }

    /**
     * @return the headImgUrl
     */
    public java.lang.String getHeadImgUrl() {
        return headImgUrl;
    }

    /**
     * @param headImgUrl the headImgUrl to set
     */
    public void setHeadImgUrl(java.lang.String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    /**
     * @return the signature
     */
    public java.lang.String getSignature() {
        return signature;
    }

    /**
     * @param signature the signature to set
     */
    public void setSignature(java.lang.String signature) {
        this.signature = signature;
    }

    /**
     * @return the country
     */
    public java.lang.String getCountry() {
        return country;
    }

    /**
     * @param country the country to set
     */
    public void setCountry(java.lang.String country) {
        this.country = country;
    }

    /**
     * @return the province
     */
    public java.lang.String getProvince() {
        return province;
    }

    /**
     * @param province the province to set
     */
    public void setProvince(java.lang.String province) {
        this.province = province;
    }

    /**
     * @return the city
     */
    public java.lang.String getCity() {
        return city;
    }

    /**
     * @param city the city to set
     */
    public void setCity(java.lang.String city) {
        this.city = city;
    }

    /**
     * @return the language
     */
    public java.lang.String getLanguage() {
        return language;
    }

    /**
     * @param language the language to set
     */
    public void setLanguage(java.lang.String language) {
        this.language = language;
    }

    /**
     * @return the remark
     */
    public java.lang.String getRemark() {
        return remark;
    }

    /**
     * @param remark the remark to set
     */
    public void setRemark(java.lang.String remark) {
        this.remark = remark;
    }

    /**
     * @return the isSubscribe
     */
    public java.lang.Integer getIsSubscribe() {
        return isSubscribe;
    }

    /**
     * @param isSubscribe the isSubscribe to set
     */
    public void setIsSubscribe(java.lang.Integer isSubscribe) {
        this.isSubscribe = isSubscribe;
    }

    /**
     * @return the subscribeTime
     */
    public java.lang.Long getSubscribeTime() {
        return subscribeTime;
    }

    /**
     * @param subscribeTime the subscribeTime to set
     */
    public void setSubscribeTime(java.lang.Long subscribeTime) {
        this.subscribeTime = subscribeTime;
    }

    /**
     * @return the unsubscribeTime
     */
    public java.lang.Long getUnsubscribeTime() {
        return unsubscribeTime;
    }

    /**
     * @param unsubscribeTime the unsubscribeTime to set
     */
    public void setUnsubscribeTime(java.lang.Long unsubscribeTime) {
        this.unsubscribeTime = unsubscribeTime;
    }

    /**
     * @return the tagIdList
     */
    public java.lang.String getTagIdList() {
        return tagIdList;
    }

    /**
     * @param tagIdList the tagIdList to set
     */
    public void setTagIdList(java.lang.String tagIdList) {
        this.tagIdList = tagIdList;
    }

    /**
     * @return the oauthAccessToken
     */
    public java.lang.String getOauthAccessToken() {
        return oauthAccessToken;
    }

    /**
     * @param oauthAccessToken the oauthAccessToken to set
     */
    public void setOauthAccessToken(java.lang.String oauthAccessToken) {
        this.oauthAccessToken = oauthAccessToken;
    }

    /**
     * @return the oauthRefreshToken
     */
    public java.lang.String getOauthRefreshToken() {
        return oauthRefreshToken;
    }

    /**
     * @param oauthRefreshToken the oauthRefreshToken to set
     */
    public void setOauthRefreshToken(java.lang.String oauthRefreshToken) {
        this.oauthRefreshToken = oauthRefreshToken;
    }

    /**
     * @return the oauthExpiredTime
     */
    public java.lang.Long getOauthExpiredTime() {
        return oauthExpiredTime;
    }

    /**
     * @param oauthExpiredTime the oauthExpiredTime to set
     */
    public void setOauthExpiredTime(java.lang.Long oauthExpiredTime) {
        this.oauthExpiredTime = oauthExpiredTime;
    }

    /**
     * @return the status
     */
    public java.lang.Integer getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(java.lang.Integer status) {
        this.status = status;
    }

    /**
     * @return the siteId
     */
    public java.lang.String getSiteId() {
        return siteId;
    }

    /**
     * @param siteId the siteId to set
     */
    public void setSiteId(java.lang.String siteId) {
        this.siteId = siteId;
    }

    /**
     * @return the oauthScope
     */
    public java.lang.String getOauthScope() {
        return oauthScope;
    }

    /**
     * @param oauthScope the oauthScope to set
     */
    public void setOauthScope(java.lang.String oauthScope) {
        this.oauthScope = oauthScope;
    }

    /**
     * @return the lastActiveTime
     */
    public java.lang.Long getLastActiveTime() {
        return lastActiveTime;
    }

    /**
     * @param lastActiveTime the lastActiveTime to set
     */
    public void setLastActiveTime(java.lang.Long lastActiveTime) {
        this.lastActiveTime = lastActiveTime;
    }

    /**
     * @return the createTime
     */
    public java.lang.Long getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime the createTime to set
     */
    public void setCreateTime(java.lang.Long createTime) {
        this.createTime = createTime;
    }

    /**
     * @return the lastModifyTime
     */
    public java.lang.Long getLastModifyTime() {
        return lastModifyTime;
    }

    /**
     * @param lastModifyTime the lastModifyTime to set
     */
    public void setLastModifyTime(java.lang.Long lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }


    //
    // Chain
    //

    public static WechatUserBuilder builder() {
        return new WechatUserBuilder();
    }

    public WechatUserBuilder bind() {
        return new WechatUserBuilder(this);
    }

    public static class WechatUserBuilder {

        private WechatUser _model;

        public WechatUserBuilder() {
            _model = new WechatUser();
        }

        public WechatUserBuilder(WechatUser model) {
            _model = model;
        }

        public WechatUser build() {
            return _model;
        }

        public java.lang.String id() {
            return _model.getId();
        }

        public WechatUserBuilder id(java.lang.String id) {
            _model.setId(id);
            return this;
        }

        public java.lang.String accountId() {
            return _model.getAccountId();
        }

        public WechatUserBuilder accountId(java.lang.String accountId) {
            _model.setAccountId(accountId);
            return this;
        }

        public java.lang.String openId() {
            return _model.getOpenId();
        }

        public WechatUserBuilder openId(java.lang.String openId) {
            _model.setOpenId(openId);
            return this;
        }

        public java.lang.String unionId() {
            return _model.getUnionId();
        }

        public WechatUserBuilder unionId(java.lang.String unionId) {
            _model.setUnionId(unionId);
            return this;
        }

        public java.lang.String nickName() {
            return _model.getNickName();
        }

        public WechatUserBuilder nickName(java.lang.String nickName) {
            _model.setNickName(nickName);
            return this;
        }

        public java.lang.Integer gender() {
            return _model.getGender();
        }

        public WechatUserBuilder gender(java.lang.Integer gender) {
            _model.setGender(gender);
            return this;
        }

        public java.lang.String headImgUrl() {
            return _model.getHeadImgUrl();
        }

        public WechatUserBuilder headImgUrl(java.lang.String headImgUrl) {
            _model.setHeadImgUrl(headImgUrl);
            return this;
        }

        public java.lang.String signature() {
            return _model.getSignature();
        }

        public WechatUserBuilder signature(java.lang.String signature) {
            _model.setSignature(signature);
            return this;
        }

        public java.lang.String country() {
            return _model.getCountry();
        }

        public WechatUserBuilder country(java.lang.String country) {
            _model.setCountry(country);
            return this;
        }

        public java.lang.String province() {
            return _model.getProvince();
        }

        public WechatUserBuilder province(java.lang.String province) {
            _model.setProvince(province);
            return this;
        }

        public java.lang.String city() {
            return _model.getCity();
        }

        public WechatUserBuilder city(java.lang.String city) {
            _model.setCity(city);
            return this;
        }

        public java.lang.String language() {
            return _model.getLanguage();
        }

        public WechatUserBuilder language(java.lang.String language) {
            _model.setLanguage(language);
            return this;
        }

        public java.lang.String remark() {
            return _model.getRemark();
        }

        public WechatUserBuilder remark(java.lang.String remark) {
            _model.setRemark(remark);
            return this;
        }

        public java.lang.Integer isSubscribe() {
            return _model.getIsSubscribe();
        }

        public WechatUserBuilder isSubscribe(java.lang.Integer isSubscribe) {
            _model.setIsSubscribe(isSubscribe);
            return this;
        }

        public java.lang.Long subscribeTime() {
            return _model.getSubscribeTime();
        }

        public WechatUserBuilder subscribeTime(java.lang.Long subscribeTime) {
            _model.setSubscribeTime(subscribeTime);
            return this;
        }

        public java.lang.Long unsubscribeTime() {
            return _model.getUnsubscribeTime();
        }

        public WechatUserBuilder unsubscribeTime(java.lang.Long unsubscribeTime) {
            _model.setUnsubscribeTime(unsubscribeTime);
            return this;
        }

        public java.lang.String tagIdList() {
            return _model.getTagIdList();
        }

        public WechatUserBuilder tagIdList(java.lang.String tagIdList) {
            _model.setTagIdList(tagIdList);
            return this;
        }

        public java.lang.String oauthAccessToken() {
            return _model.getOauthAccessToken();
        }

        public WechatUserBuilder oauthAccessToken(java.lang.String oauthAccessToken) {
            _model.setOauthAccessToken(oauthAccessToken);
            return this;
        }

        public java.lang.String oauthRefreshToken() {
            return _model.getOauthRefreshToken();
        }

        public WechatUserBuilder oauthRefreshToken(java.lang.String oauthRefreshToken) {
            _model.setOauthRefreshToken(oauthRefreshToken);
            return this;
        }

        public java.lang.Long oauthExpiredTime() {
            return _model.getOauthExpiredTime();
        }

        public WechatUserBuilder oauthExpiredTime(java.lang.Long oauthExpiredTime) {
            _model.setOauthExpiredTime(oauthExpiredTime);
            return this;
        }

        public java.lang.Integer status() {
            return _model.getStatus();
        }

        public WechatUserBuilder status(java.lang.Integer status) {
            _model.setStatus(status);
            return this;
        }

        public java.lang.String siteId() {
            return _model.getSiteId();
        }

        public WechatUserBuilder siteId(java.lang.String siteId) {
            _model.setSiteId(siteId);
            return this;
        }

        public java.lang.String oauthScope() {
            return _model.getOauthScope();
        }

        public WechatUserBuilder oauthScope(java.lang.String oauthScope) {
            _model.setOauthScope(oauthScope);
            return this;
        }

        public java.lang.Long lastActiveTime() {
            return _model.getLastActiveTime();
        }

        public WechatUserBuilder lastActiveTime(java.lang.Long lastActiveTime) {
            _model.setLastActiveTime(lastActiveTime);
            return this;
        }

        public java.lang.Long createTime() {
            return _model.getCreateTime();
        }

        public WechatUserBuilder createTime(java.lang.Long createTime) {
            _model.setCreateTime(createTime);
            return this;
        }

        public java.lang.Long lastModifyTime() {
            return _model.getLastModifyTime();
        }

        public WechatUserBuilder lastModifyTime(java.lang.Long lastModifyTime) {
            _model.setLastModifyTime(lastModifyTime);
            return this;
        }

    }

    /**
     * WechatUser 字段常量表
     */
    public class FIELDS {
        public static final String ID = "id";
        public static final String ACCOUNT_ID = "account_id";
        public static final String OPEN_ID = "open_id";
        public static final String UNION_ID = "union_id";
        public static final String NICK_NAME = "nick_name";
        public static final String GENDER = "gender";
        public static final String HEAD_IMG_URL = "head_img_url";
        public static final String SIGNATURE = "signature";
        public static final String COUNTRY = "country";
        public static final String PROVINCE = "province";
        public static final String CITY = "city";
        public static final String LANGUAGE = "language";
        public static final String REMARK = "remark";
        public static final String IS_SUBSCRIBE = "is_subscribe";
        public static final String SUBSCRIBE_TIME = "subscribe_time";
        public static final String UNSUBSCRIBE_TIME = "unsubscribe_time";
        public static final String TAG_ID_LIST = "tag_id_list";
        public static final String OAUTH_ACCESS_TOKEN = "oauth_access_token";
        public static final String OAUTH_REFRESH_TOKEN = "oauth_refresh_token";
        public static final String OAUTH_EXPIRED_TIME = "oauth_expired_time";
        public static final String STATUS = "status";
        public static final String SITE_ID = "site_id";
        public static final String OAUTH_SCOPE = "oauth_scope";
        public static final String LAST_ACTIVE_TIME = "last_active_time";
        public static final String CREATE_TIME = "create_time";
        public static final String LAST_MODIFY_TIME = "last_modify_time";
    }

    public static final String TABLE_NAME = "wechat_user";

}
