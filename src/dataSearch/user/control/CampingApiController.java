package dataSearch.user.control;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import com.ibatis.common.logging.Log;
import com.ibatis.common.logging.LogFactory;

import dataSearch.framework.common.DataMap;
import dataSearch.framework.common.LoginSession;
import dataSearch.framework.common.control.LincActionController;
import dataSearch.framework.core.CommonFacade;
import dataSearch.framework.util.BrowserUtil;
import dataSearch.framework.util.PUtil;

@Controller
public class CampingApiController extends LincActionController{
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
	
	
	@RequestMapping({"/user/CampingList.do"})
	public ModelAndView CampingList(@ModelAttribute("requestParam") DataMap dataMap, HttpServletRequest request, HttpServletResponse response
		,@RequestParam(value="PAGE_SIZE", required=false, defaultValue="10")String view_size
		,@RequestParam(value="CURR_PAGE", required=false, defaultValue="1")String page){
		String modelName = "/travel/CampingList";
		
		try {
			
			dataMap.put("CURR_PAGE",page);
			dataMap.put("PAGE_SIZE",view_size);
			
			// 시도 리스트
			dataMap.put("procedureid", "Common.getCampingSido_List");
			List SidoList = commonFacade.list(dataMap);
			dataMap.put("SidoList", SidoList);
			
			dataMap.put("procedureid", "Api.getCampingInfo_CNT");
		    DataMap cntMap = this.commonFacade.getObject(dataMap);
		    dataMap.put("TOTAL_CNT", cntMap.getString("TOTAL_CNT"));
		    
		    dataMap.put("procedureid", "Api.getCampingInfo_List");
			List resultList = commonFacade.list(dataMap);
			dataMap.put("resultList", resultList);
			
		} catch (Exception e) {
          	e.printStackTrace();
		}
			
			
		return new ModelAndView(modelName, "INIT_DATA", dataMap);
	}
	
	
	
