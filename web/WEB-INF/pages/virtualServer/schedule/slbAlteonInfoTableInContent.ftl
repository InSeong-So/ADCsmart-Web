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
