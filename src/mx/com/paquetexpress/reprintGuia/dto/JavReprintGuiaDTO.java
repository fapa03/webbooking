/**
 * @author: Jose Manuel Armenta
 * Fecha de Creación: 10/07/2017
 * Compañía: PAQUETEXPRESS.
 * Descripción del programa: Bean DTO para pantalla reimpresion de guias.
 * FileName: JavReprintGuiaDTO.java
 * SessionVariables:
 * Other Used Classes:
 * Function Names: 
 * ----------------------------------------------------------------- 
 * MODIFICACIONES:  
 * -----------------------------------------------------------------
 * Clave: AAP01
 * Autor: ABRAHAM DANIEL ARJONA PERAZA.
 * Fecha: 03/12/2018
 * Descripción: Se agregan atributos de fecha (isseDate) y referencias (refers). 
 * 
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
package mx.com.paquetexpress.reprintGuia.dto;

import java.util.ArrayList;

public class JavReprintGuiaDTO {
	private String formNumber;
	private String guiaNumber;
	private String destClientName;
	private String guiaAmount;
	private String numPack;
	private String destBranch;
	private String branchLocType;
	private String origBranch;
	private String isseDate;//AAP01
	private String refers;//AAP01
	private boolean checked;
	private ArrayList<JavReprintEtiquetaDTO> listEtiquetas;
	public String getFormNumber() {
		return formNumber;
	}
	public void setFormNumber(String formNumber) {
		this.formNumber = formNumber;
	}
	public String getGuiaNumber() {
		return guiaNumber;
	}
	public void setGuiaNumber(String guiaNumber) {
		this.guiaNumber = guiaNumber;
	}
	public String getDestClientName() {
		return destClientName;
	}
	public void setDestClientName(String destClientName) {
		this.destClientName = destClientName;
	}
	public String getGuiaAmount() {
		return guiaAmount;
	}
	public void setGuiaAmount(String guiaAmount) {
		this.guiaAmount = guiaAmount;
	}
	public String getNumPack() {
		return numPack;
	}
	public void setNumPack(String numPack) {
		this.numPack = numPack;
	}
	public String getDestBranch() {
		return destBranch;
	}
	public void setDestBranch(String destBranch) {
		this.destBranch = destBranch;
	}
	public String getBranchLocType() {
		return branchLocType;
	}
	public void setBranchLocType(String branchLocType) {
		this.branchLocType = branchLocType;
	}
	public String getOrigBranch() {
		return origBranch;
	}
	public void setOrigBranch(String origBranch) {
		this.origBranch = origBranch;
	}
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	public ArrayList<JavReprintEtiquetaDTO> getListEtiquetas() {
		return listEtiquetas;
	}
	public void setListEtiquetas(ArrayList<JavReprintEtiquetaDTO> listEtiquetas) {
		this.listEtiquetas = listEtiquetas;
	}
	public String getIsseDate() {//AAP01
		return isseDate;
	}
	public void setIsseDate(String isseDate) {//AAP01
		this.isseDate = isseDate;
	}
	public String getRefers() {//AAP01
		return refers;
	}
	public void setRefers(String refers) {//AAP01
		this.refers = refers;
	}
	
}
