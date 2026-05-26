/**
 * File Name    : ClientReport3.java
 *  
 * Modified by Balaji  On 01-DEC-2009:	 Purpos: To avoid display the amount for the client, doesn't have display flag from web_clnt_mstr
 */

import bean.Global;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import java.awt.*;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import logger.AccessLog;



public class ClientReport3{
	Document document;
	
	public void getReport(Connection con,
						  HashMap values,
						  String fileName,
						  String fromDate,
						  String toDate,
						  String clientName,
						  String displayFlag
						  )
	{
		try{
			
			document = new Document(PageSize.A4.rotate(),10,10,10,10);
			//document.setHtmlStyleClass("Report.css");
			PdfWriter.getInstance(document,new FileOutputStream(fileName));
			document.open();
			Paragraph top = new Paragraph();
			top.add(new Chunk(" ").setLocalDestination("top"));
			document.add(top);
			PdfPTable table = new PdfPTable(1);
			
			table.getDefaultCell().setBorderWidth(0);
			table.getDefaultCell().setPadding(3);
			HashMap records= values;
			HashMap totals = null;
			ArrayList defaultAddress = (ArrayList)records.get("defaultAddress");
			if(defaultAddress.isEmpty()){
				table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
				//table.addCell("IMPULSORA DE TRANSPORTES MEXICANOS SA DE CV");//Commented by rama
				table.addCell(clientName);//Added by rama
			}else{
				HashMap defaultAddressValues = (HashMap)defaultAddress.get(0);
				table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
				//table.addCell("IMPULSORA DE TRANSPORTES MEXICANOS SA DE CV");//Commented by rama
				table.addCell(clientName);//Added by rama
				table.addCell(defaultAddressValues.get("AM_STRT_NAME")+" "+							  							  
							  defaultAddressValues.get("AM_DRNR"));				
				
				table.addCell(defaultAddressValues.get("u16")+" "+
							  defaultAddressValues.get("u17"));								
			}
			table.addCell(" ");
			table.addCell(" Envios por Cliente Destino ");
			table.addCell(" ");
			table.addCell(" Periodo: "+fromDate+" to "+toDate+" ");
			document.add(table);			
			int columnWidth[] = {25,7,6,8,8,6,6,6,6,7,6,10};
			Table dataTable = new Table(12);
			
			//dataTable.setTableFitsPage(true);				
			dataTable.setWidth(100);			
			dataTable.setBorderWidth(1);
			//dataTable.setCellpadding(2);//AAP01
			dataTable.setPadding(2);//AAP01
			dataTable.setWidths(columnWidth);
			Cell cell = new Cell("Destino");
			cell.setColspan(2);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setHeader(true);
			dataTable.addCell(cell);
			cell = new Cell(" ");
			cell.setColspan(2);
			dataTable.addCell(cell);
			cell = new Cell("Importe");
			cell.setColspan(8);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setHeader(true);
			dataTable.addCell(cell);
			cell = new Cell("Cliente");
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setHeader(true);
			dataTable.addCell(cell);
			cell = new Cell("Sucursal");
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setHeader(true);
			dataTable.addCell(cell);
			cell = new Cell("No. Guias");
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setHeader(true);
			dataTable.addCell(cell);
			cell = new Cell("Paquetes");
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setHeader(true);
			dataTable.addCell(cell);
			cell = new Cell("Tarifa");
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setHeader(true);
			dataTable.addCell(cell);
			cell = new Cell("EAD");
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setHeader(true);
			dataTable.addCell(cell);
			cell = new Cell("RAD");
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setHeader(true);
			dataTable.addCell(cell);
			cell = new Cell("Seguro");
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setHeader(true);
			dataTable.addCell(cell);
			cell = new Cell("COD");
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setHeader(true);
			dataTable.addCell(cell);
			cell = new Cell("Acuse de recibo");
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setHeader(true);
			dataTable.addCell(cell);
			cell = new Cell("Servicio Adicionales");
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setHeader(true);
			dataTable.addCell(cell);
			cell = new Cell("Total");
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setHeader(true);
			dataTable.addCell(cell);
			int guias = 0;
			int packages = 0;
			double tariffAmount = 0.0;
			double eadAmount	= 0.0;
			double radAmount	= 0.0;
			double insAmount	= 0.0;
			double ackAmount	= 0.0;
			double codAmount	= 0.0;
			double addServ		= 0.0;
			double grdTotal		= 0.0;
			
			String sTariffAmount = "";
			String sEadAmount	= "";
			String sRadAmount	= "";
			String sInsAmount	= "";
			String sAckAmount	= "";
			String sCodAmount	= "";
			String sAddServ		= "";
			String sGrdTotal		= "";
			
			DecimalFormat df = new DecimalFormat(".00");
			
			//-----------------------------			
			ArrayList masterRecords = (ArrayList) records.get("destDetail");
			for(int i=0;i < masterRecords.size();i++){
				
				HashMap masterRecordsValues = (HashMap)masterRecords.get(i);
				
				String link = "link"+i;
				String destClientName =(String) masterRecordsValues.get("destClientName");
				cell = new Cell(new  Chunk(destClientName,FontFactory.getFont(FontFactory.COURIER,12,Font.UNDERLINE+Font.BOLD,new Color(0,0,255))).setLocalGoto(link));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				dataTable.addCell(cell);
				
				cell = new Cell(masterRecordsValues.get("destBranchId").toString());
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				dataTable.addCell(cell);
				
				
				
				cell = new Cell(masterRecordsValues.get("guiaCount").toString());
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				dataTable.addCell(cell);
				try{
					guias = guias + Integer.parseInt(masterRecordsValues.get("guiaCount").toString());
				}
				catch(NumberFormatException nfe){
					AccessLog.Log(nfe);
				}
				
				cell = new Cell(masterRecordsValues.get("packages").toString());
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				dataTable.addCell(cell);				
				try{
					packages = packages + Integer.parseInt((masterRecordsValues.get("packages") == null ? "0" :masterRecordsValues.get("packages").toString()));
				}
				catch(NumberFormatException nfe){
					AccessLog.Log(nfe);
				}
				
				ArrayList clientDetails = (ArrayList)masterRecordsValues.get("clAmount");
				totals = getTotals(clientDetails);
				String tarAmt = (totals.get("tariffAmount") == null ? "0.0":totals.get("tariffAmount").toString());
				tarAmt =displayFlag.equalsIgnoreCase("Y")?tarAmt:"******";	// Code added on 01/12/2009
				cell = new Cell(tarAmt);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				dataTable.addCell(cell);
				try{
					Double d= Double.valueOf(tarAmt);
					tariffAmount = tariffAmount + d.doubleValue();
					sTariffAmount = df.format(tariffAmount);
				}
				catch(NumberFormatException nfe){
					AccessLog.Log(nfe);
				}
				String eadAmt = (totals.get("eadAmount")== null ?"0.0":totals.get("eadAmount").toString());
				eadAmt =displayFlag.equalsIgnoreCase("Y")?eadAmt:"******";	// Code added on 01/12/2009
				cell = new Cell(eadAmt);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				dataTable.addCell(cell);
				try{
					Double d = Double.valueOf(eadAmt);
					eadAmount = eadAmount + d.doubleValue();
					sEadAmount = df.format(eadAmount);
				}
				catch(NumberFormatException nfe){					
					AccessLog.Log(nfe);
				}
				String radAmt = (totals.get("radAmount") == null ? "0.0" : totals.get("radAmount").toString());
				radAmt =displayFlag.equalsIgnoreCase("Y")?radAmt:"******";	// Code added on 01/12/2009
				cell = new Cell(radAmt);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				dataTable.addCell(cell);
				try{
					Double d = Double.valueOf(radAmt);
					radAmount = radAmount + d.doubleValue();
					sRadAmount = df.format(radAmount);
				}
				catch(NumberFormatException nfe){					
					AccessLog.Log(nfe);
				}
				String insAmt = (totals.get("insAmount") == null ?"0.0" : totals.get("insAmount").toString());
				insAmt =displayFlag.equalsIgnoreCase("Y")?insAmt:"******";	// Code added on 01/12/2009
				cell = new Cell(insAmt);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);				
				dataTable.addCell(cell);
				try{
					Double d = Double.valueOf(insAmt);
					insAmount = insAmount + d.doubleValue();
					sInsAmount = df.format(insAmount);
				}
				catch(NumberFormatException nfe){					
					AccessLog.Log(nfe);
				}
				String codAmt = (totals.get("codAmount")==null?"0.0":totals.get("codAmount").toString());
				codAmt =displayFlag.equalsIgnoreCase("Y")?codAmt:"******";	// Code added on 01/12/2009
				cell = new Cell(codAmt);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				dataTable.addCell(cell);
				try{
					Double d = Double.valueOf(codAmt);
					codAmount = codAmount + d.doubleValue();
					sCodAmount = df.format(codAmount);
				}
				catch(NumberFormatException nfe){					
					AccessLog.Log(nfe);
				}
				String ackAmt = (totals.get("ackAmount")==null ?"0.0":totals.get("ackAmount").toString());		
				ackAmt =displayFlag.equalsIgnoreCase("Y")?ackAmt:"******";	// Code added on 01/12/2009
				cell = new Cell(ackAmt);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				dataTable.addCell(cell);
				try{
					Double d = Double.valueOf(ackAmt);
					ackAmount = ackAmount + d.doubleValue();
					sAckAmount = df.format(ackAmount);
				}
				catch(NumberFormatException nfe){					
					AccessLog.Log(nfe);
				}
				String addServAmt = (totals.get("addService")== null ?"0.0":totals.get("addService").toString());
				addServAmt =displayFlag.equalsIgnoreCase("Y")?addServAmt:"******";	// Code added on 01/12/2009
				cell = new Cell(addServAmt);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);				
				dataTable.addCell(cell);
				try{
					Double d = Double.valueOf(addServAmt);
					addServ = addServ + d.doubleValue();
					sAddServ = df.format(addServ);
				}
				catch(NumberFormatException nfe){
					AccessLog.Log(nfe);
				}
				String grdTotalAmt = (totals.get("grandTotal")== null?"0.0":totals.get("grandTotal").toString());
				grdTotalAmt =displayFlag.equalsIgnoreCase("Y")?grdTotalAmt:"******";	// Code added on 01/12/2009
				cell = new Cell(grdTotalAmt);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				dataTable.addCell(cell);		
				try{
					Double d = Double.valueOf(grdTotalAmt);
					grdTotal = grdTotal + d.doubleValue();
					sGrdTotal = df.format(grdTotal);
				}
				catch(NumberFormatException nfe){
					AccessLog.Log(nfe);
				}
			}
			cell = new Cell("  ");
			cell.setColspan(12);
			dataTable.addCell(cell);
			
