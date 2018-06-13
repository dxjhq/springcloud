package com.hhly.utils.web;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * HttpClient工具类
 */
public class HttpHelper {

	private final static Logger logger = LoggerFactory.getLogger(HttpUtil.class);

	private static CloseableHttpClient httpClient = null;
	private static CloseableHttpClient httpsClient = null;
	
	static {
		RequestConfig config = RequestConfig.custom().setConnectionRequestTimeout(5000).setSocketTimeout(5000).build();
		httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
		
		//采用绕过验证的方式
		SSLContext sslContext = null;
		try {
			sslContext = createIgnoreVerifySSL();
		} catch (KeyManagementException | NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		//设置协议http和https对应的处理socket连接工程对象
		Registry<ConnectionSocketFactory> soctetFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
				.register("https", new SSLConnectionSocketFactory(sslContext)).build();
		
		PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(soctetFactoryRegistry);
		httpsClient = HttpClients.custom().setConnectionManager(connManager).build();
	}

	public static String sendPostRequestByParam(String path, String params) throws Exception {
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		conn.setRequestMethod("POST");// 提交模式
		conn.setDoOutput(true);// 是否输入参数
		byte[] bypes = params.toString().getBytes();
		conn.getOutputStream().write(bypes);// 输入参数
		InputStream inStream = conn.getInputStream();
		return new String(readInputStream(inStream));
	}

	/**
	 * 从输入流中读取数据
	 * 
	 * @param inStream
	 * @return
	 * @throws Exception
	 */
	public static byte[] readInputStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		byte[] data = outStream.toByteArray();// 网页的二进制数据
		outStream.close();
		inStream.close();
		return data;
	}

	/**
	 * httpclient 发起get请求
	 * 
	 * @param url
	 *            地址
	 * @param params
	 *            参数
	 * @param charset
	 *            字符编码
	 * @return 请求结果
	 */
	public static String doGet(String url, Map<String, String> params, String charset) {
		return doGet(url, params, null, charset);
	}

