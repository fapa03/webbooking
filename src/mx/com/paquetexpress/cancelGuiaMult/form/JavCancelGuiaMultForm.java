/**
 * @author: PACHECO PARRA EVA MELISSA
 * Fecha de Creaci�n: 10/12/2023
 * Compa��a: PAQUETEXPRESS.
 * Descripci�n del programa: Bean form para pantalla de cancelacion de guias multiples.
 * FileName: JavCancelGuiaMultForm.java
 * SessionVariables:
 * Other Used Classes:
 * Function Names: 
 * ----------------------------------------------------------------- 
 * MODIFICACIONES:  
 * -----------------------------------------------------------------
 * Clave: 
 * Autor: 
 * Fecha: 
 * Descripci�n: 
 * ------------------------------------------------------------------
 * Clave: 
 * Autor: 
 * Fecha: 
 * Descripci�n: 
 * 
 * ------------------------------------------------------------------ 
 * Clave: 
 * Autor: 
 * Fecha: 
 * Descripci�n: 
 * 
 * ------------------------------------------------------------------ 
 */
package mx.com.paquetexpress.cancelGuiaMult.form;

import java.util.ArrayList;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

import mx.com.paquetexpress.cancelGuiaMult.dto.JavCancelGuiaMultDTO;

public class JavCancelGuiaMultForm extends ActionForm {

	private static final long serialVersionUID = 3199895458418871074L;

	private ArrayList<JavCancelGuiaMultDTO> listGuias;
	private FormFile theFile;
	private String currentTask= "";
	private boolean checkSStotal = false;
	private String fileName = "";
	private String pathFile = "";
	private String countGuiaSucess = "0"; 
	private String countGuiaError = "0"; 
	private String filtroPor = ""; 
	private String daysRange = "0"; //Es para determinar el margen de dias a buscar (depende del parametro) 
	private String fechaInicial = ""; 
	private String fechaFinal = ""; 
	private String infoDate = "0"; 
	
	
	public String getCurrentTask() {
		return currentTask;
	}

	public void setCurrentTask(String currentTask) {
		this.currentTask = currentTask;
	}

	public ArrayList<JavCancelGuiaMultDTO> getListGuias() {
		return listGuias;
	}

	public void setListGuias(ArrayList<JavCancelGuiaMultDTO> listGuias) {
		this.listGuias = listGuias;
	}

	public boolean isCheckSStotal() {
		return checkSStotal;
	}
	public void setCheckSStotal(boolean checkSStotal) {
		this.checkSStotal = checkSStotal;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public FormFile getTheFile() {
		return theFile;
	}

	public void setTheFile(FormFile theFile) {
		this.theFile = theFile;
	}

	public String getCountGuiaSucess() {
		return countGuiaSucess;
	}

	public void setCountGuiaSucess(String countGuiaSucess) {
		this.countGuiaSucess = countGuiaSucess;
	}

	public String getCountGuiaError() {
		return countGuiaError;
	}

	public void setCountGuiaError(String countGuiaError) {
		this.countGuiaError = countGuiaError;
	}

	public String getFiltroPor() {
		return filtroPor;
	}

	public void setFiltroPor(String filtroPor) {
		this.filtroPor = filtroPor;
	}

	
	public String getDaysRange() {
		return daysRange;
	}

	public void setDaysRange(String daysRange) {
		this.daysRange = daysRange;
	}

	public String getFechaInicial() {
		return fechaInicial;
	}

	public void setFechaInicial(String fechaInicial) {
		this.fechaInicial = fechaInicial;
	}

	public String getFechaFinal() {
		return fechaFinal;
	}

	public void setFechaFinal(String fechafinal) {
		this.fechaFinal = fechafinal;
	}

	public String getInfoDate() {
		return infoDate;
	}

	public void setInfoDate(String infoDate) {
		this.infoDate = infoDate;
	}
	
	
	
	
} // End class
