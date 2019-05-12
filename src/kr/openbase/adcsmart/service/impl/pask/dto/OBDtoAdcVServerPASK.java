package kr.openbase.adcsmart.service.impl.pask.dto;

import java.io.Serializable;
import java.sql.Timestamp;

public class OBDtoAdcVServerPASK implements Serializable//, Cloneable
{
	private static final long 	serialVersionUID = 10L;
	private String 				dbIndex;
	private Integer 			adcIndex;
	private Timestamp 			applyTime;
	private String 				name;
	private String 				vIP;
	private String 				vIPView;
	private Integer				srvProtocol	=6;
	private Integer 			srvPort;
	private String  			srvPortView;
	private Integer 			state;
	private Integer 			status;
	private OBDtoAdcPoolPASK 	pool;
	private String				subInfo;
	private boolean             configurable=true;

	public OBDtoAdcVServerPASK()
	{
	}
	public OBDtoAdcVServerPASK(OBDtoAdcVServerPASK obj)
	{
		if(obj!=null)
		{
			this.dbIndex = obj.getDbIndex();
			this.adcIndex = obj.getAdcIndex();
			this.applyTime = obj.getApplyTime();
			this.name = obj.getName();
			this.vIP = obj.getvIP();
			this.vIPView = obj.getvIPView();
			this.srvProtocol = obj.getSrvProtocol();
			this.srvPort = obj.getSrvPort();
			this.srvPortView = obj.getSrvPortView();
			this.state = obj.getState();
			this.status = obj.getStatus();
			this.configurable=obj.isConfigurable();
			this.subInfo=obj.getSubInfo();
			
			if(obj.getPool()!=null)
			{
				this.pool = new OBDtoAdcPoolPASK(obj.getPool());
			}
			else
			{
				this.pool = null;
			}
		}
	}

	@Override
	public String toString()
	{
		return "OBDtoAdcVServerPASK [dbIndex=" + dbIndex + ", adcIndex="
				+ adcIndex + ", applyTime=" + applyTime + ", name=" + name
				+ ", vIP=" + vIP + ", vIPView=" + vIPView + ", srvProtocol="
				+ srvProtocol + ", srvPort=" + srvPort + ", srvPortView="
				+ srvPortView + ", state=" + state + ", status=" + status
				+ ", pool=" + pool + ", subInfo=" + subInfo + ", configurable="
				+ configurable + "]";
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
	public String getvIPView()
	{
		return vIPView;
	}
	public void setvIPView(String vIPView)
	{
		this.vIPView = vIPView;
	}
	public Integer getSrvProtocol()
	{
		return srvProtocol;
	}
	public void setSrvProtocol(Integer srvProtocol)
	{
		this.srvProtocol = srvProtocol;
	}
	public Integer getSrvPort()
	{
		return srvPort;
	}
	public void setSrvPort(Integer srvPort)
	{
		this.srvPort = srvPort;
	}
	public String getSrvPortView()
	{
		return srvPortView;
	}
	public void setSrvPortView(String srvPortView)
	{
		this.srvPortView = srvPortView;
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
	public OBDtoAdcPoolPASK getPool()
	{
		return pool;
	}
	public void setPool(OBDtoAdcPoolPASK pool)
	{
		this.pool = pool;
	}
	public boolean isConfigurable()
	{
		return configurable;
	}
	public void setConfigurable(boolean configurable)
	{
		this.configurable = configurable;
	}
	public String getSubInfo()
	{
		return subInfo;
	}
	public void setSubInfo(String subInfo)
	{
		this.subInfo = subInfo;
	}
}
