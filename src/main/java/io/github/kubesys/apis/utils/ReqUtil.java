/**
 * Copyright (2022, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubesys.apis.utils;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

/**
 * 
 * It is used for creating various HttpRequest
 * 
 * @author wuheng@iscas.ac.cn
 * @since  0.1 
 **/
public class ReqUtil {

	
	/**********************************************************
	 * 
	 *                     Commons
	 * 
	 **********************************************************/
	
	private ReqUtil() {
		super();
	}

	
	/**********************************************************
	 * 
	 *                     BearerToken
	 * 
	 **********************************************************/

	
	/**
	 * @param req                      request
	 * @param token                    token
	 * @param body                     body
	 * @return                         request
	 */
	private static HttpRequestBase createBearerTokenRequest(HttpRequestBase req, String token, String body) {
		if (req instanceof HttpEntityEnclosingRequestBase) {
			setHttpEntity((HttpEntityEnclosingRequestBase) req, body);
		}
		setBearerHeader(req, token);
		return req;
	}
	
	/**
	 * @param req                        request
	 * @param body                       body
	 */
	private static void setHttpEntity(HttpEntityEnclosingRequestBase req, String body) {
		req.setEntity(new StringEntity(
					body == null ? "" : body, 
					ContentType.APPLICATION_JSON));
	}
	
	/**
	 * @param request                     request
	 * @param token                       token
	 */
	private static void setBearerHeader(HttpRequestBase request, String token) {
		if (token != null) {
			request.addHeader("Authorization", "Bearer " + token);
		}
		request.addHeader("Connection", "keep-alive");
	}
	
	/**
	 * @param token                       token
	 * @param uri                         uri
	 * @param body                        body
	 * @return                            request or null
	 * @throws MalformedURLException      MalformedURLException
	 */
	public static HttpPost postWithBearerToken(String token, String uri, String body) throws MalformedURLException {
		return (HttpPost) createBearerTokenRequest(new HttpPost(new URL(uri).toString()), token, body);
	}
	
	/**
	 * @param token                       token
	 * @param uri                         uri
	 * @param body                        body
	 * @return                            request or null
	 * @throws MalformedURLException      MalformedURLException
	 */
	public static HttpPut putWithBearerToken(String token, String uri, String body) throws MalformedURLException {
		return (HttpPut) createBearerTokenRequest(new HttpPut(new URL(uri).toString()), token, body);
	}
	
	/**
	 * @param token                       token
	 * @param uri                         uri
	 * @return                            request or null
	 * @throws MalformedURLException      MalformedURLException
	 */
	public static HttpDelete deleteWithBearerToken(String token, String uri) throws MalformedURLException {
		return (HttpDelete) createBearerTokenRequest(new HttpDelete(new URL(uri).toString()), token, null);
	}
	
	/**
	 * @param token                       token
	 * @param uri                         uri
	 * @return                            request or null
	 * @throws MalformedURLException      MalformedURLException
	 */
	public static HttpGet getWithBearerToken(String token, String uri) throws MalformedURLException {
		return (HttpGet) createBearerTokenRequest(new HttpGet(new URL(uri).toString()), token, null);
	}
	
	
	/**********************************************************
	 * 
	 *                     BearerToken
	 * 
	 **********************************************************/
	
	private static String createUserToken(String uri, String user, String token) {
		StringBuilder sb = new StringBuilder();
		if (uri.startsWith("http")) {
			sb.append("http://").append(user).append(":").append(token)
						.append("@").append(uri.substring("http://".length()));
		} else {
			sb.append("https://").append(user).append(":").append(token)
				.append("@").append(uri.substring("https://".length()));
		}
		return sb.toString();
	}
	
	/**
	 * @param req                      request
	 * @param token                    token
	 * @param body                     body
	 * @return                         request
	 */
	private static HttpRequestBase createUserTokenRequest(HttpRequestBase req, String body) {
		if (req instanceof HttpEntityEnclosingRequestBase) {
			setHttpEntity((HttpEntityEnclosingRequestBase) req, body);
		}
		return req;
	}
	
	/**
	 * @param user                        user
	 * @param token                       token
	 * @param uri                         uri
	 * @return                            request or null
	 * @throws MalformedURLException      MalformedURLException
	 */
	public static HttpGet getWithUserToken(String user, String token, String uri) throws MalformedURLException {
		return (HttpGet) createUserTokenRequest(new HttpGet(new URL(createUserToken(uri, user, token)).toString()), null);
	}
	
}
