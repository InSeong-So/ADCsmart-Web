package kr.openbase.adcsmart.service.dto;

public class OBDtoVlanInfo
{
	private String name;
	private String tag;
	private String untaggedInterfaces;
	private String taggedInterfaces;
	
	@Override
	public String toString()
	{
		return "OBDtoVlanInfo [name=" + name + ", tag=" + tag + ", untaggedInterfaces=" + untaggedInterfaces + ", taggedInterfaces=" + taggedInterfaces + "]";
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getTag()
	{
		return tag;
	}
	public void setTag(String tag)
	{
		this.tag = tag;
	}
	public String getUntaggedInterfaces()
	{
		return untaggedInterfaces;
	}
	public void setUntaggedInterfaces(String untaggedInterfaces)
	{
		this.untaggedInterfaces = untaggedInterfaces;
	}
	public String getTaggedInterfaces()
	{
		return taggedInterfaces;
	}
	public void setTaggedInterfaces(String taggedInterfaces)
	{
		this.taggedInterfaces = taggedInterfaces;
	}
}
