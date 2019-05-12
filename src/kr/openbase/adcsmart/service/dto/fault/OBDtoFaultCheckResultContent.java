package kr.openbase.adcsmart.service.dto.fault;

public class OBDtoFaultCheckResultContent
{
	private String summary;
	private String detail;
	@Override
	public String toString()
	{
		return String.format("OBDtoFaultCheckResultContent [summary=%s, detail=%s]", summary, detail);
	}
	public String getSummary()
	{
		return summary;
	}
	public void setSummary(String summary)
	{
		this.summary = summary;
	}
	public String getDetail()
	{
		return detail;
	}
	public void setDetail(String detail)
	{
		this.detail = detail;
	}
}
