/**
 * MyCollectionResultBean.java[v 1.0.0]
 * class:com.bdyjy.entity.collection,MyCollectionResultBean
 * 周航 create at 2016-5-27 下午5:31:41
 */
package com.bdyjy.entity.collection;

/**
 * com.bdyjy.entity.collection.MyCollectionResultBean
 * 
 * @author 周航<br/>
 *         create at 2016-5-27 下午5:31:41
 */
public class MyCollectionResultBean
{
	public String getSystem_result_key()
	{
		return system_result_key;
	}

	public void setSystem_result_key(String system_result_key)
	{
		this.system_result_key = system_result_key;
	}

	public String getApp_result_key()
	{
		return app_result_key;
	}

	public void setApp_result_key(String app_result_key)
	{
		this.app_result_key = app_result_key;
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

	public MyCollectionData getData()
	{
		return data;
	}

	public void setData(MyCollectionData data)
	{
		this.data = data;
	}

	private String system_result_key;
	private String app_result_key;
	private String current_session_user_resource_ids_index;

	private MyCollectionData data; 
}
