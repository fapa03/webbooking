/**
 * @author: Jose Manuel Armenta
 * Fecha de Creación: 10/07/2017
 * Compañía: PAQUETEXPRESS.
 * Descripción del programa: Bean form para pantalla reimpresion de guias.
 * FileName: JavReprintGuiaForm.java
 * SessionVariables:
 * Other Used Classes:
 * Function Names: 
 * ----------------------------------------------------------------- 
 * MODIFICACIONES:  
 * -----------------------------------------------------------------
 * Clave: AAP01
 * Autor: ABRAHAM DANIEL ARJONA PERAZA.
 * Fecha: 03/12/2018
 * Descripción: variable checkSStotal para marcar si quiere imprimir la cantidad de CP configuradas por cliente
 * ------------------------------------------------------------------
 * Clave: 
 * Autor: 
 * Fecha: 
 * Descripción: 
 * ------------------------------------------------------------------
 * Clave: 
 * Autor: 
 * Fecha: 
 * Descripción: 
 * 
 * ------------------------------------------------------------------ 
 * Clave: 
 * Autor: 
 * Fecha: 
 * Descripción: 
 * 
 * ------------------------------------------------------------------ 
 */
package mx.com.paquetexpress.reprintGuia.form;

import java.util.ArrayList;

import org.apache.struts.action.ActionForm;

import mx.com.paquetexpress.reprintGuia.dto.JavReprintGuiaDTO;

public class JavReprintGuiaForm extends ActionForm {

	private static final long serialVersionUID = 3199895458418871074L;

	private ArrayList<JavReprintGuiaDTO> listGuias;
	private String currentTask="start";
	private boolean checkSStotal = false;//AAP01

	public String getCurrentTask() {
		return currentTask;
	}

	public void setCurrentTask(String currentTask) {
		this.currentTask = currentTask;
	}

	public ArrayList<JavReprintGuiaDTO> getListGuias() {
		return listGuias;
	}

	public void setListGuias(ArrayList<JavReprintGuiaDTO> listGuias) {
		this.listGuias = listGuias;
	}

	public boolean isCheckSStotal() {//AAP01
		return checkSStotal;
	}

	public void setCheckSStotal(boolean checkSStotal) {//AAP01
		this.checkSStotal = checkSStotal;
	}
	
} // End class