			if((displayFlag == null) || (!displayFlag.equalsIgnoreCase("Y"))) // Code added on 01/12/2009
			{
				sTariffAmount = "*******";
				sEadAmount	= "*******";
				sRadAmount	= "*******";
				sInsAmount	= "*******";
				sAckAmount	= "*******";
				sCodAmount	= "*******";
				sAddServ	= "*******";
				sGrdTotal	= "*******";
			}
			
			
			cell = new Cell(" Total ");
			cell.setColspan(2);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);//Added by rama
			dataTable.addCell(cell);
			
			cell = new Cell(guias+"");
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);//Added by rama
			dataTable.addCell(cell);
			
			cell = new Cell(packages+"");
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);//Added by rama
			dataTable.addCell(cell);
			
			//cell = new Cell(df.format(tariffAmount)); //commented and updated below on 01/12/2009
			cell = new Cell(sTariffAmount);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);//Added by rama
			dataTable.addCell(cell);
			
//			cell = new Cell(df.format(eadAmount));	//commented and updated below on 01/12/2009
			cell = new Cell(sEadAmount);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);//Added by rama
			dataTable.addCell(cell);
			
//			cell = new Cell(df.format(radAmount));	//commented and updated below on 01/12/2009
			cell = new Cell(sRadAmount);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);//Added by rama
			dataTable.addCell(cell);
			
