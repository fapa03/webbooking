 package bean;
/*----------------------------------------------------------
Author					:	V.Ramachandran
Date					:	24-March-2003
FileName				:	JavWebBookingGeneralForm.java
SessionVariables		:
Other Used Classes		:
Function Names			:
------------------------------------------------------------*/

import org.apache.struts.action.ActionForm;
public class JavWebBookingGeneralForm extends ActionForm {
	
	public String orgioncode;
	String	orgionbranch,destinationbranch,orgienclave,orgiennombre,orgien1,orgien2,orgiencolonia1;
	public String destinationcode;
	String	orgiencolonia2,orgienrfc,orgientelefono,destinationclave,destinationnombre,destino1,destino2;
	private String destinationzipcode;//aap01 agregue destinationzipcode
	String	destinationcolonia1,destinationcolonia2,destinationrfc,destinationtelefono,fiscal1,fiscal2,fiscalcolonia1,fiscalcolonia2,fiscaltelefono;
	public String destinationcoloniacode;
	String	am_addr_code,orgionaddresscode,destinationaddresscode,fiscaladdresscode,borderbranchcheck;
	public String citycode;
	String	pedinumber,custagent,ship_seqn,no_ship,cur_loc,cur_dest,lc_rout,shiperrmsg;
	String orginsite;
	public String getycode="";
	public String getytype="";
	public String getylevl="";
	String orginbranchcode;
	String destinationsitecode,destinationsite;
	public String orginCityCode;
	//CODE ADEED ON 29/03/2004
	public String originColinaCode;
	
	public JavWebBookingGeneralForm() {
		//AAP//AccessLog.Log("JavWebBookingGeneralForm");
    }
	
	public String getOriginColinaCode(){
		 return this.originColinaCode;
	}
	public void setOriginColinaCode( String orginColinaCode){
		 this.originColinaCode = orginColinaCode;
	}	
	
	public String getOrginCityCode(){
		 return this.orginCityCode;
	}
	public void setOrginCityCode( String orginCode){
		 this.orginCityCode = orginCode;
	}		
	public String getOrgioncode(){
		return this.orgioncode;
	}
	public void setOrgioncode(String orgioncode){
		this.orgioncode=orgioncode;
	}
	
	public String getOrgionbranch(){
		return this.orgionbranch;
	}
	public void setOrgionbranch(String orgionbranch){
		this.orgionbranch=orgionbranch;
	}
	
	public String getDestinationcode(){
		return this.destinationcode;
	}
	public void setDestinationcode(String destinationcode){
		this.destinationcode=destinationcode;
	}
	
	public String getDestinationbranch(){
		return this.destinationbranch;
	}
	public void setDestinationbranch(String destinationbranch){
		this.destinationbranch=destinationbranch;
	}
	
	public String getOrgienclave(){
		return this.orgienclave;
	}
	public void setOrgienclave(String orgienclave){
		this.orgienclave=orgienclave;
	}
	
	public String getOrgiennombre(){
		return this.orgiennombre;
	}
	public void setOrgiennombre(String orgiennombre){
		this.orgiennombre=orgiennombre;
	}
	
	public String getOrgien1(){
		return this.orgien1;
	}
	public void setOrgien1(String orgien1){
		this.orgien1=orgien1;
	}
	
	public String getOrgien2(){
		return this.orgien2;
	}
	public void setOrgien2(String orgien2){
		this.orgien2=orgien2;
	}
	
	public String getOrgiencolonia1(){
		return this.orgiencolonia1;
	}
	public void setOrgiencolonia1(String orgiencolonia1){
		this.orgiencolonia1=orgiencolonia1;
	}
	
	public String getOrgiencolonia2(){
		return this.orgiencolonia2;
	}
	public void setOrgiencolonia2(String orgiencolonia2){
		this.orgiencolonia2=orgiencolonia2;
	}
	
	public String getOrgienrfc(){
		return this.orgienrfc;
	}
	public void setOrgienrfc(String orgienrfc){
		this.orgienrfc=orgienrfc;
	}
	
	
	public String getOrgientelefono(){
		return this.orgientelefono;
	}
	public void setOrgientelefono(String orgientelefono){
		this.orgientelefono=orgientelefono;
	}
	
	public String getDestinationclave(){
		return this.destinationclave;
	}
	public void setDestinationclave(String destinationclave){
		this.destinationclave=destinationclave;
	}
	
	public String getDestinationnombre(){
		return this.destinationnombre;
	}
	public void setDestinationnombre(String destinationnombre){
		this.destinationnombre=destinationnombre;
	}
	
	
	public String getDestino1(){
		return this.destino1;
	}
	public void setDestino1(String destino1){
		this.destino1=destino1;
	}
	
	public String getDestino2(){
		return this.destino2;
	}
	public void setDestino2(String destino2){
		this.destino2=destino2;
	}
	
	
	public String getDestinationcolonia1(){
		return this.destinationcolonia1;
	}
	public void setDestinationcolonia1(String destinationcolonia1){
		this.destinationcolonia1=destinationcolonia1;
	}
	
	public String getDestinationcolonia2(){
		return this.destinationcolonia2;
	}
	public void setDestinationcolonia2(String destinationcolonia2){
		this.destinationcolonia2=destinationcolonia2;
	}
	
	
	public String getDestinationrfc(){
		return this.destinationrfc;
	}
	public void setDestinationrfc(String destinationrfc){
		this.destinationrfc=destinationrfc;
	}
	
