

/**
 * File Name    : ChangePassword.java
 * Description  :This is the FormBean which Provides Setter and getter MethodsBean. 
 * Date Written :  25-Feb-2003
 * @author 	    :  D.SivaKumar
 */
//import logger.AccessLog;

import org.apache.struts.action.ActionForm;

public class JavChangePasswordForm extends ActionForm {
    
    /** Creates a new instance of loginForm */
    public JavChangePasswordForm() {
    }
    String loginName;
    String oldPassword;
	String newPassword;
	String confirmPassword;
	private String sPwdUpdated;
	private boolean valPassReq;
	private String action;
	
    public String getLoginName(){
        return loginName;
    }
    public void setLoginName(String loginName){
        this.loginName= loginName;
    }
    public String getOldPassword(){
        return oldPassword;
    }
    public void setOldPassword(String oldpassword){
        this.oldPassword = oldpassword;
    }
	
	 public String getNewPassword(){
        return this.newPassword;
    }
    public void setNewPassword(String newpassword){
        this.newPassword  = newpassword;
    }
	 public String getConfirmPassword(){
        return this.newPassword;
    }
    public void setConfirmPassword(String confirmpassword){
        this.confirmPassword  = confirmpassword;
    }
	public String getsPwdUpdated() {
		return sPwdUpdated;
	}
	public void setsPwdUpdated(String sPwdUpdated) {
		this.sPwdUpdated = sPwdUpdated;
	}
	public boolean isValPassReq() {
		return valPassReq;
	}
	public void setValPassReq(boolean valPassReq) {
		this.valPassReq = valPassReq;
	}
	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}
	/**
	 * @param action the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}
	
}
