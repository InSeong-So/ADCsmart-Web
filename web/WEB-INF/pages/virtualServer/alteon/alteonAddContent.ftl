<link type="text/css" rel="stylesheet" href="/js/extern/themes/smoothness/jquery-ui.css" />
<#setting number_format="0.####">
<#if langCode??>
  <#if "ko_KR" == langCode><#assign img_lang = ""></#if>
  <#if "en_US" == langCode><#assign img_lang = "_eng"></#if>
</#if>
<#if (accountRole != 'readOnly') && (accountRole != 'rsAdmin')>
<div class="contents_area">
	<div>
		<#if !(alteonVsAdd.index)??>
			<img src="imgs/title${img_lang!""}/h3_slbAdd.gif" class="title_h3" />
		<#else>		
			<img src="imgs/title${img_lang!""}/h3_slbModify.gif" class="title_h3" />
		</#if>		 
	</div>
	<form id="alteonVsAddFrm" method="post">
	<div style="display:none;">
		<input name="alteonVsAdd.adcIndex" type="text" value="${alteonVsAdd.adcIndex}"/>
		<input name="alteonVsAdd.index" type="text" value="${alteonVsAdd.index!}"/>
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
				<li>${LANGCODEMAP["MSG_VSALTEON_ADDRESS"]!}</li>
			</th>
			<td class="Lth0">
				<input name="alteonVsAdd.ip" id="textfield2" class="inputText width130" type="text" value="${alteonVsAdd.ip!}"/><span class="mar_lft10"></span>
			</td>
		</tr>
		<tr class="DivideLine">
			<td colspan="2"></td>
		</tr>
		<tr>
			<th class="Lth1">
				<li>${LANGCODEMAP["MSG_VSALTEON_NAME"]!}</li>
			</th>
			<td class="Lth0">
				<input type="text" name="alteonVsAdd.name" id="idVsName" class="inputText width130" value="${alteonVsAdd.name!}"/>&nbsp;
				<input type="button" class="popUpVServerNameWndLnk Btn_black_small" value="${LANGCODEMAP["MSG_VSALTEON_VS_LIST"]!}"/> 
			</td>
		</tr>
		<tr class="DivideLine">
			<td colspan="2"></td>
		</tr>
		<tr>
			<th class="Lth2">
				<li>${LANGCODEMAP["MSG_VSALTEON_INEXID"]!}</li>
			</th>
			<td class="Lth0">
				<#if !(alteonVsAdd.alteonId)??>
					<input name="alteonVsAdd.alteonId" type="text" class="inputText width130" value="${alteonVsAdd.alteonId!''}"/>
					<span class="example"> (1 - 1024) </span>
				<#else>
					<input disabled="disabled" type="text" class="inputText width130" value="${alteonVsAdd.alteonId!''}"/>
					<span class="example"> (1 - 1024) </span>
					<input name="alteonVsAdd.alteonId" type="hidden"  class="inputText width130" value="${alteonVsAdd.alteonId!''}"/>
				</#if>			
			</td>
	     </tr>
		 <tr class="DivideLine">
			<td colspan="2"></td>
		 </tr>
	     <tr>
			<th class="Lth1">
				<li>Network Class</li>
			</th>
			<td class="Lth0">
				<#if !(alteonVsAdd.subInfo)??>
					-
				<#else>
					${alteonVsAdd.subInfo}
				</#if>		
			</td>
	     </tr>
		<!--이중화설정 주석처리부분-->
		<input id="d_process" name="alteonVsAdd.vrrpState" type="hidden" value="${alteonVsAdd.vrrpState!''}"/>
		<input name="alteonVsAdd.routerId" type="hidden" value="${alteonVsAdd.routerId!''}" />
		<input name="alteonVsAdd.vrId" type="hidden" value="${alteonVsAdd.vrId!''}" />
		<input name="alteonVsAdd.interfaceNo" type="hidden" value="${(alteonVsAdd.interfaceNo)!((interfaces[0].id)!'')}" />
		<tr class="DivideLine">
			<td colspan="2"></td>
		</tr>
		<tr>
			<th class="Lth1">
				<li>${LANGCODEMAP["MSG_VSALTEON_VS_SERVICE_SET"]!}</li>
			</th>
			<td class="Lth0">
				<span class="txt_bold">${LANGCODEMAP["MSG_VSALTEON_VS_SERVICE_LIST"]!}</span>
				<span class="txt_blue">&nbsp;(<span class="virtualSvcCountSpn">${(alteonVsAdd.virtualSvcs![])?size}</span>)</span> &nbsp; 
				<input type="button" class="imgOn addVirtualSvcLnk Btn_green_small" value="${LANGCODEMAP["MSG_VSALTEON_SERVICE_ADD"]!}"/>  
				<input name="version" type="hidden" value="${version.version!''}" />	
			</td>
		</tr>
		<tr>
			<th class="Lth1"></th>
			<td class="Lth0">
			<table class="Board_98" cellpadding="0" cellspacing="0">
				<colgroup>
					<col width="10%"/>
					<col width="20%" />
					<col width="20%" />
	                <col width="30%" />
					<col width="20%" />
				</colgroup>
				<tr class="StartLine">
					<td colspan="5"></td>
				</tr>
				<tr class="ContentsHeadLine">
					<th class="center"><input class="allVirtualSvcsChk" type="checkbox" id="checkbox2"/></th>
					<th class="center">Service</th>
					<th class="center">Group Index</th>
					<th class="center">${LANGCODEMAP["MSG_VSALTEON_GROUP_NAME"]!}</th>
					<th class="center">${LANGCODEMAP["MSG_VSALTEON_MEMBER_COUNT"]!}</th>
					<tbody class="virtualSvcTbd">
					<#list (alteonVsAdd.virtualSvcs)![] as svc>	
						<tr class="ContentsLine3">
							<td class="align_center">
								<input class="virtualSvcsChk" type="checkbox" name="checkbox4" id="checkbox4" value="${svc.svcPort}"/>
								<span class="virtualSvcsJson" style="display:none;">${(svc.toJSON())!''}</span>
							</td>
							<td class="align_center">
								<a class="virtualSvcPortLnk">${svc.svcPort!''}</a>
							</td>
							<td class="align_center">${(svc.pool.alteonId)!''}</td>
							<td class="align_left">${(svc.pool.name)!''}</td>
							<td class="align_center">${(svc.pool.members?size)!0}</td>					
						</tr>
						<tr class="DivideLine">
							<td colspan="5"></td>
						</tr>					
					</#list>
					</tbody>
				<tr class="EndLine">
					<td colspan="5"></td>
				</tr>
				<tr class="ContentsLine3">
					<td class="center">
					    <input type="button" class="delVirtualSvcs Btn_white_small" value="${LANGCODEMAP["MSG_VSALTEON_DELETE"]!}"/> 
		            </td>
				</tr>
				<td colspan="4"></td>
				<tr class="ContentsLine">
					<td colspan="5">&nbsp;</td>
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
				    <input type="button" class="imgOn virtualServerAddOkLnk Btn_red" value="${LANGCODEMAP["MSG_VSALTEON_COMPLETE"]!}"/> 
				    <input type="button" class="imgOn virtualServerAddCancelLnk Btn_white" value="${LANGCODEMAP["MSG_VSALTEON_CANCEL"]!}"/>  
				    <input type="button" class="imgOff none virtualServerAddCancelLnk Btn_white" value="${LANGCODEMAP["MSG_VSALTEON_CONFIRM"]!}"/>  
				</div> 
			</td>
		</tr>
	</table>
	</form>
	</div>
