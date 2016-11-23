package com.cnpc.pms.base.util;


/**
 * Keep the identity of current application instance.
 * 
 * Copyright(c) 2010 IBM Corporation, http://www.ibm.com
 * 
 * @author lushu
 * @since Apr 7, 2013
 */
public abstract class IdentityUtil {
	private static String ID;
	private static boolean setup = false;

	public static synchronized void set(String id) {
		if (false == setup) {
			ID = id;
			setup = true;
		} else {
			System.err.println("Identity has been set as: " + ID);
		}
	}

	public static String get() {
		if (true == setup) {
			return ID;
		} else {
			return "Unset ID";
		}
	}

	public static boolean setup() {
		return setup;
	}
}
