package kr.openbase.adcsmart.service.dto.report;

public class OBDtoRptSysInfo
{
	private String name="N/A";
	private String result="N/A";//정상, 비정상, 사용, 전체, 개수등등..
	private String contents="N/A";
	@Override
	public String toString()
	{
		return "OBDtoRptSysInfo [name=" + name + ", result=" + result
				+ ", contents=" + contents + "]";
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getResult()
	{
		return result;
	}
	public void setResult(String result)
	{
		this.result = result;
	}
	public String getContents()
	{
		return contents;
	}
	public void setContents(String contents)
	{
		this.contents = contents;
	}
}
