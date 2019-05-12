package kr.openbase.adcsmart.service.dto;

import java.util.Date;

public class OBDtoBackupInfo
{
	private String index;
	private Date	occurTime;
	private Integer status;
	private String fileName;
	private String accntID;
	private Integer accntIndex;
	private String comments;
	private Integer type;//0:전체. 1: ADC 설정정보. 2: ADC 로그 정보.
	private Long	fileSize;//Byte 단위.
	private boolean isLogDelete;
	private	Integer ftpYN;
	@Override
	public String toString()
	{
		return "OBDtoBackupInfo [index=" + index + ", occurTime=" + occurTime
				+ ", status=" + status + ", fileName=" + fileName
				+ ", accntID=" + accntID + ", accntIndex=" + accntIndex
				+ ", comments=" + comments + ", type=" + type + ", fileSize="
				+ fileSize + ", isLogDelete=" + isLogDelete + ", ftpYN="
				+ ftpYN + "]";
	}
	public Integer getFtpYN()
	{
		return ftpYN;
	}
	public void setFtpYN(Integer ftpYN)
	{
		this.ftpYN = ftpYN;
	}
	public boolean isLogDelete()
	{
		return isLogDelete;
	}
	public void setLogDelete(boolean isLogDelete)
	{
		this.isLogDelete = isLogDelete;
	}
	public String getIndex()
	{
		return index;
	}
	public void setIndex(String index)
	{
		this.index = index;
	}
	public Date getOccurTime()
	{
		return occurTime;
	}
	public void setOccurTime(Date occurTime)
	{
		this.occurTime = occurTime;
	}
	public Integer getStatus()
	{
		return status;
	}
	public void setStatus(Integer status)
	{
		this.status = status;
	}
	public String getFileName()
	{
		return fileName;
	}
	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}
	public String getAccntID()
	{
		return accntID;
	}
	public void setAccntID(String accntID)
	{
		this.accntID = accntID;
	}
	public Integer getAccntIndex()
	{
		return accntIndex;
	}
	public void setAccntIndex(Integer accntIndex)
	{
		this.accntIndex = accntIndex;
	}
	public String getComments()
	{
		return comments;
	}
	public void setComments(String comments)
	{
		this.comments = comments;
	}
	public Integer getType()
	{
		return type;
	}
	public void setType(Integer type)
	{
		this.type = type;
	}
	public Long getFileSize()
	{
		return fileSize;
	}
	public void setFileSize(Long fileSize)
	{
		this.fileSize = fileSize;
	}
}
