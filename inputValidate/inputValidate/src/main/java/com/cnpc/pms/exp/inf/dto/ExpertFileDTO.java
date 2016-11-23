/**
 * 
 */
package com.cnpc.pms.exp.inf.dto;

import java.util.Date;

/**
 * 处理专家附件和系统附件表的关联关系
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author yangfei
 * @since 2011-4-2
 */
public class ExpertFileDTO {
	/** The serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The expert file id. */
	private Long expertFileId;

	/** 是否为照片. */
	private Long isPhoto;

	/** 附件描述. */
	private String fileDesc;

	/** 附件编号-对应pmsFile的id. */
	private String attId;

	/** 专家编号. */
	private Long expId;

	/** The business id. */
	private Long businessId;

	/** The file size. */
	private Long fileSize;

	/** The file suffix. */
	private String fileSuffix;

	/** The last uploaded. */
	private Date lastUploaded;

	/** The name. */
	private String name;

	/** The file path. */
	private String filePath;

	/**
	 * Gets the attId.
	 * 
	 * @return the attId
	 */
	public String getAttId() {
		return this.attId;
	}

	/**
	 * Sets the attId.
	 * 
	 * @param attId
	 *            the attId to set
	 */
	public void setAttId(String attId) {
		this.attId = attId;
	}

	/**
	 * Gets the expId.
	 * 
	 * @return the expId
	 */
	public Long getExpId() {
		return this.expId;
	}

	/**
	 * Sets the expId.
	 * 
	 * @param expId
	 *            the expId to set
	 */
	public void setExpId(Long expId) {
		this.expId = expId;
	}

	/**
	 * Gets the business id.
	 * 
	 * @return the businessId
	 */
	public Long getBusinessId() {
		return this.businessId;
	}

	/**
	 * Sets the business id.
	 * 
	 * @param businessId
	 *            the businessId to set
	 */
	public void setBusinessId(Long businessId) {
		this.businessId = businessId;
	}

	/**
	 * Gets the file size.
	 * 
	 * @return the fileSize
	 */
	public Long getFileSize() {
		return this.fileSize;
	}

	/**
	 * Sets the file size.
	 * 
	 * @param fileSize
	 *            the fileSize to set
	 */
	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	/**
	 * Gets the file suffix.
	 * 
	 * @return the fileSuffix
	 */
	public String getFileSuffix() {
		return this.fileSuffix;
	}

	/**
	 * Sets the file suffix.
	 * 
	 * @param fileSuffix
	 *            the fileSuffix to set
	 */
	public void setFileSuffix(String fileSuffix) {
		this.fileSuffix = fileSuffix;
	}

	/**
	 * Gets the last uploaded.
	 * 
	 * @return the lastUploaded
	 */
	public Date getLastUploaded() {
		return this.lastUploaded;
	}

	/**
	 * Sets the last uploaded.
	 * 
	 * @param lastUploaded
	 *            the lastUploaded to set
	 */
	public void setLastUploaded(Date lastUploaded) {
		this.lastUploaded = lastUploaded;
	}

	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Sets the name.
	 * 
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the file path.
	 * 
	 * @return the filePath
	 */
	public String getFilePath() {
		return this.filePath;
	}

	/**
	 * Sets the file path.
	 * 
	 * @param filePath
	 *            the filePath to set
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	/**
	 * Gets the expert file id.
	 * 
	 * @return the expertFileId
	 */
	public Long getExpertFileId() {
		return this.expertFileId;
	}

	/**
	 * Sets the expert file id.
	 * 
	 * @param expertFileId
	 *            the expertFileId to set
	 */
	public void setExpertFileId(Long expertFileId) {
		this.expertFileId = expertFileId;
	}

	/**
	 * Gets the checks if is photo.
	 * 
	 * @return the isPhoto
	 */
	public Long getIsPhoto() {
		return this.isPhoto;
	}

	/**
	 * Sets the checks if is photo.
	 * 
	 * @param isPhoto
	 *            the isPhoto to set
	 */
	public void setIsPhoto(Long isPhoto) {
		this.isPhoto = isPhoto;
	}

	/**
	 * Gets the file desc.
	 * 
	 * @return the fileDesc
	 */
	public String getFileDesc() {
		return this.fileDesc;
	}

	/**
	 * Sets the file desc.
	 * 
	 * @param fileDesc
	 *            the fileDesc to set
	 */
	public void setFileDesc(String fileDesc) {
		this.fileDesc = fileDesc;
	}

}
