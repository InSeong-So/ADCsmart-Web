package kr.openbase.adcsmart.web.facade.dto.adcmon;

import kr.openbase.adcsmart.service.dto.OBDtoDataObj;
import kr.openbase.adcsmart.service.dto.dashboard.OBDtoDashboardTraffic;
import kr.openbase.adcsmart.web.util.NumberUtil;

public class OBDtoAdcmonTraffic
{
	private Integer vendor;
	private String index;
	private String nameIp;//virtual server면 ip 또는 ADC 이름.
	private Integer	port;
	private Integer status;//정상, 꺼짐, 단절.
	private OBDtoAdcmonDataObj connection;
	private OBDtoAdcmonDataObj throughput;
	private OBDtoAdcmonDataObj bpsIn;
	private OBDtoAdcmonDataObj bpsOut;
	private OBDtoAdcmonDataObj bpsTotal;
	private String adcName;
	private String adcIP;
	
	@Override
    public String toString()
    {
        return "OBDtoAdcmonTraffic [vendor=" + vendor + ", index=" + index + ", nameIp=" + nameIp + ", port=" + port + ", status=" + status + ", connection=" + connection + ", throughput=" + throughput + ", bpsIn=" + bpsIn + ", bpsOut=" + bpsOut + ", bpsTotal=" + bpsTotal + ", adcName=" + adcName + ", adcIP=" + adcIP + "]";
    }

    public OBDtoAdcmonDataObj getBpsIn()
    {
        return bpsIn;
    }

    public void setBpsIn(OBDtoAdcmonDataObj bpsIn)
    {
        this.bpsIn = bpsIn;
    }

    public OBDtoAdcmonDataObj getBpsOut()
    {
        return bpsOut;
    }

    public void setBpsOut(OBDtoAdcmonDataObj bpsOut)
    {
        this.bpsOut = bpsOut;
    }

    public OBDtoAdcmonDataObj getBpsTotal()
    {
        return bpsTotal;
    }

    public void setBpsTotal(OBDtoAdcmonDataObj bpsTotal)
    {
        this.bpsTotal = bpsTotal;
    }

    public String getAdcName()
	{
		return adcName;
	}

	public void setAdcName(String adcName)
	{
		this.adcName = adcName;
	}

	public String getAdcIP()
	{
		return adcIP;
	}

	public void setAdcIP(String adcIP)
	{
		this.adcIP = adcIP;
	}
	public Integer getVendor()
	{
		return vendor;
	}

	public void setVendor(Integer vendor)
	{
		this.vendor = vendor;
	}

	public String getIndex()
	{
		return index;
	}

	public void setIndex(String index)
	{
		this.index = index;
	}

	public String getNameIp()
	{
		return nameIp;
	}

	public void setNameIp(String nameIp)
	{
		this.nameIp = nameIp;
	}

	public Integer getPort()
	{
		return port;
	}

	public void setPort(Integer port)
	{
		this.port = port;
	}

	public Integer getStatus()
	{
		return status;
	}

	public void setStatus(Integer status)
	{
		this.status = status;
	}

	public OBDtoAdcmonDataObj getConnection()
	{
		return connection;
	}

	public void setConnection(OBDtoAdcmonDataObj connection)
	{
		this.connection = connection;
	}

	public OBDtoAdcmonDataObj getThroughput()
	{
		return throughput;
	}

	public void setThroughput(OBDtoAdcmonDataObj throughput)
	{
		this.throughput = throughput;
	}


	
	public OBDtoAdcmonTraffic toAdcmonTrafficContent(OBDtoDashboardTraffic item)
	{
		OBDtoAdcmonTraffic retVal = new OBDtoAdcmonTraffic();
		
		
		retVal.setIndex(item.getIndex());
		retVal.setNameIp(item.getNameIp());
		retVal.setPort(item.getPort());
		retVal.setStatus(item.getStatus());
		retVal.setVendor(item.getVendor());
		retVal.setAdcName(item.getAdcName());
		retVal.setAdcIP(item.getAdcIP());
		
		OBDtoDataObj connectionFrom = item.getConnection();
		OBDtoAdcmonDataObj obj = new OBDtoAdcmonDataObj();
		obj.setValue(NumberUtil.toStringWithUnit(connectionFrom.getValue(), " "));
		retVal.setConnection(obj);		
		
		// 기존 bpsTotal throughput data
		if(item.getThroughput() != null)
		{
		    OBDtoDataObj throughputFrom = item.getThroughput();    
	        OBDtoAdcmonDataObj obj2 = new OBDtoAdcmonDataObj();
	        if (throughputFrom.getValue() < 0)
	        {
	            obj2.setValue("-");
	        }
	        else
	        {
	            obj2.setValue(NumberUtil.toStringWithUnit(throughputFrom.getValue(), " "));
	        }       
	        retVal.setThroughput(obj2); 
		}
		else{}		
		
		// bpsIn data
		if(item.getBpsIn() != null)
        {
    		OBDtoDataObj bpsInFrom = item.getBpsIn();   
            OBDtoAdcmonDataObj obj3 = new OBDtoAdcmonDataObj();
            if (bpsInFrom.getValue() < 0)
            {
                obj3.setValue("-");
            }
            else
            {
                obj3.setValue(NumberUtil.toStringWithUnit(bpsInFrom.getValue(), " "));
            }       
            retVal.setBpsIn(obj3);
        }
		else{}
        
        // bpsOut data
		if(item.getBpsOut() != null)
        {
            OBDtoDataObj bpsOutFrom = item.getBpsOut(); 
            OBDtoAdcmonDataObj obj4 = new OBDtoAdcmonDataObj();
            if (bpsOutFrom.getValue() < 0)
            {
                obj4.setValue("-");
            }
            else
            {
                obj4.setValue(NumberUtil.toStringWithUnit(bpsOutFrom.getValue(), " "));
            }       
            retVal.setBpsOut(obj4);
        }else{}
        
        // bpsTotal data
		if(item.getBpsTotal() != null)
        {
            OBDtoDataObj bpsTotalForm = item.getBpsTotal(); 
            OBDtoAdcmonDataObj obj5 = new OBDtoAdcmonDataObj();
            if (bpsTotalForm.getValue() < 0)
            {
                obj5.setValue("-");
            }
            else
            {
                obj5.setValue(NumberUtil.toStringWithUnit(bpsTotalForm.getValue(), " "));
            }       
            retVal.setBpsTotal(obj5);
    		
    		
        }else{}
		return retVal;
	}
	

}
