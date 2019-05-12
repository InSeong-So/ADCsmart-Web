<link type="text/css" rel="stylesheet" href="/js/extern/themes/smoothness/jquery-ui.css" />
<#setting number_format="0.####">
<#if langCode??>
  <#if "ko_KR" == langCode><#assign img_lang = ""></#if>
  <#if "en_US" == langCode><#assign img_lang = "_eng"></#if>
</#if>
<div class="contents_area">
	<div>
		<#if !(alteonVsAdd.index)??>
			<img src="imgs/title${img_lang!""}/h3_slbScheduleAdd.gif" class="title_h3" />
		<#else>		
			<img src="imgs/title${img_lang!""}/h3_slbScheduleModify.gif" class="title_h3" />
		</#if>		 
	</div>

	<!-- 1 --> 
    <form id="slbScheduleAddFrm" method="post">
    <div style="display:none;">
    <#if (schedule)??>
		<input name="schedule.index" type="text" value="${schedule.index!''}"/>
	</#if>
	
	</div>
	<table class="newBasicform slbUsrTable" cellpadding="0" cellspacing="0">
		<caption>&nbsp;</caption>
		<colgroup>
			<col width="200px"/>
	    	<col width="auto"/>
		</colgroup>
		<tbody>		
			<tr>
				<th>
					요청자
					<span class="thBtn">
						<input type="button" class="popUpRequestorNameWndLnk Btn_black_small" value="목록" data-index="1" />		
					</span>
				</th>
				<td>
					<label for="requestorSel">
						<input type="radio" name="requestor" id="requestorSel" class="lastRequestUser" checked="checked" value="1" />최근 요청자
					</label>&nbsp;&nbsp;&nbsp;
					<label for="newSel">
						<input type="radio" name="requestor" id="newSel" class="newRequestUser" value="2" />새로입력
					</label>
				</td>
			</tr>
			<tr>
				<th>이름</th>
				<td>
					<#if (schedule)??>
						<input type="text" name="schedule.name" id="slbUsrName" class="inputText width130" value="${(schedule.name)!''}">
						<input name="schedule.index" id="slbUsrIndex" type="hidden" value="${schedule.index!''}"/>
					<#else>
						<input type="text" name="schedule.name" id="slbUsrName" class="inputText width130" value="${(slbUser.name)!''}">
						<input name="schedule.index" id="slbUsrIndex" type="hidden" value="${slbUser.index!''}"/>	
					</#if>
					<input name="slbUser.index" id="slbUsrIndex" type="hidden" value="${slbUser.index!''}"/>
					<input name="slbUser.name" id="slbUsrName" type="hidden"  class="inputText width130" value="${slbUser.name!''}">
					
				</td>
			</tr>
			<tr>
				<th>부서/팀</th>
				<td>
					<#if (schedule)??>
						<input type="text" name="schedule.team" id="slbUsrTeam" class="inputText width130" value="${(schedule.team)!''}">
					<#else>
						<input type="text" name="schedule.team" id="slbUsrTeam" class="inputText width130" value="${(slbUser.team)!''}">						
					</#if>
					<input name="slbUser.team" id="slbUsrTeam" type="hidden"  class="inputText width130" value="${(slbUser.team)!''}">
				</td>
			</tr>	
			<tr>
				<th>휴대전화</th>
				<td>
					<#if (schedule)??>
						<input type="text" name="schedule.phone" id="slbUsrPhone" class="inputText width130" value="${(schedule.phone)!''}">&nbsp;
					<#else>
						<input type="text" name="schedule.phone" id="slbUsrPhone" class="inputText width130" value="${(slbUser.phone)!''}">&nbsp;						
					</#if>
					<input name="slbUser.phone" id="slbUsrPhone" type="hidden"  class="inputText width130" value="${(slbUser.phone)!''}">
					<span class="txt_gray2">"-" 없이 숫자만 입력하세요.</span>
				</td>
			</tr>
			<tr>
				<th>예약</th>
				<td>
				<#if !(schedule.index)??>
					<input id="scheduleDate" name="startTime" class="inputText_calendar2" type="text" value="${systemTime?string("yyyy-MM-dd")}" readonly>
				<#else>
					<input id="scheduleDate" name="startTime" class="inputText_calendar2" type="text" value="${schedule.reservationTime?string("yyyy-MM-dd")}" readonly>
				</#if>
					<span id="scheduleTime" class="">
						<select class="inputSelect scheduleHour" name="schedule.scheduleHour" id="scheduleHour">
							<option value="1">01</option><option value="2">02</option><option value="3">03</option><option value="4">04</option>
							<option value="5">05</option><option value="6">06</option><option value="7">07</option><option value="8">08</option>     
							<option value="9">09</option><option value="10">10</option><option value="11">11</option><option value="12">12</option>
							<option value="13">13</option><option value="14">14</option><option value="15">15</option><option value="16">16</option>
							<option value="17">17</option><option value="18">18</option><option value="19">19</option><option value="20">20</option>
							<option value="21">21</option><option value="22">22</option><option value="23">23</option><option value="24">24</option>
						</select>
						<span class="scheduleDay">:</span>
						<select class="inputSelect scheduleMin" name="schedule.scheduleMinute" id="scheduleMin">
							<option value="0">00</option>
							<option value="1">01</option><option value="2">02</option><option value="3">03</option><option value="4">04</option>
							<option value="5">05</option><option value="6">06</option><option value="7">07</option><option value="8">08</option>     
							<option value="9">09</option><option value="10">10</option><option value="11">11</option><option value="12">12</option> 
							<option value="13">13</option><option value="14">14</option><option value="15">15</option><option value="16">16</option>
							<option value="17">17</option><option value="18">18</option><option value="19">19</option><option value="20">20</option> 	
							<option value="21">21</option><option value="22">22</option><option value="23">23</option><option value="24">24</option>
							<option value="25">25</option><option value="26">26</option><option value="27">27</option><option value="28">28</option>     
							<option value="29">29</option><option value="30">30</option><option value="31">31</option><option value="32">32</option>
							<option value="33">33</option><option value="34">34</option><option value="35">35</option><option value="36">36</option>
							<option value="37">37</option><option value="38">38</option><option value="39">39</option><option value="40">40</option>
							<option value="41">41</option><option value="42">42</option><option value="43">43</option><option value="44">44</option>
							<option value="45">45</option><option value="46">46</option><option value="47">47</option><option value="48">48</option>     
							<option value="49">49</option><option value="50">50</option><option value="51">51</option><option value="52">52</option>
							<option value="53">53</option><option value="54">54</option><option value="55">55</option><option value="56">56</option>
							<option value="57">57</option><option value="58">58</option><option value="59">59</option>	 	                    
						</select>
					</span>			
				</td>
			</tr>		
			<tr>
				<th>미리 알림</th>
				<td>
					<p>
						<select class="inputSelect scheduleNotice">
						<#if schedule??>
							<#if schedule.notice == 0>
								<option selected="selected" value="0">알림없음</option>
								<option value="30">30분 전</option>
								<option value="60">1시간 전</option>
							<#elseif schedule.notice == 30>
								<option value="0">알림없음</option>
								<option selected="selected" value="30">30분 전</option>
								<option value="60">1시간 전</option>
							<#elseif schedule.notice == 60>
								<option value="0">알림없음</option>
								<option value="30">30분 전</option>
								<option selected="selected" value="60">1시간 전</option>								
							</#if>
						<#else>
							<option selected="selected" value="0">알림없음</option>
							<option value="30">30분 전</option>
							<option value="60">1시간 전</option>
						</#if>
						</select>&nbsp;
					</p>					
					<p class="" style="vertical-align: inherit;">						
	 					<input type="checkbox" class="scheduleFlag" id="scheduleFlag">
						<label for="">SLB 예약 실행 후 알림은 받지 않겠습니다.</label>
					</p>
				</td>
			</tr>	
			<tr>
				<th>
					수신자
					<span class="thBtn">
						<input type="button" class="popUpRequestorNameWndLnk Btn_black_small" value="목록" data-index="0">
					</span>
				</th>				
				<#if schedule??>
				<td>
					<span class="scheduleSmReceive none">${schedule.smsReceive!''}</span>
					<p class="sel-smsReceive">						
						<span class="scheduleSmReceiveUsers" title="${schedule.userNmHp!''}">
							${schedule.userNm!''}
						</span> 						
					</p>
				<#else>
				<td>					
					<span class="scheduleSmReceive none"></span>
					<p class="sel-smsReceive">
						<span class="scheduleSmReceiveUsers" title=""></span> 						
					</p>		
				</#if>
				</td>
			</tr>	
		</tbody>											
	</table>

	
	<div class="title_h4_1">
    	<li>SLB 정보 </li>
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
					<li>SLB 
						<span class="thBtn">
							<input type="button" class="slbListPopup Btn_black_small" value="목록">
						</span>
					</li>
				</th>
				<td class="Lth0">
					<label for="orgSlb">
						<input type="radio" name="slbInfo" id="orgSlb" class="slbListPopup" value="1">기존 SLB
					</label>&nbsp;&nbsp;&nbsp;
					<label for="newSlb">
						<input type="radio" name="slbInfo" id="newSlb" class="newSlbInfo" checked="checked" value="2">새로입력
					</label>
				</td>
			</tr>
		</table>
		<#if adc.type == "Alteon">	
		
		<table class="Board slbAlteonInfo" cellpadding="0" cellspacing="0">
			<#include "slbAlteonInfoTableInContent.ftl" />
		</table>
		<#elseif adc.type = "F5">
		
		<table class="Board slbF5Info" cellpadding="0" cellspacing="0">
			<#include "slbF5InfoTableInContent.ftl" />
		</table>
		</#if>
	</form>
