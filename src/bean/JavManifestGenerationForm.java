 package bean;
/**
 * File Name    : GuiaDetail.java
 * Description  :This is the FormBean which Provides Setter and getter MethodsBean. 
 * Date Written : 2003
 * @author 	    :  D.SivaKumar
 * ------------------------------------------------------------------
 * Clave: AAP01
 * Autor: Abraham Daniel Arjona Peraza
 * Fecha: 04/07/2013
 * Descripción: Se agregaron variables:
 * formaPago. Mantiene el indicador de que existen o nó guias de flete por cobrar para generarles manifiesto. 
 * reference. Referencia relacionada al manifiesto, almacenada en WEB_GUIA_REFR.
 * ------------------------------------------------------------------ 
 */
import java.util.ArrayList;

import org.apache.struts.action.ActionForm;


	public class JavManifestGenerationForm extends ActionForm {

	//Added by Sam.D.Jabeen on [31-May-2006] 
	String clientBranchLocType = "";    //for keeping Client Branch Type
	String manifiestoType ="";       //For keeping manifestoType
	//Eoc added by Sam.D.Jabeen

	
	String avaiableCreditFlag="";
	double avaiableCredit=0.0;
	double creditLimit=0.0;
	String bookedGuiaAmt="0.0";
	String preferedAddress;
	private String preferedAddressCode ="";
	String addressCode="";
	String addressType="";
    String doorNumber="";
	String streetName="";
	String phoneNumber="";
	String suitNumber="";
	String floorNumber="";
	String addressLine1="";
	String addressLine2="";
	String addressLine3="";
	String addressLine4="";
	String addressLine5="";
	String addressLine6="";
	String addressLine7="";
	String zipCode="";
	
	String collectionTime;
	String manifestNumber;
	String operation;
	String noDataFound;
	String clickedItems;
	String disableGenerate;	
	String manifestIssueDate;
	
	double manifestAmount=0.0;
	int sumPack=0;
	int numOrdered=0; 
	int cttNumber=0;
	
	int curRow=0;
	int endRow=0;
	int totalRecord=0;
	int currentPage = 1;
	
	int pageIndex=0;
	double maxPageIndex = 0;
	private String sendEmail = "";	
	private String formaPago = "";//AAP01
	private String reference = "";//AAP01
	private String docuType = "";//AAP02
	private String centroCostoSel = "";//AAP02
	private String centrosCosto = "";//AAP02
	private String filtroPor = "";
	private String msjErr = "";
	
	private ArrayList centrosCostoValue, centrosCostoLabel;//AAP02
	
	public  JavManifestGenerationForm(){
		//AccessLog.Log("Inside JavaManifestGenerationForm Constructor..");
	}
	//Setter and Getter Methods For JavManifestGenerationForm.
	
	public void setAvaiableCreditFlag(String ac){
		this.avaiableCreditFlag=ac;
	}
	public String getAvaiableCreditFlag(){
		return this.avaiableCreditFlag;
	}
	
	public void setAvaiableCredit(double ac){
		this.avaiableCredit=ac;
	}
	public double getAvaiableCredit(){
		return this.avaiableCredit;			
	}
	
	public void setBookedGuiaAmt(String bc){
		this.bookedGuiaAmt=bc;
	}
	public String getBookedGuiaAmt(){
		return this.bookedGuiaAmt;		
	}
	
	public void setCreditLimit(double cl){
		this.creditLimit=cl;
	}
	public double getCreditLimit(){
		return this.creditLimit;
	}
	
	public void setPreferedAddress(String addr){
		this.preferedAddress=addr;
	}
	
	public String getPreferedAddress(){
		
		return this.preferedAddress;
	}
	
	public void setCollectionTime(String time){
		this.collectionTime=time;
	}
	
	public String getCollectionTime(){
		return this.collectionTime;
	}
	
	public void setManifestNumber(String number){
		this.manifestNumber=number;
		
	}
	public String getManifestNumber(){
		return  this.manifestNumber;
	}
	
	public void setOperation(String opr){
		this.operation=opr;
	}
	
	public String getOperation(){
		return this.operation;
	}
	
	public void setTotalRecord(int totrec){
		this.totalRecord=totrec;
	}
	
	public int getTotalRecord(){
		return this.totalRecord;
	}
	
	public void setEndRow(int endrec){
		this.endRow =endrec;
		
	}
	
	public int getEndRow(){
		return this.endRow;
	}
	
	public void setCurRow(int currec){
		this.curRow =currec;
		
	}
	
	public int getCurRow(){
		return this.curRow;
	}
	
	public void setPageIndex(int pi){
		this.pageIndex=pi;
		
	}
	
	public int getPageIndex(){
		return this.pageIndex;
	}
	
	public void setNoDataFound(String nodata){
		this.noDataFound=nodata;
	}
	
	public String getNoDataFound(){
		return this.noDataFound;
	}
	
	public int getCurrentPage(){
		return currentPage;
	}
	public void setCurrentPage(int currentPage){
		this.currentPage = currentPage;
	}
	
	public double getMaxPageIndex(){
		return maxPageIndex;
	}
	public void setMaxPageIndex(double maxPageIndex){
		this.maxPageIndex = maxPageIndex;
	}
	
	public String getClickedItems(){
		return this.clickedItems;
	}
	
	public void setClickedItems(String clickedItems){
		this.clickedItems = clickedItems;
	}
	
	public void setDisableGenerate(String str){
		this.disableGenerate=str;
	}
	public String getDisableGenerate(){
		return this.disableGenerate;
	}
	
	public void setManifestAmount(double sumAmnt){
		this.manifestAmount=sumAmnt;
	}
	public double getManifestAmount(){
		return this.manifestAmount;
	}
	public void setSumPack(int sumpack){
		this.sumPack=sumpack;
	}
	public int getSumPack(){
		return this.sumPack;
	}
	
	public void setNumOrdered(int numorder){
		this.numOrdered=numorder;
	}
	public int getNumOrdered(){
		return this.numOrdered;
	}
	
	public void setCttNumber(int cno){
		this.cttNumber=cno;
	}
	public int getCttNumber(){
		return this.cttNumber;
	}	
	
	public void setManifestIssueDate(String iss){
		this.manifestIssueDate=iss;
	}
	public String getManifestIssueDate(){
		return this.manifestIssueDate;
	}
	
	public void setAddressCode(String addrcode){
		this.addressCode=addrcode;
	}
	public void setAddressType(String addrtype){
		this.addressType =addrtype;
	}
	public void setDoorNumber(String drno){
		this.doorNumber=drno;
	}
	public void setStreetName(String stname){
		this.streetName=stname;
	}
	public void setPhoneNumber(String pno){
		this.phoneNumber=pno;
	}
	public void setSuitNumber(String suitno){
		this.suitNumber=suitno;
	}
	public void setFloorNumber(String fno){
		this.floorNumber=fno;
	}
	public void setAddressLine1(String al1){
		this.addressLine1=al1;
	}
	public void setAddressLine2(String al2){
		this.addressLine2=al2;
	}
	public void setAddressLine3(String al3){
		this.addressLine3=al3;
	}
	public void setAddressLine4(String al4){
		this.addressLine4=al4;
	}
	public void setAddressLine5(String al5){
		this.addressLine5=al5;
	}
	public void setAddressLine6(String al6){
		this.addressLine6=al6;
	}
	public void setAddressLine7(String al7){
		this.addressLine7=al7;
	}	 
	public void setZipCode(String zc){
		this.zipCode=zc;
	}	
	
	public String getAddressCode(){
		return this.addressCode;
	}
	public String getAddressType(){
		return this.addressType;
	}
	public String getDoorNumber(){
		return this.doorNumber;
	}
	public String getStreetName(){
		return this.streetName;
	}
	public String getPhoneNumber(){
		return this.phoneNumber;
	}
	public String getSuitNumber(){
		return this.suitNumber;
			
	}
	public String getFloorNumber(){
		return this.floorNumber;
	}
	public String getAddressLine1(){
		return this.addressLine1;
	}
	public String getAddressLine2(){
		return this.addressLine2;
	}
	public String getAddressLine3(){
		return this.addressLine3;
	}
	public String getAddressLine4(){
		return this.addressLine4;
	}
	public String getAddressLine5(){
		return this.addressLine5;
	}
	public String getAddressLine6(){
		return this.addressLine6;
	}
	public String getAddressLine7(){
		return this.addressLine7;
	}	 
	public String getZipCode(){
		return this.zipCode;
	
	}	
		

	
	//Added by Sam.D.Jabeen on [31-May-2006] 

	public String getClientBranchLocType() {
		return clientBranchLocType;
	}

	public void setClientBranchLocType(String clientBranchLocType) {
		this.clientBranchLocType = clientBranchLocType;
	}

	public String getManifiestoType() {
		return manifiestoType;
	}

	public void setManifiestoType(String manifiestoType) {
		this.manifiestoType = manifiestoType;
	}

	//Eoc added by Sam.D.Jabeen
	
	public String getSendEmail() {
		return sendEmail;
	}

	public void setSendEmail(String sendEmail) {
		this.sendEmail = sendEmail;
	}

	public String getFormaPago() {//AAP01
		return formaPago;
	}

	public void setFormaPago(String formaPago) {//AAP01
		this.formaPago = formaPago;
	}

	public String getReference() {//AAP01
		return reference;
	}

	public void setReference(String reference) {//AAP01
		this.reference = reference;
	}	

	public String getDocuType() {
		return docuType;
	}

	public void setDocuType(String docuType) {
		this.docuType = docuType;
	}

	public String getCentroCostoSel() {
		return centroCostoSel;
	}

	public void setCentroCostoSel(String centroCostoSel) {
		this.centroCostoSel = centroCostoSel;
	}
	
	public String getCentrosCosto() {
		return centrosCosto;
	}

	public void setCentrosCosto(String centrosCosto) {
		this.centrosCosto = centrosCosto;
	}
	public ArrayList getCentrosCostoValue(){		
		if (this.centrosCostoValue == null) {
			this.centrosCostoValue = new ArrayList(5);
		}		
		return this.centrosCostoValue;
	}
	
	public void setCentrosCostoValue(String centrosCostoValue){
		if ( this.centrosCostoValue == null ){			
			this.centrosCostoValue = new ArrayList(5); 
			this.centrosCostoValue.add(centrosCostoValue);
		} 
		else{
			this.centrosCostoValue.add(centrosCostoValue);
		}
	}
	
	public ArrayList getCentrosCostoLabel(){
		if (this.centrosCostoLabel == null) {
			this.centrosCostoLabel = new ArrayList(3);
		}		
		return this.centrosCostoLabel;
	}
	
	public void setCentrosCostoLabel(String centrosCostoLabel){
		if ( this.centrosCostoLabel == null ){
			this.centrosCostoLabel = new ArrayList(5); 
			this.centrosCostoLabel.add(centrosCostoLabel);
		} 
		else{
			this.centrosCostoLabel.add(centrosCostoLabel);
		}
	}

	public String getFiltroPor() {
		return filtroPor;
	}

	public void setFiltroPor(String filtroPor) {
		this.filtroPor = filtroPor;
	}

	public String getMsjErr() {
		return msjErr;
	}

	public void setMsjErr(String msjErr) {
		this.msjErr = msjErr;
	}
	public String getPreferedAddressCode() {
		return preferedAddressCode;
	}

	public void setPreferedAddressCode(String preferedAddressCode) {
		this.preferedAddressCode = preferedAddressCode;
	}	
}