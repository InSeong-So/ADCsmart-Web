package kr.openbase.adcsmart.service.snmp.alteon;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.snmp4j.Snmp;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;

import kr.openbase.adcsmart.service.adcmond.OBAdcMonitorDB;
import kr.openbase.adcsmart.service.database.OBDatabase;
import kr.openbase.adcsmart.service.dto.OBDtoAdcAdditional;
import kr.openbase.adcsmart.service.dto.OBDtoAdcHealthCheckAlteon;
import kr.openbase.adcsmart.service.dto.OBDtoAdcInfo;
import kr.openbase.adcsmart.service.dto.OBDtoAdcNodeAlteon;
import kr.openbase.adcsmart.service.dto.OBDtoAdcPoolAlteon;
import kr.openbase.adcsmart.service.dto.OBDtoAdcPoolMemberAlteon;
import kr.openbase.adcsmart.service.dto.OBDtoAdcSnmpInfo;
import kr.openbase.adcsmart.service.dto.OBDtoAdcVServerAlteon;
import kr.openbase.adcsmart.service.dto.OBDtoAdcVService;
import kr.openbase.adcsmart.service.dto.OBDtoConnectionData;
import kr.openbase.adcsmart.service.dto.OBDtoNetworkInterface;
import kr.openbase.adcsmart.service.dto.OBNameValue;
import kr.openbase.adcsmart.service.dto.flb.OBDtoFlbFilterInfo;
import kr.openbase.adcsmart.service.impl.OBAdcManagementImpl;
import kr.openbase.adcsmart.service.impl.OBVServerDB;
import kr.openbase.adcsmart.service.impl.alteon.OBAdcMonitorAlteon;
import kr.openbase.adcsmart.service.impl.dto.OBDtoAdcConfigSlbAlteon;
import kr.openbase.adcsmart.service.impl.dto.OBDtoAdcPoolSimple;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonTrafficPoolGroup;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonTrafficServiceAlteon;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonTrafficVServer;
import kr.openbase.adcsmart.service.snmp.OBSnmpOidDB;
import kr.openbase.adcsmart.service.snmp.alteon.dto.DtoSnmpFilterPortAlteon;
import kr.openbase.adcsmart.service.snmp.alteon.dto.DtoSnmpHealthcheckAlteon;
import kr.openbase.adcsmart.service.snmp.alteon.dto.DtoSnmpInterfaceAlteon;
import kr.openbase.adcsmart.service.snmp.alteon.dto.DtoSnmpNetworkClassAlteon;
import kr.openbase.adcsmart.service.snmp.alteon.dto.DtoSnmpNetworkClassNetAlteon;
import kr.openbase.adcsmart.service.snmp.alteon.dto.DtoSnmpNodeAlteon;
import kr.openbase.adcsmart.service.snmp.alteon.dto.DtoSnmpPoolAlteon;
import kr.openbase.adcsmart.service.snmp.alteon.dto.DtoSnmpVirtServerAlteon;
import kr.openbase.adcsmart.service.snmp.alteon.dto.DtoSnmpVirtServiceAlteon;
import kr.openbase.adcsmart.service.snmp.alteon.dto.DtoSnmpVrrpAlteon;
import kr.openbase.adcsmart.service.snmp.alteon.dto.OBDtoRptFilterInfo;
import kr.openbase.adcsmart.service.snmp.alteon.dto.OBDtoRptInspectionSnmpAlteon;
import kr.openbase.adcsmart.service.snmp.alteon.dto.OBDtoVirtServicesInfoEntry;
import kr.openbase.adcsmart.service.snmp.dto.DtoOidCfgAdcAdditional;
import kr.openbase.adcsmart.service.snmp.dto.DtoOidCfgInterface;
import kr.openbase.adcsmart.service.snmp.dto.DtoOidCfgNode;
import kr.openbase.adcsmart.service.snmp.dto.DtoOidCfgPool;
import kr.openbase.adcsmart.service.snmp.dto.DtoOidCfgVirtServer;
import kr.openbase.adcsmart.service.snmp.dto.DtoOidCfgVirtService;
import kr.openbase.adcsmart.service.snmp.dto.DtoOidEntity;
import kr.openbase.adcsmart.service.snmp.dto.DtoOidInfoAdcResc;
import kr.openbase.adcsmart.service.snmp.dto.DtoOidInfoHealthCheck;
import kr.openbase.adcsmart.service.snmp.dto.DtoOidInfoNetworkClass;
import kr.openbase.adcsmart.service.snmp.dto.DtoOidStatVirtServer;
import kr.openbase.adcsmart.service.snmp.dto.DtoOidStatVirtService;
import kr.openbase.adcsmart.service.snmp.dto.OBDtoOidRptInspection;
import kr.openbase.adcsmart.service.utility.OBDateTime;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBDefineHealthcheckAlteon;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBExceptionLogin;
import kr.openbase.adcsmart.service.utility.OBExceptionUnreachable;
import kr.openbase.adcsmart.service.utility.OBParser;
import kr.openbase.adcsmart.service.utility.OBSystemLog;

