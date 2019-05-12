<link type="text/css" rel="stylesheet" href="/js/extern/themes/smoothness/jquery-ui.css" />
<#setting number_format="0.####">
<#if langCode??>
  <#if "ko_KR" == langCode><#assign img_lang = ""></#if>
  <#if "en_US" == langCode><#assign img_lang = "_eng"></#if>
</#if>
<#if accountRole != 'readOnly'>
<div class="contents_area">
<div>
	<#if !(pasVsAdd.index)??>
		<img src="imgs/title${img_lang!""}/h3_vsAdd.gif" class="title_h3" />	
	<#else>		
		<img src="imgs/title${img_lang!""}/h3_vsModify.gif" class="title_h3" />
	</#if>	 
</div>
<form id="pasVsAddFrm" method="post">
	<table class="Board" cellpadding="0" cellspacing="0">
		<caption>&nbsp;</caption>
		<colgroup>
			<col width="200px"/>
	        <col width="auto"/>
		</colgroup>
		<div style="display:none;">
			<input name="pasVsAdd.index" type="text" value="${pasVsAdd.index!}"/>
			<input name="pasVsAdd.adcIndex" type="text" value="${pasVsAdd.adcIndex}"/>
		</div>
		<tr class="StartLine">
			<td colspan="2"></td>
		</tr>
		<tr>
			<th class="Lth2">
				<li>${LANGCODEMAP["MSG_VSPAS_IP_ADDR"]!}</li>
			</th>
			<td class="Lth0">
				<input  disabled="disabled" type="text" name="pasVsAdd.ip" id="textfield2" class="inputText width130" value="${pasVsAdd.ip!''}"/><span class="mar_lft10"></span>
				<input name="pasVsAdd.state" type="hidden" value="${pasVsAdd.state!''}"/><span class="mar_lft10"></span>
			</td>
		</tr>
		<tr class="DivideLine">
			<td colspan="2"></td>
		</tr>
		<tr>
			<th class="Lth2">
				<li>${LANGCODEMAP["MSG_VSPAS_NAME"]!}</li>
			</th>
			<td class="Lth0">
			<#if !(pasVsAdd.name)?? || (pasVsAdd.name)! == ''>
				<input id="idVsName" class="inputText width130" name="pasVsAdd.name" type="text" value="${pasVsAdd.name!''}"/>&nbsp;				
				<input type="button" class="popUpVServerNameWndLnk Btn_black_small" value="${LANGCODEMAP["MSG_VSPAS_USED_NAME"]!}"/>  
			<#else>
				<input disabled="disabled" class="inputText width130" type="text" value="${pasVsAdd.name!''}"/>
				<input name="pasVsAdd.name" class="inputText width130" type="hidden" value="${pasVsAdd.name!''}"/>
			</#if>
				<input type="hidden" name="pasVsAdd.poolName" value="${pasVsAdd.poolName!''}"/>	
			</td>
        </tr>
		<tr class="DivideLine">
			<td colspan="2"></td>
		</tr>
		<tr>
			<th class="Lth2">
				<li>${LANGCODEMAP["MSG_VSPAS_PORT"]!}</li>
			</th>
			<td class="Lth0">
				<input name="pasVsAdd.protocol" type="hidden" value="${pasVsAdd.protocol!''}"/>
				<select name="select2" id="select2" class="types inputSelect protocolCbx width134" disabled="disabled">
				<#if !(pasVsAdd.protocol)?? || ((pasVsAdd.protocol)?? && (pasVsAdd.protocol)! == 6)>
				  	<option selected="selected" value="">${LANGCODEMAP["MSG_VSPAS_PORT_DESI"]!}</option>
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
				<#if !(pasVsAdd.protocol)?? || (pasVsAdd.protocol)?? && (pasVsAdd.protocol)! == 6>
				&nbsp;<input  disabled="disabled" name="pasVsAdd.port" class="inputText width130" type="text" value="${pasVsAdd.port!''}" />
				<#else>								
				&nbsp;<input name="pasVsAdd.port" class="inputText width130" type="text" value="${pasVsAdd.port!''}" readonly/>								
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
					<span class="txt_bold">${LANGCODEMAP["MSG_VSPAS_MEMBER_ADD_IP_PORT"]!}</span>
					<input disabled="disabled" name="checkbox4" type="checkbox" id="enable" class="memberEnabledChk" value="true" checked="checked" /> Enable <br/>
					<input disabled="disabled" type="text" class="memberIpTxt inputText width130" />
						: 
					<input disabled="disabled" type="text" class="memberPortTxt inputText width50" /> 
					<!--
					<input type="button" class="memberAddLnk Btn_black_small" value="${LANGCODEMAP["MSG_VSPAS_ADD"]!}"/>
					<input type="button" class="onMemberAddBatch Btn_black_small" value="${LANGCODEMAP["MSG_VSPAS_MULTI_ADD"]!}"/>
					-->						
				</div>				
				<div class="selbox_add_left">
					<p class="mem_tit">Member <span class="txt_blue">(<span class="txt_blue poolMemberCount">${(pasVsAdd.members![])?size}</span>)</span></p>						
		            <div class="headtable member-innerbox">
		                <table class="head" id="selectedMember" cellpadding="0" cellspacing="0">
							<thead>
		        		        <tr>
		                            <th class="align_center">
			            				<input disabled="disabled" class="allMembersChk" name="checkbox" type="checkbox" id="checkbox"/>
			            			</th>
			            			<th class="align_center">IP</th>
			           				<th class="align_center">${LANGCODEMAP["MSG_VSPAS_PORT"]!}</th>
			            			<th class="align_center">${LANGCODEMAP["MSG_VSPAS_STATUS"]!}</th>
		                        </tr>
		        		    </thead>
		        		    <tbody class="memberTbd">
							<#list pasVsAdd.members![] as theMember>
								<tr class="regMemberTr">
									<td class="align_center">
										<input disabled="disabled" class="memberChk" name="checkbox" type="checkbox" id="checkbox"/>
									</td>
								    <td class="align_left_P10">${theMember.ip}</td>
								    <td class="align_center">${theMember.port}</td>
								    <td class="align_center"> 
								    	<select disabled="disabled">
											<option value="true" ${theMember.isEnabled?string('selected="selected"', '')}>Enabled</option>
											<option value="false" ${theMember.isEnabled?string('', 'selected="selected"')}>Disabled</option>
										</select>
								    </td>
								    <td class="c_7" style="display: none;">${theMember.id}</td>
								</tr>
							</#list>
							</tbody>	
		        		</table>
		        	</div>
				</div>
				<div class="selbox_add_right">
					<p class="mem_tit">${LANGCODEMAP["MSG_VSPAS_REAL_SERVER_LIST"]!} <span class="txt_blue">(<span class="txt_blue adcNodeCount">${(adcNodes![])?size}</span>)</span></p>								
		            <div class="headtable member-innerbox">
		                <table class="head" id="memberList" cellpadding="0" cellspacing="0">
							<thead>
		        		        <tr>
		                            <th>IP</th>
		                        </tr>
		        		    </thead>
		        		    <tbody class="adcNodeTbd" disabled="disabled">
								<#list adcNodes as theNode>
									<tr>
										<td class="adcNodeTd">${theNode.ip}</td>
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
				<input type="button" class="pasvsmodfiyOn enableMembersLnk Btn_white_small" value="Enable"/>
				<input type="button" class="pasvsmodfiyOn disableMembersLnk Btn_white_small" value="Disable"/>					
				<input type="button" class="pasvsmodfiyOn delMembersLnk Btn_white_small" value="${LANGCODEMAP["MSG_VSPAS_DEL"]!}"/>
				<input type="button" class="pasvsmodfiyOn changeMemberPortsLnk Btn_white_small" value="${LANGCODEMAP["MSG_VSPAS_PORT_CHANGE"]!}"/>	
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
				<select disabled="disabled" name="pasVsAdd.loadBalancingType" id="select2" class="inputSelect">
					<#if (pasVsAdd.loadBalancingType)! == "NOT_ALLOWED">
						<option value="NOT_ALLOWED" selected="selected">Not Allowed</option>
					<#else>
						<option value="Round Robin" ${((pasVsAdd.loadBalancingType!'') == 'Round Robin')?string('selected="selected"', '')}>Round Robin</option>
						<option value="Least Connections" ${((pasVsAdd.loadBalancingType!'') == 'Least Connections')?string('selected="selected"', '')}>Least Connections</option>
						<option value="Hash" ${((pasVsAdd.loadBalancingType!'') == 'Hash')?string('selected="selected"', '')}>Hash</option>						
					</#if>
				</select>
				&nbsp;
			</td>
		</tr>
		<tr class="DivideLine">
			<td colspan="2"></td>
		</tr>
		<tr>
			<th class="Lth2">
				<li>Health Check</li>
			</th>
			<td class="Lth0">
				<select disabled="disabled" name="pasVsAdd.healthCheckType" id="select2" class="inputSelect width134">
					<#if (pasVsAdd.healthCheckType)! == "NOT_ALLOWED">
						<option value="NOT_ALLOWED" selected="selected">Not Allowed</option>
					<#else>
						<option value="NONE" ${((pasVsAdd.healthCheckType!'') == 'NONE')?string('selected="selected"', '')}>${LANGCODEMAP["MSG_VSPAS_NOT_DESI"]!}</option>
						<option value="TCP" ${((pasVsAdd.healthCheckType!'') == 'TCP')?string('selected="selected"', '')}>TCP</option>
						<option value="HTTP" ${((pasVsAdd.healthCheckType!'') == 'HTTP')?string('selected="selected"', '')}>HTTP</option>
						<option value="ICMP" ${((pasVsAdd.healthCheckType!'') == 'ICMP')?string('selected="selected"', '')}>ICMP</option>
					</#if>
				</select>
				<input type="hidden" name="pasVsAdd.healthCheckId" value="${pasVsAdd.healthCheckId!''}"/>
				&nbsp;
			</td>
		</tr>
		<tr class="EndLine">
			<td colspan="2"></td>
		</tr>
        <td colspan="2">
		<div class="position_cT10">
			<span>
				<input class="Btn_white virtualServerAddCancelLnk" type="button" value="${LANGCODEMAP["MSG_VSPAS_CANCEL"]!}">
			</span>
		</div> 
		</td>
	</table>
