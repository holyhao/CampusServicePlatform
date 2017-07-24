package com.bdyjy.entity.assistant;

import java.io.Serializable;

public class AssistantNewBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5318377418366871030L;
	
	String id;
	String content;
	String asiNum;
	String closeDate;
	String sector;
	String title;
	String lastUpdateTime;
	String lastUpdateUser;
	String createUser;
	String createTime;
	String orderNo;
	String mybatisRecordCount;
	String jsonUpdateFlag;
	String token;
	String attachmentPrefix;
	String isCollect;
	
	
	/**
	 * getters and setters
	 * 
	 */
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	public String getAsiNum() {
		return asiNum;
	}
	public void setAsiNum(String asiNum) {
		this.asiNum = asiNum;
	}
	public String getCloseDate() {
		return closeDate;
	}
	public void setCloseDate(String closeDate) {
		this.closeDate = closeDate;
	}
	public String getSector() {
		return sector;
	}
	public void setSector(String sector) {
		this.sector = sector;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getLastUpdateTime() {
		return lastUpdateTime;
	}
	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	public String getLastUpdateUser() {
		return lastUpdateUser;
	}
	public void setLastUpdateUser(String lastUpdateUser) {
		this.lastUpdateUser = lastUpdateUser;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getMybatisRecordCount() {
		return mybatisRecordCount;
	}
	public void setMybatisRecordCount(String mybatisRecordCount) {
		this.mybatisRecordCount = mybatisRecordCount;
	}
	public String getJsonUpdateFlag() {
		return jsonUpdateFlag;
	}
	public void setJsonUpdateFlag(String jsonUpdateFlag) {
		this.jsonUpdateFlag = jsonUpdateFlag;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getAttachmentPrefix() {
		return attachmentPrefix;
	}
	public void setAttachmentPrefix(String attachmentPrefix) {
		this.attachmentPrefix = attachmentPrefix;
	}

	/**
	 * @return the isCollect
	 */
	public String getIsCollect()
	{
		return isCollect;
	}

	/**
	 * @param isCollect the isCollect to set
	 */
	public void setIsCollect(String isCollect)
	{
		this.isCollect = isCollect;
	}
	
}
