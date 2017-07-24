/**
 * Type.java[v 1.0.0]
 * class:com.bdyjy.entity.fixtype,Type
 * 周航 create at 2016-4-21 下午3:54:58
 */
package com.bdyjy.entity.fixtype;

/**
 * com.bdyjy.entity.fixtype.Type
 * 
 * @author 周航<br/>
 *         create at 2016-4-21 下午3:54:58
 */
public class Type
{
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id = id;
	}
	public String getFatherId()
	{
		return fatherId;
	}
	public void setFatherId(String fatherId)
	{
		this.fatherId = fatherId;
	}
	public String getText()
	{
		return text;
	}
	public void setText(String text)
	{
		this.text = text;
	}
	public String getSelected()
	{
		return selected;
	}
	public void setSelected(String selected)
	{
		this.selected = selected;
	}
	String id;
	String fatherId;
	String text;
	String selected;
}
