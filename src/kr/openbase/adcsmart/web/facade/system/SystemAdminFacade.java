/**
 * 
 */
package kr.openbase.adcsmart.web.facade.system;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import kr.openbase.adcsmart.service.OBDashboard;
import kr.openbase.adcsmart.service.dto.OBDtoAdcSystemLog;
import kr.openbase.adcsmart.service.dto.OBDtoConnection;
import kr.openbase.adcsmart.service.dto.OBDtoCpu;
import kr.openbase.adcsmart.service.dto.OBDtoMemory;
import kr.openbase.adcsmart.service.dto.OBDtoStatusSummary;
import kr.openbase.adcsmart.service.dto.OBDtoThroughput;
import kr.openbase.adcsmart.service.dto.OBDtoUsageConnection;
import kr.openbase.adcsmart.service.dto.OBDtoUsageCpu;
import kr.openbase.adcsmart.service.dto.OBDtoUsageMem;
import kr.openbase.adcsmart.service.dto.OBDtoUsageThroughput;
import kr.openbase.adcsmart.service.dto.history.OBDtoAdcConfigHistory;
import kr.openbase.adcsmart.service.impl.OBDashboardImpl;
import kr.openbase.adcsmart.web.facade.dto.AdcConnectionDataDto;
import kr.openbase.adcsmart.web.facade.dto.AdcConnectionInfoDto;
import kr.openbase.adcsmart.web.facade.dto.AdcCpuDataDto;
import kr.openbase.adcsmart.web.facade.dto.AdcCpuInfoDto;
import kr.openbase.adcsmart.web.facade.dto.AdcDto;
import kr.openbase.adcsmart.web.facade.dto.AdcMemoryDataDto;
import kr.openbase.adcsmart.web.facade.dto.AdcMemoryInfoDto;
import kr.openbase.adcsmart.web.facade.dto.AdcSystemLogDto;
import kr.openbase.adcsmart.web.facade.dto.AdcThroughputDataDto;
import kr.openbase.adcsmart.web.facade.dto.AdcThroughputInfoDto;
import kr.openbase.adcsmart.web.facade.dto.SystemStatusSummaryDto;
import kr.openbase.adcsmart.web.facade.dto.VsConfigEventDto;

/**
 * @author paul
 *
 */
@Component
public class SystemAdminFacade {

	private static transient Logger log = LoggerFactory.getLogger(SystemAdminFacade.class);
	
	private OBDashboard dashboard;
	
	public SystemAdminFacade() {
		dashboard = new OBDashboardImpl();
	}
	
	public SystemStatusSummaryDto getSystemStatusSummary(Integer accountIndex)
			throws Exception {
		SystemStatusSummaryDto systemStatusSummary = null;
		
		log.debug("accountIndex:{}", new Object[]{accountIndex});
		OBDtoStatusSummary statusSummary = dashboard.getStatusSummary(accountIndex);
		log.debug("{}", statusSummary);
		if (null != statusSummary) {
			systemStatusSummary = getSystemStatusSummary(statusSummary);
		}
		
		return systemStatusSummary;
	}
	
//	public List<VsConfigLogDto> getVsConfigLogList(Integer accountIndex, Integer logCount)
//			throws Exception {
//		List<VsConfigLogDto> vsConfigLogList = new ArrayList<VsConfigLogDto>();
//		
//		log.debug("accountIndex:{}, logCount:{}", new Object[]{accountIndex, logCount});
//		List<OBDtoVSMonitorLog> vsMonitorLogList = dashboard.getVSConfigLog(accountIndex, logCount);
//		log.debug("{}", vsMonitorLogList);
//		if (!CollectionUtils.isEmpty(vsMonitorLogList)) {
//			for (OBDtoVSMonitorLog vsMonitorLog : vsMonitorLogList) {
//				vsConfigLogList.add(getVsConfigLog(vsMonitorLog));
//			}
//		}
//		
//		return vsConfigLogList;
//	}
	
