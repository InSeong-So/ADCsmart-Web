package kr.openbase.adcsmart.service.dto.fault;

import kr.openbase.adcsmart.service.dto.OBDtoDataObj;

public class OBDtoFaultBpsConnInfo
{
	private OBDtoDataObj bpsValue;
	private OBDtoDataObj bpsInValue;
	private OBDtoDataObj bpsOutValue;
	private OBDtoDataObj bpsTotValue;
	private OBDtoDataObj ppsValue;
	private OBDtoDataObj connCurrValue;
	private OBDtoDataObj connMaxValue;
	private OBDtoDataObj connTotValue;

	private OBDtoDataObj totalBpsRawData; // raw data
	private OBDtoDataObj inBpsRawData;
	private OBDtoDataObj outBpsRawData;
	private OBDtoDataObj totalPpsRawData;
	private OBDtoDataObj totalConnCurrRawData;
	private OBDtoDataObj totalConnMaxRawData;
	private OBDtoDataObj totalConnTotRawData;	

    @Override
    public String toString()
    {
        return "OBDtoFaultBpsConnInfo [bpsValue=" + bpsValue + ", bpsInValue=" + bpsInValue + ", bpsOutValue=" + bpsOutValue + ", bpsTotValue=" + bpsTotValue + ", ppsValue=" + ppsValue + ", connCurrValue=" + connCurrValue + ", connMaxValue=" + connMaxValue + ", connTotValue=" + connTotValue + ", totalBpsRawData=" + totalBpsRawData + ", inBpsRawData=" + inBpsRawData + ", outBpsRawData=" + outBpsRawData + ", totalPpsRawData=" + totalPpsRawData + ", totalConnCurrRawData=" + totalConnCurrRawData + ", totalConnMaxRawData=" + totalConnMaxRawData + ", totalConnTotRawData=" + totalConnTotRawData + "]";
    }
    
    public OBDtoDataObj getInBpsRawData()
    {
        return inBpsRawData;
    }

    public void setInBpsRawData(OBDtoDataObj inBpsRawData)
    {
        this.inBpsRawData = inBpsRawData;
    }

    public OBDtoDataObj getOutBpsRawData()
    {
        return outBpsRawData;
    }

    public void setOutBpsRawData(OBDtoDataObj outBpsRawData)
    {
        this.outBpsRawData = outBpsRawData;
    }

    public OBDtoDataObj getBpsValue()
    {
        return bpsValue;
    }
    public void setBpsValue(OBDtoDataObj bpsValue)
    {
        this.bpsValue = bpsValue;
    }
    public OBDtoDataObj getBpsInValue()
    {
        return bpsInValue;
    }
    public void setBpsInValue(OBDtoDataObj bpsInValue)
    {
        this.bpsInValue = bpsInValue;
    }
    public OBDtoDataObj getBpsOutValue()
    {
        return bpsOutValue;
    }
    public void setBpsOutValue(OBDtoDataObj bpsOutValue)
    {
        this.bpsOutValue = bpsOutValue;
    }
    public OBDtoDataObj getBpsTotValue()
    {
        return bpsTotValue;
    }
    public void setBpsTotValue(OBDtoDataObj bpsTotValue)
    {
        this.bpsTotValue = bpsTotValue;
    }
    public OBDtoDataObj getPpsValue()
    {
        return ppsValue;
    }
    public void setPpsValue(OBDtoDataObj ppsValue)
    {
        this.ppsValue = ppsValue;
    }
    public OBDtoDataObj getConnCurrValue()
    {
        return connCurrValue;
    }
    public void setConnCurrValue(OBDtoDataObj connCurrValue)
    {
        this.connCurrValue = connCurrValue;
    }
    public OBDtoDataObj getConnMaxValue()
    {
        return connMaxValue;
    }
    public void setConnMaxValue(OBDtoDataObj connMaxValue)
    {
        this.connMaxValue = connMaxValue;
    }
    public OBDtoDataObj getConnTotValue()
    {
        return connTotValue;
    }
    public void setConnTotValue(OBDtoDataObj connTotValue)
    {
        this.connTotValue = connTotValue;
    }
    public OBDtoDataObj getTotalBpsRawData()
    {
        return totalBpsRawData;
    }
    public void setTotalBpsRawData(OBDtoDataObj totalBpsRawData)
    {
        this.totalBpsRawData = totalBpsRawData;
    }
    public OBDtoDataObj getTotalPpsRawData()
    {
        return totalPpsRawData;
    }
    public void setTotalPpsRawData(OBDtoDataObj totalPpsRawData)
    {
        this.totalPpsRawData = totalPpsRawData;
    }
    public OBDtoDataObj getTotalConnCurrRawData()
    {
        return totalConnCurrRawData;
    }
    public void setTotalConnCurrRawData(OBDtoDataObj totalConnCurrRawData)
    {
        this.totalConnCurrRawData = totalConnCurrRawData;
    }
    public OBDtoDataObj getTotalConnMaxRawData()
    {
        return totalConnMaxRawData;
    }
    public void setTotalConnMaxRawData(OBDtoDataObj totalConnMaxRawData)
    {
        this.totalConnMaxRawData = totalConnMaxRawData;
    }
    public OBDtoDataObj getTotalConnTotRawData()
    {
        return totalConnTotRawData;
    }
    public void setTotalConnTotRawData(OBDtoDataObj totalConnTotRawData)
    {
        this.totalConnTotRawData = totalConnTotRawData;
    }
}