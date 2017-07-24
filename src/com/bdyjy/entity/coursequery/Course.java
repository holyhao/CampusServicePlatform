package com.bdyjy.entity.coursequery;

import java.io.Serializable;

public class Course implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4604106448714717704L;
	
	String course_name;
	String stime;
	String etime;
	String classroom_name;
	String title;
	
	
	/**
	 * getters and setters
	 * 
	 */
	
	public String getCourse_name() {
		return course_name;
	}
	
	public void setCourse_name(String course_name) {
		this.course_name = course_name;
	}
	
	
	
	public String getStime() {
		return stime;
	}

	public void setStime(String stime) {
		this.stime = stime;
	}

	public String getEtime() {
		return etime;
	}

	public void setEtime(String etime) {
		this.etime = etime;
	}

	public String getClassroom_name() {
		return classroom_name;
	}
	
	public void setClassroom_name(String classroom_name) {
		this.classroom_name = classroom_name;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
}
