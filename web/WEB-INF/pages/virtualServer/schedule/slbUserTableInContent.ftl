	<table id="selectedUsr" class="table_type11 slbUserTable" summary="">
			<colgroup>
				<col width="40px" />				
				<col width="120px" />
				<col width="150px" />					
				<col width="100px" />
				<col width="auto" />
			</colgroup>	
		<tbody class="slbUsrTbd">		
		<#list slbUserList![] as theSlbUsr>	
			<tr>
				<td class="slbUsrCheck align_center" isChk="0">
					<input class="slbUsrChk" name="slbUsrChk" type="checkbox" id="slbUsrChk" value="${theSlbUsr.index!"-"}" />
					<input class="slbUsrType" name="slbUsrType" type="hidden" id="slbUsrType" value="${theSlbUsr.type!"-"}" />
				</td>
				<td class="slbUsrName align_left_P5">${theSlbUsr.name!"-"}</td>
				<td class="slbUsrTeam align_left_P5">${theSlbUsr.team!"-"}</td>
				<td class="slbUsrPhone align_center">${theSlbUsr.phone!"0"}</td>
				<td class="align_center"><input type="button" class="modifySlbUser Btn_black_small" value="수정"></td>												
			</tr>
		</#list>	
		</tbody>	
	</table>
	
