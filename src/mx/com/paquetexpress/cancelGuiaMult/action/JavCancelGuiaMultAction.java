/**
 * @author: EVA MELISSA PACHECO PARRA
 * Fecha de Creación: 24/11/2023
 * Compa��a: PAQUETEXPRESS.
 * Descripci�n del programa: Bean accion para pantalla de cancelacion de guias multiples.
 * FileName: JavCancelGuiaMultAction.java
 * SessionVariables:
 * Other Used Classes:
 * Function Names: 
 * ----------------------------------------------------------------- 
 * MODIFICACIONES:  
 * ------------------------------------------------------------------
 * Clave: 
 * Autor: 
 * Fecha: 
 * Descripci�n: 
 * ------------------------------------------------------------------
 * Clave: 
 * Autor: 
 * Fecha: 
 * Descripci�n: 
 * 
 * ------------------------------------------------------------------ 
 * Clave: 
 * Autor: 
 * Fecha: 
 * Descripci�n: 
 * 
 * ------------------------------------------------------------------ 
 */
package mx.com.paquetexpress.cancelGuiaMult.action;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import bean.Global;
import bean.SheetRecovery;
import beanUtil.ConnectDB;
import logger.AccessLog;
import mx.com.paquetexpress.cancelGuiaMult.form.JavCancelGuiaMultForm;
import mx.com.paquetexpress.cancelGuiaMult.dao.JavCancelGuiaMultDao;
import mx.com.paquetexpress.cancelGuiaMult.dto.JavCancelGuiaMultDTO;

public class JavCancelGuiaMultAction extends Action {
	private final String msgErr = new StringBuffer("MSERR_B_").append(this.getClass().getName()).append(".").toString();
	JavCancelGuiaMultDao dao;
	Time sysTime = null;
	private StringBuffer cnct = new StringBuffer();
	
	
	//DATOS GENERALES DEL ARCHIVO XLS 
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		JavCancelGuiaMultForm guiaForm = (JavCancelGuiaMultForm) form;
		String returnPage = "thispage";
		HttpSession session = request.getSession(true);
		Global global = (Global) session.getAttribute("sGlobal");
		String linkGenCartaPorte = ConnectDB.getCartaPorteExt() + "GenCartaPorte?trackingNoGen=" ;
		dao = new JavCancelGuiaMultDao();
		
		// if session expired ask him to login once again
		if (session == null || session.isNew()) { 
			return mapping.findForward("nosession"); 
		}
		
		if(guiaForm.getCurrentTask().length() < 1) { 
			guiaForm.setCurrentTask("inicial");	
			guiaForm.setFiltroPor("1");
			guiaForm.setDaysRange(global.getDaysCancelGMult());
			guiaForm.setInfoDate("Las guias mostradas corresponden del dia " + dao.fechaInicDayRange(global.getDaysCancelGMult(), request) + " al " + dao.fechaInicDayRange("0", request));
			guiaForm.setFechaInicial(dao.fechaInicDayRange(guiaForm.getDaysRange(), request));
			guiaForm.setFechaFinal(dao.fechaInicDayRange("0", request));
		}		
		
