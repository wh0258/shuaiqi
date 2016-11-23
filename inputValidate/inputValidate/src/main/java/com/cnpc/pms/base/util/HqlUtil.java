package com.cnpc.pms.base.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * The HqlUtil to construct a hql.
 * 
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author Zhou zaiqing
 * @since 2010/10/29
 */
public class HqlUtil {

	/**
	 * Check if the str contains the wildSearch char but cannot equals it.
	 * 
	 * @param str
	 *            the str
	 * @return true--contain. false-false.
	 */
	public static boolean containWildSearch(String str) {

		if (str.contains("*") && !("*").equals(str)) {
			return true;
		}
		return false;
	}

	/**
	 * Get All the Access Method (Get or IS) by attributeName.
	 * 
	 * @param methods
	 *            the methods
	 * @param attributeName
	 *            the attribute name
	 * @return the access method by attribute name
	 */
	private static Method getAccessMethodByAttributeName(Method[] methods, String attributeName) {

		for (int i = 0; i < methods.length; i++) {
			Method method = methods[i];
			String methodName = method.getName();
			if ((methodName.startsWith("get") && methodName.toLowerCase(Locale.CHINA).equals(
				"get" + attributeName.toLowerCase(Locale.CHINA)))
							|| (methodName.startsWith("is") && methodName.toLowerCase(Locale.CHINA).equals(
								"is" + attributeName.toLowerCase(Locale.CHINA)))) {
				return method;
			}
		}
		return null;
	}

	/**
	 * Gets the all not null attribute names.
	 * 
	 * @param o
	 *            the o
	 * @return the AttributeNames whose value is not null or not empty.
	 * 
	 * @throws IllegalAccessException
	 *             the illegal access exception
	 * @throws InvocationTargetException
	 *             the invocation target exception
	 * @author Fen Wang Get All the attribute names and its value of o. just
	 *         return the private and protected modifiers
	 */
	public static Map<String, Object> getAllNotNullAttributeNames(Object o) throws IllegalAccessException,
		InvocationTargetException {

		Field[] fields = o.getClass().getDeclaredFields();
		Method[] methods = o.getClass().getMethods();
		Map<String, Object> attrValues = new HashMap<String, Object>();

		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			// if it is a private variable or a protected modifiers.
			if (field.getModifiers() == 2 || field.getModifiers() == 4) {
				String attributeName = field.getName();
				Method method = getAccessMethodByAttributeName(methods, attributeName);
				if (method == null) {
					continue;
				}
				// get the value of attribute;
				Object value = method.invoke(o, null);
				// Just get the non-null attribute value.
				if (value == null) {
					continue;
				} else {
					attrValues.put(attributeName, value);
				}
			}
		}
		return attrValues;
	}

	/**
	 * Replace wild2 percent.
	 * 
	 * @param str
	 *            the str
	 * @return the string
	 * @author FenWang. Replace the wildSearch char to percent char.
	 */
	public static String replaceWild2Percent(String str) {
		return str.replaceAll("\\*", "%");
	}
}
