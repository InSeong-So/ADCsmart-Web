<#if orderType??>
	<#if 11 == orderType></#if><!-- occurTime -->
	<#if 15 == orderType></#if><!-- status -->
	<#if 14 == orderType></#if><!-- name -->
	<#if 28 == orderType></#if><!-- period  -->
	<#if 16 == orderType></#if><!-- type -->
</#if>
<#if orderDir??>
	<#if 1 == orderDir></#if><!-- Asc -->
	<#if 2 == orderDir></#if><!-- Desc -->
</#if>	
<colgroup>
	<col width="40px"/>
  	<col width="140px"/>
	<col width="75px"/>
	<col width="auto"/>
	<col width="190px">
	<col width="130px"/>
	<col width="140px"/>                         
	<col width="75px"/>                            
</colgroup>
<thead>                 
	<tr class="StartLine">
		<td colspan="8"></td>
	</tr>
	<tr class="ContentsHeadLine">
		<th>
		<#if accountRole! != 'readOnly' && accountRole! != 'vsAdmin' && accountRole! != 'rsAdmin'>
			<span>
				<input class="allReportsChk" type="checkbox"/>
			</span>
		<#else>
			<span>
				<input class="allReportsChk" type="checkbox" disabled="disabled"/>
			</span>
		</#if>
		</th>
		<th>
			<span class="css_textCursor">
				<#if orderDir == 2 && orderType == 11>		
					<a class="orderDir_Desc">${LANGCODEMAP["MSG_REPORT_OCCUR_TIME"]!}
						<img src="imgs/Desc.gif"/>
						<span class="none orderType">11</span>
						<span class="none orderDir">1</span>					
					</a>						
				<#elseif orderDir == 1 && orderType == 11>	
					<a class="orderDir_Asc">${LANGCODEMAP["MSG_REPORT_OCCUR_TIME"]!}
						<img src="imgs/Asc.gif"/>
						<span class="none orderType">11</span>
						<span class="none orderDir">2</span>		
					</a>
				<#else>
					<a class="orderDir_None">${LANGCODEMAP["MSG_REPORT_OCCUR_TIME"]!}
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
					<a class="orderDir_Desc">${LANGCODEMAP["MSG_REPORT_STATUS"]!}
						<img src="imgs/Desc.gif"/>
						<span class="none orderType">15</span>
						<span class="none orderDir">1</span>					
					</a>						
				<#elseif orderDir == 1 && orderType == 15>	
					<a class="orderDir_Asc">${LANGCODEMAP["MSG_REPORT_STATUS"]!}
						<img src="imgs/Asc.gif"/>
						<span class="none orderType">15</span>
						<span class="none orderDir">2</span>		
					</a>
				<#else>
					<a class="orderDir_None">${LANGCODEMAP["MSG_REPORT_STATUS"]!}
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
					<a class="orderDir_Desc">${LANGCODEMAP["MSG_REPORT_TABLENAME"]!}
						<img src="imgs/Desc.gif"/>
						<span class="none orderType">14</span>
						<span class="none orderDir">1</span>					
					</a>						
				<#elseif orderDir == 1 && orderType == 14>	
					<a class="orderDir_Asc">${LANGCODEMAP["MSG_REPORT_TABLENAME"]!}
						<img src="imgs/Asc.gif"/>
						<span class="none orderType">14</span>
						<span class="none orderDir">2</span>		
					</a>
				<#else>
					<a class="orderDir_None">${LANGCODEMAP["MSG_REPORT_TABLENAME"]!}
						<img src="imgs/none.gif"/>
						<span class="none orderType">14</span>
						<span class="none orderDir">2</span>		
					</a>
				</#if>
			</span>					
		</th>
		<th>
			<span class="css_textCursor">
				<#if orderDir == 2 && orderType == 28>		
					<a class="orderDir_Desc">${LANGCODEMAP["MSG_REPORT_TABLEPERIOD"]!}
						<img src="imgs/Desc.gif"/>
						<span class="none orderType">28</span>
						<span class="none orderDir">1</span>					
					</a>						
				<#elseif orderDir == 1 && orderType == 28>	
					<a class="orderDir_Asc">${LANGCODEMAP["MSG_REPORT_TABLEPERIOD"]!}
						<img src="imgs/Asc.gif"/>
						<span class="none orderType">28</span>
						<span class="none orderDir">2</span>		
					</a>
				<#else>
					<a class="orderDir_None">${LANGCODEMAP["MSG_REPORT_TABLEPERIOD"]!}
						<img src="imgs/none.gif"/>
						<span class="none orderType">28</span>
						<span class="none orderDir">2</span>		
					</a>
				</#if>
			</span>					
		</th>
		<th>
			<span class="css_textCursor">
				<#if orderDir == 2 && orderType == 16>		
					<a class="orderDir_Desc">${LANGCODEMAP["MSG_REPORT_TABLETYPE"]!}
						<img src="imgs/Desc.gif"/>
						<span class="none orderType">16</span>
						<span class="none orderDir">1</span>					
					</a>						
				<#elseif orderDir == 1 && orderType == 16>	
					<a class="orderDir_Asc">${LANGCODEMAP["MSG_REPORT_TABLETYPE"]!}
						<img src="imgs/Asc.gif"/>
						<span class="none orderType">16</span>
						<span class="none orderDir">2</span>		
					</a>
				<#else>
					<a class="orderDir_None">${LANGCODEMAP["MSG_REPORT_TABLETYPE"]!}
						<img src="imgs/none.gif"/>
						<span class="none orderType">16</span>
						<span class="none orderDir">2</span>		
					</a>
				</#if>
			</span>					
		</th>
		<th>${LANGCODEMAP["MSG_REPORT_ADC_NAME"]!}</th>
		<th>${LANGCODEMAP["MSG_REPORT_USER"]!}</th>
	</tr>
	<tr class="StartLine1">
		<td colspan="8"></td>
	</tr>
