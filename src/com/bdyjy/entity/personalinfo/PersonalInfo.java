
package com.bdyjy.entity.personalinfo;

import java.io.Serializable;

/**
 *  com.bdyjy.entity.personalinfo;
 * 
 * @author holy
 */
public class PersonalInfo implements Serializable
{


	private static final long serialVersionUID = 2092379721191866291L;
	/**
	 * 
	 */
	
    String id;
	String userName; 	//姓名	
	String password; 	//	
	String mobile; 	//手机	
	String tel; 	//电话	
	String email; 	//邮箱	
	String college; 	//学院	
	String grade; 	//年级	
	String major; 	//专业	
	String department; 	//部门	
	String identifierNumber; 	//学号/工号	
	int permission; 	//分为六类（0、1、2、3、4、5）：管理员(0)、教职工(1)、普通学生(2)、活动信息发布权限(3)、招聘信息发布权限(4)、教务通知发布权限(5)、新闻发布权限（6）	
    String userImg;
    String sex;
	
	String createUser;
	String createTime;
	String lastUpdateTime;
	String lastUpdateUser;
	String jsonUpdateFlag;
	String mybatisRecordCount;
	String orderNo;
	String attachmentPrefix;
	
	
	


	public String getUserImg() {
		return userImg;
	}





	public void setUserImg(String userImg) {
		this.userImg = userImg;
	}





	public String getSex() {
		return sex;
	}





	public void setSex(String sex) {
		this.sex = sex;
	}





	public String getId() {
		return id;
	}





	public void setId(String id) {
		this.id = id;
	}






	public String getUserName() {
		return userName;
	}





	public void setUserName(String userName) {
		this.userName = userName;
	}





	public String getPassword() {
		return password;
	}





	public void setPassword(String password) {
		this.password = password;
	}





	public String getMobile() {
		return mobile;
	}





	public void setMobile(String mobile) {
		this.mobile = mobile;
	}





	public String getTel() {
		return tel;
	}





	public void setTel(String tel) {
		this.tel = tel;
	}





	public String getEmail() {
		return email;
	}





	public void setEmail(String email) {
		this.email = email;
	}





	public String getCollege() {
		return college;
	}





	public void setCollege(String college) {
		this.college = college;
	}





	public String getGrade() {
		return grade;
	}





	public void setGrade(String grade) {
		this.grade = grade;
	}





	public String getMajor() {
		return major;
	}





	public void setMajor(String major) {
		this.major = major;
	}





	public String getDepartment() {
		return department;
	}





	public void setDepartment(String department) {
		this.department = department;
	}





	public String getIdentifierNumber() {
		return identifierNumber;
	}





	public void setIdentifierNumber(String identifierNumber) {
		this.identifierNumber = identifierNumber;
	}





	public int getPermission() {
		return permission;
	}





	public void setPermission(int permission) {
		this.permission = permission;
	}





	public String getCreateUser() {
		return createUser;
	}





	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}





	public String getCreateTime() {
		return createTime;
	}





	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}





	public String getLastUpdateTime() {
		return lastUpdateTime;
	}





	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}





	public String getLastUpdateUser() {
		return lastUpdateUser;
	}





	public void setLastUpdateUser(String lastUpdateUser) {
		this.lastUpdateUser = lastUpdateUser;
	}





	public String getJsonUpdateFlag() {
		return jsonUpdateFlag;
	}





	public void setJsonUpdateFlag(String jsonUpdateFlag) {
		this.jsonUpdateFlag = jsonUpdateFlag;
	}





	public String getMybatisRecordCount() {
		return mybatisRecordCount;
	}





	public void setMybatisRecordCount(String mybatisRecordCount) {
		this.mybatisRecordCount = mybatisRecordCount;
	}





	public String getOrderNo() {
		return orderNo;
	}





	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}





	public String getAttachmentPrefix() {
		return attachmentPrefix;
	}





	public void setAttachmentPrefix(String attachmentPrefix) {
		this.attachmentPrefix = attachmentPrefix;
	}





	public static long getSerialversionUID(){
	
		return serialVersionUID;
	}


}