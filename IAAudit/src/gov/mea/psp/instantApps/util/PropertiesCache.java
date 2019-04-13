package gov.mea.psp.instantApps.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;

public class PropertiesCache {
	private static final Properties INSTANTAPPSPROPERTIES = new Properties();
	
	private PropertiesCache() {
		BufferedReader readerObject = new BufferedReader(
				new InputStreamReader(PropertiesCache.class.getResourceAsStream("/main/resources/instantApps.properties")));

		try {
			INSTANTAPPSPROPERTIES.load(readerObject);
			PropertyConfigurator.configure(INSTANTAPPSPROPERTIES);
			readerObject.close();
		} catch (IOException e) {
			System.out.println("Only god can save us now!!!");
			e.printStackTrace();
		}
	}	
	
	//Singleton object of Properties Cache will be created!!!
	private static class LoadPropertiesCache {
		private static final PropertiesCache INSTANCE = new PropertiesCache();
	}
	
	public static PropertiesCache loadInstance() {
		return 	LoadPropertiesCache.INSTANCE;
	}
	
	public static String getProperty(String key) {
		return INSTANTAPPSPROPERTIES.getProperty(key);
	}
	
}