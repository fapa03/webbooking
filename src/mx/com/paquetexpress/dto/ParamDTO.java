package mx.com.paquetexpress.dto;

import java.io.Serializable;

public class ParamDTO implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String parmCode1;
    String parmCode2;
    String vlue1Id;
    String vlue1Desc;
    String vlue2Desc;
	public String getParmCode1() {
		return parmCode1;
	}
	public void setParmCode1(String parmCode1) {
		this.parmCode1 = parmCode1;
	}
	public String getParmCode2() {
		return parmCode2;
	}
	public void setParmCode2(String parmCode2) {
		this.parmCode2 = parmCode2;
	}
	public String getVlue1Id() {
		return vlue1Id;
	}
	public void setVlue1Id(String vlue1Id) {
		this.vlue1Id = vlue1Id;
	}
	public String getVlue1Desc() {
		return vlue1Desc;
	}
	public void setVlue1Desc(String vlue1Desc) {
		this.vlue1Desc = vlue1Desc;
	}
	public String getVlue2Desc() {
		return vlue2Desc;
	}
	public void setVlue2Desc(String vlue2Desc) {
		this.vlue2Desc = vlue2Desc;
	}

}
