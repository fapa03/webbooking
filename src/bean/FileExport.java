package bean;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.LineNumberReader;
import java.io.StringReader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import logger.AccessLog;

public class FileExport{
	String exportdirectory=null;
	
	public FileExport(String path) {
		try {
			File f=new File(path+File.separator+"InformeServ");
			if(!f.exists()) 
				f.mkdirs();
			exportdirectory=f.toString();
			//System.out.println("Export path in file export :"+exportdirectory);//commented and below added by B.Emerson on 30/04/2004
			AccessLog.Log("Export path in file export :"+exportdirectory);
		} catch (Exception e) {
			AccessLog.Log("Error: "+e);
		}
	}
	
	public void writeFile(HttpServletRequest request,String string,long sysdate) {
		BufferedWriter out = null;
		LineNumberReader ln = null;
		FileWriter  fileOutputStream = null;
		try {
			HttpSession session = request.getSession(false);
			Global global = (Global)session.getAttribute("sGlobal");
			ln = new LineNumberReader(new StringReader(string));
			// below code added by E.Sundar on 21/10/2003 to avoid data mixing hassle
			String clientId=global.clientId;
			//below code commented by E.Sundar on 21/10/2003
			//String filename = "BRPETIQ_"+sysdate+".ETI";
			String filename = "InformeServ"+sysdate+clientId+".txt";
			//System.out.println("GUIA PRINT FILE NAME IN ACTION "+filename);//commented and below added by B.Emerson on 30/04/2004
			AccessLog.Log("GUIA PRINT FILE NAME IN ACTION "+filename);
			fileOutputStream = new FileWriter(exportdirectory+File.separator+filename);
			out = new BufferedWriter(fileOutputStream);
			String data=null;
			while((data=ln.readLine())!=null){
				out.write(data);
				out.newLine();
			}
			ln.close();
			ln = null;
			out.flush();
			out.close();
			out = null;
			global.printfilename=filename;
			session.setAttribute("sGlobal",global);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (ln != null)
					ln.close();
			} catch (Exception ex) {
				AccessLog.Log(ex);
			}
			try {
				if (out != null) {
					out.flush();
					out.close();
				}
			} catch (Exception ex) {
				AccessLog.Log(ex);
			}
		}
	}
}