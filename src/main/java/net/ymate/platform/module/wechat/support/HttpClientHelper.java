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

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import net.ymate.platform.commons.lang.BlurObject;
import net.ymate.platform.module.wechat.IMediaFileWrapper;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * <p>
 * HttpClientHelper
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
 *          <td>2014年3月15日下午5:15:32</td>
 *          </tr>
 *          </table>
 */
public class HttpClientHelper {

	private static final Log _LOG = LogFactory.getLog(HttpClientHelper.class);

	// 编码方式
	public static final String DEFAULT_CHARSET = "UTF-8";

	// 连接超时时间
	public static int CONNECTION_TIMEOUT = 30000;

	// 读数据超时时间
	public static int READ_DATA_TIMEOUT = 30000;

	/**
	 * @param url
	 * @param https
	 * @return 发送GET请求并返回回应结果
	 * @throws NoSuchProviderException
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 * @throws KeyManagementException
	 */
	public static String doGet(String url, boolean https) throws NoSuchAlgorithmException, NoSuchProviderException, IOException, KeyManagementException {
		StringBuilder _resultSB = null;
		URLConnection _conn = null;
		//
		_LOG.debug("Request URL [" + url + "], With " + (https ? "HTTPS_GET" : "HTTP_GET"));
		//
		if (https) {
			SSLContext _context = SSLContext.getInstance("SSL", "SunJSSE");
			_context.init(null, new TrustManager[] { new WeChatX509TrustManager() }, new java.security.SecureRandom());
			SSLSocketFactory _sslFactory = _context.getSocketFactory();
			//
			_conn = new URL(url).openConnection();
			((HttpsURLConnection) _conn).setSSLSocketFactory(_sslFactory);
		} else {
			_conn = new URL(url).openConnection();
		}
		_conn.setConnectTimeout(CONNECTION_TIMEOUT);
		_conn.setReadTimeout(READ_DATA_TIMEOUT);
		if (https) {
			((HttpsURLConnection) _conn).setRequestMethod("GET");
		} else {
			((HttpURLConnection) _conn).setRequestMethod("GET");
		}
		_conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

		_conn.setDoOutput(true);
		_conn.setDoInput(true);
		_conn.connect();

		InputStream _inputStream = _conn.getInputStream();
		BufferedReader _reader = new BufferedReader(new InputStreamReader(_inputStream, DEFAULT_CHARSET));
		String _buffer = null;
		_resultSB = new StringBuilder();
		while ((_buffer = _reader.readLine()) != null) {
			_resultSB.append(_buffer);
		}
		_inputStream.close();
		if (_conn != null) {
			if (https) {
				((HttpsURLConnection) _conn).disconnect();
			} else {
				((HttpURLConnection) _conn).disconnect();
			}
		}
		String _result = _resultSB.toString();
		_LOG.debug("Url [" + url + "] Response [" + _result + "]");
		return _result;
	}

