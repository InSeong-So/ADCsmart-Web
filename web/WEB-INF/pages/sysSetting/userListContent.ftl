<#if orderType??>
	<#if 33 == orderType></#if><!--ORDER_TYPE_FIRST -->
	<#if 34 == orderType></#if><!-- �씠由� -->
	<#if 35 == orderType></#if><!-- 理쒖쥌濡쒓렇�씤 -->
	<#if 36 == orderType></#if><!-- �뿭�븷 -->
</#if>
<#if orderDir??>
	<#if 1 == orderDir></#if><!-- Asc -->
	<#if 2 == orderDir></#if><!-- Desc -->
</#if>
<#if langCode??>
  <#if "ko_KR" == langCode><#assign img_lang = ""></#if>
  <#if "en_US" == langCode><#assign img_lang = "_eng"></#if>
</#if>
<div class="contents_area">
	<div>
		<img src="imgs/title${img_lang!""}/h3_userList.gif" class="title_h3"/>		
	</div>
	<div class="control_Board">
		<p class="cont_sch">
			<span class="inputTextposition1" >
				<input name="searchTxt" type="text" class="searchTxt inputText_search" id="searchTxt" value="${searchKey!}">
			</span>
			<span class="btn">
				<a href="javascript:;" class="searchLnk">
					<img src="imgs/meun/btn_search_1.png" alt="${LANGCODEMAP["MSG_USERLIST_SEARCH"]!}"/>
				</a>
			</span>
			<#if accountRole! == 'system'>	
			<div class="control_positionR">
				<input type="button" class="addAccountLnk Btn_white" value="${LANGCODEMAP["MSG_USERLIST_ADD"]!}"/>  			
		    </div>
			</#if>
		</p>
	</div>
	<br class="clearfix" />
	<!----- Contents List Start ----->
	<table class="Board" cellpadding="0" cellspacing="0" style="table-layout:fixed;">
		<colgroup>
		    <col width="40px"/>
			<col width="140px"/>
			<col width="auto"/>
			<col width="140px"/>
			<col width="120px"/>
			<col width="140px"/>
			<col  width="190px"/>
		</colgroup>
			<tr class="StartLine">
				<td colspan="7"></td>
			</tr>
			<tr class="ContentsHeadLine">
		  		<th>
		  			<span>
		  				<input class="allUsersChk" type="checkbox"/>
		  			</span>
		  		</th>
				<th>
					<span class="css_textCursor">
						<#if orderDir == 2 && orderType == 33>		
							<a class="orderDir_Desc">${LANGCODEMAP["MSG_USERLIST_ID"]!}
								<img src="imgs/Desc.gif"/>
								<span class="none orderType">33</span>
								<span class="none orderDir">1</span>					
							</a>						
						<#elseif orderDir == 1 && orderType == 33>	
							<a class="orderDir_Asc">${LANGCODEMAP["MSG_USERLIST_ID"]!}
								<img src="imgs/Asc.gif"/>
								<span class="none orderType">33</span>
								<span class="none orderDir">2</span>		
							</a>
						<#else>
							<a class="orderDir_None">${LANGCODEMAP["MSG_USERLIST_ID"]!}
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
							<a class="orderDir_Desc">${LANGCODEMAP["MSG_USERLIST_NAME"]!}
								<img src="imgs/Desc.gif"/>
								<span class="none orderType">34</span>
								<span class="none orderDir">1</span>					
							</a>						
						<#elseif orderDir == 1 && orderType == 34>	
							<a class="orderDir_Asc">${LANGCODEMAP["MSG_USERLIST_NAME"]!}
								<img src="imgs/Asc.gif"/>
								<span class="none orderType">34</span>
								<span class="none orderDir">2</span>		
							</a>
						<#else>
							<a class="orderDir_None">${LANGCODEMAP["MSG_USERLIST_NAME"]!}
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
							<a class="orderDir_Desc">${LANGCODEMAP["MSG_USERLIST_LAST_LOGIN"]!}
								<img src="imgs/Desc.gif"/>
								<span class="none orderType">35</span>
								<span class="none orderDir">1</span>					
							</a>						
						<#elseif orderDir == 1 && orderType == 35>	
							<a class="orderDir_Asc">${LANGCODEMAP["MSG_USERLIST_LAST_LOGIN"]!}
								<img src="imgs/Asc.gif"/>
								<span class="none orderType">35</span>
								<span class="none orderDir">2</span>		
							</a>
						<#else>
							<a class="orderDir_None">${LANGCODEMAP["MSG_USERLIST_LAST_LOGIN"]!}
								<img src="imgs/none.gif"/>
								<span class="none orderType">35</span>
								<span class="none orderDir">2</span>		
							</a>
						</#if>
					</span>					
				</th>
				<th>
					<span class="css_textCursor">
						<#if orderDir == 2 && orderType == 36>		
							<a class="orderDir_Desc">${LANGCODEMAP["MSG_USERLIST_ROLE"]!}
								<img src="imgs/Desc.gif"/>
								<span class="none orderType">36</span>
								<span class="none orderDir">1</span>					
							</a>						
						<#elseif orderDir == 1 && orderType == 36>	
							<a class="orderDir_Asc">${LANGCODEMAP["MSG_USERLIST_ROLE"]!}
								<img src="imgs/Asc.gif"/>
								<span class="none orderType">36</span>
								<span class="none orderDir">2</span>		
							</a>
						<#else>
							<a class="orderDir_None">${LANGCODEMAP["MSG_USERLIST_ROLE"]!}
								<img src="imgs/none.gif"/>
								<span class="none orderType">36</span>
								<span class="none orderDir">2</span>		
							</a>
						</#if>
					</span>				
				</th>
				<th>${LANGCODEMAP["MSG_USERLIST_MANAGE_ADC"]!}</th>
				<th>${LANGCODEMAP["MSG_USERLIST_DESCRIPTION"]!}</th>
			</tr>
			<tr class="StartLine1">
				<td colspan="7"></td>
			</tr>			
		</thead>
   		<#list accounts as theAccount>
			<tr class="ContentsLine1 userList">			
	  			<td class="align_center">
	  				<span>
	  					<input class="userChk" type="checkbox" value="${theAccount.index!''}"/>
	  				</span>
	  			</td>
	  			<td class="BoardLink align_left_P10 textOver" title="${theAccount.id!''}">
	  				<a href="" target="_self">
	  				<#if accountRole == 'system'>
						<a class="accountIdLnk" href="#">${theAccount.id!''}</a>
	            	<#else>
                 		${theAccount.id!''}
                 	</#if>
            	</td>
				<td class="align_left_P10 textOver" title="${theAccount.name!''}">${theAccount.name!''}</td>
	  			<td class="align_center textOver" title="${(theAccount.lastLoginTime?string("yyyy-MM-dd HH:mm:ss"))!}">${(theAccount.lastLoginTime?string("yyyy-MM-dd HH:mm:ss"))!}</td>
	  			<td class="align_left_P10 textOver" title="${(roleMap[theAccount.roleId?string].name)!''}">${(roleMap[theAccount.roleId?string].name)!''}</td>	  		
				<td class="align_left_P10 textOver" title="${theAccount.nameList!''}">
					<#if theAccount.roleId == 1>
                  		${LANGCODEMAP["MSG_USERLIST_ALL"]!}
               		<#elseif ((theAccount.adcNames![])?size > 0)>
							${theAccount.nameList!''}              		
                	</#if>
            	</td>
				<td class="align_left_P10 textOver" title="${theAccount.description!''}">${theAccount.description!''}</td> 			               
			</tr>
			<tr class="DivideLine">
				<td colspan="7"></td>
			</tr>
		</#list>
		<tr class="EndLine">
			<td colspan="7"></td>
		</tr>
	</table>
	<div class="dataNotExistMsg none">${LANGCODEMAP["MSG_DATA_NOT_EXIST"]!}</div>	
	<div class="searchNotMsg none">${LANGCODEMAP["MSG_SEARCH_NOT_RESULT"]!}</div>
	<table class="Board_97" >
		<colgroup>							                            
			<col width="40px"/>
			<col width="auto"/>
		</colgroup>
		<tr height="5px">
			<td colspan="2"></td>
		</tr>
		<tr>
			<td class="center">
					<#if accountRole! == 'system'>
				  		<input type="button" class="delAccountsLnk Btn_white_small" value="${LANGCODEMAP["MSG_USERLIST_DELETE"]!}"/>  
					</#if>
 			</td>
			<td>&nbsp; </td>
		</tr>
	</table>
</div>