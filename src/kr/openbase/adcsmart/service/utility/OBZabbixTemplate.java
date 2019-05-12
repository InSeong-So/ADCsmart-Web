package kr.openbase.adcsmart.service.utility;

public class OBZabbixTemplate
{
    public enum Cisco3600{
        
        cpu("cpu_utilization_1_min"),
        memoryFree("mem_5min_free"),
        memoryUsed("mem_5min_used"),
        port1Staus("IfOperStatus.10101"),
        port2Staus("IfOperStatus.10102"),
        port3Staus("IfOperStatus.10103"),
        port4Staus("IfOperStatus.10104"),
        port5Staus("IfOperStatus.10105"),
        port6Staus("IfOperStatus.10106"),
        port7Staus("IfOperStatus.10107"),
        port8Staus("IfOperStatus.10108"),
        port9Staus("IfOperStatus.10109"),
        port10Staus("IfOperStatus.10110"),
        port11Staus("IfOperStatus.10111"),
        port12Staus("IfOperStatus.10112"),
        port13Staus("IfOperStatus.10113"),
        port14Staus("IfOperStatus.10114"),
        port15Staus("IfOperStatus.10115"),
        port16Staus("IfOperStatus.10116"),
        port17Staus("IfOperStatus.10117"),
        port18Staus("IfOperStatus.10118"),
        port19Staus("IfOperStatus.10119"),
        port20Staus("IfOperStatus.10120"),
        port21Staus("IfOperStatus.10121"),
        port22Staus("IfOperStatus.10122"),
        port23Staus("IfOperStatus.10123"),
        port24Staus("IfOperStatus.10124"),
        port1Bps("ifInOctets.10101"),
        port2Bps("ifInOctets.10102"),
        port3Bps("ifInOctets.10103"),
        port4Bps("ifInOctets.10104"),
        port5Bps("ifInOctets.10105"),
        port6Bps("ifInOctets.10106"),
        port7Bps("ifInOctets.10107"),
        port8Bps("ifInOctets.10108"),
        port9Bps("ifInOctets.10109"),
        port10Bps("ifInOctets.10110"),
        port11Bps("ifInOctets.10111"),
        port12Bps("ifInOctets.10112"),
        port13Bps("ifInOctets.10113"),
        port14Bps("ifInOctets.10114"),
        port15Bps("ifInOctets.10115"),
        port16Bps("ifInOctets.10116"),
        port17Bps("ifInOctets.10117"),
        port18Bps("ifInOctets.10118"),
        port19Bps("ifInOctets.10119"),
        port20Bps("ifInOctets.10120"),
        port21Bps("ifInOctets.10121"),
        port22Bps("ifInOctets.10122"),
        port23Bps("ifInOctets.10123"),
        port24Bps("ifInOctets.10124");
    
        private String span;
        
        // 열거 값에 (String) 값 span 에 대입
        Cisco3600(String mibs){
            span = mibs;
        }
        
        // span 값 반환
        public String getSpan(){
            return span;
        }
    }
}
