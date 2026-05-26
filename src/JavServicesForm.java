/**
 * @author: kumaran
 * Fecha de Creación: 
 * Compañía: KUMARAN.
 * Descripción del programa: Beanform para pantalla de servicios en la consulta de manifiestos.
 * FileName: JavServicesForm.java
 * SessionVariables:
 * Other Used Classes:
 * Function Names: 
 * ----------------------------------------------------------------- 
 * MODIFICACIONES:  
 * -----------------------------------------------------------------
 * Clave: AAP01
 * Autor: ABRAHAM DANIEL ARJONA PERAZA.
 * Fecha: 04/07/2013
 * Descripción: Se agregó variable formaPago para mantener el valor seleccionado en la consulta de manifiestos.
 * ------------------------------------------------------------------
 * Clave: 
 * Autor:
 * Fecha:
 * Descripción:
 * ------------------------------------------------------------------
 */

import org.apache.struts.action.ActionForm;

public class JavServicesForm extends ActionForm{
	private String comment;
	private String addInfo;
	private String hidGuiaNumber;	
	private String formaPago = "";//AAP01
	private String docuType = "";//AAP02
	private String manifiestoType = "";//AAP02
	
	private String preferedAddressCode = "";//AAP03
	private String preferedAddress = "";//AAP03
	
	public JavServicesForm(){}
	
	public String getComment(){
		return comment;			
	}
	
	public String getAddInfo(){
		return addInfo;
	}
	public String getHidGuiaNumber(){
        return hidGuiaNumber;      
	}
	
	public void setComment(String comment){		
		this.comment = comment;					
	}
	public void setAddInfo(String addInfo){
		if(addInfo == null){
			this.addInfo = "";
		}
		else{			
			this.addInfo = addInfo;
		}
	}
    public void setHidGuiaNumber(String hidGuiaNumber){
        this.hidGuiaNumber=hidGuiaNumber;
	}

	public String getFormaPago() {
		return formaPago;
	}

	public void setFormaPago(String formaPago) {
		this.formaPago = formaPago;
	}

	public String getDocuType() {
		return docuType;
	}

	public void setDocuType(String docuType) {
		this.docuType = docuType;
	}

	public String getManifiestoType() {
		return manifiestoType;
	}

	public void setManifiestoType(String manifiestoType) {
		this.manifiestoType = manifiestoType;
	}

	public String getPreferedAddressCode() {
		return preferedAddressCode;
	}

	public void setPreferedAddressCode(String preferedAddressCode) {
		this.preferedAddressCode = preferedAddressCode;
	}

	public String getPreferedAddress() {
		return preferedAddress;
	}

	public void setPreferedAddress(String preferedAddress) {
		this.preferedAddress = preferedAddress;
	}	
}
