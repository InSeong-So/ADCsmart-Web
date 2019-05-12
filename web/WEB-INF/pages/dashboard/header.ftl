<#setting number_format="0.####">
<#if langCode??>
  <#if "ko_KR" == langCode><#assign img_lang = ""></#if>
  <#if "en_US" == langCode><#assign img_lang = "_eng"></#if>
</#if>
<div class="logo2">
	<span class="Refresh">
		<span class="auto">
		<input type="checkbox" name="autoRefreshChk" class="autoRefreshChk inputCheckbox" checked="checked"/> ${LANGCODEMAP["MSG_DASHBOARD_AUTOREFRESH"]!}
		</span>
<!--
		<span class="btn">
	 		<a title="refresh" class="refresh-btn" href="#">
	 			<img src="/imgs/meun/btn_refresh.png">			 			
	 		</a>
 		</span>
 --> 		
	</span>
	<span id="refresh_timestamp" class="Refresh_timestamp"></span>
</div>
<div class="contents_area">
	<div class="widget_area">
		<div class="widget_box">
		</div>
	</div>
	<table class="widget_menu" cellpadding="0" cellspacing="0">
		<tr class="StartLine">	
			<td></td>
		</tr>	
		<tr>
		<td>	
			<div class="menu">
				<span class="margin">
					<a class="" href="#" id="dashboardSave" title="${LANGCODEMAP["MSG_DASHBOARD_SAVE"]!}"><img src="/imgs/btn/btn_save1.gif" alt="${LANGCODEMAP["MSG_DASHBOARD_SAVE"]!}" /></a>
				</span>
				<span class="margin">
					<a class="" href="#" id="dashboardNewSave" title="${LANGCODEMAP["MSG_DASHBOARD_NEWNAME_SAVE"]!}"><img src="/imgs/btn/btn_saveas.gif" alt="${LANGCODEMAP["MSG_DASHBOARD_NEWNAME_SAVE"]!}"/></a>
				</span>	
				<span>
					<a class="" href="#" id="dashboardDelete" title="${LANGCODEMAP["MSG_DASHBOARD_DELETE"]!}"><img src="/imgs/btn/btn_del.gif" alt="${LANGCODEMAP["MSG_DASHBOARD_DELETE"]!}"/></a>
				</span>
				<span class="menuR">	
					<span class="favorite_title">${LANGCODEMAP["MSG_DASHBOARD_LAST_SELECT_DASHBOARD"]!}</span>
					<#if dashboardLastList[0]?has_content>
					<img src="/imgs/dashboard/num_1.png" alt="${LANGCODEMAP["MSG_DASHBOARD_NO1"]!}" />				
					<span class="favorite_Rankings">
						<a class="lastDashboard1 favorite" id="${dashboardLastList[0].indexKey}" href="#">
							<span class="favorite_txt" title="${dashboardLastList[0].name!''}">${dashboardLastList[0].name!""}</span>			
						</a>
					</span>
					</#if>
					<#if dashboardLastList[1]?has_content>
					<img src="/imgs/dashboard/num_2.png" alt="${LANGCODEMAP["MSG_DASHBOARD_NO2"]!}" />
					<span class="favorite_Rankings">
						<a class="lastDashboard2" id="${dashboardLastList[1].indexKey}" href="#">
							<span class="favorite_txt" title="${dashboardLastList[1].name!''}">${dashboardLastList[1].name!""}</span>					
						</a>
					</span>
					</#if>
					<#if dashboardLastList[2]?has_content>
					<img src="/imgs/dashboard/num_3.png" alt="${LANGCODEMAP["MSG_DASHBOARD_NO3"]!}" />
					<span class="favorite_Rankings">
						<a class="lastDashboard3" id="${dashboardLastList[2].indexKey}" href="#">
							<span class="favorite_txt" title="${dashboardLastList[2].name!''}">${dashboardLastList[2].name!""}</span>	
						</a>
					</span>
					</#if>					
	          		<select class="DashboardList widget_select width170" id="DashboardList">
	            		<option value="0">${LANGCODEMAP["MSG_DASHBOARD_PLEASE_SELECT"]!}</option>
	            		<#if dashboardInfoList?has_content>
		            		<#list dashboardInfoList as theDashboard>
			            		<option value="${theDashboard.indexKey}">${theDashboard.name!""}</option>
		            		</#list>
	            		</#if>
	            	</select> 
	            	<span class="addBtn">
				 		<input type="button" id="addWidget" class="Btn_blue" value="${LANGCODEMAP["MSG_DASHBOARD_WIDGET_ADD"]!}"/>	
					</span>					 				
				    <span class="">
						<input type="button" id="refresh" class="Btn_white refresh-btn" value="${LANGCODEMAP["MSG_DASHBOARD_REFRESH"]!}"/>
					</span> 	
	 			</span>
			</div>						
		</td>			
		</tr>
		<tr class="EndLine">
			<td ></td>
		</tr>
	</table>
