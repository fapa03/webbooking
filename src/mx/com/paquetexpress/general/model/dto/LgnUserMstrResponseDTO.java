package mx.com.paquetexpress.general.model.dto;

import java.io.Serializable;

import org.codehaus.jackson.map.annotate.JsonSerialize;

@SuppressWarnings("serial")
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class LgnUserMstrResponseDTO implements Serializable {
	
	private String success;
    private String url;
    private String error;

    public String getSuccess() {
		return success;
	}
	public void setSuccess(String success) {
		this.success = success;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}    
}