</form>
<#else>
<!-- READ ONLY USER -->
<div>
	<img src="imgs/title${img_lang!""}/h3_vsAdd.gif" class="title_h3" />		
</div>
<form id="pasVsAddFrm" method="post">
	<table class="Board" cellpadding="0" cellspacing="0">
		<caption>&nbsp;</caption>
		<colgroup>
			<col width="200px"/>
			<col >
	        <col width="31%"/>
		</colgroup>
		<div style="display:none;">
			<input name="pasVsAdd.index" type="text" value="${pasVsAdd.index!}"/>
			<input name="pasVsAdd.adcIndex" type="text" value="${pasVsAdd.adcIndex}"/>
		</div>
		<tr class="StartLine">
			<td colspan="3"></td>
		</tr>
		<tr>
			<th class="Lth2">
				<li>${LANGCODEMAP["MSG_VSPAS_IP_ADDR"]!}</li>
			</th>
			<td colspan="2" class="Lth0">
				<input type="text" name="pasVsAdd.ip" id="textfield2" class="inputText width130" value="${pasVsAdd.ip!''}" disabled="disabled"/><span class="mar_lft10"></span>
				<input name="pasVsAdd.state" type="hidden" value="${pasVsAdd.state!''}"/><span class="mar_lft10"></span>
			</td>
		</tr>
		<tr class="DivideLine">
			<td colspan="3"></td>
		</tr>
		<tr>
			<th class="Lth2">
				<li>${LANGCODEMAP["MSG_VSPAS_NAME"]!}</li>
			</th>
			<td colspan="2" class="Lth0">
			<#if !(pasVsAdd.name)?? || (pasVsAdd.name)! == ''>
				<input id="idVsName" class="inputText width130" name="pasVsAdd.name" type="text" value="${pasVsAdd.name!''}"/>
					<span class="example">${LANGCODEMAP["MSG_VSPAS_FORMAT_VS_GROUP_NAME_SEQ"]!}&nbsp;</span>
					<span class="popUpVServerNameWndLnk">
						<input type="button" class="Btn_black_small" value="${LANGCODEMAP["MSG_VSPAS_USED_NAME"]!}">
					</span>
			<#else>
				<input disabled="disabled" class="inputText width130" type="text" value="${pasVsAdd.name!''}"/>
				<input name="pasVsAdd.name" class="inputText width130" type="hidden" value="${pasVsAdd.name!''}"/>
			</#if>
				<input type="hidden" name="pasVsAdd.poolName" value="${pasVsAdd.poolName!''}"/>	
			</td>
        </tr>
		<tr class="DivideLine">
			<td colspan="3"></td>
		</tr>
		<tr>
			<th class="Lth2">
				<li>${LANGCODEMAP["MSG_VSPAS_PORT"]!}</li>
			</th>
			<td colspan="2" class="Lth0">
				<input name="pasVsAdd.protocol" type="hidden" value="${pasVsAdd.protocol!''}"/>
				<select name="select2" id="select2" class="types inputSelect protocolCbx width134" disabled="disabled">
				<#if !(pasVsAdd.protocol)?? || ((pasVsAdd.protocol)?? && (pasVsAdd.protocol)! == 6)>
				  	<option selected="selected" value="">${LANGCODEMAP["MSG_VSPAS_PORT_DESI"]!}</option>
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
				<#if !(pasVsAdd.protocol)?? || (pasVsAdd.protocol)?? && (pasVsAdd.protocol)! == 6>
				&nbsp;<input name="pasVsAdd.port" class="inputText width130" type="text" value="${pasVsAdd.port!''}" disabled="disabled"/>
				<#else>								
				&nbsp;<input name="pasVsAdd.port" class="inputText width130" type="text" value="${pasVsAdd.port!''}" readonly/>								
				</#if>	
			</td>
        </tr>
		<tr class="DivideLine">
			<td colspan="3"></td>
		</tr>
		<tr>
			<th class="Lth1">
				<li>Member</li>
			</th>
			<td colspan="2" class="Lth0">
				<span class="txt_bold">${LANGCODEMAP["MSG_VSPAS_MEMBER_ADD_IP_PORT"]!}</span>
					<input name="checkbox4" type="checkbox" id="checkbox4" class="memberEnabledChk" value="true" checked="checked" disabled="disabled"/>Enable <br />
				</span>
			</td>
    	</tr>
		<tr>
			<th class="Lth1"></th>
			<td class="Lth0" colspan="2"> 
				<input type="text" name="textfield" id="textfield" class="memberIpTxt inputText width130" disabled="disabled"/>
					: 
				<input type="text" name="textfield4" id="textfield4" class="memberPortTxt inputText width130" disabled="disabled"/> 
			</td>
		</tr>
		<tr>
			<th class="Lth1"></th>
			<td class="Lth0">
				<span class="txt_bold">Member</span>
				<span class="txt_blue">(<span class="txt_blue poolMemberCount">${(pasVsAdd.members![])?size}</span>)</span>
			</td>
            <td>
            	<span class="txt_bold">${LANGCODEMAP["MSG_VSPAS_REAL_SERVER_LIST"]!}</span>
            	<span class="txt_blue">(<span class="txt_blue adcNodeCount">${(adcNodes![])?size}</span>)</span>
            </td>
    	</tr>
		<tr>
			<th class="Lth1"></th>
			<td class="Lth0">
            	<div class="fixed-table-container151">
	      			<div class="header-background32">
	      				<table id="selectedMember" class="fixed-table-hd" cellpadding="0" cellspacing="0" >
							<colgroup>				
								<col width="10%"/>
								<col width="30%"/>
								<col width="30%"/>
	                        	<col width="30%"/>
							</colgroup>
	          				<tr>
		            			<th class="align_center">
		            				<input class="allMembersChk" name="checkbox" type="checkbox" id="checkbox" disabled="disabled"/>
		            			</th>
		            			<th class="align_center">IP</th>
		           				<th class="align_center">${LANGCODEMAP["MSG_VSPAS_PORT"]!}</th>
		            			<th class="align_center">${LANGCODEMAP["MSG_VSPAS_STATUS"]!}</th>
	          				</tr>
				        </table>
	      			</div>
	      			<div class="fixed-table-container-inner">
	                	<table id="selectedMember" class="fixed-table_1" cellpadding="0" cellspacing="0" >
	                    	<colgroup>				
	                    		<col width="10%"/>
								<col width="30%"/>
								<col width="30%"/>
	                        	<col width="30%"/>
							</colgroup>
							<tbody class="memberTbd">
							<#list pasVsAdd.members![] as theMember>
								<tr class="regMemberTr">
									<td class="align_center">
										<input class="memberChk" name="checkbox" type="checkbox" id="checkbox" disabled="disabled"/>
									</td>
								    <td class="align_left_P10">${theMember.ip}</td>
								    <td class="align_center">${theMember.port}</td>
								    <td class="align_center"> 
								    	<select disabled="disabled">
											<option value="true" ${theMember.isEnabled?string('selected="selected"', '')}>Enabled</option>
											<option value="false" ${theMember.isEnabled?string('', 'selected="selected"')}>Disabled</option>
										</select>
								    </td>
								    <td class="c_7" style="display: none;">${theMember.id}</td>
								</tr>
							</#list>
							</tbody>		   
						</table>
					</div>
				</div>
			</td>
			<td> 
 				<div class="fixed-table-containerw200">
	      			<div class="header-background32">
	      				<table class="fixed-table-hd" cellpadding="0" cellspacing="0">

								<tr>
									<th>IP</th>
								</tr>

						</table>
					</div>	
					<div class="fixed-table-container-inner table-container_vs">
						 <table class="fixed-table" cellpadding="0" cellspacing="0">
							<tbody class="adcNodeTbd" disabled="disabled">
								<#list adcNodes as theNode>
									<tr>
										<td class="adcNodeTd">${theNode.ip}</td>
									</tr>
								</#list>
							</tbody>
						</table>
					</div>	 
       			</div>
			</td>                              
		</tr>		
	    <tr class="DivideLine">
	    	<td colspan="3"></td>
	    </tr>
		<tr>
			<th class="Lth2">
				<li>Load Balancing</li>
			</th>
			<td colspan="2" class="Lth0">
				<select name="pasVsAdd.loadBalancingType" id="select2" class="inputSelect" disabled="disabled">
					<#if (pasVsAdd.loadBalancingType)! == "NOT_ALLOWED">
						<option value="NOT_ALLOWED" selected="selected">Not Allowed</option>
					<#else>
						<option value="Round Robin" ${((pasVsAdd.loadBalancingType!'') == 'Round Robin')?string('selected="selected"', '')}>Round Robin</option>
						<option value="Least Connections" ${((pasVsAdd.loadBalancingType!'') == 'Least Connections')?string('selected="selected"', '')}>Least Connections</option>
						<option value="Hash" ${((pasVsAdd.loadBalancingType!'') == 'Hash')?string('selected="selected"', '')}>Hash</option>						
					</#if>
				</select>
				&nbsp;
			</td>
		</tr>
		<tr class="DivideLine">
			<td colspan="3"></td>
		</tr>
		<tr>
			<th class="Lth2">
				<li>Health Check</li>
			</th>
			<td colspan="2" class="Lth0">
				<select name="pasVsAdd.healthCheckType" id="select2" class="inputSelect width134" disabled="disabled">
					<#if (pasVsAdd.healthCheckType)! == "NOT_ALLOWED">
						<option value="NOT_ALLOWED" selected="selected">Not Allowed</option>
					<#else>
						<option value="NONE" ${((pasVsAdd.healthCheckType!'') == 'NONE')?string('selected="selected"', '')}>${LANGCODEMAP["MSG_VSPAS_NOT_DESI"]!}</option>
						<option value="TCP" ${((pasVsAdd.healthCheckType!'') == 'TCP')?string('selected="selected"', '')}>TCP</option>
						<option value="HTTP" ${((pasVsAdd.healthCheckType!'') == 'HTTP')?string('selected="selected"', '')}>HTTP</option>
						<option value="ICMP" ${((pasVsAdd.healthCheckType!'') == 'ICMP')?string('selected="selected"', '')}>ICMP</option>
					</#if>
				</select>
				<input type="hidden" name="pasVsAdd.healthCheckId" value="${pasVsAdd.healthCheckId!''}"/>
				&nbsp;
			</td>
		</tr>
		<tr class="EndLine">
			<td colspan="3"></td>
		</tr>
        <td colspan="3">
		<div class="position_cT10">			
			<span>
				<input class="Btn_white virtualServerAddCancelLnk" type="button" value="${LANGCODEMAP["MSG_VSPAS_CANCEL"]!}">
			</span>
		</div> 
		</td>
	</table>