	/**
	 * @param url
	 * @param https
	 * @param params
	 * @return 发送GET请求并返回回应结果
	 * @throws NoSuchProviderException
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 * @throws KeyManagementException
	 */
	public static String doGet(String url, boolean https, Map<String, String> params) throws NoSuchAlgorithmException, NoSuchProviderException, IOException, KeyManagementException {
		StringBuilder _paramSB = new StringBuilder(url);
		if (url.indexOf("?") == -1) {
			_paramSB.append("?");
		} else {
			_paramSB.append("&");
		}
		boolean _flag = true;
		for (Entry<String, String> entry : params.entrySet()) {
			if (_flag) {
				_flag = false;
			} else {
				_paramSB.append("&");
			}
			_paramSB.append(entry.getKey()).append("=");
			if (StringUtils.isNotEmpty(entry.getValue())) {
				try {
					_paramSB.append(URLEncoder.encode(entry.getValue(), DEFAULT_CHARSET));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		}
		return doGet(_paramSB.toString(), https);
	}

	public static String doParamSignatureSort(Map<String, String> params, boolean encode) {
		StringBuilder _paramSB = new StringBuilder();
		String[] _keys = params.keySet().toArray(new String[0]);
		Arrays.sort(_keys);
		boolean _flag = true;
		for (String _key : _keys) {
			if (_flag) {
				_flag = false;
			} else {
				_paramSB.append("&");
			}
			_paramSB.append(_key).append("=");
			String _value = params.get(_key);
			if (StringUtils.isNotEmpty(_value)) {
				if (encode) {
					try {
						_paramSB.append(URLEncoder.encode(_value, DEFAULT_CHARSET));
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				} else {
					_paramSB.append(_value);
				}
			}
		}
		return _paramSB.toString();
	}

	/**
	 * @param url
	 * @param https
	 * @param params
	 * @return 发送POST请求并返回回应结果
	 * @throws NoSuchProviderException
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 * @throws KeyManagementException
	 */
	public static String doPost(String url, boolean https, String params) throws NoSuchAlgorithmException, NoSuchProviderException, IOException, KeyManagementException {
		StringBuilder _resultSB = null;
		URLConnection _conn = null;
		//
		_LOG.debug("Request URL [" + url + "], Param [" + params + "], With " + (https ? "HTTPS_POST" : "HTTP_POST"));
		//
		if (https) {
			SSLContext _context = SSLContext.getInstance("SSL", "SunJSSE");
			_context.init(null, new TrustManager[] { new WeChatX509TrustManager() }, new java.security.SecureRandom());
			SSLSocketFactory _sslFactory = _context.getSocketFactory();
			_conn = new URL(url).openConnection();
			((HttpsURLConnection) _conn).setSSLSocketFactory(_sslFactory);
		} else {
			_conn = new URL(url).openConnection();
		}
		//
		_conn.setConnectTimeout(CONNECTION_TIMEOUT);
		_conn.setReadTimeout(READ_DATA_TIMEOUT);
		if (https) {
			((HttpsURLConnection) _conn).setRequestMethod("POST");
		} else {
			((HttpURLConnection) _conn).setRequestMethod("POST");
		}
		_conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

		_conn.setDoOutput(true);
		_conn.setDoInput(true);
		_conn.connect();

		OutputStream _outputStream = _conn.getOutputStream();
		_outputStream.write(params.getBytes(DEFAULT_CHARSET));
		_outputStream.flush();
		_outputStream.close();

		InputStream _inputStream = _conn.getInputStream();
		BufferedReader _reader = new BufferedReader(new InputStreamReader(_inputStream, DEFAULT_CHARSET));
		String _buffer = null;
		_resultSB = new StringBuilder();
		while ((_buffer = _reader.readLine()) != null) {
			_resultSB.append(_buffer);
		}
		_inputStream.close();
		if (_conn != null) {
			if (https) {
				((HttpsURLConnection) _conn).disconnect();
			} else {
				((HttpURLConnection) _conn).disconnect();
			}
		}
		String _result = _resultSB.toString();
		_LOG.debug("Url [" + url + "] Response [" + _result + "]");
		return _result;
	}

	/**
	 * @param url
	 * @param https
	 * @param uploadFile
	 * @return 文件上传
	 * @throws NoSuchProviderException
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 * @throws KeyManagementException
	 */
	public static String doUpload(String url, boolean https, File uploadFile) throws NoSuchAlgorithmException, NoSuchProviderException, IOException, KeyManagementException {
		StringBuilder _resultSB = null;
		URLConnection _conn = null;
		//
		if (https) {
			SSLContext _context = SSLContext.getInstance("SSL", "SunJSSE");
			_context.init(null, new TrustManager[] { new WeChatX509TrustManager() }, new java.security.SecureRandom());
			SSLSocketFactory _sslFactory = _context.getSocketFactory();
			_conn = new URL(url).openConnection();
			((HttpsURLConnection) _conn).setSSLSocketFactory(_sslFactory);
		} else {
			_conn = new URL(url).openConnection();
		}
		//
		String BOUNDARY = "----WebKitFormBoundaryiDGnV9zdZA1eM1yL";
		//
		_conn.setDoOutput(true);
		_conn.setDoInput(true);
		_conn.setUseCaches(false);
		if (https) {
			((HttpsURLConnection) _conn).setRequestMethod("POST");
		} else {
			((HttpURLConnection) _conn).setRequestMethod("POST");
		}
		_conn.setRequestProperty("connection", "Keep-Alive");
		_conn.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1700.107 Safari/537.36");
		_conn.setRequestProperty("Charsert", "UTF-8");
		_conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
		_conn.connect();
		
		//
		OutputStream _output = new DataOutputStream(_conn.getOutputStream());
		byte[] end_data = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
		StringBuilder _sb = new StringBuilder();
		_sb.append("--");
		_sb.append(BOUNDARY);
		_sb.append("\r\n");
		_sb.append("Content-Disposition: form-data;name=\"media\";filename=\"" + uploadFile.getName() + "\"\r\n");
		_sb.append("Content-Type:application/octet-stream\r\n\r\n");
		_output.write(_sb.toString().getBytes());
		//
		DataInputStream _fileStream = new DataInputStream(new FileInputStream(uploadFile));
		IOUtils.copy(_fileStream, _output);
		_output.write("\r\n".getBytes());
		_fileStream.close();
		_output.write(end_data);
		_output.flush();
		_output.close();
		//

		InputStream _inputStream = _conn.getInputStream();
		BufferedReader _reader = new BufferedReader(new InputStreamReader(_inputStream, DEFAULT_CHARSET));
		String _buffer = null;
		_resultSB = new StringBuilder();
		while ((_buffer = _reader.readLine()) != null) {
			_resultSB.append(_buffer);
		}
		_inputStream.close();
		if (_conn != null) {
			if (https) {
				((HttpsURLConnection) _conn).disconnect();
			} else {
				((HttpURLConnection) _conn).disconnect();
			}
		}
		String _result = _resultSB.toString();
		_LOG.debug("Upload File [" + uploadFile + "] Response [" + _result + "]");
		return _result;
	}

	public static IMediaFileWrapper doDownload(String url) throws IOException {
		IMediaFileWrapper _wrapper = null;
		HttpURLConnection _conn = (HttpURLConnection) new URL(url).openConnection();
		_conn.setDoOutput(true);
		_conn.setDoInput(true);
		_conn.setUseCaches(false);
		_conn.setRequestMethod("GET");
		_conn.connect();
		if (_conn.getContentType().equalsIgnoreCase("text/plain")) {
			//
			InputStream in = _conn.getInputStream();
			BufferedReader read = new BufferedReader(new InputStreamReader(in, DEFAULT_CHARSET));
			String valueString = null;
			final StringBuffer bufferRes = new StringBuffer();
			while ((valueString = read.readLine()) != null) {
				bufferRes.append(valueString);
			}
			in.close();
			_wrapper = new IMediaFileWrapper() {

				public String getErrorMsg() {
					return bufferRes.toString();
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

				public BufferedInputStream getInputStream() {
					return null;
				}
				
			};
		} else {
			final BufferedInputStream _inputStream = new BufferedInputStream(_conn.getInputStream());
			String _header = _conn.getHeaderField("Content-disposition");
			final String _fileName = StringUtils.substringBetween(_header, "filename=\"", "\"");
			final String _simpleName = _fileName.substring(0, _fileName.lastIndexOf("."));
			final String _suffix = _fileName.substring(_fileName.length() + 1);
			final long _contentLength = new BlurObject(_conn.getHeaderField("Content-Length")).toLongValue();
			final String _contentType = _conn.getHeaderField("Content-Type");
			//
			_wrapper = new IMediaFileWrapper() {

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

				public BufferedInputStream getInputStream() {
					return _inputStream;
				}
				
			};
		}
		return _wrapper;
	}
   
}

class WeChatX509TrustManager implements X509TrustManager {

	/* (non-Javadoc)
	 * @see javax.net.ssl.X509TrustManager#getAcceptedIssuers()
	 */
	public X509Certificate[] getAcceptedIssuers() {
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.net.ssl.X509TrustManager#checkClientTrusted(java.security.cert.X509Certificate[], java.lang.String)
	 */
	public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
	}

	/* (non-Javadoc)
	 * @see javax.net.ssl.X509TrustManager#checkServerTrusted(java.security.cert.X509Certificate[], java.lang.String)
	 */
	public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
	}

}