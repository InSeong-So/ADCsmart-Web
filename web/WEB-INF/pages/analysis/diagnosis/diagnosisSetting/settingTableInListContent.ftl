<table class="Board" cellpadding="0" cellspacing="0">
	<colgroup>
		<col width="3%"/>
		<col width="30%"/>
		<col width="3%"/>
		<col width="30%"/>
		<col width="3%"/>
		<col width="31%"/>						
	</colgroup>			
	<tr class="StartLine">
		<td colspan="6" ></td>
	</tr>
	<tr class="commonth" >
		<td colspan="6" class="align_left_P20">
			<li><input name="adcChkFlg" class="adcChkFlg" type="checkbox"/> ${LANGCODEMAP["MSG_DIAG_SET_ADC_DIAGNOSISS"]!} </li>
		</td>		
	</tr>
	<tr class="StartLine1">
		<td colspan="6"></td>
	</tr>
	<tr class="ContentsHeadLine">
		<th class="Lth0" >
			<input name="checkbox" class="allHwChk" type="checkbox" id="checkbox" /> 
		</th>		
		<th class="Rcolor">
			H/W
		</th>						
		<th class="Lth0" >
			<input name="checkbox" class="allL23Chk" type="checkbox" id="checkbox" /> 
		</th>		
		<th class="Rcolor">
			L2-3
		</th>	
		<th class="Lth0" >
			<input name="checkbox" class="allL47Chk" type="checkbox" id="checkbox" /> 
		</th>		
		<th>
			L4-7
		</th>		
	</tr>
	<tr class="align_top chkFlag">
		<td colspan="2" class="Rcolor" id="hw">
		<#list faultCheckTemplate.hwCheckItems as theFaultHwChk>
			<#if theFaultHwChk.index != 9>
				<#if theFaultHwChk.state == 1>
					<div class="Board_div1"><input class="hwChk" id="faultChk" value="${theFaultHwChk.index}" type="checkbox" checked/> ${theFaultHwChk.name!""}</div>
				<#else>
					<div class="Board_div1"><input class="hwChk" id="faultChk" value="${theFaultHwChk.index}" type="checkbox"/> ${theFaultHwChk.name!""}</div>
				</#if>
			<#else>
				<#if theFaultHwChk.state == 1>
					<div class="Board_div1"><input class="hwChk" id="faultChk" value="${theFaultHwChk.index}" type="checkbox" checked/> ${theFaultHwChk.name!""}						
						<input type="text" name="adcLogCount" id="${theFaultHwChk.index}" class="inputText width30" value="${faultCheckTemplate.thresholdHWAdcLogCount!""}" >&nbsp;${LANGCODEMAP["MSG_DIAG_SET_COUNT_VIEW"]!}
					</div>
				<#else>
					<div class="Board_div1"><input class="hwChk" id="faultChk" value="${theFaultHwChk.index}" type="checkbox"/> ${theFaultHwChk.name!""}
						<input type="text" name="adcLogCount" id="${theFaultHwChk.index}" class="inputText width30" value="${faultCheckTemplate.thresholdHWAdcLogCount!""}" >&nbsp;${LANGCODEMAP["MSG_DIAG_SET_COUNT_VIEW"]!}
					</div>																																			
				</#if>
			</#if>
		</#list>		
		</td>
		<td colspan="2" class="Rcolor" id="l23">
		<#list faultCheckTemplate.l23CheckItems as theFaultL23Chk>	
			<#if theFaultL23Chk.state == 1>	
				<div class="Board_div1"><input class="l23Chk" id="faultChk" value="${theFaultL23Chk.index}" type="checkbox" checked/> ${theFaultL23Chk.name!""}</div>
			<#else>
				<div class="Board_div1"><input class="l23Chk" id="faultChk" value="${theFaultL23Chk.index}" type="checkbox" /> ${theFaultL23Chk.name!""}</div>
			</#if>		
		</#list>				
		</td>
		<td colspan="2" id="l47">
		<#list faultCheckTemplate.l47CheckItems as theFaultL47Chk>
			<#if (theFaultL47Chk.index != 2)>
				<#if theFaultL47Chk.state == 1>
					<div class="Board_div1"><input class="l47Chk" id="faultChk" value="${theFaultL47Chk.index}" type="checkbox" checked/> ${theFaultL47Chk.name!""}</div>
				<#else>
					<div class="Board_div1"><input class="l47Chk" id="faultChk" value="${theFaultL47Chk.index}" type="checkbox" /> ${theFaultL47Chk.name!""}</div>
				</#if>
			<#else>
				<#if (theFaultL47Chk.index == 2)>
					<#if theFaultL47Chk.state == 1>

						<div class="Board_div1"><input class="l47Chk" id="faultChk" value="${theFaultL47Chk.index}" type="checkbox" checked/> ${theFaultL47Chk.name!""} &nbsp;
						<select name="faultMaxDays" class="inputSelect faultMaxDays_width">
							<#if faultCheckTemplate.thresholdL47SleepVSDay == 1>
								<option value="1" selected="selected">1&nbsp;${LANGCODEMAP["MSG_DIAG_SET_DAY"]!}</option>
								<option value="7">7&nbsp;${LANGCODEMAP["MSG_DIAG_SET_DAYS"]!}</option>
								<option value="15">1&nbsp;${LANGCODEMAP["MSG_DIAG_SET_DAYS"]!}</option>
								<option value="30">30&nbsp;${LANGCODEMAP["MSG_DIAG_SET_DAYS"]!}</option>
							<#elseif faultCheckTemplate.thresholdL47SleepVSDay == 7>
								<option value="1">1&nbsp;${LANGCODEMAP["MSG_DIAG_SET_DAY"]!}</option>
								<option value="7" selected="selected">7&nbsp;${LANGCODEMAP["MSG_DIAG_SET_DAYS"]!}</option>
								<option value="15">15&nbsp;${LANGCODEMAP["MSG_DIAG_SET_DAYS"]!}</option>
								<option value="30">30&nbsp;${LANGCODEMAP["MSG_DIAG_SET_DAYS"]!}</option>
							<#elseif faultCheckTemplate.thresholdL47SleepVSDay == 15>                     
								<option value="1">1&nbsp;${LANGCODEMAP["MSG_DIAG_SET_DAY"]!}</option>
								<option value="7">7&nbsp;${LANGCODEMAP["MSG_DIAG_SET_DAYS"]!}</option>
								<option value="15" selected="selected">15&nbsp;${MSG_DIAG_SET_DAYS["MSG_DIAG_SET_DAY"]!}</option>
								<option value="30">30&nbsp;${LANGCODEMAP["MSG_DIAG_SET_DAYS"]!}</option>
							<#else>                   
								<option value="1">1&nbsp;${LANGCODEMAP["MSG_DIAG_SET_DAY"]!}</option>
								<option value="7">7&nbsp;${LANGCODEMAP["MSG_DIAG_SET_DAYS"]!}</option>
								<option value="15">15&nbsp;${LANGCODEMAP["MSG_DIAG_SET_DAYS"]!}</option>
								<option value="30" selected="selected">30&nbsp;${LANGCODEMAP["MSG_DIAG_SET_DAYS"]!}</option>
							</#if>	
						</select> 					
						</div>
					<#else>
						<div class="Board_div1"><input class="l47Chk" id="faultChk" value="${theFaultL47Chk.index}" type="checkbox" /> ${theFaultL47Chk.name!""} &nbsp;
						<select name="faultMaxDays" class="inputSelect faultMaxDays_width">
							<#if faultCheckTemplate.thresholdL47SleepVSDay == 1>
								<option value="1" selected="selected">1&nbsp;${LANGCODEMAP["MSG_DIAG_SET_DAY"]!}</option>
								<option value="7">7&nbsp;${LANGCODEMAP["MSG_DIAG_SET_DAYS"]!}</option>
								<option value="15">15&nbsp;${LANGCODEMAP["MSG_DIAG_SET_DAYS"]!}</option>
								<option value="30">30&nbsp;${LANGCODEMAP["MSG_DIAG_SET_DAYS"]!}</option>
							<#elseif faultCheckTemplate.thresholdL47SleepVSDay == 7>
								<option value="1">1&nbsp;${LANGCODEMAP["MSG_DIAG_SET_DAY"]!}</option>
								<option value="7" selected="selected">7&nbsp;${LANGCODEMAP["MSG_DIAG_SET_DAYS"]!}</option>
								<option value="15">15&nbsp;${LANGCODEMAP["MSG_DIAG_SET_DAYS"]!}</option>
								<option value="30">30&nbsp;${LANGCODEMAP["MSG_DIAG_SET_DAYS"]!}</option>
							<#elseif faultCheckTemplate.thresholdL47SleepVSDay == 15>                     
								<option value="1">1&nbsp;${LANGCODEMAP["MSG_DIAG_SET_DAY"]!}</option>
								<option value="7">7&nbsp;${LANGCODEMAP["MSG_DIAG_SET_DAYS"]!}</option>
								<option value="15" selected="selected">15&nbsp;${LANGCODEMAP["MSG_DIAG_SET_DAYS"]!}</option>
								<option value="30">30&nbsp;${LANGCODEMAP["MSG_DIAG_SET_DAYS"]!}</option>
							<#else>                   
								<option value="1">1&nbsp;${LANGCODEMAP["MSG_DIAG_SET_DAY"]!}</option>
								<option value="7">7&nbsp;${LANGCODEMAP["MSG_DIAG_SET_DAYS"]!}</option>
								<option value="15">15&nbsp;${LANGCODEMAP["MSG_DIAG_SET_DAYS"]!}</option>
								<option value="30" selected="selected">30&nbsp;${LANGCODEMAP["MSG_DIAG_SET_DAYS"]!}</option>
							</#if>					
						</select>					
						</div>
					</#if>
				</#if>
			</#if>
		</#list>
		</td>
	</tr>
	<tr class="EndLine2">
		<td colspan="6" ></td>
	</tr>			
