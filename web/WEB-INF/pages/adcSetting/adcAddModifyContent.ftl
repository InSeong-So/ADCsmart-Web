<#setting number_format="0.####">
<#if langCode??>
  <#if "ko_KR" == langCode><#assign img_lang = ""></#if>
  <#if "en_US" == langCode><#assign img_lang = "_eng"></#if>
</#if>
<#if status??>
	<#if 1 == status></#if><!-- 포트 오픈 테스트 -->
	<#if 2 == status></#if><!-- 로그인 테스트  -->
	<#if 3 == status></#if><!-- version -->
	<#if 4 == status></#if><!-- 포트 오픈 테스트(역방향). alteon만 해당됨 -->
	<#if 5 == status></#if><!-- snmp 수신 테스트 -->
	<#if 6 == status></#if><!-- syslog 수신 테스트 -->
</#if>

<div class="contents_area">
	<div>
		<img src="imgs/title${img_lang!""}/h3_adcModify.gif" class="title_h3" />				
	</div>
<!-- ADC동작점검 메뉴 -->
<!--
	<#if ((adcAdd.adc.type) == "Alteon") || ((adcAdd.adc.type) == 'F5')>
		<div class="title_h2"> 
			<table class="Board100" cellpadding="0" cellspacing="0">
				<colgroup>							                            
					<col width="111px"/>
					<col width="111px"/>
					<col/>					
	  	  		</colgroup>
				<tr>
					<td>
						<img src="imgs/meun${img_lang!""}/3depth_adcedit_0.gif" />										
					</td>							
					<td>
						<a class="adcConfigCheckLnk" href="#">
							<img src="imgs/meun${img_lang!""}/3depth_adcsetinfo_1.gif" />												
						</a>
					</td>
					<td>&nbsp;</td>												
				</tr>
			</table>
		</div>
	<div>
		<img src="imgs/title${img_lang!""}/h3_adcModify.gif" class="title_h3_1" />				
	</div>
	<#else>
	<div>
		<img src="imgs/title${img_lang!""}/h3_adcModify.gif" class="title_h3" />				
	</div>
	</#if>
