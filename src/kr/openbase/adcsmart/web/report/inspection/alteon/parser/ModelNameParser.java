package kr.openbase.adcsmart.web.report.inspection.alteon.parser;

public class ModelNameParser
{

    /*
>> Standalone ADC - Main# /cfg/sys/ssnmp/cur 
Current SNMP params:
  Read community string:  "openbase"
  Write community string: "private"
  Trap source address:  192.168.100.201
  Authentication traps disabled.
  All link up/down traps enabled.

Current v1/v2 access enabled

Current SNMPv3 USM user settings:
  1: name adminmd5, auth md5, privacy des
  2: name adminsha, auth sha, privacy des
  3: name v1v2only, auth none, privacy none

Current SNMPv3 vacmAccess settings:
  1: group name admingrp, prefix , model usm
     level authPriv, match exact, 
     read view iso, write view iso, notify view iso
  2: group name v1v2grp, prefix , model snmpv1
     level noAuthNoPriv, match exact, 
     read view iso, write view iso, notify view v1v2only

Current SNMPv3 vacmSecurityToGroup settings:
  1: model usm, user name adminmd5, group name admingrp
  2: model usm, user name adminsha, group name admingrp
  3: model snmpv1, user name v1v2only, group name v1v2grp

Current SNMPv3 vacmViewTreeFamily settings:
  1: name v1v2only, subtree 1
     type included
  2: name v1v2only, subtree 1.3.6.1.6.3.15
     type excluded
  3: name v1v2only, subtree 1.3.6.1.6.3.16
     type excluded
  4: name v1v2only, subtree 1.3.6.1.6.3.18
     type excluded
  5: name iso, subtree 1
     type included

>> Standalone ADC - System SNMP# 
     */
}