</div>

<!-- virtual service 추가 팝업 -->
<div id="modify_virtualService" class="pop_type_wrap">
	<h2>${LANGCODEMAP["MSG_VSALTEON_VS_SERVICE_ADD"]!}</h2>
	<#if accountRole != 'readOnly'>
	<div class="pop_contents">
		<form>
			<input class="rowNoToModify" type="hidden" value="-1">
			<table class="form_type1" summary="${LANGCODEMAP["MSG_VSALTEON_CHANGE"]!}">
				<caption>${LANGCODEMAP["MSG_VSALTEON_CHANGE"]!}</caption>
				<colgroup>
					<col width="150px" />
					<col width="auto" />
				</colgroup>
				<tbody>
					<tr>
						<th scope="row" class="bor_top" >${LANGCODEMAP["MSG_VSALTEON_PORT"]!}</th>
						<td class="bor_top">
							<select class="protocolCbx" style="width:110px;">
								<option selected="selected" value="">${LANGCODEMAP["MSG_VSALTEON_PORT_DESI"]!}</option>
								<!--
								<option value="21">FTP</option>
								-->
								<option value="80">HTTP</option>
								<option value="443">HTTPS</option>
								<!--
								<option value="23">TELNET</option>
								<option value="25">SMTP</option>
								<option value="161">SNMP</option>
								<option value="162">SNMP-TRAP</option>
								<option value="22">SSH</option>
								-->
							</select> 
							<input name="svcPort" type="text"  value="${svcPort!}" class="inputText width130"/>
								&nbsp; ${LANGCODEMAP["MSG_VSALTEON_REAL_PORT"]!} <input name="rPort" type="text" value="${rPort!}" class="inputText width130" /><span class="mar_lft10"></span>
						</td>
					</tr>
					<tr>
						<th scope="row">Group</th>
						<td><select class="poolsCbx" style="width:110px;">
								<option value="">${LANGCODEMAP["MSG_VSALTEON_NEW_DESI"]!}</option>
							</select>
							<input name="poolName" type="text" value="" class="inputText width130"/>
							&nbsp; Index (ID) <input name="poolId" type="text" value="" class="inputText width130" />
						</td>
					</tr>
					<#if version_ ??>
			        <#if (version_) == '29'> 			
					<tr>
						<th scope="row">Member</th>
						<td>
							<div class="selbox_top">
								<!--<p class="mem_tit">Member 추가 - IP </p>-->
								<input id="enable" class="memberEnabledChk" type="checkbox" value="true" checked="checked /><label for="enable" class="mar_lft5 font_normal"> Enable</label>&nbsp;<input class="memberIpTxt inputText width130" type="text" />
								<input type="button" class="memberAddLnk Btn_white" value="${LANGCODEMAP["MSG_VSALTEON_ADD"]!}"/> 
							</div>
							<div class="selbox_add_left">
								<p class="mem_tit">Member <span class="txt_blue"> (<span class="poolMemberCount">0</span>)</span></p>						
					            <div class="headtable member-innerbox">
					                <table class="head" id="selectedMember29" cellpadding="0" cellspacing="0">
					        		    <thead>
					        		        <tr>
					                            <Th class="state">${LANGCODEMAP["MSG_VSALTEON_STATUS"]!}</Th>                         
					                            <Th>Index</Th>
					                            <Th>IP</Th>
					                            <Th>${LANGCODEMAP["MSG_NETWORK_PORT"]!}</Th>  
					                            <Th>timeout</Th>                                                     
					                            <Th>inter</Th>
					                            <Th>retry</Th>
					                            <Th>maxcon</Th>                            
					                            <Th>Real backup</Th>
					                            <Th>weight</Th>
					                        </tr>
					        		    </thead>
					        		    <tbody class="memberTbd"></tbody>
					        		</table>
								</div>
							</div>							
							<div class="selbox_add_right">
								<p class="mem_tit">${LANGCODEMAP["MSG_VSALTEON_REAL_SERVER_LIST"]!} <span class="txt_blue"> (<span class="adcNodeCount">${(adcNodes![])?size}</span>)</span></p>								
					            <div class="headtable">
					                <table class="head" id="memberList29" cellpadding="0" cellspacing="0">
					        		    <thead>
					        		        <tr>
					                            <TH>Index</TH>
					                            <TH>IP</TH>
					                        </tr>
					        		    </thead>
					        		    <tbody class="adcNodeTbd"></tbody>
					        		</table>
					           	</div>
							</div>
							<p class="clear pad_top5">
							</p>
						</td>
					</tr>
					<#else>
					<tr>
						<th scope="row">Member</th>
						<td>
							<div class="selbox_top">
								<!--<p class="mem_tit">Member 추가 - IP </p>-->
								<input id="enable" class="memberEnabledChk" type="checkbox" value="true" checked="checked /><label for="enable" class="mar_lft5 font_normal"> Enable</label>&nbsp;<input class="memberIpTxt inputText width130" type="text" />
								<input type="button" class="memberAddLnk Btn_white" value="${LANGCODEMAP["MSG_VSALTEON_ADD"]!}"/>   
							</div>
							<div class="selbox_left">
								<p class="mem_tit">Member <span class="txt_blue"> (<span class="poolMemberCount">0</span>)</span></p>
								<!--								
								<table id="selectedMember" class="ipList" summary="ip 상태 목록">
									<caption>ip 상태 목록</caption>
									<colgroup>
										<col width="50%" />
										<col width="50%" />
									</colgroup>
									<thead>
										<tr>
											<th class="center">IP</th>
											<th class="center">상태</th>
										</tr>
									</thead>
									<tbody class="memberTbd"></tbody>
								</table>
								-->
								<div class="fixed-table-container151">
					      			<div class="header-background32">
					      				<table id="selectedMember" class="fixed-table-hd" cellpadding="0" cellspacing="0" >		
					                    	<colgroup>				
												<col width="100px"/>
												<col width="130px"/>
												<col width="auto"/>
			
											</colgroup>
					          				<tr>
						            			<th class="align_center">IP</th>
						            			<th class="align_center">${LANGCODEMAP["MSG_NETWORK_PORT"]!}</th>
						            			<th class="align_center">${LANGCODEMAP["MSG_VSALTEON_STATUS"]!}</th>
					          				</tr>
								        </table>
					      			</div>
					      			<div class="fixed-table-container-inner">
					                	<table id="selectedMember" class="fixed-table_1" summary="${LANGCODEMAP["MSG_VSALTEON_IP_STATUS_LIST"]!}"  >
					                    	<colgroup>				
												<col width="100px"/>
												<col width="130px"/>
												<col width="auto"/>
											</colgroup>
												<tbody class="memberTbd"></tbody>		   
										</table>
									</div>
								</div>	
							</div>
							<!-- 
							<div class="usr_move pad_top50">
								<a href="#"><img src="imgs/adcSetting/btn_mov_lft.gif" alt="다음" /></a> <a href="#"><img src="imgs/adcSetting/btn_mov_rgt.gif" alt="이전" /></a>
							</div> 
							-->
							<div class="selbox_right">
								<p class="mem_tit">${LANGCODEMAP["MSG_VSALTEON_REAL_SERVER_LIST"]!} <span class="txt_blue"> (<span class="adcNodeCount">${(adcNodes![])?size}</span>)</span></p>
								<!--								
								<table id="memberList" class="ipList" summary="Real Server 목록">
									<caption>Real Server 목록</caption>
									<thead>
										<tr>
											<th class="center">IP</th>
										</tr>
									</thead>
									<tbody class="adcNodeTbd"></tbody>
								</table>
								-->
								<div class="fixed-table-containerw200">
					      			<div class="header-background32">
					      				<table class="fixed-table-hd" cellpadding="0" cellspacing="0">
											<tr>
												<th>IP</th>
											</tr>
										</table>	
									</div>
									<div class="fixed-table-container-inner ">
										 <table id="memberList" class="fixed-table_1" summary="${LANGCODEMAP["MSG_VSALTEON_REAL_SERVER_LIST"]!}" cellpadding="0" cellspacing="0">
											<tbody class="adcNodeTbd"></tbody>
										</table>
									</div>	 	       			
				 	       		</div>
							</div>
							<p class="clear pad_top5">
								<!-- 
								<a href="#"><img src="imgs/common/btn_del.gif" alt="삭제" /></a> 
								-->
							</p>
						</td>
					</tr>
					</#if>
					</#if>
					<tr>
						<th scope="row">Load Balancing</th>
						<td>
							<select name="loadBalancingType" class="types" >
								<option value="NOT_ALLOWED">Not Allowed</option>
								<option value="Round Robin">Round Robin</option>
								<option value="Least Connections">Least Connections</option>
								<option value="Hash">Hash</option>
							</select>
						</td>
					</tr>
					<tr>
						<th class="bor_btm" scope="row">Health Check</th>
						<td class="bor_btm">
							<select name="healthCheckType" >
								<option value="test">Not Allowed</option>
							</select>
							<input name="extra" type="text"  value="" class="none inputText width130"/>
						</td>
					</tr>
				</tbody>
			</table>			
			<div class="position_cT10">
				<input type="button" class="regOn regMemberOkLnk Btn_red" value="${LANGCODEMAP["MSG_VSALTEON_COMPLETE"]!}"/> 		
				<input type="button" class="regOn regMemberCancelLnk Btn_white" value="${LANGCODEMAP["MSG_VSALTEON_CANCEL"]!}"/>
				<input type="button" class="regOff regMemberCancelLnk Btn_white none" value="${LANGCODEMAP["MSG_VSALTEON_CONFIRM"]!}"/>   
			</div> 			
		</form>
	</div>
	<#else>
