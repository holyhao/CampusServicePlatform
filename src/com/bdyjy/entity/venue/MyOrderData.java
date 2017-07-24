/**
 * MyOrderData.java[v 1.0.0]
 * class:com.bdyjy.entity.venue,MyOrderData
 * 周航 create at 2016-4-8 下午4:40:40
 */
package com.bdyjy.entity.venue;

import java.util.List;

/**
 * com.bdyjy.entity.venue.MyOrderData
 * 
 * @author 周航<br/>
 *         create at 2016-4-8 下午4:40:40
 */
public class MyOrderData
{

	public String getTotal()
	{
		return total;
	}

	public void setTotal(String total)
	{
		this.total = total;
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

	public String getPageStartOffset()
	{
		return pageStartOffset;
	}

	public void setPageStartOffset(String pageStartOffset)
	{
		this.pageStartOffset = pageStartOffset;
	}

	public String getPageCount()
	{
		return pageCount;
	}

	public void setPageCount(String pageCount)
	{
		this.pageCount = pageCount;
	}

	public List<MyOrderDataRow> getRows()
	{
		return rows;
	}

	public void setRows(List<MyOrderDataRow> rows)
	{
		this.rows = rows;
	}

	String total;
	String pageSize;
	String pageNo;
	String pageStartOffset;
	String pageCount;

	List<MyOrderDataRow> rows;

}
