
/*
 * Created on Aug 24, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */


import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

/**
 * @author kart1479
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class JavFileUploadForm extends ActionForm {
	
	/**
	 * 
	 */
	
	private FormFile theFile;
 
	/**
	 * @return
	 */
	public FormFile getTheFile() {
		return theFile;
	}

	/**
	 * @param file
	 */
	public void setTheFile(FormFile file) {
		theFile = file;
	}

}
