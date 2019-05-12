package kr.openbase.adcsmart.web.controller.monitoring;

import java.io.File;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import kr.openbase.adcsmart.service.dto.OBDtoOrdering;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.web.controller.BaseAction;
import kr.openbase.adcsmart.web.facade.dto.AdcDto;
import kr.openbase.adcsmart.web.facade.dto.NetworkMapDto;
import kr.openbase.adcsmart.web.facade.dto.ServiceMapVsDescDto;
import kr.openbase.adcsmart.web.facade.monitoring.NetworkMapFacade;
import kr.openbase.adcsmart.web.util.CsvMaker;
import kr.openbase.adcsmart.web.util.OBMessageWeb;

@Controller
@Scope(value = "prototype")
public class NetworkMapAction extends BaseAction {
	private transient Logger log = LoggerFactory.getLogger(NetworkMapAction.class);

	@Autowired
	private NetworkMapFacade networkMapFacade;

	private AdcDto adc;
	private String searchKey;
	private Integer status;
	private Integer lbType = 0;
	private Integer adcIndex;
	private NetworkMapDto networkMap;
	private OBDtoOrdering orderOption;
//	private ServiceMapVsDescDto vsDescription;

	private String adcType = "";
	private String vsIndex = "";
	private String vsvcIndex = "";
	private String vsDescription = "";

	public OBDtoOrdering getOrderOption() {
		return orderOption;
	}

	public void setOrderOption(OBDtoOrdering orderOption) {
		this.orderOption = orderOption;
	}

	/**
	 * @return the adc
	 */
	public AdcDto getAdc() {
		return adc;
	}

	public String getAdcType() {
		return adcType;
	}

	public void setAdcType(String adcType) {
		this.adcType = adcType;
	}

	public String getVsIndex() {
		return vsIndex;
	}

	public void setVsIndex(String vsIndex) {
		this.vsIndex = vsIndex;
	}

	public String getVsvcIndex() {
		return vsvcIndex;
	}

	public void setVsvcIndex(String vsvcIndex) {
		this.vsvcIndex = vsvcIndex;
	}

	public String getVsDescription() {
		return vsDescription;
	}

	public void setVsDescription(String vsDescription) {
		this.vsDescription = vsDescription;
	}

	/**
	 * @param adc the adc to set
	 */
	public void setAdc(AdcDto adc) {
		this.adc = adc;
	}

	/**
	 * @return the searchKey
	 */
	public String getSearchKey() {
		return searchKey;
	}

	/**
	 * @param searchKey the searchKey to set
	 */
	public void setSearchKey(String searchKey) {
		this.searchKey = searchKey;
	}

	/**
	 * @return the status
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * @return the networkMap
	 */
	public NetworkMapDto getNetworkMap() {
		return networkMap;
	}

	/**
	 * @param networkMap the networkMap to set
	 */
	public void setNetworkMap(NetworkMapDto networkMap) {
		this.networkMap = networkMap;
	}

	public Integer getLbType() {
		return lbType;
	}

	public void setLbType(Integer lbType) {
		this.lbType = lbType;
	}

	public Integer getAdcIndex() {
		return adcIndex;
	}

	public void setAdcIndex(Integer adcIndex) {
		this.adcIndex = adcIndex;
	}

	public String loadNetworkMapContent() throws Exception {
		try {
			if (null != adc && null != adc.getIndex()) {
				if (StringUtils.isEmpty(searchKey)) {
					searchKey = null;
				}
				networkMap = networkMapFacade.getNetworkMap(adc, lbType, searchKey, status, session.getAccountIndex(),
						session.getAccountRole(), orderOption);
			}

			log.debug("{}", networkMap);
		} catch (Exception e) {
			throw e;
		}

		return SUCCESS;
	}

	public String checkExportServiceMapExist() throws OBException {
		try {
//            AdcDto adc = new AdcDto();
//            adc.setIndex(adcIndex);

			if (null != adc && null != adc.getIndex()) {
				if (StringUtils.isEmpty(searchKey)) {
					searchKey = null;
				}
				networkMap = networkMapFacade.getNetworkMap(adc, lbType, searchKey, status, session.getAccountIndex(),
						session.getAccountRole(), orderOption);
			}

			log.debug("{}", networkMap);

			if (networkMap != null) {
				isSuccessful = true;
			} else {
				isSuccessful = false;
				message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_EXPORT_DATA_NOT_EXIST);
			}

			networkMap = null;

		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		return SUCCESS;
	}

	public String downloadServiceMap() throws OBException {
		try {
//            AdcDto adc = new AdcDto();
//            adc.setIndex(adcIndex);

			if (null != adc && null != adc.getIndex()) {
				if (StringUtils.isEmpty(searchKey)) {
					searchKey = null;
				}
				networkMap = networkMapFacade.getNetworkMap(adc, lbType, searchKey, status, session.getAccountIndex(),
						session.getAccountRole(), orderOption);
			}

			CsvMaker csvMaker = new CsvMaker();
			csvMaker.initWithNetworkMap(networkMap.getNetworkMapVsList());
			File csv = csvMaker.write();
			if (csv != null) {
				setStrutsStream(csv);
			}

		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		return SUCCESS;
	}

	public String saveVsDescription() throws Exception {
		try {
			if (null != adcIndex && null != adcType) {
				ServiceMapVsDescDto descDto = new ServiceMapVsDescDto(adcIndex, converAdcType(adcType), vsIndex,
						vsvcIndex, vsDescription);
				networkMapFacade.saveVsDescription(descDto);
			}
		} catch (Exception e) {
			throw e;
		}

		return SUCCESS;
	}

	private Integer converAdcType(String adcType) throws Exception {
		if (adcType.equals("Alteon")) {
			return OBDefine.ADC_TYPE_ALTEON;
		} else if (adcType.equals("F5")) {
			return OBDefine.ADC_TYPE_F5;
		} else if (adcType.equals("PASK")) {
			return OBDefine.ADC_TYPE_PIOLINK_PASK;
		} else if (adcType.equals("PAS")) {
			return OBDefine.ADC_TYPE_PIOLINK_PAS;
		} else {
			throw new Exception("Not supported adcType: " + adcType);
		}
	}

}
