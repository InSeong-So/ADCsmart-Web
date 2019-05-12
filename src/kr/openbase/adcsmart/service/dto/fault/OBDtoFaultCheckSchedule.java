package kr.openbase.adcsmart.service.dto.fault;

import kr.openbase.adcsmart.service.dto.OBDtoADCObject;

public class OBDtoFaultCheckSchedule
{
	public static final  int	TYPE_EVERY_DAY		=1;
	public static final  int	TYPE_EVERY_WEEK		=2;
	public static final  int	TYPE_EVERY_MONTH	=3;
	public static final  int	TYPE_EVERY_ONCE		=4;
	
    private Long    		Index;
    private String			name;
    private String 			description;
    private OBDtoADCObject 	targetObj;
    private	Integer 		accntIndex;
    private	Integer			checkInterval;
    private	Long			templateIndex;//진단 템플릿 index
    private Integer			checkItem;//진단항목.
    private	Integer			scheduleType;
    private Integer			everyMinute;//00~59
    private	Integer 		everyHour;// 1~24
    private Integer			everyDayOfMonth;// 1~
    private Integer			everyMonth;//1~12
    private Integer			everyDayOfWeek;//일:1, 월:2, .....
    
	@Override
	public String toString()
	{
		return String.format("OBDtoFaultCheckSchedule [Index=%s, name=%s, description=%s, targetObj=%s, accntIndex=%s, checkInterval=%s, templateIndex=%s, checkItem=%s, scheduleType=%s, everyMinute=%s, everyHour=%s, everyDayOfMonth=%s, everyMonth=%s, everyDayOfWeek=%s]", Index, name, description, targetObj, accntIndex, checkInterval, templateIndex, checkItem, scheduleType, everyMinute, everyHour, everyDayOfMonth, everyMonth, everyDayOfWeek);
	}
	public Integer getScheduleType()
	{
		return scheduleType;
	}
	public void setScheduleType(Integer scheduleType)
	{
		this.scheduleType = scheduleType;
	}
	public Long getIndex()
	{
		return Index;
	}
	public void setIndex(Long index)
	{
		Index = index;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description = description;
	}
	public OBDtoADCObject getTargetObj()
	{
		return targetObj;
	}
	public void setTargetObj(OBDtoADCObject targetObj)
	{
		this.targetObj = targetObj;
	}
	public Integer getAccntIndex()
	{
		return accntIndex;
	}
	public void setAccntIndex(Integer accntIndex)
	{
		this.accntIndex = accntIndex;
	}
	public Integer getCheckInterval()
	{
		return checkInterval;
	}
	public void setCheckInterval(Integer checkInterval)
	{
		this.checkInterval = checkInterval;
	}
	public Long getTemplateIndex()
	{
		return templateIndex;
	}
	public void setTemplateIndex(Long templateIndex)
	{
		this.templateIndex = templateIndex;
	}
	public Integer getCheckItem()
	{
		return checkItem;
	}
	public void setCheckItem(Integer checkItem)
	{
		this.checkItem = checkItem;
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
