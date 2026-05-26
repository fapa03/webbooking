package beanForm;

/*
 * Created on Aug 24, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */


import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

public class JavFileUploadForm2 extends ActionForm {
	
	/**
	 * 
	 */
	
	private FormFile theFile;
	private String fileName = "";
	private String pathFile = "";
	private String currentTask = "";
	private String flagGuiaReturn = "";	
	private String flagGuiaEtiPdf = ""; 
	private String linkGuiaEtiPdf = ""; 
	private Integer countGuiaError = 0; 
	private Integer countGuiaInfo = 0; 
	private String flagGuiaProcess = "N"; 
	
 
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

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getPathFile() {
		return pathFile;
	}

	public void setPathFile(String pathFile) {
		this.pathFile = pathFile;
	}

	public String getCurrentTask() {
		return currentTask;
	}

	public void setCurrentTask(String action) {
		this.currentTask = action;
	}

	public String getFlagGuiaReturn() {
		return flagGuiaReturn;
	}

	public void setFlagGuiaReturn(String flagGuiaReturn) {
		this.flagGuiaReturn = flagGuiaReturn;
	}

	public String getFlagGuiaEtiPdf() {
		return flagGuiaEtiPdf;
	}

	public void setFlagGuiaEtiPdf(String flagGuiaEtiPdf) {
		this.flagGuiaEtiPdf = flagGuiaEtiPdf;
	}

	public String getLinkGuiaEtiPdf() {
		return linkGuiaEtiPdf;
	}

	public void setLinkGuiaEtiPdf(String linkGuiaEtiPdf) {
		this.linkGuiaEtiPdf = linkGuiaEtiPdf;
	}

	public Integer getCountGuiaError() {
		return countGuiaError;
	}

	public void setCountGuiaError(Integer countGuiaError) {
		this.countGuiaError = countGuiaError;
	}

	public Integer getCountGuiaInfo() {
		return countGuiaInfo;
	}

	public void setCountGuiaInfo(Integer countGuiaInfo) {
		this.countGuiaInfo = countGuiaInfo;
	}

	public String getFlagGuiaProcess() {
		return flagGuiaProcess;
	}

	public void setFlagGuiaProcess(String flagGuiaProcess) {
		this.flagGuiaProcess = flagGuiaProcess;
	}

	
	
	
	

}
