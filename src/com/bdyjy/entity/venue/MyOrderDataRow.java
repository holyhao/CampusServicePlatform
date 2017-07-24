/**
 * MyOrderDataRows.java[v 1.0.0]
 * class:com.bdyjy.entity.venue,MyOrderDataRows
 * 周航 create at 2016-4-8 下午4:42:00
 */
package com.bdyjy.entity.venue;

import java.util.List;

/**
 * com.bdyjy.entity.venue.MyOrderDataRows
 * 
 * @author 周航<br/>
 *         create at 2016-4-8 下午4:42:00
 */
public class MyOrderDataRow
{

	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id = id;
	}
	public String getUserName()
	{
		return userName;
	}
	public void setUserName(String userName)
	{
		this.userName = userName;
	}
	public String getStatus()
	{
		return status;
	}
	public void setStatus(String status)
	{
		this.status = status;
	}
	public String getEndTime()
	{
		return endTime;
	}
	public void setEndTime(String endTime)
	{
		this.endTime = endTime;
	}
	public String getRoomId()
	{
		return roomId;
	}
	public void setRoomId(String roomId)
	{
		this.roomId = roomId;
	}
	public List getTimeIds()
	{
		return timeIds;
	}
	public void setTimeIds(List timeIds)
	{
		this.timeIds = timeIds;
	}
	public String getAppDate()
	{
		return appDate;
	}
	public void setAppDate(String appDate)
	{
		this.appDate = appDate;
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
	public String getAmount()
	{
		return amount;
	}
	public void setAmount(String amount)
	{
		this.amount = amount;
	}
	public String getRoomName()
	{
		return roomName;
	}
	public void setRoomName(String roomName)
	{
		this.roomName = roomName;
	}
	public String getBeginTime()
	{
		return beginTime;
	}
	public void setBeginTime(String beginTime)
	{
		this.beginTime = beginTime;
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
	public String getToken()
	{
		return token;
	}
	public void setToken(String token)
	{
		this.token = token;
	}
	public String getAttachmentPrefix()
	{
		return attachmentPrefix;
	}
	public void setAttachmentPrefix(String attachmentPrefix)
	{
		this.attachmentPrefix = attachmentPrefix;
	}
	public List<ListTime> getListTime()
	{
		return listTime;
	}
	public void setListTime(List<ListTime> listTime)
	{
		this.listTime = listTime;
	}
	String id;
	String userName;
	String status;
	String endTime;
	String roomId;
	List timeIds;
	String appDate;
	String updateUser;
	String updateTime;
	String createUser;
	String createTime;
	String amount;
	String roomName;
	String beginTime;
	String orderNo;
	String mybatisRecordCount;
	String jsonUpdateFlag;
	String token;
	String attachmentPrefix;
	List<ListTime> listTime;
}
