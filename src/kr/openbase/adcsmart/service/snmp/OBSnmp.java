package kr.openbase.adcsmart.service.snmp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.ScopedPDU;
import org.snmp4j.Snmp;
import org.snmp4j.Target;
import org.snmp4j.TransportMapping;
import org.snmp4j.UserTarget;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.MPv3;
import org.snmp4j.mp.MessageProcessingModel;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.security.AuthMD5;
import org.snmp4j.security.AuthSHA;
import org.snmp4j.security.Priv3DES;
import org.snmp4j.security.PrivAES128;
import org.snmp4j.security.PrivAES192;
import org.snmp4j.security.PrivAES256;
import org.snmp4j.security.PrivDES;
import org.snmp4j.security.SecurityLevel;
import org.snmp4j.security.SecurityModels;
import org.snmp4j.security.SecurityProtocols;
import org.snmp4j.security.USM;
import org.snmp4j.security.UsmUser;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.Null;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.Variable;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultTcpTransportMapping;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import kr.openbase.adcsmart.service.dto.OBDtoAdcSnmpInfo;
import kr.openbase.adcsmart.service.snmp.dto.DtoOidInfoAdcResc;
import kr.openbase.adcsmart.service.utility.OBDateTime;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBSystemLog;
import kr.openbase.adcsmart.service.utility.OBUtility;

public class OBSnmp
{
    public static Integer    VERSION_1         = SnmpConstants.version1;
    public static Integer    VERSION_2C        = SnmpConstants.version2c;
    public static Integer    VERSION_3         = SnmpConstants.version3;
    private Integer          snmpVersion       = OBSnmp.VERSION_2C;
    private String           host;
    private String           transportProtocol = "UDP";
    private Integer          snmpPort          = 161;
    private Integer          timeout           = 1500;
    private Integer          retry             = 2;
    private OBDtoAdcSnmpInfo authInfo;
    public USM        usm               = new USM(SecurityProtocols.getInstance(), new OctetString(MPv3.createLocalEngineID()), 0); // v3

