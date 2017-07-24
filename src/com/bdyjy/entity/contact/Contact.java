package com.bdyjy.entity.contact;

import java.io.Serializable;
import java.util.List;

public class Contact implements Serializable {

	/**
	 * com.bdyjy.entity.Contact
	 * 
	 * @author cuicui
	 * 
	 *         create at 2016-3-26 上午10:17
	 */
	private static final long serialVersionUID = -5038588376138427430L;

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

	public List<ContactDepartment> getData() {
		return data;
	}

	public void setData(List<ContactDepartment> data) {
		this.data = data;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getCurrent_session_user_resource_ids_index() {
		return current_session_user_resource_ids_index;
	}

	public void setCurrent_session_user_resource_ids_index(String current_session_user_resource_ids_index) {
		this.current_session_user_resource_ids_index = current_session_user_resource_ids_index;
	}

	String system_result_key;
	String app_result_key;
	List<ContactDepartment> data;

	String current_session_user_resource_ids_index;

}