		if (guiaForm.getCurrentTask().equalsIgnoreCase("start") || guiaForm.getCurrentTask().equalsIgnoreCase("inicial") ) {
			//SE DETERMINA LA BUSQUEDA DE LAS GUIAS DISPONIBLES A CANCELAR
	
			if (guiaForm.getFiltroPor().equals("1")) {
				guiaForm = dao.getGuiasByClient((String) session.getAttribute("sClientId"), global.getAssignedBranch(), guiaForm.getFechaInicial(), guiaForm.getFechaFinal());
			}else if (guiaForm.getFiltroPor().equals("2")) {
				guiaForm = dao.getGuiasByClientWE((String) session.getAttribute("sClientId"), global, guiaForm.getFechaInicial(), guiaForm.getFechaFinal());
			}
			
			session.setAttribute("listGuias", guiaForm.getListGuias());
			request.setAttribute("javCancelGuiaMultForm", guiaForm);
			request.setAttribute("cadena", "xxx");
			request.setAttribute("linkGenCartaPorte", linkGenCartaPorte);
		
		} else if(guiaForm.getCurrentTask().equalsIgnoreCase("cancelMult")){
			//SE DETERMINA LA VALIDACION Y CREACION DE LAS GUIAS SELECCIONADAS A CANCELAR
			int countSucess = 0, countError = 0; 
			String fileName = "", fileName2 = "", ext = ".xls";
			long syDate;
			String[] guias = request.getParameterValues("checkGuia");
			
			guias = guias != null ? guias : new String[0];//Se extrae las guias seleccionadas en un array
			
			ArrayList<JavCancelGuiaMultDTO> listRastreos = new ArrayList<JavCancelGuiaMultDTO>();
 			
			if(guias.length > 0) {
				//SE INICIALIZA CICLO PARA PODER VALIDAR LAS GUIAS 
				for (String guia : guias) {
					JavCancelGuiaMultDTO rastreo = new JavCancelGuiaMultDTO();
					//Se valida el rastreo y manda el mensaje correcto al momento del llenado del formulario 
					String[] infoGuia = guia.split("//");//Divide el numero de rastreo por 
					rastreo = dao.invokeFormNumValidityCheck(infoGuia[0], infoGuia[3], infoGuia[4].substring(0, 2) , request);
					//Extrae numero de paquetes en rastreo
					rastreo.setNumPack(infoGuia[1]);
					rastreo.setRefers(infoGuia[2]);
					if (rastreo.getStatusRastreo().equals("INFO")) {
						listRastreos.add(rastreo);
						countSucess ++;
					}else {
						//Se llena el array con la informacion de la guia
						rastreo.setGuiaNumber(infoGuia[0]); 
						rastreo.setOrigBranch(infoGuia[3]); 
						rastreo.setFormNumber(infoGuia[4]);
						rastreo.setIsseDate(infoGuia[5]);
						rastreo.setDestBranch(infoGuia[6]);
						rastreo.setDestClientName(infoGuia[7]);
						String precio = infoGuia[8].replace("'",""); 
						precio = precio.replace("$", ""); 
						if(!precio.isEmpty()) {
							precio=precio.trim(); 
						}
						rastreo.setGuiaAmount(precio);
						listRastreos.add(rastreo);
						countError++; 
					}
				}
				
				for (JavCancelGuiaMultDTO guia : listRastreos) {				
					//SE REALIZA LA CANCELACION DE LOS RASTREOS
					if (guia.getStatusRastreo().equals("INFO")) {
						dao.invokeGuiaCancellation(guia, request);
					}
				}
				
				//INICIALIZA EL PROCESO PARA LA CREACION DEL ARCHIVO QUE IMPRIMIRA EL RESUMEN DE GUIAS EXITOSO 	
				SheetRecovery sr = new SheetRecovery();
				String pathFileXl = sr.getXlsMainServerPath(null);
				fileName2 = "GuiasCanceladas"; 
				try {					
					java.sql.Timestamp sysDate = getTimeStamp();
					syDate = sysDate.getTime(); 
					guiaForm.setPathFile( pathFileXl + File.separator + global.getClientId() + File.separator + global.getOrigenUserClave() + File.separator);
					fileName = global.getClientId() + "-" + global.getOrigenUserClave() + "-" + syDate + "-" + fileName2 + ext;
					guiaForm.setFileName(fileName);
					CrearExcel(global, listRastreos, fileName);
				} catch (Exception e) {
					AccessLog.Log("Data Processing Error in Extract Method" + e.getMessage());
				}
				//SE INICIALIZA LA CANCELACION DE GUIAS CORRESPONDIENTE 
				guiaForm.setCountGuiaError(String.valueOf(countError));
				guiaForm.setCountGuiaSucess(String.valueOf(countSucess));
				guiaForm.setCurrentTask("download");
			}else {
				//ingreesa mensaje de error para poder seleccionar guias
				request.setAttribute("errormsgprint", "No se seleccionaron guias a cancelar"); 
				return mapping.findForward(returnPage);
			}
		} else if(guiaForm.getCurrentTask().equalsIgnoreCase("download")){	
			 response.setContentType("*/*");
			 response.setHeader("Content-disposition","attachment;filename="+guiaForm.getFileName());			   
			   BufferedInputStream is=null;
			   OutputStream os=null;
			   FileInputStream fis=null;
			   try{
				fis = new FileInputStream(guiaForm.getPathFile() + guiaForm.getFileName());
				is  =	new BufferedInputStream(fis);
				os  =	response.getOutputStream();			   
				int z = 0;
				while(is.available() >0){
					byte b[]=new byte[1024];
					if(is.available()>1024)
						b=new byte[1024];
					else
						b= new byte[is.available()];
					int y=is.read(b);
					os.write(b);
					z=z+y;			
					os.flush();
				}
				}catch(Exception e){
					System.out.println("INSIDE CATCH ");
				}finally{
					try {
						if(fis != null) {
							fis.close();
							fis = null; 
						}
					} catch (Exception e) {
						System.out.println("DONWLOADXLSFILEGUIA CATCH 2");
					}
					try {
						if(is != null) {
							is.close();
							is = null; 
						}
					} catch (Exception e) {
						System.out.println("DONWLOADXLSFILEGUIA CATCH 3");
					}
					try {
						if(os != null) {
							os.flush();
							os.close();
							os = null; 
						}
					} catch (Exception e) {
						System.out.println("DONWLOADXLSFILEGUIA CATCH 4");
					}
				}
			guiaForm.setPathFile("");
			guiaForm.setFileName("");
			guiaForm.setCurrentTask("");
		}
		
