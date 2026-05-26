
/**
 * File Name    : ManifestReportForm.java
 * Description  :This is the FormBean which Provides Setter and getter MethodsBean. 
 * Date Written :  25-Feb-2003
 * @author 	    :  D.SivaKumar
 */

import org.apache.struts.action.ActionForm;

public class ManifestReportForm1 extends ActionForm {
    
    /** Creates a new instance of loginForm */
    public ManifestReportForm1() {
		//System.out.println("Inside ManifestReportForm1...");//commented and below added by B.Emerson on 30/04/2004
		//AAP//AccessLog.Log("Inside ManifestReportForm1...");
    }	
    String startDate;
	String endDate;
	public void setStartDate(String stdate){
		
		this.startDate=stdate;
	}
	public String  getStartDate(){
		return this.startDate;	
	}    	
	
	public void setEndDate(String enddate){
		
		this.endDate=enddate;
	}
	public String  getEndDate(){
		return this.endDate;
			
	}    
	
}
