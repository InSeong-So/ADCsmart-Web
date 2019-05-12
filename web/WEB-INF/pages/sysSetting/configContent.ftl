<#setting number_format="0.####">
<#if langCode??>
  <#if "ko_KR" == langCode><#assign img_lang = ""></#if>
  <#if "en_US" == langCode><#assign img_lang = "_eng"></#if>
</#if>
<div class="contents_area">
    <div>
        <img src="imgs/title${img_lang!""}/h3_config.gif" class="title_h3" />                
    </div>
    <form id="envNetworkFrm" class="sys_setting" method="post" name="envNetworkFrm">
    <!-- 1 --> 
    <div class="title_h4">
        <li>${LANGCODEMAP["MSG_SYSSETTING_DEFAULT_SET"]!}</li>
    </div>
    <#list envNetwork as theEnvNetwork>
    <table class="Board" cellpadding="0" cellspacing="0">   
        <colgroup>
            <col width="200px"/>
            <col >
        </colgroup>
        <tr class="StartLine">
            <td colspan="2" ></td>
        </tr>
        <tr>
            <th class="Lth1">
                <li>${LANGCODEMAP["MSG_SYSSETTING_SYSTEM_NAME"]!}</li>
            </th>
            <td class="Lth0">
                <input type="text" name="enetwork.hostName" id="textfield3" class="inputText width130" value="${(theEnvNetwork.hostName)!''}" disabled="disable"/>
                
            </td>
        </tr>   
        <tr class="DivideLine">
            <td colspan="2"></td>
        </tr>           
    </table>   
    <table class="Board" cellpadding="0" cellspacing="0">   
        <colgroup>
            <col width="200px"/>
            <col width="110px"/ >
            <col >
        </colgroup>     
        <tr>
            <th rowspan="5" class="Lth1">
                <li>${LANGCODEMAP["MSG_SYSSETTING_SYSTEM_ADDR"]!}</li>
            </th>
            <td class="Lth0">${LANGCODEMAP["MSG_SYSSETTING_IP_ADDR"]!}</td>             
            <td> 
                <input name=enetwork.ipAddress type="text" class="inputText width110" id="textfield" value="${(theEnvNetwork.ipAddress)!''}" disabled="disable"/>
            </td>
        </tr>
        <tr class="DivideLine">
            <td colspan="3"></td>
        </tr>
        <tr>
            <td class="Lth0">Netmask</td>               
            <td> 
                <input name="enetwork.netmask" type="text" class="inputText width110" id="textfield" value="${(theEnvNetwork.netmask)!''}" disabled="disable" />
            </td>
        </tr>
        <tr class="DivideLine">
            <td colspan="3"></td>
        </tr>   
        <tr>
            <td class="Lth0">Gateway</td>               
            <td> 
                <input name="enetwork.gateway" type="text" class="inputText width110" id="textfield" value="${(theEnvNetwork.gateway)!''}" disabled="disable" />
            </td>
        </tr>
    </#list>
        
    <#list envAdditional as theEnvAdditional>
        <tr class="DivideLine">
            <td colspan="3"></td>
        </tr>
        <tr>
            <th rowspan="7" class="Lth1">
                <li>${LANGCODEMAP["MSG_SYSSETTING_SNMPCOMMUNITY"]!}</li>
            </th>
            <td class="Lth0">Version</td>               
            <td> 
            <#if (theEnvAdditional.snmpCommunity.community) != ''>
                <input name="eadditional.snmpCommunity.version" type="radio" id="v2" checked="checked" value="2" />
                    <label for="v2">v2</label>&nbsp;&nbsp;&nbsp;
                <input name="eadditional.snmpCommunity.version" type="radio" id="v3" value="3" />
                    <label for="v3">v3</label>&nbsp;&nbsp;&nbsp;
            <#else>
                <input name="eadditional.snmpCommunity.version" type="radio" id="v2" value="2" />
                    <label for="v2">v2</label>&nbsp;&nbsp;&nbsp;
                <input name="eadditional.snmpCommunity.version" type="radio" id="v3" checked="checked" value="3" />
                    <label for="v3">v3</label>&nbsp;&nbsp;&nbsp;
            </#if>
            </td>       
        </tr>
        <tr class="DivideLine">
            <td colspan="3"></td>
        </tr>
        
        <tr class="snmpv2Version">
            <td class="Lth0">${LANGCODEMAP["MSG_SYSSETTING_SNMPCOMMUNITYSTRING"]!}</td>             
            <td> 
                <input name="eadditional.snmpCommunity.community" type="text" class="inputText width110" id="textfield" value="${(theEnvAdditional.snmpCommunity.community)!''}" />
            </td>
        </tr>
        <tr class="DivideLine snmpv2Version">
            <td colspan="3"></td>
        </tr>   
        <tr class="snmpv2Version">
            <td class="Lth0">${LANGCODEMAP["MSG_SYSSETTING_SNMPCOMMUNITYACCES"]!}</td>              
            <td> 
                <input name="eadditional.snmpCommunity.snmpAcessType" type="hidden" class="inputText width110" id="textfield" value="${(theEnvAdditional.snmpCommunity.accessType)!''}" />
                <select id="snmpAccessType" name="eadditional.snmpAcessType" class="filterTypeTxt select">
                <#if (theEnvAdditional.snmpCommunity.accessType == "rocommunity")>
                    <option value="rocommunity" selected="selected">Read Only</option>                  
                    <option value="rwcommunity">Read/Write</option>     
                <#elseif (theEnvAdditional.snmpCommunity.accessType == "rwcommunity")>
                    <option value="rocommunity">Read Only</option>                  
                    <option value="rwcommunity" selected="selected">Read/Write</option> 
                </#if>              
                </select>
            </td>
        </tr>   
        <tr class="DivideLine snmpv2Version">
            <td colspan="3"></td>
        </tr>
        <tr class="snmpv2Version">
            <td class="Lth0"></td>              
            <td></td>
        </tr>   
        
        
        <tr class="snmpv3Version none">
            <td class="Lth0">${LANGCODEMAP["MSG_ADCSETTING_SNMPUSER"]}</td>             
            <td> 
                <input name="eadditional.snmpCommunity.userId" type="text" class="inputText width110" id="textfield" value="${(theEnvAdditional.snmpCommunity.userId)!''}" />
            </td>
        </tr>
        <tr class="DivideLine snmpv3Version none">
            <td colspan="3"></td>
        </tr>
        
        <tr class="snmpv3Version none">
            <td class="Lth0">${LANGCODEMAP["MSG_ADCSETTING_SNMPAUTHPW"]}</td>
            <td>
                <input name="fakeIdField" type="text" style="display:none;">
                <input name="fakePwField" type="password" style="display:none;">
                <input type="password" name="eadditional.snmpCommunity.authPassword" id="textfield3" class="inputText width130" value="${(theEnvAdditional.snmpCommunity.authPassword)!''}" autocomplete="off"/>
                
                <input name="eadditional.snmpCommunity.snmpAcessType" type="hidden" class="inputText width110" id="textfield" value="${(theEnvAdditional.snmpCommunity.algorithm)!''}" />
                <select id="authProtocolCbx" style="width:110px;" name="eadditional.snmpCommunity.algorithm">
                <#if (theEnvAdditional.snmpCommunity.algorithm == "md5")>
                    <option value="md5" selected="selected">md5</option>                    
                    <option value="sha">sha</option>        
                <#elseif (theEnvAdditional.snmpCommunity.algorithm == "sha")>
                    <option value="md5">md5</option>                    
                    <option value="sha" selected="selected">sha</option>    
                </#if>  
                </select>
                                
            </td>
        </tr>
        <tr class="DivideLine snmpv3Version none">
            <td colspan="3"></td>
        </tr>
        
        <tr class="snmpv3Version none">
            <td class="Lth0">${LANGCODEMAP["MSG_ADCSETTING_SNMPPRIVPW"]}</td>
            <td>
                <table class="Board100" cellpadding="0" cellspacing="0">
                    <tr>
                        <td>
                            <input name="fakeIdField" type="text" style="display:none;">
                            <input name="fakePwField" type="password" style="display:none;">
                            <input type="password" name="eadditional.snmpCommunity.privPassword" id="textfield3" class="inputText width130" value="${(theEnvAdditional.snmpCommunity.privPassword)!''}" autocomplete="off"/>                            
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr class="DivideLine">
            <td colspan="3"></td>
        </tr>
        
        <tr>            
            <th class="Lth1">
                <li>${LANGCODEMAP["MSG_SYSSETTING_SYSLOG_SERVIPADDR"]!}</li>
            </th>
            <td class="Lth0">${LANGCODEMAP["MSG_SYSSETTING_IP_ADDR"]!}</td>             
            <td> 
                <input type="text" name="eadditional.syslogServerAddress" id="textfield2" class="inputText width130" value="${(theEnvAdditional.syslogServerAddress)!''}"/>
            </td>
        </tr>
        <tr class="DivideLine">
            <td colspan="3"></td>
        </tr>
        <tr>
            <th rowspan="7" class="Lth1">
                <li>${LANGCODEMAP["MSG_SYSSETTING_SNMPTRAP_SERVIPADDR"]!}</li>
            </th>
            <td class="Lth0">${LANGCODEMAP["MSG_SYSSETTING_IP_ADDR"]!}</td>             
            <td> 
                <input name="eadditional.snmpTrapServerAddress" type="text" class="inputText width110" id="textfield" value="${(theEnvAdditional.snmpTrap.snmpTrapServerAddress)!''}" />
            </td>
        </tr>
        <tr class="DivideLine">
            <td colspan="3"></td>
        </tr>
        <tr>
            <td class="Lth0">Port</td>              
            <td> 
                <input name="eadditional.snmpTrapPort" type="text" class="inputText width110" id="textfield" value="${(theEnvAdditional.snmpTrap.snmpTrapPort)!'162'}" />
            </td>
        </tr>
        <tr class="DivideLine">
            <td colspan="3"></td>
        </tr>   
        <tr>
            <td class="Lth0">Community</td>             
            <td> 
                <input name="eadditional.snmpTrapCommunity" type="text" class="inputText width110" id="textfield" value="${(theEnvAdditional.snmpTrap.snmpTrapCommunity)!'public'}" />
            </td>
        </tr>   
        <tr class="DivideLine">
            <td colspan="3"></td>
        </tr>
        <tr>
            <td class="Lth0">Version</td>               
            <td> 
            <#if (theEnvAdditional.snmpTrap.snmpTrapVersion) == 1>
                <input name="eadditional.snmpTrapVersion" type="radio" id="v1" checked="checked" value="1" />
                    <label for="v1">v1</label>&nbsp;&nbsp;&nbsp;
                <input name="eadditional.snmpTrapVersion" type="radio" id="v2" value="2" />
                    <label for="v2">v2</label>&nbsp;&nbsp;&nbsp;
            <#elseif (theEnvAdditional.snmpTrap.snmpTrapVersion) == 2>
                <input name="eadditional.snmpTrapVersion" type="radio" id="v1" value="1" />
                    <label for="v1">v1</label>&nbsp;&nbsp;&nbsp;
                <input name="eadditional.snmpTrapVersion" type="radio" id="v2" checked="checked" value="2" />
                    <label for="v2">v2</label>&nbsp;&nbsp;&nbsp;
            </#if>
            </td>           
        </tr>       
        </#list>
        <tr class="DivideLine">
            <td colspan="3" ></td>
        </tr>
        <#list envAdditional as theEnvAdditional>
        <tr>
            <th class="Lth1">
                <li>Alteon Save</li>
            </th>
            <td class="Lth0" colspan="2">
                <#if (theEnvAdditional.alteonAutoSaveYn)!true>
                    <input type="checkbox" name="eadditional.alteonAutoSaveYn" id="checkbox" checked="checked" value="true" />
                <#else>
                    <input type="checkbox" name="eadditional.alteonAutoSaveYn" id="checkbox" value="true" />
                 </#if> 
                 ${LANGCODEMAP["MSG_SYSSETTING_USE"]!}
            </td>
        </tr>
        </#list>
        <tr class="DivideLine">
            <td colspan="3"></td>
        </tr>
        <#list envAdditional as theEnvAdditional>
        <tr>
            <th class="Lth1">
                <li>${LANGCODEMAP["MSG_SYSSETTING_DUPL_LOGIN_PREV"]!}</li>
            </th>
            <td class="Lth0" colspan="2">
                <#if (theEnvAdditional.loginAccessYn)!true> 
                    <input type="checkbox" name="eadditional.loginAccessYn" id="checkbox" checked="checked" value="true" />
                <#else>
                    <input type="checkbox" name="eadditional.loginAccessYn" id="checkbox" value="true" />
                </#if>
                ${LANGCODEMAP["MSG_SYSSETTING_USE"]!} 
            </td>
        </tr>
        </#list>
        <tr class="DivideLine">
            <td colspan="3"></td>
        </tr>
        <#list envAdditional as theEnvAdditional>
        <tr>
            <th class="Lth1">
                <li>${LANGCODEMAP["MSG_SYSSETTING_ADC_MONI_CYCLE"]!}</li>
            </th>
            <td colspan="2">
                <span class="Lth0" >
                    <input type="text" name="eadditional.intervalAdcConfSync" id="textfield4" class="inputText width40 align_center" value="${(theEnvAdditional.intervalAdcConfSync?c)!''}"  />
                </span>${LANGCODEMAP["MSG_SYSSETTING_SECOND"]!}
            </td>
        </tr>
        <tr class="DivideLine">
            <td colspan="3"></td>
        </tr>
        </#list>
        <#list envAdditional as theEnvAdditional>
        <tr>
            <th class="Lth1">
                <li>${LANGCODEMAP["MSG_SYSSETTING_SERVICE_RESPONSE_TIME"]!}</li>
            </th>
            <td class="Lth0" colspan="2">
                <#if (theEnvAdditional.serviceRespTime)!true>
                    <input type="checkbox" name="eadditional.serviceRespTime" id="checkbox" checked="checked" value="true" />
                <#else>
                    <input type="checkbox" name="eadditional.serviceRespTime" id="checkbox" value="true" />
                </#if>
                ${LANGCODEMAP["MSG_SYSSETTING_USE"]!}
            </td>
        </tr>
        <tr class="DivideLine">
            <td colspan="3"></td>
        </tr>
        </#list>
        
        <#list envAdditional as theEnvAdditional>
        <tr>
            <th class="Lth1">
                <li>${LANGCODEMAP["MSG_SYSSETTING_AREA_RESPONSE_TIME"]!}</li>
            </th>
            <td class="Lth0" colspan="2">
                <#if (theEnvAdditional.respTimeSection)!true>
                    <input type="checkbox" name="eadditional.respTimeSection" id="checkbox" checked="checked" value="true" />
                    ${LANGCODEMAP["MSG_SYSSETTING_USE"]!}
                    <input type="text" name="eadditional.respTimeInterval" id="textfield" class="inputText width40 align_center" value="${(theEnvAdditional.respTimeInterval)!''}" /> ${LANGCODEMAP["MSG_SYSSETTING_SECOND"]!}
                <#else>
                    <input type="checkbox" name="eadditional.respTimeSection" id="checkbox" value="true" />
                    ${LANGCODEMAP["MSG_SYSSETTING_USE"]!}
                    <input type="text" name="eadditional.respTimeInterval" id="textfield" class="inputText width40 align_center" disabled="true" /> ${LANGCODEMAP["MSG_SYSSETTING_SECOND"]!}
                </#if>

            </td>
        </tr>
        <tr class="DivideLine">
            <td colspan="3"></td>
        </tr>
        <tr>
            <th class="Lth1">
                <li>알람팝업창</li>
            </th>
            <td class="Lth0" colspan="2">
                <#if (theEnvAdditional.alarmPopupYn)!true>
                    <input type="checkbox" name="eadditional.alarmPopupYn" id="checkbox" checked="checked" value="true" />
                <#else>
                    <input type="checkbox" name="eadditional.alarmPopupYn" id="checkbox" value="true" />
                 </#if> 
                             사용
            </td>
        </tr>
        
        <tr class="DivideLine">
            <td colspan="3"></td>
        </tr>
        <tr>
            <th rowspan="7" class="Lth1">
                <li>SMS대응</li>
            </th>
           <td class="Lth0" colspan="2">
                <#if (theEnvAdditional.smsActionYn)!true>
                    <input class="smsActionChk" type="checkbox" name="eadditional.smsActionYn" id="checkbox" checked="checked" value="true" />
                <#else>
                    <input class="smsActionChk" type="checkbox" name="eadditional.smsActionYn" id="checkbox" value="true" />
                 </#if> 
                             사용
            </td>
        </tr>
        <tr class="DivideLine">
            <td colspan="3"></td>
        </tr>
        
        <tr class="smsType">
        </tr>
        <tr class="DivideLine smsHPNumbers">
            <td colspan="3"></td>
        </tr>   
        <tr>
            <td class="Lth0">HP번호</td>              
            <td> 
                <#if (theEnvAdditional.smsActionYn)!true>
                    <input name="eadditional.smsHPNumbers" type="text" class="smsHPNumbers" size="100" id="textfield" placeholder="010-1234-5678;010-2345-6789 여러개 입력시 ;로 구분" value="${(theEnvAdditional.smsHPNumbers)!''}" />
                <#else>
                    <input name="eadditional.smsHPNumbers" type="text" class="smsHPNumbers" size="100" id="textfield" placeholder="010-1234-5678;010-2345-6789 여러개 입력시 ;로 구분" disabled="true" value="${(theEnvAdditional.smsHPNumbers)!''}" />
                 </#if> 
            </td>
        </tr>   
        <tr ></tr>
        <tr ></tr> 
        <tr class="EndLine2">
            <td colspan="3"></td>
        </tr>
        </#list>
        
   </table>       
    <!-- 7 --> 
 <!--
    <div class="title_h4_1">
        <li>화면 표시 설정</li>
    </div>
    <table class="Board" cellpadding="0" cellspacing="0">
        <colgroup>
            <col width="200px"/>
            <col >
        </colgroup>
        <tr class="DivideLine">
            <td colspan="2" class="StartLine"></td>
        </tr>
        <#list envView as theEnvView>
        <tr>
            <th class="Lth1">
                <li>목록 표시 개수</li>
            </th>
            <td>
                <span class="Lth0">
                    <input name="eview.logViewCount" type="text" class="inputText width130" id="textfield8"  value="${(theEnvView.logViewCount?c)!''}"/>
                </span> 개
            </td>
        </tr>
        <tr class="DivideLine">
            <td colspan="2"></td>
        </tr>
        <tr>
            <th class="Lth1">
                <li>모니터링 조회 시간</li>
            </th>
            <td>
                <span class="Lth0">
                    <select name="select3" id="logViewPeriodType" class="select width174">
                        <#if ((theEnvView.logViewPeriodType)!'') == 1>
                            <option value="1" selected="selected">최근 1시간</option>
                        <#else>
                            <option value="1">최근 1시간</option>
                        </#if>
                        <#if ((theEnvView.logViewPeriodType)!'') == 3>
                            <option value="3" selected="selected">최근 3시간</option>
                        <#else>
                            <option value="3">최근3시간</option>
                        </#if>
                        <#if ((theEnvView.logViewPeriodType)!'') == 6>
                            <option value="6" selected="selected">최근 6시간</option>
                        <#else>
                            <option value="6">최근 6시간</option>
                        </#if>
                        <#if ((theEnvView.logViewPeriodType)!'') == 12>
                            <option value="12" selected="selected">최근 12시간</option>
                        <#else>
                            <option value="12">최근 12시간</option>
                        </#if>
                        <#if ((theEnvView.logViewPeriodType)!'') == 24>
                            <option value="24" selected="selected">최근 24시간</option>
                        <#else>
                            <option value="24">최근 24시간</option>
                        </#if>
                    </select>
                </span>
            </td>
        </tr>
        <tr class="DivideLine">
            <td colspan="2"></td>
        </tr>
        <tr>                            
            <th class="Lth1">
                <li>자동 새로고침</li>
            </th>
            <td>
                <span class="Lth0">
                    <select  id="autoRefrash" name="autoR" class="select width174">
                    <#if ((theEnvView.autoRefrash)!'') == 0>
                        <option value="0" selected="selected">Disabled</option>                                 
                    <#else>
                        <option value="0">Disabled</option>
                    </#if>
                    <#if ((theEnvView.autoRefrash)!'') == 1>
                        <option value="1" selected="selected">10초</option>                                  
                    <#else>
                        <option value="1">10초</option>
                    </#if>
                    <#if ((theEnvView.autoRefrash)!'') == 2>
                        <option value="2" selected="selected">30초</option>                                  
                    <#else>
                        <option value="2">30초</option>
                    </#if>
                    <#if ((theEnvView.autoRefrash)!'') == 3>
                        <option value="3" selected="selected">1분</option>                                   
                    <#else>
                        <option value="3">1분</option>
                    </#if>
                    <#if ((theEnvView.autoRefrash)!'') == 4>
                        <option value="4" selected="selected">3분</option>                                   
                    <#else>
                        <option value="4">3분</option>
                    </#if>
                    </select>
                </span>
            </td>
        </tr>      
        <tr class="DivideLine">
            <td colspan="2"></td>
        </tr>
        <tr>                            
            <th class="Lth1">
                <li>자동 로그아웃 시간</li>
            </th>
            <td>
                <span class="Lth0">
                    <input name="eview.autoLogoutTime" type="text" class="inputText width130" id="textfield9" value="${(theEnvView.autoLogoutTime?c)!''}"/>
                </span> 초
            </td>
        </tr>                      
        <tr class="EndLine2">
            <td colspan="2"></td>
        </tr>
        </#list>
    </table>      
