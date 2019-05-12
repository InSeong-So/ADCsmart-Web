<#if langCode??>
  <#if "ko_KR" == langCode><#assign img_lang = ""></#if>
  <#if "en_US" == langCode><#assign img_lang = "_eng"></#if>
</#if>
<div class="contents_area">
	<div> 
		<img src="imgs/title${img_lang!""}/h3_realserver.gif" class="title_h3" />			 
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
		<span class="total01"> 
			${LANGCODEMAP["MSG_APPLIANCE_TOTAL"]!} <span class="txt_bold noticePageCountInfo">00</span>${LANGCODEMAP["MSG_HISTORY_COUNT"]!} <span class="txt_bold noticePageInfo">(2/4 ${LANGCODEMAP["MSG_APPLIANCE_PAGE"]!})</span>
		</span>   
		<#if accountRole != 'readOnly'>
        <div class="control_positionR">	
	 		<input type="button" class="imgOn rsGroupModifyLnk none Btn_blue" value="${LANGCODEMAP["MSG_NODE_GROUP_MODIFY"]!}">		 		
			<input type="button" class="imgOn rsGroupDelLnk none Btn_blue" value="${LANGCODEMAP["MSG_NODE_GROUP_DELETE"]!}">      			      
			<input type="button" class="imgOnC rsGroupAddLnk Btn_white" value="${LANGCODEMAP["MSG_NODE_GROUP_CREATE"]!}">				   		      		 		            
	    	<input type="button" class="imgOffC Btn_white none" disabled="disabled none" value="${LANGCODEMAP["MSG_NODE_GROUP_CREATE"]!}">    
	    	<!--<input type="button" id="selectColumn_pop" class="Btn_white" value="▦"/>-->	   	    	  	
    	</div>	  
    	</#if>
	</div>
	<br class="clearfix" />	
	<!-- <table class="Board virtualSvrTable selectedNode" id="rsGrpSelected" cellpadding="0" cellspacing="0" style="table-layout: fixed;"> -->
 	<div class="BoardOuter">
	    <div class="fixed-table-container-inner_xScroll">	
			<table id="rsGrpSelected" class="fixed-table_xScroll virtualSvrTable selectedNode"  style="table-layout: fixed;" >
				<#include "nodeTableInContent.ftl"/>		
			</table>
		</div>	
	</div>
	<div class="dataNotExistMsg none">${LANGCODEMAP["MSG_DATA_NOT_EXIST"]!}</div>
	<div class="nulldataMsg none">${LANGCODEMAP['MSG_COMMON_DATA_NULL']!}</div>
	<div class="searchNotMsg none">${LANGCODEMAP["MSG_SEARCH_NOT_RESULT"]!}</div>
	<!----- Contents List End ----->
                      
	<!----- Contents Page Start ----->
	<#if accountRole != 'readOnly'>			
	<table class="Board_97 disabledChk" border="0">
		<tr height="5">
			<td></td>
			<td></td>
		</tr>
		<tr>		
			<td class="Lth0">
				<input type="button" class="imgOn f5vsmodfiyOn enableNodeLnk Btn_white_small" value="Enable"/>
	        	<input type="button" class="imgOff none f5vsmodfiyOn enableNodeLnk Btn_white_small" disabled="disabled" value="Enable"/> 
	        	
				<input type="button" class="imgOn f5vsmodfiyOn disableNodeLnk Btn_white_small" value="Disable"/>
				<input type="button" class="imgOff none f5vsmodfiyOn disableNodeLnk Btn_white_small" disabled="disabled" value="Disable"/> 
				
				<input type="button" class="imgOn f5vsmodfiyOn forcedOffNodeLnk Btn_white_small" value="Forced Offline"/>	
				<input type="button" class="imgOff none f5vsmodfiyOn forcedOffNodeLnk Btn_white_small" disabled="disabled" value="Forced Offline"/> 
				
				<input type="button" class="imgOnC f5vsmodfiyOn rsGroupMoveLnk Btn_white_small" value="${LANGCODEMAP["MSG_NODE_GROUP_MOVE"]!}"/>	
				<input type="button" class="imgOffC f5vsmodfiyOn rsGroupMoveLnk Btn_white_small none" disabled="disabled" value="${LANGCODEMAP["MSG_NODE_GROUP_MOVE"]!}"/>
				<span class="groupname_info none">
				<div class="layer_def_wrap" id="group_move">
					<div class="layer_def_a2 have_tit">
						<h3 class="layer_tit">${LANGCODEMAP["MSG_NODE_GROUP_MOVE"]!}</h3>
							<select size="4" class="layer_conts">
								<option value="0">${LANGCODEMAP["MSG_NODE_GROUP_NO"]!}</option>
								<#list nodeGrpList as theNodeGrp>
								<#if theNodeGrp.available == 1>
							  	<option value="${theNodeGrp.groupIndex!""}">${theNodeGrp.groupName!""}</option>
							  	</#if>
							  	</#list>
							</select>			
						<div class="btn">
							<input type="button" id="rsGrpSave" class="Btn_white_small" value="${LANGCODEMAP["MSG_VSALTEON_CONFIRM"]!}">
							<input type="button" id="rsGrpCancel" class="Btn_white_small" value="${LANGCODEMAP["MSG_VSALTEON_CANCEL"]!}">					
						</div>  
					</div>
				</div>
				</span>
				<!--
				<span class="groupname_info">
				<div class="layer_def_wrap none" id="group_move">
					<div class="layer_def_a2 have_tit">
					<h3 class="layer_tit">Group이동</h3>
					<div class="layer_conts">
						<ul>
							<li> GroupName01  </li>	
						  	<li> GroupName02  </li>	
						  	<li> GroupName03  </li>	
						  	<li> 그룹없음  </li>
						  	<#list nodeGrpList as theNodeGrp>
						  	<li>${theNodeGrp.groupName!""}</li>
						  	</#list>							  						  						  	
						</ul>				
					</div>
					<div class="btn">
					<input type="button" id="refresh" class="Btn_white_small" value="확인">
					<input type="button" id="refresh" class="Btn_white_small" value="취소">					
					</div>  
					</div>
				</div>
				</span> 	
				-->			
			</td>
			<td></td>            
    	</tr>
    	<!--    	
    	<tr>
			<td class="align_center" >
				<input type="button" class="setNodeOkLnk Btn_red" value="${LANGCODEMAP["MSG_ALERT_APPLY"]!}"/>
			</td>
			<td></td>	
		</tr>
		-->		
	</table>	
	</#if> 
	
	<!----- Contents Page End ----->	
	<div class="Board_97 disabledChk">
	 	<p class="pageNavigator"></p>
		<div id="select2" class="pageRowCountCbx"></div>
	</div>	