	public List<AdcSystemLogDto> getAdcSystemFaultLogList(Integer accountIndex, Integer logCount)
			throws Exception {
		List<AdcSystemLogDto> adcSystemLogList = new ArrayList<AdcSystemLogDto>();
		
		log.debug("accountIndex:{}, logCount:{}", new Object[]{accountIndex, logCount});
		List<OBDtoAdcSystemLog> svcAdcSystemLogList = dashboard.getAdcSystemFaultLog(accountIndex, logCount);
		log.debug("{}", svcAdcSystemLogList);
		if (!CollectionUtils.isEmpty(svcAdcSystemLogList)) {
			for (OBDtoAdcSystemLog svcAdcSystemLog : svcAdcSystemLogList) {
				adcSystemLogList.add(getSystemLog(svcAdcSystemLog));
			}
		}
		
		return adcSystemLogList;
	}
	
	public AdcConnectionInfoDto getAdcConnectionInfo(AdcDto adc, String vsIndex
			, Date startTime, Date endTime) throws Exception {
		AdcConnectionInfoDto adcConnectionInfoDto = null;
		
		log.debug("adcIndex:{}, vsIndex:{}, startTime:{}, endTime:{}"
				, new Object[]{adc.getIndex(), vsIndex
						, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startTime)
						, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(endTime)});
		OBDtoConnection connection = dashboard.getUsageConnections(adc.getIndex(), vsIndex, startTime, endTime, 0);
		log.debug("{}", connection);
		if (null != connection) {
			List<OBDtoUsageConnection> connectionUsageList = connection.getConnectionList();
			if (!CollectionUtils.isEmpty(connectionUsageList)) {
				adcConnectionInfoDto = new AdcConnectionInfoDto();
				for (OBDtoUsageConnection connectionUsage : connectionUsageList) {
					adcConnectionInfoDto.getAdcConnectionDataList().add(getAdcConnectionData(connectionUsage));
				}
			}
			if (!CollectionUtils.isEmpty(connection.getConfEventList())) {
				if (null == adcConnectionInfoDto) {
					adcConnectionInfoDto = new AdcConnectionInfoDto();
				}
				for (OBDtoAdcConfigHistory adcConfigHistory : connection.getConfEventList()) {
					adcConnectionInfoDto.getVsConfigEventList().add(
							VsConfigEventDto.getVsConfigEvent(adcConfigHistory));
				}
			}
		}
		
