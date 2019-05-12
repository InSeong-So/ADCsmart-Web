<#if langCode??>
  <#if "ko_KR" == langCode><#assign img_lang = ""></#if>
  <#if "en_US" == langCode><#assign img_lang = "_eng"></#if>
</#if>
<!-- <div class="content_wrap"> -->
<div class="contents_area">
	<div>
		<#if adcScope.level == 0>
			<img src="imgs/title${img_lang!""}/h3_monitor_device_all.gif" class="title_h3" />
		<#elseif adcScope.level == 1>
			<img src="imgs/title${img_lang!""}/h3_monitor_device_group.gif" class="title_h3" />
		<#else>
		</#if>	
		<span class="title_h3txt title_name_area"></span>	
		
	<div class="control_Board">
		<p class="cont_sch">
		<span class="inputTextposition1">
			<input name="searchKey" type="text" class="searchTxt inputText_search" id="textfield3" value="${searchKey!}" placeholder="" />
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
		<input type="button" id="exportLnk" class="Btn_white" value="${LANGCODEMAP["MSG_PERFCONT_EXPORT"]!}"/>  	
		<input type="button" id="refreshLnk" class="Btn_white" value="${LANGCODEMAP["MSG_PERFCONT_REFRESH"]!}"/>	
		<input type="button" id="selectColumn_pop" class="Btn_white" value="▦"/>	   
	</div>

	</div>
	<br class="clearfix" />	
	<table class="Filter_Board optionFilter none" cellpadding="0" cellspacing="0"  style="table-layout: fixed;">	
		<thead>
			<tr>
				<#if montotalAdc.condition.status.isSelect()>
					<th data-colidx="0">${LANGCODEMAP["MSG_ADCSETTING_STATUS"]!}</th>
				<#else>			
					<th class="none" data-colidx="0">${LANGCODEMAP["MSG_ADCSETTING_STATUS"]!}</th>
				</#if>
				<#if montotalAdc.condition.type.isSelect()>
					<th data-colidx="1">${LANGCODEMAP["MSG_ADCSETTING_TYPE"]!}</th>
				<#else>			
					<th class="none" data-colidx="1">${LANGCODEMAP["MSG_ADCSETTING_TYPE"]!}</th>
				</#if>
				<#if montotalAdc.condition.activeBackupState.isSelect()>
					<th data-colidx="3">${LANGCODEMAP["MSG_ADCSETTING_CONTINUOUS_STATUS"]!}</th> 
				<#else>			
					<th class="none" data-colidx="3">${LANGCODEMAP["MSG_ADCSETTING_CONTINUOUS_STATUS"]!}</th>
				</#if>
				<#if montotalAdc.condition.model.isSelect()>
					<th data-colidx="4">${LANGCODEMAP["MSG_ADCSETTING_APPLIANCE_NAME"]!}</th>
				<#else>			
					<th class="none" data-colidx="4">${LANGCODEMAP["MSG_ADCSETTING_APPLIANCE_NAME"]!}</th>
				</#if>
				<#if montotalAdc.condition.swVersion.isSelect()>
					<th data-colidx="5">${LANGCODEMAP["MSG_ADCSETTING_OSVERSION"]!}</th> 
				<#else>			
					<th class="none" data-colidx="5">${LANGCODEMAP["MSG_ADCSETTING_OSVERSION"]!}</th>
				</#if>	
				<#if montotalAdc.condition.concurrentSession.isSelect()>
					<th data-colidx="6">${LANGCODEMAP["MSG_APPLIANCE_SESSION_COUNT"]!}</th>
				<#else>			
					<th class="none" data-colidx="6">${LANGCODEMAP["MSG_APPLIANCE_SESSION_COUNT"]!}</th>
				</#if>
				<#if montotalAdc.condition.throughput.isSelect()>
					<th data-colidx="7">${LANGCODEMAP["MSG_APPLIANCE_ADC_TRAFFIC"]!}</th>
				<#else>			
					<th class="none" data-colidx="7">${LANGCODEMAP["MSG_APPLIANCE_ADC_TRAFFIC"]!}</th>
				</#if>	
				<#if montotalAdc.condition.uptimeAge.isSelect()>
					<th data-colidx="8">${LANGCODEMAP["MSG_ALERT_UPTIME"]!}</th>
				<#else>			
					<th class="none" data-colidx="8">${LANGCODEMAP["MSG_ALERT_UPTIME"]!}</th>
				</#if>
				<#if montotalAdc.condition.sslCertValidDays.isSelect()>
					<th data-colidx="15">${LANGCODEMAP["MSG_APPLIANCE_SSL_AUTH_PERIOD"]!}</th>
				<#else>			
					<th class="none" data-colidx="15">${LANGCODEMAP["MSG_APPLIANCE_SSL_AUTH_PERIOD"]!}</th>
				</#if>
			</tr>
		</thead>
		<tbody>
			<tr class="chkFilterOption">
		<#if montotalAdc.condition.status.isSelect()>
			<td data-colidx="0">
		<#else>			
			<td class= none" data-colidx="0">
		</#if>
				<div class="multcheck">
				<ul class="chklist" data-group="group1" data-colidx="0">
					<#if montotalAdc.condition.status.isSelect()>
						<#list montotalAdc.condition.status.filter as theFilter>
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
		<#if montotalAdc.condition.type.isSelect()>
			<td data-colidx="1">
		<#else>			
			<td class= none" data-colidx="1">
		</#if>
				<div class="multcheck">
				<ul class="chklist" data-group="group2" data-colidx="1">
					<#if montotalAdc.condition.type.isSelect()>
						<#list montotalAdc.condition.type.filter as theFilter>
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
		<#if montotalAdc.condition.activeBackupState.isSelect()>
			<td data-colidx="3">
		<#else>			
			<td class= none" data-colidx="3">
		</#if>
				<div class="multcheck">
				<ul class="chklist" data-group="group3" data-colidx="3">	
					<#list montotalAdc.condition.activeBackupState.filter as theFilter>
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
		<#if montotalAdc.condition.model.isSelect()>
			<td class="align_left" data-colidx="4">
		<#else>			
			<td class="align_left none" data-colidx="4">
		</#if>	
				<div class="multcheck">
				<ul class="chklist modelFilter" data-group="group4" data-colidx="4">							
					<#list montotalAdc.condition.model.filter as theFilter>
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
					</#if>
					</#list>
					<#list montotalAdc.condition.model.filter as theFilter>
					<#if theFilter.index??>
						<#if theFilter.index == 1>
						<span class="vender F5vender none">F5</span>
						</#if>
					</#if>
					</#list>
					<#list montotalAdc.condition.model.filter as theFilter>
					<#if theFilter.index == 1>
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
					<#list montotalAdc.condition.model.filter as theFilter>
					<#if theFilter.index??>
						<#if theFilter.index == 2>
						<span class="vender Alteonvender none">Alteon</span>
						</#if>
					</#if>
					</#list>
					<#list montotalAdc.condition.model.filter as theFilter>
					<#if theFilter.index == 2>
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
					<#list montotalAdc.condition.model.filter as theFilter>
					<#if theFilter.index??>
						<#if theFilter.index == 3>
						<span class="vender PioLinkvender none">PioLink</span>
						</#if>
					</#if>
					</#list>		
					<#list montotalAdc.condition.model.filter as theFilter>
					<#if theFilter.index == 3>		
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
		<#if montotalAdc.condition.swVersion.isSelect()>
			<td class="align_left" data-colidx="5">
		<#else>			
			<td class="align_left none" data-colidx="5">
		</#if>		
				<div class="multcheck">
				<ul class="chklist"  data-group="group5" data-colidx="5">							
					<#list montotalAdc.condition.swVersion.filter as theFilter>
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
					</#if>
					</#list>
					<#list montotalAdc.condition.swVersion.filter as theFilter>
					<#if theFilter.index??>
						<#if theFilter.index == 1>
						<span class="vender F5version none">F5</span>
						</#if>
					</#if>
					</#list>
					<#list montotalAdc.condition.swVersion.filter as theFilter>
					<#if theFilter.index == 1>						
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
					<#list montotalAdc.condition.swVersion.filter as theFilter>
					<#if theFilter.index??>
						<#if theFilter.index == 2>
						<span class="vender Alteonversion none">Alteon</span>
						</#if>
					</#if>
					</#list>
					<#list montotalAdc.condition.swVersion.filter as theFilter>
					<#if theFilter.index == 2>						
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
					<#list montotalAdc.condition.swVersion.filter as theFilter>
					<#if theFilter.index??>
						<#if theFilter.index == 3>
						<span class="vender PioLinkversion none">PioLink</span>
						</#if>
					</#if>
					</#list>	
					<#list montotalAdc.condition.swVersion.filter as theFilter>
					<#if theFilter.index == 3>						
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
		<#if montotalAdc.condition.concurrentSession.isSelect()>
			<td data-colidx="6">
		<#else>			
			<td class="none" data-colidx="6">
		</#if>
				<div class="multcheck">
				<ul class="chklist" data-group="group6" data-colidx="6">							
					<#list montotalAdc.condition.concurrentSession.filter as theFilter>
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
		<#if montotalAdc.condition.throughput.isSelect()>
			<td data-colidx="7">
		<#else>			
			<td class="none" data-colidx="7">
		</#if>
				<div class="multcheck">
				<ul class="chklist"  data-group="group7" data-colidx="7">								
					<#list montotalAdc.condition.throughput.filter as theFilter>
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
					<#if montotalAdc.condition.throughput.isSelect()>
						<#if theFilter.isSelect()>		  				
			  				<input class="" name="selChk" type="checkbox" value="${(theFilter.value)!''}" checked="checked">
			  			<#else>		  				
			  				<input class="" name="selChk" type="checkbox" value="${(theFilter.value)!''}">
			  			</#if> 		
			  		<#else>
			  			<#if theFilter.isSelect()>		  				
			  				<input class="" name="selChk" type="checkbox" value="${(theFilter.value)!''}" checked="checked" disabled="disabled">
			  			<#else>		  				
			  				<input class="" name="selChk" type="checkbox" value="${(theFilter.value)!''}" disabled="disabled">
			  			</#if> 		
			  		</#if>
						<span class="none" name="selIndex">${(theFilter.index)!''}</span>		
						<label>${(theFilter.title)!''}</label>
					</li>
					</#if>
					</#list>
				</ul>
				</div>
			</td>
		<#if montotalAdc.condition.uptimeAge.isSelect()>
			<td data-colidx="8">
		<#else>			
			<td class="none" data-colidx="8">
		</#if>
				<div class="singlecheck">
				<ul class="chklist"  data-group="group8" data-colidx="8">							
					<#list montotalAdc.condition.uptimeAge.filter as theFilter>
					<#if (theFilter.index == 0)>
					<li class="chkFilterAll none">	
						<#if theFilter.isSelect()>		  				
			  				<input class="none" name="selChk" type="checkbox" value="${(theFilter.value)!''}" checked="checked">
			  			<#else>		  				
			  				<input class="none" name="selChk" type="checkbox" value="${(theFilter.value)!''}">
			  			</#if> 		
						<span class="none" name="selIndex">${(theFilter.index)!''}</span>		
						<label>${(theFilter.title)!''}</label>
					</li>
					<#else>								
					<li class="chkFilter chkFilterUptime" isClick="0">
						<#if theFilter.isSelect()>		  				
			  				<input class="none" name="selChk" type="checkbox" value="${(theFilter.value)!''}" checked="checked">
			  			<#else>		  				
			  				<input class="none" name="selChk" type="checkbox" value="${(theFilter.value)!''}">
			  			</#if> 	
						<span class="none" name="selIndex">${(theFilter.index)!''}</span>	
						
						<!--<input type="hidden" value="${(theFilter.value)!''}">-->			
						<a href="#" class="titleFiled"><label>${(theFilter.title)!''}</label></a>
						<!--<span class="none" name="selIndex">${(theFilter.index)!''}</span>-->
					</li>
					</#if>			
					</#list>	
				</ul>									
				</div>
			</td>
		<#if montotalAdc.condition.sslCertValidDays.isSelect()>
			<td data-colidx="15">
		<#else>			
			<td class="none" data-colidx="15">
		</#if>
				<div class="singlecheck">
				<ul class="chklist"  data-group="group9" data-colidx="15">							
					<#list montotalAdc.condition.sslCertValidDays.filter as theFilter>
					<#if (theFilter.index == 0)>
					<li class="chkFilterAll none">	
						<#if theFilter.isSelect()>		  				
			  				<input class="none" name="selChk" type="checkbox" value="${(theFilter.value)!''}" checked="checked">
			  			<#else>		  				
			  				<input class="none" name="selChk" type="checkbox" value="${(theFilter.value)!''}">
			  			</#if> 	
						<span class="none" name="selIndex">${(theFilter.index)!''}</span>		
						<label>${(theFilter.title)!''}</label>
					</li>
					<#else>	
					<li class="chkFilter chkFilterSsl" isClick="0">
						<input class="none" name="selChk" type="checkbox" value="${(theFilter.value)!''}">	
						<span class="none" name="selIndex">${(theFilter.index)!''}</span>
						<!--<input type="hidden" value="${(theFilter.value)!''}">-->			
						<a href="#" class="titleFiled disabled"><label>${(theFilter.title)!''}</label></a>
						<!--<span class="none" name="selIndex">${(theFilter.index)!''}</span>-->
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
	<span> ${LANGCODEMAP["MSG_APPLIANCE_FILTER_SELECT"]!}</span>
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
	<!----- Contents List Start ----->
	<div class="BoardOuter">
	<div class="fixed-table-container-inner_xScroll">
	<table id="adclistTable" class="fixed-table_xScroll adcMonListTable" cellpadding="0" cellspacing="0"  style="table-layout: fixed;">
		<#include "adcMonitoringTableInListContent.ftl"/>
	</table>	
	</div>	
	</div>
	<div class="dataNotExistMsg none">${LANGCODEMAP["MSG_DATA_NOT_EXIST"]!}</div>
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