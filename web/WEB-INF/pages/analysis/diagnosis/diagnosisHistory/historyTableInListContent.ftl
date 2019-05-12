<#setting number_format="0.####">
<#if orderType??>
	<#if 33 == orderType></#if><!-- occurTime -->
	<#if 34 == orderType></#if><!-- STATUS -->
	<#if 35 == orderType></#if><!-- 대상 ADC, ADC_NAME -->
	<#if 36 == orderType></#if><!-- 사용자. ACCNT_NAME -->
	<#if 37 == orderType></#if><!-- 요약 , 진단결과 : 정상,비정상 -->
</#if>
<#if orderDirection??>
	<#if 1 == orderDirection></#if><!--Asc-->
	<#if 2 == orderDirection></#if><!--Desc-->
</#if>	
<#if langCode??>
  <#if "ko_KR" == langCode><#assign reserve = (LANGCODEMAP["MSG_CONSTANT_RESERVE"]!)></#if>
  <#if "en_US" == langCode><#assign img_lang = ""></#if>
</#if>

<colgroup>
	<col width="5%"/>
	<col width="15%"/>
	<col width="11%"/>
	<col width="auto"/>
	<col width="10%">
	<col width="15%"/>
	<col width="29%"/>
</colgroup>
<thead>
	<tr class="StartLine">
		<td colspan="7"></td>
	</tr>
	<tr class="ContentsHeadLine">
		<th>	
			<span>
				<input class="allHistoryChk" type="checkbox"/>
			</span>					
		</th>	
		<th>	
			<span class="css_textCursor">
				<#if orderObj.orderDirection == 2 && orderObj.orderType == 33>		
					<a class="orderDir_Desc">${LANGCODEMAP["MSG_DIAG_HIS_TIME"]!}
						<img src="imgs/Desc.gif"/>
						<span class="none orderType">33</span>
						<span class="none orderDir">1</span>					
					</a>						
				<#elseif orderObj.orderDirection == 1 && orderObj.orderType == 33>	
					<a class="orderDir_Asc">${LANGCODEMAP["MSG_DIAG_HIS_TIME"]!}
						<img src="imgs/Asc.gif"/>
						<span class="none orderType">33</span>
						<span class="none orderDir">2</span>		
					</a>
				<#else>
					<a class="orderDir_None">${LANGCODEMAP["MSG_DIAG_HIS_TIME"]!}
						<img src="imgs/none.gif"/>
						<span class="none orderType">33</span>
						<span class="none orderDir">2</span>		
					</a>
				</#if>
			</span>						
		</th>
		<th>
			<span class="css_textCursor">
				<#if orderObj.orderDirection == 2 && orderObj.orderType == 34>	
					<a class="orderDir_Desc">${LANGCODEMAP["MSG_DIAG_HIS_STATUS"]!}
						<img src="imgs/Desc.gif"/>
						<span class="none orderType">34</span>
						<span class="none orderDir">1</span>					
					</a>						
				<#elseif orderObj.orderDirection == 1 && orderObj.orderType == 34>	
					<a class="orderDir_Asc">${LANGCODEMAP["MSG_DIAG_HIS_STATUS"]!}
						<img src="imgs/Asc.gif"/>
						<span class="none orderType">34</span>
						<span class="none orderDir">2</span>		
					</a>
				<#else>
					<a class="orderDir_None">${LANGCODEMAP["MSG_DIAG_HIS_STATUS"]!}
						<img src="imgs/none.gif"/>
						<span class="none orderType">34</span>
						<span class="none orderDir">2</span>		
					</a>
				</#if>
			</span>			
		</th>
		<th>
			<span class="css_textCursor">
				<#if orderObj.orderDirection == 2 && orderObj.orderType == 35>	
					<a class="orderDir_Desc">${LANGCODEMAP["MSG_DIAG_HIS_ADC_NAME"]!}
						<img src="imgs/Desc.gif"/>
						<span class="none orderType">35</span>
						<span class="none orderDir">1</span>					
					</a>						
				<#elseif orderObj.orderDirection == 1 && orderObj.orderType == 35>	
					<a class="orderDir_Asc">${LANGCODEMAP["MSG_DIAG_HIS_ADC_NAME"]!}
						<img src="imgs/Asc.gif"/>
						<span class="none orderType">35</span>
						<span class="none orderDir">2</span>		
					</a>
				<#else>
					<a class="orderDir_None">${LANGCODEMAP["MSG_DIAG_HIS_ADC_NAME"]!}
						<img src="imgs/none.gif"/>
						<span class="none orderType">35</span>
						<span class="none orderDir">2</span>		
					</a>
				</#if>
			</span>	
		</th>		
		<th>		
			<span class="css_textCursor">
				<#if orderObj.orderDirection == 2 && orderObj.orderType == 36>	
					<a class="orderDir_Desc">${LANGCODEMAP["MSG_DIAG_HIS_USER"]!}
						<img src="imgs/Desc.gif"/>
						<span class="none orderType">36</span>
						<span class="none orderDir">1</span>					
					</a>						
				<#elseif orderObj.orderDirection == 1 && orderObj.orderType == 36>	
					<a class="orderDir_Asc">${LANGCODEMAP["MSG_DIAG_HIS_USER"]!}
						<img src="imgs/Asc.gif"/>
						<span class="none orderType">36</span>
						<span class="none orderDir">2</span>		
					</a>
				<#else>
					<a class="orderDir_None">${LANGCODEMAP["MSG_DIAG_HIS_USER"]!}
						<img src="imgs/none.gif"/>
						<span class="none orderType">36</span>
						<span class="none orderDir">2</span>		
					</a>
				</#if>
			</span>	
		</th>
		<th>${LANGCODEMAP["MSG_DIAG_HIS_DIAGNOSIS_LIST"]!}</th>
		<th>
			<span class="css_textCursor">
				<#if orderObj.orderDirection == 2 && orderObj.orderType == 37>	
					<a class="orderDir_Desc">${LANGCODEMAP["MSG_DIAG_HIS_SUMMART"]!}
						<img src="imgs/Desc.gif"/>
						<span class="none orderType">37</span>
						<span class="none orderDir">1</span>					
					</a>						
				<#elseif orderObj.orderDirection == 1 && orderObj.orderType == 37>	
					<a class="orderDir_Asc">${LANGCODEMAP["MSG_DIAG_HIS_SUMMART"]!}
						<img src="imgs/Asc.gif"/>
						<span class="none orderType">37</span>
						<span class="none orderDir">2</span>		
					</a>
				<#else>
					<a class="orderDir_None">${LANGCODEMAP["MSG_DIAG_HIS_SUMMART"]!}
						<img src="imgs/none.gif"/>
						<span class="none orderType">37</span>
						<span class="none orderDir">2</span>		
					</a>
				</#if>
			</span>		
		</th>
	</tr>
	<tr class="StartLine1">
		<td colspan="7"></td>
	</tr>		
