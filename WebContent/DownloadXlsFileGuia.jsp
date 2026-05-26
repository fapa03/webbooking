<%@page import="bean.SheetRecovery"%>
<%@ page language="java" import="java.io.*,java.util.*,bean.Global,beanUtil.ConnectDB" %>
<%
	//String path = new File(request.getRealPath(request.getServletPath())).getParent(); 
   
   Global global = (Global)session.getAttribute("sGlobal");
   SheetRecovery sr = new SheetRecovery();
   String pathFileXl = sr.getXlsMainServerPath(null);
   String path = pathFileXl + File.separator + global.getClientId() +File.separator + global.getOrigenUserClave();
   
   String xlsFileName = request.getParameter("fileName");
   
   String filename = path+File.separator + xlsFileName;
   
   //System.out.println("GUIA PRINT FILE PATH IN JSP "+filename);  
   
   response.setContentType("*/*");
   response.setHeader("Content-disposition","attachment;filename="+xlsFileName);
   
   BufferedInputStream is=null;
   OutputStream os=null;
   FileInputStream fis=null;
   try{
	fis = new FileInputStream(filename);
	is  =	new BufferedInputStream(fis);
	os  =	response.getOutputStream();
   
	int z=0;
   
	while(is.available() >0){
			byte b[]=new byte[1024];
			if(is.available()>1024)
				b=new byte[1024];
			else
				b= new byte[is.available()];
			int y=is.read(b);
			os.write(b);
			
				z=z+y;			
			os.flush();
		}
	fis.close();
	fis = null;
	os.flush();
	os.close();
	os = null;
	is.close();
	is = null;
	}catch(Exception e){
		System.out.println("INSIDE CATCH ");
	}finally{
		try {
			if(fis != null) {
				fis.close();
			}
		} catch (Exception e) {
			System.out.println("DONWLOADXLSFILEGUIA CATCH 2");
		}
		try {
			if(is != null) {
				is.close();
			}
		} catch (Exception e) {
			System.out.println("DONWLOADXLSFILEGUIA CATCH 3");
		}
		try {
			if(os != null) {
				os.flush();
				os.close();
			}
		} catch (Exception e) {
			System.out.println("DONWLOADXLSFILEGUIA CATCH 4");
		}
	}
%>