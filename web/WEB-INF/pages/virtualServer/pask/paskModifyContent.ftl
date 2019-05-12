<link type="text/css" rel="stylesheet" href="/js/extern/themes/smoothness/jquery-ui.css" />
<#setting number_format="0.####">
<#if langCode??>
  <#if "ko_KR" == langCode><#assign img_lang = ""></#if>
  <#if "en_US" == langCode><#assign img_lang = "_eng"></#if>
</#if>
<div class="contents_area">
	<div>
		<img src="imgs/title${img_lang!""}/h3_vsModify.gif" class="title_h3" />		
	<#if (accountRole != 'readOnly') && ((paskVsAdd.adcMode)! != 1)>
	<form id="paskVsAddFrm" method="post">
		<div style="display:none;">
			<input name="paskVsAdd.index" type="text" value="${paskVsAdd.index!}"/>
			<input name="paskVsAdd.adcIndex" type="text" value="${paskVsAdd.adcIndex}"/>
		</div>
		<!-- 활성화 (사용자 임의 수정 가능) form -->
		<#if (paskVsAdd.configurable)! == 1>
			<table class="Board" cellpadding="0" cellspacing="0">
				<caption>&nbsp;</caption>
				<colgroup>
					<col width="200px"/>
	        		<col width="auto"/>
				</colgroup>
				<tr class="StartLine">	
					<td colspan="2"></td>
				</tr>
				<tr>
					<th class="Lth2">
						<li>${LANGCODEMAP["MSG_VSPASK_MODIP_ADDR"]!}</li>
					</th>
					<td class="Lth0">
						<input type="text" name="paskVsAdd.ip" id="textfield2" class="inputText width130" value="${paskVsAdd.ip!''}"/><span class="mar_lft10"></span>
					</td>
				</tr>
				<tr class="DivideLine">
					<td colspan="2"></td>
				</tr>
				<tr>
					<th class="Lth2">
						<li>${LANGCODEMAP["MSG_VSPASK_MODNAME"]!}</li>
					</th>
					<td class="Lth0">
						<#if !(paskVsAdd.name)?? || (paskVsAdd.name)! == ''>
							<input id="textfield2" class="inputText width130" name="paskVsAdd.name" type="text" value="${paskVsAdd.name!''}"/>
								<span class="example">${LANGCODEMAP["MSG_VSPASK_MODFORMAT_VS_GROUP_NAME_SEQ"]!}&nbsp; </span>
							<span class="popUpVServerNameWndLnk">
							<input type="button" class="Btn_black_small" value="&nbsp;${LANGCODEMAP["MSG_VSPASK_MODUSED_NAME"]!}&nbsp;">
						</span>
						<#else>
							<input disabled="disabled" type="text" class="inputText width130" value="${paskVsAdd.name!''}"/>
							<input name="paskVsAdd.name" type="hidden" class="inputText width130" value="${paskVsAdd.name!''}"/>
						</#if>						
					</td>
		        </tr>
				<tr class="DivideLine">
					<td colspan="2"></td>
				</tr>
				<tr>
					<th class="Lth2">
						<li>${LANGCODEMAP["MSG_VSPASK_MODPORT"]!}</li>
					</th>
					<td class="Lth0">
						<select name="select2" id="select2" class="types inputSelect protocolCbx select width134">
							<#if !(paskVsAdd.protocol)?? || ((paskVsAdd.protocol)?? && (paskVsAdd.protocol)! == 6)>
							  	<option selected="selected" value="">${LANGCODEMAP["MSG_VSPASK_MODPORT_DENI"]!}</option>
								<option value="21">FTP</option>
								<option value="80">HTTP</option>
								<option value="443">HTTPS</option>
								<option value="23">TELNET</option>
								<option value="25">SMTP</option>
								<option value="161">SNMP</option>
								<option value="162">SNMP-TRAP</option>
								<option value="22">SSH</option>
							<#else>
								<option value="NOT_ALLOWED" selected="selected">Not Allowed</option>
							</#if>
						</select>&nbsp;
						<#if !(paskVsAdd.protocol)?? || ((paskVsAdd.protocol)?? && (paskVsAdd.protocol)! == 6)>
							<input type="text" name="paskVsAdd.port" id="textfield3" class="inputText width130" value="${paskVsAdd.port!''}" />
						<#else>
							<input name="paskVsAdd.port" type="text" value="${paskVsAdd.port!''}" readonly="readonly"/><span class="mar_lft10"></span>
						</#if>
					</td>
	        	</tr>
				<tr class="DivideLine">
					<td colspan="2"></td>
				</tr>	
				<tr>
					<th class="Lth1">
						<li>Member</li>
					</th>					
					<td>
						<div class="selbox_top">
							<span class="txt_bold">${LANGCODEMAP["MSG_VSPASK_MODMEMBER_ADD_IP_PORT"]!}</span>
							<input name="checkbox4" type="checkbox" id="enable" class="memberEnabledChk" value="true" checked="checked" /> Enable <br/>
							<input type="text" class="memberIpTxt inputText width130" />
								: 
							<input type="text" class="memberPortTxt inputText width50" /> 
							<input type="button" class="memberAddLnk Btn_black_small" value="${LANGCODEMAP["MSG_VSPASK_MODADD"]!}"/>
							<input type="button" class="onMemberAddBatch Btn_black_small" value="${LANGCODEMAP["MSG_VSPASK_MODMULTI_ADD"]!}"/>						
						</div>
						<div class="selbox_add_left">
							<p class="mem_tit">Member <span class="txt_blue">(<span class="txt_blue poolMemberCount">${(paskVsAdd.members![])?size}</span>)</span></p>						
				            <div class="headtable member-innerbox">
				                <table class="head" id="selectedMember" cellpadding="0" cellspacing="0">
									<thead>
				        		        <tr>
				                            <th class="align_center">
					            				<input class="allMembersChk" name="checkbox" type="checkbox" id="checkbox" />
					            			</th>
					            			<th class="align_center">IP</th>
					           				<th class="align_center">${LANGCODEMAP["MSG_VSPASK_MODPORT"]!}</th>
					            			<th class="align_center">${LANGCODEMAP["MSG_VSPASK_MODSTATUS"]!}</th>
					            			<th style="display: none;">ID</th>
				                        </tr>
				        		    </thead>
				        		    <tbody class="memberTbd">
										<#list paskVsAdd.members![] as theMember>
											<tr class="regMemberTr">
												<td class="align_center"><input class="memberChk" name="checkbox" type="checkbox" id="checkbox" /></td>
										    	<td class="align_left_P10" style="display: none;">${theMember.index}</td>
											    <td class="align_left_P10">${theMember.ip}</td>
											    <td class="align_center">${theMember.port}</td>
											    <td class="align_center"> 
											    	<select>
														<option value="true" ${theMember.isEnabled?string('selected="selected"', '')}>Enabled</option>
														<option value="false" ${theMember.isEnabled?string('', 'selected="selected"')}>Disabled</option>
													</select>
											    </td>
											    <td class="c_7" style="display: none;">${theMember.id}</td>
												<td class="c_7" style="display: none;"></td>	
											</tr>
										</#list>
									</tbody>
				        		</table>
				        	</div>
						</div>
						<div class="selbox_add_right">
							<p class="mem_tit">${LANGCODEMAP["MSG_VSPASK_MODREAL_SERVER_LIST"]!} <span class="txt_blue">(<span class="txt_blue adcNodeCount">${(adcNodes![])?size}</span>)</span></p>								
				            <div class="headtable member-innerbox">
				                <table class="head" id="memberList" cellpadding="0" cellspacing="0">
									<thead>
				        		        <tr>
				                            <th>IP</th>
				            				<th>${LANGCODEMAP["MSG_VSPASK_MODPORT"]!}</th>
				                        </tr>
				        		    </thead>
				        		    <tbody class="adcNodeTbd">
										<#list adcNodes as theNode>
											<tr class="adcNodeTr">
												<td class="adcNodeIp align_left_P10">${theNode.ip}</td>
												<td class="adcNodePort align_center">${theNode.port}</td>
												<td class="adcNodeState" style="display: none;">${theNode.state}</td>
												<td class="adcNodeId" style="display: none;">${theNode.id}</td>
											</tr>
										</#list>
									</tbody>
								</table>
							</div>						
						</div>						
					</td>
		    	</tr>				
				<tr>
					<th class="Lth2">&nbsp;</th>
					<td class="Lth0">
						<input type="button" class="paskvsmodfiyOn enableMembersLnk Btn_white_small" value="Enable"/>
						<input type="button" class="paskvsmodfiyOn disableMembersLnk Btn_white_small" value="Disable"/>					
						<input type="button" class="paskvsmodfiyOn delMembersLnk Btn_white_small" value="${LANGCODEMAP["MSG_VSF5_DELETE"]!}"/>
					</td>                           
			    </tr>
			    <tr class="DivideLine">
			    	<td colspan="2"></td>
			    </tr>
				<tr>
					<th class="Lth2">
						<li>Load Balancing</li>
					</th>
					<td class="Lth0">
						<select name="paskVsAdd.loadBalancingType" id="select2" class="inputSelect width134">
							<#if (paskVsAdd.loadBalancingType)! == "NOT_ALLOWED">
								<option value="NOT_ALLOWED" selected="selected">Not Allowed</option>
							<#else>
								<option value="Round Robin" ${((paskVsAdd.loadBalancingType!'') == 'Round Robin')?string('selected="selected"', '')}>Round Robin</option>
								<option value="Least Connections" ${((paskVsAdd.loadBalancingType!'') == 'Least Connections')?string('selected="selected"', '')}>Least Connections</option>
								<option value="Hash" ${((paskVsAdd.loadBalancingType!'') == 'Hash')?string('selected="selected"', '')}>Hash</option>
							</#if>
						</select>
						&nbsp;
					</td>
				</tr>
				<tr class="DivideLine">
					<td colspan="2"></td>
				</tr>
				<tr>
					<th class="Lth1"><li>Health Check</li></th>
					<td class="Lth0" >
						<select name="paskVsAdd.healthCheckDbIndex" id="select2" class="inputSelect types healthCbx width134">
							<option value=null>${LANGCODEMAP["MSG_VSPASK_MODNOT_DESI"]!}</option>
							<#list adcHealths as theHealth>
							<#if theHealth.dbIndex == paskVsAdd.healthCheckDbIndex!''>
								<option value="${theHealth.dbIndex}" selected="selected">${theHealth.name}</option>
							<#else>
								<option value="${theHealth.dbIndex}">${theHealth.name}</option>
							</#if>
							</#list>
						</select>
					&nbsp;
					</td>
				</tr>
				<tr class="EndLine">
					<td colspan="2"></td>
				</tr>
	       		<td colspan="2">
					<div class="position_cT10">
						<input type="button" class="imgOn virtualServerAddOkLnk Btn_red" value="${LANGCODEMAP["MSG_VSPASK_MODCOMPLETE"]!}"/>
						<input type="button" class="imgOn virtualServerAddCancelLnk Btn_white" value="${LANGCODEMAP["MSG_VSPASK_MODCANCEL"]!}"/> 				
						<input type="button" class="imgOff none virtualServerAddCancelLnk Btn_white" value="${LANGCODEMAP["MSG_VSALTEON_CONFIRM"]!}"/> 								
					</div> 
				</td>
			</table>
			<div style="display:none;">
				VS State :<input name="paskVsAdd.state" type="text" value="${paskVsAdd.state!''}"/><span class="mar_lft10"></span>
				VS configurable :<input name="paskVsAdd.configurable" type="text" value="${paskVsAdd.configurable!''}"/><span class="mar_lft10"></span>
				Pool Name :<input type="text" name="paskVsAdd.poolName" value="${paskVsAdd.poolName!''}"/>
				${LANGCODEMAP["MSG_VSPASK_MODSERVICE_PROTOCOL"]!} :<input name="paskVsAdd.protocol" type="text" value="${paskVsAdd.protocol!''}"/>						
				IP View :<input name="paskVsAdd.ipView" type="text" value="${paskVsAdd.ipView!''}"/><span class="mar_lft10"></span>
				port view :<input name="paskVsAdd.portView" type="text" value="${paskVsAdd.portView!''}"/><span class="mar_lft10"></span>
				Vip Info :<input name="paskVsAdd.vipInfo" type="text" value="${paskVsAdd.vipInfo!''}"/><span class="mar_lft10"></span>
				<br>
				Helath DbIndex :<input name="paskVsAdd.healthCheckDbIndex" type="text" value="${paskVsAdd.healthCheckDbIndex!''}"/><span class="mar_lft10"></span>
				Helath ID :<input name="paskVsAdd.healthCheckId" type="text" value="${paskVsAdd.healthCheckId!''}"/><span class="mar_lft10"></span>
				Helath name :<input name="paskVsAdd.healthCheckName" type="text" value="${paskVsAdd.healthCheckName!''}"/><span class="mar_lft10"></span>
				Helath Type :<input name="paskVsAdd.healthCheckType" type="text" value="${paskVsAdd.healthCheckType!''}"/><span class="mar_lft10"></span>
				Helath port :<input name="paskVsAdd.healthCheckPort" type="text" value="${paskVsAdd.healthCheckPort!''}"/><span class="mar_lft10"></span>
				Helath Interval :<input name="paskVsAdd.healthCheckInterval" type="text" value="${paskVsAdd.healthCheckInterval!''}"/><span class="mar_lft10"></span>
				Helath timeout :<input name="paskVsAdd.healthCheckTimeout" type="text" value="${paskVsAdd.healthCheckTimeout!''}"/><span class="mar_lft10"></span>
				Helath State :<input name="paskVsAdd.healthCheckState" type="text" value="${paskVsAdd.healthCheckState!''}"/><span class="mar_lft10"></span>
			</div>
		<!-- 비활성화 (사용자 임의 수정 불가능) form -->	
		<#else>
			<table class="Board" cellpadding="0" cellspacing="0">
				<caption>&nbsp;</caption>
				<colgroup>
					<col width="200px"/>
	        		<col width="auto"/>
				</colgroup>
				<tr class="StartLine">
					<td colspan="2" ></td>
				</tr>
				<tr>
					<th class="Lth2">
						<li>${LANGCODEMAP["MSG_VSPASK_MODIP_ADDR"]!}</li>
					</th>
					<td class="Lth0">
						<input type="text" disabled="disabled" name="paskVsAdd.ipView" id="textfield2" class="inputText" value="${paskVsAdd.ipView!''}"/>
						<span class="mar_rgt10 blink" style="float: right;">
							<img src="imgs/common/icon_warn-1.png" alt="${LANGCODEMAP["MSG_VSPASK_MODUSED_NAME"]!}" />
							&nbsp;&nbsp;${LANGCODEMAP["MSG_VSPASK_MODVIP_HEALTH"]!}
						</span>
					</td>
				</tr>
				<tr class="DivideLine">
					<td colspan="2"></td>
				</tr>
				<tr>
					<th class="Lth2">
						<li>${LANGCODEMAP["MSG_VSPASK_MODNAME"]!}</li>
					</th>
					<td class="Lth0">
						<#if !(paskVsAdd.name)?? || (paskVsAdd.name)! == ''>
							<input id="textfield2" class="inputText" name="paskVsAdd.name" type="text" value="${paskVsAdd.name!''}"/>
								${LANGCODEMAP["MSG_VSPASK_MODFORMAT_VS_GROUP_NAME_SEQ"]!}&nbsp; 
							<span class="popUpVServerNameWndLnk">
								<input type="button" class="Btn_black_small" value="&nbsp;${LANGCODEMAP["MSG_VSPASK_MODUSED_NAME"]!}&nbsp;">
							</span>
						<#else>
							<input disabled="disabled" type="text" value="${paskVsAdd.name!''}"/>
							<input name="paskVsAdd.name" type="hidden" value="${paskVsAdd.name!''}"/>
						</#if>						
					</td>
		        </tr>
				<tr class="DivideLine">
					<td colspan="2"></td>
				</tr>
				<tr>
					<th class="Lth2">
						<li>${LANGCODEMAP["MSG_VSPASK_MODPORT"]!}</li>
					</th>
					<td class="Lth0">
						<select name="select2" id="select2" class="types inputSelect protocolCbx" disabled="disabled">
						<#if !(paskVsAdd.protocol)?? || ((paskVsAdd.protocol)?? && (paskVsAdd.protocol)! == 6)>
						  	<option selected="selected" value="">${LANGCODEMAP["MSG_VSPASK_MODPORT_DENI"]!}</option>
							<option value="21">FTP</option>
							<option value="80">HTTP</option>
							<option value="443">HTTPS</option>
							<option value="23">TELNET</option>
							<option value="25">SMTP</option>
							<option value="161">SNMP</option>
							<option value="162">SNMP-TRAP</option>
							<option value="22">SSH</option>
						<#else>
							<option value="NOT_ALLOWED" selected="selected">Not Allowed</option>
						</#if>
						</select>
						&nbsp;
						<input type="text" disabled="disabled" name="paskVsAdd.portView" id="textfield3" class="inputText" value="${paskVsAdd.portView!''}" />
					</td>
		        </tr>
				<tr class="DivideLine">
					<td colspan="2"></td>
				</tr>	
				<tr>
					<th class="Lth1">
						<li>Member</li>
					</th>
					<td>
						<div class="selbox_top">
							<span class="txt_bold">${LANGCODEMAP["MSG_VSPASK_MODMEMBER_ADD_IP_PORT"]!}</span>
							<input name="checkbox4" type="checkbox" id="enable" class="memberEnabledChk" value="true" checked="checked" disabled="disabled" />Enable <br/>							
							<input type="text" class="memberIpTxt inputText width130" disabled="disabled"/>
								: 
							<input type="text" class="memberPortTxt inputText width50" disabled="disabled" />
							<!--
							<input type="button" class="memberAddLnk Btn_black_small" value="${LANGCODEMAP["MSG_VSPASK_MODADD"]!}"/>
							<input type="button" class="onMemberAddBatch Btn_black_small" value="${LANGCODEMAP["MSG_VSPASK_MODMULTI_ADD"]!}"/>
							-->						
						</div>
						<div class="selbox_add_left">
							<p class="mem_tit">Member <span class="txt_blue">(<span class="txt_blue poolMemberCount">${(paskVsAdd.members![])?size}</span>)</span></p>						
				            <div class="headtable member-innerbox">
				                <table class="head" id="selectedMember" cellpadding="0" cellspacing="0">
									<thead>
				        		        <tr>
				                            <th class="align_center">
					            				<input class="allMembersChk" name="checkbox" type="checkbox" id="checkbox" disabled="disabled"/>
					            			</th>
					            			<th class="align_center">IP</th>
					           				<th class="align_center">${LANGCODEMAP["MSG_VSPASK_MODPORT"]!}</th>
					            			<th class="align_center">${LANGCODEMAP["MSG_VSPASK_MODSTATUS"]!}</th>
					            			<th style="display: none;">ID</th>
				                        </tr>
				        		    </thead>
				        		    <tbody class="memberTbd" disabled="disabled">
										<#list paskVsAdd.members![] as theMember>
											<tr class="regMemberTr">
												<td class="align_center"><input disabled="disabled" class="memberChk" name="checkbox" type="checkbox" id="checkbox" /></td>
										    	<td class="align_left_P10" style="display: none;">${theMember.index}</td>
											    <td class="align_left_P10">${theMember.ip}</td>
											    <td class="align_center">${theMember.port}</td>
											    <td class="align_center"> 
											    	<select disabled="disabled">
														<option value="true" ${theMember.isEnabled?string('selected="selected"', '')}>Enabled</option>
														<option value="false" ${theMember.isEnabled?string('', 'selected="selected"')}>Disabled</option>
													</select>
											    </td>
											    <td class="c_7" style="display: none;">${theMember.id}</td>
												<td class="c_7" style="display: none;"></td>	
											</tr>
										</#list>
									</tbody>
				        		</table>
				        	</div>
						</div>
						<div class="selbox_add_right">
							<p class="mem_tit">${LANGCODEMAP["MSG_VSPASK_MODREAL_SERVER_LIST"]!} <span class="txt_blue">(<span class="txt_blue adcNodeCount">${(adcNodes![])?size}</span>)</span></p>								
				            <div class="headtable member-innerbox">
				                <table class="head" id="memberList" cellpadding="0" cellspacing="0">
									<thead>
				        		        <tr>
				                            <th>IP</th>
				            				<th>${LANGCODEMAP["MSG_VSPASK_MODPORT"]!}</th>
				                        </tr>
				        		    </thead>
				        		    <tbody class="adcNodeTbd" disabled="disabled">
										<#list adcNodes as theNode>
											<tr class="adcNodeTr">
												<td class="adcNodeIp align_left_P10">${theNode.ip}</td>
												<td class="adcNodePort align_center">${theNode.port}</td>
												<td class="adcNodeState" style="display: none;">${theNode.state}</td>
												<td class="adcNodeId" style="display: none;">${theNode.id}</td>
											</tr>
										</#list>
									</tbody>
								</table>
							</div>						
						</div>
					</td>
		    	</tr>
			
				<!--
				<tr>
					<th class="Lth2">&nbsp;</th>
					<td class="Lth0">
						<input type="button" class="paskvsmodfiyOn enableMembersLnk Btn_white_small" value="Enable"/>
						<input type="button" class="paskvsmodfiyOn disableMembersLnk Btn_white_small" value="Disable"/>					
						<input type="button" class="paskvsmodfiyOn delMembersLnk Btn_white_small" value="${LANGCODEMAP["MSG_VSF5_DELETE"]!}"/>
						<input type="button" class="paskvsmodfiyOn changeMemberPortsLnk Btn_white_small" value="${LANGCODEMAP["MSG_VSPAS_PORT_CHANGE"]!}"/>							
					</td>
			    </tr>
			    -->
			    <tr class="DivideLine">
			    	<td colspan="2"></td>
			    </tr>
				<tr>
					<th class="Lth2">
						<li>Load Balancing</li>
					</th>
					<td class="Lth0">
						<select name="paskVsAdd.loadBalancingType" id="select2" class="inputSelect" disabled="disabled">
							<#if (paskVsAdd.loadBalancingType)! == "NOT_ALLOWED">
								<option value="NOT_ALLOWED" selected="selected">Not Allowed</option>
							<#else>
								<option value="Round Robin" ${((paskVsAdd.loadBalancingType!'') == 'Round Robin')?string('selected="selected"', '')}>Round Robin</option>
								<option value="Least Connections" ${((paskVsAdd.loadBalancingType!'') == 'Least Connections')?string('selected="selected"', '')}>Least Connections</option>
								<option value="Hash" ${((paskVsAdd.loadBalancingType!'') == 'Hash')?string('selected="selected"', '')}>Hash</option>
							</#if>
						</select>
						&nbsp;
					</td>
				</tr>
				<tr class="DivideLine">
					<td colspan="2"></td>
				</tr>
				<tr>
					<th class="Lth1">
						<li>Health Check</li>
					</th>
					<td class="Lth0">
						<select name="paskVsAdd.healthCheckDbIndex" id="select2" class="inputSelect types healthCbx width134" disabled="disabled">
							<option value=null>${LANGCODEMAP["MSG_VSPASK_MODNOT_DESI"]!}</option>
							<#list adcHealths as theHealth>
							<#if theHealth.dbIndex == paskVsAdd.healthCheckDbIndex!''>
								<option value="${theHealth.dbIndex}" selected="selected">${theHealth.name}</option>
							<#else>
								<option value="${theHealth.dbIndex}">${theHealth.name}</option>
							</#if>
							</#list>
						</select>
						&nbsp;
					</td>
				</tr>
				<tr class="EndLine">
					<td colspan="2"></td>
				</tr>
		        <td colspan="2">
					<div class="position_cT10">						
						<span>
							<input class="Btn_white virtualServerAddCancelLnk" type="button" value="${LANGCODEMAP["MSG_VSPASK_MODCANCEL"]!}">
						</span>
					</div> 
				</td>
			</table>
			<div style="display:none;">
				VS State :<input name="paskVsAdd.state" type="text" value="${paskVsAdd.state!''}"/><span class="mar_lft10"></span>
				VS configurable :<input name="paskVsAdd.configurable" type="text" value="${paskVsAdd.configurable!''}"/><span class="mar_lft10"></span>
				Pool Name :<input type="text" name="paskVsAdd.poolName" value="${paskVsAdd.poolName!''}"/>
				${LANGCODEMAP["MSG_VSPASK_MODSERVICE_PROTOCOL"]!} :<input name="paskVsAdd.protocol" type="text" value="${paskVsAdd.protocol!''}"/>						
				IP View :<input name="paskVsAdd.ipView" type="text" value="${paskVsAdd.ipView!''}"/><span class="mar_lft10"></span>
				port view :<input name="paskVsAdd.portView" type="text" value="${paskVsAdd.portView!''}"/><span class="mar_lft10"></span>
				Vip Info :<input name="paskVsAdd.vipInfo" type="text" value="${paskVsAdd.vipInfo!''}"/><span class="mar_lft10"></span>
				<br>
				Helath DbIndex :<input name="paskVsAdd.healthCheckDbIndex" type="text" value="${paskVsAdd.healthCheckDbIndex!''}"/><span class="mar_lft10"></span>
				Helath ID :<input name="paskVsAdd.healthCheckId" type="text" value="${paskVsAdd.healthCheckId!''}"/><span class="mar_lft10"></span>
				Helath name :<input name="paskVsAdd.healthCheckName" type="text" value="${paskVsAdd.healthCheckName!''}"/><span class="mar_lft10"></span>
				Helath Type :<input name="paskVsAdd.healthCheckType" type="text" value="${paskVsAdd.healthCheckType!''}"/><span class="mar_lft10"></span>
				Helath port :<input name="paskVsAdd.healthCheckPort" type="text" value="${paskVsAdd.healthCheckPort!''}"/><span class="mar_lft10"></span>
				Helath Interval :<input name="paskVsAdd.healthCheckInterval" type="text" value="${paskVsAdd.healthCheckInterval!''}"/><span class="mar_lft10"></span>
				Helath timeout :<input name="paskVsAdd.healthCheckTimeout" type="text" value="${paskVsAdd.healthCheckTimeout!''}"/><span class="mar_lft10"></span>
				Helath State :<input name="paskVsAdd.healthCheckState" type="text" value="${paskVsAdd.healthCheckState!''}"/><span class="mar_lft10"></span>
			</div>
		</#if>
	</form>
	<#else>	
	<!-- READ ONLY USER -->
	<form id="paskVsAddFrm" method="post">
		<div style="display:none;">
			<input name="paskVsAdd.index" type="text" value="${paskVsAdd.index!}"/>
			<input name="paskVsAdd.adcIndex" type="text" value="${paskVsAdd.adcIndex}"/>
		</div>		
		<table class="Board" cellpadding="0" cellspacing="0">
			<caption>&nbsp;</caption>
			<colgroup>
				<col width="200px"/>
	        	<col width="auto"/>
			</colgroup>
			<tr class="StartLine">
				<td colspan="2" ></td>
			</tr>
			<tr>
				<th class="Lth2">
					<li>${LANGCODEMAP["MSG_VSPASK_MODIP_ADDR"]!}</li>
				</th>
				<td class="Lth0">
					<input type="text" disabled="disabled" name="paskVsAdd.ipView" id="textfield2" class="inputText" value="${paskVsAdd.ipView!''}"/>
				</td>
			</tr>
			<tr class="DivideLine">
				<td colspan="2"></td>
			</tr>
			<tr>
				<th class="Lth2">
					<li>${LANGCODEMAP["MSG_VSPASK_MODNAME"]!}</li>
				</th>
				<td class="Lth0">
					<#if !(paskVsAdd.name)?? || (paskVsAdd.name)! == ''>
						<input id="textfield2" class="inputText" name="paskVsAdd.name" type="text" value="${paskVsAdd.name!''}"/>
							${LANGCODEMAP["MSG_VSPASK_MODFORMAT_VS_GROUP_NAME_SEQ"]!}&nbsp; 
						<span class="popUpVServerNameWndLnk">
							<input type="button" class="Btn_black_small" value="&nbsp;${LANGCODEMAP["MSG_VSPASK_MODUSED_NAME"]!}&nbsp;">
						</span>
					<#else>
						<input disabled="disabled" type="text" value="${paskVsAdd.name!''}"/>
						<input name="paskVsAdd.name" type="hidden" value="${paskVsAdd.name!''}"/>
					</#if>						
				</td>
		    </tr>
			<tr class="DivideLine">
				<td colspan="2"></td>
			</tr>
			<tr>
				<th class="Lth2">
					<li>${LANGCODEMAP["MSG_VSPASK_MODPORT"]!}</li>
				</th>
				<td class="Lth0">
					<select name="select2" id="select2" class="types inputSelect protocolCbx" disabled="disabled">
					<#if !(paskVsAdd.protocol)?? || ((paskVsAdd.protocol)?? && (paskVsAdd.protocol)! == 6)>
					  	<option selected="selected" value="">${LANGCODEMAP["MSG_VSPASK_MODPORT_DENI"]!}</option>
						<option value="21">FTP</option>
						<option value="80">HTTP</option>
						<option value="443">HTTPS</option>
						<option value="23">TELNET</option>
						<option value="25">SMTP</option>
						<option value="161">SNMP</option>
						<option value="162">SNMP-TRAP</option>
						<option value="22">SSH</option>
					<#else>
						<option value="NOT_ALLOWED" selected="selected">Not Allowed</option>
					</#if>
					</select>
					&nbsp;
					<input type="text" disabled="disabled" name="paskVsAdd.portView" id="textfield3" class="inputText" value="${paskVsAdd.portView!''}" />
				</td>
		    </tr>
			<tr class="DivideLine">
				<td colspan="2"></td>
			</tr>	
			<tr>
				<th class="Lth1">
					<li>Member</li>
				</th>
				<td>
					<div class="selbox_top">
						<span class="txt_bold">${LANGCODEMAP["MSG_VSPASK_MODMEMBER_ADD_IP_PORT"]!}</span>
						<input name="checkbox4" type="checkbox" id="enable" class="memberEnabledChk" value="true" checked="checked" disabled="disabled" />Enable <br/>
						<input type="text" class="memberIpTxt inputText width130" disabled="disabled"/>
							: 
						<input type="text" class="memberPortTxt inputText width50" disabled="disabled"/>
						<!-- 
						<input type="button" class="memberAddLnk Btn_black_small" value="${LANGCODEMAP["MSG_VSPASK_MODADD"]!}"/>
						<input type="button" class="onMemberAddBatch Btn_black_small" value="${LANGCODEMAP["MSG_VSPASK_MODMULTI_ADD"]!}"/>
						-->						
					</div>
					<div class="selbox_add_left">
						<p class="mem_tit">Member <span class="txt_blue">(<span class="txt_blue poolMemberCount">${(paskVsAdd.members![])?size}</span>)</span></p>						
			            <div class="headtable member-innerbox">
			                <table class="head" id="selectedMember" cellpadding="0" cellspacing="0">
								<thead>
			        		        <tr>
			                            <th class="align_center">
				            				<input class="allMembersChk" name="checkbox" type="checkbox" id="checkbox" disabled="disabled"/>
				            			</th>
				            			<th class="align_center">IP</th>
				           				<th class="align_center">${LANGCODEMAP["MSG_VSPASK_MODPORT"]!}</th>
				            			<th class="align_center">${LANGCODEMAP["MSG_VSPASK_MODSTATUS"]!}</th>
				            			<th style="display: none;">ID</th>
			                        </tr>
			        		    </thead>
			        		    <tbody class="memberTbd" disabled="disabled">
									<#list paskVsAdd.members![] as theMember>
										<tr class="regMemberTr">
											<td class="align_center"><input disabled="disabled" class="memberChk" name="checkbox" type="checkbox" id="checkbox" /></td>
									    	<td class="align_left_P10" style="display: none;">${theMember.index}</td>
										    <td class="align_left_P10">${theMember.ip}</td>
										    <td class="align_center">${theMember.port}</td>
										    <td class="align_center"> 
										    	<select disabled="disabled">
													<option value="true" ${theMember.isEnabled?string('selected="selected"', '')}>Enabled</option>
													<option value="false" ${theMember.isEnabled?string('', 'selected="selected"')}>Disabled</option>
												</select>
										    </td>
										    <td class="c_7" style="display: none;">${theMember.id}</td>
											<td class="c_7" style="display: none;"></td>	
										</tr>
									</#list>
								</tbody>
			        		</table>
			        	</div>
					</div>
					<div class="selbox_add_right">
						<p class="mem_tit">${LANGCODEMAP["MSG_VSPASK_MODREAL_SERVER_LIST"]!} <span class="txt_blue">(<span class="txt_blue adcNodeCount">${(adcNodes![])?size}</span>)</span></p>								
			            <div class="headtable member-innerbox">
			                <table class="head" id="memberList" cellpadding="0" cellspacing="0">
								<thead>
			        		        <tr>
			                            <th>IP</th>
			            				<th>${LANGCODEMAP["MSG_VSPASK_MODPORT"]!}</th>
			                        </tr>
			        		    </thead>
			        		    <tbody class="adcNodeTbd" disabled="disabled">
									<#list adcNodes as theNode>
										<tr class="adcNodeTr">
											<td class="adcNodeIp align_left_P10">${theNode.ip}</td>
											<td class="adcNodePort align_center">${theNode.port}</td>
											<td class="adcNodeState" style="display: none;">${theNode.state}</td>
											<td class="adcNodeId" style="display: none;">${theNode.id}</td>
										</tr>
									</#list>
								</tbody>
							</table>
						</div>						
					</div>
				</td>
			</tr>			
			<!--
			<tr>
				<th class="Lth2">&nbsp;</th>
				<td class="Lth0">
					<input type="button" class="paskvsmodfiyOn enableMembersLnk Btn_white_small" value="Enable"/>
					<input type="button" class="paskvsmodfiyOn disableMembersLnk Btn_white_small" value="Disable"/>					
					<input type="button" class="paskvsmodfiyOn delMembersLnk Btn_white_small" value="${LANGCODEMAP["MSG_VSF5_DELETE"]!}"/>
					<input type="button" class="paskvsmodfiyOn changeMemberPortsLnk Btn_white_small" value="${LANGCODEMAP["MSG_VSPAS_PORT_CHANGE"]!}"/>							
				</td>
		    </tr>
		    -->
		    <tr class="DivideLine">
		    	<td colspan="2"></td>
		    </tr>
			<tr>
				<th class="Lth2">
					<li>Load Balancing</li>
				</th>
				<td class="Lth0">
					<select name="paskVsAdd.loadBalancingType" id="select2" class="inputSelect" disabled="disabled">
						<#if (paskVsAdd.loadBalancingType)! == "NOT_ALLOWED">
							<option value="NOT_ALLOWED" selected="selected">Not Allowed</option>
						<#else>
							<option value="Round Robin" ${((paskVsAdd.loadBalancingType!'') == 'Round Robin')?string('selected="selected"', '')}>Round Robin</option>
							<option value="Least Connections" ${((paskVsAdd.loadBalancingType!'') == 'Least Connections')?string('selected="selected"', '')}>Least Connections</option>
							<option value="Hash" ${((paskVsAdd.loadBalancingType!'') == 'Hash')?string('selected="selected"', '')}>Hash</option>
						</#if>
					</select>
					&nbsp;
				</td>
			</tr>
			<tr class="DivideLine">
				<td colspan="2"></td>
			</tr>
			<tr>
				<th class="Lth1">
					<li>Health Check</li>
				</th>
				<td class="Lth0">
					<select name="paskVsAdd.healthCheckDbIndex" id="select2" class="inputSelect types healthCbx width134" disabled="disabled">
						<option value=null>${LANGCODEMAP["MSG_VSPASK_MODNOT_DESI"]!}</option>
						<#list adcHealths as theHealth>
						<#if theHealth.dbIndex == paskVsAdd.healthCheckDbIndex!''>
							<option value="${theHealth.dbIndex}" selected="selected">${theHealth.name}</option>
						<#else>
							<option value="${theHealth.dbIndex}">${theHealth.name}</option>
						</#if>
						</#list>
					</select>
					&nbsp;
				</td>
			</tr>
			<tr class="EndLine">
				<td colspan="2"></td>
			</tr>
		    <td colspan="2">
				<div class="position_cT10">						
					<span>
						<input class="Btn_white virtualServerAddCancelLnk" type="button" value="${LANGCODEMAP["MSG_VSPASK_MODCANCEL"]!}">
					</span>
				</div> 
			</td>
		</table>
		<div style="display:none;">
			VS State :<input name="paskVsAdd.state" type="text" value="${paskVsAdd.state!''}"/><span class="mar_lft10"></span>
			VS configurable :<input name="paskVsAdd.configurable" type="text" value="${paskVsAdd.configurable!''}"/><span class="mar_lft10"></span>
			Pool Name :<input type="text" name="paskVsAdd.poolName" value="${paskVsAdd.poolName!''}"/>
			${LANGCODEMAP["MSG_VSPASK_MODSERVICE_PROTOCOL"]!} :<input name="paskVsAdd.protocol" type="text" value="${paskVsAdd.protocol!''}"/>						
			IP View :<input name="paskVsAdd.ipView" type="text" value="${paskVsAdd.ipView!''}"/><span class="mar_lft10"></span>
			port view :<input name="paskVsAdd.portView" type="text" value="${paskVsAdd.portView!''}"/><span class="mar_lft10"></span>
			Vip Info :<input name="paskVsAdd.vipInfo" type="text" value="${paskVsAdd.vipInfo!''}"/><span class="mar_lft10"></span>
			<br>
			Helath DbIndex :<input name="paskVsAdd.healthCheckDbIndex" type="text" value="${paskVsAdd.healthCheckDbIndex!''}"/><span class="mar_lft10"></span>
			Helath ID :<input name="paskVsAdd.healthCheckId" type="text" value="${paskVsAdd.healthCheckId!''}"/><span class="mar_lft10"></span>
			Helath name :<input name="paskVsAdd.healthCheckName" type="text" value="${paskVsAdd.healthCheckName!''}"/><span class="mar_lft10"></span>
			Helath Type :<input name="paskVsAdd.healthCheckType" type="text" value="${paskVsAdd.healthCheckType!''}"/><span class="mar_lft10"></span>
			Helath port :<input name="paskVsAdd.healthCheckPort" type="text" value="${paskVsAdd.healthCheckPort!''}"/><span class="mar_lft10"></span>
			Helath Interval :<input name="paskVsAdd.healthCheckInterval" type="text" value="${paskVsAdd.healthCheckInterval!''}"/><span class="mar_lft10"></span>
			Helath timeout :<input name="paskVsAdd.healthCheckTimeout" type="text" value="${paskVsAdd.healthCheckTimeout!''}"/><span class="mar_lft10"></span>
			Helath State :<input name="paskVsAdd.healthCheckState" type="text" value="${paskVsAdd.healthCheckState!''}"/><span class="mar_lft10"></span>
		</div>		
	</form>
	</#if>
