package kr.openbase.adcsmart.web.controller.dashboard;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;

import kr.openbase.adcsmart.web.controller.BaseAction;
import kr.openbase.adcsmart.web.facade.dashboard.SdsDashboardFacade;
import kr.openbase.adcsmart.web.facade.dto.AdcConnectionInfoDto;
import kr.openbase.adcsmart.web.facade.dto.AdcCpuInfoDto;
import kr.openbase.adcsmart.web.facade.dto.AdcDto;
import kr.openbase.adcsmart.web.facade.dto.AdcGroupDto;
import kr.openbase.adcsmart.web.facade.dto.AdcMemoryInfoDto;
import kr.openbase.adcsmart.web.facade.dto.AdcThroughputInfoDto;
import kr.openbase.adcsmart.web.facade.dto.NodeOnAdcTreeDto;
import kr.openbase.adcsmart.web.facade.dto.SdsDashboardAdcInfoDto;
import kr.openbase.adcsmart.web.facade.dto.SdsDashboardAdcSummaryDto;
import kr.openbase.adcsmart.web.facade.dto.SdsDashboardFaultLogDto;
import kr.openbase.adcsmart.web.facade.dto.SdsDashboardStatusSummaryDto;
import kr.openbase.adcsmart.web.facade.dto.SdsDashboardVsInfoDto;
import kr.openbase.adcsmart.web.facade.dto.SdsDashboardVsSummaryCountDto;
import kr.openbase.adcsmart.web.facade.dto.SdsDashboardVsSummaryDto;
import kr.openbase.adcsmart.web.facade.dto.VsMemberConnectionDataDto;
import kr.openbase.adcsmart.web.facade.dto.VsStatusDataDto;
import kr.openbase.adcsmart.web.util.OBDefineWeb;

@Controller
@Scope(value = "prototype")
public class SdsDashboardAction extends BaseAction {
	private transient Logger log = LoggerFactory.getLogger(SdsDashboardAction.class);
	@Autowired
	private SdsDashboardFacade dashboardFacade;

	@Value("#{settings['dashboard.refreshTime']}")
	private Integer refreshTime;
	@Value("#{settings['dashboard.alertTime']}")
	private Integer alertTime;
	private String lastUpdatedTime;

	private NodeOnAdcTreeDto selectedNode;
	private AdcDto adc;
	private Integer vsType;
	private String vsIndex;
	private Integer vsPort;
	private Integer status;
	private Integer vsUnavailableStatusMinDays;
	private Integer faultMaxDays;
	private Integer hour;
	private Date startTime;
	private Date endTime;
	private String searchKey;
	private Integer orderDir; // sort 오름차순:1, 내림차순2
	private Integer orderType; // sort 타입

	private List<AdcGroupDto> adcGroups;
	private Integer adcCounts;
	private SdsDashboardVsSummaryCountDto vsSummaryCount;
	private SdsDashboardStatusSummaryDto statusSummary;
	private List<SdsDashboardAdcSummaryDto> adcSummaryList;
	private List<SdsDashboardVsSummaryDto> vsSummaryList;
	private List<SdsDashboardFaultLogDto> faultLogList;
	private SdsDashboardAdcInfoDto adcInfo;
	private SdsDashboardVsInfoDto vsInfo;
	private AdcConnectionInfoDto adcConnectionInfo;
	private AdcThroughputInfoDto adcThroughputInfo;
	private AdcCpuInfoDto adcCpuInfo;
	private AdcMemoryInfoDto adcMemoryInfo;
	private List<VsStatusDataDto> vsStatusDataList;
	private List<VsMemberConnectionDataDto> vsMemberConnectionDataList;

	private Integer alertStatus;