//			cell = new Cell(df.format(insAmount));	//commented and updated below on 01/12/2009
			cell = new Cell(sInsAmount);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);//Added by rama
			dataTable.addCell(cell);
			
//			cell = new Cell(df.format(codAmount));	//commented and updated below on 01/12/2009
			cell = new Cell(sCodAmount);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);//Added by rama
			dataTable.addCell(cell);
			
//			cell = new Cell(df.format(ackAmount));	//commented and updated below on 01/12/2009
			cell = new Cell(sAckAmount);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);//Added by rama
			dataTable.addCell(cell);
			
//			cell = new Cell(df.format(addServ));	//commented and updated below on 01/12/2009
			cell = new Cell(sAddServ);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);//Added by rama
			dataTable.addCell(cell);
			
//			cell = new Cell(df.format(grdTotal));	//commented and updated below on 01/12/2009
			cell = new Cell(sGrdTotal);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);//Added by rama
			dataTable.addCell(cell);
			document.add(dataTable);
			Paragraph p1 = new Paragraph();
			p1.add(" ");
			p1.add(" ");
			p1.add(" ");
			p1.add(" ");
			p1.add(" ");
			p1.add(" ");
			document.add(p1);
			document.newPage();
			
			ArrayList masterRecords1 = (ArrayList) records.get("destDetail");
			for(int i=0;i < masterRecords1.size();i++){
				String link = "link"+i;
				HashMap masterRecordsValues1 = (HashMap)masterRecords1.get(i);
				ArrayList clientDetails = (ArrayList)masterRecordsValues1.get("clAmount");
				
				String destClientName =(String) masterRecordsValues1.get("destClientName");
				String destBranchName =(String) masterRecordsValues1.get("destBranchName");
				
				//AccessLog.Log("destClientName"+destClientName);				
				//AccessLog.Log("destBranchName"+destBranchName);
				
				displayClientDetails(clientDetails,link,destClientName,destBranchName,displayFlag);
			}
			//document.close();
		}
		catch(Exception e){
			//e.printStackTrace();//commented and below added by B.Emerson on 30/04/2004
			AccessLog.Log(e);
		}finally{
				document.close();
				//pdfwriter.flush();
				//pdfwriter.close();
		}
	}
	
	public HashMap getTotals(ArrayList clientDetails)throws Exception{
		int packages = 0;
		double tariffAmount = 0.0;
		double eadAmount	= 0.0;
		double radAmount	= 0.0;
		double insAmount	= 0.0;
		double ackAmount	= 0.0;
		double codAmount	= 0.0;
		double addServ		= 0.0;
		double grdTotal		= 0.0;
		
		for(int i=0;i < clientDetails.size();i++){
			
			HashMap values = (HashMap)clientDetails.get(i);
			packages = packages + Integer.parseInt((values.get("packages")==null ? "0" : values.get("packages").toString()));
			
			//AccessLog.Log("Rama..... shp_totl "+values.get("shp_totl"));
			//AccessLog.Log("Rama..... ead_totl "+values.get("ead_totl"));
			//AccessLog.Log("Rama..... rad_totl "+values.get("rad_totl"));
			//AccessLog.Log("Rama..... ins_totl "+values.get("ins_totl"));
			//AccessLog.Log("Rama..... cod_totl "+values.get("cod_totl"));
			//AccessLog.Log("Rama..... ack_totl "+values.get("ack_totl"));
			//AccessLog.Log("Rama..... oth_totl "+values.get("oth_totl"));
			//AccessLog.Log("Rama..... add_totl "+values.get("add_totl"));
			
			Double d =  Double.valueOf((values.get("shp_totl") == null?"0.0":values.get("shp_totl").toString()));
			tariffAmount = tariffAmount + d.doubleValue();
			
			Double d1 =  Double.valueOf((values.get("ead_totl") == null?"0.0":values.get("ead_totl").toString()));
			eadAmount = eadAmount +  d1.doubleValue();
			
			Double d2 =  Double.valueOf((values.get("rad_totl") == null?"0.0":values.get("rad_totl").toString()));
			radAmount = radAmount + d2.doubleValue();
			
			Double d3 =  Double.valueOf((values.get("ins_totl") == null?"0.0":values.get("ins_totl").toString()));
			insAmount = insAmount + d3.doubleValue();
			
			Double d4 =  Double.valueOf((values.get("cod_totl") == null?"0.0":values.get("cod_totl").toString()));	
			codAmount = codAmount + d4.doubleValue();
			
			Double d5 =  Double.valueOf((values.get("ack_totl") == null?"0.0":values.get("ack_totl").toString()));	
			ackAmount = ackAmount + d5.doubleValue();
			
			Double d6 =  Double.valueOf((values.get("oth_totl") == null?"0.0":values.get("oth_totl").toString()));
			addServ  = addServ + d6.doubleValue();	
			
			Double d7 =  Double.valueOf((values.get("add_totl") == null?"0.0":values.get("add_totl").toString()));	
			grdTotal = grdTotal + d7.doubleValue();
		}
		HashMap grandTotals = new HashMap();
		DecimalFormat df = new DecimalFormat(".00");
		grandTotals.put("tariffAmount",df.format(tariffAmount));
		grandTotals.put("eadAmount",df.format(eadAmount));
		grandTotals.put("radAmount",df.format(radAmount));
		grandTotals.put("insAmount",df.format(insAmount));
		grandTotals.put("ackAmount",df.format(ackAmount));
		grandTotals.put("codAmount",df.format(codAmount));
		grandTotals.put("addService",df.format(addServ));
		grandTotals.put("grandTotal",df.format(grdTotal));
		return grandTotals;
	}
	public void displayClientDetails(ArrayList clientDetails,
									 String link,
									 String destClientName,
									 String destBranchName,
									 String displayFlag)throws Exception{
		
		Paragraph p1 = new Paragraph();
		PdfPTable table1 = new PdfPTable(1);
		table1.getDefaultCell().setBorderWidth(0);
		table1.getDefaultCell().setPadding(3);
		table1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);			
		p1.add(new Chunk(" ").setLocalDestination(link));
		table1.addCell(p1);		
		table1.addCell(destClientName);
		table1.addCell(destBranchName);
		table1.addCell(" ");
		document.add(table1);

		int colWidth[] = {10,9,9,10,9,9,9,9,9,9,9};
		Table dataTable1 = new Table(11);
		dataTable1.setWidth(100);
		dataTable1.setBorderWidth(1);
		//dataTable1.setCellpadding(2);//AAP01
		dataTable1.setPadding(2);//AAP01
		//AAP01
		dataTable1.setWidths(colWidth);

		Cell cell = new Cell();
		cell.setHeader(true);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setColspan(3);
		dataTable1.addCell(cell);

		cell = new Cell("Importe");
		cell.setHeader(true);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setColspan(8);
		dataTable1.addCell(cell);

		cell = new Cell("Fecha");
		cell.setHeader(true);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		dataTable1.addCell(cell);
		
		cell = new Cell("Guía");
		cell.setHeader(true);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		dataTable1.addCell(cell);
		
		cell = new Cell("Paquetes");
		cell.setHeader(true);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		dataTable1.addCell(cell);
		
		cell = new Cell("Tarifa");
		cell.setHeader(true);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		dataTable1.addCell(cell);
		
		cell = new Cell("EAD");
		cell.setHeader(true);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		dataTable1.addCell(cell);
		
		cell = new Cell("RAD");
		cell.setHeader(true);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		dataTable1.addCell(cell);
		
		cell = new Cell("Seguro");
		cell.setHeader(true);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		dataTable1.addCell(cell);
		
		cell = new Cell("Acuse de recibo");
		cell.setHeader(true);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		dataTable1.addCell(cell);
		
		cell = new Cell("COD ");
		cell.setHeader(true);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		dataTable1.addCell(cell);
		
		cell = new Cell("Servicio Adi.");
		cell.setHeader(true);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		dataTable1.addCell(cell);
		
		cell = new Cell("Total");
		cell.setHeader(true);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		dataTable1.addCell(cell);
		
		int packages = 0;
		double tariffAmount = 0.0;
		String STariffAmount="";
		
		double eadAmount	= 0.0;
		String SEadAmount="";
		
		double radAmount	= 0.0;
		String SRadAmount="";
		
		double insAmount	= 0.0;
		String SInsAmount="";
		
		double ackAmount	= 0.0;
		String SAckAmount="";
		
		double codAmount	= 0.0;
		String SCodAmount="";
		
		double addServ		= 0.0;
		String SAddServ="";
		
		double grdTotal		= 0.0;
		String SGrdTotal="";
		
		DecimalFormat df = new DecimalFormat(".00");	
		
		//----------------------------------------------
		for(int i=0;i < clientDetails.size();i++){
			HashMap values = (HashMap)clientDetails.get(i);
			cell = new Cell(values.get("isseDate").toString());
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable1.addCell(cell);
			
			cell = new Cell(values.get("GuiaNumb")+"");			
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable1.addCell(cell);

			cell = new Cell(values.get("packages")+"");
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable1.addCell(cell);
			try{
				packages = packages + Integer.parseInt(values.get("packages")+"");
			}
			catch(NumberFormatException nfe){				
				AccessLog.Log(nfe);
			}
			//cell = new Cell(values.get("shp_totl")+"");

			cell = new Cell(displayFlag.equalsIgnoreCase("Y")?values.get("shp_totl")+"":"******");	// Code added on 01/12/2009
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable1.addCell(cell);			
			try{				
				Double d =  Double.valueOf((values.get("shp_totl") == null?"0.0":values.get("shp_totl").toString()));
				tariffAmount = tariffAmount + d.doubleValue();
				STariffAmount = df.format(tariffAmount);
			}
			catch(NumberFormatException nfe){
				//AccessLog.Log("Continuing with the error");//commented and below added by B.Emerson on 30/04/2004
				AccessLog.Log("Continuing with the error");
				AccessLog.Log(nfe);
				
			}
			
			//cell = new Cell(values.get("ead_totl")+"");
			cell = new Cell(displayFlag.equalsIgnoreCase("Y")?values.get("ead_totl")+"":"******");	// Code added on 01/12/2009
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable1.addCell(cell);
			try{
				Double d =  Double.valueOf((values.get("ead_totl") == null?"0.0":values.get("ead_totl").toString()));
				eadAmount = eadAmount +  d.doubleValue();
				SEadAmount = df.format(eadAmount);
			}
			catch(NumberFormatException nfe){
				//AccessLog.Log("Continuing with the error");//commented and below added by B.Emerson on 30/04/2004
				AccessLog.Log("Continuing with the error");
				AccessLog.Log(nfe);
			}
			
			//cell = new Cell(values.get("rad_totl")+"");
			cell = new Cell(displayFlag.equalsIgnoreCase("Y")?values.get("rad_totl")+"":"******");	// Code added on 01/12/2009
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable1.addCell(cell);
			try{
				Double d =  Double.valueOf((values.get("rad_totl") == null?"0.0":values.get("rad_totl").toString()));
				radAmount = radAmount + d.doubleValue();
				SRadAmount=df.format(radAmount);
				
			}
			catch(NumberFormatException nfe){
				//AccessLog.Log("Continuing with the error");//commented and below added by B.Emerson on 30/04/2004
				AccessLog.Log("Continuing with the error");
				AccessLog.Log(nfe);
			}
			
			//cell = new Cell(values.get("ins_totl")+"");
			cell = new Cell(displayFlag.equalsIgnoreCase("Y")?values.get("ins_totl")+"":"******");	// Code added on 01/12/2009
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable1.addCell(cell);
			try{
				Double d =  Double.valueOf((values.get("ins_totl") == null?"0.0":values.get("ins_totl").toString()));
				insAmount = insAmount + d.doubleValue();
				SInsAmount = df.format(insAmount);
			}
			catch(NumberFormatException nfe){
				//AccessLog.Log("Continuing with the error");//commented and below added by B.Emerson on 30/04/2004
				AccessLog.Log("Continuing with the error");
				AccessLog.Log(nfe);
			}
			
			//cell = new Cell(values.get("ack_totl")+"");
			cell = new Cell(displayFlag.equalsIgnoreCase("Y")?values.get("ack_totl")+"":"******");	// Code added on 01/12/2009
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable1.addCell(cell);
			try{
				Double d =  Double.valueOf((values.get("ack_totl") == null?"0.0":values.get("ack_totl").toString()));	
				ackAmount = ackAmount + d.doubleValue();
				SAckAmount = df.format(ackAmount);
			}
			catch(NumberFormatException nfe){
				//AccessLog.Log("Continuing with the error");//commented and below added by B.Emerson on 30/04/2004
				AccessLog.Log("Continuing with the error");
				AccessLog.Log(nfe);
			}
			//cell = new Cell(values.get("cod_totl")+"");
			cell = new Cell(displayFlag.equalsIgnoreCase("Y")?values.get("cod_totl")+"":"******");	// Code added on 01/12/2009
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable1.addCell(cell);
			try{
				Double d =  Double.valueOf((values.get("cod_totl") == null?"0.0":values.get("cod_totl").toString()));	
				codAmount = codAmount + d.doubleValue();
				SCodAmount = df.format(codAmount);
			}
			catch(NumberFormatException nfe){
				//AccessLog.Log("Continuing with error");//commented and below added by B.Emerson on 30/04/2004
				AccessLog.Log("Continuing with error");
				AccessLog.Log(nfe);
			}
			
			//cell = new Cell(values.get("oth_totl")+"");
			cell = new Cell(displayFlag.equalsIgnoreCase("Y")?values.get("oth_totl")+"":"******");	// Code added on 01/12/2009
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable1.addCell(cell);
			
			try{
				Double d =  Double.valueOf((values.get("oth_totl") == null?"0.0":values.get("oth_totl").toString()));
				addServ  = addServ + d.doubleValue();
				SAddServ = df.format(addServ);
			}
			catch(NumberFormatException nfe){
				//AccessLog.Log("Continuing with error");//commented and below added by B.Emerson on 30/04/2004
				AccessLog.Log("Continuing with error");
				AccessLog.Log(nfe);
			}
			
			//cell = new Cell(values.get("add_totl")+"");
			cell = new Cell(displayFlag.equalsIgnoreCase("Y")?values.get("add_totl")+"":"******");	// Code added on 01/12/2009
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable1.addCell(cell);
			try{
				Double d =  Double.valueOf((values.get("add_totl") == null?"0.0":values.get("add_totl").toString()));	
				grdTotal = grdTotal + d.doubleValue();
				SGrdTotal = df.format(grdTotal);
			}
			catch(NumberFormatException nfe){
				//AccessLog.Log("Continuing with error");//commented and below added by B.Emerson on 30/04/2004
				AccessLog.Log("Continuing with error");
				AccessLog.Log(nfe);
			}
		}
		//--------------------------------------------		
		
		 // Below Code added on 01/12/2009 
		
		if((displayFlag == null) || (!displayFlag.equalsIgnoreCase("Y")))
		{
			STariffAmount	="*******";
			SEadAmount	="*******";
			SRadAmount	="*******";
			SInsAmount	="*******";
			SAckAmount	="*******";
			SCodAmount	="*******";
			SAddServ	="*******";
			SGrdTotal	="*******";
		}
		
		cell = new Cell("Total");
		cell.setColspan(2);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		dataTable1.addCell(cell);
		
		cell = new Cell(packages+"");
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		dataTable1.addCell(cell);
			
		
		//cell = new Cell(df.format(tariffAmount));
		cell = new Cell(STariffAmount);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		dataTable1.addCell(cell);		
		
		//cell = new Cell(df.format(eadAmount));
		cell = new Cell(SEadAmount);	
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		dataTable1.addCell(cell);		
		
		//cell = new Cell(df.format(radAmount));
		cell = new Cell(SRadAmount);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		dataTable1.addCell(cell);		

		//cell = new Cell(df.format(insAmount));
		cell = new Cell(SInsAmount);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		dataTable1.addCell(cell);		
		
		//cell = new Cell(df.format(ackAmount));
		cell = new Cell(SAckAmount);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		dataTable1.addCell(cell);		
		
		//cell = new Cell(df.format(codAmount));
		cell = new Cell(SCodAmount);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		dataTable1.addCell(cell);		
		
		//cell = new Cell(df.format(addServ));
		cell = new Cell(SAddServ);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		dataTable1.addCell(cell);		
		
		//cell = new Cell(df.format(grdTotal));
		cell = new Cell(SGrdTotal);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		dataTable1.addCell(cell);		

		cell = new Cell(" ");
		cell.setColspan(11);
		dataTable1.addCell(cell);
		
		cell = new Cell(new Chunk("TOP",FontFactory.getFont(FontFactory.COURIER,12,Font.BOLD+Font.UNDERLINE,new Color(0,0,255))).setLocalGoto("top"));
		cell.setColspan(11);
		dataTable1.addCell(cell);
		
		document.add(dataTable1);
		Paragraph p = new Paragraph();
		p.add(" ");
		p.add(" ");
		p.add(" ");
		p.add(" ");
		p.add(" ");		
		document.add(p);		
		document.newPage();
		//return grandTotals;
	}
}