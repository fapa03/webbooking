 package bean;
 /**
  * @author: D.SivaKumar
  * Fecha de Creaci?n: 25-Feb-2003
  * Compa??a: KUMARAN.
  * Descripci?n del programa: This used to Access the DataBase Values.
  * FileName: Global.java
  * SessionVariables:
  * Other Used Classes:
  * Function Names:
  * -----------------------------------------------------------------
  * MODIFICACIONES:
  * -----------------------------------------------------------------
  * Clave: AAP01
  * Autor: Abraham Daniel Arjona Peraza
  * Fecha:
  * Descripci?n: SE AGREG? VARIABLE PARA VALIDACION DE CAMBIO DE SITE EN NUEVA DOCUMENTACION.
  * ------------------------------------------------------------------
  * Clave: AAP02
  * Autor: Abraham Daniel Arjona Peraza
  * Fecha: 04/06/2012
  * Descripci?n: se agreg? variable clasifTarif para identificacion de tarifa vieja (0) o nueva (1).
  * ------------------------------------------------------------------
  * Clave: AAP03
  * Autor: Abraham Daniel Arjona Peraza
  * Fecha: 01/07/2013
  * Descripci?n: se agreg? variable allowedFXC para identificacion de clientes que aceptan flete por cobrar.
  * Se agregaron metodos get y set de todas las variables de la clase.
  * ------------------------------------------------------------------
  * Autor: Jos� Crist�bal Hernandez Fierro
  * Fecha: 09/Octubre/2015
  * Descripci?n: Se agrega un nuevo campo: Boolean "acuseXT" para validar si la guia tendra Acuse XT
  * ------------------------------------------------------------------
  * Clave: AAP19
  * Autor: Abraham Daniel Arjona Peraza
  * Fecha: 03/12/2018
  * Descripci?n: se agreg? variable SStotal para mantener la cantidad de Solicitudes de servicio totales a imprimir (configuracion de cliente principal).
  * ------------------------------------------------------------------
  * Autor: Pacheco Parra Eva Melissa
  * Fecha: 20/Junio/2023
  * Descripci�n: Se agrega printWwPdfXls como configuracion para determinar si se puede implementar la descarga de etiquetas para guias masivas en PDF 
  * ------------------------------------------------------------------
  */
