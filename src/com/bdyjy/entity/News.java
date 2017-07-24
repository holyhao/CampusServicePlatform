/**
 * News.java[v 1.0.0]
 * class:com.bdyjy.entity,News
 * 周航 create at 2016-3-23 下午7:35:26
 */
package com.bdyjy.entity;

import java.io.Serializable;

/**
 * com.bdyjy.entity.News
 * 
 * @author 周航<br/>
 *         create at 2016-3-23 下午7:35:26
 */
public class News implements Serializable
{

	public String getIsCollect()
	{
		return isCollect;
	}

	public void setIsCollect(String isCollect)
	{
		this.isCollect = isCollect;
	}

	/**
	 * @return the id
	 */
	public String getId()
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
	public String getType()
	{
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type)
	{
		this.type = type;
	}

	/**
	 * @return the content
	 */
	public String getContent()
	{
		return content;
	}

	/**
	 * @param content
	 *            the content to set
	 */
	public void setContent(String content)
	{
		this.content = content;
	}

	/**
	 * @return the logo
	 */
	public String getLogo()
	{
		return logo;
	}

	/**
	 * @param logo
	 *            the logo to set
	 */
	public void setLogo(String logo)
	{
		this.logo = logo;
	}

	/**
	 * @return the title
	 */
	public String getTitle()
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
	 * @return the pubFrom
	 */
	public String getPubFrom()
	{
		return pubFrom;
	}

	/**
	 * @param pubFrom
	 *            the pubFrom to set
	 */
	public void setPubFrom(String pubFrom)
	{
		this.pubFrom = pubFrom;
	}

	/**
	 * @return the newLogo
	 */
	public String getNewLogo()
	{
		return newLogo;
	}

	/**
	 * @param newLogo
	 *            the newLogo to set
	 */
	public void setNewLogo(String newLogo)
	{
		this.newLogo = newLogo;
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

	private static final long serialVersionUID = 5687503027702495526L;
	String id;
	String type;
	String content;
	String logo;
	String title;
	String pubFrom;
	String newLogo;
	String createUser;
	String createTime;
	String lastUpdateTime;
	String lastUpdateUser;
	String jsonUpdateFlag;
	String mybatisRecordCount;
	String orderNo;
	String attachmentPrefix;
	String isCollect;
}