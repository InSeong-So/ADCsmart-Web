<#if orderType??>
	<#if 11 == orderType></#if><!-- occurTime -->
	<#if 15 == orderType></#if><!-- status -->
	<#if 14 == orderType></#if><!-- name -->
	<#if 29 == orderType></#if><!-- option -->
	<#if 30 == orderType></#if><!-- size -->
</#if>
<#if orderDir??>
	<#if 1 == orderDir></#if><!-- Asc -->
	<#if 2 == orderDir></#if><!-- Desc -->
</#if>
<colgroup>							                            
	<col width="40px"/>
	<col width="140px"/>
	<col width="120px"/>
	<col width="150px"/>
	<col width="100px"/>
	<col width="auto"/>
	<col width="140px"/>
    <col width="75px"/>
</colgroup>
<thead>
	<tr class="StartLine">
		<td colspan="8"></td>
	</tr>
	<tr class="ContentsHeadLine">
		<th>
			<span class="bg_row2 "><input class="allBackupChk" type="checkbox" /></span>
		</th>
		<th>
			<span class="css_textCursor">
				<#if orderDir == 2 && orderType == 11>		
					<a class="orderDir_Desc">${LANGCODEMAP["MSG_SYSSETTINGBAK_OCCUR_TIME"]!}
						<img src="imgs/Desc.gif"/>
						<span class="none orderType">11</span>
						<span class="none orderDir">1</span>					
					</a>						
				<#elseif orderDir == 1 && orderType == 11>	
					<a class="orderDir_Asc">${LANGCODEMAP["MSG_SYSSETTINGBAK_OCCUR_TIME"]!}
						<img src="imgs/Asc.gif"/>
						<span class="none orderType">11</span>
						<span class="none orderDir">2</span>		
					</a>
				<#else>
					<a class="orderDir_None">${LANGCODEMAP["MSG_SYSSETTINGBAK_OCCUR_TIME"]!}
						<img src="imgs/none.gif"/>
						<span class="none orderType">11</span>
						<span class="none orderDir">2</span>		
					</a>
				</#if>
			</span>					
		</th>
		<th>
			<span class="css_textCursor">
				<#if orderDir == 2 && orderType == 15>		
					<a class="orderDir_Desc">${LANGCODEMAP["MSG_SYSSETTINGBAK_CREATE"]!}
						<img src="imgs/Desc.gif"/>
						<span class="none orderType">15</span>
						<span class="none orderDir">1</span>					
					</a>						
				<#elseif orderDir == 1 && orderType == 15>	
					<a class="orderDir_Asc">${LANGCODEMAP["MSG_SYSSETTINGBAK_CREATE"]!}
						<img src="imgs/Asc.gif"/>
						<span class="none orderType">15</span>
						<span class="none orderDir">2</span>		
					</a>
				<#else>
					<a class="orderDir_None">${LANGCODEMAP["MSG_SYSSETTINGBAK_CREATE"]!}
						<img src="imgs/none.gif"/>
						<span class="none orderType">15</span>
						<span class="none orderDir">2</span>		
					</a>
				</#if>
			</span>					
		</th>
		<th>
			<span class="css_textCursor">
				<#if orderDir == 2 && orderType == 14>		
					<a class="orderDir_Desc">${LANGCODEMAP["MSG_SYSSETTINGBAK_NAME"]!}
						<img src="imgs/Desc.gif"/>
						<span class="none orderType">14</span>
						<span class="none orderDir">1</span>					
					</a>						
				<#elseif orderDir == 1 && orderType == 14>	
					<a class="orderDir_Asc">${LANGCODEMAP["MSG_SYSSETTINGBAK_NAME"]!}
						<img src="imgs/Asc.gif"/>
						<span class="none orderType">14</span>
						<span class="none orderDir">2</span>		
					</a>
				<#else>
					<a class="orderDir_None">${LANGCODEMAP["MSG_SYSSETTINGBAK_NAME"]!}
						<img src="imgs/none.gif"/>
						<span class="none orderType">14</span>
						<span class="none orderDir">2</span>		
					</a>
				</#if>
			</span>					
		</th>
		<th>${LANGCODEMAP["MSG_SYSSETTINGBAK_USER"]!}</th>
		<th>${LANGCODEMAP["MSG_SYSSETTINGBAK_DESC"]!}</th>
		<th>
			<span class="css_textCursor">
				<#if orderDir == 2 && orderType == 29>		
					<a class="orderDir_Desc">${LANGCODEMAP["MSG_SYSSETTINGBAK_OPTION"]!}
						<img src="imgs/Desc.gif"/>
						<span class="none orderType">29</span>
						<span class="none orderDir">1</span>					
					</a>						
				<#elseif orderDir == 1 && orderType == 29>	
					<a class="orderDir_Asc">${LANGCODEMAP["MSG_SYSSETTINGBAK_OPTION"]!}
						<img src="imgs/Asc.gif"/>
						<span class="none orderType">29</span>
						<span class="none orderDir">2</span>		
					</a>
				<#else>
					<a class="orderDir_None">${LANGCODEMAP["MSG_SYSSETTINGBAK_OPTION"]!}
						<img src="imgs/none.gif"/>
						<span class="none orderType">29</span>
						<span class="none orderDir">2</span>		
					</a>
				</#if>
			</span>					
		</th>
		<th>
			<span class="css_textCursor">
				<#if orderDir == 2 && orderType == 30>		
					<a class="orderDir_Desc">${LANGCODEMAP["MSG_SYSSETTINGBAK_SIZE"]!}
						<img src="imgs/Desc.gif"/>
						<span class="none orderType">30</span>
						<span class="none orderDir">1</span>					
					</a>						
				<#elseif orderDir == 1 && orderType == 30>	
					<a class="orderDir_Asc">${LANGCODEMAP["MSG_SYSSETTINGBAK_SIZE"]!}
						<img src="imgs/Asc.gif"/>
						<span class="none orderType">30</span>
						<span class="none orderDir">2</span>		
					</a>
				<#else>
					<a class="orderDir_None">${LANGCODEMAP["MSG_SYSSETTINGBAK_SIZE"]!}
						<img src="imgs/none.gif"/>
						<span class="none orderType">30</span>
						<span class="none orderDir">2</span>		
					</a>
				</#if>
			</span>				
		</th>
	</tr>
	<tr class="StartLine1">
		<td colspan="8"></td>
	</tr>		
