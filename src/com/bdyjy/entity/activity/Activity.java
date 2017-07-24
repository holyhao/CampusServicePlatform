package com.bdyjy.entity.activity;

import java.io.Serializable;

/**
 * 社团活动实体类
 * 
 * @author cuicui create at 2016-04-02 16:13
 *
 */
public class Activity implements Serializable {

	public String getPoster() {
		return poster;
	}

	public void setPoster(String poster) {
		this.poster = poster;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -966029528325018445L;

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * @param subject
	 *            the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * @return the hoster
	 */
	public String getHoster() {
		return hoster;
	}

	/**
	 * @param hoster
	 *            the hoster to set
	 */
	public void setHoster(String hoster) {
		this.hoster = hoster;
	}

	/**
	 * @return the acttime
	 */
	public String getActtime() {
		return acttime;
	}

	/**
	 * @param acttime
	 *            the acttime to set
	 */
	public void setActtime(String acttime) {
		this.acttime = acttime;
	}

	/**
	 * @return the actplace
	 */
	public String getActplace() {
		return actplace;
	}

	/**
	 * @param actplace
	 *            the actplace to set
	 */
	public void setActplace(String actplace) {
		this.actplace = actplace;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content
	 *            the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return the createUser
	 */
	public String getCreateUser() {
		return createUser;
	}

	/**
	 * @param createUser
	 *            the createUser to set
	 */
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	/**
	 * @return the createTime
	 */
	public String getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime
	 *            the createTime to set
	 */
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	/**
	 * @return the lastUpdateTime
	 */
	public String getLastUpdateTime() {
		return lastUpdateTime;
	}

	/**
	 * @param lastUpdateTime
	 *            the lastUpdateTime to set
	 */
	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	/**
	 * @return the lastUpdateUser
	 */
	public String getLastUpdateUser() {
		return lastUpdateUser;
	}

	/**
	 * @param lastUpdateUser
	 *            the lastUpdateUser to set
	 */
	public void setLastUpdateUser(String lastUpdateUser) {
		this.lastUpdateUser = lastUpdateUser;
	}

	public String getNewPoster() {
		return newPoster;
	}

	public void setNewPoster(String newPoster) {
		this.newPoster = newPoster;
	}

	public String getIsCollect() {
		return isCollect;
	}

	public void setIsCollect(String isCollect) {
		this.isCollect = isCollect;
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

	String id;
	String subject;// 主题
	String hoster;// 主办方
	String acttime;
	String actplace;
	String poster;
	String content;
	String createUser;
	String createTime;
	String lastUpdateTime;
	String lastUpdateUser;

	String newPoster;
	String isCollect;
	String mybatisRecordCount;
	String jsonUpdateFlag;
	String token;
	String orderNo;
	String attachmentPrefix;

}
