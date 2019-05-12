<#if langCode??>
  <#if "ko_KR" == langCode><#assign img_lang = ""></#if>
  <#if "en_US" == langCode><#assign img_lang = "_eng"></#if>
</#if>
<#if orderType??>
	<#if 33 == orderType></#if><!-- profileName -->
	<#if 34 == orderType></#if><!-- timeOut -->
	<#if 35 == orderType></#if><!-- matchAcrossService -->
</#if>
<#if orderDir??>
	<#if 1 == orderDir></#if><!-- Asc -->
	<#if 2 == orderDir></#if><!-- Desc -->
</#if>
<div class="contents_area">
<!--
	<div class="title_h2">
		<table class="Board100" cellpadding="0" cellspacing="0">
			<colgroup>							                            
				<col width="111px"/>
				<col width="111px"/>
				<col/>				
    		</colgroup>
			<tr>
				<td>
					<a class="vServerLnk" href="#">
						<img src="imgs/meun${img_lang!""}/3depth_vs_1.gif" />												
					</a>
				</td>
				<td>					
					<img src="imgs/meun${img_lang!""}/3depth_profile_0.gif" />					
				</td>			
				<td>
					<a class="rServerLnk" href="#">
						<img src="imgs/meun${img_lang!""}/3depth_rs_1.gif" />												
					</a>
				</td>			
				<td>
					<a class="noticeServerLnk" href="#">
						<img src="imgs/meun${img_lang!""}/3depth_notice_1.gif" />												
					</a>
				</td>
				
				<td>&nbsp;</td>					
			</tr>
		</table>
	</div>
