package paquetexpress.internal.prepaid.usedGuia.form;

import org.apache.struts.action.ActionForm;

/**
 * @author Administrador
 *
 */
public class UsedGuiaForm extends ActionForm{	
	private static final long serialVersionUID = 1L;
	
	private String currentTask = ""; 	
	private String idSetSel = "";
	private String zonaKmSel = "";
	private String tarifaSel = "";
	private String pesoKgSel = "";
	private String volumenSel = "";
	private String numRastreoSel = "";
	private String acuseSel = "";
	private String valorDeclaradoSel = "";
	private String EADSel = "";
	private String RADSel = "";
	private String EXTSel = "";
	private String mainSetSel = "";
	private String invcSel = "";
	
	public String getCurrentTask() {
		return currentTask;
	}
	public void setCurrentTask(String currentTask) {
		this.currentTask = currentTask;
	}
	public String getIdSetSel() {
		return idSetSel;
	}
	public void setIdSetSel(String idSetSel) {
		this.idSetSel = idSetSel;
	}
	public String getZonaKmSel() {
		return zonaKmSel;
	}
	public void setZonaKmSel(String zonaKmSel) {
		this.zonaKmSel = zonaKmSel;
	}	
	public String getTarifaSel() {
		return tarifaSel;
	}
	public void setTarifaSel(String tarifaSel) {
		this.tarifaSel = tarifaSel;
	}
	public String getPesoKgSel() {
		return pesoKgSel;
	}
	public void setPesoKgSel(String pesoKgSel) {
		this.pesoKgSel = pesoKgSel;
	}
	public String getVolumenSel() {
		return volumenSel;
	}
	public void setVolumenSel(String volumenSel) {
		this.volumenSel = volumenSel;
	}
	public String getNumRastreoSel() {
		return numRastreoSel;
	}
	public void setNumRastreoSel(String numRastreoSel) {
		this.numRastreoSel = numRastreoSel;
	}
	public String getAcuseSel() {
		return acuseSel;
	}
	public void setAcuseSel(String acuseSel) {
		this.acuseSel = acuseSel;
	}
	public String getValorDeclaradoSel() {
		return valorDeclaradoSel;
	}
	public void setValorDeclaradoSel(String valorDeclaradoSel) {
		this.valorDeclaradoSel = valorDeclaradoSel;
	}
	public String getEADSel() {
		return EADSel;
	}
	public void setEADSel(String eADSel) {
		EADSel = eADSel;
	}
	public String getRADSel() {
		return RADSel;
	}
	public void setRADSel(String rADSel) {
		RADSel = rADSel;
	}
	public String getEXTSel() {
		return EXTSel;
	}
	public void setEXTSel(String eXTSel) {
		EXTSel = eXTSel;
	}	
	public String getMainSetSel() {
		return mainSetSel;
	}
	public void setMainSetSel(String mainSetSel) {
		this.mainSetSel = mainSetSel;
	}
	public String getInvcSel() {
		return invcSel;
	}
	public void setInvcSel(String invcSel) {
		this.invcSel = invcSel;
	}
}