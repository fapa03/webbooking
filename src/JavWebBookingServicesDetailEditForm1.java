
/*----------------------------------------------------------
Author					:	V.Ramachandran
Date					:	25-March-2003
FileName				:	JavWebBookingServicesDetailEditForm1.java
SessionVariables		:
Other Used Classes		:
Function Names			:
------------------------------------------------------------*/

import org.apache.struts.action.ActionForm;

public class JavWebBookingServicesDetailEditForm1 extends ActionForm {	
	
	String radioselect;
	public JavWebBookingServicesDetailEditForm1(){
		//AAP//AccessLog.Log("JavWebBookingServicesDetailEditForm1");
	}
	public String getRadioselect(){
		return this.radioselect;
	}
	public void setRadioselect(String radioselect){		
		this.radioselect=radioselect;
	}
}