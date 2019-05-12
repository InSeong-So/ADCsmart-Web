package kr.openbase.adcsmart.service.dto.dashboard;

import java.util.ArrayList;

import kr.openbase.adcsmart.service.dto.OBDtoADCObject;
import kr.openbase.adcsmart.service.dto.OBDtoDataObj;

//최상단일 경우 ADC group이고, 그 안에 다시 자신과 같은 형태의 list(=adcList)를 갖고 있는데, 이것은 소속 멤버 ADC들의 데이터(connection, throughput)목록이다. 
public class OBDtoDashboardAdcSummary
{
	private OBDtoADCObject 	objectInfo;		//group 이름 OR ADC 이름
	private OBDtoDataObj	connectionInfo;	//connection
	private OBDtoDataObj	throughputInfo;	//throughput
	private ArrayList<OBDtoDashboardAdcSummary> adcList; //하위 소속 개체에 대해 같은 데이터(connection, throughput) 목록  

	@Override
	public String toString()
	{
		return "OBDtoDashboardAdcSummary [objectInfo=" + objectInfo
				+ ", connectionInfo=" + connectionInfo + ", throughputInfo="
				+ throughputInfo + ", adcList=" + adcList + "]";
	}
	public OBDtoADCObject getObjectInfo()
	{
		return objectInfo;
	}
	public void setObjectInfo(OBDtoADCObject objectInfo)
	{
		this.objectInfo = objectInfo;
	}
	public OBDtoDataObj getConnectionInfo()
	{
		return connectionInfo;
	}
	public void setConnectionInfo(OBDtoDataObj connectionInfo)
	{
		this.connectionInfo = connectionInfo;
	}
	public OBDtoDataObj getThroughputInfo()
	{
		return throughputInfo;
	}
	public void setThroughputInfo(OBDtoDataObj throughputInfo)
	{
		this.throughputInfo = throughputInfo;
	}
	public ArrayList<OBDtoDashboardAdcSummary> getAdcList()
	{
		return adcList;
	}
	public void setAdcList(ArrayList<OBDtoDashboardAdcSummary> adcList)
	{
		this.adcList = adcList;
	}
}
