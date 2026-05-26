
/**
 * File Name    : LoginForm.java
 * Description  :This is the FormBean which Provides Setter and getter MethodsBean. 
 * Date Written :  25-Feb-2003
 * @author 	    :  D.SivaKumar
 * ----------------------------------------------------------------- 
 * MODIFICACIONES:  
 * ----------------------------------------------------------------- 
 * Clave: AAP01
 * Autor: ABRAHAM DANIEL ARJONA PERAZA
 * Fecha: 01/07/2013
 * Descripci�n: se agreg� variable allowedFXC. para verificar si el cliente origen,
 * puede realizar documentacion flete por cobrar.
 * ----------------------------------------------------------------- 
 */
import org.apache.struts.action.ActionForm;

public class JavLoginForm extends ActionForm {
    
    /** Creates a new instance of loginForm */
    public JavLoginForm() {
    }
    String loginName;
    String password;
	// Hidden Fields.
	String cliendId;
	String assignedBranch;
	String assignedSite;
	String tarifType;
	String additionalServiceFlag;
	String readOnly;
	String displayAmountFlag;
	private String newBooking;    
	String userValidate = null;
	private String sendEmail = "";
	private String allowedFXC = "";//AAP01	
	private String showWeb = "";
	private String showPpg = "";
	private String showPopup = "";
	private String msjeVal = "";
	private String milliSeconds = "";
	private String useUrl = "";
    private String showGuiasRR ="0";
	private String token = "";
	String brncIdWeb;
	
	public String getLoginName(){
        return loginName;
    }
    public void setLoginName(String loginName){
        this.loginName= loginName;
    }
    public String getPassword(){
        return password;
    }
    public void setPassword(String password){
        this.password = password;
    }
	
	public void setReadOnly(String readonly){
		this.readOnly=readonly;
	}
	
	public String getReadOnly(){
		return this.readOnly;
	}
	public void setClientId(String clntid){
		this.cliendId=clntid;
	}
	
	public String getClientId(){
		return this.cliendId;
	}
	
	public void setTarifType(String triftype){
		this.tarifType=triftype;
	}
	public String getTarifType(){
		return this.tarifType;
	}
	public void setAssignedBranch(String assbrnch){
		
		this.assignedBranch=assbrnch;
	}
	public String getAssignedBranch(){
		return this.assignedBranch;
	}
	
	public void setAdditionalServiceFlag(String addsrvcflag){
		this.additionalServiceFlag=addsrvcflag;
	}
	
	public String getAdditionalServiceFlag(){
		return this.additionalServiceFlag;
	}
	

	
	/**
	 * Added by bala
	 */

    public String getUserValidate() 
    {
        return userValidate;
    }

    public void setUserValidate(String userValidate)
    {
        this.userValidate = userValidate;
    }
	public String getAssignedSite() {
		return assignedSite;
	}
	public void setAssignedSite(String assignedSite) {
		this.assignedSite = assignedSite;
	}
	public String getDisplayAmountFlag() {
		return displayAmountFlag;
	}
	public void setDisplayAmountFlag(String displayAmountFlag) {
		this.displayAmountFlag = displayAmountFlag;
	}
	public String getNewBooking() {
		return newBooking;
	}
	public void setNewBooking(String newBooking) {
		this.newBooking = newBooking;
	}
	
	public String getSendEmail() {
		return sendEmail;
	}
	public void setSendEmail(String sendEmail) {
		this.sendEmail = sendEmail;
	}
	public String getAllowedFXC() {//AAP01
		return allowedFXC;
	}
	public void setAllowedFXC(String allowedFXC) {//AAP01
		this.allowedFXC = allowedFXC;//AAP01
	}//AAP01
	public String getShowWeb() {
		return showWeb;
	}
	public void setShowWeb(String showWeb) {
		this.showWeb = showWeb;
	}
	public String getShowPpg() {
		return showPpg;
	}
	public void setShowPpg(String showPpg) {
		this.showPpg = showPpg;
	}
	public String getMsjeVal() {
		return msjeVal;
	}
	public void setMsjeVal(String msjeVal) {
		this.msjeVal = msjeVal;
	}
	public String getMilliSeconds() {
		return milliSeconds;
	}
	public void setMilliSeconds(String milliSeconds) {
		this.milliSeconds = milliSeconds;
	}
	public String getUseUrl() {
		return useUrl;
	}
	public void setUseUrl(String useUrl) {
		this.useUrl = useUrl;
	}
	public String getShowGuiasRR() {
		return showGuiasRR;
	}
	public void setShowGuiasRR(String showGuiasRR) {
		this.showGuiasRR = showGuiasRR;
	}
	

    /**
     * @return String return the token
     */
    public String getToken() {
        return token;
    }

    /**
     * @param token the token to set
     */
    public void setToken(String token) {
        this.token = token;
    }
	public String getBrncIdWeb() {
		return brncIdWeb;
	}
	public void setBrncIdWeb(String brncIdWeb) {
		this.brncIdWeb = brncIdWeb;
	}
	/**
	 * @return the showPopup
	 */
	public String getShowPopup() {
		return showPopup;
	}
	/**
	 * @param showPopup the showPopup to set
	 */
	public void setShowPopup(String showPopup) {
		this.showPopup = showPopup;
	}

    
    
}
