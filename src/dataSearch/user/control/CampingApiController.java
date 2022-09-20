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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import com.ibatis.common.logging.Log;
import com.ibatis.common.logging.LogFactory;

import dataSearch.framework.common.DataMap;
import dataSearch.framework.common.control.LincActionController;
import dataSearch.framework.core.CommonFacade;
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
			dataMap.put("procedureid", "Common.getSido_List");
			List SidoList = commonFacade.list(dataMap);
			dataMap.put("SidoList", SidoList);
			
			listSearch();
			
			
		
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new ModelAndView(modelName, "INIT_DATA", dataMap);
	}
	
	
	public void listSearch() throws Exception{
		
		
		StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/B551011/GoCamping/basedList");
        urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=FmRJggnPbuErC7S3g3D1K51bawXyTDd7hh%2FJZP%2Bdkyl5OdU79rlNJ%2BNZWXUfncUYfKzWtgUj8Ks6oxWvRQdPSg%3D%3D");
        urlBuilder.append("&" + URLEncoder.encode("MobileOS", "UTF-8") + "=" + URLEncoder.encode("ETC", "UTF-8"));
		urlBuilder.append("&" + URLEncoder.encode("MobileApp", "UTF-8") + "=" + URLEncoder.encode("WEB", "UTF-8"));
		urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("10", "UTF-8"));
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
        ArrayList list_img = new ArrayList();
        DataMap tempMap = new DataMap();

        while (event_type != 1) {
          if (event_type == 2) {
            tag = xpp.getName();
          } else if (event_type == 4)
          {
        	  
            if ("facltNm".equals(tag)) {
            	tempMap.put("FACLTNM", xpp.getText()); 
            	System.out.println("FACLTNM========" + tempMap.getString("FACLTNM"));
            }
            if("\n    ".equals(tempMap.getString("FACLTNM"))){
            	System.out.println("동일========");
            }
            if("\n   ".equals(tempMap.getString("FACLTNM"))){
            	System.out.println("동일22========");
            }
            if("\n  ".equals(tempMap.getString("FACLTNM"))){
            	System.out.println("동일33========");
            }
            if(System.lineSeparator().equals(tempMap.getString("FACLTNM"))){
            	System.out.println("동일44========");
            }

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
//        for(int i =0; i < list.size(); i ++) {
//        	DataMap insertMap = new DataMap();
//        	insertMap = (DataMap) list.get(i);
//        	
//        	// 캠핑장 정보 insert
//        	insertMap.put("procedureid", "Api.CampingData_Insert");
//			commonFacade.processInsert(insertMap);
//	        
//        }
        
        
//        dataMap.put("procedureid", "Api.CampingImgData_Delete");
//		commonFacade.processInsert(dataMap);
        
        for(int i =0; i < list_img.size(); i ++) {
        	DataMap insertMap = new DataMap();
        	insertMap = (DataMap) list_img.get(i);
        	
        	// 캠핑장 이미지 정보 insert
        	insertMap.put("procedureid", "Api.CampingImgData_Insert");
			commonFacade.processInsert(insertMap);
        }
		
	} 
		

}
