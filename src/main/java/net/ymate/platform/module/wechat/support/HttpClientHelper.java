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

import net.ymate.platform.commons.util.FileUtils;
import net.ymate.platform.module.wechat.IMediaFileWrapper;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * HttpClientHelper
 * </p>
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
 *          <td>2014年3月15日下午5:15:32</td>
 *          </tr>
 *          </table>
 */
public class HttpClientHelper {

    private static final Log _LOG = LogFactory.getLog(HttpClientHelper.class);

    // 编码方式
    public static final String DEFAULT_CHARSET = "UTF-8";

    // 连接超时时间
    private int __connectionTimeout = 15000;

    private SSLConnectionSocketFactory __socketFactory;

    public static HttpClientHelper create() {
        return new HttpClientHelper();
    }

    private HttpClientHelper() {
    }

    public HttpClientHelper customSSL(SSLConnectionSocketFactory socketFactory) {
        __socketFactory = socketFactory;
        return this;
    }

    public HttpClientHelper setConnectionTimeout(int connectionTimeout) {
        if (connectionTimeout > 0) {
            __connectionTimeout = connectionTimeout;
        }
        return this;
    }

    private CloseableHttpClient __doBuildHttpClient() throws KeyManagementException, NoSuchAlgorithmException {
        HttpClientBuilder _builder = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(__connectionTimeout)
                        .setSocketTimeout(__connectionTimeout)
                        .setConnectionRequestTimeout(__connectionTimeout).build());
        if (__socketFactory == null) {
            __socketFactory = new SSLConnectionSocketFactory(
                    SSLContexts.custom().useSSL().build(),
                    SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        }
        return _builder.setSSLSocketFactory(__socketFactory).build();
    }

    public String doGet(String url) throws Exception {
        CloseableHttpClient _httpClient = __doBuildHttpClient();
        try {
            _LOG.debug("Request URL [" + url + "]");
            String _result = _httpClient.execute(RequestBuilder.get().setUri(url).build(), new ResponseHandler<String>() {

                public String handleResponse(HttpResponse response) throws IOException {
                    return EntityUtils.toString(response.getEntity(), DEFAULT_CHARSET);
                }

            });
            _LOG.debug("Request URL [" + url + "] Response [" + _result + "]");
            return _result;
        } finally {
            _httpClient.close();
        }
    }

    public String doGet(String url, Map<String, String> params) throws Exception {
        RequestBuilder _request = RequestBuilder.get().setUri(url);
        for (Map.Entry<String, String> entry : params.entrySet()) {
            _request.addParameter(entry.getKey(), entry.getValue());
        }
        CloseableHttpClient _httpClient = __doBuildHttpClient();
        try {
            _LOG.debug("Request URL [" + url + "], Param [" + params + "]");
            String _result = _httpClient.execute(_request.build(), new ResponseHandler<String>() {

                public String handleResponse(HttpResponse response) throws IOException {
                    return EntityUtils.toString(response.getEntity(), DEFAULT_CHARSET);
                }

            });
            _LOG.debug("Request URL [" + url + "] Response [" + _result + "]");
            return _result;
        } finally {
            _httpClient.close();
        }
    }

    public String doPost(String url, String content) throws Exception {
        CloseableHttpClient _httpClient = __doBuildHttpClient();
        try {
            _LOG.debug("Request URL [" + url + "] PostBody [" + content + "]");
            String _result = _httpClient.execute(RequestBuilder.post()
                    .setUri(url)
                    .setEntity(EntityBuilder.create()
                            .setContentEncoding(DEFAULT_CHARSET)
                            .setContentType(ContentType.create("application/x-www-form-urlencoded", DEFAULT_CHARSET))
                            .setText(content).build()).build(), new ResponseHandler<String>() {

                public String handleResponse(HttpResponse response) throws IOException {
                    return EntityUtils.toString(response.getEntity(), DEFAULT_CHARSET);
                }

            });
            _LOG.debug("Request URL [" + url + "] Response [" + _result + "]");
            return _result;
        } finally {
            _httpClient.close();
        }
    }

    public String doPost(String url, byte[] content) throws Exception {
        CloseableHttpClient _httpClient = __doBuildHttpClient();
        try {
            _LOG.debug("Request URL [" + url + "] PostBody [" + content + "]");
            String _result = _httpClient.execute(RequestBuilder.post()
                    .setUri(url)
                    .setEntity(EntityBuilder.create()
                            .setContentEncoding(DEFAULT_CHARSET)
                            .setContentType(ContentType.create("application/x-www-form-urlencoded", DEFAULT_CHARSET))
                            .setBinary(content).build())
                    .build(), new ResponseHandler<String>() {

                public String handleResponse(HttpResponse response) throws IOException {
                    return EntityUtils.toString(response.getEntity(), DEFAULT_CHARSET);
                }

            });
            _LOG.debug("Request URL [" + url + "] Response [" + _result + "]");
            return _result;
        } finally {
            _httpClient.close();
        }
    }

