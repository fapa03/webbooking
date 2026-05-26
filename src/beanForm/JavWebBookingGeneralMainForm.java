/**
 * @author: Abraham Daniel Arjona Peraza
 * Fecha de Creaci?n: 30/11/2011
 * Compa??a: PAQUETEXPRESS.
 * Descripci?n del programa: Bean de formulario para documentacion principal
 * FileName: JavWebBookingGeneralMainForm.java
 * SessionVariables:
 * Other Used Classes:
 * Function Names: 
 * ----------------------------------------------------------------- 
 * MODIFICACIONES:  
 * -----------------------------------------------------------------
 * Clave: AAP03
 * Autor: ABRAHAM DANIEL ARJONA PERAZA
 * Fecha: 31/05/2012
 * Descripci?n:  Se agreg? variable pesoMax para validar peso maximo a capturar por linea en el detalle de la captura
 * ------------------------------------------------------------------
 * Clave: AAP04
 * Autor: ABRAHAM DANIEL ARJONA PERAZA
 * Fecha: 05/06/2012
 * Descripci?n:  Se agreg? variable clasifTarif para mantener a que clasificacion pertenece la tarifa del cliente (nueva o antigua)
 * ------------------------------------------------------------------
 * Clave: AAP06
 * Autor: ABRAHAM DANIEL ARJONA PERAZA
 * Fecha: 02/07/2013
 * Descripci?n:  Se agregaron variables:
 *  private String formaPago = "PAID";//AAP06
 *	private String allowedFXC = "N";//AAP06
 *	private String permiteDestino = "N";//AAP06
 *	private String isSoloSobre = "N";//AAP06
 *	para validaciones de flete por cobrar en pantalla.
 * ------------------------------------------------------------------ 
 */
package beanForm;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.struts.action.ActionForm;

import com.paquetexpress.www.webbooking.Documentacion.ShipTypeSEG;

import beanAction.JavWebBookingGeneralMainServiceDetail.DatosCalculosTarifaSEG;
import mx.com.paquetexpress.general.model.dto.BranchDetailDTOResponse;

public class JavWebBookingGeneralMainForm extends ActionForm {
	
	/****************************************************************
	 * metodos para captura de informacion general					*
	 * **************************************************************/
	private String orgioncode;
	private String orgionbranch,destinationbranch,orgienclave,orgiennombre,orgien1,orgien2,orgiencolonia1;
	private String destinationcode="";
	private String orgiencolonia2,orgienrfc,orgientelefono,destinationclave,destinationnombre,destino1,destino2;
	private String destinationzipcode;
	private String destinationcolonia1,destinationcolonia2,destinationrfc,destinationtelefono,fiscal1,fiscal2,fiscalcolonia1,fiscalcolonia2,fiscaltelefono;
	private String destinationcoloniacode;
	private String am_addr_code,orgionaddresscode,destinationaddresscode,fiscaladdresscode,borderbranchcheck;
	private String citycode;
	private String pedinumber,custagent,ship_seqn,no_ship,cur_loc,cur_dest,lc_rout,shiperrmsg;
	private String orginsite;
	private String getycode="";
	private String getytype="";
	private String getylevl="";
	private String orginbranchcode;
	private String destinationsitecode="",destinationsite="";
	private String orginCityCode;	
	private String originColinaCode;
	private String clasifTarif;
	private String destinationRefDom = "";//AAP05
	private String checkRefDir = "";//AAP05
	private String checkTelDir = "";//AAP05
	private String eMailOrigCheck = "";//AAP07
	private String eMailOrigText = "";//AAP07
	private String eMailDestCheck = "";//AAP07
	private String eMailDestText = "";//AAP07
	private String sendeMailOrigBD = "Y";//AAP07
	private String sendeMailDestBD = "Y";//AAP07
	private String eMailOrigBD = "";//AAP07
	private String eMailDestBD = "";//AAP07
	private String origenUserClave = "", origenUserNombre = "",centrosCosto="", centrosCostoDefault="";//AAP08
	private ArrayList centrosCostoValue, centrosCostoLabel;//AAP08
	private String destinationcodeIni = "", destinationbranchIni = "";//AAP09
	private String listReferences = "";
	/****************************************************************
	 * metodos para captura de detalle								*
	 * **************************************************************/
	private String cantidad,descripcion,contenido,tarifa,peso,volumen;
	private String ss_srvc_id,ss_refr_srvc_id,descripcioncode, pesoDB, volumenDB;
	private String specialTariff = "", accion = "", defaultservicescreen, hitCount="0",calculationdone = "", tarifType, displayAmountFlag;
	private String defaultservicescreenKm = "N";//AAP10
	private boolean srvcConfigHasAmntC = false; 

	/****************************************************************
	 * metodos de calculo de servicios								*
	 * **************************************************************/
	private String entrega ="", valorcod, cobertura, acusederecibo, valordeclarado = "1000", seguro, guiano;
	private String serviceshitcount, duplicateguianumber, successmessage, confirmgenerate = "", errmsgenvelope;
	private String guiaprintstring, comments = "", reference;
	private ArrayList insurancetype, insurancetypelabel, ackvalue, acklabel;
	private int i = 0;
	private String serviceAdditional = null;
	private String importeValue = null;
	private String showAdditional = null;
	private String referenceId = "";
	private String errMsgAdditional = null;
	private String zonaExtendida = null;//aap01
	private String operadorLogistico = "";//aap01
	private String accionServices = "";
	private String montoMaxOl = "";//AAP05
	private String reqAcuse = "";//AAP05
	private String reqAcuseXT;
	private String flagValidRefrClnt = "";
	
