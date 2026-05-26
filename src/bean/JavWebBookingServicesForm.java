 package bean;
/*----------------------------------------------------------
Author					:	V.Ramachandran
Date					:	25-March-2003
FileName				:	JavWebBookingServicesForm.java
SessionVariables		:
Other Used Classes		:
Function Names			:
------------------------------------------------------------*/

import org.apache.struts.action.ActionForm;
import java.util.ArrayList;

import logger.AccessLog;
public class JavWebBookingServicesForm extends ActionForm {
	String entrega,valorcod,cobertura,acusederecibo,valordeclarado,seguro,guiano;
	String serviceshitcount,duplicateguianumber,successmessage,confirmgenerate,errmsgenvelope;
	String guiaprintstring,comments,reference;
	ArrayList insurancetype,insurancetypelabel,ackvalue,acklabel;
	

	public int i=0;
	
	//Setter & getter Added by palanivel
	String serviceAdditional= null ;
	String importeValue =null;
    String showAdditional =null;
	String referenceId =null;
	String errMsgAdditional =null;
	private String zonaExtendida = null;//aap01
	private String operadorLogistico = null;//aap01
	
	/*variables para validacion de tarifa invalida para zona extendida*///AAP02
	private String tarifaInvalida = null;//AAP02
	
	// To check Additional Service available or not 
	public String getServiceAdditional(){
		return this.serviceAdditional;
	}
	public void setServiceAdditional(String serviceAdditional){
		this.serviceAdditional=serviceAdditional;
	}
    // Importe value for Additional Service
	public String getImporteValue(){
		return this.importeValue;
	}
	public void setImporteValue(String importeValue){
		this.importeValue=importeValue;
	}
    //	Restrict the Additional Service for other user than C type 
	public void setShowAdditional(String showAdditional){
		this.showAdditional=showAdditional;		
	}
	public String getShowAdditional(){		
		return this.showAdditional;		
	}
    // To get the ReferenceId of the AdditionalService  
	public void setReferenceId(String referenceId){
		this.referenceId=referenceId;		
	}
	public String getReferenceId(){		
		return this.referenceId;		
	}
	//Error message for choosing the same Additionalservice 
	public void setErrMsgAdditional(String errMsgAdditional){
		this.errMsgAdditional=errMsgAdditional;		
	}
	public String getEerrMsgAdditional(){		
		return this.errMsgAdditional;		
	}
	//End of Code 
	
	
	public JavWebBookingServicesForm(){
		//AAP//AccessLog.Log("JavWebBookingServicesForm");
	}
	
	


	public ArrayList getInsurancetype(){
		if ( this.insurancetype != null ) 
			return this.insurancetype;
		else return new ArrayList();
	}
	
	public void setInsurancetype(String insurancetype){
		if ( this.insurancetype != null ){
			this.insurancetype.add(insurancetype);
		} 
		else{
			this.insurancetype = new ArrayList(); 
			//this.insurancetype.add("NONE");  // For Empty Item
			this.insurancetype.add(insurancetype);
		}
	}
	
	public ArrayList getInsurancetypelabel(){
		if ( this.insurancetypelabel != null ) 
			return this.insurancetypelabel;
		else return new ArrayList();
	}
	
	public void setInsurancetypelabel(String insurancetypelabel){
		if ( this.insurancetypelabel != null ){
			this.insurancetypelabel.add(insurancetypelabel);
		} 
		else{
			this.insurancetypelabel = new ArrayList(); 
			//this.insurancetypelabel.add("NONE");  // For Empty Item
			this.insurancetypelabel.add(insurancetypelabel);
		}
	}
	
	
	
	public ArrayList getAckvalue(){
		if ( this.ackvalue != null ) 
			return this.ackvalue;
		else return new ArrayList();
	}
	
