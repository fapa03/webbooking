
/**
 * File Name    : ManifestReportAction1.java
 * Description  :This is the FormBean which Provides Setter and getter MethodsBean. 
 * Date Written :  21-Apr-2003
 * @author 	    :  D.SivaKumar
 * 
 * Modified by Balaji  On 01-DEC-2009:	 Purpos: To avoid display the amount for the client, doesn't have display flag from web_clnt_mstr
 */

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import logger.AccessLog;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import bean.Global;
import beanUtil.ConnectDB;

import com.lowagie.text.Cell;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
public class ManifestReportAction1 extends Action {

	public ManifestReportAction1(){
		//System.out.println("INSIDE MANIFESTREPORT ACTION1");//commented and below added by B.Emerson on 30/04/2004
		//AAP//AccessLog.Log("INSIDE MANIFESTREPORT ACTION1");
	}
	
	int TotRec=0;
	int dateRowspan=0;
	long currenttime=0;
	@SuppressWarnings("rawtypes")
	public ActionForward perform(ActionMapping am,ActionForm af,HttpServletRequest request,HttpServletResponse response)
		throws ServletException,IOException{
		Connection con=null;
		HttpSession session=null;	
		ArrayList defaultAddress=null;
		String strClientId;
		String strBranchId;
		String strStartDate;
		String strendDate;
		//PdfWriter pdfwriter=null;
		Document document=null;
		try {
			session=request.getSession(true);
			Global global = (Global)session.getAttribute("sGlobal");//Added by rama
			if(session==null || session.isNew())
				return am.findForward("nosession");

			con=ConnectDB.getConnection();
			response.setContentType("application/pdf");

			if (af instanceof ManifestReportForm1){
				ManifestReportForm1 mrf1 = (ManifestReportForm1) af;
				// Get the Input Parameters From ManifestReportForm..

				strClientId = (String) session.getAttribute("sClientId");
				strBranchId = (String) session.getAttribute("sAssignedBranch");
				strStartDate	=	mrf1.getStartDate();
				strendDate		=	mrf1.getEndDate();
				
				// step 1: creation of a document-object
				document = new Document(PageSize.A4.rotate(), 10, 10, 10, 10);
				//System.out.println("DOCUMENT OBJECT IN MANIFESTREPORTACTION1 "+document);
				
				// step 2: we create a writer that listens to the document
				
				//String fileName = request.getRealPath("/")+strClientId+"ManifestReport(Period).pdf";
				//Added by rama
				File f=new File(request.getRealPath("/")+File.separator+"ManifestReportPeriod");
                //File f=new File(request.getContextPath()+File.separator+"ManifestReportPeriod");
				if(!f.exists())
					f.mkdirs();
				String exportdirectory=f.toString();
				//String fileName = exportdirectory+File.separator+strClientId+"ManifestReport(Period).pdf";
				currenttime = getTimeStamp(con).getTime();
				String fileName = exportdirectory+File.separator+"ManifestReport(Period)_"+currenttime+".pdf";
				
				//System.out.println("PATH INSIDE MANIFEST REPORT ACTION 1 "+fileName);
				PdfWriter.getInstance(document, new FileOutputStream(fileName));
				
				// step 3: we open the document
				document.open();

				// step 4: we add content to the document (this happens in a seperate method)
				
				
				PdfPTable table=null;								
				
				table = new PdfPTable(1);
				table.getDefaultCell().setBorderWidth(0);
				table.getDefaultCell().setPadding(3);
				table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
				
				//Get the Address and Display it...
				defaultAddress=this.getDefaultAddress(con,strClientId);
				
				if(defaultAddress.isEmpty()){
					table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
					//table.addCell("IMPULSORA DE TRANSPORTES MEXICANOS SA DE CV");//Commented by rama
					table.addCell(global.clientName);//Added by rama
				}
				else{
					HashMap defaultAddressValues = (HashMap)defaultAddress.get(0);
					table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
					//table.addCell("IMPULSORA DE TRANSPORTES MEXICANOS SA DE CV");//Commented by rama
					table.addCell(global.clientName);//Added by rama
					table.addCell(defaultAddressValues.get("AM_STRT_NAME")+" "+
								  defaultAddressValues.get("AM_DRNR"));				
					
					table.addCell(defaultAddressValues.get("u16")+" "+
								  defaultAddressValues.get("u17"));					
				}	
				
				table.addCell(" ");
				table.addCell(" Manifiestos Periodo  : "+strStartDate +"-"+strendDate);
				document.add(table);				
				
				
				ArrayList manifestdates=getManifestDates(con,strClientId,strBranchId,strStartDate,strendDate) ;
				
				for(int i=0;i<manifestdates.size();i++){
					
					String mnftDate = (String) manifestdates.get(i);
					
					Table dataTable = new Table(8);
					dataTable.setWidth(100);
					dataTable.setBorderWidth(1);
					//dataTable.setCellpadding(2);//AAP01
					dataTable.setPadding(2);//AAP01
					int columnWidth [] = {10,11,6,6,30,10,10,12};
					dataTable.setWidths(columnWidth);
					//int dateRowspan=0;
					
					
					/*Cell cell = new Cell("Date");
					cell.setRowspan(2);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setHeader(true);
					dataTable.addCell(cell);*/
					
					Paragraph p1= new Paragraph();
					p1.add("  Date : "+mnftDate);
					document.add(p1);
					
					Cell cell = new Cell("Manifiesto");
					cell.setHeader(true);
					cell.setRowspan(2);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					dataTable.addCell(cell);
					cell = new Cell("Guías");
					cell.setHeader(true);
					cell.setRowspan(2);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					dataTable.addCell(cell);
					cell = new Cell("Paquetes");
					cell.setHeader(true);
					cell.setRowspan(2);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					dataTable.addCell(cell);
					cell = new Cell("Destinatario");
					cell.setHeader(true);
					cell.setColspan(2);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					dataTable.addCell(cell);
					cell = new Cell("Importe");
					cell.setHeader(true);
					cell.setColspan(3);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					dataTable.addCell(cell);
					cell = new Cell("Sucursal");
					cell.setHeader(true);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					dataTable.addCell(cell);
					cell = new Cell("Cliente");
					cell.setWidth("twenty");
					cell.setHeader(true);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					dataTable.addCell(cell);
					cell = new Cell("Tarifa");
					cell.setHeader(true);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					dataTable.addCell(cell);
					cell = new Cell(" Servicios Adic.");
					cell.setHeader(true);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					dataTable.addCell(cell);
					cell = new Cell("Total");
					cell.setHeader(true);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					dataTable.addCell(cell);					
					
					loadDocument(con,document,dataTable,cell,strClientId,strBranchId, strStartDate, strendDate,mnftDate, global);
					//System.out.println("After calling load document ");
					document.newPage();
					
					
				}
				
				// step 5: we close the document
				//document.close();
			}
		}catch (Exception e2) {
			e2.printStackTrace();//commented and below added by B.Emerson on 30/04/2004
			AccessLog.Log(e2);
		}finally{
			try{
				document.close();
			}catch(Exception cone){
			}
			//Added by rama
			try{
				if(con!=null)
					con.close();
			}catch(Exception cone){
			}
			
			//pdfwriter.flush();
			//pdfwriter.close();
		}
		

		//System.out.println("RETURNING SUCESS");//commented and below added by B.Emerson on 30/04/2004
		//AAP//AccessLog.Log("RETURNING SUCESS");
		request.setAttribute("currenttime",String.valueOf(currenttime));
		return am.findForward("success");
	}
	
