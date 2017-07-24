/**
 * Data.java[v 1.0.0]
 * class:com.bdyjy.entity,Data
 * 周航 create at 2016-3-23 下午7:35:05
 */
package com.bdyjy.entity.activity;

import java.io.Serializable;
import java.util.List;

/**
 * com.bdyjy.entity.Data
 * 
 * @author 周航<br/>
 *         create at 2016-3-23 下午7:35:05
 */
public class Data implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6173371939025743216L;

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getPageStartOffset() {
		return pageStartOffset;
	}

	public void setPageStartOffset(String pageStartOffset) {
		this.pageStartOffset = pageStartOffset;
	}

	public String getPageSize() {
		return pageSize;
	}

	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}

	public String getPageNo() {
		return pageNo;
	}

	public void setPageNo(String pageNo) {
		this.pageNo = pageNo;
	}

	public List<Activity> getRows() {
		return rows;
	}

	public void setRows(List<Activity> rows) {
		this.rows = rows;
	}

	public String getPageCount() {
		return pageCount;
	}

	public void setPageCount(String pageCount) {
		this.pageCount = pageCount;
	}

	String total;
	String pageStartOffset;
	String pageSize;
	String pageNo;
	List<Activity> rows;
	String pageCount;
}