public class OBSnmpAlteonV295 extends OBSnmpAlteon
{
    public OBSnmpAlteonV295(String host, OBDtoAdcSnmpInfo authInfo)
    {
        super(host, authInfo);
    }
    
    
    public OBDtoConnectionData  getVServerConns(int adcType, String swVersion, String vsID)  throws OBException
    {
        OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcType:%d, vsID:%s", adcType, vsID));
        OBSnmpOidDB oidDB = new OBSnmpOidDB();
        
        DtoOidStatVirtServer oid;
        try
        {
            
            oid = oidDB.getStatVirtServer(adcType, swVersion);
            OBDtoConnectionData result = getVServerConns(vsID, oid);
            OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", result));
            return result;
        }
        catch(OBException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());

        } 
    }
    
    private OBDtoConnectionData getVServerConns(String vsID, DtoOidStatVirtServer oid)  throws OBException
    {
        OBDtoConnectionData result = new OBDtoConnectionData();
        result.setOccurTime(OBDateTime.toTimestamp(OBDateTime.now()));
        
        Snmp snmp = openSnmp();
        
        String reqOid="";
        try
        {
            Object obj;
            // current connection
            String vsEnhId = OBParser.oidAlteonEnh(vsID);
            reqOid=oid.getCurConns()+vsEnhId;
            if(reqOid!=null)
            {
                obj = getSnmp(snmp, reqOid);
                if((checkObject(obj)) && (OBParser.isNumeric(obj.toString())))
                    result.setCurConns(Long.parseLong(obj.toString()));
                else
                {
                    OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to get concurrent connections. ip:%s, oid:%s", this.getHost(), reqOid));
                }
            }

            reqOid=oid.getCurConns()+vsEnhId;
            if(reqOid!=null)
            {
                obj = getSnmp(snmp, reqOid);
                if((checkObject(obj)) && (OBParser.isNumeric(obj.toString())))
                    result.setTotConns(Long.parseLong(obj.toString()));
                else
                {
                    OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to get total connections. ip:%s, oid:%s", this.getHost(), reqOid));
                }
            }           
            
            reqOid=oid.getCurConns()+vsEnhId;
            if(reqOid!=null)
            {
                obj = getSnmp(snmp, reqOid);
                if((checkObject(obj)) && (OBParser.isNumeric(obj.toString())))
                    result.setMaxConns(Long.parseLong(obj.toString()));
                else
                {
                    OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to get max connections. ip:%s, oid:%s", this.getHost(), reqOid));
                }
            }
        }
        catch(OBException e)
        {
            closeSnmp(snmp);
            throw e;
        }  
        catch(Exception e)
        {
            closeSnmp(snmp);
            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());

        } 

        closeSnmp(snmp);
        return result;
    }
    
    public OBDtoConnectionData  getVServiceConns(int adcType, String swVersion, String vsID, Integer virtPort)  throws OBException
    {
        OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcType:%d, vsID:%s, virtPort:%d", adcType, vsID, virtPort));
        
        try
        {
            OBSnmpOidDB oidDB = new OBSnmpOidDB();
            DtoOidStatVirtService oid;
            DtoOidCfgVirtService serviceOid;
            oid = oidDB.getStatVirtService(adcType, swVersion);
            serviceOid = oidDB.getCfgVirtService(adcType, swVersion);
            
            Integer serviceIndex = getServiceIndex(vsID, virtPort, serviceOid);
            OBDtoConnectionData result = getVServiceConns(vsID, serviceIndex, oid);
            
            OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", result));
            return result;
        }
        catch(OBException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
        }
    }
    
    private int getServiceIndex(String vsID, Integer virtPort, DtoOidCfgVirtService oid) throws OBException
    {
        try
        {
            List<VariableBinding> tmpList;
            String reqOid="";
            // virtual server ID 추출
            String vsEnhId = OBParser.oidAlteonEnh(vsID);
            reqOid=oid.getVsrvVirtPort()+vsEnhId;
            tmpList = walk(reqOid);
            for(int i=0;i<tmpList.size();i++)
            {
                VariableBinding var=tmpList.get(i);
                ArrayList<Integer> oidList = parseOid(var.getOid().toString(), reqOid);
                if(var.getVariable().toInt()==virtPort.intValue())
                {
                    return oidList.get(0);
                }
            }
            return 0;
        }
        catch(OBException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());

        }       
    }
    private OBDtoConnectionData getVServiceConns(String vsID, Integer serviceIndex, DtoOidStatVirtService oid)  throws OBException
    {
        try
        {
            OBDtoConnectionData result = new OBDtoConnectionData();
            List<VariableBinding> tmpList;
            String vsEnhId = OBParser.oidAlteonEnh(vsID)+"."+serviceIndex;
            
            result.setOccurTime(OBDateTime.toTimestamp(OBDateTime.now()));
            String reqOid=oid.getCurConns()+vsEnhId;
            tmpList = walk(reqOid);
            result.setCurConns(new Long(0));
            for(VariableBinding var:tmpList)
            {
                result.setCurConns(result.getCurConns()+var.getVariable().toLong());
            }
    
            reqOid=oid.getTotConns()+vsEnhId;
            tmpList = walk(reqOid);
            result.setTotConns(new Long(0));
            for(VariableBinding var:tmpList)
            {
                result.setTotConns(result.getTotConns()+var.getVariable().toLong());
            }
    
            reqOid=oid.getMaxConns()+vsEnhId;
            tmpList = walk(reqOid);
            result.setMaxConns(new Long(0));
            for(VariableBinding var:tmpList)
            {
                result.setMaxConns(result.getMaxConns()+var.getVariable().toLong());
            }
        
            return result;
        }
        catch(OBException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());

        }
    }
    
    public ArrayList<OBDtoMonTrafficServiceAlteon>  getStatVirtServiceMember(int adcType, String swVersion, ArrayList<OBDtoAdcVServerAlteon> vsList)  throws OBException
    {
        OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcType:%d, swVersion:%s", adcType, swVersion));
        try
        {
            OBSnmpOidDB oidDB = new OBSnmpOidDB();
            
            ArrayList<OBDtoVirtServicesInfoEntry> reqList = makeServiceInfoEntry(adcType, swVersion, vsList);
            DtoOidStatVirtService oid;
            oid = oidDB.getStatVirtService(adcType, swVersion);
            ArrayList<OBDtoMonTrafficServiceAlteon> list = getStatVirtServiceMember(oid, reqList);
            OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result-size:%d", list.size()));
            return list;
        }
        catch(OBException e)
        {
//          OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("xxx22xxxxxxxxxxx:%d", adcInfo.getIndex()));
            throw e;
        }
        catch(Exception e)
        {
//          OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("111111111.adcIndex:%d", adcInfo.getIndex()));
            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());

        }    
    }
    
    private ArrayList<OBDtoVirtServicesInfoEntry> makeServiceInfoEntry(int adcType, String swVersion, ArrayList<OBDtoAdcVServerAlteon> vsList) throws OBException
    {
        ArrayList<OBDtoVirtServicesInfoEntry> list = new ArrayList<OBDtoVirtServicesInfoEntry>();

        try
        {
//          ArrayList<OBDtoAdcVServerAlteon> vsList = new OBVServerDB().getVServerListAllAlteon(adcInfo.getIndex());
            
            OBSnmpOidDB oidDB = new OBSnmpOidDB();
            DtoOidInfoAdcResc oid = oidDB.getAdcResource(adcType, swVersion);
            
            for(OBDtoAdcVServerAlteon vs:vsList)
            {
                ArrayList<OBDtoAdcVService> vserviceList = vs.getVserviceList();
                // 지정된 virtual server에 할당된 service의 index 정보를 추출한다.
                HashMap<String, Integer> srvIndexHashMap = getServiceIndexMap(vs.getAlteonId(), oid.getServiceVirtPort());
                
                for(OBDtoAdcVService vsrv:vserviceList)
                {
                    OBDtoAdcPoolAlteon pool = vsrv.getPool();
                    if(pool==null)
                    {
                        continue;
                    }
                    ArrayList<OBDtoAdcPoolMemberAlteon> memberList = new ArrayList<OBDtoAdcPoolMemberAlteon>();
                    memberList = pool.getMemberList();
                    Integer srvIndex = srvIndexHashMap.get(vs.getAlteonId()+"_"+vsrv.getServicePort());
                    if(srvIndex == null)
                        srvIndex = 1;
                    for(OBDtoAdcPoolMemberAlteon member:memberList)
                    {
                        OBDtoVirtServicesInfoEntry obj = new OBDtoVirtServicesInfoEntry();
                        obj.setPoolIndex(pool.getIndex());
                        obj.setRealID(member.getAlteonNodeID());
                        obj.setSrvPort(vsrv.getServicePort());
                        obj.setVsID(vs.getAlteonId());
                        obj.setVsIndex(vs.getIndex());
                        obj.setVsrvIndex(srvIndex);
                        obj.setPoolName(pool.getName());
                        obj.setVsIPAddress(vs.getvIP());
                        obj.setVsName(vs.getName());
                        obj.setNodeAddress(member.getIpAddress());
                        list.add(obj);
                    }
                }
            }
        }
        catch(OBException e)
        {
            OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("adcType:%d, error:%s", adcType, e.getErrorMessage()));
            throw e;
        }
        catch(Exception e)
        {
            OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("adcType:%d, error:%s", adcType, e.getMessage()));
            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());

        }
        return list;
    }
    
    /**
     * Alteon용 Virtual service별 트래픽 정보를 추출한다.
     * 
     * @param oid
     * @param reqList
     * @return
     * @throws OBException
     */
    private ArrayList<OBDtoMonTrafficServiceAlteon> getStatVirtServiceMember(DtoOidStatVirtService oid, ArrayList<OBDtoVirtServicesInfoEntry> reqList)  throws OBException
    {
        OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. oid:%s. size:%d", oid, reqList.size()));
        
        ArrayList<OBDtoMonTrafficServiceAlteon> result = new ArrayList<OBDtoMonTrafficServiceAlteon>();
        String tempOid = "";
        Object obj = null;
        Snmp snmp = openSnmp();
        try
        {
            for(OBDtoVirtServicesInfoEntry vSrv:reqList)//int i=0;i<reqList.size();i++)
            {
                OBDtoMonTrafficServiceAlteon vsObj = new OBDtoMonTrafficServiceAlteon();
                
                vsObj.setVsID(vSrv.getVsID());
//              vsObj.setVsrvIndex(vSrv.getVsrvIndex());
                vsObj.setNodeID(vSrv.getRealID());
                vsObj.setVsIndex(vSrv.getVsIndex());
                vsObj.setPoolIndex(vSrv.getPoolIndex());
                vsObj.setVsIPAddress(vSrv.getVsIPAddress());
                vsObj.setVsName(vSrv.getVsName());
                vsObj.setPoolName(vSrv.getPoolName());
                vsObj.setSrvPort(vSrv.getSrvPort());
                vsObj.setNodeAddress(vSrv.getNodeAddress());
                vsObj.setCurConns(0);
                vsObj.setMaxConns(0);
                vsObj.setTotConns(0);
                vsObj.setBytesIn(0);
                
                String vsEnhId = OBParser.oidAlteonEnh(vSrv.getVsID());
                String rsEnhId = OBParser.oidAlteonEnh(vSrv.getRealID());
                // current connections 조사.
                tempOid = oid.getCurConns();
                if((tempOid!=null) && (!tempOid.isEmpty()))
                {
                    tempOid += vsEnhId + "." + vSrv.getVsrvIndex() + rsEnhId;
                    obj = getSnmp(snmp, tempOid);
                    if((checkObject(obj)) && (OBParser.isNumeric(obj.toString())))
                        vsObj.setCurConns(Long.parseLong(obj.toString()));
                }

                // max connections 조사.
                tempOid = oid.getMaxConns();
                if((tempOid!=null) && (!tempOid.isEmpty()))
                {
                    tempOid += vsEnhId + "." + vSrv.getVsrvIndex() + rsEnhId;
                    obj = getSnmp(snmp, tempOid);
                    if((checkObject(obj)) && (OBParser.isNumeric(obj.toString())))
                        vsObj.setMaxConns(Long.parseLong(obj.toString()));
                }
                
                // total connections 조사.
                tempOid = oid.getTotConns();
                if((tempOid!=null) && (!tempOid.isEmpty()))
                {
                    tempOid += vsEnhId + "." + vSrv.getVsrvIndex() + rsEnhId;
                    obj = getSnmp(snmp, tempOid);
                    if((checkObject(obj)) && (OBParser.isNumeric(obj.toString())))
                        vsObj.setTotConns(Long.parseLong(obj.toString()));
                }
                
                // in bytes 조사.
                tempOid = oid.getBytesIn();
                if((tempOid!=null) && (!tempOid.isEmpty()))
                {
                    tempOid += vsEnhId + "." + vSrv.getVsrvIndex() + rsEnhId;
                    obj = getSnmp(snmp, tempOid);
                    if((checkObject(obj)) && (OBParser.isNumeric(obj.toString())))
                        vsObj.setBytesIn(Long.parseLong(obj.toString()));
                }

                result.add(vsObj);
            }
        }
        catch(OBException e)
        {
            closeSnmp(snmp);
            throw e;
        }  
        catch(Exception e)
        {
            closeSnmp(snmp);
            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
        } 

        closeSnmp(snmp);
        OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result-size:%d", result.size()));
        return result;
    }
    
    public ArrayList<OBDtoMonTrafficPoolGroup> getTrafficPoolGroup(int adcType, String swVersion, ArrayList<OBDtoAdcPoolSimple> groupList) throws OBException
    {
        OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcType:%d, swVersion:%s", adcType, swVersion));
        OBSnmpOidDB oidDB = new OBSnmpOidDB();

        ArrayList<DtoOidEntity> groupOids = new ArrayList<DtoOidEntity>();
        try
        {
            groupOids = oidDB.getStatPoolGroup(adcType, swVersion);
            ArrayList<OBDtoMonTrafficPoolGroup> list = getTrafficPoolGroupSelected(groupOids, groupList);
            OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result-size:%d", list.size()));
            return list;
        }
        catch(OBException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());

        }
    }
    
    private ArrayList<OBDtoMonTrafficPoolGroup> getTrafficPoolGroupSelected(ArrayList<DtoOidEntity> oids, ArrayList<OBDtoAdcPoolSimple> groups)  throws OBException
    {
        String workingOid; //walk 작업중인 oid
        Snmp snmp = openSnmp();
        //결과값 관리
        ArrayList<OBDtoMonTrafficPoolGroup> groupTrafficList = new ArrayList<OBDtoMonTrafficPoolGroup>(); //마지막에 이걸로 리턴
        try
        {
            //OID ArrayList를 hashmap으로 바꾼다.
            HashMap<String, String> oidMap= new HashMap<String, String>();
            for (DtoOidEntity oid: oids)
            {
                oidMap.put(oid.getName(), oid.getValue());
            }

            Object snmpResult = null;
        
            for(OBDtoAdcPoolSimple group:groups)
            {
                OBDtoMonTrafficPoolGroup groupTraffic = new OBDtoMonTrafficPoolGroup();
                groupTraffic.setId(group.getAlteonId());
                groupTraffic.setAdcIndex(group.getAdcIndex());
                groupTraffic.setDbIndex(group.getDbIndex());
                //데이터만 구하고, 시간은 밖의 함수에서 넣게 되어 있는 기존 구조에 따른다.
                //bytes
                workingOid = oidMap.get("statBytesIn");
                if((workingOid!=null) && (!workingOid.isEmpty()))
                {
                    workingOid += ("." + group.getAlteonId());
                    snmpResult = getSnmp(snmp, workingOid);
                    if((checkObject(snmpResult)) && (OBParser.isNumeric(snmpResult.toString())))
                    {
                        groupTraffic.setBytesIn(Long.parseLong(snmpResult.toString()));
                    }
                }
                
                workingOid = oidMap.get("statMaxConns");
                if((workingOid!=null) && (!workingOid.isEmpty()))
                {
                    workingOid += ("." + group.getAlteonId());
                    snmpResult = getSnmp(snmp, workingOid);
                    if((checkObject(snmpResult)) && (OBParser.isNumeric(snmpResult.toString())))
                    {
                        groupTraffic.setMaxConns(Long.parseLong(snmpResult.toString()));
                    }
                }
                
                workingOid = oidMap.get("statTotConns");
                if((workingOid!=null) && (!workingOid.isEmpty()))
                {
                    workingOid += ("." + group.getAlteonId());
                    snmpResult = getSnmp(snmp, workingOid);
                    if((checkObject(snmpResult)) && (OBParser.isNumeric(snmpResult.toString())))
                    {
                        groupTraffic.setTotConns(Long.parseLong(snmpResult.toString()));
                    }
                }
                
                workingOid = oidMap.get("statCurConns");
                if((workingOid!=null) && (!workingOid.isEmpty()))
                {
                    workingOid += ("." + group.getAlteonId());
                    snmpResult = getSnmp(snmp, workingOid);
                    if((checkObject(snmpResult)) && (OBParser.isNumeric(snmpResult.toString())))
                    {
                        groupTraffic.setCurConns(Long.parseLong(snmpResult.toString()));
                    }
                }
                groupTraffic.setPktsIn(-1L);
                groupTraffic.setPktsOut(-1L);
                groupTraffic.setBytesOut(-1L);
                groupTrafficList.add(groupTraffic);
            }
        }
        catch(OBException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
        }
        finally
        {
            closeSnmp(snmp);
        }
        return groupTrafficList;
    }
    
    /**
     * service의 인덱스 정보를 구한다. HashMap의 Key는 "vsIndex"_"virtual Port"로 지정한다.
     *  
     * @param vsIndex
     * @param oid
     * @param commnunity
     * @return
     * @throws OBException
     */
    private HashMap<String, Integer> getServiceIndexMap(String vsID, String oid) throws OBException
    {
        String reqOid = "";
        List<VariableBinding> tmpList = null;
        HashMap<String, Integer> result = null;
        VariableBinding var = null;
        String mapKey = "";
        try
        {
            OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. vsID:%s, oid:%s", vsID, oid));
            result = new HashMap<String, Integer>();
            
            // node ID 추출
            String vsIDEnh = OBParser.oidAlteonEnh(vsID);
            reqOid=oid+vsIDEnh;
            tmpList = walk(reqOid);
            for(int i=0;i<tmpList.size();i++)
            {
                var = tmpList.get(i);
                ArrayList<Integer> oidList = parseOid(var.getOid().toString(), reqOid);//
                Integer virtPort=var.getVariable().toInt();
                if(virtPort!=null)
                {
                    mapKey = vsID+"_"+virtPort;
                    result.put(mapKey, oidList.get(0));
                }
//              else
//              {
//                  throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(target:%s, oid:%s)", var.getOid().toString(), oid));
//              }
            }
            OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", result));
            return result;
        }
        catch(OBException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(oid:%s)", oid));

        }       
    }
    
    public OBDtoAdcConfigSlbAlteon downloadSlbConf(Integer adcIndex, Timestamp applyTime, Integer vendorType, String swVersion) throws OBExceptionUnreachable, OBExceptionLogin, OBException
    {
        OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcIndex:%s, applyTime:%s, vendorType:%d, swVersion:%s", adcIndex, applyTime, vendorType, swVersion));

        OBDtoAdcConfigSlbAlteon config = new OBDtoAdcConfigSlbAlteon();
        DtoOidCfgInterface oidCfgInterface;
        DtoOidCfgAdcAdditional oidCfgAdcAdditional;
        DtoOidCfgNode oidCfgNode;
        DtoOidCfgPool oidCfgPool;
        DtoOidCfgVirtServer oidCfgVirtServer;
        DtoOidCfgVirtService oidCfgVirtService;
        DtoOidInfoHealthCheck oidInfoHealthcheck;
        DtoOidInfoNetworkClass oidInfoNetworkClass;

        // Filter부터 OID 자료 관리를 다르게 개선해서 개시! 위의 녀석들도 시간되면 이 방식으로 바꾼다.
        ArrayList<DtoOidEntity> oidFilter = new ArrayList<DtoOidEntity>();

        try
        {

            OBSnmpOidDB oidDB = new OBSnmpOidDB();

            oidCfgInterface = oidDB.getCfgInterface(vendorType, swVersion);
            oidCfgAdcAdditional = oidDB.getCfgAdcAdditional(vendorType, swVersion);
            oidCfgNode = oidDB.getCfgNode(vendorType, swVersion);
            oidCfgPool = oidDB.getCfgPool(vendorType, swVersion);

            oidInfoHealthcheck = oidDB.getHealthCheckInfo(vendorType, swVersion);
            oidInfoNetworkClass = oidDB.getNetworkClassInfo(vendorType, swVersion);
            oidCfgVirtServer = oidDB.getCfgVirtServer(vendorType, swVersion);
            oidCfgVirtService = oidDB.getCfgVirtService(vendorType, swVersion);


            // : if(vendorType == Alteon && flb==enabled)
            oidFilter = oidDB.getCfgFilter(vendorType, swVersion);

            ArrayList<DtoSnmpVirtServiceAlteon> snmpVirtServiceList;
            ArrayList<DtoSnmpVirtServerAlteon> snmpVirtServerList;
            ArrayList<DtoSnmpPoolAlteon> snmpPool;
            ArrayList<DtoSnmpHealthcheckAlteon> snmpHealthcheckList;
            ArrayList<DtoSnmpNetworkClassAlteon> snmpNetworkClassList;

            ArrayList<DtoSnmpNodeAlteon> snmpNode;
            ArrayList<DtoSnmpVrrpAlteon> snmpVrrp;
            OBDtoAdcAdditional adcAdditionalInfo = null;
            ArrayList<DtoSnmpInterfaceAlteon> snmpInterfaceList;

            // virtual service
            snmpVirtServiceList = getVirtServiceList(oidCfgVirtService);

            // virtual server
            snmpVirtServerList = getVirtServerList(oidCfgVirtServer);

            // pool
            snmpPool = getPoolList(oidCfgPool);

            // node
            snmpNode = getNodeList(oidCfgNode);

            // healthcheck
            snmpHealthcheckList = getHealthcheckList(oidInfoHealthcheck);

            // network class
            snmpNetworkClassList = getNetworkClassList(oidInfoNetworkClass);

            // vrrp 정보, sync 정보 추출.
            adcAdditionalInfo = getAdditionalInfo(adcIndex, oidCfgAdcAdditional);
            // vrrp
            snmpVrrp = getVrrpList(oidCfgAdcAdditional);
            // interface
            snmpInterfaceList = getInterfaceList(oidCfgInterface);

            // filter 그리고 filter-enabled인 physical port마다, 할당된 filter 목록들
            ArrayList<OBDtoFlbFilterInfo> snmpFilters = getFilterList(oidFilter, swVersion);
            ArrayList<DtoSnmpFilterPortAlteon> snmpFilterPhysicalPorts = getFilterPhysicalPortList(oidFilter); // oidFilter에 filter-physicalport oid도 들어 있음

            ArrayList<OBDtoAdcVServerAlteon> virtServers = new ArrayList<OBDtoAdcVServerAlteon>();// = new ArrayList<OBAdcVirtualServer>();
            ArrayList<OBDtoAdcNodeAlteon> realServers = new ArrayList<OBDtoAdcNodeAlteon>();// = new ArrayList<OBAdcRealServer>();
            ArrayList<OBDtoAdcPoolAlteon> serverPools = new ArrayList<OBDtoAdcPoolAlteon>();// ups = new ArrayList<OBAdcServerPool>();
            ArrayList<OBDtoNetworkInterface> ifList = new ArrayList<OBDtoNetworkInterface>();
            ArrayList<OBDtoAdcHealthCheckAlteon> healthcheckList = new ArrayList<OBDtoAdcHealthCheckAlteon>();

            // virtual server
            for(int i = 0; i < snmpVirtServerList.size(); i++)
            {
                DtoSnmpVirtServerAlteon obj = snmpVirtServerList.get(i);
                OBDtoAdcVServerAlteon vs = new OBDtoAdcVServerAlteon();
                vs.setAdcIndex(adcIndex);
                vs.setApplyTime(applyTime);
                vs.setAlteonId(obj.getVsID());
                vs.setName(obj.getVsName());
                vs.setvIP(obj.getVsIpAddress());
                vs.setStatus(obj.getVsStatus());
                vs.setUseYN(obj.getVsState1());
                vs.setState(obj.getVsState1());
                vs.setVrrpYN(false); // 안 쓰는데 기본값 할당, vrrpState로 대체
                vs.setVrrpState(OBDefine.VRRP_STATE.NONE);

                DtoSnmpVrrpAlteon vrrp = getVrrpInfo(vs.getvIP(), snmpVrrp);
                if(vrrp != null)
                {
                    vs.setVrrpState(vrrpStateAlteonToCommon(vrrp.getVrrpState())); // snmp로 구한 alteon vrrp상태는 adcsmart enable/disable 정의와 다르므로 변환한다.
                    vs.setIfNum(vrrp.getVrrpIfIndex());
                    vs.setRouterIndex(vrrp.getVrrpIndex());
                    vs.setVrIndex(vrrp.getVrrpID());
                }

                vs.setVserviceList(getServiceList(vs.getAlteonId(), snmpVirtServiceList, snmpPool, snmpNode));

                // network class - begin
                String temp = "";
                if(obj.getVsNwClassList() != null && obj.getVsNwClassList().size() > 0) // nwclass id가 virtual server에 할당됐다.
                {
                    vs.setNwclassId(1); // 1:VS에 network class가 있다. / 0:network class가 없다
                    for(String nwClass : obj.getVsNwClassList())
                    {
                        for(DtoSnmpNetworkClassAlteon nc : snmpNetworkClassList)
                        {
                            if(nc.getId().equals(nwClass))
                            {
                                if(temp.isEmpty() == false)
                                {
                                    temp += ", ";
                                }
                                temp += (nc.getId() + ":" + nc.getName());
                                break;
                            }
                        }
                        // if(temp.isEmpty()==true) //virtual server에 nwclass id를 지정했는데, 실제 그 nwclass는 없는 상황. alteon v29에서는 이 상태가 가능하다. 없으면 안 표시하는 걸로. 그래서 주석처리함
                        // {
                        // temp = OBMessages.getMessage(OBMessages.MSG_SYSTEM_DATA_NOT_FOUND);
                        // }
                    }
                }
                vs.setSubInfo(temp);
                // network class - end

                virtServers.add(vs);
            }

            // pool
            for(int i = 0; i < snmpPool.size(); i++)
            {
                DtoSnmpPoolAlteon obj = snmpPool.get(i);
                OBDtoAdcPoolAlteon pool = new OBDtoAdcPoolAlteon();
                OBDtoAdcHealthCheckAlteon hc = new OBDtoAdcHealthCheckAlteon();
                pool.setAlteonId(obj.getPoolID());
                pool.setLbMethod(obj.getPoolLbMethod());

                // healthcheck
                pool.setHealthCheck(OBDefineHealthcheckAlteon.NOT_ALLOWED); // 안 쓰는 field, 일단 NotAllowed로 깐다.

                // healthcheck defined-list
                hc.setId(new OBDefineHealthcheckAlteon().getHealthcheckIdAlteon(swVersion, obj.getPoolHealthCheck()));
                // hc.setDbIndex(OBCommon.makeHealthDbIndexAlteon(pool.getAlteonId(), hc.getId()));
                pool.setHealthCheckV2(hc);

                pool.setName(obj.getPoolName());
                obj.getMemberIDs();
                pool.setBakID(obj.getBakID());
                pool.setBakType(obj.getBakType());
                pool.setMemberList(getMemberList(obj.getMemberIDs(), obj.getMemberState(), snmpNode));
                serverPools.add(pool);
            }

            // realservers
            for(DtoSnmpNodeAlteon obj : snmpNode)
            {
                OBDtoAdcNodeAlteon node = new OBDtoAdcNodeAlteon();
                node.setAlteonId(obj.getNodeID());
                node.setIpAddress(obj.getNodeIpAddr());
                node.setName(obj.getNodeName());
                node.setState(obj.getNodeState());
                node.setStatus(obj.getNodeStatus());
                node.setBakId(obj.getBakID());
                node.setBakType(obj.getBakType());
                node.setExtra(makeNodeExtraInfo(obj, swVersion));
                node.setRatio(obj.getNodeWeight()); //Alteon의 weight = ratio
                realServers.add(node);
            }

            // interface
            for(DtoSnmpInterfaceAlteon obj : snmpInterfaceList)
            {
                OBDtoNetworkInterface intf = new OBDtoNetworkInterface();
                intf.setIfNum(obj.getIntfIndex());
                intf.setIpAddress(obj.getIntfAddr());
                intf.setNetmask(obj.getIntfMask());
                ifList.add(intf);
            }

            // healthcheck defined-list
            if(swVersion.compareTo(OBDefineHealthcheckAlteon.V2_START_VERSION) < 0)
            {
                Iterator<?> keyIterator = OBDefineHealthcheckAlteon.V1.TYPE.keySet().iterator();
                Integer key;
                String val;
                while(keyIterator.hasNext())
                {
                    OBDtoAdcHealthCheckAlteon hc = new OBDtoAdcHealthCheckAlteon();
                    key = (Integer) keyIterator.next();
                    val = (String) OBDefineHealthcheckAlteon.V1.TYPE.get(key);
                    hc.setId(val);
                    hc.setName(val);
                    hc.setType(val);
                    hc.setExtra(val);
                    healthcheckList.add(hc);
                }
            }
            else
            // v29 이상
            {
                for(DtoSnmpHealthcheckAlteon obj : snmpHealthcheckList)
                {
                    OBDtoAdcHealthCheckAlteon hc = new OBDtoAdcHealthCheckAlteon();
                    String extra = "";
                    String strType = OBDefineHealthcheckAlteon.V2.TYPE.get(Integer.parseInt(obj.getType()));
                    hc.setId(obj.getId());
                    // hc.setDbIndex(OBCommon.makeHealthDbIndexAlteon(adcIndex, hc.getId())); //DB 인덱스는 여기서 안 넣는다. DB입력할때.
                    hc.setName(obj.getName());
                    hc.setType(obj.getType());
                    hc.setDestinationIp(obj.getDestinationIp());

                    if(hc.getId().equals(strType))
                    { // primitive healthcheck type이다.
                        extra = hc.getId();
                    }
                    else
                    {
                        if(hc.getName() != null && hc.getName().isEmpty() == false)
                        {
                            if(extra.isEmpty() == false)
                            {
                                extra += ", ";
                            }
                            extra += "name " + hc.getName();
                        }
                        if(hc.getType() != null && hc.getType().isEmpty() == false)
                        {
                            if(extra.isEmpty() == false)
                            {
                                extra += ", ";
                            }
                            extra += "type " + strType;
                        }
                        if(hc.getDestinationIp() != null && hc.getDestinationIp().isEmpty() == false)
                        {
                            if(hc.getType().equals(((Integer) OBDefineHealthcheckAlteon.V2.LOGEXP).toString())) // logical expression type code = 25, 문자열로 비교한다.
                            {
                                if(extra.isEmpty() == false)
                                {
                                    extra += ", ";
                                }
                                extra += "expr:" + hc.getDestinationIp();
                            }
                            else
                            // type이 logical expression이 아니면 destination 필드에 진짜 destination이 들어있다.
                            {
                                if(hc.getDestinationIp().equals("none") == false) // 없으면 none이 있으므로 none을 배제한다.
                                {
                                    if(extra.isEmpty() == false)
                                    {
                                        extra += ", ";
                                    }
                                    extra += "dest:" + hc.getDestinationIp();
                                }
                            }
                        }
                        if(hc.getId() != null && hc.getId().isEmpty() == false)
                        {
                            extra = hc.getId() + ":" + extra;
                        }
                    }
                    if(extra.isEmpty() == false)
                    {
                        hc.setExtra(extra);
                    }
                    healthcheckList.add(hc);
                }
            }
            config.setAdcAdditionalInfo(adcAdditionalInfo);
            config.setIfList(ifList);
            config.setRealServers(realServers);
            config.setServerPools(serverPools);
            config.setVirtServers(virtServers);
            config.setHealthcheckList(healthcheckList);
            config.setFilterList(snmpFilters);
            config.setFilterPhysicalPortList(snmpFilterPhysicalPorts);
        }
        catch(OBException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
        }

        OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end."));
        return config;
    }
    
    // ykkim 2013.3.4 
    // updateAdcConfigSlbOld()가 원래 업데이트 함수였는데, insert 성능 개선 목적으로 바꿈
    // - pool, virtual server, virtual service의 insert를 건건마다 하지 않고 values를 모아서 한번에 한다. 
    // - poolmember는 이미 multiple insert를 하고 있었다.    
    public void updateAdcConfigSlb(int adcIndex, Timestamp applyTime) throws OBException
    {
        OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcIndex:%d, applyTime:%s", adcIndex, applyTime));

        long tt0 = 0, tt1 = 0, tt2 = 0, tt3 = 0, tt4 = 0;
        tt0 = System.currentTimeMillis();

        OBVServerDB vDB = new OBVServerDB();
        OBDtoAdcInfo adcInfo;
        adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex);

        OBDtoAdcConfigSlbAlteon config;
        OBDatabase db = new OBDatabase();
        try
        {
            StringBuilder transactionQuery = new StringBuilder(OBDefine.MAX_STRING_BUILDER_SIZE);
            transactionQuery.append(" BEGIN; ");

            config = downloadSlbConf(adcIndex, applyTime, adcInfo.getAdcType(), adcInfo.getSwVersion()); // real, group, health, vservice, vserver 구하기
            tt1 = System.currentTimeMillis();
            OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "<time> download slb: " + (tt1 - tt0) / 1000.0);

            // 기존 db 삭제.
            vDB.delVServerAll(adcIndex, transactionQuery);
            vDB.delNodeAll(adcIndex, transactionQuery);
            vDB.delPoolAll(adcIndex, transactionQuery);
            vDB.delVServiceAll(adcIndex, transactionQuery);
            vDB.delPoolmemberAll(adcIndex, transactionQuery);
            vDB.delL3InterfaceAll(adcIndex, transactionQuery);
            vDB.delHealthCheckAll(adcIndex, transactionQuery);
            vDB.delFilterAndPortMappingAll(adcIndex, transactionQuery);

            tt2 = System.currentTimeMillis();
            OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "<time> delete db: " + (tt2 - tt1) / 1000.0);

            // 아래 작업은 순서를 지켜야 한다. 각 테이블간의 relation 이 존재.
            // node 정보 추가
            ArrayList<OBDtoAdcNodeAlteon> nodeList = config.getRealServers();
            vDB.addNodeInfoAlteon(adcIndex, nodeList, transactionQuery);

            // l3 interface 정보 추가.
            ArrayList<OBDtoNetworkInterface> ifList = config.getIfList();
            vDB.addL3InterfaceInfo(adcIndex, ifList, transactionQuery);

            ArrayList<OBDtoAdcPoolAlteon> poolList = config.getServerPools();
            ArrayList<OBDtoAdcVServerAlteon> vsList = config.getVirtServers();

            // healthcheck 정보 추가
            vDB.addHealthCheckAlteon(adcIndex, config.getHealthcheckList(), transactionQuery);

            // pool, virtual service, virtual server를 추가한다.
            // object별로 나누지 않고 한번에 하는 이유 :
            // 1. 이 3개 구성요소는 추가 순서를 지켜서 해야 한다. 작업이 서로 의존적이다. pool->virtual service->virtual server
            // 2. 성능을 높이려면 insert의 value를 모아서 query를 한번만 실행해야 한다.그러려면 query를 미리 구성해야 하고, object별로 작업을 나누기 어렵다.
            vDB.addPoolVServerVServiceAlteon(adcIndex, poolList, vsList, transactionQuery);

            OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("end of adding vs.cnt:%d", vsList.size()));
            // pool member를 추가한다. insert를 한번만 하도록 구성되어 있다.
            vDB.addPoolmemberInfoAlteon(adcIndex, poolList, transactionQuery);

            // filter를 추가한다.
            // physical port - filter 정보 mapping을 저장한다.
            vDB.addFilterAndPortMappingAlteon(adcIndex, config.getFilterList(), config.getFilterPhysicalPortList(), transactionQuery);
            vDB.updateFlbMonitoringGroup(adcIndex, transactionQuery); // 그룹이 지워졌을 수 있는데, flb 모니터링 그룹에도 확인해서 지운다.

            // adcAdditional Info 추가.
            vDB.updateAdcAdditionalInfo(config.getAdcAdditionalInfo(), transactionQuery);
            
            transactionQuery.append(" COMMIT; ");

            // peer ip에 대한 active pair 인덱스 추가.
            // if(config.getAdcAdditionalInfo().getSyncState()==OBDefine.STATE_ENABLE)
            // vDB.updateAdcPairIndex(adcInfo, config.getAdcAdditionalInfo().getPeerIP(), db);
            // else
            // vDB.updateAdcPairIndex(adcInfo, "", db);
            db.openDB();
            db.executeUpdate(transactionQuery.toString());

            tt3 = System.currentTimeMillis();
            OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "<time> write slb: " + (tt3 - tt2) / 1000.0);

            // status update
            ArrayList<OBDtoAdcVServerAlteon> list = new OBAdcMonitorAlteon().getSlbStatusAlteon(adcIndex);
            new OBAdcMonitorDB().writeSlbStatusAlteon(adcIndex, list);

            tt4 = System.currentTimeMillis();
            OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "<time> refresh status: " + (tt4 - tt3) / 1000.0);
        }
        catch(OBException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
        }
        finally
        {
            if(db!=null)
                db.closeDB();
        }
        OBSystemLog.call(OBDefine.LOGFILE_DEBUG, "end. <time> update slb total: " + (tt4 - tt0) / 1000.0);
    }
    
    private String makeNodeExtraInfo(DtoSnmpNodeAlteon node, String swVersion)
    { //이 extra string은 web에서 parsing하기 때문에 형식을 바꾸면 안된다.
        if(swVersion.compareTo("29")<0) //v29이상만 채우는 데이터이다.
        {
            if(node.getNodePort()!=null&&!node.getNodePort().isEmpty())
                return node.getNodePort().toString();
            return null;
        }
        
        String ret = "port:";
        int i;
        int portNum = node.getNodePort().size();
        if(node.getNodePort()!=null && portNum>0)
        {
            for(i=0; i<portNum; i++)
            {
                if(node.getNodePort().get(i) !=null)
                {
                    ret += node.getNodePort().get(i);
                    if(i<portNum-1)
                    {
                        ret += " ";
                    }
                }
            }
        }
        ret += ",inter:";
        if(node.getNodeInterval()!=null)
        {
            if(node.getNodeInterval()==0)
            {
                ret += OBDefine.NODESTR;
            }
            else
            {
                ret += node.getNodeInterval();
            }
        }
        ret += ",retry:";
        if(node.getNodeFailRetry()!=null)
        {
            if(node.getNodeFailRetry()==0)
            {
                ret += OBDefine.NODESTR;
            }
            else
            {
                ret += node.getNodeFailRetry();
            }
        }
        ret += ",backup:";
        if(node.getNodeBackup()!=null)
        {
            ret += node.getNodeBackup();
        }
        ret += ",weight:";
        if(node.getNodeWeight()!=null)
        {
            ret += node.getNodeWeight();
        }
        ret += ",maxcon:";
        if(node.getNodeMaxConns()!=null)
        {
            ret += node.getNodeMaxConns();
            if(node.getNodeMaxConMode()!=null)
            {
                if(node.getNodeMaxConMode().equals(1))
                {
                    ret += "/physical";
                }
                else if(node.getNodeMaxConMode().equals(2))
                {
                    ret += "/logical";
                }
            }
        }
        ret += ",timeout:";
        if(node.getNodeTimeout()!=null)
        {
            ret += node.getNodeTimeout();
        }
        return ret;
    }
    
    private ArrayList<OBDtoAdcPoolMemberAlteon> getMemberList(ArrayList<String> ids, ArrayList<Integer>state, ArrayList<DtoSnmpNodeAlteon> snmpNode)
    {
        ArrayList<OBDtoAdcPoolMemberAlteon> list = new ArrayList<OBDtoAdcPoolMemberAlteon>();
        for(int i=0;i<ids.size();i++)
        {
            for(int ii=0;ii<snmpNode.size();ii++)
            {
                DtoSnmpNodeAlteon node = snmpNode.get(ii);
                if(ids.get(i).equals(node.getNodeID()))
                {
                    OBDtoAdcPoolMemberAlteon obj = new OBDtoAdcPoolMemberAlteon();
                    obj.setAlteonNodeID(node.getNodeID());
                    obj.setIpAddress(node.getNodeIpAddr());
                    obj.setPort(0);
                    obj.setState(state.get(i));
                    obj.setStatus(node.getNodeStatus());
                    obj.setBackupType(node.getBakType());
                    obj.setBackupId(node.getBakID());
                    if(node.getNodePort()!=null)
                        obj.setExtra(node.getNodePort().toString());
                    //obj.setExtra(makeNodeExtraInfo(node)); ykk___
                    list.add(obj);
                }
            }
        }
        return list;
    }
    
    private ArrayList<OBDtoAdcVService> getServiceList(String vsID, ArrayList<DtoSnmpVirtServiceAlteon> snmpVirtServiceList, ArrayList<DtoSnmpPoolAlteon> snmpPool, ArrayList<DtoSnmpNodeAlteon> snmpNode)
    {
        ArrayList<OBDtoAdcVService> list = new ArrayList<OBDtoAdcVService>();
        
        for(int i=0;i<snmpVirtServiceList.size();i++)
        {
            DtoSnmpVirtServiceAlteon service = snmpVirtServiceList.get(i);
            if(service.getVsID().equals(vsID))
            {
                OBDtoAdcVService obj = new OBDtoAdcVService();
                obj.setPool(getPoolInfo(service.getPoolID(), snmpPool, snmpNode));
                obj.setRealPort(service.getVsrvRealPort());
                obj.setServicePort(service.getVsrvVirtPort());
                obj.setStatus(service.getStatus());
                list.add(obj);
            }
        }
        return list;
    }
    
    private Integer vrrpStateAlteonToCommon(Integer vrrpStateSnmp)
    {
        if(vrrpStateSnmp.equals(OBDefine.VRRP_STATE_ALTEON.DISABLE))
        {
            return OBDefine.VRRP_STATE.DISABLE;
        }
        else if(vrrpStateSnmp.equals(OBDefine.VRRP_STATE_ALTEON.ENABLE))
        {
            return OBDefine.VRRP_STATE.ENABLE;
        }
        else //none of above
        {
            return OBDefine.VRRP_STATE.DISABLE; //정상이라면 여기 오지 않겠지만 어떻든 vrrp 설정이 있으니 disable로 지정한다.
        }
    }
    
    private DtoSnmpVrrpAlteon getVrrpInfo(String vIP, ArrayList<DtoSnmpVrrpAlteon> snmpVrrp)
    {
        for(int i=0;i<snmpVrrp.size();i++)
        {
            if((snmpVrrp.get(i).getVrrpAddr()!=null) && (!snmpVrrp.get(i).getVrrpAddr().isEmpty()))
            {
                if(snmpVrrp.get(i).getVrrpAddr().compareToIgnoreCase(vIP)==0)
                {
                    return snmpVrrp.get(i);
                }
            }
        }
        return null;
    }
    
    private ArrayList<DtoSnmpInterfaceAlteon> getInterfaceList(DtoOidCfgInterface oid) throws OBException
    {
        try
        {
            ArrayList<DtoSnmpInterfaceAlteon> interfaceList = new ArrayList<DtoSnmpInterfaceAlteon>();
            
            List<VariableBinding> tmpList;
            
            // interface index 추출
            tmpList = walk(oid.getIntfIndex());
            for(int i=0;i<tmpList.size();i++)
            {
                VariableBinding var=tmpList.get(i);
                ArrayList<Integer> oidList = parseOid(var.getOid().toString(), oid.getIntfIndex());
                if(oidList.size()!=1)
                {
                    throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(target:%s, index:%s)", var.getOid().toString(), oid.getIntfIndex()));
                }
                Integer index=oidList.get(0);
                
                if(index!=null)
                {
                    DtoSnmpInterfaceAlteon intf = new DtoSnmpInterfaceAlteon();
                    intf.setIntfIndex(index);
                    interfaceList.add(intf);
                }
                else
                {
                    throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(target:%s, index:%s)", var.getOid().toString(), oid.getIntfIndex()));
                }
            }       
            
            if(interfaceList.size()==0)
                return interfaceList;
            
            // intfAddr 추출.
            tmpList = walk(oid.getIntfAddr(), interfaceList.size() ,3);
//          if(tmpList.size() != interfaceList.size())
//          {
//              throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("parse error.(size(%d/%d)", tmpList.size(), interfaceList.size()));
//          }
            for(int i=0;i<tmpList.size();i++)
            {
                VariableBinding var=tmpList.get(i);
                ArrayList<Integer> oidList = parseOid(var.getOid().toString(), oid.getIntfAddr());
                if(oidList.size()!=1)
                {
                    throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(target:%s, index:%s)", var.getOid().toString(), oid.getIntfIndex()));
                }
                Integer index=oidList.get(0);
                
                if(index==null)
                {
                    throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(target:%s, index:%s)", var.getOid().toString(), oid.getIntfIndex()));
                }
                
                if(index.equals(interfaceList.get(i).getIntfIndex()))
                {
                    interfaceList.get(i).setIntfAddr(var.getVariable().toString());
                }
                else
                {
                    throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(target:%s, index:%s)", var.getOid().toString(), oid.getIntfIndex()));
                }
            }       
            
            // intfMask 추출.
            tmpList = walk(oid.getIntfMask(), interfaceList.size() ,3);
//          if(tmpList.size() != interfaceList.size())
//          {
//              throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("parse error.(size(%d/%d)", tmpList.size(), interfaceList.size()));
//          }
            for(int i=0;i<tmpList.size();i++)
            {
                VariableBinding var=tmpList.get(i);
                ArrayList<Integer> oidList = parseOid(var.getOid().toString(), oid.getIntfMask());
                if(oidList.size()!=1)
                {
                    throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(target:%s, index:%s)", var.getOid().toString(), oid.getIntfIndex()));
                }
                Integer index=oidList.get(0);
                
                if(index==null)
                {
                    throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(target:%s, index:%s)", var.getOid().toString(), oid.getIntfIndex()));
                }
                
                if(index.equals(interfaceList.get(i).getIntfIndex()))
                {
                    interfaceList.get(i).setIntfMask(var.getVariable().toString());
                }
                else
                {
                    throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(target:%s, index:%s)", var.getOid().toString(), oid.getIntfIndex()));
                }
            }       
            
            // intfState 추출.
            tmpList = walk(oid.getIntfState(), interfaceList.size() ,3);
//          if(tmpList.size() != interfaceList.size())
//          {
//              throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("parse error.(size(%d/%d)", tmpList.size(), interfaceList.size()));
//          }
            for(int i=0;i<tmpList.size();i++)
            {
                VariableBinding var=tmpList.get(i);
                ArrayList<Integer> oidList = parseOid(var.getOid().toString(), oid.getIntfState());
                if(oidList.size()!=1)
                {
                    throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(target:%s, index:%s)", var.getOid().toString(), oid.getIntfIndex()));
                }
                Integer index=oidList.get(0);
                
                if(index==null)
                {
                    throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(target:%s, index:%s)", var.getOid().toString(), oid.getIntfIndex()));
                }
                
                if(index.equals(interfaceList.get(i).getIntfIndex()))
                {
                    interfaceList.get(i).setIntfState(var.getVariable().toInt());
                }
                else
                {
                    throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(target:%s, index:%s)", var.getOid().toString(), oid.getIntfIndex()));
                }
            }
            
            return interfaceList;
        }
        catch(OBException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());

        }
    }
    
    private ArrayList<OBDtoFlbFilterInfo> getFilterList(ArrayList<DtoOidEntity> oidList, String swVersion) throws OBException
    {
        OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. oidList:%s", oidList));
        String workingOid; //walk 작업중인 oid
        try
        {
            //OID ArrayList를 hashmap으로 바꾼다.
            HashMap<String, String> oidMap= new HashMap<String, String>();
            for (DtoOidEntity oid: oidList)
            {
                oidMap.put(oid.getName(), oid.getValue());
            }

            //결과값 관리
            HashMap<String, OBDtoFlbFilterInfo> filterMap= new HashMap<String, OBDtoFlbFilterInfo>(); //처리할 때 결과 모으는 hasp map
            ArrayList<OBDtoFlbFilterInfo> filters = new ArrayList<OBDtoFlbFilterInfo>(); //마지막에 이걸로 리턴

            List<VariableBinding> tmpList;
            int tmpInt = 0;
            String filterId;
            int filterNum, i;

            // AlteonFilterId
            workingOid = oidMap.get("AlteonFilterId");
            tmpList = walk(workingOid);
            for(i=0;i<tmpList.size();i++)
            {
                VariableBinding var=tmpList.get(i);
                filterId = var.getVariable().toString();
                if(filterId!=null)
                {
                    OBDtoFlbFilterInfo filter = new OBDtoFlbFilterInfo();
                    filter.setFilterId(Integer.valueOf(filterId));
                    filterMap.put(filterId, filter);
                }
                else
                {
                    throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(AlteonFilterId, target:%s, oid:%s)", 
                            var.getOid().toString(), workingOid));
                }
            }
            filterNum = filterMap.size();
            if(filterNum==0)
            {
                return filters; //빈 ArrayList를 return한다.
            }

            // AlteonFilterState
            workingOid = oidMap.get("AlteonFilterState");
            tmpList = walk(workingOid);
            OBDtoFlbFilterInfo filter;
            String currentfilterId; //filter를 순환하면서 작업할 때, id를 기록한다.

            for(i=0;i<tmpList.size();i++)
            {
                VariableBinding var=tmpList.get(i);
                ArrayList<Integer> oidRes = parseOid(var.getOid().toString(), workingOid);
                if(oidRes.size()==1)
                {
                    currentfilterId = oidRes.get(0).toString();
                    filter = filterMap.get(currentfilterId);
                    if(filter != null && currentfilterId.equals(filter.getFilterId().toString()))
                    {
                        filter.setState(var.getVariable().toInt()==1 ? OBDefine.STATE_ENABLE:OBDefine.STATE_DISABLE); //1:enable, 2:disable
                    }
                    else
                    {
                        throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("Filter info not found.(AlteonFilterState, target:%s, oid:%s)", var.getOid().toString(), workingOid));
                    }
                }
                else
                {
                    throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("Filter state Data invalid.(AlteonFilterState, target:%s, oid:%s)", var.getOid().toString(), workingOid));
                }
            }
            // AlteonFilterName
            workingOid = oidMap.get("AlteonFilterName");
            tmpList = walk(workingOid);
            for(i=0;i<tmpList.size();i++)
            {
                VariableBinding var=tmpList.get(i);
                ArrayList<Integer> oidRes = parseOid(var.getOid().toString(), workingOid);
                if(oidRes.size()==1)
                {
                    currentfilterId = oidRes.get(0).toString();
                    filter = filterMap.get(currentfilterId);
                    if(filter != null && currentfilterId.equals(filter.getFilterId().toString()))
                    {
                        filter.setName(alteonSnmpHexStringDecode(var.getVariable().toString()));
                    }
                    else
                    {
                        throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(AlteonFilterName, target:%s, oid:%s)", var.getOid().toString(), workingOid));
                    }
                }
                else
                {
                    throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(AlteonFilterName, target:%s, oid:%s)", var.getOid().toString(), workingOid));
                }
            }
            // AlteonFilterSrcIp
            workingOid = oidMap.get("AlteonFilterSrcIp");
            tmpList = walk(workingOid);
            for(i=0;i<tmpList.size();i++)
            {
                VariableBinding var=tmpList.get(i);
                ArrayList<Integer> oidRes = parseOid(var.getOid().toString(), workingOid);
                if(oidRes.size()==1)
                {
                    currentfilterId = oidRes.get(0).toString();
                    filter = filterMap.get(currentfilterId);
                    if(filter != null && currentfilterId.equals(filter.getFilterId().toString()))
                    {
                        filter.setSrcIP(var.getVariable().toString());
                    }
                    else
                    {
                        throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(AlteonFilterSrcIp, target:%s, oid:%s)", var.getOid().toString(), workingOid));
                    }
                }
                else
                {
                    throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(AlteonFilterSrcIp, target:%s, oid:%s)", var.getOid().toString(), workingOid));
                }
            }
            // AlteonFilterSrcIpMask
            workingOid = oidMap.get("AlteonFilterSrcIpMask");
            tmpList = walk(workingOid);
            for(i=0;i<tmpList.size();i++)
            {
                VariableBinding var=tmpList.get(i);
                ArrayList<Integer> oidRes = parseOid(var.getOid().toString(), workingOid);
                if(oidRes.size()==1)
                {
                    currentfilterId = oidRes.get(0).toString();
                    filter = filterMap.get(currentfilterId);
                    if(filter != null && currentfilterId.equals(filter.getFilterId().toString()))
                    {
                        filter.setSrcMask(var.getVariable().toString());
                    }
                    else
                    {
                        throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(AlteonFilterSrcIpMask, target:%s, oid:%s)", var.getOid().toString(), workingOid));
                    }
                }
                else
                {
                    throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(AlteonFilterSrcIpMask, target:%s, oid:%s)", var.getOid().toString(), workingOid));
                }
            }   
            // AlteonFilterDstIp
            workingOid = oidMap.get("AlteonFilterDstIp");
            tmpList = walk(workingOid);
            for(i=0;i<tmpList.size();i++)
            {
                VariableBinding var=tmpList.get(i);
                ArrayList<Integer> oidRes = parseOid(var.getOid().toString(), workingOid);
                if(oidRes.size()==1)
                {
                    currentfilterId = oidRes.get(0).toString();
                    filter = filterMap.get(currentfilterId);
                    if(filter != null && currentfilterId.equals(filter.getFilterId().toString()))
                    {
                        filter.setDstIP(var.getVariable().toString());
                    }
                    else
                    {
                        throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(AlteonFilterDstIp, target:%s, oid:%s)", var.getOid().toString(), workingOid));
                    }
                }
                else
                {
                    throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(AlteonFilterDstIp, target:%s, oid:%s)", var.getOid().toString(), workingOid));
                }
            }
            // AlteonFilterDstIPMask
            workingOid = oidMap.get("AlteonFilterDstIPMask");
            tmpList = walk(workingOid);
            for(i=0;i<tmpList.size();i++)
            {
                VariableBinding var=tmpList.get(i);
                ArrayList<Integer> oidRes = parseOid(var.getOid().toString(), workingOid);
                if(oidRes.size()==1)
                {
                    currentfilterId = oidRes.get(0).toString();
                    filter = filterMap.get(currentfilterId);
                    if(filter != null && currentfilterId.equals(filter.getFilterId().toString()))
                    {
                        filter.setDstMask(var.getVariable().toString());
                    }
                    else
                    {
                        throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(AlteonFilterDstIPMask, target:%s, oid:%s)", var.getOid().toString(), workingOid));
                    }
                }
                else
                {
                    throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(AlteonFilterDstIPMask, target:%s, oid:%s)", var.getOid().toString(), workingOid));
                }
            }
            // AlteonFilterProtocol
            workingOid = oidMap.get("AlteonFilterProtocol");
            tmpList = walk(workingOid);
            for(i=0;i<tmpList.size();i++)
            {
                VariableBinding var=tmpList.get(i);
                ArrayList<Integer> oidRes = parseOid(var.getOid().toString(), workingOid);
                if(oidRes.size()==1)
                {
                    currentfilterId = oidRes.get(0).toString();
                    filter = filterMap.get(currentfilterId);
                    if(filter != null && currentfilterId.equals(filter.getFilterId().toString()))
                    {
                        switch (var.getVariable().toInt())
                        {
                            case 0 : filter.setProtocol(OBDefine.PROTOCOL_ALL);  break; //any
                            case 1 : filter.setProtocol(OBDefine.PROTOCOL_ICMP); break; //ICMP
                            case 6 : filter.setProtocol(OBDefine.PROTOCOL_TCP);  break; //TCP
                            case 17: filter.setProtocol(OBDefine.PROTOCOL_UDP);  break; //UDP
                            default: filter.setProtocol(OBDefine.PROTOCOL_ETC);  break; //1~255 까지 icmp(1), tcp(6), udp(17) 이외
                        }
                    }
                    else
                    {
                        throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(AlteonFilterProtocol, target:%s, oid:%s)", var.getOid().toString(), workingOid));
                    }
                }
                else
                {
                    throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(AlteonFilterProtocol, target:%s, oid:%s)", var.getOid().toString(), workingOid));
                }
            }
            // AlteonFilterSrcPortLow
            workingOid = oidMap.get("AlteonFilterSrcPortLow");
            tmpList = walk(workingOid);
            for(i=0;i<tmpList.size();i++)
            {
                VariableBinding var=tmpList.get(i);
                ArrayList<Integer> oidRes = parseOid(var.getOid().toString(), workingOid);
                if(oidRes.size()==1)
                {
                    currentfilterId = oidRes.get(0).toString();
                    filter = filterMap.get(currentfilterId);
                    if(filter != null && currentfilterId.equals(filter.getFilterId().toString()))
                    {
                        filter.setSrcPortFrom(var.getVariable().toInt());
                    }
                    else
                    {
                        throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(AlteonFilterSrcPortHigh, target:%s, oid:%s)", var.getOid().toString(), workingOid));
                    }
                }
                else
                {
                    throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(AlteonFilterSrcPortHigh, target:%s, oid:%s)", var.getOid().toString(), workingOid));
                }
            }
            // AlteonFilterSrcPortHigh
            workingOid = oidMap.get("AlteonFilterSrcPortHigh");
            tmpList = walk(workingOid);
            for(i=0;i<tmpList.size();i++)
            {
                VariableBinding var=tmpList.get(i);
                ArrayList<Integer> oidRes = parseOid(var.getOid().toString(), workingOid);
                if(oidRes.size()==1)
                {
                    currentfilterId = oidRes.get(0).toString();
                    filter = filterMap.get(currentfilterId);
                    if(filter != null && currentfilterId.equals(filter.getFilterId().toString()))
                    {
                        filter.setSrcPortTo(var.getVariable().toInt());
                    }
                    else
                    {
                        throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(AlteonFilterSrcPortHigh, target:%s, oid:%s)", var.getOid().toString(), workingOid));
                    }
                }
                else
                {
                    throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(AlteonFilterSrcPortHigh, target:%s, oid:%s)", var.getOid().toString(), workingOid));
                }
            }
            // AlteonFilterDstPortLow
            workingOid = oidMap.get("AlteonFilterDstPortLow");
            tmpList = walk(workingOid);
            for(i=0;i<tmpList.size();i++)
            {
                VariableBinding var=tmpList.get(i);
                ArrayList<Integer> oidRes = parseOid(var.getOid().toString(), workingOid);
                if(oidRes.size()==1)
                {
                    currentfilterId = oidRes.get(0).toString();
                    filter = filterMap.get(currentfilterId);
                    if(filter != null && currentfilterId.equals(filter.getFilterId().toString()))
                    {
                        filter.setDstPortFrom(var.getVariable().toInt());
                    }
                    else
                    {
                        throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(AlteonFilterDstPortHigh, target:%s, oid:%s)", var.getOid().toString(), workingOid));
                    }
                }
                else
                {
                    throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(AlteonFilterDstPortHigh, target:%s, oid:%s)", var.getOid().toString(), workingOid));
                }
            }
            // AlteonFilterDstPortHigh
            workingOid = oidMap.get("AlteonFilterDstPortHigh");
            tmpList = walk(workingOid);
            for(i=0;i<tmpList.size();i++)
            {
                VariableBinding var=tmpList.get(i);
                ArrayList<Integer> oidRes = parseOid(var.getOid().toString(), workingOid);
                if(oidRes.size()==1)
                {
                    currentfilterId = oidRes.get(0).toString();
                    filter = filterMap.get(currentfilterId);
                    if(filter != null && currentfilterId.equals(filter.getFilterId().toString()))
                    {
                        filter.setDstPortTo(var.getVariable().toInt());
                    }
                    else
                    {
                        throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(AlteonFilterDstPortHigh, target:%s, oid:%s)", var.getOid().toString(), workingOid));
                    }
                }
                else
                {
                    throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(AlteonFilterDstPortHigh, target:%s, oid:%s)", var.getOid().toString(), workingOid));
                }
            }
            //AlteonFilterAction:  allow( 1 ), deny( 2 ), redirect( 3 ), nat( 4 ), goto( 5 )
            workingOid = oidMap.get("AlteonFilterAction");
            tmpList = walk(workingOid);
            for(i=0;i<tmpList.size();i++)
            {
                VariableBinding var=tmpList.get(i);
                ArrayList<Integer> oidRes = parseOid(var.getOid().toString(), workingOid);
                if(oidRes.size()==1)
                {
                    currentfilterId = oidRes.get(0).toString();
                    filter = filterMap.get(currentfilterId);
                    if(filter != null && currentfilterId.equals(filter.getFilterId().toString()))
                    {
                        switch(var.getVariable().toInt())
                        {
                            case 1: filter.setAction("allow");    break;
                            case 2: filter.setAction("deny");     break;
                            case 3: filter.setAction("redirect"); break;
                            case 4: filter.setAction("nat");      break;
                            case 5: filter.setAction("goto");     break;
                        }
                    }
                    else
                    {
                        throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(AlteonFilterAction, target:%s, oid:%s)", var.getOid().toString(), workingOid));
                    }
                }
                else
                {
                    throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(AlteonFilterAction, target:%s, oid:%s)", var.getOid().toString(), workingOid));
                }
            }
            //AlteonFilterRedirectionGroup
            workingOid = oidMap.get("AlteonFilterRedirectionGroup");
            tmpList = walk(workingOid);
            for(i=0;i<tmpList.size();i++)
            {
                VariableBinding var=tmpList.get(i);
                ArrayList<Integer> oidRes = parseOid(var.getOid().toString(), workingOid);
                if(oidRes.size()==1)
                {
                    currentfilterId = oidRes.get(0).toString();
                    filter = filterMap.get(currentfilterId);
                    if(filter != null && currentfilterId.equals(filter.getFilterId().toString()))
                    {
                        filter.setGroup(var.getVariable().toString());
                    }
                    else
                    {
                        throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(AlteonFilterRedirectionGroup, target:%s, oid:%s)", var.getOid().toString(), workingOid));
                    }
                }
                else
                {
                    throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(AlteonFilterRedirectionGroup, target:%s, oid:%s)", var.getOid().toString(), workingOid));
                }
            }
            //Redirection 유형은 fwlb와 thash 중에서 선택한다. 이론상 두개 다 설정할 수 있으므로 둘다 있을 경우까지 처리한다.
            //fwlb는 enable이면 선택됐다고 판단하고, thash는 값에서 상세 옵션을 뽑는다.
            //AlteonFilterRedirectionFwlb
            workingOid = oidMap.get("AlteonFilterRedirectionFwlb");
            tmpList = walk(workingOid);
            for(i=0;i<tmpList.size();i++)
            {
                VariableBinding var=tmpList.get(i);
                ArrayList<Integer> oidRes = parseOid(var.getOid().toString(), workingOid);
                if(oidRes.size()==1)
                {
                    currentfilterId = oidRes.get(0).toString();
                    filter = filterMap.get(currentfilterId);
                    if(filter != null && currentfilterId.equals(filter.getFilterId().toString()))
                    {
                        tmpInt = var.getVariable().toInt();
                        if(tmpInt==1) //fwlb enabled, 1아니면 2인데, 2=disable 
                        {
                            filter.setRedirection("fwlb");
                        }
                    }
                    else
                    {
                        throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(AlteonFilterRedirectionFwlb, target:%s, oid:%s)", var.getOid().toString(), workingOid));
                    }
                }
                else
                {
                    throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(AlteonFilterRedirectionFwlb, target:%s, oid:%s)", var.getOid().toString(), workingOid));
                }
            }
            //AlteonFilterRedirectionTHash
            workingOid = oidMap.get("AlteonFilterRedirectionTHash");
            tmpList = walk(workingOid);
            String thash="";
            //thash는 버전 29.5부터 값 예시가 다르기 때문에 버전을 보고 처리할 준비를 한다.
            String[] version = swVersion.split("\\.", 3);
            int version1=Integer.parseInt(version[0]);
            int version2=Integer.parseInt(version[1]);
            
            for(i=0;i<tmpList.size();i++)
            {
                VariableBinding var=tmpList.get(i);
                ArrayList<Integer> oidRes = parseOid(var.getOid().toString(), workingOid);
                if(oidRes.size()==1)
                {
                    currentfilterId = oidRes.get(0).toString();
                    filter = filterMap.get(currentfilterId);
                    if(filter != null && currentfilterId.equals(filter.getFilterId().toString()))
                    {
                        switch(var.getVariable().toInt())
                        {
                            case 1: 
                                thash = ""; //원래 'thash(auto)'인데, thash(auto)는 default라 표시 안 한다.
                                break; 
                            case 2: 
                                thash = "thash(sip)";
                                break;
                            case 3: 
                                thash = "thash(dip)";
                                break;
                            case 4: 
                                thash = "thash(both)";
                                break;
                            case 5:
                                if(version1>=29 && version2>=5) //29.5 이상일때
                                {
                                    thash = "thash(sipsport)";
                                }
                                else //29.4까지
                                {
                                    thash = "thash(dipd32)"; //5는 원래 sipsport 값이다. 29.4까지는 dip32도 sipsport로 나오는 snmp 문제가 있어, 거의 쓰지않는 sipsport를 포기한다.
                                }
                                break;
                            case 6:
                                if(version1>=29 && version2>=5) //29.5 이상일때, 29.4까지는 6이 없다.
                                {
                                    thash = "thash(dipd32)";
                                }
                                break;
                        }

                        if(thash!=null && thash.isEmpty()==false)
                        {
                            if(filter.getRedirection()!=null && filter.getRedirection().isEmpty()==false) //이미 redirection이 있으면(사실상 fwlb가 있으면)
                            {
                                filter.setRedirection(filter.getRedirection() + "/" + thash);
                            }
                            else
                            {
                                filter.setRedirection(thash);
                            }
                        }
                    }
                    else
                    {
                        throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(AlteonFilterRedirectionTHash, target:%s, oid:%s)", var.getOid().toString(), workingOid));
                    }
                }
                else
                {
                    throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(AlteonFilterRedirectionTHash, target:%s, oid:%s)", var.getOid().toString(), workingOid));
                }
            }
            filters.addAll(filterMap.values()); //HashMap에 있는 filter들을 싹 모아서 ArrayList로 만듦, 리턴할 데이터.
            OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result(filters):%s", filters));
            return filters;
        }
        catch(OBException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
        }
    }
    
    private ArrayList<DtoSnmpFilterPortAlteon> getFilterPhysicalPortList(ArrayList<DtoOidEntity> oidList) throws OBException
    {
        OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. oidList:%s", oidList));
        String workingOid; //walk 작업중인 oid
        try
        {
            //OID ArrayList를 hashmap으로 바꾼다.
            HashMap<String, String> oidMap= new HashMap<String, String>();
            for (DtoOidEntity oid: oidList)
            {
                oidMap.put(oid.getName(), oid.getValue());
            }

            //결과값 관리
            HashMap<Integer, DtoSnmpFilterPortAlteon> portMap= new HashMap<Integer, DtoSnmpFilterPortAlteon>(); //처리할 때 결과 모으는 hasp map
            ArrayList<DtoSnmpFilterPortAlteon> physicalPorts = new ArrayList<DtoSnmpFilterPortAlteon>(); //마지막에 이걸로 리턴

            List<VariableBinding> tmpList;
            Integer portState = 0;
            Integer portId;
            int portNum, i;
        
            // AlteonFilterEnabledPort - filter를 enable한 포트만 뽑는다.
            workingOid = oidMap.get("AlteonFilterEnabledPort");
            tmpList = walk(workingOid);
            for(i=0;i<tmpList.size();i++)
            {
                VariableBinding var=tmpList.get(i);
                ArrayList<Integer> oidRes = parseOid(var.getOid().toString(), workingOid);
                if(oidRes.size()==1)
                {
                    portId = Integer.valueOf(oidRes.get(0).toString());
                    portState = var.getVariable().toInt();
                    if(portId!=null && portState!=null && portState==1) //port 상태가 enable이면 유효하다.
                    {
                        DtoSnmpFilterPortAlteon port = new DtoSnmpFilterPortAlteon();
                        port.setPhysicalPortIndex(portId);
                        port.setFilterIndexList(new ArrayList<Integer>());
                        portMap.put(portId, port);
                    }
                }
                else
                {
                    throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(AlteonFilterEnabledPort, target:%s, oid:%s)", 
                            var.getOid().toString(), workingOid));
                }
            }
            portNum = portMap.size();
            if(portNum==0)
            {
                return physicalPorts; //빈 ArrayList를 return한다.
            }

            // AlteonFilterPortMatching, filter id bitmap 샘플:"00:6f:80:00:00:00:00:00:00:00:00:00:10:00:00:00:00:00:00:00:00:00:00:00... 256바이트"
            workingOid = oidMap.get("AlteonFilterPortMatching");
            tmpList = walk(workingOid);
            DtoSnmpFilterPortAlteon tmpPort;
            String tmpBitMap="";
            for(i=0;i<tmpList.size();i++)
            {
                VariableBinding var=tmpList.get(i);
                ArrayList<Integer> oidRes = parseOid(var.getOid().toString(), workingOid);
                if(oidRes.size()==1)
                {
                    portId = Integer.valueOf(oidRes.get(0).toString());
                    tmpPort = portMap.get(portId);
                    if(tmpPort != null && portId.equals(tmpPort.getPhysicalPortIndex())) //enabled port일 때만
                    {
                        String [] tmpHex = var.getVariable().toString().split(":"); //':'이 구분자
                        tmpBitMap = "";
                        for(int j=0; j<tmpHex.length; j++)
                        {
                            tmpBitMap += OBParser.hexByteToBinary(tmpHex[j]);
                        }
                        char [] filterBitMap = tmpBitMap.toCharArray();
                        for(int j=0;j<filterBitMap.length; j++) //bit map에서 filter들을 뽑아서 넣는다. 인덱스가 필터번호인데 1부터 시작이다.
                        {
                            if(filterBitMap[j]=='1')
                            {
                                tmpPort.getFilterIndexList().add(j+1);
                            }
                        }
                    }
                }
                else
                {
                    throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(AlteonFilterPortMatching, target:%s, oid:%s)", var.getOid().toString(), workingOid));
                }
            }
            physicalPorts.addAll(portMap.values()); //HashMap에 있는 filter들을 싹 모아서 ArrayList로 만듦, 리턴할 데이터.
            OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result(physicalPorts):%s", physicalPorts));
            return physicalPorts;
        }
        catch(OBException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
        }
    }
    
    private OBDtoAdcAdditional getAdditionalInfo(Integer adcIndex, DtoOidCfgAdcAdditional oid) throws OBException
    {
        Snmp snmp = openSnmp();
        try
        {
            OBDtoAdcAdditional result = new OBDtoAdcAdditional();
            result.setAdcIndex(adcIndex);
            
            List<VariableBinding> tmpList;
            
            String reqOid="";
            
            // confSyncState 추출.
            if(oid.getConfSyncState()!=null)
            {
                tmpList = walk(oid.getConfSyncState());
                // 첫번째 데이터만 분석한다.
                for(VariableBinding var:tmpList)
                {
                    result.setSyncState(convertGeneralState(var.getVariable().toInt()));
                    break;
                }
            }

            // confSyncPeerIP츨 추출한다.
            if(oid.getConfSyncPeerIP()!=null)
            {
                tmpList = walk(oid.getConfSyncPeerIP());
                // 첫번째 데이터만 분석한다.
                for(VariableBinding var:tmpList)
                {
                    result.setPeerIP(var.getVariable().toString());
                    break;
                }
            }
            
            int vrrpID=0;
            // enabled된 첫번째 vrrp id 추출.
            if(oid.getVrrpState()!=null)
            {
                tmpList = walk(oid.getVrrpState());
                // 첫번째 데이터만 분석한다.
                for(VariableBinding var:tmpList)
                {
                    if(convertGeneralState(var.getVariable().toInt())==OBDefine.STATE_ENABLE)
                    {
                        ArrayList<Integer> oidList = parseOid(var.getOid().toString(), oid.getVrrpState());
                        if(oidList.size()>0)
                        {
                            vrrpID=oidList.get(0);
                            break;
                        }
                    }
                }
            }
            
//          OBSystemLog.debug(OBDefine.LOGFILE_SYSTEM, String.format("vrrpID:%d\n", vrrpID));

            if(oid.getVrrpPriority()!=null)
            {
                if(vrrpID!=0)
                {
                    Object obj;
                    reqOid=oid.getVrrpPriority()+"."+vrrpID;
//                  OBSystemLog.debug(OBDefine.LOGFILE_SYSTEM, String.format("11111111111\n"));
                    if(reqOid!=null)
                    {
                        obj = getSnmp(snmp, reqOid);
                        if((checkObject(obj)) && (OBParser.isNumeric(obj.toString())))
                        {
//                          OBSystemLog.debug(OBDefine.LOGFILE_SYSTEM, String.format("2222222222222:%s\n", obj.toString()));
                            result.setVrrpPriority(Integer.parseInt(obj.toString()));
                        }
                        else
                        {
                            OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to get vrrp priority. ip:%s, oid:%s", this.getHost(), reqOid));
                        }
                    }
                }
                else
                {
//                  OBSystemLog.debug(OBDefine.LOGFILE_SYSTEM, String.format("33333333\n"));
                    tmpList = walk(oid.getVrrpPriority());
                    // 첫번째 데이터만 분석한다.
                    for(VariableBinding var:tmpList)
                    {
                        result.setVrrpPriority(var.getVariable().toInt());
                        break;
                    }
                }
            }
            
            // virtRtrSharing 추출.
            if(oid.getVrrpShare()!=null)
            {
                if(vrrpID!=0)
                {
                    Object obj;
                    reqOid=oid.getVrrpShare()+"."+vrrpID;
                    if(reqOid!=null)
                    {
                        obj = getSnmp(snmp, reqOid);
                        if((checkObject(obj)) && (OBParser.isNumeric(obj.toString())))
                            result.setVrrpShare(convertGeneralState(Integer.parseInt(obj.toString())));
                        else
                        {
                            OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to get getVrrpShare. ip:%s, oid:%s", this.getHost(), reqOid));
                        }
                    }
                }
                else
                {               
                    tmpList = walk(oid.getVrrpShare());
                    // 첫번째 데이터만 분석한다.
                    for(VariableBinding var:tmpList)
                    {
                        result.setVrrpShare(convertGeneralState(var.getVariable().toInt()));
                        break;
                    }
                }
            }
            
            // tckVrs 추출. 
            if(oid.getVrrpTrackVrs()!=null)
            {
                if(vrrpID!=0)
                {
                    Object obj;
                    reqOid=oid.getVrrpTrackVrs()+"."+vrrpID;
                    if(reqOid!=null)
                    {
                        obj = getSnmp(snmp, reqOid);
                        if((checkObject(obj)) && (OBParser.isNumeric(obj.toString())))
                            result.setVrrpTrackVrs(convertGeneralState(Integer.parseInt(obj.toString())));
                        else
                        {
                            OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to get getVrrpTrackVrs. ip:%s, oid:%s", this.getHost(), reqOid));
                        }
                    }
                }
                else
                {                   
                    tmpList = walk(oid.getVrrpTrackVrs());
                    // 첫번째 데이터만 분석한다.
                    for(VariableBinding var:tmpList)
                    {
                        result.setVrrpTrackVrs(convertGeneralState(var.getVariable().toInt()));
                        break;
                    }
                }
            }
            
            
            // virtRtrTckHsrp 추출.
            if(oid.getVrrpTrackHsrp()!=null)
            {
                if(vrrpID!=0)
                {
                    Object obj;
                    reqOid=oid.getVrrpTrackHsrp()+"."+vrrpID;
                    if(reqOid!=null)
                    {
                        obj = getSnmp(snmp, reqOid);
                        if((checkObject(obj)) && (OBParser.isNumeric(obj.toString())))
                            result.setVrrpTrackHsrp(convertGeneralState(Integer.parseInt(obj.toString())));
                        else
                        {
                            OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to get getVrrpTrackHsrp. ip:%s, oid:%s", this.getHost(), reqOid));
                        }
                    }
                }
                else
                {                   
                    tmpList = walk(oid.getVrrpTrackHsrp());
                    // 첫번째 데이터만 분석한다.
                    for(VariableBinding var:tmpList)
                    {
                        result.setVrrpTrackHsrp(convertGeneralState(var.getVariable().toInt()));
                        break;
                    }
                }
            }   
            
            // virtRtrTckHsrv 추출.
            if(oid.getVrrpTrackHsrv()!=null)
            {
                if(vrrpID!=0)
                {
                    Object obj;
                    reqOid=oid.getVrrpTrackHsrv()+"."+vrrpID;
                    if(reqOid!=null)
                    {
                        obj = getSnmp(snmp, reqOid);
                        if((checkObject(obj)) && (OBParser.isNumeric(obj.toString())))
                            result.setVrrpTrackHsrv(convertGeneralState(Integer.parseInt(obj.toString())));
                        else
                        {
                            OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to get getVrrpTrackHsrv. ip:%s, oid:%s", this.getHost(), reqOid));
                        }
                    }
                }
                else
                {               
                    tmpList = walk(oid.getVrrpTrackHsrv());
                    // 첫번째 데이터만 분석한다.
                    for(VariableBinding var:tmpList)
                    {
                        result.setVrrpTrackHsrv(convertGeneralState(var.getVariable().toInt()));
                        break;
                    }
                }
            }
            
            // virtRtrTckIpIntf 추출.
            if(oid.getVrrpTrackIfs()!=null)
            {
                if(vrrpID!=0)
                {
                    Object obj;
                    reqOid=oid.getVrrpTrackIfs()+"."+vrrpID;
                    if(reqOid!=null)
                    {
                        obj = getSnmp(snmp, reqOid);
                        if((checkObject(obj)) && (OBParser.isNumeric(obj.toString())))
                            result.setVrrpTrackIfs(convertGeneralState(Integer.parseInt(obj.toString())));
                        else
                        {
                            OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to get getVrrpTrackIfs. ip:%s, oid:%s", this.getHost(), reqOid));
                        }
                    }
                }
                else
                {                   
                    tmpList = walk(oid.getVrrpTrackIfs());
                    // 첫번째 데이터만 분석한다.
                    for(VariableBinding var:tmpList)
                    {
                        result.setVrrpTrackIfs(convertGeneralState(var.getVariable().toInt()));
                        break;
                    }
                }
            }
            
            // virtRtrTckL4Port 추출.
            if(oid.getVrrpTrackL4pts()!=null)
            {
                if(vrrpID!=0)
                {
                    Object obj;
                    reqOid=oid.getVrrpTrackL4pts()+"."+vrrpID;
                    if(reqOid!=null)
                    {
                        obj = getSnmp(snmp, reqOid);
                        if((checkObject(obj)) && (OBParser.isNumeric(obj.toString())))
                            result.setVrrpTrackL4pts(convertGeneralState(Integer.parseInt(obj.toString())));
                        else
                        {
                            OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to get virtRtrTckL4Port. ip:%s, oid:%s", this.getHost(), reqOid));
                        }
                    }
                }
                else
                {   
                    tmpList = walk(oid.getVrrpTrackL4pts());
                    // 첫번째 데이터만 분석한다.
                    for(VariableBinding var:tmpList)
                    {
                        result.setVrrpTrackL4pts(convertGeneralState(var.getVariable().toInt()));
                        break;
                    }
                }
            }
            
            // virtRtrTckRServer 추출.
            if(oid.getVrrpTrackReals()!=null)
            {
                if(vrrpID!=0)
                {
                    Object obj;
                    reqOid=oid.getVrrpTrackReals()+"."+vrrpID;
                    if(reqOid!=null)
                    {
                        obj = getSnmp(snmp, reqOid);
                        if((checkObject(obj)) && (OBParser.isNumeric(obj.toString())))
                            result.setVrrpTrackReals(convertGeneralState(Integer.parseInt(obj.toString())));
                        else
                        {
                            OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to get virtRtrTckRServer. ip:%s, oid:%s", this.getHost(), reqOid));
                        }
                    }
                }
                else
                {
                    tmpList = walk(oid.getVrrpTrackReals());
                    // 첫번째 데이터만 분석한다.
                    for(VariableBinding var:tmpList)
                    {
                        result.setVrrpTrackReals(convertGeneralState(var.getVariable().toInt()));
                        break;
                    }
                }
            }
            
            // virtRtrTckVlanPort추출.
            if(oid.getVrrpTrackPorts()!=null)
            {
                if(vrrpID!=0)
                {
                    Object obj;
                    reqOid=oid.getVrrpTrackPorts()+"."+vrrpID;
                    if(reqOid!=null)
                    {
                        obj = getSnmp(snmp, reqOid);
                        if((checkObject(obj)) && (OBParser.isNumeric(obj.toString())))
                            result.setVrrpTrackPorts(convertGeneralState(Integer.parseInt(obj.toString())));
                        else
                        {
                            OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to get virtRtrTckRServer. ip:%s, oid:%s", this.getHost(), reqOid));
                        }
                    }
                }
                else
                {
                    tmpList = walk(oid.getVrrpTrackPorts());
                    // 첫번째 데이터만 분석한다.
                    for(VariableBinding var:tmpList)
                    {
                        result.setVrrpTrackPorts(convertGeneralState(var.getVariable().toInt()));
                        break;
                    }
                }
            }   
            closeSnmp(snmp);
//          OBSystemLog.debug(OBDefine.LOGFILE_SYSTEM, String.format("result:%s\n", result));
            return result;
        }
        catch(OBException e)
        {
            closeSnmp(snmp);
            throw e;
        }
        catch(Exception e)
        {
            closeSnmp(snmp);
            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());

        }
        
    }
    
    private Integer convertGeneralState(Integer state)
    {
        switch(state)
        {
        case 1:
            return OBDefine.STATE_ENABLE;
        case 2:
            return OBDefine.STATE_DISABLE;
        default:
            return OBDefine.STATE_DISABLE;
        }
    }
    
    
    private ArrayList<DtoSnmpNetworkClassAlteon> getNetworkClassList(DtoOidInfoNetworkClass oid) throws OBException
    {
        int i,j;
        String tempBuff; //temp string
        List<VariableBinding> tmpList;
        int ncNum = 0; //network class 수
        ArrayList<DtoSnmpNetworkClassAlteon>  ncList = new ArrayList<DtoSnmpNetworkClassAlteon>();
        
        try
        {
            OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. host:%s, oid:%s", this.getHost(), oid)); 
            // network class 기본정보 - ID, name.  기본 정보가 없으면 상세 entry는 당연히 없다
            if(oid.getSlbNwClassBase()!=null && oid.getSlbNwClassBase().isEmpty()==false)
            {
                tmpList = walk(oid.getSlbNwClassBase());
                for(i=0;i<tmpList.size();i++)
                {
                    VariableBinding var=tmpList.get(i);
                    if(var.getOid().toString().startsWith(oid.getSlbNwClassBaseID()+".")) //networkclass id 엔트리다.
                    {
                        tempBuff = var.getVariable().toString();                        
                        if(tempBuff!=null && tempBuff.isEmpty()==false) //id가 있으면 list에 넣는다.
                        {
                            DtoSnmpNetworkClassAlteon nc = new DtoSnmpNetworkClassAlteon();
                            nc.setId(tempBuff);
                            nc.setNetList(new ArrayList<DtoSnmpNetworkClassNetAlteon>()); //nwclass에 속한 network 목록
                            ncList.add(nc);
                        }
                    }
                } //한바퀴 돌면서 id를 모두 챙겼음
                ncNum = ncList.size();
                for(i=0;i<tmpList.size();i++) //다시 돌면서 이름을 가져옴
                {
                    VariableBinding var=tmpList.get(i);
                    if(var.getOid().toString().startsWith(oid.getSlbNwClassBaseName()+".")) //networkclass Name 엔트리다.
                    {
                        tempBuff= parseOidName(var.getOid().toString(), oid.getSlbNwClassBaseName());
                        if(tempBuff!=null) 
                        {
                            for(DtoSnmpNetworkClassAlteon nc:ncList)
                            {
                                if(nc.getId().equals(tempBuff)) //id가 같으면
                                {
                                    nc.setName(var.getVariable().toString());
                                    break;
                                }
                            }
                        }
                    }
                } //이름도 넣음, nwclass 목록 구성 끝
                if(ncList.size()==0)
                {
                    OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. host:%s, result:healthcheck list empty", this.getHost())); 
                    return ncList;
                }
                String additionalOid;
                String tempOid;
                //이제 각 nc의 상세정보인 element들을 구한다.
                if(oid.getSlbNwClassElement()!=null && oid.getSlbNwClassElement().isEmpty()==false)
                {
                    tmpList = walk(oid.getSlbNwClassElement());
                    for(i=0; i<ncNum; i++)
                    {
                        additionalOid = makeOidTail(new String [] {ncList.get(i).getId()}); //item을 oid형식으로 변환한다. input:{"55"} --> output: "2.53.53"
                        if(additionalOid == null)
                        {
                            continue; //오류난 것은 건너뛴다.
                        }
                        for(j=0; j<tmpList.size(); j++)
                        {
                            VariableBinding var=tmpList.get(j);
                            tempOid = String.format("%s.%s.", oid.getSlbNwClassElementNetId(), additionalOid);
                            if(var.getOid().toString().startsWith(tempOid)) //net id 엔트리다.
                            {
                                DtoSnmpNetworkClassNetAlteon net = new DtoSnmpNetworkClassNetAlteon();
                                net.setNcId(ncList.get(i).getId()); //parent인 nwclass id
                                net.setNetId(var.getVariable().toString()); //자기(net) id
                                ncList.get(i).getNetList().add(net);
                            }
                        }
                    } //nwclass에 속하는 network들을 골라 해당 nwclass에 배치완료

                    for(i=0; i<ncNum; i++)
                    {
                        for(DtoSnmpNetworkClassNetAlteon net:ncList.get(i).getNetList()) //nwclass별로 소속 net들을 돈다.
                        {
                            additionalOid = makeOidTail(new String [] {ncList.get(i).getId(), net.getNetId()}); //item을 oid형식으로 변환한다. 
                            if(additionalOid == null)
                            {
                                continue;
                            }
                            for(j=0; j<tmpList.size(); j++)
                            {
                                VariableBinding var=tmpList.get(j);
                                tempOid = String.format("%s.%s", oid.getSlbNwClassElementNetType(), additionalOid); //마지막 값까지 구성해서 dot이 끝에 없어도 된다.
                                if(var.getOid().toString().startsWith(tempOid)) //net type 
                                {
                                    net.setNetType(var.getVariable().toString());
                                }   
                                tempOid = String.format("%s.%s", oid.getSlbNwClassElementIpSingle(), additionalOid); //마지막 값까지 구성해서 dot이 끝에 없어도 된다.
                                if(var.getOid().toString().startsWith(tempOid)) //ip - single인 경우
                                {
                                    net.setIpSingle(var.getVariable().toString());
                                }   
                            }
                        }
                    }
                }
            }
                        
            OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end."));
            return ncList;
        }
        catch(OBException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());

        }
    }
    
    private ArrayList<DtoSnmpHealthcheckAlteon> getHealthcheckList(DtoOidInfoHealthCheck oid) throws OBException
    {
        int i,j;
        String tempBuff; //temp string
        List<VariableBinding> tmpList;
        int hcNum = 0; //healthcheck 수
        try
        {
            OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. host:%s, oid:%s", this.getHost(), oid)); 
            ArrayList<DtoSnmpHealthcheckAlteon>  hcList = new ArrayList<DtoSnmpHealthcheckAlteon>();

            // healthcheck ID
            if(oid.getSlbHealthID()!=null && oid.getSlbHealthID().isEmpty()==false)
            {
                tmpList = walk(oid.getSlbHealthID());
                for(i=0;i<tmpList.size();i++)
                {
                    VariableBinding var=tmpList.get(i);
                    tempBuff = var.getVariable().toString();
                    //tempBuff = parseOidName(var.getOid().toString(), oid.getSlbHealthID());
                    if(tempBuff!=null)
                    {
                        DtoSnmpHealthcheckAlteon hc = new DtoSnmpHealthcheckAlteon();
                        hc.setId(tempBuff);
                        hcList.add(hc);
                    }
                    else
                    {
                        throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(target:%s, healthcheck ID:%s)", var.getOid().toString(), oid.getSlbHealthID()));
                    }
                }
            }
            if(hcList.size()==0)
            {
                OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. host:%s, result:healthcheck list empty", this.getHost())); 
                return hcList;
            }
            hcNum = hcList.size(); //healthcheck 수

            //healthcheck name
            if(oid.getSlbHealthName()!=null && oid.getSlbHealthName().isEmpty()==false)
            {   
                tmpList = walk(oid.getSlbHealthName());
                for(i=0;i<tmpList.size();i++)
                {
                    VariableBinding var=tmpList.get(i);
                    tempBuff = parseOidName(var.getOid().toString(), oid.getSlbHealthName());
                    
                    if(tempBuff!=null)
                    {
                        for(j=0; j<hcNum; j++)
                        {
                            if(tempBuff.equals(hcList.get(j).getId())==true) //health check id가 같으면
                            {
                                hcList.get(j).setName(var.getVariable().toString());
                                break;
                            }
                        }
                    }
    //              else
    //              {
    //                  throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(target:%s, poolID:%s)", var.getOid().toString(), oid.getPoolID()));
    //              }
                }
            }
            //healthcheck type
            if(oid.getSlbHealthType()!=null && oid.getSlbHealthType().isEmpty()==false)
            {
                tmpList = walk(oid.getSlbHealthType());
                for(i=0;i<tmpList.size();i++)
                {
                    VariableBinding var=tmpList.get(i);
                    tempBuff = parseOidName(var.getOid().toString(), oid.getSlbHealthType());
                    
                    if(tempBuff!=null)
                    {
                        for(j=0; j<hcNum; j++)
                        {
                            if(tempBuff.equals(hcList.get(j).getId())==true) //health check id가 같으면
                            {
                                hcList.get(j).setType(var.getVariable().toString());
                                break;
                            }
                        }
                    }
    //              else
    //              {
    //                  throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(target:%s, poolID:%s)", var.getOid().toString(), oid.getPoolID()));
    //              }
                }
            }
            //healthcheck destination IP
            if(oid.getSlbHealthDestinationIp()!=null && oid.getSlbHealthDestinationIp().isEmpty()==false)
            {
                tmpList = walk(oid.getSlbHealthDestinationIp());
                for(i=0;i<tmpList.size();i++)
                {
                    VariableBinding var=tmpList.get(i);
                    tempBuff = parseOidName(var.getOid().toString(), oid.getSlbHealthDestinationIp());
                    
                    if(tempBuff!=null)
                    {
                        for(j=0; j<hcNum; j++)
                        {
                            if(tempBuff.equals(hcList.get(j).getId())==true) //health check id가 같으면
                            {
                                hcList.get(j).setDestinationIp(var.getVariable().toString());
                                break;
                            }
                        }
                    }
    //              else
    //              {
    //                  throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(target:%s, poolID:%s)", var.getOid().toString(), oid.getPoolID()));
    //              }
                }
            }
//          //healthcheck logical expr
//          if(oid.getSlbHealthLogicalExpression()!=null && oid.getSlbHealthLogicalExpression().isEmpty()==false)
//          {
//  //          tmpList = walk(oid.getSlbHealthLogicalExpression());
//  //          for(i=0;i<tmpList.size();i++)
//  //          {
//  //              VariableBinding var=tmpList.get(i);
//  //              tempBuff = parseOidName(var.getOid().toString(), oid.getSlbHealthLogicalExpression());
//  //              
//  //              if(tempBuff!=null)
//  //              {
//  //                  for(j=0; j<hcNum; j++)
//  //                  {
//  //                      if(tempBuff.equals(hcList.get(j).getId())==true && hcList.get(j).getType().equals("25")) //health check id가 같고, type= logical expression이면
//  //                      {
//  //                          hcList.get(j).setDestinationIp(var.getVariable().toString());
////                                break;
//  //                      }
//  //                  }
//  //              }
//  ////                else
//  ////                {
//  ////                    throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(target:%s, poolID:%s)", var.getOid().toString(), oid.getPoolID()));
//  ////                }
//  //          }
//          }
            OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end."));
            OBSystemLog.info(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", hcList));
            return hcList;
        }
        catch(OBException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());

        }
    }
    
    private ArrayList<DtoSnmpVirtServiceAlteon> getVirtServiceList(DtoOidCfgVirtService oid) throws OBException
    {
        try
        {
            OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. oid:%s", oid));
            ArrayList<DtoSnmpVirtServiceAlteon> serviceList = new ArrayList<DtoSnmpVirtServiceAlteon>();
            
            List<VariableBinding> tmpList;
            
            // virtual server ID 추출
            tmpList = walk(oid.getVsrvID());
            for(int i=0;i<tmpList.size();i++)
            {
                VariableBinding var=tmpList.get(i);
                ArrayList<String> oidList = parseOidStringV295(var.getOid().toString(), oid.getVsrvID());
                if(oidList.size()!=2)
                {
                    throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(target:%s, vsrvID:%s)", var.getOid().toString(), oid.getVsrvID()));
                }
                String vsID=oidList.get(0);
                Integer vsrvIndex=Integer.parseInt(oidList.get(1));
                
                if(vsID!=null&&vsrvIndex!=null)
                {
                    DtoSnmpVirtServiceAlteon vsrv = new DtoSnmpVirtServiceAlteon();
                    vsrv.setVsID(vsID);
                    vsrv.setVsrvIndex(vsrvIndex);
                    serviceList.add(vsrv);
                }
                else
                {
                    throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(target:%s, vsrvID:%s)", var.getOid().toString(), oid.getVsrvID()));
                }
            }
            if(serviceList.size()==0)
            {
                OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", serviceList));
                return serviceList;
            }
            
            OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("after service:%d", serviceList.size()));       
            // pool id 추출.
            tmpList = walk(oid.getVsrvPoolID(), serviceList.size(), 3);
//          if(tmpList.size() != serviceList.size())
//          {
//              throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("parse error.(size(%d/%d)", tmpList.size(), serviceList.size()));
//          }
            //TODO
            for(int i=0;i<tmpList.size();i++)
            {
                VariableBinding var=tmpList.get(i);
                ArrayList<String> oidList = parseOidStringV295(var.getOid().toString(), oid.getVsrvPoolID());
                if(oidList.size()!=2)
                {
                    throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(target:%s, vsrvID:%s)", var.getOid().toString(), oid.getVsrvID()));
                }
                String vsID=oidList.get(0);
                Integer vsrvIndex=Integer.parseInt(oidList.get(1));
                
                if((vsID==null) ||(vsrvIndex==null))
                {
                    throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(target:%s, vsrvID:%s)", var.getOid().toString(), oid.getVsrvID()));
                }
                
                if(vsID.equals(serviceList.get(i).getVsID()) && vsrvIndex.equals(serviceList.get(i).getVsrvIndex()))
                {
                    serviceList.get(i).setPoolID(var.getVariable().toString());
                }
                else
                {
                    throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(target:%s, vsrvID:%s)", var.getOid().toString(), oid.getVsrvID()));
                }
            }       
            //TODO
            OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("before real port"));       
            // real port 추출.
            tmpList = walk(oid.getVsrvRealPort(), serviceList.size(), 3);
