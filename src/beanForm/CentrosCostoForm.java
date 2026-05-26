package beanForm;
/**
 * File Name    : ClientDestinationEntryForm.java
 * Description  : This is the Form Class for Client destination Entry
 * Date Written : 13-May-2006
 * @author 	    :  Murugesapandian T
 * ----------------------------------------------------------------- 
 * MODIFICACIONES:  
 * -----------------------------------------------------------------
 * Clave: AAP01
 * Autor: ABRAHAM DANIEL ARJONA PERAZA.
 * Fecha: 04/07/2013
 * Descripción: Se agregaron variables rfc1, rfc2, rfc3 para captura y 
 * validaciones de rfc
 * ------------------------------------------------------------------ 
 */
import org.apache.struts.action.ActionForm;

public class CentrosCostoForm extends ActionForm {

	/**** Proprty Names for clientDestinationEntryForm *****/

	private String codigoCliente = null;
	/* added by Kavitha on 17may2011 Starts */
	private String codigoInt = null;
	private String email = null;
	/* added by Kavitha on 17may2011 Ends */
	private String tipoCliente = null;
	private String tipoClienteRef = null;
	private String nombre = null;
	private String assignedToBranch = null;
	private String assignedToBranchRef = null;
	private String telefono = null;
	private String rfc1 = null;//AAP01
	private String rfc2 = null;//AAP01
	private String rfc3 = null;//AAP01
	// String structCheck = "Y";
	private String estructurada = "Y";
	private String calle = null;
	private String numero = null;
	private String sucursal = null;
	private String sucursalRef = null;
	private String nombreRef = null;
	private String ciudad = null;
	private String colonia = null;
	private String codigoPostal = null;
	private String codigoPostalBut = null;
	private String estado = null;
	private String zona = null;
	private String municipio = null;
	private String delegacion = null;
	private String secto = null;
	private String pais = null;

	/**** check boxes ***/
	private String factura = null;
	private String ead = "N";
	private String fxc = null;
	private String rad = null;
	private String seguro = null;

	private String branchName = null;
	private String cityName = null;
	private String type = null;
	private String code = null;
	private String level = null;
	private String refNo = null;

	// hidden field(s) entries
	private String codigoClienteHid = null;
	private String codigoIntHid = null;

	private String mode = null;

	// Error messages list
	private String mesgType = null;
	private String mesgText = null;

	// EAD Check
	private String eadCheck = "N";

	// OUT paratmeters
	private String u11 = null;
	private String u12 = null;
	private String u13 = null;
	private String u14 = null;
	private String u15 = null;
	private String u16 = null;
	private String u17 = null;
	private String zipcode = null;
	private String c11 = null;
	private String c12 = null;
	private String c13 = null;
	private String c14 = null;
	private String c15 = null;
	private String c16 = null;

	// Postal hidden values for getting Postal address
	private String geLevl = null;
	private String geType = null;
	private String geCode = null;

	// for ErrorMessage by Susheel

	private String errorMessages = "";

	// added by bala
	private String coloniaLabel = null;
	private String estadoLabel = null;
	private String municipioLabel = null;
	private String ciudadLabel = null;
	private String codigoPostalLabel = null;
	private String zonaLabel = null;
	private String paisLabel = null;
	private String delegacionSecto = null;
	// added by bala

	private String bloqueaCodCliente = "";

	public String getCodigoInt() {
		return codigoInt;
	}

