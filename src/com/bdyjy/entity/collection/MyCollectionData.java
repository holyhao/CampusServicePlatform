/**
 * MyCollectionData.java[v 1.0.0]
 * class:com.bdyjy.entity.collection,MyCollectionData
 * 周航 create at 2016-5-27 下午5:32:44
 */
package com.bdyjy.entity.collection;

import java.util.List;

/**
 * com.bdyjy.entity.collection.MyCollectionData
 * @author 周航<br/> 
 * create at 2016-5-27 下午5:32:44
 */
public class MyCollectionData
{
	public String getTotal()
	{
		return total;
	}

	public void setTotal(String total)
	{
		this.total = total;
	}

	public String getPageStartOffset()
	{
		return pageStartOffset;
	}

	public void setPageStartOffset(String pageStartOffset)
	{
		this.pageStartOffset = pageStartOffset;
	}

	public String getPageSize()
	{
		return pageSize;
	}

	public void setPageSize(String pageSize)
	{
		this.pageSize = pageSize;
	}

	public String getPageNo()
	{
		return pageNo;
	}

	public void setPageNo(String pageNo)
	{
		this.pageNo = pageNo;
	}

	public String getPageCount()
	{
		return pageCount;
	}

	public void setPageCount(String pageCount)
	{
		this.pageCount = pageCount;
	}

	public List<MyCollection> getRows()
	{
		return rows;
	}

	public void setRows(List<MyCollection> rows)
	{
		this.rows = rows;
	}

	private String total;
	private String pageStartOffset;
	private String pageSize;
	private String pageNo;
	private String pageCount;
	
	private List<MyCollection> rows;
}
