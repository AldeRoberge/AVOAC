package util;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class PropertyLAS {

	public static final String fileName = "myprops.properties";

	public static void saveProperty(String key, String value) {
		PropertiesConfiguration config;
		try {
			config = new PropertiesConfiguration(fileName);
			config.setProperty(key, value);
			config.save();
		} catch (ConfigurationException e) {
			ErrorDialog.displayError(e, "Could not set property " + key);
			e.printStackTrace();
		}
	}

	public static String getProperty(String key) {
		try {
			PropertiesConfiguration config = new PropertiesConfiguration(fileName);
			String value = (String) config.getProperty(key);
			return value;
		} catch (ConfigurationException e) {
			ErrorDialog.displayError(e, "Could not get property " + key);
			e.printStackTrace();
		}
		return null;
	}
}
