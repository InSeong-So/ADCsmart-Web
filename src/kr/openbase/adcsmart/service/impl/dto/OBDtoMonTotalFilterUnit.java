package kr.openbase.adcsmart.service.impl.dto;

public class OBDtoMonTotalFilterUnit
{
	private Integer index;
	private String title;
	private String value;
	private boolean isSelect;
	
	@Override
	public String toString()
	{
		return "OBDtoMonFilterUnit [index=" + index + ", title=" + title
				+ ", value=" + value + ", isSelect=" + isSelect + "]";
	}
	public OBDtoMonTotalFilterUnit(Integer index, String title, String value, boolean isSelect)
	{
		this.setIndex(index);
		this.setTitle(title);
		this.setValue(value);
		this.setSelect(isSelect);
	}
	public Integer getIndex()
	{
		return index;
	}
	public void setIndex(Integer index)
	{
		this.index = index;
	}
	public String getTitle()
	{
		return title;
	}
	public void setTitle(String title)
	{
		this.title = title;
	}
	public String getValue()
	{
		return value;
	}
	public void setValue(String value)
	{
		this.value = value;
	}
	public boolean isSelect()
	{
		return isSelect;
	}
	public void setSelect(boolean isSelect)
	{
		this.isSelect = isSelect;
	}
}
