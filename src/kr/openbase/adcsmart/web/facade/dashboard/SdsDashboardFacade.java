/**
 * 
 */
package kr.openbase.adcsmart.web.facade.dashboard;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kr.openbase.adcsmart.service.OBDashboardSds;
import kr.openbase.adcsmart.service.dto.OBDtoAdcGroup;
import kr.openbase.adcsmart.service.dto.OBDtoConnection;
import kr.openbase.adcsmart.service.dto.OBDtoCpu;
import kr.openbase.adcsmart.service.dto.OBDtoDashboardSdsAdcInfo;
import kr.openbase.adcsmart.service.dto.OBDtoDashboardSdsAdcSummary;
import kr.openbase.adcsmart.service.dto.OBDtoDashboardSdsIssueSummary;
import kr.openbase.adcsmart.service.dto.OBDtoDashboardSdsMemberConnection;
import kr.openbase.adcsmart.service.dto.OBDtoDashboardSdsStatusSummary;
import kr.openbase.adcsmart.service.dto.OBDtoDashboardSdsVservInfo;
import kr.openbase.adcsmart.service.dto.OBDtoDashboardSdsVservStatus;
import kr.openbase.adcsmart.service.dto.OBDtoDashboardSdsVservSummary;
import kr.openbase.adcsmart.service.dto.OBDtoDashboardSdsVservSummaryCount;
import kr.openbase.adcsmart.service.dto.OBDtoMemory;
import kr.openbase.adcsmart.service.dto.OBDtoThroughput;
import kr.openbase.adcsmart.service.dto.OBDtoUsageConnection;
import kr.openbase.adcsmart.service.dto.OBDtoUsageCpu;
import kr.openbase.adcsmart.service.dto.OBDtoUsageMem;
import kr.openbase.adcsmart.service.dto.OBDtoUsageThroughput;
import kr.openbase.adcsmart.service.dto.history.OBDtoAdcConfigHistory;
import kr.openbase.adcsmart.service.impl.OBDashboardSdsImpl;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.web.facade.dto.AdcConnectionDataDto;
import kr.openbase.adcsmart.web.facade.dto.AdcConnectionInfoDto;
import kr.openbase.adcsmart.web.facade.dto.AdcCpuDataDto;
import kr.openbase.adcsmart.web.facade.dto.AdcCpuInfoDto;
import kr.openbase.adcsmart.web.facade.dto.AdcDto;
import kr.openbase.adcsmart.web.facade.dto.AdcGroupDto;
import kr.openbase.adcsmart.web.facade.dto.AdcMemoryDataDto;
import kr.openbase.adcsmart.web.facade.dto.AdcMemoryInfoDto;
import kr.openbase.adcsmart.web.facade.dto.AdcThroughputDataDto;
import kr.openbase.adcsmart.web.facade.dto.AdcThroughputInfoDto;
import kr.openbase.adcsmart.web.facade.dto.NodeOnAdcTreeDto;
import kr.openbase.adcsmart.web.facade.dto.SdsDashboardAdcInfoDto;
import kr.openbase.adcsmart.web.facade.dto.SdsDashboardAdcSummaryDto;
import kr.openbase.adcsmart.web.facade.dto.SdsDashboardFaultLogDto;
import kr.openbase.adcsmart.web.facade.dto.SdsDashboardStatusSummaryDto;
import kr.openbase.adcsmart.web.facade.dto.SdsDashboardVsInfoDto;
import kr.openbase.adcsmart.web.facade.dto.SdsDashboardVsSummaryCountDto;
import kr.openbase.adcsmart.web.facade.dto.SdsDashboardVsSummaryDto;
import kr.openbase.adcsmart.web.facade.dto.VsConfigEventDto;
import kr.openbase.adcsmart.web.facade.dto.VsMemberConnectionDataDto;
import kr.openbase.adcsmart.web.facade.dto.VsStatusDataDto;
import kr.openbase.adcsmart.web.util.OBDefineWeb;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author paul
 *
 */
@Component
public class SdsDashboardFacade 
{

	private static transient Logger log = LoggerFactory.getLogger(SdsDashboardFacade.class);
	private OBDashboardSds dashboard;
	
	public SdsDashboardFacade() 
	{
		dashboard = new OBDashboardSdsImpl();
	}
	