</div>
<div class="dashboardContentArea">
	<#include "content.ftl"/>
</div>

<!-- 위젯추가 팝업 시작 -->
<div id="addWidgetPopup" class="pop_type_wrap">
	<h2>${LANGCODEMAP["MSG_DASHBOARD_WIDGET_ADD"]!}</h2>
	<div class="pop2_contents">
		<table class="widget_add_table100" cellpadding="0" cellspacing="0" >
		<colgroup>
			<col width="258px"/>
			<col width="258px"/>			
			<col width="auto"/>
		</colgroup>
			<tr>
				<td>
				<div class="titleL">${LANGCODEMAP["MSG_DASHBOARD_WIDGET_SELECT"]!} </div>
					<select name="widget_select" size="13" id="widget_select" class="widget_popup_Textarea">
						<#if widgetItemList?has_content>
	            			<#list widgetItemList as theWidget>
								<option value="${theWidget.index}" widthMinSize="${theWidget.widthMinSize}" widthMaxSize="${theWidget.widthMaxSize}" heightMinSize="${theWidget.heightMinSize}" heightMaxSize="${theWidget.heightMaxSize}" id="${theWidget.targetArea}">${theWidget.name!""}</option>
							</#list>
            			</#if>   
					</select>
				</td>
				<td>
					<div class="titleL">${LANGCODEMAP["MSG_DASHBOARD_PREVIEW"]!} </div>
					<div class="thumbnail">
						<img src="/imgs/dashboard/thumbnail_basic.jpg" alt="${LANGCODEMAP["MSG_DASHBOARD_PREVIEW"]!}" />
					</div>
					<table class="widget_summery_table" cellpadding="0" cellspacing="0" >
						<tr>
							<td class="td2">
								<div class="selected_widget"></div>
								<div style="display: none;" class="selected_category">-</div>
								<div style="display: none;" class="selected_group">-</div>
								<div style="display: none;" class="selected_group_index"></div>
								<div style="display: none;" class="selected_adc">-</div>
								<div style="display: none;" class="selected_adc_index"></div>
								<div style="display: none;" class="selected_adc_vendor"></div>
								<div style="display: none;" class="selected_vserver">-</div>
								<div style="display: none;" class="selected_vserver_index"></div>
								<div style="display: none;" class="selected_select_list">-</div>
								<div style="display: none;" class="selected_select_list_count">0</div>
							</td>
						</tr>
					</table>
					<div class="selected_moreinfo" style="display: none;">
						<div class="selected_time"></div>
						<div class="selected_status"></div>
						<div class="selected_statusVs">10</div>
						<div class="selected_ordering"></div>
						<div class="selected_chart"></div>
					</div>
					<div class="widget_summery_table" cellpadding="0" cellspacing="0" >
						<div class="txt widgetNoticeTxt"></div>
					</div>
				</td>				
				<td>
					<div class="titleR">${LANGCODEMAP["MSG_DASHBOARD_TARGET_SELECT"]!}
						<span class="target_header_radio">
						<input type="radio" name="target_type" id="select_typeall" value="all" checked="checked"/>
							<label>${LANGCODEMAP["MSG_ALERT_ONE"]!}</label>
						<input type="radio" name="target_type" id="select_typegroup" value="group" />
							<label>${LANGCODEMAP["MSG_ALERT_GROUP"]!}</label>																		
						</span>
					</div>
			  		<div class="widget_select_table AllSelectTr">
							<span class="head">${LANGCODEMAP["MSG_DASHBOARD_GROUP"]!}</span>
							<span class="select">&nbsp; ${LANGCODEMAP["MSG_DASHBOARD_ALL_SELECT"]!}</span>
			  		</div>	
			  		<div class="widget_select_table GroupSelectTr notop">
							<span class="head">${LANGCODEMAP["MSG_DASHBOARD_GROUP"]!}</span>
							<span class="GroupContent select">
							<select name="" id="target_group" class="widget_popup_Select" disabled="disabled;">
									<option value="0">${LANGCODEMAP["MSG_DASHBOARD_GROUP_SELECT"]!}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
							</select>
							</span>
			  		</div>			  					  					
			  		<div class="widget_select_table AdcSelectTr">
							<span class="head">ADC</span>
							<span class="adcContent select">
									<select id="target_adc" class="widget_popup_Select" disabled="disabled;">
									<option value="0" vendor="0">ADC Select&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
								</select>
							</span>			  				  		
			  		</div>		
			  		<div class="widget_select_table VsSelectTr">
							<span class="head">VS/Service</span>
							<span class="vsContent select">
								<select name="" id="target_vserver" class="widget_popup_Select" disabled="disabled;">
									<option value="0">VS Select&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
								</select>
							</span>				  		
			  		</div>
			  		<div class="widget_select_table FlbGroupSelectTr">
							<span class="head">FLB Group</span>
							<span class="flbGroupContent select">
								<select name="" id="target_flbGroup" class="widget_popup_Select" disabled="disabled;">
									<option value="0">FLB Group Select&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
								</select>
							</span>				  		
			  		</div>
			  		<div class="widget_select_table ExtendGroupSelectTr">
						<span class="head">${LANGCODEMAP["MSG_DASHBOARD_GROUP"]!}</span>
						<span class="extendGroupContent select">
						<select name="" id="target_extend_group" class="widget_popup_Select" disabled="disabled;">
								<option value="0">${LANGCODEMAP["MSG_DASHBOARD_GROUP_SELECT"]!}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
						</select>
						</span>
			  		</div>			  		
				 	<div class="widget_select_table list_container">
	   				    <span class="head2">Member</span>
						<span class="list"></span>
					</div>									
			  		
					<div class="titleR moreOptionHeader" style="display: none;">${LANGCODEMAP["MSG_DASHBOARD_MORE_OPTION"]!} </div>			  				
			  		<div class="widget_select_table" style="display: none;">
							<span class="head">Status</span>
							<span class="select">
								<select name="" id="moreInfo_Status" class="widget_popup_Select">
									<option value="-1" selected="selected">${LANGCODEMAP["MSG_DASHBOARD_STATUS_SELECT"]!}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
									<option value="0">${LANGCODEMAP["MSG_DASHBOARD_ALL"]!}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
									<option value="1">${LANGCODEMAP["MSG_DASHBOARD_ENABLE"]!}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
									<option value="2">${LANGCODEMAP["MSG_DASHBOARD_DISABLE"]!}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
								</select>
							</span>				  		
			  		</div>
			  		<div class="widget_select_table" style="display: none;">
							<span class="head">${LANGCODEMAP["MSG_DASHBOARD_DATE"]!}</span>
							<span class="select">
								<select name="" id="moreInfo_Time" class="widget_popup_Select">
									<option value="-1" selected="selected">${LANGCODEMAP["MSG_DASHBOARD_SELET_DATE"]!}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
									<option value="0">${LANGCODEMAP["MSG_DASHBOARD_ALL"]!}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
									<option value="1" >${LANGCODEMAP["MSG_DASHBOARD_ONE_DAY"]!}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
									<option value="7">${LANGCODEMAP["MSG_DASHBOARD_SEVEN_DAY"]!}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
									<option value="15">${LANGCODEMAP["MSG_DASHBOARD_FIFTEEN_DAY"]!}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
									<option value="30">${LANGCODEMAP["MSG_DASHBOARD_THIRTY_DAT"]!}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
								</select>
							</span>				  		
			  		</div>
			  		<div class="widget_select_table" style="display: none;">
							<span class="head">Status</span>
							<span class="select">
								<select name="" id="moreInfo_StatusVs" class="widget_popup_Select">
									<option value="10" selected="selected">${LANGCODEMAP["MSG_DASHBOARD_ALL"]!}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
									<option value="11" >${LANGCODEMAP["MSG_DASHBOARD_ENABLE"]!}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
									<option value="12">${LANGCODEMAP["MSG_DASHBOARD_DISABLE"]!}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
									<option value="14">${LANGCODEMAP["MSG_DASHBOARD_OFF"]!}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
								</select>	
							</span>				  		
			  		</div>
			  		<div class="widget_select_table" style="display: none;">
							<span class="head">${LANGCODEMAP["MSG_DASHBOARD_ORDER_SELECT"]!}</span>
							<span class="select">
								<select name="" id="moreInfo_Ordering" class="widget_popup_Select">
									<option value="-1" selected="selected">${LANGCODEMAP["MSG_DASHBOARD_SELET_ORDER"]!}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
									<option value="37" >${LANGCODEMAP["MSG_DASHBOARD_SESSIONS"]!}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
									<option value="38" >${LANGCODEMAP["MSG_DASHBOARD_BPSIN"]!}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
									<option value="39">${LANGCODEMAP["MSG_DASHBOARD_BPSOUT"]!}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
									<option value="40">${LANGCODEMAP["MSG_DASHBOARD_BPSTOTAL"]!}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
								</select>	
							</span>				  		
			  		</div>
			  		<div class="widget_select_table" style="display: none;">
							<span class="head">${LANGCODEMAP["MSG_DASHBOARD_ORDER_SELECT"]!}</span>
							<span class="select">
								<select name="" id="moreInfo_Chart" class="widget_popup_Select">
									<option value="-1" selected="selected">${LANGCODEMAP["MSG_DASHBOARD_SELET_ORDER"]!}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
									<option value="0" >Basic Line &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
									<option value="1" >Stacked Area &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
								</select>	
							</span>				  		
			  		</div>
			  		<div class="btn_bottom">
			  		<input type="button" id="widget_add" class="Btn_red widget_add_btn" value="${LANGCODEMAP["MSG_DASHBOARD_ADD"]!}"/> 
					<input type="button" id="widget_close" class="Btn_white widget_add_btn" value="${LANGCODEMAP["MSG_DASHBOARD_CLOSE"]!}"/>  			  		
					</div>			  					  					
				</td>
			</tr>
		</table>
	</div>
	<p id="widget_close" style="position:absolute; right:10px; top:6px;">
		<a href="#" title="${LANGCODEMAP["MSG_DASHBOARD_CLOSE"]!}"> <img src="/imgs/popup/btn_clse.gif"
			alt="${LANGCODEMAP["MSG_DASHBOARD_CLOSE"]!}" />
		</a>
	</p>
