
package com.bdyjy.entity.complaint;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.bdyjy.entity.attArryData;

/**
 * com.bdyjy.entity.complaint.complaints
 * 
 * @author holy
 */
public class Complaints implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3713627329220186523L;

	String id;
	String title;
	String contacts;
	String content;
	String typeShow;
	String statusShow;
	List<attArryData> attArry;
	ArrayList<String> filePathList;
	String createUser;
	String createTime;
	String lastUpdateTime;
	String lastUpdateUser;
	String jsonUpdateFlag;
	String mybatisRecordCount;
	String orderNo;
	String attachmentPrefix;
	String creationTime;//投诉建议时间
	
	
	







	public String getCreationTime() {
		return creationTime;
	}





	public void setCreationTime(String creationTime) {
		this.creationTime = creationTime;
	}





	public ArrayList<String> getFilePathList() {
		return filePathList;
	}





	public void setFilePathList(ArrayList<String> filePathList) {
		this.filePathList = filePathList;
	}





	public List<attArryData> getAttArry() {
		return attArry;
	}





	public void setAttArry(List<attArryData> attArry) {
		this.attArry = attArry;
	}





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





	




	public String getContacts() {
		return contacts;
	}





	public void setContacts(String contacts) {
		this.contacts = contacts;
	}





	public String getTypeShow() {
		return typeShow;
	}





	public void setTypeShow(String typeShow) {
		this.typeShow = typeShow;
	}





	public String getStatusShow() {
		return statusShow;
	}





	public void setStatusShow(String stausShow) {
		this.statusShow = stausShow;
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