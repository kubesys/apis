/**
 * Copyright (2022, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubesys.apis.jenkins;



import java.util.Base64;

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

	static {
		mapper.put("getVersion", "");
		mapper.put("getUser", "/user/");
		mapper.put("getInstalledPlugins", "/pluginManager/pluginsSearch?query=&limit=1000");
	}
	
	public JenkinsClient(String url, String user, String token) throws Exception {
		super(url, Base64.getUrlEncoder().encodeToString((user + ":" + token).getBytes()));
	}
	
	public JsonNode getVersion() throws Exception {
		return this.getHeader(ReqUtil.getWithBasicToken(this.token, 
				this.getMasterUrl() + "/api/json"), "X-Jenkins");
	}
	
	public JsonNode getUser(String user) throws Exception {
		return this.getResponse(ReqUtil.getWithBasicToken(this.token, 
				this.getMasterUrl() + mapper.get("getUser") + user + "/api/json"));
	}
	
	public JsonNode getInstalledPlugins() throws Exception {
		return this.getResponse(ReqUtil.getWithBasicToken(this.token, 
				this.getMasterUrl() + mapper.get("getInstalledPlugins")));
	}

}