	public List<AdcGroupDto> getAdcGroups(Integer accountIndex) throws Exception 
	{
		try
		{
			log.debug("accountIndex:{}", accountIndex);
			List<OBDtoAdcGroup> adcGroupsFromSvc = dashboard.getGroupAdcList(accountIndex);
			log.debug("{}", adcGroupsFromSvc);
			return AdcGroupDto.toAdcGroupDto(adcGroupsFromSvc);
		} 
		catch (OBException e) 
		{
			e.printStackTrace();
			throw e;
		}
	}
	
	public SdsDashboardStatusSummaryDto getStatusSummary(Integer accountIndex, Integer vsUnavailableStatusMinDays, Integer faultMaxDays) throws Exception 
	{
		SdsDashboardStatusSummaryDto statusSummary = null;
		OBDtoDashboardSdsStatusSummary obDtoStatusSummary = dashboard.getStatusSummary(accountIndex, faultMaxDays);
		if (null != obDtoStatusSummary) 
		{
			statusSummary = SdsDashboardStatusSummaryDto.getSdsDashboardStatusSummary(obDtoStatusSummary);
		}
		return statusSummary;
	}
	
	public List<SdsDashboardAdcSummaryDto> getAdcSummaryList(Integer accountIndex, NodeOnAdcTreeDto nodeOnAdcTree, Integer status, Integer faultMaxDays, Integer orderType, Integer orderDir) throws Exception 
	{
		List<SdsDashboardAdcSummaryDto> adcSummaryList = null;
		
		List<OBDtoDashboardSdsAdcSummary> obDtoDashboardSdsAdcSummaryList = null;
		if (nodeOnAdcTree.getNodeType().equals(OBDefineWeb.SELECTION_ALL)) 
		{
			obDtoDashboardSdsAdcSummaryList = dashboard.getAdcListAll(accountIndex, status, faultMaxDays, orderType, orderDir);
		} 
		else if (nodeOnAdcTree.getNodeType().equals(OBDefineWeb.SELECTION_GROUP)) 
		{
			if (!StringUtils.isEmpty(nodeOnAdcTree.getGroupIndex())) 
			{
				obDtoDashboardSdsAdcSummaryList = dashboard.getAdcListGroup(accountIndex, status, Integer.valueOf(nodeOnAdcTree.getGroupIndex()), faultMaxDays, orderType, orderDir);
			}
		} 
		else if (nodeOnAdcTree.getNodeType().equals(OBDefineWeb.SELECTION_ADC)) 
		{
			if (null != nodeOnAdcTree.getAdcIndex()) 
			{
				obDtoDashboardSdsAdcSummaryList = dashboard.getAdcListSingle(status, nodeOnAdcTree.getAdcIndex(), faultMaxDays, orderType, orderDir);				
			}
		}
		log.debug("{}", obDtoDashboardSdsAdcSummaryList);
		
		if (!CollectionUtils.isEmpty(obDtoDashboardSdsAdcSummaryList)) 
		{
			adcSummaryList = new ArrayList<SdsDashboardAdcSummaryDto>();
			for (OBDtoDashboardSdsAdcSummary obDtoDashboardSdsAdcSummary : obDtoDashboardSdsAdcSummaryList) 
			{
				adcSummaryList.add(SdsDashboardAdcSummaryDto.getSdsDashboardAdcSummary(obDtoDashboardSdsAdcSummary));
			}
		}
		return adcSummaryList;
	}
	
