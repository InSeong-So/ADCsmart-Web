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
		<img src="imgs/title${img_lang!""}/h3_adcAdd.gif" class="title_h3" />				
	</div>
	<!-- 1 -->
	<form id="adcAddFrm" class="setting" method="post">
	<div style="display:none;">
		<input name="adcAdd.adc.index" type="text" value="${(adcAdd.adc.index)!''}"/>
	</div>
	<div class="title_h4">
		<li>${LANGCODEMAP["MSG_ADCSETTING_FIRSTLEVEL"]!}</li>
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
				<li>${LANGCODEMAP["MSG_ADCSETTING_TYPE"]!}</li>
			</th>
			<td class="Lth0">
				<input type="radio" name="adcAdd.adc.type" id="F5Select" checked="checked" value="F5" />
					<label for="F5Select">F5</label>&nbsp;&nbsp;&nbsp;
				<input type="radio" name="adcAdd.adc.type" id="AlteonSelect" value="Alteon" />
					<label for="AlteonSelect">Alteon</label>&nbsp;&nbsp;&nbsp;
				<input type="radio" name="adcAdd.adc.type" id="PiolinkSelect" value="PiolinkUnknown" />
					<label for="PiolinkSelect">Piolink</label>
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
				<input type="text" name="adcAdd.adc.name"  id="idAdcName" class="inputText width130" value="${(adcAdd.adc.name)!''}"/>
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
				<span>
					<input type="radio" name="adcAdd.adc.mgmtMode" id="MODE_DATA" checked="checked" value="1" />				
					<label for="MODE_DATA">DATA</label>			
				</span>&nbsp;&nbsp;&nbsp;							
				<span class="mode_mgmt">
				    <input type="radio" name="adcAdd.adc.mgmtMode" id="MODE_MGMT" value="2" />
					<label for="MODE_MGMT"> MGMT</label>
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
				<input name="adcAdd.adc.ip" type="text" id="textfield" class="inputText width130" value="${(adcAdd.adc.ip)!}">
			</td>
		</tr>
		<tr class="DivideLine">
			<td colspan="2"></td>
		</tr>
		<tr class="PeeripInputEvent">
			<th class="Lth1">
				<li>${LANGCODEMAP["MSG_ADCSETTING_PEERIP_ADDRESS"]!}</li>
			</th>
			<td class="Lth0">
				<input name="adcAdd.adc.peerip" type="text" id="textfield" class="inputText width130" value="${(adcAdd.adc.peerip)!}">
			</td>
		</tr>
		<tr class="DivideLine PeeripInputEvent">
			<td colspan="2"></td>
		</tr>			
		<tr>
			<th class="Lth2">
				<li>프로토콜</li>
			</th>			
			<td>
				<table class="Board100" cellpadding="0" cellspacing="0">				
					<tr>
						<td class="Lth0">				
							<select name="adcAdd.adc.connProtocol" class="protocolChange">
								<option value="0" selected="selected">TCP</option>
								<option value="1">ICMP</option>
							</select>	
						</td>&nbsp;&nbsp;&nbsp;
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
						<td class="telnetConnService none width110">
							<span>
								<input type="radio" name="adcAdd.adc.connService" id="telnetSelect" value="${(adcAdd.adc.connService)!'23'}" />
								<label for="" class="">Telnet</label>
							    <input type="text" name="adcAdd.adc.connPort" id="telnet" class="inputText width35" disabled="disabled" value="${(adcAdd.adc.connPort)!'23'}" />				
							</span>	
						</td>
						<td class="sslConnService width100">			
							<span >
								<input type="radio" name="adcAdd.adc.connService" id="sslSelect" checked="checked" value="${(adcAdd.adc.connService)!'22'}" />
								<label for="" class="">SSH</label> 
							    <input type="text" name="adcAdd.adc.connPort" id="ssl" class="inputText width35" value="${(adcAdd.adc.connPort)!'22'}" />			
							</span>	
						</td>	
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
			<input name="adcAdd.adc.accountId" type="text" id="textfield" class="inputText width130" value="${(adcAdd.adc.accountId)!''}" />		
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
			           		<input name="adcAdd.adc.password" type="password" id="textfield" class="inputText width130" value="${(adcAdd.adc.password)!}" autocomplete="off"/>
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
		<tr class="DivideLine CliIdInputEvent">
			<td colspan="2"></td>
		</tr>
		<tr class="CliIdInputEvent">
			<th class="Lth2">
				<li>${LANGCODEMAP["MSG_ADCSETTING_CLIID"]!}</li>
			</th>
			<td class="Lth0">
			<input name="adcAdd.adc.cliAccountId" type="text" id="textfield" class="inputText width130" value="${(adcAdd.adc.cliAccountId)!''}" />		
		</tr>
		<tr class="DivideLine CliIdInputEvent">
			<td colspan="2"></td>
		</tr>		
		<tr class="CliPasswdInputEvent">
			<th class="Lth2">
				<li>${LANGCODEMAP["MSG_ADCSETTING_CLIPASSWORD"]!}</li>
			</th>
			<td class="Lth0">
	            <table class="Board100" cellpadding="0" cellspacing="0">
					<tr>
						<td>
							<input name="fakeIdField" type="text" style="display:none;">
							<input name="fakePwField" type="password" style="display:none;">
				            <input name="adcAdd.adc.cliPassword" type="password" id="textfield" class="inputText width130" value="${(adcAdd.adc.cliPassword)!}" autocomplete="off"/>
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
				<input type="radio" name="adcAdd.adc.adcSnmpInfo.version" id="v2" checked="checked" value="2" />
					<label for="v2Snmp">v2</label>&nbsp;&nbsp;&nbsp;
				<input type="radio" name="adcAdd.adc.adcSnmpInfo.version" id="v3" value="3" disabled class="snmpVesionView" />
					<label for="v3Snmp">v3</label>
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
							<input type="text" name="adcAdd.adc.adcSnmpInfo.rcomm" id="textfield3" class="inputText width130" value="${(adcAdd.adc.adcSnmpInfo.rcomm)!}" />
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
					<option selected="selected" value="md5">md5</option>
					<option value="sha">sha</option>
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
							<input name="adcAdd.adc.ipCp" type="text" id="textfield3" class="inputText width130" value="${(adcAdd.adc.ip)!}" disabled="disabled">
							<input type="text" name="adcAdd.adc.syslogip" id="textfield3" class="inputText width130" value="${(adcAdd.adc.syslogip)!}" />
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
			<span class="conn_statVersion"></span>
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
				    <input type="button" id="adcGroupMgmtLnk" class="v_btm Btn_black_small" value="${LANGCODEMAP["MSG_ADCSETTING_GROUP_ADMIN"]!}"/> 
				</a>
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
				<input type="checkbox" name="adcAdd.adc.ssoModeType" id="useSso" value="true" />
					<label for="useSso">${LANGCODEMAP["MSG_SYSSETTING_USE"]!}</label>
			</td>			
		</tr>
		<tr class="DivideLine ssoChk">
			<td colspan="2"></td>
		</tr>
		<tr>
			<th class="Lth1">
				<li>${LANGCODEMAP["MSG_ADCSETTING_BUYING_DATE"]!}</li>
			</th>
			<td class="Lth0">
				<input type="text" name="adcAdd.adc.purchaseDate" id="startTimeDate" class="inputText width130" value="${(adcAdd.adc.purchaseDate?string("yyyy-MM-dd"))!}" title="${LANGCODEMAP["MSG_ADCSETTING_RESERVE_DATA_START_TIME"]!}" readonly/>
			</td>
		</tr>
		<tr class="DivideLine">
			<td colspan="2"></td>
		</tr>
		<tr>
			<th class="Lth1">
				<li>${LANGCODEMAP["MSG_ADCSETTING_EXPLAIN"]!}</li>
			</th>
			<td class="Lth0">
				<textarea name="adcAdd.adc.description" class="desc" id="textfield4">${(adcAdd.adc.description)!}</textarea>								
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
            	<table  width="100%" border="0" cellpadding="0" cellspacing="0">
					<colgroup>
						<col width="48%" />
						<col width="4%"  >
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
							<select name="adcAdd.accountIds" size="6" multiple="multiple" class="usrselecte " id="accountsSelectedSel">
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
								<input name="userlist" type="text"  class="inputText_search"  id="textfield3" />
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
		<tr>
			<td colspan="2">
				<div class="position_cT10">
					<input type="button" id="adcAddOkBtn" class="Btn_red" value="${LANGCODEMAP["MSG_ADCSETTING_COMPLETE"]!}"/> 
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