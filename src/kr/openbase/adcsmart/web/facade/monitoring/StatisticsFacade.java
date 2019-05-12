/**
 * 
 */
package kr.openbase.adcsmart.web.facade.monitoring;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import kr.openbase.adcsmart.service.OBMonitoring;
import kr.openbase.adcsmart.service.dto.OBDtoAdcPortStatus;
import kr.openbase.adcsmart.service.dto.OBDtoMultiDataObj;
import kr.openbase.adcsmart.service.impl.OBMonitoringImpl;
import kr.openbase.adcsmart.web.facade.dto.AdcDto;
import kr.openbase.adcsmart.web.facade.dto.AdcPortStatusDto;
import kr.openbase.adcsmart.web.util.NumberUtil;

/**
 * @author paul
 *
 */
@Component
public class StatisticsFacade
{

	private static transient Logger log = LoggerFactory.getLogger(StatisticsFacade.class);
	
	private OBMonitoring moni;
	
	public StatisticsFacade() 
	{
		moni = new OBMonitoringImpl();
	}
		
//	public List<AdcSystemLogDto> getAdcSystemFaultLogList(Integer accountIndex, Integer logCount)
//			throws Exception {
//		List<AdcSystemLogDto> adcSystemLogList = new ArrayList<AdcSystemLogDto>();
//		
//		List<OBDtoAdcSystemLog> svcAdcSystemLogList = dashboard.getAdcSystemFaultLog(accountIndex, logCount);
//		log.debug("{}", svcAdcSystemLogList);
//		if (!CollectionUtils.isEmpty(svcAdcSystemLogList)) {
//			for (OBDtoAdcSystemLog svcAdcSystemLog : svcAdcSystemLogList) {
//				adcSystemLogList.add(getSystemLog(svcAdcSystemLog));
//			}
//		}
//		
//		return adcSystemLogList;
//	}	
	
	public List<AdcPortStatusDto> getAdcPortStatusLog(AdcDto adc, Integer orderType, Integer orderDir) throws Exception 
	{ 	
		List<AdcPortStatusDto> adcPortStatusList = new ArrayList<AdcPortStatusDto>();
		List<OBDtoAdcPortStatus> svcAdcPortStatusList = moni.getPortStatus(adc.getIndex(), orderType, orderDir);
		
		log.debug("{}", svcAdcPortStatusList);
		
		if (!CollectionUtils.isEmpty(svcAdcPortStatusList))
		{
			for (OBDtoAdcPortStatus svcAdcPortStatus : svcAdcPortStatusList)
			{
				adcPortStatusList.add(getPortStatus(svcAdcPortStatus));
			}
		}
		return adcPortStatusList;
	}	
	
	public List<AdcPortStatusDto> getStatisticsDownloadInfo(AdcDto adc, Date startTime, Date endTime) throws Exception 
	{ 	
		List<AdcPortStatusDto> adcPortStatusList = new ArrayList<AdcPortStatusDto>();
		List<OBDtoAdcPortStatus> svcAdcPortStatusList = moni.getPortStatusForDownload(adc.getIndex(), startTime, endTime);
		
		log.debug("{}", svcAdcPortStatusList);
		
		if (!CollectionUtils.isEmpty(svcAdcPortStatusList)) 
		{
			for (OBDtoAdcPortStatus svcAdcPortStatus : svcAdcPortStatusList)
			{
				adcPortStatusList.add(getPortStatus(svcAdcPortStatus));
			}
		}
		return adcPortStatusList;
	}
	
	
//	public List<OBDtoMultiDataObj> getStatisticsGraphInfo(AdcDto adc,  ArrayList<String> interfaceNameList ,String selectedTraffic,Date startTime, Date endTime) throws Exception
//	{
//		List<OBDtoMultiDataObj> retVal = new ArrayList<OBDtoMultiDataObj>();
//
//		int selectedTrafficType = 0;
//		
//		if(selectedTraffic.equals("Bytes"))
//		{
//			selectedTrafficType = 1;
//			retVal=moni.getStatisticsGraphDataList(adc.getIndex(), interfaceNameList, selectedTrafficType, startTime, endTime);
//		}
//		else if(selectedTraffic.equals("Packets") )
//		{
//			selectedTrafficType = 2;
//			retVal=moni.getStatisticsGraphDataList(adc.getIndex(), interfaceNameList, selectedTrafficType, startTime, endTime);
//		}
//		else if(selectedTraffic.equals("Errors") )
//		{
//			selectedTrafficType = 3;
//			retVal=moni.getStatisticsGraphDataList(adc.getIndex(), interfaceNameList, selectedTrafficType, startTime, endTime);
//		}
//		else if(selectedTraffic.equals("Drops") )
//		{
//			selectedTrafficType = 4;
//			retVal=moni.getStatisticsGraphDataList(adc.getIndex(), interfaceNameList, selectedTrafficType, startTime, endTime);
//		}
//
//		return retVal;
//	}
	
