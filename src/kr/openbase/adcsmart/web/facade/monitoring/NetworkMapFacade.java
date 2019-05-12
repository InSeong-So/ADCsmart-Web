package kr.openbase.adcsmart.web.facade.monitoring;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import kr.openbase.adcsmart.service.OBMonitoring;
import kr.openbase.adcsmart.service.dto.OBDtoNetworkMap;
import kr.openbase.adcsmart.service.dto.OBDtoNetworkMapBackup;
import kr.openbase.adcsmart.service.dto.OBDtoNetworkMapMember;
import kr.openbase.adcsmart.service.dto.OBDtoOrdering;
import kr.openbase.adcsmart.service.impl.OBMonitoringImpl;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.web.facade.dto.AdcDto;
import kr.openbase.adcsmart.web.facade.dto.NetworkMapDto;
import kr.openbase.adcsmart.web.facade.dto.NetworkMapVsBackupDto;
import kr.openbase.adcsmart.web.facade.dto.NetworkMapVsDto;
import kr.openbase.adcsmart.web.facade.dto.NetworkMapVsMemberDto;
import kr.openbase.adcsmart.web.facade.dto.ServiceMapVsDescDto;

@Component
public class NetworkMapFacade {
	private static transient Logger log = LoggerFactory.getLogger(NetworkMapFacade.class);

	private OBMonitoring monitoringSvc;

	public NetworkMapFacade() {
		monitoringSvc = new OBMonitoringImpl();
	}

	public NetworkMapDto getNetworkMap(AdcDto adc, Integer lbType, String searchKey, Integer status,
			Integer accountIndex, String accountRole, OBDtoOrdering orderOption) throws Exception {
		NetworkMapDto networkMap = getVsStatusSummary(adc, lbType, searchKey, accountIndex, accountRole, orderOption);
//		List<OBDtoNetworkMap> vsList = monitoringSvc.getNetworkMapsNew(adc.getIndex(), lbType, status, searchKey, null,
//				null, accountIndex, accountRole, orderOption);
//		log.debug("{}", vsList);
//		if (!CollectionUtils.isEmpty(vsList)) {
//			if (null == networkMap) {
//				networkMap = new NetworkMapDto();
//			}
//			for (OBDtoNetworkMap vs : vsList) {
//				networkMap.getNetworkMapVsList().add(getNetworkMapVs(vs));
//			}
//		}

		return networkMap;
	}

	private NetworkMapDto getVsStatusSummary(AdcDto adc, Integer lbType, String searchKey, Integer accountIndex,
			String accountRole, OBDtoOrdering orderOption) throws Exception {
		NetworkMapDto networkMap = null;
		List<OBDtoNetworkMap> vsList = monitoringSvc.getNetworkMapsNew(adc.getIndex(), lbType, null, searchKey, null,
				null, accountIndex, accountRole, orderOption);
		log.debug("{}", vsList);
		if (!CollectionUtils.isEmpty(vsList)) {
			networkMap = new NetworkMapDto();
			for (OBDtoNetworkMap vs : vsList) {
				networkMap.getNetworkMapVsList().add(getNetworkMapVs(vs));
				NetworkMapVsDto networkMapVs = getNetworkMapVs(vs);
				switch (networkMapVs.getStatus()) {
				case OBDefine.STATUS_DISABLE:
					networkMap.setDisabledCountVs(networkMap.getDisabledCountVs() + 1);
					break;
				case OBDefine.STATUS_AVAILABLE:
					networkMap.setAvailableCountVs(networkMap.getAvailableCountVs() + 1);
					break;
				case OBDefine.STATUS_UNAVAILABLE:
					networkMap.setUnavailableCountVs(networkMap.getUnavailableCountVs() + 1);
					break;
				default:
					networkMap.setDisabledCountVs(networkMap.getDisabledCountVs() + 1);
					break;
				}
			}
			networkMap.setTotalCountVs(vsList.size());
			networkMap.setUnavailablePercentVs(
					((float) networkMap.getUnavailableCountVs() / (float) networkMap.getTotalCountVs()) * 100);
		}

		/*
		 * NetworkMapDto networkMapDto = null; OBDtoVirtualServerSummary vsStatusSummary
		 * = monitoringSvc.getVirtualServerStatusSummary(adc.getIndex());
		 * log.debug("{}", vsStatusSummary); if (null != vsStatusSummary) { int
		 * totalCountVs = (vsStatusSummary.getVsTotal() != null) ?
		 * vsStatusSummary.getVsTotal().intValue() : 0; int availableCountVs =
		 * (vsStatusSummary.getVsAvail() != null) ?
		 * vsStatusSummary.getVsAvail().intValue() : 0; int unavailableCountVs =
		 * (vsStatusSummary.getVsUnavail() != null) ?
		 * vsStatusSummary.getVsUnavail().intValue() : 0; int disabledCountVs =
		 * totalCountVs - availableCountVs - unavailableCountVs; float
		 * unavailablePercentVs = (0 != totalCountVs) ? ((float)unavailableCountVs /
		 * (float)totalCountVs) * 100 : 0.0f;
		 * 
		 * networkMapDto = new NetworkMapDto();
		 * networkMapDto.setTotalCountVs(totalCountVs);
		 * networkMapDto.setAvailableCountVs(availableCountVs);
		 * networkMapDto.setUnavailableCountVs(unavailableCountVs);
		 * networkMapDto.setDisabledCountVs(disabledCountVs);
		 * networkMapDto.setUnavailablePercentVs(unavailablePercentVs); }
		 */
		return networkMap;
	}