//          if(tmpList.size() != serviceList.size())
//          {
//              throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("parse error.(size(%d/%d)", tmpList.size(), serviceList.size()));
//          }
            for(int i=0;i<tmpList.size();i++)
            {
                VariableBinding var=tmpList.get(i);
                ArrayList<String> oidList = parseOidStringV295(var.getOid().toString(), oid.getVsrvRealPort());
                if(oidList.size()!=2)
                {
                    throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(target:%s, vsrvID:%s)", var.getOid().toString(), oid.getVsrvID()));
                }
                String vsID=oidList.get(0);
                Integer vsrvIndex=Integer.parseInt(oidList.get(1));
                
                if((vsID==null) ||(vsrvIndex==null))
                {
                    throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(target:%s, vsrvID:%s)", var.getOid().toString(), oid.getVsrvID()));
                }
                
                if(vsID.equals(serviceList.get(i).getVsID()) && vsrvIndex.equals(serviceList.get(i).getVsrvIndex()))
                {
                        serviceList.get(i).setVsrvRealPort(var.getVariable().toInt());
                }
                else
                {
                    throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(target:%s, vsrvID:%s)", var.getOid().toString(), oid.getVsrvID()));
                }
            }   
            
            OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("before virt port"));       
            // virt port 추출.
            tmpList = walk(oid.getVsrvVirtPort(), serviceList.size(), 3);