</thead>
<tbody>
	<tr>
		<td colspan="8" >
			<#list backupSchedules![] as schedule>
		        <div class="commonheight">				
					<li class="common">
					<img src="/imgs/icon/icon_clock.png"/>
						<a class="scheduleLnk">${reserve!""}			
							<span class="none scheduleIndex">${schedule.index!''}</span>				
							<span class="none scheduleType">${schedule.scheduleType!''}</span>
							<span class="none scheduleHour">${schedule.scheduleHour!''}</span>
							<span class="none scheduleMinute">${schedule.scheduleMinute!''}</span>
							<span class="none scheduleDayweek">${schedule.scheduleDayweek!''}</span>
							<span class="none scheduleMonth">${schedule.scheduleMonth!''}</span>
							<span class="none scheduleDay">${schedule.scheduleDay!''}</span>
							<#if "ko_KR" == langCode>
								<#if (schedule.scheduleType) == 1>
								${LANGCODEMAP["MSG_CONSTANT_DAILY"]!} ${schedule.scheduleHour!""}${LANGCODEMAP["MSG_CONSTANT_HOUR"]!} ${schedule.scheduleMinute!""}${LANGCODEMAP["MSG_CONSTANT_MINUTE"]!}
								<#elseif (schedule.scheduleType) == 2>
									${LANGCODEMAP["MSG_CONSTANT_EWEEK"]!}
									<#if (schedule.scheduleDayweek) == 1>${LANGCODEMAP["MSG_CONSTANT_SUN"]!}
									<#elseif (schedule.scheduleDayweek) == 2>${LANGCODEMAP["MSG_CONSTANT_MON"]!}
									<#elseif (schedule.scheduleDayweek) == 3>${LANGCODEMAP["MSG_CONSTANT_TUE"]!}
									<#elseif (schedule.scheduleDayweek) == 4>${LANGCODEMAP["MSG_CONSTANT_WED"]!}
									<#elseif (schedule.scheduleDayweek) == 5>${LANGCODEMAP["MSG_CONSTANT_THU"]!}
									<#elseif (schedule.scheduleDayweek) == 6>${LANGCODEMAP["MSG_CONSTANT_FRI"]!}
									<#elseif (schedule.scheduleDayweek) == 7>${LANGCODEMAP["MSG_CONSTANT_SAT"]!}
									</#if>
									 ${schedule.scheduleHour!""}${LANGCODEMAP["MSG_CONSTANT_HOUR"]!} ${schedule.scheduleMinute!""}${LANGCODEMAP["MSG_CONSTANT_MINUTE"]!}
								<#elseif (schedule.scheduleType) == 3>
									${LANGCODEMAP["MSG_CONSTANT_MONTHLY"]!} ${schedule.scheduleDay!""} ${LANGCODEMAP["MSG_CONSTANT_DAY"]!} ${schedule.scheduleHour!""}${LANGCODEMAP["MSG_CONSTANT_HOUR"]!} ${schedule.scheduleMinute!""}${LANGCODEMAP["MSG_CONSTANT_MINUTE"]!}
								<#elseif (schedule.scheduleType) == 4>
									${schedule.scheduleMonth!""}${LANGCODEMAP["MSG_CONSTANT_MONTH"]!} ${schedule.scheduleDay!""}${LANGCODEMAP["MSG_CONSTANT_DAY"]!} ${schedule.scheduleHour!""}${LANGCODEMAP["MSG_CONSTANT_HOUR"]!} ${schedule.scheduleMinute!""}${LANGCODEMAP["MSG_CONSTANT_MINUTE"]!}			
								</#if>
								${LANGCODEMAP["MSG_SYSSETTINGBAK_SCHEDULED"]!}					
							<#else>										
								${LANGCODEMAP["MSG_SYSSETTINGBAK_SCHEDULED"]!}
								
								<#if (schedule.scheduleHour!"") < 10 > 
									<#assign hour = "0">
								<#else>
									<#assign hour = "">
								</#if>	
								
								<#if (schedule.scheduleMinute!"") < 10 > 
									<#assign minute = "0">
								<#else>
									<#assign minute = "">
								</#if>					
								
								<#if (schedule.scheduleType) == 1>
									${LANGCODEMAP["MSG_CONSTANT_DAILY"]!} ${LANGCODEMAP["MSG_CONSTANT_AT"]!} ${hour!""}${schedule.scheduleHour!""}:${minute!""}${schedule.scheduleMinute!""}
								<#elseif (schedule.scheduleType) == 2>
									${LANGCODEMAP["MSG_CONSTANT_EWEEK"]!}
									<#if (schedule.scheduleDayweek) == 1>${LANGCODEMAP["MSG_CONSTANT_SUN"]!}
									<#elseif (schedule.scheduleDayweek) == 2>${LANGCODEMAP["MSG_CONSTANT_MON"]!}
									<#elseif (schedule.scheduleDayweek) == 3>${LANGCODEMAP["MSG_CONSTANT_TUE"]!}
									<#elseif (schedule.scheduleDayweek) == 4>${LANGCODEMAP["MSG_CONSTANT_WED"]!}
									<#elseif (schedule.scheduleDayweek) == 5>${LANGCODEMAP["MSG_CONSTANT_THU"]!}
									<#elseif (schedule.scheduleDayweek) == 6>${LANGCODEMAP["MSG_CONSTANT_FRI"]!}
									<#elseif (schedule.scheduleDayweek) == 7>${LANGCODEMAP["MSG_CONSTANT_SAT"]!}
									</#if>
									${LANGCODEMAP["MSG_CONSTANT_AT"]!} ${hour!""}${schedule.scheduleHour!""}:${minute!""}${schedule.scheduleMinute!""}
								<#elseif (schedule.scheduleType) == 3>
									${LANGCODEMAP["MSG_CONSTANT_MONTHLY"]!} ${schedule.scheduleDay!""} ${LANGCODEMAP["MSG_CONSTANT_AT"]!} ${hour!""}${schedule.scheduleHour!""}:${minute!""}${schedule.scheduleMinute!""}
								<#elseif (schedule.scheduleType) == 4>
									<#if (schedule.scheduleMonth) == 1>${LANGCODEMAP["MSG_CONSTANT_JAN"]!}
									<#elseif (schedule.scheduleMonth) == 2>${LANGCODEMAP["MSG_CONSTANT_FEB"]!}
									<#elseif (schedule.scheduleMonth) == 3>${LANGCODEMAP["MSG_CONSTANT_MAR"]!}
									<#elseif (schedule.scheduleMonth) == 4>${LANGCODEMAP["MSG_CONSTANT_APR"]!}
									<#elseif (schedule.scheduleMonth) == 5>${LANGCODEMAP["MSG_CONSTANT_MAY"]!}
									<#elseif (schedule.scheduleMonth) == 6>${LANGCODEMAP["MSG_CONSTANT_JUNE"]!}
									<#elseif (schedule.scheduleMonth) == 7>${LANGCODEMAP["MSG_CONSTANT_JULY"]!}
									<#elseif (schedule.scheduleMonth) == 8>${LANGCODEMAP["MSG_CONSTANT_AUG"]!}
									<#elseif (schedule.scheduleMonth) == 9>${LANGCODEMAP["MSG_CONSTANT_SEP"]!}
									<#elseif (schedule.scheduleMonth) == 10>${LANGCODEMAP["MSG_CONSTANT_OCT"]!}
									<#elseif (schedule.scheduleMonth) == 11>${LANGCODEMAP["MSG_CONSTANT_NOV"]!}
									<#elseif (schedule.scheduleMonth) == 12>${LANGCODEMAP["MSG_CONSTANT_DEC"]!}
									</#if>
									${schedule.scheduleDay!""} ${LANGCODEMAP["MSG_CONSTANT_AT"]!} ${hour!""}${schedule.scheduleHour!""}:${minute!""}${schedule.scheduleMinute!""}			
								</#if>		
							</#if>
							<#if (schedule.type) == 0>
								(${LANGCODEMAP["MSG_CONSTANT_ADCS_SET_LOG_INFO"]!})
							<#elseif (schedule.type) == 1>
								(${LANGCODEMAP["MSG_CONSTANT_ADCS_SET_INFO"]!})
							<#elseif (schedule.type) == 2>
								(${LANGCODEMAP["MSG_CONSTANT_ADCS_LOG_INFO"]!})
							<#elseif (schedule.type) == 3>
								(${LANGCODEMAP["MSG_CONSTANT_ADC_SET_INFO"]!})
							</#if>
						</a>
			 			<a class="delSchedule" href="#">
			 				<span class="none">${schedule.index!''}&nbsp;</span>&nbsp; <img src="imgs/btn/delete.gif"/>
			 			</a>			
		            </li>	
		
		        </div>
			</#list>        
        </td>
	</tr>    	
	<#list sysBackups![] as theBackup>
	<tr class="ContentsLine3 backupList">
		<td class="align_center">
			<input class="backupChk" type="checkbox" />
			<span class="none backupIndex bg_row2">${theBackup.index!}</span>
		</td>
		<td class="align_center textOver" title="${(theBackup.backupTime?string("yyyy-MM-dd HH:mm:ss"))!}">${(theBackup.backupTime?string("yyyy-MM-dd HH:mm:ss"))!}</td>
		<td class="align_center">${theBackup.status!}</td>
		<td class="align_left_P10 textOver" title="${theBackup.fileName!}">
			<a class="downloadLnk" href="#">${theBackup.fileName!}</a>
		</td>
		<td class="align_left_P10">${theBackup.accountId!}</td>
		<td class="align_left_P10 textOver" title="${theBackup.description!}">${theBackup.description!} ${theBackup.target!}</td>
		
		<#if theBackup.target! == "all">
			<td class="align_center textOver" title="${LANGCODEMAP["MSG_CONSTANT_ADCS_SET_LOG_INFO"]!}">${LANGCODEMAP["MSG_CONSTANT_ADCS_SET_LOG_INFO"]!}</td>
		<#elseif theBackup.target! == "adcsmart Settings">
			<td class="align_center textOver" title="${LANGCODEMAP["MSG_CONSTANT_ADCS_SET_INFO"]!}">${LANGCODEMAP["MSG_CONSTANT_ADCS_SET_INFO"]!}</td>
		<#elseif theBackup.target! == "adcsmart">
			<td class="align_center textOver" title="${LANGCODEMAP["MSG_CONSTANT_ADCS_LOG_INFO"]!}">${LANGCODEMAP["MSG_CONSTANT_ADCS_LOG_INFO"]!}</td>	
		<#elseif theBackup.target! == "ADC Settings">
			<td class="align_center textOver" title="${LANGCODEMAP["MSG_CONSTANT_ADC_SET_INFO"]!}">${LANGCODEMAP["MSG_CONSTANT_ADC_SET_INFO"]!}</td>
		</#if>
		
		<td class="align_right_p10">${theBackup.fileSizeWithUnit!}</td>
	</tr>
	<tr class="DivideLine">
		<td colspan="8"></td>
	</tr>			
	</#list>
	<tr class="EndLine">
		<td colspan="8"></td>
	</tr>
</tbody>