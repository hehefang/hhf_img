package com.hhf.img.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class ConfigFactory {
	private static Logger logger = LoggerFactory.getLogger(ConfigFactory.class);
	private static Config config = null;

	private ConfigFactory() {

	}

	public static final Config getConfig() {
		if (config == null) {
			initConfig();
		}

		return config;
	}

	private synchronized static void initConfig() {
		if (config != null)
			return;

		config = new Config();
		String activeProfile = System.getProperty("spring.profiles.active");
		if (activeProfile == null)
			activeProfile = "develop";

		Resource resource = new ClassPathResource("/fileupload.properties");
		InputStream is = null;
		Properties properties = new Properties();

		try {
			is = resource.getInputStream();
			properties.load(is);

			String sFileSizeMax = getProperty(properties, "fileSizeMax",
					activeProfile);
			String fileSavePath = getProperty(properties, "fileSavePath",
					activeProfile);
			String globalSearchPath = getProperty(properties,
					"globalSearchPath", activeProfile);

			if (sFileSizeMax != null) {
				long fileSizeMax = Long.parseLong(sFileSizeMax);
				config.setFileSizeMax(fileSizeMax * 1024 * 1024);
			}

			if (fileSavePath != null) {
				config.setFileSavePath(fileSavePath);
			}

			if (globalSearchPath != null) {
				config.setGlobalSearchPath(globalSearchPath);
			}

		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} catch (NumberFormatException e) {
			logger.error(e.getMessage(), e);
		}
	}

	private static String getProperty(Properties properties, String key,
			String activeProfile) {

		String ret = null;

		if (activeProfile != null)
			ret = properties.getProperty(activeProfile + "." + key);

		if (ret == null)
			ret = properties.getProperty(key);

		return ret;
	}

}
