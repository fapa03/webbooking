package mx.com.paquetexpress.general.model.dto;



import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class LgnUserMstrDTO {
	public String userType;
    public String userPass;
    public String userAccess;
    public String lang;
    public String platform; 
    
	public String getPlatForm() {
		return platform;
	}
	public void setPlatForm(String platform) {
		this.platform = platform;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public String getUserPass() {
		return userPass;
	}
	public void setUserPass(String userPass) {
		this.userPass = userPass;
	}
	public String getUserAccess() {
		return userAccess;
	}
	public void setUserAccess(String userAccess) {
		this.userAccess = userAccess;
	}
	public String getLang() {
		return lang;
	}
	public void setLang(String lang) {
		this.lang = lang;
	}    
}