//          if(tmpList.size() != serviceList.size())
//          {
//              throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("parse error.(size(%d/%d)", tmpList.size(), serviceList.size()));
//          }
            for(int i=0;i<tmpList.size();i++)
            {
                VariableBinding var=tmpList.get(i);
                ArrayList<String> oidList = parseOidStringV295(var.getOid().toString(), oid.getVsrvVirtPort());
                if(oidList.size()!=2)
                {
                    throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(target:%s, vsrvID:%s)", var.getOid().toString(), oid.getVsrvID()));
                }
                String vsID=oidList.get(0);
                Integer vsrvIndex=Integer.parseInt(oidList.get(1));
                
                if((vsID==null) ||(vsrvIndex==null))
                {
                    throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(target:%s, vsrvID:%s)", var.getOid().toString(), oid.getVsrvID()));
                }
                
                if(vsID.equals(serviceList.get(i).getVsID()) && vsrvIndex.equals(serviceList.get(i).getVsrvIndex()))
                {
                    serviceList.get(i).setVsrvVirtPort(var.getVariable().toInt());
                }
                else
                {
                    throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(target:%s, vsrvID:%s)", var.getOid().toString(), oid.getVsrvID()));
                }
            }   
            
            OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("before status"));      
            // status는 한번에 업데이트하도록 한다. 여기서는 default 값으로 disabled상태로 설정한다.
