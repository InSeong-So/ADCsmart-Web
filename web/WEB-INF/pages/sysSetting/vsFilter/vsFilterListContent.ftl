<div class="contents_area">
	<div>
		<img src="imgs/title/h3_dashboard_vs_filter.gif" class="title_h3"/>		
	</div>
	<div class="control_Board">
		<p class="cont_sch">
			<span class="inputTextposition1" >
				<input name="searchTxt" type="text" class="searchTxt inputText_search" id="searchTxt" value="${searchKey!}">
			</span>
			<span class="btn">
				<a href="javascript:;" class="searchLnk">
					<img src="imgs/meun/btn_search_1.png" alt="검색"/>
				</a>
			</span>
			<span class="total01"> 
				총 <span class="txt_bold noticePageCountInfo">00</span>건 <span class="txt_bold noticePageInfo">(2/4 페이지)</span>
				&nbsp;<span class="msg_log none">※ 로그 최대표시 10,000건. </span>
			</span>
			<#if accountRole! == 'system'>	
			<div class="control_positionR">
				<input type="button" class="hideVSFilterLnk Btn_white" value="숨김"/>  			
                <input type="button" class="showVSFilterLnk Btn_white" value="표시"/>             
		    </div>
			</#if>
		</p>
	</div>
	<br class="clearfix" />
	<!----- Contents List Start ----->
	<table class="Board" cellpadding="0" cellspacing="0" style="table-layout:fixed;">
		<#include "vsFilterTableInContent.ftl">
	</table>
	<div class="dataNotExistMsg none">표시할 데이터가 없습니다.</div>	
	<div class="searchNotMsg none">검색을 완료했습니다. 검색된 결과가 없습니다.</div>
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
 			</td>
			<td>&nbsp; </td>
		</tr>
	</table>
	<!----- Contents Page End ----->
	<div class="Board_97 disabledChk">
	 	<p class="pageNavigator"></p>
		<div id="select2" class="pageRowCountCbx"></div>
	</div>
</div>