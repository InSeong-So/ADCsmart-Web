<%@ page contentType="text/html; charset=utf-8"%>
<%@ page import="javax.servlet.ServletException"%>
<%@ page isErrorPage="true"%>
<html>
<head>
<title>ADCsmart 오류</title>
<link type="text/css" rel="stylesheet" href="css/button.css"/>	
<link type="text/css" rel="stylesheet" href="css/newContent.css"/>	
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">

<style type="text/css">

body {
	margin-left: 10px;
	margin-top: 10px;
	margin-right: 10px;
	margin-bottom: 10px;
}


body,td,th {
	font-family: "돋움", Dotum, Helvetica, "Apple SD Gothic Neo", Sans-serif; 	FONT-SIZE: 13px;
}
</style>
</head>
<body>
	<table width="450px" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td>
				<table width="100%" border="0">
					<tbody>
						<tr>
							<td>
								<img src="/imgs/common/ADCSmart_error.png" align="center"></img>
							</td>
							<td>&nbsp;불편을 끼쳐드려 죄송합니다.<br>
									&nbsp;이 문제를 해결할 수 있도록 하려면<br> &nbsp;보고서를 보내주시기 바랍니다.
							</td>
						</tr>
					</tbody>
				</table>

				<table width="100%" border="0">
					<tbody>
						<tr>
							<td width="50%">오류 메세지</td>
							<td width="50%">

							</td>
						</tr>
						<tr>
							<td colspan="2">
								<textarea name="msg" id="msg" readonly class="inputTextarea_msa"></textarea>
							</td>
						</tr>
					</tbody>
				</table>
				<table width="100%" border="0">
					<tbody>
						<tr height="20px">
							<td align="right">
                                <span class="button white" href="#" onClick="window.close()" id="refresh">
      								<button class="closeWndLnk" type="button">닫기</button>
								</span> 
							</td>
						</tr>
					</tbody>
				</table>
			</td>
		</tr>
	</table>

	<!-- <a href="" target="_blank">상세 오류 보기</a>  -->

	<script language="javascript">
		var obj = window.dialogArguments;
		document.getElementById("msg").value = obj.gMsg;
		//document.getElementById("desc").value = obj.sMsg;
		//document.getElementById("dms").value = obj.dMsg;
		//document.getElementById("code").value = obj.code;
		//document.getElementById("time").value = obj.oTime;
		//document.getElementById("mna").value = obj.mName;
	</script>

</body>
</html>