	public List<SdsDashboardVsSummaryDto> getVsSummaryList(Integer accountIndex
			, NodeOnAdcTreeDto nodeOnAdcTree, Integer status, Integer faultMaxDays, Integer orderType, Integer orderDir) throws Exception {
		log.debug("accountIndex:{}, nodeOnAdcTree:{}, status:{}, faultMaxDays:{}"
				, new Object[]{accountIndex, nodeOnAdcTree, status, faultMaxDays});
		List<SdsDashboardVsSummaryDto> vsSummaryList = null;
		
		List<OBDtoDashboardSdsVservSummary> obDtoDashboardSdsVservSummaryList = null;
		if (nodeOnAdcTree.getNodeType().equals(OBDefineWeb.SELECTION_ALL))
		{
			if (13 == status) 
			{
				obDtoDashboardSdsVservSummaryList = dashboard.getVservListAllAdcUnavailNDays(accountIndex, faultMaxDays, orderType, orderDir);				
			} 
			else 
			{
				obDtoDashboardSdsVservSummaryList = dashboard.getVservListAllAdc(accountIndex, status, faultMaxDays, orderType, orderDir);	
			}
		} 
		else if (nodeOnAdcTree.getNodeType().equals(OBDefineWeb.SELECTION_GROUP)) 
		{
			if (!StringUtils.isEmpty(nodeOnAdcTree.getGroupIndex())) 
			{
				if (13 == status) 
				{
					obDtoDashboardSdsVservSummaryList = dashboard.getVservListGroupAdcUnavailNDays(accountIndex, Integer.valueOf(nodeOnAdcTree.getGroupIndex()), faultMaxDays, orderType, orderDir);
				}
				else 
				{
					obDtoDashboardSdsVservSummaryList = dashboard.getVservListGroupAdc(accountIndex, status, Integer.valueOf(nodeOnAdcTree.getGroupIndex()), faultMaxDays, orderType, orderDir);					
				}
			}
		}
		else if (nodeOnAdcTree.getNodeType().equals(OBDefineWeb.SELECTION_ADC)) 
		{
			if (null != nodeOnAdcTree.getAdcIndex()) 
			{
				if (13 == status) 
				{
					obDtoDashboardSdsVservSummaryList = dashboard.getVservListSingleAdcUnavailNDays(nodeOnAdcTree.getAdcIndex(), faultMaxDays, orderType, orderDir);					
				} 
				else 
				{
					obDtoDashboardSdsVservSummaryList = dashboard.getVservListSingleAdc(status, nodeOnAdcTree.getAdcIndex(), faultMaxDays, orderType, orderDir);	
				}
			}
		}
		log.debug("{}", obDtoDashboardSdsVservSummaryList);

		if (!CollectionUtils.isEmpty(obDtoDashboardSdsVservSummaryList))
		{
			vsSummaryList = new ArrayList<SdsDashboardVsSummaryDto>();
			for (OBDtoDashboardSdsVservSummary obDtoDashboardSdsVservSummary : obDtoDashboardSdsVservSummaryList) 
			{
				vsSummaryList.add(SdsDashboardVsSummaryDto.getSdsDashboardVsSummary(obDtoDashboardSdsVservSummary));
			}
		}
		return vsSummaryList;
	}
	
	public SdsDashboardVsSummaryCountDto getVsSummaryCount(Integer accountIndex, NodeOnAdcTreeDto nodeOnAdcTree, Integer status, Integer faultMaxDays, Integer orderType, Integer orderDir) throws Exception {
		log.debug("accountIndex:{}, nodeOnAdcTree:{}, status:{}, faultMaxDays:{}"
				, new Object[]{accountIndex, nodeOnAdcTree, status, faultMaxDays});
		SdsDashboardVsSummaryCountDto vsSummaryCount = null;		
		OBDtoDashboardSdsVservSummaryCount obDtoDashboardSdsVservSummaryListCount = null;
		if (nodeOnAdcTree.getNodeType().equals(OBDefineWeb.SELECTION_ALL))
		{
			if (13 == status) 
			{
				obDtoDashboardSdsVservSummaryListCount = dashboard.getVservCountAllAdcUnavailNDays(accountIndex, faultMaxDays, orderType, orderDir);				
			} 
			else 
			{
				obDtoDashboardSdsVservSummaryListCount = dashboard.getVservCountAllAdc(accountIndex, status, faultMaxDays, orderType, orderDir);	
			}
		} 
		else if (nodeOnAdcTree.getNodeType().equals(OBDefineWeb.SELECTION_GROUP)) 
		{
			if (!StringUtils.isEmpty(nodeOnAdcTree.getGroupIndex())) 
			{
				if (13 == status) 
				{
					obDtoDashboardSdsVservSummaryListCount = dashboard.getVservCountGroupAdcUnavailNDays(accountIndex, Integer.valueOf(nodeOnAdcTree.getGroupIndex()), faultMaxDays, orderType, orderDir);
				}
				else 
				{
					obDtoDashboardSdsVservSummaryListCount = dashboard.getVservCountGroupAdc(accountIndex, status, Integer.valueOf(nodeOnAdcTree.getGroupIndex()), faultMaxDays, orderType, orderDir);					
				}
			}
		}
		else if (nodeOnAdcTree.getNodeType().equals(OBDefineWeb.SELECTION_ADC)) 
		{
			if (null != nodeOnAdcTree.getAdcIndex()) 
			{
				if (13 == status) 
				{
					obDtoDashboardSdsVservSummaryListCount = dashboard.getVservCountSingleAdcUnavailNDays(nodeOnAdcTree.getAdcIndex(), faultMaxDays, orderType, orderDir);					
				} 
				else 
				{
					obDtoDashboardSdsVservSummaryListCount = dashboard.getVservCountSingleAdc(status, nodeOnAdcTree.getAdcIndex(), faultMaxDays, orderType, orderDir);	
				}
			}
		}
		log.debug("{}", obDtoDashboardSdsVservSummaryListCount);
		
		vsSummaryCount=	SdsDashboardVsSummaryCountDto.getSdsDashboardVsSummaryCount(obDtoDashboardSdsVservSummaryListCount);
		return vsSummaryCount;

	}
	
