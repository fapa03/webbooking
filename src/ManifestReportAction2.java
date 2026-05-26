
/**
 * File Name    : ManifestReportAction2.java
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
import java.text.DecimalFormat;
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
public class ManifestReportAction2 extends Action{

	public ManifestReportAction2(){
		//System.out.println("Inside ManifestReport2 Action..");//commented and below added by B.Emerson on 30/04/2004
		//AAP//AccessLog.Log("Inside ManifestReport2 Action..");
	}
	
	int TotRec=0;
	int dateRowspan=0;
	int grandTotGuias=0;
	int grandTotPackages=0;
	double grandTariff=0.0;
	String SgrandTariff="";
	
	double grandEAD=0.0;
	String SgrandEAD="";
	
	double grandRAD=0.0;
	String SgrandRAD="";
	
	double grandINS=0.0;
	String SgrandINS="";
	
	double grandAck=0.0;
	String SgrandAck="";
	
	double grandCOD=0.0;
	String SgrandCOD="";
	
	double addSRVC=0.0;
	String SaddSRVC="";
	
	double grandTot=0.0;
	String SgrandTot="";
	
	PdfWriter pdfwriter=null;
	Document document=null;
	long currenttime=0;
	@SuppressWarnings("rawtypes")
	public ActionForward perform(ActionMapping am,ActionForm af,HttpServletRequest request,HttpServletResponse response)
		throws ServletException,IOException{
		
		Connection con=null;
		HttpSession session=null;			
		String strClientId;
		//String strBranchId;
		String strStartDate;
		String strendDate;
		
		ArrayList defaultAddress=null;
		ArrayList destBranches=null;
		HashMap destBrancheValues=null;
		try {
			
			response.setContentType("application/pdf");
			session=request.getSession(true);
			Global global = (Global)session.getAttribute("sGlobal");

			if(session==null || session.isNew())
				return am.findForward("nosession");

			con=ConnectDB.getConnection();


			if (af instanceof ManifestReportForm2){
				//System.out.println("INSIDE PERFORM METHOD ");//commented and below added by B.Emerson on 30/04/2004
				//AAP//AccessLog.Log("INSIDE PERFORM METHOD ");
				ManifestReportForm2 mrf2 = (ManifestReportForm2) af;
				// Get the Input Parameters From ManifestReportForm..
				
				strClientId = (String)session.getAttribute("sClientId");
				//strBranchId = (String)session.getAttribute("sAssignedBranch");
				
				//strClientId = "7358";
				
				strStartDate = mrf2.getStartDate();
				strendDate =mrf2.getEndDate();		
				// step 1: creation of a document-object
				document = new Document(PageSize.A4.rotate(), 10, 10, 10, 10);
				
				// step 2: we create a writer that listens to the document
				
				//String fileName = request.getRealPath("/")+strClientId+"ManifestReport2.pdf";
				//Added by rama
				File f=new File(request.getRealPath("/")+File.separator+"ManifestReport2");
                //File f=new File(request.getContextPath()+File.separator+"ManifestReport2");
				if(!f.exists())
					f.mkdirs();
				String exportdirectory=f.toString();
				//String fileName = exportdirectory+File.separator+strClientId+"ManifestReport2.pdf";
				currenttime=getTimeStamp(con).getTime();
				String fileName = exportdirectory+File.separator+"ManifestReport2_"+currenttime+".pdf";
				//System.out.println("CURRENT FOR MANIFEST REPORT2 IN ACTION "+currenttime);//commented and below added by B.Emerson on 30/04/2004
				//AAP//AccessLog.Log("CURRENT FOR MANIFEST REPORT2 IN ACTION "+currenttime);
				pdfwriter=PdfWriter.getInstance(document,  new FileOutputStream(fileName));
				
				// step 3: we open the document
				document.open();
				
				defaultAddress=this.getDefaultAddress(con,strClientId);
				
				PdfPTable table = new PdfPTable(1);
				table.getDefaultCell().setBorderWidth(0);
				table.getDefaultCell().setPadding(3);
				table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
				
				// Get the Default Branch Address... and Display It..				
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
				table.addCell("Envíos por Sucursal Destino");
				table.addCell(" ");				
				table.addCell("Periodo :"+strStartDate+" - "+strendDate); 					
				document.add(table);

				// step 4: we add content to the document (this happens in a seperate method)
				// Load the Document for All Destination BranchIds..
				
				destBranches = this.getDestBranchNames(con,strClientId,strStartDate,strendDate,session);
				String strDestBranchId="";
				String strDestBranchName="";
				//System.out.println("NUMBER OF THE DESTINATION BRANCHES MANIFEST REPORT ACTION 2 ..... "+destBranches.size());//commented and below added by B.Emerson on 30/04/2004
				//AAP//AccessLog.Log("NUMBER OF THE DESTINATION BRANCHES MANIFEST REPORT ACTION 2 ..... "+destBranches.size());
				for(int i=0;i<destBranches.size();i++){
					destBrancheValues=(HashMap)destBranches.get(i);
					strDestBranchId=(String)destBrancheValues.get("BRANCHID");
					strDestBranchName=(String)destBrancheValues.get("BRANCHNAME");
					loadDocument(con,document,strDestBranchName,strClientId,strDestBranchId,strStartDate,strendDate, global);
				}
				
				// step 5: we close the document
				//document.newPage();
				//document.close();
				//System.out.println("BEFORE RETURNING PERFORM METHOD ");//commented and below added by B.Emerson on 30/04/2004
				//AAP//AccessLog.Log("BEFORE RETURNING PERFORM METHOD ");
			}
		}catch (Exception e2) {
			//e2.printStackTrace();//commented and below added by B.Emerson on 30/04/2004
			AccessLog.Log(e2);
			e2.printStackTrace();
		}finally{
			//Added by rama
			try{
				if(con!=null)
					con.close();			
				
				document.close();
			}catch(Exception cone){
			}
			
			//pdfwriter.flush();
			//pdfwriter.close();
		}
		request.setAttribute("currenttime",String.valueOf(currenttime));
		return am.findForward("success");
	}
	
	@SuppressWarnings("rawtypes")
	public void loadDocument(Connection con,
							 Document document,
							 String strdestBranchName,
							 String webClientId,
							 String BranchId,
							 String startDate,
							 String endDate,
							 Global global) throws Exception{
		
		//System.out.println("INSIDE LOAD DOCUMENT ");//commented and below added by B.Emerson on 30/04/2004
		//AAP//AccessLog.Log("INSIDE LOAD DOCUMENT ");
		StringBuffer sbBranchId=new  StringBuffer();
		sbBranchId.append(BranchId);
		
		ArrayList manifestNumbers=null;
		ArrayList guiaDetails=null;
		HashMap manifestAmount=null;
		HashMap guiaDetailsValues=null;		
		String manifestNumber="";
		
		try{
			int columnWidth[] = {10,10,5,8,8,8,8,8,10,8,8,8};
			Table dataTable = new Table(12);
			dataTable.setTableFitsPage(true);			
			dataTable.setWidth(100);
			dataTable.setBorderWidth(0);
			//dataTable.setCellpadding(2);//AAP01
			dataTable.setPadding(2);//AAP01
			dataTable.setWidths(columnWidth);
			Cell cell = new Cell("Sucursal Destino: "+strdestBranchName);
			cell.setHeader(true);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setColspan(12);
			cell.setBorder(0);
			dataTable.addCell(cell);
			cell = new Cell(" ");
			cell.setHeader(true);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setBorder(0);
			cell.setColspan(12);
			dataTable.addCell(cell);
			cell = new Cell(" ");
			cell.setHeader(true);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setColspan(4);                
			dataTable.addCell(cell);    
			cell = new Cell("Importe");
			cell.setHeader(true);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setColspan(8);
			dataTable.addCell(cell);    
			cell = new Cell("Fecha ");
			cell.setHeader(true);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);                
			dataTable.addCell(cell);
			cell = new Cell("Manifiesto");
			cell.setHeader(true);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);                
			dataTable.addCell(cell);
			cell = new Cell("Guía");
			cell.setHeader(true);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);                
			dataTable.addCell(cell);
			cell = new Cell("Paquetes");
			cell.setHeader(true);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);                
			dataTable.addCell(cell);
			cell = new Cell("Tarifa");
			cell.setHeader(true);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(cell);    
			cell = new Cell("EAD");
			cell.setHeader(true);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(cell);    
			cell = new Cell("RAD");
			cell.setHeader(true);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(cell);        
			cell = new Cell("Seguro");
			cell.setHeader(true);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(cell);    
			cell = new Cell("Acuse");
			cell.setHeader(true);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(cell);    
			cell = new Cell("COD");
			cell.setHeader(true);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(cell);    
			cell = new Cell("Servicios Adic.");
			cell.setHeader(true);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(cell); 
			cell = new Cell("Total");
			cell.setHeader(true);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(cell);
			
			// Get the Details and populate into the Table...
			
			
			manifestNumbers=this.getManifestNumbers(con,webClientId,BranchId,startDate,endDate);
			String strGuias="";
			String strPack="";
			String strIssueDate="";		
			
			for(int i=0;i<manifestNumbers.size();i++){
				
				manifestNumber = manifestNumbers.get(i).toString();
				
				// Get GuiaDetails for the Manifest...............
				
				guiaDetails = this.getGuiaDetails(con,manifestNumber, BranchId);
				//System.out.println("************** SIZE "+guiaDetails.size());//commented and below added by B.Emerson on 30/04/2004
				//AAP//AccessLog.Log("************** SIZE "+guiaDetails.size());
				if(guiaDetails.size()!=0)
					guiaDetailsValues = (HashMap)guiaDetails.get(0);
				
				strGuias = guiaDetailsValues.get("GUIAS")==null?"0": guiaDetailsValues.get("GUIAS").toString();
				strPack = guiaDetailsValues.get("PACKAGES")==null?"0": guiaDetailsValues.get("PACKAGES").toString();
				strIssueDate =guiaDetailsValues.get("ISSUEDATE")==null?"0": guiaDetailsValues.get("ISSUEDATE").toString();		 
				
				cell = new Cell(strIssueDate);                
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				dataTable.addCell(cell);  
				
				cell = new Cell(manifestNumber);                
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				dataTable.addCell(cell);   
				
				cell = new Cell(strGuias);                
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				dataTable.addCell(cell);   
				
				cell = new Cell(strPack);                
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				dataTable.addCell(cell); 
				
				// Call the PACK_WEB.PROC_CALC
				
				//if((global.displayAmountFlag != null) && (global.displayAmountFlag.equalsIgnoreCase("Y")))
				DecimalFormat df = new DecimalFormat(".00");	
				manifestAmount=this.getManifestAmount(con,manifestNumber,BranchId);				
				
				double totTariff	=	((Double)manifestAmount.get("TOTTARIFF")).doubleValue();
				String StotTariff = df.format(totTariff);
				
				double totEAD		=	((Double)manifestAmount.get("TOTEAD")).doubleValue();
				String StotEAD = String.valueOf(totEAD);
				
				double totRAD		=	((Double)manifestAmount.get("TOTRAD")).doubleValue();
				String StotRAD = String.valueOf(totRAD);
				
				double totINS		=	((Double)manifestAmount.get("TOTINS")).doubleValue();
				String StotINS = String.valueOf(totINS);
				
				double totAck		=	((Double)manifestAmount.get("TOTACK")).doubleValue();
				String StotAck = String.valueOf(totAck);
				
				double totCOD		=	((Double)manifestAmount.get("TOTCOD")).doubleValue();
				String StotCOD = String.valueOf(totCOD);
				
				double totSRVC 	    =	((Double)manifestAmount.get("TOTSRVC")).doubleValue();
				String StotSRVC = String.valueOf(totSRVC);
				
				double sumTot		=	((Double)manifestAmount.get("SUMTOT")).doubleValue();
				String SsumTot = String.valueOf(sumTot);
				
				
				grandTotGuias =grandTotGuias+Integer.parseInt(strGuias);
				grandTotPackages=grandTotPackages+ Integer.parseInt(strPack);
				
				grandTariff =  grandTariff +totTariff;
				SgrandTariff = df.format(grandTariff);
				
				grandEAD	 =  grandEAD +totEAD;
				SgrandEAD = String.valueOf(Math.round(grandEAD));
				
				grandRAD	 =  grandRAD +totRAD;
				SgrandRAD = String.valueOf(grandRAD);
				
				grandINS	 =  grandINS +totINS;
				SgrandINS = String.valueOf(grandINS);
				
				grandAck	 =  grandAck +totAck;
				SgrandAck = String.valueOf(grandAck);
				
				grandCOD	 =  grandCOD + totCOD;
				SgrandCOD = String.valueOf(grandCOD);
				
				addSRVC	 =  addSRVC + totSRVC;
				SaddSRVC = String.valueOf(addSRVC);
				
				grandTot	 =  grandTot +sumTot;
				SgrandTot = String.valueOf(grandTot);
				
				 // Below Code added on 01/12/2009 
				
				if((global.displayAmountFlag == null) || (!global.displayAmountFlag.equalsIgnoreCase("Y")))
				{
					StotTariff ="******";
					StotEAD	="******";
					StotRAD	="******";
					StotINS	="******";
					StotAck	="******";
					StotCOD	="******";
					StotSRVC	="******";
					SsumTot	="******";
					
					SgrandTariff	="*******";
					SgrandEAD	="*******";
					SgrandRAD	="*******";
					SgrandINS	="*******";
					SgrandAck	="*******";
					SgrandCOD	="*******";
					SaddSRVC	="*******";
					SgrandTot	="*******";
				}
				
				
			//	cell = new Cell(""+df.format(totTariff));
				
				cell = new Cell(StotTariff);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				dataTable.addCell(cell);    
				
				cell = new Cell(StotEAD);                
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				dataTable.addCell(cell);  
				
				cell = new Cell(StotRAD);                
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				dataTable.addCell(cell);
				
				cell = new Cell(StotINS);                
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				dataTable.addCell(cell);
				
				cell = new Cell(StotAck);                
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				dataTable.addCell(cell);    
				
				cell = new Cell(StotCOD);                
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				dataTable.addCell(cell);    
				cell = new Cell(StotSRVC);                
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				dataTable.addCell(cell);    
				cell = new Cell(SsumTot);                
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				dataTable.addCell(cell);    
				
				Paragraph p = new Paragraph();
				p.add(" ");
				p.add(" ");
				p.add(" ");
				p.add(" ");
				p.add(" ");
				p.add(" ");
				document.add(p);
				
				
				
			}
			
		//	DecimalFormat df = new DecimalFormat(".00");
			
			cell = new Cell("Total");                
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			dataTable.addCell(cell);
			
			cell = new Cell("");                			
			dataTable.addCell(cell); 
			
			cell = new Cell(""+grandTotGuias);                
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			dataTable.addCell(cell);
			
			cell = new Cell(""+grandTotPackages);                
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			dataTable.addCell(cell);
			
			cell = new Cell(SgrandTariff);                
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			dataTable.addCell(cell); 

			
			
			cell = new Cell(SgrandEAD);                
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			dataTable.addCell(cell); 
			
			cell = new Cell(SgrandRAD);                
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			dataTable.addCell(cell); 
			
			cell = new Cell(SgrandINS);                
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			dataTable.addCell(cell); 
			
			cell = new Cell(SgrandAck);                
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			dataTable.addCell(cell); 
			
			cell = new Cell(SgrandCOD);                
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			dataTable.addCell(cell); 
			
			cell = new Cell(SaddSRVC);                
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			dataTable.addCell(cell); 		
			
			cell = new Cell(SgrandTot);                
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			dataTable.addCell(cell); 
			
			grandTotGuias =0;
			grandTotPackages=0;
			
			grandTariff  =  0.0 ;
			grandEAD	 =  0.0;
			grandRAD	 =  0.0;
			grandINS	 =  0.0;
			grandAck	 =  0.0;
			grandCOD	 =  0.0;
			addSRVC	     =  0.0;
			grandTot	 =  0.0;
			
			SgrandTariff  =	"";
			SgrandEAD	 = 	"";
			SgrandRAD	 = 	"";
			SgrandINS	 =  "";
			SgrandAck	 =  "";
			SgrandCOD	 =  "";
			SaddSRVC	 =  "";
			SgrandTot	 =  "";
			
			document.add(dataTable);	
			

		}catch(Exception e)	{
			//e.printStackTrace();//commented and below added by B.Emerson on 30/04/2004
			AccessLog.Log(e);
			
		}
		//System.out.println("BEFORE RETURNING LOAD DOCUMENT ");//commented and below added by B.Emerson on 30/04/2004
		//AAP//AccessLog.Log("BEFORE RETURNING LOAD DOCUMENT ");
		
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList getDestBranchNames(Connection con,
										String webClientId,
										String startDate,
										String endDate,
										HttpSession session) throws Exception {
		
		//System.out.println("INSIDE GET BRANCHNAMES ");//commented and below added by B.Emerson on 30/04/2004
		//AAP//AccessLog.Log("INSIDE GET BRANCHNAMES ");
	
		String query="";
		PreparedStatement pst = null;
		ArrayList destBranches= new ArrayList();
		HashMap values =null;	
		ResultSet rs = null;
		Global global = (Global)session.getAttribute("sGlobal");
		try{
			query="SELECT distinct GH_DEST_BRNC_ID BranchId,pack_web.FUN_FTCH_BRNC_NAME( GH_DEST_BRNC_ID)BranchName "+
				  " FROM BOK_GUIA_HEAD "+
				  " WHERE GH_ORGN_CLNT_ID = ? "+
				  " AND GH_GUIA_TYPE =? "+
				  " AND GH_DOCU_TYPE = ? "+				  
				  " AND GH_GUIA_REFR_NO in(SELECT  MD_MNFT_NO FROM WEB_MNFT_DETL "+
							"	WHERE TRUNC(MD_ISSE_DATE) BETWEEN "+
							"	TO_DATE(?,'DD/MM/YYYY')"+
							"	AND TO_DATE(?,'DD/MM/YYYY')"+
							"	AND MD_CLNT_ID = ? and SUBSTR(MD_BRNC_ID, 1, 3) = SUBSTR(?, 1, 3))";

			pst=con.prepareCall(query);
			pst.setString(1,webClientId);
			pst.setString(2,"H");
			pst.setString(3,"Q");			
			pst.setString(4,startDate);
			pst.setString(5,endDate);
			pst.setString(6,webClientId);
			pst.setString(7,global.assignedBranch);
			
			rs=pst.executeQuery();
			
			values=new HashMap();			
			while(rs!=null && rs.next()){
				values.put("BRANCHID",rs.getString(1)==null?"":rs.getString(1));
				values.put("BRANCHNAME",rs.getString(2)==null?"":rs.getString(2));
				destBranches.add(values.clone());
				values.clear();
			}
		}catch(Exception e){		
			//e.printStackTrace();//commented and below added by B.Emerson on 30/04/2004
			AccessLog.Log(e);
			e.printStackTrace();
		}finally{
			if(rs!=null)
				rs.close();
			if(pst!=null)
				pst.close();
		}
		//System.out.println("BEFORE RETURNING BRANCHNAMES ");//commented and below added by B.Emerson on 30/04/2004
		//AAP//AccessLog.Log("BEFORE RETURNING BRANCHNAMES ");
		return destBranches;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList getDefaultAddress(Connection con,
									   String destinationClientId)throws Exception {
		
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
			//if(crs.next()){ //Commented by rama
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
	public ArrayList getManifestNumbers(Connection con,String webClientId,String BranchId,String startDate,String endDate) throws Exception{
		
		ArrayList manifestNumbers=new ArrayList();
		PreparedStatement pst=null;
		ResultSet rs=null;
		String query="";
		
		try{
			query= " SELECT  GH_GUIA_REFR_NO"+
				   " FROM BOK_GUIA_HEAD "+
				   " WHERE GH_ORGN_CLNT_ID = ? "+
				   " AND GH_GUIA_TYPE =? "+
				   " AND GH_DOCU_TYPE = ? "+
				   " AND GH_DEST_BRNC_ID = ?  "+
				   " GROUP BY GH_GUIA_REFR_NO ";
			
			pst=con.prepareStatement(query);
			pst.setString(1,webClientId);
			pst.setString(2,"H");
			pst.setString(3,"Q");
			//pst.setString(4,startDate);
			//pst.setString(5,endDate);
			pst.setString(4,BranchId);
			
			rs=pst.executeQuery();
			while(rs!=null && rs.next()){
				manifestNumbers.add(rs.getString(1)==null?"":rs.getString(1));				
			}
			
			
		}catch(Exception e){
			//e.printStackTrace();//commented and below added by B.Emerson on 30/04/2004
			AccessLog.Log(e);
			e.printStackTrace();
		}finally{
			if(rs!=null) 
				rs.close();
			if(pst!=null) 
				pst.close();
		}		
		return manifestNumbers;	
	}

	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList getGuiaDetails(Connection con,
									String ManifestNumber,
									String BranchId) throws Exception {
		
		ArrayList guiaDetails=new ArrayList();
		HashMap values=null;
		PreparedStatement pst=null;
		ResultSet rs=null;
		String query="";
		
		try{
			/*query=	"	SELECT count(GH_GUIA_NO) Guias ,"+
					"	sum(gh_numb_pack) Packages,to_char(gh_isse_date,'DD/MM/YYYY')"+
					"	FROM BOK_GUIA_HEAD "+
					"	WHERE GH_GUIA_REFR_NO = ? "+
					"	AND GH_DEST_BRNC_ID=?"+
					"	GROUP BY to_char(GH_ISSE_DATE,'DD/MM/YYYY')";*/
			
			/*query=	"	SELECT count(GH_GUIA_NO) Guias ,"+
					"	sum(gh_numb_pack) Packages,md.MD_ISSE_DATE"+
					"	FROM BOK_GUIA_HEAD BG, WEB_MNFT_DETL MD "+
					"	WHERE BG.GH_GUIA_REFR_NO = ? "+
					"   and MD.MD_MNFT_NO=BG.GH_GUIA_REFR_NO "+
					"	AND GH_DEST_BRNC_ID=?";		*/
			
			query=" SELECT COUNT(BG.GH_GUIA_NO) GUIAS ,"+
				  "	SUM(BG.GH_NUMB_PACK) PACKAGES,to_char(MD.MD_ISSE_DATE,'DD/MM/YYYY') "+		
				  " FROM BOK_GUIA_HEAD BG, WEB_MNFT_DETL MD "+
				  " WHERE BG.GH_GUIA_REFR_NO = ? "+
				  " AND MD.MD_MNFT_NO=BG.GH_GUIA_REFR_NO"+				   
				  " AND BG.GH_DEST_BRNC_ID=? "+
				  " AND BG.GH_GUIA_TYPE=? "+
				  "	AND NVL(MD_DELETE_FLAG,'N') = ? "+
				  " GROUP BY MD.MD_ISSE_DATE ";
			
			pst=con.prepareStatement(query);
			pst.setString(1,ManifestNumber);	
			pst.setString(2,BranchId);	
			pst.setString(3,"H");
			pst.setString(4,"N");
			
			rs = pst.executeQuery();
			
			values=new HashMap();
			while(rs!=null && rs.next()){
				
				values.put("GUIAS",rs.getString(1)==null?"":rs.getString(1));
				values.put("PACKAGES",rs.getString(2)==null?"":rs.getString(2));
				values.put("ISSUEDATE",rs.getString(3)==null?"":rs.getString(3));
				guiaDetails.add(values.clone());
				values.clear();
			}
			
		}catch(Exception e){
			//e.printStackTrace();//commented and below added by B.Emerson on 30/04/2004
			AccessLog.Log(e);
			e.printStackTrace();
		}finally{
			if(rs!=null) 
				rs.close();
			if(pst!=null) 
				pst.close();
		}		
		return guiaDetails;	
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public HashMap getManifestAmount(Connection con,
									 String manifestNumber,
									 String destBranchId) throws Exception {
		
		HashMap manifestAmount =new HashMap();
		CallableStatement cst=null;
		double totTariff=0.0;
		double totEAD=0.0;
		double totRAD=0.0;
		double totINS=0.0;
		double totAck=0.0;
		double totCOD=0.0;
		double totSRVC=0.0;
		double sumTot=0.0;
		
		
		try{
			
			cst= con.prepareCall("begin PACK_WEB.PRO_FTCH_DESTBRNC_REPORT(?,?,?,?,?,?,?,?,?,?);end;");
			//set the values to IN Parameters...
			cst.setString(1,manifestNumber);			
			
			// Register the Out Parameters.....
			
			cst.registerOutParameter(2,Types.DOUBLE);
			cst.registerOutParameter(3,Types.DOUBLE);
			cst.registerOutParameter(4,Types.DOUBLE);
			cst.registerOutParameter(5,Types.DOUBLE);
			cst.registerOutParameter(6,Types.DOUBLE);
			cst.registerOutParameter(7,Types.DOUBLE);
			cst.registerOutParameter(8,Types.DOUBLE);
			cst.registerOutParameter(9,Types.DOUBLE);
			
			cst.setString(10,destBranchId);
			
			cst.executeQuery();
			
			totTariff = cst.getDouble(2);
			totAck    = cst.getDouble(3);
			totRAD    = cst.getDouble(4);
			totEAD    = cst.getDouble(5);
			totINS	  = cst.getDouble(6);		
			totCOD    = cst.getDouble(7);
			sumTot    = cst.getDouble(8);
			totSRVC   = cst.getDouble(9);			
			
			
			manifestAmount.put("TOTTARIFF",new Double(totTariff));
			manifestAmount.put("TOTEAD",new Double(totEAD));
			manifestAmount.put("TOTRAD",new Double(totRAD));
			manifestAmount.put("TOTINS",new Double(totINS));
			manifestAmount.put("TOTACK",new Double(totAck));
			manifestAmount.put("TOTCOD",new Double(totCOD));
			manifestAmount.put("TOTSRVC",new Double(totSRVC));
			manifestAmount.put("SUMTOT",new Double(sumTot));
			
			
		}catch(Exception e){
			//e.printStackTrace();//commented and below added by B.Emerson on 30/04/2004
			AccessLog.Log(e);
			e.printStackTrace();
		}finally{
			if(cst!=null) 
				cst.close();
		}
		return manifestAmount;	
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