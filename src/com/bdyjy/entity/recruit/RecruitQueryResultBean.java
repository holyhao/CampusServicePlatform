package com.bdyjy.entity.recruit;

import java.io.Serializable;

/**
 * 后台返回的招聘信息的新闻数据
 * @author parle
 *
 */
public class RecruitQueryResultBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -664895315236338202L;

	
	String system_result_key;
	String app_result_key;
	Data data;
	String current_session_user_resource_ids_index;
	
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
