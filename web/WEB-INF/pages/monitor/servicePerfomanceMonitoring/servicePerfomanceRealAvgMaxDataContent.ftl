
	<tr class="compareChk none">
		<td>${LANGCODEMAP["MSG_PERFAVGMAX_GROWTH"]!}</td>
		<td>
			<#if (MemberAvgMaxInfo.subtractionCurrBpsIn >0) >
			<div class="up">
			<#elseif (MemberAvgMaxInfo.subtractionCurrBpsIn < 0 ) >
			<div class="down">
			<#else>
			<div class="sam">
			</#if>
				<p class="bpsInData none">${(MemberAvgMaxInfo.subtractionCurrBpsInPercent?replace("-1", "-"))!""}&nbsp;%</p>
				<p class="bpsInData none"><span class="control_positionL bu"></span>${(MemberAvgMaxInfo.subtractionCurrBpsIn?replace("-1", "-"))!""}</p>
				<p class="bpsOutData none">${(MemberAvgMaxInfo.subtractionCurrBpsOutPercent?replace("-1", "-"))!""}&nbsp;%</p>
				<p class="bpsOutData none"><span class="control_positionL bu"></span>${(MemberAvgMaxInfo.subtractionCurrBpsOut?replace("-1", "-"))!""}</p>
				<p class="bpsTotData">${(MemberAvgMaxInfo.subtractionCurrBpsTotPercent?replace("-1", "-"))!""}&nbsp;%</p>
				<p class="bpsTotData"><span class="control_positionL bu"></span>${(MemberAvgMaxInfo.subtractionCurrBpsTot?replace("-1", "-"))!""}</p>
			</div>			
		</td>	
		<td>
			<#if (MemberAvgMaxInfo.subtractionAvgBpsIn >0) >
			<div class="up">
			<#elseif (MemberAvgMaxInfo.subtractionAvgBpsIn < 0 ) >
			<div class="down">
			<#else>
			<div class="sam">
			</#if>
				<p class="bpsInData none">${(MemberAvgMaxInfo.subtractionAvgBpsInPercent?replace("-1", "-"))!""}&nbsp;%</p>
				<p class="bpsInData none"><span class="control_positionL bu"></span>${(MemberAvgMaxInfo.subtractionAvgBpsIn?replace("-1", "-"))!""}</p>
				<p class="bpsOutData none">${(MemberAvgMaxInfo.subtractionAvgBpsOutPercent?replace("-1", "-"))!""}&nbsp;%</p>
				<p class="bpsOutData none"><span class="control_positionL bu"></span>${(MemberAvgMaxInfo.subtractionAvgBpsOut?replace("-1", "-"))!""}</p>
				<p class="bpsTotData">${(MemberAvgMaxInfo.subtractionAvgBpsTotPercent?replace("-1", "-"))!""}&nbsp;%</p>
				<p class="bpsTotData"><span class="control_positionL bu"></span>${(MemberAvgMaxInfo.subtractionAvgBpsTot?replace("-1", "-"))!""}</p>
			</div>			
		</td>	
		<td>
			<#if (MemberAvgMaxInfo.subtractionMaxBpsInPercent >0) >
			<div class="up">
			<#elseif (MemberAvgMaxInfo.subtractionMaxBpsInPercent < 0 ) >
			<div class="down">
			<#else>
			<div class="sam">
			</#if>
				<p class="bpsInData none">${(MemberAvgMaxInfo.subtractionMaxBpsInPercent?replace("-1", "-"))!""}&nbsp;%</p>
				<p class="bpsInData none"><span class="control_positionL bu"></span>${(MemberAvgMaxInfo.subtractionMaxBpsIn?replace("-1", "-"))!""}</p>
				<p class="bpsOutData none">${(MemberAvgMaxInfo.subtractionMaxBpsOutPercent?replace("-1", "-"))!""}&nbsp;%</p>
				<p class="bpsOutData none"><span class="control_positionL bu"></span>${(MemberAvgMaxInfo.subtractionMaxBpsOut?replace("-1", "-"))!""}</p>
				<p class="bpsTotData">${(MemberAvgMaxInfo.subtractionMaxBpsTotPercent?replace("-1", "-"))!""}&nbsp;%</p>
				<p class="bpsTotData"><span class="control_positionL bu"></span>${(MemberAvgMaxInfo.subtractionMaxBpsTot?replace("-1", "-"))!""}</p>
			</div>			
		</td>						
	</tr>				
	<tr>
		<td class="textOver"><span class="square chart_1st"></span><span id="vsNameChange">/Common/route_int</span></td>
		
		<td class="bpsInData none">${(MemberAvgMaxInfo.currBpsInUnit?replace("-1", "-"))!""}</td>
		<td class="bpsInData none">${(MemberAvgMaxInfo.avgBpsInUnit?replace("-1", "-"))!""}</td>
		<td class="bpsInData none">${(MemberAvgMaxInfo.maxBpsInUnit?replace("-1", "-"))!""}</td>		
		
		<td class="bpsOutData none">${(MemberAvgMaxInfo.currBpsOutUnit?replace("-1", "-"))!""}</td>
		<td class="bpsOutData none">${(MemberAvgMaxInfo.avgBpsOutUnit?replace("-1", "-"))!""}</td>
		<td class="bpsOutData none">${(MemberAvgMaxInfo.maxBpsOutUnit?replace("-1", "-"))!""}</td>
		
		<td class="bpsTotData">${(MemberAvgMaxInfo.currBpsTotUnit?replace("-1", "-"))!""}</td>
		<td class="bpsTotData">${(MemberAvgMaxInfo.avgBpsTotUnit?replace("-1", "-"))!""}</td>
		<td class="bpsTotData">${(MemberAvgMaxInfo.maxBpsTotUnit?replace("-1", "-"))!""}</td>
	</tr>	
	<tr class="compareChk none">
		<td><span class="square compare_1st"></span><span id="compareChange">${LANGCODEMAP["MSG_PERFAVGMAX_PRE"]!}</span></td>
		
		<td class="bpsInData none">${(MemberAvgMaxInfo.preCurrBpsInUnit?replace("-1", "-"))!""}</td>
		<td class="bpsInData none">${(MemberAvgMaxInfo.preAvgBpsInUnit?replace("-1", "-"))!""}</td>
		<td class="bpsInData none">${(MemberAvgMaxInfo.preMaxBpsInUnit?replace("-1", "-"))!""}</td>	
		
		<td class="bpsOutData none">${(MemberAvgMaxInfo.preCurrBpsOutUnit?replace("-1", "-"))!""}</td>
		<td class="bpsOutData none">${(MemberAvgMaxInfo.preAvgBpsOutUnit?replace("-1", "-"))!""}</td>
		<td class="bpsOutData none">${(MemberAvgMaxInfo.preMaxBpsOutUnit?replace("-1", "-"))!""}</td>	
		
		<td class="bpsTotData">${(MemberAvgMaxInfo.preMaxBpsTotUnit?replace("-1", "-"))!""}</td>
		<td class="bpsTotData">${(MemberAvgMaxInfo.preAvgBpsTotUnit?replace("-1", "-"))!""}</td>
		<td class="bpsTotData">${(MemberAvgMaxInfo.preMaxBpsTotUnit?replace("-1", "-"))!""}</td>	
	</tr>						
