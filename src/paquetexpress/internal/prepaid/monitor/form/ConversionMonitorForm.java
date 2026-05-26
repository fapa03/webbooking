package paquetexpress.internal.prepaid.monitor.form;

import java.util.ArrayList;

import org.apache.struts.action.ActionForm;

/**
 * @author darjona
 *
 */
public class ConversionMonitorForm extends ActionForm{	
	private static final long serialVersionUID = 1L;
	
	private String currentTask = ""; 
	
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
	private String accionDoc = "";
	private String clickedItems = "";
	private String currentGuia = "";
	private String banCerrar = "true";
	private String idSetSelOld = "";
	private String tipoAsignacion = "";	
	private String guiasAsigFlag = "";
	private String clientePropietario = "";
	private String esPropietario = "";
	private String cveSucursalAsig ="", desSucursalAsig = "", cveClienteAsig = "", desClienteAsig = "", cveUserAsig = "", desUserAsig = "";
	private String filtrarPor = "";
	private ArrayList filtrarPorValue = new ArrayList(), filtrarPorLabel= new ArrayList();//AAP08
	private String cbAll = "off";
	private String contRastreos = "0";
	private String genPDF = "N";
	private String clickedItemsPDF = "";
	private String urlRastreoPDF = "";
	private String shippingTypeSel = "";
	private String idSetSelPDF = "";
	private String locationTypeSel = "";//AAP20
	private String vigenciaSel = "";//AAP27
	private String branchId="";

