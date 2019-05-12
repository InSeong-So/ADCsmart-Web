<form id="respSlbUserAddFrm" method="post">
	<p class="tit">수신자 수정</p>				
	<p class="form_first">
		<span>이름</span><input name="slbUser.name" class="respName" type="text" value="${slbUser.name}">
		<span class="none">
			<input name="slbUser.index" class="requestType" type="text" value="${slbUser.index}">
			<input name="slbUser.type" class="requestType" type="text" value="${slbUser.type}">
		</span>			
	</p>
	<p class="form_second">
		<span>부서/팀</span><input name="slbUser.team" class="respTeam" type="text" value="${slbUser.team}">
	</p>
	<p class="form_third">
		<span>휴대전화</span><input name="slbUser.phone" class="respPhone" type="text" value="${slbUser.phone}">&nbsp;
			<span class="txt_gray2">"-" 없이 숫자만 입력하세요.</span>
	</p>	
       <p class="setcheck">						
		<input type="checkbox" class="" id="">
		<label for="">기본 수신자로 설정합니다.</label>
	</p>			
	<div class="center mar_top10">
		<input type="button" class="respOnOk Btn_red" value="확인">   					
		<input type="button" class="respOnCancel Btn_white" value="취소">  		
	</div> 
</form>		 		