		return adcConnectionInfoDto;
	}
	
	public AdcThroughputInfoDto getAdcThroughputInfo(AdcDto adc, String vsIndex
			, Date startTime, Date endTime) throws Exception {
		AdcThroughputInfoDto adcThroughputInfo = null;
		
		log.debug("adcIndex:{}, vsIndex:{}, startTime:{}, endTime:{}"
				, new Object[]{adc.getIndex(), vsIndex
						, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startTime)
						, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(endTime)});
		OBDtoThroughput throughput = dashboard.getUsageThroughput(adc.getIndex(), vsIndex, startTime, endTime, 0);
		log.debug("{}", throughput);
		if (null != throughput) {
			List<OBDtoUsageThroughput> throughputUsageList = throughput.getThroughputList();
			if (!CollectionUtils.isEmpty(throughputUsageList)) {
				adcThroughputInfo = new AdcThroughputInfoDto();
				for (OBDtoUsageThroughput throughputUsage : throughputUsageList) {
					adcThroughputInfo.getAdcThroughputDataList().add(getAdcThroughputData(throughputUsage));
				}
			}
			if (!CollectionUtils.isEmpty(throughput.getConfEventList())) {
				if (null == adcThroughputInfo) {
					adcThroughputInfo = new AdcThroughputInfoDto();
				}
				for (OBDtoAdcConfigHistory adcConfigHistory : throughput.getConfEventList()) {
					adcThroughputInfo.getVsConfigEventList().add(
							VsConfigEventDto.getVsConfigEvent(adcConfigHistory));
				}
			}
		}
		
		return adcThroughputInfo;
	}
	
	public AdcCpuInfoDto getAdcCpuInfo(AdcDto adc, Date startTime, Date endTime)
			throws Exception {
		AdcCpuInfoDto adcCpuInfo = null;
		
		log.debug("adcIndex:{}, startTime:{}, endTime:{}"
				, new Object[]{adc.getIndex()
						, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startTime)
						, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(endTime)});
		OBDtoCpu cpu = dashboard.getUsageCpu(adc.getIndex(), startTime, endTime, 0);
		log.debug("{}", cpu);
		if (null != cpu) {
			List<OBDtoUsageCpu> cpuUsageList = cpu.getCpuList();
			if (!CollectionUtils.isEmpty(cpuUsageList)) {
				List<AdcCpuDataDto> adcCpuDataList = new ArrayList<AdcCpuDataDto>();
				for (OBDtoUsageCpu cpuUsage : cpuUsageList) {
					adcCpuDataList.add(getAdcCpuData(cpuUsage));
				}
				adcCpuInfo = new AdcCpuInfoDto();
				adcCpuInfo.setAdcCpuDataList(adcCpuDataList);
			}
		}
		
		return adcCpuInfo;
	}
	
	public AdcMemoryInfoDto getAdcMemoryInfo(AdcDto adc, Date startTime, Date endTime)
			throws Exception {
		AdcMemoryInfoDto adcMemoryInfo = null;
		
		log.debug("adcIndex:{}, startTime:{}, endTime:{}"
				, new Object[]{adc.getIndex()
						, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startTime)
						, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(endTime)});
		OBDtoMemory memory = dashboard.getUsageMem(adc.getIndex(), startTime, endTime, 0);
		log.debug("{}", memory);
		if (null != memory) {
			List<OBDtoUsageMem> memoryUsageList = memory.getMemList();
			if (!CollectionUtils.isEmpty(memoryUsageList)) {
				List<AdcMemoryDataDto> adcMemoryDataList = new ArrayList<AdcMemoryDataDto>();
				for (OBDtoUsageMem memoryUsage : memoryUsageList) {
					adcMemoryDataList.add(getAdcMemoryData(memoryUsage));
				}
				adcMemoryInfo = new AdcMemoryInfoDto();
				adcMemoryInfo.setAdcMemoryDataList(adcMemoryDataList);
			}
		}
		
		return adcMemoryInfo;
	}

	private SystemStatusSummaryDto getSystemStatusSummary(OBDtoStatusSummary statusSummary) {
		SystemStatusSummaryDto systemStatusSummary = new SystemStatusSummaryDto();
		systemStatusSummary.setAdcTotalCount(statusSummary.getAdcCount());
		systemStatusSummary.setAdcAvailableCount(statusSummary.getAdcCountAvail());
		systemStatusSummary.setAdcUnavailableCount(statusSummary.getAdcCountUnavail());
		systemStatusSummary.setVsTotalCount(statusSummary.getVsCount());
		systemStatusSummary.setVsAvailableCount(statusSummary.getVsCountAvail());
		systemStatusSummary.setVsDisableCount(statusSummary.getVsCountDisable());
		systemStatusSummary.setVsUnavailableCount(statusSummary.getVsCountUnavali());
		systemStatusSummary.setCpuUsage(statusSummary.getSysCpuUsage());
		systemStatusSummary.setMemoryUsage(statusSummary.getSysMemUsage());
		systemStatusSummary.setHddUsage(statusSummary.getSysHddUsage());
		return systemStatusSummary;
	}
	
