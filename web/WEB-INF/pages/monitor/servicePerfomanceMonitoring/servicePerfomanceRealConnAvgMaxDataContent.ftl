	<tr class="compareChk none">
		<td>${LANGCODEMAP["MSG_PERFAVGMAX_GROWTH"]!}</td>
		<td>
			<#if (MemberAvgMaxInfo.subtractionCurrConnCurr >0) >
			<div class="up">
			<#elseif (MemberAvgMaxInfo.subtractionCurrConnCurr < 0 ) >
			<div class="down">
			<#else>
			<div class="sam">
			</#if>
				<p class="connCurrData none">${(MemberAvgMaxInfo.subtractionCurrConnCurrPercent?replace("-1", "-"))!""}&nbsp;%</p>
				<p class="connCurrData none"><span class="control_positionL bu"></span>${(MemberAvgMaxInfo.subtractionCurrConnCurr?replace("-1", "-"))!""}</p>
			</div>			
		</td>	
		<td>
			<#if (MemberAvgMaxInfo.subtractionAvgConnCurr >0) >
			<div class="up">
			<#elseif (MemberAvgMaxInfo.subtractionAvgConnCurr < 0 ) >
			<div class="down">
			<#else>
			<div class="sam">
			</#if>
				<p class="connCurrData none">${(MemberAvgMaxInfo.subtractionAvgConnCurrPercent?replace("-1", "-"))!""}&nbsp;%</p>
				<p class="connCurrData none"><span class="control_positionL bu"></span>${(MemberAvgMaxInfo.subtractionAvgConnCurr?replace("-1", "-"))!""}</p>
			</div>			
		</td>	
		<td>
			<#if (MemberAvgMaxInfo.subtractionMaxConnCurr >0) >
			<div class="up">
			<#elseif (MemberAvgMaxInfo.subtractionMaxConnCurr < 0 ) >
			<div class="down">
			<#else>
			<div class="sam">
			</#if>
				<p class="connCurrData none">${(MemberAvgMaxInfo.subtractionMaxConnCurrPercent?replace("-1", "-"))!""}&nbsp;%</p>
				<p class="connCurrData none"><span class="control_positionL bu"></span>${(MemberAvgMaxInfo.subtractionMaxConnCurr?replace("-1", "-"))!""}</p>
			</div>			
		</td>					
	</tr>				
	<tr>
		<td class="textOver"><span class="square chart_1st"></span><span id="vsNameChange">/Common/route_int</span></td>		
		<td class="connCurrData none">${(MemberAvgMaxInfo.currConnCurrUnit?replace("-1", "-"))!""}</td>
		<td class="connCurrData none">${(MemberAvgMaxInfo.avgConnCurrUnit?replace("-1", "-"))!""}</td>
		<td class="connCurrData none">${(MemberAvgMaxInfo.maxConnCurrUnit?replace("-1", "-"))!""}</td>
	</tr>	
	<tr class="compareChk none">
		<td><span class="square compare_1st"></span><span id="compareChange">${LANGCODEMAP["MSG_PERFAVGMAX_PRE"]!}</span></td>			
		<td class="connCurrData none">${(MemberAvgMaxInfo.preCurrConnCurrUnit?replace("-1", "-"))!""}</td>
		<td class="connCurrData none">${(MemberAvgMaxInfo.preAvgConnCurrUnit?replace("-1", "-"))!""}</td>
		<td class="connCurrData none">${(MemberAvgMaxInfo.preMaxConnCurrUnit?replace("-1", "-"))!""}</td>									
	</tr>	