	/*variables para validacion de tarifa invalida para zona extendida*///AAP02
	private String tarifaInvalida = null;//AAP02
	private String tarifaInvalidaZp = null;
	
	private String formaPago = "PAID";//AAP06
	private String allowedFXC = "N";//AAP06
	private String permiteDestino = "N";//AAP06
	private String isSoloSobre = "N";//AAP06
	private String brncVrtl = "";
	private String hasEnvelope = "N";//AAPXX
	
	/*Variables para el servicio express garantizado*/
	String shippingType;//JSA01
	List<String> shippingTypeTO;
	List<String> shippingTypesName;
	String changeShippingType = "N";
	String changeShippingTypeValorOLD;
	Double volLMAXSEGSobre = 32.0, volWMAXSEGSobre = 24.0,
			volHMAXSEGSobre = 1.0, pesoMAXSEGSobre = 60.0,volMAXSobre =0.001278, volLMAXSEG = 32.0,
			volWMAXSEG = 24.0, volHMAXSEG = 1.0, pesoMAXSEG = 60.0, volMAX =20.0;// JSA01
	String volL = "", volH = "", volW = "", weightVolumetric = "";// JSA01
	private String isShippingTypeSEG = "N";
	String shippingTypeSEGActive;
	private List<ShipTypeSEG> shippingTypeSEGALL;//JSA01
	private DatosCalculosTarifaSEG datosCalculosTarifaSEG;
	private Double factorDividorPesoVol= 5000D;
	private int cantPesoVolDecimales = 6;
	private String companyIdForServices = "";
	private int radioselectCurrent = -1;
	private String forceCaptureDimensions = "N";
	private Double volLMinPaq;
	private Double volWMinPaq;
	private Double volHMinPaq;
	private Double volMinPaq;
	private Double wghtMinPaq;
	private Double volLMinEnv;
	private Double volWMinEnv;
	private Double volHMinEnv;
	private Double volMinEnv;
	private Double wghtMinEnv;
	
	/*Variables para validaci?n sucursal ocurre a selecci?n*/
	private Boolean opcOcurreChck = false;
	private String brnchOcurre = "";
	private ArrayList<BranchDetailDTOResponse> filteredBrnch = new ArrayList<BranchDetailDTOResponse>();
	private String defaultBrnchAddr = "";
	
	/*Variable Documentaci?n de gu?as T7 con peso = 0*/
	private Boolean tarifDefaultChck = false;

	/* Variable para implementaci?n Complemento Carta Porte */
	private String productIdSat = "";
	private String productDescSat = "";
	private String flagValidProductId = "N";

	/* Variable para Captura de Datos Fiscales */
	private String validFiscal = "S";
	
	/* Variable valor maximo declarado */
	private Double maxDeclAmnt = 0.0d;
	
	/* Variable para n�mero de pedimento */
	private String errMsgPediNum = "";
	private String lastWrongPediNum = "";
	
	//Variable necesaria para poder validar el cambio de centro de costo y evitar la accion agregar renglon 
	//Y = Si tiene una sucursal asignada N= no contiene una sucursal destinada 
	private String flagCCAssignedBrnc = "Y"; 
	
	/*Variables documentaci�n origen 70 RAD ZP*/
	private String allowRadZe = "";
	private String allowRadZeT7 = ""; 
	private String prevCCSelect = "";
	
	private String validCCAddrCvge="Y";
	
	private int maxQtyPack;
	
	private String msjShippingCbtr = ""; //Variable necesaria para saber el status de la cobertura del tipo de envio. 
	
	public JavWebBookingGeneralMainForm() {
 }
	
