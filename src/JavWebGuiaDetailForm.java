
/**
 * File Name    : GuiaDetail.java
 * Description  :This is the FormBean which Provides Setter and getter MethodsBean. 
 * Date Written :  28-Feb-2003
 * @author 	    :  D.SivaKumar
 * ----------------------------------------------------------------- 
 * MODIFICACIONES:  
 * -----------------------------------------------------------------
 * Clave: AAP01
 * Autor: ABRAHAM DANIEL ARJONA PERAZA.
 * Fecha: 04/07/2013
 * Descripción: Se agregó variable formaPago para mantener el valor seleccionado en la consulta de manifiestos.
 * ------------------------------------------------------------------ 
 */
import org.apache.struts.action.ActionForm;

public class JavWebGuiaDetailForm extends ActionForm {
	
	// Proprty Names in the e GuiaDetailForm  Bean for-Section1
	
	String formNumber;
	String guiaNumber;
	String status;
	String statusDesc;	
	String issueDate;
	String guiaAmount;
	String totalValue;
	String codeValue;
	
	// Proprty Names in the e GuiaDetailForm  Bean for-Section2 Origin Client Address
	String orginBranchId;
	String orginBranchName;
	String orginClientId;
	String orginClientName;
	String orginClientAddress;
	String orginDoorNumber;
	String orginColony;
	String orginCity;
	String orginRfc;
	String orginPhoneNumber;
	String orginSite;
	
	// Proprty Names in the e GuiaDetailForm  Bean for-Section2 destination  Client Address
	String destBranchId;
	String destBranchName;
	String destClientId;
	String destClientName;
	String destClientAddress;
	String destDoorNumber;
	String destColony;
	String destCity;	
	String destPhoneNumber;
	String destSite;
	// Proprty Names in the e GuiaDetailForm  Bean for-Section3 Delivery Details
	
	String deleiveryType;
	String deliverer;
	String delivererName;
	String deliveryDate;
	
	String fiscalAddress;
	String fiscalColony;
	String fiscalDoorNumber;
	String fiscalStreet;
	String fiscalPhoneNumber;	
	
	//hidFromFlag to identift the Screen which called the Guia Details..
	String hidFromFlag;
	private String formaPago = "";//AAP01
	private String docuType = "";//AAP02
	private String manifiestoType = "";//AAP02
	
	private String preferedAddressCode = "";//AAP03
	private String preferedAddress = "";//AAP03
	
	//Constructor to crate Object for GuiaDetailForm
	public JavWebGuiaDetailForm() {
	}  
	
	public void setHidFromFlag(String ff){
		this.hidFromFlag=ff;
	}
	
	public String getHidFromFlag(){
		return this.hidFromFlag;
	}
	
	//Setter and Getter Methods For GuiaDetailForm Bean Setion1.
	
	public void setFormNumber(String formno){
		this.formNumber=formno;
	}	
	public String getFormNumber(){
		return this.formNumber;
	}
	public void setGuiaNumber(String gno){
		this.guiaNumber=gno;
	}
	public String getGuiaNumber(){
		return this.guiaNumber;
	}
	public void setStatus(String st){
		this.status=st;
	}
	public String getStatus(){
		return this.status;
	}
	public void setStatusDesc(String statusdesc){
		this.statusDesc = statusdesc;
	}
	public void setIssueDate(String issusedate){
		this.issueDate =issusedate;
	}
	public String getIssueDate(){
		return this.issueDate ;
	}
	
	public void setGuiaAmount(String gamt){
		this.guiaAmount =gamt;
	}
	
	public String getGuiaAmount(){
		return this.guiaAmount;
	}
	
	public void setTotalValue(String totalvalue){
		this.totalValue=totalvalue;
	}
	public String getTotalValue(){
		return this.totalValue;
	}
	
	public void setCodeValue(String codevalue){
		this.codeValue=codevalue;
	}
	public String getCodeValue(){
		return this.codeValue;
	}
	public String getStatusDesc(){
		return this.statusDesc;		
	}
	
	//Setter and Getter Methods For GuiaDetailForm Bean Setion2. Origin Client Address	
	
	
	public String getOrginBranchId(){
		return this.orginBranchId;
	}
	public void setOrginBranchId(String obid){
		this.orginBranchId=obid;		
	}
	public String getOrginBranchName(){
		return this.orginBranchName;
	}
	public void setOrginBranchName(String obname){
		this.orginBranchName=obname;		
	}
	public String getOrginClientId(){
		return this.orginClientId;
	}
	public void setOrginClientId(String ocid){
		this.orginClientId=ocid;		
	}
	public String getOrginClientName(){
		return this.orginClientName;
	}
	public void setOrginClientName(String ocname){
		this.orginClientName=ocname;		
	}
	
