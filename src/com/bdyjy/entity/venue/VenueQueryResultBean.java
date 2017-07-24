/**
 * VenueQueryResultBean.java[v 1.0.0]
 * class:com.bdyjy.entity.venue,VenueQueryResultBean
 * 周航 create at 2016-4-7 下午3:18:19
 */
package com.bdyjy.entity.venue;

import java.util.List;

/**
 * com.bdyjy.entity.venue.VenueQueryResultBean
 * 
 * @author 周航<br/>
 *         create at 2016-4-7 下午3:18:19
 */
public class VenueQueryResultBean
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
	public List<ListRoom> getListRoom()
	{
		return listRoom;
	}
	public void setListRoom(List<ListRoom> listRoom)
	{
		this.listRoom = listRoom;
	}
	public List<Data> getData()
	{
		return data;
	}
	public void setData(List<Data> data)
	{
		this.data = data;
	}
	String system_result_key;
	String app_result_key;
	String current_session_user_resource_ids_index;
	List<ListRoom> listRoom;
	List<Data> data;
}
