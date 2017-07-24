/**
 * NewsItem.java[v 1.0.0]
 * class:com.bdyjy.entity.news,NewsItem
 * 周航 create at 2016-4-28 上午10:43:29
 */
package com.bdyjy.entity.news;

/**
 * com.bdyjy.entity.news.NewsItem
 * @author 周航<br/> 
 * create at 2016-4-28 上午10:43:29
 */
public class NewsItem
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
	public String getCat_name()
	{
		return cat_name;
	}
	public void setCat_name(String cat_name)
	{
		this.cat_name = cat_name;
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
	public String getThumb()
	{
		return thumb;
	}
	public void setThumb(String thumb)
	{
		this.thumb = thumb;
	}
	private String id;
	private String catid;
	private String title;
	private String cat_name;
	private String date;
	private String sourcefrom;
	private String thumb;
}