</div>
<!-- 위젯추가 팝업  끝 -->

<!-- 위젯대상 수정 팝업 시작   -->
<div id="widgetModifyPopup" class="pop_type_wrap">
	<h2>${LANGCODEMAP["MSG_DASHBOARD_TARGET_CHANGE"]!}</h2>
	<div class="pop2_contents">
		<table class="widget_add_table100" cellpadding="0" cellspacing="0" >
			<tr>
				<td>
					<div class="titleR">${LANGCODEMAP["MSG_DASHBOARD_TARGET_SELECT"]!}
						<span class="target_header_radio_Modify">
							<input type="radio" name="target_type_Modify" id="select_typeall_Modify" value="all" checked="checked"/>
								<label>${LANGCODEMAP["MSG_ALERT_ONE"]!}</label>
							<input type="radio" name="target_type_Modify" id="select_typegroup_Modify" value="group" />
								<label>${LANGCODEMAP["MSG_ALERT_GROUP"]!}</label>																		
						</span>					
					</div>
			  		<div class="widget_select_table AllSelectTr_Modify">
						<span class="head">${LANGCODEMAP["MSG_DASHBOARD_GROUP"]!}</span>
						<span class="select">&nbsp; ${LANGCODEMAP["MSG_DASHBOARD_ALL_SELECT"]!}</span>
			  		</div>	
			  		<div class="widget_select_table GroupSelectTr_Modify">
						<span class="head">${LANGCODEMAP["MSG_DASHBOARD_GROUP"]!}</span>
						<span class="GroupContent select">
							<select name="" id="target_group_Modify" class="widget_popup_Select" disabled="disabled;">
								<option value="0">${LANGCODEMAP["MSG_DASHBOARD_GROUP_SELECT"]!}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
							</select>
						</span>
			  		</div>			  					  					
			  		<div class="widget_select_table AdcSelectTr_Modify">
						<span class="head">ADC</span>
							<span class="adcContent select">
								<select id="target_adc_Modify" class="widget_popup_Select" disabled="disabled;">
									<option value="0" vendor="0">ADC Select&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
								</select>
							</span>			  				  		
			  		</div>		
			  		<div class="widget_select_table VsSelectTr_Modify">
						<span class="head">VS/Service</span>
						<span class="vsContent_Modify select">
							<select name="" id="target_vserver_Modify" class="widget_popup_Select" disabled="disabled;">
								<option value="0">VS Select&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
							</select>
						</span>				  		
			  		</div>
			  		<div class="widget_select_table FlbGroupSelectTr_Modify">
							<span class="head">FLB Group</span>
							<span class="flbGroupContent_Modify select">
								<select name="" id="target_flbGroup_Modify" class="widget_popup_Select" disabled="disabled;">
									<option value="0">FLB Group Select&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
								</select>
							</span>				  		
			  		</div>
			  		<div class="widget_select_table ExtendGroupSelectTr_Modify">
							<span class="head">${LANGCODEMAP["MSG_DASHBOARD_GROUP"]!}</span>
							<span class="extendGroupContent_Modify select">
							<select name="" id="target_extend_group_Modify" class="widget_popup_Select" disabled="disabled;">
									<option value="0">${LANGCODEMAP["MSG_DASHBOARD_GROUP_SELECT"]!}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
							</select>
							</span>
			  		</div>
			  		<div class="widget_select_table list_container_Modify">
	   				    <span class="head2">Member</span>
						<span class="list"></span>
					</div>  		
			  		
					<div class="titleR moreOptionHeader_Modify" style="display: none;">${LANGCODEMAP["MSG_DASHBOARD_MORE_OPTION"]!} </div>			  				
			  		<div class="widget_select_table" style="display: none;">
						<span class="head">Status</span>
						<span class="select">
							<select name="" id="moreInfo_Status_Modify" class="widget_popup_Select">
								<option value="-1" selected="selected">${LANGCODEMAP["MSG_DASHBOARD_STATUS_SELECT"]!}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
								<option value="0">${LANGCODEMAP["MSG_DASHBOARD_ALL"]!}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
								<option value="1">${LANGCODEMAP["MSG_DASHBOARD_ENABLE"]!}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
								<option value="2">${LANGCODEMAP["MSG_DASHBOARD_DISABLE"]!}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
							</select>
						</span>				  		
			  		</div>
			  		<div class="widget_select_table" style="display: none;">
						<span class="head">${LANGCODEMAP["MSG_DASHBOARD_DATE"]!}</span>
						<span class="select">
							<select name="" id="moreInfo_Time_Modify" class="widget_popup_Select">
								<option value="-1" selected="selected">${LANGCODEMAP["MSG_DASHBOARD_SELET_DATE"]!}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
								<option value="0">${LANGCODEMAP["MSG_DASHBOARD_ALL"]!}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
								<option value="1" >${LANGCODEMAP["MSG_DASHBOARD_ONE_DAY"]!}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
								<option value="7">${LANGCODEMAP["MSG_DASHBOARD_SEVEN_DAY"]!}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
								<option value="15">${LANGCODEMAP["MSG_DASHBOARD_FIFTEEN_DAY"]!}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
								<option value="30">${LANGCODEMAP["MSG_DASHBOARD_THIRTY_DAT"]!}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
							</select>
						</span>				  		
			  		</div>
			  		<div class="widget_select_table" style="display: none;">
						<span class="head">Status</span>
						<span class="select">
							<select name="" id="moreInfo_StatusVs_Modify" class="widget_popup_Select">
								<option value="10" selected="selected">${LANGCODEMAP["MSG_DASHBOARD_ALL"]!}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
								<option value="11" >${LANGCODEMAP["MSG_DASHBOARD_ENABLE"]!}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
								<option value="12">${LANGCODEMAP["MSG_DASHBOARD_DISABLE"]!}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
								<option value="14">${LANGCODEMAP["MSG_DASHBOARD_OFF"]!}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
							</select>
						</span>				  		
			  		</div>
			  		<div class="widget_select_table" style="display: none;">
							<span class="head">${LANGCODEMAP["MSG_DASHBOARD_ORDER_SELECT"]!}</span>
							<span class="select">
								<select name="" id="moreInfo_Ordering_Modify" class="widget_popup_Select">
									<option value="-1" selected="selected">${LANGCODEMAP["MSG_DASHBOARD_SELET_ORDER"]!}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
									<option value="37" >${LANGCODEMAP["MSG_DASHBOARD_SESSIONS"]!}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
									<option value="38" >${LANGCODEMAP["MSG_DASHBOARD_BPSIN"]!}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
									<option value="39">${LANGCODEMAP["MSG_DASHBOARD_BPSOUT"]!}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
									<option value="40">${LANGCODEMAP["MSG_DASHBOARD_BPSTOTAL"]!}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
								</select>	
							</span>				  		
			  		</div>
			  		<div class="widget_select_table" style="display: none;">
							<span class="head">${LANGCODEMAP["MSG_DASHBOARD_ORDER_SELECT"]!}</span>
							<span class="select">
								<select name="" id="moreInfo_Chart_Modify" class="widget_popup_Select">
									<option value="-1" selected="selected">${LANGCODEMAP["MSG_DASHBOARD_SELET_ORDER"]!}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
									<option value="0" >Basic Line &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
									<option value="1" >Stacked Area &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
								</select>	
							</span>				  		
			  		</div>
			  		<div class="widget_summery_table_Modify" cellpadding="0" cellspacing="0" style="display: none;">						
						<div style="display: none;" class="selected_category_Modify"></div>
						<div style="display: none;" class="selected_group_Modify"></div>
						<div style="display: none;" class="selected_group_index_Modify"></div>					
						<div style="display: none;" class="selected_adc_Modify"></div>
						<div style="display: none;" class="selected_adc_index_Modify"></div>
						<div style="display: none;" class="selected_adc_vendor_Modify"></div>						
						<div style="display: none;" class="selected_vserver_Modify"></div>
						<div style="display: none;" class="selected_vserver_index_Modify"></div>
						<div style="display: none;" class="selected_select_list_Modify">-</div>
						<div style="display: none;" class="selected_select_list_count_Modify">0</div>														
					</div>
			  		<div class="selected_moreinfo" style="display: none;">
						<div class="selected_time_Modify"></div>
						<div class="selected_status_Modify"></div>
						<div class="selected_statusVs_Modify">10</div>
						<div class="selected_ordering_Modify"></div>
						<div class="selected_chart_Modify"></div>
					</div>			  					
			  		<div class="btn_bottom2">
						<input type="button" id="widget_modify"  class="widget_add_btn2 Btn_blue" value="${LANGCODEMAP["MSG_DASHBOARD_APPLY"]!}"/> 
						<input type="button" id="widget_modify_close" class="widget_add_btn2 Btn_white" value="${LANGCODEMAP["MSG_DASHBOARD_CANCEL"]!}"/>   			  		
					</div>	
				</td>
			</tr>
		</table>					
	</div>
	<p id="widget_modify_close" style="position:absolute; right:10px; top:6px;">
		<a href="#" title="${LANGCODEMAP["MSG_DASHBOARD_CLOSE"]!}"> <img src="/imgs/popup/btn_clse.gif"
			alt="${LANGCODEMAP["MSG_DASHBOARD_CLOSE"]!}" />
		</a>
	</p>
