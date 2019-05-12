<#setting number_format="0.####">
<colgroup>
	<col width="200px"/>
    <col width="auto"/>
</colgroup>
<tr class="DivideLine">
	<td colspan="2" ></td>
</tr>
<tr>
	<th class="Lth2">
		<li >${LANGCODEMAP["MSG_VSF5_IP_ADDR"]!}</li>
	</th>
	<td class="Lth0">
		<input type="text" name="f5VsAdd.ip" id="textfield2" class="inputText width130" value="${f5VsAdd.ip!''}"/><span class="mar_lft10"></span>		
	</td>
</tr>
<tr class="DivideLine">
	<td colspan="2"></td>
</tr>
<tr>
	<th class="Lth2">
		<li>${LANGCODEMAP["MSG_VSF5_NAME"]!}</li>
	</th>				
	<td class="Lth0">
		<#if !(f5VsAdd.name)?? || (f5VsAdd.name)! == ''>
			<input id="idVsName" class="inputText width130" name="f5VsAdd.name" type="text" value="${f5VsAdd.name!''}"/>&nbsp; 
			<input type="button" class="popUpVServerNameWndLnk Btn_black_small" value="${LANGCODEMAP["MSG_VSF5_USED_NAME"]!}"/>  						
		<#else>
			<input type="text" disabled="disabled" class="inputText width130" value="${f5VsAdd.name!''}"/>
			<input type="hidden" name="f5VsAdd.name" class="inputText width130" value="${f5VsAdd.name!''}"/>
		</#if>
	</td>
</tr>
<tr class="DivideLine">
	<td colspan="2"></td>
</tr>
<tr>
	<th class="Lth2">
		<li>${LANGCODEMAP["MSG_VSF5_PORT"]!}</li>
	</th>
	<td class="Lth0">
		<select name="select2" id="select2" class="types inputSelect protocolCbx select width134">
		  	<option selected="selected" value="">${LANGCODEMAP["MSG_VSF5_PORT_DESI"]!}</option>
			<!--<option value="21">FTP</option>-->
			<option value="80">HTTP</option>
			<option value="443">HTTPS</option>
			<!--<option value="23">TELNET</option>
			<option value="25">SMTP</option>
			<option value="161">SNMP</option>
			<option value="162">SNMP-TRAP</option>
			<option value="22">SSH</option>-->
		</select>
		<input type="text" name="f5VsAdd.port" id="textfield3" class="inputText width130" value="${f5VsAdd.port!''}" />
	</td>
</tr>
<tr class="DivideLine">
	<td colspan="2"></td>
