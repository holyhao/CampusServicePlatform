package com.bdyjy.entity.venue;

import java.util.List;

/**
 * 球场场地实体类
 *
 */
public class Data
{
	public List<ListRoom> getListRoom()
	{
		return listRoom;
	}
	public void setListRoom(List<ListRoom> listRoom)
	{
		this.listRoom = listRoom;
	}
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id = id;
	}
	public String getEndTime()
	{
		return endTime;
	}
	public void setEndTime(String endTime)
	{
		this.endTime = endTime;
	}
	public String getStartTime()
	{
		return startTime;
	}
	public void setStartTime(String startTime)
	{
		this.startTime = startTime;
	}
	public String getStatus()
	{
		return status;
	}
	public void setStatus(String status)
	{
		this.status = status;
	}
	public String getUpdateUser()
	{
		return updateUser;
	}
	public void setUpdateUser(String updateUser)
	{
		this.updateUser = updateUser;
	}
	public String getUpdateTime()
	{
		return updateTime;
	}
	public void setUpdateTime(String updateTime)
	{
		this.updateTime = updateTime;
	}
	public String getSort()
	{
		return sort;
	}
	public void setSort(String sort)
	{
		this.sort = sort;
	}
	public String getRemark()
	{
		return remark;
	}
	public void setRemark(String remark)
	{
		this.remark = remark;
	}
	public String getCreateUser()
	{
		return createUser;
	}
	public void setCreateUser(String createUser)
	{
		this.createUser = createUser;
	}
	public String getCreateTime()
	{
		return createTime;
	}
	public void setCreateTime(String createTime)
	{
		this.createTime = createTime;
	}
	public String getToken()
	{
		return token;
	}
	public void setToken(String token)
	{
		this.token = token;
	}
	public String getOrderNo()
	{
		return orderNo;
	}
	public void setOrderNo(String orderNo)
	{
		this.orderNo = orderNo;
	}
	public String getMybatisRecordCount()
	{
		return mybatisRecordCount;
	}
	public void setMybatisRecordCount(String mybatisRecordCount)
	{
		this.mybatisRecordCount = mybatisRecordCount;
	}
	public String getJsonUpdateFlag()
	{
		return jsonUpdateFlag;
	}
	public void setJsonUpdateFlag(String jsonUpdateFlag)
	{
		this.jsonUpdateFlag = jsonUpdateFlag;
	}
	public String getAttachmentPrefix()
	{
		return attachmentPrefix;
	}
	public void setAttachmentPrefix(String attachmentPrefix)
	{
		this.attachmentPrefix = attachmentPrefix;
	}
	List<ListRoom> listRoom;
	String id;
	String endTime;
	String startTime;
	String status;
	String updateUser;
	String updateTime;
	String sort;
	String remark;
	String createUser;
	String createTime;
	String token;
	String orderNo;
	String mybatisRecordCount;
	String jsonUpdateFlag;
	String attachmentPrefix;
}