<!--  READ Only User virtual service 추가 팝업-->
	<div class="pop_contents">
		<form>
			<input class="rowNoToModify" type="hidden" value="-1">
			<table class="form_type1" summary="${LANGCODEMAP["MSG_VSALTEON_CHANGE"]!}">
				<caption>${LANGCODEMAP["MSG_VSALTEON_CHANGE"]!}</caption>
				<colgroup>
					<col width="150px" />
					<col width="auto" />
				</colgroup>
				<tbody>
					<tr>
						<th scope="row" class="bor_top">${LANGCODEMAP["MSG_VSALTEON_PORT"]!}</th>
						<td class="bor_top">
							<select class="protocolCbx" style="width:110px;" disabled="disabled">
								<option selected="selected" value="">${LANGCODEMAP["MSG_VSALTEON_PORT_DESI"]!}</option>
								<option value="21">FTP</option>
								<option value="80">HTTP</option>
								<option value="443">HTTPS</option>
								<option value="23">TELNET</option>
								<option value="25">SMTP</option>
								<option value="161">SNMP</option>
								<option value="162">SNMP-TRAP</option>
								<option value="22">SSH</option>
							</select> 
							<input name="svcPort" type="text"  value="${svcPort!}" class="inputText width130" disabled="disabled"/>
								&nbsp; ${LANGCODEMAP["MSG_VSALTEON_REAL_PORT"]!}<input name="rPort" type="text" value="${rPort!}" class="inputText width130" disabled="disabled"/><span class="mar_lft10"></span>
						</td>
					</tr>
					<tr>
						<th scope="row">Group</th>
						<td><select class="poolsCbx" style="width:110px;" disabled="disabled">
								<option value="">${LANGCODEMAP["SG_VSALTEON_NEW_DESI"]!}</option>
							</select>
							<input name="poolName" type="text" value="" class="inputText width130" disabled="disabled"/>
							&nbsp; Index (ID) <input name="poolId" type="text" value="" class="inputText width130" disabled="disabled"/>
						</td>
					</tr>
			        <#if (version_) == '29'> 			
					<tr>
						<th scope="row">Member</th>
						<td>
							<div class="selbox_top">
								<!--<p class="mem_tit">Member 추가 - IP </p>-->
								<input id="enable" class="memberEnabledChk" type="checkbox" value="true" checked="checked" disabled="disabled" /><label for="enable" class="mar_lft5 font_normal"> Enable</label>&nbsp;<input class="memberIpTxt inputText width130" type="text" disabled="disabled"/>
							</div>
							<div class="selbox_add_left">
								<p class="mem_tit">Member <span class="txt_blue"> (<span class="poolMemberCount">0</span>)</span></p>						
					            <div class="headtable member-innerbox">
					                <table class="head" id="selectedMember29" cellpadding="0" cellspacing="0">
					        		    <thead>
					        		        <tr>
					                            <Th class="state">${LANGCODEMAP["MSG_VSALTEON_STATUS"]!}</Th>                         
					                            <Th>Index</Th>
					                            <Th>IP</Th>
					                            <Th>${LANGCODEMAP["MSG_NETWORK_PORT"]!}</Th>  
					                            <Th>timeout</Th>                                                     
					                            <Th>inter</Th>
					                            <Th>retry</Th>
					                            <Th>maxcon</Th>                            
					                            <Th>Real backup</Th>
					                            <Th>weight</Th>
					                        </tr>
					        		    </thead>
					        		    <tbody class="memberTbd"></tbody>
					        		</table>
								</div>
							</div>							
							<div class="selbox_add_right">
								<p class="mem_tit">${LANGCODEMAP["MSG_VSALTEON_REAL_SERVER_LIST"]!} <span class="txt_blue"> (<span class="adcNodeCount">${(adcNodes![])?size}</span>)</span></p>								
					            <div class="headtable">
					                <table class="head" id="memberList29" cellpadding="0" cellspacing="0">
					        		    <thead>
					        		        <tr>
					                            <TH>Index</TH>
					                            <TH>IP</TH>
					                        </tr>
					        		    </thead>
					        		    <tbody class="adcNodeTbd"></tbody>
					        		</table>
					           	</div>
							</div>
							<p class="clear pad_top5">
							</p>
						</td>
					</tr>
					<#else>
					<tr>
						<th scope="row">Member</th>
						<td>
							<div class="selbox_top">
								<!--<p class="mem_tit">Member 추가 - IP </p>-->
								<input id="enable" class="memberEnabledChk" type="checkbox" value="true" checked="checked" disabled="disabled" /><label for="enable" class="mar_lft5 font_normal"> Enable</label>&nbsp;<input class="memberIpTxt inputText width130" type="text" disabled="disabled" />
							</div>
							<div class="selbox_left">
								<p class="mem_tit">Member <span class="txt_blue"> (<span class="poolMemberCount">0</span>)</span></p>
								<div class="fixed-table-container151">
					      			<div class="header-background32">
					      				<table id="selectedMember" class="fixed-table-hd" cellpadding="0" cellspacing="0" >		
					                    	<colgroup>				
												<col width="181px"/>
												<col width="auto"/>
			
											</colgroup>
					          				<tr>
						            			<th class="align_center">IP</th>
						            			<th class="align_center">${LANGCODEMAP["MSG_VSALTEON_STATUS"]!}</th>
					          				</tr>
								        </table>
					      			</div>
					      			<div class="fixed-table-container-inner">
					                	<table id="selectedMember" class="fixed-table_1" summary="${LANGCODEMAP["MSG_VSALTEON_IP_STATUS_LIST"]!}"  >
					                    	<colgroup>				
												<col width="180px"/>
												<col width="auto"/>
											</colgroup>
												<tbody class="memberTbd"></tbody>		   
										</table>
									</div>
								</div>	
							</div>
							<!-- 
							<div class="usr_move pad_top50">
								<a href="#"><img src="imgs/adcSetting/btn_mov_lft.gif" alt="다음" /></a> <a href="#"><img src="imgs/adcSetting/btn_mov_rgt.gif" alt="이전" /></a>
							</div> 
							-->
							<div class="selbox_right">
								<p class="mem_tit">${LANGCODEMAP["MSG_VSALTEON_REAL_SERVER_LIST"]!} <span class="txt_blue"> (<span class="adcNodeCount">${(adcNodes![])?size}</span>)</span></p>
								<!--								
								<table id="memberList" class="ipList" summary="Real Server 목록">
									<caption>Real Server 목록</caption>
									<thead>
										<tr>
											<th class="center">IP</th>
										</tr>
									</thead>
									<tbody class="adcNodeTbd"></tbody>
								</table>
								-->
								<div class="fixed-table-containerw200">
					      			<div class="header-background32">
					      				<table class="fixed-table-hd" cellpadding="0" cellspacing="0">
											<tr>
												<th>IP</th>
											</tr>
										</table>	
									</div>
									<div class="fixed-table-container-inner ">
										 <table id="memberList" class="fixed-table_1" summary="${LANGCODEMAP["MSG_VSALTEON_REAL_SERVER_LIST"]!}" cellpadding="0" cellspacing="0">
											<tbody class="adcNodeTbd"></tbody>
										</table>
									</div>	 	       			
				 	       		</div>
							</div>
							<p class="clear pad_top5">
								<!-- 
								<a href="#"><img src="imgs/common/btn_del.gif" alt="삭제" /></a> 
								-->
							</p>
						</td>
					</tr>
					</#if>
					<tr>
						<th scope="row">Load Balancing</th>
						<td>
							<select name="loadBalancingType" class="types" disabled="disabled">
								<option value="NOT_ALLOWED">Not Allowed</option>
								<option value="Round Robin">Round Robin</option>
								<option value="Least Connections">Least Connections</option>
								<option value="Hash">Hash</option>
							</select>
						</td>
					</tr>
					<tr>
						<th class="bor_btm" scope="row">Health Check</th>
						<td class="bor_btm">
							<select name="healthCheckType" class="types"  disabled="disabled">
								<option value="test">Not Allowed</option>
							</select>
							<input name="extra" type="text"  value="" class="none inputText width130"/>
						</td>
					</tr>
				</tbody>
			</table>			
			<div class="position_cT10">	
				<input type="button" class="regMemberCancelLnk Btn_white" value="${LANGCODEMAP["MSG_VSALTEON_CANCEL"]!}"/>   	
			</div> 			
		</form>
	</div>
	</#if>
