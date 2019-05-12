<#if langCode??>
  <#if "ko_KR" == langCode><#assign img_lang = ""></#if>
  <#if "en_US" == langCode><#assign img_lang = "_eng"></#if>
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
					<a class="profileLnk" href="#">
						<img src="imgs/meun${img_lang!""}/3depth_profile_1.gif" />											
					</a>						
				</td>				
				<td>
					<a class="rServerLnk" href="#">
						<img src="imgs/meun${img_lang!""}/3depth_rs_1.gif" />												
					</a>
				</td>		
				<td>
					<a class="noticeServerLnk" href="#">
						<img src="imgs/meun${img_lang!""}/3depth_notice_0.gif" />												
					</a>
				</td>				
				<td>&nbsp;</td>					
			</tr>
		</table>
-->
	<div> 
		<img src="imgs/title${img_lang!""}/h3_notice.gif" class="title_h3" />			 
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
		<div class="control_positionR"><span class="accntIndex"></span>		
		<input type="button" class="imgOn noticeGrpSetLnk Btn_white" value="${LANGCODEMAP["MSG_VSNOTICE_GROUP_SET"]!}"/> 
	    <!-- <#if (accountRole == 'system')>
			<input type="button" class="imgOn noticeGrpSetLnk Btn_white" value="${LANGCODEMAP["MSG_VSNOTICE_GROUP_SET"]!}"/>  			
	    </#if> --> 	  
	    <!--	
	    <input type="button" class="imgOn refreshLnk Btn_white" value="${LANGCODEMAP["MSG_VSF5_LISTREFRESH"]!}"/>
	    -->   
		</div>   
	</div>
	<br class="clearfix" />
	
	<!----- Contents List Start ----->
	<div class="title_h4_11 ">
		<li>${LANGCODEMAP["MSG_VSNOTICE_NVS"]!}
		<span class="noticeOnCount"></span>
		<!--
		<#if (VServerNoticeOn??)>
			<span>(${VServerNoticeOn?size})</span>	
		<#else>
			<span>(0)</span>
		</#if>
		-->
		<span class="total03"> 
			${LANGCODEMAP["MSG_APPLIANCE_TOTAL"]!} <span class="txt_bold on_noticePageCountInfo">00</span>${LANGCODEMAP["MSG_HISTORY_COUNT"]!} <span class="txt_bold on_noticePageInfo">(2/4 ${LANGCODEMAP["MSG_APPLIANCE_PAGE"]!})</span>
		</span>
		</li>
	</div>
	<table class="Board noticeOnTable selectedNotice" cellpadding="0" cellspacing="0" style="table-layout: fixed;">
		<#include "noticeOnTableInListContent.ftl"/>	
	</table>
	<div class="dataNotExistMsg msgOn none">${LANGCODEMAP["MSG_DATA_NOT_EXIST"]!}</div>
	<div class="nulldataMsg nullMsgOn none">${LANGCODEMAP['MSG_COMMON_DATA_NULL']!}</div>
	<div class="searchNotMsg searchOn none">${LANGCODEMAP["MSG_SEARCH_NOT_RESULT"]!}</div>
	<!----- Contents List End ----->
                 
	<!----- Contents Page Start ----->
	<table class="Board_97 disabledChk" border="0">
		<colgroup>
		    <col width="5%"/>
			<col width="auto"/>
		</colgroup>
		<tr height="5">
			<td></td>
			<td></td>
		</tr>
		<#if accountRole != 'readOnly'>		
		<tr>
			<td class="center">
				<input type="button" class="imgOn noticeRevertLnk Btn_white_small" value="${LANGCODEMAP["MSG_VSNOTICE_RESTORE"]!}"/>
				<input type="button" class="imgOff none noticeRevertLnk Btn_white_small" disabled="disabled"  value="${LANGCODEMAP["MSG_VSNOTICE_RESTORE"]!}"/>			
			</td>
			<td>&nbsp;</td>                             
    	</tr>
    	</#if>
	</table>
	<!----- Contents Page End ----->

	<div class="Board_97 disabledChk">
	 	<p class="pageNavigatorOn"></p>
		<div id="select2" class="pageRowCountCbxOn"></div>
	</div>
	
  	
	<!----- Contents List Start ----->
	<div class="title_h4_1 ">
		<li>${LANGCODEMAP["MSG_VSNOTICE_SVS"]!}
		<span class="noticeOffCount"></span>
		<!--
		<#if (VServerNoticeOff??)>
			<span>(${VServerNoticeOff?size})</span>	
		<#else>
			<span>(0)</span>
		</#if>
		-->
		<span class="total03"> 
			${LANGCODEMAP["MSG_APPLIANCE_TOTAL"]!} <span class="txt_bold off_noticePageCountInfo">00</span>${LANGCODEMAP["MSG_HISTORY_COUNT"]!} <span class="txt_bold off_noticePageInfo">(2/4 ${LANGCODEMAP["MSG_APPLIANCE_PAGE"]!})</span>
		</span>	
		</li>
	</div>
	<table class="Board noticeOffTable selectedNoticeGrp" cellpadding="0" cellspacing="0" style="table-layout: fixed;">		
		<#include "noticeOffTableInListContent.ftl"/>		
	</table>
	<div class="dataNotExistMsg msgOff none">${LANGCODEMAP["MSG_DATA_NOT_EXIST"]!}</div>
	<div class="nulldataMsg none">${LANGCODEMAP['MSG_COMMON_DATA_NULL']!}</div>
	<div class="searchNotMsg searchOff none">${LANGCODEMAP["MSG_SEARCH_NOT_RESULT"]!}</div>
	<!----- Contents List End ----->
                      
	<!----- Contents Page Start ----->
	<table class="Board_97 disabledChk" border="0">
		<colgroup>
		    <col width="5%"/>
			<col width="auto"/>
		</colgroup>	
		<tr height="5">
			<td></td>
			<td></td>
		</tr>
		<#if accountRole != 'readOnly'>		
		<tr>
			<td class="center">
				<input type="button" class="imgOn noticeChangeLnk Btn_white_small" value="${LANGCODEMAP["MSG_VSNOTICE_CONVERSION"]!}"/>
				<input type="button" class="imgOff none noticeChangeLnk Btn_white_small" disabled="disabled"  value="${LANGCODEMAP["MSG_VSNOTICE_CONVERSION"]!}"/>					
			</td>
			<td>&nbsp;</td>                             
    	</tr>
    	</#if>
	</table>
	<!----- Contents Page End ----->	
	<div class="Board_97 disabledChk">
	 	<p class="pageNavigator"></p>
		<div id="select2" class="pageRowCountCbx"></div>
	</div>
