/**
 * 장애 진단 검사중인 현황 데이터 객체.
 */
package kr.openbase.adcsmart.service.dto.fault;

public class OBDtoFaultCheckStatus
{
	private		Long		checkKey;// 관련 정보가 저장된 로그 key.
	private 	Integer 	hwCheckTotal;// hw 진단 항목 개수.
	private		Integer		hwCheckCompleteCount;// hw 진단 완료 개수.
	private		Integer		hwCheckFailCount;// 진단 완료중 실패 개수.
	private		Integer		hwCheckSuccCount;// 진단 완료중에서 성공 개수.
	private		Integer		svcCheckRate;// 서비스 진단 항목 개수.
	private		Integer 	progressRate;//진단 진행율.
	private		Integer		elapsedTime;// 진단 시작 후 경과 시간. msec 단위.
	private		String		currentCheckItem;
	private		String		cliApiMessage;// CLI/API message
	@Override
	public String toString()
	{
		return String.format("OBDtoFaultCheckStatus [checkKey=%s, hwCheckTotal=%s, hwCheckCompleteCount=%s, hwCheckFailCount=%s, hwCheckSuccCount=%s, svcCheckRate=%s, progressRate=%s, elapsedTime=%s, currentCheckItem=%s, cliApiMessage=%s]", checkKey, hwCheckTotal, hwCheckCompleteCount, hwCheckFailCount, hwCheckSuccCount, svcCheckRate, progressRate, elapsedTime, currentCheckItem, cliApiMessage);
	}

	public Integer getSvcCheckRate()
	{
		return svcCheckRate;
	}
	public Integer getHwCheckTotal()
	{
		return hwCheckTotal;
	}

	public void setHwCheckTotal(Integer hwCheckTotal)
	{
		this.hwCheckTotal = hwCheckTotal;
	}

	public Integer getHwCheckCompleteCount()
	{
		return hwCheckCompleteCount;
	}

	public void setHwCheckCompleteCount(Integer hwCheckCompleteCount)
	{
		this.hwCheckCompleteCount = hwCheckCompleteCount;
	}

	public Integer getHwCheckFailCount()
	{
		return hwCheckFailCount;
	}

	public void setHwCheckFailCount(Integer hwCheckFailCount)
	{
		this.hwCheckFailCount = hwCheckFailCount;
	}

	public Integer getHwCheckSuccCount()
	{
		return hwCheckSuccCount;
	}

	public void setHwCheckSuccCount(Integer hwCheckSuccCount)
	{
		this.hwCheckSuccCount = hwCheckSuccCount;
	}

	public void setSvcCheckRate(Integer svcCheckRate)
	{
		this.svcCheckRate = svcCheckRate;
	}
	public Long getCheckKey()
	{
		return checkKey;
	}
	public void setCheckKey(Long checkKey)
	{
		this.checkKey = checkKey;
	}
	public Integer getProgressRate()
	{
		return progressRate;
	}
	public void setProgressRate(Integer progressRate)
	{
		this.progressRate = progressRate;
	}
	public Integer getElapsedTime()
	{
		return elapsedTime;
	}
	public void setElapsedTime(Integer elapsedTime)
	{
		this.elapsedTime = elapsedTime;
	}
	public String getCurrentCheckItem()
	{
		return currentCheckItem;
	}
	public void setCurrentCheckItem(String currentCheckItem)
	{
		this.currentCheckItem = currentCheckItem;
	}
	public String getCliApiMessage()
	{
		return cliApiMessage;
	}
	public void setCliApiMessage(String cliApiMessage)
	{
		this.cliApiMessage = cliApiMessage;
	}
}
