package kr.openbase.adcsmart.service.dashboard;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import kr.openbase.adcsmart.service.OBIntegratedDashboard;
import kr.openbase.adcsmart.service.dashboard.dto.OBDtoADCList;
import kr.openbase.adcsmart.service.dashboard.dto.OBDtoIntegrated;
import kr.openbase.adcsmart.service.dto.OBDtoVSFilterInfo;
import kr.openbase.adcsmart.service.utility.OBDefine;

public class OBIntegratedDashboardImpl implements OBIntegratedDashboard {
	@Override
	public OBDtoIntegrated getIntegratedDashboard() throws Exception {
		OBDtoIntegrated result = new OBDtoIntegrated();
		ArrayList<OBDtoADCList> adcs = new ArrayList<OBDtoADCList>();
		List<OBDtoADCList> resultADCs = new ArrayList<OBDtoADCList>();
		Integer downCountTotal = 0;
		Integer adcNormalCount = 0;
		Integer vsFiltered = 0;
		Integer adcAbnormalCount = 0;
		Integer vsTotalCount = 0;
		adcs = new OBIntegratedDashboardDB().getAdcInfo();

		OBIntegratedDashboardDB integratedDashboard = new OBIntegratedDashboardDB();
		Map<String, OBDtoVSFilterInfo> vsFilterMap = integratedDashboard.getDashboardVSFilterMap();

		for (OBDtoADCList adc : adcs) {
			if (adc.getType() == OBDefine.ADC_TYPE_ALTEON) {
				adc = integratedDashboard.getNetworkMapsAlteonSlbVService(adc.getIndex(), adc.getName(), adc,
						vsFilterMap);
				vsTotalCount += new OBIntegratedDashboardDB().getSvcPerfInfoTotalCountAlteon(adc.getIndex());
				vsFiltered += adc.getVsFilteredCount();
				resultADCs.add(adc);
//				adc.setVsTotalCount(vsTotalCount);
			} else if (adc.getType() == OBDefine.ADC_TYPE_F5) {
				adc = integratedDashboard.getNetworkMapsF5(adc.getIndex(), adc.getName(), adc, vsFilterMap);
				vsTotalCount += new OBIntegratedDashboardDB().getSvcPerfInfoTotalCountExceptAlteon(adc.getIndex());
				vsFiltered += adc.getVsFilteredCount();
				resultADCs.add(adc);
//				adc.setVsTotalCount(vsTotalCount);
			} else if (adc.getType() == OBDefine.ADC_TYPE_PIOLINK_PAS
					|| adc.getType() == OBDefine.ADC_TYPE_PIOLINK_PASK) {
				adc = integratedDashboard.getNetworkMapsPASAndPASK(adc.getIndex(), adc.getName(), adc, vsFilterMap);
				vsTotalCount += new OBIntegratedDashboardDB().getSvcPerfInfoTotalCountExceptAlteon(adc.getIndex());
				vsFiltered += adc.getVsFilteredCount();
				resultADCs.add(adc);
//				adc.setVsTotalCount(vsTotalCount);
			}
		}
		for (OBDtoADCList adc : resultADCs) {
			if (adc.getStatus() == OBDefine.ADC_STATUS.REACHABLE) {
				adcNormalCount += 1;
				downCountTotal += adc.getVsDownCount() + adc.getVsPartDownCount();
			} else {
				adcAbnormalCount += 1;
			}
		}

		result.setVsAbnomal(downCountTotal);
		result.setAdcAbnomal(adcAbnormalCount);
		result.setVsFiltered(vsFiltered);
		result.setAdcNomal(adcNormalCount);
		result.setVsTotal(vsTotalCount);

		result.setAdcs(resultADCs);
		return result;
	}

	@Override
	public int getDashboardVSFilterCount(String searchKey) throws Exception {
		OBIntegratedDashboardDB OBIntegratedDashboardDB = new OBIntegratedDashboardDB();
		return OBIntegratedDashboardDB.getDashboardVSFilterCount(searchKey);
	}

	@Override
	public List<OBDtoVSFilterInfo> getDashboardVSFilterList(String searchKey, Integer fromRow, Integer toRow,
			Integer orderType, Integer orderDirw) throws Exception {
		OBIntegratedDashboardDB dashboardDB = new OBIntegratedDashboardDB();
		return dashboardDB.getAbnormalVSStatusList(searchKey, fromRow, toRow, orderType, orderDirw);
	}

	@Override
	public void addRemoveDashboardVSFilterList(List<String> indexList, List<OBDtoVSFilterInfo> vsFilterInfoList,
			int opFlag) throws Exception {
		OBIntegratedDashboardDB OBIntegratedDashboardDB = new OBIntegratedDashboardDB();
		if (opFlag == OBDefine.HIDE_FLAG) {
			OBIntegratedDashboardDB.addDashboardVSFilters(getFilterInfoListByIndexList(indexList, vsFilterInfoList));
		} else if (opFlag == OBDefine.SHOW_FLAG) {
			OBIntegratedDashboardDB.removeDashboardVSFilters(indexList);
		} else {
			new Exception("Not supported flag. opFlag: " + opFlag);
		}
	}

	private List<OBDtoVSFilterInfo> getFilterInfoListByIndexList(List<String> indexList,
			List<OBDtoVSFilterInfo> vsFilterInfoList) {
		List<OBDtoVSFilterInfo> results = new ArrayList<OBDtoVSFilterInfo>();
		Map<String, OBDtoVSFilterInfo> filterMap = vsFilterInfoList.stream()
				.collect(Collectors.toMap(OBDtoVSFilterInfo::getIndex, x -> x));
		for (String index : indexList) {
			results.add(filterMap.get(index));
		}
		return results;
	}
}