    public String doPost(String url, Map<String, String> params) throws Exception {
        CloseableHttpClient _httpClient = __doBuildHttpClient();
        try {
            _LOG.debug("Request URL [" + url + "] PostBody [" + params + "]");
            String _result = _httpClient.execute(RequestBuilder.post()
                    .setUri(url)
                    .setEntity(EntityBuilder.create()
                            .setContentEncoding(DEFAULT_CHARSET)
                            .setParameters(__doBuildNameValuePairs(params)).build()).build(), new ResponseHandler<String>() {

                public String handleResponse(HttpResponse response) throws IOException {
                    return EntityUtils.toString(response.getEntity(), DEFAULT_CHARSET);
                }

            });
            _LOG.debug("Request URL [" + url + "] Response [" + _result + "]");
            return _result;
        } finally {
            _httpClient.close();
        }
    }

    private static List<NameValuePair> __doBuildNameValuePairs(Map<String, String> params) {
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (StringUtils.isNotBlank(entry.getValue())) {
                nameValuePair.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }
        return nameValuePair;
    }

    public String doUpload(String url, File uploadFile) throws Exception {
        CloseableHttpClient _httpClient = __doBuildHttpClient();
        try {
            _LOG.debug("Upload File [" + uploadFile + "]");
            String _result = EntityUtils.toString(_httpClient.execute(RequestBuilder.post().setUri(url)
                    .setEntity(MultipartEntityBuilder.create().addPart("media", new FileBody(uploadFile)).build()).build()).getEntity(), DEFAULT_CHARSET);
            _LOG.debug("Upload File [" + uploadFile + "] Response [" + _result + "]");
            return _result;
        } finally {
            _httpClient.close();
        }
    }

    protected static ResponseHandler<IMediaFileWrapper> __INNER_DOWNLOAD_HANDLER = new ResponseHandler<IMediaFileWrapper>() {

        public IMediaFileWrapper handleResponse(HttpResponse response) throws IOException {
            if (response.getEntity().getContentType().getValue().equalsIgnoreCase("text/plain")) {
                final String _errMsg = EntityUtils.toString(response.getEntity(), DEFAULT_CHARSET);
                //
                return new IMediaFileWrapper() {

                    public String getErrorMsg() {
                        return _errMsg;
                    }

                    public String getFileName() {
                        return null;
                    }

                    public String getSimpleName() {
                        return null;
                    }

                    public String getSuffix() {
                        return null;
                    }

                    public long getContentLength() {
                        return 0;
                    }

                    public String getContentType() {
                        return null;
                    }

                    public InputStream getInputStream() throws IOException {
                        return null;
                    }

                    public void writeTo(File file) throws IOException {
                    }

                };
            } else {
                final String _fileName = StringUtils.substringBetween(response.getFirstHeader("Content-disposition").getValue(), "filename=\"", "\"");
                final String _simpleName = StringUtils.substringBefore(_fileName, ".");
                final String _suffix = FileUtils.getExtName(_fileName);
                final long _contentLength = response.getEntity().getContentLength();
                final String _contentType = response.getEntity().getContentType().getValue();
                //
                final File _tmpFile = File.createTempFile(_fileName, _suffix);
                org.apache.commons.io.FileUtils.copyInputStreamToFile(response.getEntity().getContent(), _tmpFile);
                //
                return new IMediaFileWrapper() {

                    public String getErrorMsg() {
                        return null;
                    }

                    public String getFileName() {
                        return _fileName;
                    }

                    public String getSimpleName() {
                        return _simpleName;
                    }

                    public String getSuffix() {
                        return _suffix;
                    }

                    public long getContentLength() {
                        return _contentLength;
                    }

                    public String getContentType() {
                        return _contentType;
                    }

                    public InputStream getInputStream() throws IOException {
                        return org.apache.commons.io.FileUtils.openInputStream(_tmpFile);
                    }

                    public void writeTo(File file) throws IOException {
                        if (!_tmpFile.renameTo(file)) {
                            org.apache.commons.io.FileUtils.copyInputStreamToFile(getInputStream(), file);
                        } else {
                            throw new IOException("Cannot write file to disk!");
                        }
                    }

                };
            }
        }
    };

    public IMediaFileWrapper doDownload(String url, String content) throws Exception {
        CloseableHttpClient _httpClient = __doBuildHttpClient();
        try {
            return _httpClient.execute(RequestBuilder.post()
                    .setUri(url)
                    .setEntity(EntityBuilder.create()
                            .setContentEncoding(DEFAULT_CHARSET)
                            .setContentType(ContentType.create("application/x-www-form-urlencoded", DEFAULT_CHARSET))
                            .setText(content).build()).build(), __INNER_DOWNLOAD_HANDLER);
        } finally {
            _httpClient.close();
        }
    }

    public IMediaFileWrapper doDownload(String url) throws Exception {
        CloseableHttpClient _httpClient = __doBuildHttpClient();
        try {
            return _httpClient.execute(RequestBuilder.get().setUri(url).build(), __INNER_DOWNLOAD_HANDLER);
        } finally {
            _httpClient.close();
        }
    }

}