	public List<OBDtoMultiDataObj> getStatistics5GraphInfo(AdcDto adc,  ArrayList<String> interfaceNameList ,String selectedTraffic,Date startTime, Date endTime) throws Exception
	{
		ArrayList<OBDtoMultiDataObj> retVal = new ArrayList<OBDtoMultiDataObj>();

		int selectedTrafficType = 0;
		
		if(selectedTraffic.equals("Bytes"))
		{
			selectedTrafficType = 1;
			retVal=moni.getStatisticsGraphDataList(adc.getIndex(), interfaceNameList, selectedTrafficType, startTime, endTime);
		}
		else if(selectedTraffic.equals("Packets") )
		{
			selectedTrafficType = 2;
			retVal=moni.getStatisticsGraphDataList(adc.getIndex(), interfaceNameList, selectedTrafficType, startTime, endTime);
		}
		else if(selectedTraffic.equals("Errors") )
		{
			selectedTrafficType = 3;
			retVal=moni.getStatisticsGraphDataList(adc.getIndex(), interfaceNameList, selectedTrafficType, startTime, endTime);
		}
		else if(selectedTraffic.equals("Drops") )
		{
			selectedTrafficType = 4;
			retVal=moni.getStatisticsGraphDataList(adc.getIndex(), interfaceNameList, selectedTrafficType, startTime, endTime);
		}
		log.debug("{}",retVal);

		return retVal;
	}
	
	
	public OBDtoMultiDataObj getGraphData(AdcDto adc, String interfaceName, String selectedTraffic, Date startTime, Date endTime)throws Exception
	{
		OBDtoMultiDataObj retVal = new OBDtoMultiDataObj();
		int selectedTrafficType = 0;
		
		if(selectedTraffic.equals("Bytes"))
		{
			selectedTrafficType = 1;
			retVal=moni.getStatisticsGraphData(adc.getIndex(), interfaceName, selectedTrafficType, startTime, endTime);
		}
		else if(selectedTraffic.equals("Packets") )
		{
			selectedTrafficType = 2;
			retVal=moni.getStatisticsGraphData(adc.getIndex(), interfaceName, selectedTrafficType, startTime, endTime);
		}
		else if(selectedTraffic.equals("Errors") )
		{
			selectedTrafficType = 3;
			retVal=moni.getStatisticsGraphData(adc.getIndex(), interfaceName, selectedTrafficType, startTime, endTime);
		}
		else if(selectedTraffic.equals("Drops") )
		{
			selectedTrafficType = 4;
			retVal=moni.getStatisticsGraphData(adc.getIndex(), interfaceName, selectedTrafficType, startTime, endTime);
		}
		return retVal;
	}
	public ArrayList<OBDtoMultiDataObj> getGraphData(AdcDto adc, ArrayList<String> interfaceNameList, String selectedTraffic, Date startTime, Date endTime)throws Exception
	{
		 ArrayList<OBDtoMultiDataObj> retVal = new  ArrayList<OBDtoMultiDataObj>();
		int selectedTrafficType = 0;
		
		if(selectedTraffic.equals("Bytes"))
		{
			selectedTrafficType = 1;
			retVal=moni.getStatisticsGraphDataList(adc.getIndex(), interfaceNameList, selectedTrafficType, startTime, endTime);
		}
		else if(selectedTraffic.equals("Packets") )
		{
			selectedTrafficType = 2;
			retVal=moni.getStatisticsGraphDataList(adc.getIndex(), interfaceNameList, selectedTrafficType, startTime, endTime);
		}
		else if(selectedTraffic.equals("Errors") )
		{
			selectedTrafficType = 3;
			retVal=moni.getStatisticsGraphDataList(adc.getIndex(), interfaceNameList, selectedTrafficType, startTime, endTime);
		}
		else if(selectedTraffic.equals("Drops") )
		{
			selectedTrafficType = 4;
			retVal=moni.getStatisticsGraphDataList(adc.getIndex(), interfaceNameList, selectedTrafficType, startTime, endTime);
		}
		return retVal;
	}
//	
//	public List<OBDtoDataObj> getGraphData(AdcDto adc, String interfaceName, String selectedTraffic, Date startTime, Date endTime)throws Exception
//	{
//		OBDtoDataObj a = new OBDtoDataObj();
//		OBDtoDataObj a1 = new OBDtoDataObj();
//		OBDtoDataObj a2 = new OBDtoDataObj();
//		
//		List<OBDtoDataObj> b= new ArrayList<OBDtoDataObj>();
//		a.setDiff(123123L);
//		a.setOccurTime(new Date());
//		a.setValue(22222L);
//		
//		a1.setDiff(123123L);
//		a1.setOccurTime(new Date());
//		a1.setValue(444444L);
//		
//		a2.setDiff(123123L);
//		a2.setOccurTime(new Date());
//		a2.setValue(33333L);
//		
//	
//		b.add(a);
//		b.add(a1);
//		b.add(a2);
//		
//		return b;
//	}
		
