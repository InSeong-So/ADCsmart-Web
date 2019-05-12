package kr.openbase.adcsmart.service.impl.dto;

public class OBDtoMonTotalGroupOne
{
	private String    index;
	private String    name;
	private String    id; //adc에서 쓰는 인덱스
	private String    backup;
	private Integer   member; //서비스의 전체 멤버
	private Integer   filter; //그룹이 연관된 필터수
	private Integer   vsAssigned; //group을 쓰고 있는 virtual server

	private Integer   adcIndex;
	private String    adcName;
	private String    adcIp;
	private Integer   adcType; //화면에 표시하지 않지만 필요

	@Override
	public String toString()
	{
		return "OBDtoMonTotalGroupOne [index=" + index + ", name=" + name
				+ ", id=" + id + ", backup=" + backup + ", member=" + member
				+ ", filter=" + filter + ", vsAssigned=" + vsAssigned
				+ ", adcIndex=" + adcIndex + ", adcName=" + adcName
				+ ", adcIp=" + adcIp + ", adcType=" + adcType + "]";
	}	
	public String getIndex()
	{
		return index;
	}
	public void setIndex(String index)
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
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id = id;
	}
	public String getBackup()
	{
		return backup;
	}
	public void setBackup(String backup)
	{
		this.backup = backup;
	}
	public Integer getMember()
	{
		return member;
	}
	public void setMember(Integer member)
	{
		this.member = member;
	}
	public Integer getFilter()
	{
		return filter;
	}
	public void setFilter(Integer filter)
	{
		this.filter = filter;
	}
	public Integer getVsAssigned()
	{
		return vsAssigned;
	}
	public void setVsAssigned(Integer vsAssigned)
	{
		this.vsAssigned = vsAssigned;
	}
	public Integer getAdcIndex()
	{
		return adcIndex;
	}
	public void setAdcIndex(Integer adcIndex)
	{
		this.adcIndex = adcIndex;
	}
	public String getAdcName()
	{
		return adcName;
	}
	public void setAdcName(String adcName)
	{
		this.adcName = adcName;
	}
	public String getAdcIp()
	{
		return adcIp;
	}
	public void setAdcIp(String adcIp)
	{
		this.adcIp = adcIp;
	}
	public Integer getAdcType()
	{
		return adcType;
	}
	public void setAdcType(Integer adcType)
	{
		this.adcType = adcType;
	}
}
