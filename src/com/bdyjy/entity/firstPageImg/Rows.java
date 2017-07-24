/**
 * Rows.java[v 1.0.0]
 * class:com.bdyjy.entity.firstPageImg,Rows
 * 周航 create at 2016-5-17 下午4:12:14
 */
package com.bdyjy.entity.firstPageImg;

/**
 * com.bdyjy.entity.firstPageImg.Rows
 * 
 * @author 周航<br/>
 *         create at 2016-5-17 下午4:12:14
 */
public class Rows
{

	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id = id;
	}
	public String getLink()
	{
		return link;
	}
	public int getType(){
		return type;
	}
	public void setType(int type){
		this.type=type;
	}
	public void setLink(String link)
	{
		this.link = link;
	}
	public String getStatus()
	{
		return status;
	}
	public void setStatus(String status)
	{
		this.status = status;
	}
	public String getTitle()
	{
		return title;
	}
	public void setTitle(String title)
	{
		this.title = title;
	}
	public String getPicture()
	{
		return picture;
	}
	public void setPicture(String picture)
	{
		this.picture = picture;
	}
	public String getNewPicture()
	{
		return newPicture;
	}
	public void setNewPicture(String newPicture)
	{
		this.newPicture = newPicture;
	}
	public String getCreateTime()
	{
		return createTime;
	}
	public void setCreateTime(String createTime)
	{
		this.createTime = createTime;
	}
	public String getStatusShow()
	{
		return statusShow;
	}
	public void setStatusShow(String statusShow)
	{
		this.statusShow = statusShow;
	}
	public String getCreateUser()
	{
		return createUser;
	}
	public void setCreateUser(String createUser)
	{
		this.createUser = createUser;
	}
	public String getLastUpdateTime()
	{
		return lastUpdateTime;
	}
	public void setLastUpdateTime(String lastUpdateTime)
	{
		this.lastUpdateTime = lastUpdateTime;
	}
	public String getLastUpdateUser()
	{
		return lastUpdateUser;
	}
	public void setLastUpdateUser(String lastUpdateUser)
	{
		this.lastUpdateUser = lastUpdateUser;
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
	public String getAttachmentPrefix()
	{
		return attachmentPrefix;
	}
	public void setAttachmentPrefix(String attachmentPrefix)
	{
		this.attachmentPrefix = attachmentPrefix;
	}
	
	public void setForeignId(String foreignId){
		this.foreignId=foreignId;
	}
	public String getForeignId(){
		return this.foreignId;
	}
	private String id;
	private String link;
	private String status;
	private String title;
	private int type;
	private String picture;
	private String newPicture;
	private String createTime;
	private String statusShow;
	private String createUser;
	private String lastUpdateTime;
	private String lastUpdateUser;
	private String mybatisRecordCount;
	private String jsonUpdateFlag;
	private String orderNo;
	private String foreignId;
	private String token;
	private String attachmentPrefix;

}
