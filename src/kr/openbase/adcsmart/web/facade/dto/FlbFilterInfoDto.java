package kr.openbase.adcsmart.web.facade.dto;

import java.util.ArrayList;

public class FlbFilterInfoDto
{
	private int                adcIndex;  //ADC index
	private String             dbIndex;   //ADCsmart에서 index
	private Integer            filterId;  //alteon에서 filter index
	private int                state;     //filter enable? 1:yes, 0:disable
	private String             name;      //filter name
	private String             srcIP;
	private String             srcMask;
	private String             dstIP;
	private String             dstMask;
	private String             protocol;
	private Integer            srcPortFrom;
	private Integer            dstPortFrom;
	private Integer            srcPortTo;
	private Integer            dstPortTo;
	private String             action;
	private String             group;
	private String             redirection;
	private ArrayList<Integer> physicalPortList;
	
	public int getAdcIndex()
	{
		return adcIndex;
	}

	public void setAdcIndex(int adcIndex)
	{
		this.adcIndex = adcIndex;
	}

	public String getDbIndex()
	{
		return dbIndex;
	}

	public void setDbIndex(String dbIndex)
	{
		this.dbIndex = dbIndex;
	}

	public Integer getFilterId()
	{
		return filterId;
	}

	public void setFilterId(Integer filterId)
	{
		this.filterId = filterId;
	}

	public int getState()
	{
		return state;
	}

	public void setState(int state)
	{
		this.state = state;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getSrcIP()
	{
		return srcIP;
	}

	public void setSrcIP(String srcIP)
	{
		this.srcIP = srcIP;
	}

	public String getSrcMask()
	{
		return srcMask;
	}

	public void setSrcMask(String srcMask)
	{
		this.srcMask = srcMask;
	}

	public String getDstIP()
	{
		return dstIP;
	}

	public void setDstIP(String dstIP)
	{
		this.dstIP = dstIP;
	}

	public String getDstMask()
	{
		return dstMask;
	}

	public void setDstMask(String dstMask)
	{
		this.dstMask = dstMask;
	}

	public String getProtocol()
	{
		return protocol;
	}

	public void setProtocol(String protocol)
	{
		this.protocol = protocol;
	}

	public Integer getSrcPortFrom()
	{
		return srcPortFrom;
	}

	public void setSrcPortFrom(Integer srcPortFrom)
	{
		this.srcPortFrom = srcPortFrom;
	}

	public Integer getDstPortFrom()
	{
		return dstPortFrom;
	}

	public void setDstPortFrom(Integer dstPortFrom)
	{
		this.dstPortFrom = dstPortFrom;
	}

	public Integer getSrcPortTo()
	{
		return srcPortTo;
	}

	public void setSrcPortTo(Integer srcPortTo)
	{
		this.srcPortTo = srcPortTo;
	}

	public Integer getDstPortTo()
	{
		return dstPortTo;
	}

	public void setDstPortTo(Integer dstPortTo)
	{
		this.dstPortTo = dstPortTo;
	}

	public String getAction()
	{
		return action;
	}

	public void setAction(String action)
	{
		this.action = action;
	}

	public String getGroup()
	{
		return group;
	}

	public void setGroup(String group)
	{
		this.group = group;
	}

	public String getRedirection()
	{
		return redirection;
	}

	public void setRedirection(String redirection)
	{
		this.redirection = redirection;
	}
	
	public ArrayList<Integer> getPhysicalPortList()
	{
		return physicalPortList;
	}

	public void setPhysicalPortList(ArrayList<Integer> physicalPortList)
	{
		this.physicalPortList = physicalPortList;
	}

	@Override
	public String toString()
	{
		return "FlbFilterInfoDto [adcIndex=" + adcIndex + ", dbIndex=" + dbIndex + ", filterId=" + filterId + ", state=" + state + ", name=" + name + ", srcIP=" + srcIP + ", srcMask=" + srcMask + ", dstIP=" + dstIP + ", dstMask=" + dstMask + ", protocol=" + protocol + ", srcPortFrom=" + srcPortFrom + ", dstPortFrom=" + dstPortFrom + ", srcPortTo=" + srcPortTo + ", dstPortTo=" + dstPortTo + ", action=" + action + ", group=" + group + ", redirection=" + redirection + ", physicalPortList=" + physicalPortList + "]";
	}	
}