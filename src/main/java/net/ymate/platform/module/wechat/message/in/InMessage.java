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
package net.ymate.platform.module.wechat.message.in;

import net.ymate.platform.module.wechat.WeChat;
import net.ymate.platform.module.wechat.message.AbstractMessage;
import net.ymate.platform.module.wechat.message.event.IClickEvent;
import net.ymate.platform.module.wechat.message.event.ILocationEvent;
import net.ymate.platform.module.wechat.message.event.IMassSendJobFinishEvent;
import net.ymate.platform.module.wechat.message.event.IScanEvent;
import net.ymate.platform.module.wechat.message.event.ISubscribeEvent;
import net.ymate.platform.module.wechat.message.event.IUnsubscribeEvent;
import net.ymate.platform.module.wechat.message.event.IViewEvent;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * <p>
 * InMessage
 * </p>
 * <p>
 * 
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
 *          <td>2014年3月15日上午11:55:44</td>
 *          </tr>
 *          </table>
 */
@XStreamAlias("xml")
public class InMessage extends AbstractMessage implements ITextMessage,
		IImageMessage, IVoiceMessage, IVideoMessage, ILocationMessage,
		ILinkMessage, ISubscribeEvent, IUnsubscribeEvent, IScanEvent,
		IClickEvent, ILocationEvent, IViewEvent, IMassSendJobFinishEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -305904856689278383L;

	@XStreamAlias("MsgId")
	private Long msgId;

	@XStreamAlias("Content")
	private String content;

	@XStreamAlias("PicUrl")
	private String picUrl;

	@XStreamAlias("MediaId")
	private String mediaId;

	@XStreamAlias("Location_X")
	private Double x;

	@XStreamAlias("Location_Y")
	private Double y;

	@XStreamAlias("Scale")
	private Integer scale;

	@XStreamAlias("Label")
	private String label;

	@XStreamAlias("ThumbMediaId")
	private String thumbMediaId;

	@XStreamAlias("Format")
	private String format;

	@XStreamAlias("Recognition")
	private String recognition;

	@XStreamAlias("Title")
	private String title;

	@XStreamAlias("Description")
	private String description;

	@XStreamAlias("Url")
	private String url;

	@XStreamAlias("Event")
	private String event;

	@XStreamAlias("EventKey")
	private String eventKey;

	@XStreamAlias("Ticket")
	private String ticket;

	@XStreamAlias("Latitude")
	private Double latitude;

	@XStreamAlias("Longitude")
	private Double longitude;

	@XStreamAlias("Precision")
	private Double precision;

	// ----

	@XStreamAlias("MsgID")
	private Long msgID;

	@XStreamAlias("Status")
	private String status;

	@XStreamAlias("TotalCount")
	private Integer totalCount;

	@XStreamAlias("FilterCount")
	private Integer filterCount;

	@XStreamAlias("SendCount")
	private Integer sendCount;

	@XStreamAlias("ErrorCount")
	private Integer errorCount;

	public Long getMsgId() {
		return msgId;
	}

	public void setMsgId(Long msgId) {
		this.msgId = msgId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	public Double getX() {
		return x;
	}

	public void setX(Double x) {
		this.x = x;
	}

	public Double getY() {
		return y;
	}

	public void setY(Double y) {
		this.y = y;
	}

	public Integer getScale() {
		return scale;
	}

	public void setScale(Integer scale) {
		this.scale = scale;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getThumbMediaId() {
		return thumbMediaId;
	}

	public void setThumbMediaId(String thumbMediaId) {
		this.thumbMediaId = thumbMediaId;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getRecognition() {
		return recognition;
	}

	public void setRecognition(String recognition) {
		this.recognition = recognition;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getEventKey() {
		return eventKey;
	}

	public void setEventKey(String eventKey) {
		this.eventKey = eventKey;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getPrecision() {
		return precision;
	}

	public void setPrecision(Double precision) {
		this.precision = precision;
	}

	// ----

	

	public String getStatus() {
		return status;
	}

	public Long getMsgID() {
		return msgID;
	}

	public void setMsgID(Long msgID) {
		this.msgID = msgID;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public Integer getFilterCount() {
		return filterCount;
	}

	public void setFilterCount(Integer filterCount) {
		this.filterCount = filterCount;
	}

	public Integer getSendCount() {
		return sendCount;
	}

	public void setSendCount(Integer sendCount) {
		this.sendCount = sendCount;
	}

	public Integer getErrorCount() {
		return errorCount;
	}

	public void setErrorCount(Integer errorCount) {
		this.errorCount = errorCount;
	}

	// ----

	public Boolean isEvent(){
		return this.getMsgType().equalsIgnoreCase(WeChat.WX_MESSAGE.TYPE_EVENT);
	}
	
	public Boolean isText(){
		return this.getMsgType().equalsIgnoreCase(WeChat.WX_MESSAGE.TYPE_TEXT);
	}
	
	public Boolean isImage(){
		return this.getMsgType().equalsIgnoreCase(WeChat.WX_MESSAGE.TYPE_IMAGE);
	}
	
	public Boolean isVoice(){
		return this.getMsgType().equalsIgnoreCase(WeChat.WX_MESSAGE.TYPE_VOICE);
	}
	
	public Boolean isVideo(){
		return this.getMsgType().equalsIgnoreCase(WeChat.WX_MESSAGE.TYPE_VIDEO);
	}
	
	public Boolean isLocation(){
		return this.getMsgType().equalsIgnoreCase(WeChat.WX_MESSAGE.TYPE_LOCATION);
	}
	
	public Boolean isLink(){
		return this.getMsgType().equalsIgnoreCase(WeChat.WX_MESSAGE.TYPE_LINK);
	}

}
