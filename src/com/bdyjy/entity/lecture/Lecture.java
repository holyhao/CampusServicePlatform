package com.bdyjy.entity.lecture;

import java.io.Serializable;

import android.net.Uri;

public class Lecture implements Serializable {
	/**
	 * com.bdyjy.entity.Lecture
	 * 
	 * @author cuicui
	 * 
	 *         create at 2016-3-26 上午11:10
	 */
	private static final long serialVersionUID = -3868764817817785351L;

	/**
	 * 
	 * @return id
	 */
	public String getId() {
		return id;
	}

	/**
	 * 
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 
	 * @return title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * 
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * 
	 * @return speaker
	 */
	public String getSpeaker() {
		return speaker;
	}

	/**
	 * 
	 * @param speaker
	 *            the speaker to set
	 */
	public void setSpeaker(String speaker) {
		this.speaker = speaker;
	}

	/**
	 * 
	 * @return poster
	 */
	public String getPoster() {
		return poster;
	}

	/**
	 * 
	 * @param poster
	 *            the poster to set
	 */
	public void setPoster(String poster) {
		this.poster = poster;
	}

	/**
	 * 
	 * @return lecTime
	 */
	public String getLecTime() {
		return lecTime;
	}

	/**
	 * 
	 * @param lecTime
	 *            the lecTime to set
	 */
	public void setLecTime(String lecTime) {
		this.lecTime = lecTime;
	}

	/**
	 * 
	 * @return lecLoPlace
	 */

	public String getLecPlace() {
		return lecPlace;
	}

	/**
	 * 
	 * @param lecLoPlace
	 *            the lecLoPlace to set
	 */

	public void setLecPlace(String lecPlace) {
		this.lecPlace = lecPlace;
	}

	/**
	 * 
	 * @return createTime
	 */
	public String getCreateTime() {
		return createTime;
	}

	/**
	 * 
	 * @param createTime
	 *            the createTime to set
	 */
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	/**
	 * 
	 * @return lastUpdateTime
	 */
	public String getLastUpdateTime() {
		return lastUpdateTime;
	}

	/**
	 * 
	 * @param lastUpdateTime
	 *            the lastUpdateTime to set
	 */
	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	/**
	 * 
	 * @return createUser
	 */
	public String getCreateUser() {
		return createUser;
	}

	/**
	 * 
	 * @param createUser
	 *            the createUser to set
	 */
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	/**
	 * 
	 * @return lastUpdateUser
	 */
	public String getLastUpdateUser() {
		return lastUpdateUser;
	}

	/**
	 * 
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getIsCollect() {
		return isCollect;
	}

	public void setIsCollect(String isCollect) {
		this.isCollect = isCollect;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	String id;

	String content;
	String isCollect;
	String token;
	String title;
	String poster;// 图片
	String newPoster;
	String speaker;
	String lecTime;
	String lecPlace;
	String createTime;
	String lastUpdateTime;
	String createUser;
	String lastUpdateUser;
	String jsonUpdateFlag;
	String mybatisRecordCount;
	String orderNo;
	String attachmentPrefix;// 图片前缀
}
