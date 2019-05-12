<colgroup>			
	<col width="80px" />
	<col width="150px" />					
	<col width="80px" />
	<col width="auto" />
</colgroup>		
<thead>
	<tr>
		<th>상태</th>									
		<th>IP</th>
		<th>포트</th>
		<th>이름</th>																		
	</tr>
</thead>
<tbody class="slbListTbd">	
<#list virtualServers![] as theSlbList>	
	<tr>
		<td class="slbListStatus align_center">
			<input class="slbUsrIndex" type="hidden" value="${theSlbList.index!"-"}" />
			<#if theSlbList.status == 'block'>
				<img class="imgOn status_imgon" src="imgs/icon/icon_2d_3.png" />
			<#elseif theSlbList.status == 'disable'>
				<img class="imgOn status_imgon" src="imgs/icon/icon_vs_disabled.png" />
			<#elseif theSlbList.status == 'available'>
				<img class="imgOn status_imgon" src="imgs/icon/icon_vs_conn.png" />
			<#else>
				<img class="imgOn status_imgon" src="imgs/icon/icon_vs_disconn.png" />
			</#if>
		</td>	
		<td class="slbListVsIp align_center"><a id="mar_lft5" class="modifyVirtualServerLnk" href="#">${theSlbList.virtualIp!"0"}</a></td>
		<td class="slbListPort align_center">${theSlbList.port!""}</td>
		<td class="slbListName align_left_P5">${theSlbList.name!"-"}</td>												
	</tr>
</#list>																																									
</tbody>
