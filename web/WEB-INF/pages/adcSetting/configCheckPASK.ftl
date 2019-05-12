<#if langCode??>
  <#if "ko_KR" == langCode><#assign img_lang = ""></#if>
  <#if "en_US" == langCode><#assign img_lang = "_eng"></#if>
</#if>
<div class="contents_area"> 
<!--
	<div class="title_h2">
		<table class="Board100" cellpadding="0" cellspacing="0">
			<colgroup>							                            
				<col width="111px"/>
				<col width="111px"/>
				<col/>				
    		</colgroup>
			<tr>
				<td>
					<a class="adcModifyLnk" href="#">
						<img src="imgs/meun${img_lang!""}/3depth_adcedit_1.gif" />						
					</a>
				</td>
				<td>
					<img src="imgs/meun${img_lang!""}/3depth_adcsetinfo_0.gif" />					
				</td>	
				<td>&nbsp;</td>				
			</tr>
		</table>
	</div>	
-->
	<div> 
	<img src="imgs/title${img_lang!""}/h3_adcInfo.gif" class="title_h3"/>
	</div>
<div>
	<input name="adcIpaddress" type="text" style="display: none;" value="${(config.adcIpaddress)!''}" />
	<input name="adcId" type="text" style="display: none;" value="${(config.adcId)!''}" />
	<input name="adcPassword" type="text" style="display: none;" value="${(config.adcPassword)!''}" />
</div>
<#if opMode == 1>
<#else>
<div class="setting_status_area">
	<div class="Board">
		<div class="title_h4_2">
			<li>${LANGCODEMAP["MSG_ADCSETTING_SETTING_STATUS"]!}</li>
		</div>
		<div class="position_R10">
		    <input type="button" class="allConfigCheck Btn_white" value="${LANGCODEMAP["MSG_ADCSETTING_REFRESH"]!}"/>
		</div>
	</div>
<table class="Board" cellpadding="0" cellspacing="0">
	<caption>&nbsp;</caption>
	<colgroup>
		<col width="15%"/>
		<col width="20%"/>
		<col width="35%"/>
		<col width="20%"/>
		<col width="10%"/>
	</colgroup>		
	<tr class="StartLine">
		<td colspan="5" ></td>
	</tr>		
	<tr class="ContentsHeadLine">
		<th class="align_center Rcolor">${LANGCODEMAP["MSG_ADCSETTING_DIVISION"]!}</th>
		<th class="align_center Rcolor">${LANGCODEMAP["MSG_ADCSETTING_ARTICLE"]!}</th>
		<th class="align_center Rcolor">${LANGCODEMAP["MSG_ADCSETTING_SETTING_STATUS"]!}</th>
		<th class="align_center Rcolor">${LANGCODEMAP["MSG_ADCSETTING_RESULT"]!}</th>
		<th class="align_center">Action</th>
	</tr>
	<tr class="StartLine1">
		<td colspan="5"></td>
	</tr>
	<tr class="ContentsLine3">
		<td rowspan="3"class="align_center Rcolor">snmp</td>
		<td class="align_center Rcolor">${LANGCODEMAP["MSG_ADCSETTING_CHECK_USE"]!}</td>
		<#if !(config.snmpState)?? ||(config.snmpState)! == ''> 
			<td class="align_center Rcolor">-</td>
		<#else>
			<td class="align_center Rcolor">${config.snmpState}</td>
		</#if>
		<#if !(config.snmpStateResult)?? ||(config.snmpStateResult)! == ''> 
			<td class="align_center Rcolor">-</td>
		<#else>
			<td class="align_center Rcolor">${config.snmpStateResult}</td>
		</#if>