	public List<SdsDashboardFaultLogDto> getFaultLogList(Integer accountIndex, NodeOnAdcTreeDto nodeOnAdcTree, Integer status, Integer faultMaxDays, Integer orderType, Integer orderDir) throws Exception 
	{
		log.debug("accountIndex:{}, nodeOnAdcTree:{}, status:{}, faultMaxDays:{}"
				, new Object[]{accountIndex, nodeOnAdcTree, status, faultMaxDays});
		List<SdsDashboardFaultLogDto> faultLogList = null;
		
		List<OBDtoDashboardSdsIssueSummary> obDtoDashboardSdsIssueSummaryList = null;
		if (nodeOnAdcTree.getNodeType().equals(OBDefineWeb.SELECTION_ALL)) 
		{
			obDtoDashboardSdsIssueSummaryList = dashboard.getIssueListAllAdc(accountIndex, status, faultMaxDays, orderType, orderDir);
		} 
		else if (nodeOnAdcTree.getNodeType().equals(OBDefineWeb.SELECTION_GROUP)) 
		{
			if (!StringUtils.isEmpty(nodeOnAdcTree.getGroupIndex())) 
			{
				obDtoDashboardSdsIssueSummaryList = dashboard.getIssueListGroupAdc(accountIndex, status, Integer.valueOf(nodeOnAdcTree.getGroupIndex()), faultMaxDays, orderType, orderDir);
			}
		} 
		else if (nodeOnAdcTree.getNodeType().equals(OBDefineWeb.SELECTION_ADC)) 
		{
			if (null != nodeOnAdcTree.getAdcIndex()) 
			{
				obDtoDashboardSdsIssueSummaryList = dashboard.getIssueListSingleAdc(status, nodeOnAdcTree.getAdcIndex(), faultMaxDays, orderType, orderDir);
			}
		}
		log.debug("{}", obDtoDashboardSdsIssueSummaryList);
		
		if (!CollectionUtils.isEmpty(obDtoDashboardSdsIssueSummaryList)) 
		{
			faultLogList = new ArrayList<SdsDashboardFaultLogDto>();
			for (OBDtoDashboardSdsIssueSummary obDtoDashboardSdsIssueSummary : obDtoDashboardSdsIssueSummaryList) 
			{
				faultLogList.add(SdsDashboardFaultLogDto.getSdsDashboardFaultLog(obDtoDashboardSdsIssueSummary));
			}
		}
		return faultLogList;
	}
	
	public SdsDashboardAdcInfoDto getAdcInfo(AdcDto adc, Integer faultMaxDays) throws Exception 
	{
		log.debug("adcIndex:{}", adc.getIndex());
		SdsDashboardAdcInfoDto adcInfo = null;
		
		OBDtoDashboardSdsAdcInfo obDtoDashboardSdsAdcInfo = dashboard.getAdcInfo(adc.getIndex(), faultMaxDays);
		log.debug("{}", obDtoDashboardSdsAdcInfo);
		if (null != obDtoDashboardSdsAdcInfo) 
		{
			adcInfo = SdsDashboardAdcInfoDto.getSdsDashboardAdcInfo(obDtoDashboardSdsAdcInfo);
		}
		return adcInfo;
	}
	
	public SdsDashboardVsInfoDto getVsInfo(AdcDto adc, Integer vsType, String vsIndex, Integer vsPort) throws Exception 
	{
		log.debug("adcIndex:{}, vsType:{}, vsIndex:{}, vsPort:{}", new Object[]{adc.getType(), vsType, vsIndex, vsPort});
		SdsDashboardVsInfoDto vsInfo = null;
		
		OBDtoDashboardSdsVservInfo obDtoDashboardSdsVservInfo = dashboard.getVservInfo(vsType, vsIndex, vsPort, adc.getIndex());
		log.debug("{}", obDtoDashboardSdsVservInfo);
		if (null != obDtoDashboardSdsVservInfo) 
		{
			vsInfo = SdsDashboardVsInfoDto.getSdsDashboardVsInfo(obDtoDashboardSdsVservInfo);
		}
		return vsInfo;
	}
	
