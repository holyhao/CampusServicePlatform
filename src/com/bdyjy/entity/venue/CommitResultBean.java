/**
 * CommitResultBean.java[v 1.0.0]
 * class:com.bdyjy.entity.venue,CommitResultBean
 * 周航 create at 2016-4-8 上午11:29:42
 */
package com.bdyjy.entity.venue;

/**
 * com.bdyjy.entity.venue.CommitResultBean
 * 
 * @author 周航<br/>
 *         create at 2016-4-8 上午11:29:42
 */
public class CommitResultBean
{

	public String getApp_result_key()
	{
		return app_result_key;
	}

	public void setApp_result_key(String app_result_key)
	{
		this.app_result_key = app_result_key;
	}

	public String getSystem_result_key()
	{
		return system_result_key;
	}

	public void setSystem_result_key(String system_result_key)
	{
		this.system_result_key = system_result_key;
	}

	public String getSystem_result_message_key()
	{
		return system_result_message_key;
	}

	public void setSystem_result_message_key(String system_result_message_key)
	{
		this.system_result_message_key = system_result_message_key;
	}

	public String getCurrent_session_user_resource_ids_index()
	{
		return current_session_user_resource_ids_index;
	}

	public void setCurrent_session_user_resource_ids_index(
			String current_session_user_resource_ids_index)
	{
		this.current_session_user_resource_ids_index = current_session_user_resource_ids_index;
	}

	// {"system_result_key":"0","app_result_key":"0","current_session_user_resource_ids_index":""}
	String system_result_key;
	String system_result_message_key;
	String current_session_user_resource_ids_index;
	String app_result_key;

}
