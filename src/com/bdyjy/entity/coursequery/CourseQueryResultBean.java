package com.bdyjy.entity.coursequery;

import java.io.Serializable;

public class CourseQueryResultBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5670826297642108182L;
	
	Course event;

	public Course getEvent() {
		return event;
	}

	
	public void setEvent(Course event) {
		this.event = event;
	}
	
	
}