</tr>
<tr>
	<th class="Lth2">
		<li>Group</li>
	</th>
	<td class="Lth0">
		<select name="f5VsAdd.poolIndex" id="select2" class="inputSelect types poolCbx width134">
			<option value="">${LANGCODEMAP["MSG_VSF5_NEW_DESI"]!}</option>
			<#list adcPools as thePool>
				<#if thePool.name == f5VsAdd.poolName!''>
					<option value="${thePool.index}" selected="selected">${thePool.name}</option>
				<#else>
					<option value="${thePool.index}">${thePool.name}</option>
				</#if>
			</#list>
		</select>	
    	<input type="text" name="f5VsAdd.poolName" id="textfield3" class="inputText width130" value="${f5VsAdd.poolName!''}" />							
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
			<input name="checkbox4" type="checkbox" id="checkbox4" class="memberEnabledChk" value="true" checked="checked" /><label for="enable" class="mar_lft5 font_normal">Enable</label> &nbsp;
			<input class="memberIpTxt inputText width130" type="text" />
				: 
			<input type="text" class="memberPortTxt inputText width50" />
			<input type="hidden" class="memberRatioTxt inputText width50" />
			<input type="button" class="imgOn memberAddLnk Btn_black_small" value="${LANGCODEMAP["MSG_VSF5_ADD"]!}"/>
			<input type="button" class="imgOn onMemberAddBatch Btn_black_small" value="${LANGCODEMAP["MSG_VSF5_MULTI_ADD"]!}"/> 							
		</div>
		<div class="selbox_add_left">
			<p class="mem_tit">Member <span class="txt_blue">(<span class="txt_blue poolMemberCount">${(f5VsAdd.members![])?size}</span>)</span></p>						
            <div class="headtable member-innerbox">
                <table class="head" id="selectedMember" cellpadding="0" cellspacing="0">
        		    <thead>
        		        <tr>
                            <th class="align_center"><input class="allMembersChk" name="checkbox" type="checkbox" id="checkbox" /></th>
                            <Th>IP</Th>
                            <Th>${LANGCODEMAP["MSG_NETWORK_PORT"]!}</Th>  
                            <Th>Ratio</Th>                                                     
                            <Th>Member ${LANGCODEMAP["MSG_LAYOUT_SETTING"]!}</Th>
                        </tr>
        		    </thead>
        		    <tbody class="memberTbd">
        		    	<#list f5VsAdd.members![] as theMember>
				<tr class="regMemberTr">
					<td class="align_center">
						<input class="memberChk" name="checkbox" type="checkbox" id="checkbox"/>
					</td>
				    <td class="align_left_P5">${theMember.ip}</td>
				    <td class="align_center">${theMember.port}</td>
				    <td class="align_center">${theMember.ratio!""}</td>
				    <td class="align_center">
				    	<select class="memStatus">			
					    	<#if ((theMember.memStatus)!'') == 1>
								<option value="1" selected="selected">Enabled</option>
							<#else>
								<option value="1">Enabled</option>
							</#if>
							<#if ((theMember.memStatus)!'') == 0>
								<option value="0" selected="selected">Disabled</option>
							<#else>
								<option value="0">Disabled</option>
							</#if>
							<#if ((theMember.memStatus)!'') == 2>
								<option value="2" selected="selected">Forced Offline</option>
							<#else>
								<option value="2">Forced Offline</option>
							</#if>
						<!--
							<option value="0" ${((theMember.memStatus!'') == 0)?string('selected="selected"', '')}>Disabled</option>
							<option value="4" ${((theMember.memStatus!'') == 4)?string('selected="selected"', '')}>Forced Offline</option>
						-->
						</select>
					</select>
				    </td>									    
				</tr>
			</#list>
        		    </tbody>
        		</table>
			</div>
		</div>							
		<div class="selbox_add_right">
			<p class="mem_tit">${LANGCODEMAP["MSG_VSALTEON_REAL_SERVER_LIST"]!} <span class="txt_blue">(<span class="txt_blue adcNodeCount">${(adcNodes![])?size}</span>)</span></p>								
            <div class="headtable member-innerbox">
                <table class="head" id="memberList" cellpadding="0" cellspacing="0">
        		    <thead>
        		        <tr>
                            <TH>IP</TH>
                            <TH>${LANGCODEMAP["MSG_VSF5_SETSTATUS"]!}</TH>
                        </tr>
        		    </thead>
        		    <tbody class="adcNodeTbd">
        		    	<#list adcNodes as theNode>
					<tr>
						<td class="adcNodeTd">${theNode.ip!""}<span class="none">|${theNode.ratio!""}</span></td>
						<td>${theNode.state!""}</td>
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
		<input type="button" class="f5vsmodfiyOn enableMembersLnk Btn_white_small" value="Enable"/>
		<input type="button" class="f5vsmodfiyOn disableMembersLnk Btn_white_small" value="Disable"/>
		<input type="button" class="f5vsmodfiyOn forcedOffMembersLnk Btn_white_small" value="Forced Offline"/>						
		<input type="button" class="f5vsmodfiyOn delMembersLnk Btn_white_small" value="${LANGCODEMAP["MSG_VSF5_DELETE"]!}"/>
		<input type="button" class="f5vsmodfiyOn changeMemberPortsLnk Btn_white_small" value="${LANGCODEMAP["MSG_VSF5_PORT_CHANGE"]!}"/>
	</td>                          
