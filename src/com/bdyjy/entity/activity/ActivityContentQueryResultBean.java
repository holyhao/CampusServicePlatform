package com.bdyjy.entity.activity;

import java.io.Serializable;

/**
 * 社团活动内容查询结果bean
 * 
 * @author cuicui create at 2016-04-02 16:37
 *
 */
public class ActivityContentQueryResultBean implements Serializable {

	String system_result_key;
	String app_result_key;
	String current_session_user_resource_ids_index;

	Activity entity;

	/**
	 * @return the entity
	 */
	public Activity getEntity() {
		return entity;
	}

	/**
	 * @param entity
	 *            the entity to set
	 */
	public void setEntity(Activity entity) {
		this.entity = entity;
	}

	/**
	 * @return the system_result_key
	 */
	public String getSystem_result_key() {
		return system_result_key;
	}

	/**
	 * @param system_result_key
	 *            the system_result_key to set
	 */
	public void setSystem_result_key(String system_result_key) {
		this.system_result_key = system_result_key;
	}

	/**
	 * @return the app_result_key
	 */
	public String getApp_result_key() {
		return app_result_key;
	}

	/**
	 * @param app_result_key
	 *            the app_result_key to set
	 */
	public void setApp_result_key(String app_result_key) {
		this.app_result_key = app_result_key;
	}

	/**
	 * @return the current_session_user_resource_ids_index
	 */
	public String getCurrent_session_user_resource_ids_index() {
		return current_session_user_resource_ids_index;
	}

	/**
	 * @param current_session_user_resource_ids_index
	 *            the current_session_user_resource_ids_index to set
	 */
	public void setCurrent_session_user_resource_ids_index(String current_session_user_resource_ids_index) {
		this.current_session_user_resource_ids_index = current_session_user_resource_ids_index;
	}
}
