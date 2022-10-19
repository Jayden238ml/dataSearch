package dataSearch.user.control;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import com.ibatis.common.logging.Log;
import com.ibatis.common.logging.LogFactory;

import dataSearch.framework.common.DataMap;
import dataSearch.framework.common.control.LincActionController;
import dataSearch.framework.core.CommonFacade;
import dataSearch.framework.util.MessageUtil;
import dataSearch.framework.util.PUtil;
import dataSearch.framework.util.Utils;

@Controller
public class DataApiController extends LincActionController{
	protected CommonFacade commonFacade;
	private PlatformTransactionManager transactionManager;
	Log log = LogFactory.getLog(getClass());
	
	//protected DataMap paramMap = null;
	
	@Autowired
	public void setTransactionManager(PlatformTransactionManager transactionManager)
	{
	  this.transactionManager = transactionManager;
	}
	@Autowired
	@Qualifier("commonImpl")
	public void setCommonImpl(CommonFacade commonFacade) { this.commonFacade = commonFacade; }
	
	@ModelAttribute("requestParam")
	public DataMap requestParam(HttpServletRequest arg0, HttpServletResponse arg1)
	  throws Exception{
		  showParameters(arg0);
		  DataMap paramMap = new PUtil().getParameterDataMap(arg0);
		  setSessionMenu(this.commonFacade, arg0, paramMap);
		  if("N".equals(paramMap.getString("RETOK"))){
			  arg1.sendRedirect("/main.do");
		  }
	
		  return paramMap;
	}
	
