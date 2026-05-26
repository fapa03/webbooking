package bean;
/*----------------------------------------------------------
Author					:	Kavitha.P
Date					:	06-April-2011
FileName				:	ShipmentBookingForm.java
SessionVariables		:
Other Used Classes		:
Function Names			:
------------------------------------------------------------*/

import java.util.ArrayList;

import org.apache.struts.action.ActionForm;

import logger.AccessLog;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.Action;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
public class ShipmentBookingForm extends ActionForm 
{
	public ShipmentBookingForm(){
		AccessLog.Log("ShipmentBookingForm");
		
	}
	String destinationclave,destinationnombre,destino1,orgienClave,orgienNombre;
	
	public String cantidad,descripcion,contenido,tarifa,destinationcode;
	public String peso,volumen;
	String orgionbranch,orgien1;
	
	String entrega,acusederecibo,radioselect,destinationaddresscode,guiano;
	String destino2,destinationcolonia1,destinationcolonia2,destinationrfc;
	

	String destinationtelefono,destinationsitecode,destinationsite,destinationbranch;
	
	String cobertura,valorcod,valordeclarado,seguro,comments,reference="";
	String	pedinumber,fiscaladdresscode,orgionaddresscode,custagent,ship_seqn,no_ship,cur_loc,cur_dest,lc_rout,shiperrmsg;
	String borderbranchcheck;
	
	public int i=0;
	public String citycode;
	//public String hborderType;
	public String destinationcoloniacode;
	public String getycode="";
	

	
	public String getytype="";
	public String getylevl="";
	String serviceshitcount,duplicateguianumber,successmessage,confirmgenerate,errmsgenvelope;
	String referenceId =null;
	String showAdditional =null;

	ArrayList ackvalue,insurancetypelabel,insurancetype,acklabel;
	
	String ss_srvc_id,ss_refr_srvc_id,descripcioncode,importe,tarifType;
	

	String specialTariff="";
	
	String serviceAdditional= null ;
	String importeValue =null;
    /*String showAdditional =null;
	String referenceId =null;
	*/String errMsgAdditional =null;
     String addEmparque="";
	public String getAddEmparque() {
		return addEmparque;
	}
	public void setAddEmparque(String addEmparque) {
		this.addEmparque = addEmparque;
	}
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
	
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		  this.destinationclave=null;
		  this.destinationnombre=null;
		  this.destino1=null;
		  this.destino2=null;
		  this.destinationcolonia1=null;
		  this.destinationcolonia2=null;
		  this.destinationrfc=null;
		  this.destinationtelefono=null;
		  this.destinationsitecode=null;
		  this.destinationsite=null;
		  this.destinationcode=null;
		  this.destinationbranch=null;
		  this.cantidad = null;
		  
		  }
	
	