	public static String doGet(String url, Map<String, String> params, Map<String, String> headers, String charset) {
		StringBuilder builder = new StringBuilder(url);
		HttpEntity entity = null;
		CloseableHttpResponse response = null;
		HttpGet httpGet = null;
		try {
			if (null != params && !params.isEmpty()) {
				List<NameValuePair> pairs = new ArrayList<NameValuePair>(params.size());
				for (Map.Entry<String, String> entry : params.entrySet()) {
					String value = entry.getValue();
					if (null == value)
						continue;
					pairs.add(new BasicNameValuePair(entry.getKey(), value));
				}
				builder.append("?").append(EntityUtils.toString(new UrlEncodedFormEntity(pairs, charset)));
			}
			httpGet = new HttpGet(builder.toString());
			if (headers != null)
				for (Map.Entry<String, String> entry : headers.entrySet()) {
					httpGet.addHeader(entry.getKey(), entry.getValue());
				}
			response = httpClient.execute(httpGet);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200) {
				httpGet.abort();
			}
			entity = response.getEntity();
			String result = null;
			if (entity != null) {
				result = EntityUtils.toString(entity, charset);
			}
			return result;
		} catch (Exception e) {
			logger.error("httpclient execute get method 异常，msg:{}", e.getMessage(), e);
		} finally {
			close(entity, response, httpGet);
		}
		return null;
	}

	/**
	 * httpclient提交post请求
	 * 
	 * @param url
	 *            请求地址
	 * @param params
	 *            参数
	 * @param charset
	 *            字符编码
	 * @return 请求结果
	 */
	public static String doPost(String url, Map<String, String> params, String charset) {
		String result = null;
		CloseableHttpResponse response = null;
		HttpEntity entity = null;
		HttpPost httpPost = null;
		try {
			List<NameValuePair> pairs = null;
			if (params != null && !params.isEmpty()) {
				pairs = new ArrayList<NameValuePair>(params.size());
				for (Map.Entry<String, String> entry : params.entrySet()) {
					String value = entry.getValue();
					if (null == value)
						continue;
					pairs.add(new BasicNameValuePair(entry.getKey(), value));
				}
			}
			httpPost = new HttpPost(url);
			if (null != pairs && pairs.size() > 0) {
				httpPost.setEntity(new UrlEncodedFormEntity(pairs, charset));

			}
			response = httpClient.execute(httpPost);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200) {
				httpPost.abort();
			}
			entity = response.getEntity();
			if (entity != null) {
				result = EntityUtils.toString(entity, charset);
			}
		} catch (Exception e) {
			logger.error("httpclient execute post method 异常，msg:{}", e.getMessage(), e);
		} finally {
			close(entity, response, httpPost);
		}
		return result;
	}

	public static String doPostSSL(String url, Map<String, String> params, String charset) {
		String result = null;
		CloseableHttpResponse response = null;
		HttpEntity entity = null;
		HttpPost httpPost = null;
		try {
			List<NameValuePair> pairs = null;
			if (params != null && !params.isEmpty()) {
				pairs = new ArrayList<NameValuePair>(params.size());
				for (Map.Entry<String, String> entry : params.entrySet()) {
					String value = entry.getValue();
					if (null == value)
						continue;
					pairs.add(new BasicNameValuePair(entry.getKey(), value));
				}
			}
			httpPost = new HttpPost(url);
			if (null != pairs && pairs.size() > 0) {
				httpPost.setEntity(new UrlEncodedFormEntity(pairs, charset));

			}
			response = httpsClient.execute(httpPost);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200) {
				httpPost.abort();
			}
			entity = response.getEntity();
			if (entity != null) {
				result = EntityUtils.toString(entity, charset);
			}
		} catch (Exception e) {
			logger.error("httpclient execute post method 异常，msg:{}", e.getMessage(), e);
		} finally {
			close(entity, response, httpPost);
		}
		return result;
	}
	
	/**
	 * httpclient提交post请求
	 * 
	 * @param url
	 *            请求地址
	 * @param jsonParams
	 *            json请求参数
	 * @param charset
	 *            字符编码
	 * @return 请求结果
	 */
	public static String httpPostWithJSON(String url, String jsonParams, String charset) {
		String result = null;
		CloseableHttpResponse response = null;
		HttpEntity entity = null;
		HttpPost httpPost = null;
		try {
			httpPost = new HttpPost(url);
			httpPost.addHeader(HTTP.CONTENT_TYPE, "application/json");
			StringEntity se = new StringEntity(jsonParams);
			se.setContentType("text/json");
			se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
			httpPost.setEntity(se);
			response = httpClient.execute(httpPost);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200) {
				httpPost.abort();
			}
			entity = response.getEntity();
			if (entity != null) {
				result = EntityUtils.toString(entity, charset);
			}
		} catch (Exception e) {
			logger.error("httpclient execute post method 异常，msg:{}", e.getMessage(), e);
		} finally {
			close(entity, response, httpPost);
		}
		return result;
	}

	/**
	 * httpclient提交post请求
	 * 
	 * @param url
	 *            请求地址
	 * @param header
	 *            new BasicHeader(HTTP.CONTENT_TYPE, "application/xml")
	 * @param value
	 *            参数
	 * @param charset
	 *            字符编码
	 * @return 请求结果
	 */
	public static String doPost(String url, Header header, String value, String charset) {
		String result = null;
		CloseableHttpResponse response = null;
		HttpEntity entity = null;
		HttpPost httpPost = null;
		try {
			httpPost = new HttpPost(url);
			if (header != null)
				httpPost.addHeader(header);
			httpPost.setEntity(new StringEntity(value, charset));
			response = httpClient.execute(httpPost);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200) {
				httpPost.abort();
			}
			entity = response.getEntity();
			if (entity != null) {
				result = EntityUtils.toString(entity, charset);
			}
		} catch (Exception e) {
			logger.error("httpclient execute post method 异常，msg:{}", e.getMessage(), e);
		} finally {
			close(entity, response, httpPost);
		}
		return result;
	}

	/**
	 * httpclient提交delete请求
	 * 
	 * @param url
	 *            url地址
	 * @param params
	 *            参数
	 * @param charset
	 *            字符编码
	 * @return 登录结果
	 */
	public static String doDelete(String url, Map<String, String> params, String charset) {
		StringBuilder builder = new StringBuilder(url);
		HttpEntity entity = null;
		CloseableHttpResponse response = null;
		HttpDelete httpDelete = null;
		try {
			if (null != params && !params.isEmpty()) {
				List<NameValuePair> pairs = new ArrayList<NameValuePair>(params.size());
				for (Map.Entry<String, String> entry : params.entrySet()) {
					String value = entry.getValue();
					if (null == value)
						continue;
					pairs.add(new BasicNameValuePair(entry.getKey(), value));
				}
				builder.append("?").append(EntityUtils.toString(new UrlEncodedFormEntity(pairs, charset)));
			}
			httpDelete = new HttpDelete(builder.toString());
			response = httpClient.execute(httpDelete);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200) {
				httpDelete.abort();
			}
			entity = response.getEntity();
			String result = null;
			if (entity != null) {
				result = EntityUtils.toString(entity, charset);
			}
			return result;
		} catch (Exception e) {
			logger.error("httpclient execute delete method 异常，msg:{}", e.getMessage(), e);
		} finally {
			close(entity, response, httpDelete);
		}
		return null;
	}

	/**
	 * 释放资源
	 * 
	 * @param entity
	 * @param response
	 * @param httpRequestBase
	 */
	public static void close(HttpEntity entity, CloseableHttpResponse response, HttpRequestBase httpRequestBase) {
		close(response, httpRequestBase);
	}

	/**
	 * 释放资源
	 * 
	 * @param response
	 * @param httpRequestBase
	 */
	public static void close(CloseableHttpResponse response, HttpRequestBase httpRequestBase) {
		HttpClientUtils.closeQuietly(response);
		try {
			if (httpRequestBase != null)
				httpRequestBase.releaseConnection();
		} catch (Exception e) {
			logger.error("Httpclient releaseConnection httpRequestBase 异常，msg:{}", e.getMessage(), e);
		}
	}

	/**
	 * 获取 post 请求内容
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static String getRequestPostStr(HttpServletRequest request) throws Exception {
		byte buffer[] = readInputStream(request.getInputStream());
		String charEncoding = request.getCharacterEncoding();
		if (charEncoding == null) {
			charEncoding = "UTF-8";
		}
		return new String(buffer, charEncoding);
	}

	/**
	 * 获取 request 中 json 字符串的内容
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static String getRequestJsonString(HttpServletRequest request)throws Exception {
		String submitMehtod = request.getMethod();
		// GET
		if (submitMehtod.equals("GET")) {
			return new String(request.getQueryString().getBytes("iso-8859-1"),"utf-8").replaceAll("%22", "\"");
			// POST
		} else {
			return getRequestPostStr(request);
		}
	}
	
	public static SSLContext createIgnoreVerifySSL() throws NoSuchAlgorithmException, KeyManagementException {
		SSLContext sc = SSLContext.getInstance("SSLv3");

		// 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
		X509TrustManager trustManger = new X509TrustManager() {

			@Override
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			@Override
			public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			}

			@Override
			public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			}
		};

		sc.init(null, new TrustManager[] { trustManger }, null);
		return sc;
	}
}