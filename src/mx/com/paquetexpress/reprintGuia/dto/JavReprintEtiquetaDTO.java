package mx.com.paquetexpress.reprintGuia.dto;

import java.io.Serializable;

public class JavReprintEtiquetaDTO implements Serializable{


	private static final long serialVersionUID = 5450163524304261231L;
	private String guiaNo;
	private String guiaDescCode;
	private String guiaDesc;
	private String content;
	private String quantity;
	private boolean checked;
	
	public String getGuiaNo() {
		return guiaNo;
	}

	public void setGuiaNo(String guiaNo) {
		this.guiaNo = guiaNo;
	}

	public String getGuiaDescCode() {
		return guiaDescCode;
	}

	public void setGuiaDescCode(String guiaDescCode) {
		this.guiaDescCode = guiaDescCode;
	}

	public String getGuiaDesc() {
		return guiaDesc;
	}

	public void setGuiaDesc(String guiaDesc) {
		this.guiaDesc = guiaDesc;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}
}