		return mapping.findForward(returnPage);
	}

	public java.sql.Timestamp getTimeStamp() throws Exception {
		Connection con = ConnectDB.getConnection();
		PreparedStatement pst = con.prepareStatement("select sysdate from dual");// extrae la fecha por completo 
		ResultSet rs = pst.executeQuery();
		java.sql.Timestamp sysTimeStamp = null;

		if (rs.next())
			sysTime = rs.getTime(1);
		sysTimeStamp = rs.getTimestamp(1);
		
		con.close();
		if (rs != null)
			rs.close();
		if (pst != null)
			pst.close();
		return sysTimeStamp;
	}

	
	//creacion del documento en excel que se necesitara descargar 

	public HashMap<String, Object> getDataSheet(File fWorkbook, boolean isXlsxFile) {
		// codigo para leer cualquier formato de dato de la celda y lo pasa a String
		HashMap<String, Object> result = new HashMap<String, Object>();
		ArrayList<Object> arrayRows = new ArrayList<Object>();
		ArrayList<String> cols = new ArrayList<String>();
		Workbook wb = null;
		try {
			wb = WorkbookFactory.create(fWorkbook);
			DataFormatter objDefaultFormat = new DataFormatter();
			
			FormulaEvaluator objFormulaEvaluator = null;
			if (isXlsxFile) {
				objFormulaEvaluator = new XSSFFormulaEvaluator( (XSSFWorkbook) wb);
			} else {
				objFormulaEvaluator = new HSSFFormulaEvaluator( (HSSFWorkbook) wb);
			}		

			Sheet sheet = wb.getSheetAt(0);
			Iterator<Row> objIterator = sheet.rowIterator();
			
			boolean todasVacias = true;
			Row row = null;
			Iterator<Cell> cellIterator = null;
			Cell cellValue = null;
			String cellValueStr = null;
			while (objIterator.hasNext()) {
				todasVacias = true;
				row = objIterator.next();
				cellIterator = row.cellIterator();
				
				while (cellIterator.hasNext()) {
					cellValue = cellIterator.next();
					if (cellValue.getCellType()!= Cell.CELL_TYPE_BLANK && todasVacias) {
						todasVacias = false;
					}
					objFormulaEvaluator.evaluate(cellValue); 
					cellValueStr = objDefaultFormat.formatCellValue(cellValue, objFormulaEvaluator);
					cols.add(cellValueStr);
				}
				if (todasVacias) {
					break;
				}
				arrayRows.add(cols.clone());
				cols.clear();
			}
			result.put("cveMsje", "WB0000");
			result.put("desMsje", "PROCESO EXITOSO");
			result.put("matriz", arrayRows);
		} catch (EncryptedDocumentException e) {
			result.put("cveMsje", "WB0001");
			result.put("desMsje", "Archivo encriptado. Error Log: "+e);
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getDataSheet()_Error Archivo encriptado:").append(e).toString());			
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			result.put("cveMsje", "WB0002");
			result.put("desMsje", "Formato invalido. Error Log: "+e);
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getDataSheet()_Error Formato invalido:").append(e).toString());			
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			result.put("cveMsje", "WB0003");
			result.put("desMsje", "Archivo no encontrado. Error Log: "+e);
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getDataSheet()_Error Archivo no encontrado:").append(e).toString());
			e.printStackTrace();
		} catch (IOException e) {
			result.put("cveMsje", "WB0004");
			result.put("desMsje", "Error de entrada/salida en archivo. Error Log: "+e);
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getDataSheet()_Error de entrada/salida en archivo:").append(e).toString());
			e.printStackTrace();		
		} catch (Exception e) {
			result.put("cveMsje", "WB0005");
			result.put("desMsje", "Error general. Error Log: "+e);
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getDataSheet()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				if (wb!=null) {
					wb.close();
				}
			} catch (Exception e2) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getDataSheet()_Error:").append(e2).toString());
				e2.printStackTrace();		
			}
		}
		return result;
	}
	
	
	public void CrearExcel(Global global, ArrayList<JavCancelGuiaMultDTO> arrayRows, String fileName) {
		SheetRecovery sr = new SheetRecovery();
		String pathFileXl = sr.getXlsMainServerPath(null);
		String pathFile = pathFileXl + File.separator + global.getClientId() + File.separator + global.getOrigenUserClave() + File.separator;

			short Configuracion = 250; 
	        //Crear libro de trabajo en blanco
	        Workbook workbook = new HSSFWorkbook();
	        //Ingresa el nombre de la hoja nueva
	        Sheet sheet = workbook.createSheet("Guias Canceladas");
	        
	        sheet.setDefaultColumnWidth(28); //Define el tamaño de las columnas 
	        Configuracion = 350;  
	        sheet.setDefaultRowHeight(Configuracion);
	        Cell cell = null;
	        
	        //SE CREAN LOS TITULOS DE LA HOJA PRINCIPAL. 
	        CellStyle styleTitulo = workbook.createCellStyle();
	        Font fontTitle = workbook.createFont();
	        fontTitle.setBold(true);
	        fontTitle.setColor(IndexedColors.WHITE.getIndex());
	        Configuracion = 12;  //tamaño de letra 
	        fontTitle.setFontHeightInPoints(Configuracion);
	        //Se administra el estilo del titulo del documento. 
	        styleTitulo.setFont(fontTitle);
	        styleTitulo.setFillForegroundColor(IndexedColors.BLUE_GREY.getIndex());
	        Configuracion = 2;  //configuracion para fondo 
	        styleTitulo.setFillPattern(Configuracion);
	        int numeroCelda = 0;
	        int numeroRenglon = 0;
	        //SE INGRESA LOS TITULOS 
	        Row row = sheet.createRow(numeroRenglon++);
	        
	        cell = row.createCell(numeroCelda++);
	    	cell.setCellValue("NO. GUIA");
	    	cell.setCellStyle(styleTitulo);

	    	cell = row.createCell(numeroCelda++);
		    cell.setCellValue("NO. RASTREO");
		    cell.setCellStyle(styleTitulo);

		    cell = row.createCell(numeroCelda++);
			cell.setCellValue("REFERENCIAS");
			cell.setCellStyle(styleTitulo);
		    
		    cell = row.createCell(numeroCelda++);
			cell.setCellValue("FECHA DE CREACION");
			cell.setCellStyle(styleTitulo);
	        
			cell = row.createCell(numeroCelda++);
	    	cell.setCellValue("SUCURSAL DESTINO");
	    	cell.setCellStyle(styleTitulo);
	    	
	    	cell = row.createCell(numeroCelda++);
	    	cell.setCellValue("CLIENTE DESTINO");
	    	cell.setCellStyle(styleTitulo);

	    	cell = row.createCell(numeroCelda++);
		    cell.setCellValue("IMPORTE");
		    cell.setCellStyle(styleTitulo);

		    cell = row.createCell(numeroCelda++);
			cell.setCellValue("NO. PAQUETES");
			cell.setCellStyle(styleTitulo);
	        
			cell = row.createCell(numeroCelda++);
	    	cell.setCellValue("STATUS");
	    	cell.setCellStyle(styleTitulo);

	    	cell = row.createCell(numeroCelda++);
		    cell.setCellValue("MENSAJE DE STATUS");
		    cell.setCellStyle(styleTitulo);

	        //FORMATO PARA EL REGISTRO DE CADA GUIA CANCELADA
			//Se establece un estilo para determinar si 
			CellStyle style = workbook.createCellStyle();
	   		Font font = workbook.createFont();
	   		
	   		Configuracion = 1;	//Define el grosor del borde de la celda
	   		style.setBorderTop(Configuracion);
	   		style.setBorderBottom(Configuracion);
	   		style.setBorderLeft(Configuracion);
	   		style.setBorderRight(Configuracion);
	   		style.setFont(font); // se agrega al estilo el formato de las letrasa
	       
	   		
	   		//itegra el cuerpo del excel. 
	   		for (JavCancelGuiaMultDTO guia : arrayRows) {
	   		  row = sheet.createRow(numeroRenglon++);
	   		  numeroCelda = 0; 
				cell = row.createCell(numeroCelda++);
		    	cell.setCellValue(guia.getOrigBranch() + guia.getFormNumber());
		    	cell.setCellStyle(style);
		    	
		    	cell = row.createCell(numeroCelda++);
		    	cell.setCellValue(guia.getGuiaNumber());
		    	cell.setCellStyle(style);
		    	
		    	cell = row.createCell(numeroCelda++);
		    	cell.setCellValue(guia.getRefers());
		    	cell.setCellStyle(style);
		    	
		    	cell = row.createCell(numeroCelda++);
		    	cell.setCellValue(guia.getIsseDate());
		    	cell.setCellStyle(style);
		    	
		    	cell = row.createCell(numeroCelda++);
		    	cell.setCellValue(guia.getDestBranch());
		    	cell.setCellStyle(style);
		    	
		    	cell = row.createCell(numeroCelda++);
		    	cell.setCellValue(guia.getDestClientName());
		    	cell.setCellStyle(style);
		    	
		    	cell = row.createCell(numeroCelda++);
		    	cell.setCellValue(guia.getGuiaAmount());
		    	cell.setCellStyle(style);
		    	
		    	cell = row.createCell(numeroCelda++);
		    	cell.setCellValue(guia.getNumPack());
		    	cell.setCellStyle(style);
		    	
		    	cell = row.createCell(numeroCelda++);
		    	cell.setCellValue(guia.getStatusRastreo());
		    	cell.setCellStyle(style);
		    	
		    	cell = row.createCell(numeroCelda++);
		    	cell.setCellValue(guia.getMsjStatus());
		    	cell.setCellStyle(style);
			}

	        try {
	          
	        	Files.createDirectories(Paths.get(pathFile));//se crea el directorio en caso de no encontrar la ruta 
	        	  //Se genera el documento
	            FileOutputStream out = new FileOutputStream(new File(pathFile + fileName)); 
	            workbook.write(out);
	            out.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	
	
	
	
	
	
}