</table>	
<br> 
<table  class="Board serviceDiagnosis" cellpadding="0" cellspacing="0">
	<colgroup>
		<col width="3%"/>
		<col width="47%"/>	
		<col width="3%"/>
		<col width="47%"/>							
	</colgroup>	
	<tr class="StartLine">
		<td colspan="4"></td>
	</tr>							
	<tr class="commonth" >
		<td colspan="4" class="align_left_P20">
		<#if faultCheckTemplate.svcCheckFlg??>
			<#if (faultCheckTemplate.svcCheckFlg == 1)>
				<li><input name="svcChkFlg" id="svcChkFlg" type="checkbox" checked /> ${LANGCODEMAP["MSG_DIAG_SET_SERVICE_DIAG"]!} </li>
			<#else>
				<li><input name="svcChkFlg" id="svcChkFlg" type="checkbox" /> ${LANGCODEMAP["MSG_DIAG_SET_SERVICE_DIAG"]!} </li>
			</#if>				
		<#else>
			<li><input name="svcChkFlg" id="svcChkFlg" type="checkbox" /> ${LANGCODEMAP["MSG_DIAG_SET_SERVICE_DIAG"]!} </li>
		</#if>
		</td>		
	</tr>
	<tr class="StartLine1">
		<td colspan="4"></td>
	</tr>
	<tr class="ContentsHeadLine">
		<th class="Lth0">
			
		</th>		
		<th class="Rcolor">
			${LANGCODEMAP["MSG_DIAG_SET_USER_IP"]!}
		</th>					
		<th class="Lth0">				
		</th>		
		<th>
			${LANGCODEMAP["MSG_DIAG_SET_SERVICE"]!}
		</th>	
	</tr>				
	<tr class="align_top">
		<td colspan="2" class="Rcolor align_center">		
			<#if faultCheckTemplate.svcCheckFlg?? && (faultCheckTemplate.svcCheckFlg == 1)>					
				<div class="Board_div1">
					<input type="text" id="clientIp" class="inputText width130" name="clientIp" value="${faultCheckTemplate.svcClientIPAddress!''}" />&nbsp;					
					<select class="inputSelect width130" id="selectedIp">
						<option value="0">${LANGCODEMAP["MSG_DIAG_SET_INPUT"]!}</option>
						<#list faultClientIpList as theFaultIp>
							<#if faultCheckTemplate.svcClientIPAddress == theFaultIp>
							<option value="${theFaultIp!""}" selected="selected">${theFaultIp!""}</option>
							<#else>
							<option value="${theFaultIp!""}">${theFaultIp!""}</option>
							</#if>
						</#list> 
					</select>						
				</div>
			<#else>	
			<div class="Board_div1">
				<input type="text" id="clientIp" class="inputText width130" name="clientIp" value="" disabled="disabled" />&nbsp;					
				<select class="inputSelect width130" id="selectedIp"  disabled="disabled">
					<option value="">${LANGCODEMAP["MSG_DIAG_SET_INPUT"]!}</option>
					<#list faultClientIpList as theFaultIp>
					<option value="${theFaultIp!""}">${theFaultIp!""}</option>
					</#list> 
				</select>	
			</div>
			</#if>
		</td>
		<td colspan="2"  class="align_center" id="virtualSvc">	
			<#if faultCheckTemplate.svcCheckFlg?? && (faultCheckTemplate.svcCheckFlg == 1)>		
			<div class="Board_div1">	
				<input type="text" id="clientIp" class="inputText width130 none" name="clientIp" value="${faultCheckTemplate.svcVSIPAddress!''}" />&nbsp;								
				<select name="svc" class="inputSelect selectedSvc" id="selectedSvc">
					<#list faultVirtualSvcList as theFaultVirtualSvc>
						<#if faultCheckTemplate.svcVSIndex == theFaultVirtualSvc.svcIndex>
						<option value="${theFaultVirtualSvc.ipAddress!""}|${theFaultVirtualSvc.svcPort!""}|${theFaultVirtualSvc.svcIndex!""}|${theFaultVirtualSvc.svcName!""}" selected="selected">
							${theFaultVirtualSvc.svcName!""}								
						</option>							
						<#else>
						<option value="${theFaultVirtualSvc.ipAddress!""}|${theFaultVirtualSvc.svcPort!""}|${theFaultVirtualSvc.svcIndex!""}|${theFaultVirtualSvc.svcName!""}">
							${theFaultVirtualSvc.svcName!""}								
						</option>
						</#if>							
					</#list>																																				
				</select>		
			</div>
			<div class="Board_div1" id="virtualSvcDesc">	
			</div>	
			
			<#else>
			<div class="Board_div1">				
				<select name="svc" class="inputSelect selectedSvc" id="selectedSvc"  disabled="disabled">
					<#list faultVirtualSvcList as theFaultVirtualSvc>
						<option value="${theFaultVirtualSvc.ipAddress!""}|${theFaultVirtualSvc.svcPort!""}|${theFaultVirtualSvc.svcIndex!""}|${theFaultVirtualSvc.svcName!""}">
							${theFaultVirtualSvc.ipAddress!""}:${theFaultVirtualSvc.svcPort!""} [${theFaultVirtualSvc.svcName!""}]								
						</option>
					</#list>
				</select>						
			</div>
			<div class="Board_div1" id="virtualSvcDesc">
				<!--<input type="text" id="virtualSvcDesc1" class="inputText width130" name="virtualSvcDesc" value="" />&nbsp;-->
				<#if faultVirtualSvcList?has_content>
					<span> IP : ${faultVirtualSvcList[0].ipAddress!""} / ${LANGCODEMAP["MSG_NETWORK_PORT"]!} : ${faultVirtualSvcList[0].svcPort!""} / Name : ${faultVirtualSvcList[0].svcName!""}</span>
				<#else>
					<span> ${LANGCODEMAP["MSG_DIAG_SET_IP_NOTICE"]!} </span>
				</#if>
			</div>	
			</#if>					
		</td>
	</tr>		
	<tr class="EndLine2">
		<td colspan="4"></td>
	</tr>
</table>
