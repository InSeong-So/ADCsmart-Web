<body>
	<div class="contents_area">
		<table class="Board" cellpadding="0" cellspacing="0">
			<caption>&nbsp;</caption>
			<colgroup>
				<col width="120px" />
				<col >
			</colgroup>	
			<tr class="StartLine">	
				<td colspan="2"></td>
			</tr>		
			<tr>
				<th class="Lth2">
					<li>버튼이벤트</li>
				</th>
				<td class="Lth0">					
	                <div class="position_R10">
						<span class="button" >
							<button type="button" id="sortablesave">위치저장하기</button>
						</span>
						<span class="button">
							<button type="button" id="refresh">새로고침</button>
						</span>
						<span class="button">
							<button type="button" id="sizesave">크기저장하기</button>
						</span>
						<span class="button">
							<button type="button" id="printpage">프린트하기</button>
						</span>
					</div>						
				</td>			
			</tr>
			<tr class="EndLine2">
				<td colspan=2"></td>
			</tr>
		</table>
	</div>
	<div class="printArea">
		<div id ="tabs">
			<ul>
				<li><a href="#tabs-1">활성화된 item</a></li>
				<li><a href="#tabs-2">비활성화 item</a></li>	
			</ul>
			<div id ="tabs-1" style="height: 1024px;">
				<div id="sortable1" class="connectedSortable ui-helper-reset">
					<div class="column" id="col1">
						<div class="portlet" id="1">
							<div class="portlet-header">사용중인 ADC 비율 Chart (No.1)</div>
							<div class="portlet-content">
								<div id="testchart3" style="width: 100%; height: 150px;"></div>	
							</div>
						</div>
						<div class="portlet" id="2">
							<div class="portlet-header">F5 ADC Connection Chart (No.2)</div>
							<div class="portlet-content">
								<div id="testchart2" style="width: 100%; height: 150px;"></div>		
							</div>
						</div>
						<div class="portlet" id="3">
							<div class="portlet-header">adcsmart 라이선스 정보 (No.3)</div>
								<div class="portlet-content">
									상태 정보 : 정상<br>
									버전 : v1.2<br>
									모델 이름 : AS5440<br>
									소프트웨어 키 : 7140-b27b2-47e3-31dc6<br>
									사용기한 : unlimited
								</div>
						</div>			
					</div>		
					<div class="column" id="col2">
						<div class="portlet" id="4">
							<div class="portlet-header">192.168.200.11 Vs Connection Chart (No.4)</div>
							<div class="portlet-content">
								<div id="testchart1" style="width: 100%; height: 150px;"></div>			
							</div>
						</div>	
						<div class="portlet" id="5">
							<div class="portlet-header">adcsmart 공지사항 (No.5)</div>
							<div class="portlet-content">
							JQuery UI TEST 중입니다.
							</div>
						</div>
					</div>
				</div>
			</div>
			<div id ="tabs-2" style="height: 1024px;">
				<div id="sortable2" class="connectedSortable ui-helper-reset">
					<div class="column connectedSortable ui-helper-reset" id="col3">
						<div class="portlet" id="6">
							<div class="portlet-header">adcsmart 공지사항 (No.6)</div>
							<div class="portlet-content">
							비활성화 Test 입니다.
							</div>
						</div>
						<div class="portlet" id="7">
							<div class="portlet-header">adcsmart 공지사항 (No.7)</div>
							<div class="portlet-content">
							비활성화 Test2 입니다.
							</div>
						</div>
					</div>
				</div>		
			</div>	
		</div>
	</div>
</body>