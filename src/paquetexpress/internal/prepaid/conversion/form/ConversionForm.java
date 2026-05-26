package paquetexpress.internal.prepaid.conversion.form;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.paquetexpress.www.webbooking.Documentacion.ShipTypeSEG;

import mx.com.paquetexpress.general.model.dto.BranchDetailDTOResponse;
public class ConversionForm extends ActionForm {

	String trackingNo="";
	String guiaNo = "";//aapww
	String userId="";
	String refNo="";
	String clientName="";
	String clientId="";
	String sessionId="";
	String branchId="";
	String branchUserId="";
	String fiscal1="",fiscal2="",fiscalcolonia1="",fiscalcolonia2="",fiscaltelefono="",fiscalrfc="";
	String fiscaladdresscode="";
	String orgioncode="",orgionbranch="",orgienclave="",orgiennombre="",orgien1="",orgien2="",orgiencolonia1="";
	String orgiencolonia2="",orgienrfc="",orgientelefono="";	
	String destcode="",destbranch="",destclave="",destnombre="",dest1="",dest2="",destcolonia1="";
	String destcolonia2="",destrfc="",desttelefono="",dest_am_gety_code="";
	String destSite="",horasEntregaOcu="",horasEntregaEad="";
	
	String destSiteName="";
	String weight="";
	String volume="";
	double volL=32.0;//JSA01
	double volH=1.0;//JSA01
	double volW=24.0;//JSA01
	String numberOfGuias="";
	String zone="";
	String refClientName="";
	String refRFC="";
	String totalGuiaAmount="";
	String shippingType[];
	String deliveryType;// Added on 14/07/2010 - For kitsId : 70454
	
	// Agregado el 02/Ene/2012 Para zona extendida en sobres
	String ext="";
	private String destRefDom = "", checkRefDir = "", checkTelDir = "", reqAcuse = "",destBranch = "";//AAP01
	private String reqAcuseXT;
	private String operadorLogistico = "";//aap01
	private String actType;
	private String actCheck="";
	private String act="";
	
	private boolean eMailOrigCheck = false;//AAP02
	private String eMailOrigText = "";//AAP02
	private boolean eMailDestCheck = false;//AAP02
	private String eMailDestText = "";//AAP02
	private String allowNewInvoice="";
	private String centrosCosto = "";
	private ArrayList centrosCostoValue, centrosCostoLabel;//AAP08
	private String clientHasLocalCredit = "";//AAP08
	
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
	private String clickedItems = "";
	private String banCerrar = "";
	private String reLoad = "";
	private String facturaMod = "";	
	private String filtrarPor = "";
	private String brncVrtl = "";
	private String comments = "";
	private String reference = "";
	List<ShipTypeSEG> shippingTypeSEG;
	//Cuando ya se paso de la hora de corte y ocupa confirmar si se va a enviar pero con el servicio del segundo dia (2D).
	private boolean neededConfirmationService2D = false;//JAS01
	//Esta variable es para cuando el usuario confirma el servicio 2D
	private boolean confirmationService2D;//JAS01
	private List<ShipTypeSEG> shippingTypeSEGALL;//JSA01
	private String shipTypeSEGChange;//JSA01
	private String shipTypeSEGDescChange;//JSA01
	private String shipTypeSEGPPChange;//JSA01
	private String msjConfirmationService;
	private String alertNOConfirmationService;
	double volLMAX = 32.0;//JSA01
	double volHMAX = 1.0;//JSA01
	double volWMAX = 24.0;//JSA01
	double pesoMAX = 60.0;//JSA01
	private String shipType = "";//JSA01
	private String listReferences = "";
	private String flagValidRefrClnt = "";
    private String cmpyId;//JSA03
	private Double factorDividorPesoVol= 6000D;//JSA04
	private int cantPesoVolDecimales = 6;//JSA04
    private String volumeOriginal;//JSA04    
    private String locationType;//AAP20
    private String borderBranch = "";//AAP20
    private Double volLMin;
	private Double volWMin;
	private Double volHMin;
	private Double volMin;
	private Double wghtMin;
    
    /*Variables para validaci?n sucursal ocurre a selecci?n*/
	private Boolean opcOcurre = false;
	private String brnchOcurre = "";
	private ArrayList<BranchDetailDTOResponse> filteredBrnch = new ArrayList<BranchDetailDTOResponse>();
	private String defaultBrnchAddr = "";
	
