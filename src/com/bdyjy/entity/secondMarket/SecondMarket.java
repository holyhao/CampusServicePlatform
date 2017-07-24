package com.bdyjy.entity.secondMarket;
import java.io.Serializable;
import java.util.List;

import com.bdyjy.entity.attArryData;

public class SecondMarket implements Serializable 
{
	

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
	 * @return the price
	 */
	public String getPrice()  //价格的获取与修改
	{
		return price;
	}

	/**
	 * @param price
	 *            the price to set
	 */
	public void setPrice(String price)
	{
		this.price = price;
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
	 * @return the goodsName
	 */
	public String getGoodsName()  
	{
		return goodsName;
	}

	/**
	 * @param goodsName
	 *            the goodsName to set
	 */
	public void setGoodsname(String goodsName)
	{
		this.goodsName = goodsName;
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
	
	
	public String getStatusShow(){
		return statusShow;
	}
	public void setStatusShow(String statusShow){
		this.statusShow=statusShow;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid()
	{
		return serialVersionUID;
	}
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
	
	public int getStatus(){
		return status;
	}
	public void setStatus(int status){
		this.status=status;
	}
	





	/**
	 * @return the isCollect
	 */
	public String getIsCollect()
	{
		return isCollect;
	}

	/**
	 * @param isCollect the isCollect to set
	 */
	public void setIsCollect(String isCollect)
	{
		this.isCollect = isCollect;
	}

	
	private static final long serialVersionUID = 0000000000000000666L;
	String id;
	String description;
	String price;
	String title;
	List<attArryData> attArry; //图片类
	String tel;
	String contacts;
	String index1;
	String goodsName;
	String lastUpdateTime;
	String lastUpdateUser;
	String statusShow;
	int status;
	String createTime;
	String createUser;
	String orderNo;
	String mybatisRecordCount;
	String jsonUpdateFlag;
	String token; //定义方法 n
	String attachmentPrefix;
	private String isCollect;

}
