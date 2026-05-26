<%@ page language="java" import="java.io.*" %><%
	String currenttime = (String)request.getAttribute("currenttime");
	String fileName="/ManifestReport2/ManifestReport2_"+currenttime+".pdf";
	
	response.setContentType("*/*");
	response.setHeader("Content-disposition","attachment;filename=ManifestBranchReport.pdf");
	BufferedInputStream is=null;
	OutputStream os=null;
	FileInputStream fis=null;
	try{
		fis = new FileInputStream(request.getRealPath("/")+"ManifestReport2/ManifestReport2_"+currenttime+".pdf");
		is=new BufferedInputStream(fis);
		os = response.getOutputStream();
		
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
		fis.close();
		fis = null;
		os.flush();
		os.close();
		os = null;
		is.close();
		is = null;
		File f = new File(request.getRealPath("/")+"ManifestReport2/ManifestReport2_"+currenttime+".pdf");
		if(f.exists()){
			f.delete();
		}
	}catch(Exception e){
		System.out.println("VIEWMANIFESTREPORT2 CATCH ");
	}finally{
		try {
			if(fis != null) {
				fis.close();
			}
		} catch (Exception e) {
			System.out.println("VIEWMANIFESTREPORT2 CATCH 2");
		}
		try {
			if(is != null) {
				is.close();
			}
		} catch (Exception e) {
			System.out.println("VIEWMANIFESTREPORT2 CATCH 3");
		}
		try {
			if(os != null) {
				os.flush();
				os.close();
			}
		} catch (Exception e) {
			System.out.println("VIEWMANIFESTREPORT2 CATCH 4");
		}
	}
%>