//	private VsConfigLogDto getVsConfigLog(OBDtoVSMonitorLog vsMonitorLog) 
//	{
//		VsConfigLogDto vsConfigLog = new VsConfigLogDto();
//		if (null != vsMonitorLog.getOccurTime()) 
//		{
//			vsConfigLog.setOccurredTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(vsMonitorLog.getOccurTime()));			
//		}
//		if (vsMonitorLog.getAdcType() == OBDefine.ADC_TYPE_F5) 
//		{
//			vsConfigLog.setAdcType("F5");	
//		} 
//		else if (vsMonitorLog.getAdcType() == OBDefine.ADC_TYPE_ALTEON) 
//		{
//			vsConfigLog.setAdcType("Alteon");
//		}
//		else if (vsMonitorLog.getAdcType() == OBDefine.ADC_TYPE_ALTEON) 
//		{
//			vsConfigLog.setAdcType("Alteon");
//		}
//		
//		vsConfigLog.setAdcIndex(vsMonitorLog.getAdcIndex());
//		vsConfigLog.setAdcName(vsMonitorLog.getAdcName());
//		vsConfigLog.setIndex(vsMonitorLog.getVsIndex());
//		vsConfigLog.setName(vsMonitorLog.getVsName());
//		vsConfigLog.setIpAddress(vsMonitorLog.getVsIPAddress());
//		vsConfigLog.setStatus(vsMonitorLog.getVsStatus());
//		return vsConfigLog;
//	}
	
	private AdcSystemLogDto getSystemLog(OBDtoAdcSystemLog svcAdcSystemLog) {
		AdcSystemLogDto adcSystemLog = new AdcSystemLogDto();
		if (null != svcAdcSystemLog.getOccurTime()) {
			adcSystemLog.setOccurredTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(
					svcAdcSystemLog.getOccurTime()));			
		}
		adcSystemLog.setLogType(svcAdcSystemLog.getLogType());
		adcSystemLog.setLogLevel(svcAdcSystemLog.getLogLevel());
		adcSystemLog.setAdcIndex(svcAdcSystemLog.getAdcIndex());
		adcSystemLog.setAdcName(svcAdcSystemLog.getAdcName());
		adcSystemLog.setVsIndex(svcAdcSystemLog.getVsIndex());
		adcSystemLog.setEvent(svcAdcSystemLog.getEvent());
		adcSystemLog.setStatus(svcAdcSystemLog.getStatus());
		
		if (null != svcAdcSystemLog.getFinishTime()) {
			adcSystemLog.setFinishTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(svcAdcSystemLog.getFinishTime()));
		}
		return adcSystemLog;
	}
	
	private AdcConnectionDataDto getAdcConnectionData(OBDtoUsageConnection connectionUsage) {
		AdcConnectionDataDto adcConnectionData = new AdcConnectionDataDto();
		adcConnectionData.setOccurredTime(connectionUsage.getOccurTime());
		adcConnectionData.setConnections(connectionUsage.getConns());
		return adcConnectionData;
	}
	
	private AdcThroughputDataDto getAdcThroughputData(OBDtoUsageThroughput throughputUsage) {
		AdcThroughputDataDto adcThroughputData = new AdcThroughputDataDto();
		adcThroughputData.setOccurredTime(throughputUsage.getOccurTime());
		adcThroughputData.setBps(throughputUsage.getBps());
		adcThroughputData.setPps(throughputUsage.getPps());
		return adcThroughputData;
	}
	
	private AdcCpuDataDto getAdcCpuData(OBDtoUsageCpu cpuUsage) {
		AdcCpuDataDto adcCpuUsageData = new AdcCpuDataDto();
		adcCpuUsageData.setOccurredTime(cpuUsage.getOccurTime());
		adcCpuUsageData.setUsage(cpuUsage.getUsage());
		return adcCpuUsageData;
	}

	private AdcMemoryDataDto getAdcMemoryData(OBDtoUsageMem memoryUsage) {
		AdcMemoryDataDto adcMemoryUsageData = new AdcMemoryDataDto();
		adcMemoryUsageData.setOccurredTime(memoryUsage.getOccurTime());
		adcMemoryUsageData.setUsage(memoryUsage.getUsage());
		adcMemoryUsageData.setTotal(memoryUsage.getTotal());
		adcMemoryUsageData.setUsed(memoryUsage.getUsed());
		return adcMemoryUsageData;
	}
	
}
