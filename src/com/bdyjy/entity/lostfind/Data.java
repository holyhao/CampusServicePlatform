package com.bdyjy.entity.lostfind;

import java.io.Serializable;
import java.util.List;


public class Data implements Serializable{
	
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
	public List<LostFind> getRows() {
		return rows;
	}

	/**
	 * @param rows
	 *            the rows to set
	 */
	public void setRows(List<LostFind> rows) {
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
	 * @return the contactRows
	 */




	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID =  0000000000000000111L;
	String total;
	String pageSize;
	String pageNo;
	List<LostFind> rows;
	String pageStartOffset;
	String pageCount;

}