</form>
</#if>

<!-- 일괄 추가 팝업 -->
<div id="memberAddBatch" class="pop_type_wrap">
	<h2>${LANGCODEMAP["MSG_VSPAS_GM_MULTI_ADD"]!}</h2>
	<div class="pop_contents">
		<p class="ptop mar_top10">
			<span>${LANGCODEMAP["MSG_VSPAS_FORMAT"]!}</span>${LANGCODEMAP["MSG_VSPAS_INPUT_IP_PORT"]!}
		</p>
		<p class="ptop none_bt">
			<span>${LANGCODEMAP["MSG_VSPAS_USE_EX"]!}</span>192.168.110.11:80
		</p>		
		<textarea name="ipsTxt" rows="10" style="width:95%; resize:none;" ></textarea>
		<p class="center pbtm">
			<span class="txt">&nbsp;</span>
			<span class="center">		
				<span><input type="button" class="Btn_red onOk" value="${LANGCODEMAP["MSG_VSPAS_COMPLETE"]!}"></span>
				<span><input type="button" class="Btn_white onCancel" value="${LANGCODEMAP["MSG_VSPAS_CANCEL"]!}"></span>	
			</span>
		</p>
	</div>
	<p class="close">
		<a href="#" title="${LANGCODEMAP["MSG_VSPAS_CLOSE"]!}">
			<img src="imgs/popup/btn_clse.gif" alt="${LANGCODEMAP["MSG_VSPAS_CLOSE"]!}" />
		</a>
	</p>
