<#if langCode??>
  <#if "ko_KR" == langCode><#assign img_lang = ""></#if>
  <#if "en_US" == langCode><#assign img_lang = "_eng"></#if>
</#if>
<!-- <div class="content_wrap"> -->
<div class="contents_area">
	<div>
		<#if adcScope.level == 0>
			<img src="imgs/title${img_lang!""}/h3_monitor_service_all.gif" class="title_h3" />
		<#elseif adcScope.level == 1>
			<img src="imgs/title${img_lang!""}/h3_monitor_service_group.gif" class="title_h3" />
		<#else>
		</#if>	
		<span class="title_h3txt title_name_area"></span>			
	</div>
<div class="control_Board">
	<span class="calendar">			
		<input type="text" class="inputText_calendar" id="reservationtime" name="reservation"  value="periodMode" readonly="" >
	</span>
		<span class="inputTextposition">
			<input name="searchKey" type="text" class="searchTxt inputText_search" id="textfield3" value="${searchKey!}" />
		</span>
		<span class="btn"> 
			<a class="searchLnk" href="#">
				<img src="imgs/meun/btn_search_1.png" alt="${LANGCODEMAP["MSG_ADCSETTING_SEARCH"]!}" />
			</a>
		</span>
		<button class="filter_option_btn_close" id="filterAdd" isFlag="false">${LANGCODEMAP["MSG_APPLIANCE_FILTER_OPTION"]!}</button>		
		<span class="total01"> 
		${LANGCODEMAP["MSG_APPLIANCE_TOTAL"]!} <span class="txt_bold noticePageCountInfo">00</span>${LANGCODEMAP["MSG_HISTORY_COUNT"]!} <span class="txt_bold noticePageInfo">(2/4 ${LANGCODEMAP["MSG_APPLIANCE_PAGE"]!})</span>
		</span>
  		
	<div class="control_positionR">
		<input type="button" id="exportLnk" class="Btn_white" value="${LANGCODEMAP["MSG_PERFCONT_EXPORT"]!}"/>  		
		<input type="button" id="refreshLnk" class="Btn_white" value="${LANGCODEMAP["MSG_PERFCONT_REFRESH"]!}"/>	
		<input type="button" id="selectColumn_pop" class="Btn_white" value="▦"/>	   
	</div>

	</div>
	<br class="clearfix" />	
		<table class="Filter_Board optionFilter none" cellpadding="0" cellspacing="0"  style="table-layout: fixed;">
		<thead>
			<tr>
			<#if montotalSvc.condition.status.isSelect()>
				<th data-colidx="0">${LANGCODEMAP["MSG_ADCSETTING_STATUS"]!}</th>
			<#else>			
				<th class= none" data-colidx="0">${LANGCODEMAP["MSG_ADCSETTING_STATUS"]!}</th>
			</#if>
			<#if montotalSvc.condition.port.isSelect()>
				<th data-colidx="1">${LANGCODEMAP["MSG_PERFALTEON_PORT"]!}</th>
			<#else>			
				<th class= none" data-colidx="1">${LANGCODEMAP["MSG_PERFALTEON_PORT"]!}</th>
			</#if>
			<#if montotalSvc.condition.bpsTotal.isSelect()>
				<th data-colidx="2">bps ${LANGCODEMAP["MSG_PERFALTEON_TOTAL"]!}</th>
			<#else>			
				<th class= none" data-colidx="2">bps ${LANGCODEMAP["MSG_PERFALTEON_TOTAL"]!}</th>
			</#if>
			<#if montotalSvc.condition.concurrentSession.isSelect()>
				<th data-colidx="3">Concurrent Sessions</th>
			<#else>			
				<th class= none" data-colidx="3">Concurrent Sessions</th>
			</#if>
			</tr>
		</thead>
		<tbody>
			<tr class="chkFilterOption">
			<#if montotalSvc.condition.status.isSelect()>
				<td data-colidx="0">
			<#else>			
				<td class= none" data-colidx="0">
			</#if>
					<div class="multcheck">
					<ul class="chklist" data-group="group1">
						<#if montotalSvc.condition.status.isSelect()>
							<#list montotalSvc.condition.status.filter as theFilter>
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
						</#if>
					</ul>
					</div>
				</td>
			<#if montotalSvc.condition.port.isSelect()>
				<td data-colidx="1">
			<#else>			
				<td class= none" data-colidx="1">
			</#if>
					<div class="multcheck">
					<ul class="chklist" data-group="group2">	
						<#list montotalSvc.condition.port.filter as theFilter>
						<#if (theFilter.value == '-1' )>
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
			<#if montotalSvc.condition.bpsTotal.isSelect()>
				<td data-colidx="2">
			<#else>			
				<td class= none" data-colidx="2">
			</#if>
					<div class="multcheck">
					<ul class="chklist"  data-group="group3">								
						<#list montotalSvc.condition.bpsTotal.filter as theFilter>
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
			<#if montotalSvc.condition.concurrentSession.isSelect()>
				<td data-colidx="3">
			<#else>			
				<td class= none" data-colidx="3">
			</#if>
					<div class="multcheck">
					<ul class="chklist" data-group="group4">							
						<#list montotalSvc.condition.concurrentSession.filter as theFilter>
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
			<span class="deselFilter"><span class="btn_desel">${LANGCODEMAP["MSG_GROUPMONITOR_DESEL"]!}</span></span>
		</button>
	</div>	
	<div class="servicechart">	
	<div class="item11">
	<div class="title">${LANGCODEMAP["MSG_PERFAVGMAX_ITEM"]!}</div>	
		<ul>
			<li>				
				<input type="checkbox" class="bpsChk" id="bpsChk" name="bpsValChk" checked="checked"/>
				<label for="bpsChk">Throughput(bps)</label>
			</li>
			<li>																					
				<input type="checkbox" id="concurrSessionChk" name="concurrSessionValChk" checked="checked"/>
				<label for="concurrSessionChk">Concurrent Sessions</label>
			</li>			
		</ul>	
	</div>		
	<div class="item3 bpsChart">	
		<div class="chart">
			<div class="stackChart2" id="SvcPerfBpsChart"></div>
	    </div>	
		<div class="legend">
			<div class="title defaultItem"><span id="itemChange">Throughput(bps)</span></div>
			<table class="chartvalue bpsAvgMax" style="table-layout: fixed;">				
				<colgroup>
					<col width="auto">
					<col width="20%"/>
					<col width="20%"/>
					<col width="20%"/>	
				</colgroup>
				<tbody class="bpsCurAvgMaxTbd">
					<tr class="head">
						<th></th>
						<th>Current</th>
						<th>Avg</th>
						<th>Max</th>						
					</tr>	
					<#if bpsConnInfoData??>
					<#list bpsConnInfoData.bpsInfo as theBpsCurAvgMax>	
					<tr>
						<td class="textOver"><span class="square chart_1st"></span>${theBpsCurAvgMax.name!""}</td>		
						<td class="connCurrData">${(theBpsCurAvgMax.currentUnit)!""}</td>
						<td class="connCurrData">${(theBpsCurAvgMax.avgUnit)!""}</td>
						<td class="connCurrData">${(theBpsCurAvgMax.maxUnit)!""}</td>
					</tr>				
					</#list>	
					</#if>
				</tbody>	
			</table>
		</div>		
	</div>
	<div class="item3 connCurrChart">	
		<div class="chart">
			<div class="stackChart2" id="SvcPerfConncurrChart"></div>
	    </div>	
		<div  class="legend" >
			<div class="title defaultItem">Concurrent Sessions</div>	
			<table class="chartvalue connAvgMax" style="table-layout: fixed;">				
				<colgroup>
					<col width="auto">
					<col width="20%"/>
					<col width="20%"/>
					<col width="20%"/>	
				</colgroup>
				<tbody class="connCurAvgMaxTbd">
					<tr class="head">
						<th></th>
						<th>Current</th>
						<th>Avg</th>
						<th>Max</th>					
					</tr>
					<#if bpsConnInfoData??>
					<#list bpsConnInfoData.connInfo as theConnCurAvgMax>	
					<tr>
						<td class="textOver"><span class="square chart_1st"></span> ${theConnCurAvgMax.name!""}</td>		
						<td class="connCurrData">${(theConnCurAvgMax.currentUnit)!""}</td>
						<td class="connCurrData">${(theConnCurAvgMax.avgUnit)!""}</td>
						<td class="connCurrData">${(theConnCurAvgMax.maxUnit)!""}</td>
					</tr>				
					</#list>	
					</#if>			
				</tbody>
			</table>
		</div>		
	</div>
