/*
 * a 계정에 할당된 권한 자료 구조.
 */
package kr.openbase.adcsmart.service.dto;

public class OBDtoAlertConfig
{
	private Integer alertType; // 0 : 사용안함, 1: 팝업형식, 2: 상단티커형식
	private Integer alertSound; // 0 : 사용안함, 1: 사용함

	public Integer getAlertType()
	{
		return alertType;
	}
	public void setAlertType(Integer alertType)
	{
		this.alertType = alertType;
	}
	public Integer getAlertSound()
	{
		return alertSound;
	}
	public void setAlertSound(Integer alertSound)
	{
		this.alertSound = alertSound;
	}
	@Override
	public String toString()
	{
		return "OBDtoAlertConfig [alertType=" + alertType + ", alertSound=" + alertSound + "]";
	}	
}