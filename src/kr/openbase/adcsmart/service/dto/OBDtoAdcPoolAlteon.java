package kr.openbase.adcsmart.service.dto;

import java.io.Serializable;
import java.util.ArrayList;

public class OBDtoAdcPoolAlteon implements Serializable
{
	private static final long serialVersionUID = 10L;
	private String index;
	private String name;
	private String alteonId;
	private Integer lbMethod;
	private Integer healthCheck;
	private Integer bakType;
	private String bakID;
	private OBDtoAdcHealthCheckAlteon healthCheckV2;
	private ArrayList<OBDtoAdcPoolMemberAlteon> memberList;

	
	public OBDtoAdcPoolAlteon()
	{
	}
	public OBDtoAdcPoolAlteon(OBDtoAdcPoolAlteon obj)
	{
		if(obj!=null)
		{
			this.index = obj.getIndex();
			this.name = obj.getName();
			this.alteonId = obj.getAlteonId();
			this.lbMethod = obj.getLbMethod();
			this.healthCheck = obj.getHealthCheck();
			this.bakType = obj.getBakType();
			this.bakID = obj.getBakID();
			if(obj.getHealthCheckV2()!=null)
			{
				this.healthCheckV2 = new OBDtoAdcHealthCheckAlteon(obj.getHealthCheckV2());
			}
			else
			{
				this.healthCheckV2 = null;
			}
			this.memberList = new ArrayList<OBDtoAdcPoolMemberAlteon>();
			for(OBDtoAdcPoolMemberAlteon obj2: obj.getMemberList())
			{
				if(obj2!=null)
				{
					OBDtoAdcPoolMemberAlteon temp = new OBDtoAdcPoolMemberAlteon(obj2);
					this.memberList.add(temp);
				}
			}
		}
	}
	
	@Override
	public String toString()
	{
		return "OBDtoAdcPoolAlteon [index=" + index + ", name=" + name + ", alteonId=" + alteonId + 
				", lbMethod=" + lbMethod + ", healthCheck=" + healthCheck + ", bakType=" + bakType + 
				", bakID=" + bakID + ", healthCheckV2=" + healthCheckV2 + ", memberList=" + memberList + "]";
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
	public String getAlteonId()
	{
		return alteonId;
	}
	public void setAlteonId(String alteonId)
	{
		this.alteonId = alteonId;
	}
	public Integer getLbMethod()
	{
		return lbMethod;
	}
	public void setLbMethod(Integer lbMethod)
	{
		this.lbMethod = lbMethod;
	}
	public Integer getHealthCheck()
	{
		return healthCheck;
	}
	public void setHealthCheck(Integer healthCheck)
	{
		this.healthCheck = healthCheck;
	}
	public OBDtoAdcHealthCheckAlteon getHealthCheckV2()
	{
		return healthCheckV2;
	}
	public void setHealthCheckV2(OBDtoAdcHealthCheckAlteon healthCheckV2)
	{
		this.healthCheckV2 = healthCheckV2;
	}
	public ArrayList<OBDtoAdcPoolMemberAlteon> getMemberList()
	{
		return memberList;
	}
	public void setMemberList(ArrayList<OBDtoAdcPoolMemberAlteon> memberList)
	{
		this.memberList = memberList;
	}
	public Integer getBakType()
	{
		return bakType;
	}
	public void setBakType(Integer bakType)
	{
		this.bakType = bakType;
	}
	public void setBakID(String bakID)
	{
		this.bakID = bakID;
	}
	public String getBakID()
	{
		return bakID;
	}
}
