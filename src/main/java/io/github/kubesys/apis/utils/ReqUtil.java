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
 * It is used for creating get, post, put and delete HttpRequest on various authorizations.
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
	 *                     Common
	 * 
	 **********************************************************/
	
	private static void setHttpTokenHeader(HttpRequestBase request, String token) {
		request.addHeader("Authorization", token);
		request.addHeader("Connection", "keep-alive");
	}
	
	/**
	 * @param req                        request
	 * @param body                       body
	 */
	private static void setHttpBodyEntity(HttpRequestBase req, String body) {
		if (req instanceof HttpEntityEnclosingRequestBase) {
			((HttpEntityEnclosingRequestBase) req).setEntity(
					new StringEntity(body == null ? "" : body, ContentType.APPLICATION_JSON));
		}
	}
	
	/**********************************************************
	 * 
	 *                     Bearer Token
	 * 
	 **********************************************************/

	
	/**
	 * @param req                      request
	 * @param token                    token
	 * @param body                     body
	 * @return                         request
	 */
	private static HttpRequestBase createBearerTokenRequest(HttpRequestBase req, String token, String body) {
		setHttpTokenHeader(req, "Bearer " + token);
		setHttpBodyEntity(req, body);
		return req;
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
	 *                     Basic Token
	 * 
	 **********************************************************/
	
	public static String toBasicToken(String user, String token) {
		return null;
	}
	
	
	
	/**
	 * @param req                      request
	 * @param token                    token
	 * @param body                     body
	 * @return                         request
	 */
	private static HttpRequestBase createBasicTokenRequest(HttpRequestBase req, String token, String body) {
		setHttpTokenHeader(req, "Basic " + token);
		setHttpBodyEntity(req, body);
		return req;
	}
	
	/**
	 * @param user                        user
	 * @param token                       token
	 * @param uri                         uri
	 * @return                            request or null
	 * @throws MalformedURLException      MalformedURLException
	 */
	public static HttpGet getWithBasicToken(String token, String uri) throws MalformedURLException {
		return (HttpGet) createBasicTokenRequest(new HttpGet(new URL(uri).toString()), token , null);
	}
	
	/**
	 * @param token                       token
	 * @param uri                         uri
	 * @param body                        body
	 * @return                            request or null
	 * @throws MalformedURLException      MalformedURLException
	 */
	public static HttpPost postWithBasicToken(String token, String uri, String body) throws MalformedURLException {
		return (HttpPost) createBasicTokenRequest(new HttpPost(new URL(uri).toString()), token, body);
	}
	
	/**
	 * @param token                       token
	 * @param uri                         uri
	 * @param body                        body
	 * @return                            request or null
	 * @throws MalformedURLException      MalformedURLException
	 */
	public static HttpPut putWithBasicToken(String token, String uri, String body) throws MalformedURLException {
		return (HttpPut) createBasicTokenRequest(new HttpPut(new URL(uri).toString()), token, body);
	}
	
	/**
	 * @param token                       token
	 * @param uri                         uri
	 * @return                            request or null
	 * @throws MalformedURLException      MalformedURLException
	 */
	public static HttpDelete deleteWithBasicToken(String token, String uri) throws MalformedURLException {
		return (HttpDelete) createBasicTokenRequest(new HttpDelete(new URL(uri).toString()), token, null);
	}
	
}
