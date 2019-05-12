package kr.openbase.adcsmart.service.impl.pas.dto;

import java.io.Serializable;
import java.util.ArrayList;

public class OBDtoAdcPoolPAS implements Serializable//, Cloneable
{
	private static final long serialVersionUID = 10L;
	private String 		dbIndex;
	private String 		name;
	private Integer 	lbMethod;
	private OBDtoAdcHealthCheckPAS healthCheckInfo;
	private ArrayList<OBDtoAdcPoolMemberPAS> memberList;
	
	public OBDtoAdcPoolPAS()
	{
	}

	public OBDtoAdcPoolPAS(OBDtoAdcPoolPAS obj)
	{
		if(obj!=null)
		{
			this.dbIndex = obj.getDbIndex();
			this.name = obj.getName();
			this.lbMethod = obj.getLbMethod();
			if(obj.getHealthCheckInfo() != null)
			{
				this.healthCheckInfo = new OBDtoAdcHealthCheckPAS(obj.getHealthCheckInfo());
			}
			else
			{
				this.healthCheckInfo = null;
			}
			this.memberList = new ArrayList<OBDtoAdcPoolMemberPAS>();
			for(OBDtoAdcPoolMemberPAS obj2: obj.getMemberList())
			{
				if(obj2!=null)
				{
					OBDtoAdcPoolMemberPAS temp = new OBDtoAdcPoolMemberPAS(obj2);
					this.memberList.add(temp);
				}
			}
		}
	}

	@Override
	public String toString()
	{
		return "OBDtoAdcPoolPAS [dbIndex=" + dbIndex + ", name=" + name
				+ ", lbMethod=" + lbMethod + ", healthCheckInfo="
				+ healthCheckInfo + ", memberList=" + memberList + "]";
	}
	public OBDtoAdcHealthCheckPAS getHealthCheckInfo()
	{
		return healthCheckInfo;
	}

	public void setHealthCheckInfo(OBDtoAdcHealthCheckPAS healthCheckInfo)
	{
		this.healthCheckInfo = healthCheckInfo;
	}

	public String getDbIndex()
	{
		return dbIndex;
	}

	public void setDbIndex(String dbIndex)
	{
		this.dbIndex = dbIndex;
	}

	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public Integer getLbMethod()
	{
		return lbMethod;
	}
	public void setLbMethod(Integer lbMethod)
	{
		this.lbMethod = lbMethod;
	}
	public ArrayList<OBDtoAdcPoolMemberPAS> getMemberList()
	{
		return memberList;
	}
	public void setMemberList(ArrayList<OBDtoAdcPoolMemberPAS> memberList)
	{
		this.memberList = memberList;
	}
}