	/****************************************************************
	 * metodos de informacion general								*
	 * **************************************************************/
	public String getOrgioncode() {
		return orgioncode;
	}
	public void setOrgioncode(String orgioncode) {
		this.orgioncode = orgioncode;
	}
	public String getOrgionbranch() {
		return orgionbranch;
	}
	public void setOrgionbranch(String orgionbranch) {
		this.orgionbranch = orgionbranch;
	}
	public String getDestinationbranch() {
		return destinationbranch;
	}
	public void setDestinationbranch(String destinationbranch) {
		this.destinationbranch = destinationbranch;
	}
	public String getOrgienclave() {
		return orgienclave;
	}
	public void setOrgienclave(String orgienclave) {
		this.orgienclave = orgienclave;
	}
	public String getOrgiennombre() {
		return orgiennombre;
	}
	public void setOrgiennombre(String orgiennombre) {
		this.orgiennombre = orgiennombre;
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
	public String getOrgiencolonia1() {
		return orgiencolonia1;
	}
	public void setOrgiencolonia1(String orgiencolonia1) {
		this.orgiencolonia1 = orgiencolonia1;
	}
	public String getDestinationcode() {
		return destinationcode;
	}
	public void setDestinationcode(String destinationcode) {
		this.destinationcode = destinationcode;
	}
	public String getOrgiencolonia2() {
		return orgiencolonia2;
	}
	public void setOrgiencolonia2(String orgiencolonia2) {
		this.orgiencolonia2 = orgiencolonia2;
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
	public String getDestinationzipcode() {
		return destinationzipcode;
	}
	public void setDestinationzipcode(String destinationzipcode) {
		this.destinationzipcode = destinationzipcode;
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
	public String getDestinationcoloniacode() {
		return destinationcoloniacode;
	}
	public void setDestinationcoloniacode(String destinationcoloniacode) {
		this.destinationcoloniacode = destinationcoloniacode;
	}
	public String getAm_addr_code() {
		return am_addr_code;
	}
	public void setAm_addr_code(String am_addr_code) {
		this.am_addr_code = am_addr_code;
	}
	public String getOrgionaddresscode() {
		return orgionaddresscode;
	}
	public void setOrgionaddresscode(String orgionaddresscode) {
		this.orgionaddresscode = orgionaddresscode;
	}
	public String getDestinationaddresscode() {
		return destinationaddresscode;
	}
	public void setDestinationaddresscode(String destinationaddresscode) {
		this.destinationaddresscode = destinationaddresscode;
	}
	public String getFiscaladdresscode() {
		return fiscaladdresscode;
	}
	public void setFiscaladdresscode(String fiscaladdresscode) {
		this.fiscaladdresscode = fiscaladdresscode;
	}
	public String getBorderbranchcheck() {
		return borderbranchcheck;
	}
	public void setBorderbranchcheck(String borderbranchcheck) {
		this.borderbranchcheck = borderbranchcheck;
	}
	public String getCitycode() {
		return citycode;
	}
	public void setCitycode(String citycode) {
		this.citycode = citycode;
	}
	public String getPedinumber() {
		return pedinumber;
	}
	public void setPedinumber(String pedinumber) {
		this.pedinumber = pedinumber;
	}
	public String getCustagent() {
		return custagent;
	}
	public void setCustagent(String custagent) {
		this.custagent = custagent;
	}
	public String getShip_seqn() {
		return ship_seqn;
	}
	public void setShip_seqn(String ship_seqn) {
		this.ship_seqn = ship_seqn;
	}
	public String getNo_ship() {
		return no_ship;
	}
	public void setNo_ship(String no_ship) {
		this.no_ship = no_ship;
	}
	public String getCur_loc() {
		return cur_loc;
	}
	public void setCur_loc(String cur_loc) {
		this.cur_loc = cur_loc;
	}
	public String getCur_dest() {
		return cur_dest;
	}
	public void setCur_dest(String cur_dest) {
		this.cur_dest = cur_dest;
	}
	public String getLc_rout() {
		return lc_rout;
	}
	public void setLc_rout(String lc_rout) {
		this.lc_rout = lc_rout;
	}
	public String getShiperrmsg() {
		return shiperrmsg;
	}
	public void setShiperrmsg(String shiperrmsg) {
		this.shiperrmsg = shiperrmsg;
	}
	public String getOrginsite() {
		return orginsite;
	}
	public void setOrginsite(String orginsite) {
		this.orginsite = orginsite;
	}
	public String getGetycode() {
		return getycode;
	}
	public void setGetycode(String getycode) {
		this.getycode = getycode;
	}
	public String getGetytype() {
		return getytype;
	}
	public void setGetytype(String getytype) {
		this.getytype = getytype;
	}
	public String getGetylevl() {
		return getylevl;
	}
	public void setGetylevl(String getylevl) {
		this.getylevl = getylevl;
	}
	public String getOrginbranchcode() {
		return orginbranchcode;
	}
	public void setOrginbranchcode(String orginbranchcode) {
		this.orginbranchcode = orginbranchcode;
	}
	public String getDestinationsitecode() {
		return destinationsitecode;
	}
	public void setDestinationsitecode(String destinationsitecode) {
		this.destinationsitecode = destinationsitecode;
	}
	public String getDestinationsite() {
		return destinationsite;
	}
	public void setDestinationsite(String destinationsite) {
		this.destinationsite = destinationsite;
	}
	public String getOrginCityCode() {
		return orginCityCode;
	}
	public void setOrginCityCode(String orginCityCode) {
		this.orginCityCode = orginCityCode;
	}
	public String getOriginColinaCode() {
		return originColinaCode;
	}
	public void setOriginColinaCode(String originColinaCode) {
		this.originColinaCode = originColinaCode;
	}
	
	/****************************************************************
	 * metodos para captura de detalle								*
	 * **************************************************************/
	public String getCantidad() {
		return cantidad;
	}

	public void setCantidad(String cantidad) {
		this.cantidad = cantidad;
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
		this.contenido = contenido;
	}

	public String getTarifa() {
		return tarifa;
	}

	public void setTarifa(String tarifa) {
		this.tarifa = tarifa;
	}

	public String getPeso() {
		return peso;
	}

	public void setPeso(String peso) {
		this.peso = peso;
	}

	public String getVolumen() {
		String tmp = volumen;
		if(shippingType != null && !shippingType.equalsIgnoreCase("STD-T")){
			tmp = getVolumenCalculado(volL, volH, volW);
		}
		return tmp;
	}

	public void setVolumen(String volumen) {
		this.volumen = volumen;
	}

	public String getSs_srvc_id() {
		return ss_srvc_id;
	}

	public void setSs_srvc_id(String ss_srvc_id) {
		this.ss_srvc_id = ss_srvc_id;
	}

	public String getSs_refr_srvc_id() {
		return ss_refr_srvc_id;
	}

	public void setSs_refr_srvc_id(String ss_refr_srvc_id) {
		this.ss_refr_srvc_id = ss_refr_srvc_id;
	}

	public String getDescripcioncode() {
		return descripcioncode;
	}

	public void setDescripcioncode(String descripcioncode) {
		this.descripcioncode = descripcioncode;
	}

	public String getSpecialTariff() {
		return specialTariff;
	}

	public void setSpecialTariff(String specialTariff) {
		this.specialTariff = specialTariff;
	}

	public String getAccion() {
		return accion;
	}
	
	public void setAccion(String accion) {
		this.accion = accion;
	}

	public String getDefaultservicescreen() {
		return defaultservicescreen;
	}

	public void setDefaultservicescreen(String defaultservicescreen) {
		this.defaultservicescreen = defaultservicescreen;
	}

	public String getDefaultservicescreenKm() {//AAP10
		return defaultservicescreenKm;
	}

	public void setDefaultservicescreenKm(String defaultservicescreenKm) {//AAP10
		this.defaultservicescreenKm = defaultservicescreenKm;
	}
	
	public String getHitCount() {
		return hitCount;
	}

	public void setHitCount(String hitCount) {
		this.hitCount = hitCount;
	}

	public String getCalculationdone() {
		return calculationdone;
	}

	public void setCalculationdone(String calculationdone) {
		this.calculationdone = calculationdone;
	}

	public String getPesoDB() {
		return pesoDB;
	}

	public void setPesoDB(String pesoDB) {
		this.pesoDB = pesoDB;
	}

	public String getVolumenDB() {
		return volumenDB;
	}

	public void setVolumenDB(String volumenDB) {
		this.volumenDB = volumenDB;
	}

	public String getTarifType() {
		return tarifType;
	}

	public void setTarifType(String tarifType) {
		this.tarifType = tarifType;
	}
	
	public String getDisplayAmountFlag() {
		return displayAmountFlag;
	}

	public void setDisplayAmountFlag(String displayAmountFlag) {
		this.displayAmountFlag = displayAmountFlag;
	}

	/****************************************************************
	 * metodos de calculo de servicios								*
	 * **************************************************************/
	public String getEntrega() {
		return entrega;
	}

	public void setEntrega(String entrega) {
		this.entrega = entrega;
	}

	public String getValorcod() {
		return valorcod;
	}

	public void setValorcod(String valorcod) {
		this.valorcod = valorcod;
	}

	public String getCobertura() {
		return cobertura;
	}

	public void setCobertura(String cobertura) {
		this.cobertura = cobertura;
	}

	public String getAcusederecibo() {
		return acusederecibo;
	}

	public void setAcusederecibo(String acusederecibo) {
		this.acusederecibo = acusederecibo;
	}

	public String getValordeclarado() {
		return valordeclarado;
	}

	public void setValordeclarado(String valordeclarado) {
		this.valordeclarado = valordeclarado;
	}

	public String getSeguro() {
		return seguro;
	}

	public void setSeguro(String seguro) {
		this.seguro = seguro;
	}

	public String getGuiano() {
		return guiano;
	}

	public void setGuiano(String guiano) {
		this.guiano = guiano;
	}

	public String getServiceshitcount() {
		return serviceshitcount;
	}

	public void setServiceshitcount(String serviceshitcount) {
		this.serviceshitcount = serviceshitcount;
	}

	public String getDuplicateguianumber() {
		return duplicateguianumber;
	}

	public void setDuplicateguianumber(String duplicateguianumber) {
		this.duplicateguianumber = duplicateguianumber;
	}

	public String getSuccessmessage() {
		return successmessage;
	}

	public void setSuccessmessage(String successmessage) {
		this.successmessage = successmessage;
	}

	public String getConfirmgenerate() {
		return confirmgenerate;
	}

	public void setConfirmgenerate(String confirmgenerate) {
		this.confirmgenerate = confirmgenerate;
	}

	public String getErrmsgenvelope() {
		return errmsgenvelope;
	}

	public void setErrmsgenvelope(String errmsgenvelope) {
		this.errmsgenvelope = errmsgenvelope;
	}

	public String getGuiaprintstring() {
		return guiaprintstring;
	}

	public void setGuiaprintstring(String guiaprintstring) {
		this.guiaprintstring = guiaprintstring;
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

	public ArrayList getInsurancetype(){
		if (this.insurancetype == null) {
			this.insurancetype = new ArrayList();
		}		
		return this.insurancetype;		
	}

	public void setInsurancetype(String insurancetype){
		if ( this.insurancetype == null ){
			this.insurancetype = new ArrayList(3); 
			this.insurancetype.add(insurancetype);			
		} else{
			this.insurancetype.add(insurancetype);
		}
	}

	public ArrayList getInsurancetypelabel(){
		if (this.insurancetypelabel == null) {
			this.insurancetypelabel = new ArrayList(3);
		}		
		return this.insurancetypelabel;	
	}
	
	public void setInsurancetypelabel(String insurancetypelabel){
		if (this.insurancetypelabel == null) {
			this.insurancetypelabel = new ArrayList(3);			
			this.insurancetypelabel.add(insurancetypelabel);			
		} else {
			this.insurancetypelabel.add(insurancetypelabel);
		}
	}

	public ArrayList getAckvalue(){		
		if (this.ackvalue == null) {
			this.ackvalue = new ArrayList(4);
		}		
		return this.ackvalue;
	}
	
	public void setAckvalue(String ackvalue){
		if ( this.ackvalue == null ){			
			this.ackvalue = new ArrayList(3); 
			this.ackvalue.add(ackvalue);
		} 
		else{
			this.ackvalue.add(ackvalue);
		}
	}
	
	public ArrayList getAcklabel(){
		if (this.acklabel == null) {
			this.acklabel = new ArrayList(3);
		}		
		return this.acklabel;
	}
	
	public void setAcklabel(String acklabel){
		if ( this.acklabel == null ){
			this.acklabel = new ArrayList(3); 
			this.acklabel.add(acklabel);
		} 
		else{
			this.acklabel.add(acklabel);
		}
	}

	public int getI() {
		return i;
	}

	public void setI(int i) {
		this.i = i;
	}

	public String getServiceAdditional() {
		return serviceAdditional;
	}

	public void setServiceAdditional(String serviceAdditional) {
		this.serviceAdditional = serviceAdditional;
	}

	public String getImporteValue() {
		return importeValue;
	}

	public void setImporteValue(String importeValue) {
		this.importeValue = importeValue;
	}

	public String getShowAdditional() {
		return showAdditional;
	}

	public void setShowAdditional(String showAdditional) {
		this.showAdditional = showAdditional;
	}

	public String getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}

	public String getErrMsgAdditional() {
		return errMsgAdditional;
	}

	public void setErrMsgAdditional(String errMsgAdditional) {
		this.errMsgAdditional = errMsgAdditional;
	}

	public String getZonaExtendida() {
		return zonaExtendida;
	}

	public void setZonaExtendida(String zonaExtendida) {
		this.zonaExtendida = zonaExtendida;
	}

	public String getOperadorLogistico() {
		return operadorLogistico;
	}

	public void setOperadorLogistico(String operadorLogistico) {
		this.operadorLogistico = operadorLogistico;
	}

	public String getTarifaInvalida() {
		return tarifaInvalida;
	}

	public void setTarifaInvalida(String tarifaInvalida) {
		this.tarifaInvalida = tarifaInvalida;
	}

	public String getAccionServices() {
		return accionServices;
	}

	public void setAccionServices(String accionServices) {
		this.accionServices = accionServices;
	}

	public String getClasifTarif() {//AAP04
		return clasifTarif;
	}

	public void setClasifTarif(String clasifTarif) {//AAP04
		this.clasifTarif = clasifTarif;
	}

	public String getMontoMaxOl() {
		return montoMaxOl;
	}

	public void setMontoMaxOl(String montoMaxOl) {
		this.montoMaxOl = montoMaxOl;
	}

	public String getDestinationRefDom() {
		return destinationRefDom;
	}

	public void setDestinationRefDom(String destinationRefDom) {
		this.destinationRefDom = destinationRefDom;
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

	public String getFormaPago() {//AAP06
		return formaPago;
	}

	public void setFormaPago(String formaPago) {//AAP06
		this.formaPago = formaPago;
	}

	public String getAllowedFXC() {//AAP06
		return allowedFXC;
	}

	public void setAllowedFXC(String allowedFXC) {//AAP06
		this.allowedFXC = allowedFXC;
	}

	public String getPermiteDestino() {//AAP06
		return permiteDestino;
	}

	public void setPermiteDestino(String permiteDestino) {//AAP06
		this.permiteDestino = permiteDestino;
	}

	public String getIsSoloSobre() {//AAP06
		return isSoloSobre;
	}

	public void setIsSoloSobre(String isSoloSobre) {//AAP06
		this.isSoloSobre = isSoloSobre;
	}

	public String geteMailOrigCheck() {//AAP07
		return eMailOrigCheck;
	}

	public void seteMailOrigCheck(String eMailOrigCheck) {//AAP07
		this.eMailOrigCheck = eMailOrigCheck;
	}

	public String geteMailOrigText() {//AAP07
		return eMailOrigText;
	}

	public void seteMailOrigText(String eMailOrigText) {//AAP07
		this.eMailOrigText = eMailOrigText;
	}

	public String geteMailDestCheck() {//AAP07
		return eMailDestCheck;
	}

	public void seteMailDestCheck(String eMailDestCheck) {//AAP07
		this.eMailDestCheck = eMailDestCheck;
	}

	public String geteMailDestText() {//AAP07
		return eMailDestText;
	}

	public void seteMailDestText(String eMailDestText) {//AAP07
		this.eMailDestText = eMailDestText;
	}

	public String getSendeMailOrigBD() {
		return sendeMailOrigBD;
	}

	public void setSendeMailOrigBD(String sendeMailOrigBD) {
		this.sendeMailOrigBD = sendeMailOrigBD;
	}

	public String getSendeMailDestBD() {
		return sendeMailDestBD;
	}

	public void setSendeMailDestBD(String sendeMailDestBD) {
		this.sendeMailDestBD = sendeMailDestBD;
	}

	public String geteMailOrigBD() {
		return eMailOrigBD;
	}

	public void seteMailOrigBD(String eMailOrigBD) {
		this.eMailOrigBD = eMailOrigBD;
	}

	public String geteMailDestBD() {
		return eMailDestBD;
	}

	public void seteMailDestBD(String eMailDestBD) {
		this.eMailDestBD = eMailDestBD;
	}

	public String getOrigenUserClave() {
		return origenUserClave;
	}

	public void setOrigenUserClave(String origenUserClave) {
		this.origenUserClave = origenUserClave;
	}

	public String getOrigenUserNombre() {
		return origenUserNombre;
	}

	public void setOrigenUserNombre(String origenUserNombre) {
		this.origenUserNombre = origenUserNombre;
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

	public String getDestinationcodeIni() {
		return destinationcodeIni;
	}

	public void setDestinationcodeIni(String destinationcodeIni) {
		this.destinationcodeIni = destinationcodeIni;
	}

	public String getDestinationbranchIni() {
		return destinationbranchIni;
	}

	public void setDestinationbranchIni(String destinationbranchIni) {
		this.destinationbranchIni = destinationbranchIni;
	}

	public String getListReferences() {
		return listReferences;
	}

	public void setListReferences(String listReferences) {
		this.listReferences = listReferences;
	}

	public String getBrncVrtl() {
		return brncVrtl;
	}

	public void setBrncVrtl(String brncVrtl) {
		this.brncVrtl = brncVrtl;
	}
	
	public String getReqAcuseXT() {
		return reqAcuseXT;
	}

	public void setReqAcuseXT(String reqAcuseXT) {
		this.reqAcuseXT = reqAcuseXT;
	}
	public String getHasEnvelope() {
		return hasEnvelope;
	}

	public void setHasEnvelope(String hasEnvelope) {
		this.hasEnvelope = hasEnvelope;
	}

	public String getShippingType() {
		return shippingType;
	}

	public void setShippingType(String shippingType) {
		this.shippingType = shippingType;
	}

	public List<String> getShippingTypeTO() {
		return shippingTypeTO;
	}

	public void setShippingTypeTO(List<String> shippingTypeTO) {
		this.shippingTypeTO = shippingTypeTO;
	}

	public List<String> getShippingTypesName() {
		return shippingTypesName;
	}

	public void setShippingTypesName(List<String> shippingTypesName) {
		this.shippingTypesName = shippingTypesName;
	}

	public String getChangeShippingType() {
		return changeShippingType;
	}

	public void setChangeShippingType(String changeShippingType) {
		this.changeShippingType = changeShippingType;
	}

	public String getChangeShippingTypeValorOLD() {
		return changeShippingTypeValorOLD;
	}

	public void setChangeShippingTypeValorOLD(String changeShippingTypeValorOLD) {
		this.changeShippingTypeValorOLD = changeShippingTypeValorOLD;
	}

	public Double getVolLMAXSEG() {
		return volLMAXSEG;
	}

	public void setVolLMAXSEG(Double volLMAXSEG) {
		this.volLMAXSEG = volLMAXSEG;
	}

	public Double getVolWMAXSEG() {
		return volWMAXSEG;
	}

	public void setVolWMAXSEG(Double volWMAXSEG) {
		this.volWMAXSEG = volWMAXSEG;
	}

	public Double getVolHMAXSEG() {
		return volHMAXSEG;
	}

	public void setVolHMAXSEG(Double volHMAXSEG) {
		this.volHMAXSEG = volHMAXSEG;
	}

	public Double getPesoMAXSEG() {
		return pesoMAXSEG;
	}

	public void setPesoMAXSEG(Double pesoMAXSEG) {
		this.pesoMAXSEG = pesoMAXSEG;
	}

	public Double getVolLMAXSEGSobre() {
		return volLMAXSEGSobre;
	}

	public void setVolLMAXSEGSobre(Double volLMAXSEGSobre) {
		this.volLMAXSEGSobre = volLMAXSEGSobre;
	}

	public Double getVolWMAXSEGSobre() {
		return volWMAXSEGSobre;
	}

	public void setVolWMAXSEGSobre(Double volWMAXSEGSobre) {
		this.volWMAXSEGSobre = volWMAXSEGSobre;
	}

	public Double getVolHMAXSEGSobre() {
		return volHMAXSEGSobre;
	}

	public void setVolHMAXSEGSobre(Double volHMAXSEGSobre) {
		this.volHMAXSEGSobre = volHMAXSEGSobre;
	}

	public Double getPesoMAXSEGSobre() {
		return pesoMAXSEGSobre;
	}

	public void setPesoMAXSEGSobre(Double pesoMAXSEGSobre) {
		this.pesoMAXSEGSobre = pesoMAXSEGSobre;
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

	public String getWeightVolumetric() {
		String tmp = weightVolumetric;
		if(shippingType != null && !shippingType.equalsIgnoreCase("STD-T")){
			//factorDividorPesoVol= 6000D;//se coment? linea... ahora se saca de configuracion
			tmp = getVolumenVolumentricoCalculado(volL, volH, volW);
		}/* else {//se coment? linea... ahora se saca de configuracion
			factorDividorPesoVol= 5000D;//AAP20 Se agrega para calculo de STD (para visualizacion de dato en pantalla a solicitud de COMERCIAL)
		}*/
		return tmp;
	}

	public void setWeightVolumetric(String weightVolumetric) {
		this.weightVolumetric = weightVolumetric;
	}

	private String getVolumenCalculado(String volLx, String volHx, String volWx){
		Double volL = 0.0, volH = 0.0, volW = 0.0;
		if(volLx != null && !volLx.isEmpty()){
			volL = Double.valueOf(volLx);
		}
		if(volHx != null && !volHx.isEmpty()){
			volH = Double.valueOf(volHx);
		}
		if(volWx != null && !volWx.isEmpty()){
			volW = Double.valueOf(volWx);
		}
		BigDecimal volumen = new BigDecimal(((volH / 100) * (volL / 100) * (volW / 100))).setScale(cantPesoVolDecimales, BigDecimal.ROUND_HALF_EVEN);
		return volumen.toString();
	}

	private String getVolumenVolumentricoCalculado(String volLx, String volHx, String volWx){
		Double volL = 0.0, volH = 0.0, volW = 0.0;
		if(volLx != null && !volLx.isEmpty()){
			volL = Double.valueOf(volLx);
		}
		if(volHx != null && !volHx.isEmpty()){
			volH = Double.valueOf(volHx);
		}
		if(volWx != null && !volWx.isEmpty()){
			volW = Double.valueOf(volWx);
		}
		BigDecimal volumen = new BigDecimal((((volH) * (volL) * (volW)))/factorDividorPesoVol).setScale(cantPesoVolDecimales, BigDecimal.ROUND_HALF_EVEN);
		return volumen.toString();
	}

	public String getIsShippingTypeSEG() {
		return isShippingTypeSEG;
	}

	public void setIsShippingTypeSEG(String isShippingTypeSEG) {
		this.isShippingTypeSEG = isShippingTypeSEG;
	}

	public String getShippingTypeSEGActive() {
		return shippingTypeSEGActive;
	}

	public void setShippingTypeSEGActive(String shippingTypeSEGActive) {
		this.shippingTypeSEGActive = shippingTypeSEGActive;
	}

	public List<ShipTypeSEG> getShippingTypeSEGALL() {
		return shippingTypeSEGALL;
	}

	public void setShippingTypeSEGALL(List<ShipTypeSEG> shippingTypeSEGALL) {
		this.shippingTypeSEGALL = shippingTypeSEGALL;
	}

	public DatosCalculosTarifaSEG getDatosCalculosTarifaSEG() {
		return datosCalculosTarifaSEG;
	}

	public void setDatosCalculosTarifaSEG(DatosCalculosTarifaSEG datosCalculosTarifaSEG) {
		this.datosCalculosTarifaSEG = datosCalculosTarifaSEG;
	}
	
	public Double getFactorDividorPesoVol() {
		return factorDividorPesoVol;
	}

	public void setFactorDividorPesoVol(Double factorDividorPesoVol) {
		this.factorDividorPesoVol = factorDividorPesoVol;
	}

	//Este metodo se utiliza para saber si el servicio es un SEG-2D y regresar el servicio original para los calculos de tarifa o coberturas.
	public String getTypeSEGOperativa(String typeService){
		return typeService;
	}

	public int getCantPesoVolDecimales() {
		return cantPesoVolDecimales;
	}

	public void setCantPesoVolDecimales(int cantPesoVolDecimales) {
		this.cantPesoVolDecimales = cantPesoVolDecimales;
	}

	public String getFlagValidRefrClnt() {
		return flagValidRefrClnt;
	}

	public void setFlagValidRefrClnt(String flagValidRefrClnt) {
		this.flagValidRefrClnt = flagValidRefrClnt;
	}	

	public String getCompanyIdForServices() {
		return companyIdForServices;
	}

	public void setCompanyIdForServices(String companyIdForServices) {
		this.companyIdForServices = companyIdForServices;
	}

	public int getRadioselectCurrent() {
		return radioselectCurrent;
	}

	public void setRadioselectCurrent(int radioselectCurrent) {
		this.radioselectCurrent = radioselectCurrent;
	}

	public String getForceCaptureDimensions() {
		return forceCaptureDimensions;
	}

	public void setForceCaptureDimensions(String forceCaptureDimensions) {
		this.forceCaptureDimensions = forceCaptureDimensions;
	}

	public Double getVolMAXSobre() {
		return volMAXSobre;
	}

	public void setVolMAXSobre(Double volMAXSobre) {
		this.volMAXSobre = volMAXSobre;
	}

	public Double getVolMAX() {
		return volMAX;
	}

	public void setVolMAX(Double volMAX) {
		this.volMAX = volMAX;
	}
	
	public Boolean getOpcOcurre() {
		return opcOcurreChck;
	}

	public void setOpcOcurre(Boolean opcOcurre) {
		this.opcOcurreChck = opcOcurre;
	}

	/**
	 * @return the sucOcurre
	 */
	public String getBrnchOcurre() {
		return brnchOcurre;
	}

	/**
	 * @param sucOcurre the sucOcurre to set
	 */
	public void setBrnchOcurre(String sucOcurre) {
		this.brnchOcurre = sucOcurre;
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
	 * @return the tarifDefaultChck
	 */
	public Boolean getTarifDefaultChck() {
		return tarifDefaultChck;
	}

	/**
	 * @param tarifDefaultChck the tarifDefaultChck to set
	 */
	public void setTarifDefaultChck(Boolean tarifDefaultChck) {
		this.tarifDefaultChck = tarifDefaultChck;
	}

	/**
	 * @return the allowRadZe
	 */
	public String getAllowRadZe() {
		return allowRadZe;
	}

	/**
	 * @param allowRadZe the allowRadZe to set
	 */
	public void setAllowRadZe(String allowRadZe) {
		this.allowRadZe = allowRadZe;
	}

	/**
	 * @return the allowRadZeT7
	 */
	public String getAllowRadZeT7() {
		return allowRadZeT7;
	}

	/**
	 * @param allowRadZeT7 the allowRadZeT7 to set
	 */
	public void setAllowRadZeT7(String allowRadZeT7) {
		this.allowRadZeT7 = allowRadZeT7;
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
	 * @return the flagValidProductId
	 */
	public String getFlagValidProductId() {
		return flagValidProductId;
	}

	/**
	 * @param flagValidProductId the flagValidProductId to set
	 */
	public void setFlagValidProductId(String flagValidProductId) {
		this.flagValidProductId = flagValidProductId;
	}

	/**
	 * @return the validFiscal
	 */
	public String getValidFiscal() {
		return validFiscal;
	}

	/**
	 * @param validFiscal the validFiscal to set
	 */
	public void setValidFiscal(String validFiscal) {
		this.validFiscal = validFiscal;
	}

	/**
	 * @return the maxDeclAmnt
	 */
	public Double getMaxDeclAmnt() {
		return maxDeclAmnt;
	}

	/**
	 * @param maxDeclAmnt the maxDeclAmnt to set
	 */
	public void setMaxDeclAmnt(Double maxDeclAmnt) {
		this.maxDeclAmnt = maxDeclAmnt;
	}

	public boolean getSrvcConfigHasAmntC() {
		return srvcConfigHasAmntC;
	}

	public void setSrvcConfigHasAmntC(boolean srvcConfigHasAmntC) {
		this.srvcConfigHasAmntC = srvcConfigHasAmntC;
	}

	/**
	 * @return the prevCCSelect
	 */
	public String getPrevCCSelect() {
		return prevCCSelect;
	}

	/**
	 * @param prevCCSelect the prevCCSelect to set
	 */
	public void setPrevCCSelect(String prevCCSelect) {
		this.prevCCSelect = prevCCSelect;
	}

	/**
	 * @return the tarifaInvalidaZp
	 */
	public String getTarifaInvalidaZp() {
		return tarifaInvalidaZp;
	}

	/**
	 * @param tarifaInvalidaZp the tarifaInvalidaZp to set
	 */
	public void setTarifaInvalidaZp(String tarifaInvalidaZp) {
		this.tarifaInvalidaZp = tarifaInvalidaZp;
	}
	
	/**
	 * @return the errMsgPediNum
	 */
	public String getErrMsgPediNum() {
		return errMsgPediNum;
	}

	/**
	 * @param errMsgPediNum the errMsgPediNum to set
	 */
	public void setErrMsgPediNum(String errPediNum) {
		this.errMsgPediNum = errPediNum;
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

	public String getFlagCCAssignedBrnc() {
		return flagCCAssignedBrnc;
	}

	public void setFlagCCAssignedBrnc(String flagCCAssignedBrnc) {
		this.flagCCAssignedBrnc = flagCCAssignedBrnc;
	}

	public Double getVolLMinPaq() {
		return volLMinPaq;
	}

	public void setVolLMinPaq(Double volLMinPaq) {
		this.volLMinPaq = volLMinPaq;
	}

	public Double getVolWMinPaq() {
		return volWMinPaq;
	}

	public void setVolWMinPaq(Double volWMinPaq) {
		this.volWMinPaq = volWMinPaq;
	}

	public Double getVolHMinPaq() {
		return volHMinPaq;
	}

	public void setVolHMinPaq(Double volHMinPaq) {
		this.volHMinPaq = volHMinPaq;
	}

	public Double getVolMinPaq() {
		return volMinPaq;
	}

	public void setVolMinPaq(Double volMinPaq) {
		this.volMinPaq = volMinPaq;
	}

	public Double getWghtMinPaq() {
		return wghtMinPaq;
	}

	public void setWghtMinPaq(Double wghtMinPaq) {
		this.wghtMinPaq = wghtMinPaq;
	}

	public Double getVolLMinEnv() {
		return volLMinEnv;
	}

	public void setVolLMinEnv(Double volLMinEnv) {
		this.volLMinEnv = volLMinEnv;
	}

	public Double getVolWMinEnv() {
		return volWMinEnv;
	}

	public void setVolWMinEnv(Double volWMinEnv) {
		this.volWMinEnv = volWMinEnv;
	}

	public Double getVolHMinEnv() {
		return volHMinEnv;
	}

	public void setVolHMinEnv(Double volHMinEnv) {
		this.volHMinEnv = volHMinEnv;
	}

	public Double getVolMinEnv() {
		return volMinEnv;
	}

	public void setVolMinEnv(Double volMinEnv) {
		this.volMinEnv = volMinEnv;
	}

	public Double getWghtMinEnv() {
		return wghtMinEnv;
	}

	public void setWghtMinEnv(Double wghtMinEnv) {
		this.wghtMinEnv = wghtMinEnv;
	}

	/**
	 * @return the validCCAddrCvge
	 */
	public String getValidCCAddrCvge() {
		return validCCAddrCvge;
	}

	/**
	 * @param validCCAddrCvge the validCCAddrCvge to set
	 */
	public void setValidCCAddrCvge(String validCCAddrCvge) {
		this.validCCAddrCvge = validCCAddrCvge;
	}

	public String getCentrosCostoDefault() {
		return centrosCostoDefault;
	}

	public void setCentrosCostoDefault(String centrosCostoDefault) {
		this.centrosCostoDefault = centrosCostoDefault;
	}

	public int getMaxQtyPack() {
		return maxQtyPack;
	}

	public void setMaxQtyPack(int maxQtyPack) {
		this.maxQtyPack = maxQtyPack;
	}

	public String getMsjShippingCbtr() {
		return msjShippingCbtr;
	}

	public void setMsjShippingCbtr(String msjShippingCbtr) {
		this.msjShippingCbtr = msjShippingCbtr;
	}


	
}