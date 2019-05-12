package kr.openbase.adcsmart.service.dto;

public class OBDtoNoticeInfo
{
	private String 	message;
	private Integer index;
	
	@Override
	public String toString()
	{
		return "OBDtoNoticeInfo [message=" + message + ", index=" + index + "]";
	}
	public String getMessage()
	{
		return message;
	}
	public void setMessage(String message)
	{
		this.message = message;
	}
	public Integer getIndex()
	{
		return index;
	}
	public void setIndex(Integer index)
	{
		this.index = index;
	}
}
