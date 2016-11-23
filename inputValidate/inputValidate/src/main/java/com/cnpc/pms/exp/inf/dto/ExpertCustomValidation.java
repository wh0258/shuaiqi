/**
 * 
 */
package com.cnpc.pms.exp.inf.dto;

/**
 * 专家信息导入自定义验证辅助类
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author Song Shitao
 * @since 2011-3-29
 */
public class ExpertCustomValidation {
	/**
	 * Gets the className
	 * 
	 * @return the className
	 */
	public String getClassName() {
		return this.className;
	}

	/**
	 * sets the className
	 * 
	 * @param className
	 *            the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
	}

	/**
	 * Gets the methodName
	 * 
	 * @return the methodName
	 */
	public String getMethodName() {
		return this.methodName;
	}

	/**
	 * sets the methodName
	 * 
	 * @param methodName
	 *            the methodName to set
	 */
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	/**
	 * 类路径
	 */
	private String className;
	/**
	 * 方法名
	 */
	private String methodName;
}
