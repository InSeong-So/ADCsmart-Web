<#if orderOption.orderType??>
	<#if 11 == orderOption.orderType></#if><!-- occurTime -->
	<#if 12 == orderOption.orderType></#if><!-- solvedTime -->
	<#if  8 == orderOption.orderType></#if><!-- adcName -->
	<#if 13 == orderOption.orderType></#if><!-- content -->
</#if>
<#if orderOption.orderDirection??>
	<#if 1 == orderOption.orderDirection></#if><!--Asc-->
	<#if 2 == orderOption.orderDirection></#if><!--Desc-->
</#if>
<caption>${LANGCODEMAP["MSG_FAULT_ADC_FAULT_FAULT"]!}</caption>
<colgroup>
	<col width="160px"/>
	<col width="110px"/>	
	<col width="auto"/>
</colgroup>
<thead>
	<tr class="StartLine">
		<td colspan="3"></td>
	</tr>
	<tr class="ContentsHeadLine">
		<input class="none" value="2" name="orderDir_Desc" />
		<input class="none" value="1" name="orderDir_Asc" />		
		<th>
			<span class="css_textCursor">
				<#if orderOption.orderDirection == 2 && orderOption.orderType == 11>		
					<a class="orderDir_Desc">${LANGCODEMAP["MSG_FAULT_OCCU_TIME"]!}
						<img src="imgs/Desc.gif"/>
						<span class="none orderType">11</span>
						<span class="none orderDir">1</span>					
					</a>						
				<#elseif orderOption.orderDirection == 1 && orderOption.orderType == 11>	
					<a class="orderDir_Asc">${LANGCODEMAP["MSG_FAULT_OCCU_TIME"]!}
						<img src="imgs/Asc.gif"/>
						<span class="none orderType">11</span>
						<span class="none orderDir">2</span>		
					</a>
				<#else>
					<a class="orderDir_None">${LANGCODEMAP["MSG_FAULT_OCCU_TIME"]!}
						<img src="imgs/none.gif"/>
						<span class="none orderType">11</span>
						<span class="none orderDir">2</span>		
					</a>
				</#if>
			</span>	
		</th>	
		<th>
			<span class="css_textCursor">
				<#if orderOption.orderDirection == 2 && orderOption.orderType == 15>		
					<a class="orderDir_Desc">${LANGCODEMAP["MSG_FAULT_TYPE"]!}
						<img src="imgs/Desc.gif"/>
						<span class="none orderType">15</span>
						<span class="none orderDir">1</span>					
					</a>						
				<#elseif orderOption.orderDirection == 1 && orderOption.orderType == 15>	
					<a class="orderDir_Asc">${LANGCODEMAP["MSG_FAULT_TYPE"]!}
						<img src="imgs/Asc.gif"/>
						<span class="none orderType">15</span>
						<span class="none orderDir">2</span>		
					</a>
				<#else>
					<a class="orderDir_None">${LANGCODEMAP["MSG_FAULT_TYPE"]!}
						<img src="imgs/none.gif"/>
						<span class="none orderType">15</span>
						<span class="none orderDir">2</span>		
					</a>
				</#if>
			</span>					
		</th>			
		<th> ${LANGCODEMAP["MSG_FAULT_CONTENT"]!} </th>
	</tr>
	<tr class="StartLine1">
		<td colspan="3"></td>
	</tr>	
</thead>
<tbody>
	<#list adcSystemLogList![] as adcSystemLog>
	<tr class="ContentsLine3 faultLogList">
		<td class="align_center textOver">${adcSystemLog.occurredTime!""}</td>
		<td class="align_left_P10">
			<input type="hidden" class="fault-adcname" value="${adcSystemLog.adcName!}" />
			<input type="hidden" class="fault-occurtime" value="${adcSystemLog.occurredTime!}" />			
			<input type="hidden" class="fault-status" value="${adcSystemLog.status!}" />			
			<input type="hidden" class="fault-event" value='${adcSystemLog.event!}' />
		<#if 0 == adcSystemLog.status>
			<img src="imgs/icon/icon_critical.gif" alt="${LANGCODEMAP["MSG_LFAULT_FAULT_OCCUR"]!}">${LANGCODEMAP["MSG_LFAULT_FAULT_OCCUR"]!}
		<#elseif 1 == adcSystemLog.status>
		<span class="txt_gray3">
			<img src="imgs/icon/icon_critical_none.gif" alt="${LANGCODEMAP["MSG_LFAULT_FAULT_RESOLVE"]!}">${LANGCODEMAP["MSG_LFAULT_FAULT_RESOLVE"]!}
		</span>
		<#else>
			<img src="imgs/icon/icon_warning.gif" alt="${LANGCODEMAP["MSG_FAULT_WARNING"]!}">${LANGCODEMAP["MSG_FAULT_WARNING"]!}
		</#if>
		</td>
		<td class="align_left_P10 textOver">
			<a class="fault-popuplink" href="#">${adcSystemLog.event!""}</a>
		</td>
	</tr>
	<tr class="DivideLine">
		<td colspan="3"></td>
	</tr>
	</#list>
</tbody>
<tr class="EndLine">
	<td colspan="3"></td>
</tr>