	public AdcConnectionInfoDto getConnectionInfo(AdcDto adc, Integer vsType, String vsIndex, Integer vsPort, Date startTime, Date endTime) throws Exception 
	{
		log.debug("adcIndex:{}, vsType:{}, vsIndex:{}, vsPort:{}, startTime:{}, endTime:{}"
				, new Object[]{adc.getIndex()
						, vsType, vsIndex, vsPort
						, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startTime)
						, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(endTime)});
		AdcConnectionInfoDto adcConnectionInfoDto = null;
		
		OBDtoConnection obDtoConnection = null;
		if (StringUtils.isEmpty(vsIndex))
		{
			obDtoConnection = dashboard.getAdcConnections(adc.getIndex(), startTime, endTime);
		} 
		else 
		{
			obDtoConnection = dashboard.getVservConnections(vsType, adc.getIndex(), vsIndex, vsPort, startTime, endTime);
		}
		log.debug("{}", obDtoConnection);
		if (null != obDtoConnection) 
		{
			List<OBDtoUsageConnection> obDtoUsageConnectionList = obDtoConnection.getConnectionList();
			if (!CollectionUtils.isEmpty(obDtoUsageConnectionList)) 
			{
				adcConnectionInfoDto = new AdcConnectionInfoDto();
				for (OBDtoUsageConnection obDtoUsageConnection : obDtoUsageConnectionList) 
				{
					adcConnectionInfoDto.getAdcConnectionDataList().add(AdcConnectionDataDto.getAdcConnectionData(obDtoUsageConnection));
				}
			}
			if (!CollectionUtils.isEmpty(obDtoConnection.getConfEventList())) 
			{
				if (null == adcConnectionInfoDto) 
				{
					adcConnectionInfoDto = new AdcConnectionInfoDto();
				}
				for (OBDtoAdcConfigHistory obDtoAdcConfigHistory : obDtoConnection.getConfEventList()) 
				{
					adcConnectionInfoDto.getVsConfigEventList().add(VsConfigEventDto.getVsConfigEvent(obDtoAdcConfigHistory));
				}
			}
		}
		return adcConnectionInfoDto;
	}
	
	public AdcThroughputInfoDto getThroughputInfo(AdcDto adc, Integer vsType, String vsIndex, Integer vsPort, Date startTime, Date endTime) throws Exception 
	{
		log.debug("adcIndex:{}, vsType:{}, vsIndex:{}, vsPort:{}, startTime:{}, endTime:{}"
				, new Object[]{adc.getIndex()
						, vsType, vsIndex, vsPort
						, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startTime)
						, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(endTime)});
		AdcThroughputInfoDto adcThroughputInfo = null;
		
		OBDtoThroughput throughput = null;
		if (StringUtils.isEmpty(vsIndex)) 
		{
			throughput = dashboard.getAdcThroughput(adc.getIndex(), startTime, endTime);	
		} 
		else 
		{
			throughput = dashboard.getVservThroughput(vsType, adc.getIndex(), vsIndex, vsPort, startTime, endTime);
		}
		log.debug("{}", throughput);
		if (null != throughput) 
		{
			List<OBDtoUsageThroughput> obDtoUsageThroughputList = throughput.getThroughputList();
			if (!CollectionUtils.isEmpty(obDtoUsageThroughputList)) 
			{
				adcThroughputInfo = new AdcThroughputInfoDto();
				for (OBDtoUsageThroughput obDtoUsageThroughput : obDtoUsageThroughputList) 
				{
					adcThroughputInfo.getAdcThroughputDataList().add(AdcThroughputDataDto.getAdcThroughputData(obDtoUsageThroughput));
				}
			}
			if (!CollectionUtils.isEmpty(throughput.getConfEventList())) 
			{
				if (null == adcThroughputInfo) 
				{
					adcThroughputInfo = new AdcThroughputInfoDto();
				}
				for (OBDtoAdcConfigHistory obDtoAdcConfigHistory : throughput.getConfEventList()) 
				{
					adcThroughputInfo.getVsConfigEventList().add(VsConfigEventDto.getVsConfigEvent(obDtoAdcConfigHistory));
				}
			}
		}
		return adcThroughputInfo;
	}
	
	public AdcCpuInfoDto getAdcCpuInfo(AdcDto adc, Date startTime, Date endTime) throws Exception 
	{
		AdcCpuInfoDto adcCpuInfo = null;
		log.debug("adcIndex:{}, startTime:{}, endTime:{}"
				, new Object[]{adc.getIndex()
						, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startTime)
						, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(endTime)});
		
		OBDtoCpu obDtoCpu = dashboard.getAdcUsageCpu(adc.getIndex(), startTime, endTime);
		log.debug("{}", obDtoCpu);
		if (null != obDtoCpu) 
		{
			List<OBDtoUsageCpu> obDtoUsageCpuList = obDtoCpu.getCpuList();
			if (!CollectionUtils.isEmpty(obDtoUsageCpuList)) 
			{
				adcCpuInfo = new AdcCpuInfoDto();
				for (OBDtoUsageCpu obDtoUsageCpu : obDtoUsageCpuList) 
				{
					adcCpuInfo.getAdcCpuDataList().add(AdcCpuDataDto.getAdcCpuData(obDtoUsageCpu));
				}
			}
		}
		return adcCpuInfo;
	}
	
	public AdcMemoryInfoDto getAdcMemoryInfo(AdcDto adc, Date startTime, Date endTime) throws Exception {
		AdcMemoryInfoDto adcMemoryInfo = null;
		log.debug("adcIndex:{}, startTime:{}, endTime:{}"
				, new Object[]{adc.getIndex()
						, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startTime)
						, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(endTime)});
		
		OBDtoMemory obDtoMemory = dashboard.getAdcUsageMem(adc.getIndex(), startTime, endTime);
		log.debug("{}", obDtoMemory);
		if (null != obDtoMemory) 
		{
			List<OBDtoUsageMem> obDtoUsageMemList = obDtoMemory.getMemList();
			if (!CollectionUtils.isEmpty(obDtoUsageMemList)) 
			{
				adcMemoryInfo = new AdcMemoryInfoDto();
				for (OBDtoUsageMem obDtoUsageMem : obDtoUsageMemList) 
				{
					adcMemoryInfo.getAdcMemoryDataList().add(AdcMemoryDataDto.getAdcMemoryData(obDtoUsageMem));
				}
			}
		}
		return adcMemoryInfo;
	}
	
	public List<VsStatusDataDto> getVsStatusDataList(AdcDto adc, Date startTime, Date endTime) throws Exception 
	{
		log.debug("adcIndex:{}, startTime:{}, endTime:{}"
				, new Object[]{adc.getIndex()
						, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startTime)
						, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(endTime)});
		List<VsStatusDataDto> vsStatusDataList = new ArrayList<VsStatusDataDto>();
		
		List<OBDtoDashboardSdsVservStatus> obDtoDashboardSdsVservStatusList = dashboard.getVservStatus(adc.getIndex(), startTime, endTime);
		log.debug("{}", obDtoDashboardSdsVservStatusList);
		if (!CollectionUtils.isEmpty(obDtoDashboardSdsVservStatusList)) 
		{
			for (OBDtoDashboardSdsVservStatus obDtoDashboardSdsVservStatus : obDtoDashboardSdsVservStatusList) 
			{
				vsStatusDataList.add(VsStatusDataDto.getVsStatusData(obDtoDashboardSdsVservStatus));
			}
		}
		return vsStatusDataList;
	}
	
	public List<VsMemberConnectionDataDto> getVsMemberConnectionDataList(AdcDto adc, String vsIndex, Integer vsPort) throws Exception 
	{
		log.debug("vsIndex:{}, vsPort:{}", new Object[]{vsIndex, vsPort});
		List<VsMemberConnectionDataDto> vsMemberConnectionDataList = new ArrayList<VsMemberConnectionDataDto>();
		
		List<OBDtoDashboardSdsMemberConnection> obDtoDashboardSdsMemberConnectionList =
				dashboard.getVservMemberConnections(vsIndex, vsPort, adc.getIndex());
		log.debug("{}", obDtoDashboardSdsMemberConnectionList);
		if (!CollectionUtils.isEmpty(obDtoDashboardSdsMemberConnectionList)) 
		{
			for (OBDtoDashboardSdsMemberConnection obDtoDashboardSdsMemberConnection:obDtoDashboardSdsMemberConnectionList) 
			{
				vsMemberConnectionDataList.add(VsMemberConnectionDataDto.getVsMemberConnectionData(obDtoDashboardSdsMemberConnection));
			}
		}
		return vsMemberConnectionDataList;
	}
	
}