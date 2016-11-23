package com.cnpc.pms.base.util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.annotations.DigesterLoader;
import org.apache.commons.digester.annotations.DigesterLoaderBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.xml.sax.InputSource;


/**
 * Configuration File Read Utility.<br/>
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author Zhou zaiqing
 * @author lushu
 * @since 2010/11/19
 */
public final class ConfigurationUtil {

	/**
	 * This is the package holds default configuration.
	 */
	public static final String DEFAULT_CONFIG_PACKAGE = "pmsbase";
	private static Logger log = LoggerFactory.getLogger(ConfigurationUtil.class);
	private static PathMatchingResourcePatternResolver loader = new PathMatchingResourcePatternResolver();
	private static String dominator;

	/**
	 * Parse Configuration Object from configuration file and specified Object type.
	 * 
	 * @param <T>
	 *            Generic type of mapping class.
	 * @param clazz
	 *            the clazz
	 * @param resource
	 *            Spring Resource
	 * @return Configured Object Instance.
	 * @throws UtilityException
	 */
	@SuppressWarnings("unchecked")
	public static <T> T parseXMLObject(Class<T> clazz, Resource resource) throws Exception {
		try {
			InputStream is = resource.getInputStream();
			InputSource inputSource = new InputSource(is);
			DigesterLoader digesterLoader = new DigesterLoaderBuilder().useDefaultAnnotationRuleProviderFactory()
					.useDefaultDigesterLoaderHandlerFactory();
			Digester digester = digesterLoader.createDigester(clazz);
			return (T) digester.parse(inputSource);
		} catch (Exception e) {
			log.error("Fail to transfer Java Object[class: {}] from {}, reason: ", clazz.getSimpleName(), resource);
			e.printStackTrace();
			throw new Exception("Fail to parse XML Object of " + resource, e);
		}
	}

	/**
	 * Get single resource with pattern like "classpath*:/conf/logging.xml"
	 * Used for initialize data (config xml and scripts), quartz cluster, slf4j setting
	 * These files can work stand alone and no overwritten needed.
	 * 
	 * The order is:
	 * 1. file or the dominating jar
	 * 2. jar: {@link #DEFAULT_CONFIG_PACKAGE}.
	 * 3. any other first jar
	 * 
	 * @param location
	 *            location of the resource
	 * @return null if fail to find resource
	 */
	public static Resource getSingleResource(String location) {
		location = prepareLocation(location);
		Resource activeResource = null;
		try {
			Resource[] resources = loader.getResources(location);
			if (resources.length == 0) {
				log.warn("Can't find any [{}] :", location);
				return null;
			}
			if (resources.length > 1) {
				for (Resource resource : resources) {
					if (isDominator(resource)) {// resource.getURL().getProtocol().equals("file")
						// activeResource = resource;
						// break;
						return resource;
					}
				}
				for (Resource resource : resources) {
					if (resource.getURL().toString().indexOf(DEFAULT_CONFIG_PACKAGE) > -1) {
						// activeResource = resource;
						return resource;
					}
				}
			}
			activeResource = resources[0];
			log.debug("Use [{}] at {}", location, activeResource.getURI());
		} catch (IOException e) {
			log.error("Fail to find [{}] :", location, e);
		}
		return activeResource;
	}

	/**
	 * Get ordered resource with pattern like "classpath*:/conf/logging.xml"
	 * The order is:
	 * 1. jar: {@link #DEFAULT_CONFIG_PACKAGE}.
	 * 2. other jars
	 * 3. file.
	 * 
	 * @param location
	 * @return
	 */
	public static Resource[] getSortedResources(String location) {
		location = prepareLocation(location);
		Resource[] resources = null;
		try {
			resources = loader.getResources(location);
			if (resources.length == 0) {
				log.warn("Can't find any [{}] :", location);
				return null;
			}
			int flag = 0;
			boolean handledFile = false;// Treat the file dependence in Eclipse editor mode.
			if (resources.length > 1) {
				for (int i = 0; (i < resources.length || flag < 2); i++) {
					Resource resource = resources[i];
					// resource.getURL().getProtocol().equals("file")
					if (isDominator(resource) && handledFile == false) {//
						if (i != resources.length - 1) {
							resources[i] = resources[resources.length - 1];
							resources[resources.length - 1] = resource;
							handledFile = true;
						}
					} else if (resource.getURI().toString().indexOf(DEFAULT_CONFIG_PACKAGE) > -1 && i != 0) {
						resources[i] = resources[0];
						resources[0] = resource;
					}
					flag++;
				}
			}
			log.debug("Get [{}] configuration at {}", location, resources);
		} catch (IOException e) {
			log.error("Fail to find [{}] :", location, e);
		}
		return resources;
	}

	public static Resource[] getAllResources(String location) {
		location = prepareLocation(location);
		Resource[] resources = null;
		try {
			resources = loader.getResources(location);
			log.debug("Get [{}] configuration at {}", location, resources);
		} catch (IOException e) {
			log.error("Fail to find [{}] :", location, e);
		}
		return resources;
	}

	/**
	 * Add "classpath*:" before location.
	 * 
	 * @param location
	 * @return
	 */
	private static String prepareLocation(String location) {
		if (location.startsWith("classpath") == false) {
			location = "classpath*:" + location;
		}
		return location;
	}

	private static boolean isDominator(Resource resource) throws IOException {
		if (dominator == null) {
			return resource.getURL().getProtocol().equals("file");
		} else {
			// NOTE in the Eclipse mode, depended projects may involve more than one "file" configuration.
			// Should be fixed later.
			if (resource.getURL().getProtocol().equals("file")) {
				return true;
			}
			return resource.getURL().getFile().contains(dominator);
		}
	}

	public static void setDominator(String d) {
		if (dominator == null) {
			log.info("Set Dominative Configurer Path: {}", d);
			dominator = d;
		} else {
			log.warn("The dominator has already bean set as {}, and now try to set as {} which will be ignored.",
					dominator, d);
		}
	}
}
