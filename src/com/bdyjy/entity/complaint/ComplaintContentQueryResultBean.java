package com.bdyjy.entity.complaint;

import java.io.Serializable;

import com.bdyjy.entity.recruit.RecruitNewBean;

public class ComplaintContentQueryResultBean implements Serializable {

	/**
	 *@author holy
	 */
	private static final long serialVersionUID = 7202325575108527930L;
	
	String system_result_key;
	String app_result_key;
	Complaints entity;
	String current_session_user_resource_ids_index;
	String app_result_message_key;
	
	
	public String getApp_result_message_key() {
		return app_result_message_key;
	}

	public void setApp_result_message_key(String app_result_message_key) {
		this.app_result_message_key = app_result_message_key;
	}

	/**
	 * getters and setters
	 * 
	 */
	public String getSystem_result_key() {
		return system_result_key;
	}
	
	public void setSystem_result_key(String system_result_key) {
		this.system_result_key = system_result_key;
	}
	
	public String getApp_result_key() {
		return app_result_key;
	}
	
	public void setApp_result_key(String app_result_key) {
		this.app_result_key = app_result_key;
	}
	
	public Complaints getEntity() {
		return entity;
	}
	
	public void setEntity(Complaints entity) {
		this.entity = entity;
	}
	
	public String getCurrent_session_user_resource_ids_index() {
		return current_session_user_resource_ids_index;
	}
	
	public void setCurrent_session_user_resource_ids_index(String current_session_user_resource_ids_index) {
		this.current_session_user_resource_ids_index = current_session_user_resource_ids_index;
	}
	
	
	

}