</div>

<!-- 일괄 추가 팝업 -->
<div id="memberAddBatch" class="pop_type_wrap">
	<h2>${LANGCODEMAP["MSG_VSPASK_MODGB_MULTI_ADD"]!}</h2>
	<div class="pop_contents">
		<p class="ptop mar_top26"><span>${LANGCODEMAP["MSG_VSPASK_MODFORMAT"]!}</span>${LANGCODEMAP["MSG_VSPASK_MODINPUT_IP_PORT"]!}</p>
		<p class="ptop none_bt"><span>${LANGCODEMAP["MSG_VSPASK_MODUSE_EX"]!}</span>192.168.110.11:80</p>
			<textarea name="ipsTxt" rows="10" style="width:95%; resize:none;" ></textarea>
		<p class="center pbtm">
			<!--<span class="txt">&nbsp;</span>-->
			<span><input type="button" class="Btn_red onOk" value="${LANGCODEMAP["MSG_VSPASK_MODCOMPLETE"]!}"></span>
			<span><input type="button" class="Btn_white onCancel" value="${LANGCODEMAP["MSG_VSPASK_MODCANCEL"]!}"></span>	
		</p>
	</div>
	<p class="close">
		<a href="#" title="${LANGCODEMAP["MSG_VSPASK_MODCLOSE"]!}">
			<img src="imgs/popup/btn_clse.gif" alt="${LANGCODEMAP["MSG_VSPASK_MODCLOSE"]!}" />
		</a>
	</p>
