package com.bdyjy.entity.notice;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Notice implements Serializable {
	/**
	 * com.bdyjy.entity.Notice
	 * 
	 * @author cuicui
	 * 
	 *         create at 2016-3-26 上午11:22
	 * 
	 */
	private static final long serialVersionUID = 8653830484215407912L;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<NoticeContent> getList() {
		return list;
	}

	public void setList(List<NoticeContent> list) {
		this.list = list;
	}

	String code;
	String total;

	List<NoticeContent> list;
}
