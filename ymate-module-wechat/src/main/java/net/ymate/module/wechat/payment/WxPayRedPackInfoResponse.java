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
package net.ymate.module.wechat.payment;

import net.ymate.module.wechat.payment.base.WxPayBaseResponse;
import net.ymate.platform.core.lang.BlurObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author 刘镇 (suninformation@163.com) on 16/6/27 上午12:58
 * @version 1.0
 */
public class WxPayRedPackInfoResponse extends WxPayBaseResponse {

    /**
     * 商户订单号
     */
    private String mchBillNo;

    /**
     * 红包单号
     */
    private String detailId;

    /**
     * 红包状态
     */
    private String status;

    /**
     * 发放类型
     */
    private String sendType;

    /**
     * 红包类型
     */
    private String hbType;

    /**
     * 红包个数
     */
    private Integer totalNum;

    /**
     * 红包金额
     */
    private Integer totalAmount;

    /**
     * 失败原因
     */
    private String reason;

    /**
     * 红包发送时间
     */
    private String sendTime;

    /**
     * 红包退款时间
     */
    private String refundTime;

    /**
     * 红包退款金额
     */
    private Integer refundAmount;

    /**
     * 祝福语
     */
    private String wishing;

    /**
     * 活动描述
     */
    private String remark;

    /**
     * 活动名称
     */
    private String actName;

    /**
     * 裂变红包领取列表
     */
    private List<HbInfo> hbList;

    public static WxPayRedPackInfoResponse bind(String protocol) throws Exception {
        return new WxPayRedPackInfoResponse(protocol);
    }

    public WxPayRedPackInfoResponse(String protocol) throws Exception {
        super(protocol);
        this.hbList = new ArrayList<HbInfo>();
        this.mchBillNo = BlurObject.bind(this.getResponseParams().get("mch_billno")).toStringValue();
        this.detailId = BlurObject.bind(this.getResponseParams().get("detail_id")).toStringValue();
        this.status = BlurObject.bind(this.getResponseParams().get("status")).toStringValue();
        this.sendType = BlurObject.bind(this.getResponseParams().get("send_type")).toStringValue();
        this.hbType = BlurObject.bind(this.getResponseParams().get("hb_type")).toStringValue();
        this.totalNum = BlurObject.bind(this.getResponseParams().get("total_num")).toIntValue();
        this.totalAmount = BlurObject.bind(this.getResponseParams().get("total_amount")).toIntValue();
        this.reason = BlurObject.bind(this.getResponseParams().get("reason")).toStringValue();
        this.sendTime = BlurObject.bind(this.getResponseParams().get("send_time")).toStringValue();
        this.refundTime = BlurObject.bind(this.getResponseParams().get("refund_time")).toStringValue();
        this.refundAmount = BlurObject.bind(this.getResponseParams().get("refund_amount")).toIntValue();
        this.wishing = BlurObject.bind(this.getResponseParams().get("wishing")).toStringValue();
        this.remark = BlurObject.bind(this.getResponseParams().get("remark")).toStringValue();
        this.actName = BlurObject.bind(this.getResponseParams().get("act_name")).toStringValue();
        //
        int _hbCount = this.getXPathHelper().getNumberValue("count(//hblist/hbinfo)").intValue();
        for (int i = 1; i <= _hbCount; i++) {
            Map<String, Object> _values = this.getXPathHelper().toMap(this.getXPathHelper().getNode("//hblist/hbinfo[" + i + "]"));
            if (!_values.isEmpty() && _values.size() == 4) {
                hbList.add(new HbInfo(BlurObject.bind(_values.get("openid")).toStringValue(),
                        BlurObject.bind(_values.get("amount")).toIntValue(),
                        BlurObject.bind(_values.get("status")).toStringValue(),
                        BlurObject.bind(_values.get("rcv_time")).toStringValue()));
            }
        }
    }

    public String mchBillNo() {
        return mchBillNo;
    }

    public String detailId() {
        return detailId;
    }

    public String status() {
        return status;
    }

    public String sendType() {
        return sendType;
    }

    public String hbType() {
        return hbType;
    }

    public Integer totalNum() {
        return totalNum;
    }

    public Integer totalAmount() {
        return totalAmount;
    }

    public String reason() {
        return reason;
    }

    public String sendTime() {
        return sendTime;
    }

    public String refundTime() {
        return refundTime;
    }

    public Integer refundAmount() {
        return refundAmount;
    }

    public String wishing() {
        return wishing;
    }

    public String remark() {
        return remark;
    }

    public String actName() {
        return actName;
    }

    public List<HbInfo> hbList() {
        return hbList;
    }

    public static class HbInfo {

        private String openId;

        private Integer amount;

        private String status;

        private String revTime;

        public HbInfo(String openId, Integer amount, String status, String revTime) {
            this.openId = openId;
            this.amount = amount;
            this.status = status;
            this.revTime = revTime;
        }

        public String openId() {
            return openId;
        }

        public Integer amount() {
            return amount;
        }

        public String status() {
            return status;
        }

        public String revTime() {
            return revTime;
        }
    }
}