<#else>
<!-- READ ONLY USER -->
	<div class="contents_area">
	<div>
		<img src="imgs/title${img_lang!""}/h3_slbAdd.gif" class="title_h3" />			
	</div>
	<form id="alteonVsAddFrm" method="post">
	<div style="display:none;">
		<input name="alteonVsAdd.adcIndex" type="text" value="${alteonVsAdd.adcIndex}"/>
		<input name="alteonVsAdd.index" type="text" value="${alteonVsAdd.index!}"/>
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
				<li>${LANGCODEMAP["MSG_VSALTEON_ADDRESS"]!}</li>
			</th>
			<td class="Lth0">
				<input name="alteonVsAdd.ip" id="textfield2" class="inputText width130" type="text" value="${alteonVsAdd.ip!}" disabled="disabled"/><span class="mar_lft10"></span>
			</td>
		</tr>
		<tr class="DivideLine">
			<td colspan="2"></td>
		</tr>
		<tr>
			<th class="Lth1">
				<li>${LANGCODEMAP["MSG_VSALTEON_NAME"]!}</li>
			</th>
			<td class="Lth0">
				<input type="text" name="alteonVsAdd.name" id="idVsName" class="inputText width130" value="${alteonVsAdd.name!}" disabled="disabled"/>
				<span class="example"> ${LANGCODEMAP["MSG_VSALTEON_FORMAT_VSGROUP_SEQ"]!}&nbsp; </span>
				<input type="button" class="popUpVServerNameWndLnk Btn_black_small" value="${LANGCODEMAP["MSG_VSALTEON_VS_LIST"]!}"/>  
			</td>
		</tr>
		<tr class="DivideLine">
			<td colspan="2"></td>
		</tr>
		<tr>
			<th class="Lth2">
				<li>Index(ID)</li>
			</th>
			<td class="Lth0">
				<#if !(alteonVsAdd.alteonId)??>
					<input name="alteonVsAdd.alteonId" type="text" class="inputText width130" value="${alteonVsAdd.alteonId!''}"/>
					<span class="example"> (1 - 1024) </span>
				<#else>
					<input disabled="disabled" type="text" class="inputText width130" value="${alteonVsAdd.alteonId!''}"/>
					<span class="example"> (1 - 1024) </span>
					<input name="alteonVsAdd.alteonId" type="hidden"  class="inputText width130" value="${alteonVsAdd.alteonId!''}"/>
				</#if>			
			</td>
	     </tr>
		 <tr class="DivideLine">
			<td colspan="2"></td>
		 </tr>
	     <tr>
			<th class="Lth1">
				<li>Network Class</li>
			</th>
			<td class="Lth0">
				<#if !(alteonVsAdd.subInfo)??>
					-
				<#else>
					${alteonVsAdd.subInfo}
				</#if>		
			</td>
	     </tr>
		<!--이중화설정 주석처리부분-->
		<input id="d_process" name="alteonVsAdd.vrrpState" type="hidden" value="${alteonVsAdd.vrrpState!''}"/>
		<input name="alteonVsAdd.routerId" type="hidden" value="${alteonVsAdd.routerId!''}" />
		<input name="alteonVsAdd.vrId" type="hidden" value="${alteonVsAdd.vrId!''}" />
		<input name="alteonVsAdd.interfaceNo" type="hidden" value="${(alteonVsAdd.interfaceNo)!((interfaces[0].id)!'')}" />
		<tr class="DivideLine">
			<td colspan="2"></td>
		</tr>
		<tr>
			<th class="Lth1">
				<li>${LANGCODEMAP["MSG_VSALTEON_VS_SERVICE_SET"]!}</li>
			</th>
			<td class="Lth0">
				<span class="txt_bold">${LANGCODEMAP["MSG_VSALTEON_VS_SERVICE_LIST"]!}</span>
				<span class="txt_blue">(<span class="virtualSvcCountSpn">${(alteonVsAdd.virtualSvcs![])?size}</span>)</span> &nbsp;
				<input type="button" class="addVirtualSvcLnk Btn_green_small" value="${LANGCODEMAP["MSG_VSALTEON_SERVICE_ADD"]!}"/> 
				<input name="version" type="hidden" value="${version.version!''}" />	  
			</td>
		</tr>
		<tr>
			<th class="Lth1"></th>
			<td class="Lth0">
			<table class="Board_98" cellpadding="0" cellspacing="0">
				<colgroup>
					<col width="10%"/>
					<col width="20%" />
					<col width="20%" />
	                <col width="30%" />
					<col width="20%" />
				</colgroup>
				<tr class="StartLine">
					<td colspan="5"></td>
				</tr>
				<tr class="ContentsHeadLine">
					<th class="center"><input class="allVirtualSvcsChk" type="checkbox" id="checkbox2" disabled="disabled"/></th>
					<th class="center">Service</th>
					<th class="center">Group Index</th>
					<th class="center">${LANGCODEMAP["MSG_VSALTEON_GROUP_NAME"]!}</th>
					<th class="center">${LANGCODEMAP["MSG_VSALTEON_MEMBER_COUNT"]!}</th>
					<tbody class="virtualSvcTbd">
					<#list (alteonVsAdd.virtualSvcs)![] as svc>	
						<tr class="ContentsLine3">
							<td class="align_center">
								<input class="virtualSvcsChk" type="checkbox" name="checkbox4" id="checkbox4" value="${svc.svcPort}" disabled="disabled"/>
								<span class="virtualSvcsJson" style="display:none;">${(svc.toJSON())!''}</span>
							</td>
							<td class="align_center">
								<a class="virtualSvcPortLnk">${svc.svcPort!''}</a>
							</td>
							<td class="align_center">${(svc.pool.alteonId)!''}</td>
							<td class="align_left">${svc.pool.name!''}</td>
							<td class="align_center">${(svc.pool.members?size)!0}</td>					
						</tr>
						<tr class="DivideLine">
							<td colspan="5"></td>
						</tr>					
					</#list>
					</tbody>
				<tr class="EndLine">
					<td colspan="5"></td>
				</tr>
				<tr class="ContentsLine3">					
				</tr>
				<td colspan="4"></td>
				<tr class="ContentsLine">
					<td colspan="5">&nbsp;</td>
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
					<input type="button" class="virtualServerAddCancelLnk Btn_white" value="${LANGCODEMAP["MSG_VSALTEON_CANCEL"]!}"/>
				</div> 
			</td>
		</tr>
	</table>
	</form>
	</div>
</#if>
<!-- //content_wrap -->
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
