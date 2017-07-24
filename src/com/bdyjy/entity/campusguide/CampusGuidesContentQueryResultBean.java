package com.bdyjy.entity.campusguide;

import java.io.Serializable;

import com.bdyjy.entity.recruit.RecruitNewBean;

public class CampusGuidesContentQueryResultBean implements Serializable {

	/**
	 *@author holy
	 */
	private static final long serialVersionUID = -6642381620231291711L;
	
	
	
	String system_result_key;
	String app_result_key;
	CampusGuides entity;
	String current_session_user_resource_ids_index;
	
	
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
	
	public CampusGuides getEntity() {
		return entity;
	}
	
	public void setEntity(CampusGuides entity) {
		this.entity = entity;
	}
	
	public String getCurrent_session_user_resource_ids_index() {
		return current_session_user_resource_ids_index;
	}
	
	public void setCurrent_session_user_resource_ids_index(String current_session_user_resource_ids_index) {
		this.current_session_user_resource_ids_index = current_session_user_resource_ids_index;
	}
	
	
	

}
