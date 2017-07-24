package com.bdyjy.entity.notice;

import java.io.Serializable;

public class NoticeResultBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8546141497976780311L;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public NoticeContentById getDetail() {
		return detail;
	}

	public void setDetail(NoticeContentById detail) {
		this.detail = detail;
	}

	String code;

	NoticeContentById detail;

}