<!-- 버튼 기능 구현 보류 -->
<!-- 		<td class="align_center"> -->
<!-- 			<#if !(config.snmpStateResult!"")?? || (config.snmpStateResult)! =="${LANGCODEMAP['MSG_CONSTANT_FAIL']!}">  -->
<!-- 				<span class="button black small"> -->
<!-- 					<button type="button" class="snmpConfigkLnk">${LANGCODEMAP["MSG_ADCSETTING_CHANGE"]!}</button> -->
<!-- 				</span> -->
<!-- 			<#else> -->
<!-- 				<img src="imgs/btn${img_lang!""}/btn_change_off.gif" alt="changeOff"/>				 -->
<!-- 			</#if>		 -->
<!-- 		</td>					 -->
	</tr>
	<tr class="DivideLine">
		<td colspan="4"></td>
	</tr>
		<tr class="ContentsLine3">
		<td class="align_center Rcolor">read community</td>
		<#if !(config.snmpRcommunityADC)?? ||(config.snmpRcommunityADC)! == ''> 
			<td class="align_center Rcolor">-</td>
		<#else>
			<td class="align_center Rcolor">${config.snmpRcommunityADC}</td>
		</#if>
		<#if !(config.snmpRcommunityResult)?? ||(config.snmpRcommunityResult)! == ''> 
			<td class="align_center Rcolor">-</td>
		<#else>
			<td class="align_center Rcolor">${config.snmpRcommunityResult}</td>
		</#if>
<!-- 버튼 기능 구현 보류 -->
<!-- 		<td class="align_center"> -->
<!-- 			<#if !(config.snmpRcommunityResult!"")?? || (config.snmpRcommunityResult)! =="${LANGCODEMAP['MSG_CONSTANT_FAIL']!}">  -->
<!-- 				<span class="button black small"> -->
<!-- 					<button type="button" class="snmpRcommunityConfigkLnk">${LANGCODEMAP["MSG_ADCSETTING_SYN"]!}</button> -->
<!-- 				</span>	 -->
<!-- 			<#else> -->
<!-- 				<img src="imgs/btn${img_lang!""}/btn_sync_off.gif" alt="syncOff"/>				 -->
<!-- 			</#if>				 -->
<!-- 		</td>					 -->
	</tr>	
	<tr class="DivideLine">
		<td colspan="5"></td>
	</tr>
		<tr class="ContentsLine3">
		<td rowspan="3" class="align_center Rcolor">syslog</td>
		<td class="align_center Rcolor">${LANGCODEMAP["MSG_ADCSETTING_CHECK_USE"]!}</td>
		<#if !(config.syslogState)?? ||(config.syslogState)! == ''> 
			<td class="align_center Rcolor">-</td>
		<#else>
			<td class="align_center Rcolor">${config.syslogState}</td>
		</#if>
		<#if !(config.syslogStateResult)?? ||(config.syslogStateResult)! == ''> 
			<td class="align_center Rcolor">-</td>
		<#else>
			<td class="align_center Rcolor">${config.syslogStateResult}</td>
		</#if>
<!-- 버튼 기능 구현 보류 -->
<!-- 		<td class="align_center"> -->
<!-- 			<#if !(config.syslogStateResult!"")?? || (config.syslogStateResult)! =="${LANGCODEMAP['MSG_CONSTANT_FAIL']!}">  -->
<!-- 				<span class="button black small"> -->
<!-- 					<button type="button" class="syslogConfigkLnk">${LANGCODEMAP["MSG_ADCSETTING_CHANGE"]!}</button> -->
<!-- 				</span> -->
<!-- 			<#else> -->
<!-- 				<img src="imgs/btn${img_lang!""}/btn_change_off.gif" alt="changeOff"/>				 -->
<!-- 			</#if>				  -->
<!-- 		</td>					 -->
	</tr>	
	<tr class="DivideLine">
		<td colspan="4"></td>
	</tr>
		<tr class="ContentsLine3">
		<td class="align_center Rcolor">server</td>
		<#if !(config.syslogServerList)?? ||(config.syslogServerList)! == ''> 
			<td class="align_center Rcolor">-</td>
		<#else>
			<td class="align_center Rcolor">${config.nameList}</td>
		</#if>
		<#if !(config.syslogServerListResult)?? ||(config.syslogServerListResult)! == ''> 
			<td class="align_center Rcolor">-</td>
		<#else>
			<td class="align_center Rcolor"><pre>${config.syslogServerListResult}</pre></td>
		</#if>
