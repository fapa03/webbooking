package mx.com.paquetexpress.zipCodes.form;

import java.util.ArrayList;

import org.apache.struts.action.ActionForm;

/**
 * @author Administrador
 *
 */
/**
 * @author darjona
 *
 */
public class ZipCodeForm extends ActionForm{	
	private static final long serialVersionUID = 1L;
	
	private String currentTask = ""; 
	private String find = "";
	private String findText = "";	

	private String msjeErr = "";
	private String valAcordion ="off";
	
	private String zcPais= "",zcZona="",zcEstado="",zcDelMun="", zcCiuPob="";
	
	private String colonia = "";
	@SuppressWarnings("rawtypes")
	private ArrayList zcPaisValue = new ArrayList(), zcPaisLabel= new ArrayList();
	
	@SuppressWarnings("rawtypes")
	private ArrayList zcZonaValue = new ArrayList(), zcZonaLabel= new ArrayList();
	
	@SuppressWarnings("rawtypes")
	private ArrayList zcEstadoValue = new ArrayList(), zcEstadoLabel= new ArrayList();
	
	@SuppressWarnings("rawtypes")
	private ArrayList zcDelMunValue = new ArrayList(), zcDelMunLabel= new ArrayList();
	
	@SuppressWarnings("rawtypes")
	private ArrayList zcCiuPobValue = new ArrayList(), zcCiuPobLabel= new ArrayList();

	private String zipCodeSel = "";
	private String colDesSel = "";
	private String colCodSel = "";
	private String colLevSel = "";
	private String colTypeSel = "";
	private String idSucSel = "";
	private String ciuDesSel = "";
	private String ciuCodSel = "";
	private String ciuLevSel = "";
	private String ciuTypeSel = "";
	private String munDesSel = "";
	private String munCodSel = "";
	private String munLevSel = "";
	private String munTypeSel = "";
	private String edoDesSel = "";
	private String edoCodSel = "";
	private String edoLevSel = "";
	private String edoTypeSel = "";
	private String paisDesSel = "";
	private String paisCodSel = "";
	private String paisLevSel = "";
	private String paisTypeSel = "";	
	private String zonaDesSel = "";
	private String zonaCodSel = "";
	private String zonaLevSel = "";
	private String zonaTypeSel = "";
	private String searchType = "";

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public String getCurrentTask() {
		return currentTask;
	}
	public void setCurrentTask(String currentTask) {
		this.currentTask = currentTask;
	}	
	public String getFind() {
		return find;
	}
	public void setFind(String find) {
		this.find = find;
	}	
	
	public String getZcPais() {
		return zcPais;
	}
	
	public void setZcPais(String zcPais) {
		this.zcPais = zcPais;
	}
	
