package kr.openbase.adcsmart.service.dto;


public class OBDtoAdcRealServerGroup
{
	private int groupIndex;
	private String groupName;
	private int accntIndex;
	private int available; // 0 : 그룹 삭제   1: 그룹 추가
	private String description;
	
	@Override
	public String toString()
	{
		return "OBDtoAdcRealServerGroup [groupIndex=" + groupIndex + ", groupName=" + groupName + ", accntIndex=" + accntIndex + ", available=" + available + ", description=" + description + "]";
	}
	public int getGroupIndex()
	{
		return groupIndex;
	}
	public void setGroupIndex(int groupIndex)
	{
		this.groupIndex = groupIndex;
	}
	public String getGroupName()
	{
		return groupName;
	}
	public void setGroupName(String groupName)
	{
		this.groupName = groupName;
	}
	public int getAccntIndex()
	{
		return accntIndex;
	}
	public void setAccntIndex(int accntIndex)
	{
		this.accntIndex = accntIndex;
	}
	public int getAvailable()
	{
		return available;
	}
	public void setAvailable(int available)
	{
		this.available = available;
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
