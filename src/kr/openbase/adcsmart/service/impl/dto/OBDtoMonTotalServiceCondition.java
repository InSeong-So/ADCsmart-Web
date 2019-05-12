package kr.openbase.adcsmart.service.impl.dto;

import java.util.ArrayList;

import kr.openbase.adcsmart.service.utility.OBMessages;

public class OBDtoMonTotalServiceCondition
{
	private OBDtoMonTotalConditionUnit status;
	private OBDtoMonTotalConditionUnit name;
	private OBDtoMonTotalConditionUnit ip;
	private OBDtoMonTotalConditionUnit port;
	private OBDtoMonTotalConditionUnit bpsIn;
	private OBDtoMonTotalConditionUnit bpsOut;
	private OBDtoMonTotalConditionUnit bpsTotal;
	private OBDtoMonTotalConditionUnit concurrentSession;
	private OBDtoMonTotalConditionUnit adcName;
	private OBDtoMonTotalConditionUnit adcType;
	private OBDtoMonTotalConditionUnit adcIp;
	private OBDtoMonTotalConditionUnit member;
	private OBDtoMonTotalConditionUnit group;
	private OBDtoMonTotalConditionUnit loadbalancing;
	private OBDtoMonTotalConditionUnit healthCheck;
	private OBDtoMonTotalConditionUnit persistence;
	private OBDtoMonTotalConditionUnit noticeGroup;
	private OBDtoMonTotalConditionUnit updateTime;
	private OBDtoMonTotalConditionUnit configHistory;
	private String searchKeyword;
	
	public OBDtoMonTotalServiceCondition()
	{
		ArrayList<OBDtoMonTotalFilterUnit> temp = new ArrayList<OBDtoMonTotalFilterUnit>();
		temp.add(new OBDtoMonTotalFilterUnit(0, OBMessages.getMessage(OBMessages.MSG_DEFINEWEB_STATUS_TOTAL), "-1", true));
		temp.add(new OBDtoMonTotalFilterUnit(1, OBMessages.getMessage(OBMessages.MSG_DEFINEWEB_STATUS_ONLINE), "1", false));
		temp.add(new OBDtoMonTotalFilterUnit(2, OBMessages.getMessage(OBMessages.MSG_DEFINEWEB_STATUS_OFFLINE), "2", false));
		temp.add(new OBDtoMonTotalFilterUnit(3, OBMessages.getMessage(OBMessages.MSG_DEFINEWEB_STATUS_DISABLED), "0", false));
		this.status            = new OBDtoMonTotalConditionUnit(true, temp);
		
		this.name              = new OBDtoMonTotalConditionUnit(true, null); //필터없음
		this.ip                = new OBDtoMonTotalConditionUnit(true, null); //필터없음
		
		temp = new ArrayList<OBDtoMonTotalFilterUnit>();
		temp.add(new OBDtoMonTotalFilterUnit(0, OBMessages.getMessage(OBMessages.MSG_DEFINEWEB_STATUS_TOTAL), "-1", true));
		this.port              = new OBDtoMonTotalConditionUnit(true, temp); //필터를 DB에서 추가해야함
		
		this.bpsIn             = new OBDtoMonTotalConditionUnit(true, null);
		this.bpsOut            = new OBDtoMonTotalConditionUnit(true, null);
		
		temp = new ArrayList<OBDtoMonTotalFilterUnit>();
		temp.add(new OBDtoMonTotalFilterUnit(0, OBMessages.getMessage(OBMessages.MSG_DEFINEWEB_STATUS_TOTAL), "-1", true));
		temp.add(new OBDtoMonTotalFilterUnit(1, "~100M", "0:100000000", false));
		temp.add(new OBDtoMonTotalFilterUnit(2, "100M~500M", "100000000:500000000", false));
		temp.add(new OBDtoMonTotalFilterUnit(3, "500M~1G", "500000000:1000000000", false));
		temp.add(new OBDtoMonTotalFilterUnit(4, "1G~", "1000000000:", false));
		this.bpsTotal        = new OBDtoMonTotalConditionUnit(true, temp);
		
		temp = new ArrayList<OBDtoMonTotalFilterUnit>();
		temp.add(new OBDtoMonTotalFilterUnit(0, OBMessages.getMessage(OBMessages.MSG_DEFINEWEB_STATUS_TOTAL), "-1", true));
		temp.add(new OBDtoMonTotalFilterUnit(1, "~1K", "0:1000", false));
		temp.add(new OBDtoMonTotalFilterUnit(2, "1K~10K", "1000:10000", false));
		temp.add(new OBDtoMonTotalFilterUnit(3, "10K~100K", "10000:100000", false));
		temp.add(new OBDtoMonTotalFilterUnit(4, "100K~", "100000:", false));
		this.concurrentSession = new OBDtoMonTotalConditionUnit(true, temp);

		this.adcName           = new OBDtoMonTotalConditionUnit(true, null);
		this.adcType           = new OBDtoMonTotalConditionUnit(true, null);
		this.adcIp             = new OBDtoMonTotalConditionUnit(false, null);
		this.member            = new OBDtoMonTotalConditionUnit(false, null);
		this.group             = new OBDtoMonTotalConditionUnit(false, null);
		this.loadbalancing     = new OBDtoMonTotalConditionUnit(false, null);
		this.healthCheck       = new OBDtoMonTotalConditionUnit(false, null);
		this.persistence       = new OBDtoMonTotalConditionUnit(false, null);
		this.noticeGroup       = new OBDtoMonTotalConditionUnit(false, null);
		this.updateTime        = new OBDtoMonTotalConditionUnit(false, null);
		this.configHistory     = new OBDtoMonTotalConditionUnit(false, null);
	}

