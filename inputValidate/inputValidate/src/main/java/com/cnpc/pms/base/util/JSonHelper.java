package com.cnpc.pms.base.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JSonHelper {

	final static Logger log = LoggerFactory.getLogger(JSonHelper.class);

	private final static Map<Class<?>, Class<?>> PRIMITIVE_MAP = new HashMap<Class<?>, Class<?>>();
	static {
		PRIMITIVE_MAP.put(float.class, Float.class);
		PRIMITIVE_MAP.put(double.class, Double.class);
		PRIMITIVE_MAP.put(int.class, Integer.class);
		PRIMITIVE_MAP.put(long.class, Long.class);
		PRIMITIVE_MAP.put(byte.class, Byte.class);
		PRIMITIVE_MAP.put(short.class, Short.class);
		PRIMITIVE_MAP.put(char.class, Character.class);
		PRIMITIVE_MAP.put(boolean.class, Boolean.class);
	}
	private final static Set<Class<?>> NUMBER_MAP = new HashSet<Class<?>>(PRIMITIVE_MAP.values());
	static {
		NUMBER_MAP.add(BigDecimal.class);
		NUMBER_MAP.add(BigInteger.class);
	}

	public static Class<?> getClass(String className) throws ClassNotFoundException {
		if (className.indexOf('.') == -1) {
			if (className.equalsIgnoreCase("date")) {
				className = "java.util." + StrUtil.capitalize(className);
			} else {
				className = "java.lang." + StrUtil.capitalize(className);
			}
		}
		Class<?> clazz = Class.forName(className);
		return clazz;
	}

	public static Object getValue(String type, Object rawValue) throws SecurityException, IllegalArgumentException,
		NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException,
		ClassNotFoundException, ParseException {
		return getValue(getClass(type), rawValue);
	}

	public static Object getValue(Class<?> type, Object rawValue) throws SecurityException, IllegalArgumentException,
		NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException,
		ParseException {
		Object value;
		int flag = 0;
		if (java.util.Date.class.equals(type) || java.util.Date.class.equals(type.getSuperclass())) {
			value = getDiversedDateValue(rawValue, type);
			flag = 1;
		} else if (java.lang.String.class.equals(type)) {
			value = getString(rawValue);
			flag = 2;
		} else if (type.isPrimitive()) {
			value = getNumberValue(rawValue, getWrappedClass(type));
			flag = 3;
		} else if (NUMBER_MAP.contains(type)) {
			value = getNumberValue(rawValue, type);
			flag = 4;
		} else {
			value = rawValue;
		}
		log.debug("Set [{},{}] with value {}", new Object[] {type, flag, value });
		return value;
	}

	// XXX should use better way to transfer from json date to a java date.
	private static Object getDiversedDateValue(Object raw, Class<?> clazz) throws SecurityException,
		NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException,
		InvocationTargetException, ParseException {
		Object tmp;
		if (raw.toString().indexOf(':') > 0) {
			tmp = DateUtil.parseDate(raw.toString(), "yyyy-MM-dd HH:mm:ss");
		} else {
			tmp = DateUtil.parseDate(raw.toString(), "yyyy-MM-dd");
		}
		long time = ((Date) tmp).getTime();
		Constructor<?> constructor = clazz.getConstructor(long.class);
		Object value = constructor.newInstance(time);
		return value;
	}

	private static Class<?> getWrappedClass(Class<?> clazz) {
		Class<?> wrappedClass = PRIMITIVE_MAP.get(clazz);
		log.debug("original class [{}], wrapped with [{}]", clazz, wrappedClass);
		return wrappedClass;
	}

	private static Object getNumberValue(Object raw, Class<?> clazz) throws SecurityException, NoSuchMethodException,
		IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
		String tmp = getString(raw);
		if (tmp.length() == 0)
			return null;
		Constructor<?> constructor = clazz.getConstructor(String.class);
		Object value = constructor.newInstance(tmp);
		return value;
	}

	private static String getString(Object o) {
		return String.valueOf(o).trim();
	}

}