<!--  //READ Only User virtual service 추가 팝업-->
	<p class="close">
		<a href="#" title="${LANGCODEMAP["MSG_VSALTEON_CLOSE"]!}">
			<img src="imgs/popup/btn_clse.gif" alt="${LANGCODEMAP["MSG_VSALTEON_CLOSE"]!}" />
		</a>
	</p>
</div>
<!-- //virtual service 추가 팝업 -->

<!-- virtualserver ip 팝업 -->
<div id="vs_ipList" class="pop_type_wrap">
	<h2>${LANGCODEMAP["MSG_VSALTEON_VS_IP_LIST"]!}</h2>
	<div class="pop_contents">
		<p class="sch">
			<input class="searchTxt inputText_search" type="text"/>
			<span class="btn2">
				<a class="searchLnk" href="#">
					<img src="imgs/meun/btn_search_1.png" alt="${LANGCODEMAP["MSG_VSALTEON_SEARCH"]!}"/>
				</a>
			</span>	
		</p>
		<table id="vsIP" class="table_type11" summary="virtual server list">
			<caption>virtual server list</caption>
			<colgroup>
					<col width="46px" />
					<col width="207px" />
					<col width="auto" />
			</colgroup>
			<thead>
				<tr>
					<th>${LANGCODEMAP["MSG_VSALTEON_NUMBER"]!}</th>
					<th>${LANGCODEMAP["MSG_VSALTEON_VS_NAME"]!}</th>
					<th>${LANGCODEMAP["MSG_VSALTEON_ADDRESS"]!}</th>
				</tr>
			</thead>
		</table>
		<div class="listWrap11">
			<table id="vsIP" class="table_type11" summary="virtual server list">
				<caption>virtual server list</caption>
				<colgroup>
					<col width="46px" />
					<col width="207px" />
					<col width="auto" />
				</colgroup>

				<tbody class="vServerNameTbd"></tbody>
			</table>
		</div>
		<p class="center mar_top10 ">
		    <input type="button" class="closeLnk Btn_white" value="${LANGCODEMAP["MSG_VSALTEON_CLOSE"]!}"/>   
		</p>
	</div>
	<p class="close">
		<a class="closeWndLnk" href="#" title="${LANGCODEMAP["MSG_VSALTEON_CLOSE"]!}">
			<img src="imgs/popup/btn_clse.gif" alt="${LANGCODEMAP["MSG_VSALTEON_CLOSE"]!}" />
		</a>
	</p>
