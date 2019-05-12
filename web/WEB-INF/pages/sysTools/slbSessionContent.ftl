<div class="contents_area"> 
	<img src="imgs/title/h3_sessionMonitor.gif" class="title_h3"/> 
</div>
<!--1-->
<div>
<table class="Board" cellpadding="0" cellspacing="0">
	<caption>&nbsp;</caption>
	<colgroup>
		<col width="150px"/>
		<col width="70px"/>
		<col width="100px"/>
		<col width="80px"/">
		<col width="100px"/">
		<col width="80px"/">		
		<col >
	</colgroup>		
	<tr class="StartLine">
		<td colspan="7" ></td>
	</tr>		
	<tr>
		<th class="Lth2">
			<li>ADC 정보</li>
		</th>
		<td class="Lth0">주소</td>
		<td>
			<input name="adcIPAddress" type="text" class="inputText width100" id="adcIp" value="${(adcIPAddress)!}"/>
	    </td>
		<td class="Lth0">종류</td>			
		<td>
			<select name="adcType" class="types adcType width104"><option>Alteon</option>			   		
		</td>
		<td class="Lth0">통신 포트</td>			
		<td class="connServiceSelect">				
			<span class="telnetConnService">
			<input type="radio" name="connService" id="telnetSelect" checked="checked" value="23" />
			<label for="" class="">Telnet</label>
		    <input type="text" name="connPort"  id="telnet" class="inputText width35" value="23" />&nbsp;				
			</span>
			<span class="sslConnService">
			<input type="radio" name="connService" id="sshSelect" value="22" />
			<label for="" class="">SSH</label> 
		    <input type="text" name="connPort"  id="ssh" class="inputText width35" value="22" disabled="disabled" />			
			</span>									   		
		</td>

	</tr>
	<tr class="DivideLine">
		<td colspan="7"></td>
	</tr>
	<tr>
		<th class="Lth2">
			<li>로그인 정보</li>
		</th>		
		<td class="Lth0">아이디</td>
		<td>
			<input name="adcUserID" type="text" class="inputText width100" id="textfield3"value="${(adcUserID)!}"  />  			
    	</td>		
        <td class="Lth0">비밀번호</td>
        <td>
			<input name="adcPasswd" type="password" class="inputText width100" id="textfield3"value="${(adcPasswd)!}" />		
  	    </td>					
	</tr>
	<tr class="DivideLine">
		<td colspan="7"></td>
	</tr>
	<tr>
		<th class="Lth2">
			<li>검색키워드</li>
		</th>		
		<td class="Lth0">IP 종류</td>
        <td>	
        	<select name="searchIPType" class="inputSelect width104" id="ipType">
				<option value="0" selected="selected">미지정 </option>
				<option value="1">Source IP</option>
				<option value="2">Destination IP</option>
			</select>				
		</td>
        <td class="Lth0">IP주소</td>
		<td colspan="3">
        <span>
        	<input name="searchIPAddress" type="text" class="inputText width100" id="textfield3 ip" value="${(searchIPAddress)!}"/>
	    </span>									
		<div class="position_R10">
			<span>
				<input type="button" class="Btn_white searchLnk" value="검색">
			</span>
			<span>
				<input type="button" class="Btn_white exportCssLnk" value="내보내기">
			</span>		
		</div>				
		</td>			
	</tr>	
	<tr class="EndLine2">
		<td colspan="7"></td>
	</tr>
</table>
</div>

<!----- Contents List Start ----->
<table class="Board" cellpadding="0" cellspacing="0">
	<tr class="ContentsLine3">
		<td class="align_center"> 
			<textarea  class="session_textarea" rows="20" name="contents" readonly="readonly" style="overflow-y: scroll; resize:none; ">${contents}</textarea>
		</td>
	</tr>
	<tr class="EndLine2"><td></td></tr>
</table>
<!----- Contents List End ----->
</div>
                  

