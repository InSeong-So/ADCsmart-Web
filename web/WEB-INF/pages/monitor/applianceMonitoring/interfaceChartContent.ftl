<div class="Board">
	<div class="position_R10B5">
		<!-- <input type="radio" name="searchTimeMode" id="hoursMode" checked="checked" value="${LANGCODEMAP["MSG_CONSTANT_HOURS_MODE"]!}" />
		<select class="formtext_wdth115" id="lastHours">
			<option value="1">${LANGCODEMAP["MSG_APPLIANCE_ONE_TIME"]!}</option>
			<option value="3">${LANGCODEMAP["MSG_APPLIANCE_THREE_TIME"]!}</option>
			<option value="6">${LANGCODEMAP["MSG_APPLIANCE_SIX_TIME"]!}</option>
			<option value="12">${LANGCODEMAP["MSG_APPLIANCE_TWELVE_TIME"]!}</option>
			<option value="24">${LANGCODEMAP["MSG_APPLIANCE_TWNTYFOUR_TIME"]!}</option>
		</select> &nbsp;&nbsp;&nbsp; <input type="radio" name="searchTimeMode" id="periodMode" value="${LANGCODEMAP["MSG_CONSTANT_PERIOD_MODE"]!}"/>
		<label for="periodMode" class="spCpuSub">
		<input name="startTime" class="inputText_calendar" type="text" title="${LANGCODEMAP["MSG_APPLIANCE_SELECTE_SEARCH_DATE"]!}" readonly/>
		&nbsp;~&nbsp;
		<input name="endTime" class="inputText_calendar" type="text" title="${LANGCODEMAP["MSG_APPLIANCE_SELECTE_SEARCH_DATE"]!}" readonly/>
		</label>&nbsp; -->
		<input type="text" class="inputText_calendar"  name="reservation" id="reservationtime"  value="" readonly="">
		<input type="button" id="refresh" class="Btn_white" value="${LANGCODEMAP["MSG_STATISTICS_REFRESH"]!}"/>
	</div>
</div>
<br class="clearfix" />
<div class="device">
	<div class="info">
		<div class="title">
			${LANGCODEMAP["MSG_STATISTICS_INTERFACE"]!}
			<select id="interfaceNamePicker">
			</select>
			<select id="interfaceTypePicker">
				<option value="Bytes">${LANGCODEMAP["MSG_STATISTICS_TRAFFIC_BPS"]!}</option>
				<option value="Packets">${LANGCODEMAP["MSG_STATISTICS_TRAFFIC_PPS"]!}</option>
				<option value="Errors">${LANGCODEMAP["MSG_STATISTICS_ERROR"]!}</option>
				<option value="Drops">${LANGCODEMAP["MSG_STATISTICS_LOSS"]!}</option>
			</select>
			<span class="gray_text_box">
				In ${LANGCODEMAP["MSG_STATISTICS_MAX"]!}<span id="chartdata_max_in"></span>
				${LANGCODEMAP["MSG_STATISTICS_AVG"]!}<span id="chartdata_avg_in"></span>
			</span>
			<span class="gray_text_box">
				Out ${LANGCODEMAP["MSG_STATISTICS_MAX"]!}<span id="chartdata_max_out"></span>
				${LANGCODEMAP["MSG_STATISTICS_AVG"]!}<span id="chartdata_avg_out"></span>
			</span>
			<span class="gray_text_box">
				Total ${LANGCODEMAP["MSG_STATISTICS_MAX"]!}<span id="chartdata_max_total"></span>
				${LANGCODEMAP["MSG_STATISTICS_AVG"]!}<span id="chartdata_avg_total"></span>	
			</span>
		</div>
		<div class="position_R10B5">
			<input type="button" id="exportCssLnk" class="export_width Btn_white" value="${LANGCODEMAP["MSG_STATISTICS_OUT_FILE"]!}"/>
		</div>
	</div>
	<div class="chart">
		<div id="detailChart" style="width: 100%; height: 300px;"></div>
	</div>
	<input type="button" id="interfaceListBtn" class="Btn_white" value="${LANGCODEMAP["MSG_APPLIANCE_HISTORYBACK"]!}"/>
</div>