</div>
<!-- //일괄 추가 팝업 -->

<!-- 포토 변경 팝업 -->
<div id="portChange" class="pop_type_wrap">
	<h2>${LANGCODEMAP["MSG_VSPAS_PORT_CHANGE"]!}</h2>
	<div class="pop_contents">
		<p class="setport">
			<span>${LANGCODEMAP["MSG_VSPAS_PORT_NUMBER"]!}</span><input class="portNo" type="text"/>
		</p>
		<p class="right mar_top10">
			<a class="onCancel" href="#">
				<img src="imgs/common/btn_cancel.gif" alt="${LANGCODEMAP["MSG_VSPAS_CANCEL"]!}"/>
			</a>
			<a class="onOk" href="#">
				<img src="imgs/common/btn_ok.gif" alt="${LANGCODEMAP["MSG_VSPAS_COMPLETE"]!}"/>
			</a>
		</p>
	</div>
	<p class="close">
		<a href="#" title="${LANGCODEMAP["MSG_VSPAS_CLOSE"]!}">
			<img src="imgs/popup/btn_clse.gif" alt="${LANGCODEMAP["MSG_VSPAS_CLOSE"]!}" />
		</a>
	</p>
</div>
<!-- //포토 변경 팝업 -->

<!-- Persistence 설정 정보 팝업 -->
<div id="persistenceDetail" class="pop_type_wrap">
	<h2>${LANGCODEMAP["MSG_VSPAS_PERSIST_SET_INFO_VIEW"]!}</h2>
	<div class="pop_contents">
		<!--<div class="recent_time">
			<p>최종 변경 시간 : 2012-03-28 13:28:00</p>
		</div>-->
		<form class="setting">
			<fieldset>
				<legend>${LANGCODEMAP["MSG_VSPAS_DEFAULT_SET"]!}</legend>
				<table class="form_type1" summary="${LANGCODEMAP["MSG_VSPAS_PERSIST_SET_INFO_VIEW"]!}">
					<caption>${LANGCODEMAP["MSG_VSPAS_PERSIST_SET_INFO_VIEW"]!}</caption>
					<colgroup>
						<col width="35%" />
						<col width="55%" />
					</colgroup>
					<tbody>
						<tr>
							<th scope="row" class="none_bg_img">${LANGCODEMAP["MSG_VSPAS_NAME"]!}</th>
							<td class="bor_top"><span name="profileAdd.name" class="f_left"></span> <span class="f_right"><img src="imgs/popup/icon_pas.png" alt="f5" /></span></td>
						</tr>
						<tr>
							<th scope="row" class="none_bg_img">Persistence Type</th>
							<td><span name="profileAdd.persistenceType"></span></td>
						</tr>
						<tr>
							<th scope="row" class="none_bg_img">Parent Profile</th>
							<td><span name="profileAdd.parentProfileName"></span></td>
						</tr>
						<tr>
							<th scope="row" class="none_bg_img">Match Across Services</th>
							<td><span name="profileAdd.isMatchAcrossServices"></span></td>
						</tr>
						<tr>
							<th scope="row" class="none_bg_img">Timeout</th>
							<td><span name="profileAdd.timeOutInSec"></span>${LANGCODEMAP["MSG_VSPAS_SECOND"]!}</td>
						</tr>
					</tbody>
				</table>
			</fieldset>
		</form>
	</div>
	<p class="close">
		<a class="closeWndLnk" href="#" title="${LANGCODEMAP["MSG_VSPAS_CLOSE"]!}">
			<img src="imgs/popup/btn_clse.gif" alt="${LANGCODEMAP["MSG_VSPAS_CLOSE"]!}" />
		</a>
	</p>
