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

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import net.ymate.platform.commons.util.RuntimeUtils;
import net.ymate.platform.module.wechat.message.OutMessage;
import net.ymate.platform.module.wechat.message.in.InMessage;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * <p>
 * MessageHelper
 * </p>
 * <p>
 * <p/>
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
 *          <td>2014年3月15日下午3:16:53</td>
 *          </tr>
 *          </table>
 */
public class MessageHelper {

    static Charset __CHARSET = Charset.forName("utf-8");

    static String __RANDOM_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    /**
     * @param protocolStr 接收到的消息协议
     * @return 分析协议并转换成对象
     */
    public static InMessage parsingInMessage(String protocolStr) {
        XStream _xstream = XStreamHelper.createXStream(false);
        _xstream.ignoreUnknownElements();
        _xstream.processAnnotations(InMessage.class);
        return (InMessage) _xstream.fromXML(protocolStr);
    }

    public static String parsingOutMessage(OutMessage message) {
        XStream _xstream = XStreamHelper.createXStream(true);
        _xstream.processAnnotations(message.getClass());
        return _xstream.toXML(message);
    }

    //------------
    // 消息加/解密
    //------------

    public static EncryptMsg parsingEncryptMsg(String protocolStr) {
        XStream _xstream = XStreamHelper.createXStream(false);
        _xstream.ignoreUnknownElements();
        _xstream.processAnnotations(EncryptMsg.class);
        return (EncryptMsg) _xstream.fromXML(protocolStr);
    }

    /**
     * 验证URL
     *
     * @param appId
     * @param encodingAesKey
     * @param token
     * @param msgSignature 签名串
     * @param timeStamp    时间戳
     * @param nonce        随机串
     * @param echoStr      随机串
     * @return 解密之后的echostr
     * @throws AesException 执行失败，请查看该异常的错误码和具体的错误信息
     */
    @Deprecated
    public static String verifyUrl(String appId, String encodingAesKey, String token, String msgSignature, String timeStamp, String nonce, String echoStr) throws AesException {
        String signature = getSHA1(token, timeStamp, nonce, echoStr);
        if (!signature.equals(msgSignature)) {
            throw new AesException(AesException.ValidateSignatureError);
        }
        byte[] _aesKey = Base64.decodeBase64(encodingAesKey + "=");
        return decrypt(appId, _aesKey, echoStr);
    }

    /**
     *
     * @param appId
     * @param encodingAesKey
     * @param token
     * @param messageStr
     * @return
     * @throws Exception
     */
    public static String encryptMessage(String appId, String encodingAesKey, String token, String messageStr) throws Exception {
        if (encodingAesKey.length() != 43) {
            throw new AesException(AesException.IllegalAesKey);
        }
        byte[] _aesKey = Base64.decodeBase64(encodingAesKey + "=");
        //
        String randomStr = RandomStringUtils.random(16, __RANDOM_CHARS);
        String nonce = RandomStringUtils.random(16, __RANDOM_CHARS);
        // 加密
        String encryptedXml = encrypt(appId, _aesKey, randomStr, messageStr);
        // 参数
        String timeStamp = Long.toString(System.currentTimeMillis() / 1000);
        // 生成安全签名
        String signature = getSHA1(token, timeStamp, nonce, encryptedXml);
        return new EncryptMsg(signature, timeStamp, nonce, encryptedXml).toXML();
    }

    /**
     *
     * @param appId
     * @param encodingAesKey
     * @param token
     * @param protocolStr
     * @return
     * @throws Exception
     */
    public static String decryptMessage(String appId, String encodingAesKey, String token, String protocolStr) throws Exception {
        if (encodingAesKey.length() != 43) {
            throw new AesException(AesException.IllegalAesKey);
        }
        String _token = token;
        String _appId = appId;
        byte[] _aesKey = Base64.decodeBase64(encodingAesKey + "=");
        // 密钥，公众账号的app secret
        // 提取密文
        EncryptMsg _encryptMsg = parsingEncryptMsg(protocolStr);
        // 验证安全签名
        String signature = getSHA1(token, _encryptMsg.getTimeStamp(), _encryptMsg.getNonce(), _encryptMsg.getEncrypt());
        // 和URL中的签名比较是否相等
        // System.out.println("第三方收到URL中的签名：" + msg_sign);
        // System.out.println("第三方校验签名：" + signature);
        if (!signature.equals(_encryptMsg.getMsgSignature())) {
            throw new AesException(AesException.ValidateSignatureError);
        }
        // 解密
        return decrypt(appId, _aesKey, _encryptMsg.getEncrypt());
    }

