/**
 * NewsContentQueryResultBean.java[v 1.0.0]
 * class:com.bdyjy.entity,NewsContentQueryResultBean
 * 周航 create at 2016-3-23 下午9:51:20
 */
package com.bdyjy.entity;

import java.io.Serializable;

/**
 * com.bdyjy.entity.NewsContentQueryResultBean
 * 
 * @author 周航<br/>
 *         create at 2016-3-23 下午9:51:20
 */
public class NewsContentQueryResultBean implements Serializable
{
	/**
	 * @return the entity
	 */
	public News getEntity()
	{
		return entity;
	}

	/**
	 * @param entity
	 *            the entity to set
	 */
	public void setEntity(News entity)
	{
		this.entity = entity;
	}

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

	String system_result_key;
	String app_result_key;
	String current_session_user_resource_ids_index;

	News entity;

}