	public String getDestinationtelefono(){
		return this.destinationtelefono;
	}
	public void setDestinationtelefono(String destinationtelefono){
		this.destinationtelefono=destinationtelefono;
	}
	
	public String getFiscal1(){
		return this.fiscal1;
	}
	public void setFiscal1(String fiscal1){
		this.fiscal1=fiscal1;
	}
	
	public String getFiscal2(){
		return this.fiscal2;
	}
	public void setFiscal2(String fiscal2){
		this.fiscal2=fiscal2;
	}
	
	public String getFiscalcolonia1(){
		return this.fiscalcolonia1;
	}
	public void setFiscalcolonia1(String fiscalcolonia1){
		this.fiscalcolonia1=fiscalcolonia1;
	}
	
	public String getFiscalcolonia2(){
		return this.fiscalcolonia2;
	}
	public void setFiscalcolonia2(String fiscalcolonia2){
		this.fiscalcolonia2=fiscalcolonia2;
	}
	
	public String getFiscaltelefono(){
		return this.fiscaltelefono;
	}
	public void setFiscaltelefono(String fiscaltelefono){
		this.fiscaltelefono=fiscaltelefono;
	} 
	
	public String getDestinationcoloniacode(){
		return this.destinationcoloniacode;
	}
	public void setDestinationcoloniacode(String destinationcoloniacode){
		this.destinationcoloniacode=destinationcoloniacode;
	}
	
	public String getCitycode(){
		return this.citycode;
	}
	public void setCitycode(String citycode){
		this.citycode=citycode;
	}
	
	public String getDestinationaddresscode(){
		return this.destinationaddresscode;
	}
	public void setDestinationaddresscode(String destinationaddresscode){
		this.destinationaddresscode=destinationaddresscode;
	}
	
	public String getFiscaladdresscode(){
		return this.fiscaladdresscode;
	}
	public void setFiscaladdresscode(String fiscaladdresscode){
		this.fiscaladdresscode=fiscaladdresscode;
	}
	
	public String getOrgionaddresscode(){
		return this.orgionaddresscode;
	}
	public void setOrgionaddresscode(String orgionaddresscode){
		this.orgionaddresscode=orgionaddresscode;
	}
	
	public String getBorderbranchcheck(){
		return this.borderbranchcheck;
	}
	public void setBorderbranchcheck(String borderbranchcheck){
		this.borderbranchcheck=borderbranchcheck;
	}
	
	public String getPedinumber(){
		return this.pedinumber;
	}
	public void setPedinumber(String pedinumber){
		this.pedinumber=pedinumber;
	}
	
	public String getCustagent(){
		return this.custagent;
	}
	public void setCustagent(String custagent){
		this.custagent=custagent;
	}
	
	public String getShip_seqn(){
		return this.ship_seqn;
	}
	public void setShip_seqn(String ship_seqn){
		this.ship_seqn=ship_seqn;
	}
	
	public String getNo_ship(){
		return this.no_ship;
	}
	public void setNo_ship(String no_ship){
		this.no_ship=no_ship;
	}
	
	public String getCur_loc(){
		return this.cur_loc;
	}
	public void setCur_loc(String cur_loc){
		this.cur_loc=cur_loc;
	}
	
	public String getCur_dest(){
		return this.cur_dest;
	}
	public void setCur_dest(String cur_dest){
		this.cur_dest=cur_dest;
	}
	
	public String getLc_rout(){
		return this.lc_rout;
	}
	public void setLc_rout(String lc_rout){
		this.lc_rout=lc_rout;
	}
	
	public String getShiperrmsg(){
		return this.shiperrmsg;
	}
	public void setShiperrmsg(String shiperrmsg){
		this.shiperrmsg=shiperrmsg;
	}

	public String getAm_addr_code() {
		return am_addr_code;
	}

	public void setAm_addr_code(String am_addr_code) {
		this.am_addr_code = am_addr_code;
	}

	public String getOrginsite() {
		return orginsite;
	}

	public void setOrginsite(String orginsite) {
		this.orginsite = orginsite;
	}

	public String getOrginbranchcode() {
		return orginbranchcode;
	}

	public void setOrginbranchcode(String orginbranchcode) {
		this.orginbranchcode = orginbranchcode;
	}

	public String getDestinationsite() {
		return destinationsite;
	}

	public void setDestinationsite(String destinationsite) {
		this.destinationsite = destinationsite;
	}

	public String getDestinationsitecode() {
		return destinationsitecode;
	}

	public void setDestinationsitecode(String destinationsitecode) {
		this.destinationsitecode = destinationsitecode;
	}

	public String getGetycode() {
		return getycode;
	}

	public void setGetycode(String getycode) {
		this.getycode = getycode;
	}

	public String getGetylevl() {
		return getylevl;
	}

	public void setGetylevl(String getylevl) {
		this.getylevl = getylevl;
	}

	public String getGetytype() {
		return getytype;
	}

	public void setGetytype(String getytype) {
		this.getytype = getytype;
	}
	public String getDestinationzipcode() {//AAP01
		return destinationzipcode;
	}

	public void setDestinationzipcode(String destinationzipcode) {//AAP01
		this.destinationzipcode = destinationzipcode;
	}	
	
}