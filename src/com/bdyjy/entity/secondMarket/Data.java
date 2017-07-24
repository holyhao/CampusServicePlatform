package com.bdyjy.entity.secondMarket;



import java.io.Serializable;
import java.util.List;

/**
 * com.bdyjy.entity.Data
 * 
 * @author songdebin<br/>
 *         create at 2016-4-14 下午7:35:05
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
	public List<SecondMarket> getRows() {
		return rows;
	}

	/**
	 * @param rows
	 *            the rows to set
	 */
	public void setRows(List<SecondMarket> rows) {
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
	String pageStartOffset;
	String pageSize;
	String pageNo;
	List<SecondMarket> rows;
	String pageCount;
}