	public String getOrginClientAddress(){
		return this.orginClientAddress;
	}
	public void setOrginClientAddress(String ocaddr){
		this.orginClientAddress=ocaddr;		
	}
	public String getOrginDoorNumber(){
		return this.orginDoorNumber;
	}
	public void setOrginDoorNumber(String odn){
		this.orginDoorNumber=odn;		
	}
	
	public String getOrginColony(){
		return this.orginColony;
	}
	public void setOrginColony(String oc){
		this.orginColony=oc;		
	}
	
	public String getOrginCity(){
		return this.orginCity;
	}
	public void setOrginCity(String ocity){
		this.orginCity=ocity;		
	}
	
	public String getOrginRfc(){
		return this.orginRfc;
	}
	public void setOrginRfc(String rfc){
		this.orginRfc=rfc;
	}
	
	public String getOrginPhoneNumber(){
		return this.orginPhoneNumber;
	}
	public void setOrginPhoneNumber(String pno){
		this.orginPhoneNumber=pno;
	}
	
	//Setter and Getter Methods For GuiaDetailForm Bean Setion2. Destination Client Address
	
	public String getDestBranchId(){
		return this.destBranchId;
	}
	public void setDestBranchId(String dbid){
		this.destBranchId=dbid;		
	}
	public String getDestBranchName(){
		return this.destBranchName;
	}
	public void setDestBranchName(String obname){
		this.destBranchName=obname;		
	}
	public String getDestClientId(){
		return this.destClientId;
	}
	public void setDestClientId(String dcid){
		this.destClientId=dcid;		
	}
	public String getDestClientName(){
		return this.destClientName;
	}
	public void setDestClientName(String ocname){
		this.destClientName=ocname;		
	}
	
	public String getDestClientAddress(){
		return this.destClientAddress;
	}
	public void setDestClientAddress(String ocaddr){
		this.destClientAddress=ocaddr;		
	}
	public String getDestDoorNumber(){
		return this.destDoorNumber;
	}
	public void setDestDoorNumber(String odn){
		this.destDoorNumber=odn;		
	}
	
	public String getDestColony(){
		return this.destColony;
	}
	public void setDestColony(String oc){
		this.destColony=oc;		
	}
	
	public String getDestCity(){
		return this.destCity;
	}
	public void setDestCity(String ocity){
		this.destCity=ocity;		
	}	
	public String getDestPhoneNumber(){
		return this.destPhoneNumber;
	}
	public void setDestPhoneNumber(String pno){
		this.destPhoneNumber=pno;
	}
	
	
	// Getter and Setter Methods For Section3 Delivery Details...
	
	public void setDeleiveryType(String delitype){
		this.deleiveryType =delitype;
	}
	
	public String getDeleiveryType(){
		return this.deleiveryType;
	}
	
	
	public void setDeliverer(String deliver){
		this.deliverer=deliver;
	}
	
	public String getDeliverer(){
		return this.deliverer;
	}
	public void setDelivererName(String delivername){
		this.delivererName =delivername;
	}
	
	public String getDelivererName(){
		return this.delivererName;
	}
	public void setDeliveryDate(String delydate){
		this.deliveryDate =delydate;
	}
	
	public String getDeliveryDate(){
		return this.deliveryDate;
	}
	
	public void setFiscalAddress(String fiscaladdr){
		this.fiscalAddress =fiscaladdr;
	}
	
	public String getFiscalAddress(){
		return this.fiscalAddress;
	}
	
	public void setFiscalColony(String fiscalcol){
		this.fiscalColony =fiscalcol;
	}
	
	public String getFiscalColony(){
		return this.fiscalColony;
	}
	
	public void setFiscalDoorNumber(String fiscaldrnr){
		this.fiscalDoorNumber=fiscaldrnr;
	}
	
	public String getFiscalDoorNumber(){
		return this.fiscalDoorNumber ;
	}
	
	public void setFiscalStreet(String fiscalstrt){
		this.fiscalStreet=fiscalstrt;
	}
	
	public String getFiscalStreet(){
		return this.fiscalStreet ;
	}
	
	public void setFiscalPhoneNumber(String fiscalpno){
		this.fiscalPhoneNumber=fiscalpno;
	}
	
	public String getFiscalPhoneNumber(){
		return this.fiscalPhoneNumber ;
	}

	public String getDestSite() {
		return destSite;
	}

	public void setDestSite(String destSite) {
		this.destSite = destSite;
	}

	public String getOrginSite() {
		return orginSite;
	}

	public void setOrginSite(String orginSite) {
		this.orginSite = orginSite;
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
