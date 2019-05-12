<#setting number_format="0.####">
<#if langCode??>
  <#if "ko_KR" == langCode><#assign img_lang = ""></#if>
  <#if "en_US" == langCode><#assign img_lang = "_eng"></#if>
</#if>
<div class="contents_area"> 
<div>
	<img class="title_h3" src="imgs/title${img_lang!""}/h3_diagnosisResult.gif" /></img>
</div>
	<div class="Board" style="margin-bottom:8px; padding-right:10px;">
	    <input type="button" id="resultPrint" class="btn_new Btn_white" value="${LANGCODEMAP["MSG_DIAG_HIS_PRINT"]!}"/>  
		<input type="button" class="btn_out exportCssLnk Btn_white" value="${LANGCODEMAP["MSG_DIAG_HIS_OUT_FILE"]!}"/>  		
	</div>
	<br class="clearfix" />
	<div class="printArea">
	<table class="Board" cellpadding="0" cellspacing="0" >
		<caption>&nbsp;</caption>
		<colgroup>
			<col width="200px" />
			<col />
		</colgroup>
		<tr class="StartLine">
			<td colspan="2"></td>
		</tr>
		<tr>
			<th class="Lth1">
				<li>${LANGCODEMAP["MSG_DIAG_HIS_ADC_NAME"]!}</li>
			</th>
			<td class="Lth0">				
				<#if adcObject.category == 0>
					<span class="txt_bold">${adc.name!""}</span>
				<#elseif adcObject.category == 1>
					<span class="txt_bold"><img src="/imgs/layout/icon_folder_off.gif"/>&nbsp;${adc.name!""}</span>
				<#elseif adcObject.category == 2>
					<#if adc.type == 'Alteon'>
						<img src="/imgs/icon/adc/icon_alteon_s.png"/>
					<#elseif adc.type == 'F5'>
						<img src="/imgs/icon/adc/icon_f5_s.png"/>
					<#elseif adc.type == 'PAS'>
						<img src="/imgs/icon/adc/icon_piolink_s.png"/>
					<#elseif adc.type == 'PASK'>
						<img src="/imgs/icon/adc/icon_piolink_s.png"/>
					<#else>
						<img src="/imgs/icon/adc/icon_alteon_s.png"/>
					</#if>
					&nbsp;<span class="txt_bold">${adc.name!""}</span>
				<#else>
				</#if>							
			</td>
		</tr>
		<tr class="DivideLine1">
			<td colspan="2"></td>
		</tr>
		<tr>
			<th class="Lth1">
				<li>${LANGCODEMAP["MSG_DIAG_HIS_DIAGNOSIS_TIME"]!}</li>
			</th>
			<td class="Lth0">
				${(faultCheckResult.startTime?string("yyyy-MM-dd HH:mm:ss"))!''} ~ ${(faultCheckResult.endTime?string("yyyy-MM-dd HH:mm:ss"))!''}
			        (${LANGCODEMAP["MSG_DIAG_HIS_ABOUT"]!} ${faultCheckResult.elapseTime!''} ${LANGCODEMAP["MSG_DIAG_HIS_SEC"]!})	
			</td>
		</tr>
		<tr class="EndLine2">
			<td colspan="2"></td>
		</tr>
	</table>
	<div class="title_h4_1">
		<li>${LANGCODEMAP["MSG_DIAG_HIS_DIAGNOSIS_RESULT"]!} 
	     	<input type="button" class="resultAllShow allShow Btn_white_small" value="${LANGCODEMAP["MSG_DIAG_HIS_OPEN_ALL"]!}"/>  	     	
	     	<input type="button" class="resultAllShowDisable none Btn_white_small" disabled="disabled" value="${LANGCODEMAP["MSG_DIAG_HIS_OPEN_ALL"]!}"/> 						
			<input type="button" class="resultAllHide none allHide Btn_black_small" value="${LANGCODEMAP["MSG_DIAG_HIS_CLOSE_ALL"]!}"/>   
			<input type="button" class="resultAllHideDisable none Btn_black_small" disabled="disabled" value="${LANGCODEMAP["MSG_DIAG_HIS_CLOSE_ALL"]!}"/>   			
		</li>
	</div>
	<#if (faultCheckResult.hwResults?has_content) || (faultCheckResult.l23Results?has_content) || (faultCheckResult.l47Results?has_content)>
	<table class="Board upDownAdc" cellpadding="0" cellspacing="0">
		<caption>&nbsp;</caption>
		<colgroup>
			<col width="61px" />
			<col width="141px" />
			<col width="70px" />
			<col width="auto" />
		</colgroup>
		<tr class="StartLine">
			<td colspan="4"></td>
		</tr>		
		<tr class="commonth">
			<td colspan="3" class="align_left_P20">
				<li>${LANGCODEMAP["MSG_DIAG_HIS_ADC_APPLIANCE_DIAGNOSIS"]!}  </li>
			</td>
			<td class="bno">
				<p class="open upDownAdc" id="adcFlag" isflag="0">
					<img class="adcDiagnosisClick" src="/imgs/icon/arrows_up.png" alt="${LANGCODEMAP["MSG_DIAG_HIS_OPEN"]!}">
				</p>
			</td>		
		</tr>
		<tr class="StartLine1">
			<td colspan="4"></td>
		</tr>
	</table>	
	<div>
	
	<table class="Board" cellpadding="0" cellspacing="0" style="table-layout: fixed;">
		<caption>&nbsp;</caption>
		<colgroup>
			<col width="61px" />
			<col width="141px" />
			<col width="70px" />
			<col width="auto" />
		</colgroup>		
		<tr class="ContentsHeadLine">
			<th colspan="2" class="Rcolor">${LANGCODEMAP["MSG_DIAG_HIS_LIST"]!}</th>
			<th class="Rcolor">${LANGCODEMAP["MSG_DIAG_HIS_STATUS"]!}</th>
			<th>${LANGCODEMAP["MSG_DIAG_HIS_DETAIL_CONTENT"]!}</th>
		</tr>
		<tr class="DivideLine">
			<td colspan="4"></td>
		</tr>
		<#if faultCheckResult.hwResults?has_content>		
		<tr>
			<th class="RWcolor hd_bottom_line">H/W</th>
			<td colspan="3">
				<table id="report_table" cellpadding="0" cellspacing="0">
					<colgroup>
						<col width="140px" />
						<col width="70px" />
						<col width="auto" />
					</colgroup>
					<tr>
						<th colspan="3"></th>
					</tr>							
					<#list faultCheckResult.hwResults as theFaultChkRs> 
						<#if 0 == theFaultChkRs.status>
							<#assign imageFileName = "icon_result_normal.png">
							<#assign statusText = "normal">					
						<#elseif 1 == theFaultChkRs.status>
							<#assign imageFileName = "icon_result_error.png">
							<#assign statusText = "error">
						<#elseif 2 == theFaultChkRs.status>
							<#assign imageFileName = "icon_result_ok.png">
							<#assign statusText = "ok">
						<#elseif 3 == theFaultChkRs.status>
							<#assign imageFileName = "icon_result_info.png">
							<#assign statusText = "info">		
						</#if>
					<tr class="odd">
						<td class="objName">${theFaultChkRs.obj.name!''}</td>
						<td class="align_center">
							<span>					    		
					    		<#if "" != imageFileName>
					    			<img src="imgs/icon/${imageFileName}" alt="${statusText}" />
					    		</#if>
					    	</span>					    	
						</td>
						<td> 
							<table id="report" cellpadding="0" cellspacing="0">
								<tr>
									<th colspan="3"></th>
								</tr>								
								<tr>												
									<td>
										<#list theFaultChkRs.resultList as theResult>						
											<#if 0 == theFaultChkRs.status>
												<div class="txt1_None">
													<a class="summaryTitle cusor01" isdetail="0">${theResult.summary!''}</a>
												</div>
											<#elseif 1 == theFaultChkRs.status>
												<div class="txt1_error">
													<a class="summaryTitle cusor01" isdetail="1">${theResult.summary!''}</a>
												</div>
											<#elseif 2 == theFaultChkRs.status>
												<div class="txt1_ok okStyle">
													<a class="summaryTitle cusor01" isdetail="0">${theResult.summary!''}</a>
												</div>
											<#elseif 3 == theFaultChkRs.status>
												<div class="txt1_info infoStyle">
													<a class="summaryTitle cusor01" isdetail="0">${theResult.summary!''}</a>
												</div>
											</#if>
											
											<div style="display:none;">
											<#if theResult.detail?has_content>
												<#if 0 == theFaultChkRs.status>
												<div class="detail_normal80 padding4_5">
												<#elseif 1 == theFaultChkRs.status>
												<div class="detail_error80 padding4_5">
												<#elseif 2 == theFaultChkRs.status>
												<div class="detail_ok80 padding4_5">
												<#elseif 3 == theFaultChkRs.status>
												<div class="detail_info80 padding4_5">
												</#if>
													<div class="copy none">
														<a href="#"><img  src="imgs/icon/icon_copy.gif" /></img></a>
													</div>
													<div>
														<span><pre>${theResult.detail!''}</pre></span>
													</div>													
												</div>
											</#if>
											<#if theFaultChkRs.solution?has_content>
												<div class="detail_solution80 padding4_5">&raquo; <span>${theFaultChkRs.solution!''}</span></div>
											<#else>
											</#if>
											</div>
										</#list>									
										<#if (theFaultChkRs.obj.index) == 5>																
											<div class="gage1">
												<div class="cpuGauge1">												
												</div>
											</div>
											<div class="chart1">
												<div class="cpuChart1">												
												</div> 
											</div>						
										<#else>
										</#if>	
									</td>								
								</tr>
							</table>
						</td>				
					</tr>					
					</#list>											
				</table>
			</td>
		</tr>
		</#if>
		<#if faultCheckResult.l23Results?has_content>
		<tr>
			<th class="RWcolor hd_bottom_line">L2-3</th>
			<td colspan="3">
				<table id="report_table" cellpadding="0" cellspacing="0">
					<colgroup>
						<col width="140px" />
						<col width="70px" />
						<col width="auto" />
					</colgroup>
					<tr>
						<th colspan="3"></th>
					</tr>							
					<#list faultCheckResult.l23Results as theFaultChkRs>
						<#if 0 == theFaultChkRs.status>
							<#assign imageFileName = "icon_result_normal.png">
							<#assign statusText = "normal">					
						<#elseif 1 == theFaultChkRs.status>
							<#assign imageFileName = "icon_result_error.png">
							<#assign statusText = "error">
						<#elseif 2 == theFaultChkRs.status>
							<#assign imageFileName = "icon_result_ok.png">
							<#assign statusText = "ok">
						<#elseif 3 == theFaultChkRs.status>
							<#assign imageFileName = "icon_result_info.png">
							<#assign statusText = "info">		
						</#if>
					<tr class="odd">
						<td>${theFaultChkRs.obj.name!''}</td>
						<td class="align_center">
							<span >					    		
					    		<#if "" != imageFileName>
					    			<img src="imgs/icon/${imageFileName}" alt="${statusText}" />
					    		</#if>
					    	</span>
						</td>
						<td>
							<table id="report" cellpadding="0" cellspacing="0">
								<tr>
									<th colspan="3"></th>
								</tr>								
								<tr>
									<td>
										<#list theFaultChkRs.resultList as theResult>							
											<#if 0 == theFaultChkRs.status> 
												<div class="txt1_None">
													<a class="summaryTitle cusor01" isdetail="0">${theResult.summary!''}</a>
												</div>
											<#elseif 1 == theFaultChkRs.status>
												<div class="txt1_error">
													<a class="summaryTitle cusor01" isdetail="1">${theResult.summary!''}</a>
												</div>
											<#elseif 2 == theFaultChkRs.status>
												<div class="txt1_ok">
													<a class="summaryTitle cusor01" isdetail="0">${theResult.summary!''}</a>
												</div>
											<#elseif 3 == theFaultChkRs.status>
												<div class="txt1_info">
													<a class="summaryTitle cusor01" isdetail="0">${theResult.summary!''}</a>
												</div>
											</#if>		
											
											<div style="display:none;">
											<#if theResult.detail?has_content>
												<#if 0 == theFaultChkRs.status>
												<div class="detail_normal80 padding4_5">
												<#elseif 1 == theFaultChkRs.status>
												<div class="detail_error80 padding4_5">
												<#elseif 2 == theFaultChkRs.status>
												<div class="detail_ok80 padding4_5">
												<#elseif 3 == theFaultChkRs.status>
												<div class="detail_info80 padding4_5">
												</#if>
													<div class="copy none">
														<a href="#"><img  src="imgs/icon/icon_copy.gif" /></img></a>
													</div>
													<div>
														<span><pre>${theResult.detail!''}</pre></span>
													</div>													
												</div>
											</#if>
											<#if theFaultChkRs.solution?has_content>
												<div class="detail_solution80 padding4_5">&raquo; <span>${theFaultChkRs.solution!''}</span></div>
											<#else>
											</#if>
											</div>															
										</#list>										
									</td>
								</tr>								
							</table>
						</td>				
					</tr>								
					</#list>											
				</table>
			</td>
		</tr>
		</#if>
		<#if faultCheckResult.l47Results?has_content>
		<tr>
			<th class="RWcolor hd_bottom_line">L4-7</th>
			<td colspan="3">
				<table id="report_table" cellpadding="0" cellspacing="0">
					<colgroup>
						<col width="140px" />
						<col width="70px" />
						<col width="auto" />
					</colgroup>
					<tr>
						<th colspan="3"></th>
					</tr>							
					<#list faultCheckResult.l47Results as theFaultChkRs>
						<#if 0 == theFaultChkRs.status>
							<#assign imageFileName = "icon_result_normal.png">
							<#assign statusText = "normal">					
						<#elseif 1 == theFaultChkRs.status>
							<#assign imageFileName = "icon_result_error.png">
							<#assign statusText = "error">
						<#elseif 2 == theFaultChkRs.status>
							<#assign imageFileName = "icon_result_ok.png">
							<#assign statusText = "ok">
						<#elseif 3 == theFaultChkRs.status>
							<#assign imageFileName = "icon_result_info.png">
							<#assign statusText = "info">		
						</#if>
					<tr class="odd">
						<td>${theFaultChkRs.obj.name!''}</td>
						<td class="align_center">
							<span >					    		
					    		<#if "" != imageFileName>
					    			<img src="imgs/icon/${imageFileName}" alt="${statusText}" />
					    		</#if>
					    	</span>
						</td>
						<td>
							<table id="report" cellpadding="0" cellspacing="0">
								<tr>
									<th colspan="3"></th>
								</tr>
								<tr>
									<td>
										<#list theFaultChkRs.resultList as theResult>							
											<#if 0 == theFaultChkRs.status> 
												<div class="txt1_None">
													<a class="summaryTitle cusor01" isdetail="0" isdetail1="0" id="sumTitle">${theResult.summary!''}</a>
												</div>
											<#elseif 1 == theFaultChkRs.status>
												<div class="txt1_error">
													<a class="summaryTitle cusor01" isdetail="0" isdetail1="0">${theResult.summary!''}</a>
												</div>
											<#elseif 2 == theFaultChkRs.status>
												<div class="txt1_ok">
													<a class="summaryTitle cusor01" isdetail="0" isdetail1="0">${theResult.summary!''}</a>
												</div>
											<#elseif 3 == theFaultChkRs.status>
												<div class="txt1_info">
													<a class="summaryTitle cusor01" isdetail="0" isdetail1="0">${theResult.summary!''}</a>
												</div>
											</#if>		
											
											<div style="display:none;">
											<#if theResult.detail?has_content>
												<#if 0 == theFaultChkRs.status>
												<div class="detail_normal80 padding4_5">
												<#elseif 1 == theFaultChkRs.status>
												<div class="detail_error80 padding4_5">
												<#elseif 2 == theFaultChkRs.status>
												<div class="detail_ok80 padding4_5">
												<#elseif 3 == theFaultChkRs.status>
												<div class="detail_info80 padding4_5">
												</#if>
													<div class="copy none">
														<a href="#"><img  src="imgs/icon/icon_copy.gif" /></img></a>
													</div>
													<div>
														<span><pre>${theResult.detail!''}</pre></span>
													</div>													
												</div>
											</#if>
											<#if theFaultChkRs.solution?has_content>
												<div class="detail_solution80 padding4_5">&raquo; <span>${theFaultChkRs.solution!''}</span></div>
											<#else>
											</#if>
											</div>				
										</#list>
									</td>
								</tr>																
							</table>
						</td>				
					</tr>								
					</#list>											
				</table>
			</td>
		</tr>
		</#if>
	</table>
	</div>
	</#if>
	<#if faultCheckResult.svcResults?has_content>
	<table class="Board upDownSvc" cellpadding="0" cellspacing="0">
		<caption>&nbsp;</caption>
		<colgroup>
			<col width="auto" />
			<col width="50px" />			
			<col width="40px" />
		</colgroup>
		<tr class="StartLine1">
			<td colspan="3"></td>
		</tr>
		<tr class="commonth">
			<td class="align_left_P20">
				<li>${LANGCODEMAP["MSG_DIAG_HIS_SERVICE_DIAGNOSIS"]!}
					<span class="txt_blue">[${faultCheckResult.vsvcName!''}(${faultCheckResult.vsvcIPAddress!''} : ${faultCheckResult.vsvcPort!''})]</span>						
				</li>
			</td>
			<#list faultCheckResult.svcResults as result>
				<#if 2 == result.status>
					<#assign downloadBtnDisplay = "">
					<#break>
				</#if>
			</#list>
			<td>
				<span class="none logKey">${faultCheckResult.checkKey!''}</span>	
				<input type="button" class="downloadPktDumpLnk Btn_white_small ${downloadBtnDisplay!'none'}" value="${LANGCODEMAP["MSG_DIAG_HIS_DOWNLOAD"]!}"/>  
			</td>
			<td class="bno">
				<p class="open" id="svcFlag" isflag="0">
					<a><img class="svcClick" src="/imgs/icon/arrows_up.png" alt="${LANGCODEMAP["MSG_DIAG_HIS_OPEN"]!}"></a>
				</p>
			</td>				
		</tr>
		<tr class="StartLine1">
			<td colspan="3"></td>
		</tr>
	</table>	
	<div>
	<table class="Board" cellpadding="0" cellspacing="0">
		<caption>&nbsp;</caption>
		<colgroup>
			<col width="202px" />
			<col width="auto" />
		</colgroup>		
		<tr class="ContentsHeadLine">
			<th class="Rcolor">${LANGCODEMAP["MSG_DIAG_HIS_LIST"]!}</th>
			<th>${LANGCODEMAP["MSG_DIAG_HIS_DETAIL_CONTENT"]!}</th>
		</tr>
		<tr class="DivideLine">
			<td colspan="2"></td>
		</tr>
		<tr>
			<td colspan="2">
				<table id="report_table" cellpadding="0" cellspacing="0">
					<colgroup>
						<col width="202px" />
						<col width="auto" />
					</colgroup>
					<tr>
						<th colspan="2"></th>
					</tr>
					
					<#list faultCheckResult.svcResults as theFaultChkRs>
					<tr class="odd ">
						<td  class="no_left_line" >${theFaultChkRs.obj.name!''}</td>						
						<td>
							<table id="report" cellpadding="0" cellspacing="0">
								<tr>
									<th colspan="2"></th>
								</tr>
								<tr >
									<td>
										<#list theFaultChkRs.resultList as theResult>
											<#if 0 == theFaultChkRs.status> 
												<div class="txt1_None">
													<a class="summaryTitle cusor01 hover" isdetail="0">${theResult.summary!''}</a>
												</div>
											<#elseif 1 == theFaultChkRs.status>
												<div class="txt1_error">
													<a class="summaryTitle cusor01 hover" isdetail="1">${theResult.summary!''}</a>
												</div>
											<#elseif 2 == theFaultChkRs.status>
												<div class="txt1_ok">
													<a class="summaryTitle cusor01 hover" isdetail="0">${theResult.summary!''}</a>
												</div>
											<#elseif 3 == theFaultChkRs.status>
												<div class="txt1_info">
													<a class="summaryTitle cusor01 hover" isdetail="0">${theResult.summary!''}</a>
												</div>
											</#if>
											<div style="display:none;">
											<#if theResult.detail?has_content>
												<#if 0 == theFaultChkRs.status>
												<div class="detail_normal80 padding4_5">
												<#elseif 1 == theFaultChkRs.status>
												<div class="detail_error80 padding4_5">
												<#elseif 2 == theFaultChkRs.status>
												<div class="detail_ok80 padding4_5">
												<#elseif 3 == theFaultChkRs.status>
												<div class="detail_info80 padding4_5">
												</#if>
													<div class="copy none">
														<a href="#"><img  src="imgs/icon/icon_copy.gif" /></img></a>
													</div>
													<div>
														<span><pre>${theResult.detail!''}</pre></span>
													</div>													
												</div>
											</#if>
											<#if theFaultChkRs.solution?has_content>
												<div class="detail_solution80 padding4_5">&raquo; <span>${theFaultChkRs.solution!''}</span></div>
											<#else>
											</#if>
											</div>		
										</#list>
										<div>										
											<#if 3 != theFaultChkRs.status && (theFaultChkRs.obj.index) == 3>
												<table class="Board" cellpadding="0" cellspacing="0">
													<colgroup>
														<col width="200px" />
														<col />
													</colgroup>
													<tr>
														<td colspan="2" class="align_right_p10">${LANGCODEMAP["MSG_DIAG_HIS_MEASURE_MS"]!}</td>													
													</tr>
													<tr>
														<th class="Lth0-2">
															<li>${LANGCODEMAP["MSG_DIAG_HIS_DIAGNOSIS_RESULT"]!}</li>
														</th>														
														<td class="Lth0">
															<div class="progressbarareaCurr"></div>														
														</td>													
													</tr> 
													<tr>
														<th class="Lth0-2">
															<li>${LANGCODEMAP["MSG_DIAG_HIS_AVERAGE_TIME"]!}</li>
														</th>													
														<td class="Lth0">
															<div class="progressbarareaAvg"></div>															
														</td>													
													</tr>
													<tr>
														<th class="Lth0-2">
															<li>${LANGCODEMAP["MSG_DIAG_HIS_BEFORE_DIAGNOSIS_RESULT"]!}</li>
														</th>														
														<td class="Lth0">
															<div class="progressbarareaPrev"></div>															
														</td>													
													</tr>
												</table>	
																															
											<#elseif (theFaultChkRs.obj.index) == 4>	
												<div id="packetImg" class="detail_packet"></div>	
											<#else>												
											</#if>
										</div>	
									</td>
								</tr>									
							</table>
						</td>				
					</tr>								
					</#list>
				</table>
			</td>
		</tr>		
	</table>
	</div>
	</#if>

	<table class="Board_97" border="0">
		<tr height="10px">
			<td></td>
		</tr>
		<tr>
			<td class="center">
				<input type="button" class="faultHistoryLnk Btn_white" value="${LANGCODEMAP["MSG_DIAG_HIS_LIST"]!}"/> 			
			</td>
		</tr>
		<tr height="10px">
			<td></td>
		</tr>	
	</table>
	</div>
</div>