</div>
<!-- //일괄 추가 팝업 -->

<!-- 포트 변경 팝업 -->
<div id="portChange" class="pop_type_wrap">
	<h2>${LANGCODEMAP["MSG_VSPASK_MODPORT_CHANGE"]!}</h2>
	<div class="pop_contents">
		<p class="setport"><span>${LANGCODEMAP["MSG_VSPASK_MODPORT_NUMBER"]!}</span><input class="portNo" type="text"/></p>
		<p class="center mar_top10">
			<span><input type="button" class="Btn_red onOk" value="${LANGCODEMAP["MSG_VSPASK_MODCOMPLETE"]!}"></span>
			<span><input type="button" class="Btn_white onCancel" value="${LANGCODEMAP["MSG_VSPASK_MODCANCEL"]!}"></span>	
		</p>
	</div>
	<p class="close">
		<a href="#" title="${LANGCODEMAP["MSG_VSPASK_MODCLOSE"]!}"><img src="imgs/popup/btn_clse.gif" alt="${LANGCODEMAP["MSG_VSPASK_MODCLOSE"]!}" /></a>
	</p>
</div>
<!-- //포트 변경 팝업 -->

<!-- virtualserver ip 팝업 -->
<div id="vs_ipList" class="pop_type_wrap">
	<h2>${LANGCODEMAP["MSG_VSPASK_MODVS_IP_LIST"]!}</h2>
	<div class="pop_contents">
		<p class="sch">
			<input class="searchTxt" type="text" value="" />
			<a class="searchLnk" href="#">
				<img src="imgs/layout/btn_search2.gif" alt="${LANGCODEMAP["MSG_VSPASK_LISTSEARCH"]!}" />
			</a>
		</p>
		<div class="listWrap">
			<table id="vsIP" class="table_type1" summary="virtual server list">
				<caption>virtual server list</caption>
				<colgroup>
					<col width="10%" />
					<col width="45%" />
					<col width="45%" />
				</colgroup>
				<thead>
					<tr>
						<th>${LANGCODEMAP["MSG_VSPASK_MODNUMBER"]!}</th>
						<th>${LANGCODEMAP["MSG_VSPASK_MODVS_NAME"]!}</th>
						<th>${LANGCODEMAP["MSG_VSPASK_MODIP_ADDR"]!}</th>
					</tr>
				</thead>
				<tbody class="vServerNameTbd"></tbody>
			</table>
		</div>
		<p class="center mar_top10">
			<span>
				<input type="button" class="Btn_white closeLnk" value="${LANGCODEMAP["MSG_VSPASK_MODCLOSE"]!}">
			</span>	
		</p>
	</div>
	<p class="close">
		<a class="closeWndLnk" href="#" title="${LANGCODEMAP["MSG_VSPASK_MODCLOSE"]!}">
			<img src="imgs/popup/btn_clse.gif" alt="${LANGCODEMAP["MSG_VSPASK_MODCLOSE"]!}" />
		</a>
	</p>