-->	
<!-- ADC동작점검 메뉴 -->
	
	<!-- 1 -->
	<form id="adcAddFrm" class="setting" method="post">
		<div style="display: none;">
			<input name="adcAdd.adc.index" type="text" value="${(adcAdd.adc.index)!''}" />
			<input name="adcAdd.adc.status" type="hidden" value="${(adcAdd.adc.status)!}"/>
			<input name="adcAdd.adc.opModeHidden" type="hidden" value="${(adcAdd.adc.opMode)!}"/>
		</div>
		<div class="title_h4">
			<!--
			<li>${LANGCODEMAP["MSG_ADCSETTING_BASIC_SETTING"]!}</li>
			-->
			<li>${LANGCODEMAP["MSG_ADCSETTING_FIRSTLEVEL"]!}</li>
		</div>
		<table class="Board" cellpadding="0" cellspacing="0">
			<colgroup>
				<col width="200px" />
				<col >
			</colgroup>
			<tr class="StartLine">
				<td colspan="2"></td>
			</tr>
			<tr>				
				<th class="Lth2">
					<li>${LANGCODEMAP["MSG_ADCSETTING_TYPE"]!}</li>
				</th>
				<td class="Lth0">
					<#if ((adcAdd.adc.type)) == 'F5'>
						<input name="adcAdd.adc.type" class="inputText width130" value="F5" disabled="disabled" />
						<input name="adcAdd.adc.type" class="inputText width130" value="F5" type="hidden"/> 
					<#elseif ((adcAdd.adc.type)) == 'Alteon'>
						<input name="adcAdd.adc.type" class="inputText width130" value="Alteon" disabled="disabled" />
						<input name="adcAdd.adc.type" class="inputText width130" value="Alteon" type="hidden" />
					<#elseif ((adcAdd.adc.type)) == 'PAS'>
						<input name="adcAdd.adc.type" class="inputText width130" value="PAS" disabled="disabled" />
						<input name="adcAdd.adc.type" class="inputText width130" value="PAS" type="hidden" />
					<#elseif ((adcAdd.adc.type)) == 'PASK'>
						<input name="adcAdd.adc.type" class="inputText width130" value="PASK" disabled="disabled" />
						<input name="adcAdd.adc.type" class="inputText width130" value="PASK" type="hidden" />
					<#elseif ((adcAdd.adc.type)) == 'PiolinkUnknown'>
						<input name="adcAdd.adc.type" class="inputText width130" value="PiolinkUnknown" disabled="disabled" />
						<input name="adcAdd.adc.type" class="inputText width130" value="PiolinkUnknown" type="hidden" />
					</#if>
					<!--
					<select name="adcAdd.adc.type" id="select" class="inputSelect width134"> 
						<#if ((adcAdd.adc.type)) == 'F5'>
							<option value="F5" selected="selected">F5</option> 
						<#elseif ((adcAdd.adc.type)) == 'Alteon'>
							<option value="Alteon" selected="selected">Alteon</option>
						<#elseif ((adcAdd.adc.type)) == 'PAS'>
							<option value="PAS" selected="selected">Piolink</option> 
						<#elseif ((adcAdd.adc.type)) == 'PASK'>
							<option value="PASK" selected="selected">Piolink</option> 
						<#elseif ((adcAdd.adc.type)) == 'PiolinkUnknown'>
							<option value="PiolinkUnknown" selected="selected">Piolink</option>
						</#if>
					</select>
					-->
				</td>
			</tr>
			<tr class="DivideLine">
				<td colspan="2"></td>
			</tr>
			<tr>
				<th class="Lth2">
					<li>${LANGCODEMAP["MSG_ADCSETTING_NAME"]!}</li>
				</th>
				<td class="Lth0">
					<#if !(adcAdd.adc.name)?? ||(adcAdd.adc.name)! == ''>
						<#if accountRole == 'readOnly'>
							<input type="text" name="adcAdd.adc.name" id="idAdcName" class="inputText width130" value="${(adcAdd.adc.name)!''}" disabled="disabled"/>
						<#else>
							<input type="text" name="adcAdd.adc.name" id="idAdcName" class="inputText width130" value="${(adcAdd.adc.name)!''}" />
						</#if>
					<#else> 
						<#if accountRole == 'readOnly'>
							<input type="text" name="adcAdd.adc.name" id="idAdcName" class="inputText width130" value="${(adcAdd.adc.name)!''}" disabled="disabled"/>
						<#else>
							<input type="text" name="adcAdd.adc.name" id="idAdcName" class="inputText width130" value="${(adcAdd.adc.name)!''}" />
						</#if>
					</#if>
				</td>
			</tr>
			<tr class="DivideLine">
				<td colspan="2"></td>
			</tr>
			<tr>
				<th class="Lth2">
					<li>${LANGCODEMAP["MSG_ADCSETTING_MODE"]!}</li>
				</th>
				<td class="Lth0">
					<#if ((adcAdd.adc.type)) == 'F5' || ((adcAdd.adc.type)) == 'Alteon'>
						<#if (adcAdd.adc.opMode) == 3>
							<span class="mode_info">
								<label for="MODE_MONITORING"><input type="radio" name="adcAdd.adc.opMode" id="MODE_MONITORING" value="1" /> ${LANGCODEMAP["MSG_ADCSETTING_MONITORING"]!}</label>
								<button type="button" class="defbtn_info monitoring_qm">${LANGCODEMAP["MSG_ADCSETTING_DESCVIEW"]!}</button>
								<div class="layer_def_wrap none" id="monitoring_notice" >
									<div class="layer_def_a2 have_tit">
										<h3 class="layer_tit">${LANGCODEMAP["MSG_ADCSETTING_MODE_MONITORING"]!}</h3>
										<div class="layer_conts">
											<ul>
												<li>${LANGCODEMAP["MSG_ADCSETTING_MONITORING_FL"]!}</li>
												<li>${LANGCODEMAP["MSG_ADCSETTING_MONITORING_SL"]!}</li>												
											</ul>
										</div>
									</div>
								</div>
							</span>&nbsp;&nbsp;&nbsp;
							<span class="mode_info mode1">
								<label for="MODE_SETTING"><input type="radio" name="adcAdd.adc.opMode" id="MODE_SETTING" value="2" /> ${LANGCODEMAP["MSG_ADCSETTING_SETTING"]!}</label>
								<button type="button" class="defbtn_info setting_qm">${LANGCODEMAP["MSG_ADCSETTING_DESCVIEW"]!}</button>
								<div class="layer_def_wrap none" id="setting_notice">
									<div class="layer_def_a2 have_tit">
										<h3 class="layer_tit">${LANGCODEMAP["MSG_ADCSETTING_MODE_SETTING"]!}</h3>
										<div class="layer_conts">
											<ul>
												<li>${LANGCODEMAP["MSG_ADCSETTING_SETTING_FL"]!}</li>
												<li>${LANGCODEMAP["MSG_ADCSETTING_SETTING_SL"]!}</li>
											</ul>
										</div>
									</div>
								</div>
							</span>&nbsp;&nbsp;&nbsp;
							<span class="mode_info mode1"">
								<label for="MODE_DIAGNOSIS"><input type="radio" class="mode1" name="adcAdd.adc.opMode" id="MODE_DIAGNOSIS" value="3" checked="checked" /> ${LANGCODEMAP["MSG_ADCSETTING_DIAGNOSIS"]!}</label>
								<button type="button" class="defbtn_info diagnosis_qm">${LANGCODEMAP["MSG_ADCSETTING_DESCVIEW"]!}</button>
								<div class="layer_def_wrap none" id="diagnosis_notice">
									<div class="layer_def_a2 have_tit">
										<h3 class="layer_tit">${LANGCODEMAP["MSG_ADCSETTING_MODE_DIAGNOSIS"]!}</h3>
										<div class="layer_conts">
											<ul>
												<li>${LANGCODEMAP["MSG_ADCSETTING_DIAGNOSIS_FL"]!}</li>
												<li>${LANGCODEMAP["MSG_ADCSETTING_DIAGNOSIS_SL"]!}</li>
												<li>${LANGCODEMAP["MSG_ADCSETTING_DIAGNOSIS_TL"]!}</li>											
											</ul>
										</div>
									</div>
								</div>
							</span>					
						<#elseif (adcAdd.adc.opMode) == 2>
							<span class="mode_info">
								<label for="MODE_MONITORING"><input type="radio" name="adcAdd.adc.opMode" id="MODE_MONITORING" value="1" /> ${LANGCODEMAP["MSG_ADCSETTING_MONITORING"]!}</label>
								<button type="button" class="defbtn_info monitoring_qm">${LANGCODEMAP["MSG_ADCSETTING_DESCVIEW"]!}</button>
								<div class="layer_def_wrap none" id="monitoring_notice" >
									<div class="layer_def_a2 have_tit">
										<h3 class="layer_tit">${LANGCODEMAP["MSG_ADCSETTING_MODE_MONITORING"]!}</h3>
										<div class="layer_conts">
											<ul>
												<li>${LANGCODEMAP["MSG_ADCSETTING_MONITORING_FL"]!}</li>
												<li>${LANGCODEMAP["MSG_ADCSETTING_MONITORING_SL"]!}</li>											
											</ul>
										</div>
									</div>
								</div>
							</span>&nbsp;&nbsp;&nbsp;
							<span class="mode_info mode1">
								<label for="MODE_SETTING"><input type="radio" name="adcAdd.adc.opMode" id="MODE_SETTING" value="2" checked="checked" /> ${LANGCODEMAP["MSG_ADCSETTING_SETTING"]!}</label>
								<button type="button" class="defbtn_info setting_qm">${LANGCODEMAP["MSG_ADCSETTING_DESCVIEW"]!}</button>
								<div class="layer_def_wrap none" id="setting_notice">
									<div class="layer_def_a2 have_tit">
										<h3 class="layer_tit">${LANGCODEMAP["MSG_ADCSETTING_MODE_SETTING"]!}</h3>
										<div class="layer_conts">
											<ul>
												<li>${LANGCODEMAP["MSG_ADCSETTING_SETTING_FL"]!}</li>
												<li>${LANGCODEMAP["MSG_ADCSETTING_SETTING_SL"]!}</li>
											</ul>
										</div>
									</div>
								</div>
							</span>&nbsp;&nbsp;&nbsp;
							<span class="mode_info mode1"">
								<label for="MODE_DIAGNOSIS"><input type="radio" class="mode1" name="adcAdd.adc.opMode" id="MODE_DIAGNOSIS" value="3" /> ${LANGCODEMAP["MSG_ADCSETTING_DIAGNOSIS"]!}</label>
								<button type="button" class="defbtn_info diagnosis_qm">${LANGCODEMAP["MSG_ADCSETTING_DESCVIEW"]!}</button>
								<div class="layer_def_wrap none" id="diagnosis_notice">
									<div class="layer_def_a2 have_tit">
										<h3 class="layer_tit">${LANGCODEMAP["MSG_ADCSETTING_MODE_DIAGNOSIS"]!}</h3>
										<div class="layer_conts">
											<ul>
												<li>${LANGCODEMAP["MSG_ADCSETTING_DIAGNOSIS_FL"]!}</li>
												<li>${LANGCODEMAP["MSG_ADCSETTING_DIAGNOSIS_SL"]!}</li>
												<li>${LANGCODEMAP["MSG_ADCSETTING_DIAGNOSIS_TL"]!}</li>										
											</ul>
										</div>
									</div>
								</div>
							</span>
						<#else>
							<span class="mode_info">
								<label for="MODE_MONITORING"><input type="radio" name="adcAdd.adc.opMode" id="MODE_MONITORING" value="1" checked="checked" /> ${LANGCODEMAP["MSG_ADCSETTING_MONITORING"]!}</label>
								<button type="button" class="defbtn_info monitoring_qm">${LANGCODEMAP["MSG_ADCSETTING_DESCVIEW"]!}</button>
								<div class="layer_def_wrap none" id="monitoring_notice" >
									<div class="layer_def_a2 have_tit">
										<h3 class="layer_tit">${LANGCODEMAP["MSG_ADCSETTING_MODE_MONITORING"]!}</h3>
										<div class="layer_conts">
											<ul>
												<li>${LANGCODEMAP["MSG_ADCSETTING_MONITORING_FL"]!}</li>
												<li>${LANGCODEMAP["MSG_ADCSETTING_MONITORING_SL"]!}</li>
											</ul>
										</div>
									</div>
								</div>
							</span>&nbsp;&nbsp;&nbsp;
							<span class="mode_info mode1">
								<label for="MODE_SETTING"><input type="radio" name="adcAdd.adc.opMode" id="MODE_SETTING" value="2" /> ${LANGCODEMAP["MSG_ADCSETTING_SETTING"]!}</label>
								<button type="button" class="defbtn_info setting_qm">${LANGCODEMAP["MSG_ADCSETTING_DESCVIEW"]!}</button>
								<div class="layer_def_wrap none" id="setting_notice">
									<div class="layer_def_a2 have_tit">
										<h3 class="layer_tit">${LANGCODEMAP["MSG_ADCSETTING_MODE_SETTING"]!}</h3>
										<div class="layer_conts">
											<ul>
												<li>${LANGCODEMAP["MSG_ADCSETTING_SETTING_FL"]!}</li>
												<li>${LANGCODEMAP["MSG_ADCSETTING_SETTING_SL"]!}</li>
											</ul>
										</div>
									</div>
								</div>
							</span>&nbsp;&nbsp;&nbsp;
							<span class="mode_info mode1"">
								<label for="MODE_DIAGNOSIS"><input type="radio" class="mode1" name="adcAdd.adc.opMode" id="MODE_DIAGNOSIS" value="3" /> ${LANGCODEMAP["MSG_ADCSETTING_DIAGNOSIS"]!}</label>
								<button type="button" class="defbtn_info diagnosis_qm">${LANGCODEMAP["MSG_ADCSETTING_DESCVIEW"]!}</button>
								<div class="layer_def_wrap none" id="diagnosis_notice">
									<div class="layer_def_a2 have_tit">
										<h3 class="layer_tit">${LANGCODEMAP["MSG_ADCSETTING_MODE_DIAGNOSIS"]!}</h3>
										<div class="layer_conts">
											<ul>
												<li>${LANGCODEMAP["MSG_ADCSETTING_DIAGNOSIS_FL"]!}</li>
												<li>${LANGCODEMAP["MSG_ADCSETTING_DIAGNOSIS_SL"]!}</li>
												<li>${LANGCODEMAP["MSG_ADCSETTING_DIAGNOSIS_TL"]!}</li>
											</ul>
										</div>
									</div>
								</div>
							</span>						
						</#if>
					<#else>																
						<#if (adcAdd.adc.opMode) == 2>
							<span class="mode_info mode1">
								<label for="MODE_MONITORING"><input type="radio" name="adcAdd.adc.opMode" id="MODE_MONITORING" value="1" /> ${LANGCODEMAP["MSG_ADCSETTING_MONITORING"]!}</label>
								<button type="button" class="defbtn_info monitoring_qm">${LANGCODEMAP["MSG_ADCSETTING_DESCVIEW"]!}</button>
								<div class="layer_def_wrap none" id="monitoring_notice" >
									<div class="layer_def_a2 have_tit">
										<h3 class="layer_tit">${LANGCODEMAP["MSG_ADCSETTING_MODE_MONITORING"]!}</h3>
										<div class="layer_conts">
											<ul>
												<li>${LANGCODEMAP["MSG_ADCSETTING_MONITORING_FL"]!}</li>
												<li>${LANGCODEMAP["MSG_ADCSETTING_MONITORING_SL"]!}</li>
											</ul>
										</div>
									</div>
								</div>
							</span>&nbsp;&nbsp;&nbsp;
							<span class="mode_info mode1">
								<label for="MODE_SETTING"><input type="radio" name="adcAdd.adc.opMode" id="MODE_SETTING" value="2" checked="checked" /> ${LANGCODEMAP["MSG_ADCSETTING_SETTING"]!}</label>
								<button type="button" class="defbtn_info setting_qm">${LANGCODEMAP["MSG_ADCSETTING_DESCVIEW"]!}</button>
								<div class="layer_def_wrap none" id="setting_notice">
									<div class="layer_def_a2 have_tit">
										<h3 class="layer_tit">${LANGCODEMAP["MSG_ADCSETTING_MODE_SETTING"]!}</h3>
										<div class="layer_conts">
											<ul>
												<li>${LANGCODEMAP["MSG_ADCSETTING_SETTING_FL"]!}</li>
												<li>${LANGCODEMAP["MSG_ADCSETTING_SETTING_SL"]!}</li>
											</ul>
										</div>
									</div>
								</div>
							</span>
						<#else>
							<span class="mode_info">
								<label for="MODE_MONITORING"><input type="radio" name="adcAdd.adc.opMode" id="MODE_MONITORING" value="1" checked="checked" /> ${LANGCODEMAP["MSG_ADCSETTING_MONITORING"]!}</label>
								<button type="button" class="defbtn_info monitoring_qm">${LANGCODEMAP["MSG_ADCSETTING_DESCVIEW"]!}</button>
								<div class="layer_def_wrap none" id="monitoring_notice" >
									<div class="layer_def_a2 have_tit">
										<h3 class="layer_tit">${LANGCODEMAP["MSG_ADCSETTING_MODE_MONITORING"]!}</h3>
										<div class="layer_conts">
											<ul>
												<li>${LANGCODEMAP["MSG_ADCSETTING_MONITORING_FL"]!}</li>
												<li>${LANGCODEMAP["MSG_ADCSETTING_MONITORING_SL"]!}</li>
											</ul>
										</div>
									</div>
								</div>
							</span>&nbsp;&nbsp;&nbsp;
							<!--
							<span class="mode_info mode1">
								<label for="MODE_SETTING"><input type="radio" name="adcAdd.adc.opMode" id="MODE_SETTING" value="2" /> ${LANGCODEMAP["MSG_ADCSETTING_SETTING"]!}</label>
								<button type="button" class="defbtn_info setting_qm">${LANGCODEMAP["MSG_ADCSETTING_DESCVIEW"]!}</button>
								<div class="layer_def_wrap none" id="setting_notice">
									<div class="layer_def_a2 have_tit">
										<h3 class="layer_tit">${LANGCODEMAP["MSG_ADCSETTING_MODE_SETTING"]!}</h3>
										<div class="layer_conts">
											<ul>
												<li>${LANGCODEMAP["MSG_ADCSETTING_SETTING_FL"]!}</li>
												<li>${LANGCODEMAP["MSG_ADCSETTING_SETTING_SL"]!}</li>
											</ul>
										</div>
									</div>
								</div>
							</span>
							-->
						</#if>
					</#if>			
				</td>
			</tr>
			<tr class="DivideLine">
				<td colspan="2"></td>
			</tr>
			<tr class="mgmtChk none">
				<th class="Lth2">
					<li>${LANGCODEMAP["MSG_ADCSETTING_MGMT"]!}</li>
				</th>
				<td class="Lth0">
				<#if (adcAdd.adc.mgmtMode == 1)> 
				<span>
					<input type="radio" name="adcAdd.adc.mgmtMode" id="MODE_DATA" checked="checked" value="1" />
					<label for="MODE_DATA">DATA</label>									
				</span>&nbsp;&nbsp;&nbsp;							
				<span class="mode_mgmt">
				    <input type="radio" name="adcAdd.adc.mgmtMode" id="MODE_MGMT" value="2" />
					<label for="MODE_MGMT">MGMT</label>
					<span class="txt_gray2 mgmt_notice none">${LANGCODEMAP["MSG_ADCSETTING_MGMT_CHECK"]!}
					<button type="button" class="defbtn_mgmt mgmt_qm">${LANGCODEMAP["MSG_ADCSETTING_DESCVIEW"]!}</button>
					<div class="layer_def_wrap none" id="mgmt_notice" >
						<div class="layer_def_a2 have_tit">
							<h3 class="layer_tit">${LANGCODEMAP["MSG_ADCSETTING_MGMT_MODE_CHK"]!}</h3>
							<div class="layer_conts">
								<ul>
									<li>syslog:  /cfg/sys/mmgmt/syslog mgmt</li>
									<li>tftp:    /cfg/sys/mmgmt/tftp mgmt</li>								
								</ul>
							</div>
						</div>
					</div>					
				${LANGCODEMAP["MSG_ADCSETTING_MGMT_CONFIRM"]!}</span>
				</span>			
				<#else>		
				<span>					
					<input type="radio" name="adcAdd.adc.mgmtMode" id="MODE_DATA" value="1" />
					<label for="MODE_DATA">DATA</label>
				</span>&nbsp;&nbsp;&nbsp;							
				<span class="mode_mgmt">
				    <input type="radio" name="adcAdd.adc.mgmtMode" id="MODE_MGMT" checked="checked" value="2" />
					<label for="MODE_MGMT">MGMT</label>
					<span class="txt_gray2 mgmt_notice none">${LANGCODEMAP["MSG_ADCSETTING_MGMT_CHECK"]!}
					<button type="button" class="defbtn_mgmt mgmt_qm">${LANGCODEMAP["MSG_ADCSETTING_DESCVIEW"]!}</button>
					<div class="layer_def_wrap none" id="mgmt_notice" >
						<div class="layer_def_a2 have_tit">
							<h3 class="layer_tit">${LANGCODEMAP["MSG_ADCSETTING_MGMT_MODE_CHK"]!}</h3>
							<div class="layer_conts">
								<ul>
									<li>syslog:  /cfg/sys/mmgmt/syslog mgmt</li>
									<li>tftp:    /cfg/sys/mmgmt/tftp mgmt</li>							
								</ul>
							</div>
						</div>
					</div>					
				${LANGCODEMAP["MSG_ADCSETTING_MGMT_CONFIRM"]!}</span>
				</span>							
				</#if>
				</td>		
			</tr>
			<tr class="DivideLine mgmtChk none">
				<td colspan="2"></td>
			</tr>
			<tr>
				<th class="Lth2">
					<li>${LANGCODEMAP["MSG_ADCSETTING_IP_ADDRESS"]!}</li>
				</th>
				<td class="Lth0">
					<input name="adcAdd.adc.ip" disabled="disabled" type="text" id="textfield" class="inputText width130" value="${(adcAdd.adc.ip)!}">
					<input name="adcAdd.adc.ip" type="hidden" value="${(adcAdd.adc.ip)!}"/>
				</td>
			</tr>
			<tr class="DivideLine">
				<td colspan="2"></td>
			</tr>
			<#if ((adcAdd.adc.type) == "Alteon") || ((adcAdd.adc.type) == 'F5')>
				<tr>
					<th class="Lth1">
						<li>${LANGCODEMAP["MSG_ADCSETTING_PEERIP_ADDRESS"]!}</li>
					</th>
					<#if accountRole == 'readOnly'>
						<td class="Lth0">
							<input name="adcAdd.adc.peerip" type="text" id="textfield" class="inputText width130" value="${(adcAdd.adc.peerip)!}" disabled="disabled">
						</td>
					<#else>
						<td class="Lth0">
							<input name="adcAdd.adc.peerip" type="text" id="textfield" class="inputText width130" value="${(adcAdd.adc.peerip)!}">
						</td>
					</#if>
				</tr>
				<tr class="DivideLine">
					<td colspan="2"></td>
				</tr>
			</#if>
			<tr>
				<th class="Lth2">
					<li>프로토콜</li>
				</th>				
				<td>
					<table class="Board100" cellpadding="0" cellspacing="0">				
						<tr>
							<td class="Lth0">
								<select name="adcAdd.adc.connProtocol" class="protocolChange">
									<#if ((adcAdd.adc.connProtocol)!"") == 0>
										<option value="0" selected="selected">TCP</option>							
									<#else>
										<option value="0">TCP</option>
									</#if>
									<#if ((adcAdd.adc.connProtocol)!"") == 1>
										<option value="1" selected="selected">ICMP</option>							
									<#else>
										<option value="1">ICMP</option>
									</#if>						
								</select>					
							</td> &nbsp;&nbsp;&nbsp;
							<td class="width50 protocolCheck none">				
								<span class="networkDefault default none">
									<button type="button" class="Btn_white_small defaultConn"><span class="none status">1</span>${LANGCODEMAP["MSG_ADCSETTING_CONNECT"]!}</button>
								</span>				         				
								<span class="networkConfirm confirm">
									<button type="button" class="Btn_black_small testConnectionNetworkLnk"><span class="none status">1</span>${LANGCODEMAP["MSG_ADCSETTING_CONNECT"]!}</button>
								</span>
								<span class="networkOk none">
									<button type="button" class="Btn_green_small testConnectionNetworkLnk"><span class="none status">1</span>${LANGCODEMAP["MSG_ADCSETTING_CONNECTSUCCESS"]!}</button>
								</span>
								<span class="networkFail none">
									<button type="button" class="Btn_red_small testConnectionNetworkLnk"><span class="none status">1</span>${LANGCODEMAP["MSG_ADCSETTING_CONNECTFAIL"]!}</button>
								</span>
							</td>
							<td>
								<span class="conn_statNetwork noConn"></span>		
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr class="DivideLine">
				<td colspan="2"></td>
			</tr>	
			<tr class="connPortView">	
				<th class="Lth2">
					<li>${LANGCODEMAP["MSG_ADCSETTING_COMMUNICATION_PORT"]!}</li>
				</th>
				<td class="Lth0 connServiceSelect">
					<table class="Board100" cellpadding="0" cellspacing="0">
						<tr>
							<#if (adcAdd.adc.type) == 'F5'>							
								<td class="width110">
									<input type="radio" name="adcAdd.adc.connService" id="sslSelect" checked="checked" value="${(adcAdd.adc.connService)!'22'}" />
									<label for="" class="">SSH</label>
									<#if accountRole == 'readOnly'>
								    	<input type="text" name="adcAdd.adc.connPort"  id="ssl" class="inputText width35" value="${(adcAdd.adc.connPort)!'22'}" disabled="disabled"/>
								    <#else>
								    	<input type="text" name="adcAdd.adc.connPort"  id="ssl" class="inputText width35" value="${(adcAdd.adc.connPort)!'22'}" />
								    </#if>		
								</td>	
							<#else>
								<#if (adcAdd.adc.connService) == 23>
									<td class="telnetConnService width110">
										<#if accountRole == 'readOnly'>
											<input type="radio" name="adcAdd.adc.connService" id="telnetSelect" checked="checked" value="23" disabled="disabled"/>
											<label for="" class="">Telnet</label>
									    	<input type="text" name="adcAdd.adc.connPort"  id="telnet" class="inputText width35" value="${(adcAdd.adc.connPort)!'23'}" disabled="disabled"/>
									    <#else>
									    	<input type="radio" name="adcAdd.adc.connService" id="telnetSelect" checked="checked" value="23" />
											<label for="" class="">Telnet</label>
									    	<input type="text" name="adcAdd.adc.connPort"  id="telnet" class="inputText width35" value="${(adcAdd.adc.connPort)!'23'}" />
									    </#if>
									</td>		
									<td class="sslConnService width100">
										<#if accountRole == 'readOnly'>
											<input type="radio" name="adcAdd.adc.connService" id="sslSelect" value="22" disabled="disabled"/>
											<label for="" class="">SSH</label> 									
										    <input type="text" name="adcAdd.adc.connPort"  id="ssl" class="inputText width35" value="22" disabled="disabled" />
										<#else>
											<input type="radio" name="adcAdd.adc.connService" id="sslSelect" value="22" />
											<label for="" class="">SSH</label> 									
										    <input type="text" name="adcAdd.adc.connPort"  id="ssl" class="inputText width35" value="22" disabled="disabled" />
										</#if>				
									</td>
								
								<#else>								
									<td class="telnetConnService width110">
										<#if accountRole == 'readOnly'>
											<input type="radio" name="adcAdd.adc.connService" id="telnetSelect" value="23" disabled="disabled"/>
											<label for="" class="">Telnet</label>
										    <input type="text" name="adcAdd.adc.connPort"  id="telnet" class="inputText width35" value="23" disabled="disabled" />
									    <#else>
										    <input type="radio" name="adcAdd.adc.connService" id="telnetSelect" value="23" />
											<label for="" class="">Telnet</label>
										    <input type="text" name="adcAdd.adc.connPort"  id="telnet" class="inputText width35" value="23" disabled="disabled" />
									    </#if>
									</td>		
									<td class="sslConnService width100">
										<#if accountRole == 'readOnly'>
											<input type="radio" name="adcAdd.adc.connService" id="sslSelect" checked="checked" value="22"  disabled="disabled"/>
											<label for="" class="">SSH</label> 
									    	<input type="text" name="adcAdd.adc.connPort"  id="ssl" class="inputText width35" value="${(adcAdd.adc.connPort)!'22'}" disabled="disabled"/>
									    <#else>
									    	<input type="radio" name="adcAdd.adc.connService" id="sslSelect" checked="checked" value="22" />
											<label for="" class="">SSH</label> 
									    	<input type="text" name="adcAdd.adc.connPort"  id="ssl" class="inputText width35" value="${(adcAdd.adc.connPort)!'22'}" />
									    </#if>			
									</td>
								</#if>
							</#if>		
							<td class="width50">	
								<span class="networkDefault default none">
									<button type="button" class="Btn_white_small defaultConn"><span class="none status">1</span>${LANGCODEMAP["MSG_ADCSETTING_CONNECT"]!}</button>
								</span>				         				
								<span class="networkConfirm confirm">
									<button type="button" class="Btn_black_small testConnectionNetworkLnk"><span class="none status">1</span>${LANGCODEMAP["MSG_ADCSETTING_CONNECT"]!}</button>
								</span>
								<span class="networkOk none">
									<button type="button" class="Btn_green_small testConnectionNetworkLnk"><span class="none status">1</span>${LANGCODEMAP["MSG_ADCSETTING_CONNECTSUCCESS"]!}</button>
								</span>
								<span class="networkFail none">
									<button type="button" class="Btn_red_small testConnectionNetworkLnk"><span class="none status">1</span>${LANGCODEMAP["MSG_ADCSETTING_CONNECTFAIL"]!}</button>
								</span>
							</td>
							<td>
								<span class="conn_statNetwork noConn"></span>		
							</td>
						</tr>
					</table>							
				</td>
			</tr>
			<tr class="EndLine2">
				<td colspan="2"></td>
			</tr>
		</table>
		<div class="title_h4_1">
			<li>${LANGCODEMAP["MSG_ADCSETTING_SECONDLEVEL"]!}</li>
		</div>
		<table class="Board" cellpadding="0" cellspacing="0">
			<colgroup>
				<col width="200px" />
				<col >
			</colgroup>	
			<tr class="StartLine">
				<td colspan="2" ></td>
			</tr>			
			<tr>
				<th class="Lth2 loginField">
					<li>${LANGCODEMAP["MSG_ADCSETTING_ID"]!}</li>
				</th>
				<td class="Lth0">
					<#if accountRole == 'readOnly'>
						<input name="adcAdd.adc.accountId" type="text" id="textfield" class="inputText width130" value="${(adcAdd.adc.accountId)!''}" disabled="disabled"/>
					<#else>
						<input name="adcAdd.adc.accountId" type="text" id="textfield" class="inputText width130" value="${(adcAdd.adc.accountId)!''}" />
					</#if>
				</td>
			</tr>
			<tr class="DivideLine">
				<td colspan="2"></td>
			</tr>
			<tr>
				<th class="Lth2 loginField">
					<li>${LANGCODEMAP["MSG_ADCSETTING_PASSWORD"]!}</li>
				</th>
				<td class="Lth0">
					<table class="Board100" cellpadding="0" cellspacing="0">
						<tr>
							<td>
								<input name="fakeIdField" type="text" style="display:none;">
								<input name="fakePwField" type="password" style="display:none;">
								<#if accountRole == 'readOnly'>			
				           			<input name="adcAdd.adc.password" type="password" id="textfield" class="inputText width130" value="${(adcAdd.adc.password)!}" disabled="disabled" autocomplete="off"/>
				           		<#else>	
				           			<input name="adcAdd.adc.password" type="password" id="textfield" class="inputText width130" value="${(adcAdd.adc.password)!}" autocomplete="off"/>
				           		</#if>
				            </td>		
								<span class="loginDefault none">
									<button type="button" class="Btn_white_small defaultConn"><span class="none status">2</span>${LANGCODEMAP["MSG_ADCSETTING_CONNECT"]!}</button>
								</span>
							<td style="padding-left:10px">
								<span class="loginConfirm none">
									<button type="button" class="Btn_black_small testConnectionLoginLnk"><span class="none status">2</span>${LANGCODEMAP["MSG_ADCSETTING_CONNECT"]!}</button>
								</span>
								<span class="loginOk none">
									<button type="button" class="Btn_green_small testConnectionLoginLnk"><span class="none status">2</span>${LANGCODEMAP["MSG_ADCSETTING_CONNECTSUCCESS"]!}</button>
								</span>
								<span class="loginFail none">
									<button type="button" class="Btn_red_small testConnectionLoginLnk"><span class="none status">2</span>${LANGCODEMAP["MSG_ADCSETTING_CONNECTFAIL"]!}</button>
								</span>
								</td>
							<td style="padding-left:10px">
								<span class="conn_statLogin noConn"></span>
							</td>
						</tr>
					</table>
				</td>				
			</tr>
			<#if ((adcAdd.adc.type) == "F5")>
			<tr class="DivideLine">
			<#else>
			<tr class="EndLine2">	
			</#if>
				<td colspan="2"></td>
			</tr>
			
			<#if ((adcAdd.adc.type) == "F5")>
			<tr>
				<th class="Lth2">
					<li>${LANGCODEMAP["MSG_ADCSETTING_CLIID"]!}</li>
				</th>
				<td class="Lth0">
					<#if accountRole == 'readOnly'>	
						<input name="adcAdd.adc.cliAccountId" type="text" id="textfield" class="inputText width130" value="${(adcAdd.adc.cliAccountId)!''}" disabled="disabled"/>
					<#else>
						<input name="adcAdd.adc.cliAccountId" type="text" id="textfield" class="inputText width130" value="${(adcAdd.adc.cliAccountId)!''}" />
					</#if>
				</td>		
			</tr>
			<tr class="DivideLine">
				<td colspan="2"></td>
			</tr>
			<tr>
				<th class="Lth2">
					<li>${LANGCODEMAP["MSG_ADCSETTING_CLIPASSWORD"]!}</li>
				</th>
				<td class="Lth0">
	           		<table class="Board100" cellpadding="0" cellspacing="0">
						<tr>
							<td>
								<input name="fakeIdField" type="text" style="display:none;">
								<input name="fakePwField" type="password" style="display:none;">
								<#if accountRole == 'readOnly'>			
					            	<input name="adcAdd.adc.cliPassword" type="password" id="textfield" class="inputText width130" value="${(adcAdd.adc.cliPassword)!}" disabled="disabled" autocomplete="off"/>
					            <#else>
					            	<input name="adcAdd.adc.cliPassword" type="password" id="textfield" class="inputText width130" value="${(adcAdd.adc.cliPassword)!}" autocomplete="off"/>
					            </#if>
							</td>
							<td style="padding-left:10px">            						
								<span class="loginF5Default none">
									<button type="button" class="Btn_white_small defaultConn"><span class="none status">2</span>${LANGCODEMAP["MSG_ADCSETTING_CONNECT"]!}</button>
								</span>
								<span class="loginF5Confirm">
									<button type="button" class="Btn_black_small testConnectionLoginF5Lnk"><span class="none status">2</span>${LANGCODEMAP["MSG_ADCSETTING_CONNECT"]!}</button>
								</span>
								<span class="loginF5Ok none">
									<button type="button" class="Btn_green_small testConnectionLoginF5Lnk"><span class="none status">2</span>${LANGCODEMAP["MSG_ADCSETTING_CONNECTSUCCESS"]!}</button>
								</span>
								<span class="loginF5Fail none">
									<button type="button" class="Btn_red_small testConnectionLoginF5Lnk"><span class="none status">2</span>${LANGCODEMAP["MSG_ADCSETTING_CONNECTFAIL"]!}</button>
								</span>
							</td>
							<td style="padding-left:10px">
								<span class="conn_statLogin_F5 noConn"></span>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr class="EndLine2">
				<td colspan="2"></td>
			</tr>
			</#if>	
		</table>
		
		<div class="title_h4_1">
			<li>${LANGCODEMAP["MSG_ADCSETTING_THIRDLEVEL"]!}</li>
		</div>
		<table class="Board" cellpadding="0" cellspacing="0">
			<colgroup>
				<col width="200px" />
				<col >
			</colgroup>	
			<tr class="StartLine">
				<td colspan="2" ></td>
			</tr>
			<tr>
				<th class="Lth2">
					<li>${LANGCODEMAP["MSG_ADCSETTING_SNMPVERSION"]}</li>
				</th>
				<td class="Lth0">
				<#if ((adcAdd.adc.type)!"") == "Alteon">
					<#if ((adcAdd.adc.adcSnmpInfo.version)!"") == 3>
						<input type="radio" name="adcAdd.adc.adcSnmpInfo.version" id="v2" value="2" />
							<label for="v2Snmp">v2</label>&nbsp;&nbsp;&nbsp;
						<input type="radio" name="adcAdd.adc.adcSnmpInfo.version" id="v3" checked="checked" value="3" />
							<label for="v3Snmp">v3</label>					
					<#else>
						<input type="radio" name="adcAdd.adc.adcSnmpInfo.version" id="v2" checked="checked" value="2" />
							<label for="v2Snmp">v2</label>&nbsp;&nbsp;&nbsp;
						<input type="radio" name="adcAdd.adc.adcSnmpInfo.version" id="v3" value="3" />
							<label for="v3Snmp">v3</label>
					</#if>
				<#else>
					<input type="radio" name="adcAdd.adc.adcSnmpInfo.version" id="v2" checked="checked" value="2" />
							<label for="v2Snmp">v2</label>&nbsp;&nbsp;&nbsp;
						<input type="radio" name="adcAdd.adc.adcSnmpInfo.version" id="v3" value="3" disabled />
							<label for="v3Snmp">v3</label>
				</#if>
				</td>
			</tr>	
			<tr class="DivideLine snmpv2Version">
				<td colspan="2"></td>
			</tr>	 		
			<tr class="snmpv2Version">
				<th class="Lth2">
					<li>${LANGCODEMAP["MSG_ADCSETTING_SNMPCOMMUNITY"]}</li>
				</th>
				<td class="Lth0">
					<table class="Board100" cellpadding="0" cellspacing="0">
						<tr>
							<td>
								<#if accountRole == 'readOnly'>	
									<input type="text" name="adcAdd.adc.adcSnmpInfo.rcomm" id="textfield3" class="inputText width130" value="${(adcAdd.adc.adcSnmpInfo.rcomm)!}" disabled="disabled"/>
								<#else>
									<input type="text" name="adcAdd.adc.adcSnmpInfo.rcomm" id="textfield3" class="inputText width130" value="${(adcAdd.adc.adcSnmpInfo.rcomm)!}" />
								</#if>
							</td>
							<td style="padding-left:10px">   						
								<span class="snmpDefault none">
									<button type="button" class="Btn_white_small defaultConn"><span class="none status">5</span>${LANGCODEMAP["MSG_ADCSETTING_CONNECT"]!}</button>
								</span>
								<span class="snmpConfirm">
									<button type="button" class="Btn_black_small testConnectionSnmpLnk"><span class="none status">5</span>${LANGCODEMAP["MSG_ADCSETTING_CONNECT"]!}</button>
								</span>
								<span class="snmpOk none">
									<button type="button" class="Btn_green_small testConnectionSnmpLnk"><span class="none status">5</span>${LANGCODEMAP["MSG_ADCSETTING_CONNECTSUCCESS"]!}</button>
								</span>
								<span class="snmpFail none">
									<button type="button" class="Btn_red_small testConnectionSnmpLnk"><span class="none status">5</span>${LANGCODEMAP["MSG_ADCSETTING_CONNECTFAIL"]!}</button>
								</span>
							</td>
							<td style="padding-left:10px">
								<span class="conn_statSnmp noConn"></span>
							</td>
						</tr>
					</table>	
				</td>
			</tr>
			<tr class="DivideLine snmpv3Version none">
				<td colspan="2"></td>
			</tr>
			<tr class="snmpv3Version none">
				<th class="Lth2">
					<li>${LANGCODEMAP["MSG_ADCSETTING_SNMPUSER"]}</li>
				</th>
				<td class="Lth0">
					<input type="text" name="adcAdd.adc.adcSnmpInfo.securityName" id="textfield3" class="inputText width130" value="${(adcAdd.adc.adcSnmpInfo.securityName)!}" />
				</td>
			</tr>
			<tr class="DivideLine snmpv3Version none">
				<td colspan="2"></td>
			</tr>
			<tr class="snmpv3Version none">
				<th class="Lth1">
					<li>${LANGCODEMAP["MSG_ADCSETTING_SNMPAUTHPW"]}</li>
				</th>
				<td class="Lth0">
					<input name="fakeIdField" type="text" style="display:none;">
					<input name="fakePwField" type="password" style="display:none;">
					<input type="password" name="adcAdd.adc.adcSnmpInfo.authPassword" id="textfield3" class="inputText width130" value="${(adcAdd.adc.adcSnmpInfo.authPassword)!}" autocomplete="off"/>
					<select class="authProtocolCbx" style="width:110px;" name="adcAdd.adc.adcSnmpInfo.authProto">
						<#if (adcAdd.adc.adcSnmpInfo.authProto)! == "md5">
							<option selected="selected" value="md5">md5</option>
						<#else>
							<option value="md5">md5</option>
						</#if>
						<#if (adcAdd.adc.adcSnmpInfo.authProto)! == "sha">
							<option selected="selected" value="sha">sha</option>
						<#else>
							<option value="sha">sha</option>
						</#if>							
					</select>
				</td>
			</tr>
			<tr class="DivideLine snmpv3Version none">
				<td colspan="2"></td>
			</tr>
			<tr class="snmpv3Version none">
				<th class="Lth1">
					<li>${LANGCODEMAP["MSG_ADCSETTING_SNMPPRIVPW"]}</li>
				</th>
				<td class="Lth0">
					<table class="Board100" cellpadding="0" cellspacing="0">
						<tr>
							<td>
								<input name="fakeIdField" type="text" style="display:none;">
								<input name="fakePwField" type="password" style="display:none;">
								<input type="password" name="adcAdd.adc.adcSnmpInfo.privPassword" id="textfield3" class="inputText width130" value="${(adcAdd.adc.adcSnmpInfo.privPassword)!}" autocomplete="off"/>
							</td>
							<td style="padding-left:10px">							
								<span class="snmpDefault none">
									<button type="button" class="Btn_white_small defaultConn"><span class="none status">5</span>${LANGCODEMAP["MSG_ADCSETTING_CONNECT"]!}</button>
								</span>
								<span class="snmpConfirm">
									<button type="button" class="Btn_black_small testConnectionSnmpLnk"><span class="none status">5</span>${LANGCODEMAP["MSG_ADCSETTING_CONNECT"]!}</button>
								</span>
								<span class="snmpOk none">
									<button type="button" class="Btn_green_small testConnectionSnmpLnk"><span class="none status">5</span>${LANGCODEMAP["MSG_ADCSETTING_CONNECTSUCCESS"]!}</button>
								</span>
								<span class="snmpFail none">
									<button type="button" class="Btn_red_small testConnectionSnmpLnk"><span class="none status">5</span>${LANGCODEMAP["MSG_ADCSETTING_CONNECTFAIL"]!}</button>
								</span>
							</td>
							<td style="padding-left:10px">
								<span class="conn_statSnmp noConn"></span>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr class="EndLine2">
				<td colspan="2"></td>
			</tr>
		</table>
		
		<div class="title_h4_1">
			<li>${LANGCODEMAP["MSG_ADCSETTING_FORTHLEVEL"]!}</li>
		</div>
		<table class="Board" cellpadding="0" cellspacing="0">
			<colgroup>
				<col width="200px" />
				<col >
			</colgroup>	
			<tr class="StartLine">
				<td colspan="2" ></td>
			</tr>
			<tr>
			<th class="Lth1">
				<li>${LANGCODEMAP["MSG_ADCSETTING_SYSLOGIP"]}</li>
			</th>
				<td class="Lth0">
					<table class="Board100" cellpadding="0" cellspacing="0">
				<colgroup>		
					<col width="310px">				
					<col width="50px">
					<col width="auto">
				 </colgroup>					
						<tr>
							<td>			
								<input name="adcAdd.adc.ip" disabled="disabled" type="text" id="textfield" class="inputText width130" value="${(adcAdd.adc.ip)!}">
								<#if accountRole == 'readOnly'>						
									<input type="text" name="adcAdd.adc.syslogip" id="textfield3" class="inputText width130" value="${(adcAdd.adc.syslogip)!}" disabled="disabled"/>
								<#else>
									<input type="text" name="adcAdd.adc.syslogip" id="textfield3" class="inputText width130" value="${(adcAdd.adc.syslogip)!}" />
								</#if>
							</td>
							<td style="padding-left:10px">   	
								<span class="syslogDefault none">
									<button type="button" class="Btn_white_small defaultConn"><span class="none status">6</span>${LANGCODEMAP["MSG_ADCSETTING_CONNECT"]!}</button>
								</span>
								<span class="syslogConfirm">
									<button type="button" class="Btn_black_small testConnectionSyslogLnk"><span class="none status">6</span>${LANGCODEMAP["MSG_ADCSETTING_CONNECT"]!}</button>
								</span>
								<span class="syslogOk none">
									<button type="button" class="Btn_green_small testConnectionSyslogLnk"><span class="none status">6</span>${LANGCODEMAP["MSG_ADCSETTING_CONNECTSUCCESS"]!}</button>
								</span>
								<span class="syslogFail none">
									<button type="button" class="Btn_red_small testConnectionSyslogLnk"><span class="none status">6</span>${LANGCODEMAP["MSG_ADCSETTING_CONNECTFAIL"]!}</button>
								</span>
							</td>
							<td style="padding-left:10px">
								<span class="conn_statSyslog noConn"></span>
							</td>	
						</tr>
					</table>
				</td>
			</tr>
			<tr class="EndLine2">
				<td colspan="2"></td>
			</tr>
		</table>
		
		<div class="title_h4_1">
			<li>${LANGCODEMAP["MSG_ADCSETTING_FIFTHLEVEL"]!}</li>
		</div>
		<table class="Board" cellpadding="0" cellspacing="0">
			<colgroup>
				<col width="200px" />
				<col >
			</colgroup>	
			<tr class="StartLine">
				<td colspan="2" ></td>
			</tr>
			<tr>
				<th class="Lth1">
					<li>${LANGCODEMAP["MSG_ADCSETTING_ADCVERSION"]}</li>
				</th>
				<td class="Lth0">
					<#if !(addAdc.adc.index)??>
					<input type="text" name="adcAdd.adc.version" class="inputText width130" value="${(adcAdd.adc.version)!''}" disabled="disabled" />
					<#else>
					<span class="conn_statVersion"></span>
					</#if>
				</td>
			</tr>
			<tr class="DivideLine">
				<td colspan="2"></td>
			</tr>
			<tr>
				<th rowspan="2" class="Lth2">
					<li>${LANGCODEMAP["MSG_ADCSETTING_GROUP"]!}</li>
				</th>
				<td class="Lth0">
					<select name="adcAdd.adc.groupIndex" size="5" multiple="multiple" class="adclist group_list" id="textarea" cols="45" rows="5"> 
					<#list adcGroups![] as theAdcGroup> 
						<#if theAdcGroup.index == (adcAdd.adc.groupIndex)!0>
							<option value="${theAdcGroup.index}" selected="selected">${theAdcGroup.name}</option>
						<#else>
							<option value="${theAdcGroup.index}">${theAdcGroup.name}</option>
						</#if> 
					</#list>
					</select>
				</td>
			</tr>
			<tr>
				<td class="Lth0">
					<#if accountRole == 'system'>
						    <input type="button" id="adcGroupMgmtLnk"  class="v_btm Btn_black_small" value="${LANGCODEMAP["MSG_ADCSETTING_GROUP_ADMIN"]!}"/> 
					</#if>
				</td>
			</tr>
			<tr class="DivideLine">
				<td colspan="2"></td>
			</tr>
			<tr class="ssoChk">
				<th class="Lth1">
					<li>${LANGCODEMAP["MSG_ADCSETTING_EXTRAAUTH"]!}</li>
				</th>
				<td class="Lth0">
					<#if (adcAdd.adc.ssoModeType)!true>
						<input type="checkbox" name="adcAdd.adc.ssoModeType" id="useSso" checked="checked" value="true" />
					<#else>
						<input type="checkbox" name="adcAdd.adc.ssoModeType" id="useSso" value="true" />
					</#if>
						<label for="useSso">${LANGCODEMAP["MSG_SYSSETTING_USE"]!}</label>						
				</td>			
			</tr> 
			<tr class="DivideLine ssoChk">
				<td colspan="2"></td>
			</tr>			
			<!--
			<tr>
				<th class="Lth1">
					<li>${LANGCODEMAP["MSG_ADCSETTING_TEST_CCONNECT"]!}</li>
				</th>
				<td class="Lth0">
					<a id="testConnectionLnk" href="#">
						<span class="button black small ">
							<button type="button">${LANGCODEMAP["MSG_ADCSETTING_TEST_CCONNECT"]!}</button>
						</span>
					</a>
					<span class="conn_stat"></span>
				</td>
			</tr>
			-->
			<tr>
				<th class="Lth1">
					<li>${LANGCODEMAP["MSG_ADCSETTING_BUYING_DATE"]!}</li>
				</th>
				<td class="Lth0">
					<#if accountRole == 'readOnly'>
						<input type="text" id="startTimeDate" class="inputText width130" value="${(adcAdd.adc.purchaseDate?string("yyyy-MM-dd"))!}" title="${LANGCODEMAP["MSG_ADCSETTING_RESERVE_DATA_START_TIME"]!}" disabled="disabled"/>
					<#else>	
						<input type="text" name="adcAdd.adc.purchaseDate" id="startTimeDate" class="inputText width130" value="${(adcAdd.adc.purchaseDate?string("yyyy-MM-dd"))!}" title="${LANGCODEMAP["MSG_ADCSETTING_RESERVE_DATA_START_TIME"]!}" />
					</#if>
				</td>
			<tr class="DivideLine">
				<td colspan="2"></td>
			</tr>
			<tr>
				<th class="Lth1">
					<li>${LANGCODEMAP["MSG_ADCSETTING_EXPLAIN"]!}</li>
				</th>
				<td class="Lth0">
					<#if accountRole == 'readOnly'>
						<textarea disabled="disabled" name="adcAdd.adc.description" class="desc" id="textfield4">${(adcAdd.adc.description)!}</textarea>
					<#else>	
						<textarea name="adcAdd.adc.description" class="desc" id="textfield4">${(adcAdd.adc.description)!}</textarea>
					</#if>
					<!--<input name="adcAdd.adc.description" class="desc" id="textfield4" value="${(adcAdd.adc.description)!}">-->					
				</td>
			</tr>	
			<tr class="DivideLine">
				<td colspan="2"></td>
			</tr>
			<tr>
				<th class="Lth1">
					<li>${LANGCODEMAP["MSG_ADCSETTING_SELECT_USER"]!}</li>
				</th>
				<td class="Lth0 align_top" >
					<table width="100%" border="0" cellpadding="0" cellspacing="0">
						<colgroup>
							<col width="48%" />
							<col width="4%" >
							<col width="48%" />
						</colgroup>
						<tr> 
						  	<td>
						  		<span class="usrselected_th">${LANGCODEMAP["MSG_ADCSETTING_SELECT"]!}</span><span class="usrselected_txt"><span class="selectedAccountsCount txt_blue">${adcCount}</span>&nbsp;${LANGCODEMAP["MSG_ADCSETTING_SELECTED_COUNT"]!}</span>
						  	</td>
							<td></td>
							<td>
								<span class="userlist_txt">${LANGCODEMAP["MSG_ADCSETTING_LIST"]!}</span>
							</td>
						</tr>
						<tr>			
							<td>											
					        	<select name="adcAdd.accountIds" size="6" multiple="multiple" class="usrselecte" id="accountsSelectedSel">
								<#list registeredAccounts![] as account>
									<option value="${account.id}">${account.id}</option>
								</#list>
						    	</select>
							</td>
							<td>
								<div class="position_arrow">
									<a class="toAccountsSelectionLnk" href="#">
										<img src="imgs/meun/btn_mov_lft.png" alt="${LANGCODEMAP["MSG_ADCSETTING_MOVE_SELECTED"]!}" />
									</a> 
								</div>
								<div class="position_arrow">	
									<a class="toAccountsDeselectionLnk" href="#">
										<img src="imgs/meun/btn_mov_rgt.png" alt="${LANGCODEMAP["MSG_ADCSETTING_MOVE_SELECTED"]!}" />
									</a>
								</div>
							</td>
							<td> 	
			     				<span class="inputTextposition1">
									<input name="textfield3" type="text"  class="inputText_search"  id="textfield3" />
								</span>
								<span class="btn1">
									<a href="#" class="accountSearchLnk">
										<img src="imgs/meun/btn_search_1.png" alt="${LANGCODEMAP["MSG_ADCSETTING_SEARCH"]!}" />
									</a>
								</span>
								<br class="clearfix" />
								<select name="textarea2" size="5" multiple="multiple" class="userlist" id="accountsDeselectedSel">
									<#list availableAccounts![] as account>
										<option value="${account.id}">${account.id}</option> 
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
		</table>
		
		<!-- 3 -->
		<div class="alteonSet">
			<div class="title_h4_1">
				<li>${LANGCODEMAP["MSG_ADCSETTING_CONTINUOUS_SET_INFO"]!}</li>
			</div>
			<div class="position_L20">
				${LANGCODEMAP["MSG_ADCSETTING_CONTINUOUS_NOTICE"]!} </br>${LANGCODEMAP["MSG_ADCSETTING_SET_IMPOSSIBLE_NOTICE"]!}
			</div>
			<table class="Board" cellpadding="0" cellspacing="0">
				<colgroup>
					<col width="200px" />
					<col >
					<col />
				</colgroup>			
				<tr class="StartLine">
					<td colspan="3"></td>
				</tr>
				<tbody>
				<#list vrrpInfos as theVrrpInfos>
				<tr>
					<th class="Lth2">
						<li>Peer ADC IP</li>
					</th>
					<td class="Lth0">
						<#if (theVrrpInfos.peerIP)! != "" >
							<label>${(theVrrpInfos.peerIP)!}</label>
						<#else>
							<label>${LANGCODEMAP["MSG_ADCSETTING_SETTING_NONE"]!}</label>
						</#if>
					</td>
					<td class="Lth0"></td>
				</tr>
				<tr class="DivideLine">
					<td colspan="3"></td>
				</tr>
				<tr>
					<th class="Lth2">
						<li>${LANGCODEMAP["MSG_ADCSETTING_SETTING_TRACKING"]!}</li>
					</th>
					<td class="Lth0">
					<#if (theVrrpInfos.trackPorts)==1>
						<input type="checkbox" checked="checked"  disabled="disabled"/> Physical Ports <br/>
					<#else>
						<input type="checkbox" disabled="disabled"/> Physical Ports <br/>
					</#if>
					
					<#if (theVrrpInfos.trackInt)==1>
						<input type="checkbox" checked="checked"  disabled="disabled" /> IP Interface <br/>
					<#else>
						<input type="checkbox" disabled="disabled" /> IP Interface <br/>
					</#if>
					
					<#if (theVrrpInfos.trackL4pts)==1>
						<input type="checkbox" checked="checked"  disabled="disabled"/> SLB Ports <br/>
					<#else>
						<input type="checkbox" disabled="disabled"/> SLB Ports <br/>
					</#if>
					
					<#if (theVrrpInfos.trackReals)==1>
						<input type="checkbox" checked="checked" disabled="disabled"/> Real Server <br/>
					<#else>
						<input type="checkbox" disabled="disabled"/> Real Server <br/>
					</#if>
					</td>
					<td class="Lth0">
					<#if (theVrrpInfos.trackHsrp)==1>
						<input type="checkbox" checked="checked" disabled="disabled"/> HSRP <br/>
					<#else>
						<input type="checkbox" disabled="disabled"/> HSRP <br/>
					</#if>
					
					<#if (theVrrpInfos.trackHsrv)==1>
						<input type="checkbox" checked="checked" disabled="disabled"/> HSRP with VLAN <br/>
					<#else>
						<input type="checkbox" disabled="disabled"/> HSRP with VLAN <br/>
					</#if>
					
					<#if (theVrrpInfos.trackVrs)==1>
						<input type="checkbox" checked="checked" disabled="disabled"/> Virtual Server <br/>
					<#else>
						<input type="checkbox" disabled="disabled"/> Virtual Server <br/>
					</#if>							
					</td>
				</tr>
				<tr class="DivideLine">
					<td colspan="3"></td>
				</tr>
				<tr>
					<th class="Lth2">
						<li>${LANGCODEMAP["MSG_ADCSETTING_SETTING_SHARING"]!}</li>
					</th>
					<td class="Lth0" colspan="2">
					<#if (theVrrpInfos.sharing)==1>
						<lable>enabled</label>
					<#else>
						<lable>disabled</label>
					</#if>
					</td>
				</tr>
				<tr class="DivideLine">
					<td colspan="3"></td>
				</tr>
				<tr>
					<th class="Lth2">
						<li>${LANGCODEMAP["MSG_ADCSETTING_SETTING_PRIORITY"]!}</li>
					</th>
					<td class="Lth0" colspan="2">
						<label>${(theVrrpInfos.priority)!}</label>
					</td>
				</tr>
				</#list>
				</tbody>
				<tr class="EndLine2">
					<td colspan="3"></td>
				</tr>
				</table>
			</div>
			<table class="Board" cellpadding="0" cellspacing="0">
			<tr>
				<td colspan="4">
					<div class="position_cT10">
						<#if accountRole == 'system'>
						    <input type="button" id="adcAddOkBtn" class="Btn_red" value="${LANGCODEMAP["MSG_ADCSETTING_COMPLETE"]!}"/>  
						</#if>
						<input type="button" id="adcAddCancelBtn" class="Btn_white" value="${LANGCODEMAP["MSG_ADCSETTING_CANCEL"]!}"/> 
					</div>
				</td>
			</tr>
		</table>
	</form>