</thead>
<tbody>
	<tr>
		<td colspan="7" >
	
			<#list faultCheckScheduleList![] as theFaultSchedule>
        <div class="commonheight">				
			<li class="common">
			<img src="/imgs/icon/icon_clock.png"/>
				<a class="scheduleLnk" href="#">${reserve!""}			
					<span class="none scheduleIndex">${theFaultSchedule.index!''}</span>				
					<span class="none templateIndex">${theFaultSchedule.templateIndex!''}</span>
					<span class="none scheduleType">${theFaultSchedule.scheduleType!''}</span>
					<span class="none everyHour">${theFaultSchedule.everyHour!''}</span>
					<span class="none everyMinute">${theFaultSchedule.everyMinute!''}</span>
					<span class="none everyDayOfWeek">${theFaultSchedule.everyDayOfWeek!''}</span>
					<span class="none everyMonth">${theFaultSchedule.everyMonth!''}</span>
					<span class="none everyDayOfMonth">${theFaultSchedule.everyDayOfMonth!''}</span>
					<#if "ko_KR" == langCode>
						<#if (theFaultSchedule.scheduleType) == 1>
						${LANGCODEMAP["MSG_CONSTANT_DAILY"]!} ${theFaultSchedule.everyHour!""}${LANGCODEMAP["MSG_CONSTANT_HOUR"]!} ${theFaultSchedule.everyMinute!""}${LANGCODEMAP["MSG_CONSTANT_MINUTE"]!}
						<#elseif (theFaultSchedule.scheduleType) == 2>
							${LANGCODEMAP["MSG_CONSTANT_EWEEK"]!}
							<#if (theFaultSchedule.everyDayOfWeek) == 1>${LANGCODEMAP["MSG_CONSTANT_SUN"]!}
							<#elseif (theFaultSchedule.everyDayOfWeek) == 2>${LANGCODEMAP["MSG_CONSTANT_MON"]!}
							<#elseif (theFaultSchedule.everyDayOfWeek) == 3>${LANGCODEMAP["MSG_CONSTANT_TUE"]!}
							<#elseif (theFaultSchedule.everyDayOfWeek) == 4>${LANGCODEMAP["MSG_CONSTANT_WED"]!}
							<#elseif (theFaultSchedule.everyDayOfWeek) == 5>${LANGCODEMAP["MSG_CONSTANT_THU"]!}
							<#elseif (theFaultSchedule.everyDayOfWeek) == 6>${LANGCODEMAP["MSG_CONSTANT_FRI"]!}
							<#elseif (theFaultSchedule.everyDayOfWeek) == 7>${LANGCODEMAP["MSG_CONSTANT_SAT"]!}
							</#if>
							 ${theFaultSchedule.everyHour!""}${LANGCODEMAP["MSG_CONSTANT_HOUR"]!} ${theFaultSchedule.everyMinute!""}${LANGCODEMAP["MSG_CONSTANT_MINUTE"]!}
						<#elseif (theFaultSchedule.scheduleType) == 3>
							${LANGCODEMAP["MSG_CONSTANT_MONTHLY"]!} ${theFaultSchedule.everyDayOfMonth!""} ${LANGCODEMAP["MSG_CONSTANT_DAY"]!} ${theFaultSchedule.everyHour!""}${LANGCODEMAP["MSG_CONSTANT_HOUR"]!} ${theFaultSchedule.everyMinute!""}${LANGCODEMAP["MSG_CONSTANT_MINUTE"]!}
						<#elseif (theFaultSchedule.scheduleType) == 4>
							${theFaultSchedule.everyMonth!""}${LANGCODEMAP["MSG_CONSTANT_MONTH"]!} ${theFaultSchedule.everyDayOfMonth!""}${LANGCODEMAP["MSG_CONSTANT_DAY"]!} ${theFaultSchedule.everyHour!""}${LANGCODEMAP["MSG_CONSTANT_HOUR"]!} ${theFaultSchedule.everyMinute!""}${LANGCODEMAP["MSG_CONSTANT_MINUTE"]!}			
						</#if>
						<#if (theFaultSchedule.checkItem) == 1> 
							${LANGCODEMAP["MSG_DIAG_HIS_ADC_APPLIANCE"]!}, <span class="txt_gray1">${LANGCODEMAP["MSG_DIAG_HIS_SERVICE"]!}</span>
						<#elseif (theFaultSchedule.checkItem) == 2>
							<span class="txt_gray1">${LANGCODEMAP["MSG_DIAG_HIS_ADC_APPLIANCE"]!}</span>, ${LANGCODEMAP["MSG_DIAG_HIS_SERVICE"]!}
						<#elseif (theFaultSchedule.checkItem) == 3>
							${LANGCODEMAP["MSG_DIAG_HIS_ADC_APPLIANCE_SERVICE"]!}
						<#elseif (theFaultSchedule.checkItem) == 0>
							<span class="txt_gray1">${LANGCODEMAP["MSG_DIAG_HIS_ADC_APPLIANCE_SERVICE"]!}</span> 
						<#else>
						</#if>
						${LANGCODEMAP["MSG_DIAG_HIS_NOTICE_RESERVE"]!}					
					<#else>										
						<#if (theFaultSchedule.checkItem) == 1> 
							${LANGCODEMAP["MSG_DIAG_HIS_ADC_APPLIANCE"]!}</span>
						<#elseif (theFaultSchedule.checkItem) == 2>
							${LANGCODEMAP["MSG_DIAG_HIS_SERVICE"]!}
						<#elseif (theFaultSchedule.checkItem) == 3>
							${LANGCODEMAP["MSG_DIAG_HIS_ADC_APPLIANCE_SERVICE"]!}
						<#elseif (theFaultSchedule.checkItem) == 0>
							<span class="txt_gray1">${LANGCODEMAP["MSG_DIAG_HIS_ADC_APPLIANCE_SERVICE"]!}</span> 
						<#else>
						</#if>
						${LANGCODEMAP["MSG_DIAG_HIS_NOTICE_RESERVE"]!}
						
						<#if (theFaultSchedule.everyHour!"") < 10 > 
							<#assign hour = "0">
						<#else>
							<#assign hour = "">
						</#if>	
						
						<#if (theFaultSchedule.everyMinute!"") < 10 > 
							<#assign minute = "0">
						<#else>
							<#assign minute = "">
						</#if>					
						
						<#if (theFaultSchedule.scheduleType) == 1>
							${LANGCODEMAP["MSG_CONSTANT_DAILY"]!} ${LANGCODEMAP["MSG_CONSTANT_AT"]!} ${hour!""}${theFaultSchedule.everyHour!""}:${minute!""}${theFaultSchedule.everyMinute!""}
						<#elseif (theFaultSchedule.scheduleType) == 2>
							${LANGCODEMAP["MSG_CONSTANT_EWEEK"]!}
							<#if (theFaultSchedule.everyDayOfWeek) == 1>${LANGCODEMAP["MSG_CONSTANT_SUN"]!}
							<#elseif (theFaultSchedule.everyDayOfWeek) == 2>${LANGCODEMAP["MSG_CONSTANT_MON"]!}
							<#elseif (theFaultSchedule.everyDayOfWeek) == 3>${LANGCODEMAP["MSG_CONSTANT_TUE"]!}
							<#elseif (theFaultSchedule.everyDayOfWeek) == 4>${LANGCODEMAP["MSG_CONSTANT_WED"]!}
							<#elseif (theFaultSchedule.everyDayOfWeek) == 5>${LANGCODEMAP["MSG_CONSTANT_THU"]!}
							<#elseif (theFaultSchedule.everyDayOfWeek) == 6>${LANGCODEMAP["MSG_CONSTANT_FRI"]!}
							<#elseif (theFaultSchedule.everyDayOfWeek) == 7>${LANGCODEMAP["MSG_CONSTANT_SAT"]!}
							</#if>
							${LANGCODEMAP["MSG_CONSTANT_AT"]!} ${hour!""}${theFaultSchedule.everyHour!""}:${minute!""}${theFaultSchedule.everyMinute!""}
						<#elseif (theFaultSchedule.scheduleType) == 3>
							${LANGCODEMAP["MSG_CONSTANT_MONTHLY"]!} ${theFaultSchedule.everyDayOfMonth!""} ${LANGCODEMAP["MSG_CONSTANT_AT"]!} ${hour!""}${theFaultSchedule.everyHour!""}:${minute!""}${theFaultSchedule.everyMinute!""}
						<#elseif (theFaultSchedule.scheduleType) == 4>
							<#if (theFaultSchedule.everyMonth) == 1>${LANGCODEMAP["MSG_CONSTANT_JAN"]!}
							<#elseif (theFaultSchedule.everyMonth) == 2>${LANGCODEMAP["MSG_CONSTANT_FEB"]!}
							<#elseif (theFaultSchedule.everyMonth) == 3>${LANGCODEMAP["MSG_CONSTANT_MAR"]!}
							<#elseif (theFaultSchedule.everyMonth) == 4>${LANGCODEMAP["MSG_CONSTANT_APR"]!}
							<#elseif (theFaultSchedule.everyMonth) == 5>${LANGCODEMAP["MSG_CONSTANT_MAY"]!}
							<#elseif (theFaultSchedule.everyMonth) == 6>${LANGCODEMAP["MSG_CONSTANT_JUNE"]!}
							<#elseif (theFaultSchedule.everyMonth) == 7>${LANGCODEMAP["MSG_CONSTANT_JULY"]!}
							<#elseif (theFaultSchedule.everyMonth) == 8>${LANGCODEMAP["MSG_CONSTANT_AUG"]!}
							<#elseif (theFaultSchedule.everyMonth) == 9>${LANGCODEMAP["MSG_CONSTANT_SEP"]!}
							<#elseif (theFaultSchedule.everyMonth) == 10>${LANGCODEMAP["MSG_CONSTANT_OCT"]!}
							<#elseif (theFaultSchedule.everyMonth) == 11>${LANGCODEMAP["MSG_CONSTANT_NOV"]!}
							<#elseif (theFaultSchedule.everyMonth) == 12>${LANGCODEMAP["MSG_CONSTANT_DEC"]!}
							</#if>
							${theFaultSchedule.everyDayOfMonth!""} ${LANGCODEMAP["MSG_CONSTANT_AT"]!} ${hour!""}${theFaultSchedule.everyHour!""}:${minute!""}${theFaultSchedule.everyMinute!""}			
						</#if>		
					</#if>	
					(${theFaultSchedule.targetObj.name!''}&nbsp;)		
				</a>
	 			<a class="delSchedule" href="#" data-index="${theFaultSchedule.index!''}">
	 				&nbsp; <img src="imgs/btn/delete.gif"/> 
	 			</a>			
            </li>	

        </div>
			</#list>        
        </td>
	</tr>    	
	<#list faultCheckLogList![] as theFault>
	<tr class="ContentsLine1 faultHistoryList">
		<td class="align_center">
			<input class="historyChk" type="checkbox"/>
			<span class="none logKey">${theFault.logKey!}</span>		
		</td>
		<td class="align_center">						
			${(theFault.occurTime?string("yyyy-MM-dd HH:mm:ss"))!}	
		</td>
		<td class="align_center">
			<#if (theFault.status!"") == '100'> ${LANGCODEMAP["MSG_CONSTANT_COM"]!}
			<#elseif (theFault.status!"") == '101'> ${LANGCODEMAP["MSG_CONSTANT_FAIL"]!}
			<#elseif (theFault.status!"") == '102'> ${LANGCODEMAP["MSG_CONSTANT_CANCEL"]!}
			<#else> ${LANGCODEMAP["MSG_CONSTANT_PROGING"]!}  (${theFault.status!""}%)
			</#if>			
		</td>
		<#if (theFault.status!"") == '100'>
        <td class="align_left_P10">       	
			<a class="diagnosisResult" href="#" data-index="${theFault.adcIndex!}">${theFault.adcName!}</a>
		</td>	
		<#else>
		<td class="align_left_P10">${theFault.adcName!}</td>
		</#if>		
		<td class="align_center">${theFault.accntName!}</td>
		<td class="align_center">
			<#if (theFault.checkItem) == 1>
				${LANGCODEMAP["MSG_DIAG_HIS_ADC_APPLIANCE"]!}</span>
			<#elseif (theFault.checkItem) == 2>
				${LANGCODEMAP["MSG_DIAG_HIS_SERVICE"]!}
			<#elseif (theFault.checkItem) == 3>
				${LANGCODEMAP["MSG_DIAG_HIS_ADC_APPLIANCE_SERVICE"]!}
			<#elseif (theFault.checkItem) == 0>
			<#else>
			</#if>
		</td>		
	    <td class="align_left_P10 max40bytes" title="${theFault.summary!}">${theFault.summary!}</td>
	</tr>
	<tr class="DivideLine">
		<td colspan="7"></td>			
	</tr>
	</#list>
</tbody>
<tr class="EndLine">
	<td colspan="7"></td>
</tr>
	
