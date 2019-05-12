package kr.openbase.adcsmart.web.facade.dto.adcmon;

import java.util.ArrayList;

import kr.openbase.adcsmart.service.dto.OBDtoADCObject;
import kr.openbase.adcsmart.service.dto.OBDtoDataObj;
import kr.openbase.adcsmart.service.dto.dashboard.OBDtoDashboardAdcSummary;
import kr.openbase.adcsmart.web.util.NumberUtil;

public class OBDtoAdcmonSummary 
{
	private OBDtoADCObject						objectInfo;
	private OBDtoAdcmonDataObj					connectionInfo;
	private OBDtoAdcmonDataObj					throughputInfo;
	private ArrayList<OBDtoAdcmonSummary>		adcList;
	
	@Override
	public String toString()
	{
		return "OBDtoAdcmonSummary [objectInfo=" + objectInfo
				+ ", connectionInfo=" + connectionInfo + ", throughputInfo="
				+ throughputInfo + ", adcList=" + adcList + "]";
 	}

	public OBDtoAdcmonSummary(OBDtoDashboardAdcSummary org)
	{
		if(org != null)
		{
			this.setObjectInfo(org.getObjectInfo());
			this.setConnectionInfo(this.getConnectionInfo(org.getConnectionInfo()));
			this.setThroughputInfo(this.getThroughputInfo(org.getThroughputInfo()));
			if(org.getAdcList()!=null)
			{
				this.setAdcList(new ArrayList<OBDtoAdcmonSummary>());
				for(OBDtoDashboardAdcSummary item: org.getAdcList())
				{
					this.getAdcList().add(new OBDtoAdcmonSummary(item));
				}
			}
		}
	}
	public OBDtoAdcmonSummary() {
		// TODO Auto-generated constructor stub
	}

	public OBDtoAdcmonDataObj getConnectionInfo(OBDtoDataObj connectionInfo) 
	{
		OBDtoAdcmonDataObj objConnect = new OBDtoAdcmonDataObj();
		
		objConnect.setValue(NumberUtil.toStringWithUnit(connectionInfo.getValue(), " "));
		return objConnect;
	}
	public OBDtoAdcmonDataObj getThroughputInfo(OBDtoDataObj throughputInfo) 
	{
		OBDtoAdcmonDataObj objThroughput = new OBDtoAdcmonDataObj();
		
		objThroughput.setValue(NumberUtil.toStringWithUnit(throughputInfo.getValue(), " "));
		return objThroughput;
	}
	public OBDtoADCObject getObjectInfo() 
	{
		return objectInfo;
	}
	public void setObjectInfo(OBDtoADCObject objectInfo) 
	{
		this.objectInfo = objectInfo;
	}
	public OBDtoAdcmonDataObj getConnectionInfo() 
	{
		return connectionInfo;
	}
	public void setConnectionInfo(OBDtoAdcmonDataObj connectionInfo) 
	{
		this.connectionInfo = connectionInfo;
	}
	public OBDtoAdcmonDataObj getThroughputInfo() 
	{
		return throughputInfo;
	}
	public void setThroughputInfo(OBDtoAdcmonDataObj throughputInfo) 
	{
		this.throughputInfo = throughputInfo;
	}
	public ArrayList<OBDtoAdcmonSummary> getAdcList() 
	{
		return adcList;
	}
	public void setAdcList(ArrayList<OBDtoAdcmonSummary> adcList) 
	{
		this.adcList = adcList;
	}
	
	public OBDtoAdcmonSummary getAdcGroupSummaryList(OBDtoDashboardAdcSummary item)
	{
		OBDtoAdcmonSummary retVal = new OBDtoAdcmonSummary();
		
		retVal.setObjectInfo(item.getObjectInfo());
		retVal.setAdcList(adcList);
		
		OBDtoDataObj connectionSvc = item.getConnectionInfo();
		OBDtoAdcmonDataObj objConnect = new OBDtoAdcmonDataObj();
		
		objConnect.setValue(NumberUtil.toStringWithUnit(connectionSvc.getValue(), " "));
		retVal.setConnectionInfo(objConnect);
		
		
		OBDtoDataObj throughtputSvc = item.getThroughputInfo();
		OBDtoAdcmonDataObj objThrough = new OBDtoAdcmonDataObj();
		
		objThrough.setValue(NumberUtil.toStringWithUnit(throughtputSvc.getValue(), " "));
		retVal.setThroughputInfo(objThrough);
		
		return retVal;
	}
}
