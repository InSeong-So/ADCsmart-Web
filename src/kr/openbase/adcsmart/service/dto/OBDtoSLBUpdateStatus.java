package kr.openbase.adcsmart.service.dto;

public class OBDtoSLBUpdateStatus
{
    private boolean updateSuccess = false;
    private String extraMsg = "";
    private String extraMsg2 ="";
    private int extraKey;
    
    public boolean isUpdateSuccess()
    {
        return updateSuccess;
    }
    public void setUpdateSuccess(boolean updateSuccess)
    {
        this.updateSuccess = updateSuccess;
    }
    public int getExtraKey()
	{
		return extraKey;
	}
	public void setExtraKey(int extraKey)
	{
		this.extraKey = extraKey;
	}
	public String getExtraMsg()
    {
        return extraMsg;
    }
    public void setExtraMsg(String extraMsg)
    {
        this.extraMsg = extraMsg;
    }
    public String getExtraMsg2()
    {
        return extraMsg2;
    }
    public void setExtraMsg2(String extraMsg2)
    {
        this.extraMsg2 = extraMsg2;
    }
    @Override
    public String toString()
    {
        return "OBDtoSLBUpdateStatus [updateSuccess=" + updateSuccess
                + ", extraMsg=" + extraMsg + ", extraMsg2=" + extraMsg2
                + ", extraKey=" + extraKey + "]";
    }
    
}