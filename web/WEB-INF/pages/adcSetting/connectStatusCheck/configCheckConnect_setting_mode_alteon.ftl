<table class="Atype connectTest" cellpadding="0" cellspacing="0">
	<caption>&nbsp;</caption>
	<colgroup>
		<col width="40px"/>
		<col width="14px"/>
		<col width="auto"/>
		<col width="14px"/>
		<col width="auto"/>
		<col width="14px"/>
		<col width="auto"/>
		<col width="14px"/>		
		<col width="auto"/>
		<col width="14px"/>
	</colgroup>		
	<tr>
		<td class="Start">
			<a href="#" class="connectAdcChkResult">
				<img src="imgs/adcSetting/conntest_Start_on.png"/>
			</a>			
		</td>
		<#if chkResultList??>
		<#list chkResultList as thsChkResult>
			<#if thsChkResult.checkID == 1>
				<#if thsChkResult.status == 1>
					<#assign networkStatus = "o">
					<td class="State_sx${networkStatus} networkOk"></td>
					<td class="State_ok networkOk">① ${LANGCODEMAP["MSG_ADCSETTING_CHECK_NETWORK"]!}(ADCsmart→ADC)</td>		
				<#elseif thsChkResult.status == 2>
					<#assign networkStatus = "f">
					<td class="State_sx${networkStatus} networkFail"></td>
					<td class="State_fail networkFail">① ${LANGCODEMAP["MSG_ADCSETTING_CHECK_NETWORK"]!}(ADCsmart→ADC)</td>
				<#else>	
					<#assign networkStatus = "s">
					<td class="State_${networkStatus}xn networkConfirm"></td>
					<td class="State_normal networkConfirm">① ${LANGCODEMAP["MSG_ADCSETTING_CHECK_NETWORK"]!}(ADCsmart→ADC)</td>
				</#if>				
			<#elseif thsChkResult.checkID == 2>			
				<#if thsChkResult.status == 1>
					<#assign loginStatus = "o">					
					<td class="State_${networkStatus}x${loginStatus} loginOk"></td>
					<td class="State_ok loginOk">② ${LANGCODEMAP["MSG_ADCSETTING_APPLIANCE_LOGIN"]!}</td>			
				<#elseif thsChkResult.status == 2>	
					<#assign loginStatus = "f">		
					<td class="State_${networkStatus}x${loginStatus} loginFail"></td>
					<td class="State_fail loginFail">② ${LANGCODEMAP["MSG_ADCSETTING_APPLIANCE_LOGIN"]!}</td>
				<#else>		
					<#assign loginStatus = "n">		
					<td class="State_${networkStatus}xn loginConfirm"></td>
					<td class="State_normal loginConfirm">② ${LANGCODEMAP["MSG_ADCSETTING_APPLIANCE_LOGIN"]!}</td>					
				</#if>				
			<#elseif thsChkResult.checkID == 5>			
				<#if thsChkResult.status == 1>
					<#assign snmpStatus = "o">	
					<td class="State_${loginStatus}x${snmpStatus} snmpOk"></td>
					<#if adcType == "Alteon">
						<td class="State_ok snmpOk">④ snmp</td>
					<#else>
						<td class="State_ok snmpOk">③ snmp</td>
					</#if>
				<#elseif thsChkResult.status == 2>		
					<#assign snmpStatus = "f">						
					<td class="State_${loginStatus}x${snmpStatus} snmpFail"></td>
					<#if adcType == "Alteon">
						<td class="State_fail snmpFail">④ snmp</td>
					<#else>
						<td class="State_fail snmpFail">③ snmp</td>
					</#if>
				<#else>	
					<td class="State_nxn snmpConfirm"></td>
					<#if adcType == "Alteon">
						<td class="State_normal snmpConfirm">④ snmp</td>
					<#else>
						<td class="State_normal snmpConfirm">③ snmp</td>
					</#if>
					
				</#if>						
			<#elseif thsChkResult.checkID == 6>
				<#if thsChkResult.status == 1>	
					<td class="State_${snmpStatus}xo syslogOk"></td>
					<#if adcType == "Alteon">
						<td class="State_ok syslogOk">⑤ syslog</td>
					<#else>
						<td class="State_ok syslogOk">④ syslog</td>
					</#if>
					<td class="State_o"></td>
				<#elseif thsChkResult.status == 2>			
					<td class="State_${snmpStatus}xf syslogFail"></td>
					<#if adcType == "Alteon">
						<td class="State_fail syslogFail">⑤ syslog</td>
					<#else>
						<td class="State_fail syslogFail">④ syslog</td>
					</#if>					
					<td class="State_f"></td>
				<#else>
					<td class="State_nxn syslogConfirm"></td>
					<#if adcType == "Alteon">
						<td class="State_normal syslogConfirm">⑤ syslog</td>
					<#else>
						<td class="State_normal syslogConfirm">④ syslog</td>
					</#if>					
					<td class="State_n"></td>
				</#if>
			</#if>						
		</#list>		
		</#if>
	</tr>	
	<tr>
		<td></td>
		<#if chkResultList??>
		<#list chkResultList as thsChkResult>
		<#if thsChkResult.checkID == 1>
			<#if thsChkResult.status == 1>	
			<td colspan=2" class="Script_ok">
			<#elseif thsChkResult.status == 2>	
			<td colspan=2" class="Script_fail">
			<#else>
			<td colspan=2" class="Script_normal">
			</#if>
				<#list lastAdcChkTime as theLastTime>
					<#if theLastTime.checkID == 1>	
						<div class="txt"><div class="time">${(theLastTime.checkTime?string("yyyy-MM-dd HH:mm:ss"))!""}</div>
					</#if>
				</#list>
				${(thsChkResult.summary)!""}</div>
			</td>
		</#if>
		<#if thsChkResult.checkID == 2>
			<#if thsChkResult.status == 1>	
			<td colspan=2" class="Script_ok">
			<#elseif thsChkResult.status == 2>	
			<td colspan=2" class="Script_fail">
			<#else>
			<td colspan=2" class="Script_normal">
			</#if>
				<#list lastAdcChkTime as theLastTime>
					<#if theLastTime.checkID == 2>	
						<div class="txt"><div class="time">${(theLastTime.checkTime?string("yyyy-MM-dd HH:mm:ss"))!""}</div>
					</#if>
				</#list>
				${(thsChkResult.summary)!""}</div>
			</td>
		</#if>		
		<#if thsChkResult.checkID == 5>
			<#if thsChkResult.status == 1>	
			<td colspan=2" class="Script_ok">
			<#elseif thsChkResult.status == 2>	
			<td colspan=2" class="Script_fail">
			<#else>
			<td colspan=2" class="Script_normal">
			</#if>
				<#list lastAdcChkTime as theLastTime>
					<#if theLastTime.checkID == 5>	
						<div class="txt"><div class="time">${(theLastTime.checkTime?string("yyyy-MM-dd HH:mm:ss"))!""}</div>
					</#if>
				</#list>
				${(thsChkResult.summary)!""}</div>
			</td>
		</#if>
		<#if thsChkResult.checkID == 6>
			<#if thsChkResult.status == 1>	
			<td colspan=2" class="Script_ok">
			<#elseif thsChkResult.status == 2>	
			<td colspan=2" class="Script_fail">
			<#else>
			<td colspan=2" class="Script_normal">
			</#if>
				<#list lastAdcChkTime as theLastTime>
					<#if theLastTime.checkID == 6>	
						<div class="txt"><div class="time">${(theLastTime.checkTime?string("yyyy-MM-dd HH:mm:ss"))!""}</div>
					</#if>
				</#list>
				${(thsChkResult.summary)!""}</div>
			</td>
		</#if>
		</#list>
		</#if>
		<td></td>
	</tr>	
</table>      