	public void setCodigoInt(String codigoInt) {
		this.codigoInt = codigoInt;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	/*** getter and setter method for ClientDestinationEntryForm */

	/**
	 * @return Returns the assignedToBranch.
	 */
	public String getAssignedToBranch() {
		return assignedToBranch;
	}

	/**
	 * @param assignedToBranch
	 *            The assignedToBranch to set.
	 */
	public void setAssignedToBranch(String assignedToBranch) {
		this.assignedToBranch = assignedToBranch;
	}

	/**
	 * @return Returns the calle.
	 */
	public String getCalle() {
		return calle;
	}

	/**
	 * @param calle
	 *            The calle to set.
	 */
	public void setCalle(String calle) {
		this.calle = calle;
	}

	/**
	 * @return Returns the ciudad.
	 */
	public String getCiudad() {
		return ciudad;
	}

	/**
	 * @param ciudad
	 *            The ciudad to set.
	 */
	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	/**
	 * @return Returns the codigoCliente.
	 */
	public String getCodigoCliente() {
		return codigoCliente;
	}

	/**
	 * @param codigoCliente
	 *            The codigoCliente to set.
	 */
	public void setCodigoCliente(String codigoCliente) {
		this.codigoCliente = codigoCliente;
	}

	/**
	 * @return Returns the codigoPostal.
	 */
	public String getCodigoPostal() {
		return codigoPostal;
	}

	/**
	 * @param codigoPostal
	 *            The codigoPostal to set.
	 */
	public void setCodigoPostal(String codigoPostal) {
		this.codigoPostal = codigoPostal;
	}

	/**
	 * @return Returns the colonia.
	 */
	public String getColonia() {
		return colonia;
	}

	/**
	 * @param colonia
	 *            The colonia to set.
	 */
	public void setColonia(String colonia) {
		this.colonia = colonia;
	}

	/**
	 * @return Returns the delegacion.
	 */
	public String getDelegacion() {
		return delegacion;
	}

	/**
	 * @param delegacion
	 *            The delegacion to set.
	 */
	public void setDelegacion(String delegacion) {
		this.delegacion = delegacion;
	}

	/**
	 * @return Returns the ead.
	 */
	public String getEad() {
		return ead;
	}

	/**
	 * @param ead
	 *            The ead to set.
	 */
	public void setEad(String ead) {
		this.ead = ead;
	}

	/**
	 * @return Returns the estado.
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * @param estado
	 *            The estado to set.
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * @return Returns the factura.
	 */
	public String getFactura() {
		return factura;
	}

	/**
	 * @param factura
	 *            The factura to set.
	 */
	public void setFactura(String factura) {
		this.factura = factura;
	}

	/**
	 * @return Returns the fxc.
	 */
	public String getFxc() {
		return fxc;
	}

	/**
	 * @param fxc
	 *            The fxc to set.
	 */
	public void setFxc(String fxc) {
		this.fxc = fxc;
	}

	/**
	 * @return Returns the municipio.
	 */
	public String getMunicipio() {
		return municipio;
	}

	/**
	 * @param municipio
	 *            The municipio to set.
	 */
	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}

	/**
	 * @return Returns the nombre.
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param nombre
	 *            The nombre to set.
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * @return Returns the numero.
	 */
	public String getNumero() {
		return numero;
	}

	/**
	 * @param numero
	 *            The numero to set.
	 */
	public void setNumero(String numero) {
		this.numero = numero;
	}

	/**
	 * @return Returns the pais.
	 */
	public String getPais() {
		return pais;
	}

	/**
	 * @param pais
	 *            The pais to set.
	 */
	public void setPais(String pais) {
		this.pais = pais;
	}

	/**
	 * @return Returns the rad.
	 */
	public String getRad() {
		return rad;
	}

	/**
	 * @param rad
	 *            The rad to set.
	 */
	public void setRad(String rad) {
		this.rad = rad;
	}

	/**
	 * @return Returns the rfc1.
	 */
	public String getRfc1() {//AAP01
		return rfc1;
	}

	/**
	 * @param rfc1
	 *            The rfc1 to set.
	 */
	public void setRfc1(String rfc1) {//AAP01
		this.rfc1 = rfc1;
	}

	/**
	 * @return Returns the rfc2.
	 */
	public String getRfc2() {//AAP01
		return rfc2;
	}

	/**
	 * @param rfc2
	 *            The rfc2 to set.
	 */
	public void setRfc2(String rfc2) {//AAP01
		this.rfc2 = rfc2;
	}
	
	/**
	 * @return Returns the rfc3.
	 */
	public String getRfc3() {//AAP01
		return rfc3;
	}

	/**
	 * @param rfc3
	 *            The rfc3 to set.
	 */
	public void setRfc3(String rfc3) {
		this.rfc3 = rfc3;
	}
	