</div>
<!-- //virtualserver ip 팝업 팝업 -->

<!-- 일괄 추가 팝업 -->
<div id="memberAddBatch" class="pop_type_wrap">
	<h2>${LANGCODEMAP["MSG_VSF5_GM_MULTI_ADD"]!}</h2>
	<div class="pop_contents">
		<p class="ptop mar_top26">
			<span>${LANGCODEMAP["MSG_VSF5_FORMAT"]!}</span>${LANGCODEMAP["MSG_VSF5_INPUT_IP_PORT"]!}
		</p>
		<p class="ptop none_bt">
			<span>${LANGCODEMAP["MSG_VSF5_USE_EX"]!}</span>192.168.110.11:80
		</p>
		<textarea name="ipsTxt" rows="10" style="width:95%; resize:none;" ></textarea>
		<p class="center pbtm">
		<!--	<span class="txt">&nbsp;</span>-->
			<input type="button" class="onOk Btn_red" value="${LANGCODEMAP["MSG_VSF5_COMPLETE"]!}"/> 
			<input type="button" class="onCancel Btn_white" value="${LANGCODEMAP["MSG_VSF5_CANCEL"]!}"/>   			
		</p>
	</div>
	<p class="close">
		<a href="#" title="${LANGCODEMAP["MSG_VSF5_CLOSE"]!}">
			<img src="imgs/popup/btn_clse.gif" alt="${LANGCODEMAP["MSG_VSF5_CLOSE"]!}" />
		</a>
	</p>
