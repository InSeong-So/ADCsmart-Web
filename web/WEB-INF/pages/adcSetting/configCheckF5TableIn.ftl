<table class="Board configTableIn" cellpadding="0" cellspacing="0">
	<caption>&nbsp;</caption>
			<colgroup>
				<col width="15%"/>
				<col width="auto"/>
			</colgroup>			
	<thead>		
	<tr class="ContentsHeadLine">
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
			<!--
			<th>${LANGCODEMAP["MSG_ADCSETTING_RESULT"]!}</th>
			<th>Action</th>
			-->
		</tr>
		</thead>
		</table>
		</th>
	</tr>
	</thead>	
	<tbody>	
	<#if adcConfigList??>
	<#list adcConfigList as theAdcConfig>				
	<#if theAdcConfig.configID == 12> 
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
				<td>read community</td>
				<td><pre>${(theAdcConfig.localInfo)!""}</pre></td>		
				<td><pre>${(theAdcConfig.adcInfo)!""}</pre></td>
				<#if theAdcConfig.status == 0>
					<td></td>
				<#elseif theAdcConfig.status == 1> 
					<td><span class="set_ok_txt">${LANGCODEMAP["MSG_ADCSETTING_AVAILABLE"]!}</span></td>
				<#elseif theAdcConfig.status == 2>				
					<td class="align_centert"><span class="set_fail_txt">${LANGCODEMAP["MSG_ADCSETTING_UNAVAILABLE"]!}</span></td>
				<#elseif theAdcConfig.status == 3>
					<td>${LANGCODEMAP["MSG_ADCSETTING_FAIL"]!}</td>
				</#if>
				<!-- 버튼 기능 구현 보류 -->
				<!-- 		
				<td> 
		 			<#if !(config.snmpRcommunityResult!"")?? || (config.snmpRcommunityResult)! =="${LANGCODEMAP['MSG_CONSTANT_FAIL']!}">  
		 				<span class="button black small"> 
		 					<button type="button" class="snmpRcommunityConfigkLnk">${LANGCODEMAP["MSG_ADCSETTING_SYN"]!}</button> 
		 				</span>	 
		 			<#else> 
		 				<img src="imgs/btn${img_lang!""}/btn_sync_off.gif" alt="syncOff"/>				 
		 			</#if>				 
		 		</td>					 
		 		-->
	 		</tr>
			<#elseif theAdcConfig.configID == 13>
			<tr>
				<td>Client Allow List</td>
				<td><pre>${(theAdcConfig.localInfo)!""}</pre></td>
				<td><pre>${(theAdcConfig.adcInfo)!""}</pre></td>
				<#if theAdcConfig.status == 0>
					<td></td>
				<#elseif theAdcConfig.status == 1> 
					<td><span class="set_ok_txt">${LANGCODEMAP["MSG_ADCSETTING_AVAILABLE"]!}</span></td>
				<#elseif theAdcConfig.status == 2>				
					<td><span class="set_fail_txt">${LANGCODEMAP["MSG_ADCSETTING_UNAVAILABLE"]!}</span></td>
				<#elseif theAdcConfig.status == 3>
					<td>${LANGCODEMAP["MSG_ADCSETTING_FAIL"]!}</td>
				</#if>
				<!-- 버튼 기능 구현 보류 -->
				<!-- 		
				<td> 
		 			<#if !(config.allowListResult!"")?? || (config.allowListResult)! =="${LANGCODEMAP['MSG_CONSTANT_FAIL']!}"> 
		 				<span class="button black small"> 
		 					<button type="button" class="snmpAllowListConfigkLnk">${LANGCODEMAP["MSG_ADCSETTING_CHANGE"]!}</button> 
		 				</span>	 
		 			<#else> 
		 				<img src="imgs/btn${img_lang!""}/btn_sync_off.gif" alt="syncOff"/>				 
		 			</#if>				 
		 		</td>					 
		 		-->
			</tr>	
 		</table>
 		</td>
	</tr>						
	<#elseif theAdcConfig.configID == 22>
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
				<td>server</td>
				<td><pre>${(theAdcConfig.localInfo)!""}</pre></td>
				<td><pre>${(theAdcConfig.adcInfo)!""}</pre></td>
				<#if theAdcConfig.status == 0>
					<td></td>
				<#elseif theAdcConfig.status == 1> 
					<td><span class="set_ok_txt">${LANGCODEMAP["MSG_ADCSETTING_AVAILABLE"]!}</span></td>
				<#elseif theAdcConfig.status == 2>				
					<td><span class="set_fail_txt">${LANGCODEMAP["MSG_ADCSETTING_UNAVAILABLE"]!}</span></td>
				<#elseif theAdcConfig.status == 3>
					<td>${LANGCODEMAP["MSG_ADCSETTING_FAIL"]!}</td>
				</#if>
				<!-- 버튼 기능 구현 보류 -->
				<!-- 		
				<td> 
		 			<#if !(config.syslogServerListResult!"")?? ||(config.syslogServerListResult)! == "${AdcConstants.STATUS_FAIL}">  
						<span class="button black small"> 
		 					<button type="button" class="syslogListConfigkLnk">${LANGCODEMAP["MSG_ADCSETTING_CHANGE"]!}</button> 
		 				</span>	 
		 			<#else> 
		 				<img src="imgs/btn${img_lang!""}/btn_change_off.gif" alt="changeOff"/>				 
		 			</#if>			 
		 		</td>						 
		 		-->
	 		</tr>
 		</table>
 		</td>		 		
	</tr>
	<#elseif theAdcConfig.configID == 32>			
	<tr>
		<td>ssh</td>
		<td>
		<table class="" cellpadding="0" cellspacing="0">
			<colgroup>
				<col width="20%"/>
				<col width="30%"/>
				<col width="35%"/>
				<col width="15%"/>
			</colgroup>						
			<tr>		
				<td>host</td>
				<td><pre>${(theAdcConfig.localInfo)!""}</pre></td>
				<td><pre>${(theAdcConfig.adcInfo)!""}</pre></td>
				<#if theAdcConfig.status == 0>
					<td></td>
				<#elseif theAdcConfig.status == 1> 
					<td><span class="set_ok_txt">${LANGCODEMAP["MSG_ADCSETTING_AVAILABLE"]!}</span></td>
				<#elseif theAdcConfig.status == 2>				
					<td><span class="set_fail_txt">${LANGCODEMAP["MSG_ADCSETTING_UNAVAILABLE"]!}</span></td>
				<#elseif theAdcConfig.status == 3>
					<td>${LANGCODEMAP["MSG_ADCSETTING_FAIL"]!}</td>
				</#if>
				<!-- 버튼 기능 구현 보류 -->
				<!-- 		
				<td> 
		 			<#if !(config.syslogServerListResult!"")?? ||(config.syslogServerListResult)! == "${AdcConstants.STATUS_FAIL}">  
		 				<span class="button black small"> 
		 					<button type="button" class="syslogListConfigkLnk">${LANGCODEMAP["MSG_ADCSETTING_CHANGE"]!}</button> 
		 				</span>	 
		 			<#else> 
		 				<img src="imgs/btn${img_lang!""}/btn_change_off.gif" alt="changeOff"/>				 
		 			</#if>			 
		 		</td>						 
		 		-->
	 		</tr>
 		</table>
 		</td>
	</tr>
	<#else>
	</#if>
	</#list>
	<#else>				
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
				<td>read community</td>
				<td></td>
				<td></td>
				<td></td>	
			</tr>		
			<tr>
				<td>Client Allow List</td>
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
				<td>server</td>
				<td></td>
				<td></td>
				<td></td>
			</tr>			
		</table>
		</td>
	</tr>
	<!--
	<tr>
		<td>https</td>
		<td>
		<table class="" cellpadding="0" cellspacing="0">
			<colgroup>
				<col width="20%"/>
				<col width="30%"/>
				<col width="35%"/>
				<col width="15%"/>
			</colgroup>						
			<tr>		
				<td>host</td>
				<td></td>
				<td></td>
				<td></td>	
			</tr>
		</table>
		</td>		
	</tr>
	-->
	<tr>
		<td>ssh</td>
		<td>
		<table class="" cellpadding="0" cellspacing="0">
			<colgroup>
				<col width="20%"/>
				<col width="30%"/>
				<col width="35%"/>
				<col width="15%"/>
			</colgroup>						
			<tr>		
				<td>host</td>
				<td></td>
				<td></td>
				<td></td>
			</tr>
		</table>
		</td>			
	</tr>
	</#if>
</table>