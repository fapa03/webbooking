<%@ page language="java" %>
<%@ page import="java.io.*,java.util.*,bean.Global" %>

<%
   String path = new File(request.getRealPath(request.getServletPath())).getParent();
   Global global = (Global)session.getAttribute("sGlobal");
   String filename = path+File.separator+"Guiaprint"+File.separator+"BRPETIQ"+global.clientId+".ETI";
   
   response.setContentType("*/*");
   response.setHeader("Content-disposition","attachment;filename="+"BRPETIQ.ETI");
   BufferedInputStream is = null;
   OutputStream os = null;
   FileInputStream fis=null;
   try{
	   fis = new FileInputStream(filename);
	   is=new BufferedInputStream(fis);
	   os = response.getOutputStream();
	   
	   //AAP//System.out.println("input stream "+is.available());
	   
	   while(is.available() >0){
			byte b[]=new byte[1024];
			if(is.available()>1024)
				b=new byte[1024];
			else
				b= new byte[is.available()];
			int y=is.read(b);
			os.write(b);
			os.flush();
		}
		//System.out.println("output stream "+z);
		fis.close();
		fis = null;
		is.close();
		is = null;
		os.flush();
		os.close();
		os = null;
	}catch(Exception e){
		System.out.println("INSIDE CATCH ");
	}finally{
		try {
			if(fis != null) {
				fis.close();
			}
		} catch (Exception e) {
			System.out.println("INSIDE CATCH 2");
		}
		try {
			if(is != null) {
				is.close();
			}
		} catch (Exception e) {
			System.out.println("INSIDE CATCH 3");
		}
		try {
			if(os != null) {
				os.flush();
				os.close();
			}
		} catch (Exception e) {
			System.out.println("INSIDE CATCH 4");
		}
	}
%>