package com.bdyjy.entity.fix;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.bdyjy.entity.attArryData;

public class MyFix implements Serializable{
	

	/**
	 * @return the location
	 */
	public String getLocation()  //位置的获取
	{
		return location;
	}

	/**
	 * @param location
	 *            the location to set
	 */
	public void setLocation(String location)
	{
		this.location = location;
	}
	
	
	/**
	 * @return the id
	 */
	public String getId()  //id的获取与修改
	{
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id)
	{
		this.id = id;
	}
	
	
	/**
	 * @return the type
	 */
	public String getType()  //id的获取与修改
	{
		return type;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setType(String type)
	{
		this.type = type;
	}
	
	
	
	
	
	
	/**
	 * @param description
	 *            get description
	 */
	public String getDescription()  //id的获取与修改
	{
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}
	
	
	/**
	 * @param status
	 *            get status
	 */
	public String getStatus()  //id的获取与修改
	{
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(String status)
	{
		this.status = status;
	}
		
	
	
	

	/**
	 * @return the title
	 */
	public String getTitle()  //标题的获取与修改
	{
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title)
	{
		this.title = title;
	}
	
	/**
	 * @return the attArry
	 */
//	public String getAttArry()
//	{
//		return attArry;
//	}
//
//	/**
//	 * @param attArry
//	 *            the attArry to set
//	 */
//	public void setAttArry(String attArry)
//	{
//		this.attArry = attArry;
//	}
	
	
	
	
	//临时所加   待修改
	/**   
	 * @return the rows
	 */
	public List<attArryData> getAttArry() {
		return attArry;
	}

	/**
	 * @param rows
	 *            the rows to set
	 */
	public void setAttArry(List<attArryData>  attArry) {
		this.attArry = attArry;
	}	
	
	
	
	/**
	 * @return the Tel
	 */
	public String getTel()
	{
		return tel;
	}

	/**
	 * @param Tel
	 *            the Tel to set
	 */
	public void setTel(String tel)
	{
		this.tel = tel;
	}

	
	/**
	 * @return the Contact
	 */
	public String getContacts()
	{
		return contacts;
	}

	/**
	 * @param contact
	 *            the contact to set
	 */
	public void setContacts(String contacts)
	{
		this.contacts = contacts;
	}
	
	
	/**
	 * @return the department
	 */
	public String getDepartment()
	{
		return department;
	}

	/**
	 * @param department
	 *            the department to set
	 */
	public void setDepartment(String department)
	{
		this.department =department;
	}
	
	
	
	/**
	 * @return the picture
	 */
	public String getPicture()
	{
		return picture;
	}

	/**
	 * @param picture
	 *            the picturet to set
	 */
	public void setPicture(String picture)
	{
		this.picture =picture;
	}
	

	/**
	 * @return the createUser
	 */
	public String getCreateUser()
	{
		return createUser;
	}

	/**
	 * @param createUser
	 *            the createUser to set
	 */
	public void setCreateUser(String createUser)
	{
		this.createUser = createUser;
	}


	/**
	 * @return the typeShow
	 */
	public String getTypeShow()
	{
		return typeShow;
	}

	/**
	 * @param typeShow
	 *            the typeShow to set
	 */
	public void setTypeShow(String typeShow)
	{
		this.typeShow = typeShow;
	}	
	
	
	/**
	 * @return the statusShow
	 */
	public String getStatusShow()
	{
		return statusShow;
	}

	/**
	 * @param statusShow
	 *            the statusShow to set
	 */
	public void setStatusShow(String statusShow)
	{
		this.statusShow = statusShow;
	}	
	

	/**
	 * @return the createTime
	 */
	public String getCreateTime()
	{
		return createTime;
	}

	/**
	 * @param createTime
	 *            the createTime to set
	 */
	public void setCreateTime(String createTime)
	{
		this.createTime = createTime;
	}

	/**
	 * @return the lastUpdateTime
	 */
	public String getLastUpdateTime()
	{
		return lastUpdateTime;
	}

	/**
	 * @param lastUpdateTime
	 *            the lastUpdateTime to set
	 */
	public void setLastUpdateTime(String lastUpdateTime)
	{
		this.lastUpdateTime = lastUpdateTime;
	}

	/**
	 * @return the lastUpdateUser
	 */
	public String getLastUpdateUser()
	{
		return lastUpdateUser;
	}

	/**
	 * @param lastUpdateUser
	 *            the lastUpdateUser to set
	 */
	public void setLastUpdateUser(String lastUpdateUser)
	{
		this.lastUpdateUser = lastUpdateUser;
	}
	

	/**
	 * @return the jsonUpdateFlag
	 */
	public String getJsonUpdateFlag()
	{
		return jsonUpdateFlag;
	}

	/**
	 * @param jsonUpdateFlag
	 *            the jsonUpdateFlag to set
	 */
	public void setJsonUpdateFlag(String jsonUpdateFlag)
	{
		this.jsonUpdateFlag = jsonUpdateFlag;
	}

	/**
	 * @return the mybatisRecordCount
	 */
	public String getMybatisRecordCount()
	{
		return mybatisRecordCount;
	}

	/**
	 * @param mybatisRecordCount
	 *            the mybatisRecordCount to set
	 */
	public void setMybatisRecordCount(String mybatisRecordCount)
	{
		this.mybatisRecordCount = mybatisRecordCount;
	}

	/**
	 * @return the orderNo
	 */
	public String getOrderNo()
	{
		return orderNo;
	}

	/**
	 * @param orderNo
	 *            the orderNo to set
	 */
	public void setOrderNo(String orderNo)
	{
		this.orderNo = orderNo;
	}
	
	
	/**
	 * @return the orderNo
	 */
	public String getToken()
	{
		return token;
	}

	/**
	 * @param orderNo
	 *            the orderNo to set
	 */
	public void setToken(String token)
	{
		this.token = token;
	}
	
	
	

	/**
	 * @return the attachmentPrefix
	 */
	public String getAttachmentPrefix()
	{
		return attachmentPrefix;
	}

	/**
	 * @param attachmentPrefix
	 *            the attachmentPrefix to set
	 */
	public void setAttachmentPrefix(String attachmentPrefix)
	{
		this.attachmentPrefix = attachmentPrefix;
	}
	
	

	
	
	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid()
	{
		return serialVersionUID;
	}

	private static final long serialVersionUID = 0000000000000001004L;
	String location;
	String id;
	String type;
	String description;
	String status;
	String title;
	String tel;
	String contacts;
	String department;
	String picture;
	String lastUpdateTime;
	String lastUpdateUser;  
	String createTime;
	String createUser;
	String typeShow; 
	String statusShow;
	String orderNo;
	String mybatisRecordCount;
	String jsonUpdateFlag;
	String token; //未定义方法
	String attachmentPrefix;
	List<attArryData> attArry; //图片类
	

}