	private AdcPortStatusDto getPortStatus(OBDtoAdcPortStatus svcAdcPortStatus)
	{
		
		AdcPortStatusDto adcPortStatus = new AdcPortStatusDto();
		adcPortStatus.setOccurTime(svcAdcPortStatus.getOccurTime());
		adcPortStatus.setIntfName(svcAdcPortStatus.getIntfName());
		adcPortStatus.setDispName(svcAdcPortStatus.getDispName());
		adcPortStatus.setLinkStatus(svcAdcPortStatus.getLinkStatus());		
		adcPortStatus.setBytesIn(svcAdcPortStatus.getBytesIn());		
		adcPortStatus.setBytesOut(svcAdcPortStatus.getBytesOut());
		adcPortStatus.setBytesTot(svcAdcPortStatus.getBytesTot());
		adcPortStatus.setPktsIn(svcAdcPortStatus.getPktsIn());
		adcPortStatus.setPktsOut(svcAdcPortStatus.getPktsOut());
		adcPortStatus.setPktsTot(svcAdcPortStatus.getPktsTot());
		adcPortStatus.setErrorsIn(svcAdcPortStatus.getErrorsIn());
		adcPortStatus.setErrorsOut(svcAdcPortStatus.getErrorsOut());
		adcPortStatus.setErrorsTot(svcAdcPortStatus.getErrorsTot());
		adcPortStatus.setDropsIn(svcAdcPortStatus.getDropsIn());
		adcPortStatus.setDropsOut(svcAdcPortStatus.getDropsOut());
		adcPortStatus.setDropsTot(svcAdcPortStatus.getDropsTot());
		
		//단위 변환해서 화면에 보여주기
		adcPortStatus.setBbytesIn(NumberUtil.toStringWithUnitFilterNegativeNumber(svcAdcPortStatus.getBytesIn(), ""));
		adcPortStatus.setBbytesOut(NumberUtil.toStringWithUnitFilterNegativeNumber(svcAdcPortStatus.getBytesOut(), ""));
		adcPortStatus.setBbytesTot(NumberUtil.toStringWithUnitFilterNegativeNumber(svcAdcPortStatus.getBytesTot(), ""));
		adcPortStatus.setPpktsIn(NumberUtil.toStringWithUnitFilterNegativeNumber(svcAdcPortStatus.getPktsIn(), ""));
		adcPortStatus.setPpktsOut(NumberUtil.toStringWithUnitFilterNegativeNumber(svcAdcPortStatus.getPktsOut(), ""));
		adcPortStatus.setPpktsTot(NumberUtil.toStringWithUnitFilterNegativeNumber(svcAdcPortStatus.getPktsTot(), ""));
		adcPortStatus.setEerrorsIn(NumberUtil.toStringWithUnitFilterNegativeNumber(svcAdcPortStatus.getErrorsIn(), ""));
		adcPortStatus.setEerrorsOut(NumberUtil.toStringWithUnitFilterNegativeNumber(svcAdcPortStatus.getErrorsOut(), ""));
		adcPortStatus.setEerrorsTot(NumberUtil.toStringWithUnitFilterNegativeNumber(svcAdcPortStatus.getErrorsTot(), ""));
		adcPortStatus.setDdropsIn(NumberUtil.toStringWithUnitFilterNegativeNumber(svcAdcPortStatus.getDropsIn(), ""));
		adcPortStatus.setDdropsOut(NumberUtil.toStringWithUnitFilterNegativeNumber(svcAdcPortStatus.getDropsOut(), ""));
		adcPortStatus.setDdropsTot(NumberUtil.toStringWithUnitFilterNegativeNumber(svcAdcPortStatus.getDropsTot(), ""));
		
		return adcPortStatus;
	}
}