</div>

<!-- 그룹 관리 팝업 -->
<div id="groupMgmtWnd" class="pop_type_wrap">
	<h2>${LANGCODEMAP["MSG_ADCSETTING_ADC_GROUP_ADMIN"]!}</h2>
	<div class="pop_contents">
		<div class="group_add">
			<p>
				<label>${LANGCODEMAP["MSG_ADCSETTING_GROUP_NAME"]!}</label><input name="adcGroup.name" type="text" class="g_name" />
			</p>
			<p>
				<label>${LANGCODEMAP["MSG_ADCSETTING_EXPLAIN"]!}</label><input name="adcGroup.description" type="text" class="g_summ" />
				<input type="button" class="addAdcGroupLnk Btn_red" value="${LANGCODEMAP["MSG_ADCSETTING_ADD"]!}"/>  						
			</p>		
		</div>
			<table class="table_type11" summary="${LANGCODEMAP["MSG_ADCSETTING_ADC_GROUP_ADMIN"]!}">
				<caption>${LANGCODEMAP["MSG_ADCSETTING_ADC_GROUP_ADMIN"]!}</caption>
				<colgroup>
					<col width="10%"/>
					<col width="35%"/>
					<col/>
				</colgroup>
				<thead>
					<tr>
						<th>
							<input class="allAdcGroupsChk" type="checkbox"/>
						</th>
						<th>${LANGCODEMAP["MSG_ADCSETTING_GROUP_NAME"]!}</th>
						<th>${LANGCODEMAP["MSG_ADCSETTING_EXPLAIN"]!}</th>
					</tr>
				</thead>
			</table>
		<div class="listWrap1">		
			<table class="table_type11" summary="${LANGCODEMAP["MSG_ADCSETTING_ADC_GROUP_ADMIN"]!}" style="table-layout: fixed;">
				<caption>${LANGCODEMAP["MSG_ADCSETTING_ADC_GROUP_ADMIN"]!}</caption>
				<colgroup>
					<col width="10%"/>
					<col width="35%"/>
					<col/>
				</colgroup>
				<tbody class="adcGroupTbd"></tbody>
			</table>
		</div>
	</div>
	<p class="f_left mar_btm10 mar_lft10">
		<input type="button" class="delAdcGroupsLnk Btn_white" value="${LANGCODEMAP["MSG_ADCSETTING_DELETE"]!}"/>   
	</p>
	<p class="f_right center mar_btm10 mar_rgt10">
		<input type="button" class="closeLnk Btn_white" value="${LANGCODEMAP["MSG_ADCSETTING_CLOSE"]!}"/>   			
	</p>	
	<p class="close">
		<a class="closeLnk" href="#" title="${LANGCODEMAP["MSG_ADCSETTING_CLOSE"]!}">
			<img src="imgs/popup/btn_clse.gif" alt="${LANGCODEMAP["MSG_ADCSETTING_CLOSE"]!}"/>
		</a>
	</p>
</div>