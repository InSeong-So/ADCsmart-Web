package kr.openbase.adcsmart.web.facade.dto;

public class OBDtoAdcSlbChange
{
	private	Integer  	rank=0;
	private String			content="";
	@Override
	public String toString()
	{
		return "OBDtoAdcSlbChange [rank=" + rank + ", content=" + content + "]";
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
}