</div>

<!-- 怨듭�洹몃９ �꽕�젙 popup -->
<div id="noticeGrp" class="pop_type_wrap">
	<h2>${LANGCODEMAP["MSG_VSNOTICE_GROUP_SET"]!}</h2>
	<div class="pop_contents">		
		<p class="ptopN mar_top26 ">
			<span>${LANGCODEMAP["MSG_VSNOTICE_GROUP"]!}</span>
			<#if noticeGrpList?has_content>
				<#list noticeGrpList as theNoticeGrp>
				<#if accntIndex == theNoticeGrp.accntIndex>
					<#if adc.type == "Alteon">
						<input name="grpPoolMemberName" class="grpPoolMemberName" type="text" value="${theNoticeGrp.alteonID}(${theNoticeGrp.poolName})" readonly style="border: none; width: 250px;" />
					<#else>						
						<input name="grpPoolMemberName" class="grpPoolMemberName" type="text" value="${theNoticeGrp.poolName}" readonly style="border: none; width: 250px;" />						
					</#if>
				</#if>										
				</#list>
			</#if>
		</p>
		<p class="ptopN mar_top26 none">
			<span>${LANGCODEMAP["MSG_VSNOTICE_GROUP"]!}</span>
			<#if noticeGrpList?has_content>
				<#list noticeGrpList as theNoticeGrp>	
					<#if adc.type == "Alteon">
						<input name="noticeGrpNmChk" class="noticeGrpNmChk" type="text" value="${theNoticeGrp.alteonID}(${theNoticeGrp.poolName})" disabled="disabled" />
					<#else>
						<input name="noticeGrpNmChk" class="noticeGrpNmChk" type="text" value="${theNoticeGrp.poolName}" disabled="disabled" />
					</#if>								
				</#list>
			</#if>
		</p>	
		<p class="ptopN none_bt">
			<span>${LANGCODEMAP["MSG_VSNOTICE_CHANGEGROUP"]!}</span>
			<select class="inputSelect selectGrpMemList" id="selectGrpMemList" style="width: 55%;">
				<option value="-">${LANGCODEMAP["MSG_VSF5_NOT_DESI"]!}</option>	
				<#if adcPools?has_content>			
					<#list adcPools as thePool>
					<#if adc.type == "Alteon">
						<option value="${thePool.index}" selected="selected">${thePool.alteonId}(${thePool.name})</option>
					<#else>
						<#if thePool.name == "">
							<option value="${thePool.index}" selected="selected">${thePool.name}</option>
						<#else>
							<option value="${thePool.index}">${thePool.name}</option>
						</#if>
					</#if>						
					</#list>
				</#if>
			</select>			
		</p>
		<p class="center mar_top10">
			<input type="button" class="noticeOk Btn_red" value="${LANGCODEMAP["MSG_VSF5_COMPLETE"]!}"/>  
			<input type="button" class="noticeCancel Btn_white" value="${LANGCODEMAP["MSG_VSF5_CANCEL"]!}"/>  	
		</p>
	</div>
	<p class="close">
		<a href="#" title="${LANGCODEMAP["MSG_VSF5_CLOSE"]!}">
			<img src="imgs/popup/btn_clse.gif" alt="${LANGCODEMAP["MSG_VSF5_CLOSE"]!}" />
		</a>
	</p>
</div>
