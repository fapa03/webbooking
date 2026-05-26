/**
 * @author: EVA MELISSA PACHECO PARRA
 * Fecha de Creación: 24/11/2023
 * Compa��a: PAQUETEXPRESS.
 * Descripci�n del programa: Bean accion para pantalla reimpresion de guias.
 * FileName: JavCancelGuiaMultDTO.java
 * SessionVariables:
 * Other Used Classes:
 * Function Names: 
 * ----------------------------------------------------------------- 
 * MODIFICACIONES:  
 * ------------------------------------------------------------------
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
package mx.com.paquetexpress.cancelGuiaMult.dto;


public class JavCancelGuiaMultDTO {
	private String formNumber;
	private String guiaNumber;
	private String destClientName;
	private String guiaAmount;
	private String numPack;
	private String destBranch;
	private String branchLocType;
	private String origBranch;
	private String isseDate;
	private boolean checked;
	private String guiaDescCode;
	
	private String statusRastreo; 
	private String msjStatus; 
	private String docuType;
	private String refers;
	
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
	public String getIsseDate() {
		return isseDate;
	}
	public void setIsseDate(String isseDate) {
		this.isseDate = isseDate;
	}
	public String getMsjStatus() {
		return msjStatus;
	}
	public void setMsjStatus(String msjStatus) {
		this.msjStatus = msjStatus;
	}
	public String getDocuType() {
		return docuType;
	}
	public void setDocuType(String docuType) {
		this.docuType = docuType;
	}
	public String getGuiaDescCode() {
		return guiaDescCode;
	}
	public void setGuiaDescCode(String guiaDescCode) {
		this.guiaDescCode = guiaDescCode;
	}
	public String getStatusRastreo() {
		return statusRastreo;
	}
	public void setStatusRastreo(String statusRastreo) {
		this.statusRastreo = statusRastreo;
	}
	public String getRefers() {
		return refers;
	}
	public void setRefers(String refers) {
		this.refers = refers;
	}
	
	
	
}
