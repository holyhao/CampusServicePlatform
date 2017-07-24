package com.bdyjy.entity.lostfind;

import java.io.Serializable;


public class LostFindContentBean  implements Serializable {
	/**
	 * @return the system_result_key
	 */
	public String getSystem_result_key()
	{
		return system_result_key;
	}

	/**
	 * @param system_result_key
	 *            the system_result_key to set
	 */
	public void setSystem_result_key(String system_result_key)
	{
		this.system_result_key = system_result_key;
	}

	/**
	 * @return the app_result_key
	 */
	public String getApp_result_key()
	{
		return app_result_key;
	}

	/**
	 * @param app_result_key
	 *            the app_result_key to set
	 */
	public void setApp_result_key(String app_result_key)
	{
		this.app_result_key = app_result_key;
	}

	/**
	 * @return the data
	 */
	public LostFind getEntity()
	{
		return entity;
	}

	/**
	 * @param entity
	 *            the entity to set
	 */
	public void setEntity(LostFind entity)
	{
		this.entity =entity;
	}

	/**
	 * @return the current_session_user_resource_ids_index
	 */
	public String getCurrent_session_user_resource_ids_index()
	{
		return current_session_user_resource_ids_index;
	}

	/**
	 * @param current_session_user_resource_ids_index
	 *            the current_session_user_resource_ids_index to set
	 */
	public void setCurrent_session_user_resource_ids_index(
			String current_session_user_resource_ids_index)
	{
		this.current_session_user_resource_ids_index = current_session_user_resource_ids_index;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid()
	{
		return serialVersionUID;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 0000000000000001002L;
	String system_result_key;
	String app_result_key;
	LostFind entity;
	String current_session_user_resource_ids_index;	

}
