<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<script language="javascript">
$(document).ready(function(){
	
});

function fnLeftMenuMove(lmc){
	$.ajax({ 
		 type : "post"
		,url  : "/moveMenu.do"
		,data : {
			"LMC" 	  :lmc
			,"GB" : "L"
		}
		,dataType : "json"
		,success : function(transport) {					
			if(transport.RET_MSG != ''){
				alert(transport.RET_MSG);
			}else{
				var resultMst = eval(transport.leftMenu);
				var leftFrm = document.leftFrm;
				var tmc = resultMst.PRTCODE;
				$('#L_TMC').val(tmc);
				$('#L_LMC').val(lmc);
				leftFrm.target= "_self";
				if(resultMst.MENU_URL.indexOf("?") > -1){
					leftFrm.action=resultMst.MENU_URL;
				}else{
					leftFrm.action=resultMst.MENU_URL;
				}
				leftFrm.submit();
			}
			
		}
	});
}


</script>
<form name="leftFrm" id="leftFrm" method="post" action="">
	<input type="hidden" name="L_TMC" id="L_TMC" value="${sessionScope.TMC }"> 
	<input type="hidden" name="L_LMC" id="L_LMC" value="${sessionScope.LMC }"> 
</form>
	<div class="apt_leftMenu" id="leftMenu">
		<div class="apt_userLeftMenuWrap"><p class="userLeftMenu"></p></div>
			<dl id="LMC014"><dt><a href="/user/apt_TradingL.do?L_TMC=TMC004&L_LMC=LMC014" <c:if test="${sessionScope.LMC eq 'LMC014'}">class="on"</c:if>>아파트 실거래이력</a></dt></dl>
			<dl id="LMC013"><dt><a href="/user/apt_ConceL.do?L_TMC=TMC004&L_LMC=LMC013" <c:if test="${sessionScope.LMC eq 'LMC013'}">class="on"</c:if>>아파트분양권 거래이력</a></dt></dl>
			<dl id="LMC015"><dt><a href="/user/apt_CompareL.do?L_TMC=TMC004&L_LMC=LMC015" <c:if test="${sessionScope.LMC eq 'LMC015'}">class="on"</c:if>>실거래가 비교</a></dt></dl>
			<dl id="LMC016"><dt><a href="/user/apt_OutCompareL.do?L_TMC=TMC004&L_LMC=LMC016" <c:if test="${sessionScope.LMC eq 'LMC016'}">class="on"</c:if>>분양권 비교</a></dt></dl>
		<%-- <c:if test="${not empty INIT_DATA.USER_LEFT_MENU.USERLEFTMENU }">
			<c:forEach var="item" items="${INIT_DATA.USER_LEFT_MENU.USERLEFTMENU}">
				<c:if test="${sessionScope.TMC eq item.PRTCODE }">
					<dl id="${item.MENU_CODE}"><dt><a href="javascript:fnLeftMenuMove('${item.MENU_CODE}');" <c:if test="${sessionScope.LMC eq item.MENU_CODE}">class="on"</c:if>>${item.MENUNM}</a></dt></dl>
				</c:if>
			</c:forEach>
		</c:if> --%>
	</div>
