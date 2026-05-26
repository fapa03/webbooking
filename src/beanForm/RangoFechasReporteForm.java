package beanForm;


import org.apache.struts.action.ActionForm;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionMapping;

public class RangoFechasReporteForm extends ActionForm{
	public RangoFechasReporteForm(){}
	private String startDate = null;
	private String endDate = null;
	
	public String getStartDate(){
		return startDate;
	}
	public void setStartDate(String startDate){
		this.startDate = startDate;
	}
	
	public String getEndDate(){
		return endDate;
	}
	public void setEndDate(String endDate){
		this.endDate = endDate;
	}
	
	public void reset(ActionMapping mapping,HttpServletRequest request){
		this.startDate = null;
		this.endDate = null;
	}
		
}

										 