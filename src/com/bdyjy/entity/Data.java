/**
 * Data.java[v 1.0.0]
 * class:com.bdyjy.entity,Data
 * 周航 create at 2016-3-23 下午7:35:05
 */
package com.bdyjy.entity;

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
	 * @return the total
	 */
	public String getTotal() {
		return total;
	}

	/**
	 * @param total
	 *            the total to set
	 */
	public void setTotal(String total) {
		this.total = total;
	}

	/**
	 * @return the pageStartOffset
	 */
	public String getPageStartOffset() {
		return pageStartOffset;
	}

	/**
	 * @param pageStartOffset
	 *            the pageStartOffset to set
	 */
	public void setPageStartOffset(String pageStartOffset) {
		this.pageStartOffset = pageStartOffset;
	}

	/**
	 * @return the pageSize
	 */
	public String getPageSize() {
		return pageSize;
	}

	/**
	 * @param pageSize
	 *            the pageSize to set
	 */
	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * @return the pageNo
	 */
	public String getPageNo() {
		return pageNo;
	}

	/**
	 * @param pageNo
	 *            the pageNo to set
	 */
	public void setPageNo(String pageNo) {
		this.pageNo = pageNo;
	}

	/**
	 * @return the rows
	 */
	public List<News> getRows() {
		return rows;
	}

	/**
	 * @param rows
	 *            the rows to set
	 */
	public void setRows(List<News> rows) {
		this.rows = rows;
	}

	/**
	 * @return the pageCount
	 */
	public String getPageCount() {
		return pageCount;
	}

	/**
	 * @param pageCount
	 *            the pageCount to set
	 */
	public void setPageCount(String pageCount) {
		this.pageCount = pageCount;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -8165553687587705005L;
	String total;
	String pageStartOffset;
	String pageSize;
	String pageNo;
	List<News> rows;
	String pageCount;
}