	@RequestMapping({"/user/CampingDataInsert.do"})
	public ModelAndView CampingDataInsert(@ModelAttribute("requestParam") DataMap dataMap, HttpServletRequest request, HttpServletResponse response
		,@RequestParam(value="PAGE_SIZE", required=false, defaultValue="10")String view_size
		,@RequestParam(value="CURR_PAGE", required=false, defaultValue="1")String page){
		String modelName = "/travel/CampingList";
		
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = transactionManager.getTransaction(def);
		
		try {
			
			dataMap.put("CURR_PAGE",page);
			dataMap.put("PAGE_SIZE",view_size);
			
			// 시도 리스트
			dataMap.put("procedureid", "Common.getSido_List");
			List SidoList = commonFacade.list(dataMap);
			dataMap.put("SidoList", SidoList);
			
			// ================================
			
			dataMap.put("procedureid", "Api.CampingData_Delete");
			commonFacade.processDelete(dataMap);
			
			StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/B551011/GoCamping/basedList");
	        urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=FmRJggnPbuErC7S3g3D1K51bawXyTDd7hh%2FJZP%2Bdkyl5OdU79rlNJ%2BNZWXUfncUYfKzWtgUj8Ks6oxWvRQdPSg%3D%3D");
	        urlBuilder.append("&" + URLEncoder.encode("MobileOS", "UTF-8") + "=" + URLEncoder.encode("ETC", "UTF-8"));
			urlBuilder.append("&" + URLEncoder.encode("MobileApp", "UTF-8") + "=" + URLEncoder.encode("WEB", "UTF-8"));
			urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("3215", "UTF-8"));
			urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8"));
			URL url = new URL(urlBuilder.toString());
			
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
	        factory.setNamespaceAware(true);
	        XmlPullParser xpp = factory.newPullParser();
	        BufferedInputStream bis = new BufferedInputStream(url.openStream());
	        xpp.setInput(bis, "utf-8");

	        String tag = null;
	        int event_type = xpp.getEventType();

	        ArrayList list = new ArrayList();
	        DataMap tempMap = new DataMap();

	        while (event_type != 1) {
	          if (event_type == 2) {
	            tag = xpp.getName();
	          } else if (event_type == 4)
	          {
	        	  
	            if ("exprnProgrm".equals(tag) && !"".equals(xpp.getText().replace("\n    ", ""))) {tempMap.put("EXPRNPROGRM", xpp.getText()); }
	            if ("extshrCo".equals(tag) && !"".equals(xpp.getText().replace("\n    ", ""))) {tempMap.put("EXTSHRCO", xpp.getText()); } 
	            if ("frprvtWrppCo".equals(tag) && !"".equals(xpp.getText().replace("\n    ", ""))) {tempMap.put("FRPRVTWRPPCO", xpp.getText()); } 
	            if ("frprvtSandCo".equals(tag) && !"".equals(xpp.getText().replace("\n    ", ""))) {tempMap.put("FRPRVTSANDCO", xpp.getText()); } 
	            if ("fireSensorCo".equals(tag) && !"".equals(xpp.getText().replace("\n    ", ""))) {tempMap.put("FIRESENSORCO", xpp.getText()); } 
	            if ("themaEnvrnCl".equals(tag) && !"".equals(xpp.getText().replace("\n    ", ""))) {tempMap.put("THEMAENVRNCL", xpp.getText()); } 
	            if ("eqpmnLendCl".equals(tag) && !"".equals(xpp.getText().replace("\n    ", ""))) {tempMap.put("EQPMNLENDCL", xpp.getText()); } 
	            if ("animalCmgCl".equals(tag) && !"".equals(xpp.getText().replace("\n    ", ""))) {tempMap.put("ANIMALCMGCL", xpp.getText()); } 
	            if ("tourEraCl".equals(tag) && !"".equals(xpp.getText().replace("\n    ", ""))) {tempMap.put("TOURERACL", xpp.getText()); } 
	            if ("firstImageUrl".equals(tag) && !"".equals(xpp.getText().replace("\n    ", ""))) {tempMap.put("FIRSTIMAGEURL", xpp.getText()); } 
	            if ("createdtime".equals(tag) && !"".equals(xpp.getText().replace("\n    ", ""))) {tempMap.put("CREATEDTIME", xpp.getText()); } 
	            if ("modifiedtime".equals(tag) && !"".equals(xpp.getText().replace("\n    ", ""))) {tempMap.put("MODIFIEDTIME", xpp.getText()); } 
	            if ("contentId".equals(tag) && !"".equals(xpp.getText().replace("\n    ", ""))) {tempMap.put("CONTENTID", xpp.getText()); } 
	            if ("facltNm".equals(tag) && !"".equals(xpp.getText().replace("\n    ", ""))) {tempMap.put("FACLTNM", xpp.getText()); } 
	            if ("lineIntro".equals(tag) && !"".equals(xpp.getText().replace("\n    ", ""))) {tempMap.put("LINEINTRO", xpp.getText()); } 
	            if ("intro".equals(tag) && !"".equals(xpp.getText().replace("\n    ", ""))) {tempMap.put("INTRO", xpp.getText()); } 
	            if ("allar".equals(tag) && !"".equals(xpp.getText().replace("\n    ", ""))) {tempMap.put("ALLAR", xpp.getText()); } 
	            if ("insrncAt".equals(tag) && !"".equals(xpp.getText().replace("\n    ", ""))) {tempMap.put("INSRNCAT", xpp.getText()); } 
	            if ("trsagntNo".equals(tag) && !"".equals(xpp.getText().replace("\n    ", ""))) {tempMap.put("TRSAGNTNO", xpp.getText()); } 
	            if ("bizrno".equals(tag) && !"".equals(xpp.getText().replace("\n    ", ""))) {tempMap.put("BIZRNO", xpp.getText()); } 
	            if ("facltDivNm".equals(tag) && !"".equals(xpp.getText().replace("\n    ", ""))) {tempMap.put("FACLTDIVNM", xpp.getText()); } 
	            if ("mangeDivNm".equals(tag) && !"".equals(xpp.getText().replace("\n    ", ""))) {tempMap.put("MANGEDIVNM", xpp.getText()); } 
	            if ("mgcDiv".equals(tag) && !"".equals(xpp.getText().replace("\n    ", ""))) {tempMap.put("MGCDIV", xpp.getText()); } 
	            if ("manageSttus".equals(tag) && !"".equals(xpp.getText().replace("\n    ", ""))) {tempMap.put("MANAGESTTUS", xpp.getText()); } 
	            if ("hvofBgnde".equals(tag) && !"".equals(xpp.getText().replace("\n    ", ""))) {tempMap.put("HVOFBGNDE", xpp.getText()); } 
	            if ("hvofEnddle".equals(tag) && !"".equals(xpp.getText().replace("\n    ", ""))) {tempMap.put("HVOFENDDLE", xpp.getText()); } 
	            if ("featureNm".equals(tag) && !"".equals(xpp.getText().replace("\n    ", ""))) {tempMap.put("FEATURENM", xpp.getText()); } 
	            if ("induty".equals(tag) && !"".equals(xpp.getText().replace("\n    ", ""))) {tempMap.put("INDUTY", xpp.getText()); } 
	            if ("lctCl".equals(tag) && !"".equals(xpp.getText().replace("\n    ", ""))) {tempMap.put("LCTCL", xpp.getText()); } 
	            if ("doNm".equals(tag) && !"".equals(xpp.getText().replace("\n    ", ""))) {tempMap.put("DONM", xpp.getText()); } 
	            if ("sigunguNm".equals(tag) && !"".equals(xpp.getText().replace("\n    ", ""))) {tempMap.put("SIGUNGUNM", xpp.getText()); } 
	            if ("zipcode".equals(tag) && !"".equals(xpp.getText().replace("\n    ", ""))) {tempMap.put("ZIPCODE", xpp.getText()); } 
	            if ("addr1".equals(tag) && !"".equals(xpp.getText().replace("\n    ", ""))) {tempMap.put("ADDR1", xpp.getText()); } 
	            if ("addr2".equals(tag) && !"".equals(xpp.getText().replace("\n    ", ""))) {tempMap.put("ADDR2", xpp.getText()); } 
	            if ("mapX".equals(tag) && !"".equals(xpp.getText().replace("\n    ", ""))) {tempMap.put("MAPX", xpp.getText()); } 
	            if ("mapY".equals(tag) && !"".equals(xpp.getText().replace("\n    ", ""))) {tempMap.put("MAPY", xpp.getText()); } 
	            if ("direction".equals(tag) && !"".equals(xpp.getText().replace("\n    ", ""))) {tempMap.put("DIRECTION", xpp.getText()); } 
	            if ("tel".equals(tag) && !"".equals(xpp.getText().replace("\n    ", ""))) {tempMap.put("TEL", xpp.getText()); } 
	            if ("homepage".equals(tag) && !"".equals(xpp.getText().replace("\n    ", ""))) {tempMap.put("HOMEPAGE", xpp.getText()); } 
	            if ("resveUrl".equals(tag) && !"".equals(xpp.getText().replace("\n    ", ""))) {tempMap.put("RESVEURL", xpp.getText()); } 
	            if ("resveCl".equals(tag) && !"".equals(xpp.getText().replace("\n    ", ""))) {tempMap.put("RESVECL", xpp.getText()); } 
	            if ("manageNmpr".equals(tag) && !"".equals(xpp.getText().replace("\n    ", ""))) {tempMap.put("MANAGENMPR", xpp.getText()); } 
	            if ("gnrlSiteCo".equals(tag) && !"".equals(xpp.getText().replace("\n    ", ""))) {tempMap.put("GNRLSITECO", xpp.getText()); } 
	            if ("autoSiteCo".equals(tag) && !"".equals(xpp.getText().replace("\n    ", ""))) {tempMap.put("AUTOSITECO", xpp.getText()); } 
	            if ("glampSiteCo".equals(tag) && !"".equals(xpp.getText().replace("\n    ", ""))) {tempMap.put("GLAMPSITECO", xpp.getText()); } 
	            if ("caravSiteCo".equals(tag) && !"".equals(xpp.getText().replace("\n    ", ""))) {tempMap.put("CARAVSITECO", xpp.getText()); } 
	            if ("indvdlCaravSiteCo".equals(tag) && !"".equals(xpp.getText().replace("\n    ", ""))) {tempMap.put("INDVDLCARAVSITECO", xpp.getText()); } 
	            if ("sitedStnc".equals(tag) && !"".equals(xpp.getText().replace("\n    ", ""))) {tempMap.put("SITEDSTNC", xpp.getText()); } 
	            if ("siteMg1Width".equals(tag) && !"".equals(xpp.getText().replace("\n    ", ""))) {tempMap.put("SITEMG1WIDTH", xpp.getText()); } 
	            if ("siteMg2Width".equals(tag) && !"".equals(xpp.getText().replace("\n    ", ""))) {tempMap.put("SITEMG2WIDTH", xpp.getText()); } 
	            if ("siteMg3Width".equals(tag) && !"".equals(xpp.getText().replace("\n    ", ""))) {tempMap.put("SITEMG3WIDTH", xpp.getText()); } 
	            if ("siteMg1Vrticl".equals(tag) && !"".equals(xpp.getText().replace("\n    ", ""))) {tempMap.put("SITEMG1VRTICL", xpp.getText()); } 
	            if ("siteMg2Vrticl".equals(tag) && !"".equals(xpp.getText().replace("\n    ", ""))) {tempMap.put("SITEMG2VRTICL", xpp.getText()); } 
	            if ("siteMg3Vrticl".equals(tag) && !"".equals(xpp.getText().replace("\n    ", ""))) {tempMap.put("SITEMG3VRTICL", xpp.getText()); } 
	            if ("siteMg1Co".equals(tag) && !"".equals(xpp.getText().replace("\n    ", ""))) {tempMap.put("SITEMG1CO", xpp.getText()); } 
	            if ("siteMg2Co".equals(tag) && !"".equals(xpp.getText().replace("\n    ", ""))) {tempMap.put("SITEMG2CO", xpp.getText()); } 
	            if ("siteMg3Co".equals(tag) && !"".equals(xpp.getText().replace("\n    ", ""))) {tempMap.put("SITEMG3CO", xpp.getText()); } 
	            if ("siteBottomCl1".equals(tag) && !"".equals(xpp.getText().replace("\n    ", ""))) {tempMap.put("SITEBOTTOMCL1", xpp.getText()); } 
	            if ("siteBottomCl2".equals(tag) && !"".equals(xpp.getText().replace("\n    ", ""))) {tempMap.put("SITEBOTTOMCL2", xpp.getText()); } 
	            if ("siteBottomCl3".equals(tag) && !"".equals(xpp.getText().replace("\n    ", ""))) {tempMap.put("SITEBOTTOMCL3", xpp.getText()); } 
	            if ("siteBottomCl4".equals(tag) && !"".equals(xpp.getText().replace("\n    ", ""))) {tempMap.put("SITEBOTTOMCL4", xpp.getText()); } 
	            if ("siteBottomCl5".equals(tag) && !"".equals(xpp.getText().replace("\n    ", ""))) {tempMap.put("SITEBOTTOMCL5", xpp.getText()); } 
	            if ("tooltip".equals(tag) && !"".equals(xpp.getText().replace("\n    ", ""))) {tempMap.put("TOOLTIP", xpp.getText()); } 
	            if ("glampInnerFclty".equals(tag) && !"".equals(xpp.getText().replace("\n    ", ""))) {tempMap.put("GLAMPINNERFCLTY", xpp.getText()); } 
	            if ("caravInnerFclty".equals(tag) && !"".equals(xpp.getText().replace("\n    ", ""))) {tempMap.put("CARAVINNERFCLTY", xpp.getText()); } 
	            if ("prmisnDe".equals(tag) && !"".equals(xpp.getText().replace("\n    ", ""))) {tempMap.put("PRMISNDE", xpp.getText()); } 
	            if ("operPdCl".equals(tag) && !"".equals(xpp.getText().replace("\n    ", ""))) {tempMap.put("OPERPDCL", xpp.getText()); } 
	            if ("operDeCl".equals(tag) && !"".equals(xpp.getText().replace("\n    ", ""))) {tempMap.put("OPERDECL", xpp.getText()); } 
	            if ("trlerAcmpnyAt".equals(tag) && !"".equals(xpp.getText().replace("\n    ", ""))) {tempMap.put("TRLERACMPNYAT", xpp.getText()); } 
	            if ("caravAcmpnyAt".equals(tag) && !"".equals(xpp.getText().replace("\n    ", ""))) {tempMap.put("CARAVACMPNYAT", xpp.getText()); } 
	            if ("toiletCo".equals(tag) && !"".equals(xpp.getText().replace("\n    ", ""))) {tempMap.put("TOILETCO", xpp.getText()); } 
	            if ("swrmCo".equals(tag) && !"".equals(xpp.getText().replace("\n    ", ""))) {tempMap.put("SWRMCO", xpp.getText()); } 
	            if ("wtrplCo".equals(tag) && !"".equals(xpp.getText().replace("\n    ", ""))) {tempMap.put("WTRPLCO", xpp.getText()); } 
	            if ("brazierCl".equals(tag) && !"".equals(xpp.getText().replace("\n    ", ""))) {tempMap.put("BRAZIERCL", xpp.getText()); } 
	            if ("sbrsCl".equals(tag) && !"".equals(xpp.getText().replace("\n    ", ""))) {tempMap.put("SBRSCL", xpp.getText()); } 
	            if ("sbrsEtc".equals(tag) && !"".equals(xpp.getText().replace("\n    ", ""))) {tempMap.put("SBRSETC", xpp.getText()); } 
	            if ("posblFcltyCl".equals(tag) && !"".equals(xpp.getText().replace("\n    ", ""))) {tempMap.put("POSBLFCLTYCL", xpp.getText()); } 
	            if ("posblFcltyEtc".equals(tag) && !"".equals(xpp.getText().replace("\n    ", ""))) {tempMap.put("POSBLFCLTYETC", xpp.getText()); } 
	            if ("clturEventAt".equals(tag) && !"".equals(xpp.getText().replace("\n    ", ""))) {tempMap.put("CLTUREVENTAT", xpp.getText()); } 
	            if ("clturEvent".equals(tag) && !"".equals(xpp.getText().replace("\n    ", ""))) {tempMap.put("CLTUREVENT", xpp.getText()); } 
	            if ("exprnProgrmAt".equals(tag) && !"".equals(xpp.getText().replace("\n    ", ""))) {tempMap.put("EXPRNPROGRMAT", xpp.getText()); } 
	            
	          }
	          else if (event_type == 3) {
	            tag = xpp.getName();
	            if (tag.equals("item")) {
	            	list.add(tempMap);
	  	            tempMap = new DataMap();
	            }
	          }

	          event_type = xpp.next();
	        }
	        for(int i =0; i < list.size(); i ++) {
	        	DataMap insertMap = new DataMap();
	        	insertMap = (DataMap) list.get(i);
	        	
	        	// 캠핑장 정보 insert
	        	insertMap.put("procedureid", "Api.CampingData_Insert");
				commonFacade.processInsert(insertMap);
				
				System.out.println("행 :::" + i);
				
	        }
			
			log.debug("######################## "+ dataMap.getString("TODAY") +" 캠핑정보 배치 종료(END) #########################");
			
			transactionManager.commit(status);
		} catch (Exception e) {
			transactionManager.rollback(status);
          	e.printStackTrace();
		}finally {
			if (!status.isCompleted()) transactionManager.rollback(status);
		}
			
