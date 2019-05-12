package kr.openbase.adcsmart.service.impl.dto;

import java.util.ArrayList;

import kr.openbase.adcsmart.service.utility.OBMessages;

public class OBDtoMonTotalGroupCondition
{
	private OBDtoMonTotalConditionUnit name;
	private OBDtoMonTotalConditionUnit id;
	private OBDtoMonTotalConditionUnit backup;
	private OBDtoMonTotalConditionUnit member;
	private OBDtoMonTotalConditionUnit filter;
	private OBDtoMonTotalConditionUnit vsAssigned;
	private OBDtoMonTotalConditionUnit adcName;
	private OBDtoMonTotalConditionUnit adcType;
	private OBDtoMonTotalConditionUnit adcIp;
	private String searchKeyword;
	
	public OBDtoMonTotalGroupCondition()
	{
		ArrayList<OBDtoMonTotalFilterUnit> temp = new ArrayList<OBDtoMonTotalFilterUnit>();
		
		this.name             = new OBDtoMonTotalConditionUnit(true, null);
		this.id               = new OBDtoMonTotalConditionUnit(true, null);

		temp.add(new OBDtoMonTotalFilterUnit(0, OBMessages.getMessage(OBMessages.MSG_DEFINEWEB_STATUS_TOTAL), "-1", true));
		temp.add(new OBDtoMonTotalFilterUnit(1, OBMessages.getMessage(OBMessages.MSG_DEFINEWEB_BACKUP_HAVE), "1", false));
		temp.add(new OBDtoMonTotalFilterUnit(2, OBMessages.getMessage(OBMessages.MSG_DEFINEWEB_BACKUP_NOT), "0", false));
		this.backup           = new OBDtoMonTotalConditionUnit(true, temp);

		this.member           = new OBDtoMonTotalConditionUnit(true, null);
		this.filter           = new OBDtoMonTotalConditionUnit(true, null);
		this.vsAssigned       = new OBDtoMonTotalConditionUnit(true, null);
		this.adcName          = new OBDtoMonTotalConditionUnit(true, null);
		this.adcType          = new OBDtoMonTotalConditionUnit(true, null);
		this.adcIp            = new OBDtoMonTotalConditionUnit(true, null);
	}
	
	@Override
	public String toString()
	{
		return "OBDtoMonTotalGroupCondition [name=" + name + ", id=" + id
				+ ", backup=" + backup + ", member=" + member + ", filter="
				+ filter + ", vsAssigned=" + vsAssigned + ", adcName="
				+ adcName + ", adcType=" + adcType + ", adcIp=" + adcIp
				+ ", searchKeyword=" + searchKeyword + "]";
	}

	public OBDtoMonTotalConditionUnit getName()
	{
		return name;
	}
	public void setName(OBDtoMonTotalConditionUnit name)
	{
		this.name = name;
	}
	public OBDtoMonTotalConditionUnit getId()
	{
		return id;
	}
	public void setId(OBDtoMonTotalConditionUnit id)
	{
		this.id = id;
	}
	public OBDtoMonTotalConditionUnit getBackup()
	{
		return backup;
	}
	public void setBackup(OBDtoMonTotalConditionUnit backup)
	{
		this.backup = backup;
	}
	public OBDtoMonTotalConditionUnit getMember()
	{
		return member;
	}
	public void setMember(OBDtoMonTotalConditionUnit member)
	{
		this.member = member;
	}
	public OBDtoMonTotalConditionUnit getFilter()
	{
		return filter;
	}
	public void setFilter(OBDtoMonTotalConditionUnit filter)
	{
		this.filter = filter;
	}
	public OBDtoMonTotalConditionUnit getVsAssigned()
	{
		return vsAssigned;
	}
	public void setVsAssigned(OBDtoMonTotalConditionUnit vsAssigned)
	{
		this.vsAssigned = vsAssigned;
	}
	public OBDtoMonTotalConditionUnit getAdcName() 
	{
		return adcName;
	}
	public void setAdcName(OBDtoMonTotalConditionUnit adcName) 
	{
		this.adcName = adcName;
	}
	public OBDtoMonTotalConditionUnit getAdcType()
	{
		return adcType;
	}
	public void setAdcType(OBDtoMonTotalConditionUnit adcType)
	{
		this.adcType = adcType;
	}
	public OBDtoMonTotalConditionUnit getAdcIp()
	{
		return adcIp;
	}
	public void setAdcIp(OBDtoMonTotalConditionUnit adcIp)
	{
		this.adcIp = adcIp;
	}
	public String getSearchKeyword()
	{
		return searchKeyword;
	}
	public void setSearchKeyword(String searchKeyword)
	{
		this.searchKeyword = searchKeyword;
	}
}
