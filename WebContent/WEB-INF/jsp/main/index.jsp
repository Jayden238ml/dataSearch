<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1" />
	<meta name="title" content="공공데이터 조회" />
	<meta name="description" content="아파트,시세조회,분양가조회" />
	<meta name="keywords" content="아파트,시세조회,분양가조회" />
	<meta name="robots" content="index, follow">
	<link rel="canonical" href="http://www.datasearch.co.kr">
	<meta property="og:type" content="article" />
	<meta property="og:site_name" content="공공데이터 조회">
	<meta property="og:type" content="article">
	<meta property="og:url" content="http://www.datasearch.co.kr">
	<meta property="og:title" content="아파트정보 | 실거래가 조회">
	<meta property="og:image" content="/static_root/images/common/meta_img.jpg">
	<meta property="og:description" content="데이터 검색 " />

	<title>아파트관리</title>
	<%@ include file="/static_root/inc/usr_top.jsp" %>
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
		
		function fnMove(num){
			var url = "/apt/apt_cont.do";
			if(num == "2"){
				url = "/apt/apt_qna.do";
			}else if(num == "3"){
				url = "/apt/apt_req.do";
			}else if(num == "4"){
				url = "/apt/apt_notice.do";
			}
			$('#frm').attr("action", url);
			$('#frm').submit();
		}
		
		function fnWarrant(){
			$('#frm').attr("action", "/apt/apt_warrant.do");
			$('#frm').submit();
		}
		
		function fnAptInfo(){
			$('#frm').attr("action", "/apt/apt_Info.do");
			$('#frm').submit();
		}
		
	</script>

</head>
<body id="user" class="main_bd">
	<form name="frm" id="frm" method="post" action="">
	</form>
	<div id="wrap">
		<!-- 상단영역 -->
		<%@ include file="/static_root/inc/apt_header.jsp" %>
		<!-- 상단영역 끝 -->
		
		<!-- 메인 콘텐츠영역 -->
		<div id="main_wrap">
			<div class="mn_visual">
				<div class="mn_slogan">
					<h2>우리집 만들기의 첫걸음</h2>
				</div>
				<div class="mn_quick">
					<ul class="clearfix">
						<li onclick="fnAptInfo();">
							<dl class="clearfix">
								<dt>
									<strong>아파트 정보</strong>
									<span>Apartmane Infomation</span>
								</dt>
								<dd><span><img src="/static_root/images/main/mn_quick01.png" alt="아파트 정보" /></span></dd>
							</dl>
						</li>
						<li onclick="fnWarrant();">
							<dl class="clearfix">
								<dt>
									<strong>입주자 관리</strong>
									<span>Infomation</span>
								</dt>
								<dd><span><img src="/static_root/images/main/mn_quick02.png" alt="입주자 관리" /></span></dd>
							</dl>
						</li>
						<li onclick="#">
							<dl class="clearfix">
								<dt>
									<strong>실거래가 조회</strong>
									<span>Infomation</span>
								</dt>
								<dd><span><img src="/static_root/images/main/mn_quick03.png" alt="실거래가 조회" /></span></dd>
							</dl>
						</li>
					</ul>
				</div>
			</div>
			
			<div class="mn_cont" id="cont">
				<div class="mn_cont_btm">
					<div id="cont_box" class="box_selfAc clearfix" onclick="fnMove('1')">
						<h3>사이트 안내</h3>
						<p>Our Apt<br/>사이트를 소개합니다.</p>
						<a href="javascript:fnMove('1');">VIEW MORE <span>+</span></a>
					</div>
					<div id="cont_box" class="box_blue box_jinroRm clearfix" onclick="fnMove('2')">
						<h3>Q&A</h3>
						<p>궁금한 모든 내용을 물어보세요.</p>
						<a href="javascript:fnMove('2');">VIEW MORE <span>+</span></a>
					</div>
					<div id="cont_box" class="box_jobInfo clearfix" onclick="fnMove('3')">
						<h3>견적요청</h3>
						<p>DB관리의 편리성, 지금 문의해 보세요.</p>
						<a href="javascript:fnMove('3');">VIEW MORE <span>+</span></a>
					</div>
					<div id="cont_box" class="box_notice clearfix">
						<div class="nt_title clearfix">
							<h3>공지사항</h3>
							<a href="javascript:fnMove('4');">VIEW MORE <span>+</span></a>
						</div>
						<p>Our Apt 공지사항.</p>
						<ul class="nt_list">
<%-- 							<c:if test="${not empty INIT_DATA.MainBdList}"> --%>
<%-- 								<c:forEach var="item" items="${INIT_DATA.MainBdList }" varStatus="rowStatus"> --%>
									<li class="clearfix">
										부천 일루미스테이트 입주예정자분들 환영합니다.
										<span>2021-05-24</span>
<%-- 										<a href="javascript:fncMainMenuMove('MENU0059','/user/Bd/BdCm010D.do?BD_NO=1&BBS_NO=${item.BBS_NO}','MENU0007');">${item.TITLE}</a> --%>
<%-- 										<span>${item.REGDATE}</span> --%>
									</li>
									<li class="clearfix">
										한화포레나 수원장인 입주예정자분들 환영합니다.
										<span>2021-05-22</span>
									</li>
									<li class="clearfix">
										올마이티ㅋㅋㅋ퍼니 설립
										<span>2021-05-19</span>
									</li>
<%-- 								</c:forEach> --%>
<%-- 							</c:if> --%>
<%-- 							<c:if test="${empty INIT_DATA.MainBdList}"> --%>
<!-- 								<li class="clearfix">등록된 공지사항이 없습니다.</li> -->
<%-- 							</c:if> --%>
						</ul>
					</div>
				</div>
				
			</div>
			
		</div>
	</div>
	<!-- 메인 콘텐츠영역 끝 -->
	<!-- 하단영역 -->
	<%@ include file="/static_root/inc/apt_footer.jsp" %>
	<!-- 하단영역 끝 -->
	
</body>
</html>
