/**
 * Data.java[v 1.0.0]
 * class:com.bdyjy.entity.firstPageImg,Data
 * 周航 create at 2016-5-17 下午4:10:44
 */
package com.bdyjy.entity.firstPageImg;

import java.util.List;

/**
 * com.bdyjy.entity.firstPageImg.Data
 * @author 周航<br/> 
 * create at 2016-5-17 下午4:10:44
 */
public class Data
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

	public String getPageNo()
	{
		return pageNo;
	}

	public void setPageNo(String pageNo)
	{
		this.pageNo = pageNo;
	}

	public String getPageSize()
	{
		return pageSize;
	}

	public void setPageSize(String pageSize)
	{
		this.pageSize = pageSize;
	}

	public String getPageCount()
	{
		return pageCount;
	}

	public void setPageCount(String pageCount)
	{
		this.pageCount = pageCount;
	}

	public List<Rows> getRows()
	{
		return rows;
	}

	public void setRows(List<Rows> rows)
	{
		this.rows = rows;
	}

	private String total;
	private String pageStartOffset;
	private String pageNo;
	private String pageSize;
	private String pageCount;
	
	private List<Rows> rows;
}	
