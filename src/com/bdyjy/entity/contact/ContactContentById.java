package com.bdyjy.entity.contact;

import java.io.Serializable;
import java.util.List;

public class ContactContentById implements Serializable {

	public List<ContactContent> getData()
	{
		return data;
	}

	public void setData(List<ContactContent> data)
	{
		this.data = data;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -7306203368708141160L;

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

	public String getCurrent_session_user_resource_ids_index() {
		return current_session_user_resource_ids_index;
	}

	public void setCurrent_session_user_resource_ids_index(String current_session_user_resource_ids_index) {
		this.current_session_user_resource_ids_index = current_session_user_resource_ids_index;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	String system_result_key;
	String app_result_key;
	String current_session_user_resource_ids_index;
	List<ContactContent> data;
}
