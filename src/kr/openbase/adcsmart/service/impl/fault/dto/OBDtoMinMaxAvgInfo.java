package kr.openbase.adcsmart.service.impl.fault.dto;

public class OBDtoMinMaxAvgInfo
{
	private int min=0;
	private int max=0;
	private int avg=0;
	@Override
	public String toString()
	{
		return String.format("OBDtoMinMaxAvgInfo [min=%s, max=%s, avg=%s]", min, max, avg);
	}
	public int getMin()
	{
		return min;
	}
	public void setMin(int min)
	{
		this.min = min;
	}
	public int getMax()
	{
		return max;
	}
	public void setMax(int max)
	{
		this.max = max;
	}
	public int getAvg()
	{
		return avg;
	}
	public void setAvg(int avg)
	{
		this.avg = avg;
	}
}
