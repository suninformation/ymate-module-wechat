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
package net.ymate.module.wechat.message.event;

import net.ymate.framework.commons.XPathHelper;
import net.ymate.module.wechat.IWechat;
import net.ymate.module.wechat.message.InEvent;
import org.apache.commons.lang.StringUtils;

import javax.xml.xpath.XPathExpressionException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 刘镇 (suninformation@163.com) on 16/5/22 上午7:54
 * @version 1.0
 */
public class MenuEvent extends InEvent {

    private String menuId;

    private String scanType;
    private String scanResult;

    //

    private String location_X;

    private String location_Y;

    private String scale;

    private String label;

    private String poiname;

    //

    private Integer picsCount;

    private List<String> picsMd5 = new ArrayList<String>();

    public MenuEvent(XPathHelper xPathHelper, String toUserName, String fromUserName, Integer createTime, IWechat.MessageType msgType, IWechat.EventType event) throws XPathExpressionException {
        super(xPathHelper, toUserName, fromUserName, createTime, msgType, event);
        switch (event) {
            case MENU_VIEW:
                this.menuId = xPathHelper.getStringValue("//MenuID");
                break;
            case MENU_SCANCODE_PUSH:
            case MENU_SCANCODE_WAITMSG:
                this.scanType = xPathHelper.getStringValue("//ScanCodeInfo/ScanType");
                this.scanResult = xPathHelper.getStringValue("//ScanCodeInfo/ScanResult");
                break;
            case MENU_LOCATION_SELECT:
                this.location_X = xPathHelper.getStringValue("//SendLocationInfo/Location_X");
                this.location_Y = xPathHelper.getStringValue("//SendLocationInfo/Location_Y");
                this.scale = xPathHelper.getStringValue("//SendLocationInfo/Scale");
                this.label = xPathHelper.getStringValue("//SendLocationInfo/Label");
                this.poiname = xPathHelper.getStringValue("//SendLocationInfo/Poiname");
                break;
            case MENU_PIC_SYSPHOTO:
            case MENU_PIC_PHOTO_OR_ALBUM:
                this.picsCount = xPathHelper.getNumberValue("//SendPicsInfo/Count").intValue();
                if (this.picsCount > 0) {
                    for (int i = 1; i <= this.picsCount; i++) {
                        String _item = xPathHelper.getStringValue("//SendPicsInfo/item[" + i + "]/PicMd5Sum");
                        if (StringUtils.isNotBlank(_item)) {
                            this.picsMd5.add(_item);
                        }
                    }
                }
                break;
        }
    }

    public String getMenuId() {
        return menuId;
    }

    public String getScanType() {
        return scanType;
    }

    public String getScanResult() {
        return scanResult;
    }

    public String getLocation_X() {
        return location_X;
    }

    public String getLocation_Y() {
        return location_Y;
    }

    public String getScale() {
        return scale;
    }

    public String getLabel() {
        return label;
    }

    public String getPoiname() {
        return poiname;
    }
}