</div>

<!-- selectColumn 팝업 시작 -->
<div id="selCol-popup" class="pop_type_wrap">
    <h2>${LANGCODEMAP["MSG_APPLIANCE_COLUMN_ADD"]!}</h2>
    <div class="pop2_contents">
      <span class="groupselected_txt"><span class="txt_blue checkedCount">0</span><span class="totalCount">/8</span> ${LANGCODEMAP["MSG_GROUPMONITOR_COUNT_SEL"]!}</span>  
      <br class="clearfix">   
   		<table class="table_type11">			
			<colgroup>
				<col width="50px"/>
				<col width="auto"/>
				<col/>
			</colgroup>
			<thead>
				<tr>
					<th>
						<input class="allChk" type="checkbox"/> 
					</th>
					<th>${LANGCODEMAP["MSG_APPLIANCE_OPTION_COLUMN"]!}</th>
				</tr>
			</thead>		
		</table>       
 	 <div class="listWrap1">    
    	<table class="table_type11">			
			<colgroup>
				<col width="50px"/>
				<col width="auto"/>
				<col/>
			</colgroup>
			<tbody>				
			</tbody>
		</table>
	</div>        
    <p class="center mar_top10">
		<input type="button" class="onOk Btn_red" value="${LANGCODEMAP["MSG_VSF5_COMPLETE"]!}"/>  
		<input type="button" class="onCancel Btn_white" value="${LANGCODEMAP["MSG_VSF5_CANCEL"]!}"/>  	
	</p>
	</div>		
    <p class="close">
        <a href="#" title="${LANGCODEMAP["MSG_DIAG_ANAL_CLOSE"]!}"> 
            <img src="imgs/popup/btn_clse.gif" alt="${LANGCODEMAP["MSG_DIAG_ANAL_CLOSE"]!}" />
        </a>
    </p>
</div>
<!-- selectColumn 팝업끝 -->

