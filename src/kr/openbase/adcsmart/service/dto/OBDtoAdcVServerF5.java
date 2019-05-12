/*
 * TMP_SLB_VIRTUAL_SERVER 테이블 처
 */
package kr.openbase.adcsmart.service.dto;

import java.sql.Timestamp;

import kr.openbase.adcsmart.service.impl.f5.DtoVirtualServer;
import kr.openbase.adcsmart.service.impl.f5.DtoVlanTunnelFilter;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBSystemLog;

import java.io.Serializable;

public class OBDtoAdcVServerF5 implements Serializable, Cloneable
{
	private static final long serialVersionUID = 10L;
	private String index;
	private Integer adcIndex;
	private Timestamp applyTime;
	private Integer status;
	private String name;
	private String vIP;
	private Integer servicePort;
	private String persistence;
	private DtoVlanTunnelFilter vlanFilter;
	private Integer useYN;
	private Integer deleteYN = 0;
	
	private OBDtoAdcPoolF5 pool;
	
	public OBDtoAdcVServerF5(OBDtoAdcVServerF5 obj)
	{
		if(obj!=null)
		{
			this.index = obj.getIndex();
			this.adcIndex = obj.getAdcIndex();
			this.applyTime = obj.getApplyTime();
			this.status = obj.getStatus();
			this.name = obj.getName();
			this.vIP = obj.getvIP();
			this.servicePort = obj.getServicePort();
			this.persistence = obj.getPersistence();
			this.useYN = obj.getUseYN();
			this.deleteYN = obj.getDeleteYN();
			this.vlanFilter = obj.getVlanFilter();
			if(obj.getPool()!=null)
			{
				this.pool = new OBDtoAdcPoolF5(obj.getPool());
			}
			else
			{
				this.pool = null;
			}
		}
	}

	public OBDtoAdcVServerF5()
	{
	}
    public OBDtoAdcVServerF5(DtoVirtualServer obj) //형을 변환하는 생성자
    {
        this.setIndex(null);
        this.setAdcIndex(obj.getAdcIndex());
        this.setApplyTime(obj.getApplyTime());
        this.setStatus(obj.getStatus());
        this.setName(obj.getName());
        this.setvIP(obj.getvIP());
        this.setServicePort(obj.getServicePort());
        this.setPersistence(obj.getPersistenceName());
        this.setVlanFilter(new DtoVlanTunnelFilter());
        this.getVlanFilter().setStatus(obj.getVlanTunnel().getStatus());
        this.getVlanFilter().setVlanName(obj.getVlanTunnel().getVlanName());
        this.setUseYN(obj.getUseYN());
        this.setDeleteYN(obj.getDeleteYN());
        this.setPool(new OBDtoAdcPoolF5());
        this.getPool().setName(obj.getPoolName());
    }
    
    public boolean configEquals(Object obj)
    {
        if(this == obj)
            return true;
        if(obj == null)
            return false;
        if(getClass() != obj.getClass())
            return false;
        OBDtoAdcVServerF5 other = (OBDtoAdcVServerF5) obj;

        if(name == null)
        {
            if(other.name != null)
                return false;
        }
        else if(!name.equals(other.name))
            return false;
        
        if(persistence == null)
        {
            if(other.persistence != null)
                return false;
        }
        else if(!persistence.equals(other.persistence))
            return false;
        
        if(pool == null)
        {
            if(other.pool != null)
                return false;
        }
        else if(!pool.configEquals(other.pool))
            return false;
        OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "POOL FIN");
        if(servicePort == null)
        {
            if(other.servicePort != null)
                return false;
        }
        else if(!servicePort.equals(other.servicePort))
            return false;
        OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "PORT FIN");
        
        if(useYN == null)
        {
            if(other.useYN != null)
                return false;
        }
        else if(!useYN.equals(other.useYN))
            return false;
        OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "USED FIN");
        if(vIP == null)
        {
            if(other.vIP != null)
                return false;
        }
        else if(!vIP.equals(other.vIP))
            return false;
        OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "VIP FIN");
        if(vlanFilter == null)
        {
            if(other.vlanFilter != null)
                return false;
        }
        else if(!vlanFilter.configEquals(other.vlanFilter))
            return false;
        OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "VLAN FIN");
        return true;
    }

    @Override
	public String toString()
	{
		return "OBDtoAdcVServerF5 [index=" + index + ", adcIndex=" + adcIndex + ", applyTime=" + applyTime + ", status=" + status + ", name=" + name + ", vIP=" + vIP + ", servicePort=" + servicePort + ", persistence=" + persistence + ", vlanFilter=" + vlanFilter + ", useYN=" + useYN + ", pool=" + pool + "]";
	}

	public String getIndex()
	{
		return index;
	}
	public void setIndex(String index)
	{
		this.index = index;
	}

	public Integer getUseYN()
	{
		return useYN;
	}

	public void setUseYN(Integer useYN)
	{
		this.useYN = useYN;
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

	public Integer getStatus()
	{
		return status;
	}

	public void setStatus(Integer status)
	{
		this.status = status;
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

	public Integer getServicePort()
	{
		return servicePort;
	}

	public void setServicePort(Integer servicePort)
	{
		this.servicePort = servicePort;
	}

	public String getPersistence()
	{
		return persistence;
	}

	public void setPersistence(String persistence)
	{
		this.persistence = persistence;
	}

	public OBDtoAdcPoolF5 getPool()
	{
		return pool;
	}

	public void setPool(OBDtoAdcPoolF5 pool)
	{
		this.pool = pool;
	}

	public DtoVlanTunnelFilter getVlanFilter()
	{
		return vlanFilter;
	}

	public void setVlanFilter(DtoVlanTunnelFilter vlanFilter)
	{
		this.vlanFilter = vlanFilter;
	}

    public Integer getDeleteYN()
    {
        return deleteYN;
    }

    public void setDeleteYN(Integer deleteYN)
    {
        this.deleteYN = deleteYN;
    }
}
