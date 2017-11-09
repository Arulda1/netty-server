package com.auzmor.netty.netty_httpserver.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropUtils {

	//to work for all os I'm using file-path
		public static final String PROPERTIES_FILE_PATH="C:"+File.separatorChar+"Users"+File.separatorChar+"Arul"+
				File.separatorChar+"workspace"+File.separatorChar+"netty-httpserver"+File.separatorChar+"src"+
				File.separatorChar+"main"+File.separatorChar+"resources"+File.separatorChar+"server_conf.prop";
	
	public static String getProperty(String propName){
		
		Properties prop = new Properties();
		// try-with resource. no need to close the stream
		try(InputStream input=new FileInputStream(PROPERTIES_FILE_PATH)){
			prop.load(input);
			return prop.getProperty(propName);
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		return null;
	}
	
}