	/**
	 * @return Returns the secto.
	 */
	public String getSecto() {
		return secto;
	}

	/**
	 * @param secto
	 *            The secto to set.
	 */
	public void setSecto(String secto) {
		this.secto = secto;
	}

	/**
	 * @return Returns the seguro.
	 */
	public String getSeguro() {
		return seguro;
	}

	/**
	 * @param seguro
	 *            The seguro to set.
	 */
	public void setSeguro(String seguro) {
		this.seguro = seguro;
	}

	/**
	 * @return Returns the sucursal.
	 */
	public String getSucursal() {
		return sucursal;
	}

	/**
	 * @param sucursal
	 *            The sucursal to set.
	 */
	public void setSucursal(String sucursal) {
		this.sucursal = sucursal;
	}

	/**
	 * @return Returns the sucursalRef.
	 */
	public String getSucursalRef() {
		return sucursalRef;
	}

	/**
	 * @param sucursalRef
	 *            The sucursalRef to set.
	 */
	public void setSucursalRef(String sucursalRef) {
		this.sucursalRef = sucursalRef;
	}

	/**
	 * @return Returns the telefono.
	 */
	public String getTelefono() {
		return telefono;
	}

	/**
	 * @param telefono
	 *            The telefono to set.
	 */
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	/**
	 * @return Returns the tipoCliente.
	 */
	public String getTipoCliente() {
		return tipoCliente;
	}

	/**
	 * @param tipoCliente
	 *            The tipoCliente to set.
	 */
	public void setTipoCliente(String tipoCliente) {
		this.tipoCliente = tipoCliente;
	}

	/**
	 * @return Returns the zona.
	 */
	public String getZona() {
		return zona;
	}

	/**
	 * @param zona
	 *            The zona to set.
	 */
	public void setZona(String zona) {
		this.zona = zona;
	}

	/**
	 * @return Returns the mode.
	 */
	public String getMode() {
		return mode;
	}

	/**
	 * @param mode
	 *            The mode to set.
	 */
	public void setMode(String mode) {
		this.mode = mode;
	}

	/**
	 * @return Returns the assignedToBranchRef.
	 */
	public String getAssignedToBranchRef() {
		return assignedToBranchRef;
	}

	/**
	 * @param assignedToBranchRef
	 *            The assignedToBranchRef to set.
	 */
	public void setAssignedToBranchRef(String assignedToBranchRef) {
		this.assignedToBranchRef = assignedToBranchRef;
	}

	/**
	 * @return Returns the nombreRef.
	 */
	public String getNombreRef() {
		return nombreRef;
	}

	/**
	 * @param nombreRef
	 *            The nombreRef to set.
	 */
	public void setNombreRef(String nombreRef) {
		this.nombreRef = nombreRef;
	}

	/**
	 * @return Returns the tipoClienteRef.
	 */
	public String getTipoClienteRef() {
		return tipoClienteRef;
	}

	/**
	 * @param tipoClienteRef
	 *            The tipoClienteRef to set.
	 */
	public void setTipoClienteRef(String tipoClienteRef) {
		this.tipoClienteRef = tipoClienteRef;
	}

	// By Susheel for extra Bean Classes

	/**
	 * @return Returns the branchName.
	 */
	public String getBranchName() {
		return branchName;
	}

	/**
	 * @param branchName
	 *            The branchName to set.
	 */
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	/**
	 * @return Returns the cityName.
	 */
	public String getCityName() {
		return cityName;
	}

	/**
	 * @param cityName
	 *            The cityName to set.
	 */
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	/**
	 * @return Returns the code.
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code
	 *            The code to set.
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return Returns the level.
	 */
	public String getLevel() {
		return level;
	}

	/**
	 * @param level
	 *            The level to set.
	 */
	public void setLevel(String level) {
		this.level = level;
	}

	/**
	 * @return Returns the type.
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            The type to set.
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return Returns the codigoClienteHid.
	 */
	public String getCodigoClienteHid() {
		return codigoClienteHid;
	}

	/**
	 * @param codigoClienteHid
	 *            The type to set.
	 */
	public void setCodigoClienteHid(String codigoClienteHid) {
		this.codigoClienteHid = codigoClienteHid;
	}

