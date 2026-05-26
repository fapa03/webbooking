import logger.AccessLog;

import org.apache.struts.action.ActionForm;

public class LovForm extends ActionForm {
	public LovForm() {
		AccessLog.Log("CALLING LOV FORM");
	}

	private String name;
	private String isNewBooking;

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIsNewBooking() {
		return isNewBooking;
	}

	public void setIsNewBooking(String isNewBooking) {
		this.isNewBooking = isNewBooking;
	}
}