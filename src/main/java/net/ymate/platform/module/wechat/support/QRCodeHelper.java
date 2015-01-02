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
package net.ymate.platform.module.wechat.support;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.apache.commons.lang.StringUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Hashtable;

/**
 * 二维码生成工具类
 *
 * @author 刘镇 (suninformation@163.com) on 15/1/2 下午4:21
 * @version 1.0
 */
public class QRCodeHelper {

    private static final int BLACK = 0xFF000000;
    private static final int WHITE = 0xFFFFFFFF;

    private BitMatrix __matrix;

    // 二维码的图片格式
    private String __format = "png";

    private QRCodeHelper(BitMatrix matrix) {
        __matrix = matrix;
    }

    /**
     * 创建二维码工具类实例对象
     *
     * @param content      二维码内容字符串
     * @param characterSet 使用的字符编码集，默认UTF-8
     * @param width        二维码图片宽度
     * @param height       二维码图片高度
     * @param margin       二维码图片边距，默认3
     * @param level        二维码容错级别
     * @return
     * @throws WriterException
     */
    public static QRCodeHelper create(String content, String characterSet, int width, int height, int margin, ErrorCorrectionLevel level) throws WriterException {
        Hashtable hints = new Hashtable();
        //内容所使用编码
        hints.put(EncodeHintType.CHARACTER_SET, StringUtils.defaultIfEmpty(characterSet, "UTF-8"));
//        // 以下两行貌似没什么用...
//        hints.put(EncodeHintType.MAX_SIZE, 298);
//        hints.put(EncodeHintType.MIN_SIZE, 235);
        hints.put(EncodeHintType.MARGIN, margin <= 0 ? 3 : margin);
        //设置QR二维码的纠错级别（H为最高级别）
        if (level != null) {
            hints.put(EncodeHintType.ERROR_CORRECTION, level);
        }
        //生成二维码
        return new QRCodeHelper(new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints));
    }

    /**
     * 创建二维码工具类实例对象
     *
     * @param content 二维码内容字符串
     * @param width   二维码图片宽度
     * @param height  二维码图片高度
     * @param level   二维码容错级别
     * @return
     * @throws WriterException
     */
    public static QRCodeHelper create(String content, int width, int height, ErrorCorrectionLevel level) throws WriterException {
        return create(content, null, width, height, 0, level);
    }

    /**
     * 创建二维码工具类实例对象
     *
     * @param content 二维码内容字符串
     * @param width   二维码图片宽度
     * @param height  二维码图片高度
     * @return
     * @throws WriterException
     */
    public static QRCodeHelper create(String content, int width, int height) throws WriterException {
        return create(content, null, width, height, 0, null);
    }

    /**
     * 设置二维码图片格式，默认PNG
     *
     * @param format
     * @return
     */
    public QRCodeHelper setFormat(String format) {
        this.__format = StringUtils.defaultIfEmpty(format, "png");
        return this;
    }

    public BufferedImage toBufferedImage() {
        int width = __matrix.getWidth();
        int height = __matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, __matrix.get(x, y) ? BLACK : WHITE);
            }
        }
        return image;
    }

    /**
     * 输出二维码图片到文件
     *
     * @param file
     * @throws IOException
     */
    public void writeToFile(File file) throws IOException {
        BufferedImage image = toBufferedImage();
        if (!ImageIO.write(image, __format, file)) {
            throw new IOException("Could not write an image of format " + __format + " to " + file);
        }
    }

    /**
     * 输出二维码图片到输出流
     *
     * @param stream
     * @throws IOException
     */
    public void writeToStream(OutputStream stream) throws IOException {
        BufferedImage image = toBufferedImage();
        if (!ImageIO.write(image, __format, stream)) {
            throw new IOException("Could not write an image of format " + __format);
        }
    }

}
