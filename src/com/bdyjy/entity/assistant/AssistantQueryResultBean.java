package com.bdyjy.entity.assistant;

import java.io.Serializable;

public class AssistantQueryResultBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5838965227740075445L;
	
	String system_result_key;
	String app_result_key;
	Data data;
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
	public Data getData() {
		return data;
	}
	public void setData(Data data) {
		this.data = data;
	}
	public String getCurrent_session_user_resource_ids_index() {
		return current_session_user_resource_ids_index;
	}
	public void setCurrent_session_user_resource_ids_index(String current_session_user_resource_ids_index) {
		this.current_session_user_resource_ids_index = current_session_user_resource_ids_index;
	}
	
	
}
