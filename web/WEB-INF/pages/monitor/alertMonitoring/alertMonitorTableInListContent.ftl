<#if orderOption.orderType??>
	<#if 33 == orderOption.orderType></#if><!-- occurTime -->
	<#if 34 == orderOption.orderType></#if><!-- alertTime -->
	<#if 35 == orderOption.orderType></#if><!-- adcName -->	
	<#if 36 == orderOption.orderType></#if><!-- type -->
</#if>
<#if orderOption.orderDirection??>
	<#if 1 == orderOption.orderDirection></#if><!--Asc-->
	<#if 2 == orderOption.orderDirection></#if><!--Desc-->
</#if>
<caption>${LANGCODEMAP["MSG_MONITOR_ALERT_ADC_ALERT_LOG"]!}</caption>
	<colgroup>
		<col width="160px"/>
		<col width="160px"/>
		<col width="110px"/>		
		<col width="auto"/>
		<col width="180px"/>			
	</colgroup>
	<thead>
		<tr class="StartLine">
			<td colspan="5"></td>
		</tr>
		<tr class="ContentsHeadLine">
			<th>
				<span class="css_textCursor">
					<#if orderOption.orderDirection == 2 && orderOption.orderType == 33>		
						<a class="orderDir_Desc">${LANGCODEMAP["MSG_MONITOR_ALERT_OCCU_TIME"]!}
							<img src="imgs/Desc.gif"/>
							<span class="none orderType">33</span>
							<span class="none orderDir">1</span>					
						</a>						
					<#elseif orderOption.orderDirection == 1 && orderOption.orderType == 33>	
						<a class="orderDir_Asc">${LANGCODEMAP["MSG_MONITOR_ALERT_OCCU_TIME"]!}
							<img src="imgs/Asc.gif"/>
							<span class="none orderType">33</span>
							<span class="none orderDir">2</span>		
						</a>
					<#else>
						<a class="orderDir_None">${LANGCODEMAP["MSG_MONITOR_ALERT_OCCU_TIME"]!}
							<img src="imgs/none.gif"/>
							<span class="none orderType">33</span>
							<span class="none orderDir">2</span>		
						</a>
					</#if>
				</span>	
			</th>
			<th>
				<span class="css_textCursor">
					<#if orderOption.orderDirection == 2 && orderOption.orderType == 34>		
						<a class="orderDir_Desc">${LANGCODEMAP["MSG_MONITOR_ALERT_TIME"]!}
							<img src="imgs/Desc.gif"/>
							<span class="none orderType">34</span>
							<span class="none orderDir">1</span>					
						</a>						
					<#elseif orderOption.orderDirection == 1 && orderOption.orderType == 34>	
						<a class="orderDir_Asc">${LANGCODEMAP["MSG_MONITOR_ALERT_TIME"]!}
							<img src="imgs/Asc.gif"/>
							<span class="none orderType">34</span>
							<span class="none orderDir">2</span>		
						</a>
					<#else>
						<a class="orderDir_None">${LANGCODEMAP["MSG_MONITOR_ALERT_TIME"]!}
							<img src="imgs/none.gif"/>
							<span class="none orderType">34</span>
							<span class="none orderDir">2</span>		
						</a>
					</#if>
				</span>	
			</th>
			<th>
				<span class="css_textCursor">
					<#if orderOption.orderDirection == 2 && orderOption.orderType == 36>		
						<a class="orderDir_Desc">${LANGCODEMAP["MSG_MONITOR_ALERT_TYPE"]!}
							<img src="imgs/Desc.gif"/>
							<span class="none orderType">36</span>
							<span class="none orderDir">1</span>					
						</a>						
					<#elseif orderOption.orderDirection == 1 && orderOption.orderType == 36>	
						<a class="orderDir_Asc">${LANGCODEMAP["MSG_MONITOR_ALERT_TYPE"]!}
							<img src="imgs/Asc.gif"/>
							<span class="none orderType">36</span>
							<span class="none orderDir">2</span>		
						</a>
					<#else>
						<a class="orderDir_None">${LANGCODEMAP["MSG_MONITOR_ALERT_TYPE"]!}
							<img src="imgs/none.gif"/>
							<span class="none orderType">36</span>
							<span class="none orderDir">2</span>		
						</a>
					</#if>
				</span>
			</th>			
			<th>내용</th>	
			<th>경보대응설정</th>				
		</tr>
		<tr class="StartLine1">
			<td colspan="5"></td>
		</tr>
	</thead>	
	<tbody>
	<#list adcAlertLogList![] as adcAlertLog>
		<tr class="ContentsLine1 adcAlertLogList">
			<td class="align_center textOver">${adcAlertLog.occurTime!""}</td>
			<td class="align_center textOver">${adcAlertLog.alertTime!""}</td>
			<td class="align_left_P10">
				<input type="hidden" class="alert-occurtime" value="${adcAlertLog.occurTime!}" />
				<input type="hidden" class="alert-alerttime" value="${adcAlertLog.alertTime!}" />
				<input type="hidden" class="alert-type" value="${adcAlertLog.type}" />
				<input type="hidden" class="alert-status" value="${adcAlertLog.status}" />
				<input type="hidden" class="alert-actionsyslog" value="${adcAlertLog.actionSyslog}" />
				<input type="hidden" class="alert-adcname" value="-1" />
				<input type="hidden" class="alert-event" value='${adcAlertLog.event}' />
				<#if adcAlertLog.type == 0>
					<#if adcAlertLog.status == 1>
					<span class="txt_gray3">
						<img src="imgs/icon/icon_critical_none.gif" alt="${LANGCODEMAP["MSG_LFAULT_FAULT_RESOLVE"]!}">${LANGCODEMAP["MSG_LFAULT_FAULT_RESOLVE"]!}
					</span>
					<#elseif adcAlertLog.status == 0>
						<img src="imgs/icon/icon_critical.gif" alt="${LANGCODEMAP["MSG_LFAULT_FAULT_OCCUR"]!}">${LANGCODEMAP["MSG_LFAULT_FAULT_OCCUR"]!}
					<#else>
						<img src="imgs/icon/icon_warning.gif" alt="${LANGCODEMAP["MSG_FAULT_WARNING"]!}">${LANGCODEMAP["MSG_FAULT_WARNING"]!}
					</#if>
				<#else>
					<img src="imgs/icon/icon_warning.gif" alt="${LANGCODEMAP["MSG_FAULT_WARNING"]!}">${LANGCODEMAP["MSG_FAULT_WARNING"]!}
				</#if>
			</td>			
			<td class="align_left_P10  textOver">
				<a class="popuplink" href="#">${adcAlertLog.event!""}</a>	
			</td>
			<td class="align_center">
			<#if adcAlertLog.actionSyslog == 1>
				<span class="position_R1L1">					
					<img src="imgs/icon/icon_action_syslog.png" alt="syslog" title="syslog">									
				</span>							
			<#else>
				<span class="position_R1L1">
					<img src="imgs/icon/icon_action_syslog_none.png" alt="syslog" title="syslog">
				</span>				
			</#if>
			<#if adcAlertLog.actionSnmptrap == 1>
				<span class="position_R1L1">					
					<img src="imgs/icon/icon_action_snmptrap.png" alt="snmp trap" title="snmp trap">									
				</span>								
			<#else>
				<span class="position_R1L1">
					<img src="imgs/icon/icon_action_snmptrap_none.png" alt="snmp trap" title="snmp trap">
				</span>			
			</#if>	
			<#if adcAlertLog.actionSMS == 1>
                <span class="position_R1L1">                    
                    <img src="imgs/icon/icon_action_sms.png" alt="sms" title="sms">                                    
                </span>                             
            <#else>
                <span class="position_R1L1">
                    <img src="imgs/icon/icon_action_sms_none.png" alt="sms" title="sms">
                </span>         
            </#if>  
			</td>				
		</tr>
		<tr class="DivideLine">
			<td colspan="5"></td>
		</tr>
	</#list>
		<tr class="EndLine">
			<td colspan="5"></td>
		</tr>						
	</tbody>