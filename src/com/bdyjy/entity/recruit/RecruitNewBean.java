package com.bdyjy.entity.recruit;

import java.io.Serializable;

public class RecruitNewBean implements Serializable{

	public String getIsCollect()
	{
		return isCollect;
	}
	public void setIsCollect(String isCollect)
	{
		this.isCollect = isCollect;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 760473835483269823L;
	
	/**
	 * 根据后台返回数据定义的数据成员
	 */
	String id;
	String content;
	String position;
	String title;
	String company;
	String recPlace;
	String recTIme;
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
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getRecPlace() {
		return recPlace;
	}
	public void setRecPlace(String recPlace) {
		this.recPlace = recPlace;
	}
	public String getRecTIme() {
		return recTIme;
	}
	public void setRecTIme(String recTIme) {
		this.recTIme = recTIme;
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
	

}