	@Override
	public String toString()
	{
		return "OBDtoMonTotalServiceCondition [status=" + status + ", name="
				+ name + ", ip=" + ip + ", port=" + port + ", bpsIn=" + bpsIn
				+ ", bpsOut=" + bpsOut + ", bpsTotal=" + bpsTotal
				+ ", concurrentSession=" + concurrentSession + ", adcName="
				+ adcName + ", adcType=" + adcType + ", adcIp=" + adcIp
				+ ", member=" + member + ", group=" + group
				+ ", loadbalancing=" + loadbalancing + ", healthCheck="
				+ healthCheck + ", persistence=" + persistence
				+ ", noticeGroup=" + noticeGroup + ", updateTime=" + updateTime
				+ ", configHistory=" + configHistory + ", searchKeyword="
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
	public OBDtoMonTotalConditionUnit getPort()
	{
		return port;
	}
	public void setPort(OBDtoMonTotalConditionUnit port)
	{
		this.port = port;
	}
	public OBDtoMonTotalConditionUnit getConcurrentSession()
	{
		return concurrentSession;
	}
	public OBDtoMonTotalConditionUnit getBpsIn()
	{
		return bpsIn;
	}
	public void setBpsIn(OBDtoMonTotalConditionUnit bpsIn)
	{
		this.bpsIn = bpsIn;
	}
	public OBDtoMonTotalConditionUnit getBpsOut()
	{
		return bpsOut;
	}
	public void setBpsOut(OBDtoMonTotalConditionUnit bpsOut)
	{
		this.bpsOut = bpsOut;
	}
	public OBDtoMonTotalConditionUnit getBpsTotal()
	{
		return bpsTotal;
	}
	public void setBpsTotal(OBDtoMonTotalConditionUnit bpsTotal)
	{
		this.bpsTotal = bpsTotal;
	}
	public void setConcurrentSession(OBDtoMonTotalConditionUnit concurrentSession)
	{
		this.concurrentSession = concurrentSession;
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
	public OBDtoMonTotalConditionUnit getMember()
	{
		return member;
	}
	public void setMember(OBDtoMonTotalConditionUnit member)
	{
		this.member = member;
	}
	public OBDtoMonTotalConditionUnit getGroup()
	{
		return group;
	}
	public void setGroup(OBDtoMonTotalConditionUnit group)
	{
		this.group = group;
	}
	public OBDtoMonTotalConditionUnit getLoadbalancing()
	{
		return loadbalancing;
	}
	public void setLoadbalancing(OBDtoMonTotalConditionUnit loadbalancing)
	{
		this.loadbalancing = loadbalancing;
	}
	public OBDtoMonTotalConditionUnit getHealthCheck()
	{
		return healthCheck;
	}
	public void setHealthCheck(OBDtoMonTotalConditionUnit healthCheck)
	{
		this.healthCheck = healthCheck;
	}
	public OBDtoMonTotalConditionUnit getPersistence()
	{
		return persistence;
	}
	public void setPersistence(OBDtoMonTotalConditionUnit persistence)
	{
		this.persistence = persistence;
	}
	public OBDtoMonTotalConditionUnit getNoticeGroup()
	{
		return noticeGroup;
	}
	public void setNoticeGroup(OBDtoMonTotalConditionUnit noticeGroup)
	{
		this.noticeGroup = noticeGroup;
	}
	public OBDtoMonTotalConditionUnit getUpdateTime()
	{
		return updateTime;
	}
	public void setUpdateTime(OBDtoMonTotalConditionUnit updateTime)
	{
		this.updateTime = updateTime;
	}
	public OBDtoMonTotalConditionUnit getConfigHistory()
	{
		return configHistory;
	}
	public void setConfigHistory(OBDtoMonTotalConditionUnit configHistory)
	{
		this.configHistory = configHistory;
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
