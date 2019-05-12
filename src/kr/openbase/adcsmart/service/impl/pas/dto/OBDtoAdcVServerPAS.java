package kr.openbase.adcsmart.service.impl.pas.dto;

import java.io.Serializable;
import java.sql.Timestamp;

public class OBDtoAdcVServerPAS implements Serializable//, Cloneable
{
	private static final long serialVersionUID = 10L;
	private String 		dbIndex;
	private Integer 	adcIndex;
	private Timestamp 	applyTime;
	private String 		name;
	private String 		vIP;
	private Integer		srvProtocol	=6;
	private Integer 	srvPort;
	private Integer 	state;
	private Integer 	status;
	private OBDtoAdcPoolPAS pool;

	public OBDtoAdcVServerPAS(OBDtoAdcVServerPAS obj)
	{
		if(obj!=null)
		{
			this.dbIndex = obj.getDbIndex();
			this.adcIndex = obj.getAdcIndex();
			this.applyTime = obj.getApplyTime();
			this.name = obj.getName();
			this.vIP = obj.getvIP();
			this.srvProtocol = obj.getSrvProtocol();
			this.srvPort = obj.getSrvPort();
			this.state = obj.getState();
			this.status = obj.getStatus();
			
			if(obj.getPool()!=null)
			{
				this.pool = new OBDtoAdcPoolPAS(obj.getPool());
			}
			else
			{
				this.pool = null;
			}
		}
	}

	public OBDtoAdcVServerPAS()
	{
	}

	@Override
	public String toString()
	{
		return "OBDtoAdcVServerPAS [dbIndex=" + dbIndex + ", adcIndex="
				+ adcIndex + ", applyTime=" + applyTime + ", name=" + name
				+ ", vIP=" + vIP + ", srvProtocol=" + srvProtocol
				+ ", srvPort=" + srvPort + ", state=" + state + ", status="
				+ status + ", pool=" + pool + "]";
	}

	public String getDbIndex()
	{
		return dbIndex;
	}
	public void setDbIndex(String dbIndex)
	{
		this.dbIndex = dbIndex;
	}
	public Integer getAdcIndex()
	{
		return adcIndex;
	}
	public void setAdcIndex(Integer adcIndex)
	{
		this.adcIndex = adcIndex;
	}
	public Timestamp getApplyTime()
	{
		return applyTime;
	}
	public void setApplyTime(Timestamp applyTime)
	{
		this.applyTime = applyTime;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getvIP()
	{
		return vIP;
	}
	public void setvIP(String vIP)
	{
		this.vIP = vIP;
	}
	public Integer getSrvPort()
	{
		return srvPort;
	}
	public void setSrvPort(Integer srvPort)
	{
		this.srvPort = srvPort;
	}
	public Integer getSrvProtocol()
	{
		return srvProtocol;
	}
	public void setSrvProtocol(Integer srvProtocol)
	{
		this.srvProtocol = srvProtocol;
	}
	public Integer getState()
	{
		return state;
	}
	public void setState(Integer state)
	{
		this.state = state;
	}
	public Integer getStatus()
	{
		return status;
	}
	public void setStatus(Integer status)
	{
		this.status = status;
	}
	public OBDtoAdcPoolPAS getPool()
	{
		return pool;
	}
	public void setPool(OBDtoAdcPoolPAS pool)
	{
		this.pool = pool;
	}
}
