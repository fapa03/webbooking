/**
 * @author: kumaran
 * Fecha de Creaci�n: 
 * Compa��a: KUMARAN.
 * Descripci�n del programa: Beanform para pantalla de servicios en la consulta de manifiestos.
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
 * Descripci�n: Se agreg� variable formaPago para mantener el valor seleccionado en la consulta de manifiestos.
 * ------------------------------------------------------------------
 * Clave: 
 * Autor:
 * Fecha:
 * Descripci�n:
 * ------------------------------------------------------------------
 */

import java.util.ArrayList;

import org.apache.struts.action.ActionForm;

public class JavShipmentHistoryForm extends ActionForm{
	public JavShipmentHistoryForm(){
	}
	// For Print Option..
	String operation="";
	// for manifest sent
	int curIndex = 0;
	int endIndex = 0;
	int pageIndex = 0;
	int maxPageIndex = 0;
	int currentPage = 1;

	// for manifest not sent
	int curNSIndex = 0;
	int endNSIndex = 0;
	int pageNSIndex = 0;
	int maxPageNSIndex = 0;
	int currentNSPage = 1;

    // for deleting the record;
    String deleteNotSentGuiaNumber;
	
	//for getting the date
	String manifestDate;
	
	//for getting the value from the checkbox
	String genManifestNumber;
	private String formaPago = ""; //AAP01
	private String docuType = "";
	private String genManifest = "";//AAP02
	private ArrayList genManifestValue, genManifestLabel;//AAP02
	private String manifiestoType = "";
	private String filtroPor = "";
	private String preferedAddressCode = "";
	private String preferedAddress = "";
	
	private String flagManifestWE = "N"; //Se integra para poder visualiazar todas las guias WE del cliente 
	
	
	public void setOperation(String opr){
		this.operation = opr;
	}
	public String getOperation(){
		return this.operation;
	}
	
	// getter setter methods for Manifest sent
	public int getStartIndex(){
		return curIndex;
	}
	public void setStartIndex(int curIndex){
		this.curIndex = curIndex;
	}

	public int getEndIndex(){
		return endIndex;
	}
	public void setEndIndex(int endIndex){
		this.endIndex = endIndex;
	}

	public int getPageIndex(){
		return pageIndex;
	}

	public void setPageIndex(int pageIndex){
		this.pageIndex = pageIndex;
	}
	public int getMaxPageIndex(){
		return maxPageIndex;
	}
	public void setMaxPageIndex(double maxPageIndex){
		this.maxPageIndex =(int) Math.abs(maxPageIndex);
	}

	public int getCurrentPage(){
		return currentPage;
	}
	public void setCurrentPage(int currentPage){
		this.currentPage = currentPage;
	}
	
	//getter setter method for Manifest Not Sent
	public int getStartNSIndex(){
		return curNSIndex;			
	}
	public void setStartNSIndex(int curNSIndex){
		this.curNSIndex = curNSIndex;
	}
	
	public int getEndNSIndex(){
		return endNSIndex;
	}
	public void setEndNSIndex(int endNSIndex){
		this.endNSIndex = endNSIndex;
	}

	public int getPageNSIndex(){
		return pageNSIndex;
	}

	public void setPageNSIndex(int pageNSIndex){
		this.pageNSIndex = pageNSIndex;
	}
	public int getMaxPageNSIndex(){
		return maxPageNSIndex;
	}
	public void setMaxPageNSIndex(double maxPageNSIndex){
		this.maxPageNSIndex =(int) Math.abs(maxPageNSIndex);
	}

	public int getCurrentNSPage(){
		return currentNSPage;
	}
	public void setCurrentNSPage(int currentNSPage){
		this.currentNSPage = currentNSPage;
	}

    //getter setter method for deleting the guia number
    public String getDeleteGuiaNumber(){
        return deleteNotSentGuiaNumber;
    }

    public void setDeleteGuiaNumber(String deleteNotSentGuiaNumber){
        this.deleteNotSentGuiaNumber = deleteNotSentGuiaNumber;
    }
	
	//getter setter method for the date field
	
	public String getManifestDate(){
		return manifestDate;		
	}
	public void setManifestDate(String manifestDate){
        this.manifestDate = manifestDate;
	}
	
	// getter setter method for generating the guiadetails
	public String getGenManifestNumber(){
		return genManifestNumber;
	}
	public void setGenManifestNumber(String genManifestNumber){
		this.genManifestNumber = genManifestNumber;
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
	public String getGenManifest() {
		return genManifest;
	}
	public void setGenManifest(String genManifest) {
		this.genManifest = genManifest;
	}
	public ArrayList getGenManifestValue() {
		if (this.genManifestValue == null) {
			this.genManifestValue = new ArrayList(5);
		}		
		return genManifestValue;
	}
	public void setGenManifestValue(String genManifestValue) {		
		if ( this.genManifestValue == null ){			
			this.genManifestValue = new ArrayList(5); 
			this.genManifestValue.add(genManifestValue);
		} 
		else{
			this.genManifestValue.add(genManifestValue);
		}
	}
	public ArrayList getGenManifestLabel() {
		if (this.genManifestLabel == null) {
			this.genManifestLabel = new ArrayList(5);
		}		
		return genManifestLabel;		
	}
	public void setGenManifestLabel(String genManifestLabel) {
		if ( this.genManifestLabel == null ){			
			this.genManifestLabel = new ArrayList(5); 
			this.genManifestLabel.add(genManifestLabel);
		} 
		else{
			this.genManifestLabel.add(genManifestLabel);
		}
	}
	public String getManifiestoType() {
		return manifiestoType;
	}
	public void setManifiestoType(String manifiestoType) {		
		this.manifiestoType = manifiestoType;
	}
	public void setGenManifestValue(ArrayList genManifestValue) {
		this.genManifestValue = genManifestValue;
	}
	public void setGenManifestLabel(ArrayList genManifestLabel) {
		this.genManifestLabel = genManifestLabel;
	}
	public String getFiltroPor() {
		return filtroPor;
	}
	public void setFiltroPor(String filtroPor) {
		this.filtroPor = filtroPor;
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
	public String getFlagManifestWE() {
		return flagManifestWE;
	}
	public void setFlagManifestWE(String flagManifestWE) {
		this.flagManifestWE = flagManifestWE;
	}
	
	
}
