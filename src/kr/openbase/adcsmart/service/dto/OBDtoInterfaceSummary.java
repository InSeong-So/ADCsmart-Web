package kr.openbase.adcsmart.service.dto;

public class OBDtoInterfaceSummary
{   //
	private Integer index;
	private String description;

	@Override
	public String toString()
	{
		return "OBDtoInterfaceSummary [index=" + index + ", description="
				+ description + "]";
	}
	public Integer getIndex()
	{
		return index;
	}
	public void setIndex(Integer index)
	{
		this.index = index;
	}
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description = description;
	}
}
