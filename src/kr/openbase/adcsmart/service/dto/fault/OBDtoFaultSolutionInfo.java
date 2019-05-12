package kr.openbase.adcsmart.service.dto.fault;

public class OBDtoFaultSolutionInfo
{
	private Integer category;
	private Integer index;
	private String  description;
	public Integer getCategory()
	{
		return category;
	}
	public void setCategory(Integer category)
	{
		this.category = category;
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
