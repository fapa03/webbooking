<%@ page language="java" import="java.io.*,java.util.*,bean.Global" %><%
   
   String path = new File(request.getRealPath(request.getServletPath())).getParent();
   Global global = (Global)session.getAttribute("sGlobal");
   String printfilename = global.printfilename;
   
   String filename = path+File.separator+"Guiaprint"+File.separator+printfilename;
   
   //AAP//System.out.println("GUIA PRINT FILE PATH IN JSP "+filename);
   
   response.setContentType("*/*");
   response.setHeader("Content-disposition","attachment;filename="+"BRPETIQ.ETI");
   
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
		System.out.println("DONWLOADFILEGUIAIMPORT CATCH ");
	}finally{
		try {
			if(fis != null) {
				fis.close();
			}
		} catch (Exception e) {
			System.out.println("DONWLOADFILEGUIAIMPORT CATCH 2");
		}
		try {
			if(is != null) {
				is.close();
			}
		} catch (Exception e) {
			System.out.println("DONWLOADFILEGUIAIMPORT CATCH 3");
		}
		try {
			if(os != null) {
				os.flush();
				os.close();
			}
		} catch (Exception e) {
			System.out.println("DONWLOADFILEGUIAIMPORT CATCH 4");
		}
	}
   /*BufferedReader reader=new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
   String data=null;
   PrintWriter pw=response.getWriter();
   while((data=reader.readLine())!=null){
      pw.println(data);
      System.out.println("DATA "+data);
   }
   pw.flush();
   pw.close();*/
%>