<!-- 버튼 기능 구현 보류 -->
<!-- 		<td class="align_center"> -->
<!-- 			<#if !(config.syslogServerListResult!"")?? || (config.syslogServerListResult)! =="${LANGCODEMAP['MSG_CONSTANT_FAIL']!}">  -->
<!-- 				<span class="button black small"> -->
<!-- 					<button type="button" class="syslogListConfigkLnk">${LANGCODEMAP["MSG_ADCSETTING_CHANGE"]!}</button> -->
<!-- 				</span>	 -->
<!-- 			<#else> -->
<!-- 				<img src="imgs/btn${img_lang!""}/btn_change_off.gif" alt="changeOff"/>				 -->
<!-- 			</#if>			 -->
<!-- 		</td>						 -->
	</tr>			
	<tr class="EndLine2">
		<td colspan="5"></td>
	</tr>
</table>
</div>
</#if>
<div class="title_h4">
	<li>${LANGCODEMAP["MSG_ADCSETTING_MOVEMENT_STATUS"]!}
	</li>
</div>
<div>
<table class="Board" cellpadding="0" cellspacing="0">
	<caption>&nbsp;</caption>
	<colgroup>
		<col width="15%"/>
		<col width="20%"/>
		<col width="35%"/>
		<col width="30%"/>
	</colgroup>			
	<tr class="StartLine">
		<td colspan="5" ></td>
	</tr>		
	<tr class="ContentsHeadLine">
		<th class="align_center Rcolor">${LANGCODEMAP["MSG_ADCSETTING_DIVISION"]!}</th>
		<th class="align_center Rcolor">${LANGCODEMAP["MSG_ADCSETTING_ARTICLE"]!}</th>
		<th class="align_center Rcolor">${LANGCODEMAP["MSG_ADCSETTING_LAST"]!}</th>
		<th class="align_center">${LANGCODEMAP["MSG_ADCSETTING_RESULT"]!}</th>
	</tr>
	<tr class="DivideLine">
		<td colspan="4"></td>
	</tr>
	<tr class="ContentsLine3">
		<td class="align_center Rcolor">login</td>
		<td class="align_center Rcolor">${LANGCODEMAP["MSG_ADCSETTING_ID_PASSWORD"]!}</td>
		<td class="align_center Rcolor">${(config.function_loginStateTime?string("yyyy-MM-dd HH:mm:ss"))!"-"}</td>
		<#if !(config.function_loginStateResult)?? ||(config.function_loginStateResult)! == ''> 
			<td class="align_center">-</td>
		<#else>
			<td class="align_center">${config.function_loginStateResult}</td>
		</#if>										
	</tr>
	<tr class="DivideLine">
		<td colspan="4"></td>
	</tr>
	<tr class="ContentsLine3">
		<td class="align_center Rcolor">snmp</td>
		<td class="align_center Rcolor">${LANGCODEMAP["MSG_ADCSETTING_DATA_COLLECT"]!}</td>
		<td class="align_center Rcolor">${(config.function_snmpStateTime?string("yyyy-MM-dd HH:mm:ss"))!"-"}</td>
		<#if !(config.function_snmpStateResult)?? ||(config.function_snmpStateResult)! == ''> 
			<td class="align_center">-</td>
		<#else>
			<td class="align_center">${config.function_snmpStateResult}</td>
		</#if>				
	</tr>	
	<tr class="DivideLine">
		<td colspan="4"></td>
	</tr>
	<tr class="ContentsLine3">
		<td class="align_center Rcolor">syslog</td>
		<td class="align_center Rcolor">${LANGCODEMAP["MSG_ADCSETTING_DATA_COLLECT"]!}</td>
		<td class="align_center Rcolor">${(config.function_syslogStateTime?string("yyyy-MM-dd HH:mm:ss"))!"-"}</td>
		<#if !(config.function_syslogStateResult)?? ||(config.function_syslogStateResult)! == ''> 
			<td class="align_center">-</td>
		<#else>
			<td class="align_center ">${config.function_syslogStateResult}</td>
		</#if>
	</tr>							
	<tr class="EndLine2">
		<td colspan="4"></td>
	</tr>
</table>
</div>
</div> 