</div>
	<!----- Contents List Start ----->	
	<div class="title_h4_1">
		<li>${LANGCODEMAP["MSG_PERFCONT_PERF_INFO"]!}
		</li>	 
	</div>
	<div class="selectVs selectedVsFilter">
		<span>선택된 VS</span>
		<span class="selectVsList">
			<span class="sel_filter none">
				<span>|</span>
				<a href="javascript:;" class="selVsLabel"></a>
				<button type="button" class="delVsBtn">${LANGCODEMAP["MSG_GROUPMONITOR_DEL"]!}</button>
			</span>
		</span>
		<button class="btn_sm deselVsBtn">
			<span class="deselVsFilter"><span class="btn_desel">${LANGCODEMAP["MSG_GROUPMONITOR_DESEL"]!}</span></span>
		</button>
	</div>	
 	<div class="BoardOuter">
    <div class="fixed-table-container-inner_xScroll">	
	<table id="serviceListTable" class="fixed-table_xScroll_2Head serviceMonListTable" cellpadding="0" cellspacing="0"  style="table-layout: fixed;">
		<#include "serviceMonitoringTableInListContent.ftl"/>
	</table>
	</div>
	</div>
	<div class="dataNotExistMsg none">${LANGCODEMAP["MSG_DATA_NOT_EXIST"]!}</div>
	<div class="nulldataMsg none">${LANGCODEMAP["MSG_COMMON_DATA_NULL"]!}</div>	
	<div class="searchNotMsg none">${LANGCODEMAP["MSG_SEARCH_NOT_RESULT"]!}</div>
	<!----- Contents Page End ----->	
	<div class="Board">
	 	<p class="pageNavigator"></p>
		<div id="select1" class="pageRowCountCbx"></div>
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