</div>
<!-- //일괄 추가 팝업 -->

<!-- 포토 변경 팝업 -->
<div id="portChange" class="pop_type_wrap">
	<h2>${LANGCODEMAP["MSG_VSF5_PORT_CHANGE"]!}</h2>
	<div class="pop_contents">
		<p class="setport">
			<span>${LANGCODEMAP["MSG_VSF5_PORT_NUMBER"]!}</span><input class="portNo" type="text"/>
		</p>
		<p class="center mar_top10">
			<input type="button" class="onOk Btn_red" value="${LANGCODEMAP["MSG_VSF5_COMPLETE"]!}"/>  
			<input type="button" class="onCancel Btn_white" value="${LANGCODEMAP["MSG_VSF5_CANCEL"]!}"/>  	
		</p>
	</div>
	<p class="close">
		<a href="#" title="${LANGCODEMAP["MSG_VSF5_CLOSE"]!}">
			<img src="imgs/popup/btn_clse.gif" alt="${LANGCODEMAP["MSG_VSF5_CLOSE"]!}" />
		</a>
	</p>
</div>
<!-- //포토 변경 팝업 -->

<!-- Persistence 설정 정보 팝업 -->
<div id="persistenceDetail" class="pop_type_wrap">
	<h2>${LANGCODEMAP["MSG_VSF5_PERSIST_SET_INFO_VIEW"]!}</h2>
	<div class="pop_contents">
		<!--
		<div class="recent_time">
			<p>최종 변경 시간 : 2012-03-28 13:28:00</p>
		</div>
		-->
		<form class="setting">
			<fieldset>
				<legend>${LANGCODEMAP["MSG_VSF5_DEFAULT_SET"]!}</legend>
				<table class="form_type1" summary="${LANGCODEMAP["MSG_VSF5_PERSIST_SET_INFO_VIEW"]!}">
					<caption>${LANGCODEMAP["MSG_VSF5_PERSIST_SET_INFO_VIEW"]!}</caption>
					<colgroup>
						<col width="35%" />
						<col width="55%" />
					</colgroup>
					<tbody>
						<tr>
							<th scope="row" class="none_bg_img bor_top">${LANGCODEMAP["MSG_VSF5_NAME"]!}</th>
							<td class="bor_top">
								<span name="profileAdd.name" class="f_left"></span> <img src="imgs/popup/icon_f5.png" alt="f5" />
							</td>
						</tr>
						<tr>
							<th scope="row" class="none_bg_img">Persistence Type</th>
							<td>
								<span name="profileAdd.persistenceType"></span>
							</td>
						</tr>
						<tr>
							<th scope="row" class="none_bg_img">Parent Profile</th>
							<td>
								<span name="profileAdd.parentProfileName"></span>
							</td>
						</tr>
						<tr>
							<th scope="row" class="none_bg_img">Match Across Services</th>
							<td>
								<span name="profileAdd.isMatchAcrossServices"></span>
							</td>
						</tr>
						<tr>
							<th scope="row" class="none_bg_img bor_btm">Timeout</th>
							<td class=" bor_btm">
								<span name="profileAdd.timeOutInSec"></span>${LANGCODEMAP["MSG_VSF5_SECOND"]!}
							</td>
						</tr>
					</tbody>
				</table>
			</fieldset>
		</form>
	</div>
	<p class="close">
		<a class="closeWndLnk" href="#" title="${LANGCODEMAP["MSG_VSF5_CLOSE"]!}">
			<img src="imgs/popup/btn_clse.gif" alt="${LANGCODEMAP["MSG_VSF5_CLOSE"]!}" />
		</a>
	</p>
