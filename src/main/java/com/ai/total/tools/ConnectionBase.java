package com.ai.total.tools;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class ConnectionBase {
	public static Connection conn = null;
	public static Properties props = null;

	static {
		String classpath = ConnectionBase.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		classpath = classpath.substring(0, classpath.lastIndexOf("/"));
		File file = new File(classpath + "/conf/jdbc.Properties");
		props = new Properties();
		try {
			props.load(new FileInputStream(file));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void setDataSource(String index) throws Exception {
		if (index.equals("j")) {
			Class.forName(props.getProperty("jdbc_driverClassName"));
			conn = DriverManager.getConnection(props.getProperty("jdbc_url"), props.getProperty("jdbc_username"), props.getProperty("jdbc_password"));
			System.out.println("connect db " + index + " : " + props.getProperty("jdbc_url"));
		} else if (index.equals("h")) {
			Class.forName(props.getProperty("hqdriver"));
			conn = DriverManager.getConnection(props.getProperty("hqurl"), props.getProperty("hqusername"), props.getProperty("hqpassword"));
			System.out.println("connect db " + index + " : " + props.getProperty("hqurl"));
		}
	}

	public static void setDataSource2(String index) throws Exception {
		URL url = ConnectionBase.class.getProtectionDomain().getCodeSource().getLocation();
		String path;
		path = URLDecoder.decode(url.getPath(), "utf-8");
		if (path.endsWith(".jar"))
			path = path.substring(0, path.lastIndexOf("/") + 1);
		Class.forName("org.h2.Driver");
		String h2url = "jdbc:h2:file:" + path + "/" + index;
		String h2user = "sonar";
		String h2pass = "sonar";
		conn = DriverManager.getConnection(h2url, h2user, h2pass);
		System.out.println("connect to h2 database ok " + h2url);
	}
}
