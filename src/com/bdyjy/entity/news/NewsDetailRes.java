/**
 * NewsDetailRes.java[v 1.0.0]
 * class:com.bdyjy.entity.news,NewsDetailRes
 * 周航 create at 2016-4-28 上午10:46:40
 */
package com.bdyjy.entity.news;

/**
 * com.bdyjy.entity.news.NewsDetailRes
 * @author 周航<br/> 
 * create at 2016-4-28 上午10:46:40
 */
public class NewsDetailRes
{
	public String getCode()
	{
		return code;
	}
	public void setCode(String code)
	{
		this.code = code;
	}
	public NewsDetail getDetail()
	{
		return detail;
	}
	public void setDetail(NewsDetail detail)
	{
		this.detail = detail;
	}
	private String code;
	private NewsDetail detail;
}
