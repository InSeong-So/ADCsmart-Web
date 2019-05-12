package kr.openbase.adcsmart.service.impl.dto;

public class OBDtoVRRP
{
	private int vrIndex;
	private int routerIndex;
	private int ifNum;
	
	public void setIfNum(int ifNum)
	{
		this.ifNum = ifNum;
	}
	public int getIfNum()
	{
		return this.ifNum;
	}
	
	public void setRouterIndex(int routerIndex)
	{
		this.routerIndex = routerIndex;
	}
	public int getRouterIndex()
	{
		return this.routerIndex;
	}
	
	public void setVrIndex(int vrIndex)
	{
		this.vrIndex = vrIndex;
	}
	public int getVrIndex()
	{
		return this.vrIndex;
	}
	
	@Override
	public String toString() 
	{
		return "OBDtoVRRP [vrIndex=" + vrIndex + ", routerIndex=" + routerIndex + ", ifNum="
				+ ifNum + "]";
	}
}
