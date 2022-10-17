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
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/swiper/swiper-bundle.min.css" />
<script src="https://cdn.jsdelivr.net/npm/swiper/swiper-bundle.min.js"></script>
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
		
		doFunc = function(){
			var swiper = new Swiper(".mySwiper", {
		        navigation: {
		          nextEl: ".swiper-button-next",
		          prevEl: ".swiper-button-prev",
		        },
		      });
		}
		
		
	});
	
	
	//페이징
	var page_List = function(pageNo) {
	    $("#CURR_PAGE").val(pageNo);        
	    $('#frm').submit();
	};
	
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
	
	function fnDetail_pop(idx){
// 		gfnOpenLayerPopup('/common/index.do?jpath=/common/pop_camping&CONTENTID='+idx + '&TYPE=P&TMC=' + $('#TMC').val() + '&LMC=' + $('#LMC').val());
		$('.tableView').hide();
		if(idx != $('#CONTENTID').val()){
			$('#CONTENTID').val(idx);
			$('.swiper-wrapper').html('');
			$('#detail_'+idx).show();
			$.ajax({
				url : "/user/getCampingDetail_Info.do",
				data:  $("#frm").serialize(),
				dataType: "json",
				success: function (transport) {
					dataSet(transport.dtailMap);
					imgSet(transport.list_img);
				},error : function(e) { //ERROR
					alert("조회 중 에러가 발생 하였습니다.");
				}
			});
		}
	}
	
	function imgSet(imgList){
		var img_html = '';
// 		$('.swiper-wrapper').html('');
// 		alert(imgList);
		if(imgList != null){
			$.each(imgList,function(key,json) {
				img_html = '';
				img_html += '<div class="swiper-slide"><img src="'+ json.IMAGEURL +'" title=""></div>';
				$('.swiper-wrapper').append(img_html);
			});
				setTimeout(function() {
					doFunc();
				}, 2000);
		}
	}
	
	
	function dataSet(camping_info){
		$('.FACLTNM').html(camping_info.FACLTNM);
		$('.ADDR').html(camping_info.ADDR1 + " " + camping_info.ADDR2);
		if(camping_info.HOMEPAGE != ""){
			$('.HOMEPAGE_TR').show();
			$('.HOMEPAGE').html('<a href="'+ camping_info.HOMEPAGE +'" target="_blank">'+ camping_info.HOMEPAGE +'</a>');
		}else{
			$('.HOMEPAGE_TR').hide();
		}
		
		if(camping_info.RESVEURL != ""){
			$('.RESVEURL_TR').show();
			$('.RESVEURL').html('<a href="'+ camping_info.RESVEURL +'" target="_blank">'+ camping_info.RESVEURL +'</a>');
		}else{
			$('.RESVEURL_TR').hide();
		}
		// 특징
		if(camping_info.FEATURENM != ""){
			$('.FEATURENM_TR').show();
			$('.FEATURENM').html(camping_info.FEATURENM);
		}else{
			$('.FEATURENM_TR').hide();
		}
		// 소개
		if(camping_info.LINEINTRO != ""){
			$('.INTRO_TR').show();
			$('.INTRO').html(camping_info.LINEINTRO);
		}else{
			$('.INTRO_TR').hide();
		}
		// 예약방법
		if(camping_info.RESVECL != ""){
			$('.RESVECL_TR').show();
			$('.RESVECL').html(camping_info.RESVECL);
		}else{
			$('.RESVECL_TR').hide();
		}
		// 연락처
		if(camping_info.TEL != ""){
			$('.TEL_TR').show();
			$('.TEL').html(camping_info.TEL);
		}else{
			$('.TEL_TR').hide();
		}
		// 부대시설
		if(camping_info.SBRSCL != ""){
			$('.SBRSCL_TR').show();
			$('.SBRSCL').html(camping_info.SBRSCL);
		}else{
			$('.SBRSCL_TR').hide();
		}
		// 주변 이용 시설
		if(camping_info.POSBLFCLTYCL != ""){
			$('.POSBLFCLTYCL_TR').show();
			$('.POSBLFCLTYCL').html(camping_info.POSBLFCLTYCL);
		}else{
			$('.POSBLFCLTYCL_TR').hide();
		}
		
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
							<input type="hidden" name="CONTENTID" id="CONTENTID" value=""/>
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
					                    <a href="javascript:fnDetail_pop('${item.CONTENTID}');" class="link-article">
					                      <c:if test="${not empty item.FIRSTIMAGEURL}">
					                      	<p class="thumbnail" has-thumbnail="1" style="background-image: url('${item.FIRSTIMAGEURL}');"> <img src="${item.FIRSTIMAGEURL}" class="img-thumbnail" role="presentation" title="${item.FACLTNM}"></p>
					                      </c:if>
					                      <c:if test="${empty item.FIRSTIMAGEURL}">
					                      	<p class="thumbnail" has-thumbnail="1" style="background-image: url('/static_root/images/content/no_camp.png');"> <img src="/static_root/images/content/no_camp.png" class="img-thumbnail" role="presentation" title="${item.FACLTNM}"></p>
					                      </c:if>
					                    </a>
					                    <div class="article-content">
					                      <a href="javascript:fnDetail_pop('${item.CONTENTID}');" class="link-article">
					                        <strong class="title">${item.FACLTNM}</strong>
					                        <p class="summary">${item.INTRO}</p>
					                      </a>
					                      <div class="box-meta">
					                        <a href="javascript:fnDetail_pop('${item.CONTENTID}');" class="link-category">${item.ADDR1}</a>
					                        <span class="date">${item.POSBLFCLTYCL}</span>
<!-- 					                        <span class="reply"> -->
<!-- 					                          <s_rp_count></s_rp_count> -->
<!-- 					                        </span> -->
					                      </div>
					                    </div>
					                  </article>
					                  <div class="tableView" id="detail_${item.CONTENTID}" style="display:none;">
					                  <br/>
										<table summary="캠핑장 상세">
											<caption>캠핑장 상세</caption>
											<colgroup>
												<col width="20%">
												<col width="30%">
												<col width="20%">
												<col width="*">
											</colgroup>
											<tbody>
												<tr>
													<th colspan="4" style="text-align:center;" class="FACLTNM"></th>
												</tr>
												<tr>
													<td colspan="4">
														<div class="swiper mySwiper">
													      <div class="swiper-wrapper">
<!-- 													        <div class="swiper-slide"><img src="https://gocamping.or.kr/upload/camp/10/thumb/thumb_720_1869epdMHtUyrinZWKFHDWty.jpg" title="Funky roots"></div> -->
<!-- 													        <div class="swiper-slide"><img src="https://gocamping.or.kr/upload/camp/3605/7443RPycjqODND0kKBcw1X9X.jpg" title="The long and winding road"></div> -->
<!-- 													        <div class="swiper-slide">Slide 3</div> -->
<!-- 													        <div class="swiper-slide">Slide 4</div> -->
<!-- 													        <div class="swiper-slide">Slide 5</div> -->
<!-- 													        <div class="swiper-slide">Slide 6</div> -->
<!-- 													        <div class="swiper-slide">Slide 7</div> -->
<!-- 													        <div class="swiper-slide">Slide 8</div> -->
<!-- 													        <div class="swiper-slide">Slide 9</div> -->
													      </div>
													      <div class="swiper-button-next"></div>
													      <div class="swiper-button-prev"></div>
													    </div>
													</td>
												</tr>
												<tr>
													<th>주소</th>
													<td class="ADDR" colspan="3"></td>
												</tr>
												<tr class = "HOMEPAGE_TR">
													<th>홈페이지</th>
													<td class="HOMEPAGE" colspan="3"></td>
												</tr>
												<tr class = "RESVEURL_TR">
													<th>예약페이지</th>
													<td class="RESVEURL" colspan="3"></td>
												</tr>
												<tr class = "RESVECL_TR">
													<th>예약방법</th>
													<td class="RESVECL" colspan="3"></td>
												</tr>
												<tr class = "TEL_TR">
													<th>연락처</th>
													<td class="TEL" colspan="3"></td>
												</tr>
												<tr class = "SBRSCL_TR">
													<th>부대시설</th>
													<td class="SBRSCL" colspan="3"></td>
												</tr>
												<tr class = "POSBLFCLTYCLTR">
													<th>주변 이용 시설</th>
													<td class="POSBLFCLTYCL" colspan="3"></td>
												</tr>
												<tr class = "FEATURENM_TR">
													<th>특징</th>
													<td class="FEATURENM" colspan="3"></td>
												</tr>
												<tr class = "INTRO_TR">
													<th>소개</th>
													<td class="INTRO" colspan="3"></td>
												</tr>
											</tbody>
										</table>
									<br/>
									</div>
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