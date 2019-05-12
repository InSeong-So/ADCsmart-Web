<#if orderType??>
	<#if 14 == orderType></#if><!-- interfaceName -->
	<#if 15 == orderType></#if><!-- status -->
	<#if  4 == orderType></#if><!-- bps -->
	<#if  5 == orderType></#if><!-- pps -->
</#if>
<#if orderDir??>
	<#if 1 == orderDir></#if><!-- Asc -->
	<#if 2 == orderDir></#if><!-- Desc -->
</#if>
<#if langCode??>
  <#if "ko_KR" == langCode><#assign img_lang = ""></#if>
  <#if "en_US" == langCode><#assign img_lang = "_eng"></#if>
</#if>
<div class="contents_area">
	<div>
		<img src="imgs/title${img_lang!""}/h3_statistics.gif" class="title_h3" />				
	</div>
	<div class="nulldata none"></div>
	<div class="successdata">
	<table class="Board" cellpadding="0" cellspacing="0">
		<caption>&nbsp;</caption>
		<colgroup>
			<col width="200px" />
			<col width="auto" />
			<col width="80px" />
		</colgroup>	
		<tr class="StartLine">
			<td colspan="3" ></td>
		</tr>
		<tr>
			<th class="Lth2">
				<li>${LANGCODEMAP["MSG_STATISTICS_QUERY_PERIOD"]!}</li>
			</th>
			<td>
				<span class="Lth0">
					<input type="radio" name="searchTimeMode" id="hoursMode" checked="checked" value="${LANGCODEMAP["MSG_CONSTANT_HOURS_MODE"]!}"/>
					<select class="mar_lft5" id="lastHours" >
						<option value="1">${LANGCODEMAP["MSG_STATISTICS_ONE_TIME"]!}</option>
						<option value="3">${LANGCODEMAP["MSG_STATISTICS_THREE_TIME"]!}</option>
						<option value="6">${LANGCODEMAP["MSG_STATISTICS_SIX_TIME"]!}</option>
						<option value="12">${LANGCODEMAP["MSG_STATISTICS_TWELVE_TIME"]!}</option>
						<option value="24">${LANGCODEMAP["MSG_STATISTICS_TWNTYFOUR_TIME"]!}</option>
					</select>&nbsp;&nbsp;&nbsp;
					<input type="radio" name="searchTimeMode" id="periodMode" value="${LANGCODEMAP["MSG_CONSTANT_PERIOD_MODE"]!}"/>
					<label for="periodMode" >${LANGCODEMAP["MSG_STATISTICS_PERIOD"]!}
						<input name="startTime" class="inputText_calendar" type="text" title="${LANGCODEMAP["MSG_STATISTICS_QUERY_PERIOD_SEL"]!}" readonly/>
							&nbsp;~&nbsp;
						<input name="endTime" class="inputText_calendar" type="text" title="${LANGCODEMAP["MSG_STATISTICS_QUERY_PERIOD_SEL"]!}" readonly/>
					</label>
				</span>				
			</td>			
			<td rowspan="3">							
				<div class="position_R10B10" >
				    <input type="button" id="exportCssLnk" class="export_width Btn_white" value="${LANGCODEMAP["MSG_STATISTICS_OUT_FILE"]!}"/>
				</div>
	            <br class="clearfix" />						
				<div class="position_R10B10" >
			  		<input type="button" id="refresh"  class="export_width Btn_white" value="${LANGCODEMAP["MSG_STATISTICS_REFRESH"]!}"/> 
				</div>						
			</td>			
					</tr>
		<tr class="DivideLine1">
			<td colspan="2"></td>
		</tr>
		<tr>
			<th class="Lth2">
				<li>${LANGCODEMAP["MSG_STATISTICS_SEARCH_CONDITION"]!}</li>
			</th>
			<td>
				<span class="Lth0">
					<input type="radio" name="searchTrafficMode" id="sltBox_bps" checked="checked" value="Bytes"/>
						<label for="sltBox_bps">${LANGCODEMAP["MSG_STATISTICS_TRAFFIC_BPS"]!}</label>
					<input type="radio" name="searchTrafficMode" id="sltBox_pps" class="mar_lft5" value="Packets"/>
						<label for="sltBox_pps">${LANGCODEMAP["MSG_STATISTICS_TRAFFIC_PPS"]!}</label>
					<input type="radio" name="searchTrafficMode" id="sltBox_error" class="mar_lft5" value="Errors"/>
						<label for="sltBox_error">${LANGCODEMAP["MSG_STATISTICS_ERROR"]!}</label>
					<input type="radio" name="searchTrafficMode" id="sltBox_drop" class="mar_lft5" value="Drops"/>
						<label for="sltBox_drop" >${LANGCODEMAP["MSG_STATISTICS_LOSS"]!}</label>				
				</span>
			</td>
		</tr>		
		<tr class="EndLine2">
			<td colspan="3"></td>
		</tr>
	</table>
	<!--2-->
	<div class="title_h4_1">
		<li>
			<span id="TrafficModeCange">${LANGCODEMAP["MSG_STATISTICS_TRAFFIC_BPS"]!}&nbsp;</span>
			<span class="gray_text_box">
				${LANGCODEMAP["MSG_STATISTICS_MAX"]!}<span id="notice_chartdata_max"></span>
				${LANGCODEMAP["MSG_STATISTICS_AVG"]!}<span id="notice_chartdata_avg"></span>			
			</span>			
		</li>		
	</div>
	<table class="Board" cellpadding="0" cellspacing="0">
		<tr class="StartLine">
			<td colspan="3"></td>
		</tr>
	</table>
				<!--<div id="chartContainer" style="width:100%; height:400px;"></div>-->
				<div class="Board">
						<div id="StatisticsChart" style="width: 100%; height: 300px;"></div>																
				</div>	

	
	<!--3-->
	<div class="title_h4_1">
		<li>${LANGCODEMAP["MSG_STATISTICS_INTERFACE_INFO"]!}</li>
	</div>
	<!--<table class="Board" cellpadding="0" cellspacing="0" id="table_statistics">-->
	<table class="Board" cellpadding="0" cellspacing="0">
		<caption></caption>
		<colgroup>
			<col width="5%" />
			<col width="14%" />
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
				<td colspan="15">
				</td>
			</tr>
			<tr class="ContentsHeadLine">
				<input class ="none" value="2" name="orderDir_Desc"/>
				<input class ="none" value="1" name="orderDir_Asc"/>
				<th rowspan="3" class="bg_row2 Rcolor">
					<input class="allChk" type="checkbox" />
				</th>
				<th rowspan="3" class="bg_row2 Rcolor">
					<span class="css_textCursor">
						<#if orderDir == 2 && orderType == 14>		
							<a class="orderDir_Desc">${LANGCODEMAP["MSG_STATISTICS_INTERFACE"]!}
								<img src="imgs/Desc.gif"/>
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
								<img src="imgs/Desc.gif"/>
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
								<img src="imgs/none.gif"/>
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
								<img src="imgs/Desc.gif"/>
								<span class="none orderType">5</span>
								<span class="none orderDir">1</span>					
							</a>						
						<#elseif orderDir == 1 && orderType == 5>	
							<a class="orderDir_Asc">${LANGCODEMAP["MSG_STATISTICS_TOTAL"]!}
								<img src="imgs/Asc.gif"/>
								<span class="none orderType">5</span>
								<span class="none orderDir">2</span>		
							</a>
						<#else>
							<a class="orderDir_None">${LANGCODEMAP["MSG_STATISTICS_TOTAL"]!}
								<img src="imgs/none.gif"/>
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
		<tbody class= "statistic">
		<#list adcPortStatusList![] as adcPortStatus>				
			<tr class="ContentsLine1 adcPortStatusList">
				<td class="align_center Rcolor">
					<#if adcPortStatus.linkStatus == 1>
						<input class="indChk" type="checkbox" checked="checked" value="${adcPortStatus.intfName!}">${adcPortStatus.interfaceIndex!}
					<#elseif adcPortStatus.linkStatus == 2>
					  	<input class="indChk" type="checkbox" value="${adcPortStatus.intfName!}">${adcPortStatus.interfaceIndex!}
					<#elseif adcPortStatus.linkStatus == 3>
					  	<input class="indChk" type="checkbox" value="${adcPortStatus.intfName!}">${adcPortStatus.interfaceIndex!}
					<#else>
					  	<input class="indChk" type="checkbox" value="${adcPortStatus.intfName!}">${adcPortStatus.interfaceIndex!}
					</#if>
				</td>
				<td class="align_center Rcolor" id="dispName">
					${adcPortStatus.dispName!}
				</td>
				<td class="align_center Rcolor">
					<span class="align_center">
						<#if adcPortStatus.linkStatus == 1>
							<img src="imgs/icon/icon_greenDot.png" alt="${LANGCODEMAP["MSG_PERFALTEON_NORMAL"]!}"/>
						<#elseif adcPortStatus.linkStatus == 2>
							<img src="imgs/icon/icon_redDot.png" alt="${LANGCODEMAP["MSG_PERFALTEON_ABNORMAL"]!}"/>
						<#elseif adcPortStatus.linkStatus == 3>
							<img src="imgs/icon/icon_BlackDot.png" alt="disabled"/>
						<#elseif adcPortStatus.linkStatus == 5>
							<img src="imgs/icon/icon_gryDot.png" alt="etc"/>
						<#else>
							<img src="imgs/icon/icon_yellowDot.png" alt="${LANGCODEMAP["MSG_PERFALTEON_DISCONN"]!}"/>													
						</#if>
					</span>
				</td>
				<td class="align_center">${adcPortStatus.bbytesIn}</td>
				<td class="align_center">${adcPortStatus.bbytesOut}</td>
				<td class="align_center Rcolor">${adcPortStatus.bbytesTot}</td>
				<td class="align_center">${adcPortStatus.ppktsIn}</td>
				<td class="align_center">${adcPortStatus.ppktsOut}</td>
				<td class="align_center Rcolor">${adcPortStatus.ppktsTot}</td>
				<td class="align_center">${adcPortStatus.eerrorsIn}</td>
				<td class="align_center">${adcPortStatus.eerrorsOut}</td>
				<td class="align_center Rcolor">${adcPortStatus.eerrorsTot}</td>
				<td class="align_center">${adcPortStatus.ddropsIn}</td>
				<td class="align_center">${adcPortStatus.ddropsOut}</td>
				<td class="align_center">${adcPortStatus.ddropsTot}</td>
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
</div>
<br class="clearfix" />