-->   
    <!-- 9 NTP  --> 
    <div class="title_h4_1">
        <li>Date and Time</li>
    </div>
    <table class="Board" cellpadding="0" cellspacing="0">
        <colgroup>
            <col width="200px"/>
            <col >
        </colgroup>
        <tr class="StartLine">
            <td colspan="2"></td>
        </tr>
        <tr>
            <th class="Lth1">
                <li>System Time</li>
            </th>
            <td class="Lth0 txt_blue">
                ${systemTime?string("yyyy-MM-dd HH:mm:ss")}
            </td>
        </tr>
        <tr class="DivideLine">
            <td colspan="2"></td>
        </tr>
        <tr>
            <th rowspan="1" class="Lth1">
                <li>System Clock Source</li>
            </th>
            <td class="Lth9">
                <p>
                <input type="hidden" class="none" id="use_ntp_value" value="${syncSystemTimeInfo.useNTP_YN!}"/>
                <input type="hidden" class="none" id="synctime_hour_value" value="${systemTime?string("HH")}" />
                <input type="hidden" class="none" id="synctime_min_value" value="${systemTime?string("mm")}" />
                <input type="hidden" class="none" id="synctime_sec_value" value="${systemTime?string("ss")}" />
                    <!-- NTP 설정 기능 진입점 차단 -->
                    <!-- <#if syncSystemTimeInfo.useNTP_YN == 1>
                    <select id="sync_system_time_select" name="syncSystemTimeInfo.timeSyncType" class="filterTypeTxt select">
                        <option value="0">${LANGCODEMAP["MSG_SYSSETTING_SELECTE"]!}</option>                    
                        <option value="1">Set time Manually</option>
                        <option value="2" selected="selected">Use Network Time Protocol(NTP)</option>
                    </select>
                    <#else>
                    <select id="sync_system_time_select" name="syncSystemTimeInfo.timeSyncType" class="filterTypeTxt select">
                        <option value="0" selected="selected">${LANGCODEMAP["MSG_SYSSETTING_SELECTE"]!}</option>                    
                        <option value="1">Set time Manually</option>
                        <option value="2">Use Network Time Protocol(NTP)</option> 
                    </select>
                    </#if> -->
                    <select id="sync_system_time_select" name="syncSystemTimeInfo.timeSyncType" class="filterTypeTxt select">
                        <option value="0" selected="selected">${LANGCODEMAP["MSG_SYSSETTING_SELECTE"]!}</option>                    
                        <option value="1">Set time Manually</option>                        
                    </select>   
                </p>
                    <!----- Use Network Time Protocol(NTP) ----->
                    <div class="Lth9_option ntp_input_area none">
                        <p class="ntp_input_area none">
                            <span class="width152">Primary NTP Server</span> : 
                            <input type="text" name="syncSystemTimeInfo.primary_NTP" id="primary_ntp" class="inputText width130" value="${syncSystemTimeInfo.primary_NTP!}">
                        </p>                        
                        <p class="ntp_input_area none">
                            <span class="width152">Secondary NTP Server</span> : 
                            <input type="text" name="syncSystemTimeInfo.secondary_NTP" id="secondary_ntp" class="inputText width130" value="${syncSystemTimeInfo.secondary_NTP!}">
                        </p>        
                        <p class="ntp_input_area none">
                            <span class="width152">Update Interval</span> : 
                            <input type="text" name="syncSystemTimeInfo.intervalNTPSync" id="ntp_interval" class="inputText width40" value="${syncSystemTimeInfo.intervalNTPSync!0}"> ${LANGCODEMAP["MSG_SYSSETTING_HOUR"]!}
                            &nbsp; <input type="button" class="apply_systemtime_sync Btn_black_small" value="${LANGCODEMAP["MSG_SYSSETTING_NOW_APPLY"]!}"/>                         
                        </p>
                    </div>
                        <!----- Set time Manually ----->
                    <div class="Lth9_option manually_input_area none">
                        <p>                     
                            <input name="settingTime" class="inputText_calendar2" type="text"  title="${LANGCODEMAP["MSG_STATISTICS_QUERY_PERIOD_SEL"]!}" value='${systemTime?string("yyyy-MM-dd")}'/>
                            <select class="inputSelect everyHour" id="synctime_hour">
                                <option value="01">01</option><option value="02">02</option><option value="03">03</option><option value="04">04</option>
                                <option value="05">05</option><option value="06">06</option><option value="07">07</option><option value="08">08</option>     
                                <option value="09">09</option><option value="10">10</option><option value="11">11</option><option value="12">12</option>
                                <option value="13">13</option><option value="14">14</option><option value="15">15</option><option value="16">16</option>
                                <option value="17">17</option><option value="18">18</option><option value="19">19</option><option value="20">20</option>
                                <option value="21">21</option><option value="22">22</option><option value="23">23</option><option value="24">24</option>
                            </select>
                            <span class="scheduleDay">:</span>
                            <select class="inputSelect everyMin" id="synctime_min">
                                <option value="0">00</option>
                                <option value="01">01</option><option value="02">02</option><option value="03">03</option><option value="04">04</option>
                                <option value="05">05</option><option value="06">06</option><option value="07">07</option><option value="08">08</option>     
                                <option value="09">09</option><option value="10">10</option><option value="11">11</option><option value="12">12</option> 
                                <option value="13">13</option><option value="14">14</option><option value="15">15</option><option value="16">16</option>
                                <option value="17">17</option><option value="18">18</option><option value="19">19</option><option value="20">20</option>    
                                <option value="21">21</option><option value="22">22</option><option value="23">23</option><option value="24">24</option>
                                <option value="25">25</option><option value="26">26</option><option value="27">27</option><option value="28">28</option>     
                                <option value="29">29</option><option value="30">30</option><option value="31">31</option><option value="32">32</option>
                                <option value="33">33</option><option value="34">34</option><option value="35">35</option><option value="36">36</option>
                                <option value="37">37</option><option value="38">38</option><option value="39">39</option><option value="40">40</option>
                                <option value="41">41</option><option value="42">42</option><option value="43">43</option><option value="44">44</option>
                                <option value="45">45</option><option value="46">46</option><option value="47">47</option><option value="48">48</option>     
                                <option value="49">49</option><option value="50">50</option><option value="51">51</option><option value="52">52</option>
                                <option value="53">53</option><option value="54">54</option><option value="55">55</option><option value="56">56</option>
                                <option value="57">57</option><option value="58">58</option><option value="59">59</option>                          
                            </select>
                            <span class="scheduleDay">:</span>
                            <select class="inputSelect everyMin" id="synctime_sec">
                                <option value="0">00</option>
                                <option value="01">01</option><option value="02">02</option><option value="03">03</option><option value="04">04</option>
                                <option value="05">05</option><option value="06">06</option><option value="07">07</option><option value="08">08</option>     
                                <option value="09">09</option><option value="10">10</option><option value="11">11</option><option value="12">12</option> 
                                <option value="13">13</option><option value="14">14</option><option value="15">15</option><option value="16">16</option>
                                <option value="17">17</option><option value="18">18</option><option value="19">19</option><option value="20">20</option>    
                                <option value="21">21</option><option value="22">22</option><option value="23">23</option><option value="24">24</option>
                                <option value="25">25</option><option value="26">26</option><option value="27">27</option><option value="28">28</option>     
                                <option value="29">29</option><option value="30">30</option><option value="31">31</option><option value="32">32</option>
                                <option value="33">33</option><option value="34">34</option><option value="35">35</option><option value="36">36</option>
                                <option value="37">37</option><option value="38">38</option><option value="39">39</option><option value="40">40</option>
                                <option value="41">41</option><option value="42">42</option><option value="43">43</option><option value="44">44</option>
                                <option value="45">45</option><option value="46">46</option><option value="47">47</option><option value="48">48</option>     
                                <option value="49">49</option><option value="50">50</option><option value="51">51</option><option value="52">52</option>
                                <option  value="53">53</option><option value="54">54</option><option value="55">55</option><option value="56">56</option>
                                <option value="57">57</option><option value="58">58</option><option value="59">59</option>                          
                            </select>
                            &nbsp; <input type="button" class="apply_systemtime_sync Btn_black_small" value="${LANGCODEMAP["MSG_SYSSETTING_NOW_APPLY"]!}"/>             
                        </p>                    
                    </div>
                </td>
        </tr>
        <tr class="EndLine2">
            <td colspan="2"></td>
        </tr>
    </table>                                     
    <!-- 8 --> 
    <div class="title_h4_1">
        <li>${LANGCODEMAP["MSG_SYSSETTING_ADC_LOG_FILLTER"]!}</li>
    </div>
    <table class="Board" cellpadding="0" cellspacing="0">
        <colgroup>
            <col width="200"/>
            <col >
        </colgroup>
        <tr class="StartLine">
            <td colspan="2"></td>
        </tr>
        <tr>
            <th class="Lth1">
                <li>${LANGCODEMAP["MSG_SYSSETTING_ADC_LOG_FILLTER"]!}</li>
            </th>
            <td class="Lth0">    
                <span>
                    <input name="pattern" type="text" class="filterPatternTxt inputText width170" />
                        <select id="logFilterType" name="logFilterType" class="filterTypeTxt select">
                            <option value="0">${LANGCODEMAP["MSG_SYSSETTING_LOG_PART"]!}</option>                                       
                            <option value="1">${LANGCODEMAP["MSG_SYSSETTING_LOG_TOTAL"]!}</option>
                        </select>
                </span> 
                <input type="button" class="addAdcLogFilterLnk Btn_white" value="${LANGCODEMAP["MSG_SYSSETTING_ADD"]!}"/>               
            </td>
        </tr>
        <tr>
            <th class="Lth1"></th>
            <td class="Lth0">
            <div class="width_90">
                <span class="usrselected_th">ADC 로그 필터링</span><!--(<span class="adcLogFilterCount">${(logFilter)?size}</span>)-->
                <span class="usrselected_txt"><span class="adcLogFilterCount txt_blue">${(logFilter)?size}</span>&nbsp;${LANGCODEMAP["MSG_SYSSETTING_COUNT_REGI"]!}</span>              
            </div>
            </td>
        </tr>
        <tr>                            
            <th class="Lth1"></th>
            <td class="Lth0">
                <table class="width_90Border table-content ipList" id="selectedLogFilter">              
                    <colgroup>
                    <col width="46px" />
                    <col width="460px" />
                    <col width="auto" />
                    </colgroup>
                    <thead>
                    <tr>
                        <th class="align_center">
                            <input type="checkbox" class="allLogFiltersChk" name="checkbox2" id="checkbox2" />
                        </th>
                        <th class="align_center">${LANGCODEMAP["MSG_SYSSETTING_FILLTER_PATT"]!}</th>
                        <th class="align_center">${LANGCODEMAP["MSG_SYSSETTING_SEARCH_RANGE"]!}</th>                    
                    </tr>
                    </thead>
                </table>
                <div class="table-wapper">
                <table class="table_type11 table-content ipList" id="selectedLogFilter">                
                    <colgroup>
                    <col width="46px" />
                    <col width="460px" />
                    <col width="auto" />
                    </colgroup>             
                    <tbody class="filterTbd">
                    <#list logFilter as theLogFilter>
                    <tr class="regFilterTr">
                        <td class="align_center">
                            <input class="logFilterChk" type="checkbox"/>
                        </td>                                                   
                        <td class="align_left_P20">
                            <input type="hidden" name="filterUserPattern" value="${theLogFilter.userPattern}">${theLogFilter.userPattern}
                        </td>
                        <td class="align_center">
                            <input type="hidden" name="filterType" value="${theLogFilter.type}">
                            <#if theLogFilter.type?number == 0>${LANGCODEMAP["MSG_SYSSETTING_PART"]!}</#if>
                            <#if theLogFilter.type?number == 1>${LANGCODEMAP["MSG_SYSSETTING_ALL"]!}</#if>
                        </td>                                                                   
                    </tr>
                    </#list>                                        
                    </tbody>
               </table>
               </div>
           </div>
           </td>
        </tr>   
        <!--        
        <tr>
            <td class="Lth1"></td>
            <td class="Lth0">                           
                <span class="button white small">
                    <button type="button" class="delAdcLogFilterLnk">삭제</button>
                </span>
            </td>
        </tr>
        -->     
        <tr>
            <td class="Lth1"></td>
            <td class="Lth0">
            <div class="width_90">
                    <table width="100%"  cellpadding="0" cellspacing="0">
                    <colgroup>
                        <col width="46px"/>
                        <col width="auto"/>
                    </colgroup>
                    <tr>
                    <td class="center">
                    <input type="button" class="delAdcLogFilterLnk Btn_white" value="${LANGCODEMAP["MSG_SYSSETTING_DELETE"]!}"/> 
                    </td>
                <td>&nbsp;</td>
                </tr>
                </table>
                </div>
           </td>
        </tr>
        <tr class="EndLine2">
            <td colspan="2"></td>
        </tr>
        <tr> 
            <td colspan="2">                            
                <div class="position_cT10">             
                    <input type="button" class="systemConfigOkLnk Btn_red" value="${LANGCODEMAP["MSG_SYSSETTING_APPLY"]!}"/>         
                </div> 
            </td>
        </tr>    
    <!----- Contents List End ----->
    </form>
</div>