</div>
<!-- //Persistence 설정 정보 팝업 -->

<!-- 요청자 관리 팝업 -->
<div id="slbUsrReqList" class="pop_type_wrap slbUsrReqList">
	<h2>요청자 관리</h2>
	<div class="pop_contents reqModify none">
		<#include "slbUserModifyContent.ftl"/>
	</div>
	<div class="pop_contents reqList none">	
		<span class="tit">
			요청자 목록
		 	(<span class="txt_blue noticeReqPageCountInfo"></span>)
		</span>	 	
		<table id="" class="table_type11" summary="">
			<caption></caption>
			<colgroup>
				<col width="40px" />				
				<col width="120px" />
				<col width="150px" />					
				<col width="100px" />
				<col width="auto" />
			</colgroup>
			<thead>
				<tr>
					<th><input class="slbUsrAllChk" name="checkbox" type="checkbox" id="checkbox" /></th>
					<th>이름</th>
					<th>부서/팀</th>
					<th>휴대전화</th>
					<th>관리</th>												
				</tr>
			</thead>
		</table>
		<div class="listWrap1">
		<#include "slbUserTableInContent.ftl"/>	
		</div>
	</div>
	
	<div class="reqList none">
	<p class="f_left mar_btm10 mar_lft10">
		<input type="button" class="delSlbUsersLnk Btn_white" value="삭제">   
	</p>
	<p class="f_right center mar_btm10 mar_rgt10 reqList none">
		<input type="button" class="selectedLnk Btn_white" value="선택 요청자 사용"/> 
		<input type="button" class="closeLnk Btn_white" value="취소"/> 								
	</p>	
	</div>
	<p class="close">
		<a class="closeWndLnk" href="#" title="${LANGCODEMAP["MSG_VSF5_CLOSE"]!}">
			<img src="imgs/popup/btn_clse.gif" alt="${LANGCODEMAP["MSG_VSF5_CLOSE"]!}" />
		</a>
	</p>
