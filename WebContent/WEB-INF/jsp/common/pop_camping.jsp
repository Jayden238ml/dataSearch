<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<title>로그인</title>
<%@ include file="/static_root/inc/usr_top.jsp" %>
<script type="text/javascript">

	$(document).ready(function(){
// 		fnDetail_info();
		
	});

	function fnDetail_info(){
		$.ajax({
			url : "/user/getCampingDetail_Info.do",
			data:  $("#popFrm").serialize(),
			dataType: "json",
			success: function (transport) {
				dataSet(transport.dtailMap);
				imgSet(transport.list_img);
			},error : function(e) { //ERROR
				alert("조회 중 에러가 발생 하였습니다.");
			}
		});
	}	
	
	function dataSet(dtailMap){
		$('#FACLTNM').text(dtailMap.FACLTNM);
	}
	
	function imgSet(imgList){
		var img_html = '';
		alert(imgList);
		if(imgList != null){
			$.each(imgList,function(key,json) {
				img_html += '<div class="mvs-item" style="background:url("'+json.IMAGEURL+'") center top no-repeat; background-size:cover;">';
				img_html += '	<div class="main-copy">';
				img_html += '		<h1>보호중인 유기동물<br />이젠 주인이 되어 주세요.</h1>';
				img_html += '		<div><a href="/user/animal/adoptAnimal.do">자세히보기</a></div>';
				img_html += '	</div>';
				img_html += '</div>';
			});
// 			for(var i = 0; i < imgList.size; i ++){
// 				img_html += '<div class="mvs-item" style="background:url('/static_root/images/main/dog2.jpg') center top no-repeat; background-size:cover;">';
// 				img_html += '</div>';
// 			}
		}
		alert(img_html);
		$('.main-visual-slick').html(img_html);
// 		<div class="mvs-item" style="background:url('/static_root/images/main/dog2.jpg') center top no-repeat; background-size:cover;">
// 			<div class="main-copy">
// 				<h1>보호중인 유기동물<br />이젠 주인이 되어 주세요.</h1>
// 				<div><a href="/user/animal/adoptAnimal.do">자세히보기</a></div>
// 			</div>
// 		</div>
	}
	
</script>
</head>
<body>
<form name="popFrm" id="popFrm" method="post">
	<input type="hidden" name="CONTENTID" id="CONTENTID" value="${INIT_DATA.CONTENTID}"/>
	<input type="hidden" name="TMC" id="TMC" value="${INIT_DATA.TMC}"/>
	<input type="hidden" name="LMC" id="LMC" value="${INIT_DATA.LMC}"/>
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
				<h2 id="FACLTNM"></h2>
				<div>&nbsp;</div>
				<fieldset>
					<legend>로그인</legend>
					<dl>
						<dt>
							<ul>
								<div class="main-visual-slick">
<!-- 									<div class="mvs-item" style="background:url('/static_root/images/main/dog2.jpg') center top no-repeat; background-size:cover;"> -->
<!-- 										<div class="main-copy"> -->
<!-- 											<h1>보호중인 유기동물<br />이젠 주인이 되어 주세요.</h1> -->
<!-- 											<div><a href="/user/animal/adoptAnimal.do">자세히보기</a></div> -->
<!-- 										</div> -->
<!-- 									</div>					 -->
<!-- 									<div class="mvs-item" style="background:url('/static_root/images/main/dog1.jpg') center top no-repeat; background-size:cover;"> -->
<!-- 										<div class="main-copy"> -->
<!-- 											<h1>잃어버린 반려동물,<br />보호소에서 안전하게 보호하고 있습니다.</h1> -->
<!-- 											<div><a href="/user/animal/findAnimal.do" >자세히보기</a></div> -->
<!-- 										</div> -->
<!-- 									</div> -->
<!-- 									<div class="mvs-item" style="background:url('/static_root/images/main/dog3.jpg') center top no-repeat; background-size:cover;"> -->
<!-- 										<div class="main-copy"> -->
<!-- 											<h1>반려동물을 잃어버렸나요?<br />주위에 널리 알려보세요.</h1> -->
<!-- 											<div><a href="/user/Board/findBoard.do">자세히보기</a></div> -->
<!-- 										</div> -->
<!-- 									</div> -->
								</div>
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