package kr.openbase.adcsmart.service.dashboard.dto;

public class OBDtoWidgetItemInfo
{
	private Integer index;
	private String  name;
	private String  description;
	private Integer widthMinSize;
	private Integer widthMaxSize;
	private Integer heightMinSize;
	private Integer heightMaxSize;
	private Integer targetArea;
	@Override
	public String toString()
	{
		return String.format("OBDtoWidgetItemInfo [index=%s, name=%s, description=%s, widthMinSize=%s, widthMaxSize=%s, heightMinSize=%s, heightMaxSize=%s, targetArea=%s]", index, name, description, widthMinSize, widthMaxSize, heightMinSize, heightMaxSize, targetArea);
	}
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description = description;
	}
	public Integer getIndex()
	{
		return index;
	}
	public void setIndex(Integer index)
	{
		this.index = index;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}	
	public Integer getWidthMinSize()
	{
		return widthMinSize;
	}
	public void setWidthMinSize(Integer widthMinSize)
	{
		this.widthMinSize = widthMinSize;
	}
	public Integer getWidthMaxSize()
	{
		return widthMaxSize;
	}
	public void setWidthMaxSize(Integer widthMaxSize)
	{
		this.widthMaxSize = widthMaxSize;
	}
	public Integer getHeightMinSize()
	{
		return heightMinSize;
	}
	public void setHeightMinSize(Integer heightMinSize)
	{
		this.heightMinSize = heightMinSize;
	}
	public Integer getHeightMaxSize()
	{
		return heightMaxSize;
	}
	public void setHeightMaxSize(Integer heightMaxSize)
	{
		this.heightMaxSize = heightMaxSize;
	}
	public Integer getTargetArea()
	{
		return targetArea;
	}
	public void setTargetArea(Integer targetArea)
	{
		this.targetArea = targetArea;
	}
}
