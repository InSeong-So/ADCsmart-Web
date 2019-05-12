<colgroup>
	<col width="auto">
	<col width="20%"/>
	<col width="20%"/>
	<col width="20%"/>	
</colgroup>
<tbody>
	<tr>
		<th></th>
		<th>Current</th>
		<th>Avg</th>
		<th>Max</th>					
	</tr>
	<tr class="compareChk none">
		<td>${LANGCODEMAP["MSG_PERFAVGMAX_GROWTH"]!}</td>
		<td>
		<#if ResponseTimeInfo.subtractionCurrRespTime != -1>
			<#if (ResponseTimeInfo.subtractionCurrRespTime >0) >
			<div class="up">
			<#elseif (ResponseTimeInfo.subtractionCurrRespTime < 0 ) >
			<div class="down">
			<#else>
			<div class="sam">
			</#if>
				<#if (ResponseTimeInfo.currRespTime??) && (ResponseTimeInfo.preCurrRespTime??)>
					<p class="responseData none">${(ResponseTimeInfo.subtractionCurrRespTimePercent?replace("-1", "-"))!""}&nbsp;%</p>
					<p class="responseData none"><span class="control_positionL bu"></span>${(ResponseTimeInfo.subtractionCurrRespTime?replace("-1", "-"))!""}</p>
				<#else>
					-
				</#if>
			</div>
		<#else>
			-
		</#if>						
		</td>	
		<td>
		<#if ResponseTimeInfo.subtractionAvgRespTime != -1>
			<#if (ResponseTimeInfo.subtractionAvgRespTime >0) >
			<div class="up">
			<#elseif (ResponseTimeInfo.subtractionAvgRespTime < 0 ) >
			<div class="down">
			<#else>
			<div class="sam">
			</#if>
				<#if (ResponseTimeInfo.avgRespTime??) && (ResponseTimeInfo.preAvgRespTime??)>
					<p class="responseData none">${(ResponseTimeInfo.subtractionAvgRespTimePercent?replace("-1", "-"))!""}&nbsp;%</p>
					<p class="responseData none"><span class="control_positionL bu"></span>${(ResponseTimeInfo.subtractionAvgRespTime?replace("-1", "-"))!""}</p>
				<#else>
					-
				</#if>
			</div>	
		<#else>
			-
		</#if>		
		</td>	
		<td>
		<#if ResponseTimeInfo.subtractionMaxRespTime != -1>
			<#if (ResponseTimeInfo.subtractionMaxRespTime >0) >
			<div class="up">
			<#elseif (ResponseTimeInfo.subtractionMaxRespTime < 0 ) >
			<div class="down">
			<#else>
			<div class="sam">
			</#if>
				<#if (ResponseTimeInfo.maxRespTime??) && (ResponseTimeInfo.preMaxRespTime??)>
					<p class="responseData none">${(ResponseTimeInfo.subtractionMaxRespTimePercent?replace("-1", "-"))!""}&nbsp;%</p>
					<p class="responseData none"><span class="control_positionL bu"></span>${(ResponseTimeInfo.subtractionMaxRespTime?replace("-1", "-"))!""}</p>
				<#else>
					-
				</#if>
			</div>
		<#else>
			-
		</#if>			
		</td>							
	</tr>		
	<tr>
		<td class="textOver"><span class="square chart_1st"></span><span id="vsNameChange">/Common/route_int</span></td>		
		<td class="responseData">${(ResponseTimeInfo.currRespTime?replace("-1", "-"))!""}</td>
		<td class="responseData">${(ResponseTimeInfo.avgRespTime?replace("-1", "-"))!""}</td>
		<td class="responseData">${(ResponseTimeInfo.maxRespTime?replace("-1", "-"))!""}</td>							
	</tr>						
	<tr class="compareChk none">
		<td><span class="square compare_1st"></span><span id="compareChange">${LANGCODEMAP["MSG_PERFAVGMAX_PRE"]!}</span></td>			
		<td class="responseData none">${(ResponseTimeInfo.preCurrRespTime?replace("-1", "-"))!""}</td>
		<td class="responseData none">${(ResponseTimeInfo.preAvgRespTime?replace("-1", "-"))!""}</td>
		<td class="responseData none">${(ResponseTimeInfo.preMaxRespTime?replace("-1", "-"))!""}</td>									
	</tr>
</tbody>