	/* Variable para implementaci?n Complemento Carta Porte */
	private String productIdSat = "";
	private String productDescSat = "";
    
	/* Validaci?n de modificaci?n de rastreo */
	private String modFormnoFlag = "N";
	private String extOrgnValue = "";
	
	/* Validación de número de pedimento */
	private String pedinumber = "";
	private String custagent = "";
	private String borderbranchcheck = "";
	private String msgInfo = "";
	private String lastWrongPediNum = "";
	
	private String assignedBranch = "",destClaveAux = "", destNombreAux = "";
	/**
	 * @return the extOrgnValue
	 */
	public String getExtOrgnValue() {
		return extOrgnValue;
	}
	/**
	 * @param extOrgnValue the extOrgnValue to set
	 */
	public void setExtOrgnValue(String extOrgnValue) {
		this.extOrgnValue = extOrgnValue;
	}
	public String getExt()
	{
		return this.ext;
	}
	public void setExt(String ext)
	{
		this.ext=ext;
	}
	String extCheck="";
	
	public void setExtCheck(String extCheck)
	{
		this.extCheck=extCheck;
	}
	public String getExtCheck()
	{
		return this.extCheck;
	}
	String guiaOld="";
	public void setGuiaOld(String guiaOld)
	{
		this.guiaOld=guiaOld;
	}
	public String getGuiaOld()
	{
		return this.guiaOld;
	}
	String aceptarNuevosCargos="";
	public void setAceptarNuevosCargos(String aceptarNuevosCargos)
	{
		this.aceptarNuevosCargos=aceptarNuevosCargos;
	}
	public String getAceptarNuevosCargos()
	{
		return this.aceptarNuevosCargos;
	}
	
	String nuevoValorExt="";
	public void setNuevoValorExt(String nuevoValorExt)
	{
		this.nuevoValorExt=nuevoValorExt;
	}
	public String getNuevoValorExt()
	{
		return this.nuevoValorExt;
	}
	
	String valorIVA="";
	public void setValorIVA(String valorIVA)
	{
		this.valorIVA=valorIVA;
	}
	public String getValorIVA()
	{
		return this.valorIVA;
	}
	
	String valorRetAmount = "";
	public void setValorRetAmount(String valorRetAmount)
	{
		this.valorRetAmount = valorRetAmount;
	}
	public String getValorRetAmount()
	{
		return this.valorRetAmount;
	}
	
	String clientType="";
	public String getClientType() {
		return clientType;
	}
	public void setClientType(String clientType) {
		this.clientType = clientType;
	}
	
	String isExtendedZone="";
	
	public void setIsExtendedZone(String isExtendedZone)
	{
		this.isExtendedZone=isExtendedZone;
	}
	public String getIsExtendedZone()
	{
		return this.isExtendedZone;
	}
	String guiaHasErrors="";
	String guiaErrorType="";
	public void setGuiaHasErrors(String guiaHasErrors)
	{
		this.guiaHasErrors=guiaHasErrors;
	}
	public String getGuiaHasErrors()
	{
		return this.guiaHasErrors;
	}
	
	public void setGuiaErrorType(String guiaErrorType)
	{
		this.guiaErrorType=guiaErrorType;
	}
	public String getGuiaErrorType()
	{
		return this.guiaErrorType;
	}
	// ******************************************************
	
	public String getDeliveryType() {
		return deliveryType;
	}
	public void setDeliveryType(String deliveryType) {
		this.deliveryType = deliveryType;
	}
	boolean checkOriginAddress=false;	
	String currentTask="start";
	String payMode="";
	Hashtable refNos=new Hashtable();
	String valordeclarado="";
	String cobertura="";
	String content="";
	String oldDestSite="";
	String oldDestSiteName="";
	String packType="";
	