	public String loadHeader() throws Exception {
		try {
			Integer accountIndex = session.getAccountIndex();
			if (null != accountIndex && null != faultMaxDays) {
				statusSummary = dashboardFacade.getStatusSummary(accountIndex, vsUnavailableStatusMinDays,
						faultMaxDays);
			}
			log.debug("{}", statusSummary);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return SUCCESS;
	}

	public String loadLeftPane() throws Exception {
		try {
			log.debug("loadLeftPane");
			adcGroups = dashboardFacade.getAdcGroups(session.getAccountIndex());
			countAdcs();
			log.debug("{}", adcGroups);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

		return SUCCESS;
	}

	private void countAdcs() {
		adcCounts = 0;
		if (CollectionUtils.isEmpty(adcGroups))
			return;

		for (AdcGroupDto e : adcGroups) {
			if (!CollectionUtils.isEmpty(e.getAdcs()))
				adcCounts += e.getAdcs().size();
		}
	};

	public String loadAdcSummary() throws Exception {
		try {
			Integer accountIndex = session.getAccountIndex();
			if (null != accountIndex && null != selectedNode && !StringUtils.isEmpty(selectedNode.getNodeType())) {
				adcSummaryList = dashboardFacade.getAdcSummaryList(accountIndex, selectedNode, status, faultMaxDays,
						orderType, orderDir);
			}
			log.debug("{}", adcSummaryList);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return SUCCESS;
	}

	public String loadVsSummary() throws Exception {
		try {
			Integer accountIndex = session.getAccountIndex();
			if (null != accountIndex && null != selectedNode && !StringUtils.isEmpty(selectedNode.getNodeType())) {
				vsSummaryList = dashboardFacade.getVsSummaryList(accountIndex, selectedNode, status, faultMaxDays,
						orderType, orderDir);
				vsSummaryCount = dashboardFacade.getVsSummaryCount(accountIndex, selectedNode, status, faultMaxDays,
						orderType, orderDir);
//				vsSummaryList = dashboardFacade.getVsSummaryList(accountIndex, selectedNode, status, vsUnavailableStatusMinDays);
			}
			log.debug("{}", vsSummaryList);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return SUCCESS;
	}

	public String loadFaultLog() throws Exception {
		try {
			Integer accountIndex = session.getAccountIndex();
			if (null != accountIndex && null != selectedNode && !StringUtils.isEmpty(selectedNode.getNodeType())
					&& null != faultMaxDays) {
				faultLogList = dashboardFacade.getFaultLogList(accountIndex, selectedNode, status, faultMaxDays,
						orderType, orderDir);
			}
			log.debug("{}", faultLogList);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return SUCCESS;
	}

	public String loadAdcMonitoring() throws Exception {
		return SUCCESS;
	}

	public String loadVsMonitoring() throws Exception {
		return SUCCESS;
	}

	public String loadSelectAdcInfo() throws Exception {
		try {
			if (null != adc && null != adc.getIndex()) {
				adcInfo = dashboardFacade.getAdcInfo(adc, faultMaxDays);
			}
			log.debug("{}", adcInfo);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return SUCCESS;
	}

	public String loadSelectVsInfo() throws Exception {
		try {
			if (null != adc && null != adc.getIndex() && null != vsType && !StringUtils.isEmpty(vsIndex)) {
				vsInfo = dashboardFacade.getVsInfo(adc, vsType, vsIndex, vsPort);
			}
			log.debug("{}", vsInfo);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return SUCCESS;
	}

	public String loadAdcTrafficInfo() throws Exception {
		try {
			setSearchTimeInterval();
			if (null != adc && null != adc.getIndex()) {
				adcConnectionInfo = dashboardFacade.getConnectionInfo(adc, null, null, null, startTime, endTime);
				adcThroughputInfo = dashboardFacade.getThroughputInfo(adc, null, null, null, startTime, endTime);
			}
			log.debug("{}", adcConnectionInfo);
			log.debug("{}", adcThroughputInfo);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return SUCCESS;
	}

	public String loadVsTrafficInfo() throws Exception {
		try {
			setSearchTimeInterval();
			if (null != adc && null != adc.getIndex() && null != vsType && !StringUtils.isEmpty(vsIndex)) {
				adcConnectionInfo = dashboardFacade.getConnectionInfo(adc, vsType, vsIndex, vsPort, startTime, endTime);
				adcThroughputInfo = dashboardFacade.getThroughputInfo(adc, vsType, vsIndex, vsPort, startTime, endTime);
			}
			log.debug("{}", adcConnectionInfo);
			log.debug("{}", adcThroughputInfo);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return SUCCESS;
	}

	public String loadAdcSystemUsageInfo() throws Exception {
		try {
			setSearchTimeInterval();
			if (null != adc && null != adc.getIndex()) {
				adcCpuInfo = dashboardFacade.getAdcCpuInfo(adc, startTime, endTime);
				adcMemoryInfo = dashboardFacade.getAdcMemoryInfo(adc, startTime, endTime);
			}
			log.debug("{}", adcCpuInfo);
			log.debug("{}", adcMemoryInfo);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return SUCCESS;
	}

	public String loadVsStatusDataList() throws Exception {
		try {
			setSearchTimeInterval();
			if (null != adc && null != adc.getIndex()) {
				vsStatusDataList = dashboardFacade.getVsStatusDataList(adc, startTime, endTime);
			}
			log.debug("{}", vsStatusDataList);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return SUCCESS;
	}

	public String loadVsMemberConnectionDataList() throws Exception {
		try {
			setSearchTimeInterval();
			if (null != adc && null != adc.getIndex() && !StringUtils.isEmpty(vsIndex)) {
				vsMemberConnectionDataList = dashboardFacade.getVsMemberConnectionDataList(adc, vsIndex, vsPort);
			}
			log.debug("{}", vsMemberConnectionDataList);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return SUCCESS;
	}

	public Integer getAlertTime() {
		return alertTime;
	}

	public void setAlertTime(Integer alertTime) {
		this.alertTime = alertTime;
	}

	public Integer getRefreshTime() {
		return refreshTime;
	}

	public void setRefreshTime(Integer refreshTime) {
		this.refreshTime = refreshTime;
	}

	public String getLastUpdatedTime() {
		lastUpdatedTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		return lastUpdatedTime;
	}

	public void setLastUpdatedTime(String lastUpdatedTime) {
		this.lastUpdatedTime = lastUpdatedTime;
	}

	public NodeOnAdcTreeDto getSelectedNode() {
		return selectedNode;
	}

	public void setSelectedNode(NodeOnAdcTreeDto selectedNode) {
		this.selectedNode = selectedNode;
	}

	public AdcDto getAdc() {
		return adc;
	}

	public void setAdc(AdcDto adc) {
		this.adc = adc;
	}

	public Integer getVsType() {
		return vsType;
	}

	public void setVsType(Integer vsType) {
		this.vsType = vsType;
	}

	public String getVsIndex() {
		return vsIndex;
	}

	public void setVsIndex(String vsIndex) {
		this.vsIndex = vsIndex;
	}

	public Integer getVsPort() {
		return vsPort;
	}

	public void setVsPort(Integer vsPort) {
		this.vsPort = vsPort;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getVsUnavailableStatusMinDays() {
		return vsUnavailableStatusMinDays;
	}

	public void setVsUnavailableStatusMinDays(Integer vsUnavailableStatusMinDays) {
		this.vsUnavailableStatusMinDays = vsUnavailableStatusMinDays;
	}

	public Integer getFaultMaxDays() {
		return faultMaxDays;
	}

	public void setFaultMaxDays(Integer faultMaxDays) {
		this.faultMaxDays = faultMaxDays;
	}

	public Integer getHour() {
		return hour;
	}

	public void setHour(Integer hour) {
		this.hour = hour;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getSearchKey() {
		return searchKey;
	}

	public void setSearchKey(String searchKey) {
		this.searchKey = searchKey;
	}

	public List<AdcGroupDto> getAdcGroups() {
		return adcGroups;
	}

	public void setAdcGroups(List<AdcGroupDto> adcGroups) {
		this.adcGroups = adcGroups;
	}

	public Integer getAdcCounts() {
		return adcCounts;
	}

	public void setAdcCounts(Integer adcCounts) {
		this.adcCounts = adcCounts;
	}

	public SdsDashboardStatusSummaryDto getStatusSummary() {
		return statusSummary;
	}

	public void setStatusSummary(SdsDashboardStatusSummaryDto statusSummary) {
		this.statusSummary = statusSummary;
	}

	public List<SdsDashboardAdcSummaryDto> getAdcSummaryList() {
		return adcSummaryList;
	}

	public void setAdcSummaryList(List<SdsDashboardAdcSummaryDto> adcSummaryList) {
		this.adcSummaryList = adcSummaryList;
	}

	public List<SdsDashboardVsSummaryDto> getVsSummaryList() {
		return vsSummaryList;
	}

	public void setVsSummaryList(List<SdsDashboardVsSummaryDto> vsSummaryList) {
		this.vsSummaryList = vsSummaryList;
	}

	public List<SdsDashboardFaultLogDto> getFaultLogList() {
		return faultLogList;
	}

	public void setFaultLogList(List<SdsDashboardFaultLogDto> faultLogList) {
		this.faultLogList = faultLogList;
	}

	public SdsDashboardAdcInfoDto getAdcInfo() {
		return adcInfo;
	}

	public void setAdcInfo(SdsDashboardAdcInfoDto adcInfo) {
		this.adcInfo = adcInfo;
	}

	public SdsDashboardVsInfoDto getVsInfo() {
		return vsInfo;
	}

	public void setVsInfo(SdsDashboardVsInfoDto vsInfo) {
		this.vsInfo = vsInfo;
	}

	public AdcConnectionInfoDto getAdcConnectionInfo() {
		return adcConnectionInfo;
	}

	public void setAdcConnectionInfo(AdcConnectionInfoDto adcConnectionInfo) {
		this.adcConnectionInfo = adcConnectionInfo;
	}

	public AdcThroughputInfoDto getAdcThroughputInfo() {
		return adcThroughputInfo;
	}

	public void setAdcThroughputInfo(AdcThroughputInfoDto adcThroughputInfo) {
		this.adcThroughputInfo = adcThroughputInfo;
	}

	public AdcCpuInfoDto getAdcCpuInfo() {
		return adcCpuInfo;
	}

	public void setAdcCpuInfo(AdcCpuInfoDto adcCpuInfo) {
		this.adcCpuInfo = adcCpuInfo;
	}

	public AdcMemoryInfoDto getAdcMemoryInfo() {
		return adcMemoryInfo;
	}

	public void setAdcMemoryInfo(AdcMemoryInfoDto adcMemoryInfo) {
		this.adcMemoryInfo = adcMemoryInfo;
	}

	public List<VsStatusDataDto> getVsStatusDataList() {
		return vsStatusDataList;
	}

	public void setVsStatusDataList(List<VsStatusDataDto> vsStatusDataList) {
		this.vsStatusDataList = vsStatusDataList;
	}

	public List<VsMemberConnectionDataDto> getVsMemberConnectionDataList() {
		return vsMemberConnectionDataList;
	}

	public void setVsMemberConnectionDataList(List<VsMemberConnectionDataDto> vsMemberConnectionDataList) {
		this.vsMemberConnectionDataList = vsMemberConnectionDataList;
	}

	public Integer getAlertStatus() {
		return alertStatus;
	}

	public void setAlertStatus(Integer alertStatus) {
		this.alertStatus = alertStatus;
	}

	public String setAlertStatus() throws Exception {
		try {
			if (alertStatus == null) {
				// log.debug("처음눌렀을때");
			} else if (alertStatus == OBDefineWeb.CFG_ON) {
				// log.debug("알람체크됨");
				session.setAlertCheckboxStatus(alertStatus);
			} else if (alertStatus == OBDefineWeb.CFG_OFF) {
				// log.debug("알람체크안됨");
				session.setAlertCheckboxStatus(alertStatus);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return SUCCESS;
	}

	public String loadRefreshTime() throws Exception {
		return SUCCESS;
	}

	public String loadAlertTime() throws Exception {
		return SUCCESS;
	}

	private void setSearchTimeInterval() {
		endTime = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(endTime);
		if (null != hour) {
			calendar.add(Calendar.HOUR_OF_DAY, -hour);
		} else {
			calendar.add(Calendar.HOUR_OF_DAY, -1);
		}
		startTime = calendar.getTime();
		log.debug("startTime: " + startTime.toString() + "endTime: " + endTime.toString());
	}

	public Integer getOrderDir() {
		return orderDir;
	}

	public void setOrderDir(Integer orderDir) {
		this.orderDir = orderDir;
	}

	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}

	public SdsDashboardVsSummaryCountDto getVsSummaryCount() {
		return vsSummaryCount;
	}

	public void setVsSummaryCount(SdsDashboardVsSummaryCountDto vsSummaryCount) {
		this.vsSummaryCount = vsSummaryCount;
	}

	@Override
	public String toString() {
		return "SdsDashboardAction [dashboardFacade=" + dashboardFacade + ", refreshTime=" + refreshTime
				+ ", alertTime=" + alertTime + ", lastUpdatedTime=" + lastUpdatedTime + ", selectedNode=" + selectedNode
				+ ", adc=" + adc + ", vsType=" + vsType + ", vsIndex=" + vsIndex + ", vsPort=" + vsPort + ", status="
				+ status + ", vsUnavailableStatusMinDays=" + vsUnavailableStatusMinDays + ", faultMaxDays="
				+ faultMaxDays + ", hour=" + hour + ", startTime=" + startTime + ", endTime=" + endTime + ", searchKey="
				+ searchKey + ", orderDir=" + orderDir + ", orderType=" + orderType + ", adcGroups=" + adcGroups
				+ ", adcCounts=" + adcCounts + ", vsSummaryCount=" + vsSummaryCount + ", statusSummary=" + statusSummary
				+ ", adcSummaryList=" + adcSummaryList + ", vsSummaryList=" + vsSummaryList + ", faultLogList="
				+ faultLogList + ", adcInfo=" + adcInfo + ", vsInfo=" + vsInfo + ", adcConnectionInfo="
				+ adcConnectionInfo + ", adcThroughputInfo=" + adcThroughputInfo + ", adcCpuInfo=" + adcCpuInfo
				+ ", adcMemoryInfo=" + adcMemoryInfo + ", vsStatusDataList=" + vsStatusDataList
				+ ", vsMemberConnectionDataList=" + vsMemberConnectionDataList + ", alertStatus=" + alertStatus + "]";
	}
}