    static String encrypt(String appId, byte[] aesKey, String randomStr, String messageStr) throws AesException {
        ByteGroup byteCollector = new ByteGroup();
        byte[] randomStrBytes = randomStr.getBytes(__CHARSET);
        byte[] textBytes = messageStr.getBytes(__CHARSET);
        byte[] networkBytesOrder = getNetworkBytesOrder(textBytes.length);
        byte[] appidBytes = appId.getBytes(__CHARSET);
        // randomStr + networkBytesOrder + text + appid
        byteCollector.addBytes(randomStrBytes);
        byteCollector.addBytes(networkBytesOrder);
        byteCollector.addBytes(textBytes);
        byteCollector.addBytes(appidBytes);
        // ... + pad: 使用自定义的填充方式对明文进行补位填充
        byte[] padBytes = PKCS7Encoder.encode(byteCollector.size());
        byteCollector.addBytes(padBytes);
        // 获得最终的字节流, 未加密
        byte[] unencrypted = byteCollector.toBytes();

        try {
            // 设置加密模式为AES的CBC模式
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            SecretKeySpec keySpec = new SecretKeySpec(aesKey, "AES");
            IvParameterSpec iv = new IvParameterSpec(aesKey, 0, 16);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);
            // 加密
            byte[] encrypted = cipher.doFinal(unencrypted);
            // 使用BASE64对加密后的字符串进行编码
            String base64Encrypted = Base64.encodeBase64URLSafeString(encrypted);
            //
            return base64Encrypted;
        } catch (Exception e) {
            throw new AesException(AesException.EncryptAESError, RuntimeUtils.unwrapThrow(e));
        }
    }

    /**
     * 对密文进行解密.
     *
     * @param encryptText 需要解密的密文
     * @return 解密得到的明文
     * @throws AesException aes解密失败
     */
    static String decrypt(String appId, byte[] aesKey, String encryptText) throws AesException {
        byte[] original;
        try {
            // 设置解密模式为AES的CBC模式
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            SecretKeySpec key_spec = new SecretKeySpec(aesKey, "AES");
            IvParameterSpec iv = new IvParameterSpec(Arrays.copyOfRange(aesKey, 0, 16));
            cipher.init(Cipher.DECRYPT_MODE, key_spec, iv);
            // 使用BASE64对密文进行解码
            byte[] encrypted = Base64.decodeBase64(encryptText);
            // 解密
            original = cipher.doFinal(encrypted);
        } catch (Exception e) {
            throw new AesException(AesException.DecryptAESError, RuntimeUtils.unwrapThrow(e));
        }

        String xmlContent, from_appid;
        try {
            // 去除补位字符
            byte[] bytes = PKCS7Encoder.decode(original);
            // 分离16位随机字符串,网络字节序和AppId
            byte[] networkOrder = Arrays.copyOfRange(bytes, 16, 20);
            int xmlLength = recoverNetworkBytesOrder(networkOrder);
            xmlContent = new String(Arrays.copyOfRange(bytes, 20, 20 + xmlLength), __CHARSET);
            from_appid = new String(Arrays.copyOfRange(bytes, 20 + xmlLength, bytes.length), __CHARSET);
        } catch (Exception e) {
            throw new AesException(AesException.IllegalBuffer, RuntimeUtils.unwrapThrow(e));
        }
        // appid不相同的情况
        if (!from_appid.equals(appId)) {
            throw new AesException(AesException.ValidateAppidError);
        }
        return xmlContent;

    }

    /**
     * 生成4个字节的网络字节序
     *
     * @param number
     */
    static byte[] getNetworkBytesOrder(int number) {
        byte[] orderBytes = new byte[4];
        orderBytes[3] = (byte) (number & 0xFF);
        orderBytes[2] = (byte) (number >> 8 & 0xFF);
        orderBytes[1] = (byte) (number >> 16 & 0xFF);
        orderBytes[0] = (byte) (number >> 24 & 0xFF);
        return orderBytes;
    }

    /**
     * 还原4个字节的网络字节序
     *
     * @param orderBytes
     * @return
     */
    static int recoverNetworkBytesOrder(byte[] orderBytes) {
        int sourceNumber = 0;
        for (int i = 0; i < 4; i++) {
            sourceNumber <<= 8;
            sourceNumber |= orderBytes[i] & 0xff;
        }
        return sourceNumber;
    }

    /**
     * 用SHA1算法生成安全签名
     *
     * @param token     票据
     * @param timestamp 时间戳
     * @param nonce     随机字符串
     * @param encrypt   密文
     * @return 安全签名
     */
    static String getSHA1(String token, String timestamp, String nonce, String encrypt) {
        String[] array = new String[]{token, timestamp, nonce, encrypt};
        // 字符串排序
        Arrays.sort(array);
        // SHA1签名生成
        return DigestUtils.shaHex(StringUtils.join(array, ""));
    }

    static class ByteGroup {
        ArrayList<Byte> byteContainer = new ArrayList<Byte>();

        public byte[] toBytes() {
            byte[] bytes = new byte[byteContainer.size()];
            for (int i = 0; i < byteContainer.size(); i++) {
                bytes[i] = byteContainer.get(i);
            }
            return bytes;
        }

        public ByteGroup addBytes(byte[] bytes) {
            for (byte b : bytes) {
                byteContainer.add(b);
            }
            return this;
        }

        public int size() {
            return byteContainer.size();
        }
    }

    @XStreamAlias("xml")
    static class EncryptMsg {

        @XStreamAlias("ToUserName")
        private String toUserName;

        @XStreamAlias("MsgSignature")
        private String msgSignature;

        @XStreamAlias("TimeStamp")
        private String timeStamp;

        @XStreamAlias("Nonce")
        private String nonce;

        @XStreamAlias("Encrypt")
        private String encrypt;

        public EncryptMsg() {
        }

        public EncryptMsg(String msgSignature, String timeStamp, String nonce, String encrypt) {
            this.msgSignature = msgSignature;
            this.timeStamp = timeStamp;
            this.nonce = nonce;
            this.encrypt = encrypt;
        }

        public String toXML() {
            XStream _xStream = XStreamHelper.createXStream(true);
            _xStream.processAnnotations(this.getClass());
            _xStream.ignoreUnknownElements();
            return _xStream.toXML(this);
        }

        public String getToUserName() {
            return toUserName;
        }

        public void setToUserName(String toUserName) {
            this.toUserName = toUserName;
        }

        public String getMsgSignature() {
            return msgSignature;
        }

        public void setMsgSignature(String msgSignature) {
            this.msgSignature = msgSignature;
        }

        public String getTimeStamp() {
            return timeStamp;
        }

        public void setTimeStamp(String timeStamp) {
            this.timeStamp = timeStamp;
        }

        public String getNonce() {
            return nonce;
        }

        public void setNonce(String nonce) {
            this.nonce = nonce;
        }

        public String getEncrypt() {
            return encrypt;
        }

        public void setEncrypt(String encrypt) {
            this.encrypt = encrypt;
        }
    }

    /**
     * 提供基于PKCS7算法的加解密接口.
     */
    static class PKCS7Encoder {

        static int BLOCK_SIZE = 32;

        /**
         * 获得对明文进行补位填充的字节.
         *
         * @param count 需要进行填充补位操作的明文字节个数
         * @return 补齐用的字节数组
         */
        static byte[] encode(int count) {
            // 计算需要填充的位数
            int amountToPad = BLOCK_SIZE - (count % BLOCK_SIZE);
            if (amountToPad == 0) {
                amountToPad = BLOCK_SIZE;
            }
            // 获得补位所用的字符
            char padChr = chr(amountToPad);
            String tmp = new String();
            for (int index = 0; index < amountToPad; index++) {
                tmp += padChr;
            }
            return tmp.getBytes(__CHARSET);
        }

        /**
         * 删除解密后明文的补位字符
         *
         * @param decrypted 解密后的明文
         * @return 删除补位字符后的明文
         */
        static byte[] decode(byte[] decrypted) {
            int pad = (int) decrypted[decrypted.length - 1];
            if (pad < 1 || pad > 32) {
                pad = 0;
            }
            return Arrays.copyOfRange(decrypted, 0, decrypted.length - pad);
        }

        /**
         * 将数字转化成ASCII码对应的字符，用于对明文进行补码
         *
         * @param a 需要转化的数字
         * @return 转化得到的字符
         */
        static char chr(int a) {
            byte target = (byte) (a & 0xFF);
            return (char) target;
        }

    }

    public static class AesException extends Exception {

        public final static int OK = 0;
        public final static int ValidateSignatureError = -40001;
        public final static int ParseXmlError = -40002;
        public final static int ComputeSignatureError = -40003;
        public final static int IllegalAesKey = -40004;
        public final static int ValidateAppidError = -40005;
        public final static int EncryptAESError = -40006;
        public final static int DecryptAESError = -40007;
        public final static int IllegalBuffer = -40008;

        private int code;

        private static String getMessage(int code) {
            switch (code) {
                case ValidateSignatureError:
                    return "签名验证错误";
                case ParseXmlError:
                    return "xml解析失败";
                case ComputeSignatureError:
                    return "sha加密生成签名失败";
                case IllegalAesKey:
                    return "SymmetricKey非法";
                case ValidateAppidError:
                    return "appid校验失败";
                case EncryptAESError:
                    return "aes加密失败";
                case DecryptAESError:
                    return "aes解密失败";
                case IllegalBuffer:
                    return "解密后得到的buffer非法";
                default:
                    return null; // cannot be
            }
        }

        public int getCode() {
            return code;
        }

        AesException(int code) {
            super(getMessage(code));
            this.code = code;
        }

        AesException(int code, Throwable cause) {
            super(getMessage(code), cause);
            this.code = code;
        }

    }

}