	public void setAckvalue(String ackvalue){
		if ( this.ackvalue != null ){
			this.ackvalue.add(ackvalue);
		} 
		else{
			this.ackvalue = new ArrayList(); 
			//this.insurancetype.add("NONE");  // For Empty Item
			this.ackvalue.add(ackvalue);
		}
	}
	
	public ArrayList getAcklabel(){
		if ( this.acklabel != null ) 
			return this.acklabel;
		else return new ArrayList();
	}
	
	public void setAcklabel(String acklabel){
		if ( this.acklabel != null ){
			this.acklabel.add(acklabel);
		} 
		else{
			this.acklabel = new ArrayList(); 
			//this.insurancetypelabel.add("NONE");  // For Empty Item
			this.acklabel.add(acklabel);
		}
	}
	
	
	public void setEntrega(String entrega){
		this.entrega=entrega;
	}
	public String getEntrega(){
		return this.entrega;
	}
	
	public void setValorcod(String valorcod){
		this.valorcod=valorcod;
	}
	public String getValorcod(){
		return this.valorcod;
	}
	
	public void setCobertura(String cobertura){
		this.cobertura=cobertura;
	}
	public String getCobertura(){
		return this.cobertura;
	}
	
	public void setAcusederecibo(String acusederecibo){
		this.acusederecibo=acusederecibo;
	}
	public String getAcusederecibo(){
		return this.acusederecibo;
	}
	
	public void setValordeclarado(String valordeclarado){
		this.valordeclarado=valordeclarado;
	}
	public String getValordeclarado(){
		return this.valordeclarado;
	}
	
	public void setSeguro(String seguro){
		this.seguro=seguro;
	}
	public String getSeguro(){
		return this.seguro;
	}
	
	public void setGuiano(String guiano){
		this.guiano=guiano;
	}
	public String getGuiano(){
		return this.guiano;
	}
	
	public void setServiceshitcount(String serviceshitcount){
		this.serviceshitcount=serviceshitcount;
		//AAP//AccessLog.Log("CALLING SETTER METHOD");
	}
	public String getServiceshitcount(){
		//AAP//AccessLog.Log("CALLING GETTER METHOD BEFORE "+i);
		i++;
		//AAP//AccessLog.Log("CALLING GETTER METHOD AFTER "+i);
		return this.serviceshitcount;
	}
	
	public void setDuplicateguianumber(String duplicateguianumber){
		this.duplicateguianumber=duplicateguianumber;		
	}
	public String getDuplicateguianumber(){		
		return this.duplicateguianumber;		
	}
	
	public void setSuccessmessage(String successmessage){
		this.successmessage=successmessage;		
	}
	public String getSuccessmessage(){		
		return this.successmessage;
	}
	
	public void setConfirmgenerate(String confirmgenerate){
		this.confirmgenerate=confirmgenerate;		
	}
	public String getConfirmgenerate(){
		return this.confirmgenerate;
	}
	public void setErrmsgenvelope(String errmsgenvelope){
		this.errmsgenvelope=errmsgenvelope;		
	}
	public String getErrmsgenvelope(){
		return this.errmsgenvelope;
	}
	
	public void setComments(String comments){
		this.comments=comments;		
	}
	public String getComments(){
		return this.comments;
	}	
	public void setReference(String reference){
		this.reference=reference;		
	}
	public String getReference(){
		return this.reference;
	}
	public String getZonaExtendida() {//aap01
		return zonaExtendida;
	}
	public void setZonaExtendida(String zonaExtendida) {//aap01
		this.zonaExtendida = zonaExtendida;
	}
	public String getOperadorLogistico() {//aap01
		return operadorLogistico;
	}
	public void setOperadorLogistico(String operadorLogistico) {//aap01
		this.operadorLogistico = operadorLogistico;
	}	
	public String getTarifaInvalida() {
		return tarifaInvalida;
	}
	public void setTarifaInvalida(String tarifaInvalida) {//AAP02
		this.tarifaInvalida = tarifaInvalida;
	}
}