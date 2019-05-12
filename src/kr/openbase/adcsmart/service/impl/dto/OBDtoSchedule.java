package kr.openbase.adcsmart.service.impl.dto;

public class OBDtoSchedule
{
	public static int TYPE_TIME_SYNC = 1;
	public static int TYPE_SLB_SAVE = 2;
	public static int TYPE_SCHBACKUP = 3;

	private int index;
	private int type;//1. 시간 동기화, 	2. SLB 설정 save
	private int isCycle;
	private int wkdy;
	private int day;
	private int hour;
	private int adcIndex;
	private String opt1;
	private String opt2;
	private String opt3;
	private String opt4;
	private String opt5;
	
	@Override
	public String toString()
	{
		return "OBDtoSchedule [index=" + index + ", type=" + type
				+ ", isCycle=" + isCycle + ", wkdy=" + wkdy + ", day=" + day
				+ ", hour=" + hour + ", adcIndex=" + adcIndex + ", opt1="
				+ opt1 + ", opt2=" + opt2 + ", opt3=" + opt3 + ", opt4=" + opt4
				+ ", opt5=" + opt5 + "]";
	}
	public void setOpt5(String opt5)
	{
		this.opt5 = opt5;
	}
	public String getOpt5()
	{
		return this.opt5;
	}

	public void setOpt4(String opt4)
	{
		this.opt4 = opt4;
	}
	public String getOpt4()
	{
		return this.opt4;
	}

	public void setOpt3(String opt3)
	{
		this.opt3 = opt3;
	}
	public String getOpt3()
	{
		return this.opt3;
	}

	public void setOpt2(String opt2)
	{
		this.opt2 = opt2;
	}
	public String getOpt2()
	{
		return this.opt2;
	}

	public void setOpt1(String opt1)
	{
		this.opt1 = opt1;
	}
	public String getOpt1()
	{
		return this.opt1;
	}

	public void setAdcIndex(int adcIndex)
	{
		this.adcIndex = adcIndex;
	}
	public int getAdcIndex()
	{
		return this.adcIndex;
	}

	public void setHour(int hour)
	{
		this.hour = hour;
	}
	public int getHour()
	{
		return this.hour;
	}

	public void setDay(int day)
	{
		this.day = day;
	}
	public int getDay()
	{
		return this.day;
	}

	public void setWkdy(int wkdy)
	{
		this.wkdy = wkdy;
	}
	public int getWkdy()
	{
		return this.wkdy;
	}

	public void setIsCycle(int isCycle)
	{
		this.isCycle = isCycle;
	}
	public int getIsCycle()
	{
		return this.isCycle;
	}

	public void setType(int i)
	{
		this.type = i;
	}
	
	public int getType()
	{
		return this.type;
	}

	public void setIndex(int index)
	{
		this.index = index;
	}
	public int getIndex()
	{
		return this.index;
	}
}
