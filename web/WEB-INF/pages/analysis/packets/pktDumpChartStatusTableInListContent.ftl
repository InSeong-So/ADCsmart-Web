<div class="pktDumpStatusCPUData">
<table class="table_type12">
	<colgroup>
		<col width="50%" />
		<col width="50%" />
	</colgroup>
	<thead>
		<tr>
			<th>CPU</th>
			<th>Memory</th>
		</tr>
	</thead>
	<tbody>
		<!-- 그룹으로 패킷수집경우 chart 모두 표현 시작 -->
		<tr class="groupSelect none">
			<td>						
				<div class="chartonly">
					<div class="cpuChart">${LANGCODEMAP["MSG_DIAG_ANAL_HISTORY_CHART"]!}</div>								
				</div>
			</td>
			<td>
				<div class="chartonly">
					<div class="memoryChart">${LANGCODEMAP["MSG_DIAG_ANAL_HISTORY_CHART"]!}</div>
				</div>
			</td>
		</tr>
		<!-- 그룹으로 패킷수집경우 chart 모두 표현 끝 -->
		
		<!-- 개별 ADC 경우 gage, chart 모두 표현 시작 -->
		<tr class="adcSelect">
			<td>
				<div class="gage">
					<div class="cpuGauge"></div>
				</div>
				<div class="chart">
					<div class="cpuChart">${LANGCODEMAP["MSG_DIAG_ANAL_HISTORY_CHART"]!}</div>								
				</div>
			</td>
			<td>
				<div class="gage">
					<div class="memoryGauge"></div>
				</div>
				<div class="chart">
					<div class="memoryChart">${LANGCODEMAP["MSG_DIAG_ANAL_HISTORY_CHART"]!}</div>
				</div>
			</td>
		</tr>
		<!-- 개별 ADC 경우 gage, chart 모두 표현 끝 -->
	</tbody>
</table>	
</div>			
