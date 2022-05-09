/**
 * 
 */
package io.github.kubesys.apis.jenkins;

import io.github.kubesys.apis.ClientFactory;

/**
 * @author henry
 *
 */
public class JenkinsTest {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		JenkinsClient client = ClientFactory.createJenkinsClient();
		System.out.println(client.getVersion().toPrettyString());
		System.out.println(client.getUser("admin").toPrettyString());
		System.out.println(client.getInstalledPlugins().toPrettyString());
	}

}
