package beanForm;

import org.apache.struts.action.ActionForm;

public class JavGuiaReturnForm extends ActionForm {


	/**
	 * Member variable declaration
	 */

	 String guiaNumber;
	 String useraction;
	 String docuType;
	 String guiaNumberReturn;
	 String typeRecoleccion = "1";//1 =piso, 0 =RAD;
	 String horaInicio;
	 String horaFinal;
	 String emailDest;
	 String phoneDest;
	 String destBrncId;
	 String destBrncName;
	 String destSiteName;
	 String radFlag;
	 String orgnBrncName;
	 String orgnSiteName;
	 String orgnClntName;
	 String orgnClntClave;
	 String destClntName;
	 String destClntClave;
	 String prepBrncId;
	 String issueDate;
	 String guiaAmount;
	 String mrbServiceInfo;
	 String validado ="0";
	/** 
 	 * method to set the value
 	 */ 
	public void setGuiaNumber(String guiaNumber) 
	{
		this.guiaNumber = guiaNumber; 
	}  //End method setGuianumber


	/**
 	 * method to get the value
 	 * @return String 
 	 * 
 	 */
	public String getGuiaNumber()
	{ 
		return this.guiaNumber;
	}  //End method getGuianumber

	

	/** 
 	 * method to set the value 
 	 */
	public void setUseraction(String userAction) 
	{
		this.useraction = userAction; 
	}  //End method setUseraction


	/**
 	 * method to get the value
 	 *
 	 */
	public String getUseraction()
	{ 
		return this.useraction;
	}  //End method getUseraction


	public String getDocuType() {
		return docuType;
	}


	public void setDocuType(String docuType) {
		this.docuType = docuType;
	}


	public String getGuiaNumberReturn() {
		return guiaNumberReturn;
	}


	public void setGuiaNumberReturn(String guiaNumberReturn) {
		this.guiaNumberReturn = guiaNumberReturn;
	}


	public String getTypeRecoleccion() {
		return typeRecoleccion;
	}


	public void setTypeRecoleccion(String typeRecoleccion) {
		this.typeRecoleccion = typeRecoleccion;
	}


	public String getHoraInicio() {
		return horaInicio;
	}


	public void setHoraInicio(String horaInicio) {
		this.horaInicio = horaInicio;
	}


	public String getHoraFinal() {
		return horaFinal;
	}


	public void setHoraFinal(String horaFinal) {
		this.horaFinal = horaFinal;
	}


	public String getEmailDest() {
		return emailDest;
	}


	public void setEmailDest(String emailDest) {
		this.emailDest = emailDest;
	}


	public String getPhoneDest() {
		return phoneDest;
	}


	public void setPhoneDest(String phoneDest) {
		this.phoneDest = phoneDest;
	}


	public String getDestBrncId() {
		return destBrncId;
	}


	public void setDestBrncId(String destBrncId) {
		this.destBrncId = destBrncId;
	}


	public String getRadFlag() {
		return radFlag;
	}


	public void setRadFlag(String radFlag) {
		this.radFlag = radFlag;
	}


	public String getOrgnClntName() {
		return orgnClntName;
	}


	public void setOrgnClntName(String orgnClntName) {
		this.orgnClntName = orgnClntName;
	}


	public String getOrgnClntClave() {
		return orgnClntClave;
	}


	public void setOrgnClntClave(String orgnClntClave) {
		this.orgnClntClave = orgnClntClave;
	}


	public String getDestClntName() {
		return destClntName;
	}


	public void setDestClntName(String destClntName) {
		this.destClntName = destClntName;
	}


	public String getDestClntClave() {
		return destClntClave;
	}


	public void setDestClntClave(String destClntClave) {
		this.destClntClave = destClntClave;
	}


	public String getDestBrncName() {
		return destBrncName;
	}


	public void setDestBrncName(String destBrncName) {
		this.destBrncName = destBrncName;
	}


	public String getDestSiteName() {
		return destSiteName;
	}


	public void setDestSiteName(String destSiteName) {
		this.destSiteName = destSiteName;
	}


	public String getOrgnBrncName() {
		return orgnBrncName;
	}


	public void setOrgnBrncName(String orgnBrncName) {
		this.orgnBrncName = orgnBrncName;
	}


	public String getOrgnSiteName() {
		return orgnSiteName;
	}


	public void setOrgnSiteName(String orgnSiteName) {
		this.orgnSiteName = orgnSiteName;
	}


	public String getPrepBrncId() {
		return prepBrncId;
	}


	public void setPrepBrncId(String prepBrncId) {
		this.prepBrncId = prepBrncId;
	}


	public String getIssueDate() {
		return issueDate;
	}


	public void setIssueDate(String issueDate) {
		this.issueDate = issueDate;
	}


	public String getGuiaAmount() {
		return guiaAmount;
	}


	public void setGuiaAmount(String guiaAmount) {
		this.guiaAmount = guiaAmount;
	}


	public String getMrbServiceInfo() {
		return mrbServiceInfo;
	}


	public void setMrbServiceInfo(String mrbServiceInfo) {
		this.mrbServiceInfo = mrbServiceInfo;
	}


	public String getValidado() {
		return validado;
	}


	public void setValidado(String validado) {
		this.validado = validado;
	}
	

}
