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
package net.ymate.module.wechat.base;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 刘镇 (suninformation@163.com) on 16/5/28 上午2:19
 * @version 1.0
 */
public class WechatResult implements Serializable {

    private static final Map<Integer, String> __ERROR_CODES = new HashMap<Integer, String>();

    static {
        __ERROR_CODES.put(-1, "系统繁忙");
        __ERROR_CODES.put(0, "请求成功");
        __ERROR_CODES.put(40001, "获取access_token时AppSecret错误，或者access_token无效");
        __ERROR_CODES.put(40002, "不合法的凭证类型");
        __ERROR_CODES.put(40003, "不合法的OpenID");
        __ERROR_CODES.put(40004, "不合法的媒体文件类型");
        __ERROR_CODES.put(40005, "不合法的文件类型");
        __ERROR_CODES.put(40006, "不合法的文件大小");
        __ERROR_CODES.put(40007, "不合法的媒体文件id");
        __ERROR_CODES.put(40008, "不合法的消息类型");
        __ERROR_CODES.put(40009, "不合法的图片文件大小");
        __ERROR_CODES.put(40010, "不合法的语音文件大小");
        __ERROR_CODES.put(40011, "不合法的视频文件大小");
        __ERROR_CODES.put(40012, "不合法的缩略图文件大小");
        __ERROR_CODES.put(40013, "不合法的APPID");
        __ERROR_CODES.put(40014, "不合法的access_token");
        __ERROR_CODES.put(40015, "不合法的菜单类型");
        __ERROR_CODES.put(40016, "不合法的按钮个数");
        __ERROR_CODES.put(40017, "不合法的按钮个数");
        __ERROR_CODES.put(40018, "不合法的按钮名字长度");
        __ERROR_CODES.put(40019, "不合法的按钮KEY长度");
        __ERROR_CODES.put(40020, "不合法的按钮URL长度");
        __ERROR_CODES.put(40021, "不合法的菜单版本号");
        __ERROR_CODES.put(40022, "不合法的子菜单级数");
        __ERROR_CODES.put(40023, "不合法的子菜单按钮个数");
        __ERROR_CODES.put(40024, "不合法的子菜单按钮类型");
        __ERROR_CODES.put(40025, "不合法的子菜单按钮名字长度");
        __ERROR_CODES.put(40026, "不合法的子菜单按钮KEY长度");
        __ERROR_CODES.put(40027, "不合法的子菜单按钮URL长度");
        __ERROR_CODES.put(40028, "不合法的自定义菜单使用用户");
        __ERROR_CODES.put(40029, "不合法的oauth_code");
        __ERROR_CODES.put(40030, "不合法的refresh_token");
        __ERROR_CODES.put(40031, "不合法的openid列表");
        __ERROR_CODES.put(40032, "不合法的openid列表长度");
        __ERROR_CODES.put(40033, "不合法的请求字符，不能包含\\uxxxx格式的字符");
        __ERROR_CODES.put(40035, "不合法的参数");
        __ERROR_CODES.put(40038, "不合法的请求格式");
        __ERROR_CODES.put(40039, "不合法的URL长度");
        __ERROR_CODES.put(40050, "不合法的分组id");
        __ERROR_CODES.put(40051, "分组名字不合法");
        __ERROR_CODES.put(41001, "缺少access_token参数");
        __ERROR_CODES.put(41002, "缺少appid参数");
        __ERROR_CODES.put(41003, "缺少refresh_token参数");
        __ERROR_CODES.put(41004, "缺少secret参数");
        __ERROR_CODES.put(41005, "缺少多媒体文件数据");
        __ERROR_CODES.put(41006, "缺少media_id参数");
        __ERROR_CODES.put(41007, "缺少子菜单数据");
        __ERROR_CODES.put(41008, "缺少oauth code");
        __ERROR_CODES.put(41009, "缺少openid");
        __ERROR_CODES.put(42001, "access_token超时");
        __ERROR_CODES.put(42002, "refresh_token超时");
        __ERROR_CODES.put(42003, "oauth_code超时");
        __ERROR_CODES.put(43001, "需要GET请求");
        __ERROR_CODES.put(43002, "需要POST请求");
        __ERROR_CODES.put(43003, "需要HTTPS请求");
        __ERROR_CODES.put(43004, "需要接收者关注");
        __ERROR_CODES.put(43005, "需要好友关系");
        __ERROR_CODES.put(44001, "多媒体文件为空");
        __ERROR_CODES.put(44002, "POST的数据包为空");
        __ERROR_CODES.put(44003, "图文消息内容为空");
        __ERROR_CODES.put(44004, "文本消息内容为空");
        __ERROR_CODES.put(45001, "多媒体文件大小超过限制");
        __ERROR_CODES.put(45002, "消息内容超过限制");
        __ERROR_CODES.put(45003, "标题字段超过限制");
        __ERROR_CODES.put(45004, "描述字段超过限制");
        __ERROR_CODES.put(45005, "链接字段超过限制");
        __ERROR_CODES.put(45006, "图片链接字段超过限制");
        __ERROR_CODES.put(45007, "语音播放时间超过限制");
        __ERROR_CODES.put(45008, "图文消息超过限制");
        __ERROR_CODES.put(45009, "接口调用超过限制");
        __ERROR_CODES.put(45010, "创建菜单个数超过限制");
        __ERROR_CODES.put(45015, "回复时间超过限制");
        __ERROR_CODES.put(45016, "系统分组，不允许修改");
        __ERROR_CODES.put(45017, "分组名字过长");
        __ERROR_CODES.put(45018, "分组数量超过上限");
        __ERROR_CODES.put(45056, "创建的标签数过多，请注意不能超过100个");
        __ERROR_CODES.put(45057, "该标签下粉丝数超过10w，不允许直接删除");
        __ERROR_CODES.put(45058, "不能修改0/1/2这三个系统默认保留的标签");
        __ERROR_CODES.put(45059, "有粉丝身上的标签数已经超过限制");
        __ERROR_CODES.put(45157, "标签名非法，请注意不能和其他标签重名");
        __ERROR_CODES.put(45158, "标签名长度超过30个字节");
        __ERROR_CODES.put(45159, "非法的tag_id");
        __ERROR_CODES.put(46001, "不存在媒体数据");
        __ERROR_CODES.put(46002, "不存在的菜单版本");
        __ERROR_CODES.put(46003, "不存在的菜单数据");
        __ERROR_CODES.put(46004, "不存在的用户");
        __ERROR_CODES.put(47001, "解析JSON/XML内容错误");
        __ERROR_CODES.put(48001, "api功能未授权");
        __ERROR_CODES.put(48004, "api接口被封禁，请登录mp.weixin.qq.com查看详情");
        __ERROR_CODES.put(50001, "用户未授权该api");
        __ERROR_CODES.put(50002, "用户受限，可能是违规后接口被封禁");
        __ERROR_CODES.put(61451, "参数错误(invalid parameter)");
        __ERROR_CODES.put(61452, "无效客服账号(invalid kf_account)");
        __ERROR_CODES.put(61453, "客服帐号已存在(kf_account exsited)");
        __ERROR_CODES.put(61454, "客服帐号名长度超过限制(仅允许10个英文字符，不包括@及@后的公众号的微信号)(invalid kf_acount length)");
        __ERROR_CODES.put(61455, "客服帐号名包含非法字符(仅允许英文+数字)(illegal character in kf_account)");
        __ERROR_CODES.put(61456, "客服帐号个数超过限制(10个客服账号)(kf_account count exceeded)");
        __ERROR_CODES.put(61457, "无效头像文件类型(invalid file type)");
        __ERROR_CODES.put(61450, "系统错误(system error)");
        __ERROR_CODES.put(61500, "日期格式错误");
        __ERROR_CODES.put(65301, "不存在此menuid对应的个性化菜单");
        __ERROR_CODES.put(65302, "没有相应的用户");
        __ERROR_CODES.put(65303, "没有默认菜单，不能创建个性化菜单");
        __ERROR_CODES.put(65304, "MatchRule信息为空");
        __ERROR_CODES.put(65305, "个性化菜单数量受限");
        __ERROR_CODES.put(65306, "不支持个性化菜单的帐号");
        __ERROR_CODES.put(65307, "个性化菜单信息为空");
        __ERROR_CODES.put(65308, "包含没有响应类型的button");
        __ERROR_CODES.put(65309, "个性化菜单开关处于关闭状态");
        __ERROR_CODES.put(65310, "填写了省份或城市信息，国家信息不能为空");
        __ERROR_CODES.put(65311, "填写了城市信息，省份信息不能为空");
        __ERROR_CODES.put(65312, "不合法的国家信息");
        __ERROR_CODES.put(65313, "不合法的省份信息");
        __ERROR_CODES.put(65314, "不合法的城市信息");
        __ERROR_CODES.put(65316, "该公众号的菜单设置了过多的域名外跳（最多跳转到3个域名的链接）");
        __ERROR_CODES.put(65317, "不合法的URL");
        __ERROR_CODES.put(9001001, "POST数据参数不合法");
        __ERROR_CODES.put(9001002, "远端服务不可用");
        __ERROR_CODES.put(9001003, "Ticket不合法");
        __ERROR_CODES.put(9001004, "获取摇周边用户信息失败");
        __ERROR_CODES.put(9001005, "获取商户信息失败");
        __ERROR_CODES.put(9001006, "获取OpenID失败");
        __ERROR_CODES.put(9001007, "上传文件缺失");
        __ERROR_CODES.put(9001008, "上传素材的文件类型不合法");
        __ERROR_CODES.put(9001009, "上传素材的文件尺寸不合法");
        __ERROR_CODES.put(9001010, "上传失败");
        __ERROR_CODES.put(9001020, "帐号不合法");
        __ERROR_CODES.put(9001021, "已有设备激活率低于50%，不能新增设备");
        __ERROR_CODES.put(9001022, "设备申请数不合法，必须为大于0的数字");
        __ERROR_CODES.put(9001023, "已存在审核中的设备ID申请");
        __ERROR_CODES.put(9001024, "一次查询设备ID数量不能超过50");
        __ERROR_CODES.put(9001025, "设备ID不合法");
        __ERROR_CODES.put(9001026, "页面ID不合法");
        __ERROR_CODES.put(9001027, "页面参数不合法");
        __ERROR_CODES.put(9001028, "一次删除页面ID数量不能超过10");
        __ERROR_CODES.put(9001029, "页面已应用在设备中，请先解除应用关系再删除");
        __ERROR_CODES.put(9001030, "一次查询页面ID数量不能超过50");
        __ERROR_CODES.put(9001031, "时间区间不合法");
        __ERROR_CODES.put(9001032, "保存设备与页面的绑定关系参数错误");
        __ERROR_CODES.put(9001033, "门店ID不合法");
        __ERROR_CODES.put(9001034, "设备备注信息过长");
        __ERROR_CODES.put(9001035, "设备申请参数不合法");
        __ERROR_CODES.put(9001036, "查询起始值begin不合法");
    }

    @JSONField(serialize = false)
    private JSONObject originalResult;

    @JSONField(name = "err_code")
    private int errCode;
    @JSONField(name = "err_msg")
    private String errMsg;

    public WechatResult(JSONObject result) {
        this.originalResult = result;
        if (result.containsKey("errcode")) {
            this.errCode = result.getIntValue("errcode");
            if (this.errCode != 0) {
                this.errMsg = result.getString("errmsg");
                if (StringUtils.isBlank(this.errMsg)) {
                    this.errMsg = __ERROR_CODES.get(this.errCode);
                }
            }
        }
    }

    @JSONField(serialize = false)
    public boolean isOK() {
        return this.errCode == 0;
    }

    public int getErrCode() {
        return errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public JSONObject getOriginalResult() {
        return originalResult;
    }
}