	private NetworkMapVsDto getNetworkMapVs(OBDtoNetworkMap networkMap) throws Exception {
		NetworkMapVsDto networkMapVs = new NetworkMapVsDto();
		networkMapVs.setIndex(networkMap.getVsIndex());
		networkMapVs.setVsvcIndex(networkMap.getVsvcIndex());
		networkMapVs.setIpAddress(networkMap.getVsIPAddress());
		networkMapVs.setDescription(networkMap.getVsDescription());
		networkMapVs.setName(networkMap.getVsName());
		networkMapVs.setPort(networkMap.getServicePort());
		networkMapVs.setStatus(networkMap.getServiceStatus());
		networkMapVs.setBackupStatus(networkMap.getBackupStatus());
		networkMapVs.setGroupBakupType(networkMap.getGroupBakupType());
		networkMapVs.setGroupBakupId(networkMap.getGroupBakupId());
		networkMapVs.setLbClass(networkMap.getLbClass());
		networkMapVs.setLoadBalancingType(networkMap.getLoadBalancingType());
		networkMapVs.setHealthCheckType(networkMap.getHealthCheckType());
		networkMapVs.setModel(networkMap.getModel());
		networkMapVs.setHostName(networkMap.getHostName());
		networkMapVs.setAlteonId(networkMap.getAlteonId());
		networkMapVs.setGroupId(networkMap.getGroupId());
		networkMapVs.setLoadBalancingType(networkMap.getLoadBalancingType());
		networkMapVs.setRport(networkMap.getRport());

		if (null != networkMap.getMemberList()) {
			List<NetworkMapVsMemberDto> networkMapVsMemberList = new ArrayList<NetworkMapVsMemberDto>();
			for (OBDtoNetworkMapMember networkMapMember : networkMap.getMemberList()) {
				NetworkMapVsMemberDto networkMapVsMember = new NetworkMapVsMemberDto();
				networkMapVsMember.setIndex(networkMapMember.getNodeIndex());
				networkMapVsMember.setIpAddress(networkMapMember.getIpAddress());
				networkMapVsMember.setAddPort(networkMapMember.getAddPort());
				networkMapVsMember.setPort(networkMapMember.getPort());
				networkMapVsMember.setStatus(networkMapMember.getStatus());
				networkMapVsMember.setBakupType(networkMapMember.getBakupType());
				networkMapVsMember.setBakupIndex(networkMapMember.getBakupIndex());
				networkMapVsMemberList.add(networkMapVsMember);
			}
			networkMapVs.setNetworkMapVsMemberList(networkMapVsMemberList);
		}
		if (null != networkMap.getBackupList()) {
			List<NetworkMapVsBackupDto> networkMapVsBackupList = new ArrayList<NetworkMapVsBackupDto>();
			for (OBDtoNetworkMapBackup networkMapBackup : networkMap.getBackupList()) {
				NetworkMapVsBackupDto networkMapVsBackup = new NetworkMapVsBackupDto();
				networkMapVsBackup.setBakType(networkMapBackup.getBakType());
				networkMapVsBackup.setIpAddress(networkMapBackup.getIpAddress());
				networkMapVsBackup.setNodeIndex(networkMapBackup.getNodeIndex());
				networkMapVsBackup.setIdType(networkMapBackup.getIdType());
				networkMapVsBackup.setPoolIndex(networkMapBackup.getPoolIndex());
				networkMapVsBackup.setStatus(networkMapBackup.getStatus());
				networkMapVsBackupList.add(networkMapVsBackup);
			}
			networkMapVs.setNetworkMapVsBackupList(networkMapVsBackupList);
		}
		return networkMapVs;
	}

	public void saveVsDescription(ServiceMapVsDescDto descDto) throws Exception {
		monitoringSvc.saveVsDescription(descDto);
	}
}
