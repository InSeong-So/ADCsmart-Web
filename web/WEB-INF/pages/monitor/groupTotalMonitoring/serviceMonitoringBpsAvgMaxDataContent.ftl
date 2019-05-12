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
	<#list bpsConnCurAvgMaxData.bpsInfo as theBpsCurAvgMax>			
	<tr>
		<td class="textOver">${theBpsCurAvgMax.name!""}</td>		
		<td class="connCurrData">${(theBpsCurAvgMax.currentUnit?replace("-1", "-"))!""}</td>
		<td class="connCurrData">${(theBpsCurAvgMax.avgUnit?replace("-1", "-"))!""}</td>
		<td class="connCurrData">${(theBpsCurAvgMax.maxUnit?replace("-1", "-"))!""}</td>
	</tr>	
	</#list>					
</tbody>