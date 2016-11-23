/**
 * 
 */
package com.cnpc.pms.exp.inf.dto;

/**
 * 专家信息导入唯一行校验辅助类
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author Song Shitao
 * @since 2011-3-29
 */
public class ExpertUniqValidation {

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
	 * Gets the fieldName
	 * 
	 * @return the fieldName
	 */
	public String getFieldName() {
		return this.fieldName;
	}

	/**
	 * sets the fieldName
	 * 
	 * @param fieldName
	 *            the fieldName to set
	 */
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	/**
	 * 类路径
	 */
	private String className;
	/**
	 * 列名（属性名）
	 */
	private String fieldName;

}