    public void closeSnmp(Snmp snmp) throws OBException
    {
        try
        {
            if(snmp != null)
            {
                snmp.close();
            }
        }
        catch(Exception e)
        {
            throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("failed to close session. message:%s", e.getMessage()));
        }
    }

    public Snmp openSnmp() throws OBException
    {
        TransportMapping<?> transport;
        try
        {
            if(this.getTransportProtocol().equalsIgnoreCase("UDP"))
            {
                transport = new DefaultUdpTransportMapping();
            }
            else
            {
                transport = new DefaultTcpTransportMapping();
            }
        }
        catch(Exception e)
        {
            throw new OBException(e.getMessage());
        }
        Snmp snmp = new Snmp(transport);
        try
        {
            snmp.listen();
        }
        catch(IOException e)
        {
            throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("failed to put all associated transport mappings into listen mode. message:%s", e.getMessage()));
        }
        return snmp;
    }

    public Object getSnmp(Snmp snmp, String targetOID) throws OBException
    {
        OID oid = new OID(targetOID);
        Object ret = null;
        Address address = GenericAddress.parse(this.getTransportProtocol() + ":" + this.getHost() + "/" + this.getSnmpPort());
        PDU pdu; // v2,v3 common
        Target target; // v2,v3 common
        CommunityTarget targetV2;

        UsmUser user = null; // v3
        UserTarget targetV3; // v3

        if(this.snmpVersion.equals(VERSION_3))
        {
            // 보안레벨 결정을 위한 접속정보 변환
            OctetString securityName = null;
            OID authenticationProtocol = AuthMD5.ID;
            OctetString authenticationPassphrase = null;
            OID privacyProtocol = PrivDES.ID;
            OctetString privacyPassphrase = null;
            
            // securityName 셋
            if (authInfo.getSecurityName() != null && !authInfo.getSecurityName().isEmpty())
                securityName = new OctetString(authInfo.getSecurityName());
            
            // auth protocol 셋
            if (authInfo.getAuthProto() != null && !authInfo.getAuthProto().isEmpty())
            {
                if ("md5".equalsIgnoreCase(authInfo.getAuthProto()))
                    authenticationProtocol = AuthMD5.ID;
                else if ("sha".equalsIgnoreCase(authInfo.getAuthProto()))
                    authenticationProtocol = AuthSHA.ID;
                else if ("none".equalsIgnoreCase(authInfo.getAuthProto()))
                    authenticationProtocol = null;
            }
            
            // auth password 셋
            if (authInfo.getAuthPassword() != null && !authInfo.getAuthPassword().isEmpty())
                authenticationPassphrase = new OctetString(authInfo.getAuthPasswordDecrypt());
            
            // priv protocol 셋
            if (authInfo.getPrivProto() != null && !authInfo.getPrivProto().isEmpty())
            {
                if ("des".equalsIgnoreCase(authInfo.getPrivProto()))
                    privacyProtocol = PrivDES.ID;
                else if ("des3".equalsIgnoreCase(authInfo.getPrivProto()))
                    privacyProtocol = Priv3DES.ID;
                else if ("aes128".equalsIgnoreCase(authInfo.getPrivProto()))
                    privacyProtocol = PrivAES128.ID;
                else if ("aes192".equalsIgnoreCase(authInfo.getPrivProto()))
                    privacyProtocol = PrivAES192.ID;
                else if ("aes256".equalsIgnoreCase(authInfo.getPrivProto()))
                    privacyProtocol = PrivAES256.ID;
                else if ("none".equalsIgnoreCase(authInfo.getPrivProto()) || authenticationProtocol == null)
                    privacyProtocol = null;
            }

            // priv password 셋
            if (authInfo.getPrivPassword() != null && !authInfo.getPrivPassword().isEmpty())
                privacyPassphrase = new OctetString(authInfo.getPrivPasswordDecrypt());
            
            // 보안레벨 판단: authPassword, privPassword 존재여부로
            int securityLevel = SecurityLevel.AUTH_PRIV;
            if (authenticationPassphrase == null && privacyPassphrase == null)
                securityLevel = SecurityLevel.NOAUTH_NOPRIV;
            else if (authenticationPassphrase != null && privacyPassphrase == null)
                securityLevel = SecurityLevel.AUTH_NOPRIV;
            else if (authenticationPassphrase != null && privacyPassphrase != null)
                securityLevel = SecurityLevel.AUTH_PRIV;
            else
                throw new OBException(OBException.ERRCODE_SYSTEM_PARAMETER, "authInfo: " + authInfo);
            
            ScopedPDU pduV3 = new ScopedPDU();
            // pduV3.setContextEngineID(contextEngineId); //<-- 세팅하면 안됨!!! unknown pdu handlers 나옴.
            // pduV3.add(new VariableBinding(oid));

            user = new UsmUser(securityName, authenticationProtocol,
                    authenticationPassphrase, privacyProtocol, privacyPassphrase);

            usm.addUser(new OctetString(this.authInfo.getSecurityName()), user); // 반드시! security name 찾을 수 없다고 나옴!!
            // SecurityModels.getInstance().addSecurityModel(usm);
            MultiThreadedSecurityModels mtm = new MultiThreadedSecurityModels();
            mtm.addSecurityModel(usm);
            MessageProcessingModel mpm = snmp.getMessageProcessingModel(MPv3.ID);
            if(mpm instanceof MPv3)
            {
                ((MPv3) mpm).setSecurityModels(mtm);
            }
            else
            {
                throw new RuntimeException("SNMPv3 impossible processing.");
            }

            targetV3 = new UserTarget();
            targetV3.setVersion(VERSION_3);
            targetV3.setAddress(address);
            targetV3.setSecurityLevel(securityLevel);
            targetV3.setSecurityName(securityName);
            targetV3.setTimeout(this.getTimeout());
            // targetV3.setRetries(?);

            target = targetV3;
            pdu = pduV3;
        }
        else
        // v1 and v2
        {
            PDU pduV2 = new PDU();
            targetV2 = new CommunityTarget();
            targetV2.setCommunity(new OctetString(this.authInfo.getRcomm()));
            targetV2.setAddress(address);
            targetV2.setVersion(this.getSnmpVersion());
            targetV2.setTimeout(this.getTimeout());

            target = targetV2;
            pdu = pduV2;
        }

        pdu.addOID(new VariableBinding(oid));
        pdu.setType(PDU.GET);
        pdu.setNonRepeaters(0);

        ResponseEvent resp;
        try
        {
            resp = snmp.send(pdu, target);
            PDU respPDU = resp.getResponse();
            if(respPDU == null)
            {
                OBDateTime.Sleep(100);
                resp = snmp.send(pdu, target);

                OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM, String.format("2nd try to get snmp data. host:%s", this.host));
                respPDU = resp.getResponse();
                if(respPDU == null)
                {
                    OBDateTime.Sleep(100);
                    resp = snmp.send(pdu, target);
                    OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM, String.format("3rd try to get snmp data. host:%s", this.host));
                    respPDU = resp.getResponse();
                    if(respPDU == null)
                    {
                        throw new OBException(OBException.ERRCODE_SYSTEM_GETSNMP, String.format("failed to get snmp. host:%s, oid:%s, version:%d", this.getHost(), targetOID, this.getSnmpVersion()));
                    }
                }
            }
            Vector<? extends VariableBinding> vbs = respPDU.getVariableBindings();
            // for (VariableBinding vb : vbs)
            // {
            // System.out.println(vb + " ," + vb.getVariable().getSyntaxString());
            // }
            if(vbs.size() > 0)
            {
                VariableBinding vb = (VariableBinding) vbs.get(0);
                ret = vb.getVariable();
            }
            else
            {
                ret = null;
            }
            return ret;
        }
        catch(OBException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("%s", e.getMessage()));
        }
    }

    public Object get(String targetOID, String community) throws OBException
    {
        OID oid = new OID(targetOID);
        Object ret = null;
        TransportMapping<?> transport;

        try
        {
            if(this.getTransportProtocol().equalsIgnoreCase("UDP"))
            {
                transport = new DefaultUdpTransportMapping();
            }
            else
            {
                transport = new DefaultTcpTransportMapping();
            }
        }
        catch(Exception e)
        {
            throw new OBException(e.getMessage());
        }

        PDU pdu; // v2,v3 common
        Target target; // v2,v3 common

        CommunityTarget targetV2; // v2
        UsmUser user = null; // v3
        UserTarget targetV3; // v3

        Address address = GenericAddress.parse(this.getTransportProtocol() + ":" + this.getHost() + "/" + this.getSnmpPort());

        if(this.snmpVersion.equals(VERSION_3))
        {
         // 보안레벨 결정을 위한 접속정보 변환
            OctetString securityName = null;
            OID authenticationProtocol = AuthMD5.ID;
            OctetString authenticationPassphrase = null;
            OID privacyProtocol = PrivDES.ID;
            OctetString privacyPassphrase = null;
            
            // securityName 셋
            if (authInfo.getSecurityName() != null && !authInfo.getSecurityName().isEmpty())
                securityName = new OctetString(authInfo.getSecurityName());
            
            // auth protocol 셋
            if (authInfo.getAuthProto() != null && !authInfo.getAuthProto().isEmpty())
            {
                if ("md5".equalsIgnoreCase(authInfo.getAuthProto()))
                    authenticationProtocol = AuthMD5.ID;
                else if ("sha".equalsIgnoreCase(authInfo.getAuthProto()))
                    authenticationProtocol = AuthSHA.ID;
                else if ("none".equalsIgnoreCase(authInfo.getAuthProto()))
                    authenticationProtocol = null;
            }
            
            // auth password 셋
            if (authInfo.getAuthPassword() != null && !authInfo.getAuthPassword().isEmpty())
                authenticationPassphrase = new OctetString(authInfo.getAuthPasswordDecrypt());
            
            // priv protocol 셋
            if (authInfo.getPrivProto() != null && !authInfo.getPrivProto().isEmpty())
            {
                if ("des".equalsIgnoreCase(authInfo.getPrivProto()))
                    privacyProtocol = PrivDES.ID;
                else if ("des3".equalsIgnoreCase(authInfo.getPrivProto()))
                    privacyProtocol = Priv3DES.ID;
                else if ("aes128".equalsIgnoreCase(authInfo.getPrivProto()))
                    privacyProtocol = PrivAES128.ID;
                else if ("aes192".equalsIgnoreCase(authInfo.getPrivProto()))
                    privacyProtocol = PrivAES192.ID;
                else if ("aes256".equalsIgnoreCase(authInfo.getPrivProto()))
                    privacyProtocol = PrivAES256.ID;
                else if ("none".equalsIgnoreCase(authInfo.getPrivProto()) || authenticationProtocol == null)
                    privacyProtocol = null;
            }

            // priv password 셋
            if (authInfo.getPrivPassword() != null && !authInfo.getPrivPassword().isEmpty())
                privacyPassphrase = new OctetString(authInfo.getPrivPasswordDecrypt());
            
            // 보안레벨 판단: authPassword, privPassword 존재여부로
            int securityLevel = SecurityLevel.AUTH_PRIV;
            if (authenticationPassphrase == null && privacyPassphrase == null)
                securityLevel = SecurityLevel.NOAUTH_NOPRIV;
            else if (authenticationPassphrase != null && privacyPassphrase == null)
                securityLevel = SecurityLevel.AUTH_NOPRIV;
            else if (authenticationPassphrase != null && privacyPassphrase != null)
                securityLevel = SecurityLevel.AUTH_PRIV;
            else
                throw new OBException(OBException.ERRCODE_SYSTEM_PARAMETER, "authInfo: " + authInfo);
            
            ScopedPDU pduV3 = new ScopedPDU();
            // pduV3.setContextEngineID(contextEngineId); //<-- 세팅하면 안됨!!! unknown pdu handlers 나옴.
            // pduV3.add(new VariableBinding(oid));

            user = new UsmUser(securityName, authenticationProtocol,
                    authenticationPassphrase, privacyProtocol, privacyPassphrase);

            targetV3 = new UserTarget();
            targetV3.setVersion(VERSION_3);
            targetV3.setAddress(address);
            targetV3.setSecurityLevel(securityLevel);
            targetV3.setSecurityName(securityName);
            targetV3.setTimeout(this.getTimeout());
            // targetV3.setRetries(?);

            target = targetV3;
            pdu = pduV3;
        }
        else
        // v1 and v2
        {
            PDU pduV2 = new PDU();
            targetV2 = new CommunityTarget();
            targetV2.setCommunity(new OctetString(this.authInfo.getRcomm()));
            targetV2.setAddress(address);
            targetV2.setVersion(this.getSnmpVersion());
            targetV2.setTimeout(this.getTimeout());
            target = targetV2;
            pdu = pduV2;
        }

        pdu.addOID(new VariableBinding(oid));
        pdu.setType(PDU.GET);
        pdu.setNonRepeaters(0);

        Snmp snmp = new Snmp(transport);
        try
        {
            if(this.snmpVersion.equals(VERSION_3))
            {
                usm.addUser(new OctetString(this.authInfo.getSecurityName()), user); // 반드시! security name 찾을 수 없다고 나옴!!
                MultiThreadedSecurityModels mtm = new MultiThreadedSecurityModels();
                mtm.addSecurityModel(usm);
                MessageProcessingModel mpm = snmp.getMessageProcessingModel(MPv3.ID);
                if(mpm instanceof MPv3)
                {
                    ((MPv3) mpm).setSecurityModels(mtm);
                }
                else
                {
                    throw new RuntimeException("SNMPv3 impossible processing.");
                }
            }
            snmp.listen();
        }
        catch(IOException e)
        {
            throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("failed to put all associated transport mappings into listen mode. message:%s", e.getMessage()));
        }

        ResponseEvent resp;
        try
        {
            resp = snmp.send(pdu, target);
        }
        catch(IOException e)
        {
            closeSnmp(snmp);
            throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("%s", e.getMessage()));
        }
        PDU respPDU = resp.getResponse();
        if(respPDU == null)
        {
            try
            {
                OBDateTime.Sleep(100);
                resp = snmp.send(pdu, target);
            }
            catch(IOException e)
            {
                closeSnmp(snmp);
                throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("%s", e.getMessage()));
            }
            respPDU = resp.getResponse();
            if(respPDU == null)
            {
                try
                {
                    OBDateTime.Sleep(100);
                    resp = snmp.send(pdu, target);
                }
                catch(IOException e)
                {
                    closeSnmp(snmp);
                    throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("%s", e.getMessage()));
                }
                respPDU = resp.getResponse();
                if(respPDU == null)
                {
                    closeSnmp(snmp);
                    return null;
                }
            }
        }

        Vector<?> vbs = respPDU.getVariableBindings();
        if(vbs.size() > 0)
        {
            VariableBinding vb = (VariableBinding) vbs.get(0);
            ret = vb.getVariable();
        }
        else
        {
            ret = null;
        }

        closeSnmp(snmp);
        return ret;
    }

    public List<VariableBinding> getList(List<String> oid_list, String community) throws OBException
    {
        List<VariableBinding> ret = new ArrayList<VariableBinding>();
        TransportMapping<?> transport;

        try
        {
            if(this.getTransportProtocol().equalsIgnoreCase("UDP"))
            {
                transport = new DefaultUdpTransportMapping();
            }
            else
            {
                transport = new DefaultTcpTransportMapping();
            }
        }
        catch(Exception e)
        {
            throw new OBException(e.getMessage());
        }

        PDU pdu; // v2,v3 common
        Target target; // v2,v3 common

        CommunityTarget targetV2; // v2
        UsmUser user = null; // v3
        UserTarget targetV3; // v3
        Address address = GenericAddress.parse(this.getTransportProtocol() + ":" + this.getHost() + "/" + this.getSnmpPort());

        if(this.snmpVersion.equals(VERSION_3))
        {
         // 보안레벨 결정을 위한 접속정보 변환
            OctetString securityName = null;
            OID authenticationProtocol = AuthMD5.ID;
            OctetString authenticationPassphrase = null;
            OID privacyProtocol = PrivDES.ID;
            OctetString privacyPassphrase = null;
            
            // securityName 셋
            if (authInfo.getSecurityName() != null && !authInfo.getSecurityName().isEmpty())
                securityName = new OctetString(authInfo.getSecurityName());
            
            // auth protocol 셋
            if (authInfo.getAuthProto() != null && !authInfo.getAuthProto().isEmpty())
            {
                if ("md5".equalsIgnoreCase(authInfo.getAuthProto()))
                    authenticationProtocol = AuthMD5.ID;
                else if ("sha".equalsIgnoreCase(authInfo.getAuthProto()))
                    authenticationProtocol = AuthSHA.ID;
                else if ("none".equalsIgnoreCase(authInfo.getAuthProto()))
                    authenticationProtocol = null;
            }
            
            // auth password 셋
            if (authInfo.getAuthPassword() != null && !authInfo.getAuthPassword().isEmpty())
                authenticationPassphrase = new OctetString(authInfo.getAuthPasswordDecrypt());
            
            // priv protocol 셋
            if (authInfo.getPrivProto() != null && !authInfo.getPrivProto().isEmpty())
            {
                if ("des".equalsIgnoreCase(authInfo.getPrivProto()))
                    privacyProtocol = PrivDES.ID;
                else if ("des3".equalsIgnoreCase(authInfo.getPrivProto()))
                    privacyProtocol = Priv3DES.ID;
                else if ("aes128".equalsIgnoreCase(authInfo.getPrivProto()))
                    privacyProtocol = PrivAES128.ID;
                else if ("aes192".equalsIgnoreCase(authInfo.getPrivProto()))
                    privacyProtocol = PrivAES192.ID;
                else if ("aes256".equalsIgnoreCase(authInfo.getPrivProto()))
                    privacyProtocol = PrivAES256.ID;
                else if ("none".equalsIgnoreCase(authInfo.getPrivProto()) || authenticationProtocol == null)
                    privacyProtocol = null;
            }

            // priv password 셋
            if (authInfo.getPrivPassword() != null && !authInfo.getPrivPassword().isEmpty())
                privacyPassphrase = new OctetString(authInfo.getPrivPasswordDecrypt());
            
            // 보안레벨 판단: authPassword, privPassword 존재여부로
            int securityLevel = SecurityLevel.AUTH_PRIV;
            if (authenticationPassphrase == null && privacyPassphrase == null)
                securityLevel = SecurityLevel.NOAUTH_NOPRIV;
            else if (authenticationPassphrase != null && privacyPassphrase == null)
                securityLevel = SecurityLevel.AUTH_NOPRIV;
            else if (authenticationPassphrase != null && privacyPassphrase != null)
                securityLevel = SecurityLevel.AUTH_PRIV;
            else
                throw new OBException(OBException.ERRCODE_SYSTEM_PARAMETER, "authInfo: " + authInfo);
            
            ScopedPDU pduV3 = new ScopedPDU();
            // pduV3.setContextEngineID(contextEngineId); //<-- 세팅하면 안됨!!! unknown pdu handlers 나옴.
            // pduV3.add(new VariableBinding(oid));

            user = new UsmUser(securityName, authenticationProtocol,
                    authenticationPassphrase, privacyProtocol, privacyPassphrase);

            targetV3 = new UserTarget();
            targetV3.setVersion(VERSION_3);
            targetV3.setAddress(address);
            targetV3.setSecurityLevel(securityLevel);
            targetV3.setSecurityName(securityName);
            targetV3.setTimeout(this.getTimeout());
            // targetV3.setRetries(?);

            target = targetV3;
            pdu = pduV3;
        }
        else
        // v1 and v2
        {
            PDU pduV2 = new PDU();
            targetV2 = new CommunityTarget();
            targetV2.setCommunity(new OctetString(this.authInfo.getRcomm()));
            targetV2.setAddress(address);
            targetV2.setVersion(this.getSnmpVersion());
            targetV2.setTimeout(this.getTimeout());
            target = targetV2;
            pdu = pduV2;
        }

        pdu.setType(PDU.GET);
        // put the oids you want to get
        List<VariableBinding> ivbs = new ArrayList<VariableBinding>();
        for(String o : oid_list)
        {
            OID oid = new OID(o);
            ivbs.add(new VariableBinding(oid));
        }
        pdu.addAll(ivbs.toArray(new VariableBinding[] {}));
        pdu.setMaxRepetitions(10);
        pdu.setNonRepeaters(0);

        Snmp snmp = new Snmp(transport);
        try
        {
            if(this.snmpVersion.equals(VERSION_3))
            {

                usm.addUser(new OctetString(this.authInfo.getSecurityName()), user); // 반드시! security name 찾을 수 없다고 나옴!!
                MultiThreadedSecurityModels mtm = new MultiThreadedSecurityModels();
                mtm.addSecurityModel(usm);
                MessageProcessingModel mpm = snmp.getMessageProcessingModel(MPv3.ID);
                if(mpm instanceof MPv3)
                {
                    ((MPv3) mpm).setSecurityModels(mtm);
                }
                else
                {
                    throw new RuntimeException("SNMPv3 impossible processing.");
                }
            }
            snmp.listen();
        }
        catch(IOException e)
        {
            throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("failed to put all associated transport mappings into listen mode. message:%s", e.getMessage()));
        }

        // send the PDU
        ResponseEvent responseEvent;
        try
        {
            responseEvent = snmp.send(pdu, target);
        }
        catch(IOException e)
        {
            closeSnmp(snmp);
            throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("failed to send a PDU to the %s. message:%s", this.host, e.getMessage()));
        }

        // extract the response PDU (could be null if timed out)
        PDU responsePDU = responseEvent.getResponse();
        Vector<?> vbs = responsePDU.getVariableBindings();
        if(vbs.size() > 0)
        {
            List<OID> rec_oid = new ArrayList<OID>();
            for(int i = 0; i < vbs.size(); i++)
            {
                VariableBinding v = (VariableBinding) vbs.get(i);
                if(!rec_oid.contains(v.getOid()))
                {
                    rec_oid.add(v.getOid());
                    ret.add((VariableBinding) vbs.get(i));
                }
            }
        }
        closeSnmp(snmp);
        return ret;
    }

    public boolean set(OID targetOID, Variable value, String community) throws OBException
    {
        OID oid = new OID(targetOID);
        boolean ret = false;
        TransportMapping<?> transport;

        try
        {
            if(this.getTransportProtocol().equalsIgnoreCase("UDP"))
            {
                transport = new DefaultUdpTransportMapping();
            }
            else
            {
                transport = new DefaultTcpTransportMapping();
            }
        }
        catch(Exception e)
        {
            throw new OBException(e.getMessage());
        }

        PDU pdu; // v2,v3 common
        Target target; // v2,v3 common

        CommunityTarget targetV2; // v2
        UsmUser user = null; // v3
        UserTarget targetV3; // v3

        Address address = GenericAddress.parse(this.getTransportProtocol() + ":" + this.getHost() + "/" + this.getSnmpPort());
        if(this.snmpVersion.equals(VERSION_3))
        {
         // 보안레벨 결정을 위한 접속정보 변환
            OctetString securityName = null;
            OID authenticationProtocol = AuthMD5.ID;
            OctetString authenticationPassphrase = null;
            OID privacyProtocol = PrivDES.ID;
            OctetString privacyPassphrase = null;
            
            // securityName 셋
            if (authInfo.getSecurityName() != null && !authInfo.getSecurityName().isEmpty())
                securityName = new OctetString(authInfo.getSecurityName());
            
            // auth protocol 셋
            if (authInfo.getAuthProto() != null && !authInfo.getAuthProto().isEmpty())
            {
                if ("md5".equalsIgnoreCase(authInfo.getAuthProto()))
                    authenticationProtocol = AuthMD5.ID;
                else if ("sha".equalsIgnoreCase(authInfo.getAuthProto()))
                    authenticationProtocol = AuthSHA.ID;
                else if ("none".equalsIgnoreCase(authInfo.getAuthProto()))
                    authenticationProtocol = null;
            }
            
            // auth password 셋
            if (authInfo.getAuthPassword() != null && !authInfo.getAuthPassword().isEmpty())
                authenticationPassphrase = new OctetString(authInfo.getAuthPasswordDecrypt());
            
            // priv protocol 셋
            if (authInfo.getPrivProto() != null && !authInfo.getPrivProto().isEmpty())
            {
                if ("des".equalsIgnoreCase(authInfo.getPrivProto()))
                    privacyProtocol = PrivDES.ID;
                else if ("des3".equalsIgnoreCase(authInfo.getPrivProto()))
                    privacyProtocol = Priv3DES.ID;
                else if ("aes128".equalsIgnoreCase(authInfo.getPrivProto()))
                    privacyProtocol = PrivAES128.ID;
                else if ("aes192".equalsIgnoreCase(authInfo.getPrivProto()))
                    privacyProtocol = PrivAES192.ID;
                else if ("aes256".equalsIgnoreCase(authInfo.getPrivProto()))
                    privacyProtocol = PrivAES256.ID;
                else if ("none".equalsIgnoreCase(authInfo.getPrivProto()) || authenticationProtocol == null)
                    privacyProtocol = null;
            }

            // priv password 셋
            if (authInfo.getPrivPassword() != null && !authInfo.getPrivPassword().isEmpty())
                privacyPassphrase = new OctetString(authInfo.getPrivPasswordDecrypt());
            
            // 보안레벨 판단: authPassword, privPassword 존재여부로
            int securityLevel = SecurityLevel.AUTH_PRIV;
            if (authenticationPassphrase == null && privacyPassphrase == null)
                securityLevel = SecurityLevel.NOAUTH_NOPRIV;
            else if (authenticationPassphrase != null && privacyPassphrase == null)
                securityLevel = SecurityLevel.AUTH_NOPRIV;
            else if (authenticationPassphrase != null && privacyPassphrase != null)
                securityLevel = SecurityLevel.AUTH_PRIV;
            else
                throw new OBException(OBException.ERRCODE_SYSTEM_PARAMETER, "authInfo: " + authInfo);
            
            ScopedPDU pduV3 = new ScopedPDU();
            // pduV3.setContextEngineID(contextEngineId); //<-- 세팅하면 안됨!!! unknown pdu handlers 나옴.
            // pduV3.add(new VariableBinding(oid));

            user = new UsmUser(securityName, authenticationProtocol,
                    authenticationPassphrase, privacyProtocol, privacyPassphrase);

            targetV3 = new UserTarget();
            targetV3.setVersion(VERSION_3);
            targetV3.setAddress(address);
            targetV3.setSecurityLevel(securityLevel);
            targetV3.setSecurityName(securityName);
            targetV3.setTimeout(this.getTimeout());
            // targetV3.setRetries(?);

            target = targetV3;
            pdu = pduV3;
        }
        else
        // v1 and v2
        {
            PDU pduV2 = new PDU();
            targetV2 = new CommunityTarget();
            targetV2.setCommunity(new OctetString(this.authInfo.getRcomm()));
            targetV2.setAddress(address);
            targetV2.setVersion(this.getSnmpVersion());
            targetV2.setTimeout(this.getTimeout());
            target = targetV2;
            pdu = pduV2;
        }

        pdu.setType(PDU.SET);
        pdu.add(new VariableBinding(oid, value));
        pdu.setNonRepeaters(0);

        Snmp snmp = new Snmp(transport);
        try
        {
            if(this.snmpVersion.equals(VERSION_3))
            {
                usm.addUser(new OctetString(this.authInfo.getSecurityName()), user); // 반드시! security name 찾을 수 없다고 나옴!!
                MultiThreadedSecurityModels mtm = new MultiThreadedSecurityModels();
                mtm.addSecurityModel(usm);
                MessageProcessingModel mpm = snmp.getMessageProcessingModel(MPv3.ID);
                if(mpm instanceof MPv3)
                {
                    ((MPv3) mpm).setSecurityModels(mtm);
                }
                else
                {
                    throw new RuntimeException("SNMPv3 impossible processing.");
                }
            }
            snmp.listen();
        }
        catch(IOException e)
        {
            throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("failed to put all associated transport mappings into listen mode. message:%s", e.getMessage()));
        }

        ResponseEvent resp;
        try
        {
            resp = snmp.set(pdu, target);
        }
        catch(IOException e)
        {
            closeSnmp(snmp);
            throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("failed to send a SET request to a taget(%s). message:%s", this.host, e.getMessage()));
        }
        PDU respPDU = resp.getResponse();
        if(respPDU == null)
        {
            closeSnmp(snmp);
            throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("OBSnmp:set: SNMP Timeout."));
        }
        else
        {
            Vector<?> vbs = respPDU.getVariableBindings();
            if(vbs.size() > 0)
            {
                VariableBinding vb = (VariableBinding) vbs.get(0);
                ret = vb.isException() ? false : true;
            }
            else
            {
                ret = false;
            }
        }
        closeSnmp(snmp);
        return ret;
    }

    public List<VariableBinding> walk(String targetOID, int itemNum, int retryNum) throws OBException
    {
        List<VariableBinding> tmpList = new ArrayList<VariableBinding>();
        for(int i = 0; i < retryNum; i++)
        {
            tmpList = walk(targetOID);
            if(tmpList.size() == itemNum)
                return tmpList;
            else
            {
                OBDateTime.Sleep(2000);
                continue;
            }
        }
        throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("failed to get data. host:%s, oid:%s, tmpSize:%d, itemNum:%d", this.host, targetOID, tmpList.size(), itemNum));
    }

    public List<VariableBinding> walk(String targetOID) throws OBException
    {
        OID oid = new OID(targetOID);
        List<VariableBinding> ret = new ArrayList<VariableBinding>();
        Address address = GenericAddress.parse(this.getTransportProtocol() + ":" + this.getHost() + "/" + this.getSnmpPort());

        PDU requestPDU; // v2,v3 common
        Target target; // v2,v3 common
        CommunityTarget targetV2; // v2
        UsmUser user = null; // v3
        UserTarget targetV3; // v3

        if(this.snmpVersion.equals(VERSION_3))
        {
         // 보안레벨 결정을 위한 접속정보 변환
            OctetString securityName = null;
            OID authenticationProtocol = AuthMD5.ID;
            OctetString authenticationPassphrase = null;
            OID privacyProtocol = PrivDES.ID;
            OctetString privacyPassphrase = null;
            
            // securityName 셋
            if (authInfo.getSecurityName() != null && !authInfo.getSecurityName().isEmpty())
                securityName = new OctetString(authInfo.getSecurityName());
            
            // auth protocol 셋
            if (authInfo.getAuthProto() != null && !authInfo.getAuthProto().isEmpty())
            {
                if ("md5".equalsIgnoreCase(authInfo.getAuthProto()))
                    authenticationProtocol = AuthMD5.ID;
                else if ("sha".equalsIgnoreCase(authInfo.getAuthProto()))
                    authenticationProtocol = AuthSHA.ID;
                else if ("none".equalsIgnoreCase(authInfo.getAuthProto()))
                    authenticationProtocol = null;
            }
            
            // auth password 셋
            if (authInfo.getAuthPassword() != null && !authInfo.getAuthPassword().isEmpty())
                authenticationPassphrase = new OctetString(authInfo.getAuthPasswordDecrypt());
            
            // priv protocol 셋
            if (authInfo.getPrivProto() != null && !authInfo.getPrivProto().isEmpty())
            {
                if ("des".equalsIgnoreCase(authInfo.getPrivProto()))
                    privacyProtocol = PrivDES.ID;
                else if ("des3".equalsIgnoreCase(authInfo.getPrivProto()))
                    privacyProtocol = Priv3DES.ID;
                else if ("aes128".equalsIgnoreCase(authInfo.getPrivProto()))
                    privacyProtocol = PrivAES128.ID;
                else if ("aes192".equalsIgnoreCase(authInfo.getPrivProto()))
                    privacyProtocol = PrivAES192.ID;
                else if ("aes256".equalsIgnoreCase(authInfo.getPrivProto()))
                    privacyProtocol = PrivAES256.ID;
                else if ("none".equalsIgnoreCase(authInfo.getPrivProto()) || authenticationProtocol == null)
                    privacyProtocol = null;
            }

            // priv password 셋
            if (authInfo.getPrivPassword() != null && !authInfo.getPrivPassword().isEmpty())
                privacyPassphrase = new OctetString(authInfo.getPrivPasswordDecrypt());
            
            // 보안레벨 판단: authPassword, privPassword 존재여부로
            int securityLevel = SecurityLevel.AUTH_PRIV;
            if (authenticationPassphrase == null && privacyPassphrase == null)
                securityLevel = SecurityLevel.NOAUTH_NOPRIV;
            else if (authenticationPassphrase != null && privacyPassphrase == null)
                securityLevel = SecurityLevel.AUTH_NOPRIV;
            else if (authenticationPassphrase != null && privacyPassphrase != null)
                securityLevel = SecurityLevel.AUTH_PRIV;
            else
                throw new OBException(OBException.ERRCODE_SYSTEM_PARAMETER, "authInfo: " + authInfo);
            
            ScopedPDU pduV3 = new ScopedPDU();
            // pduV3.setContextEngineID(contextEngineId); //<-- 세팅하면 안됨!!! unknown pdu handlers 나옴.
            // pduV3.add(new VariableBinding(oid));

            user = new UsmUser(securityName, authenticationProtocol,
                    authenticationPassphrase, privacyProtocol, privacyPassphrase);

            targetV3 = new UserTarget();
            targetV3.setVersion(VERSION_3);
            targetV3.setAddress(address);
            targetV3.setSecurityLevel(securityLevel);
            targetV3.setSecurityName(securityName);
            targetV3.setTimeout(this.getTimeout());
            // targetV3.setRetries(?);

            target = targetV3;
            requestPDU = pduV3;
        }
        else
        {
            PDU pduV2 = new PDU();
            targetV2 = new CommunityTarget();
            targetV2.setCommunity(new OctetString(this.authInfo.getRcomm()));
            targetV2.setAddress(address);
            targetV2.setVersion(this.getSnmpVersion());
            targetV2.setTimeout(this.getTimeout());
            target = targetV2;
            requestPDU = pduV2;
        }

        requestPDU.add(new VariableBinding(oid));
        requestPDU.setType(PDU.GETNEXT);

        TransportMapping<?> transport;
        try
        {
            if(this.getTransportProtocol().equalsIgnoreCase("UDP"))
            {
                transport = new DefaultUdpTransportMapping();
            }
            else
            {
                transport = new DefaultTcpTransportMapping();
            }
        }
        catch(Exception e)
        {
            // ByteArrayOutputStream out = new ByteArrayOutputStream();
            // PrintStream pinrtStream = new PrintStream(out);
            // e.printStackTrace(pinrtStream);
            // String stackTraceString = out.toString();
            throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("trace:%s, message:%s", OBUtility.getStackTrace(e), e.getMessage()));
        }

        Snmp snmp = new Snmp(transport);
        try
        {
            if(this.snmpVersion.equals(VERSION_3))
            {
                usm.addUser(new OctetString(this.authInfo.getSecurityName()), user);
                MultiThreadedSecurityModels mtm = new MultiThreadedSecurityModels();
                mtm.addSecurityModel(usm);
                MessageProcessingModel mpm = snmp.getMessageProcessingModel(MPv3.ID);
                if(mpm instanceof MPv3)
                {
                    ((MPv3) mpm).setSecurityModels(mtm);
                }
                else
                {
                    throw new RuntimeException("SNMPv3 impossible processing.");
                }
            }
            snmp.listen();

            boolean finished = false;

            OID lastOid = oid;

            while(!finished)
            {
                VariableBinding vb = null;

                ResponseEvent respEvt;
                try
                {
                    respEvt = snmp.send(requestPDU, target);
                }
                catch(Exception e)
                {
                    closeSnmp(snmp);
                    throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("1failed to send a PDU to the host:%s. message:%s", this.host, e.getMessage()));
                }
                PDU responsePDU = respEvt.getResponse();
                if(responsePDU == null)
                {
                    OBDateTime.Sleep(100);
                    try
                    {// 2번째 시도.
                        OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM, String.format("2nd try to get snmp data. host:%s", this.host));
                        respEvt = snmp.send(requestPDU, target);
                    }
                    catch(Exception e)
                    {
                        closeSnmp(snmp);
                        throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("2failed to send a PDU to the host:%s. oid:%s, message:%s", this.host, targetOID, e.getMessage()));
                    }
                    responsePDU = respEvt.getResponse();
                    if(responsePDU == null)
                    {
                        OBDateTime.Sleep(100);
                        try
                        {// 세번째 시도
                            OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM, String.format("3rd try to get snmp data. host:%s, oid:%s", this.host, targetOID));
                            respEvt = snmp.send(requestPDU, target);
                        }
                        catch(Exception e)
                        {
                            closeSnmp(snmp);
                            throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("3failed to send a PDU to the %s. oid:%s, message:%s", this.host, targetOID, e.getMessage()));
                        }
                        responsePDU = respEvt.getResponse();
                        if(responsePDU == null)
                        {
                            finished = true;
                            closeSnmp(snmp);
                            throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("responsePDU is null(host:%s, oid:%s)", this.host, targetOID));
                        }
                        else
                        {
                            vb = responsePDU.get(0);
                        }
                    }
                    else
                    {
                        vb = responsePDU.get(0);
                    }
                    // OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("responsePDU is null(host:%s, oid:%s)", this.host, targetOID));
                }

                vb = responsePDU.get(0);
                if(responsePDU.getErrorStatus() != 0)
                {
                    OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("responsePDU got a error(%d)(host:%s, oid:%s)", responsePDU.getErrorStatus(), this.host, targetOID));
                    finished = true;
                }
                else if(vb.getOid() == null)
                {
                    OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("vb's oid is null(host:%s, oid:%s)", this.host, targetOID));
                    finished = true;
                }
                else if(vb.getOid().size() < oid.size())
                {// OID가 다른 경우.. 작업을 끝낸다.
                 // OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("vb's oid is different1(oid:%s, retOid:%s)", targetOID, vb.getOid()));
                    finished = true;
                }
                else if(oid.leftMostCompare(oid.size(), vb.getOid()) != 0)
                {// OID가 다른 경우.. 작업을 끝낸다.
                 // OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("vb's oid is different2(oid:%s, retOid:%s)", targetOID, vb.getOid()));
                    finished = true;
                }
                else if(Null.isExceptionSyntax(vb.getVariable().getSyntax()))
                {
                    OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("vb syntax error(host:%s, oid:%s, getSyntax:%d)", this.host, targetOID, vb.getVariable().getSyntax()));
                    finished = true;
                }
                else if(vb.getOid().compareTo(oid) <= 0)
                {// OID가 다른 경우.. 작업을 끝낸다.
                 // OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("vb's oid is different3(oid:%s, retOid:%s)", targetOID, vb.getOid()));
                    finished = true;
                }
                else if(vb.getOid().compareTo(lastOid) == 0)
                {// OID가 다른 경우.. 작업을 끝낸다.
                    OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("invalid situation(lastOid:%s, retOid:%s)", lastOid, vb.getOid()));
                    finished = true;
                }
                else
                {
                    ret.add(vb);
                    lastOid = vb.getOid();
                    // Set up the variable binding for the next entry.
                    requestPDU.setRequestID(new Integer32(0));
                    requestPDU.set(0, vb);
                }
            }
            closeSnmp(snmp);
        }
        catch(RuntimeException e)
        {
            closeSnmp(snmp);
            throw new OBException(OBException.ERRCODE_SYSTEM_GETSNMP, "RuntimeException:" + e.getMessage());// , String.format("failed to get snmpwalk. message:%s", e.getMessage()));
        }
        catch(Exception e)
        {
            closeSnmp(snmp);
            throw new OBException(OBException.ERRCODE_SYSTEM_GETSNMP, "General exception");// , String.format("failed to get snmpwalk. message:%s", e.getMessage()));
        }
        return ret;
    }

    public VariableBinding getNext(OID targetOID, String community) throws OBException
    {
        OID oid = new OID(targetOID);
        VariableBinding ret = null;
        TransportMapping<?> transport;

        // create transport
        try
        {
            if(this.getTransportProtocol().equalsIgnoreCase("UDP"))
            {
                transport = new DefaultUdpTransportMapping();
            }
            else
            {
                transport = new DefaultTcpTransportMapping();
            }
        }
        catch(Exception e)
        {
            throw new OBException(e.getMessage());
        }

        PDU pdu; // v2,v3 common
        Target target; // v2,v3 common

        CommunityTarget targetV2; // v2
        UsmUser user = null; // v3
        UserTarget targetV3; // v3

        Address address = GenericAddress.parse(this.getTransportProtocol() + ":" + this.getHost() + "/" + this.getSnmpPort());

        if(this.snmpVersion.equals(VERSION_3))
        {
         // 보안레벨 결정을 위한 접속정보 변환
            OctetString securityName = null;
            OID authenticationProtocol = AuthMD5.ID;
            OctetString authenticationPassphrase = null;
            OID privacyProtocol = PrivDES.ID;
            OctetString privacyPassphrase = null;
            
            // securityName 셋
            if (authInfo.getSecurityName() != null && !authInfo.getSecurityName().isEmpty())
                securityName = new OctetString(authInfo.getSecurityName());
            
            // auth protocol 셋
            if (authInfo.getAuthProto() != null && !authInfo.getAuthProto().isEmpty())
            {
                if ("md5".equalsIgnoreCase(authInfo.getAuthProto()))
                    authenticationProtocol = AuthMD5.ID;
                else if ("sha".equalsIgnoreCase(authInfo.getAuthProto()))
                    authenticationProtocol = AuthSHA.ID;
                else if ("none".equalsIgnoreCase(authInfo.getAuthProto()))
                    authenticationProtocol = null;
            }
            
            // auth password 셋
            if (authInfo.getAuthPassword() != null && !authInfo.getAuthPassword().isEmpty())
                authenticationPassphrase = new OctetString(authInfo.getAuthPasswordDecrypt());
            
            // priv protocol 셋
            if (authInfo.getPrivProto() != null && !authInfo.getPrivProto().isEmpty())
            {
                if ("des".equalsIgnoreCase(authInfo.getPrivProto()))
                    privacyProtocol = PrivDES.ID;
                else if ("des3".equalsIgnoreCase(authInfo.getPrivProto()))
                    privacyProtocol = Priv3DES.ID;
                else if ("aes128".equalsIgnoreCase(authInfo.getPrivProto()))
                    privacyProtocol = PrivAES128.ID;
                else if ("aes192".equalsIgnoreCase(authInfo.getPrivProto()))
                    privacyProtocol = PrivAES192.ID;
                else if ("aes256".equalsIgnoreCase(authInfo.getPrivProto()))
                    privacyProtocol = PrivAES256.ID;
                else if ("none".equalsIgnoreCase(authInfo.getPrivProto()) || authenticationProtocol == null)
                    privacyProtocol = null;
            }

            // priv password 셋
            if (authInfo.getPrivPassword() != null && !authInfo.getPrivPassword().isEmpty())
                privacyPassphrase = new OctetString(authInfo.getPrivPasswordDecrypt());
            
            // 보안레벨 판단: authPassword, privPassword 존재여부로
            int securityLevel = SecurityLevel.AUTH_PRIV;
            if (authenticationPassphrase == null && privacyPassphrase == null)
                securityLevel = SecurityLevel.NOAUTH_NOPRIV;
            else if (authenticationPassphrase != null && privacyPassphrase == null)
                securityLevel = SecurityLevel.AUTH_NOPRIV;
            else if (authenticationPassphrase != null && privacyPassphrase != null)
                securityLevel = SecurityLevel.AUTH_PRIV;
            else
                throw new OBException(OBException.ERRCODE_SYSTEM_PARAMETER, "authInfo: " + authInfo);
            
            ScopedPDU pduV3 = new ScopedPDU();
            // pduV3.setContextEngineID(contextEngineId); //<-- 세팅하면 안됨!!! unknown pdu handlers 나옴.
            // pduV3.add(new VariableBinding(oid));

            user = new UsmUser(securityName, authenticationProtocol,
                    authenticationPassphrase, privacyProtocol, privacyPassphrase);

            targetV3 = new UserTarget();
            targetV3.setVersion(VERSION_3);
            targetV3.setAddress(address);
            targetV3.setSecurityLevel(securityLevel);
            targetV3.setSecurityName(securityName);
            targetV3.setTimeout(this.getTimeout());
            // targetV3.setRetries(?);

            target = targetV3;
            pdu = pduV3;
        }
        else
        // v1 and v2
        {
            PDU pduV2 = new PDU();
            targetV2 = new CommunityTarget();
            targetV2.setCommunity(new OctetString(this.authInfo.getRcomm()));
            targetV2.setAddress(address);
            targetV2.setVersion(this.getSnmpVersion());
            targetV2.setTimeout(this.getTimeout());
            target = targetV2;
            pdu = pduV2;
        }

        pdu.setType(PDU.GETNEXT);
        // put the oid you want to get
        pdu.add(new VariableBinding(oid));
        pdu.setNonRepeaters(0);

        Snmp snmp = new Snmp(transport);

        try
        {
            if(this.snmpVersion.equals(VERSION_3))
            {
                usm.addUser(new OctetString(this.authInfo.getSecurityName()), user); // 반드시! security name 찾을 수 없다고 나옴!!
                MultiThreadedSecurityModels mtm = new MultiThreadedSecurityModels();
                mtm.addSecurityModel(usm);
                MessageProcessingModel mpm = snmp.getMessageProcessingModel(MPv3.ID);
                if(mpm instanceof MPv3)
                {
                    ((MPv3) mpm).setSecurityModels(mtm);
                }
                else
                {
                    throw new RuntimeException("SNMPv3 impossible processing.");
                }
            }
            snmp.listen();
        }
        catch(IOException e)
        {
            throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("failed to put all associated transport mappings into listen mode. message:%s", e.getMessage()));
        }

        // send the PDU
        ResponseEvent responseEvent;
        try
        {
            responseEvent = snmp.send(pdu, target);
        }
        catch(IOException e)
        {
            closeSnmp(snmp);
            throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("failed to send a PDU to the %s. message:%s", this.host, e.getMessage()));
        }

        // extract the response PDU (could be null if timed out)
        PDU responsePDU = responseEvent.getResponse();
        Vector<?> vbs = responsePDU.getVariableBindings();
        if(vbs.size() > 0)
        {
            ret = (VariableBinding) vbs.get(0);
        }
        closeSnmp(snmp);
        return ret;
    }

    public OBSnmp()
    {
    }

    // public OBSnmp(Integer version)
    // {
    // setSnmpVersion(version);
    // }
    // public OBSnmp(Integer version, String host)
    // {
    // setSnmpVersion(version);
    // setHost(host);
    // }
    // public OBSnmp(String host)
    // {
    // setSnmpVersion(OBSnmp.VERSION_2C);
    // setHost(host);
    // }
    public OBSnmp(String host, OBDtoAdcSnmpInfo authInfo)
    {
        setHost(host);
        setAuthInfo(authInfo);
        if(authInfo.getVersion() == 3)
        {
            setSnmpVersion(OBSnmp.VERSION_3);
        }
        else
        // if(snmpInfo.getVersion()==2) //default
        {
            setSnmpVersion(OBSnmp.VERSION_2C);
        }
    }

    /**
     * @return the transportProtocol
     */
    public String getTransportProtocol()
    {
        return transportProtocol;
    }

    /**
     * @param transportProtocol
     *            the transportProtocol to set
     */
    public void setTransportProtocol(String transportProtocol)
    {
        this.transportProtocol = transportProtocol;
    }

    /**
     * @return the snmpPort
     */
    public Integer getSnmpPort()
    {
        return snmpPort;
    }

    /**
     * @param snmpPort
     *            the snmpPort to set
     */
    public void setSnmpPort(Integer snmpPort)
    {
        this.snmpPort = snmpPort;
    }

    /**
     * @return the timeout
     */
    public Integer getTimeout()
    {
        return timeout;
    }

    /**
     * @param timeout
     *            the timeout to set
     */
    public void setTimeout(Integer timeout)
    {
        this.timeout = timeout;
    }

    /**
     * @return the retry
     */
    public Integer getRetry()
    {
        return retry;
    }

    /**
     * @param retry
     *            the retry to set
     */
    public void setRetry(Integer retry)
    {
        this.retry = retry;
    }

    /**
     * @return the snmpVersion
     */
    public Integer getSnmpVersion()
    {
        return snmpVersion;
    }

    /**
     * @param snmpVersion
     *            the snmpVersion to set
     */
    public void setSnmpVersion(Integer snmpVersion)
    {
        this.snmpVersion = snmpVersion;
    }

    /**
     * @return the host
     */
    public String getHost()
    {
        return host;
    }

    /**
     * @param host
     *            the host to set
     */
    public void setHost(String host)
    {
        this.host = host;
    }

    public OBDtoAdcSnmpInfo getAuthInfo()
    {
        return authInfo;
    }

    public void setAuthInfo(OBDtoAdcSnmpInfo authInfo)
    {
        this.authInfo = authInfo;
    }

    public ArrayList<Integer> parseOid(String targetOid, String baseOid)
    {
        ArrayList<Integer> list = new ArrayList<Integer>();

        int tmpIndex = targetOid.indexOf(baseOid);
        String sLine = targetOid.substring(tmpIndex + baseOid.length());
        String[] data = sLine.split("\\.");
        for(int i = 0; i < data.length; i++)
        {
            if(data[i].isEmpty())
                continue;
            Integer index = Integer.parseInt(data[i]);
            list.add(index);
        }
        return list;
    }
    
    public ArrayList<String> parseOidString(String targetOid, String baseOid)
    {
        ArrayList<String> list = new ArrayList<String>();

        int tmpIndex = targetOid.indexOf(baseOid);
        String sLine = targetOid.substring(tmpIndex + baseOid.length());
        String[] data = sLine.split("\\.");
        for(int i = 0; i < data.length; i++)
        {
            if(data[i].isEmpty())
                continue;
            String index = data[i];
            list.add(index);
        }
        return list;
    }
    
    public ArrayList<String> parseOidStringV295(String targetOid, String baseOid)
    {
        ArrayList<String> list = new ArrayList<String>();

        int tmpIndex = targetOid.indexOf(baseOid);
        String sLine = targetOid.substring(tmpIndex + baseOid.length());
        String[] data = sLine.split("\\.");
        for(int i = 0; i < data.length; i++)
        {
            if(data[i].isEmpty())
                continue;
            String index = data[i];
            list.add(index);
        }

        int listSize = list.size();
        int indexSize = Integer.parseInt(list.get(0))+1;
        String index = "";
        String lastIndex = list.get(list.size()-1);
        for(int i = 1; i < indexSize; i++)
        {
            int value = Integer.parseInt(list.get(i));
            index += Character.toString((char)value);
        }
        list.clear();
        list.add(index);
        if(!(indexSize == listSize))
            list.add(lastIndex);
        
        return list;
    }
    
    public ArrayList<String> parseOidStatus(String targetOid, String baseOid)
    {
        ArrayList<String> list = new ArrayList<String>();

        int tmpIndex = targetOid.indexOf(baseOid);
        String sLine = targetOid.substring(tmpIndex + baseOid.length());
        String[] data = sLine.split("\\.");
        for(int i = 0; i < data.length; i++)
        {
            if(data[i].isEmpty())
                continue;
            String index = data[i];
            list.add(index);
        }
        return list;
    }
    
    public ArrayList<String> parseOidStatusV295(String targetOid, String baseOid)
    {
        ArrayList<String> list = new ArrayList<String>();

        int tmpIndex = targetOid.indexOf(baseOid);
        String sLine = targetOid.substring(tmpIndex + baseOid.length());
        String[] data = sLine.split("\\.");
        for(int i = 0; i < data.length; i++)
        {
            if(data[i].isEmpty())
                continue;
            String index = data[i];
            list.add(index);
        }
        int listSize = list.size();

        String index = "";
        String vsServiceIndex = list.get(0);
        for(int i = 2; i < listSize; i++)
        {
            int value = Integer.parseInt(list.get(i));
            index += Character.toString((char)value);
        }
        list.clear();
        list.add(vsServiceIndex);
        list.add(index);
        return list;
    }

    public String parseOidName(String targetOid, String baseOid)
    {
        String result = "";
        int tmpIndex = targetOid.indexOf(baseOid);
        String sLine = targetOid.substring(tmpIndex + baseOid.length() + 1);
        String[] data = sLine.split("\\.");

        int length = Integer.parseInt(data[0]);
        for(int i = 1; i <= length; i++)
        {
            byte[] bytes = new byte[1];
            bytes[0] = (byte) Integer.parseInt(data[i]);
            if((bytes[0] >= 0x20) && (bytes[0] <= '~')) //space부터 '~' 사이 문자를 유효문자로 봄
            {
                String letter = new String(bytes);
                result += letter;
            }
        }
        return result;
    }

    public ArrayList<String> parseOidNameMulti(String targetOid, String baseOid)
    {
        ArrayList<String> retVal = new ArrayList<String>();
        int tmpIndex = targetOid.indexOf(baseOid);
        String sLine = targetOid.substring(tmpIndex + baseOid.length() + 1);
        String[] data = sLine.split("\\.");
        int index = 0;
        do
        {
            int dlength = Integer.parseInt(data[index++]);
            String tmpValue = "";
            for(int i = 0; i < dlength; i++)
            {
                byte[] bytes = new byte[1];
                bytes[0] = (byte) Integer.parseInt(data[i + index]);
                if((bytes[0] >= 0x20) && (bytes[0] <= '~')) //space부터 '~' 사이 문자를 유효문자로 봄
                {
                    String letter = new String(bytes);
                    tmpValue += letter;
                }
            }
            retVal.add(tmpValue);
            index += dlength;
        } while(index < data.length);

        return retVal;
    }

    public String makeOidTail(String[] items) // oid 끝에 붙는 부가 oid를 만든다. item을 oid형식으로 변환한다. input:"55" --> output: "2.53.53"
    {
        int i, j;
        String ret = "";
        if(items == null || items.length == 0)
        {
            return null;
        }

        for(i = 0; i < items.length; i++)
        {
            if(items[i] == null || items[i].isEmpty())
            {
                return null;
            }
            if(i > 0) // 두번째부터 .으로 연결
            {
                ret += ".";
            }
            ret += Integer.toString(items[i].length());
            for(j = 0; j < items[i].length(); j++)
            {
                ret += "." + (int) items[i].charAt(j);
            }
        }
        return ret;
    }

    public boolean checkObject(Object obj)
    {
        if(obj.toString().endsWith("noSuchObject"))
            return false;
        if(obj.toString().endsWith("noSuchInstance"))
            return false;
        return true;
    }

    // public static void main(String[] args)
    // {
    // OBSnmp snmp = new OBSnmp(OBSnmp.VERSION_2C, "localhost");
    // OBDatabase db=new OBDatabase();
    // try
    // {
    // db.openDB();
    // DtoSnmpSystemInfo info = snmp.getSystemInfo(db);
    // System.out.println(info);
    // }
    // catch(Exception e)
    // {
    // e.printStackTrace();
    // }
    // }

    public String getAdcHostname(int adcType, String swVersion) throws OBException
    {
        OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start."));
        OBSnmpOidDB oidDB = new OBSnmpOidDB();

        DtoOidInfoAdcResc oidInfo;

        try
        {
            
            oidInfo = oidDB.getAdcResource(adcType, swVersion);
            String name = getAdcHostname(oidInfo.getAdcName());
            OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", name));
            return name;
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

    private String getAdcHostname(String oid) throws OBException
    {
        // OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("start. %s.", oid.toString()));

        String result = "";
        String tempOid;
        Snmp snmp = openSnmp();

        try
        {
            tempOid = oid;
            if((tempOid != null) && (!tempOid.isEmpty()))
            {
                Object obj = getSnmp(snmp, tempOid);
                if(checkObject(obj))
                    result = obj.toString();
            }
            // OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s.", info.toString()));
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
        finally
        {
            closeSnmp(snmp);
        }
    }   
}

class MultiThreadedSecurityModels extends SecurityModels
{
    public MultiThreadedSecurityModels()
    {
        super();
    }

    public synchronized static SecurityModels getInstance()
    {
        throw new RuntimeException("Cannot get instance in this object");
    }
}
