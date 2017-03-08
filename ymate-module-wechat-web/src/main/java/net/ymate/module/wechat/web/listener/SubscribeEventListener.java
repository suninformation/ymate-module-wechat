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
package net.ymate.module.wechat.web.listener;

import net.ymate.module.wechat.IWechat;
import net.ymate.module.wechat.base.WechatUserResult;
import net.ymate.module.wechat.message.event.SubscribeEvent;
import net.ymate.module.wechat.model.WechatUser;
import net.ymate.module.wechat.web.WechatEvent;
import net.ymate.platform.core.event.IEventListener;
import net.ymate.platform.core.util.RuntimeUtils;
import net.ymate.platform.persistence.Fields;
import net.ymate.platform.persistence.jdbc.query.IDBLocker;
import net.ymate.platform.persistence.jdbc.transaction.ITrade;
import net.ymate.platform.persistence.jdbc.transaction.Transactions;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author 刘镇 (suninformation@163.com) on 16/7/11 上午1:46
 * @version 1.0
 */
public class SubscribeEventListener implements IEventListener<WechatEvent> {

    private static final Log _LOG = LogFactory.getLog(SubscribeEventListener.class);

    public boolean handle(final WechatEvent context) {
        switch (context.getEventName()) {
            case SUBSCRIBE:
                final SubscribeEvent _subscribe = (SubscribeEvent) context.getSource();
                try {
                    Transactions.execute(new ITrade() {
                        public void deal() throws Throwable {
                            String _wxUid = DigestUtils.md5Hex(_subscribe.getFromUserName());
                            WechatUser _wxUser = WechatUser.builder().id(_wxUid).build().load(Fields.create(WechatUser.FIELDS.ID), IDBLocker.MYSQL);
                            long _currentTime = System.currentTimeMillis();
                            boolean _isNew = true;
                            Fields _fields = Fields.create();
                            if (_wxUser != null) {
                                _isNew = false;
                            } else {
                                _wxUser = new WechatUser();
                                _wxUser.setId(_wxUid);
                                _wxUser.setOpenId(_subscribe.getFromUserName());
                                _wxUser.setAccountId(_subscribe.getToUserName());
                                _wxUser.setSiteId((String) context.getParamExtend("site_id"));
                                //
                                _fields.add(Fields.create(WechatUser.FIELDS.ID, WechatUser.FIELDS.OPEN_ID, WechatUser.FIELDS.ACCOUNT_ID, WechatUser.FIELDS.SITE_ID));
                            }
                            //
                            IWechat _wechat = (IWechat) context.getParamExtend(IWechat.class.getName());
                            WechatUserResult _userinfo = _wechat.wxUserGetInfo(_wechat.getAccountById(_subscribe.getToUserName()), _subscribe.getFromUserName());
                            if (_userinfo != null && _userinfo.isOK()) {
                                _wxUser.setNickName(_userinfo.getNickname());
                                _wxUser.setHeadImgUrl(_userinfo.getHeadImgUrl());
                                _wxUser.setGender(_userinfo.getSex());
                                _wxUser.setCountry(_userinfo.getCountry());
                                _wxUser.setProvince(_userinfo.getProvince());
                                _wxUser.setCity(_userinfo.getCity());
                                _wxUser.setUnionId(_userinfo.getUnionId());
                                _wxUser.setIsSubscribe(1);
                                _wxUser.setUnsubscribeTime(0L);
                                _wxUser.setLanguage(_userinfo.getLanguage());
                                _wxUser.setRemark(_userinfo.getRemark());
                                //
                                _fields.add(Fields.create(WechatUser.FIELDS.NICK_NAME,
                                        WechatUser.FIELDS.HEAD_IMG_URL,
                                        WechatUser.FIELDS.GENDER,
                                        WechatUser.FIELDS.COUNTRY,
                                        WechatUser.FIELDS.PROVINCE,
                                        WechatUser.FIELDS.CITY,
                                        WechatUser.FIELDS.UNION_ID,
                                        WechatUser.FIELDS.IS_SUBSCRIBE,
                                        WechatUser.FIELDS.UNSUBSCRIBE_TIME,
                                        WechatUser.FIELDS.LANGUAGE));
                                if (_isNew) {
                                    _wxUser.setSubscribeTime(_currentTime);
                                    _fields.add(WechatUser.FIELDS.SUBSCRIBE_TIME);
                                }
                                //
                                if (_userinfo.getTagidList() != null && _userinfo.getTagidList().length > 0) {
                                    _wxUser.setTagIdList(StringUtils.join(_userinfo.getTagidList(), ","));
                                    //
                                    _fields.add(WechatUser.FIELDS.TAG_ID_LIST);
                                }
                            }
                            //
                            _wxUser.setLastActiveTime(_currentTime);
                            _wxUser.setLastModifyTime(_currentTime);
                            _fields.add(WechatUser.FIELDS.LAST_ACTIVE_TIME).add(WechatUser.FIELDS.LAST_MODIFY_TIME);
                            if (_isNew) {
                                _wxUser.save(_fields);
                            } else {
                                _wxUser.update(_fields);
                            }
                        }
                    });
                } catch (Exception e) {
                    _LOG.warn("", RuntimeUtils.unwrapThrow(e));
                }
                break;
        }
        return false;
    }
}
