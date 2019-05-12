package kr.openbase.adcsmart.service.dto.fault;

public class OBDtoScheduleDateTime
{
    private Integer			everyMinute;
    private	Integer 		everyHour;
    private Integer			everyDayOfMonth;
    private Integer			everyMonth;
    private Integer			everyDayOfWeek;
	@Override
	public String toString()
	{
		return String.format("OBDtoScheduleDateTime [everyMinute=%s, everyHour=%s, everyDayOfMonth=%s, everyMonth=%s, everyDayOfWeek=%s]", everyMinute, everyHour, everyDayOfMonth, everyMonth, everyDayOfWeek);
	}
	public Integer getEveryMinute()
	{
		return everyMinute;
	}
	public void setEveryMinute(Integer everyMinute)
	{
		this.everyMinute = everyMinute;
	}
	public Integer getEveryHour()
	{
		return everyHour;
	}
	public void setEveryHour(Integer everyHour)
	{
		this.everyHour = everyHour;
	}
	public Integer getEveryDayOfMonth()
	{
		return everyDayOfMonth;
	}
	public void setEveryDayOfMonth(Integer everyDayOfMonth)
	{
		this.everyDayOfMonth = everyDayOfMonth;
	}
	public Integer getEveryMonth()
	{
		return everyMonth;
	}
	public void setEveryMonth(Integer everyMonth)
	{
		this.everyMonth = everyMonth;
	}
	public Integer getEveryDayOfWeek()
	{
		return everyDayOfWeek;
	}
	public void setEveryDayOfWeek(Integer everyDayOfWeek)
	{
		this.everyDayOfWeek = everyDayOfWeek;
	}
}
