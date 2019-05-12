package kr.openbase.adcsmart.service.impl.pas.handler.dto;

public class OBDtoParserInfoPAS
{
	private int strIndex;
	private int extraIndex;
	private int strLength;
	
	public OBDtoParserInfoPAS()
	{
		
	}
	
	public OBDtoParserInfoPAS(int strIndex, int extraIndex, int strLength)
	{
		this.strIndex = strIndex;
		this.extraIndex = extraIndex;
		this.strLength = strLength;
	}
	
	@Override
	public String toString()
	{
		return "OBDtoParserInfoPAS [strIndex=" + strIndex + ", extraIndex=" + extraIndex + ", strLength=" + strLength + "]";
	}
	public int getStrIndex()
	{
		return strIndex;
	}
	public int getStrLength()
	{
		return strLength;
	}

	public void setStrLength(int strLength)
	{
		this.strLength = strLength;
	}

	public void setStrIndex(int strIndex)
	{
		this.strIndex = strIndex;
	}
	public int getExtraIndex()
	{
		return extraIndex;
	}
	public void setExtraIndex(int extraIndex)
	{
		this.extraIndex = extraIndex;
	}
}