</div>
<!-- //virtualserver ip 팝업 팝업 -->

<script type="text/javascript">
	$(function() 
	{
		//리스트형 테이블 해더 고정을 위한 함수
		$('.ipList').fixheadertable({
			height : 200, colratio:['percent',10,40,25,25]
		});
		
		//tableHeadSize('#selectedMember', 3);
		//tableHeadSize('#memberList', 3);
		
		// 리스트형 테이블 행의 첫번째 열 배경색 지정
		//$('.ipList > tbody > tr').each(function() {
		//	$(this).children(":eq(1)").css({
		//		'background-color' : '#e7e8ed',
		//		'border-right-weight' : '0'
		//	});
		//	$(this).children(":eq(2)").css({
		//		'background-color' : '#dbdde5',
		//		'border-right-weight' : '0'
		//	});
		//});
		
		// 리스트형 테이블 행 롤오버 효과
		$('.ipList tbody tr:not(.empty)').hover(function() {
			$(this).css({
				'background-color' : '#ccc',
				'cursor' : 'pointer'
			});
			//$(this).children(":eq(1)").removeAttr('style');
			//$(this).children(":eq(2)").removeAttr('style');
		}, function() {
			$(this).removeAttr('style');
			//$(this).children(":eq(1)").css('background-color', '#e7e8ed');
			//$(this).children(":eq(2)").css('background-color', '#dbdde5');
		});
	});
</script>