-->	
	<div> 
		<img src="imgs/title${img_lang!""}/h3_profileList.gif" class="title_h3" />				 
	</div>
	<div class="control_Board">
		<span class="inputTextposition1">
			<input class="searchTxt inputText_search" name="searchKey" type="text" id="textfield3" value="${searchKey!}" />
		</span>
		<span class="btn"> 
			<a class="searchLnk " href="#">
				<img class="imageChange" src="imgs/meun/btn_search_1.png" alt="${LANGCODEMAP["MSG_PROFILE_SEARCH"]!}" />
			</a>
		</span>
		<div class="control_positionR">		
	    <#if accountRole != 'readOnly' && accountRole != 'vsAdmin' && accountRole != 'rsAdmin'>
			<input type="button" class="imgOn addProfileLnk Btn_white" value="${LANGCODEMAP["MSG_PROFILE_ADD"]!}">           	
			<input type="button" class="imgOff none Btn_white" disabled="disabled" value="${LANGCODEMAP["MSG_PROFILE_ADD"]!}"> 	    
	    </#if>
	    </div>
	</div>
	<br class="clearfix" />
	<!----- Contents List Start ----->
	<table class="Board" cellpadding="0" cellspacing="0">
		<colgroup>
			<col width="5%"/>
			<col width="35%"/>
			<col width="30%"/>
			<col width="30%"/>
		</colgroup>	
		<thead>
			<tr class="StartLine">
				<td colspan="4"></td>
			</tr>
			<tr class="ContentsHeadLine">
				<th>
					<#if accountRole == 'readOnly'>
						<input class="allProfileChk" type="checkbox" disabled="disabled"/>
					<#else>	
						<input class="allProfileChk" type="checkbox"/>
					</#if>
				</th>
				<th>
					<span class="css_textCursor">
						<#if orderDir == 2 && orderType == 33>		
							<a class="orderDir_Desc">${LANGCODEMAP["MSG_PROFILE_PROFILE_NAME"]!}
								<img src="imgs/Desc.gif"/>
								<span class="none orderType">33</span>
								<span class="none orderDir">1</span>					
							</a>						
						<#elseif orderDir == 1 && orderType == 33>	
							<a class="orderDir_Asc">${LANGCODEMAP["MSG_PROFILE_PROFILE_NAME"]!}
								<img src="imgs/Asc.gif"/>
								<span class="none orderType">33</span>
								<span class="none orderDir">2</span>		
							</a>
						<#else>
							<a class="orderDir_None">${LANGCODEMAP["MSG_PROFILE_PROFILE_NAME"]!}
								<img src="imgs/none.gif"/>
								<span class="none orderType">33</span>
								<span class="none orderDir">2</span>		
							</a>
						</#if>
					</span>
				</th>
				<th>
					<span class="css_textCursor">
						<#if orderDir == 2 && orderType == 34>		
							<a class="orderDir_Desc">Timeout
								<img src="imgs/Desc.gif"/>
								<span class="none orderType">34</span>
								<span class="none orderDir">1</span>					
							</a>					
						<#elseif orderDir == 1 && orderType == 34>	
							<a class="orderDir_Asc">Timeout
								<img src="imgs/Asc.gif"/>
								<span class="none orderType">34</span>
								<span class="none orderDir">2</span>		
							</a>
						<#else>
							<a class="orderDir_None">Timeout
								<img src="imgs/none.gif"/>
								<span class="none orderType">34</span>
								<span class="none orderDir">2</span>		
							</a>
						</#if>
					</span>				
				</th>
				<th>
					<span class="css_textCursor">
						<#if orderDir == 2 && orderType == 35>		
							<a class="orderDir_Desc">Match Across Service
								<img src="imgs/Desc.gif"/>
								<span class="none orderType">35</span>
								<span class="none orderDir">1</span>					
							</a>						
						<#elseif orderDir == 1 && orderType == 35>	
							<a class="orderDir_Asc">Match Across Service
								<img src="imgs/Asc.gif"/>
								<span class="none orderType">35</span>
								<span class="none orderDir">2</span>		
							</a>
						<#else>
							<a class="orderDir_None">Match Across Service
								<img src="imgs/none.gif"/>
								<span class="none orderType">35</span>
								<span class="none orderDir">2</span>		
							</a>
						</#if>
					</span>				
				</th>
			</tr>
		</thead>
		<tbody class="disabledChk">
			<tr class="StartLine1">
				<td colspan="4"></td>
			</tr>	
		<#list profiles as theProfile>		
			<tr class="ContentsLine1 profileList">
				<#if theProfile.name == "source_addr" || theProfile.name == "/Common/source_addr" || accountRole == 'readOnly'>
					<td class="align_center">
						<input class="profileChk" type="checkbox" value="${theProfile.index}" disabled="disabled" />
					</td>
				<#else>
					<td class="align_center">
						<input class="profileChk" type="checkbox" value="${theProfile.index}"/>
					</td>
				</#if>
				<td>
					<a class="modifyProfileLnk , align_left_P30" href="" target="_self">${theProfile.name}</a>
				</td>
				<td class="align_center">${theProfile.timeOutInSec!1800}</td>
				<td class="align_center">${(theProfile.isMatchAcrossServices!false)?string('On', 'Off')}</td>
			</tr>
			<tr class="DivideLine">
				<td colspan="4"></td>
			</tr>
		</#list>
		<tr class="EndLine">
			<td colspan="4"></td>
		</tr>
		</tbody>		
	</table>
	<div class="dataNotExistMsg none">${LANGCODEMAP["MSG_DATA_NOT_EXIST"]!}</div>
	<div class="nulldataMsg none">${LANGCODEMAP["MSG_COMMON_DATA_NULL"]!}</div>
	<div class="searchNotMsg none">${LANGCODEMAP["MSG_SEARCH_NOT_RESULT"]!}</div>
	<!----- Contents List End ----->
                      
	<!----- Contents Page Start ----->
	<table class="Board_97 disabledChk" border="0">
		<tr height="5">
			<td></td>
			<td></td>
		</tr>
		<tr>
		  	<td>
			<#if accountRole != 'readOnly' && accountRole != 'vsAdmin' && accountRole != 'rsAdmin'>
				<span class="imgOn">
		    		<input class="Btn_white_small delProfiles" type="button" value="${LANGCODEMAP["MSG_PROFILE_DEL"]!}">
		        </span>
		        
		        <span class="imgOff none">
					<input class="Btn_white_small delProfiles" type="button" disabled="disabled" value="${LANGCODEMAP["MSG_PROFILE_DEL"]!}">
				</span>	
		    </#if>    
		</td>
	</tr>
	<td></td>
    <td></td>
	</table>
	<!----- Contents Page End ----->
</div>

