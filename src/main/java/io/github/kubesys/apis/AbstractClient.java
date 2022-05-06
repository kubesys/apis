/**
 * Copyright (2022, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubesys.apis;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import javax.net.ssl.KeyManager;
import javax.net.ssl.TrustManager;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultClientConnectionReuseStrategy;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultServiceUnavailableRetryStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.kubesys.apis.utils.SSLUtil;


/**
 * This class is used for creating a connection between users' application and target server.
 * It provides an easy-to-use way to Create, Update, Delete, Get, List and Watch all resources.
 * 
 * @author wuheng@iscas.ac.cn
 * @since  0.1
 * 
 */
public abstract class AbstractClient {

	/**
	 * m_logger
	 */
	public static final Logger m_logger = Logger.getLogger(AbstractClient.class.getName());


	/**
	 * it is used for sending requests to Kuberenetes kube-apiserver,
	 * and then receiving response from it.
	 */
	protected final HttpRequester requester;


	
	/******************************************************
	 * 
	 *  Using token
	 * 
	 ******************************************************/
	
	/**
	 * 
	 * @param url   default is https://IP:6443/
	 * @param token bearer token, you can create it using ServiceAccount and
	 *              ClusterRoleBinding
	 * @throws Exception exception
	 */
	public AbstractClient(String url, String token) throws Exception {
		this.requester = new HttpRequester(url, token);
	}
	

	 
	/**
	 * @return httpCaller, it is used for sending request to Kuberenetes and
	 *         receiving response from Kubernetes.
	 */
	public HttpRequester getRequester() {
		return requester;
	}

	/**
	 * create a new HttpCaller for each WatchResource or WatchResources API
	 * 
	 * @return httpCaller
	 * @throws Exception exception
	 */
	public HttpRequester copy() throws Exception {
			return new HttpRequester(requester.getMasterUrl(),
					             requester.getToken());
	}

	/**
	 * Http Requester
	 * 
	 * TODO consider two cases (token and cert) respectively later  
	 * 
	 * @author wuheng@iscas.ac.cn
	 * @since  2.0.0
	 *
	 */
	public static class HttpRequester {

		/**
		 * master IP
		 */
		protected String masterUrl;

		/**
		 * token
		 */
		protected String token;


		/**
		 * client
		 */
		protected final CloseableHttpClient httpClient;

		/**
		 * @param requester requester
		 * @throws Exception exception
		 */
		public HttpRequester(HttpRequester requester) throws Exception {
			this(requester.getMasterUrl(), requester.getToken());
		}

		/**
		 * @param masterUrl masterUrl
		 * @param token     token
		 * @throws Exception exception
		 */
		public HttpRequester(String masterUrl, String token) throws Exception {
			super();
			this.masterUrl = masterUrl;
			this.token = token;
			this.httpClient = createDefaultHttpClient();
		}


		/**
		 * @return httpClient
		 * @throws Exception
		 */
		protected CloseableHttpClient createDefaultHttpClient() throws Exception {

			SocketConfig socketConfig = SocketConfig.custom().setSoKeepAlive(true).setSoTimeout(0)
					.setSoReuseAddress(true).build();

			RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(0).setConnectionRequestTimeout(0)
					.setSocketTimeout(0).build();

			return createDefaultHttpClientBuilder().setConnectionTimeToLive(0, TimeUnit.SECONDS)
					.setDefaultSocketConfig(socketConfig).setDefaultRequestConfig(requestConfig)
					.setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy())
					.setConnectionReuseStrategy(new DefaultClientConnectionReuseStrategy())
					.setServiceUnavailableRetryStrategy(new DefaultServiceUnavailableRetryStrategy()).build();
		}

		/**
		 * @return builder
		 * @throws Exception
		 */
		protected HttpClientBuilder createDefaultHttpClientBuilder() throws Exception {
			HttpClientBuilder builder = HttpClients.custom();

			KeyManager[] keyManagers = null;
			TrustManager[] trustManagers = null;

			builder.setSSLHostnameVerifier(SSLUtil.createDefaultHostnameVerifier())
					.setSSLSocketFactory(SSLUtil.createSocketFactory(keyManagers, trustManagers));

			return builder;
		}

		

		public void setMasterUrl(String masterUrl) {
			this.masterUrl = masterUrl;
		}

		public void setToken(String token) {
			this.token = token;
		}
		
		

		/**
		 * @param response response
		 * @return json json
		 */
		protected synchronized JsonNode parseResponse(CloseableHttpResponse response) {

			try {
				return new ObjectMapper().readTree(response.getEntity().getContent());
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			} finally {
				if (response != null) {
					try {
						response.close();
					} catch (IOException e) {
						m_logger.severe(e.toString());
					}
				}
			}
		}

		/**
		 * @param req req
		 * @return json json
		 * @throws Exception exception
		 */
		public synchronized JsonNode getResponse(HttpRequestBase req) throws Exception {
			return parseResponse(httpClient.execute(req));
		}

		/**
		 * 
		 */
		protected void close() {
			if (httpClient != null) {
				try {
					httpClient.close();
				} catch (IOException e) {
					m_logger.warning(e.toString());
				}
			}
		}

		/**
		 * @return masterUrl
		 */
		public String getMasterUrl() {
			return masterUrl;
		}

		/**
		 * @return token
		 */
		public String getToken() {
			return token;
		}

		/**
		 * @return httpClient
		 */
		public CloseableHttpClient getHttpClient() {
			return httpClient;
		}

	}

}