	@SuppressWarnings("rawtypes")
	public void loadDocument(Connection con,
							 Document document,
							 Table dataTable,
							 Cell cell,
							 String clientId,
							 String branchId,
							 String StartDate,
							 String endDate,
							 String mnftDate,
							 Global global) throws Exception{
		
		ArrayList manifestNumbers=null;
		ArrayList guiaDetails=null;
		HashMap  totals=null;
		
		try{
			//System.out.println("INSIDE LOAD DOCUMENT");//commented and below added by B.Emerson on 30/04/2004
			//AAP//AccessLog.Log("INSIDE LOAD DOCUMENT");
			// get mnaifestNumbers for the date..			
			manifestNumbers=getManifestNumbers(con,clientId,branchId,mnftDate);
			// Get GuiaDetails and its Totals..	
			
			if(manifestNumbers !=null){
				
				//System.out.println("inside not null manifest numbers");
				//System.out.println("MANIFESTNUMBERS SIZE "+manifestNumbers.size());
				String mnftNo = "";
				for(int i=0;i<manifestNumbers.size();i++){
					mnftNo=(String) manifestNumbers.get(i);
					dateRowspan =dateRowspan + getGuiaCount(con,clientId,branchId,mnftNo);
					mnftNo = "";
				}
				
				//System.out.println("Date Rows Span...."+dateRowspan);
				
				String totGuias="";
				String totPackages="";
				String totTariff="";
				String totAddService="";
				String grantTotal="";
				
				/*	cell = new Cell(mnftDate);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setRowspan(dateRowspan);
				dataTable.addCell(cell);
				*/
				
				for(int j=0;j<manifestNumbers.size();j++) {
					
					String manifestNumber =(String) manifestNumbers.get(j);
					//System.out.println("MANIFEST NUMBER "+manifestNumber);
					
					guiaDetails=getGuiaDetails(con,manifestNumber,clientId,branchId,StartDate,endDate);
					
					//System.out.println("GUIA DETAILS "+guiaDetails);
					
					totals = getTotal(con,clientId,branchId,StartDate,endDate);
					
					int rowspan=0;
					if(guiaDetails!=null)
						rowspan=guiaDetails.size();
					
					//System.out.println("RowSpan... in  ManifestReport Action "+rowspan);//commented and below added by B.Emerson on 30/04/2004
					//AAP//AccessLog.Log("RowSpan... in  ManifestReport Action "+rowspan);
					if(rowspan!=0){//Added by rama
						cell = new Cell(manifestNumber);
						//cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
						cell.setHorizontalAlignment(Element.ALIGN_CENTER);						
						cell.setRowspan(rowspan);
						dataTable.addCell(cell);
					}
					HashMap values = null;
					String guias = "";
					String packages = "";
					String destBranch = "";
					String destClient = "";
					String tariff = "";
					String addService = "";
					String tot = "";
					for(int i=0;i<guiaDetails.size();i++){
						
						//System.out.println("INSIDE GUIA DETAILS FOR LOOP MANIFEST REPORT ACTION1...");//commented and below added by B.Emerson on 30/04/2004
						//AAP//AccessLog.Log("INSIDE GUIA DETAILS FOR LOOP MANIFEST REPORT ACTION1...");
						
						//HashMap values = (HashMap)guiaDetails.get(i);	
						values = (HashMap)guiaDetails.get(i);	
						guias=values.get("GUIAS")==null?"":values.get("GUIAS").toString();
						packages=values.get("PACKAGES")==null?"":values.get("PACKAGES").toString();
						destBranch=values.get("DESTINATION")==null?"":values.get("DESTINATION").toString();
						destClient=values.get("DESTCLNT")==null?"":values.get("DESTCLNT").toString();
					
						tariff=values.get("TARIFF")==null?"":values.get("TARIFF").toString();
							tariff = global.displayAmountFlag.equalsIgnoreCase("Y")?tariff:"******"; // Code added on 01/12/2009
						addService=values.get("ADDSERV")==null?"":values.get("ADDSERV").toString();
							addService = global.displayAmountFlag.equalsIgnoreCase("Y")?addService:"******"; // Code added on 01/12/2009
						tot=values.get("TOTAL")==null?"":values.get("TOTAL").toString();
						tot = global.displayAmountFlag.equalsIgnoreCase("Y")?tot:"******"; // Code added on 01/12/2009
						
						if(totals !=null){					
							totGuias=totals.get("TOTALGUIAS")==null?"":totals.get("TOTALGUIAS").toString();				
							totPackages=totals.get("TOTALPACK")==null?"":totals.get("TOTALPACK").toString();
							totTariff=totals.get("TOTALTARIFF")==null?"":totals.get("TOTALTARIFF").toString();
								totTariff = global.displayAmountFlag.equalsIgnoreCase("Y")?totTariff:"*******"; // Code added on 01/12/2009
							totAddService=totals.get("TOTALADD")==null?"":totals.get("TOTALADD").toString();
								totAddService = global.displayAmountFlag.equalsIgnoreCase("Y")?totAddService:"*******"; // Code added on 01/12/2009
							grantTotal=totals.get("TOTALAMOUNT")==null?"":totals.get("TOTALAMOUNT").toString();
							grantTotal = global.displayAmountFlag.equalsIgnoreCase("Y")?grantTotal:"*******"; // Code added on 01/12/2009
						}
						
						
						// Populate the Values into the Cells.				
						
						cell = new Cell(guias);
						cell.setHorizontalAlignment(Element.ALIGN_CENTER);
						dataTable.addCell(cell);
						
						cell = new Cell(packages);
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						dataTable.addCell(cell);
						
						cell = new Cell(destBranch);
						cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						dataTable.addCell(cell);
						
						cell = new Cell(destClient);
						cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						dataTable.addCell(cell);
						
						
						
						cell = new Cell(tariff);
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						dataTable.addCell(cell);
						
						cell = new Cell(addService);
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						dataTable.addCell(cell);
						
						cell = new Cell(tot);
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						
						dataTable.addCell(cell);
						
						guias = "";
						packages = "";
						destBranch = "";
						destClient = "";
						tariff = "";
						addService = "";
						tot = "";
					}
				}
				
				// Put the Summation Values for Each Manifest Number...
				
				
				/*cell = new Cell("");				
				dataTable.addCell(cell);*/	
				
				cell = new Cell("Total");
				//cell.setColspan(2);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				dataTable.addCell(cell);

				cell = new Cell(totGuias);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				dataTable.addCell(cell);

				cell = new Cell(totPackages);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				dataTable.addCell(cell);

				cell = new Cell("");
				dataTable.addCell(cell);

				cell = new Cell("");
				dataTable.addCell(cell);

			
		
				cell = new Cell(totTariff);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				dataTable.addCell(cell);
				
				cell= new Cell(totAddService);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				dataTable.addCell(cell);
				
				cell = new Cell(grantTotal);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				
				dataTable.addCell(cell);
				Paragraph p = new Paragraph();
				p.add(" ");
				p.add(" ");
				p.add(" ");
				p.add(" ");
				p.add(" ");			
				document.add(p);				
				document.add(dataTable); 
				//System.out.println("BEFORE RETURNING LOAD DOCUMENT IN REPORT1 ");
			}
		}catch(Exception e)	{
			//e.printStackTrace();//commented and below added by B.Emerson on 30/04/2004
			AccessLog.Log(e);
		}
		
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList getDefaultAddress(Connection con,String destinationClientId)throws Exception{
		CallableStatement cst = null;
		ResultSet rs = null;
		//ResultSet crs = null;
		PreparedStatement pst = null;
		HashMap values = null;
		ArrayList result = new ArrayList();
		
		String addressType="CLNT";
		String defaultAddressFlag = "Y";
		String query = "SELECT AM_ADDR_CODE,AM_DRNR,AM_STRT_NAME, "+
					   "       AM_PHNO1,AM_SUIT_NO,AM_FLOR_NO,"+
					   "       AM_ADDR_STYP,AM_ADDR_DEFN_TYPE,"+
					   "       AM_ADDR_REF_NO,AM_GETY_LEVL,"+
					   "       AM_GETY_TYPE,AM_GETY_CODE "+
					   "  FROM SYS_ADDR_MSTR "+
					   " WHERE AM_ENTY_ID=?"+
					   "   AND AM_ADDR_TYPE=?"+
					   "   AND AM_DEFA_FLAG=?";
		
		pst = con.prepareStatement(query);
		pst.setString(1,destinationClientId);
		pst.setString(2,addressType);
		pst.setString(3,defaultAddressFlag);
		
		rs=pst.executeQuery();
		String AM_ADDR_CODE=null,AM_DRNR=null,AM_STRT_NAME=null,AM_PHNO1=null,AM_SUIT_NO=null,AM_FLOR_NO=null,
			AM_ADDR_STYP=null,AM_ADDR_DEFN_TYPE=null,AM_ADDR_REF_NO=null,AM_GETY_LEVL=null,AM_GETY_TYPE=null,AM_GETY_CODE=null;
		while(rs.next()){
			AM_ADDR_CODE  =(rs.getString(1)== null?"":rs.getString(1));
			AM_DRNR    =(rs.getString(2)== null?"":rs.getString(2));
			AM_STRT_NAME  =(rs.getString(3)== null?"":rs.getString(3));
			AM_PHNO1   =(rs.getString(4)== null?"":rs.getString(4));
			AM_SUIT_NO   =(rs.getString(5)== null?"":rs.getString(5));
			AM_FLOR_NO   =(rs.getString(6)== null?"":rs.getString(6));
			AM_ADDR_STYP  =(rs.getString(7)== null?"":rs.getString(7));
			AM_ADDR_DEFN_TYPE =rs.getString(8);
			AM_ADDR_REF_NO  =rs.getString(9);
			AM_GETY_LEVL  =rs.getString(10);
			AM_GETY_TYPE  =rs.getString(11);
			AM_GETY_CODE  =rs.getString(12);
			
			cst = con.prepareCall( "{call pack_web.pro_ftch_addr(?,?,?,"+
								   "?,?,"+
								   "?,?,?,"+
								   "?,?,?,?,?,"+
								   "?,?,?,?,?,?) }");      
			cst.setString(1,AM_ADDR_DEFN_TYPE);
			cst.setString(2,AM_ADDR_REF_NO);
			cst.setString(3,AM_GETY_CODE);
			cst.setString(4,AM_GETY_LEVL);
			cst.setString(5,AM_GETY_TYPE);
			
			cst.registerOutParameter(6,Types.VARCHAR);
			cst.registerOutParameter(7,Types.VARCHAR);
			cst.registerOutParameter(8,Types.VARCHAR);
			cst.registerOutParameter(9,Types.VARCHAR);
			cst.registerOutParameter(10,Types.VARCHAR);
			cst.registerOutParameter(11,Types.VARCHAR);
			cst.registerOutParameter(12,Types.VARCHAR);
			cst.registerOutParameter(13,Types.VARCHAR);
			cst.registerOutParameter(14,Types.VARCHAR);
			cst.registerOutParameter(15,Types.VARCHAR);
			cst.registerOutParameter(16,Types.VARCHAR);
			cst.registerOutParameter(17,Types.VARCHAR);
			cst.registerOutParameter(18,Types.VARCHAR);
			cst.registerOutParameter(19,Types.VARCHAR);
			
			cst.executeQuery();
			//if(crs.next()){
			values = new HashMap();
			values.put("AM_ADDR_CODE",AM_ADDR_CODE);
			values.put("AM_DRNR",AM_DRNR);
			values.put("AM_STRT_NAME",AM_STRT_NAME);
			values.put("AM_PHNO1",AM_PHNO1); 
			values.put("AM_SUIT_NO",AM_SUIT_NO);
			values.put("AM_FLOR_NO",AM_FLOR_NO);
			values.put("AM_ADDR_STYP",AM_ADDR_STYP);
			values.put("AM_ADDR_DEFN_TYPE",AM_ADDR_DEFN_TYPE);
			values.put("AM_GETY_LEVL",AM_GETY_LEVL);
			values.put("AM_GETY_TYPE",AM_GETY_TYPE);
			values.put("AM_GETY_CODE",AM_GETY_CODE);
			values.put("u11",(cst.getString(6)== null?"":cst.getString(6)));
			values.put("u12",(cst.getString(7)== null?"":cst.getString(7)));
			values.put("u13",(cst.getString(8)== null?"":cst.getString(8)));
			values.put("u14",(cst.getString(9)== null?"":cst.getString(9)));
			values.put("u15",(cst.getString(10)== null?"":cst.getString(10)));
			values.put("u16",(cst.getString(11)== null?"":cst.getString(11)));
			values.put("u17",(cst.getString(12)== null?"":cst.getString(12)));
			values.put("Zipcode",(cst.getString(13)== null?"":cst.getString(13)));
			values.put("c11",(cst.getString(14)== null?"":cst.getString(14)));
			values.put("c12",(cst.getString(15)== null?"":cst.getString(15)));
			values.put("c13",(cst.getString(16)== null?"":cst.getString(16)));
			values.put("c14",(cst.getString(17)== null?"":cst.getString(17)));
			values.put("c15",(cst.getString(18)== null?"":cst.getString(18)));
			values.put("c16",(cst.getString(19)== null?"":cst.getString(19)));
			result.add(values);
			//}
			//Added by rama
			if(cst != null){
				cst.close();
			}
		}
		
		//Added by rama
		if(rs!=null)
			rs.close();
		if(pst != null)
			pst.close();
		
		return result;
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList getManifestDates(Connection con,
									  String webclientId,
									  String BranchId,
									  String startDate,
									  String endDate) throws Exception{
		String strSqlQuery;
		PreparedStatement pst=null;		
		ResultSet rs=null;
		//HashMap hm = null;
		ArrayList manifestDates = new ArrayList();
		try{
			strSqlQuery =	"	SELECT  distinct(to_char(MD_ISSE_DATE,'DD/MM/YYYY')) "+
							"	FROM WEB_MNFT_DETL "+
							"	WHERE MD_CLNT_ID=?"+
							"	AND TRUNC(MD_ISSE_DATE) between "+
							"	TO_DATE(?,'DD/MM/YYYY') "+
							"	and TO_DATE(?,'DD/MM/YYYY') "+  
							"	AND NVL(MD_DELETE_FLAG,'N') = ? "+
						    "   ORDER BY 1";
			pst=con.prepareCall(strSqlQuery);
			pst.setString(1,webclientId);
			pst.setString(2,startDate);
			pst.setString(3,endDate);
			pst.setString(4,"N");
			
			rs=pst.executeQuery();
			while(rs!=null && rs.next()){
				manifestDates.add(rs.getString(1));
			}
			
		}catch(Exception e){
			//e.printStackTrace();//commented and below added by B.Emerson on 30/04/2004
			AccessLog.Log(e);	
			e.printStackTrace();
		} finally{
			 try{
				 if(rs!=null)  
					 rs.close();
				 if(pst!=null) 
					 pst.close();
			 }
			 catch(Exception e){
				 //e.printStackTrace();//commented and below added by B.Emerson on 30/04/2004
				 AccessLog.Log(e);
				 e.printStackTrace();
			 }
		 }
		
		return manifestDates;	
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList getManifestNumbers(Connection con,
										String webclientId,
										String BranchId,
										String startDate) throws Exception {
		String strSqlQuery;
		PreparedStatement pst=null;		
		ResultSet rs=null;		
		ArrayList manifestNumbers = new ArrayList();
		try{
			//COMMENTED BY RAMA FOR TUNING
			/*strSqlQuery=" SELECT  MD_MNFT_NO "+
						" FROM WEB_MNFT_DETL"+
						" WHERE MD_MNFT_NO in (select GH_GUIA_REFR_NO "+
						" from bok_guia_head where "+
						" GH_ORGN_CLNT_ID=? "+
						" AND TRUNC(GH_ISSE_DATE)=TRUNC(TO_DATE(TO_DATE(?,'DD/MM/YY'),'DD-MON-YY'))"+
						" AND GH_guia_type=? "+
						" AND GH_ORGN_BRNC_ID=?)"+  
						" AND MD_BRNC_ID = ? "+ 
						" ORDER BY 1";*/
			
			strSqlQuery =	" SELECT  MD_MNFT_NO FROM WEB_MNFT_DETL "+
							" WHERE TRUNC(MD_ISSE_DATE)=TO_DATE(?,'DD/MM/YYYY')"+
							" AND MD_CLNT_ID = ? and SUBSTR(MD_BRNC_ID, 1, 3) = SUBSTR(?, 1, 3) ";
			
			pst=con.prepareStatement(strSqlQuery);
			/*pst.setString(1,webclientId);
			pst.setString(2,startDate);			
			pst.setString(3,"H");
			pst.setString(4,BranchId);
			pst.setString(5,BranchId);*/
			
			pst.setString(1,startDate);
			pst.setString(2,webclientId);						
			pst.setString(3,BranchId);			
			rs=pst.executeQuery();
			while(rs.next()){
				manifestNumbers.add(rs.getString(1)==null?"":rs.getString(1));
			}
			
		}catch(Exception e){
			//e.printStackTrace();//commented and below added by B.Emerson on 30/04/2004
			AccessLog.Log(e);
		}finally{
			//Added by rama
			if(rs!=null)
				rs.close();
			if(pst!=null)
				pst.close();
		}
		if(manifestNumbers !=null)
			return manifestNumbers;
		else 
			return null;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList getGuiaDetails(Connection con,
									String mnftno,
									String webclientId,
									String BranchId,
									String startDate,
									String endDate) throws Exception {
		//System.out.println("INSIDE GET GUIA DETAIL ");//commented and below added by B.Emerson on 30/04/2004
		//AAP//AccessLog.Log("INSIDE GET GUIA DETAIL ");
		String strSqlQuery;
		PreparedStatement pst=null;		
		ResultSet rs=null;
		HashMap hm = null;
		ArrayList records = new ArrayList();
		try{
			
			strSqlQuery=" SELECT B.GH_ORGN_BRNC_ID||B.GH_FORM_NO,"+
						"B.GH_NUMB_PACK, "+
						"B.GH_GUIA_NO,"+
						"B.GH_DEST_BRNC_ID,"+						
						"B.GH_DEST_CLNT_NAME,"+
						"TO_CHAR(PACK_WEB.FUN_CALC_SHP_TOTL(B.GH_GUIA_NO),'99999.99') TARIF_AMT,"+
						"TO_CHAR(PACK_WEB.FUN_CALC_ADD_TOTL(B.GH_GUIA_NO),'99999.99') ADDSRVC_AMT,"+
						"TO_CHAR((PACK_WEB.FUN_CALC_SHP_TOTL(B.GH_GUIA_NO) + PACK_WEB.FUN_CALC_ADD_TOTL(B.GH_GUIA_NO)),'999999.99') TOT_AMT "+
						" FROM 	 BOK_GUIA_HEAD B "+
						" WHERE 	B.GH_GUIA_REFR_NO =? "+
						" AND B.GH_ORGN_CLNT_ID=?"+						
						" AND B.GH_GUIA_TYPE=?"+
						" AND SUBSTR (B.GH_ORGN_BRNC_ID, 1, 3) = SUBSTR (?, 1, 3) ";
			
			pst=con.prepareStatement(strSqlQuery);
			pst.setString(1,mnftno);
			pst.setString(2,webclientId);				
			pst.setString(3,"H");
			pst.setString(4,BranchId);
			
			rs=pst.executeQuery();				
			
			hm = new HashMap();	
			while(rs.next()){			
				
				hm.put("GUIAS",rs.getString(1)==null?"":rs.getString(1));
				hm.put("PACKAGES",rs.getString(2)==null?"":rs.getString(2));
				hm.put("DESTINATION",rs.getString(4)==null?"":rs.getString(4));
				hm.put("DESTCLNT",rs.getString(5)==null?"":rs.getString(5));
				hm.put("TARIFF",rs.getString(6)==null?"":rs.getString(6));
				hm.put("ADDSERV",rs.getString(7)==null?"":rs.getString(7));
				hm.put("TOTAL",rs.getString(8)==null?"":rs.getString(8));				
				records.add(hm.clone());
				hm.clear();				
			}	
			
		}catch(Exception e){
			//System.out.println("Exception From  getGuiaDetailsMethod.....");//commented and below added by B.Emerson on 30/04/2004
			AccessLog.Log("Exception From  getGuiaDetailsMethod.....");
			//System.out.println(e.getMessage());//commented and below added by B.Emerson on 30/04/2004
			AccessLog.Log(e.getMessage());
			//System.out.println(e.toString());//commented and below added by B.Emerson on 30/04/2004
			AccessLog.Log(e.toString());
			//e.printStackTrace();//commented and below added by B.Emerson on 30/04/2004
			AccessLog.Log(e);				
			e.printStackTrace();
		} finally {
			 try {
				 if(rs!=null)  
					 rs.close();
				 if(pst!=null) 
					 pst.close();
				 
			 } catch(Exception e){
				 //e.printStackTrace();//commented and below added by B.Emerson on 30/04/2004
				 AccessLog.Log(e);
				 e.printStackTrace();
			 }
		 }
		//System.out.println("BEFORE RETURNING GETGUIA DETAILS " + records);
		return records;
	}	
	
	public int getGuiaCount(Connection con, 
							String webClientId,
							String BranchId,
							String manifestNumber) throws Exception {
		
		//System.out.println("INSIDE GET GUIA AMOUNT ");//commented and below added by B.Emerson on 30/04/2004
		//AAP//AccessLog.Log("INSIDE GET GUIA AMOUNT ");
		
		//String gnoset="";
		//StringTokenizer st=null;
		String query="";
		PreparedStatement pst=null;		
		ResultSet rs=null;
		int totGuiaCount=0;
		
		try{
			query=" SELECT count(1) "+
				  " FROM  BOK_GUIA_HEAD  "+
				  " WHERE GH_ORGN_CLNT_ID=?"+
				  " AND   GH_GUIA_TYPE=?"+
				  " AND SUBSTR(GH_ORGN_BRNC_ID, 1, 3) = SUBSTR (?, 1, 3) "+
				  " AND GH_GUIA_REFR_NO =?";	
			
			pst=con.prepareStatement(query);
			pst.setString(1,webClientId);
			pst.setString(2,"H");
			pst.setString(3,BranchId);
			pst.setString(4,manifestNumber);
			
			rs=pst.executeQuery();
			if(rs!=null && rs.next()){
				totGuiaCount=rs.getInt(1);
			}
			
			
		}catch(Exception e){
			//e.printStackTrace();//commented and below added by B.Emerson on 30/04/2004
			AccessLog.Log(e);
			e.printStackTrace();
		} finally {
			if(rs!=null)  
				rs.close();
			if(pst!=null) 
				pst.close();			
		}
		//System.out.println("GUIA AMOUNT "+totGuiaCount);
		return totGuiaCount;
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public HashMap getTotal(Connection con,
							String webClientId,
							String BranchId,
							String startDate,
							String endDate) throws Exception {
		
		//System.out.println("INSIDE GET TOTAL METHOD ");//commented and below added by B.Emerson on 30/04/2004
		//AAP//AccessLog.Log("INSIDE GET TOTAL METHOD ");
		String strSqlQuery;
		PreparedStatement pst=null;
		ResultSet rs=null;
		HashMap hm = null;
		//ArrayList records = new ArrayList();
		try{
			//con=ConnectDB.getConnection();//Commented By rama
			
			strSqlQuery = "SELECT  COUNT(GH_GUIA_NO), "+
						  "SUM(GH_NUMB_PACK), "+
						  "SUM(PACK_WEB.FUN_CALC_SHP_TOTL(GH_GUIA_NO)) TARIFF_AMT, "+
						  "SUM(PACK_WEB.FUN_CALC_ADD_TOTL(GH_GUIA_NO)) ADD_SRVC_AMNT,"+
						  "(SUM(PACK_WEB.FUN_CALC_SHP_TOTL(GH_GUIA_NO))+ SUM(PACK_WEB.FUN_CALC_ADD_TOTL(GH_GUIA_NO))) TOTAL_AMT "+
						  " FROM BOK_GUIA_HEAD "+
						  " WHERE gh_orgn_clnt_id=? "+
						  " AND SUBSTR (gh_orgn_brnc_id, 1, 3) = SUBSTR (?, 1, 3) "+
						  " AND gh_actv_flag=? "+	
						  " AND gh_guia_refr_no in(	  "+
						  "SELECT  MD_MNFT_NO"+
						  " FROM WEB_MNFT_DETL"+
						  " WHERE MD_CLNT_ID=? "+
						  " AND TRUNC(MD_ISSE_DATE) between TO_DATE(?,'DD/MM/YYYY') and  TO_DATE(?,'DD/MM/YYYY')"+
						  "AND SUBSTR(MD_BRNC_ID, 1, 3) = SUBSTR(?, 1, 3) ) " +
						  
						  " ORDER BY 1 ";

			
			pst=con.prepareStatement(strSqlQuery);		
			
			pst.setString(1,webClientId);
			pst.setString(2,BranchId);
			pst.setString(3,"A");
			pst.setString(4,webClientId);
			pst.setString(5,startDate);
			pst.setString(6,endDate);
			pst.setString(7,BranchId);
			
			rs=pst.executeQuery();

			while(rs.next()){				
				hm =new HashMap();
				hm.put("TOTALGUIAS",rs.getString(1));
				hm.put("TOTALPACK",rs.getString(2));
				hm.put("TOTALTARIFF",rs.getString(3));
				hm.put("TOTALADD",rs.getString(4));
				hm.put("TOTALAMOUNT",rs.getString(5));
			}
			
		}catch(Exception e){
			//System.out.println("Exception From  getTotals Method.....");//commented and below added by B.Emerson on 30/04/2004
			AccessLog.Log("Exception From  getTotals Method.....");
			//System.out.println(e.getMessage());//commented and below added by B.Emerson on 30/04/2004
			AccessLog.Log(e.getMessage());
			//System.out.println(e.toString());//commented and below added by B.Emerson on 30/04/2004
			AccessLog.Log(e.toString());
			//e.printStackTrace();//commented and below added by B.Emerson on 30/04/2004
			AccessLog.Log(e);
			e.printStackTrace();
		}finally{
			if(rs!=null)
				rs.close();
			if(pst!=null)
				pst.close();
		}
		//System.out.println("BEFORE RETURNING GETTOTALS "+hm);
		if(hm !=null)
			return hm;
		else 
			return null;
	}
	public java.sql.Timestamp getTimeStamp(Connection con)throws Exception{
		PreparedStatement pst = con.prepareStatement("select sysdate from dual");//Checked
		ResultSet rs = pst.executeQuery();
		java.sql.Timestamp sysTimeStamp =null;
		if(rs.next())
			//sysDate = rs.getString(1);
			sysTimeStamp=rs.getTimestamp(1);

		if(rs!=null)
			rs.close();
		if(pst!=null)
			pst.close();
		//System.out.println("SYSDATE WITH TIME STAMP IN SERVICES ACTION "+sysTimeStamp);//commented and below added by B.Emerson on 30/04/2004
		//AAP//AccessLog.Log("SYSDATE WITH TIME STAMP IN SERVICES ACTION "+sysTimeStamp);
		return sysTimeStamp;
	}
}