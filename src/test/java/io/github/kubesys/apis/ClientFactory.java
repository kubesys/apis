/**
 * Copyright (2020, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubesys.apis;

import io.github.kubesys.apis.jenkins.JenkinsClient;

/**
 * @author wuheng09@gmail.com
 *
 */
public class ClientFactory {

	static String JENKINS_URL = "http://39.100.91.95:31017";
	
	static String JENKINS_TOKEN = System.getenv("JENKINS_TOKEN");

	public static JenkinsClient createJenkinsClient() throws Exception {
		return new JenkinsClient(JENKINS_URL, "admin", JENKINS_TOKEN);
	}
}
