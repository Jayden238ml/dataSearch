<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<head>
<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1" />
	<meta name="title" content="공공데이터 조회" />
	<meta name="description" content="캠핑장 조회, 전국 캠핑장 조회, 여행정보, 캠핑장 예약" />
	<meta name="keywords" content="캠핑장 조회, 전국 캠핑장 조회, 여행정보, 캠핑장 예약" />
	<meta name="robots" content="index, follow">
	<link rel="canonical" href="http://www.datasearch.co.kr">
	<meta property="og:type" content="article" />
	<meta property="og:site_name" content="공공데이터 조회">
	<meta property="og:type" content="article">
	<meta property="og:url" content="http://www.datasearch.co.kr">
	<meta property="og:title" content="여행정보 | 캠핑장 조회 | 캠핑장 예약">
	<meta property="og:image" content="/static_root/images/common/meta_img.jpg">
	<meta property="og:description" content="데이터 검색 " />
<title>여행정보 | 캠핑장 조회</title>
<%@ include file="/static_root/inc/usr_top.jsp" %>
<link type="text/css" rel="stylesheet" href="/static_root/css/camping.css" media=""/>
<script type="text/javascript">
	var PAGE_SIZE = "${INIT_DATA.PAGE_SIZE}"; 
	$(document).ready(function(){
		fncMakePageBody('${INIT_DATA.TOTAL_CNT}','${INIT_DATA.CURR_PAGE}');
		$('#frm').find("input").each(function(e){
	        $(this).bind("keyup",function(){
	                if(event.keyCode == 13){
	                	fnSearch();
	                }
	        });
	    });
		
		if("${INIT_DATA.SCH_TOP_LAWD_CD}" != ""){
			fnGetSidoList("${INIT_DATA.SCH_TOP_LAWD_CD}");
		}
		
		$(window).resize(function() { 
			fnAutoResize();
		});
	});
	
	window.onload = function () {
		fnAutoResize();
	}
	
	//페이징
	var page_List = function(pageNo) {
	    $("#CURR_PAGE").val(pageNo);        
	    $('#frm').submit();
	};
	
	function fnAutoResize(){
		var windowWidth = $( window ).width();	
		var content_width = $('.imgTr').width();
// 		alert(content_width - 758);
// 		$('.imgTd img').each(function() {
// 			if($('.videoWrap tr td').width() < this.naturalWidth){
// 				$(this).css("width", "100%");
// 				$(this).css("height", "auto");
// 			}else{
// 				$(this).css("width", $('.videoList').width());
// 				alert(content_width - 758);
// 				$(this).css("width", "100%");
// 				$(this).css("text-align", "center");
// 				$(this).css("margin", "auto");
// 				$(this).css("display", "block");
// 				$(this).css("height", "auto"); 
// 			}
// 		});
	}
	
	function fnSearch(){
		$('#frm').attr("action", "/user/CampingList.do");
		$('#frm').submit();
	}
	
	function fnGetSidoList(){
		$.ajax({
			type: "POST",
			url : "/api/getCampingSidoList.do",
			data:  $("#frm").serialize(),
			dataType: "json",
			success: function (transport) {
				fnSetSido(eval(transport.SidoList));
			}
		});
	}
	
	function fnSetSido(SidoList){
		var resultHtml ="";
		var row =  "<option value=''>시/군/구 선택</option>";
		$.each(SidoList,function(key,json) {
			row += '<option value="'+json.CODENM+'">'+json.CODENM+'</option>';
		});
		resultHtml += row;
		$("#SCH_LAWD_CD").html(resultHtml);
		if("${INIT_DATA.SCH_LAWD_CD}" != ""){
			$("#SCH_LAWD_CD").val("${INIT_DATA.SCH_LAWD_CD}");
		}
	}
	
	function fnDetail(bonbun, area , apt_nm){
		$("#ROAD_NAME_BONBUN").val(bonbun);
		$("#AREA_EXCLUSIVE_USE").val(area);
		$("#APARTMENT_NAME").val(apt_nm);
		var param = "?L_TMC=TMC004&L_LMC=LMC014&ROAD_NAME_BONBUN=" + bonbun + "&AREA_EXCLUSIVE_USE=" + area + "&APARTMENT_NAME=" + escape(encodeURIComponent(apt_nm));
		$('#frm').attr("action", "/api/aptDealDetail.do" + param);
		$('#frm').submit();
	}
	
	
