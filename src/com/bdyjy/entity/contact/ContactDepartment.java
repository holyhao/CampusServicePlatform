package com.bdyjy.entity.contact;

import java.io.Serializable;
import java.util.List;

public class ContactDepartment implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7268266104773182387L;

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public List<ContactContent> getData() {
		return data;
	}

	public void setData(List<ContactContent> data) {
		this.data = data;
	}

	public String getDepartment_name() {
		return department_name;
	}

	public void setDepartment_name(String department_name) {
		this.department_name = department_name;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	String total;
	List<ContactContent> data;
	String department_name;

}
