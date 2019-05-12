	<tr class="compareChk none">
		<td>${LANGCODEMAP["MSG_PERFAVGMAX_GROWTH"]!}</td>
		<td>
			<#if (BpsConnAvgMaxInfo.subtractionCurrConnCurr >0) >
			<div class="up">
			<#elseif (BpsConnAvgMaxInfo.subtractionCurrConnCurr < 0 ) >
			<div class="down">
			<#else>
			<div class="sam">
			</#if>
				<p class="connCurrData none">${(BpsConnAvgMaxInfo.subtractionCurrConnCurrPercent?replace("-1", "-"))!""}&nbsp;%</p>
				<p class="connCurrData none"><span class="control_positionL bu"></span>${(BpsConnAvgMaxInfo.subtractionCurrConnCurrUnit?replace("-1", "-"))!""}</p>
			</div>			
		</td>	
		<td>
			<#if (BpsConnAvgMaxInfo.subtractionAvgConnCurr >0) >
			<div class="up">
			<#elseif (BpsConnAvgMaxInfo.subtractionAvgConnCurr < 0 ) >
			<div class="down">
			<#else>
			<div class="sam">
			</#if>
				<p class="connCurrData none">${(BpsConnAvgMaxInfo.subtractionAvgConnCurrPercent?replace("-1", "-"))!""}&nbsp;%</p>
				<p class="connCurrData none"><span class="control_positionL bu"></span>${(BpsConnAvgMaxInfo.subtractionAvgConnCurrUnit?replace("-1", "-"))!""}</p>
			</div>			
		</td>	
		<td>
			<#if (BpsConnAvgMaxInfo.subtractionMaxConnCurr >0) >
			<div class="up">
			<#elseif (BpsConnAvgMaxInfo.subtractionMaxConnCurr < 0 ) >
			<div class="down">
			<#else>
			<div class="sam">
			</#if>
				<p class="connCurrData none">${(BpsConnAvgMaxInfo.subtractionMaxConnCurrPercent?replace("-1", "-"))!""}&nbsp;%</p>
				<p class="connCurrData none"><span class="control_positionL bu"></span>${(BpsConnAvgMaxInfo.subtractionMaxConnCurrUnit?replace("-1", "-"))!""}</p>
			</div>			
		</td>							
	</tr>				
	<tr>
		<td class="textOver"><span class="square chart_1st"></span><span id="vsNameChange">/Common/route_int</span></td>		
		<td class="connCurrData none">${(BpsConnAvgMaxInfo.currConnCurrUnit?replace("-1", "-"))!""}</td>
		<td class="connCurrData none">${(BpsConnAvgMaxInfo.avgConnCurrUnit?replace("-1", "-"))!""}</td>
		<td class="connCurrData none">${(BpsConnAvgMaxInfo.maxConnCurrUnit?replace("-1", "-"))!""}</td>
	</tr>
	<tr class="compareChk none">
		<td><span class="square compare_1st"></span><span id="compareChange">${LANGCODEMAP["MSG_PERFAVGMAX_PRE"]!}</span></td>			
		<td class="connCurrData none">${(BpsConnAvgMaxInfo.preCurrConnCurrUnit?replace("-1", "-"))!""}</td>
		<td class="connCurrData none">${(BpsConnAvgMaxInfo.preAvgConnCurrUnit?replace("-1", "-"))!""}</td>
		<td class="connCurrData none">${(BpsConnAvgMaxInfo.preMaxConnCurrUnit?replace("-1", "-"))!""}</td>									
	</tr>		