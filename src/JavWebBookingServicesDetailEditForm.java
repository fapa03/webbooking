
/*----------------------------------------------------------
Author					:	V.Ramachandran
Date					:	25-March-2003
FileName				:	JavWebBookingServicesDetailEditForm.java
SessionVariables		:
Other Used Classes		:
Function Names			:
------------------------------------------------------------*/

import org.apache.struts.action.ActionForm;

public class JavWebBookingServicesDetailEditForm extends ActionForm {	
	
	String radioselect;
	
	public JavWebBookingServicesDetailEditForm(){
		//AAP//AccessLog.Log("JavWebBookingServicesDetailEditForm");
	}
	public String getRadioselect(){
		return this.radioselect;
	}
	public void setRadioselect(String radioselect){		
		this.radioselect=radioselect;
	}
}