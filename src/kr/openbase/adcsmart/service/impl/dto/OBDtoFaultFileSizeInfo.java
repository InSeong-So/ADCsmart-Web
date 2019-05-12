package kr.openbase.adcsmart.service.impl.dto;

public class OBDtoFaultFileSizeInfo
{
	private String fileName;
	private Long	fileSize;
	@Override
	public String toString()
	{
		return String.format("OBDtoFaultFileSizeInfo [fileName=%s, fileSize=%s]", fileName, fileSize);
	}
	public String getFileName()
	{
		return fileName;
	}
	public void setFileName(String fileName)
	{
		this.fileName = fileName;
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
