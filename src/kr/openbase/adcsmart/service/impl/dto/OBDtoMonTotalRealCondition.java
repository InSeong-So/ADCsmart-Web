package kr.openbase.adcsmart.service.impl.dto;

import java.util.ArrayList;

import kr.openbase.adcsmart.service.utility.OBMessages;

public class OBDtoMonTotalRealCondition
{
	private OBDtoMonTotalConditionUnit status;
	private OBDtoMonTotalConditionUnit state;
	private OBDtoMonTotalConditionUnit name;
	private OBDtoMonTotalConditionUnit ip;
	private OBDtoMonTotalConditionUnit backup;
	private OBDtoMonTotalConditionUnit used;
	private OBDtoMonTotalConditionUnit group;
	private OBDtoMonTotalConditionUnit ratio;
	
	private OBDtoMonTotalConditionUnit adcName;
	private OBDtoMonTotalConditionUnit adcType;
	private OBDtoMonTotalConditionUnit adcIp;
	
	private OBDtoMonTotalConditionUnit alteonId;

	private String searchKeyword;
	
	public OBDtoMonTotalRealCondition()
	{
		ArrayList<OBDtoMonTotalFilterUnit> temp = new ArrayList<OBDtoMonTotalFilterUnit>();
		temp.add(new OBDtoMonTotalFilterUnit(0, OBMessages.getMessage(OBMessages.MSG_DEFINEWEB_STATUS_TOTAL), "-1", true));
		temp.add(new OBDtoMonTotalFilterUnit(1, OBMessages.getMessage(OBMessages.MSG_DEFINEWEB_STATUS_ONLINE), "1", false));
		temp.add(new OBDtoMonTotalFilterUnit(2, OBMessages.getMessage(OBMessages.MSG_DEFINEWEB_STATUS_OFFLINE), "0", false));
		temp.add(new OBDtoMonTotalFilterUnit(3, OBMessages.getMessage(OBMessages.MSG_DEFINEWEB_STATUS_DISABLED), "2", false));
		this.status            = new OBDtoMonTotalConditionUnit(true, temp);
		
		temp = new ArrayList<OBDtoMonTotalFilterUnit>();
		temp.add(new OBDtoMonTotalFilterUnit(0, OBMessages.getMessage(OBMessages.MSG_DEFINEWEB_STATUS_TOTAL), "-1", true));
		temp.add(new OBDtoMonTotalFilterUnit(1, "Enabled", "1", false));
		temp.add(new OBDtoMonTotalFilterUnit(2, "Disabled", "0", false));
		temp.add(new OBDtoMonTotalFilterUnit(3, "Forced Offline", "2", false));
		this.state             = new OBDtoMonTotalConditionUnit(true, temp);
		
		this.name              = new OBDtoMonTotalConditionUnit(true, null); //필터없음
		this.ip                = new OBDtoMonTotalConditionUnit(true, null); //필터없음
		
		temp = new ArrayList<OBDtoMonTotalFilterUnit>();
		temp.add(new OBDtoMonTotalFilterUnit(0, OBMessages.getMessage(OBMessages.MSG_DEFINEWEB_STATUS_TOTAL), "-1", true));
		temp.add(new OBDtoMonTotalFilterUnit(1, OBMessages.getMessage(OBMessages.MSG_DEFINEWEB_BACKUP_HAVE), "1", false));
		temp.add(new OBDtoMonTotalFilterUnit(2, OBMessages.getMessage(OBMessages.MSG_DEFINEWEB_BACKUP_NOT), "0", false));
		this.backup            = new OBDtoMonTotalConditionUnit(true, temp);
		
		temp = new ArrayList<OBDtoMonTotalFilterUnit>();
		temp.add(new OBDtoMonTotalFilterUnit(0, OBMessages.getMessage(OBMessages.MSG_DEFINEWEB_STATUS_TOTAL), "-1", true));
		temp.add(new OBDtoMonTotalFilterUnit(1, OBMessages.getMessage(OBMessages.MSG_DEFINEWEB_USEORNOT_USE), "1", false));
		temp.add(new OBDtoMonTotalFilterUnit(2, OBMessages.getMessage(OBMessages.MSG_DEFINEWEB_USEORNOT_UNUSE), "0", false));
		this.used              = new OBDtoMonTotalConditionUnit(true, temp);

		this.group             = new OBDtoMonTotalConditionUnit(true, null);
		this.ratio             = new OBDtoMonTotalConditionUnit(true, null);
		
		this.adcName           = new OBDtoMonTotalConditionUnit(true, null);
		this.adcType           = new OBDtoMonTotalConditionUnit(true, null);
		this.adcIp             = new OBDtoMonTotalConditionUnit(true, null);
		
		this.alteonId          = new OBDtoMonTotalConditionUnit(true, null);

	}

	@Override
    public String toString()
    {
        return "OBDtoMonTotalRealCondition [status=" + status + ", state="
                + state + ", name=" + name + ", ip=" + ip + ", backup=" + backup
                + ", used=" + used + ", group=" + group + ", ratio=" + ratio
                + ", adcName=" + adcName + ", adcType=" + adcType + ", adcIp="
                + adcIp + ", alteonId=" + alteonId + ", searchKeyword="
                + searchKeyword + "]";
    }

	public OBDtoMonTotalConditionUnit getStatus()
	{
		return status;
	}
	public void setStatus(OBDtoMonTotalConditionUnit status)
	{
		this.status = status;
	}
	public OBDtoMonTotalConditionUnit getState()
	{
		return state;
	}
	public void setState(OBDtoMonTotalConditionUnit state)
	{
		this.state = state;
	}
	public OBDtoMonTotalConditionUnit getName()
	{
		return name;
	}
	public void setName(OBDtoMonTotalConditionUnit name)
	{
		this.name = name;
	}
	public OBDtoMonTotalConditionUnit getIp()
	{
		return ip;
	}
	public void setIp(OBDtoMonTotalConditionUnit ip)
	{
		this.ip = ip;
	}
	public OBDtoMonTotalConditionUnit getBackup()
	{
		return backup;
	}
	public void setBackup(OBDtoMonTotalConditionUnit backup)
	{
		this.backup = backup;
	}
	public OBDtoMonTotalConditionUnit getUsed()
	{
		return used;
	}
	public void setUsed(OBDtoMonTotalConditionUnit used)
	{
		this.used = used;
	}
	public OBDtoMonTotalConditionUnit getGroup()
	{
		return group;
	}
	public void setGroup(OBDtoMonTotalConditionUnit group)
	{
		this.group = group;
	}
	public OBDtoMonTotalConditionUnit getRatio()
	{
		return ratio;
	}
	public void setRatio(OBDtoMonTotalConditionUnit ratio)
	{
		this.ratio = ratio;
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

    public OBDtoMonTotalConditionUnit getAlteonId()
    {
        return alteonId;
    }

    public void setAlteonId(OBDtoMonTotalConditionUnit alteonId)
    {
        this.alteonId = alteonId;
    }
}
