package com.cnpc.pms.base.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author Zhou zaiqing
 * @since 2010/10/29
 */
public class DateUtil extends DateUtils {

	public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd hh:mm:ss";

	private static final Logger log = LoggerFactory.getLogger(DateUtil.class);

	/**
	 * Parse string date to Date type object.
	 * 
	 * @param strDate
	 *            the str date
	 * @return parsed date object.
	 * @throws ParseException
	 *             the parse exception
	 */
	public static Date parseDate(String strDate) throws ParseException {
		return parseDate(strDate, DEFAULT_DATE_FORMAT);
	}

	/**
	 * Parse date string to Date type object with specify pattern.
	 * 
	 * @param strDate
	 *            the str date
	 * @param pattern
	 *            the pattern
	 * @return parsed date object.
	 * @throws ParseException
	 *             the parse exception
	 */
	public static Date parseDate(String strDate, String pattern) throws ParseException {
		try {
			SimpleDateFormat format = new SimpleDateFormat(pattern);
			Date d = format.parse(strDate);
			return d;
		} catch (ParseException e) {
			log.error("Fail to parse Date from String:[{}], with Pattern:[{}]", strDate, pattern);
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * Less equal.
	 * 
	 * @param d1
	 *            the d1
	 * @param d2
	 *            the d2
	 * @return true, if and only if d1 less equal than d2.
	 */
	public static boolean le(Date d1, Date d2) {
		if (d1 == null || d2 == null) {
			return false;
		}
		return d1.before(d2) || d1.equals(d2);
	}

	/**
	 * Less equal.
	 * 
	 * @param c1
	 *            the c1
	 * @param c2
	 *            the c2
	 * @return true, if and only if c1 less equal than c2.
	 */
	public static boolean le(Calendar c1, Calendar c2) {
		if (c1 == null || c2 == null) {
			return false;
		}
		return c1.before(c2) || c1.equals(c2);
	}

	/**
	 * Date2 long.
	 * 
	 * @param d
	 *            the Date
	 * @return Returns the number of milliseconds since January 1, 1970, 00:00:00 GMT
	 *         represented by this Date object.If d is null, returns null.
	 */
	public static Long date2Long(Date d) {
		if (d == null) {
			return null;
		}
		return d.getTime();
	}

	/**
	 * Long2 date.
	 * 
	 * @param time
	 *            milliseconds
	 * @return the date
	 */
	public static Date long2Date(Long time) {
		if (time == null) {
			return null;
		}
		return new Date(time);
	}

	/**
	 * Long2 calendar.
	 * 
	 * @param time
	 *            the milliseconds
	 * @param tz
	 *            the TimeZone
	 * @return the calendar.If time or timeZone is null, returns null.
	 */
	public static Calendar long2Calendar(Long time, TimeZone tz) {
		if (time == null || tz == null) {
			return null;
		}
		Calendar c = Calendar.getInstance(tz);
		c.setTimeInMillis(time);
		return c;
	}

	/**
	 * Calendar2 long.
	 * 
	 * @param c
	 *            the Calendar
	 * @return this Calendar's time value in milliseconds.If c is null, returns null.
	 */
	public static Long calendar2Long(Calendar c) {
		if (c == null) {
			return null;
		}
		return c.getTimeInMillis();
	}

	public static Object getLastMilliSecond(Object date) throws SecurityException, NoSuchMethodException,
		IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
		Calendar cal = Calendar.getInstance();
		cal.setTime((Date) date);
		cal.add(Calendar.DATE, 1);
		cal.add(Calendar.MILLISECOND, -1);
		Constructor<?> constructor = date.getClass().getConstructor(long.class);
		Object newDate = constructor.newInstance(cal.getTimeInMillis());
		return newDate;
	}
}