</tr>
<tr class="DivideLine">
	<td colspan="2"></td>
</tr>
<tr>
	<th class="Lth1">
		<li>VLAN and Tunnel Traffic</li>
	</th>
	<td class="Lth0">
		<select name="f5VsAdd.vlanTunnel.status" id="valnTunnel" class="valnTunnel inputSelect width134">	
		<#if f5VsAdd.vlanTunnel??>				
			<#if ((f5VsAdd.vlanTunnel.status)!-1) == 0>
				<option value="0" selected="selected">All VLANs and Tunnels</option>
			<#else>
				<option value="0">All VLANs and Tunnels</option>
			</#if>
			<#if ((f5VsAdd.vlanTunnel.status)!-1) == 1>
				<option value="1" selected="selected">Enabled on...</option>
			<#else>
				<option value="1">Enabled on...</option>
			</#if>
			<#if ((f5VsAdd.vlanTunnel.status)!-1) == 2>
				<option value="2" selected="selected">Disabled on...</option>
			<#else>
				<option value="2">Disabled on...</option>	
			</#if>
		<#else>
			<option value="0" selected="selected">All VLANs and Tunnels</option>
			<option value="1">Enabled on...</option>
			<option value="2">Disabled on...</option>
		</#if>
		</select>
	&nbsp;
	</td>
</tr>				    							
<tr class="DivideLine">
	<td colspan="2"></td>
</tr>			
<tr class="allVlan none">
	<th class="Lth1">
		<li>VLANs and Tunnels</li>
	</th>
	<td class="Lth0 align_top">
		<table border="0" cellpadding="0" cellspacing="0">
			<colgroup>
			<col width="315px" />
			<col width="40px"  >
			<col width="315px" />
			</colgroup>
			<tr> 
			  	<td>
			  		<span class="usrselected_th">${LANGCODEMAP["MSG_USERADD_SELECT"]!}</span><span class="usrselected_txt"><span class="selectedVlanTunnelCount txt_blue">00</span>&nbsp;${LANGCODEMAP["MSG_USERMODIFY_COUNT_SEL"]!}</span>
			  	</td>
				<td></td>
				<td>
					<span class="userlist_txt">${LANGCODEMAP["MSG_USERADD_LIST"]!}</span>
				</td>
			</tr>
			<tr>			
				<td>											
		        	<select name="f5VsAdd.vlanTunnel.vlanName" size="6" multiple="multiple" class="usrselecte" id="vlanTunnelSelectedSel">
					<#if registeredVlanMap??>
					<#list registeredVlanMap.vlanName![] as vlanTunnels>
						<option value="${vlanTunnels!''}">${vlanTunnels!''}</option> 
					</#list>
					</#if>
			    	</select>
				</td>
				<td>
					<div class="position_arrow">
						<a class="toValnTunnelSelectionLnk" href="#">
							<img src="imgs/meun/btn_mov_lft.png" alt="${LANGCODEMAP["MSG_ADCSETTING_MOVE_SELECTED"]!}" />
						</a> 
					</div>
					<div class="position_arrow">	
						<a class="toValnTunnelDeselectionLnk" href="#">
							<img src="imgs/meun/btn_mov_rgt.png" alt="${LANGCODEMAP["MSG_ADCSETTING_MOVE_SELECTED"]!}" />
						</a>
					</div>
				</td>
				<td> 	
     				<span class="inputTextposition1">
						<input name="vlanTunnelSearch" type="text"  class="vlanTunnelSearch inputText_search"  id="vlanTunnelSearch" />
					</span>
					<span class="btn1">
						<a href="#" class="vlanTunnelSearchLnk">
							<img src="imgs/meun/btn_search_1.png" alt="${LANGCODEMAP["MSG_ADCSETTING_SEARCH"]!}" />
						</a>
					</span>
					<br class="clearfix" />
					<select name="textarea2" size="5" multiple="multiple" class="userlist" id="vlanTunnelDeselectedSel">								
					<#list availableVlans![] as vlan>
						<option value="${vlan.vlanName}">${vlan.vlanName}</option>
					</#list>
      				</select>
				</td>
			</tr>
		</table>
	</td>
