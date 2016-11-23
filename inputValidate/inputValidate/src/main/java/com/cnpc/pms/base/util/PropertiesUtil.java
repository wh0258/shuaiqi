package com.cnpc.pms.base.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.util.DefaultPropertiesPersister;
import org.springframework.util.PropertiesPersister;

/**
 * 
 * PMS property utility class.<br/>
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author lushu
 * @since Apr 22, 2011
 */
public class PropertiesUtil {
	public static final String DEFAULT_CONFIG_LOCATION = "/conf/application.properties";

	public static final String XML_FILE_EXTENSION = ".xml";
	public final String fileEncoding = "UTF-8";
	private final static Logger log = LoggerFactory.getLogger(PropertiesUtil.class);
	private static PropertiesUtil instance = new PropertiesUtil();
	private final Properties properties = new Properties();

	private static final String PLACEHOLDER_PREFIX = "${";// PropertyPlaceholderConfigurer.DEFAULT_PLACEHOLDER_PREFIX;
	private static final String PLACEHOLDER_SUFFIX = "}";// PropertyPlaceholderConfigurer.DEFAULT_PLACEHOLDER_SUFFIX;

	protected PropertiesUtil() {
		PropertiesPersister propertiesPersister = new DefaultPropertiesPersister();
		Resource[] resources = ConfigurationUtil.getSortedResources(DEFAULT_CONFIG_LOCATION);
		if (resources != null) {
			for (int i = 0; i < resources.length; i++) {
				Resource resource = resources[i];
				log.info("Loading properties file from {}", resource);
				InputStream is = null;
				try {
					is = resource.getInputStream();
					if (resource.getFilename().endsWith(XML_FILE_EXTENSION)) {
						propertiesPersister.loadFromXml(properties, is);
					} else {
						if (this.fileEncoding != null) {
							propertiesPersister.load(properties, new InputStreamReader(is, this.fileEncoding));
						} else {
							propertiesPersister.load(properties, is);
						}
					}
				} catch (IOException ex) {
					log.warn("Could not load properties from {}, reason:", resource, ex.getMessage());
				} finally {
					if (is != null) {
						try {
							is.close();
						} catch (IOException e) {
							log.warn("Fail to close {}, reason: ", resource, e.getMessage());
						}
					}
				}
			}
		}
	}

	/**
	 * Get Properties
	 * 
	 * @return properties
	 * 
	 */
	public static Properties getProperties() {
		return instance.properties;
	}

	/**
	 * Gets the value of properties.
	 * 
	 * @param key
	 *            the key
	 * @return the value of properties
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static String getValue(String key) {
		return instance.properties.getProperty(key);
	}

	public static int getIntValue(String key) {
		return Integer.valueOf(instance.properties.getProperty(key));
	}

	public static int getIntValue(String key, int defaultValue) {
		try {
			return getIntValue(key);
		} catch (Exception e) {
			log.error("Fail to get int value of {}", key);
			return defaultValue;
		}
	}

	public static String getPlaceHolderString(String key) {
		if (key == null) {
			return null;
		}
		return PLACEHOLDER_PREFIX + key + PLACEHOLDER_SUFFIX;
	}

	public static String getPlaceHolderKey(String s) {
		if (s == null) {
			return null;
		}
		if (s.startsWith(PLACEHOLDER_PREFIX) && s.endsWith(PLACEHOLDER_SUFFIX)) {
			return s.substring(PLACEHOLDER_PREFIX.length(), s.length() - PLACEHOLDER_SUFFIX.length());
		} else {
			return s;
		}
	}

}
