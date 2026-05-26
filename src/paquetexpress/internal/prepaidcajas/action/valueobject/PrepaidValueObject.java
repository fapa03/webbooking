package paquetexpress.internal.prepaidcajas.action.valueobject;

import java.util.ArrayList;
import java.util.List;

import com.paquetexpress.www.webbooking.Documentacion.ShipTypeSEG;
public class PrepaidValueObject {
	String acusederecibo="ACK-N";
	String formNo="";
	String tracNo="";
	/**
	 * @return Returns the tracNo.
	 */
	public String getTracNo() {
		return tracNo;
	}
	/**
	 * @param tracNo The tracNo to set.
	 */
	public void setTracNo(String tracNo) {
		this.tracNo = tracNo;
	}
	/**
	 * @return Returns the formNo.
	 */
	public String getFormNo() {
		return formNo;
	}
	/**
	 * @param formNo The formNo to set.
	 */
	public void setFormNo(String formNo) {
		this.formNo = formNo;
	}
	String salesmanId="";
	/**
	 * @return Returns the salesmanId.
	 */
	public String getSalesmanId() {
		return salesmanId;
	}
	/**
	 * @param salesmanId The salesmanId to set.
	 */
	public void setSalesmanId(String salesmanId) {
		this.salesmanId = salesmanId;
	}
	String acklabels="";
	String userId="";
	String refNo="";
	String clientName="";
	String sessionId="";
	String clientId="";
	String branchId="";
	String branchUserId="";
	String	fiscal1="",fiscal2="",fiscalcolonia1="",fiscalcolonia2="",fiscaltelefono="";
	String	fiscaladdresscode="";
	String	orgioncode="",orgionbranch="",orgienclave="",orgiennombre="",orgien1="",orgien2="",orgiencolonia1="";
	String	orgiencolonia2="",orgienrfc="",orgientelefono="";
	String	destcode="",destbranch="",destclave="",destnombre="",dest1="",dest2="",destcolonia1="";
	String	destcolonia2="",destrfc="",desttelefono="", dest_am_gety_code="";
	String destSite="";
	String ackAmt="";
	String destSiteName="";
	String weight="0";
	String volume="0";
	String numberOfGuias="";
	String zone="";
	String refClientName="";
	String refRFC="";
	String totalGuiaAmount="";
	String shippingType[]=new String[100];
	String deliveryType;// Added on 14/07/2010 - For kitsId : 70454
	
	boolean checkOriginAddress=false;	
	String currentTask="";
	String valordeclarado="0";
	String cobertura="";
	ArrayList insurancetype=new ArrayList();
	ArrayList insurancetypelabel=new ArrayList();
	String insuranceSubTotal="0";
	String insTax="0";
	String insTaxRet="0";
	String insInformation="0";
	String discount="0";
	String content="";
	String packType="";
	String tarifa="";
	private String shipType = "";
	List<ShipTypeSEG> shippingTypeSEG;//JSA01
	private String shipTypeSEGCurrent = "";//JSA01
	List<ShipTypeSEG> shippingTypeSEGALL;//JSA01
	double volLMAX = 32.0;//JSA01
	double volHMAX = 1.0;//JSA01
	double volWMAX = 24.0;//JSA01
	double pesoMAX = 60.0;//JSA01
	Double volMAX = 20.0;//JSA01
	private Double volLMin;
	private Double volWMin;
	private Double volHMin;
	private Double volMin;
	private Double wghtMin;
	String volL;//JSA01
	String volH;//JSA01
	String volW;//JSA01
	private String cmpyId;//JSA03
	private String locationType;//AAP20
	
