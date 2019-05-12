
	<tr class="compareChk none">
		<td>${LANGCODEMAP["MSG_PERFAVGMAX_GROWTH"]!}</td>			
		<td>
			<#if (BpsConnAvgMaxInfo.subtractionCurrBpsIn >0) >
			<div class="up">
			<#elseif (BpsConnAvgMaxInfo.subtractionCurrBpsIn < 0 ) >
			<div class="down">
			<#else>
			<div class="sam">
			</#if>
				<p class="bpsInData">${(BpsConnAvgMaxInfo.subtractionCurrBpsInPercent?replace("-1", "-"))!""}&nbsp;%</p>
				<p class="bpsInData"><span class="control_positionL bu"></span>${(BpsConnAvgMaxInfo.subtractionCurrBpsInUnit?replace("-1", "-"))!""}</p>
				<p class="bpsOutData none">${(BpsConnAvgMaxInfo.subtractionCurrBpsOutPercent?replace("-1", "-"))!""}&nbsp;%</p>
				<p class="bpsOutData none"><span class="control_positionL bu"></span>${(BpsConnAvgMaxInfo.subtractionCurrBpsOutUnit?replace("-1", "-"))!""}</p>
				<p class="bpsTotData none">${(BpsConnAvgMaxInfo.subtractionCurrBpsTotPercent?replace("-1", "-"))!""}&nbsp;%</p>
				<p class="bpsTotData none"><span class="control_positionL bu"></span>${(BpsConnAvgMaxInfo.subtractionCurrBpsTotUnit?replace("-1", "-"))!""}</p>
			</div>			
		</td>	
		<td>
			<#if (BpsConnAvgMaxInfo.subtractionAvgBpsOut >0) >
			<div class="up">
			<#elseif (BpsConnAvgMaxInfo.subtractionAvgBpsOut < 0 ) >
			<div class="down">
			<#else>
			<div class="sam">
			</#if>
				<p class="bpsInData none">${(BpsConnAvgMaxInfo.subtractionAvgBpsInPercent?replace("-1", "-"))!""}&nbsp;%</p>
				<p class="bpsInData none"><span class="control_positionL bu"></span>${(BpsConnAvgMaxInfo.subtractionAvgBpsInUnit?replace("-1", "-"))!""}</p>
				<p class="bpsOutData">${(BpsConnAvgMaxInfo.subtractionAvgBpsOutPercent?replace("-1", "-"))!""}&nbsp;%</p>
				<p class="bpsOutData"><span class="control_positionL bu"></span>${(BpsConnAvgMaxInfo.subtractionAvgBpsOutUnit?replace("-1", "-"))!""}</p>
				<p class="bpsTotData none">${(BpsConnAvgMaxInfo.subtractionAvgBpsTotPercent?replace("-1", "-"))!""}&nbsp;%</p>
				<p class="bpsTotData none"><span class="control_positionL bu"></span>${(BpsConnAvgMaxInfo.subtractionAvgBpsTotUnit?replace("-1", "-"))!""}</p>
			</div>			
		</td>	
		<td>
			<#if (BpsConnAvgMaxInfo.subtractionMaxBpsTot >0) >
			<div class="up">
			<#elseif (BpsConnAvgMaxInfo.subtractionMaxBpsTot < 0 ) >
			<div class="down">
			<#else>
			<div class="sam">
			</#if>
				<p class="bpsInData none">${(BpsConnAvgMaxInfo.subtractionMaxBpsInPercent?replace("-1", "-"))!""}&nbsp;%</p>
				<p class="bpsInData none"><span class="control_positionL bu"></span>${(BpsConnAvgMaxInfo.subtractionMaxBpsInUnit?replace("-1", "-"))!""}</p>
				<p class="bpsOutData none">${(BpsConnAvgMaxInfo.subtractionMaxBpsOutPercent?replace("-1", "-"))!""}&nbsp;%</p>
				<p class="bpsOutData none"><span class="control_positionL bu"></span>${(BpsConnAvgMaxInfo.subtractionMaxBpsOutUnit?replace("-1", "-"))!""}</p>
				<p class="bpsTotData">${(BpsConnAvgMaxInfo.subtractionMaxBpsTotPercent?replace("-1", "-"))!""}&nbsp;%</p>
				<p class="bpsTotData"><span class="control_positionL bu"></span>${(BpsConnAvgMaxInfo.subtractionMaxBpsTotUnit?replace("-1", "-"))!""}</p>
			</div>			
		</td>							
	</tr>				
	<tr>
		<td class="textOver"><span class="square chart_1st"></span><span id="vsNameChange">/Common/route_int</span></td>
		
		<td class="bpsInData none">${(BpsConnAvgMaxInfo.currBpsInUnit?replace("-1", "-"))!""}</td>
		<td class="bpsInData none">${(BpsConnAvgMaxInfo.avgBpsInUnit?replace("-1", "-"))!""}</td>
		<td class="bpsInData none">${(BpsConnAvgMaxInfo.maxBpsInUnit?replace("-1", "-"))!""}</td>		
		
		<td class="bpsOutData none">${(BpsConnAvgMaxInfo.currBpsOutUnit?replace("-1", "-"))!""}</td>
		<td class="bpsOutData none">${(BpsConnAvgMaxInfo.avgBpsOutUnit?replace("-1", "-"))!""}</td>
		<td class="bpsOutData none">${(BpsConnAvgMaxInfo.maxBpsOutUnit?replace("-1", "-"))!""}</td>
		
		<td class="bpsTotData">${(BpsConnAvgMaxInfo.currBpsTotUnit?replace("-1", "-"))!""}</td>
		<td class="bpsTotData">${(BpsConnAvgMaxInfo.avgBpsTotUnit?replace("-1", "-"))!""}</td>
		<td class="bpsTotData">${(BpsConnAvgMaxInfo.maxBpsTotUnit?replace("-1", "-"))!""}</td>
	</tr>	
	<tr class="compareChk none">
		<td><span class="square compare_1st"></span><span id="compareChange">${LANGCODEMAP["MSG_PERFAVGMAX_PRE"]!}</span></td>
		
		<td class="bpsInData none">${(BpsConnAvgMaxInfo.preCurrBpsInUnit?replace("-1", "-"))!""}</td>
		<td class="bpsInData none">${(BpsConnAvgMaxInfo.preAvgBpsInUnit?replace("-1", "-"))!""}</td>
		<td class="bpsInData none">${(BpsConnAvgMaxInfo.preMaxBpsInUnit?replace("-1", "-"))!""}</td>	
		
		<td class="bpsOutData none">${(BpsConnAvgMaxInfo.preCurrBpsOutUnit?replace("-1", "-"))!""}</td>
		<td class="bpsOutData none">${(BpsConnAvgMaxInfo.preAvgBpsOutUnit?replace("-1", "-"))!""}</td>
		<td class="bpsOutData none">${(BpsConnAvgMaxInfo.preMaxBpsOutUnit?replace("-1", "-"))!""}</td>	
		
		<td class="bpsTotData">${(BpsConnAvgMaxInfo.preCurrBpsTotUnit?replace("-1", "-"))!""}</td>
		<td class="bpsTotData">${(BpsConnAvgMaxInfo.preAvgBpsTotUnit?replace("-1", "-"))!""}</td>
		<td class="bpsTotData">${(BpsConnAvgMaxInfo.preMaxBpsTotUnit?replace("-1", "-"))!""}</td>	
	</tr>						
