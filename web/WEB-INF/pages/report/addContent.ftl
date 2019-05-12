<#setting number_format="0.####">
<#if langCode??>
  <#if "ko_KR" == langCode><#assign img_lang = ""></#if>
  <#if "en_US" == langCode><#assign img_lang = "_eng"></#if>
</#if>
<div class="contents_area">
	<form id="reportAddFrm" class="reportSetting">	
	<div>
		<img src="imgs/title${img_lang!""}/h3_reportAdd.gif" class="title_h3"/>				
	</div>
	<div class="title_h4">
		<li>${LANGCODEMAP["MSG_REPORT_DEFAULT_SET"]!}</li>
	</div>
		<table class="Board" cellpadding="0" cellspacing="0">
			<colgroup>
				<col width="200px"/>
				<col >
			</colgroup>
			<tr class="StartLine">
				<td colspan="2" ></td>
			</tr>
			<tr>
				<th class="Lth2">
					<li>${LANGCODEMAP["MSG_REPORT_TYPE"]!}</li>
				</th>
				<td class="Lth0">
					<select name="reportAdd.reportType" id="select2" class="inputSelect">
						<option value="sysFalultReport">${LANGCODEMAP["MSG_CONSTANT_FAIL_ANAL_REPORT"]!}</option>
						<option value="sysAdminReport">${LANGCODEMAP["MSG_CONSTANT_SYS_OPER_REPORT"]!}</option>
						<option value="sysAdminTotalReport">${LANGCODEMAP["MSG_CONSTANT_SYS_OPER_TOTAL_REPORT"]!}</option>
						<option value="adcDiagnosisReport">${LANGCODEMAP["MSG_CONSTANT_ADC_DIAGNOSIS_REPORT"]!}</option>
						<#if (varIsSDSSite) == true>
							<option value="l4OperationReport">${LANGCODEMAP["MSG_CONSTANT_L4_OPER_REPORT"]!}</option> 
						</#if>
					</select>
					<span class="alertMsg_Monitor_Mode alertMsg"  style="vertical-align: inherit;" >						
						<input type="checkbox" class="chk_sysadmin_report" id="chk_sysadmin">
							<label for="chk_sysadmin">${LANGCODEMAP["MSG_REPORT_SYSADMIN_REPORT_MSG"]!}</label>
					</span>
				</td>
			</tr>
			<tr class="DivideLine">
				<td colspan="2"></td>
			</tr>
			<tr class="duration">
				<th class="Lth2">
					<li>${LANGCODEMAP["MSG_REPORT_PERIOD"]!}</li>
				</th>
				<td class="Lth0">
					<span class="reportGroup">
						<input id="previousDate" name="reportAdd.periodType" type="radio" checked="checked" value="previousDate"/>
						<label class="txt_red" for="previousDate">${LANGCODEMAP["MSG_REPORT_PREVIOUS_DAY"]!}</label>&nbsp;&nbsp;&nbsp;
						<input name="reportAdd.previousDate" type="text" class="none formtext_wdth200" title="${LANGCODEMAP["MSG_REPORT_PREVIOUS_DAY"]!}" value="${(reportAdd.previousDate?string("yyyy-MM-dd"))!}"/>
						<input id="customDate" name="reportAdd.periodType" type="radio" value="custom"/>
						<label for="customDate" >${LANGCODEMAP["MSG_REPORT_QUERY_PERIOD_SEL"]!}
							<input name="reportAdd.fromPeriod" class="inputText_calendar2" id ="id_all_txtbox_fromDate" type="text" title="${LANGCODEMAP["MSG_REPORT_QUERY_PERIOD_SEL"]!}" readonly/><img src="imgs/meun/btn_calendar.png" id="id_all_lnk_fromDate"/>	
									&nbsp;~&nbsp;
							<input name="reportAdd.toPeriod" class="inputText_calendar2" id ="id_all_txtbox_toDate" type="text" title="${LANGCODEMAP["MSG_REPORT_QUERY_PERIOD_SEL"]!}" readonly/><img src="imgs/meun/btn_calendar.png" id="id_all_lnk_toDate"/>
						</label>
					</span>
				</td>
			</tr>
			<tr class="result none">
				<th class="Lth2">
					<li>${LANGCODEMAP["MSG_REPORT_RESULTLIST"]!}</li>
				</th>
				<td class="Lth0">
					<select name="reportAdd.extraInfo" id="select2" class="inputSelect">
						<#list faultCheckLogList as theFault>
							<#assign checkItemText = "">
							<#if (theFault.checkItem) == 1>
								<#assign checkItemText = "${LANGCODEMAP['MSG_DIAG_HIS_ADC_APPLIANCE']!}">
							<#elseif (theFault.checkItem) == 2>
								<#assign checkItemText = "${LANGCODEMAP['MSG_DIAG_HIS_SERVICE']!}">
							<#elseif (theFault.checkItem) == 3>
								<#assign checkItemText = "${LANGCODEMAP['MSG_DIAG_HIS_ADC_APPLIANCE_SERVICE']!}">
							<#else>
							</#if>
							<option value="${theFault.logKey!''}">${(theFault.occurTime?string("yyyy-MM-dd HH:mm:ss"))!''} ${checkItemText!""}</option>
						</#list>
					</select>					
				</td>
			</tr>
			<tr class="DivideLine">
				<td colspan="2"></td>
			</tr>			
			<tr>
				<th class="Lth2" ><li>${LANGCODEMAP["MSG_REPORT_FILE_FORMAT"]!}</li></th>
				<td> 
					<span class="Lth0 none" id="sysAdmin" >
						<input type="radio" name="reportAdd.outType" id="select_PDF" checked="checked" value="pdf"/>
							<label for="select_PDF">
							<img src="imgs/icon/icon_pdf.gif"/>
							</label>&nbsp; &nbsp; 
						<input type="radio" name="reportAdd.outType" id="select_RTF" value="rtf"/>
							<label for="select_RTF">
							<img src="imgs/icon/icon_rtf.gif"/>
							</label>&nbsp; &nbsp; 
						<input type="radio" name="reportAdd.outType" id="select_PPTX" value="pptx"/>
							<label for="select_PPTX">
							<img src="imgs/icon/icon_ppt.gif"/>
							</label>
					</span>
					<span class="Lth0 none" id="sysAdminTotal" >
						<input type="radio" name="reportAdd.outType" id="select_PDF_total" checked="checked" value="pdf"/>
							<label for="select_PDF">
								<img src="imgs/icon/icon_pdf.gif"/>
							</label>
					</span>
					<span class="Lth0" id="sysFault" >
						<input type="radio" name="reportAdd.outType" id="select_PDF_fault" checked="checked" value="pdf"/>
							<label for="select_PDF">
							<img src="imgs/icon/icon_pdf.gif"/>
							</label>&nbsp; &nbsp; 
						<input type="radio" name="reportAdd.outType" id="select_PPTX" value="pptx"/>
							<label for="select_PPTX">
							<img src="imgs/icon/icon_ppt.gif"/>
							</label>
					</span>	
				</td>
         	</tr>
			<tr class="DivideLine">
				<td colspan="2"></td>
			</tr>
			<tr>
				<th class="Lth2">
					<li>${LANGCODEMAP["MSG_REPORT_NAME"]!}</li>
				</th>
				<td class="Lth0">
					<span class="inputTextposition">
						<input name="reportAdd.name" type="text" class="inputText reportAdd_width" id="textfield3" value="${reportAdd.name!}"/>
					</span>
				</td>
	        </tr> 
 	         
			<tr class="DivideLine" style="display: none;">
				<td colspan="2"></td>
			</tr>             
			<!-- 아래의 ADC 선택 부분을 display none으로 처리함. 다중 ADC 선택 후 처리 부분에 문제가 발생됨. 20130809 -->
	 		<tr style="display: none;">
				<th class="Lth2">
					<li>${LANGCODEMAP["MSG_REPORT_ADC_SEL"]!}</li>
				</th>
				<td class="Lth0 align_top">
	            	<table  width="97%" border="0" cellpadding="0" cellspacing="0">
						<colgroup>
							<col width="48%" />
							<col width="4% >
							<col width="48%" />
						</colgroup>
						<tr> 
					 		<td>
					 			<span class="usrselected_th">${LANGCODEMAP["MSG_REPORT_SELECT"]!}</span>
					 			<div class="usrselected_txt">
					 			<span class="selectedAdcsCount txt_blue">${(registeredAdcMap!{})?values?size}</span> ${LANGCODEMAP["MSG_REPORT_COUNT_SEL"]!}</div>
					 		</td>
							
							<td></td>
							<td>
								<span class="userlist_txt">${LANGCODEMAP["MSG_REPORT_LIST"]!}</span>
							</td>
						</tr>    
						<tr>
							<td>		  
								<select name="reportAdd.adcs" size="6" multiple="multiple" class="adcsSelectedSel usrselecte" id="textarea2" >
								</select>
							</td>
							<td>								
                                <div class="position_arrow">
			      					<a class="toAdcSelectionLnk " href="#">
			      						<img src="imgs/meun/btn_mov_lft.png" alt="${LANGCODEMAP["MSG_REPORT_MOVE_TO_SEL"]!}" />
			      					</a>
			      				</div>
								<div class="position_arrow">
									<a class="toAdcDeselectionLnk" href="#">
										<img src="imgs/meun/btn_mov_rgt.png" alt="${LANGCODEMAP["MSG_REPORT_MOVE_TO_SEL"]!}" />
									</a>
								</div>								
							</td>
							<td> 	                                		
				     			<span class="inputTextposition1">
									<input name="textfield3" type="text" class="inputText_search" id="textfield3" />
								</span>
								<span class="btn1">
									<a href="#" class="adcSearchLnk">
										<img src="imgs/meun/btn_search_1.png" alt="${LANGCODEMAP["MSG_REPORT_SEARCH"]!}" />
									</a>
								</span>				
								<br class="clearfix" />
								<select name="textarea2" size="5" multiple="multiple" class="adcsDeselectedSel userlist" >
									<#list availableAdcs![] as theAdc>
										<option value="${theAdc.index}">${theAdc.name}</option>
									</#list>
  								</select>
							</td>
    				 	</tr>
					</table>
				</td>
			</tr>
			<tr class="EndLine2">
				<td colspan="2"></td>
			</tr>
			<tr> 
				<td colspan="4">                            
					<div class="position_cT10">
						<input type="button" class="okLnk Btn_red" value="${LANGCODEMAP["MSG_REPORT_COMPLETE"]!}"/>   
						<input type="button" class="cancelLnk Btn_white" value="${LANGCODEMAP["MSG_REPORT_CANCEL"]!}"/>  
					</div> 
				</td>
			</tr>                          
		</table> 
	</form>	   
</div>
<br class="clearfix" />