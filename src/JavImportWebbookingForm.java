
/*
 *    To keep the values taken from the Excel file
 */
public class JavImportWebbookingForm {
	
	
	private String rowNum="";
	private String destClientBranch=null;
	private String destClient=null;
	private String destClientName=null;
	private String destStreet=null;
	private String destDoorNo=null;
	private String destColony=null;
	private String destZipCode=null;
	private String destClientCity=null;
	private String destClientPhone=null;
	private String shipQuantity=null;
	private String shipDescription=null;
	private String shipContent=null;
	private String shipWeight=null;
	private String shipVolume=null;
	private String servicesDeliveryType=null;
	private String servicesAck=null;
	private String servicesCodValue=null;
	private String servicesDeclaredValue=null;
	private String servicesCommentaries=null;
	private String servicesReference=null;
	private String numberDePedimento=null;
	private String agentsAduanal=null;
	private String seqNumber=null;
	
	
	
	public void assign(String valueFromExcel,String LabelColumn)
	{
		 if(LabelColumn.trim().equalsIgnoreCase("DESTINATION CITY"))
		 {
			 this.destClientBranch=valueFromExcel;
		 }
		 else if(LabelColumn.trim().equalsIgnoreCase("CLIENT ID"))
		 {
			 this.destClient=valueFromExcel;
		 }
		 else if(LabelColumn.trim().equalsIgnoreCase("CLIENT NAME"))
		 {
			 this.destClientName=valueFromExcel;
		 }
		 else if(LabelColumn.trim().equalsIgnoreCase("STREET"))
		 {
			 this.destStreet=valueFromExcel;
		 }
		 else if(LabelColumn.trim().equalsIgnoreCase("DOOR NUMBER"))
		 {
			 this.destDoorNo=valueFromExcel;
		 }
		 else if(LabelColumn.trim().equalsIgnoreCase("COLONY"))
		 {
			 this.destColony=valueFromExcel;
		 }
		 else if(LabelColumn.trim().equalsIgnoreCase("ZIP CODE"))
		 {
			 this.destZipCode=valueFromExcel;
		 }
		 else if(LabelColumn.trim().equalsIgnoreCase("CITY"))
		 {
			 this.destClientCity=valueFromExcel;
		 }
		 else if(LabelColumn.trim().equalsIgnoreCase("PHONE"))
		 {
			 this.destClientPhone=valueFromExcel;
		 }
		 else if(LabelColumn.trim().equalsIgnoreCase("QUANTITY"))
		 {
			 this.shipQuantity=valueFromExcel;
		 }
		 else if(LabelColumn.trim().equalsIgnoreCase("DESCRIPTION"))
		 {
			 this.shipDescription=valueFromExcel;
		 }
		 else if(LabelColumn.trim().equalsIgnoreCase("CONTENT"))
		 {
			 this.shipContent=valueFromExcel;
		 }
		 else if(LabelColumn.trim().equalsIgnoreCase("WEIGHT"))
		 {
			 this.shipWeight=valueFromExcel;
		 }
		 else if(LabelColumn.trim().equalsIgnoreCase("VOLUME"))
		 {
			 this.shipVolume=valueFromExcel;
		 }
		 else if(LabelColumn.trim().equalsIgnoreCase("DELIVERY TYPE"))
		 {
			 this.servicesDeliveryType=valueFromExcel;
		 }
		 else if(LabelColumn.trim().equalsIgnoreCase("ACKNOWLEDGEMENT"))
		 {
			 this.servicesAck=valueFromExcel;
		 }
		 else if(LabelColumn.trim().equalsIgnoreCase("COD VALUE"))
		 {
			 this.servicesCodValue=valueFromExcel;
		 }
		 else if(LabelColumn.trim().equalsIgnoreCase("DECLARED VALUE"))
		 {
			 this.servicesDeclaredValue=valueFromExcel;
		 }
		 else if(LabelColumn.trim().equalsIgnoreCase("COMMENTARIES"))
		 {
			 this.servicesCommentaries=valueFromExcel;
		 }
		 else if(LabelColumn.trim().equalsIgnoreCase("REFERENCE"))
		 {
			 this.servicesReference=valueFromExcel;
		 }
		 else if(LabelColumn.trim().equalsIgnoreCase("NUMBERDEPEDIMENTO"))
		 {
			 this.numberDePedimento=valueFromExcel;
		 }
		 else if(LabelColumn.trim().equalsIgnoreCase("AGENTSADUANAL"))
		 {
			 this.agentsAduanal=valueFromExcel;
		 }
		 else if(LabelColumn.trim().equalsIgnoreCase("SEQNUMBER"))
		 {
			 this.seqNumber=valueFromExcel;
		 }
		
	}

	public String getDestClient() {
		return destClient;
	}

	public String getDestClientBranch() {
		return destClientBranch;
	}

	public String getDestClientCity() {
		return destClientCity;
	}

	public String getDestClientName() {
		return destClientName;
	}

	public String getDestClientPhone() {
		return destClientPhone;
	}

	public String getDestColony() {
		return destColony;
	}

	public String getDestDoorNo() {
		return destDoorNo;
	}

	public String getDestStreet() {
		return destStreet;
	}

	public String getDestZipCode() {
		return destZipCode;
	}

	public String getServicesAck() {
		return servicesAck;
	}

	public String getServicesCodValue() {
		return servicesCodValue;
	}

	public String getServicesCommentaries() {
		return servicesCommentaries;
	}

	public String getServicesDeclaredValue() {
		return servicesDeclaredValue;
	}

	public String getServicesDeliveryType() {
		return servicesDeliveryType;
	}

	public String getServicesReference() {
		return servicesReference;
	}

	public String getShipContent() {
		return shipContent;
	}

	public String getShipDescription() {
		return shipDescription;
	}

	public String getShipQuantity() {
		return shipQuantity;
	}

	public String getShipVolume() {
		return shipVolume;
	}

	public String getShipWeight() {
		return shipWeight;
	}

	public String getAgentsAduanal() {
		return agentsAduanal;
	}

	public String getNumberDePedimento() {
		return numberDePedimento;
	}

	public String getRowNum() {
		return rowNum;
	}

	public void setRowNum(String rowNum) {
		this.rowNum = rowNum;
	}

	public String getSeqNumber() {
		return seqNumber;
	}
	

}
