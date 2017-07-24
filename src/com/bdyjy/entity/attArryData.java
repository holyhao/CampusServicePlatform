package com.bdyjy.entity;

import java.io.Serializable;

public class attArryData implements Serializable
{

	private Integer id; 	//附件ID	
	private String fileName; 	//文件名称	
	private String fileType; 	//文件类型	
	private String fileSize; 	//文件大小	
	private String creationTime; 	//上传时间	
	private String creationPerson; 	//创建人	
	private String filePath; 	//文件路径	
	private int businessType; 	//附件类型 
	private String foreignId; 	//外键ID 比如可以是下订单打款时候的外键ID	


 	public void setId(Integer id){	
 		this.id=id;	
 	}	
 
 	public Integer getId(){	
 		return this.id;	
 	}	
 
 	public void setFileName(String fileName){	
 		this.fileName=fileName;	
 	}	
 
 	public String getFileName(){	
 		return this.fileName;	
 	}	
 
 	public void setFileType(String fileType){	
 		this.fileType=fileType;	
 	}	
 
 	public String getFileType(){	
 		return this.fileType;	
 	}	
 
 	public void setFileSize(String fileSize){	
 		this.fileSize=fileSize;	
 	}	
 
 	public String getFileSize(){	
 		return this.fileSize;	
 	}	
 
 	public void setCreationTime(String creationTime){	
 		this.creationTime=creationTime;	
 	}	
 
 	public String getCreationTime(){	
 		return this.creationTime;	
 	}	
 
 	public void setCreationPerson(String creationPerson){	
 		this.creationPerson=creationPerson;	
 	}	
 
 	public String getCreationPerson(){	
 		return this.creationPerson;	
 	}	
 
 	public void setFilePath(String filePath){	
 		this.filePath=filePath;	
 	}	
 
 	public String getFilePath(){	
 		return this.filePath;	
 	}	
 
 	public void setBusinessType(int businessType){	
 		this.businessType=businessType;	
 	}	
 
 	public int getBusinessType(){	
 		return this.businessType;	
 	}	
 
 	public void setForeignId(String foreignId){	
 		this.foreignId=foreignId;	
 	}	
 
 	public String getForeignId(){	
 		return this.foreignId;	
 	}	
 
	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid()
	{
		return serialVersionUID;
	}

	private static final long serialVersionUID = 0000000000000001022L;	
	
	
}
