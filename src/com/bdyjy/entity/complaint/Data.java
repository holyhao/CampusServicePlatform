package com.bdyjy.entity.complaint;

import java.io.Serializable;
import java.util.List;

public class Data implements Serializable {
		
	/**
	 * @author holy
	 * 投诉建议
	 */
	private static final long serialVersionUID = 1269902487574635697L;
		String total;
		String pageSize;
		String pageNo;
		List<Complaints> rows;
		String pageStartOffset;
		String pageCount;
		
		
		/**
		 * getters and setters
		 *  
		 */
		public String getTotal() {
			return total;
		}
		public void setTotal(String total) {
			this.total = total;
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
		public List<Complaints> getRows() {
			return rows;
		}
		public void setRows(List<Complaints> rows) {
			this.rows = rows;
		}
		public String getPageStartOffset() {
			return pageStartOffset;
		}
		public void setPageStartOffset(String pageStartOffset) {
			this.pageStartOffset = pageStartOffset;
		}
		public String getPageCount() {
			return pageCount;
		}
		public void setPageCount(String pageCount) {
			this.pageCount = pageCount;
		}
			
	}
