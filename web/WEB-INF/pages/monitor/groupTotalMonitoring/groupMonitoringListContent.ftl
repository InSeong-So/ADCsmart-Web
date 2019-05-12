<#if langCode??>
  <#if "ko_KR" == langCode><#assign img_lang = ""></#if>
  <#if "en_US" == langCode><#assign img_lang = "_eng"></#if>
</#if>
<!-- <div class="content_wrap"> -->
<div class="contents_area">
	<div>
		<#if adcScope.level == 0>
			<img src="imgs/title${img_lang!""}/h3_monitor_group_all.gif" class="title_h3" />
		<#elseif adcScope.level == 1>
			<img src="imgs/title${img_lang!""}/h3_monitor_group_group.gif" class="title_h3" />
		<#else>
			<img src="imgs/title${img_lang!""}/h3_monitor_group_adc.gif" class="title_h3" />
		</#if>	
		<span class="title_h3txt title_name_area"></span>			
	</div>
<div class="control_Board">
		<p class="cont_sch">
		<span class="inputTextposition1">
			<input name="searchKey" type="text" class="searchTxt inputText_search" id="textfield3" value="${searchKey!}" />
		</span>
		<span class="btn"> 
			<a class="searchLnk" href="#">
				<img src="imgs/meun/btn_search_1.png" alt="${LANGCODEMAP["MSG_ADCSETTING_SEARCH"]!}" />
			</a>
		</span>
		<button class="filter_option_btn_close" id="filterAdd" isFlag="false">${LANGCODEMAP["MSG_APPLIANCE_FILTER_OPTION"]!}</button>		
		<span class="total01"> 
			${LANGCODEMAP["MSG_APPLIANCE_TOTAL"]!} <span class="txt_bold noticePageCountInfo">00</span>${LANGCODEMAP["MSG_HISTORY_COUNT"]!} <span class="txt_bold noticePageInfo">(2/4 ${LANGCODEMAP["MSG_APPLIANCE_PAGE"]!} )</span>
		</span>	                      	
		</p>
  		
	<div class="control_positionR">
		<input type="button" id="exportLnk" class="Btn_white" value="${LANGCODEMAP["MSG_GROUPMONITOR_EXPORT"]!}"/>  	
		<input type="button" id="refreshLnk" class="Btn_white" value="${LANGCODEMAP["MSG_GROUPMONITOR_REFRESH"]!}"/>		
		<input type="button" id="selectColumn_pop" class="Btn_white" value="▦"/>	   
	</div>

	</div>
	<br class="clearfix" />	
	<table class="Filter_Board optionFilter none" cellpadding="0" cellspacing="0"  style="table-layout: fixed;">
		<thead>
			<tr>
			<#if montotalGrp.condition.backup.isSelect()>
				<th data-colidx="2">Backup</th>
			<#else>
				<th class="nonoe" data-colidx="2">Backup</th>
			</#if>				
			</tr>
		</thead>
		<tbody>
			<tr>				
			<#if montotalGrp.condition.backup.isSelect()>
				<td data-colidx="2">
			<#else>
				<td class="nonoe" data-colidx="2">
			</#if>
					<div class="multcheck">
					<ul class="chklist" data-group="group1" data-colidx="2">							
						<#list montotalGrp.condition.backup.filter as theFilter>
						<#if (theFilter.index == 0)>
						<li class="chkFilterAll none">	
							<#if theFilter.isSelect()>		  				
				  				<input class="" name="selChk" type="checkbox" value="${(theFilter.value)!''}" checked="checked">
				  			<#else>		  				
				  				<input class="" name="selChk" type="checkbox" value="${(theFilter.value)!''}">
				  			</#if> 	
							<span class="none" name="selIndex">${(theFilter.index)!''}</span>		
							<label>${(theFilter.title)!''}</label>
						</li>
						<#else>								
						<li class="chkFilter">	
							<#if theFilter.isSelect()>		  				
				  				<input class="" name="selChk" type="checkbox" value="${(theFilter.value)!''}" checked="checked">
				  			<#else>		  				
				  				<input class="" name="selChk" type="checkbox" value="${(theFilter.value)!''}">
				  			</#if> 	
							<span class="none" name="selIndex">${(theFilter.index)!''}</span>		
							<label>${(theFilter.title)!''}</label>
						</li>
						</#if>
						</#list>		
					</ul>
					</div>
				</td>	
			</tr>
		</tbody> 
	</table>
	<div class="selectFilter selectedOptionFilter none">
		<span> ${LANGCODEMAP["MSG_APPLIANCE_FILTER_SELECT"]!} </span>
		<span class="selectFilterList">
			<span class="sel_filter none">
				<span>|</span>
				<a href="javascript:;" class="selFilterLabel"></a>
				<button type="button" class="delBtn">${LANGCODEMAP["MSG_GROUPMONITOR_DEL"]!}</button>
			</span>
		</span>
		<button class="btn_sm deselBtn">
			<span><span class="btn_desel">${LANGCODEMAP["MSG_GROUPMONITOR_DESEL"]!}</span></span>
		</button>
	</div>
	<!----- Contents List Start ----->
 	<div class="BoardOuter">
    <div class="fixed-table-container-inner_xScroll">	
	<table id="groupListTable" class="fixed-table_xScroll serviceMonListTable" cellpadding="0" cellspacing="0"  style="table-layout: fixed;">
		<#include "groupMonitoringTableInListContent.ftl"/>
	</table>	
    </div>    
    </div>	
    <div class="dataNotExistMsg none">${LANGCODEMAP["MSG_DATA_NOT_EXIST"]!}</div>
	<div class="nulldataMsg none">${LANGCODEMAP["MSG_COMMON_DATA_NULL"]!}</div>
	<div class="searchNotMsg none">${LANGCODEMAP["MSG_SEARCH_NOT_RESULT"]!}</div>	
	
	<!----- Contents Page End ----->	
	<div class="Board_97 disabledChk">
	 	<p class="pageNavigator"></p>
		<div id="select2" class="pageRowCountCbx"></div>
	</div>
</div>

<!-- selectColumn 팝업  시작-->
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
<!-- selectColumn 팝업  끝-->