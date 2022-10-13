<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<title>로그인</title>
<%@ include file="/static_root/inc/apt_top.jsp" %>
<script type="text/javascript">

	$(document).ready(function(){
// 		fnDetail_info();
		
	});

	function fnDetail_info(){
		$.ajax({
			url : "/user/getCampingDetail_Info.do",
			data:  $("#popFrm").serialize(),
// 			data:  {"SCH_TOP_LAWD_CD" : "서울시"},
			dataType: "json",
			success: function (transport) {
				alert(transport.SidoList);
			},error : function(request,status,error) { //ERROR
				alert("status:"+status);
			}
			
// 			type: "POST",
// 			url : "/user/getCampingDetail_Info.do",
// 			data:  $("#popFrm").serialize(),
// 			dataType: "json",
// 			success: function (dat) {
// 				alert(dat.dtailMap);
// 			},error : function(request,status,error) { //ERROR
// 				alert(error);
// 			}
		});
// 		$.ajax({
// 			 type		: "POST"
// 			,url		: "/getCampingDetail_Info.do"
// 			,dataType	: "json"
// 			,data		: {
// 				"CONTENTID" : $('#CONTENTID').val()
// 			}
// 			,success : function(transport) {
// 				alert(transport.dtailMap);
// 			}
			
// 		});
	}	
	
	
	
</script>
</head>
<body>
<form name="popFrm" id="popFrm" method="post">
	<input type="hidden" name="CONTENTID" id="CONTENTID" value="${INIT_DATA.CONTENTID}"/>
</form>
	<div class="layerWrap680 login_pop_inner">
		<div class="bg"></div>
		<!-- 로그인 -->
		<div class="loginLayer">
			<p>
				<c:if test="${INIT_DATA.TYPE eq 'P'}">
					<a href="javascript:gfnLayerClose();" class="cbtn"><img src="/static_root/images/content/popClose.jpg" alt="닫기" title="닫기" /></a>
				</c:if>
				<c:if test="${INIT_DATA.TYPE eq 'M'}">
					<a href="javascript:gfnMobileLayerClose();" class="cbtn"><img src="/static_root/images/content/popClose.jpg" alt="닫기" title="닫기" /></a>
				</c:if>
			</p>
			<div id="loginDiv">
				<h1>입주자<span>&nbsp;회원관리 시스템</span></h1>
				<h2>발급받은 아이디와 비밀번호를 사용해 주세요.<br/>발급받지 않은 계정 로그인 시에는 카카오 또는 네이버로 로그인 해 주세요.</h2>
				<div>&nbsp;</div>
				<fieldset>
					<legend>로그인</legend>
					<dl>
						<dt>
							<ul>
<!-- 								<li><input type="text" name="USER_ID" id="USER_ID" value="" title="아이디" class="lgID" placeholder="ID" maxlength="20" onkeyup="if(event.keyCode==13){fnAptLogin();}"/></li> -->
<!-- 								<li><input type="password" name="USER_PWD" id="USER_PWD" value="" title="비밀번호" class="lgPW" placeholder="Password" onkeyup="if(event.keyCode==13){fnAptLogin();}"/></li> -->
							</ul> 
							<div>
								<a href="javascript:fnDetail_info();">로그인</a>
<!-- 								<a href="javascript:fnKaKaoLogin();" style="background:#fae630; color:black;margin-top: 5px;">카카오 로그인</a> -->
							</div>
						</dt>
					</dl>
				</fieldset>
			</div>				
		</div>
		<!-- 로그인 끝 -->
	</div>
</body>
</html>