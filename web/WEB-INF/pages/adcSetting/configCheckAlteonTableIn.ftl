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
						<!--<th class="align_center Rcolor">${LANGCODEMAP["MSG_ADCSETTING_RESULT"]!}</th>
						<th class="align_center">Action</th>-->
					</tr>
					</thead>
				</table>
			</th>
		</tr>
	</thead>
<tbody>
	<#if adcConfigList??>
	<#list adcConfigList as theAdcConfig>	
	<#if theAdcConfig.configID == 1> 
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
				</tr>	
			</table>
		</td>
	</tr>							
		<!-- 버튼 기능 구현 보류 -->
		<!-- 		
		<td class="align_center">
			<#if !(config.vstatStateResult!"")?? || (config.vstatStateResult)! == "${AdcConstants.STATUS_FAIL}">  
 				<span class="button black small">>
					<button type="button" class="vstatConfigLnk">${LANGCODEMAP["MSG_ADCSETTING_CHANGE"]!}</button>
				</span>
 			<#else> 
			<img src="imgs/btn${img_lang!""}/btn_change_off.gif" alt="changeOff"/>				 
 			</#if>			 
 		</td>					 
		-->
	<#elseif theAdcConfig.configID == 11> 
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
				<td class="align_center"> 
		 			<#if !(config.snmpStateResult!"")?? || (config.snmpStateResult)! == "${AdcConstants.STATUS_FAIL}">  
		 				<span class="button black small"> 
		 					<button type="button" class="snmpConfigkLnk">${LANGCODEMAP["MSG_ADCSETTING_CHANGE"]!}</button> 
		 				</span> 
		 			<#else> 
		 				<img src="imgs/btn${img_lang!""}/btn_change_off.gif" alt="changeOff"/> 
		 			</#if>		 
		 		</td>					
		 		-->
			</tr>
			<#elseif theAdcConfig.configID == 12>
			<tr>
				<td>read community</td>
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
				<td class="align_center"> 
		 			<#if !(config.snmpRcommunityResult!"")?? || (config.snmpRcommunityResult)! == "${AdcConstants.STATUS_FAIL}"> 
		 				<span class="button black small"> 
		 					<button type="button" class="snmpRcommunityConfigkLnk">${LANGCODEMAP["MSG_ADCSETTING_SYN"]!}</button> 
		 				</span>	 
		 			<#else> 
		 				<img src="imgs/btn${img_lang!""}/btn_sync_off.gif" alt="syncOff"/>								 
		 			</#if>				 
		 		</td>					 
				-->
			</tr>	
			<#elseif theAdcConfig.configID == 14>
			<tr>
				<td>snmp user</td>
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
				<td class="align_center"> 
		 			<#if !(config.snmpRcommunityResult!"")?? || (config.snmpRcommunityResult)! == "${AdcConstants.STATUS_FAIL}"> 
		 				<span class="button black small"> 
		 					<button type="button" class="snmpRcommunityConfigkLnk">${LANGCODEMAP["MSG_ADCSETTING_SYN"]!}</button> 
		 				</span>	 
		 			<#else> 
		 				<img src="imgs/btn${img_lang!""}/btn_sync_off.gif" alt="syncOff"/>								 
		 			</#if>				 
		 		</td>					 
				-->
			</tr>
			<#elseif theAdcConfig.configID == 15>
			<tr>
				<td>auth password</td>
				<td><pre style="-webkit-text-security:disc">${(theAdcConfig.localInfo)!""}</pre></td>
				<td><pre style="-webkit-text-security:disc">${(theAdcConfig.adcInfo)!""}</pre></td>
				<#if theAdcConfig.status == 0>
					<td></td>
				<#elseif theAdcConfig.status == 1> 
					<td><span class="set_ok_txt">${LANGCODEMAP["MSG_ADCSETTING_AGREE"]!}</span></td>
				<#elseif theAdcConfig.status == 2>				
					<td><span class="set_fail_txt">${LANGCODEMAP["MSG_ADCSETTING_DISAGREE"]!}</span></td>
				<#elseif theAdcConfig.status == 3>
					<td>${LANGCODEMAP["MSG_ADCSETTING_FAIL"]!}</td>
				</#if>
				<!-- 버튼 기능 구현 보류 -->
				<!-- 
				<td class="align_center"> 
		 			<#if !(config.snmpRcommunityResult!"")?? || (config.snmpRcommunityResult)! == "${AdcConstants.STATUS_FAIL}"> 
		 				<span class="button black small"> 
		 					<button type="button" class="snmpRcommunityConfigkLnk">${LANGCODEMAP["MSG_ADCSETTING_SYN"]!}</button> 
		 				</span>	 
		 			<#else> 
		 				<img src="imgs/btn${img_lang!""}/btn_sync_off.gif" alt="syncOff"/>								 
		 			</#if>				 
		 		</td>					 
				-->
			</tr>
			<#elseif theAdcConfig.configID == 55>
			<tr>
				<td>priv password</td>
				<td><pre style="-webkit-text-security:disc">${(theAdcConfig.localInfo)!""}</pre></td>
				<td><pre style="-webkit-text-security:disc">${(theAdcConfig.adcInfo)!""}</pre></td>
				<#if theAdcConfig.status == 0>
					<td></td>
				<#elseif theAdcConfig.status == 1> 
					<td><span class="set_ok_txt">${LANGCODEMAP["MSG_ADCSETTING_AGREE"]!}</span></td>
				<#elseif theAdcConfig.status == 2>				
					<td><span class="set_fail_txt">${LANGCODEMAP["MSG_ADCSETTING_DISAGREE"]!}</span></td>
				<#elseif theAdcConfig.status == 3>
					<td>${LANGCODEMAP["MSG_ADCSETTING_FAIL"]!}</td>
				</#if>
				<!-- 버튼 기능 구현 보류 -->
				<!-- 
				<td class="align_center"> 
		 			<#if !(config.snmpRcommunityResult!"")?? || (config.snmpRcommunityResult)! == "${AdcConstants.STATUS_FAIL}"> 
		 				<span class="button black small"> 
		 					<button type="button" class="snmpRcommunityConfigkLnk">${LANGCODEMAP["MSG_ADCSETTING_SYN"]!}</button> 
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
	<#elseif theAdcConfig.configID == 21>
	<tr>
		<td >syslog</td>	
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
					<td class="align_center">
			 			<#if !(config.syslogStateResult!"")?? || (config.syslogStateResult)! == "${AdcConstants.STATUS_FAIL}"> 
			 				<span class="button black small"> 
			 					<button type="button" class="syslogConfigkLnk">${LANGCODEMAP["MSG_ADCSETTING_CHANGE"]!}</button> 
			 				</span>
			 			<#else>
			 				<img src="imgs/btn${img_lang!""}/btn_change_off.gif" alt="changeOff"/>								 
			 			</#if>				  
			 		</td>					 
					-->
			</tr>	
			<#elseif theAdcConfig.configID == 22>
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
				<td class="align_center">
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
	</#if>		
</tbody>	
</table> 