	public String getCurrentTask() {
		return currentTask;
	}
	public void setCurrentTask(String currentTask) {
		this.currentTask = currentTask;
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
	public String getAccionDoc() {
		return accionDoc;
	}
	public void setAccionDoc(String accionDoc) {
		this.accionDoc = accionDoc;
	}
	public String getClickedItems() {
		return clickedItems;
	}
	public void setClickedItems(String clickedItems) {
		this.clickedItems = clickedItems;
	}
	public String getCurrentGuia() {
		return currentGuia;
	}
	public void setCurrentGuia(String currentGuia) {
		this.currentGuia = currentGuia;
	}
	public String getBanCerrar() {
		return banCerrar;
	}
	public void setBanCerrar(String banCerrar) {
		this.banCerrar = banCerrar;
	}
	public String getIdSetSelOld() {
		return idSetSelOld;
	}
	public void setIdSetSelOld(String idSetSelOld) {
		this.idSetSelOld = idSetSelOld;
	}
	public String getTipoAsignacion() {
		return tipoAsignacion;
	}
	public void setTipoAsignacion(String tipoAsignacion) {
		this.tipoAsignacion = tipoAsignacion;
	}
	public String getCveSucursalAsig() {
		return cveSucursalAsig;
	}
	public void setCveSucursalAsig(String cveSucursalAsig) {
		this.cveSucursalAsig = cveSucursalAsig;
	}
	public String getDesSucursalAsig() {
		return desSucursalAsig;
	}
	public void setDesSucursalAsig(String desSucursalAsig) {
		this.desSucursalAsig = desSucursalAsig;
	}
	public String getCveClienteAsig() {
		return cveClienteAsig;
	}
	public void setCveClienteAsig(String cveClienteAsig) {
		this.cveClienteAsig = cveClienteAsig;
	}
	public String getDesClienteAsig() {
		return desClienteAsig;
	}
	public void setDesClienteAsig(String desClienteAsig) {
		this.desClienteAsig = desClienteAsig;
	}
	public String getCveUserAsig() {
		return cveUserAsig;
	}
	public void setCveUserAsig(String cveUserAsig) {
		this.cveUserAsig = cveUserAsig;
	}
	public String getDesUserAsig() {
		return desUserAsig;
	}
	public void setDesUserAsig(String desUserAsig) {
		this.desUserAsig = desUserAsig;
	}
	public String getGuiasAsigFlag() {
		return guiasAsigFlag;
	}
	public void setGuiasAsigFlag(String guiasAsigFlag) {
		this.guiasAsigFlag = guiasAsigFlag;
	}
	public String getClientePropietario() {
		return clientePropietario;
	}
	public void setClientePropietario(String clientePropietario) {
		this.clientePropietario = clientePropietario;
	}
	public String getFiltrarPor() {
		return filtrarPor;
	}
	public void setFiltrarPor(String filtrarPor) {
		this.filtrarPor = filtrarPor;
	}
	public ArrayList getFiltrarPorValue(){		
		if (this.filtrarPorValue == null) {
			this.filtrarPorValue = new ArrayList();
		}		
		return this.filtrarPorValue;
	}
	
	public void setFiltrarPorValue(String centrosCostoValue){
		if ( this.filtrarPorValue == null ){			
			this.filtrarPorValue = new ArrayList(); 
			this.filtrarPorValue.add(centrosCostoValue);
		} else {
			this.filtrarPorValue.add(centrosCostoValue);
		}
	}
	
	public ArrayList getFiltrarPorLabel(){
		if (this.filtrarPorLabel == null) {
			this.filtrarPorLabel = new ArrayList();
		}		
		return this.filtrarPorLabel;
	}
	
	public void setFiltrarPorLabel(String centrosCostoLabel){
		if ( this.filtrarPorLabel == null ){
			this.filtrarPorLabel = new ArrayList(); 
			this.filtrarPorLabel.add(centrosCostoLabel);
		} 
		else{
			this.filtrarPorLabel.add(centrosCostoLabel);
		}
	}
	public void setFiltrarPorValue(ArrayList filtrarPorValue) {
		this.filtrarPorValue = filtrarPorValue;
	}
	public void setFiltrarPorLabel(ArrayList filtrarPorLabel) {
		this.filtrarPorLabel = filtrarPorLabel;
	}
	public String getEsPropietario() {
		return esPropietario;
	}
	public void setEsPropietario(String esPropietario) {
		this.esPropietario = esPropietario;
	}
	public String getCbAll() {
		return cbAll;
	}
	public void setCbAll(String cbAll) {
		this.cbAll = cbAll;
	}
	public String getContRastreos() {
		return contRastreos;
	}
	public void setContRastreos(String contRastreos) {
		this.contRastreos = contRastreos;
	}
	public String getGenPDF() {
		return genPDF;
	}
	public void setGenPDF(String genPDF) {
		this.genPDF = genPDF;
	}
	public String getClickedItemsPDF() {
		return clickedItemsPDF;
	}
	public void setClickedItemsPDF(String clickedItemsPDF) {
		this.clickedItemsPDF = clickedItemsPDF;
	}
	public String getUrlRastreoPDF() {
		return urlRastreoPDF;
	}
	public void setUrlRastreoPDF(String urlRastreoPDF) {
		this.urlRastreoPDF = urlRastreoPDF;
	}
	public String getShippingTypeSel() {
		return shippingTypeSel;
	}
	public void setShippingTypeSel(String shippingTypeSel) {
		this.shippingTypeSel = shippingTypeSel;
	}
	public String getIdSetSelPDF() {
		return idSetSelPDF;
	}
	public void setIdSetSelPDF(String idSetSelPDF) {
		this.idSetSelPDF = idSetSelPDF;
	}
	public String getLocationTypeSel() {
		return locationTypeSel;
	}
	public void setLocationTypeSel(String locationTypeSel) {
		this.locationTypeSel = locationTypeSel;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getVigenciaSel() {//AAP27
		return vigenciaSel;
	}
	public void setVigenciaSel(String vigenciaSel) {//AAP27
		this.vigenciaSel = vigenciaSel;
	}

    /**
     * @return String return the branchId
     */
    public String getBranchId() {
        return branchId;
    }

    /**
     * @param branchId the branchId to set
     */
    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

}