</script>
</head>
<body id="user">
	<div id="wrap">
		<!-- 상단영역 -->
		<%@ include file="/static_root/inc/apt_header.jsp" %>
		<!-- 상단영역 끝 -->

		<ul id="content">
			<li id="left">
				<!-- 좌측영역 -->
				<%@ include file="/static_root/inc/usr_left.jsp" %>
				<!-- 좌측영역 끝 -->
			</li>
			<li id="right">
				<!-- 타이틀/네비 -->
				<div class="titleNaviNew">
					<h3 class="subTitle">캠핑장 조회</h3>
				</div>
				<!-- 타이틀/네비 끝 -->

				<!-- 본문내용 //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// -->
				<div id="cont">
					<section class="sect_area sect_first_area">
						<!-- 검색 -->
						<form name="frm" id="frm" method="post" action="#">
							<input type="hidden" name="TMC" id="TMC" value="${INIT_DATA.TMC}" />
							<input type="hidden" name="LMC" id="LMC" value="${INIT_DATA.LMC}" />
							<input type="hidden" id="CURR_PAGE"  name="CURR_PAGE" value="${INIT_DATA.CURR_PAGE}"/>
							<input type="hidden" name="RTN_URL" id="RTN_URL" value="/user/apt_TradingL.do" />
							<input type="hidden" name="ROAD_NAME_BONBUN" id="ROAD_NAME_BONBUN" value="" />
							<input type="hidden" name="AREA_EXCLUSIVE_USE" id="AREA_EXCLUSIVE_USE" value="" />
							<input type="hidden" name="APARTMENT_NAME" id="APARTMENT_NAME" value="" />
							<fieldset>
								<div class="searchApt">
									<ul>
										<li class="scharea2">
											<ul>
												<li>
													<select name="SCH_TOP_LAWD_CD" id="SCH_TOP_LAWD_CD" title="광역시/도" class="select" onchange="fnGetSidoList();">
														<option value="">시/도 선택</option>
														<c:forEach var="item" items="${INIT_DATA.SidoList }" >
															<option value="${item.DONM}" <c:if test="${item.DONM eq INIT_DATA.SCH_TOP_LAWD_CD}">selected="selected"</c:if> >${item.DONM}</option>
														</c:forEach>
													</select>
												</li>
												<li>
													<select name="SCH_LAWD_CD" id="SCH_LAWD_CD" title="시/군/구 선택" class="select">
														<option value="">시/군/구 선택</option>
													</select>
												</li>
											</ul>
										</li>
										<li class="scharea2">
											<ul>
												<li>
													<select name="SCH_ANIMAL_YN" id="SCH_ANIMAL_YN" title="반려동물 여부" class="select">
														<option value="">반려동물 여부</option>
														<option value="가능" <c:if test="${'가능' eq INIT_DATA.SCH_ANIMAL_YN}">selected="selected"</c:if> >가능</option>
														<option value="가능(소형견)" <c:if test="${'가능(소형견)' eq INIT_DATA.SCH_ANIMAL_YN}">selected="selected"</c:if> >가능(소형견)</option>
														<option value="불가능" <c:if test="${'불가능' eq INIT_DATA.SCH_ANIMAL_YN}">selected="selected"</c:if> >불가능</option>
													</select>
												</li>
												<li>
													<input type="text" name="SCH_APARTMENT_NAME" id="SCH_APARTMENT_NAME" title="캠핑장명/테마/주소" value="${INIT_DATA.SCH_APARTMENT_NAME}" placeholder="캠핑장명/테마/주소" class="input" />
												</li>
											</ul>
										</li>
									</ul>
								<div style="padding:40px 15px;"><a href="javascript:fnSearch();">검색</a></div>  
								</div>
							</fieldset>
						</form>
						<!-- 검색 끝 -->
						<dl class="titleEaB">
							<dt>
								<c:if test="${INIT_DATA.DEFAULT eq 'Y'}">
									검색조건 입력 후 검색해 주세요.&nbsp;&nbsp;
								</c:if>
								<c:if test="${INIT_DATA.DEFAULT ne 'Y'}">
									총 <strong><fmt:formatNumber value="${INIT_DATA.TOTAL_CNT}" pattern="#,###"/></strong>&nbsp;건
								</c:if>
							</dt>
							<dd>
							
							</dd> 
						</dl>
						<!-- 리스트 -->
						<div class="area-common">
							<c:if test="${not empty INIT_DATA.resultList}">
								<c:forEach items="${INIT_DATA.resultList}" var="item" varStatus="status">
									<article class="article-type-common article-type-thumbnail checked-item">
					                    <a href="#" class="link-article">
<%-- 					                      <p class="thumbnail" has-thumbnail="1" style="background-image: url(&quot;https://i1.daumcdn.net/thumb/S160x108/?scode=mtistory2&amp;fname=https://blog.kakaocdn.net/dn/bUa0ep/btrMIoAXFQ0/eD6mvAIKW80hExjJTx00Uk/img.jpg&quot;);"> <img src="${item.FIRSTIMAGEURL}" class="img-thumbnail" role="presentation"> --%>
					                      <p class="thumbnail" has-thumbnail="1" style="background-image: url('${item.FIRSTIMAGEURL}');"> <img src="${item.FIRSTIMAGEURL}" class="img-thumbnail" role="presentation" title="${item.FACLTNM}">
					                      </p>
					                    </a>
					                    <div class="article-content">
					                      <a href="#" class="link-article">
					                        <strong class="title">${item.FACLTNM}</strong>
					                        <p class="summary">${item.INTRO}</p>
					                      </a>
					                      <div class="box-meta">
					                        <a href="#" class="link-category">${item.ADDR1}</a>
					                        <span class="date">${item.POSBLFCLTYCL}</span>
<!-- 					                        <span class="reply"> -->
<!-- 					                          <s_rp_count></s_rp_count> -->
<!-- 					                        </span> -->
					                      </div>
					                    </div>
					                  </article>
								</c:forEach>
							</c:if>
						</div>
<!-- 						<p class="textPoint mbArea mt5">스크롤하실 수 있습니다.</p> -->
						<div class="listPaging mt30" id="paging_bar"></div>
					</section>
				</div>
				<!-- 본문내용 끝 //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// -->
			</li>
		</ul>

		<!-- 하단영역 -->
		<%@ include file="/static_root/inc/apt_footer.jsp" %>
		<!-- 하단영역 끝 -->
	</div>
</body>
</html>