	public void setTarifa(String tarifa)
	{
		this.tarifa=tarifa;	
	}
	public String getTarifa()
	{
		return this.tarifa;	
	}
	
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
	public String getDiscount() {
		if (discount==null)
			return "0";
		else
			return discount;
	}
	public void setDiscount(String discount) {
		this.discount = discount;
	}
	public String getInsInformation() {
		return insInformation;
	}
	public void setInsInformation(String insInformation) {
		this.insInformation = insInformation;
	}
	public String getInsTax() {
		return insTax;
	}
	public void setInsTax(String insTax) {
		this.insTax = insTax;
	}
	public String getInsTaxRet() {
		return insTaxRet;
	}
	public void setInsTaxRet(String insTaxRet) {
		this.insTaxRet = insTaxRet;
	}
	public String getInsuranceSubTotal() {
		return insuranceSubTotal;
	}
	public void setInsuranceSubTotal(String insuranceSubTotal) {
		this.insuranceSubTotal = insuranceSubTotal;
	}
	public String getCobertura() {
		return cobertura;
	}
	public void setCobertura(String cobertura) {
		this.cobertura = cobertura;
	}
	public String getValordeclarado() {
		if (valordeclarado==null | valordeclarado=="")
			return "0";
		else
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
	public String getRefNo() {
		return refNo;
	}
	public void setRefNo(String refNo) {
		this.refNo = refNo;
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
	public void setShippingType(String[] shippingType) {
		this.shippingType = shippingType;
	}
	public ArrayList getInsurancetype() {
		return insurancetype;
	}
	public void setInsurancetype(ArrayList insurancetype) {
		this.insurancetype = insurancetype;
	}
	public ArrayList getInsurancetypelabel() {
		return insurancetypelabel;
	}
	public void setInsurancetypelabel(ArrayList insurancetypelabel) {
		this.insurancetypelabel = insurancetypelabel;
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
	public String getDesttelefono() {
		return desttelefono;
	}
	public void setDesttelefono(String desttelefono) {
		this.desttelefono = desttelefono;
	}
	public String getAcusederecibo() {
		return acusederecibo;
	}
	public void setAcusederecibo(String acusederecibo) {
		this.acusederecibo = acusederecibo;
	}
	public String getAcklabels() {
		return acklabels;
	}
	public void setAcklabels(String acklabels) {
		this.acklabels = acklabels;
	}

	/**
	 * @return Returns the ackAmt.
	 */
	public String getAckAmt() {
		return ackAmt;
	}
	/**
	 * @param ackAmt The ackAmt to set.
	 */
	public void setAckAmt(String ackAmt) {
		this.ackAmt = ackAmt;
	}
	public String getDeliveryType() {
		return deliveryType;
	}
	public void setDeliveryType(String deliveryType) {
		this.deliveryType = deliveryType;
	}
	
	// Agregado 17-Oct-1022  C.Solano  CenitSfot/EnlaceDise�o
	String ead="";
	String rad="";
	public String getEad()
	{
		return this.ead;
	}
	public void setEad(String ead)
	{
		this.ead=ead;
	}
	public String getRad()
	{
		return this.rad;
	}
	public void setRad(String rad)
	{
		this.rad=rad;
	}
	// Agregado 27-Oct-2011 C.Solano CenitSoft/EnlaceDise�o 
	// Para Zonas Extendidas
	String ext="";
	Double extAmt;
	public void setExt(String ext)
	{
		this.ext=ext;
	}
	public String getExt()
	{
	    return this.ext;	
	}
	public void setExtAmt(Double extAmt)
	{
		this.extAmt=extAmt;
	}
	public Double getExtAmt()
	{
		return this.extAmt;
	}
	String oldRefNo="";
	public void setOldRefNo(String oldRefNo)
	{
		this.oldRefNo=oldRefNo;
	}
	public String getOldRefNo()
	{
		return this.oldRefNo;
	}
	public String getDest_am_gety_code() {
		return dest_am_gety_code;
	}
	
	public void setDest_am_gety_code(String dest_am_gety_code) {
		this.dest_am_gety_code = dest_am_gety_code;
	}

	public String getShipType() {
		return shipType;
	}
	public void setShipType(String shipType) {
		this.shipType = shipType;
	}
	public List<ShipTypeSEG> getShippingTypeSEG() {
		return shippingTypeSEG;
	}
	public void setShippingTypeSEG(List<ShipTypeSEG> shippingTypeSEG) {
		this.shippingTypeSEG = shippingTypeSEG;
	}
	public String getShipTypeSEGCurrent() {
		return shipTypeSEGCurrent;
	}
	public void setShipTypeSEGCurrent(String shipTypeSEGCurrent) {
		this.shipTypeSEGCurrent = shipTypeSEGCurrent;
	}
	public List<ShipTypeSEG> getShippingTypeSEGALL() {
		return shippingTypeSEGALL;
	}
	public void setShippingTypeSEGALL(List<ShipTypeSEG> shippingTypeSEGALL) {
		this.shippingTypeSEGALL = shippingTypeSEGALL;
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
	public double getPesoMAX() {
		return pesoMAX;
	}
	public void setPesoMAX(double pesoMAX) {
		this.pesoMAX = pesoMAX;
	}
	public String getVolL() {
		return volL;
	}
	public void setVolL(String volL) {
		this.volL = volL;
	}
	public String getVolH() {
		return volH;
	}
	public void setVolH(String volH) {
		this.volH = volH;
	}
	public String getVolW() {
		return volW;
	}
	public void setVolW(String volW) {
		this.volW = volW;
	}
	public String getCmpyId() {
		return cmpyId;
	}
	public void setCmpyId(String cmpyId) {
		this.cmpyId = cmpyId;
	}
	public Double getVolMAX() {
		return volMAX;
	}
	public void setVolMAX(Double volMAX) {
		this.volMAX = volMAX;
	}
	public String getLocationType() {
		return locationType;
	}
	public void setLocationType(String locationType) {
		this.locationType = locationType;
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
	
}