public class Global implements java.io.Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	public String clientId;
	public String assignedBranch;
	public String assignedSite;
	public String siteName;
	public String tarifType;
	public String additionalServiceFlag;
	public String clientIdAgreement;

	public String clientName;
	public String rfc;
	public String destinationBranchId="";
	public String destinationSiteId="";//AAP01 SE AGREG? VARIABLE PARA VALIDACION DE CAMBIO DE SITE EN NUEVA DOCUMENTACION.
	public String tariffSlab;
	public String groupClientId;
	public String sysDate;
	public String serverDate;
	public boolean isOrigionBorderBranch;
	public boolean isDestinationBorderBranch;
	private String origionBorderBranch; //AAP20
	private String destinationBorderBranch;//AAP20
	public String kmTarifType;
	public String commentText = "";
	public String clientType;
	public String destinationBranchName="";//no se utiliza esta variable ABMREVISION
	public long timediff;
	public String printfilename=null;
	public String printlogfilename=null;
	public String displayAmountFlag;
	public String clasifTarif;//AAP02
	private String allowedFXC = "";//AAP03
	private String agreementKey = "";//AAP04
	private String password = "";//AAP04
	private String origenUserClave = "", origenUserNombre = "", origenUserLevel = "";//AAP02
	private String acceptZeT7 = "";
	public String printfilenameRurn=null;
	private boolean acuseXT = false;
	private String trackingNoGenRet;
	private String localService = "0";//AAP17
	private boolean genLocalService = false;//AAP17
	private String catalogMbr = "0";//AAP18
	private String disableMFGen = "0";//AAP18
	private String showReprintGuia = "0";
	private String showGuiasRR = "0";
	private String forceCaptureDimensions = "N";
	private String allowRRFromXLS = "N";//AAP25
	private String allowPrintQZ = "N";
	private String manifestTypeWE = "N";

	private int SStotal = 0;//AAP19
	private String printWwPdfXls = "N";
	private String allowCancelGuiaMult = "N";
	public String groupClientIdDestino;
	private double factorDivisorPesoVol = 5000;
	//valores necesario para el centro de costo especifico
	private String brncIdWeb ="";
	private String brncNameValue = "";
	private String daysCancelGMult = ""; //Parametro para buscar el rango de dias en la busqueda inicial de guias en la pantalla de cancelacion multiple de guias

	/* Variables para RAD ZONA PLUS */
	private String acceptRadZp = "";
	private String acceptRadZpT7 = "";

	private String serverName = "";
	private String pdfFormat = "";

	private String showGlobalMultCncl = "";

	private String showGlobalRR ="";

	public Global(){
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getAssignedBranch() {
		return assignedBranch;
	}

	public void setAssignedBranch(String assignedBranch) {
		this.assignedBranch = assignedBranch;
	}

	public String getAssignedSite() {
		return assignedSite;
	}

	public void setAssignedSite(String assignedSite) {
		this.assignedSite = assignedSite;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getTarifType() {
		return tarifType;
	}

	public void setTarifType(String tarifType) {
		this.tarifType = tarifType;
	}

	public String getAdditionalServiceFlag() {
		return additionalServiceFlag;
	}

	public void setAdditionalServiceFlag(String additionalServiceFlag) {
		this.additionalServiceFlag = additionalServiceFlag;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getRfc() {
		return rfc;
	}

	public void setRfc(String rfc) {
		this.rfc = rfc;
	}

	public String getDestinationBranchId() {
		return destinationBranchId;
	}

	public void setDestinationBranchId(String destinationBranchId) {
		this.destinationBranchId = destinationBranchId;
	}

	public String getDestinationSiteId() {
		return destinationSiteId;
	}

	public void setDestinationSiteId(String destinationSiteId) {
		this.destinationSiteId = destinationSiteId;
	}

	public String getTariffSlab() {
		return tariffSlab;
	}

	public void setTariffSlab(String tariffSlab) {
		this.tariffSlab = tariffSlab;
	}

	public String getGroupClientId() {
		return groupClientId;
	}

	public void setGroupClientId(String groupClientId) {
		this.groupClientId = groupClientId;
	}

	public String getSysDate() {
		return sysDate;
	}

	public void setSysDate(String sysDate) {
		this.sysDate = sysDate;
	}

	public String getServerDate() {
		return serverDate;
	}

	public void setServerDate(String serverDate) {
		this.serverDate = serverDate;
	}

	public boolean isOrigionBorderBranch() {
		return isOrigionBorderBranch;
	}

	public void setOrigionBorderBranch(boolean isOrigionBorderBranch) {
		this.isOrigionBorderBranch = isOrigionBorderBranch;
	}

	public boolean isDestinationBorderBranch() {
		return isDestinationBorderBranch;
	}

	public void setDestinationBorderBranch(boolean isDestinationBorderBranch) {
		this.isDestinationBorderBranch = isDestinationBorderBranch;
	}

	public String getKmTarifType() {
		return kmTarifType;
	}

	public void setKmTarifType(String kmTarifType) {
		this.kmTarifType = kmTarifType;
	}

	public String getCommentText() {
		return commentText;
	}

	public void setCommentText(String commentText) {
		this.commentText = commentText;
	}

	public String getClientType() {
		return clientType;
	}

	public void setClientType(String clientType) {
		this.clientType = clientType;
	}

	public String getDestinationBranchName() {
		return destinationBranchName;
	}

	public void setDestinationBranchName(String destinationBranchName) {
		this.destinationBranchName = destinationBranchName;
	}

	public long getTimediff() {
		return timediff;
	}

	public void setTimediff(long timediff) {
		this.timediff = timediff;
	}

	public String getPrintfilename() {
		return printfilename;
	}

	public void setPrintfilename(String printfilename) {
		this.printfilename = printfilename;
	}

	public String getPrintlogfilename() {
		return printlogfilename;
	}

	public void setPrintlogfilename(String printlogfilename) {
		this.printlogfilename = printlogfilename;
	}

	public String getDisplayAmountFlag() {
		return displayAmountFlag;
	}

	public void setDisplayAmountFlag(String displayAmountFlag) {
		this.displayAmountFlag = displayAmountFlag;
	}

	public String getClasifTarif() {
		return clasifTarif;
	}

	public void setClasifTarif(String clasifTarif) {
		this.clasifTarif = clasifTarif;
	}

	public String getAllowedFXC() {
		return allowedFXC;
	}

	public void setAllowedFXC(String allowedFXC) {
		this.allowedFXC = allowedFXC;
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

	public String getOrigenUserLevel() {
		return origenUserLevel;
	}

	public void setOrigenUserLevel(String origenUserLevel) {
		this.origenUserLevel = origenUserLevel;
	}

	public String getAgreementKey() {
		return agreementKey;
	}

	public void setAgreementKey(String agreementKey) {
		this.agreementKey = agreementKey;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAcceptZeT7() {
		return acceptZeT7;
	}

	public void setAcceptZeT7(String acceptZeT7) {
		this.acceptZeT7 = acceptZeT7;
	}

	public String getPrintfilenameRurn() {
		return printfilenameRurn;
	}

	public void setPrintfilenameRurn(String printfilenameRurn) {
		this.printfilenameRurn = printfilenameRurn;
	}

	public boolean isAcuseXT() {
		return acuseXT;
	}
	public void setAcuseXT(boolean acuseXT) {
		this.acuseXT = acuseXT;
	}

	public String getTrackingNoGenRet() {
		return trackingNoGenRet;
	}

	public void setTrackingNoGenRet(String trackingNoGenRet) {
		this.trackingNoGenRet = trackingNoGenRet;
	}

	public String getClientIdAgreement() {
		return clientIdAgreement;
	}

	public void setClientIdAgreement(String clientIdAgreement) {
		this.clientIdAgreement = clientIdAgreement;
	}

	public String getLocalService() {//AAP17
		return localService;//AAP17
	}//AAP17

	public void setLocalService(String localService) {//AAP17
		this.localService = localService;//AAP17
	}//AAP17

	public boolean isGenLocalService() {//AAP17
		return genLocalService;//AAP17
	}//AAP17

	public void setGenLocalService(boolean genLocalService) {//AAP17
		this.genLocalService = genLocalService;//AAP17
	}//AAP17

	public String getCatalogMbr() {//AAP18
		return catalogMbr;//AAP18
	}//AAP18

	public void setCatalogMbr(String catalogMbr) {//AAP18
		this.catalogMbr = catalogMbr;//AAP18
	}//AAP18

	public String getDisableMFGen() {//AAP18
		return disableMFGen;//AAP18
	}//AAP18

	public void setDisableMFGen(String disableMFGen) {//AAP18
		this.disableMFGen = disableMFGen;//AAP18
	}//AAP18

	public String getShowReprintGuia() {
		return showReprintGuia;
	}

	public void setShowReprintGuia(String showReprintGuia) {
		this.showReprintGuia = showReprintGuia;
	}

	public String getShowGuiasRR() {
		return showGuiasRR;
	}

	public void setShowGuiasRR(String showGuiasRR) {
		this.showGuiasRR = showGuiasRR;
	}

	public String getForceCaptureDimensions() {
		return forceCaptureDimensions;
	}

	public void setForceCaptureDimensions(String forceCaptureDimensions) {
		this.forceCaptureDimensions = forceCaptureDimensions;
	}

	public int getSStotal() {//AAP19
		return SStotal;
	}

	public void setSStotal(int sStotal) {//AAP19
		SStotal = sStotal;//AAP19
	}//AAP19

	public String getOrigionBorderBranch() {
		return origionBorderBranch;
	}

	public void setOrigionBorderBranch(String origionBorderBranch) {
		this.origionBorderBranch = origionBorderBranch;
	}

	public String getDestinationBorderBranch() {
		return destinationBorderBranch;
	}

	public void setDestinationBorderBranch(String destinationBorderBranch) {
		this.destinationBorderBranch = destinationBorderBranch;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getAllowRRFromXLS() {
		return allowRRFromXLS;
	}

	public void setAllowRRFromXLS(String allowRRFromXLS) {
		this.allowRRFromXLS = allowRRFromXLS;
	}

	public String getAllowPrintQZ() {
		return allowPrintQZ;
	}

	public void setAllowPrintQZ(String allowPrintQZ) {
		this.allowPrintQZ = allowPrintQZ;
	}

	public String getGroupClientIdDestino() {
		return groupClientIdDestino;
	}

	public void setGroupClientIdDestino(String groupClientIdDestino) {
		this.groupClientIdDestino = groupClientIdDestino;
	}

	public double getFactorDivisorPesoVol() {
		return factorDivisorPesoVol;
	}

	public void setFactorDivisorPesoVol(double factorDivisorPesoVol) {
		this.factorDivisorPesoVol = factorDivisorPesoVol;
	}

	public String getBrncIdWeb() {
		return brncIdWeb;
	}

	public void setBrncIdWeb(String brncIdWeb) {
		this.brncIdWeb = brncIdWeb;
	}

	public String getBrncNameValue() {
		return brncNameValue;
	}

	public void setBrncNameValue(String brncNameValue) {
		this.brncNameValue = brncNameValue;
	}



	public String getPrintWwPdfXls() {
		return printWwPdfXls;
	}

	public void setPrintWwPdfXls(String printWwPdfXls) {
		this.printWwPdfXls = printWwPdfXls;
	}

	public String getManifestTypeWE() {
		return manifestTypeWE;
	}

	public void setManifestTypeWE(String manifestTypeWE) {
		this.manifestTypeWE = manifestTypeWE;
	}

	public String getAllowCancelGuiaMult() {
		return allowCancelGuiaMult;
	}

	public void setAllowCancelGuiaMult(String allowCancelGuiaMult) {
		this.allowCancelGuiaMult = allowCancelGuiaMult;
	}

	public String getDaysCancelGMult() {
		return daysCancelGMult;
	}

	public void setDaysCancelGMult(String daysCancelGMult) {
		this.daysCancelGMult = daysCancelGMult;
	}



	/**
	 * @return the acceptRadZp
	 */
	public String getAcceptRadZp() {
		return acceptRadZp;
	}

	/**
	 * @param acceptRadZp the acceptRadZp to set
	 */
	public void setAcceptRadZp(String acceptRadZe) {
		this.acceptRadZp = acceptRadZe;
	}

	/**
	 * @return the acceptRadZpT7
	 */
	public String getAcceptRadZpT7() {
		return acceptRadZpT7;
	}

	/**
	 * @param acceptRadZpT7 the acceptRadZpT7 to set
	 */
	public void setAcceptRadZpT7(String acceptRadZeT7) {
		this.acceptRadZpT7 = acceptRadZeT7;
	}

	/**
	 * @return the serverName
	 */
	public String getServerName() {
		return serverName;
	}
	/**
	 * @return the pdfFormat
	 */
	public String getPdfFormat() {
		return pdfFormat;
	}

	/**
	 * @param serverName the serverName to set
	 */
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	
	/**
	 * @param pdfFormat the pdfFormat to set
	 */
	public void setPdfFormat(String pdfFormat) {
		this.pdfFormat = pdfFormat;
	}

	public String getShowGlobalMultCncl() {
		return showGlobalMultCncl;
	}
	public String getShowGlobalRR() {
		return showGlobalRR;
	}

	public void setShowGlobalMultCncl(String showGlobalMultCncl) {
		this.showGlobalMultCncl = showGlobalMultCncl;
	}


	public void setShowGlobalRR(String showGlobalRR) {
		this.showGlobalRR = showGlobalRR;
	}


}