</thead>
<tbody>
<#list reports as theReport>
	<tr class="ContentsLine1 reportList">
    	<td class="align_center">
    	<#if accountRole! != 'readOnly' && accountRole! != 'vsAdmin' && accountRole! != 'rsAdmin'>	
			<input class="reportChk" type="checkbox"/>
		<#else>
			<input class="reportChk" type="checkbox" disabled="disabled"/>
		</#if>
			<span class="none reportIndex">${theReport.index!}</span>
		</td>
		<td class="align_center textOver" title="${(theReport.creationTime?string("yyyy-MM-dd HH:mm:ss"))!}">${(theReport.creationTime?string("yyyy-MM-dd HH:mm:ss"))!}</td>
		<td class="align_center">${theReport.creationStatus!}</td>
		<#if theReport.status! == 3>
			<td class="align_left_P10 textOver" title="${theReport.name!}">
				<a class="downloadLnk" href="#">${theReport.name!}</a>
			</td>
		<#else>
			<td class="align_left_P10 textOver" title="${theReport.name!}">${theReport.name!}</td>
		</#if>
		
		<#if theReport.type! != "sysFalultReport">
			<td class="align_center textOver">-</td>
		<#else>
			<td class="align_center textOver" title="${(theReport.fromPeriod?string("yyyy-MM-dd"))!} ~ ${(theReport.toPeriod?string("yyyy-MM-dd"))!}">${(theReport.fromPeriod?string("yyyy-MM-dd"))!} ~ ${(theReport.toPeriod?string("yyyy-MM-dd"))!}</td>
		</#if>

		<#if theReport.type! == "sysAdminReport">   
			<td class="align_center textOver" title="${LANGCODEMAP["MSG_CONSTANT_SYS_OPER_REPORT"]!}">${LANGCODEMAP["MSG_CONSTANT_SYS_OPER_REPORT"]!}</td>
		<#elseif theReport.type! == "sysAdminTotalReport">
			<td class="align_center textOver" title="${LANGCODEMAP["MSG_CONSTANT_SYS_OPER_TOTAL_REPORT"]!}">${LANGCODEMAP["MSG_CONSTANT_SYS_OPER_TOTAL_REPORT"]!}</td>			
		<#elseif theReport.type! == "sysFalultReport">
			<td class="align_center textOver" title="${LANGCODEMAP["MSG_CONSTANT_FAIL_ANAL_REPORT"]!}">${LANGCODEMAP["MSG_CONSTANT_FAIL_ANAL_REPORT"]!}</td>			
		<#elseif theReport.type! == "adcDiagnosisReport">
			<td class="align_center textOver" title="${LANGCODEMAP["MSG_CONSTANT_ADC_DIAGNOSIS_REPORT"]!}">${LANGCODEMAP["MSG_CONSTANT_ADC_DIAGNOSIS_REPORT"]!}</td>			
		<#elseif theReport.type! == "l4OpDailyReport">
			<td class="align_center textOver" title="${LANGCODEMAP["MSG_CONSTANT_L4_OPER_REPORT_DAY"]!}">${LANGCODEMAP["MSG_CONSTANT_L4_OPER_REPORT_DAY"]!}</td>
		<#elseif theReport.type! == "l4OpWeeklyReport">
			<td class="align_center textOver" title="${LANGCODEMAP["MSG_CONSTANT_L4_OPER_REPORT_WEEK"]!}">${LANGCODEMAP["MSG_CONSTANT_L4_OPER_REPORT_WEEK"]!}</td>
		<#elseif theReport.type! == "l4OpMonthlyReport">
			<td class="align_center textOver" title="${LANGCODEMAP["MSG_CONSTANT_L4_OPER_REPORT_MONTH"]!}">${LANGCODEMAP["MSG_CONSTANT_L4_OPER_REPORT_MONTH"]!}</td>
		<#elseif theReport.type! == "l4OperationReport">
			<td class="align_left_P10 textOver" title="${LANGCODEMAP["MSG_CONSTANT_L4_OPER_REPORT"]!}">${LANGCODEMAP["MSG_CONSTANT_L4_OPER_REPORT"]!}</td>
		</#if>

		<td class="align_left_P10 textOver" title="<#list theReport.adcNames as x> ${x!} 
			<#if x_has_next>
			</#if></#list>"><#list theReport.adcNames as x> ${x!} <#if x_has_next></#if></#list>
		</td>
		<td class="align_left_P10 textOver" title="${theReport.accountId!}">${theReport.accountId!}</td>
	</tr>
	<tr class="DivideLine">
		<td colspan="8"></td>
	</tr>
</#list>
</tbody>							
<tr class="EndLine">
	<td colspan="8"></td>
</tr>