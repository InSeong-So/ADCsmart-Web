<#setting number_format="0.####">
<div class="pktDumpStatusTableData">
<table class="table_type12" >
<colgroup>
	<col width="170px" />
	<col width="200px" />
	<col width="130px" />
	<col width="130px" />	
	<col width="60px" />								
	<col width="auto" />
</colgroup>
<thead>
	<tr>
		<th>ADC</th>
		<th>${LANGCODEMAP["MSG_DIAG_ANAL_STATUS"]!}</th>
		<th>${LANGCODEMAP["MSG_DIAG_ANAL_START_TIME"]!}</th>
		<th>${LANGCODEMAP["MSG_DIAG_ANAL_END_TIME"]!}</th>
		<th>CPU</th>
		<th>Memory</th>	
	</tr>
</thead>
</table>
<div class="list">		
	<table class="table_type12">
	<colgroup>
		<col width="170px" />
		<col width="200px" />
		<col width="130px" />
		<col width="130px" />	
		<col width="60px" />								
		<col width="auto" />
	</colgroup>
	<tbody>	
		<#list pktDumpStatusInfoList as thePktDumpStatus>
		<tr class="ContentsLine1" id=${thePktDumpStatus.adcIndex}>		
			<td>
				<span class="none logKey">${thePktDumpStatus.logKey!''},</span>
				<span class="none adcIndex">${thePktDumpStatus.adcIndex!''}</span>
				<#if adcObject.category == 0>
					<#if thePktDumpStatus.adcType == 2>
						<img src="/imgs/icon/adc/icon_alteon_s.png"/>
					<#elseif thePktDumpStatus.adcType == 1>
						<img src="/imgs/icon/adc/icon_f5_s.png"/>
					<#elseif thePktDumpStatus.adcType == 3>
						<img src="/imgs/icon/adc/icon_piolink_s.png"/>
					<#elseif thePktDumpStatus.adcType == 4>
						<img src="/imgs/icon/adc/icon_piolink_s.png"/>
					<#else>
						<img src="/imgs/icon/adc/icon_alteon_s.png"/>
					</#if>
					&nbsp;<span class="txt_bold">${thePktDumpStatus.adcName!''}</span>
				<#elseif adcObject.category == 1>
					<#if thePktDumpStatus.adcType == 2>
						<img src="/imgs/icon/adc/icon_alteon_s.png"/>
					<#elseif thePktDumpStatus.adcType == 1>
						<img src="/imgs/icon/adc/icon_f5_s.png"/>
					<#elseif thePktDumpStatus.adcType == 3>
						<img src="/imgs/icon/adc/icon_piolink_s.png"/>
					<#elseif thePktDumpStatus.adcType == 4>
						<img src="/imgs/icon/adc/icon_piolink_s.png"/>
					<#else>
						<img src="/imgs/icon/adc/icon_alteon_s.png"/>
					</#if>
					&nbsp;<span class="txt_bold">${thePktDumpStatus.adcName!''}</span>
				<#elseif adcObject.category == 2>
					<#if adc.type == 'Alteon'>
						<img src="/imgs/icon/adc/icon_alteon_s.png"/>
					<#elseif adc.type == 'F5'>
						<img src="/imgs/icon/adc/icon_f5_s.png"/>
					<#elseif adc.type == 'PAS'>
						<img src="/imgs/icon/adc/icon_piolink_s.png"/>
					<#elseif adc.type == 'PASK'>
						<img src="/imgs/icon/adc/icon_piolink_s.png"/>
					<#else>
						<img src="/imgs/icon/adc/icon_alteon_s.png"/>
					</#if>
					&nbsp;<span class="txt_bold">${thePktDumpStatus.adcName!''}</span>
				<#else>
				</#if> 
			</td>		
			<#if (thePktDumpStatus.progressRate) == 100 || (thePktDumpStatus.progressRate) == 102> 
			<td> 		
				<span class="none pktDumpIndex">${thePktDumpStatus.logKey!''}</span>	
				<span class="downloadPktDumpLnk">				
				 	<input type="button" class="Btn_black" value="${LANGCODEMAP["MSG_DIAG_ANAL_DOWNLOAD_PACKET"]!}">
		    	</span>	    	
		    </td>
		    <#elseif (thePktDumpStatus.progressRate) == 101>
			<td>					
				${LANGCODEMAP["MSG_CONSTANT_FAIL"]!}
			</td>
			<#else>
			<td>					
				<img src="/imgs/icon/loading_0.gif" />&nbsp; ${LANGCODEMAP["MSG_DIAG_ANAL_ELAPSE_TIME"]!} ${thePktDumpStatus.elapsedTime!''} ${LANGCODEMAP["MSG_DIAG_ANAL_SEC"]!} &nbsp;
			</td>
			</#if>
			<td>${(thePktDumpStatus.startTime?string("yyyy-MM-dd HH:mm:ss"))!}</td>
			<td>${(thePktDumpStatus.endTime?string("yyyy-MM-dd HH:mm:ss"))!}</td>
			<td>
				<#if (thePktDumpStatus.lastCpuUsage) == -1>
					-					
				<#else>
					${thePktDumpStatus.lastCpuUsage!''}%
				</#if>
			</td>
			<td>
				<#if (thePktDumpStatus.lastMemUsage) == -1>
					-					
				<#else>
					${thePktDumpStatus.lastMemUsage!''}%
				</#if>
			</td>		
		</tr>
		</#list>
	</tbody>
	</table>	
</div>