	public void showParameters(HttpServletRequest request)
	{
		this.log.debug("###############################################################");
		this.log.debug("REQUEST  URL : " + request.getRequestURL());
		Enumeration paramNames = request.getParameterNames();
	  try{
		  while (paramNames.hasMoreElements()) {
	      String name = ((String)paramNames.nextElement()).toString();
	      String value = StringUtils.defaultIfEmpty(request.getParameter(name), "");
	
	      this.log.debug("PARAM : " + name.toUpperCase() + "\t VALUE : " + value);
	    }
	
	    this.log.debug("###############################################################");
	  } catch (Exception e) {
	    e.printStackTrace();
	  }
	}
	
	
	@RequestMapping({"/user/apt_ConceL.do"})
	public ModelAndView apt_warrant(@ModelAttribute("requestParam") DataMap dataMap, HttpServletRequest request, HttpServletResponse response
		,@RequestParam(value="PAGE_SIZE", required=false, defaultValue="10")String view_size
		,@RequestParam(value="CURR_PAGE", required=false, defaultValue="1")String page){
		String modelName = "/apt_price/apt_ConceL";
		try {
			
			dataMap.put("CURR_PAGE",page);
			dataMap.put("PAGE_SIZE",view_size);
			
			// 시도 리스트
			dataMap.put("procedureid", "Common.getSido_List");
			List SidoList = commonFacade.list(dataMap);
			dataMap.put("SidoList", SidoList);
			
			if("".equals(dataMap.getString("SCH_TOP_LAWD_CD")) && "".equals(dataMap.getString("SCH_LAWD_CD"))
				&& "".equals(dataMap.getString("SCH_APARTMENT_NAME")) && "".equals(dataMap.getString("SCH_AREA_EXCLUSIVE_USE"))
					) {
				dataMap.put("DEFAULT", "Y");
			}
			
			if("Y".equals(dataMap.getString("DEFAULT"))) {
				dataMap.put("TOTAL_CNT", "200");
			}else {
				dataMap.put("procedureid", "Api.getParcelOutInfo_CNT");
			    DataMap cntMap = this.commonFacade.getObject(dataMap);
			    dataMap.put("TOTAL_CNT", cntMap.getString("TOTAL_CNT"));
			}
			
			dataMap.put("procedureid", "Api.getParcelOutInfo_List");
			List resultList = commonFacade.list(dataMap);
			dataMap.put("resultList", resultList);
			
		
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new ModelAndView(modelName, "INIT_DATA", dataMap);
	}
	
	
	
	@RequestMapping({"/user/apt_TradingL.do"})
	public ModelAndView apt_TradingL(@ModelAttribute("requestParam") DataMap dataMap, HttpServletRequest request, HttpServletResponse response
			,@RequestParam(value="PAGE_SIZE", required=false, defaultValue="10")String view_size
			,@RequestParam(value="CURR_PAGE", required=false, defaultValue="1")String page){
		String modelName = "/apt_price/apt_TradingL";
		try {
			
			dataMap.put("CURR_PAGE",page);
			dataMap.put("PAGE_SIZE",view_size);
			
			// 시도 리스트
			dataMap.put("procedureid", "Common.getSido_List");
			List SidoList = commonFacade.list(dataMap);
			dataMap.put("SidoList", SidoList);
			
			
			if("".equals(dataMap.getString("SCH_TOP_LAWD_CD")) && "".equals(dataMap.getString("SCH_LAWD_CD"))
				&& "".equals(dataMap.getString("SCH_APARTMENT_NAME")) && "".equals(dataMap.getString("SCH_AREA_EXCLUSIVE_USE"))
					) {
				dataMap.put("DEFAULT", "Y");
			}
			
			if("Y".equals(dataMap.getString("DEFAULT"))) {
				dataMap.put("TOTAL_CNT", "200");
			}else {
				dataMap.put("procedureid", "Api.getDealAptInfo_CNT");
			    DataMap cntMap = this.commonFacade.getObject(dataMap);
			    dataMap.put("TOTAL_CNT", cntMap.getString("TOTAL_CNT"));
			}
			
			if("Y".equals(dataMap.getString("DEFAULT"))) {
				dataMap.put("procedureid", "Api.getDealAptInfo_List");
				List resultList = commonFacade.list(dataMap);
				dataMap.put("resultList", resultList);
			}else{
				dataMap.put("procedureid", "Api.getDealAptInfoSearch_List");
				List resultList = commonFacade.list(dataMap);
				dataMap.put("resultList", resultList);
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new ModelAndView(modelName, "INIT_DATA", dataMap);
	}
	
	
	/* 아파트 실거래가 비교 */
	@RequestMapping({"/user/apt_CompareL.do"})
	public ModelAndView apt_CompareL(@ModelAttribute("requestParam") DataMap dataMap, HttpServletRequest request, HttpServletResponse response
			,@RequestParam(value="PAGE_SIZE", required=false, defaultValue="10")String view_size
			,@RequestParam(value="CURR_PAGE", required=false, defaultValue="1")String page){
		String modelName = "/apt_price/apt_CompareList";
		try {
			
			dataMap.put("CURR_PAGE",page);
			dataMap.put("PAGE_SIZE",view_size);
			
			// 시도 리스트
			dataMap.put("procedureid", "Common.getSido_List");
			List SidoList = commonFacade.list(dataMap);
			dataMap.put("SidoList", SidoList);
			
			if(!"".equals(dataMap.getString("MY_ROAD_NAME_BONBUN")) && !"".equals(dataMap.getString("YOU_ROAD_NAME_BONBUN"))) {
				dataMap.put("procedureid", "Api.getAptCompareDeal_ChartList");
				List chartList = commonFacade.list(dataMap);
				dataMap.put("chartList", chartList);
			}
			
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new ModelAndView(modelName, "INIT_DATA", dataMap);
	}
	
	/* 아파트 분양권 비교 */
	@RequestMapping({"/user/apt_OutCompareL.do"})
	public ModelAndView apt_OutCompareL(@ModelAttribute("requestParam") DataMap dataMap, HttpServletRequest request, HttpServletResponse response
			,@RequestParam(value="PAGE_SIZE", required=false, defaultValue="10")String view_size
			,@RequestParam(value="CURR_PAGE", required=false, defaultValue="1")String page){
		String modelName = "/apt_price/apt_OutCompareList";
		try {
			
			dataMap.put("CURR_PAGE",page);
			dataMap.put("PAGE_SIZE",view_size);
			
			// 시도 리스트
			dataMap.put("procedureid", "Common.getSido_List");
			List SidoList = commonFacade.list(dataMap);
			dataMap.put("SidoList", SidoList);
			
			if(!"".equals(dataMap.getString("MY_JIBUN")) && !"".equals(dataMap.getString("YOU_JIBUN"))) {
				dataMap.put("procedureid", "Api.getAptCompareOut_ChartList");
				List chartList = commonFacade.list(dataMap);
				dataMap.put("chartList", chartList);
			}
			
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new ModelAndView(modelName, "INIT_DATA", dataMap);
	}
	
	
	@RequestMapping({"/api/getSidoList.do"}) 
	public ModelAndView getSidoList(@ModelAttribute("requestParam") DataMap dataMap, HttpServletRequest request, HttpServletResponse response){
		try {
			
			dataMap.put("TMP_AREA_SIDO", dataMap.getString("SCH_TOP_LAWD_CD"));
			dataMap.put("procedureid", "Common.getAreaSiGunGu_List");
			List SidoList = commonFacade.list(dataMap);
			dataMap.put("SidoList", SidoList);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		FlashMap fm = RequestContextUtils.getOutputFlashMap(request);
		fm.put("TMC", dataMap.getString("TMC"));
		fm.put("LMC", dataMap.getString("LMC"));
		return new ModelAndView("jsonView", dataMap);
	}
	
	@RequestMapping({"/api/getCampingSidoList.do"}) 
	public ModelAndView getCampingSidoList(@ModelAttribute("requestParam") DataMap dataMap, HttpServletRequest request, HttpServletResponse response){
		try {
			
			String SCH_TOP_LAWD_CD = dataMap.getString("SCH_TOP_LAWD_CD");
			String TMP_AREA_SIDO = "11";
			if("강원도".equals(SCH_TOP_LAWD_CD)){
				TMP_AREA_SIDO = "42";
			}else if("경기도".equals(SCH_TOP_LAWD_CD)){
				TMP_AREA_SIDO = "41";
			}else if("경상남도".equals(SCH_TOP_LAWD_CD)){
				TMP_AREA_SIDO = "48";
			}else if("경상북도".equals(SCH_TOP_LAWD_CD)){
				TMP_AREA_SIDO = "47";
			}else if("광주시".equals(SCH_TOP_LAWD_CD)){
				TMP_AREA_SIDO = "29";
			}else if("대구시".equals(SCH_TOP_LAWD_CD)){
				TMP_AREA_SIDO = "27";
			}else if("대전시".equals(SCH_TOP_LAWD_CD)){
				TMP_AREA_SIDO = "30";
			}else if("부산시".equals(SCH_TOP_LAWD_CD)){
				TMP_AREA_SIDO = "26";
			}else if("세종시".equals(SCH_TOP_LAWD_CD)){
				TMP_AREA_SIDO = "36";
			}else if("울산시".equals(SCH_TOP_LAWD_CD)){
				TMP_AREA_SIDO = "31";
			}else if("인천시".equals(SCH_TOP_LAWD_CD)){
				TMP_AREA_SIDO = "28";
			}else if("전라남도".equals(SCH_TOP_LAWD_CD)){
				TMP_AREA_SIDO = "46";
			}else if("전라북도".equals(SCH_TOP_LAWD_CD)){
				TMP_AREA_SIDO = "45";
			}else if("제주도".equals(SCH_TOP_LAWD_CD)){
				TMP_AREA_SIDO = "50";
			}else if("충청남도".equals(SCH_TOP_LAWD_CD)){
				TMP_AREA_SIDO = "44";
			}else if("충청북도".equals(SCH_TOP_LAWD_CD)){
				TMP_AREA_SIDO = "43";
			}
			
			dataMap.put("TMP_AREA_SIDO", TMP_AREA_SIDO);
			dataMap.put("procedureid", "Common.getAreaSiGunGu_List");
			List SidoList = commonFacade.list(dataMap);
			dataMap.put("SidoList", SidoList);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		FlashMap fm = RequestContextUtils.getOutputFlashMap(request);
		fm.put("TMC", dataMap.getString("TMC"));
		fm.put("LMC", dataMap.getString("LMC"));
		return new ModelAndView("jsonView", dataMap);
	}
	
	
	/**
	 * 실거래가 상세
	 * 
	 * @param dataMap
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/api/aptDealDetail.do")
	public ModelAndView aptDealDetail(@ModelAttribute("requestParam") DataMap dataMap, HttpServletRequest request,HttpServletResponse response){
		
		String modelName = "/apt_price/apt_DealDetail";
		
		try {
			
			String apartment_name = request.getParameter("APARTMENT_NAME");
			apartment_name = URLDecoder.decode(apartment_name, "UTF-8");
			dataMap.put("APARTMENT_NAME", apartment_name);
			
			dataMap.put("procedureid", "Api.getAptDeal_List");
			List resultList = commonFacade.list(dataMap);
			dataMap.put("resultList", resultList);
			
			dataMap.put("procedureid", "Api.getAptDealMinMax_List");
			List MinMaxList = commonFacade.list(dataMap);
			dataMap.put("MinMaxList", MinMaxList);
			
			if(resultList.size() > 0) {
				dataMap.put("X_LOCATION", ((DataMap)resultList.get(0)).getString("X_LOCATION"));
				dataMap.put("Y_LOCATION", ((DataMap)resultList.get(0)).getString("Y_LOCATION"));
				
				if("".equals(dataMap.getString("X_LOCATION")) && "".equals(dataMap.getString("Y_LOCATION"))) {
					String address = ((DataMap)resultList.get(0)).getString("ADDRESS");
					DataMap xyMap = new DataMap();
					PUtil pu = new PUtil();
					if(!"".equals(address)){
						  xyMap = pu.addrToLocation(URLEncoder.encode(address,"UTF-8"));
					  }
					  if(!"".equals(xyMap)){
						  dataMap.put("X_LOCATION", xyMap.getString("X_LOCATION"));
						  dataMap.put("Y_LOCATION", xyMap.getString("Y_LOCATION"));
						  
						  dataMap.put("procedureid", "Api.XY_location_Update");
						  commonFacade.processUpdate(dataMap);
					  }
				}
			}
			
			dataMap.put("procedureid", "Api.getAptDeal_ChartList");
			List chartList = commonFacade.list(dataMap);
			dataMap.put("chartList", chartList);
			
			dataMap.put("procedureid", "Api.getAptDealArea_List");
			List areaList = commonFacade.list(dataMap);
			dataMap.put("areaList", areaList);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new ModelAndView(modelName, "INIT_DATA", dataMap);
	}
	
	
	
	/**
	 * 실거래가 상세
	 * 
	 * @param dataMap
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/api/aptParcOutDetail.do")
	public ModelAndView aptParcOutDetail(@ModelAttribute("requestParam") DataMap dataMap, HttpServletRequest request,HttpServletResponse response){
		
		String modelName = "/apt_price/apt_ConCeDetail";
		
		try {
			
			String apartment_name = request.getParameter("APARTMENT_NAME");
			apartment_name = URLDecoder.decode(apartment_name, "UTF-8");
			dataMap.put("APARTMENT_NAME", apartment_name);
			
			dataMap.put("procedureid", "Api.getAptParcelOut_List");
			List resultList = commonFacade.list(dataMap);
			dataMap.put("resultList", resultList);
			
			dataMap.put("procedureid", "Api.getAptParcelOutMinMax_List");
			List MinMaxList = commonFacade.list(dataMap);
			dataMap.put("MinMaxList", MinMaxList);
			
			if(resultList.size() > 0) {
				dataMap.put("X_LOCATION", ((DataMap)resultList.get(0)).getString("X_LOCATION"));
				dataMap.put("Y_LOCATION", ((DataMap)resultList.get(0)).getString("Y_LOCATION"));
				
				if("".equals(dataMap.getString("X_LOCATION")) && "".equals(dataMap.getString("Y_LOCATION"))) {
					String address1 = ((DataMap)resultList.get(0)).getString("SIGUNGU");
					String address2 = ((DataMap)resultList.get(0)).getString("DONG");
					String jibun = ((DataMap)resultList.get(0)).getString("JIBUN");
					
					String address = address1 + " " + address2 + " " + jibun;
					DataMap xyMap = new DataMap();
					PUtil pu = new PUtil();
					if(!"".equals(address)){
						xyMap = pu.addrToLocation(URLEncoder.encode(address,"UTF-8"));
						dataMap.put("ADDRESS", address);
					}
					if(!"".equals(xyMap)){
						dataMap.put("X_LOCATION", xyMap.getString("X_LOCATION"));
						dataMap.put("Y_LOCATION", xyMap.getString("Y_LOCATION"));
						
						dataMap.put("procedureid", "Api.XY_locationPo_Update");
						commonFacade.processUpdate(dataMap);
					}
				}
			}
			
			dataMap.put("procedureid", "Api.getAptParcelOut_ChartList");
			List chartList = commonFacade.list(dataMap);
			dataMap.put("chartList", chartList);
			
			dataMap.put("procedureid", "Api.getAptParcelOutArea_List");
			List areaList = commonFacade.list(dataMap);
			dataMap.put("areaList", areaList);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new ModelAndView(modelName, "INIT_DATA", dataMap);
	}
	
	
	
	@RequestMapping(value = "/api/serachComPareAptDeal_List.do")
	public ModelAndView serachComPareAptDeal_List(@ModelAttribute("requestParam") DataMap dataMap, HttpServletRequest request, HttpServletResponse response){
		try {
			
			if("M".equals(dataMap.getString("SEARCH_TYPE"))) {
				dataMap.put("A_NM", dataMap.getString("MY_APART_NM"));
				dataMap.put("SIDO_CD", dataMap.getString("MY_SIDO_CD"));
			}else {
				dataMap.put("A_NM", dataMap.getString("YOU_APART_NM"));
				dataMap.put("SIDO_CD", dataMap.getString("YOU_SIDO_CD"));
			}
			
			dataMap.put("procedureid", "Api.serachComPareAptDeal_List");
			dataMap.put("resultList", commonFacade.list(dataMap));
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return new ModelAndView("jsonView", dataMap);
	}
	
	
	@RequestMapping(value = "/api/serachComPareAptOut_List.do")
	public ModelAndView serachComPareAptOut_List(@ModelAttribute("requestParam") DataMap dataMap, HttpServletRequest request, HttpServletResponse response){
		try {
			
			if("M".equals(dataMap.getString("SEARCH_TYPE"))) {
				dataMap.put("A_NM", dataMap.getString("MY_APART_NM"));
				dataMap.put("SIDO_CD", dataMap.getString("MY_SIDO_CD"));
			}else {
				dataMap.put("A_NM", dataMap.getString("YOU_APART_NM"));
				dataMap.put("SIDO_CD", dataMap.getString("YOU_SIDO_CD"));
			}
			
			dataMap.put("procedureid", "Api.serachComPareAptOut_List");
			dataMap.put("resultList", commonFacade.list(dataMap));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return new ModelAndView("jsonView", dataMap);
	}
	

}
