package kr.openbase.adcsmart.web.facade.dto.adcmon;

public class OBDtoAdcmonSystemLog
{
	private Integer rank=0;
	private String content="";
	private String occurred="";
	private boolean dateChk;
	@Override
	public String toString()
	{
		return "OBDtoAdcmonSystemLog [rank=" + rank + ", content=" + content + ", occurred=" + occurred + "]";
	}
	public Integer getRank()
	{
		return rank;
	}
	public void setRank(Integer rank)
	{
		this.rank = rank;
	}
	public String getContent()
	{
		return content;
	}
	public void setContent(String content)
	{
		this.content = content;
	}
	public String getOccurred()
	{
		return occurred;
	}
	public void setOccurred(String occurred)
	{
		this.occurred = occurred;
	}
	public boolean isDateChk()
	{
		return dateChk;
	}
	public void setDateChk(boolean dateChk)
	{
		this.dateChk = dateChk;
	}
	
}
