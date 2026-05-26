package mx.com.paquetexpress.general.model.dto;

import java.io.Serializable;

import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
*
* @author cmunoz
*/

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class SysSspDesMstrDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String descr = "";
    private String code = "";
    private String srvcId = "";
    private String refrSrvcId = "";
    private String similar = "";
    private String similarPqtx = "";
    
    public SysSspDesMstrDTO(){
        
    }
    
    public SysSspDesMstrDTO(String code, String descr, String similar, String similarPqtx) {
        this.code = code;
        this.descr = descr;
        this.similar = similar;
        this.similarPqtx = similarPqtx;
    }    

    /**
	 * @return the similar
	 */
	public String getSimilar() {
		return similar;
	}

	/**
	 * @param similar the similar to set
	 */
	public void setSimilar(String similar) {
		this.similar = similar;
	}

	/**
	 * @return the similarPqtx
	 */
	public String getSimilarPqtx() {
		return similarPqtx;
	}

	/**
	 * @param similarPqtx the similarPqtx to set
	 */
	public void setSimilarPqtx(String similarPqtx) {
		this.similarPqtx = similarPqtx;
	}

	public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSrvcId() {
        return srvcId;
    }

    public void setSrvcId(String SrvcId) {
        this.srvcId = SrvcId;
    }

    public String getRefrSrvcId() {
        return refrSrvcId;
    }

    public void setRefrSrvcId(String refrSrvcId) {
        this.refrSrvcId = refrSrvcId;
    }
}