</div>
<!-- 요청자 관리 팝업 -->

<!-- 수신자 목록 팝업 -->
<div id="slbUsrRespList" class="pop_type_wrap slbUsrRespList">
	<h2>수신자 관리</h2>
	<div class="pop_contents respAdd none">
		<#include "slbUserModifyContent.ftl" />
	</div>	
	<div class="pop_contents respList none">	
		<span class="tit"> 
			SMS 수신자 목록 
			(<span class="txt_blue noticeRespPageCountInfo"></span>)
		</span>
		<span class="tit-rightbtn"><input type="button" class="Btn_white addRespUsr" value="추가"/></span>
		<table id="" class="table_type11" summary="">
			<caption></caption>
			<colgroup>
				<col width="40px" />				
				<col width="120px" />
				<col width="150px" />					
				<col width="100px" />
				<col width="auto" />
			</colgroup>
			<thead>
				<tr>
					<th><input class="slbUsrAllChk" name="checkbox" type="checkbox" id="checkbox" /></th>
					<th>이름</th>
					<th>부서/팀</th>
					<th>휴대전화</th>
					<th>관리</th>												
				</tr>
			</thead>
		</table>
		<div class="listWrap1">		
			<#include "slbUserTableInContent.ftl"/>	
		</div>
	</div>
	
	<div class="respList none">
	<p class="f_left mar_btm10 mar_lft10">
		<input type="button" class="delSlbUsersLnk Btn_white" value="삭제">   
	</p>
	<p class="f_right center mar_btm10 mar_rgt10 respList none">
		<input type="button" class="selRespLnk Btn_white" value="선택 수신자 사용"/> 
		<input type="button" class="closeLnk Btn_white" value="닫기"/> 								
	</p>	
	</div>
	<p class="close">
		<a class="closeWndLnk" href="#" title="${LANGCODEMAP["MSG_VSF5_CLOSE"]!}">
			<img src="imgs/popup/btn_clse.gif" alt="${LANGCODEMAP["MSG_VSF5_CLOSE"]!}" />
		</a>
	</p>
</div>
<!-- //수신자 목록팝업 -->

<!-- SLB 목록팝업 -->
<div id="slbList" class="pop_type_wrap slbList">
	<h2>SLB 목록</h2>		
	<div class="pop_contents vsList none">
		<p class="sch">
			<input class="searchTxt inputText_search" type="text"/>
			<span class="btn2">
				<a class="searchLnk" href="#">
					<img src="imgs/meun/btn_search_1.png" alt="${LANGCODEMAP["MSG_VSALTEON_SEARCH"]!}"/>
				</a>
			</span>	
			<span class="total_popup"> 
				총 <span class="txt_bold noticeSlbPageCountInfo">403</span>건 <span class="txt_bold noticeSlbPageInfo">(1/21페이지)</span>
			</span>				
		</p>		
		
		<table id="selectedSlbList" class="table_type11 slbListTable" summary="">
			<caption></caption>
			<#include "slbListTableInContent.ftl" />	
		</table>
		<div class="disabledChk">
		 	<p class="pageNavigatorOn"></p>
			<div id="select2" class="pageRowCountCbxOn"></div>
		</div>
	<p class="center mar_top10 ">
		    <input type="button" class="closeLnk Btn_white" value="닫기">   
		</p>		
	</div>	
	<p class="close">
		<a class="closeWndLnk" href="#" title="${LANGCODEMAP["MSG_VSF5_CLOSE"]!}">
			<img src="imgs/popup/btn_clse.gif" alt="${LANGCODEMAP["MSG_VSF5_CLOSE"]!}" />
		</a>
	</p>
</div>
<!-- //SLB 목록팝업 -->