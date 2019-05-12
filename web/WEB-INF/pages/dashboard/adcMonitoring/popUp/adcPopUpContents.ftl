<table id="table1" class="form_type1 mar_top10">
	<caption>Virtual Server 모니터링</caption>
	<colgroup>
		<col width="23%" />
		<col width="77%" />
	</colgroup>
	<tbody>
		<tr>
			<th class="bor_top">조회 기간</th>
			<td class="bor_top">
			<span class="PopupNotice"></span>
				<div style="display: none;">
					<span class="search_period">
							<input id = "startTime" name="startTime" class="formtext_wdth115 mar_rgt5"  type="text" title="조회 기간선택"/>
							&nbsp;~&nbsp;
							<input id = "endTime" name="endTime" class="formtext_wdth115 mar_rgt5" type="text" title="조회 기간선택"/>
					</span>
				<a class="f_right mar_rgt7" id="refresh" href="#"><img src="/imgs/common/btn_refresh.gif" alt="새로고침" /></a>
			</div>
			</td>
		</tr>
	</tbody>
</table>
<div class = "gragh_sessnInfo">
	<div id="adcConnection">
		<div id="GeneratePopupGroupConnectionChartData" style="width: 100%; height: 280px;"></div>	
	</div>	
	<div id="adcThroughput">
		<div id="GeneratePopupGroupThroughputChartData" style="width: 100%; height: 280px;"></div>	
	</div>
</div>