/**
 * 
 */
package com.example.demo;

import java.io.File;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author pjasw
 *
 */
@ConfigurationProperties(prefix = "datastax.astra")
public class DataStaxConnectionProperties {

	private File secureConnectBundle;

	public File getSecureConnectBundle() {
		return secureConnectBundle;
	}

	public void setSecureConnectBundle(File secureConnectBundle) {
		this.secureConnectBundle = secureConnectBundle;
		System.out.println("set called");
	}
	
	
	
}
