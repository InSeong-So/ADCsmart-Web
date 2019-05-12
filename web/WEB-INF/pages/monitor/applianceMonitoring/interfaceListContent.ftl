<#if orderType??>
	<#if 14 == orderType></#if>
	<!-- interfaceName -->
	<#if 15 == orderType></#if>
	<!-- status -->
	<#if 4 == orderType></#if>
	<!-- bps -->
	<#if 5 == orderType></#if>
	<!-- pps -->
	</#if> <#if orderDir??> <#if 1 == orderDir></#if>
	<!-- Asc -->
	<#if 2 == orderDir></#if>
	<!-- Desc -->
</#if>
<#if langCode??>
	<#if "ko_KR" == langCode>
		<#assign img_lang = "">
	</#if>
	<#if "en_US" == langCode>
		<#assign img_lang = "_eng">
	</#if>
</#if>
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
		<div class="title">${LANGCODEMAP["MSG_STATISTICS_INTERFACE"]!}</div>
	</div>
	<table class="Board" cellpadding="0" cellspacing="0">
		<caption></caption>
		<colgroup>
			<col width="19%" />
			<col width="5%" />
			<col width="6%" />
			<col width="6%" />
			<col width="7%" />
			<col width="6%" />
			<col width="6%" />
			<col width="7%" />
			<col width="6%" />
			<col width="6%" />
			<col width="7%" />
			<col width="6%" />
			<col width="6%" />
			<col width="7%" />
		</colgroup>
		<thead>
			<tr class="StartLine">
				<td colspan="15"></td>
			</tr>
			<tr class="ContentsHeadLine">
				<input class="none" value="2" name="orderDir_Desc" />
				<input class="none" value="1" name="orderDir_Asc" />
				<th rowspan="3" class="bg_row2 Rcolor">
					<span class="css_textCursor">
						<#if orderDir == 2 && orderType == 14>
							<a class="orderDir_Desc">${LANGCODEMAP["MSG_STATISTICS_INTERFACE"]!}
								<img src="imgs/Desc.gif" />
								<span class="none orderType">14</span>
								<span class="none orderDir">1</span>
							</a>
						<#elseif orderDir == 1 && orderType == 14>
							<a class="orderDir_Asc">${LANGCODEMAP["MSG_STATISTICS_INTERFACE"]!}
								<img src="imgs/Asc.gif"/>
								<span class="none orderType">14</span>
								<span class="none orderDir">2</span>
							</a>
						<#else>
							<a class="orderDir_None">${LANGCODEMAP["MSG_STATISTICS_INTERFACE"]!}
								<img src="imgs/none.gif"/>
								<span class="none orderType">14</span>
								<span class="none orderDir">2</span>
							</a>
						</#if>
					</span>
				</th>
				<th rowspan="3" class="bg_row2 Rcolor">
					<span class="css_textCursor">
						<#if orderDir == 2 && orderType == 15>
							<a class="orderDir_Desc">${LANGCODEMAP["MSG_STATISTICS_STATUS"]!}
								<img src="imgs/Desc.gif" />
								<span class="none orderType">15</span>
								<span class="none orderDir">1</span>
							</a>
						<#elseif orderDir == 1 && orderType == 15>
							<a class="orderDir_Asc">${LANGCODEMAP["MSG_STATISTICS_STATUS"]!}
								<img src="imgs/Asc.gif"/>
								<span class="none orderType">15</span>
								<span class="none orderDir">2</span>
							</a>
						<#else>
							<a class="orderDir_None">${LANGCODEMAP["MSG_STATISTICS_STATUS"]!}
								<img src="imgs/none.gif"/>
								<span class="none orderType">15</span>
								<span class="none orderDir">2</span>
							</a>
						</#if>
					</span>
				</th>
				<th colspan="3" class="bg_row2 Rcolor">${LANGCODEMAP["MSG_STATISTICS_TRAFFIC_BPS"]!}</th>
				<th colspan="3" class="Rcolor">${LANGCODEMAP["MSG_STATISTICS_TRAFFIC_PPS"]!}</th>
				<th colspan="3" class="Rcolor">${LANGCODEMAP["MSG_STATISTICS_ERROR_COUNT"]!}</th>
				<th colspan="3">${LANGCODEMAP["MSG_STATISTICS_LOSS_COUNT"]!}</th>
			</tr>
			<tr class="DivideLine1">
				<th colspan="12"></th>
			</tr>
			<tr class="ContentsHeadLine1">
				<th>In</th>
				<th>Out</th>
				<th class="Rcolor">
					<span class="css_textCursor">
						<#if orderDir == 2 && orderType == 4>
							<a class="orderDir_Desc">${LANGCODEMAP["MSG_STATISTICS_TOTAL"]!}
								<img src="imgs/Desc.gif"/>
								<span class="none orderType">4</span>
								<span class="none orderDir">1</span>
							</a>
						<#elseif orderDir == 1 && orderType == 4>
							<a class="orderDir_Asc">${LANGCODEMAP["MSG_STATISTICS_TOTAL"]!}
								<img src="imgs/Asc.gif"/>
								<span class="none orderType">4</span>
								<span class="none orderDir">2</span>
							</a>
						<#else>
							<a class="orderDir_None">${LANGCODEMAP["MSG_STATISTICS_TOTAL"]!}
								<img src="imgs/none.gif" />
								<span class="none orderType">4</span>
								<span class="none orderDir">2</span>
							</a>
						</#if>
					</span>
				</th>
				<th>In</th>
				<th>Out</th>
				<th class="Rcolor">
					<span class="css_textCursor">
						<#if orderDir == 2 && orderType == 5>
							<a class="orderDir_Desc">${LANGCODEMAP["MSG_STATISTICS_TOTAL"]!}
								<img src="imgs/Desc.gif" />
								<span class="none orderType">5</span>
								<span class="none orderDir">1</span>
							</a>
						<#elseif orderDir == 1 && orderType == 5>
							<a class="orderDir_Asc">${LANGCODEMAP["MSG_STATISTICS_TOTAL"]!}
								<img src="imgs/Asc.gif" />
								<span class="none orderType">5</span>
								<span class="none orderDir">2</span>
							</a>
						<#else>
							<a class="orderDir_None">${LANGCODEMAP["MSG_STATISTICS_TOTAL"]!}
								<img src="imgs/none.gif" />
								<span class="none orderType">5</span>
								<span class="none orderDir">2</span>
							</a>
						</#if>
					</span>
				</th>
				<th>In</th>
				<th>Out</th>
				<th class="Rcolor">${LANGCODEMAP["MSG_STATISTICS_TOTAL"]!}</th>
				<th>In</th>
				<th>Out</th>
				<th>${LANGCODEMAP["MSG_STATISTICS_TOTAL"]!}</th>
			</tr>
		</thead>
		<tr class="DivideLine1">
			<td colspan="15"></td>
		</tr>
		<tbody class="statistic">
			<#list adcPortStatusList![] as adcPortStatus>
				<tr class="ContentsLine1 adcPortStatusList" data-intfname="${adcPortStatus.intfName!}">
					<td class="align_center Rcolor" id="dispName">
						<a href="javascript:;" class="bbyteTot">${adcPortStatus.dispName!}</a>
					</td>
					<td class="align_center Rcolor">
						<span class="align_center">
							<#if adcPortStatus.linkStatus == 1>
								<img src="imgs/icon/icon_greenDot.png" alt="${LANGCODEMAP[" MSG_PERFALTEON_NORMAL"]!}"/>
							<#elseif adcPortStatus.linkStatus == 2>
								<img src="imgs/icon/icon_redDot.png" alt="${LANGCODEMAP[" MSG_PERFALTEON_ABNORMAL"]!}"/>
							<#elseif adcPortStatus.linkStatus == 3>
								<img src="imgs/icon/icon_BlackDot.png" alt="disabled" />
							<#elseif adcPortStatus.linkStatus == 5>
								<img src="imgs/icon/icon_gryDot.png" alt="etc" />
							<#else>
								<img src="imgs/icon/icon_yellowDot.png" alt="${LANGCODEMAP[" MSG_PERFALTEON_DISCONN"]!}"/>
							</#if>
						</span>
					</td>
					<td class="align_center">${adcPortStatus.bbytesIn}</td>
					<td class="align_center">${adcPortStatus.bbytesOut}</td>
					<td class="align_center Rcolor">
						<a href="javascript:;" class="bbyteTot">${adcPortStatus.bbytesTot}</a>
					</td>
					<td class="align_center">${adcPortStatus.ppktsIn}</td>
					<td class="align_center">${adcPortStatus.ppktsOut}</td>
					<td class="align_center Rcolor">
						<a href="javascript:;" class="ppktsTot">${adcPortStatus.ppktsTot}</a>
					</td>
					<td class="align_center">${adcPortStatus.eerrorsIn}</td>
					<td class="align_center">${adcPortStatus.eerrorsOut}</td>
					<td class="align_center Rcolor">
						<a href="javascript:;" class="eerrorsTot">${adcPortStatus.eerrorsTot}</a>
					</td>
					<td class="align_center">${adcPortStatus.ddropsIn}</td>
					<td class="align_center">${adcPortStatus.ddropsOut}</td>
					<td class="align_center">
						<a href="javascript:;" class="ddropsTot">${adcPortStatus.ddropsTot}</a>
					</td>
				</tr>
				<tr class="DivideLine">
					<td colspan="15"></td>
				</tr>
			</#list>
		</tbody>
		<tr class="EndLine">
			<td colspan="15"></td>
		</tr>
	</table>
</div>
<br class="clearfix" />