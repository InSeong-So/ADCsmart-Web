package kr.openbase.adcsmart.web.facade.dto.adcmon;

public class OBDtoAdcmonConfigHistory
{
	private Integer rank=0;
	private String content="";
	private String adcName="";//툴팁 내용추가
	private String vsIP="";//툴팁 내용추가
	@Override
	public String toString()
	{
		return "OBDtoAdcmonConfigHistory [rank=" + rank + ", content="
				+ content + ", adcName=" + adcName + ", vsIP=" + vsIP + "]";
	}
	public String getVsIP()
	{
		return vsIP;
	}
	public void setVsIP(String vsIP)
	{
		this.vsIP = vsIP;
	}
	public String getAdcName()
	{
		return adcName;
	}
	public void setAdcName(String adcName)
	{
		this.adcName = adcName;
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
