

/*
 * Created on Aug 24, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */


import java.io.BufferedOutputStream;
import java.io.FileOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import logger.AccessLog;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;



/**
 * @author kart1479
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class FileUploadAction extends Action {
	/* (non-Javadoc)
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		
		
		
		FileUpForm fileUpForm = (FileUpForm)form;
		FormFile myFile = fileUpForm.getTheFile();
		
		
		//getServlet().
		if(myFile==null)
			return mapping.findForward("input");
		String contentType = myFile.getContentType();
		String fileName    = myFile.getFileName();
		int fileSize       = myFile.getFileSize();
		byte[] fileData    = myFile.getFileData();
		
		//AAP//System.out.println("contentType: " + contentType);
		//AAP//System.out.println("File Name: " + fileName);
		//AAP//System.out.println("File Size: " + fileSize);
		//AAP//System.out.println("File Size: " + fileData[0]);
		BufferedOutputStream buff = null;
		FileOutputStream stream = null;
		try{
			stream = new FileOutputStream("c:\\karthi.xls");
			buff = new BufferedOutputStream(stream);
			buff.write(fileData);
			stream.flush();
			stream.close();
			stream = null;
			buff.flush();
			buff.close();
			buff = null;
		}catch(Exception e){
			e.printStackTrace();
		} finally {
			try {
				if (stream != null) {
					stream.flush();
					stream.close();
				}
			} catch (Exception e) {
				AccessLog.Log("FileUploadAction_execute()Exception_Error:"+e);
				e.printStackTrace();
			}
			try {
				if (buff != null) {
					buff.flush();
					buff.close();
				}
			} catch (Exception e) {
				AccessLog.Log("FileUploadAction_execute()Exception_Error:"+e);
				e.printStackTrace();
			}
		}
		//myFile.getInputStream().
		return mapping.findForward("success");

	}


}