	/* (non-Javadoc)
	 * @see org.apache.struts.action.ActionForm#reset(org.apache.struts.action.ActionMapping, javax.servlet.ServletRequest)
	 */
	
	
	public String getPackType() {
		return packType;
	}
	public void setPackType(String packType) {
		this.packType = packType;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCobertura() {
		return cobertura;
	}
	public void setCobertura(String cobertura) {
		this.cobertura = cobertura;
	}
	public String getValordeclarado() {
		return valordeclarado;
	}
	public void setValordeclarado(String valordeclarado) {
		this.valordeclarado = valordeclarado;
	}
	public String getNumberOfGuias() {
		return numberOfGuias;
	}
	public void setNumberOfGuias(String numberOfGuias) {
		this.numberOfGuias = numberOfGuias;
	}
	public String getVolume() {
		volume=getVolumenCalculado(volL, volH, volW);
		return volume;
	}
	public void setVolume(String volume) {
		this.volume = volume;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String wight) {
		this.weight = wight;
	}
	public String getZone() {
		return zone;
	}
	public void setZone(String zone) {
		this.zone = zone;
	}
	public String getBranchId() {
		return branchId;
	}
	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}
	public String getBranchUserId() {
		return branchUserId;
	}
	public void setBranchUserId(String branchUserId) {
		this.branchUserId = branchUserId;
	}
	public String getFiscal1() {
		return fiscal1;
	}
	public void setFiscal1(String fiscal1) {
		this.fiscal1 = fiscal1;
	}
	public String getFiscal2() {
		return fiscal2;
	}
	public void setFiscal2(String fiscal2) {
		this.fiscal2 = fiscal2;
	}
	public String getFiscaladdresscode() {
		return fiscaladdresscode;
	}
	public void setFiscaladdresscode(String fiscaladdresscode) {
		this.fiscaladdresscode = fiscaladdresscode;
	}
	public String getFiscalcolonia1() {
		return fiscalcolonia1;
	}
	public void setFiscalcolonia1(String fiscalcolonia1) {
		this.fiscalcolonia1 = fiscalcolonia1;
	}
	public String getFiscalcolonia2() {
		return fiscalcolonia2;
	}
	public void setFiscalcolonia2(String fiscalcolonia2) {
		this.fiscalcolonia2 = fiscalcolonia2;
	}
	public String getFiscaltelefono() {
		return fiscaltelefono;
	}
	public void setFiscaltelefono(String fiscaltelefono) {
		this.fiscaltelefono = fiscaltelefono;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getOrgien1() {
		return orgien1;
	}
	public void setOrgien1(String orgien1) {
		this.orgien1 = orgien1;
	}
	public String getOrgien2() {
		return orgien2;
	}
	public void setOrgien2(String orgien2) {
		this.orgien2 = orgien2;
	}
	public String getOrgienclave() {
		return orgienclave;
	}
	public void setOrgienclave(String orgienclave) {
		this.orgienclave = orgienclave;
	}
	public String getOrgiencolonia1() {
		return orgiencolonia1;
	}
	public void setOrgiencolonia1(String orgiencolonia1) {
		this.orgiencolonia1 = orgiencolonia1;
	}
	public String getOrgiencolonia2() {
		return orgiencolonia2;
	}
	public void setOrgiencolonia2(String orgiencolonia2) {
		this.orgiencolonia2 = orgiencolonia2;
	}
	public String getOrgiennombre() {
		return orgiennombre;
	}
	public void setOrgiennombre(String orgiennombre) {
		this.orgiennombre = orgiennombre;
	}
	public String getOrgienrfc() {
		return orgienrfc;
	}
	public void setOrgienrfc(String orgienrfc) {
		this.orgienrfc = orgienrfc;
	}
	public String getOrgientelefono() {
		return orgientelefono;
	}
	public void setOrgientelefono(String orgientelefono) {
		this.orgientelefono = orgientelefono;
	}
	public String getOrgionbranch() {
		return orgionbranch;
	}
	public void setOrgionbranch(String orgionbranch) {
		this.orgionbranch = orgionbranch;
	}
	public String getOrgioncode() {
		return orgioncode;
	}
	public void setOrgioncode(String orgioncode) {
		this.orgioncode = orgioncode;
	}
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getRefClientName() {
		return refClientName;
	}
	public void setRefClientName(String refClientName) {
		this.refClientName = refClientName;
	}
	public String getRefRFC() {
		return refRFC;
	}
	public void setRefRFC(String refRFC) {
		this.refRFC = refRFC;
	}
	public boolean isCheckOriginAddress() {
		return checkOriginAddress;
	}
	public void setCheckOriginAddress(boolean checkOriginAddress) {
		this.checkOriginAddress = checkOriginAddress;
	}
	public String getCurrentTask() {
		return currentTask;
	}
	public void setCurrentTask(String currentTask) {
		this.currentTask = currentTask;
	}
	public String getFiscalrfc() {
		return fiscalrfc;
	}
	public void setFiscalrfc(String fiscalrfc) {
		this.fiscalrfc = fiscalrfc;
	}
	public String getRefNo() {
		return refNo;
	}
	public void setRefNo(String refNo) {
		this.refNo = refNo;
	}
	public Hashtable getRefNos() {
		return refNos;
	}
	public void setRefNos(Hashtable refNos) {
		this.refNos = refNos;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public String getTotalGuiaAmount() {
		return totalGuiaAmount;
	}
	public void setTotalGuiaAmount(String totalGuiaAmount) {
		this.totalGuiaAmount = totalGuiaAmount;
	}
	public String[] getShippingType() {
		return shippingType;
	}
	public void setShippingType(String shippingType[]) {
		this.shippingType = shippingType;
	}
	public String getPayMode() {
		return payMode;
	}
	public void setPayMode(String payMode) {
		this.payMode = payMode;
	}
	public String getTrackingNo() {
		return trackingNo;
	}
	public void setTrackingNo(String trackingNo) {
		this.trackingNo = trackingNo;
	}
	public String getDest1() {
		return dest1;
	}
	public void setDest1(String dest1) {
		this.dest1 = dest1;
	}
	public String getDest2() {
		return dest2;
	}
	public void setDest2(String dest2) {
		this.dest2 = dest2;
	}
	public String getDestbranch() {
		return destbranch;
	}
	public void setDestbranch(String destbranch) {
		this.destbranch = destbranch;
	}
	public String getDestclave() {
		return destclave;
	}
	public void setDestclave(String destclave) {
		this.destclave = destclave;
	}
	public String getDestcode() {
		return destcode;
	}
	public void setDestcode(String destcode) {
		this.destcode = destcode;
	}
	public String getDestcolonia1() {
		return destcolonia1;
	}
	public void setDestcolonia1(String destcolonia1) {
		this.destcolonia1 = destcolonia1;
	}
	public String getDestcolonia2() {
		return destcolonia2;
	}
	public void setDestcolonia2(String destcolonia2) {
		this.destcolonia2 = destcolonia2;
	}
	public String getDestnombre() {
		return destnombre;
	}
	public void setDestnombre(String destnombre) {
		this.destnombre = destnombre;
	}
	public String getDestrfc() {
		return destrfc;
	}
	public void setDestrfc(String destrfc) {
		this.destrfc = destrfc;
	}
	public String getDesttelefono() {
		return desttelefono;
	}
	public void setDesttelefono(String desttelefono) {
		this.desttelefono = desttelefono;
	}
	public String getDestSite() {
		return destSite;
	}
	public void setDestSite(String destSite) {
		this.destSite = destSite;
	}
	public String getDestSiteName() {
		return destSiteName;
	}
	public void setDestSiteName(String destSiteName) {
		this.destSiteName = destSiteName;
	}
	public void reset(ActionMapping arg0, HttpServletRequest arg1) {
	
		trackingNo="";
		userId="";
		refNo="";
		clientName="";
		clientId="";
		sessionId="";
		branchId="";
		branchUserId="";
		fiscal1="";
		fiscal2="";fiscalcolonia1="";fiscalcolonia2="";fiscaltelefono="";fiscalrfc="";
		fiscaladdresscode="";
		orgioncode="";orgionbranch="";orgienclave="";orgiennombre="";orgien1="";orgien2="";orgiencolonia1="";
		orgiencolonia2="";orgienrfc="";orgientelefono="";	
		destcode="";destbranch="";destclave="";destnombre="";dest1="";dest2="";destcolonia1="";
		destcolonia2="";destrfc="";desttelefono="";
		destSite="";

		destSiteName="";
		weight="";
		volume="";
		numberOfGuias="";
		zone="";
		refClientName="";
		refRFC="";
		totalGuiaAmount="";
		currentTask="start";
		payMode="";
		
		valordeclarado="";
		cobertura="";

		facturaMod="";
		clickedItems = "";
		reLoad = "";
		guiaNo = "";
		centrosCosto = "";
		filtrarPor = "";
		brncVrtl = "";
		deliveryType = "";
		comments = "";
		reference = "";
		listReferences = "";
		flagValidRefrClnt = "";
		
		locationType = "";//AAP20
	    borderBranch = "";//AAP20
		super.reset(arg0, arg1);
}
	public String getOldDestSiteName() {
		return oldDestSiteName;
	}
	public void setOldDestSiteName(String oldDestSiteName) {
		this.oldDestSiteName = oldDestSiteName;
	}
	public String getOldDestSite() {
		return oldDestSite;
	}
	public void setOldDestSite(String oldDestSite) {
		this.oldDestSite = oldDestSite;
	}
	// Agregado 18-Oct-2011  C.Solano CenitSoft/EnlaceDise?o
	
	
	String ead="";
	String rad="";
	
	public void setEad(String ead)
	{
		this.ead=ead;
	}
	public String getEad()
	{
		return this.ead;
	}
	public void setRad(String rad)
	{
		this.rad=rad;
	}
	public String getRad()
	{
		return this.rad;
	}
	
	// Modificacion 23-01-2012 Para agregar Serie Caja
	// A la documentacion de guias de prepago
	
	String serieCaja ="";
	public String getSerieCaja() {
		return serieCaja;
	}
	public void setSerieCaja(String serieCaja) {
		this.serieCaja = serieCaja;
	}
	
	ArrayList listaCajasValue;

	public ArrayList getListaCajasValue() {
		return listaCajasValue;
	}
	public void setListaCajasValue(ArrayList listaCajasValue) {
		this.listaCajasValue = listaCajasValue;
	}

	ArrayList listaCajasType;

	public ArrayList getListaCajasType() {
		return listaCajasType;
	}
	public void setListaCajasType(ArrayList listaCajasType) {
		this.listaCajasType = listaCajasType;
	}
	
	public String getDest_am_gety_code() {
		return dest_am_gety_code;
	}
	
	public void setDest_am_gety_code(String dest_am_gety_code) {
		this.dest_am_gety_code = dest_am_gety_code;
	}
	
	public String getHorasEntregaOcu() {
		return horasEntregaOcu;
	}
	public void setHorasEntregaOcu(String horasEntregaOcu) {
		this.horasEntregaOcu = horasEntregaOcu;
	}
	public String getHorasEntregaEad() {
		return horasEntregaEad;
	}
	public void setHorasEntregaEad(String horasEntregaEad) {
		this.horasEntregaEad = horasEntregaEad;
	}
	public String getDestRefDom() {
		return destRefDom;
	}
	public void setDestRefDom(String destRefDom) {
		this.destRefDom = destRefDom;
	}
	public String getCheckRefDir() {
		return checkRefDir;
	}
	public void setCheckRefDir(String checkRefDir) {
		this.checkRefDir = checkRefDir;
	}
	public String getCheckTelDir() {
		return checkTelDir;
	}
	public void setCheckTelDir(String checkTelDir) {
		this.checkTelDir = checkTelDir;
	}
	public String getReqAcuse() {
		return reqAcuse;
	}
	public void setReqAcuse(String reqAcuse) {
		this.reqAcuse = reqAcuse;
	}
	public String getOperadorLogistico() {
		return operadorLogistico;
	}
	public void setOperadorLogistico(String operadorLogistico) {
		this.operadorLogistico = operadorLogistico;
	}
	public String getActType() {
		return actType;
	}
	public void setActType(String actType) {
		this.actType = actType;
	}
	public String getActCheck() {
		return actCheck;
	}
	public void setActCheck(String actCheck) {
		this.actCheck = actCheck;
	}
	public String getAct() {
		return act;
	}
	public void setAct(String act) {
		this.act = act;
	}
	public String getDestBranch() {
		return destBranch;
	}
	public void setDestBranch(String destBranch) {
		this.destBranch = destBranch;
	}
	/*public String geteMailOrigCheck() {
		return eMailOrigCheck;
	}
	public void seteMailOrigCheck(String eMailOrigCheck) {
		this.eMailOrigCheck = eMailOrigCheck;
	}*/
	
	public String geteMailOrigText() {
		return eMailOrigText;
	}
	public boolean iseMailOrigCheck() {
		return eMailOrigCheck;
	}
	public void seteMailOrigCheck(boolean eMailOrigCheck) {
		this.eMailOrigCheck = eMailOrigCheck;
	}
	public void seteMailOrigText(String eMailOrigText) {
		this.eMailOrigText = eMailOrigText;
	}
	/*public String geteMailDestCheck() {
		return eMailDestCheck;
	}
	public void seteMailDestCheck(String eMailDestCheck) {
		this.eMailDestCheck = eMailDestCheck;
	}*/

	public String geteMailDestText() {
		return eMailDestText;
	}
	public boolean iseMailDestCheck() {
		return eMailDestCheck;
	}
	public void seteMailDestCheck(boolean eMailDestCheck) {
		this.eMailDestCheck = eMailDestCheck;
	}
	public void seteMailDestText(String eMailDestText) {
		this.eMailDestText = eMailDestText;
	}	
	public String getAllowNewInvoice() {
		return allowNewInvoice;
	}
	public void setAllowNewInvoice(String allowNewInvoice) {
		this.allowNewInvoice = allowNewInvoice;
	}
	public String getGuiaNo() {
		return guiaNo;
	}
	public void setGuiaNo(String guiaNo) {
		this.guiaNo = guiaNo;
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
	public String getClientHasLocalCredit() {
		return clientHasLocalCredit;
	}
	public void setClientHasLocalCredit(String clientHasLocalCredit) {
		this.clientHasLocalCredit = clientHasLocalCredit;
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
	public String getClickedItems() {
		return clickedItems;
	}
	public void setClickedItems(String clickedItems) {
		this.clickedItems = clickedItems;
	}
	public String getBanCerrar() {
		return banCerrar;
	}
	public void setBanCerrar(String banCerrar) {
		this.banCerrar = banCerrar;
	}
	public void setCentrosCostoValue(ArrayList centrosCostoValue) {
		this.centrosCostoValue = centrosCostoValue;
	}
	public void setCentrosCostoLabel(ArrayList centrosCostoLabel) {
		this.centrosCostoLabel = centrosCostoLabel;
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
	public String getReLoad() {
		return reLoad;
	}
	public void setReLoad(String reLoad) {
		this.reLoad = reLoad;
	}
	public String getFacturaMod() {
		return facturaMod;
	}
	public void setFacturaMod(String facturaMod) {
		this.facturaMod = facturaMod;
	}
	public String getFiltrarPor() {
		return filtrarPor;
	}
	public void setFiltrarPor(String filtrarPor) {
		this.filtrarPor = filtrarPor;
	}
	public String getBrncVrtl() {
		return brncVrtl;
	}
	public void setBrncVrtl(String brncVrtl) {
		this.brncVrtl = brncVrtl;
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
	public String getReqAcuseXT() {
		return reqAcuseXT;
	}
	public void setReqAcuseXT(String reqAcuseXT) {
		this.reqAcuseXT = reqAcuseXT;
	}	

	public double getVolL() {
		return volL;
	}
	public void setVolL(double volL) {
		this.volL = volL;
	}
	public double getVolH() {
		return volH;
	}
	public void setVolH(double volH) {
		this.volH = volH;
	}
	public double getVolW() {
		return volW;
	}
	public void setVolW(double volW) {
		this.volW = volW;
	}
	public boolean isNeededConfirmationService2D() {
		return neededConfirmationService2D;
	}
	public void setNeededConfirmationService2D(boolean neededConfirmationService2D) {
		this.neededConfirmationService2D = neededConfirmationService2D;
	}
	public boolean isConfirmationService2D() {
		return confirmationService2D;
	}
	public void setConfirmationService2D(boolean confirmationService2D) {
		this.confirmationService2D = confirmationService2D;
	}
	public List<ShipTypeSEG> getShippingTypeSEGALL() {
		return shippingTypeSEGALL;
	}
	public void setShippingTypeSEGALL(List<ShipTypeSEG> shippingTypeSEGALL) {
		this.shippingTypeSEGALL = shippingTypeSEGALL;
	}
	public String getShipTypeSEGChange() {
		return shipTypeSEGChange;
	}
	public void setShipTypeSEGChange(String shipTypeSEGChange) {
		this.shipTypeSEGChange = shipTypeSEGChange;
	}
	public String getShipTypeSEGDescChange() {
		return shipTypeSEGDescChange;
	}
	public void setShipTypeSEGDescChange(String shipTypeSEGDescChange) {
		this.shipTypeSEGDescChange = shipTypeSEGDescChange;
	}
	public String getShipTypeSEGPPChange() {
		return shipTypeSEGPPChange;
	}
	public void setShipTypeSEGPPChange(String shipTypeSEGPPChange) {
		this.shipTypeSEGPPChange = shipTypeSEGPPChange;
	}
	public String getMsjConfirmationService() {
		return msjConfirmationService;
	}
	public void setMsjConfirmationService(String msjConfirmationService) {
		this.msjConfirmationService = msjConfirmationService;
	}
	public String getAlertNOConfirmationService() {
		return alertNOConfirmationService;
	}
	public void setAlertNOConfirmationService(String alertNOConfirmationService) {
		this.alertNOConfirmationService = alertNOConfirmationService;
	}
	public double getVolLMAX() {
		return volLMAX;
	}
	public void setVolLMAX(double volLMAX) {
		this.volLMAX = volLMAX;
	}
	public double getVolHMAX() {
		return volHMAX;
	}
	public void setVolHMAX(double volHMAX) {
		this.volHMAX = volHMAX;
	}
	public double getVolWMAX() {
		return volWMAX;
	}
	public void setVolWMAX(double volWMAX) {
		this.volWMAX = volWMAX;
	}	

	public List<ShipTypeSEG> getShippingTypeSEG() {
		return shippingTypeSEG;
	}
	public void setShippingTypeSEG(List<ShipTypeSEG> shippingTypeSEG) {
		this.shippingTypeSEG = shippingTypeSEG;
	}
	public double getPesoMAX() {
		return pesoMAX;
	}
	public void setPesoMAX(double pesoMAX) {
		this.pesoMAX = pesoMAX;
	}
	public String getShipType() {
		return shipType;
	}
	public void setShipType(String shipType) {
		this.shipType = shipType;
	}
	private String getVolumenCalculado(Double volL, Double volH, Double volW){
		BigDecimal volumen = new BigDecimal(((volH / 100) * (volL / 100) * (volW / 100))).setScale(cantPesoVolDecimales, BigDecimal.ROUND_HALF_EVEN);
		//System.out.println("volumen "+volumen.toString());
		return volumen.toString();
	}
	
	public String getListReferences() {
		return listReferences;
	}

	public void setListReferences(String listReferences) {
		this.listReferences = listReferences;
	}

	public String getFlagValidRefrClnt() {
		return flagValidRefrClnt;
	}

	public void setFlagValidRefrClnt(String flagValidRefrClnt) {
		this.flagValidRefrClnt = flagValidRefrClnt;
	}
	public String getCmpyId() {
		return cmpyId;
	}
	public void setCmpyId(String cmpyId) {
		this.cmpyId = cmpyId;
	}
	public int getCantPesoVolDecimales() {
		return cantPesoVolDecimales;
	}
	public void setCantPesoVolDecimales(int cantPesoVolDecimales) {
		this.cantPesoVolDecimales = cantPesoVolDecimales;
	}
	public String getVolumeOriginal() {
		return volumeOriginal;
	}
	public void setVolumeOriginal(String volumeOriginal) {
		this.volumeOriginal = volumeOriginal;
	}
	public Double getFactorDividorPesoVol() {
		return factorDividorPesoVol;
	}
	public void setFactorDividorPesoVol(Double factorDividorPesoVol) {
		this.factorDividorPesoVol = factorDividorPesoVol;
	}
	public String getLocationType() {
		return locationType;
	}
	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}
	public String getBorderBranch() {
		return borderBranch;
	}
	public void setBorderBranch(String borderBranch) {
		this.borderBranch = borderBranch;
	}
	public Boolean getOpcOcurre() {
		return opcOcurre;
	}
	public void setOpcOcurre(Boolean opcOcurre) {
		this.opcOcurre = opcOcurre;
	}
	public String getBrnchOcurre() {
		return brnchOcurre;
	}
	public void setBrnchOcurre(String sucursalesOcurre) {
		this.brnchOcurre = sucursalesOcurre;
	}
	/**
	 * @return the filteredBrnch
	 */
	public ArrayList<BranchDetailDTOResponse> getFilteredBrnch() {
		if ( this.filteredBrnch == null ){
			this.filteredBrnch = new ArrayList<BranchDetailDTOResponse>(3);
		} 
		return filteredBrnch;
	}

	/**
	 * @param filteredBrnch the filteredBrnch to set
	 */
	public void setFilteredBrnch(BranchDetailDTOResponse filteredBrnch) {
		if ( this.filteredBrnch == null ){
			this.filteredBrnch = new ArrayList<BranchDetailDTOResponse>(5); 
		} 
		else{
			this.filteredBrnch.add(filteredBrnch);
		}
	}
	
	/**
	 * @return the defaultAddr
	 */
	public String getDefaultBrnchAddr() {
		return defaultBrnchAddr;
	}

	/**
	 * @param defaultAddr the defaultAddr to set
	 */
	public void setDefaultBrnchAddr(String defaultAddr) {
		this.defaultBrnchAddr = defaultAddr;
	}
	
	/**
	 * @return the productIdSat
	 */
	public String getProductIdSat() {
		return productIdSat;
	}
	/**
	 * @param productIdSat the productIdSat to set
	 */
	public void setProductIdSat(String productIdSat) {
		this.productIdSat = productIdSat;
	}
	/**
	 * @return the productDescSat
	 */
	public String getProductDescSat() {
		return productDescSat;
	}
	/**
	 * @param productDescSat the productDescSat to set
	 */
	public void setProductDescSat(String productDescSat) {
		this.productDescSat = productDescSat;
	}
	/**
	 * @return the modFormnoFlag
	 */
	public String getModFormnoFlag() {
		return modFormnoFlag;
	}
	/**
	 * @param modFormnoFlag the modFormnoFlag to set
	 */
	public void setModFormnoFlag(String modFormnoFlag) {
		this.modFormnoFlag = modFormnoFlag;
	}
	/**
	 * @return the pedinumber
	 */
	public String getPedinumber() {
		return pedinumber;
	}
	/**
	 * @param pedinumber the pedinumber to set
	 */
	public void setPedinumber(String pedinumber) {
		this.pedinumber = pedinumber;
	}
	/**
	 * @return the custagent
	 */
	public String getCustagent() {
		return custagent;
	}
	/**
	 * @param custagent the custagent to set
	 */
	public void setCustagent(String custagent) {
		this.custagent = custagent;
	}
	/**
	 * @return the borderbranchcheck
	 */
	public String getBorderbranchcheck() {
		return borderbranchcheck;
	}
	/**
	 * @param borderbranchcheck the borderbranchcheck to set
	 */
	public void setBorderbranchcheck(String borderbranchcheck) {
		this.borderbranchcheck = borderbranchcheck;
	}
	/**
	 * @return the msgInfo
	 */
	public String getMsgInfo() {
		return msgInfo;
	}
	/**
	 * @param msgInfo the msgInfo to set
	 */
	public void setMsgInfo(String mesgInfo) {
		this.msgInfo = mesgInfo;
	}
	/**
	 * @return the lastWrongPediNum
	 */
	public String getLastWrongPediNum() {
		return lastWrongPediNum;
	}
	/**
	 * @param lastWrongPediNum the lastWrongPediNum to set
	 */
	public void setLastWrongPediNum(String lastWrongPediNum) {
		this.lastWrongPediNum = lastWrongPediNum;
	}
	public Double getVolLMin() {
		return volLMin;
	}
	public void setVolLMin(Double volLMin) {
		this.volLMin = volLMin;
	}
	public Double getVolWMin() {
		return volWMin;
	}
	public void setVolWMin(Double volWMin) {
		this.volWMin = volWMin;
	}
	public Double getVolHMin() {
		return volHMin;
	}
	public void setVolHMin(Double volHMin) {
		this.volHMin = volHMin;
	}
	public Double getVolMin() {
		return volMin;
	}
	public void setVolMin(Double volMin) {
		this.volMin = volMin;
	}
	public Double getWghtMin() {
		return wghtMin;
	}
	public void setWghtMin(Double wghtMin) {
		this.wghtMin = wghtMin;
	}
	

    /**
     * @return String return the assignedBranch
     */
    public String getAssignedBranch() {
        return assignedBranch;
    }

    /**
     * @param assignedBranch the assignedBranch to set
     */
    public void setAssignedBranch(String assignedBranch) {
        this.assignedBranch = assignedBranch;
    }
	public String getDestClaveAux() {
		return destClaveAux;
	}
	public void setDestClaveAux(String destClaveAux) {
		this.destClaveAux = destClaveAux;
	}
	public String getDestNombreAux() {
		return destNombreAux;
	}
	public void setDestNombreAux(String destNombreAux) {
		this.destNombreAux = destNombreAux;
	}

    
}
