
package com.bdyjy.entity.campusguide;

import java.io.Serializable;

/**
 *  com.bdyjy.entity.campusguide;
 * 
 * @author holy
 */
public class CampusGuides implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8199600076738824400L;
	
	

	String id;
	String title;
	String content;
	String typeShow;
	String createUser;
	String createTime;
	String lastUpdateTime;
	String lastUpdateUser;
	String jsonUpdateFlag;
	String mybatisRecordCount;
	String orderNo;
	String attachmentPrefix;
	
	
	


	public String getId() {
		return id;
	}





	public void setId(String id) {
		this.id = id;
	}





	public String getTitle() {
		return title;
	}





	public void setTitle(String title) {
		this.title = title;
	}





	public String getContent() {
		return content;
	}





	public void setContent(String content) {
		this.content = content;
	}






	public String getTypeShow() {
		return typeShow;
	}





	public void setTypeShow(String typeShow) {
		this.typeShow = typeShow;
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





	public String getJsonUpdateFlag() {
		return jsonUpdateFlag;
	}





	public void setJsonUpdateFlag(String jsonUpdateFlag) {
		this.jsonUpdateFlag = jsonUpdateFlag;
	}





	public String getMybatisRecordCount() {
		return mybatisRecordCount;
	}





	public void setMybatisRecordCount(String mybatisRecordCount) {
		this.mybatisRecordCount = mybatisRecordCount;
	}





	public String getOrderNo() {
		return orderNo;
	}





	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}





	public String getAttachmentPrefix() {
		return attachmentPrefix;
	}





	public void setAttachmentPrefix(String attachmentPrefix) {
		this.attachmentPrefix = attachmentPrefix;
	}





	public static long getSerialversionUID(){
	
		return serialVersionUID;
	}


}