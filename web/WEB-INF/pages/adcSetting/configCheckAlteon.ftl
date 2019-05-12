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

	<div class="title_h4">
		<li>${LANGCODEMAP["MSG_ADCSETTING_MOVEMENT_STATUS"]!}</li>
	</div>
	<#if (opMode == 1)> <!-- 모니터링 모드 : 네트워크(ADCsmart→ADC),SNMP -->
		<div>
			<table class="Atype connectTest" cellpadding="0" cellspacing="0">
				<caption>&nbsp;</caption>
				<colgroup>
					<col width="40px"/>
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
					<td class="State_sxn"></td>
					<td class="State_normal">① ${LANGCODEMAP["MSG_ADCSETTING_CHECK_NETWORK"]!}(ADCsmart→ADC)</td>
					<td class="State_nxn"></td>							
					<td class="State_normal">④ SNMP</td>					
					<td class="State_n"></td>
				</tr>
				<tr>		
					<td class=""></td>
					<#if lastAdcChkTime??>
					<#list lastAdcChkTime as theLastTime>
						<#if theLastTime.checkID == 1>	
							<td colspan=2" class="Script_normal">
								<div class="txt"><div class="time">${(theLastTime.checkTime?string("yyyy-MM-dd HH:mm:ss"))!""}</div>
							</td>						
						<#elseif theLastTime.checkID == 5>
							<td colspan=2" class="Script_normal">
								<div class="txt"><div class="time">${(theLastTime.checkTime?string("yyyy-MM-dd HH:mm:ss"))!""}</div>
							</td>						
						<#else>
						</#if>
					</#list>
					<#else>
						<td colspan=2" class="Script_normal">
							<div class="txt"><div class="time"></div>
						</td>
						<td colspan=2" class="Script_normal">
							<div class="txt"><div class="time"></div>
						</td>						
						<td class=""></td>
					</#if>
				</tr>				
			</table>
		</div>
	<#elseif (opMode == 2)> <!-- 설정 모드 : 네트워크(ADCsmart→ADC), 장비로그인, snmp, syslog -->
		<div>
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
						<!--
						<img src="imgs/adcSetting/conntest_Start_off.png"/>
						-->	
					</td>
					
					<td class="State_sxn"></td>
					<td class="State_normal">① ${LANGCODEMAP["MSG_ADCSETTING_CHECK_NETWORK"]!}(ADCsmart→ADC)</td>
					<td class="State_nxn"></td>
					<td class="State_normal">② ${LANGCODEMAP["MSG_ADCSETTING_APPLIANCE_LOGIN"]!}</td>
					<td class="State_nxn"></td>
					<td class="State_normal">④ SNMP</td>
					<td class="State_nxn"></td>				
					<td class="State_normal">⑤ syslog</td>
					<td class="State_n"></td>
				</tr>
				<tr>		
					<td class=""></td>
					<#if lastAdcChkTime??>
					<#list lastAdcChkTime as theLastTime>
						<#if theLastTime.checkID == 1>	
							<td colspan=2" class="Script_normal">
								<div class="txt"><div class="time">${(theLastTime.checkTime?string("yyyy-MM-dd HH:mm:ss"))!""}</div>
							</td>
						<#elseif theLastTime.checkID == 2>
							<td colspan=2" class="Script_normal">
								<div class="txt"><div class="time">${(theLastTime.checkTime?string("yyyy-MM-dd HH:mm:ss"))!""}</div>
							</td>						
						<#elseif theLastTime.checkID == 5>
							<td colspan=2" class="Script_normal">
								<div class="txt"><div class="time">${(theLastTime.checkTime?string("yyyy-MM-dd HH:mm:ss"))!""}</div>
							</td>
						<#elseif theLastTime.checkID == 6>					
							<td colspan=2" class="Script_normal">
								<div class="txt"><div class="time">${(theLastTime.checkTime?string("yyyy-MM-dd HH:mm:ss"))!""}</div>
							</td>
						<#else>
						</#if>
					</#list>
					<#else>
						<td colspan=2" class="Script_normal">
							<div class="txt"><div class="time"></div>
						</td>
						<td colspan=2" class="Script_normal">
							<div class="txt"><div class="time"></div>
						</td>
						<td colspan=2" class="Script_normal">
							<div class="txt"><div class="time"></div>
						</td>						
						<td colspan=2" class="Script_normal">
							<div class="txt"><div class="time"></div>
						</td>
						<td class=""></td>
					</#if>
				</tr>				
			</table>
		</div>
	<#else>	
		<div>
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
					<col width="auto"/>			
					<col width="14px"/>
				</colgroup>		
				<tr>
					<td class="Start">
						<a href="#" class="connectAdcChkResult">
							<img src="imgs/adcSetting/conntest_Start_on.png"/>
						</a>	
						<!--
						<img src="imgs/adcSetting/conntest_Start_off.png"/>
						-->	
					</td>
					
					<td class="State_sxn"></td>
					<td class="State_normal">① ${LANGCODEMAP["MSG_ADCSETTING_CHECK_NETWORK"]!}(ADCsmart→ADC)</td>
					<td class="State_nxn"></td>
					<td class="State_normal">② ${LANGCODEMAP["MSG_ADCSETTING_APPLIANCE_LOGIN"]!}</td>
					<td class="State_nxn"></td>				
					<td class="State_normal">③ ${LANGCODEMAP["MSG_ADCSETTING_CHECK_NETWORK"]!}(ADC→ADCsmart)</td>
					<td class="State_nxn"></td>				
					<td class="State_normal">④ SNMP</td>
					<td class="State_nxn"></td>				
					<td class="State_normal">⑤ syslog</td>
					<td class="State_n"></td>
				</tr>
				<tr>		
					<td class=""></td>
					<#if lastAdcChkTime??>
					<#list lastAdcChkTime as theLastTime>
						<#if theLastTime.checkID == 1>	
							<td colspan=2" class="Script_normal">
								<div class="txt"><div class="time">${(theLastTime.checkTime?string("yyyy-MM-dd HH:mm:ss"))!""}</div>
							</td>
						<#elseif theLastTime.checkID == 2>
							<td colspan=2" class="Script_normal">
								<div class="txt"><div class="time">${(theLastTime.checkTime?string("yyyy-MM-dd HH:mm:ss"))!""}</div>
							</td>					
						<#elseif theLastTime.checkID == 4>
							<td colspan=2" class="Script_normal">
								<div class="txt"><div class="time">${(theLastTime.checkTime?string("yyyy-MM-dd HH:mm:ss"))!""}</div>
							</td>
						<#elseif theLastTime.checkID == 5>
							<td colspan=2" class="Script_normal">
								<div class="txt"><div class="time">${(theLastTime.checkTime?string("yyyy-MM-dd HH:mm:ss"))!""}</div>
							</td>
						<#elseif theLastTime.checkID == 6>					
							<td colspan=2" class="Script_normal">
								<div class="txt"><div class="time">${(theLastTime.checkTime?string("yyyy-MM-dd HH:mm:ss"))!""}</div>
							</td>
						<#else>
						</#if>
					</#list>
					<#else>
						<td colspan=2" class="Script_normal">
							<div class="txt"><div class="time"></div>
						</td>
						<td colspan=2" class="Script_normal">
							<div class="txt"><div class="time"></div>
						</td>
						<td colspan=2" class="Script_normal">
							<div class="txt"><div class="time"></div>
						</td>
						<td colspan=2" class="Script_normal">
							<div class="txt"><div class="time"></div>
						</td>
						<td colspan=2" class="Script_normal">
							<div class="txt"><div class="time"></div>
						</td>
						<td class=""></td>
					</#if>
				</tr>				
			</table>
		</div>
	</#if>
	<#if opMode == 1>
	<#else> 
		<div class="setting_status_area">
			<div class="control_Board">
				<div class="title_h4_21">
					<li>${LANGCODEMAP["MSG_ADCSETTING_SETTING_STATUS"]!}</li>
				</div>
				<div class="control_positionR">
					<input type="button" class="refreshAdcSet Btn_white" value="${LANGCODEMAP["MSG_ADCSETTING_REFRESH"]!}"/>  			  
				</div>
			</div>
			<table class="Board_conn configTableIn" cellpadding="0" cellspacing="0">
				<caption>&nbsp;</caption>
				<colgroup>
					<col width="15%"/>
					<col width="auto"/>
				</colgroup>	
				<thead>						
				<tr>
					<th>${LANGCODEMAP["MSG_ADCSETTING_DIVISION"]!}</th>
		            <th>
			            <table class="" cellpadding="0" cellspacing="0">
							<colgroup>
								<col width="20%"/>
								<col width="30%"/>
								<col width="35%"/>
								<col width="15%"/>
							</colgroup>	
							<thead>            
				            <tr>			
								<th>${LANGCODEMAP["MSG_ADCSETTING_ARTICLE"]!}</th>
								<th>${LANGCODEMAP["MSG_ADCSETTING_ADCSMART"]!}</th>
								<th>${LANGCODEMAP["MSG_ADCSETTING_ADC"]!}</th>			
								<th>${LANGCODEMAP["MSG_ADCSETTING_SETTING_STATUS"]!}</th>
								<!--<th>${LANGCODEMAP["MSG_ADCSETTING_RESULT"]!}</th>
								<th>Action</th>-->
							</tr>
							</thead>
						</table>
					</th>
				</tr>
				</thead>	
				<tbody>				
				<tr>
					<td>vstat</td>
					<td>
						<table class="" cellpadding="0" cellspacing="0">
							<colgroup>
								<col width="20%"/>
								<col width="30%"/>
								<col width="35%"/>
								<col width="15%"/>
							</colgroup>						
							<tr>			
								<td>${LANGCODEMAP["MSG_ADCSETTING_CHECK_USE"]!}</td>
								<td></td>			
								<td></td>
								<td></td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td>snmp</td>
					<td>
						<table class="" cellpadding="0" cellspacing="0">
							<colgroup>
								<col width="20%"/>
								<col width="30%"/>
								<col width="35%"/>
								<col width="15%"/>
							</colgroup>						
							<tr>
								<td>${LANGCODEMAP["MSG_ADCSETTING_SNMP_VERSION"]!}</td>
								<td></td>
								<td></td>
								<td></td>			
							</tr>
							<tr>
								<td>read community</td>
								<td></td>
								<td></td>
								<td></td>			
							</tr>	
							<tr>
								<td>snmp user</td>
								<td></td>
								<td></td>
								<td></td>			
							</tr>
							<tr>
								<td>auth password</td>
								<td></td>
								<td></td>
								<td></td>			
							</tr>
							<tr>
								<td>priv password</td>
								<td></td>
								<td></td>
								<td></td>			
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td>syslog</td>
					<td>
						<table class="" cellpadding="0" cellspacing="0">
						<colgroup>
							<col width="20%"/>
							<col width="30%"/>
							<col width="35%"/>
							<col width="15%"/>
						</colgroup>						
						<tr>				
							<td>${LANGCODEMAP["MSG_ADCSETTING_CHECK_USE"]!}</td>
							<td></td>
							<td></td>
							<td></td>			
						</tr>	
						<tr>
							<td>server</td>
							<td></td>
							<td></td>
							<td></td>			
						</tr>
						</table>
					</td>		
				</tr>			
				</tbody>
			</table>
		</div>
	</#if>	
</div>          