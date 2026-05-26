 package bean;
/*----------------------------------------------------------
Author					:	V.Ramachandran
Date					:	25-March-2003
FileName				:	JavWebBookingServicesDetailAddForm1.java
SessionVariables		:
Other Used Classes		:
Function Names			:
------------------------------------------------------------*/

import org.apache.struts.action.ActionForm;

public class JavWebBookingServicesDetailAddForm1 extends ActionForm {	
	
	String cantidad,descripcion,contenido,peso,volumen,pesoDB,volumenDB;//AAP02
	
	String ss_srvc_id,ss_refr_srvc_id,descripcioncode,importe;
	String specialTariff="";
	String pesoMax = "";
	
	public String getSpecialTariff() {
		return specialTariff;
	}

	public void setSpecialTariff(String specialTariff) {
		this.specialTariff = specialTariff;
	}
	public JavWebBookingServicesDetailAddForm1(){
		//AAP//AccessLog.Log("JavWebBookingServicesDetailAddForm1");
	}
	
	public String getCantidad(){
		return this.cantidad;
	}
	public void setCantidad(String cantidad){		
		this.cantidad=cantidad;
	}
	
	public String getDescripcion(){
		return this.descripcion;
	}
	public void setDescripcion(String descripcion){		
		this.descripcion=descripcion;
	}
	
	public String getContenido(){
		return this.contenido;
	}
	public void setContenido(String contenido){		
		this.contenido=contenido;
	}
	
	public String getPeso(){
		return this.peso;
	}
	public void setPeso(String peso){		
		this.peso=peso;
	}
	
	public String getVolumen(){
		return this.volumen;
	}
	public void setVolumen(String volumen){		
		this.volumen=volumen;
	}
	
	public String getSs_srvc_id(){
		return this.ss_srvc_id;
	}
	public void setSs_srvc_id(String ss_srvc_id){		
		this.ss_srvc_id=ss_srvc_id;
	}
	
	public String getSs_refr_srvc_id(){
		return this.ss_refr_srvc_id;
	}
	public void setSs_refr_srvc_id(String ss_refr_srvc_id){		
		this.ss_refr_srvc_id=ss_refr_srvc_id;
	}
	
	public String getDescripcioncode(){
		return this.descripcioncode;
	}
	public void setDescripcioncode(String descripcioncode){		
		this.descripcioncode=descripcioncode;
	}
	
	public String getImporte(){
		return this.importe;
	}
	public void setImporte(String importe){		
		this.importe=importe;
	}
	public String getPesoDB() {//AAP02
		return pesoDB;
	}

	public void setPesoDB(String pesoDB) {//AAP02
		this.pesoDB = pesoDB;
	}

	public String getVolumenDB() {//AAP02
		return volumenDB;
	}

	public void setVolumenDB(String volumenDB) {//AAP02
		this.volumenDB = volumenDB;
	}

	public String getPesoMax() {
		return pesoMax;
	}

	public void setPesoMax(String pesoMax) {
		this.pesoMax = pesoMax;
	}
	
}