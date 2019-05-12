package kr.openbase.adcsmart.service.snmp.dto;

public class DtoOidTrap
{
	private String notiName;
	private String notiOid;
	private String varMsgOid;
	private String varObjectOid;
	private String varLevelOid;
	private String varTypeOid;
    @Override
    public String toString()
    {
        return "DtoOidTrap [notiName=" + notiName + ", notiOid=" + notiOid + ", varMsgOid=" + varMsgOid + ", varObjectOid=" + varObjectOid + ", varLevelOid=" + varLevelOid + ", varTypeOid=" + varTypeOid + "]";
    }
    public String getNotiName()
    {
        return notiName;
    }
    public void setNotiName(String notiName)
    {
        this.notiName = notiName;
    }
    public String getNotiOid()
    {
        return notiOid;
    }
    public void setNotiOid(String notiOid)
    {
        this.notiOid = notiOid;
    }
    public String getVarMsgOid()
    {
        return varMsgOid;
    }
    public void setVarMsgOid(String varMsgOid)
    {
        this.varMsgOid = varMsgOid;
    }
    public String getVarObjectOid()
    {
        return varObjectOid;
    }
    public void setVarObjectOid(String varObjectOid)
    {
        this.varObjectOid = varObjectOid;
    }
    public String getVarLevelOid()
    {
        return varLevelOid;
    }
    public void setVarLevelOid(String varLevelOid)
    {
        this.varLevelOid = varLevelOid;
    }
    public String getVarTypeOid()
    {
        return varTypeOid;
    }
    public void setVarTypeOid(String varTypeOid)
    {
        this.varTypeOid = varTypeOid;
    }
}
