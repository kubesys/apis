/**
 * Copyright (2022, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubesys.apis.jenkins;



import com.fasterxml.jackson.databind.JsonNode;

import io.github.kubesys.apis.AbstractClient;
import io.github.kubesys.apis.utils.ReqUtil;


/**
 * This class is used for creating a connection between users' application and target server.
 * It provides an easy-to-use way to Create, Update, Delete, Get, List and Watch all resources.
 * 
 * @author wuheng@iscas.ac.cn
 * @since  0.1
 * 
 */
public class JenkinsClient extends AbstractClient {

	protected String user;
	
	public JenkinsClient(String url, String user, String token) throws Exception {
		super(url, token);
		this.user = user;
	}
	
	public JsonNode api() throws Exception {
		return this.getResponse(ReqUtil.getWithUserToken(this.user, this.getToken(), this.getMasterUrl() + "api/json"));
	}

}