</div>
<!-- //Persistence 설정 정보 팝업 -->

<!-- virtualserver ip 팝업 -->
<div id="vs_ipList" class="pop_type_wrap">
	<h2>${LANGCODEMAP["MSG_VSPAS_VS_IP_LIST"]!}</h2>
	<div class="pop_contents">
		<p class="sch">
			<input class="searchTxt inputText_search" type="text" value="" />		
			<span class="btn2">
			<a class="searchLnk" href="#">
			<img src="imgs/meun/btn_search_1.png" alt="${LANGCODEMAP["MSG_VSPAS_SEARCH"]!}" />
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
						<th>${LANGCODEMAP["MSG_VSPAS_NUMBER"]!}</th>
						<th>${LANGCODEMAP["MSG_VSPAS_VS_NAME"]!}</th>
						<th>${LANGCODEMAP["MSG_VSPAS_IP_ADDR"]!}</th>
					</tr>
				</thead>
				</table>
		<div class="listWrap1">
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
			<span><input type="button" class="Btn_white closeLnk" value="${LANGCODEMAP["MSG_VSPAS_CLOSE"]!}"></span>
		</p>
	</div>
	<p class="close">
		<a class="closeWndLnk" href="#" title="${LANGCODEMAP["MSG_VSPAS_CLOSE"]!}">
			<img src="imgs/popup/btn_clse.gif" alt="${LANGCODEMAP["MSG_VSPAS_CLOSE"]!}" />
		</a>
	</p>
</div>
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