	/**
	 * @return Returns the codigoIntHid.
	 */

	public String getCodigoIntHid() {
		return codigoIntHid;
	}

	/**
	 * @param codigoIntHid
	 *            The type to set.
	 */
	public void setCodigoIntHid(String codigoIntHid) {
		this.codigoIntHid = codigoIntHid;
	}

	/**
	 * @return Returns the mesgType.
	 */
	public String getMesgType() {
		return mesgType;
	}

	/**
	 * @param mesgType
	 *            The mesgType to set.
	 */
	public void setMesgType(String mesgType) {
		this.mesgType = mesgType;
	}

	/**
	 * @return Returns the codigoClienteHid.
	 */
	public String getMesgText() {
		return mesgText;
	}

	/**
	 * @param mesgText
	 *            The type to set.
	 */
	public void setMesgText(String mesgText) {
		this.mesgText = mesgText;
	}

	/**
	 * @return Returns the eadCheck.
	 */
	public String getEadCheck() {
		return eadCheck;
	}

	/**
	 * @param eadCheck
	 *            The type to set.
	 */
	public void setEadCheck(String eadCheck) {
		this.eadCheck = eadCheck;
	}

	/**
	 * @return Returns the refNo.
	 */
	public String getRefNo() {
		return refNo;
	}

	/**
	 * @param refNo
	 *            The refNo to set.
	 */
	public void setRefNo(String refNo) {
		this.refNo = refNo;
	}

	/**
	 * @return Returns the c11.
	 */
	public String getC11() {
		return c11;
	}

	/**
	 * @param c11
	 *            The c11 to set.
	 */
	public void setC11(String c11) {
		this.c11 = c11;
	}

	/**
	 * @return Returns the c12.
	 */
	public String getC12() {
		return c12;
	}

	/**
	 * @param c12
	 *            The c12 to set.
	 */
	public void setC12(String c12) {
		this.c12 = c12;
	}

	/**
	 * @return Returns the c13.
	 */
	public String getC13() {
		return c13;
	}

	/**
	 * @param c13
	 *            The c13 to set.
	 */
	public void setC13(String c13) {
		this.c13 = c13;
	}

	/**
	 * @return Returns the c14.
	 */
	public String getC14() {
		return c14;
	}

	/**
	 * @param c14
	 *            The c14 to set.
	 */
	public void setC14(String c14) {
		this.c14 = c14;
	}

	/**
	 * @return Returns the c15.
	 */
	public String getC15() {
		return c15;
	}

	/**
	 * @param c15
	 *            The c15 to set.
	 */
	public void setC15(String c15) {
		this.c15 = c15;
	}

	/**
	 * @return Returns the c16.
	 */
	public String getC16() {
		return c16;
	}

	/**
	 * @param c16
	 *            The c16 to set.
	 */
	public void setC16(String c16) {
		this.c16 = c16;
	}

	/**
	 * @return Returns the u11.
	 */
	public String getU11() {
		return u11;
	}

	/**
	 * @param u11
	 *            The u11 to set.
	 */
	public void setU11(String u11) {
		this.u11 = u11;
	}

	/**
	 * @return Returns the u12.
	 */
	public String getU12() {
		return u12;
	}

	/**
	 * @param u12
	 *            The u12 to set.
	 */
	public void setU12(String u12) {
		this.u12 = u12;
	}

	/**
	 * @return Returns the u13.
	 */
	public String getU13() {
		return u13;
	}

	/**
	 * @param u13
	 *            The u13 to set.
	 */
	public void setU13(String u13) {
		this.u13 = u13;
	}

	/**
	 * @return Returns the u14.
	 */
	public String getU14() {
		return u14;
	}

	/**
	 * @param u14
	 *            The u14 to set.
	 */
	public void setU14(String u14) {
		this.u14 = u14;
	}

	/**
	 * @return Returns the u15.
	 */
	public String getU15() {
		return u15;
	}

	/**
	 * @param u15
	 *            The u15 to set.
	 */
	public void setU15(String u15) {
		this.u15 = u15;
	}