//          int srvIndex = 0;
            for(DtoSnmpVirtServiceAlteon srv:serviceList)
            {
//              srvIndex++;
                srv.setStatus(OBDefine.STATUS_DISABLE);//getVSStatus(srv.getVsID(), srvIndex, oid.getVsrvStatus()));
            }
            OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", serviceList));
            return serviceList;
        }
        catch(OBException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());

        }
    }
    
    private ArrayList<DtoSnmpVirtServerAlteon> getVirtServerList(DtoOidCfgVirtServer oid) throws OBException
    {
        try
        {
            OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. oid:%s", oid));
            ArrayList<DtoSnmpVirtServerAlteon> vsList = new ArrayList<DtoSnmpVirtServerAlteon>();
            
            List<VariableBinding> tmpList;
            int i, j;
            int vsNum;
            // virtual server ID 추출
            tmpList = walk(oid.getVsID());
            for(i=0;i<tmpList.size();i++)
            {
                VariableBinding var=tmpList.get(i);
                String vsID=var.getVariable().toString();
                if(vsID!=null)
                {
                    DtoSnmpVirtServerAlteon vs = new DtoSnmpVirtServerAlteon();
                    vs.setVsID(vsID);
                    vs.setVsStatus(OBDefine.STATUS_DISABLE);
                    vs.setVsState1(OBDefine.STATE_DISABLE);
                    vs.setVsName("");
                    vs.setVsNwClassList(new ArrayList<String>()); //network class는 복수일 수 있다.
                    vsList.add(vs);
                }
                else
                {
                    throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(target:%s, poolID:%s)", var.getOid().toString(), oid.getVsID()));
                }
            }   
            
            if(vsList.size()==0)
            {
                return vsList;
            }
            
            vsNum = vsList.size();
            // vs status는 한꺼번에 업데이트하도록 한다. 여기서는 default 값으로만 설정한다.
            for(DtoSnmpVirtServerAlteon vsInfo:vsList)
            {
                vsInfo.setVsStatus(OBDefine.STATUS_DISABLE);
            }       
            
            // ip 주소 추출.
            tmpList = walk(oid.getVsIpAddress());
//          if(tmpList.size() != vsList.size())
//          {
//              throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("parse error.(size(%d/%d)", tmpList.size(), vsList.size()));
//          }
            //TODO
            for(i=0;i<tmpList.size();i++)
            {
                VariableBinding var=tmpList.get(i);
                ArrayList<String> oidList = parseOidStringV295(var.getOid().toString(), oid.getVsIpAddress());
                if(oidList.size()==1)//for tests
                {
                    if(oidList.get(0).equals(vsList.get(i).getVsID()))
                    {
                        
                        vsList.get(i).setVsIpAddress(var.getVariable().toString());
                    }
                    else
                    {
                        throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(target:%s, vsID:%s)", var.getOid().toString(), oid.getVsID()));
                    }
                }
                else
                {
                    throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(target:%s, poolID:%s)", var.getOid().toString(), oid.getVsID()));
                }
            }
            
            // 이름 추출..
            tmpList = walk(oid.getVsName()); // 버전에 따라 데이터 추출이 없을 수 있다.
//          if(tmpList.size() != vsList.size())
//          {
//              throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("parse error.(size(%d/%d)", tmpList.size(), vsList.size()));
//          }
            for(i=0;i<tmpList.size();i++)
            {
                VariableBinding var=tmpList.get(i);
                ArrayList<String> oidList = parseOidStringV295(var.getOid().toString(), oid.getVsName());
                if(oidList.size()==1)
                {
                    if(oidList.get(0).equals(vsList.get(i).getVsID()))
                    {
                        
                        vsList.get(i).setVsName(alteonSnmpHexStringDecode(var.getVariable().toString()));
                    }
                    else
                    {
                        throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(target:%s, vsID:%s)", var.getOid().toString(), oid.getVsID()));
                    }
                }
                else
                {
                    throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(target:%s, poolID:%s)", var.getOid().toString(), oid.getVsID()));
                }
            }
            
            // state 추출.
            tmpList = walk(oid.getVsState2());
//          if(tmpList.size() != vsList.size())
//          {
//              throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("parse error.(size(%d/%d)", tmpList.size(), vsList.size()));
//          }
            for(i=0;i<tmpList.size();i++)
            {
                VariableBinding var=tmpList.get(i);
                ArrayList<String> oidList = parseOidStringV295(var.getOid().toString(), oid.getVsState2());
                if(oidList.size()==1)
                {
                    if(oidList.get(0).equals(vsList.get(i).getVsID()))
                    {
                        if(var.getVariable().toInt()==2)
                            vsList.get(i).setVsState1(OBDefine.STATE_ENABLE);
                        else
                            vsList.get(i).setVsState1(OBDefine.STATE_DISABLE);
                    }
                    else
                    {
                        throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(target:%s, vsID:%s)", var.getOid().toString(), oid.getVsID()));
                    }
                }
                else
                {
                    throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(target:%s, vsID:%s)", var.getOid().toString(), oid.getVsID()));
                }
            }
            
            //virtual server network class 가져오기
            //alteon v29부터 생겨서 없을 수도 있다. oid를 pick 했을 때만 한다. network class는 복수로 할당할 수 있는데 1개만 처리한다. 복수인 환경을 현재 구성해서 데이터를 확인할 수 없다.
            
            ArrayList<String> temp;
            if(oid.getVsSrcNwClass()!=null && oid.getVsSrcNwClass().isEmpty()==false)
            {
                tmpList = walk(oid.getVsSrcNwClass());
                for(i=0;i<tmpList.size();i++)
                {
                    VariableBinding var=tmpList.get(i);
                    if(var.getVariable().toString()==null || var.getVariable().toString().isEmpty())
                    { //v29.0.2에서 확인됐는데, vs에 src net이 없어 snmp 엔트리가 온다. 빈문자열이 나온다.
                      //그냥 두면 이후에 src net 정보를 가져오려고 Integer.parseInt()할때 문제가 되므로, 없는 것을 건너뛰는 처리를 한다.
                        continue;
                    }
                    temp = parseOidStringV295(var.getOid().toString(), oid.getVsSrcNwClass());
                    if(temp!=null && temp.size()==1)
                    {
                        for(j=0; j<vsNum; j++)
                        {
                            if(temp.get(0).equals(vsList.get(j).getVsID())==true) //virtual server id가 같으면
                            {
                                vsList.get(j).getVsNwClassList().add(var.getVariable().toString()); //여러개 있을 수 있다.
                            }
                        }
                    }
                }
            }
            OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", vsList));
            return vsList;
        }
        catch(OBException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());

        }
    }
    
    private ArrayList<DtoSnmpPoolAlteon> getPoolList(DtoOidCfgPool oid) throws OBException
    {
        try
        {
            OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. host:%s, oid:%s", this.getHost(), oid)); 
            ArrayList<DtoSnmpPoolAlteon>  poolList = new ArrayList<DtoSnmpPoolAlteon>();
            
            String reqOid="";
            List<VariableBinding> tmpList;
            
            // pool ID 추출
            tmpList = walk(oid.getPoolID());
            for(int i=0;i<tmpList.size();i++)
            {
                VariableBinding var=tmpList.get(i);
                String poolID=var.getVariable().toString();
                if(poolID!=null)
                {
                    DtoSnmpPoolAlteon pool = new DtoSnmpPoolAlteon();
                    pool.setPoolID(poolID);
                    poolList.add(pool);
                }
                else
                {
                    throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(target:%s, poolID:%s)", var.getOid().toString(), oid.getPoolID()));
                }
            }   
            if(poolList.size()==0)
            {
                OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. host:%s, result:%s", this.getHost(), poolList));   
                return poolList;
            }
            tmpList = walk(oid.getPoolName(), poolList.size(), 3);
            for(int i=0;i<tmpList.size();i++)
            {
                VariableBinding var=tmpList.get(i);
                ArrayList<String> oidList = parseOidStringV295(var.getOid().toString(), oid.getPoolName());
                if(oidList.size()==1)
                {
                    if(oidList.get(0).equals(poolList.get(i).getPoolID()))
                    {
                        //poolList.get(i).setPoolName(var.getVariable().toString());
                        poolList.get(i).setPoolName(alteonSnmpHexStringDecode(var.getVariable().toString())); //한글 pool 이름 처리
                    }
                    else
                    {
                        throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(target:%s, poolID:%s)", var.getOid().toString(), oid.getPoolID()));
                    }
                }
                else
                {
                    throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(target:%s, poolID:%s)", var.getOid().toString(), oid.getPoolID()));
                }
            }
        
            // LB Method
            tmpList = walk(oid.getPoolLBMethod(), poolList.size(), 3);
            for(int i=0;i<tmpList.size();i++)
            {
                VariableBinding var=tmpList.get(i);
                ArrayList<String> oidList = parseOidStringV295(var.getOid().toString(), oid.getPoolLBMethod());
                if(oidList.size()==1)
                {
                    if(oidList.get(0).equals(poolList.get(i).getPoolID()))
                    {
                        poolList.get(i).setPoolLbMethod(convertLbMethod(var.getVariable().toInt()));
                    }
                    else
                    {
                        throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(target:%s, node:%s)", var.getOid().toString(), oid.getPoolID()));
                    }
                }
                else
                {
                    throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(target:%s, node:%s)", var.getOid().toString(), oid.getPoolID()));
                }
            }

            // Group Backup
            tmpList = walk(oid.getPoolBackup(), poolList.size(), 3);
            for(int i=0;i<tmpList.size();i++)
            {
                VariableBinding var=tmpList.get(i);
                ArrayList<String> oidList = parseOidStringV295(var.getOid().toString(), oid.getPoolBackup());
                if(oidList.size()==1)
                {
                    if(oidList.get(0).equals(poolList.get(i).getPoolID()))
                    {
//                      if(var.getVariable().isException() != true && var.getVariable().toInt()!=OBDefine.BACKUP_STATE.EMPTY)
                        if(!var.getVariable().toString().equals("") && !var.getVariable().toString().equals("0"))
                        {
                            poolList.get(i).setBakType(OBDefine.BACKUP_STATE.GROUPBAK);//backup Group 설정 시
                            poolList.get(i).setBakID(var.getVariable().toString());
                        }
                        else
                        {
                            poolList.get(i).setBakType(OBDefine.BACKUP_STATE.EMPTY);//backup Group 미 설정 시
                            poolList.get(i).setBakID(var.getVariable().toString());
                        }
                    }
                    else
                    {
                        throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(target:%s, node:%s)", var.getOid().toString(), oid.getPoolID()));
                    }
                }
                else
                {
                    throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(target:%s, node:%s)", var.getOid().toString(), oid.getPoolID()));
                }
            }

            // Group Real Backup
            tmpList = walk(oid.getPoolRealBackup(), poolList.size(), 3);
            for(int i=0;i<tmpList.size();i++)
            {
                VariableBinding var=tmpList.get(i);
                ArrayList<String> oidList = parseOidStringV295(var.getOid().toString(), oid.getPoolRealBackup());
                if(oidList.size()==1)
                {
                    if(oidList.get(0).equals(poolList.get(i).getPoolID()))
                    {
                        if(poolList.get(i).getBakType()==OBDefine.BACKUP_STATE.EMPTY)
                        {
                            if(!var.getVariable().toString().equals("") && !var.getVariable().toString().equals("0"))
                            {
                                poolList.get(i).setBakType(OBDefine.BACKUP_STATE.REALBAK);//Group Real Backup 설정 시
                                poolList.get(i).setBakID(var.getVariable().toString());
                            }
                            else
                            {
                                poolList.get(i).setBakType(OBDefine.BACKUP_STATE.EMPTY);//Group Real Backup 미 설정 시
                                poolList.get(i).setBakID(var.getVariable().toString());
                            }
                        }

                    }
                    else
                    {
                        throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(target:%s, node:%s)", var.getOid().toString(), oid.getPoolID()));
                    }
                }
                else
                {
                    throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(target:%s, node:%s)", var.getOid().toString(), oid.getPoolID()));
                }
            }
            
            if(oid.getPoolHealthCheck()!=null)
            {
                // health check
                tmpList = walk(oid.getPoolHealthCheck(), poolList.size(), 3);
                for(int i=0;i<tmpList.size();i++)
                {
                    VariableBinding var=tmpList.get(i);
                    ArrayList<String> oidList = parseOidStringV295(var.getOid().toString(), oid.getPoolHealthCheck());
                    if(oidList.size()==1)
                    {
                        if(oidList.get(0).equals(poolList.get(i).getPoolID()))
                        {
                            poolList.get(i).setPoolHealthCheck(var.getVariable().toString());
                        }
                        else
                        {
                            throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(target:%s, node:%s)", var.getOid().toString(), oid.getPoolID()));
                        }
                    }
                    else
                    {
                        throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(target:%s, node:%s)", var.getOid().toString(), oid.getPoolID()));
                    }
                }
            }
                
            // pool members
            // 29.5.0 이상 Index를 영문옵션을 주면 Octec으로 값이 들어오지 않는다.
            tmpList = walk(oid.getPoolMemberID());
            for(int i=0; i<poolList.size();i++)
            {
                ArrayList<String> memberList = new ArrayList<String>();
                for(int j=0;j<tmpList.size();j++)
                {
                    VariableBinding var=tmpList.get(j);
                    ArrayList<String> oidList = parseOidStringV295(var.getOid().toString(), oid.getPoolMemberID());
                    if(oidList.size()==2)
                    {
                        if(oidList.get(0).equals(poolList.get(i).getPoolID()))
                        {
                            memberList.add(var.getVariable().toString());
                        }
                    }
                    else
                    {
                        throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(target:%s, node:%s)", var.getOid().toString(), oid.getPoolID()));
                    }
                }
                poolList.get(i).setMemberIDs(memberList);
            }
            for(DtoSnmpPoolAlteon pool:poolList)
            {
                ArrayList<Integer> memberState = new ArrayList<Integer>();
                pool.setMemberState(memberState);
                String pooID = OBParser.oidAlteonEnh(pool.getPoolID());
                reqOid = oid.getPoolMemberState()+pooID;
                tmpList = walk(reqOid);
                if(tmpList==null)
                    continue;   
                for(VariableBinding var:tmpList)
                {
                    memberState.add(convertPoolMemberState(var.getVariable().toInt()));
                }
            }
            
            OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end."));
            OBSystemLog.info(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", poolList));
            return poolList;
        }
        catch(OBException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());

        }
    }
    
    private ArrayList<DtoSnmpNodeAlteon> getNodeList(DtoOidCfgNode oid) throws OBException
    {
        try
        {
            OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. host:%s, oid:%s", this.getHost(), oid)); 
            ArrayList<DtoSnmpNodeAlteon> nodeList = new ArrayList<DtoSnmpNodeAlteon>();
    
            List<VariableBinding> tmpList;
            // node ID
            tmpList = walk(oid.getNodeID());
            for(int i=0;i<tmpList.size();i++)
            {
                VariableBinding var=tmpList.get(i);
                String nodeID=var.getVariable().toString();
                if(nodeID!=null)
                {
                    DtoSnmpNodeAlteon node = new DtoSnmpNodeAlteon();
                    node.setNodeID(nodeID);
                    node.setNodeStatus(OBDefine.STATUS_DISABLE);
                    node.setNodePort(new ArrayList<Integer>()); //port 목록 준비
                    nodeList.add(node);
                }
                else
                {
                    throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(target:%s, node:%s)", var.getOid().toString(), oid.getNodeID()));
                }
            }
            if(nodeList.size()==0)
                return nodeList;
            // ipaddress
            
            if(tmpList != null)
            {
                tmpList = walk(oid.getNodeIpAddr(), nodeList.size(), 3);
                for(int i=0;i<tmpList.size();i++)
                {
                    VariableBinding var=tmpList.get(i);
                    ArrayList<String> oidList = parseOidStringV295(var.getOid().toString(), oid.getNodeIpAddr());
                    if(oidList.size()==1)
                    {
                        if(oidList.get(0).equals(nodeList.get(i).getNodeID()))
                        {
                            nodeList.get(i).setNodeIpAddr(var.getVariable().toString());
                        }
                        else
                        {
                            throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(target:%s, node:%s)", var.getOid().toString(), oid.getNodeID()));
                        }
                    }
                    else
                    {
                        throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(target:%s, node:%s)", var.getOid().toString(), oid.getNodeID()));
                    }
                }
            }
            // node state
            if(oid.getNodeState()!=null && oid.getNodeState().isEmpty()==false)
            {
                tmpList = walk(oid.getNodeState(), nodeList.size(), 3);
                if(tmpList != null)
                {
                    for(int i=0;i<tmpList.size();i++)
                    {
                        VariableBinding var=tmpList.get(i);
                        ArrayList<String> oidList = parseOidStringV295(var.getOid().toString(), oid.getNodeState());
                        if(oidList.size()==1)
                        {
                            if(oidList.get(0).equals(nodeList.get(i).getNodeID()))
                            {
                                nodeList.get(i).setNodeState(convertNodeState(var.getVariable().toInt()));
                            }
                            else
                            {
                                throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(target:%s, node:%s)", var.getOid().toString(), oid.getNodeID()));
                            }
                        }
                        else
                        {
                            throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(target:%s, node:%s)", var.getOid().toString(), oid.getNodeID()));
                        }
                    }
                }
            }
            // node name
            if(oid.getNodeName()!=null && oid.getNodeName().isEmpty()==false)
            {
                tmpList = walk(oid.getNodeName(), nodeList.size(), 3);
                if(tmpList != null)
                {
                    for(int i=0;i<tmpList.size();i++)
                    {
                        VariableBinding var=tmpList.get(i);
                        ArrayList<String> oidList = parseOidStringV295(var.getOid().toString(), oid.getNodeName());
                        if(oidList.size()==1)
                        {
                            if(oidList.get(0).equals(nodeList.get(i).getNodeID()))
                            {
                                nodeList.get(i).setNodeName(var.getVariable().toString());
                            }
                            else
                            {
                                throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(target:%s, node:%s)", var.getOid().toString(), oid.getNodeID()));
                            }
                        }
                        else
                        {
                            throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(target:%s, node:%s)", var.getOid().toString(), oid.getNodeID()));
                        }
                    }
                }
            }
            // node avail
            if(oid.getNodeAvail()!=null && oid.getNodeAvail().isEmpty()==false)
            {
                tmpList = walk(oid.getNodeAvail(), nodeList.size(), 3);
                if(tmpList != null)
                {
                    for(int i=0;i<tmpList.size();i++)
                    {
                        VariableBinding var=tmpList.get(i);
                        ArrayList<String> oidList = parseOidStringV295(var.getOid().toString(), oid.getNodeAvail());
                        if(oidList.size()==1)
                        {
                            if(oidList.get(0).equals(nodeList.get(i).getNodeID()))
                            {
                                nodeList.get(i).setNodeAvail(var.getVariable().toInt());
                            }
                            else
                            {
                                throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(target:%s, node:%s)", var.getOid().toString(), oid.getNodeID()));
                            }
                        }
                        else
                        {
                            throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(target:%s, node:%s)", var.getOid().toString(), oid.getNodeID()));
                        }
                    }
                }
            }
            // node status
            if(oid.getNodeStatus()!=null && oid.getNodeStatus().isEmpty()==false)
            {
                tmpList = walk(oid.getNodeStatus(), nodeList.size(), 3);
                if(tmpList != null)
                {
                    for(int i=0;i<tmpList.size();i++)
                    {
                        VariableBinding var=tmpList.get(i);
                        ArrayList<String> oidList = parseOidStringV295(var.getOid().toString(), oid.getNodeStatus());
                        if(oidList.size()==1)
                        {
                            if(oidList.get(0).equals(nodeList.get(i).getNodeID()))
                            {
                                nodeList.get(i).setNodeStatus(convertNodeStatus(var.getVariable().toInt()));
                            }
                            else
                            {
                                throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(target:%s, node:%s)", var.getOid().toString(), oid.getNodeID()));
                            }
                        }
                        else
                        {
                            throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(target:%s, node:%s)", var.getOid().toString(), oid.getNodeID()));
                        }
                    }
                }
            }
            // node status
//          HashMap<Integer, Integer> nodeStatusMap = getNodeStatus(oid.getNodeStatus());
//          for(DtoSnmpNodeAlteon node:nodeList)
//          {
//              node.setNodeStatus(nodeStatusMap.get(node.getNodeID()));
//          }
            
            // port - 0개 이상 - 추가작업 필요
            if(oid.getNodePort()!=null && oid.getNodePort().isEmpty()==false)
            {
                tmpList = walk(oid.getNodePort());
                if(tmpList != null)
                {
                    for(int i=0;i<tmpList.size();i++)
                    {
                        VariableBinding var=tmpList.get(i);
                        ArrayList<String> oidList = parseOidStringV295(var.getOid().toString(), oid.getNodePort());
                        if(oidList.size()==2) //두개가 나오는데, 첫번째가 node id, 두번째가 add port index
                        {
                            for(int j=0; j<nodeList.size(); j++)
                            {
                                if(oidList.get(0).equals(nodeList.get(j).getNodeID())) //node id 일치
                                {
                                    nodeList.get(j).getNodePort().add(var.getVariable().toInt());
                                    break;
                                }
                            }
                        }
                        else
                        {
                            throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(target:%s, node:%s)", var.getOid().toString(), oid.getNodeID()));
                        }
                    }
                }
            }
            //interval
            if(oid.getNodeInterval()!=null && oid.getNodeInterval().isEmpty()==false)
            {
                tmpList = walk(oid.getNodeInterval(), nodeList.size(), 3);
                if(tmpList != null)
                {
                    for(int i=0;i<tmpList.size();i++)
                    {
                        VariableBinding var=tmpList.get(i);
                        ArrayList<String> oidList = parseOidStringV295(var.getOid().toString(), oid.getNodeInterval());
                        if(oidList.size()==1)
                        {
                            if(oidList.get(0).equals(nodeList.get(i).getNodeID()))
                            {
                                nodeList.get(i).setNodeInterval(var.getVariable().toInt());
                            }
                            else
                            {
                                throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(target:%s, node:%s)", var.getOid().toString(), oid.getNodeID()));
                            }
                        }
                        else
                        {
                            throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(target:%s, node:%s)", var.getOid().toString(), oid.getNodeID()));
                        }
                    }
                }
            }

            //node retry(FailRetry)
            if(oid.getNodeFailRetry()!=null && oid.getNodeFailRetry().isEmpty()==false)
            {
                tmpList = walk(oid.getNodeFailRetry(), nodeList.size(), 3);
                if(tmpList != null)
                {
                    for(int i=0;i<tmpList.size();i++)
                    {
                        VariableBinding var=tmpList.get(i);
                        ArrayList<String> oidList = parseOidStringV295(var.getOid().toString(), oid.getNodeFailRetry());
                        if(oidList.size()==1)
                        {
                            if(oidList.get(0).equals(nodeList.get(i).getNodeID()))
                            {
                                nodeList.get(i).setNodeFailRetry(var.getVariable().toInt());
                            }
                            else
                            {
                                throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(target:%s, node:%s)", var.getOid().toString(), oid.getNodeID()));
                            }
                        }
                        else
                        {
                            throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(target:%s, node:%s)", var.getOid().toString(), oid.getNodeID()));
                        }
                    }
                }
            }
            //backup
            if(oid.getNodeBackup()!=null && oid.getNodeBackup().isEmpty()==false)
            {
                tmpList = walk(oid.getNodeBackup(), nodeList.size(), 3);
                if(tmpList != null)
                {
                    for(int i=0;i<tmpList.size();i++)
                    {
                        VariableBinding var=tmpList.get(i);
                        ArrayList<String> oidList = parseOidStringV295(var.getOid().toString(), oid.getNodeBackup());
                        if(oidList.size()==1)
                        {
                            if(oidList.get(0).equals(nodeList.get(i).getNodeID()))
                            {
                                if(!var.getVariable().toString().equals(""))
                                    nodeList.get(i).setNodeBackup(var.getVariable().toString());
                            }
                            else
                            {
                                throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(target:%s, node:%s)", var.getOid().toString(), oid.getNodeID()));
                            }
                        }
                        else
                        {
                            throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(target:%s, node:%s)", var.getOid().toString(), oid.getNodeID()));
                        }
                    }
                }
            }
            //backup id
            if(oid.getNodeBackup()!=null && oid.getNodeBackup().isEmpty()==false)
            {
                tmpList = walk(oid.getNodeBackup(), nodeList.size(), 3);
                if(tmpList != null)
                {
                    for(int i=0;i<tmpList.size();i++)
                    {
                        VariableBinding var=tmpList.get(i);
                        ArrayList<String> oidList = parseOidStringV295(var.getOid().toString(), oid.getNodeBackup());
                        if(oidList.size()==1)
                        {
                            if(oidList.get(0).equals(nodeList.get(i).getNodeID()))
                            {
                                if(!var.getVariable().toString().equals("")&&!var.getVariable().toString().equals("0"))
                                {
                                    nodeList.get(i).setBakType(OBDefine.BACKUP_STATE.REALBAK);//backup Real 설정 시
                                    nodeList.get(i).setBakID(var.getVariable().toString());
                                }
                                else
                                {
                                    nodeList.get(i).setBakType(OBDefine.BACKUP_STATE.EMPTY);//backup Real 미 설정 시
                                    nodeList.get(i).setBakID(var.getVariable().toString());
                                }
                            }
                            else
                            {
                                throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(target:%s, node:%s)", var.getOid().toString(), oid.getNodeID()));
                            }
                        }
                        else
                        {
                            throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(target:%s, node:%s)", var.getOid().toString(), oid.getNodeID()));
                        }
                    }
                }
            }
            //weight
            if(oid.getNodeWeight()!=null && oid.getNodeWeight().isEmpty()==false)
            {
                tmpList = walk(oid.getNodeWeight(), nodeList.size(), 3);
                if(tmpList != null)
                {
                    for(int i=0;i<tmpList.size();i++)
                    {
                        VariableBinding var=tmpList.get(i);
                        ArrayList<String> oidList = parseOidStringV295(var.getOid().toString(), oid.getNodeWeight());
                        if(oidList.size()==1)
                        {
                            if(oidList.get(0).equals(nodeList.get(i).getNodeID()))
                            {
                                nodeList.get(i).setNodeWeight(var.getVariable().toInt());
                            }
                            else
                            {
                                throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(target:%s, node:%s)", var.getOid().toString(), oid.getNodeID()));
                            }
                        }
                        else
                        {
                            throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(target:%s, node:%s)", var.getOid().toString(), oid.getNodeID()));
                        }
                    }
                }
            }

            // max conns
            if(oid.getNodeMaxConns()!=null && oid.getNodeMaxConns().isEmpty()==false)
            {
                tmpList = walk(oid.getNodeMaxConns(), nodeList.size(), 3);
                if(tmpList != null)
                {
                    for(int i=0;i<tmpList.size();i++)
                    {
                        VariableBinding var=tmpList.get(i);
                        ArrayList<String> oidList = parseOidStringV295(var.getOid().toString(), oid.getNodeMaxConns());
                        if(oidList.size()==1)
                        {
                            if(oidList.get(0).equals(nodeList.get(i).getNodeID()))
                            {
                                nodeList.get(i).setNodeMaxConns(var.getVariable().toInt());
                            }
                            else
                            {
                                throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(target:%s, node:%s)", var.getOid().toString(), oid.getNodeID()));
                            }
                        }
                        else
                        {
                            throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(target:%s, node:%s)", var.getOid().toString(), oid.getNodeID()));
                        }
                    }
                }
            }
            
            //max conn mode
            if(oid.getNodeMaxConMode()!=null && oid.getNodeMaxConMode().isEmpty()==false)
            {
                tmpList = walk(oid.getNodeMaxConMode(), nodeList.size(), 3);
                if(tmpList != null)
                {
                    for(int i=0;i<tmpList.size();i++)
                    {
                        VariableBinding var=tmpList.get(i);
                        ArrayList<String> oidList = parseOidStringV295(var.getOid().toString(), oid.getNodeMaxConMode());
                        if(oidList.size()==1)
                        {
                            if(oidList.get(0).equals(nodeList.get(i).getNodeID()))
                            {
                                nodeList.get(i).setNodeMaxConMode(var.getVariable().toInt());
                            }
                            else
                            {
                                throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(target:%s, node:%s)", var.getOid().toString(), oid.getNodeID()));
                            }
                        }
                        else
                        {
                            throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(target:%s, node:%s)", var.getOid().toString(), oid.getNodeID()));
                        }
                    }
                }
            }
            //timeout
            if(oid.getNodeTimeout()!=null && oid.getNodeTimeout().isEmpty()==false)
            {
                tmpList = walk(oid.getNodeTimeout(), nodeList.size(), 3);
                if(tmpList != null)
                {
                    for(int i=0;i<tmpList.size();i++)
                    {
                        VariableBinding var=tmpList.get(i);
                        ArrayList<String> oidList = parseOidStringV295(var.getOid().toString(), oid.getNodeTimeout());
                        if(oidList.size()==1)
                        {
                            if(oidList.get(0).equals(nodeList.get(i).getNodeID()))
                            {
                                nodeList.get(i).setNodeTimeout(var.getVariable().toInt());
                            }
                            else
                            {
                                throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(target:%s, node:%s)", var.getOid().toString(), oid.getNodeID()));
                            }
                        }
                        else
                        {
                            throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(target:%s, node:%s)", var.getOid().toString(), oid.getNodeID()));
                        }
                    }
                }
            }
            OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end."));
            return nodeList;
        }
        catch(OBException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());

        }
    }
    
    public ArrayList<OBDtoMonTrafficVServer> getTrafficVServer(int adcType, String swVersion) throws OBException
    {
        OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcType:%d, swVersion:%s", adcType, swVersion));
        OBSnmpOidDB oidDB = new OBSnmpOidDB();

        DtoOidStatVirtServer oid;
        try
        {
            oid = oidDB.getStatVirtServer(adcType, swVersion);
            ArrayList<OBDtoMonTrafficVServer> list = getTrafficVServer(oid);
            OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result-size:%d", list.size()));
            return list;
        }
        catch(OBException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());

        }
    }
    
    private ArrayList<OBDtoMonTrafficVServer> getTrafficVServer(DtoOidStatVirtServer oid)  throws OBException
    {
        try
        {
            ArrayList<OBDtoMonTrafficVServer> result = new ArrayList<OBDtoMonTrafficVServer>();
    
            List<VariableBinding> tmpList;
            
            // virtual server ID 추출
            tmpList = walk(oid.getVsID());
            for(int i=0;i<tmpList.size();i++)
            {
                VariableBinding var=tmpList.get(i);
                String vsID=var.getVariable().toString();
                if(vsID!=null)
                {
                    OBDtoMonTrafficVServer vs = new OBDtoMonTrafficVServer();
                    vs.setAlteonID(vsID);
                    result.add(vs);
                }
                else
                {
                    throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(target:%s, vsID:%s)", var.getOid().toString(), oid.getVsID()));
                }
            }   
            
            // ip 주소 추출.
            tmpList = walk(oid.getVsIPAddress(), result.size(), 3);
            for(int i=0;i<tmpList.size();i++)
            {
                VariableBinding var=tmpList.get(i);
                ArrayList<String> oidList = parseOidStringV295(var.getOid().toString(), oid.getVsIPAddress());
                if(oidList.size()==1)
                {
                    if(oidList.get(0).equals(result.get(i).getAlteonID()))
                    {
                        
                        result.get(i).setIpaddress(var.getVariable().toString());
                    }
                    else
                    {
                        throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(target:%s, vsID:%s)", var.getOid().toString(), oid.getVsID()));
                    }
                }
                else
                {
                    throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(target:%s, vsID:%s)", var.getOid().toString(), oid.getVsID()));
                }
            }
            
            // 이름 추출..
            tmpList = walk(oid.getVsName());// 이름은 버전에 따라 제공되지 않을 수 있음.
            for(int i=0;i<tmpList.size();i++)
            {
                VariableBinding var=tmpList.get(i);
                ArrayList<String> oidList = parseOidStringV295(var.getOid().toString(), oid.getVsName());
                if(oidList.size()==1)
                {
                    if(oidList.get(0).equals(result.get(i).getAlteonID()))
                    {
                        
                        result.get(i).setName(var.getVariable().toString());
                    }
                    else
                    {
                        throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(target:%s, vsID:%s)", var.getOid().toString(), oid.getVsID()));
                    }
                }
                else
                {
                    throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(target:%s, vsID:%s)", var.getOid().toString(), oid.getVsID()));
                }
            }
            
            // total conns 추출.
            tmpList = walk(oid.getTotConns(), result.size(), 3);
            for(int i=0;i<tmpList.size();i++)
            {
                VariableBinding var=tmpList.get(i);
                ArrayList<String> oidList = parseOidStringV295(var.getOid().toString(), oid.getTotConns());
                if(oidList.size()==1)
                {
                    if(oidList.get(0).equals(result.get(i).getAlteonID()))
                    {
                        result.get(i).setTotConns(var.getVariable().toLong());
                    }
                    else
                    {
                        throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(target:%s, vsID:%s)", var.getOid().toString(), oid.getVsID()));
                    }
                }
                else
                {
                    throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(target:%s, vsID:%s)", var.getOid().toString(), oid.getVsID()));
                }
            }       
            
            // max conns
            tmpList = walk(oid.getMaxConns(), result.size(), 3);
            for(int i=0;i<tmpList.size();i++)
            {
                VariableBinding var=tmpList.get(i);
                ArrayList<String> oidList = parseOidStringV295(var.getOid().toString(), oid.getMaxConns());
                if(oidList.size()==1)
                {
                    if(oidList.get(0).equals(result.get(i).getAlteonID()))
                    {
                        result.get(i).setMaxConns(var.getVariable().toLong());
                    }
                    else
                    {
                        throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(target:%s, vsID:%s)", var.getOid().toString(), oid.getVsID()));
                    }
                }
                else
                {
                    throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(target:%s, vsID:%s)", var.getOid().toString(), oid.getVsID()));
                }
            }       
            
            // cur conns
            tmpList = walk(oid.getCurConns(), result.size(), 3);
            for(int i=0;i<tmpList.size();i++)
            {
                VariableBinding var=tmpList.get(i);
                ArrayList<String> oidList = parseOidStringV295(var.getOid().toString(), oid.getCurConns());
                if(oidList.size()==1)
                {
                    if(oidList.get(0).equals(result.get(i).getAlteonID()))
                    {
                        result.get(i).setCurConns(var.getVariable().toLong());
                    }
                    else
                    {
                        throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(target:%s, vsID:%s)", var.getOid().toString(), oid.getVsID()));
                    }
                }
                else
                {
                    throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(target:%s, vsID:%s)", var.getOid().toString(), oid.getVsID()));
                }
            }   
            
            // bytes in 추출
            tmpList = walk(oid.getBytesIn(), result.size(), 3);
            for(int i=0;i<tmpList.size();i++)
            {
                VariableBinding var=tmpList.get(i);
                ArrayList<String> oidList = parseOidStringV295(var.getOid().toString(), oid.getBytesIn());
                if(oidList.size()==1)
                {
                    if(oidList.get(0).equals(result.get(i).getAlteonID()))
                    {
                        result.get(i).setBytesIn(var.getVariable().toLong());
                    }
                    else
                    {
                        throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(target:%s, vsID:%s)", var.getOid().toString(), oid.getVsID()));
                    }
                }
                else
                {
                    throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(target:%s, vsID:%s)", var.getOid().toString(), oid.getVsID()));
                }
            }       
    
            return result;
        }
        catch(OBException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
        }    
    }
    
    //slb 정보를 가져오면서 status를 다른 곳에서도 구하지만, 결국 이 함수로 status를 업데이트한다.  
    public ArrayList<OBDtoAdcVServerAlteon> getSlbStatus(int adcType, String swVersion, ArrayList<OBDtoAdcVServerAlteon> vsList, ArrayList<OBDtoAdcPoolAlteon> backupGroupList, ArrayList<OBDtoAdcPoolMemberAlteon> backupRealList) throws OBException
    {
        OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcType:%d, swVersion:%s", adcType, swVersion));
        try
        {
            // ArrayList<OBDtoAdcVServerAlteon> vsList = new OBVServerDB().getVServerListAllAlteon(adcInfo.getIndex());

            String mapKey = "";
            OBSnmpOidDB oidDB = new OBSnmpOidDB();
            DtoOidInfoAdcResc oid = oidDB.getAdcResource(adcType, swVersion);
            for(OBDtoAdcVServerAlteon vs : vsList)
            {
                ArrayList<OBDtoAdcVService> srvList = vs.getVserviceList();
                if(srvList == null || srvList.isEmpty())
                {
                    if(vs.getState() == OBDefine.STATE_ENABLE) // 가진 service가 없는데 vs가enable이면 available
                    {
                        vs.setStatus(OBDefine.STATUS_AVAILABLE);
                    }
                    else
                    {
                        vs.setStatus(OBDefine.STATUS_DISABLE); // 가진 service가 없는데, vs가 disable이면 disable
                    }
                    continue;
                }

                // 여기부터 서비스가 있을 때
                OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "status check VS IP = " + vs.getvIP() + " / state = " + vs.getState());
                // 지정된 virtual server에 할당된 service의 index 정보를 추출한다.
                HashMap<String, Integer> srvIndexHashMap = getServiceIndexMap(vs.getAlteonId(), oid.getServiceVirtPort());
                HashMap<String, Integer> stateListHahsMap = getMemberStatus(vs.getAlteonId(), oid.getNodeState());

                for(OBDtoAdcVService srv : srvList)
                {
                    OBDtoAdcPoolAlteon pool = srv.getPool();
                    if(pool == null)
                    {
                        if(vs.getState().equals(OBDefine.STATE_ENABLE))
                        {
                            srv.setStatus(OBDefine.STATUS_AVAILABLE);
                        }
                        else
                        {
                            srv.setStatus(OBDefine.STATUS_DISABLE);
                        }
                        continue;
                    }
                    ArrayList<OBDtoAdcPoolMemberAlteon> memberListOrg = pool.getMemberList();
                    //status 관리는 backup이 들어가지 않은 원래 멤버들만 하므로 원래 멤버목록은 보존한다. status 계산은 백업까지 포함하므로, clone을 만들어서 백업을 더한다. 
                    ArrayList<OBDtoAdcPoolMemberAlteon> memberListWithBackupMember = new ArrayList<OBDtoAdcPoolMemberAlteon>();
                    for(OBDtoAdcPoolMemberAlteon m: memberListOrg)
                    {
                        memberListWithBackupMember.add(new OBDtoAdcPoolMemberAlteon(m));
                    }
                    int backupGroupSize = backupGroupList.size();
                    int backupRealSize = backupRealList.size();
                    if(pool.getBakType() == OBDefine.BACKUP_STATE.REALBAK)
                    {
                        for(int i = 0; i < backupRealSize; i++)
                        {
                            if(pool.getBakID().equals(backupRealList.get(i).getIndex()))
                            {
                                memberListWithBackupMember.add(backupRealList.get(i));
                            }

                        }
                    }
                    else if(pool.getBakType() == OBDefine.BACKUP_STATE.GROUPBAK)
                    {
                        for(int i = 0; i < backupGroupSize; i++)
                        {
                            if(pool.getBakID().equals(backupGroupList.get(i).getIndex()))
                            {
                                memberListWithBackupMember.addAll(backupGroupList.get(i).getMemberList());
                            }

                        }
                    }
                    int memberSize = memberListWithBackupMember.size();
                    if(memberListWithBackupMember==null || memberSize==0)
                    {
                        if(vs.getState().equals(OBDefine.STATE_ENABLE))
                            srv.setStatus(OBDefine.STATUS_AVAILABLE);
                        else
                            srv.setStatus(OBDefine.STATUS_DISABLE);
                        continue;
                    }
                    Integer srvIndex = srvIndexHashMap.get(vs.getAlteonId() + "_" + srv.getServicePort());
                    
                    for(int i = 0; i < memberSize; i++)
                    {
                        if(memberListWithBackupMember.get(i).getBackupType() == OBDefine.BACKUP_STATE.REALBAK)
                        {
                            for(int j = 0; j < backupRealSize; j++)
                            {
                                if(memberListWithBackupMember.get(i).getBackupId().equals(backupRealList.get(j).getIndex()))
                                {
                                    memberListWithBackupMember.add(backupRealList.get(j));
                                }

                            }
                        }
                    }
                    for(OBDtoAdcPoolMemberAlteon member : memberListOrg)
                    {
                        if(null == srvIndex)
                        {
                            member.setStatus(OBDefine.STATUS_DISABLE);
                        }
                        else
                        {
                            // v29에서 기존 oid가 먹히지 않아서 임시로 만든 코드. 반드시 정정해야 한다.
                            // if(adcInfo.getSwVersion().compareTo("29")<0) //29이전
                            {
                                mapKey = vs.getAlteonId() + "_" + srvIndex + "_" + member.getAlteonNodeID();
                            }
                            // else
                            // {
                            // mapKey = member.getAlteonNodeID().toString();
                            // }
                            Integer state = stateListHahsMap.get(mapKey);
                            if(state != null)
                            {
                                member.setStatus(state.intValue());
                            }
                            else
                            {
                                OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "member status = null, disable");
                                // if(member.getStatus()==null||vs.getState()==OBDefine.STATUS_DISABLE) //Alteon group member disable을 enable로 표시되게 만든 오류 조건문. 의미 없이 들어가서 제거.2014.11.18 ykkim.
                                // {
                                member.setStatus(OBDefine.STATUS_DISABLE);
                                // }

                            }
                        }
                    }
                    for(OBDtoAdcPoolMemberAlteon member:memberListWithBackupMember)
                    {
                        if(null == srvIndex)
                        {
                            member.setStatus(OBDefine.STATUS_DISABLE);
                        }
                        else
                        {   
                            //v29에서 기존 oid가 먹히지 않아서 임시로 만든 코드. 반드시 정정해야 한다.
//                          if(adcInfo.getSwVersion().compareTo("29")<0) //29이전
                            {
                                mapKey = vs.getAlteonId()+"_"+srvIndex+"_"+member.getAlteonNodeID();
                            }
//                          else
//                          {
//                              mapKey = member.getAlteonNodeID().toString();
//                          }
                            Integer state=stateListHahsMap.get(mapKey);
                            if(state!=null)
                            {
                                member.setStatus(state.intValue());
                            }
                            else
                            {
                                OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "member status = null, disable");
                                //if(member.getStatus()==null||vs.getState()==OBDefine.STATUS_DISABLE) //Alteon group member disable을 enable로 표시되게 만든 오류 조건문. 의미 없이 들어가서 제거.2014.11.18 ykkim.
                                //{
                                    member.setStatus(OBDefine.STATUS_DISABLE);
                                //}
                                
                            }
                        }
                    }
                    // service status 계산.
                    boolean isAvailable = false;
                    for(OBDtoAdcPoolMemberAlteon member : memberListWithBackupMember)
                    {
                        if(member.getStatus().equals(OBDefine.STATUS_AVAILABLE))
                        {
                            srv.setStatus(OBDefine.STATUS_AVAILABLE); // 살아있는 멤버가 하나 나왔으니 서비스가 살았다고 확인. 그만하자.
                            OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "service (" + srv.getRealPort() + ") status = AVAILABLE");
                            isAvailable = true;
                            break;
                        }
                    }
                    if(isAvailable == true)
                        continue;

                    int disableStatusCnt = 0;
                    for(OBDtoAdcPoolMemberAlteon member : memberListWithBackupMember)
                    {
                        if(member.getStatus().equals(OBDefine.STATUS_DISABLE) == true)
                            disableStatusCnt++;
                    }
                    if(disableStatusCnt != 0 && disableStatusCnt == memberListWithBackupMember.size())
                    {
                        if(vs.getState().equals(OBDefine.STATE_ENABLE))
                        {
                            srv.setStatus(OBDefine.STATUS_UNAVAILABLE);
                            OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "service(" + srv.getRealPort() + ") status = UNAVAILABLE because member all disable && vs enable");
                        }
                        else
                        {
                            srv.setStatus(OBDefine.STATUS_DISABLE);
                            OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "service(" + srv.getRealPort() + ") status = UNAVAILABLE because member all disable && vs disable");
                        }
                        continue;
                    }

                    srv.setStatus(OBDefine.STATUS_UNAVAILABLE);
                    OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "service(" + srv.getRealPort() + ") status = UNAVAILABLE");
                }

                // LAST!! vitual server 상태 계산
                if(vs.getState().equals(OBDefine.STATE_DISABLE)) // vs가 disable이면 서비스를 검사할 필요도 없다.
                {
                    vs.setStatus(OBDefine.STATUS_DISABLE);
                }
                else
                {
                    boolean isAvailable = false;
                    for(OBDtoAdcVService srv : srvList)
                    {
                        if(srv.getStatus().equals(OBDefine.STATUS_AVAILABLE))
                        {
                            isAvailable = true;
                            OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "SERVER AVAILABLE by service(" + srv.getRealPort() + ")");
                            break;
                        }
                    }
                    if(isAvailable == true)
                    {
                        vs.setStatus(OBDefine.STATUS_AVAILABLE);
                    }
                    else
                    // enabled service가 하나도 없다.
                    {
                        // service enable이 하나도 없으면, disable이나 unavailable만 있는건데,
                        // 모두 disable이든 unavailable이 섞였든 vs 상태는 똑같이 UNAVAILABLE이다.그러므로 더 이상의 계산은 필요없다.
                        // 기존에 service가 모두 disable인지 체크하는 코드가 있었데 그렇든 아니든 모두 vs = unavailable이 나갔기때문에 불필요 코드로 확인하고 제거했다.
                        vs.setStatus(OBDefine.STATUS_UNAVAILABLE);
                        OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "SERVER UNAVAILABLE because service all disable or unavailable");
                    }
                }
            }
            OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result-size:%d", vsList.size()));

            return vsList;
        }
        catch(OBException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());

        }
    }
    
    private HashMap<String, Integer> getMemberStatus(String vsID, String oid)  throws OBException
    {
        try
        {
            OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. vsID:%s, oid:%s", vsID, oid));
            HashMap<String, Integer> result = new HashMap<String, Integer>();
            List<VariableBinding> tmpList;
            
            // node ID 구성
            String reqOid="";
            String vsIDEnh = OBParser.oidAlteonEnh(vsID);
            reqOid=oid+vsIDEnh;
            if(oid.equals("1.3.6.1.4.1.1872.2.5.4.3.1.1.7")==true) //v29이상 임시 처리
            {
                reqOid = oid;
            }

            tmpList = walk(reqOid);
            
            String mapKey = "";

            for(VariableBinding var:tmpList)
            {
                ArrayList<String> oidList = parseOidStatusV295(var.getOid().toString(), reqOid);
                Integer nodeState=var.getVariable().toInt();
                if(nodeState!=null)
                {
                    if(oid.equals("1.3.6.1.4.1.1872.2.5.4.3.1.1.7")==true) //v29이상 임시 처리
                    {
                        mapKey = oidList.get(0).toString();
                    }
                    else //기존 방법
                    {
                        mapKey = vsID+"_"+oidList.get(0)+"_"+oidList.get(1);
                    }
                    result.put(mapKey, convertNodeStatus(nodeState));
                }
            }

            OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", result));
            return result;
        }
        catch(OBException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
        }
    }
    
    public OBDtoRptInspectionSnmpAlteon getRptInspection(int adcType, String swVersion) throws OBException
    {
        OBSnmpOidDB oidDB = new OBSnmpOidDB();

        try
        {
            
            OBDtoOidRptInspection oid = oidDB.getRptInspection(adcType, swVersion);
            OBDtoRptInspectionSnmpAlteon retVal = getRptInspection(oid);

            // link 정보를 추출 후 리턴 DTO에 설정. 분리한 이유는 link 관련된 기능의 함수 재사용을 위해.  
            OBDtoRptInspectionSnmpAlteon linkInfo = getRptInspectionLinkInfo(oid);
            retVal.setLinkDiscardsInList(linkInfo.getLinkDiscardsInList());
            retVal.setLinkDiscardsOutList(linkInfo.getLinkDiscardsOutList());
            retVal.setLinkDuplexInfoList(linkInfo.getLinkDuplexInfoList());
            retVal.setLinkErrorInList(linkInfo.getLinkErrorInList());
            retVal.setLinkErrorOutInfoList(linkInfo.getLinkErrorOutInfoList());
            retVal.setLinkSpeedInfoList(linkInfo.getLinkSpeedInfoList());
            retVal.setLinkUpInfoList(linkInfo.getLinkUpInfoList());
            return retVal;
        }
        catch(OBException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, "general: "+e.getMessage());

        }
    }
    
    private OBDtoRptInspectionSnmpAlteon getRptInspectionLinkInfo(OBDtoOidRptInspection oidInfo) throws OBException
    {
     // OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("start. %s.", oid.toString()));
        OBDtoRptInspectionSnmpAlteon retVal = new OBDtoRptInspectionSnmpAlteon();
        
        Snmp snmp = null;
        String queryOid = "";
        List<VariableBinding> tmpList;
        try
        {
            snmp = openSnmp();
            
            //"rptLinkUpInfo"    
            ArrayList<OBNameValue> linkUpInfoList = new ArrayList<OBNameValue>();
            retVal.setLinkUpInfoList(linkUpInfoList);
            queryOid = oidInfo.getLinkupInfo();
            try
            {
                if(queryOid!=null && !queryOid.isEmpty())
                {
                    tmpList = walk(queryOid);
                    for(VariableBinding var : tmpList)
                    {
                        ArrayList<Integer> indexList = parseOid(var.getOid().toString(), queryOid);
                        if(indexList.size()==1)
                        {
                            OBNameValue value = new OBNameValue();
                            value.setName(indexList.get(0).toString());
                            value.setValue(var.getVariable().toString());
                            linkUpInfoList.add(value);
                        }
                        else
                        {
                            OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to get snmp data(host:%s, oid:%s). invalid format", getHost(), queryOid));
                        }
                    }
                }
            }
            catch(Exception e)
            {
                OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to get snmp data(host:%s, oid:%s)", getHost(), queryOid));
                throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("failed to get snmp data(host:%s, oid:%s)", getHost(), queryOid));
           }
            
            //"rptStatsPhyIfDiscardsIn"   
            ArrayList<OBNameValue> linkDiscardsInList = new ArrayList<OBNameValue>();
            retVal.setLinkDiscardsInList(linkDiscardsInList);
            queryOid = oidInfo.getPortDiscardsIn();
            try
            {
                if(queryOid!=null && !queryOid.isEmpty())
                {
                    tmpList = walk(queryOid);
                    for(VariableBinding var : tmpList)
                    {
                        ArrayList<Integer> indexList = parseOid(var.getOid().toString(), queryOid);
                        if(indexList.size()==1)
                        {
                            OBNameValue value = new OBNameValue();
                            value.setName(indexList.get(0).toString());
                            value.setValue(var.getVariable().toString());
                            linkDiscardsInList.add(value);
                        }
                        else
                        {
                            OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to get snmp data(host:%s, oid:%s). invalid format", getHost(), queryOid));
                        }
                    }
                }
            }
            catch(Exception e)
            {
                OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to get snmp data(host:%s, oid:%s)", getHost(), queryOid));
                throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("failed to get snmp data(host:%s, oid:%s)", getHost(), queryOid));
            }
            
            //"rptStatsPhyIfDiscardsOut"   
            ArrayList<OBNameValue> linkDiscardsOutList = new ArrayList<OBNameValue>();
            retVal.setLinkDiscardsOutList(linkDiscardsOutList);
            queryOid = oidInfo.getPortDiscardsIn();
            try
            {
                if(queryOid!=null && !queryOid.isEmpty())
                {
                    tmpList = walk(queryOid);
                    for(VariableBinding var : tmpList)
                    {
                        ArrayList<Integer> indexList = parseOid(var.getOid().toString(), queryOid);
                        if(indexList.size()==1)
                        {
                            OBNameValue value = new OBNameValue();
                            value.setName(indexList.get(0).toString());
                            value.setValue(var.getVariable().toString());
                            linkDiscardsOutList.add(value);
                        }
                        else
                        {
                            OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to get snmp data(host:%s, oid:%s). invalid format", getHost(), queryOid));
                        }
                    }
                }
            }
            catch(Exception e)
            {
                OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to get snmp data(host:%s, oid:%s)", getHost(), queryOid));
                throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("failed to get snmp data(host:%s, oid:%s)", getHost(), queryOid));
            }
            
            //"rptPortInfoMode"    
            ArrayList<OBNameValue> linkDuplexInfoList = new ArrayList<OBNameValue>();
            retVal.setLinkDuplexInfoList(linkDuplexInfoList);
            queryOid = oidInfo.getPortDiscardsIn();
            try
            {
                if(queryOid!=null && !queryOid.isEmpty())
                {
                    tmpList = walk(queryOid);
                    for(VariableBinding var : tmpList)
                    {
                        ArrayList<Integer> indexList = parseOid(var.getOid().toString(), queryOid);
                        if(indexList.size()==1)
                        {
                            OBNameValue value = new OBNameValue();
                            value.setName(indexList.get(0).toString());
                            value.setValue(var.getVariable().toString());
                            linkDuplexInfoList.add(value);
                        }
                        else
                        {
                            OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to get snmp data(host:%s, oid:%s). invalid format", getHost(), queryOid));
                        }
                    }
                }
            }
            catch(Exception e)
            {
                OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to get snmp data(host:%s, oid:%s)", getHost(), queryOid));
                throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("failed to get snmp data(host:%s, oid:%s)", getHost(), queryOid));
            }
            
            //"rptStatsPhyIfErrorsIn"     
            ArrayList<OBNameValue> linkErrorInList = new ArrayList<OBNameValue>();
            retVal.setLinkErrorInList(linkErrorInList);
            queryOid = oidInfo.getPortDiscardsIn();
            try
            {
                if(queryOid!=null && !queryOid.isEmpty())
                {
                    tmpList = walk(queryOid);
                    for(VariableBinding var : tmpList)
                    {
                        ArrayList<Integer> indexList = parseOid(var.getOid().toString(), queryOid);
                        if(indexList.size()==1)
                        {
                            OBNameValue value = new OBNameValue();
                            value.setName(indexList.get(0).toString());
                            value.setValue(var.getVariable().toString());
                            linkErrorInList.add(value);
                        }
                        else
                        {
                            OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to get snmp data(host:%s, oid:%s). invalid format", getHost(), queryOid));
                        }
                    }
                }
            }
            catch(Exception e)
            {
                OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to get snmp data(host:%s, oid:%s)", getHost(), queryOid));
                throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("failed to get snmp data(host:%s, oid:%s)", getHost(), queryOid));
            }
            
            //"rptStatsPhyIfErrorsOut"    
            ArrayList<OBNameValue> linkErrorOutInfoList = new ArrayList<OBNameValue>();
            retVal.setLinkErrorOutInfoList(linkErrorOutInfoList);
            queryOid = oidInfo.getPortDiscardsIn();
            try
            {
                if(queryOid!=null && !queryOid.isEmpty())
                {
                    tmpList = walk(queryOid);
                    for(VariableBinding var : tmpList)
                    {
                        ArrayList<Integer> indexList = parseOid(var.getOid().toString(), queryOid);
                        if(indexList.size()==1)
                        {
                            OBNameValue value = new OBNameValue();
                            value.setName(indexList.get(0).toString());
                            value.setValue(var.getVariable().toString());
                            linkErrorOutInfoList.add(value);
                        }
                        else
                        {
                            OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to get snmp data(host:%s, oid:%s). invalid format", getHost(), queryOid));
                        }
                    }
                }
            }
            catch(Exception e)
            {
                OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to get snmp data(host:%s, oid:%s)", getHost(), queryOid));
                throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("failed to get snmp data(host:%s, oid:%s)", getHost(), queryOid));
            }
            
            //"rptPortInfoSpeed"    
            ArrayList<OBNameValue> linkSpeedInfoList = new ArrayList<OBNameValue>();
            retVal.setLinkSpeedInfoList(linkSpeedInfoList);
            queryOid = oidInfo.getPortDiscardsIn();
            try
            {
                if(queryOid!=null && !queryOid.isEmpty())
                {
                    tmpList = walk(queryOid);
                    for(VariableBinding var : tmpList)
                    {
                        ArrayList<Integer> indexList = parseOid(var.getOid().toString(), queryOid);
                        if(indexList.size()==1)
                        {
                            OBNameValue value = new OBNameValue();
                            value.setName(indexList.get(0).toString());
                            value.setValue(var.getVariable().toString());
                            linkSpeedInfoList.add(value);
                        }
                        else
                        {
                            OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to get snmp data(host:%s, oid:%s). invalid format", getHost(), queryOid));
                        }
                    }
                }
            }
            catch(Exception e)
            {
                OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to get snmp data(host:%s, oid:%s)", getHost(), queryOid));
                throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("failed to get snmp data(host:%s, oid:%s)", getHost(), queryOid));
            }
        }
        catch(OBException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, "general: " + e.getMessage());
        }
        finally
        {
            if(snmp!=null) closeSnmp(snmp);
        }
        
        return retVal;
    }
    
    private OBDtoRptInspectionSnmpAlteon getRptInspection(OBDtoOidRptInspection oidInfo) throws OBException
    {
     // OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("start. %s.", oid.toString()));
        OBDtoRptInspectionSnmpAlteon retVal = new OBDtoRptInspectionSnmpAlteon();
        
        Snmp snmp = null;
        String queryOid = "";
        List<VariableBinding> tmpList;
        try
        {
            snmp = openSnmp();
            //"rptFilterEnabledPort"
            ArrayList<Integer> enabledPortList = new ArrayList<Integer>();
            queryOid = oidInfo.getFilterEnabledPort();// 
            try
            {// enabled된 포트 리스트를 추출.
                if(queryOid!=null && !queryOid.isEmpty())
                {
                    tmpList = walk(queryOid);
                    for(VariableBinding var : tmpList)
                    {
                        int value = var.getVariable().toInt();
                        if(value==1)
                        {
                            ArrayList<Integer> oidList = parseOid(var.getOid().toString(), queryOid);
                            enabledPortList.add(oidList.get(0));
                        }
                    }
                }
            }
            catch(Exception e)
            {
                OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to get snmp data(host:%s, oid:%s)", getHost(), queryOid));
                throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("failed to get snmp data(host:%s, oid:%s)", getHost(), queryOid));
            }            
            
            //"rptFilterPortMatching"
            // port 에 매핑된 filter index를 차례대로 추출한다.
            ArrayList<OBDtoRptFilterInfo> filterInfoList = new ArrayList<OBDtoRptFilterInfo>();
            retVal.setFilterInfo(filterInfoList); 
            for(Integer portIndex:enabledPortList)
            {
                OBDtoRptFilterInfo filterInfo = new OBDtoRptFilterInfo();
                ArrayList<Integer> filtList = new ArrayList<Integer>();

                filterInfo.setFiltList(filtList);
                filterInfo.setPortIndex(portIndex);
                filterInfoList.add(filterInfo);
                
                queryOid = oidInfo.getFilterPortMatching() + "." + portIndex;
                OctetString var = (OctetString) getSnmp(snmp, queryOid);
                
                if(var!=null)
                {
                    ArrayList<Integer> memberList = parsePortList(var);
                    filterInfo.setFiltList(memberList);
                }
            }
            
            //"rptPoolMemberStatusAvail"
            ArrayList<OBNameValue> realServerStatusList = new ArrayList<OBNameValue>();
            retVal.setRealServerStatusList(realServerStatusList);
            queryOid = oidInfo.getPoolMemberStatus();// real server를 의미한다. DTO를 같이 쓰기 위해 poolmember를 사용함.
            System.out.println(queryOid);
            try
            {
                if(queryOid!=null && !queryOid.isEmpty())
                {                
                    tmpList = walk(queryOid);
                    for(VariableBinding var : tmpList)
                    {
                        
                        ArrayList<String> indexList = parseOidStringV295(var.getOid().toString(), queryOid);
                        
                        if(indexList.size()==1)
                        {
                            OBNameValue obj = new OBNameValue();
                            obj.setName(indexList.get(0).toString());
                            obj.setValue(var.getVariable().toString());
                            realServerStatusList.add(obj);
                        }
                        else
                        {
                            OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to get snmp data(host:%s, oid:%s). invalid format", getHost(), queryOid));
                        }
                    }
                }
            }
            catch(Exception e)
            {
                OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to get snmp data(host:%s, oid:%s)", getHost(), queryOid));
                throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("failed to get snmp data(host:%s, oid:%s)", getHost(), queryOid));
            }            

            //"rptVSStatusAvail"
            HashMap<String, OBNameValue> vsStatusHashMap = new HashMap<String, OBNameValue>();
            queryOid = oidInfo.getVirtualServerStatus();
            try
            {
                if(queryOid!=null && !queryOid.isEmpty())
                {
                    tmpList = walk(queryOid);
                    for(VariableBinding var : tmpList)
                    {
                        ArrayList<String> indexList = parseOidStringV295(var.getOid().toString(), queryOid);
                        
                        if(indexList.size()==2||indexList.size()==3)
                        {
                            String key=indexList.get(0)+"_"+indexList.get(1);
                            OBNameValue hashValue = vsStatusHashMap.get(key);
                            Integer currValue = var.getVariable().toInt();
                            if(hashValue==null)
                            {
                                OBNameValue obj = new OBNameValue();
                                obj.setName(key);
                                obj.setValue(currValue.toString());
                                vsStatusHashMap.put(key, obj);
                                continue;
                            }
                            // a. 상태를 업데이트 한다. 멤버중에 한개라도 running이면 running으로 설정한다. 
                            if(hashValue.getValue().equals(currValue.toString()))// block(1), running(2), failed(3), disabled(4), slowstart(5)
                            {
                                continue;
                            }
                            if(currValue.intValue()==2)
                            {
                                hashValue.setValue(currValue.toString());
                            }
                        }
                        else
                        {
                            OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to get snmp data(host:%s, oid:%s). invalid format", getHost(), queryOid));
                        }
                    }
                }
            }
            catch(Exception e)
            {
                OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to get snmp data(host:%s, oid:%s)", getHost(), queryOid));
                throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("failed to get snmp data(host:%s, oid:%s)", getHost(), queryOid));
            }            
            ArrayList<OBNameValue> virtualServerStatusList = new ArrayList<OBNameValue>(vsStatusHashMap.values());
            retVal.setVirtualServerStatusList(virtualServerStatusList);
        }
        catch(OBException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, "general: " + e.getMessage());
        }
        finally
        {
            if(snmp!=null) closeSnmp(snmp);
        }
        
        return retVal;
    }
    
    private ArrayList<Integer> parsePortList(OctetString members)
    {
        ArrayList<Integer> list = new ArrayList<Integer>();
        for(int i=0;i<members.length();i++)
        {
            Byte octect = members.get(i);
            if(octect == 0)
                continue;
            
            int base=0x80;
            Integer nodeID=0;
            for(int bit=7;bit>=0;bit--)
            {
                if((octect&base)>0)
                {
                    nodeID = i*8 + (8-bit);
                    if(nodeID!=2056)
                        list.add(nodeID);
                }
                base=base>>1;
            }
        }
        return list;
    }
    
    /**
     * Alteon 기준 각 값이 나타내는 상태 
     * INTEGER {blocked(1),running(2),failed(3),disabled(4),slowstart(5)}
     * 
     * @param state
     * @return
     */
    private Integer convertNodeStatus(Integer state)
    {
        switch(state)
        {
        case 2:
            return OBDefine.STATUS_AVAILABLE;
        case 3://FAILED
            return OBDefine.STATUS_UNAVAILABLE;
        case 4://Disabled
            return OBDefine.STATUS_DISABLE;
        default:
            return OBDefine.STATUS_UNAVAILABLE;
        }
    }
    
    private Integer convertNodeState(Integer state)
    {
        switch(state)
        {
        case 2:
            return OBDefine.MEMBER_STATE.ENABLE;
        case 3://DISABLED
            return OBDefine.MEMBER_STATE.DISABLE;
        default:
            return OBDefine.MEMBER_STATE.DISABLE;
        }
    }
    
    private Integer convertPoolMemberState(Integer state)
    {
        switch(state)
        {
        case 1:// enabled
            return OBDefine.STATE_ENABLE;
        case 2:// disalbed
            return OBDefine.STATE_DISABLE;
        default:
            return OBDefine.STATE_DISABLE;
        }
    }
    
    private Integer convertLbMethod(Integer lbMethod)
    {
        switch(lbMethod)
        {
        case 1:
            return OBDefine.LB_METHOD_ROUND_ROBIN;
        case 2:
            return OBDefine.LB_METHOD_LEAST_CONNECTION_MEMBER;
        case 4:
            return OBDefine.LB_METHOD_HASH;
        default:
            return OBDefine.COMMON_NOT_DEFINED;
        }
    }
    
    private String alteonSnmpHexStringDecode(String snmpStr)
    {
        String sTemp;
        
        if(snmpStr==null)
        {
            return null;
        }
        //snmp hex string format에 맞으면 변환한다. Alteon snmp string은 ":"으로 바이트를 구분하기도 하고 " "으로 구분하기도 한다. 규칙없음
        // FF:AD:09:EC:ED:03  이거나 FF AD 09 EC ED 03
        if(snmpStr.matches("([0-9a-fA-F]{2}[: ])+[0-9a-fA-F]{2}")) 
        {
            sTemp = snmpStr.replaceAll("[: ]", ""); //":"을 없앤다.
            byte [] bytes = OBParser.hexStringToBytes(sTemp); //byte로 바꾸는데 실패하면 null이 온다.
            
            if(bytes==null) //변환 실패하면 원래대로라도 저장하자
            {
                return snmpStr;
            }
            else //snmp에서 반환된 값이 정상적인 byte 였다! 
            {
                sTemp = OBParser.ms949ByteToString(bytes);
                if(sTemp==null) //byte가 UTF-8이 아니던가 해서 변환 실패하면 원래대로라도 쓰자.
                {
                    return snmpStr;
                }
                else //제대로 변환
                {
                    return sTemp;
                }
            }
        }
        else
        {
            return snmpStr; //Alteon snmp hex 형식이 아니므로 일반 ascii 문자열이다. 그냥 리턴하면 된다.
        }
    }
    private OBDtoAdcPoolAlteon getPoolInfo(String poolID, ArrayList<DtoSnmpPoolAlteon> snmpPool, ArrayList<DtoSnmpNodeAlteon> snmpNode)
    {
        OBDtoAdcPoolAlteon obj = new OBDtoAdcPoolAlteon();
        for(int i=0;i<snmpPool.size();i++)
        {
            DtoSnmpPoolAlteon pool = snmpPool.get(i);
            if(pool.getPoolID().equals(poolID))
            {
                obj.setAlteonId(pool.getPoolID());
                obj.setLbMethod(pool.getPoolLbMethod());
                obj.setBakType(pool.getBakType());
                obj.setBakID(pool.getBakID());
                obj.setMemberList(getMemberList(pool.getMemberIDs(), pool.getMemberState(), snmpNode));
                obj.setName(pool.getPoolName());
                return obj;
            }
        }
        obj.setAlteonId(poolID);
        ArrayList<String> dummyIDs = new ArrayList<String>();
        ArrayList<Integer> dummyStates = new ArrayList<Integer>();
        obj.setMemberList(getMemberList(dummyIDs, dummyStates, snmpNode));
        return obj;
    }
    
    public Integer getVirtServiceIndex(int adcType, String swVersion, String vsID, Integer svcPort) throws OBException
    {//
        Integer retVal = 0;
        
        OBSnmpOidDB oidDB = new OBSnmpOidDB();
        try
        {
            DtoOidInfoAdcResc oid = oidDB.getAdcResource(adcType, swVersion);
            retVal = getVirtServiceIndex(oid.getServiceVirtPort(), vsID, svcPort);
            return retVal;          
        }
        catch(OBException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
        } 
    }
    
    private Integer getVirtServiceIndex(String oid, String vsID, Integer svcPort)  throws OBException
    {
        try
        {
            List<VariableBinding> tmpList;
            
            String vsEnhId = OBParser.oidAlteonEnh(vsID);
            
            // virtual server ID 추출
            String reqOid = oid+vsEnhId;
            
            tmpList = walk(reqOid);
            for(VariableBinding var:tmpList)
            {
                Integer portValue=var.getVariable().toInt();
                if(portValue.intValue()==svcPort.intValue())
                {
                    ArrayList<String> oidList = parseOidString(var.getOid().toString(), reqOid);
                    Integer svcIndex = Integer.parseInt(oidList.get(0));
                    return svcIndex;
                }
            }   
            OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to get svc index. reqOid:%s", reqOid));
            return 0;
        }
        catch(OBException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());

        }    
    }
    
    /**
     * service member의 상태 정보를 추출한다. Key: vsID+svcIndex+nodeIndex
     * @param vsID
     * @param oid
     * @param community
     * @return
     * @throws OBException
     */
    
    public OBDtoConnectionData  getVServiceConns(int adcType, String swVersion, String vsID, Integer virtPort, Integer serviceIndex)  throws OBException
    {
        OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcType:%d, vsID:%s, virtPort:%d", adcType, vsID, virtPort));
        
        try
        {
            OBSnmpOidDB oidDB = new OBSnmpOidDB();
            DtoOidStatVirtService oid;
            oid = oidDB.getStatVirtService(adcType, swVersion);
            
            OBDtoConnectionData result = getVServiceConns(vsID, serviceIndex, oid);
            
            OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", result));
            return result;
        }
        catch(OBException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());

        }
    }
    
    public OBDtoMonTrafficVServer  getTrafficVServerInfo(int adcType, String swVersion, String vsID, Integer svcIndex)  throws OBException
    {
        OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcType:%d, swVersion:%s", adcType, swVersion));
        OBSnmpOidDB oidDB = new OBSnmpOidDB();
        
        try
        {
            DtoOidStatVirtService oidInfo = oidDB.getStatVirtService(adcType, swVersion);
//          Integer svcIndex = getVirtServiceIndex(adcInfo, vsID, svcPort, db);
            OBDtoMonTrafficVServer retVal = getTrafficVServerInfo(oidInfo, vsID, svcIndex);
            return retVal;          
        }
        catch(OBException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
        } 
    }
    
    private OBDtoMonTrafficVServer getTrafficVServerInfo(DtoOidStatVirtService oid, String vsID, Integer svcIndex)  throws OBException
    {

        try
        {
            OBDtoMonTrafficVServer retVal = new OBDtoMonTrafficVServer();
            List<VariableBinding> tmpList;
            
            String vsEnhId = OBParser.oidAlteonEnh(vsID)+"."+svcIndex;
            
            String reqOid = oid.getTotConns()+vsEnhId;
            
            // virtual server ID 
            retVal.setAlteonID(vsID);
            
            Long value = 0L;
            // total conns 추출.
            // total conns 추출.
            tmpList = walk(reqOid);
            if(tmpList != null)
            {
                Long tmpValue=0L;
                for(int i=0;i<tmpList.size();i++)
                {
                    VariableBinding var=tmpList.get(i);
                    tmpValue+=var.getVariable().toLong();
                }
                retVal.setTotConns(tmpValue);
            }
            else
            {
                retVal.setTotConns(value);
            }
   
            
            reqOid = oid.getMaxConns()+vsEnhId;
            
            // max conns
            tmpList = walk(reqOid);
            if(tmpList != null)
            {
                Long tmpValue=0L;
                for(int i=0;i<tmpList.size();i++)
                {
                    VariableBinding var=tmpList.get(i);
                    tmpValue+=var.getVariable().toLong();
                }
                retVal.setMaxConns(tmpValue);
            }
            else
            {
                retVal.setMaxConns(value);
            }
            
            reqOid = oid.getCurConns()+vsEnhId;
            
            // cur conns
            tmpList = walk(reqOid);
            if(tmpList != null)
            {
                Long tmpValue=0L;
                for(int i=0;i<tmpList.size();i++)
                {
                    VariableBinding var=tmpList.get(i);
                    tmpValue+=var.getVariable().toLong();
                }
                retVal.setCurConns(tmpValue);
            }
            else
            {
                retVal.setCurConns(value);
            }
            
            reqOid = oid.getBytesIn()+vsEnhId;
            
            // bytes in 추출
            tmpList = walk(reqOid);
            if(tmpList != null)
            {
                Long tmpValue=0L;
                for(int i=0;i<tmpList.size();i++)
                {
                    VariableBinding var=tmpList.get(i);
                    tmpValue+=var.getVariable().toLong();
                }
                retVal.setBytesIn(tmpValue);
            }
            else
            {
                retVal.setBytesIn(value);
            }
    
            return retVal;
        }
        catch(OBException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
        }    
    }
    
    
}