		// ================================
			
		return new ModelAndView(modelName, "INIT_DATA", dataMap);
	}
	
	
	@RequestMapping({"/user/CampingImgDataInsert.do"})
	public ModelAndView CampingImgDataInsert(@ModelAttribute("requestParam") DataMap dataMap, HttpServletRequest request, HttpServletResponse response
			,@RequestParam(value="PAGE_SIZE", required=false, defaultValue="10")String view_size
			,@RequestParam(value="CURR_PAGE", required=false, defaultValue="1")String page){
		String modelName = "/travel/CampingList";
		
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = transactionManager.getTransaction(def);
		
		try {
			
			dataMap.put("CURR_PAGE",page);
			dataMap.put("PAGE_SIZE",view_size);
			
			ArrayList list_img = new ArrayList();
			
			// 시도 리스트
			dataMap.put("procedureid", "Api.getCampingAll_List");
			List CampingList = commonFacade.list(dataMap);
			
			// ================================
			
			dataMap.put("procedureid", "Api.CampingImgData_Delete");
			commonFacade.processDelete(dataMap);
			
			for(int i = 0; i < CampingList.size(); i ++){
				DataMap insertMap = (DataMap) CampingList.get(i);
				
				StringBuilder urlBuilder_img = new StringBuilder("http://apis.data.go.kr/B551011/GoCamping/imageList");
				urlBuilder_img.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=FmRJggnPbuErC7S3g3D1K51bawXyTDd7hh%2FJZP%2Bdkyl5OdU79rlNJ%2BNZWXUfncUYfKzWtgUj8Ks6oxWvRQdPSg%3D%3D");
				urlBuilder_img.append("&" + URLEncoder.encode("MobileOS", "UTF-8") + "=" + URLEncoder.encode("ETC", "UTF-8"));
				urlBuilder_img.append("&" + URLEncoder.encode("MobileApp", "UTF-8") + "=" + URLEncoder.encode("WEB", "UTF-8"));
				urlBuilder_img.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("40", "UTF-8"));
				urlBuilder_img.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8"));
				urlBuilder_img.append("&" + URLEncoder.encode("contentId", "UTF-8") + "=" + URLEncoder.encode(insertMap.getString("CONTENTID").toString(), "UTF-8"));
				
				URL url_img = new URL(urlBuilder_img.toString());
				
				XmlPullParserFactory factory_img = XmlPullParserFactory.newInstance();
				factory_img.setNamespaceAware(true);
		        XmlPullParser xpp_img = factory_img.newPullParser();
		        BufferedInputStream bis_img = new BufferedInputStream(url_img.openStream());
		        xpp_img.setInput(bis_img, "utf-8");

		        String tag_img = null;
		        int event_type_img = xpp_img.getEventType();

		        DataMap tempMap_img = new DataMap();

		        while (event_type_img != 1) {
		          if (event_type_img == 2) {
		        	  tag_img = xpp_img.getName();
		          } else if (event_type_img == 4)
		          {
		            if ("contentId".equals(tag_img) && !"".equals(xpp_img.getText().replace("\n    ", ""))) {tempMap_img.put("CONTENTID", xpp_img.getText()); }
		            if ("serialnum".equals(tag_img) && !"".equals(xpp_img.getText().replace("\n    ", ""))) {tempMap_img.put("SERIALNUM", xpp_img.getText()); }
		            if ("imageUrl".equals(tag_img) && !"".equals(xpp_img.getText().replace("\n    ", ""))) {tempMap_img.put("IMAGEURL", xpp_img.getText()); }
		            if ("createdtime".equals(tag_img) && !"".equals(xpp_img.getText().replace("\n    ", ""))) {tempMap_img.put("CREATEDTIME", xpp_img.getText()); } 
		            if ("modifiedtime".equals(tag_img) && !"".equals(xpp_img.getText().replace("\n    ", ""))) {tempMap_img.put("MODIFIEDTIME", xpp_img.getText()); } 
		          }
		          else if (event_type_img == 3) {
		        	  tag_img = xpp_img.getName();
		            if (tag_img.equals("item")) {
		            	list_img.add(tempMap_img);
		            	tempMap_img = new DataMap();
		            }
		          }

		          event_type_img = xpp_img.next();
		        }
			}

			
	        for(int i =0; i < list_img.size(); i ++) {
	        	DataMap insertMap = new DataMap();
	        	insertMap = (DataMap) list_img.get(i);
	        	
	        	// 캠핑장 이미지 정보 insert
	        	insertMap.put("procedureid", "Api.CampingImgData_Insert");
				commonFacade.processInsert(insertMap);
				System.out.println("행 :::" + i);
	        }
			
			transactionManager.commit(status);
		} catch (Exception e) {
			transactionManager.rollback(status);
			e.printStackTrace();
		}finally {
			if (!status.isCompleted()) transactionManager.rollback(status);
		}
		
		// ================================
		
		return new ModelAndView(modelName, "INIT_DATA", dataMap);
	}
	
	
	@RequestMapping({"/user/getCampingDetail_Info.do"})
	public ModelAndView getCampingDetail_Info(@ModelAttribute("requestParam") DataMap dataMap, HttpServletRequest request, HttpServletResponse response) {

	    try
	    {
	    	dataMap.put("procedureid", "Api.getCamping_Dtail_info");
			DataMap dtailMap = this.commonFacade.getObject(dataMap);
			dataMap.put("dtailMap", dtailMap);
			
			ArrayList list_img = new ArrayList();
//				
			StringBuilder urlBuilder_img = new StringBuilder("http://apis.data.go.kr/B551011/GoCamping/imageList");
			urlBuilder_img.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=FmRJggnPbuErC7S3g3D1K51bawXyTDd7hh%2FJZP%2Bdkyl5OdU79rlNJ%2BNZWXUfncUYfKzWtgUj8Ks6oxWvRQdPSg%3D%3D");
			urlBuilder_img.append("&" + URLEncoder.encode("MobileOS", "UTF-8") + "=" + URLEncoder.encode("ETC", "UTF-8"));
			urlBuilder_img.append("&" + URLEncoder.encode("MobileApp", "UTF-8") + "=" + URLEncoder.encode("WEB", "UTF-8"));
			urlBuilder_img.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("40", "UTF-8"));
			urlBuilder_img.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8"));
			urlBuilder_img.append("&" + URLEncoder.encode("contentId", "UTF-8") + "=" + URLEncoder.encode(dataMap.getString("CONTENTID").toString(), "UTF-8"));
//			
			URL url_img = new URL(urlBuilder_img.toString());
//			
			XmlPullParserFactory factory_img = XmlPullParserFactory.newInstance();
			factory_img.setNamespaceAware(true);
	        XmlPullParser xpp_img = factory_img.newPullParser();
	        BufferedInputStream bis_img = new BufferedInputStream(url_img.openStream());
	        xpp_img.setInput(bis_img, "utf-8");
//
	        String tag_img = null;
	        int event_type_img = xpp_img.getEventType();
//
	        DataMap tempMap_img = new DataMap();

	        while (event_type_img != 1) {
	          if (event_type_img == 2) {
	        	  tag_img = xpp_img.getName();
	          } else if (event_type_img == 4)
	          {
	            if ("contentId".equals(tag_img) && !"".equals(xpp_img.getText().replace("\n    ", ""))) {tempMap_img.put("CONTENTID", xpp_img.getText()); }
	            if ("serialnum".equals(tag_img) && !"".equals(xpp_img.getText().replace("\n    ", ""))) {tempMap_img.put("SERIALNUM", xpp_img.getText()); }
	            if ("imageUrl".equals(tag_img) && !"".equals(xpp_img.getText().replace("\n    ", ""))) {tempMap_img.put("IMAGEURL", xpp_img.getText()); }
	            if ("createdtime".equals(tag_img) && !"".equals(xpp_img.getText().replace("\n    ", ""))) {tempMap_img.put("CREATEDTIME", xpp_img.getText()); } 
	            if ("modifiedtime".equals(tag_img) && !"".equals(xpp_img.getText().replace("\n    ", ""))) {tempMap_img.put("MODIFIEDTIME", xpp_img.getText()); } 
	          }
	          else if (event_type_img == 3) {
	        	  tag_img = xpp_img.getName();
	            if (tag_img.equals("item")) {
	            	list_img.add(tempMap_img);
	            	tempMap_img = new DataMap();
	            }
	          }
//
	          event_type_img = xpp_img.next();
	        }
	        
	        dataMap.put("list_img", list_img);
			
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	    FlashMap fm = RequestContextUtils.getOutputFlashMap(request);
		fm.put("TMC", dataMap.getString("TMC"));
		fm.put("LMC", dataMap.getString("LMC"));
	    
	    return new ModelAndView("jsonView", dataMap);
	  }
	

}