	/**
	 * @return Returns the u16.
	 */
	public String getU16() {
		return u16;
	}

	/**
	 * @param u16
	 *            The u16 to set.
	 */
	public void setU16(String u16) {
		this.u16 = u16;
	}

	/**
	 * @return Returns the u17.
	 */
	public String getU17() {
		return u17;
	}

	/**
	 * @param u17
	 *            The u17 to set.
	 */
	public void setU17(String u17) {
		this.u17 = u17;
	}

	/**
	 * @return Returns the zipcode.
	 */
	public String getZipcode() {
		return zipcode;
	}

	/**
	 * @param zipcode
	 *            The zipcode to set.
	 */
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	/**
	 * @return Returns the estructurada.
	 */
	public String getEstructurada() {
		return estructurada;
	}

	/**
	 * @param zipcode
	 *            The zipcode to set.
	 */
	public void setEstructurada(String estructurada) {
		this.estructurada = estructurada;
	}

	/**
	 * @return Returns the geLevl.
	 */
	public String getGeLevl() {
		return geLevl;
	}

	/**
	 * @param geLevl
	 *            The geLevl to set.
	 */
	public void setGeLevl(String geLevl) {
		this.geLevl = geLevl;
	}

	/**
	 * @return Returns the geType.
	 */
	public String getGeType() {
		return geType;
	}

	/**
	 * @param geType
	 *            The geType to set.
	 */
	public void setGeType(String geType) {
		this.geType = geType;
	}

	/**
	 * @return Returns the geCode.
	 */
	public String getGeCode() {
		return geCode;
	}

	/**
	 * @param geCode
	 *            The geCode to set.
	 */
	public void setGeCode(String geCode) {
		this.geCode = geCode;
	}

	/**
	 * @return Returns the codigoPostalBut.
	 */
	public String getCodigoPostalBut() {
		return codigoPostalBut;
	}

	/**
	 * @param codigoPostalBut
	 *            The codigoPostalBut to set.
	 */
	public void setCodigoPostalBut(String codigoPostalBut) {
		this.codigoPostalBut = codigoPostalBut;
	}

	/**
	 * @return Returns the errorMessages.
	 */
	public String getErrorMessages() {
		return errorMessages;
	}

	/**
	 * @param errorMessages
	 *            The errorMessages to set.
	 */
	public void setErrorMessages(String errorMessages) {
		this.errorMessages = errorMessages;
	}

	// added by Bala

	public String getColoniaLabel() {
		return coloniaLabel;
	}

	public void setColoniaLabel(String coloniaLabel) {
		this.coloniaLabel = coloniaLabel;
	}

	public String getEstadoLabel() {
		return estadoLabel;
	}

	public void setEstadoLabel(String estadoLabel) {
		this.estadoLabel = estadoLabel;
	}

	public String getMunicipioLabel() {
		return municipioLabel;
	}

	public void setMunicipioLabel(String municipioLabel) {
		this.municipioLabel = municipioLabel;
	}

	public String getCiudadLabel() {
		return ciudadLabel;
	}

	public void setCiudadLabel(String ciudadLabel) {
		this.ciudadLabel = ciudadLabel;
	}

	public String getCodigoPostalLabel() {
		return codigoPostalLabel;
	}

	public void setCodigoPostalLabel(String codigoPostalLabel) {
		this.codigoPostalLabel = codigoPostalLabel;
	}

	public String getZonaLabel() {
		return zonaLabel;
	}

	public void setZonaLabel(String zonaLabel) {
		this.zonaLabel = zonaLabel;
	}

	public String getPaisLabel() {
		return paisLabel;
	}

	public void setPaisLabel(String paisLabel) {
		this.paisLabel = paisLabel;
	}

	public String getDelegacionSecto() {
		return delegacionSecto;
	}

	public void setDelegacionSecto(String delegacionSecto) {
		this.delegacionSecto = delegacionSecto;
	}

	// ended by bala
	public String getBloqueaCodCliente() {
		return bloqueaCodCliente;
	}

	public void setBloqueaCodCliente(String bloqueaCodCliente) {
		this.bloqueaCodCliente = bloqueaCodCliente;
	}

}
