package kr.openbase.adcsmart.service;

import kr.openbase.adcsmart.service.dto.OBDtoADCObject;
import kr.openbase.adcsmart.service.dto.OBDtoAlarmConfig;
import kr.openbase.adcsmart.service.dto.OBDtoExtraInfo;
import kr.openbase.adcsmart.service.utility.OBException;

// 참고 : alarm syslog를 전송할 IP를 설정 : adc management에서 한다.

public interface OBAlarm
{
	/**
	 * ADC의 alarm 설정 내역을 조회한다.
	 * @param adcIndex
	 * @return
	 * @throws OBException
	 */
	public OBDtoAlarmConfig getAlarmConfiguration(OBDtoADCObject object, Integer accountIndex) throws OBException;
	
	/**
	 * ADC의 alarm 설정을 수정한다.
	 * @param adcIndex
	 * @return
	 * @throws OBException
	 */
	// #3926-3 #6: 14.07.23 sw.jung 경보설정 감사로그 발생을 위한 setAlarmConfiguration 메소드 이원화(기존 메소드는 private으로)
	public void setAlarmConfiguration(OBDtoAlarmConfig alarmConfig, OBDtoExtraInfo extraInfo) throws OBException;
	
	/**
	 * ADC의 alarm 설정을 default 값으로 초기화 한다. ADC를 등록했을 때, "초기화"버튼을 눌렀을 때 쓴다. 
	 * @param adcIndex
	 * @throws OBException
	 */
	public void initAlarmConfiguration(OBDtoADCObject object) throws OBException;
	
	/**
	 * ADC의 alarm 설정을 global이나 group으로 전환한다.
	 * @param adcIndex
	 * @throws OBException
	 */
	public OBDtoAlarmConfig changeAlarmConfigurationToGlobal(Integer adcIndex) throws OBException;
	public OBDtoAlarmConfig changeAlarmConfigurationToGroup(Integer adcIndex) throws OBException;

}
