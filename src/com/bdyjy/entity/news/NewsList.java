/**
 * NewsList.java[v 1.0.0]
 * class:com.bdyjy.entity.news,NewsList
 * 周航 create at 2016-4-28 上午10:41:57
 */
package com.bdyjy.entity.news;

import java.util.List;

/**
 * com.bdyjy.entity.news.NewsList
 * @author 周航<br/> 
 * create at 2016-4-28 上午10:41:57
 */
public class NewsList
{
	public String getCode()
	{
		return code;
	}
	public void setCode(String code)
	{
		this.code = code;
	}
	public String getTotal()
	{
		return total;
	}
	public void setTotal(String total)
	{
		this.total = total;
	}
	public List<NewsItem> getList()
	{
		return list;
	}
	public void setList(List<NewsItem> list)
	{
		this.list = list;
	}
	private String code;
	private String total;
	private List<NewsItem> list;
}