</tr>

<tr class="DivideLine allVlan none">
	<td colspan="2"></td>
</tr>
<tr>
	<th class="Lth2">
		<li>Load Balancing</li>
	</th>
	<td class="Lth0">
		<select name="f5VsAdd.loadBalancingType" id="select2" class="inputSelect width134">
			<#if (f5VsAdd.loadBalancingType)! == "NOT_ALLOWED">
				<option value="NOT_ALLOWED" selected="selected">Not Allowed</option>
			<#else>
				<option value="Round Robin" ${((f5VsAdd.loadBalancingType!'') == 'Round Robin')?string('selected="selected"', '')}>Round Robin</option>
				<option value="Least Connections" ${((f5VsAdd.loadBalancingType!'') == 'Least Connections')?string('selected="selected"', '')}>Least Connections</option>
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
		<select name="f5VsAdd.healthCheckType" id="select2" class="inputSelect width134">
			<#if (f5VsAdd.healthCheckType)! == "NOT_ALLOWED">
				<option value="NOT_ALLOWED" selected="selected">Not Allowed</option>
			<#else>
				<option value="NONE" ${((f5VsAdd.healthCheckType!) == 'NONE')?string('selected="selected"', '')}>${LANGCODEMAP["MSG_VSF5_NOT_DESI"]!}</option>
				<option value="TCP" ${((f5VsAdd.healthCheckType!) == 'TCP')?string('selected="selected"', '')}>TCP</option>
				<option value="HTTP" ${((f5VsAdd.healthCheckType!) == 'HTTP')?string('selected="selected"', '')}>HTTP</option>
				<option value="HTTPS" ${((f5VsAdd.healthCheckType!) == 'HTTPS')?string('selected="selected"', '')}>HTTPS</option>
				<option value="UDP" ${((f5VsAdd.healthCheckType!) == 'UDP')?string('selected="selected"', '')}>UDP</option>
				<option value="GATEWAY_ICMP" ${((f5VsAdd.healthCheckType!) == 'GATEWAY_ICMP')?string('selected="selected"', '')}>GATEWAY_ICMP</option>
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
		<li>Persistence</li>
	</th>
	<td class="Lth0">
		<select name="f5VsAdd.profileIndex" id="select3" class="inputSelect width134">
			<#if f5VsAdd.profileIndex! == 'Not Allowed'>
				<option value="Not Allowed">Not Allowed</option>
			<#else>
				<option value="">${LANGCODEMAP["MSG_VSF5_NOT_DESI"]!}</option>
				<#list profiles![] as theProfile>
					<option value="${theProfile.index}" ${(theProfile.index == f5VsAdd.profileIndex!)?string('selected="selected"', '')}>${theProfile.name}</option>
				</#list>
			</#if>
	    </select>
	    &nbsp; 
		<input type="button" class="persistenceDetailLnk Btn_black_small" style="visibility: hidden;" value="${LANGCODEMAP["MSG_VSF5_SET_INFO_VIEW"]!}"/> 				    
	</td>
</tr>                            
<tr class="EndLine">
	<td colspan="2"></td>
</tr>
<td colspan="2">
	<div class="position_cT10">
	<#if (accountRole != 'readOnly') && (accountRole != 'rsAdmin')>
		<input type="button" class="imgOn virtualServerAddOkLnk Btn_red" value="${LANGCODEMAP["MSG_VSF5_COMPLETE"]!}"/>
	</#if>	
		<input type="button" class="imgOn virtualServerAddCancelLnk Btn_white" value="${LANGCODEMAP["MSG_VSF5_CANCEL"]!}"/> 				
		<input type="button" class="imgOff none virtualServerAddCancelLnk Btn_white" value="${LANGCODEMAP["MSG_VSALTEON_CONFIRM"]!}"/> 							
	</div> 
</td>