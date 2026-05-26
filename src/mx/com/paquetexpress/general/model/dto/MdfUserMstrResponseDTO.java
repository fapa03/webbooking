package mx.com.paquetexpress.general.model.dto;

import java.io.Serializable;

import org.codehaus.jackson.map.annotate.JsonSerialize;

@SuppressWarnings("serial")
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class MdfUserMstrResponseDTO implements Serializable {
	private String brncOrig;
	private String dateIni;
	private String email;
	private String date;
    private String userAccess;
    private String userPass;
    private String userName;
    private String userType;
    private String userCreation;
    private String userEmail;
    private String userClient;
    private String action;
    private String lan; 
    private String typeModule;
    
	public String getUserAccess() {
		return userAccess;
	}
	public void setUserAccess(String userAccess) {
		this.userAccess = userAccess;
	}
	public String getUserPass() {
		return userPass;
	}
	public void setUserPass(String userPass) {
		this.userPass = userPass;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public String getUserCreation() {
		return userCreation;
	}
	public void setUserCreation(String userCreation) {
		this.userCreation = userCreation;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public String getUserClient() {
		return userClient;
	}
	public void setUserClient(String userClient) {
		this.userClient = userClient;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getTypeModule() {
		return typeModule;
	}
	public void setTypeModule(String typeModule) {
		this.typeModule = typeModule;
	}
	public String getBrncOrig() {
		return brncOrig;
	}
	public void setBrncOrig(String brncOrig) {
		this.brncOrig = brncOrig;
	}
	public String getDateIni() {
		return dateIni;
	}
	public void setDateIni(String dateIni) {
		this.dateIni = dateIni;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getLan() {
		return lan;
	}
	public void setLan(String lan) {
		this.lan = lan;
	}
    
}
