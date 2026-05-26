package bean;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.StringReader;
import java.sql.Clob;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import logger.AccessLog;

public class PrintFileExport{
	String exportdirectory=null;
	
	public PrintFileExport(String path) throws Exception{
		File f=new File(path+File.separator+"Guiaprint");
		if(!f.exists()) 
			f.mkdirs();
		exportdirectory=f.toString();
		//System.out.println("Export path in file export :"+exportdirectory);//commented and below added by B.Emerson on 30/04/2004
		//AccessLog.Log("Export path in file export :"+exportdirectory);
	}
	
	public void writeToGuiaFile(HttpServletRequest request, Clob myClob,long sysdate) {
		BufferedReader reader = null;
		InputStreamReader streamReader = null;
		BufferedWriter out = null;
                FileWriter fileOutputStream = null;
		try {
			HttpSession session = request.getSession(false);
			Global global = (Global)session.getAttribute("sGlobal");
			streamReader = new InputStreamReader(myClob.getAsciiStream());
			reader = new BufferedReader(streamReader);			
			
			// below code added by E.Sundar on 21/10/2003 to avoid data mixing hassle
			String clientId=global.clientId;
			//below code commented by E.Sundar on 21/10/2003
			//String filename = "BRPETIQ_"+sysdate+".ETI";
			String filename = "BRPETIQ_"+sysdate+clientId+".ETI";
			//System.out.println("GUIA PRINT FILE NAME IN ACTION "+filename);//commented and below added by B.Emerson on 30/04/2004
			//AccessLog.Log("GUIA PRINT FILE NAME IN ACTION "+filename);
			//BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(exportdirectory+File.separator+"BRPETIQ"+global.clientId+".ETI")));
			fileOutputStream = new FileWriter(exportdirectory+File.separator+filename);
                        out = new BufferedWriter(fileOutputStream);
			String data=null;
			while((data=reader.readLine())!=null){
				out.write(data);
				out.newLine();
			}
			streamReader.close();
			streamReader = null;
			reader.close();
			reader = null;
			out.flush();
			out.close();
			out = null;
                        fileOutputStream = null;
			global.printfilename=filename;
			session.setAttribute("sGlobal",global);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (streamReader != null) {
					streamReader.close();
				}
			} catch (Exception e) {
				AccessLog.Log("writeToGuiaFile()Exception_Error:"+e);
				e.printStackTrace();
			}
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (Exception e) {
				AccessLog.Log("writeToGuiaFile()Exception_Error:"+e);
				e.printStackTrace();
			}
			try {
				if (out != null) {
					out.flush();
					out.close();
				}
			} catch (Exception e) {
				AccessLog.Log("writeToGuiaFile()Exception_Error:"+e);
				e.printStackTrace();
			}
			try {
				if (fileOutputStream != null) {
					fileOutputStream.flush();
					fileOutputStream.close();
				}
			} catch (Exception e) {
				AccessLog.Log("writeToGuiaFile()Exception_Error:"+e);
				e.printStackTrace();
			}
		}
	}
	public void writeToGuiaFile(HttpServletRequest request, InputStream is,long sysdate) {
		BufferedReader reader = null;
		InputStreamReader streamReader = null;
		BufferedWriter out = null;
                FileWriter fileOutputStream  = null;
		try {
			HttpSession session = request.getSession(false);
			Global global = (Global)session.getAttribute("sGlobal");
			streamReader = new InputStreamReader(is);
			reader = new BufferedReader(streamReader);			

			// below code added by E.Sundar on 21/10/2003 to avoid data mixing hassle
			String clientId=global.clientId;
			//below code commented by E.Sundar on 21/10/2003
			//String filename = "BRPETIQ_"+sysdate+".ETI";
			String filename = "BRPETIQ_"+sysdate+clientId+".ETI";
			//System.out.println("GUIA PRINT FILE NAME IN ACTION "+filename);//commented and below added by B.Emerson on 30/04/2004
			//AccessLog.Log("GUIA PRINT FILE NAME IN ACTION "+filename);
			//BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(exportdirectory+File.separator+"BRPETIQ"+global.clientId+".ETI")));
			fileOutputStream = new FileWriter(exportdirectory+File.separator+filename);
                        out = new BufferedWriter(fileOutputStream);
			String data=null;
			while((data=reader.readLine())!=null){
				out.write(data);
				out.newLine();
			}
			streamReader.close();
			streamReader = null;
			reader.close();
			reader = null;
                        out.flush();
			out.close();
			out = null;
			global.printfilename=filename;
			session.setAttribute("sGlobal",global);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (streamReader != null) {
					streamReader.close();
				}
			} catch (Exception ex) {
				AccessLog.Log("writeToGuiaFile()Exception_Error:"+ex);
				ex.printStackTrace();
			}
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (Exception ex) {
				AccessLog.Log("writeToGuiaFile()Exception_Error:"+ex);
				ex.printStackTrace();
			}
			try {
				if (out != null) {
					out.flush();
					out.close();
				}
			} catch (Exception e) {
				AccessLog.Log("writeToGuiaFile()Exception_Error:"+e);
				e.printStackTrace();
			}
		}
	}
	
	public void writeToGuiaFile(HttpServletRequest request,String string,long sysdate) {
		LineNumberReader ln = null;
		BufferedWriter out = null;
                FileWriter fileOutputStream = null;
		try {
			HttpSession session = request.getSession(false);
			Global global = (Global)session.getAttribute("sGlobal");
			ln = new LineNumberReader(new StringReader(string));
			// below code added by E.Sundar on 21/10/2003 to avoid data mixing hassle
			String clientId=global.clientId;
			//below code commented by E.Sundar on 21/10/2003
			//String filename = "BRPETIQ_"+sysdate+".ETI";
			String filename = "BRPETIQ_"+sysdate+clientId+".ETI";
			//System.out.println("GUIA PRINT FILE NAME IN ACTION "+filename);//commented and below added by B.Emerson on 30/04/2004
			//AccessLog.Log("GUIA PRINT FILE NAME IN ACTION "+filename);
			//BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(exportdirectory+File.separator+"BRPETIQ"+global.clientId+".ETI")));
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
			AccessLog.Log("writeToGuiaFile()Exception_Error1:"+e);
			e.printStackTrace();
		} finally {
			try {
				if (ln != null) {
					ln.close();
				}
			} catch (Exception e) {
				AccessLog.Log("writeToGuiaFile()Exception_Error2:"+e);
				e.printStackTrace();
			}
			try {
				if (out != null) {
					out.flush();
					out.close();
				}
			} catch (Exception e) {
				AccessLog.Log("writeToGuiaFile()Exception_Error3:"+e);
				e.printStackTrace();
			}
		}
	}
	
	public void writeToGuiaFile(HttpServletRequest request,String string, String rastreo, long sysdate) {
		LineNumberReader ln = null;
		BufferedWriter out = null;
                FileWriter fileOutputStream = null;
		try {
			HttpSession session = request.getSession(false);
			Global global = (Global)session.getAttribute("sGlobal");
			ln = new LineNumberReader(new StringReader(string));
			// below code added by E.Sundar on 21/10/2003 to avoid data mixing hassle
			String clientId=global.clientId;
			//below code commented by E.Sundar on 21/10/2003
			//String filename = "BRPETIQ_"+sysdate+".ETI";
			String filename = "BRPETIQ_"+sysdate+clientId+"_"+rastreo+".ETI";
			//System.out.println("GUIA PRINT FILE NAME IN ACTION "+filename);//commented and below added by B.Emerson on 30/04/2004
			//AccessLog.Log("GUIA PRINT FILE NAME IN ACTION "+filename);
			//BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(exportdirectory+File.separator+"BRPETIQ"+global.clientId+".ETI")));
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
			AccessLog.Log("writeToGuiaFile()2Exception_Error1:"+e);
			e.printStackTrace();
		} finally {
			try {
				if (ln != null) {
					ln.close();
				}
			} catch (Exception e) {
				AccessLog.Log("writeToGuiaFile()2Exception_Error2:"+e);
				e.printStackTrace();
			}
			try {
				if (out != null) {
					out.flush();
					out.close();
				}
			} catch (Exception e) {
				AccessLog.Log("writeToGuiaFile()2Exception_Error3:"+e);
				e.printStackTrace();
			}
		}
	}
	public void writeToGuiaReturnFile(HttpServletRequest request,String string,long sysdate) {
		LineNumberReader ln = null;
		BufferedWriter out = null;
                FileWriter fileOutputStream = null;
		try {
			HttpSession session = request.getSession(false);
			Global global = (Global)session.getAttribute("sGlobal");
			ln = new LineNumberReader(new StringReader(string));
			
			String clientId=global.getClientId();
			
			String filename = "BRPETIQ_RETURN_"+sysdate+"_"+clientId+".ETI";
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
			global.setPrintfilenameRurn(filename);
			session.setAttribute("sGlobal",global);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (ln != null) {
					ln.close();
				}
			} catch (Exception e) {
				AccessLog.Log("writeToGuiaReturnFile()Exception_Error:"+e);
				e.printStackTrace();
			}
			try {
				if (out != null) {
					out.flush();
					out.close();
				}
			} catch (Exception e) {
				AccessLog.Log("writeToGuiaReturnFile()Exception_Error:"+e);
				e.printStackTrace();
			}
		}
	}
}