/*	public String getHborderType() {
		return hborderType;
	}
	public void setHborderType(String hborderType) {
		this.hborderType = hborderType;
	}*/
	
    //	Restrict the Additional Service for other user than C type 
	/*public void setShowAdditional(String showAdditional){
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
	}*/
	//Error message for choosing the same Additionalservice 
	public String getOrgionbranch() {
		return orgionbranch;
	}
	public void setOrgionbranch(String orgionbranch) {
		this.orgionbranch = orgionbranch;
	}
	public String getOrgien1() {
		return orgien1;
	}
	public void setOrgien1(String orgien1) {
		this.orgien1 = orgien1;
	}
	
	
	
	public String getGuiano() {
		return guiano;
	}
	public void setGuiano(String guiano) {
		this.guiano = guiano;
	}
	
	public void setErrMsgAdditional(String errMsgAdditional){
		this.errMsgAdditional=errMsgAdditional;		
	}
	public String getEerrMsgAdditional(){		
		return this.errMsgAdditional;		
	}
	
	
	public String getSpecialTariff() {
		return specialTariff;
	}

	public void setSpecialTariff(String specialTariff) {
		this.specialTariff = specialTariff;
	}
	public String getTarifType() {
		return tarifType;
	}
	public void setTarifType(String tarifType) {
		this.tarifType = tarifType;
	}
	
	
	
	public String getSs_srvc_id(){
		return this.ss_srvc_id;
	}
	public void setSs_srvc_id(String ss_srvc_id){		
		this.ss_srvc_id=ss_srvc_id;
	}
	
	public String getSs_refr_srvc_id(){
		return this.ss_refr_srvc_id;
	}
	public void setSs_refr_srvc_id(String ss_refr_srvc_id){		
		this.ss_refr_srvc_id=ss_refr_srvc_id;
	}
	
	public String getDescripcioncode(){
		return this.descripcioncode;
	}
	public void setDescripcioncode(String descripcioncode){		
		this.descripcioncode=descripcioncode;
	}
	
	public String getImporte(){
		return this.importe;
	}
	public void setImporte(String importe){		
		this.importe=importe;
	}
	
	public String getDestinationsitecode() {
		return destinationsitecode;
	}

	public void setDestinationsitecode(String destinationsitecode) {
		this.destinationsitecode = destinationsitecode;
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
	//here i put it,,
	public String getDestinationclave() {
		return destinationclave;
	}

	public void setDestinationclave(String destinationclave) {
		this.destinationclave = destinationclave;
	}

	public String getDestinationnombre() {
		return destinationnombre;
	}

	public void setDestinationnombre(String destinationnombre) {
		this.destinationnombre = destinationnombre;
	}
	
	
	public String getGetycode() {
		return getycode;
	}

	public void setGetycode(String getycode) {
		this.getycode = getycode;
	}

	public String getGetylevl() {
		return getylevl;
	}

	public void setGetylevl(String getylevl) {
		this.getylevl = getylevl;
	}

	public String getGetytype() {
		return getytype;
	}

	public void setGetytype(String getytype) {
		this.getytype = getytype;
	}
	public String getShiperrmsg(){
		return this.shiperrmsg;
	}
	public void setShiperrmsg(String shiperrmsg){
		this.shiperrmsg=shiperrmsg;
	}
	
	public String getLc_rout(){
		return this.lc_rout;
	}
	public void setLc_rout(String lc_rout){
		this.lc_rout=lc_rout;
	}
	
	public String getCur_loc(){
		return this.cur_loc;
	}
	public void setCur_loc(String cur_loc){
		this.cur_loc=cur_loc;
	}
	
	public String getCur_dest(){
		return this.cur_dest;
	}
	public void setCur_dest(String cur_dest){
		this.cur_dest=cur_dest;
	}
	
	public String getNo_ship(){
		return this.no_ship;
	}
	public void setNo_ship(String no_ship){
		this.no_ship=no_ship;
	}
	public String getDestinationcoloniacode(){
		return this.destinationcoloniacode;
	}
	public void setDestinationcoloniacode(String destinationcoloniacode){
		this.destinationcoloniacode=destinationcoloniacode;
	}
	public String getShip_seqn(){
		return this.ship_seqn;
	}
	public void setShip_seqn(String ship_seqn){
		this.ship_seqn=ship_seqn;
	}
	public String getCustagent(){
		return this.custagent;
	}
	public void setCustagent(String custagent){
		this.custagent=custagent;
	}
	public String getBorderbranchcheck(){
		return this.borderbranchcheck;
	}
	public void setBorderbranchcheck(String borderbranchcheck){
		this.borderbranchcheck=borderbranchcheck;
	}
	public String getPedinumber(){
		return this.pedinumber;
	}
	public void setPedinumber(String pedinumber){
		this.pedinumber=pedinumber;
	}
	public String getOrgionaddresscode(){
		return this.orgionaddresscode;
	}
	public void setOrgionaddresscode(String orgionaddresscode){
		this.orgionaddresscode=orgionaddresscode;
	}
	public String getDestinationaddresscode(){
		return this.destinationaddresscode;
	}
	public void setDestinationaddresscode(String destinationaddresscode){
		this.destinationaddresscode=destinationaddresscode;
	}
	public String getFiscaladdresscode(){
		return this.fiscaladdresscode;
	}
	public void setFiscaladdresscode(String fiscaladdresscode){
		this.fiscaladdresscode=fiscaladdresscode;
	}
	public String getCitycode(){
		return this.citycode;
	}
	public void setCitycode(String citycode){
		this.citycode=citycode;
	}
	
	public void setServiceshitcount(String serviceshitcount){
		this.serviceshitcount=serviceshitcount;
		AccessLog.Log("CALLING SETTER METHOD");
	}
	public String getServiceshitcount(){
		AccessLog.Log("CALLING GETTER METHOD BEFORE "+i);
		i++;
		AccessLog.Log("CALLING GETTER METHOD AFTER "+i);
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
	
	
	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
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
		if ( this.insurancetypelabel != null )
	{
			this.insurancetypelabel.add(insurancetypelabel);
		} 
		else{
			this.insurancetypelabel = new ArrayList(); 
			//this.insurancetypelabel.add("NONE");  // For Empty Item
			this.insurancetypelabel.add(insurancetypelabel);
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
	public void setCobertura(String cobertura){
		this.cobertura=cobertura;
	}
	public String getCobertura(){
		return this.cobertura;
	}
	
	public void setValordeclarado(String valordeclarado){
		this.valordeclarado=valordeclarado;
	}
	public String getValordeclarado(){
		return this.valordeclarado;
	}
	
	public String getSeguro() {
		return this.seguro;
	}

	public void setSeguro(String seguro) {
		this.seguro = seguro;
	}
	
	public String getOrgienNombre() {
		return orgienNombre;
	}
	public void setOrgienNombre(String orgienNombre) {
		this.orgienNombre = orgienNombre;
	}

	//Hidden field of tariff  
	public String getTarifa() {
		return tarifa;
	}
	public void setTarifa(String tarifa) {
		this.tarifa = tarifa;
	}

	public void setAcusederecibo(String acusederecibo){
		this.acusederecibo=acusederecibo;
	}
	public String getAcusederecibo(){
		return this.acusederecibo;
	}
	public String getEntrega() {
		return entrega;
	}
	public void setEntrega(String entrega) {
		this.entrega = entrega;
	}
	
	public void setValorcod(String valorcod){
		this.valorcod=valorcod;
	}
	public String getValorcod(){
		return this.valorcod;
	}

	public String getDestinationcolonia1() {
		return destinationcolonia1;
	}

	public void setDestinationcolonia1(String destinationcolonia1) {
		this.destinationcolonia1 = destinationcolonia1;
	}

	public String getDestinationcolonia2() {
		return destinationcolonia2;
	}

	public void setDestinationcolonia2(String destinationcolonia2) {
		this.destinationcolonia2 = destinationcolonia2;
	}

	public String getDestinationrfc() {
		return destinationrfc;
	}

	public void setDestinationrfc(String destinationrfc) {
		this.destinationrfc = destinationrfc;
	}

	public String getDestinationtelefono() {
		return destinationtelefono;
	}

	public void setDestinationtelefono(String destinationtelefono) {
		this.destinationtelefono = destinationtelefono;
	}
	
	public String getDestinationsite() {
		return destinationsite;
	}

	public void setDestinationsite(String destinationsite) {
		this.destinationsite = destinationsite;
	}
	public String getDestinationcode() {
		return destinationcode;
	}

	public void setDestinationcode(String destinationcode) {
		this.destinationcode = destinationcode;
	}
	
	public String getOrgienClave() {
		return orgienClave;
	}
	public void setOrgienClave(String orgienClave) {
		this.orgienClave = orgienClave;
	}
	public String getDestinationbranch() {
		return destinationbranch;
	}

	public void setDestinationbranch(String destinationbranch) {
		this.destinationbranch = destinationbranch;
	}
	
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getContenido() {
		return contenido;
	}
	public void setContenido(String contenido) {
		this.contenido = contenido;// "Kavi m herere in Content";
	}
	public String getPeso() {
		return peso;
	}
	public void setPeso(String peso) {
		this.peso = peso;
	}
	public String getVolumen() {
		return volumen;
	}
	public void setVolumen(String volumen) {
		this.volumen = volumen;
	}

	
	
	
	public String getCantidad() {
		return cantidad;
	}
	public void setCantidad(String cantidad) {
		this.cantidad = cantidad;
	}
	public String getRadioselect(){
		return this.radioselect;
	}
	public void setRadioselect(String radioselect){		
		this.radioselect=radioselect;
	}
	
	

	public String getDestino1() {
		return destino1;
	}

	public void setDestino1(String destino1) {
		this.destino1 = destino1;
	}

	public String getDestino2() {
		return destino2;
	}

	public void setDestino2(String destino2) {
		this.destino2 = destino2;
	}


}