</div>
<!-- 위젯대상 수정 팝업  끝 -->

<!-- 대시보드 새 이름으로 저장 팝업 시작 -->
<div id="dashboardNewSavePopup" class="pop_type_wrap">
	<h2>${LANGCODEMAP["MSG_DASHBOARD_NEWNAME_SAVE"]!}</h2>
	<div class="pop2_contents">
			<table class="widget_add_table100" cellpadding="0" cellspacing="0" >
			<tr><td>
					<div class="titleR">${LANGCODEMAP["MSG_DASHBOARD_DASHBOARD_NAME_INPUT"]!} </div>
			  		<div class="widget_select_table ">
							<span class="select_c">
								<input type="text" name="dashboardNameInput" class="dashboardNameInput widget_inputText2"/>							
							</span>
			  		</div>		

			  		<div class="btn_bottom2">
					<input type="button" id="dash_newSave" class="widget_add_btn2 Btn_blue" value="${LANGCODEMAP["MSG_DASHBOARD_SAVE"]!}"/>
                    <input type="button" id="dash_newSave_close"  class="widget_add_btn2 Btn_white" value="${LANGCODEMAP["MSG_DASHBOARD_CANCEL"]!}"/>     					   			  				
					</div>	</td>
			</tr>
		</table>
	</div>
	<p id="dash_newSave_close" style="position:absolute; right:10px; top:6px;">
		<a href="#" title="${LANGCODEMAP["MSG_DASHBOARD_CLOSE"]!}"> <img src="/imgs/popup/btn_clse.gif"
			alt="${LANGCODEMAP["MSG_DASHBOARD_CLOSE"]!}" />
		</a>
	</p>
</div>
<!-- 대시보드 새 이름으로 저장 팝업 끝-->