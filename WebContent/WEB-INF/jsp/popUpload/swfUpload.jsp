<%@ page contentType = "text/html;charset=euc-kr" %>
<%@ page import = "java.io.File,
				   com.oreilly.servlet.MultipartRequest,
		 		   com.oreilly.servlet.multipart.DefaultFileRenamePolicy"	
%>
<%	
	
	// IE�̿��� ���������� SESSION���� ������ �ʿ�
	if (request.getParameter("JSESSIONID")!=null) 
	{ 
		Cookie userCookie = new Cookie("JSESSIONID", request.getParameter("JSESSIONID")); 
		response.addCookie(userCookie); 
	}
	

	MultipartRequest multi = new MultipartRequest( request, "D:/webPro/yestrade/WebContent/static_root/userUpload", 1000 * 1024 * 1024, "UTF-8", new DefaultFileRenamePolicy() );


	// �Ķ���� �׽�Ʈ 
	String p1 = multi.getParameter("param1");
	String p2 = multi.getParameter("param2");
	
	System.out.println("p1 ======================> " + p1);
	System.out.println("p2 ======================> " + p2);
		
	//	==================== SESSION GET TEST =========================
	System.out.println("���� : �Ҽ� = " + session.getAttribute("SOSOK") );
	System.out.println("���� : �̸� = " + session.getAttribute("NAME") );
	System.out.println("���� : ���� = " + session.getAttribute("AGE") );
	

	
	//Cookie[] cc = request.getCookies();	
	//for(int i=0; i<cc.length; i++) System.out.println(cc[i].getName() + "=" + cc[i].getValue());
%>