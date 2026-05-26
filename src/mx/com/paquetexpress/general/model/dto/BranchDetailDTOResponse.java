package mx.com.paquetexpress.general.model.dto;

import java.io.Serializable;

public class BranchDetailDTOResponse implements Serializable, Cloneable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public String clave;
	public String nombre;
	public String tipoSuc;
	public String codigoPostal;
	public String colonia;
	public String calle;
	public String numero;
	public String ciudad;
	public String estado;
	public String municipio;
	public String pais;
	public String telefono1;
	public String telefono2;
	public String telefono3;
	public String telefono4;
	public String telefono5;
	public String localizaLatitud;
	public String localizaLongitud;
	public String horario;
	public String largo;
	public String ancho;
	public String alto;
	public String peso;
	public String volumen;

	/**
	 * 
	 */
	public BranchDetailDTOResponse() {
		super();
	}

	public BranchDetailDTOResponse(String clave, String nombre, String tipoSuc, String codigoPostal, String colonia,
			String calle, String numero, String ciudad, String estado, String municipio, String pais, String telefono1,
			String telefono2, String telefono3, String telefono4, String telefono5, String localizaLatitud,
			String localizaLongitud, String horario, String largo, String ancho, String alto, String peso,
			String volumen) {
		super();
		this.clave = clave;
		this.nombre = nombre;
		this.tipoSuc = tipoSuc;
		this.codigoPostal = codigoPostal;
		this.colonia = colonia;
		this.calle = calle;
		this.numero = numero;
		this.ciudad = ciudad;
		this.estado = estado;
		this.municipio = municipio;
		this.pais = pais;
		this.telefono1 = telefono1;
		this.telefono2 = telefono2;
		this.telefono3 = telefono3;
		this.telefono4 = telefono4;
		this.telefono5 = telefono5;
		this.localizaLatitud = localizaLatitud;
		this.localizaLongitud = localizaLongitud;
		this.horario = horario;
		this.largo = largo;
		this.ancho = ancho;
		this.alto = alto;
		this.peso = peso;
		this.volumen = volumen;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getTipoSuc() {
		return tipoSuc;
	}

	public void setTipoSuc(String tipoSuc) {
		this.tipoSuc = tipoSuc;
	}

	public String getCodigoPostal() {
		return codigoPostal;
	}

	public void setCodigoPostal(String codigoPostal) {
		this.codigoPostal = codigoPostal;
	}

	public String getColonia() {
		return colonia;
	}

	public void setColonia(String colonia) {
		this.colonia = colonia;
	}

	public String getCalle() {
		return calle;
	}

	public void setCalle(String calle) {
		this.calle = calle;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getCiudad() {
		return ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getMunicipio() {
		return municipio;
	}

	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	public String getTelefono1() {
		return telefono1;
	}

	public void setTelefono1(String telefono1) {
		this.telefono1 = telefono1;
	}

	public String getTelefono2() {
		return telefono2;
	}

	public void setTelefono2(String telefono2) {
		this.telefono2 = telefono2;
	}

	public String getTelefono3() {
		return telefono3;
	}

	public void setTelefono3(String telefono3) {
		this.telefono3 = telefono3;
	}

	public String getTelefono4() {
		return telefono4;
	}

	public void setTelefono4(String telefono4) {
		this.telefono4 = telefono4;
	}

	public String getTelefono5() {
		return telefono5;
	}

	public void setTelefono5(String telefono5) {
		this.telefono5 = telefono5;
	}

	public String getLocalizaLatitud() {
		return localizaLatitud;
	}

	public void setLocalizaLatitud(String localizaLatitud) {
		this.localizaLatitud = localizaLatitud;
	}

	public String getLocalizaLongitud() {
		return localizaLongitud;
	}

	public void setLocalizaLongitud(String localizaLongitud) {
		this.localizaLongitud = localizaLongitud;
	}

	public String getHorario() {
		return horario;
	}

	public void setHorario(String horario) {
		this.horario = horario;
	}

	public String getLargo() {
		return largo;
	}

	public void setLargo(String largo) {
		this.largo = largo;
	}

	public String getAncho() {
		return ancho;
	}

	public void setAncho(String ancho) {
		this.ancho = ancho;
	}

	public String getAlto() {
		return alto;
	}

	public void setAlto(String alto) {
		this.alto = alto;
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

	@Override
	public String toString() {
		return "BranchDetailDTOResponse [clave=" + clave + ", nombre=" + nombre + ", tipoSuc=" + tipoSuc
				+ ", codigoPostal=" + codigoPostal + ", colonia=" + colonia + ", calle=" + calle + ", numero=" + numero
				+ ", ciudad=" + ciudad + ", estado=" + estado + ", municipio=" + municipio + ", pais=" + pais
				+ ", telefono1=" + telefono1 + ", telefono2=" + telefono2 + ", telefono3=" + telefono3 + ", telefono4="
				+ telefono4 + ", telefono5=" + telefono5 + ", localizaLatitud=" + localizaLatitud
				+ ", localizaLongitud=" + localizaLongitud + ", horario=" + horario + ", largo=" + largo + ", ancho="
				+ ancho + ", alto=" + alto + ", peso=" + peso + ", volumen=" + volumen + "]";
	}

	
}
