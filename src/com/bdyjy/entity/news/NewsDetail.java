/**
 * News.java[v 1.0.0]
 * class:com.bdyjy.entity.news,News
 * 周航 create at 2016-4-28 上午10:38:22
 */
package com.bdyjy.entity.news;

/**
 * com.bdyjy.entity.news.News
 * @author 周航<br/> 
 * create at 2016-4-28 上午10:38:22
 */
public class NewsDetail
{
	
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id = id;
	}
	public String getCatid()
	{
		return catid;
	}
	public void setCatid(String catid)
	{
		this.catid = catid;
	}
	public String getTitle()
	{
		return title;
	}
	public void setTitle(String title)
	{
		this.title = title;
	}
	public String getDate()
	{
		return date;
	}
	public void setDate(String date)
	{
		this.date = date;
	}
	public String getSourcefrom()
	{
		return sourcefrom;
	}
	public void setSourcefrom(String sourcefrom)
	{
		this.sourcefrom = sourcefrom;
	}
	public String getContent()
	{
		return content;
	}
	public void setContent(String content)
	{
		this.content = content;
	}
	private String id;
	private String catid;
	private String title;
	private String date;
	private String sourcefrom;
	private String content;
}