	@SuppressWarnings("rawtypes")
	public ArrayList getZcPaisValue(){		
		if (this.zcPaisValue == null) {
			this.zcPaisValue = new ArrayList();
		}		
		return this.zcPaisValue;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setZcPaisValue(String zcPaisValue){
		if ( this.zcPaisValue == null ){			
			this.zcPaisValue = new ArrayList(); 
			this.zcPaisValue.add(zcPaisValue);
		} else {
			this.zcPaisValue.add(zcPaisValue);
		}
	}
	
	@SuppressWarnings("rawtypes")
	public ArrayList getZcPaisLabel(){		
		if (this.zcPaisLabel == null) {
			this.zcPaisLabel = new ArrayList();
		}		
		return this.zcPaisLabel;
	}
	
	@SuppressWarnings("rawtypes")
	public void setZcPaisValue(ArrayList zcPaisValue) {
		this.zcPaisValue = zcPaisValue;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setZcPaisLabel(String zcPaisValue){
		if ( this.zcPaisLabel == null ){			
			this.zcPaisLabel = new ArrayList(); 
			this.zcPaisLabel.add(zcPaisValue);
		} else {
			this.zcPaisLabel.add(zcPaisValue);
		}
	}
	
	@SuppressWarnings("rawtypes")
	public void setZcPaisLabel(ArrayList zcPaisLabel) {
		this.zcPaisLabel = zcPaisLabel;
	}
	public String getZcZona() {
		return zcZona;
	}
	
	public void setZcZona(String zcZona) {
		this.zcZona = zcZona;
	}
	
	@SuppressWarnings("rawtypes")
	public ArrayList getZcZonaValue(){		
		if (this.zcZonaValue == null) {
			this.zcZonaValue = new ArrayList();
		}		
		return this.zcZonaValue;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setZcZonaValue(String zcZona){
		if ( this.zcZonaValue == null ){			
			this.zcZonaValue = new ArrayList(); 
			this.zcZonaValue.add(zcZona);
		} else {
			this.zcZonaValue.add(zcZona);
		}
	}	
	
	@SuppressWarnings("rawtypes")
	public void setZcZonaValue(ArrayList zcZonaValue) {
		this.zcZonaValue = zcZonaValue;
	}
	
	@SuppressWarnings("rawtypes")
	public void setZcZonaLabel(ArrayList zcZonaLabel) {
		this.zcZonaLabel = zcZonaLabel;
	}
	
	@SuppressWarnings("rawtypes")
	public ArrayList getZcZonaLabel(){		
		if (this.zcZonaLabel == null) {
			this.zcZonaLabel = new ArrayList();
		}		
		return this.zcZonaLabel;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setZcZonaLabel(String zcZona){
		if ( this.zcZonaLabel == null ){			
			this.zcZonaLabel = new ArrayList(); 
			this.zcZonaLabel.add(zcZona);
		} else {
			this.zcZonaLabel.add(zcZona);
		}
	}
	
	public String getZcEstado() {
		return zcEstado;
	}
	
	public void setZcEstado(String zcEstado) {
		this.zcEstado = zcEstado;
	}
	
	@SuppressWarnings("rawtypes")
	public ArrayList getZcEstadoValue(){		
		if (this.zcEstadoValue == null) {
			this.zcEstadoValue = new ArrayList();
		}		
		return this.zcEstadoValue;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setZcEstadoValue(String zcEstado){
		if ( this.zcEstadoValue == null ){			
			this.zcEstadoValue = new ArrayList(); 
			this.zcEstadoValue.add(zcEstado);
		} else {
			this.zcEstadoValue.add(zcEstado);
		}
	}
	
	@SuppressWarnings("rawtypes")	
	public void setZcEstadoValue(ArrayList zcEstadoValue) {
		this.zcEstadoValue = zcEstadoValue;
	}
	@SuppressWarnings("rawtypes")
	public ArrayList getZcEstadoLabel(){		
		if (this.zcEstadoLabel == null) {
			this.zcEstadoLabel = new ArrayList();
		}		
		return this.zcEstadoLabel;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setZcEstadoLabel(String zcEstado){
		if ( this.zcEstadoLabel == null ){			
			this.zcEstadoLabel = new ArrayList(); 
			this.zcEstadoLabel.add(zcEstado);
		} else {
			this.zcEstadoLabel.add(zcEstado);
		}
	}
	
	@SuppressWarnings("rawtypes")
	public void setZcEstadoLabel(ArrayList zcEstadoLabel) {
		this.zcEstadoLabel = zcEstadoLabel;
	}
	
	public String getZcDelMun() {
		return zcDelMun;
	}
	
	public void setZcDelMun(String zcDelMun) {
		this.zcDelMun = zcDelMun;
	}
	
	@SuppressWarnings("rawtypes")
	public ArrayList getZcDelMunValue(){		
		if (this.zcDelMunValue == null) {
			this.zcDelMunValue = new ArrayList();
		}		
		return this.zcDelMunValue;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setZcDelMunValue(String zcDelMun){
		if ( this.zcDelMunValue == null ){			
			this.zcDelMunValue = new ArrayList(); 
			this.zcDelMunValue.add(zcDelMun);
		} else {
			this.zcDelMunValue.add(zcDelMun);
		}
	}
	
	@SuppressWarnings("rawtypes")
	public void setZcDelMunValue(ArrayList zcDelMunValue) {
		this.zcDelMunValue = zcDelMunValue;
	}
	
	@SuppressWarnings("rawtypes")
	public ArrayList getZcDelMunLabel(){		
		if (this.zcDelMunLabel == null) {
			this.zcDelMunLabel = new ArrayList();
		}		
		return this.zcDelMunLabel;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setZcDelMunLabel(String zcDelMun){
		if ( this.zcDelMunLabel == null ){			
			this.zcDelMunLabel = new ArrayList(); 
			this.zcDelMunLabel.add(zcDelMun);
		} else {
			this.zcDelMunLabel.add(zcDelMun);
		}
	}
	
	@SuppressWarnings("rawtypes")	
	public void setZcDelMunLabel(ArrayList zcDelMunLabel) {
		this.zcDelMunLabel = zcDelMunLabel;
	}
	
	public String getZcCiuPob() {
		return zcCiuPob;
	}
	
	public void setZcCiuPob(String zcCiuPob) {
		this.zcCiuPob = zcCiuPob;
	}
	
	@SuppressWarnings("rawtypes")
	public ArrayList getZcCiuPobValue(){		
		if (this.zcCiuPobValue == null) {
			this.zcCiuPobValue = new ArrayList();
		}		
		return this.zcCiuPobValue;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setZcCiuPobValue(String zcCiuPob){
		if ( this.zcCiuPobValue == null ){			
			this.zcCiuPobValue = new ArrayList(); 
			this.zcCiuPobValue.add(zcCiuPob);
		} else {
			this.zcCiuPobValue.add(zcCiuPob);
		}
	}
	
	@SuppressWarnings("rawtypes")	
	public void setZcCiuPobValue(ArrayList zcCiuPobValue) {
		this.zcCiuPobValue = zcCiuPobValue;
	}
	
	@SuppressWarnings("rawtypes")
	public ArrayList getZcCiuPobLabel(){		
		if (this.zcCiuPobLabel == null) {
			this.zcCiuPobLabel = new ArrayList();
		}		
		return this.zcCiuPobLabel;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setZcCiuPobLabel(String zcCiuPob){
		if ( this.zcCiuPobLabel == null ){			
			this.zcCiuPobLabel = new ArrayList(); 
			this.zcCiuPobLabel.add(zcCiuPob);
		} else {
			this.zcCiuPobLabel.add(zcCiuPob);
		}
	}
	@SuppressWarnings("rawtypes")
	public void setZcCiuPobLabel(ArrayList zcCiuPobLabel) {
		this.zcCiuPobLabel = zcCiuPobLabel;
	}
	
	public String getColonia() {
		return colonia;
	}
	public void setColonia(String colonia) {
		this.colonia = colonia;
	}
	public String getFindText() {
		return findText;
	}
	public void setFindText(String findText) {
		this.findText = findText;
	}
	public String getMsjeErr() {
		return msjeErr;
	}
	public void setMsjeErr(String msjeErr) {
		this.msjeErr = msjeErr;
	}
	public String getValAcordion() {
		return valAcordion;
	}
	public void setValAcordion(String valAcordion) {
		this.valAcordion = valAcordion;
	}
	public String getZipCodeSel() {
		return zipCodeSel;
	}
	public void setZipCodeSel(String zipCodeSel) {
		this.zipCodeSel = zipCodeSel;
	}
	public String getColDesSel() {
		return colDesSel;
	}
	public void setColDesSel(String colDesSel) {
		this.colDesSel = colDesSel;
	}
	public String getColCodSel() {
		return colCodSel;
	}
	public void setColCodSel(String colCodSel) {
		this.colCodSel = colCodSel;
	}
	public String getColLevSel() {
		return colLevSel;
	}
	public void setColLevSel(String colLevSel) {
		this.colLevSel = colLevSel;
	}
	public String getColTypeSel() {
		return colTypeSel;
	}
	public void setColTypeSel(String colTypeSel) {
		this.colTypeSel = colTypeSel;
	}
	public String getIdSucSel() {
		return idSucSel;
	}
	public void setIdSucSel(String idSucSel) {
		this.idSucSel = idSucSel;
	}
	public String getCiuDesSel() {
		return ciuDesSel;
	}
	public void setCiuDesSel(String ciuDesSel) {
		this.ciuDesSel = ciuDesSel;
	}
	public String getCiuCodSel() {
		return ciuCodSel;
	}
	public void setCiuCodSel(String ciuCodSel) {
		this.ciuCodSel = ciuCodSel;
	}
	public String getCiuLevSel() {
		return ciuLevSel;
	}
	public void setCiuLevSel(String ciuLevSel) {
		this.ciuLevSel = ciuLevSel;
	}
	public String getCiuTypeSel() {
		return ciuTypeSel;
	}
	public void setCiuTypeSel(String ciuTypeSel) {
		this.ciuTypeSel = ciuTypeSel;
	}
	public String getMunDesSel() {
		return munDesSel;
	}
	public void setMunDesSel(String munDesSel) {
		this.munDesSel = munDesSel;
	}
	public String getMunCodSel() {
		return munCodSel;
	}
	public void setMunCodSel(String munCodSel) {
		this.munCodSel = munCodSel;
	}
	public String getMunLevSel() {
		return munLevSel;
	}
	public void setMunLevSel(String munLevSel) {
		this.munLevSel = munLevSel;
	}
	public String getMunTypeSel() {
		return munTypeSel;
	}
	public void setMunTypeSel(String munTypeSel) {
		this.munTypeSel = munTypeSel;
	}
	public String getEdoDesSel() {
		return edoDesSel;
	}
	public void setEdoDesSel(String edoDesSel) {
		this.edoDesSel = edoDesSel;
	}
	public String getEdoCodSel() {
		return edoCodSel;
	}
	public void setEdoCodSel(String edoCodSel) {
		this.edoCodSel = edoCodSel;
	}
	public String getEdoLevSel() {
		return edoLevSel;
	}
	public void setEdoLevSel(String edoLevSel) {
		this.edoLevSel = edoLevSel;
	}
	public String getEdoTypeSel() {
		return edoTypeSel;
	}
	public void setEdoTypeSel(String edoTypeSel) {
		this.edoTypeSel = edoTypeSel;
	}
	public String getPaisDesSel() {
		return paisDesSel;
	}
	public void setPaisDesSel(String paisDesSel) {
		this.paisDesSel = paisDesSel;
	}
	public String getPaisCodSel() {
		return paisCodSel;
	}
	public void setPaisCodSel(String paisCodSel) {
		this.paisCodSel = paisCodSel;
	}
	public String getPaisLevSel() {
		return paisLevSel;
	}
	public void setPaisLevSel(String paisLevSel) {
		this.paisLevSel = paisLevSel;
	}
	public String getPaisTypeSel() {
		return paisTypeSel;
	}
	public void setPaisTypeSel(String paisTypeSel) {
		this.paisTypeSel = paisTypeSel;
	}

	public String getZonaDesSel() {
		return zonaDesSel;
	}

	public void setZonaDesSel(String zonaDesSel) {
		this.zonaDesSel = zonaDesSel;
	}

	public String getZonaCodSel() {
		return zonaCodSel;
	}

	public void setZonaCodSel(String zonaCodSel) {
		this.zonaCodSel = zonaCodSel;
	}

	public String getZonaLevSel() {
		return zonaLevSel;
	}

	public void setZonaLevSel(String zonaLevSel) {
		this.zonaLevSel = zonaLevSel;
	}

	public String getZonaTypeSel() {
		return zonaTypeSel;
	}

	public void setZonaTypeSel(String zonaTypeSel) {
		this.zonaTypeSel = zonaTypeSel;
	}

	/**
	 * @return the searchType
	 */
	public String getSearchType() {
		return searchType;
	}

	/**
	 * @param searchType the searchType to set
	 */
	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}
}