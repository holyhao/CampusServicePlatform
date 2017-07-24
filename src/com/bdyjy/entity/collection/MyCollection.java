/**
 * MyCollection.java[v 1.0.0]
 * class:com.bdyjy.entity.collection,MyCollection
 * 周航 create at 2016-5-27 下午5:27:29
 */
package com.bdyjy.entity.collection;

/**
 * com.bdyjy.entity.collection.MyCollection
 * 
 * @author 周航<br/>
 *         create at 2016-5-27 下午5:27:29
 */
public class MyCollection
{
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id = id;
	}
	public String getType()
	{
		return type;
	}
	public void setType(String type)
	{
		this.type = type;
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
	public String getForeignId()
	{
		return foreignId;
	}
	public void setForeignId(String foreignId)
	{
		this.foreignId = foreignId;
	}
	public String getTitle()
	{
		return title;
	}
	public void setTitle(String title)
	{
		this.title = title;
	}
	public String getAttachmentPrefix()
	{
		return attachmentPrefix;
	}
	public void setAttachmentPrefix(String attachmentPrefix)
	{
		this.attachmentPrefix = attachmentPrefix;
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
	public String getOrderNo()
	{
		return orderNo;
	}
	public void setOrderNo(String orderNo)
	{
		this.orderNo = orderNo;
	}
	public String getToken()
	{
		return token;
	}
	public void setToken(String token)
	{
		this.token = token;
	}
	private String id;
	private String type;
	private String createUser;
	private String createTime;
	private String foreignId;
	private String title;
	private String attachmentPrefix;
	private String mybatisRecordCount;
	private String jsonUpdateFlag;
	private String orderNo;
	private String token;

}
