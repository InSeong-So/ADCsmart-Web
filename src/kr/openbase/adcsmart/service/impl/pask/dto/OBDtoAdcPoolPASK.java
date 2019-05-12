package kr.openbase.adcsmart.service.impl.pask.dto;

import java.io.Serializable;
import java.util.ArrayList;

public class OBDtoAdcPoolPASK implements Serializable //, Cloneable
{
	private static final long serialVersionUID = 10L;
	private String 								dbIndex;
	private String 								name;
	private Integer 							lbMethod;
	private OBDtoAdcHealthCheckPASK 			healthCheckInfo;
	private ArrayList<OBDtoAdcPoolMemberPASK> 	memberList;
	
	public OBDtoAdcPoolPASK()
	{
	}

	public OBDtoAdcPoolPASK(OBDtoAdcPoolPASK obj)
	{
		if(obj!=null)
		{
			this.dbIndex = obj.getDbIndex();
			this.name = obj.getName();
			this.lbMethod = obj.getLbMethod();
			if(obj.getHealthCheckInfo() != null)
			{
				this.healthCheckInfo = new OBDtoAdcHealthCheckPASK(obj.getHealthCheckInfo());
			}
			else
			{
				this.healthCheckInfo = null;
			}
			this.memberList = new ArrayList<OBDtoAdcPoolMemberPASK>();
			for(OBDtoAdcPoolMemberPASK obj2: obj.getMemberList())
			{
				if(obj2!=null)
				{
					OBDtoAdcPoolMemberPASK temp = new OBDtoAdcPoolMemberPASK(obj2);
					this.memberList.add(temp);
				}
			}
		}
	}	
	
	@Override
	public String toString()
	{
		return "OBDtoAdcPoolPASK [dbIndex=" + dbIndex + ", name=" + name + ", lbMethod=" + lbMethod + ", healthCheckInfo=" + healthCheckInfo + ", memberList=" + memberList + "]";
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
	public OBDtoAdcHealthCheckPASK getHealthCheckInfo()
	{
		return healthCheckInfo;
	}
	public void setHealthCheckInfo(OBDtoAdcHealthCheckPASK healthCheckInfo)
	{
		this.healthCheckInfo = healthCheckInfo;
	}
	public ArrayList<OBDtoAdcPoolMemberPASK> getMemberList()
	{
		return memberList;
	}
	public void setMemberList(ArrayList<OBDtoAdcPoolMemberPASK> memberList)
	{
		this.memberList = memberList;
	}
}
