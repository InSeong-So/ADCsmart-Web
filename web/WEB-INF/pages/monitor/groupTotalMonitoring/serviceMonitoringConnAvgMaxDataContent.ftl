<tbody>
<colgroup>
	<col width="auto">
	<col width="20%"/>
	<col width="20%"/>
	<col width="20%"/>	
</colgroup>
	<tr>
		<th></th>
		<th>Current</th>
		<th>Avg</th>
		<th>Max</th>					
	</tr>
	<#list bpsConnCurAvgMaxData.connInfo as theConnCurAvgMax>			
	<tr>
		<td class="textOver">${theConnCurAvgMax.name!""}</td>		
		<td class="connCurrData">${(theConnCurAvgMax.currentUnit?replace("-1", "-"))!""}</td>
		<td class="connCurrData">${(theConnCurAvgMax.avgUnit?replace("-1", "-"))!""}</td>
		<td class="connCurrData">${(theConnCurAvgMax.maxUnit?replace("-1", "-"))!""}</td>
	</tr>	
	</#list>				
</tbody>