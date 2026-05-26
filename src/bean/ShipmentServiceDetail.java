 package bean;

import beanAction.JavWebBookingGeneralMainServiceDetail.DatosCalculosTarifaSEG;

public class ShipmentServiceDetail{
	
	public String cantidad;
	public String descripcion;
	public String contenido;
	public String tarifa;
	public String peso;
	public String volumen;
	public String importe;
	public String serviceId;	
	public String refServiceId;
	public String value1;
	public String value2;
	public String descripcionCode;
	public String costoTarifaBase;
	public String mostrarDesCuento;
	public String convenioAlto = "N";
	public String volL = "";
	public String volH = "";
	public String volW = "";
	public String weightVolumetric = "";// JSA01
	public String companyID;//JSA02
	private DatosCalculosTarifaSEG datosCalculosTarifaSEG;
	private DatosCalculosTarifaSEG datosCalculosTarifaSEGPISO;
	private String productIdSat = "";
	private String productDescSat = "";
	
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
		return peso.isEmpty() ? "0" : peso;
	}
	public void setPeso(String peso) {
		this.peso = peso;
	}
	public String getVolumen() {
		return volumen.isEmpty() ? "0" : volumen;
	}
	public void setVolumen(String volumen) {
		this.volumen = volumen;
	}
	public String getImporte() {
		return importe;
	}
	public void setImporte(String importe) {
		this.importe = importe;
	}
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public String getRefServiceId() {
		return refServiceId;
	}
	public void setRefServiceId(String refServiceId) {
		this.refServiceId = refServiceId;
	}
	public String getValue1() {
		return value1;
	}
	public void setValue1(String value1) {
		this.value1 = value1;
	}
	public String getValue2() {
		return value2;
	}
	public void setValue2(String value2) {
		this.value2 = value2;
	}
	public String getDescripcionCode() {
		return descripcionCode;
	}
	public void setDescripcionCode(String descripcionCode) {
		this.descripcionCode = descripcionCode;
	}
	public String getCostoTarifaBase() {
		return costoTarifaBase;
	}
	public void setCostoTarifaBase(String costoTarifaBase) {
		this.costoTarifaBase = costoTarifaBase;
	}
	public String getMostrarDesCuento() {
		return mostrarDesCuento;
	}
	public void setMostrarDesCuento(String mostrarDesCuento) {
		this.mostrarDesCuento = mostrarDesCuento;
	}
	public String getConvenioAlto() {
		return convenioAlto;
	}
	public void setConvenioAlto(String convenioAlto) {
		this.convenioAlto = convenioAlto;
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
		return weightVolumetric;
	}
	public void setWeightVolumetric(String weightVolumetric) {
		this.weightVolumetric = weightVolumetric;
	}
	public String getCompanyID() {
		return companyID;
	}
	public void setCompanyID(String companyID) {
		this.companyID = companyID;
	}
	public DatosCalculosTarifaSEG getDatosCalculosTarifaSEG() {
		return datosCalculosTarifaSEG;
	}
	public void setDatosCalculosTarifaSEG(
			DatosCalculosTarifaSEG datosCalculosTarifaSEG) {
		this.datosCalculosTarifaSEG = datosCalculosTarifaSEG;
	}
	public DatosCalculosTarifaSEG getDatosCalculosTarifaSEGPISO() {
		return datosCalculosTarifaSEGPISO;
	}
	public void setDatosCalculosTarifaSEGPISO(
			DatosCalculosTarifaSEG datosCalculosTarifaSEGPISO) {
		this.datosCalculosTarifaSEGPISO = datosCalculosTarifaSEGPISO;
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
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ShipmentServiceDetail [cantidad=" + cantidad + ", descripcion=" + descripcion + ", contenido="
				+ contenido + ", tarifa=" + tarifa + ", peso=" + peso + ", volumen=" + volumen + ", importe=" + importe
				+ ", serviceId=" + serviceId + ", refServiceId=" + refServiceId + ", value1=" + value1 + ", value2="
				+ value2 + ", descripcionCode=" + descripcionCode + ", costoTarifaBase=" + costoTarifaBase
				+ ", mostrarDesCuento=" + mostrarDesCuento + ", convenioAlto=" + convenioAlto + ", volL=" + volL
				+ ", volH=" + volH + ", volW=" + volW + ", weightVolumetric=" + weightVolumetric + ", companyID="
				+ companyID + ", datosCalculosTarifaSEG=" + datosCalculosTarifaSEG + ", datosCalculosTarifaSEGPISO="
				+ datosCalculosTarifaSEGPISO + ", productIdSat=" + productIdSat + "]";
	}
	
}