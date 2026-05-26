<%@ page language="java" trimDirectiveWhitespaces="true" import="java.io.File, java.io.BufferedInputStream, java.io.OutputStream, java.io.FileInputStream,bean.Global" %><%   
   //String path = new File(request.getRealPath(request.getServletPath())).getParent();
   String path =  request.getSession().getServletContext().getRealPath("/");
   //System.out.println("request.getSession().getServletContext().getRealPath(\"/\") "+request.getSession().getServletContext().getRealPath("/"));
   
   Global global = (Global)session.getAttribute("sGlobal");
   String printfilename = global.printfilename;
   
   String filename = path+File.separator+"Guiaprint"+File.separator+printfilename;   
   //System.out.println("GUIA PRINT FILE PATH IN JSP "+filename);   
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
	}catch(Exception e){
		System.out.println("INSIDE CATCH ");
	}finally{
		try {
			if (os!= null)
				os.close();	
		} catch(Exception e) {
			
		}
		try {
			if (fis!= null)
				fis.close();	
		} catch(Exception e) {
			
		}
		try {
			if (is!= null)
				is.close();	
		} catch(Exception e) {
			
		}		
		/*File f = new File(filename);
		if(f.exists()){
			f.delete();
		}*/
	}
   return;
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