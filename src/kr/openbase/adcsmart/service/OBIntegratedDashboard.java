package kr.openbase.adcsmart.service;

import java.util.List;

import kr.openbase.adcsmart.service.dashboard.dto.OBDtoIntegrated;
import kr.openbase.adcsmart.service.dto.OBDtoVSFilterInfo;

public interface OBIntegratedDashboard {
	public OBDtoIntegrated getIntegratedDashboard() throws Exception;

	public void addRemoveDashboardVSFilterList(List<String> indexList, List<OBDtoVSFilterInfo> vsFilterInfoList,
			int opFlag) throws Exception;

	public List<OBDtoVSFilterInfo> getDashboardVSFilterList(String searchKey, Integer fromRow, Integer toRow,
			Integer orderType, Integer orderDirw) throws Exception;

	public int getDashboardVSFilterCount(String searchKey) throws Exception;
}
