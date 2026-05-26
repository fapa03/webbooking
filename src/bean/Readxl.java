package bean;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import logger.AccessLog;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFHyperlink;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCreationHelper;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFHyperlink;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import beanUtil.ConnectDB;

import com.paquetexpress.www.webbooking.Documentacion.ClienteDestino;
import com.paquetexpress.www.webbooking.Documentacion.DatoAdicional;
import com.paquetexpress.www.webbooking.Documentacion.DatosAdicionales;
import com.paquetexpress.www.webbooking.Documentacion.DetalleEmbarque;
import com.paquetexpress.www.webbooking.Documentacion.DocumentacionProxy;
import com.paquetexpress.www.webbooking.Documentacion.DomicilioDestino;
import com.paquetexpress.www.webbooking.Documentacion.Embarque;
import com.paquetexpress.www.webbooking.Documentacion.FormaPago;
import com.paquetexpress.www.webbooking.Documentacion.Header;
import com.paquetexpress.www.webbooking.Documentacion.Servicios;
import com.paquetexpress.www.webbooking.Documentacion.SolicitudEnvio;
import com.paquetexpress.www.webbooking.Documentacion.TypeEvent;
import com.paquetexpress.www.webbooking.Documentacion.retorno.DataResponse;
import com.paquetexpress.www.webbooking.GuiasRetorno.retorno.Mensaje;
import com.paquetexpress.www.webbooking.GuiasRetorno.retorno.RastreoRetorno;
import com.paquetexpress.www.webbooking.GuiasRetorno.retorno.RetornoSolicitud;

import beanForm.JavFileUploadForm2;

public class Readxl {
	private StringBuffer cnct = new StringBuffer();
	private final String msgErr = new StringBuffer("MSERR_B_").append(this.getClass().getName()).append(".").toString();
	private Resources resources = new Resources();
	//@SuppressWarnings("unchecked")
	
	public HashMap<String, Object> getDataSheet(File fWorkbook, boolean isXlsxFile) {
		// codigo para leer cualquier formato de dato de la celda y lo pasa a
		// String
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
				/*condicion para romper el ciclo cuando la primer celda del renglon actual, se encuentre vacia*/
				/*if (row.getCell(0).getCellType()== Cell.CELL_TYPE_BLANK){
					break;
				}*/
				
				cellIterator = row.cellIterator();
				
				while (cellIterator.hasNext()) {
					cellValue = cellIterator.next();
					if (cellValue.getCellType()!= Cell.CELL_TYPE_BLANK && todasVacias) {
						todasVacias = false;
					}
					objFormulaEvaluator.evaluate(cellValue); // This will evaluate the cell, And any type of cell will return string value
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
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ArrayList cotizarGenerar(Connection con, ArrayList<Object> arrayRowsDTO, String fName, Global global, String typeEvent, HttpServletRequest req, String flow, JavFileUploadForm2 fileUpForm) {
		String linkPDFIni = "";
		String linkPDF = "";
		String linkPDFXT = "";
		String endPoint = "";
		String guia = "";
		boolean procExitoso = false;
		int cveMsje = 0;
		String desMsje= "";
		String tipMsje= "";
		SheetRecovery sr = new SheetRecovery();
		try {
			
			endPoint = ConnectDB.getEndPointDocExt();
			linkPDFIni = ConnectDB.getCartaPorteExt();
			if (req.getRemoteAddr().trim().substring(0,3).equals("0:0") || req.getRemoteAddr().trim().substring(0,8).equals("192.168.")) {
				endPoint =ConnectDB.getEndPointDocInt();
				linkPDFIni = ConnectDB.getCartaPorteInt();
			}
			
			DocumentacionProxy proxy = new DocumentacionProxy(endPoint);
			HashMap<String, String> msjeVal = null;
			/*empieza de 1 para no tomar cabecera obtenida de hoja de excel*/
			HashMap<String, String> resultReload = new HashMap<String, String>();
			String rastreoReload = "";
			String rastreoReloadXT = ""; 
			String guiasRetornoReload = "";
			int guiasRetornoReloadInt = 0;
			String cadenaImpresionReload = "";
			for(int i=0;i<arrayRowsDTO.size();i++) {
				if (flow.equals("reload")) {
					resultReload = sr.getFilesPInfo(con, global, fName, i);
					if (resultReload.isEmpty()) {
						
					} else {
						rastreoReload = resultReload.get("guiaNo");
						guiasRetornoReload = resultReload.get("guiasRetorno");
						rastreoReloadXT = resultReload.get("guiaNoXT");
						if (rastreoReload.trim().length()>0) {
							linkPDF = linkPDFIni +"GenCartaPorte?trackingNoGen="+rastreoReload;
							
							fileUpForm.setCountGuiaInfo(fileUpForm.getCountGuiaInfo() + 1);
							//Se hace condicional para poder generar el link del acuse XT
							if(rastreoReloadXT.trim().length()>0) {
								linkPDFXT = linkPDFIni +"GenCartaPorte?trackingNoGen="+rastreoReloadXT; 
							}else {linkPDFXT ="";}
							((ReadxlDTO)arrayRowsDTO.get(i)).setRastreo(rastreoReload);
							((ReadxlDTO)arrayRowsDTO.get(i)).setCantidadRastreos(guiasRetornoReload);
							((ReadxlDTO)arrayRowsDTO.get(i)).setLinkPDF(linkPDF); //link pdf
							((ReadxlDTO)arrayRowsDTO.get(i)).setLinkPDFXT(linkPDFXT); //link pdf de Acuse XT
							cadenaImpresionReload = getCadenaImpresion(con, global, rastreoReload); 
							((ReadxlDTO)arrayRowsDTO.get(i)).setCadeImpresion(cadenaImpresionReload);//cadena impresion // se almacena en arreglo pero no existe en matriz de hoja de excel
							
							/*se verifica si trae valor mayor a 0 para ir a obtener las guias de retorno*/
							if (guiasRetornoReload.trim().length()>0) {		 
								try { guiasRetornoReloadInt = Integer.parseInt(guiasRetornoReload); } catch (Exception e) { guiasRetornoReloadInt = 0; }
								
								if (guiasRetornoReloadInt>0) {
									
									com.paquetexpress.www.webbooking.GuiasRetorno.retorno.DataResponse 
									dataResponseGuiasRetReload = getSavedGuiasReturn(con, (ReadxlDTO)arrayRowsDTO.get(i), guiasRetornoReloadInt, fName, global);
									
									((ReadxlDTO)arrayRowsDTO.get(i)).setDataResponseGuiasRetorno(dataResponseGuiasRetReload);
									
								}
							}
							//Rompe iteraccion porque ya existe rastreo.
							continue;
						}
					}
				}
				
				msjeVal = validObject(((ReadxlDTO)arrayRowsDTO.get(i)));
				if (msjeVal.isEmpty()) {
					com.paquetexpress.www.webbooking.Documentacion.Data parameters = new com.paquetexpress.www.webbooking.Documentacion.Data();
					
					parameters.setHeader(new Header());
					parameters.getHeader().setOrgnClntId(global.getOrigenUserClave());
					parameters.getHeader().setClntPswd(global.getPassword());
					parameters.getHeader().setAgreementKey(global.getAgreementKey());
				
					//parameters.getHeader().setTypeEvent(typeEvent)
					
					//parameters.getHeader().getTypeEvent();
					if (typeEvent.equals("Generar")) {
						parameters.getHeader().setTypeEvent( TypeEvent.Generar );
					} else {
						parameters.getHeader().setTypeEvent( TypeEvent.Calcular );
					}
					
					parameters.setSolicitudEnvio(new SolicitudEnvio());
					parameters.getSolicitudEnvio().setClienteDestino(new ClienteDestino());
					parameters.getSolicitudEnvio().getClienteDestino().setDestCustClntId(((ReadxlDTO)arrayRowsDTO.get(i)).getIdClienteDest());//id de cliente destino
					parameters.getSolicitudEnvio().getClienteDestino().setDestClntName(((ReadxlDTO)arrayRowsDTO.get(i)).getNombre());//nombre cliente destino
					
					DatoAdicional daRFC = new DatoAdicional("rfcDest", ((ReadxlDTO)arrayRowsDTO.get(i)).getRfc(), "", "", "", "");//RFC
					DatoAdicional daFileName = new DatoAdicional("fileName", fName, "", "", "", "");//nombre de archivo
					DatoAdicional daGuiasRetorno = new DatoAdicional("guiasRetorno", ((ReadxlDTO)arrayRowsDTO.get(i)).getCantidadRastreos(), "", "", "", "");//Guias de retorno
					DatoAdicional fromXLS = new DatoAdicional("fromXLS", "Y", "", "", "", "");// bandera de xls
					DatoAdicional orgClntId = new DatoAdicional("orgClntId", global.getClientId(), "", "", "", "");//orgClntId
					DatoAdicional orgSite = new DatoAdicional("orgSite", global.getAssignedSite(), "", "", "", "");//orgSite
					DatoAdicional orgBrnc = new DatoAdicional("orgBrnc", global.getAssignedBranch(), "", "", "", "");//orgBrnc
					DatoAdicional ccosto = new DatoAdicional("centroCosto", ((ReadxlDTO)arrayRowsDTO.get(i)).getCentroCosto().trim(), "", "", "", "");//centroCosto					
					DatoAdicional seg = new DatoAdicional("segId", ((ReadxlDTO)arrayRowsDTO.get(i)).getSegId().trim(), "N", "N", "N", "");//segId//AAP25
					
					parameters.getSolicitudEnvio().getClienteDestino().setDomicilioDestino(new DomicilioDestino(
							((ReadxlDTO)arrayRowsDTO.get(i)).getPais(),//pais
							((ReadxlDTO)arrayRowsDTO.get(i)).getEstado(),//estado
							((ReadxlDTO)arrayRowsDTO.get(i)).getCiudad(),//ciudad
							((ReadxlDTO)arrayRowsDTO.get(i)).getCalle(),//calle
							((ReadxlDTO)arrayRowsDTO.get(i)).getNumero(),//Numero
							((ReadxlDTO)arrayRowsDTO.get(i)).getColonia(),//Colonia
							((ReadxlDTO)arrayRowsDTO.get(i)).getCp(),//Codigo postal
							((ReadxlDTO)arrayRowsDTO.get(i)).geteMail(),//Email, 
							((ReadxlDTO)arrayRowsDTO.get(i)).getTelefono(),//Telefono 
							((ReadxlDTO)arrayRowsDTO.get(i)).getReferenciaDomicilio())//Referencia domicilio
					);
					
					String productID = ((ReadxlDTO)arrayRowsDTO.get(i)).getProductId().trim();
					if (!productID.isEmpty()) {
						productID = "|"+productID;
					}
					
					Embarque embarque1 = new Embarque(
							Integer.parseInt( ((ReadxlDTO)arrayRowsDTO.get(i)).getCantidad().trim() ),//cantidad
							((ReadxlDTO)arrayRowsDTO.get(i)).getCodigoBulto() + productID, //codigo bulto + productID SAT, si viene alimentado
							((ReadxlDTO)arrayRowsDTO.get(i)).getContenido(), //contenido
							((ReadxlDTO)arrayRowsDTO.get(i)).getTipoTarifa(), //tipo de tarifa (T1, T2, T3... T7)
							Double.parseDouble(((ReadxlDTO)arrayRowsDTO.get(i)).getPeso().trim()), //peso
							Double.parseDouble(((ReadxlDTO)arrayRowsDTO.get(i)).getVolumen().trim()), //vol
							Double.parseDouble(((ReadxlDTO)arrayRowsDTO.get(i)).getLargo().trim()), //largo 
							Double.parseDouble(((ReadxlDTO)arrayRowsDTO.get(i)).getAncho().trim()), //ancho
							Double.parseDouble(((ReadxlDTO)arrayRowsDTO.get(i)).getAlto().trim()) ); // alto

					Embarque[] embarque = {embarque1};
					
					parameters.getSolicitudEnvio().setDetalleEmbarque( new DetalleEmbarque() );
					
					parameters.getSolicitudEnvio().getDetalleEmbarque().setEmbarque(embarque);
					
					parameters.getSolicitudEnvio().setServicios(new Servicios());
					parameters.getSolicitudEnvio().getServicios().setDlvyType(((ReadxlDTO)arrayRowsDTO.get(i)).getTipoEntrega());
					if( ((ReadxlDTO)arrayRowsDTO.get(i)).getFormaPago().equals("TO_PAY")) {
						parameters.getSolicitudEnvio().getServicios().setFormaPago(FormaPago.TO_PAY);
					}else {
						parameters.getSolicitudEnvio().getServicios().setFormaPago(FormaPago.PAID);
					}
					parameters.getSolicitudEnvio().getServicios().setAckType(((ReadxlDTO)arrayRowsDTO.get(i)).getAcuse());
					if (!((ReadxlDTO)arrayRowsDTO.get(i)).getCod().trim().equals("0") ) {
						parameters.getSolicitudEnvio().getServicios().setCodAmount(Double.parseDouble(((ReadxlDTO)arrayRowsDTO.get(i)).getCod().trim()));
					}
					if (!((ReadxlDTO)arrayRowsDTO.get(i)).getValorDeclarado().trim().equals("0") ) {
						parameters.getSolicitudEnvio().getServicios().setTotlDeclVlue(Double.parseDouble(((ReadxlDTO)arrayRowsDTO.get(i)).getValorDeclarado().toString().trim()));
					}
					parameters.getSolicitudEnvio().getServicios().setInvType(((ReadxlDTO)arrayRowsDTO.get(i)).getCobertura());
					parameters.getSolicitudEnvio().getServicios().setComments(((ReadxlDTO)arrayRowsDTO.get(i)).getComentarios());
					parameters.getSolicitudEnvio().getServicios().setReference(((ReadxlDTO)arrayRowsDTO.get(i)).getReferencia());
					
					parameters.getSolicitudEnvio().setCustAgnt(((ReadxlDTO)arrayRowsDTO.get(i)).getAgenteAduanal());//agente aduanal
					parameters.getSolicitudEnvio().setGhPediNumb(((ReadxlDTO)arrayRowsDTO.get(i)).getPedimento());//pedimento
					
					
					DatoAdicional[] datoAdicionalArr = new DatoAdicional[9];

					datoAdicionalArr[0] = daRFC;
					datoAdicionalArr[1] = daFileName;
					datoAdicionalArr[2] = daGuiasRetorno;
					datoAdicionalArr[3] = fromXLS;
					datoAdicionalArr[4] = orgClntId;
					datoAdicionalArr[5] = orgSite;
					datoAdicionalArr[6] = orgBrnc;
					datoAdicionalArr[7] = ccosto;
					
					if (seg != null ) {//AAP25
						if (seg.getValorDataAd()!= null) {//AAP25
							if (seg.getValorDataAd().trim().length()>0) {//AAP25
								datoAdicionalArr[8] = seg;//AAP25
							}//AAP25
						}//AAP25
					}//AAP25
					
					parameters.getSolicitudEnvio().setDatosAdicionales(new DatosAdicionales(datoAdicionalArr));
					
					DataResponse dataResponse = proxy.documentarGuia(parameters);
					
					//dataResponse.getRetornoSolicitud().getMensajes().get
					if (dataResponse ==  null || 
							dataResponse.getRetornoSolicitud() == null || 
							dataResponse.getRetornoSolicitud().getMensajes() == null || 
							dataResponse.getRetornoSolicitud().getMensajes().getMensaje() == null) {
						((ReadxlDTO)arrayRowsDTO.get(i)).setCveMensaje("WB9999");
						((ReadxlDTO)arrayRowsDTO.get(i)).setTipoMensaje("ERRO");
						((ReadxlDTO)arrayRowsDTO.get(i)).setDesMensaje("ERROR GENERAL, VERIFICAR DATOS CON SERVICIOS TI");
						
						((ReadxlDTO)arrayRowsDTO.get(i)).setRastreo("");//rastreo
						((ReadxlDTO)arrayRowsDTO.get(i)).setGuia("");//guia
						((ReadxlDTO)arrayRowsDTO.get(i)).setImporte("");//importe
						((ReadxlDTO)arrayRowsDTO.get(i)).setLinkPDF("");//link pdf
						((ReadxlDTO)arrayRowsDTO.get(i)).setLinkPDFXT(""); //Link PDF de AcuseXT 
						((ReadxlDTO)arrayRowsDTO.get(i)).setCadeImpresion(""); //cadena impresion // se almacena en arreglo pero no existe en matriz de hoja de excel
						
						//aumenta contador para poder determinar las guias erroneas 
						fileUpForm.setCountGuiaError(fileUpForm.getCountGuiaError() + 1);
						
					} else {
						procExitoso = false;						
						for (int jj=0;jj<dataResponse.getRetornoSolicitud().getMensajes().getMensaje().length;jj++) {
							
							if (!procExitoso && 
									(dataResponse.getRetornoSolicitud().getMensajes().getMensaje(jj).getCveMsjeRetorno()==0 &&
									dataResponse.getRetornoSolicitud().getMensajes().getMensaje(jj).getTipoMsje().equals("INFO")
									)) {
								procExitoso = true;
								cveMsje = dataResponse.getRetornoSolicitud().getMensajes().getMensaje(jj).getCveMsjeRetorno();
								tipMsje = dataResponse.getRetornoSolicitud().getMensajes().getMensaje(jj).getTipoMsje();
								desMsje = dataResponse.getRetornoSolicitud().getMensajes().getMensaje(jj).getDesMsjeRetorno();
								//aumenta contador para poder determinar las guias validas 
								fileUpForm.setCountGuiaInfo(fileUpForm.getCountGuiaInfo() + 1);
							}
						}					
						
						if (procExitoso) {	
							((ReadxlDTO)arrayRowsDTO.get(i)).setCveMensaje(String.valueOf(cveMsje));
							((ReadxlDTO)arrayRowsDTO.get(i)).setTipoMensaje(tipMsje);
							((ReadxlDTO)arrayRowsDTO.get(i)).setDesMensaje(desMsje);
							
							
							if (dataResponse.getRetornoSolicitud().getGuiaNo().trim().length()>0) {
								linkPDF = linkPDFIni +"GenCartaPorte?trackingNoGen="+ dataResponse.getRetornoSolicitud().getGuiaNo();
								guia = dataResponse.getRetornoSolicitud().getFormNo();
							}
							
							if (dataResponse.getRetornoSolicitud().getGuiaNoTemp().trim().length()>0) {
								linkPDFXT = linkPDFIni + "GenCartaPorte?trackingNoGen="+ dataResponse.getRetornoSolicitud().getGuiaNoTemp();
							}else { linkPDFXT="";}
							
							((ReadxlDTO)arrayRowsDTO.get(i)).setRastreo(dataResponse.getRetornoSolicitud().getGuiaNo());//rastreo
							((ReadxlDTO)arrayRowsDTO.get(i)).setGuia(guia);//guia
							
							double importe = 0;
							try {
								importe = dataResponse.getRetornoSolicitud().getServicios().getImporte().getTotalAmnt();
							} catch (Exception e) {
								importe = 0;
							}
							((ReadxlDTO)arrayRowsDTO.get(i)).setImporte(String.valueOf(importe)); //importe
							((ReadxlDTO)arrayRowsDTO.get(i)).setLinkPDF(linkPDF); //link pdf
							((ReadxlDTO)arrayRowsDTO.get(i)).setLinkPDFXT(linkPDFXT); //link pdfAcuseTX
							((ReadxlDTO)arrayRowsDTO.get(i)).setCadeImpresion(dataResponse.getRetornoSolicitud().getCadenaImpresion()); //cadena impresion // se almacena en arreglo pero no existe en matriz de hoja de excel
							
							/* verifica si hay cadena de impresion*/
							if (((ReadxlDTO)arrayRowsDTO.get(i)).getCadeImpresion()!= null && 
									((ReadxlDTO)arrayRowsDTO.get(i)).getCadeImpresion().trim().length()>0) {
								/*Identifica que getCantidadRastreos() contenga valor para generacion de sobres de retorno*/
								if (((ReadxlDTO)arrayRowsDTO.get(i)).getCantidadRastreos().trim().length()> 0 &&
										!((ReadxlDTO)arrayRowsDTO.get(i)).getCantidadRastreos().equals("0")) {
									int cantidadRastreos = 0;
									GuiasRetorno guiasRetorno = new GuiasRetorno();
									
									try {cantidadRastreos = Integer.parseInt(((ReadxlDTO)arrayRowsDTO.get(i)).getCantidadRastreos()); } catch (Exception e) { cantidadRastreos = 0; }
									if (cantidadRastreos>0) {
										/* Se ejecuta servicio web de rastreos de retorno*/
										com.paquetexpress.www.webbooking.GuiasRetorno.retorno.DataResponse dataResponseGuiasRetorno = 
												guiasRetorno.getGuiaRetorno(
												((ReadxlDTO)arrayRowsDTO.get(i)).getRastreo(), 
												((ReadxlDTO)arrayRowsDTO.get(i)).getGuia(), 
												cantidadRastreos, null, null, null, global.getOrigenUserClave());
										
										/*se almacena resultado de rastreos de RETORNO*/
										((ReadxlDTO)arrayRowsDTO.get(i)).setDataResponseGuiasRetorno(dataResponseGuiasRetorno);
										
										sr.saveGuiasReturn(con, global, fName, (ReadxlDTO)arrayRowsDTO.get(i), dataResponseGuiasRetorno);
									}
								}
							}
							
						} else {
							((ReadxlDTO)arrayRowsDTO.get(i)).setCveMensaje(String.valueOf(dataResponse.getRetornoSolicitud().getMensajes().getMensaje(0).getCveMsjeRetorno()));
							((ReadxlDTO)arrayRowsDTO.get(i)).setTipoMensaje(dataResponse.getRetornoSolicitud().getMensajes().getMensaje(0).getTipoMsje());
							((ReadxlDTO)arrayRowsDTO.get(i)).setDesMensaje(dataResponse.getRetornoSolicitud().getMensajes().getMensaje(0).getDesMsjeRetorno());
							
							((ReadxlDTO)arrayRowsDTO.get(i)).setRastreo("");//rastreo
							((ReadxlDTO)arrayRowsDTO.get(i)).setGuia("");//guia
							((ReadxlDTO)arrayRowsDTO.get(i)).setImporte("");//importe
							((ReadxlDTO)arrayRowsDTO.get(i)).setLinkPDF("");//link pdf
							((ReadxlDTO)arrayRowsDTO.get(i)).setLinkPDFXT("");//link pdf acuseXT
							((ReadxlDTO)arrayRowsDTO.get(i)).setCadeImpresion(""); //cadena impresion // se almacena en arreglo pero no existe en matriz de hoja de excel
							//aumenta contador para poder determinar las guias erroneas 
							fileUpForm.setCountGuiaError(fileUpForm.getCountGuiaError() + 1);
							
						}					
					}
					
					/*reinicio de variable*/
					procExitoso = false;
				} else {
					
					((ReadxlDTO)arrayRowsDTO.get(i)).setCveMensaje(msjeVal.get("cveMsje").toString());
					((ReadxlDTO)arrayRowsDTO.get(i)).setTipoMensaje("ERRO");
					((ReadxlDTO)arrayRowsDTO.get(i)).setDesMensaje(msjeVal.get("desMsje").toString());
					
					((ReadxlDTO)arrayRowsDTO.get(i)).setRastreo("");//rastreo
					((ReadxlDTO)arrayRowsDTO.get(i)).setGuia("");//guia
					((ReadxlDTO)arrayRowsDTO.get(i)).setImporte("");//importe
					((ReadxlDTO)arrayRowsDTO.get(i)).setLinkPDF("");//link pdf
					((ReadxlDTO)arrayRowsDTO.get(i)).setLinkPDFXT("");//link pdf acuseXT
					((ReadxlDTO)arrayRowsDTO.get(i)).setCadeImpresion(""); //cadena impresion // se almacena en arreglo pero no existe en matriz de hoja de excel
					fileUpForm.setCountGuiaError(fileUpForm.getCountGuiaError() + 1);
				}
				
				/*inserta renglon por renglon el resultado de cada peticion*/
				if (flow.equalsIgnoreCase("upload")) {
					sr.setRowSheetDtl(con, global, fName, (ReadxlDTO)arrayRowsDTO.get(i), i);
				} else if (flow.equalsIgnoreCase("process") || flow.equalsIgnoreCase("reload")) {
					sr.updateRowSheetDtl(con, global, fName, (ReadxlDTO)arrayRowsDTO.get(i), i);
				}
			}
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("cotizarGenerar()_Error:").append(e).toString());
			e.printStackTrace();
		}
		return arrayRowsDTO;
	}
	
	public void updateWorkBookHSSFW(ArrayList<Object> arrayRows, File fWorkbook){
		 HSSFWorkbook workbook = null;
		 FileInputStream file = null;
		 FileOutputStream outFile = null;
		try {
		    file = new FileInputStream(fWorkbook);
		 
		    workbook = new HSSFWorkbook(file);
		    HSSFSheet sheet = workbook.getSheetAt(0);
		    Cell cell = null;
		    int posSheetRow = 1;
		    //Se establece un estilo para determinar si 
		    CellStyle style = workbook.createCellStyle();
    		Font font = workbook.createFont();
    		font.setUnderline(Font.U_SINGLE); // Subrayado simple
    		font.setColor(IndexedColors.BLUE.getIndex());
    		
    		short thinBorderStyle = 2;	
    		style.setBorderTop(thinBorderStyle);
    		style.setBorderBottom(thinBorderStyle);
    		style.setBorderLeft(thinBorderStyle);
    		style.setBorderRight(thinBorderStyle);
    		style.setFont(font); // se agrega al estilo el formato de las letrasa
    		
		    for (int i = 0;i<arrayRows.size();i++) {
		    	
		    	cell = sheet.getRow(posSheetRow).getCell(33);//clave de mensaje
		    	cell.setCellValue(((ReadxlDTO)arrayRows.get(i)).getCveMensaje());
		    	
		    	cell = sheet.getRow(posSheetRow).getCell(34);//tipo de mensaje
		    	cell.setCellValue(((ReadxlDTO)arrayRows.get(i)).getTipoMensaje());
		    	
		    	cell = sheet.getRow(posSheetRow).getCell(35);//descripcion de mensaje
		    	cell.setCellValue(((ReadxlDTO)arrayRows.get(i)).getDesMensaje());
		    	
		    	cell = sheet.getRow(posSheetRow).getCell(36);//rastreo
		    	cell.setCellValue(((ReadxlDTO)arrayRows.get(i)).getRastreo());
		    	
		    	HSSFHyperlink url_link = new HSSFHyperlink(HSSFHyperlink.LINK_URL);
		    	cell = sheet.getRow(posSheetRow).getCell(37);//guia
		    	cell.setCellValue(((ReadxlDTO)arrayRows.get(i)).getGuia());
		    	url_link.setAddress(((ReadxlDTO)arrayRows.get(i)).getLinkPDF());
		    	cell.setHyperlink(url_link);
		    	cell.setCellStyle(style);
		    	cell = sheet.getRow(posSheetRow).getCell(38);//importe
		    	cell.setCellValue(((ReadxlDTO)arrayRows.get(i)).getImporte());
		    	
		    	if (((ReadxlDTO)arrayRows.get(i)).getLinkPDFXT().trim().equals("")){
		    		HSSFHyperlink url_link_pdf = new HSSFHyperlink(HSSFHyperlink.LINK_URL); //link pdf
		    		cell = sheet.getRow(posSheetRow).getCell(39);//link pdf
		    		cell.setCellValue("PDFGUIA");
		    		url_link_pdf.setAddress(((ReadxlDTO)arrayRows.get(i)).getLinkPDF());
		    		cell.setHyperlink(url_link_pdf);
		    		cell.setCellStyle(style);
		    	}else {
		    		HSSFHyperlink url_link_acuse = new HSSFHyperlink(HSSFHyperlink.LINK_URL);
		    		cell = sheet.getRow(posSheetRow).getCell(39);//link pdf
		    		cell.setCellValue("PDFACUSEXT"); 
		    		url_link_acuse.setAddress(((ReadxlDTO)arrayRows.get(i)).getLinkPDFXT());
		    		cell.setHyperlink(url_link_acuse);
		    		cell.setCellStyle(style);
		    	}			    		

		    	posSheetRow++;
		    }
		    file.close();
		    file = null;
		    outFile =new FileOutputStream(fWorkbook);
		    workbook.write(outFile);
		    outFile.flush();
		    outFile.close();
		    outFile = null;
		    workbook.close();
		    workbook = null;
		} catch (FileNotFoundException e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("updateWorkBookHSSFW()_Error FileNotFoundException:").append(e).toString());
			e.printStackTrace();
		} catch (IOException e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("updateWorkBookHSSFW()_Error IOException:").append(e).toString());
			e.printStackTrace();
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("updateWorkBookHSSFW()_Error Exception:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				if (file!= null) {
					file.close();
				}
			} catch (Exception e2) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("updateWorkBookHSSFW()_Error1:").append(e2).toString());
				e2.printStackTrace();
			}
			try {
				if (outFile != null) {
					outFile.flush();
					outFile.close();
				}
			} catch (Exception e2) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("updateWorkBookHSSFW()_Error2:").append(e2).toString());
				e2.printStackTrace();
			}
			try {
				if (workbook!= null) {
					workbook.close();
				}
			} catch (Exception e2) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("updateWorkBookHSSFW()_Error3:").append(e2).toString());
				e2.printStackTrace();
			}
		}
	}
	
	public void updateWorkBookXSSFW(ArrayList<Object> arrayRows, File fWorkbook){
		XSSFWorkbook workbook = null;
		FileInputStream file = null;
		FileOutputStream outFile = null;
		try {
		    file = new FileInputStream(fWorkbook);
		 
		    workbook = new XSSFWorkbook(file);
		    XSSFSheet sheet = workbook.getSheetAt(0);
		    Cell cell = null;
		    int posSheetRow = 1;
		    
		    //Se genera el estilo de la letra para hacerlo en forma de hipervinculo
		    CellStyle style = workbook.createCellStyle();
    		Font font = workbook.createFont();
    		font.setUnderline(Font.U_SINGLE); // Subrayado simple
    		font.setColor(IndexedColors.BLUE.getIndex());
    		//Se establecen el formato de los bordes de la celda 
    		short thinBorderStyle = 2;	
    		style.setBorderTop(thinBorderStyle);
    		style.setBorderBottom(thinBorderStyle);
    		style.setBorderLeft(thinBorderStyle);
    		style.setBorderRight(thinBorderStyle);
		    
		    for (int i = 0;i<arrayRows.size();i++) {
		    	
		    	/* First get XSSFCreationHelper object using the workbook*/
                XSSFCreationHelper helper= workbook.getCreationHelper();
                
		    	cell = sheet.getRow(posSheetRow).getCell(33);//clave de mensaje
		    	cell.setCellValue(((ReadxlDTO)arrayRows.get(i)).getCveMensaje());
		    	
		    	cell = sheet.getRow(posSheetRow).getCell(34);//tipo de mensaje
		    	cell.setCellValue(((ReadxlDTO)arrayRows.get(i)).getTipoMensaje());
		    	
		    	cell = sheet.getRow(posSheetRow).getCell(35);//descripcion de mensaje
		    	cell.setCellValue(((ReadxlDTO)arrayRows.get(i)).getDesMensaje());
		    	
		    	cell = sheet.getRow(posSheetRow).getCell(36);//rastreo
		    	cell.setCellValue(((ReadxlDTO)arrayRows.get(i)).getRastreo());
		    	
		    	cell = sheet.getRow(posSheetRow).getCell(37);//guia
		    	/* Now use createHyperlink method to get XSSFHyperlink */
                XSSFHyperlink url_link = helper.createHyperlink(Hyperlink.LINK_URL);
		    	cell.setCellValue(((ReadxlDTO)arrayRows.get(i)).getGuia());
		    	url_link.setAddress(((ReadxlDTO)arrayRows.get(i)).getLinkPDF());
		    	url_link.setTooltip("Click para cargar PDF de carta porte");
		    	cell.setHyperlink(url_link);
		    	cell.setCellStyle(style);

		    	cell = sheet.getRow(posSheetRow).getCell(38);//importe
		    	cell.setCellValue(((ReadxlDTO)arrayRows.get(i)).getImporte());
		    	
		    	if (((ReadxlDTO)arrayRows.get(i)).getLinkPDFXT().trim().equals("")){
		    		XSSFHyperlink url_link_pdf = helper.createHyperlink(Hyperlink.LINK_URL); //link pdf
		    		cell = sheet.getRow(posSheetRow).getCell(39);//link pdf
			    	cell.setCellValue("PDF");
			    	url_link_pdf.setAddress(((ReadxlDTO)arrayRows.get(i)).getLinkPDF());
			    	url_link_pdf.setTooltip("Click para cargar PDF de carta porte");
			    	cell.setHyperlink(url_link_pdf);
			    	cell.setCellStyle(style);
		    	}else {
		    		XSSFHyperlink url_link_acuse = helper.createHyperlink(Hyperlink.LINK_URL); //link pdf
		    		cell = sheet.getRow(posSheetRow).getCell(39);//link pdf
			    	cell.setCellValue("PDFACUSEXT");
			    	url_link_acuse.setAddress(((ReadxlDTO)arrayRows.get(i)).getLinkPDFXT());
			    	url_link_acuse.setTooltip("Click para cargar PDF de carta porte");
			    	cell.setHyperlink(url_link_acuse);
			    	cell.setCellStyle(style);
		    	}			    		
	
		    	posSheetRow++;
		    }
		     
		    file.close();
		    file = null;
		    outFile =new FileOutputStream(fWorkbook);
		    workbook.write(outFile);
		    outFile.flush();
		    outFile.close();
		    outFile = null;
		    workbook.close();
		    workbook = null;
		} catch (FileNotFoundException e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("updateWorkBookXSSFW()_Error FileNotFoundException:").append(e).toString());
			e.printStackTrace();
		} catch (IOException e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("updateWorkBookXSSFW()_Error IOException:").append(e).toString());
			e.printStackTrace();
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("updateWorkBookXSSFW()_Error Exception:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				if (file!= null) {
					file.close();
				}
			} catch (Exception e2) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("updateWorkBookXSSFW()_Error1:").append(e2).toString());
				e2.printStackTrace();
			}
			try {			
				if (outFile != null) {
					outFile.flush();
					outFile.close();
				}
			} catch (Exception e2) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("updateWorkBookXSSFW()_Error2:").append(e2).toString());
				e2.printStackTrace();
			}
			try {
				if (workbook!= null) {
					workbook.close();
				}
			} catch (Exception e2) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("updateWorkBookXSSFW()_Error3:").append(e2).toString());
				e2.printStackTrace();
			}
		}
	}
	
	public HashMap<String, String> validObject(ReadxlDTO arrayRows){
		HashMap<String, String> msjes =  new HashMap<String, String>(3);
		String msje = "";
		try {
			msjes = validAtribute(arrayRows.getIdClienteDest(), true, 15, false);//id cliente destino
			if (!msjes.isEmpty()) {
				msje = msjes.get("desMsje").toString();
				msjes.put("desMsje", "ID CLIENTE DESTINO. "+msje);
				msjes.put("cveMsje", "WB0006_"+msjes.get("cveMsje").toString());
			}
			
			if (msje.isEmpty()) {
				msjes = validAtribute(arrayRows.getNombre(), true, 80, false);//Nombre cliente destino
				if (!msjes.isEmpty()) {
					msje = msjes.get("desMsje").toString();
					msjes.put("desMsje", "NOMBRE CLIENTE DESTINO. "+msje);
					msjes.put("cveMsje", "WB0007_"+msjes.get("cveMsje").toString());
				}
			}
			
			if (msje.isEmpty()) {
				msjes = validAtribute(arrayRows.getRfc(), false, 15, false);//RFC
				if (!msjes.isEmpty()) {
					msje = msjes.get("desMsje").toString();
					msjes.put("desMsje", "RFC. "+msje);
					msjes.put("cveMsje", "WB0008_"+msjes.get("cveMsje").toString());
				}
			}
			
			if (msje.isEmpty()) {
				msjes = validAtribute(arrayRows.getPais(), false, 60, false);//Pais
				if (!msjes.isEmpty()) {
					msje = msjes.get("desMsje").toString();
					msjes.put("desMsje", "PAIS. "+msje);
					msjes.put("cveMsje", "WB0008_"+msjes.get("cveMsje").toString());
				}
			}
			if (msje.isEmpty()) {
				msjes = validAtribute(arrayRows.getEstado(), false, 60, false);//Estado
				if (!msjes.isEmpty()) {
					msje = msjes.get("desMsje").toString();
					msjes.put("desMsje", "ESTADO. "+msje);
					msjes.put("cveMsje", "WB0009_"+msjes.get("cveMsje").toString());
				}
			}
			if (msje.isEmpty()) {
				msjes = validAtribute(arrayRows.getCiudad(), false, 60, false);//Ciudad
				if (!msjes.isEmpty()) {
					msje = msjes.get("desMsje").toString();
					msjes.put("desMsje", "CIUDAD. "+msje);
					msjes.put("cveMsje", "WB0010_"+msjes.get("cveMsje").toString());
				}
			}
			if (msje.isEmpty()) {
				msjes = validAtribute(arrayRows.getCalle(), true, 80, false);//calle
				if (!msjes.isEmpty()) {
					msje = msjes.get("desMsje").toString();
					msjes.put("desMsje", "CALLE. "+msje);
					msjes.put("cveMsje", "WB0011_"+msjes.get("cveMsje").toString());
				}
			}
			if (msje.isEmpty()) {
				msjes = validAtribute(arrayRows.getNumero(), true, 15, false);//Numero
				if (!msjes.isEmpty()) {
					msje = msjes.get("desMsje").toString();
					msjes.put("desMsje", "NUMERO. "+msje);
					msjes.put("cveMsje", "WB0012_"+msjes.get("cveMsje").toString());
				}
			}
			if (msje.isEmpty()) {
				msjes = validAtribute(arrayRows.getColonia(), true, 60, false);//Colonia
				if (!msjes.isEmpty()) {
					msje = msjes.get("desMsje").toString();
					msjes.put("desMsje", "COLONIA. "+msje);
					msjes.put("cveMsje", "WB0013_"+msjes.get("cveMsje").toString());
				}
			}
			if (msje.isEmpty()) {
				msjes = validAtribute(arrayRows.getCp(), true, 15, false);//Código postal
				if (!msjes.isEmpty()) {
					msje = msjes.get("desMsje").toString();
					msjes.put("desMsje", "CODIGO POSTAL. "+msje);
					msjes.put("cveMsje", "WB0014_"+msjes.get("cveMsje").toString());
				}
			}
			if (msje.isEmpty()) {
				msjes = validAtribute(arrayRows.geteMail(), false, 80, false);//Email
				if (!msjes.isEmpty()) {
					msje = msjes.get("desMsje").toString();
					msjes.put("desMsje", "CORREO ELECTRONICO. "+msje);
					msjes.put("cveMsje", "WB0015_"+msjes.get("cveMsje").toString());
				}
			}
			if (msje.isEmpty()) {
				msjes = validAtribute(arrayRows.getTelefono(), false, 20, false);//Telefono
				if (!msjes.isEmpty()) {
					msje = msjes.get("desMsje").toString();
					msjes.put("desMsje", "TELEFONO. "+msje);
					msjes.put("cveMsje", "WB0016_"+msjes.get("cveMsje").toString());
				}
			}
			if (msje.isEmpty()) {
				msjes = validAtribute(arrayRows.getReferenciaDomicilio(), false, 50, false);//Referencia domicilio
				if (!msjes.isEmpty()) {
					msje = msjes.get("desMsje").toString();
					msjes.put("desMsje", "REFERENCIA DE DOMICILIO DESTINO. "+msje);
					msjes.put("cveMsje", "WB0016_"+msjes.get("cveMsje").toString());
				}
			}
			if (msje.isEmpty()) {
				msjes = validAtribute(arrayRows.getCantidad(), true, 0, true);//Cantidad
				if (!msjes.isEmpty()) {
					msje = msjes.get("desMsje").toString();
					msjes.put("desMsje", "CANTIDAD DE BULTOS. "+msje);
					msjes.put("cveMsje", "WB0017_"+msjes.get("cveMsje").toString());
				}
			}
			if (msje.isEmpty()) {
				msjes = validAtribute(arrayRows.getCodigoBulto(), true, 15, false);//clave tipo de bulto
				if (!msjes.isEmpty()) {
					msje = msjes.get("desMsje").toString();
					msjes.put("desMsje", "CLAVE TIPO DE BULTO. "+msje);
					msjes.put("cveMsje", "WB0018_"+msjes.get("cveMsje").toString());
				}
			}
			if (msje.isEmpty()) {
				msjes = validAtribute(arrayRows.getContenido(), true, 100, false);//Contenido
				if (!msjes.isEmpty()) {
					msje = msjes.get("desMsje").toString();
					msjes.put("desMsje", "CONTENIDO. "+msje);
					msjes.put("cveMsje", "WB0020_"+msjes.get("cveMsje").toString());
				}
			}
			if (msje.isEmpty()) {
				msjes = validAtribute(arrayRows.getTipoTarifa(), false, 0, false);//tipo tarifa
				if (!msjes.isEmpty()) {
					msje = msjes.get("desMsje").toString();
					msjes.put("desMsje", "TIPO TARIFA. "+msje);
					msjes.put("cveMsje", "WB0021_"+msjes.get("cveMsje").toString());
				}
			}
			if (msje.isEmpty()) {
				msjes = validAtribute(arrayRows.getPeso(), false, 0, true);//peso
				if (!msjes.isEmpty()) {
					msje = msjes.get("desMsje").toString();
					msjes.put("desMsje", "PESO. "+msje);
					msjes.put("cveMsje", "WB0022_"+msjes.get("cveMsje").toString());
				}
			}
			if (msje.isEmpty()) {
				msjes = validAtribute(arrayRows.getVolumen(), false, 0, true);//Volumen
				if (!msjes.isEmpty()) {
					msje = msjes.get("desMsje").toString();
					msjes.put("desMsje", "VOLUMEN. "+msje);
					msjes.put("cveMsje", "WB0023_"+msjes.get("cveMsje").toString());
				}
			}
			if (msje.isEmpty()) {
				msjes = validAtribute(arrayRows.getLargo(), false, 0, true);//Largo
				if (!msjes.isEmpty()) {
					msje = msjes.get("desMsje").toString();
					msjes.put("desMsje", "LARGO. "+msje);
					msjes.put("cveMsje", "WB0024_"+msjes.get("cveMsje").toString());
				}
			}
			if (msje.isEmpty()) {
				msjes = validAtribute(arrayRows.getAncho(), false, 0, true);//Ancho
				if (!msjes.isEmpty()) {
					msje = msjes.get("desMsje").toString();
					msjes.put("desMsje", "ANCHO. "+msje);
					msjes.put("cveMsje", "WB0025_"+msjes.get("cveMsje").toString());
				}
			}
			if (msje.isEmpty()) {
				msjes = validAtribute(arrayRows.getAlto(), false, 0, true);//Alto
				if (!msjes.isEmpty()) {
					msje = msjes.get("desMsje").toString();
					msjes.put("desMsje", "ALTO. "+msje);
					msjes.put("cveMsje", "WB0026_"+msjes.get("cveMsje").toString());
				}
			}
			
			if (msje.isEmpty()) {
				msjes = validAtribute(arrayRows.getTipoEntrega(), true, 15, false);//tipo entrega
				if (!msjes.isEmpty()) {
					msje = msjes.get("desMsje").toString();
					msjes.put("desMsje", "TIPO DE ENTREGA. "+msje);
					msjes.put("cveMsje", "WB0027_"+msjes.get("cveMsje").toString());
				}
			}
			
			if (msje.isEmpty()) {
				msjes = validAtribute(arrayRows.getFormaPago(), false, 10, false);//Forma de pago
				if (!msjes.isEmpty()) {
					msje = msjes.get("desMsje").toString();
					msjes.put("desMsje", "FORMA DE PAGO. "+msje);
					msjes.put("cveMsje", "WB0028_"+msjes.get("cveMsje").toString());
				}
			}
			
			if (msje.isEmpty()) {
				msjes = validAtribute(arrayRows.getAcuse(), true, 15, false);//tipo acuse
				if (!msjes.isEmpty()) {
					msje = msjes.get("desMsje").toString();
					msjes.put("desMsje", "TIPO DE ACUSE. "+msje);
					msjes.put("cveMsje", "WB0029_"+msjes.get("cveMsje").toString());
				}
			}
			
			if (msje.isEmpty()) {
				msjes = validAtribute(arrayRows.getCod(), false, 0, true);//cod
				if (!msjes.isEmpty()) {
					msje = msjes.get("desMsje").toString();
					msjes.put("desMsje", "COD. "+msje);
					msjes.put("cveMsje", "WB0030_"+msjes.get("cveMsje").toString());
				}
			}
			if (msje.isEmpty()) {
				msjes = validAtribute(arrayRows.getValorDeclarado(), false, 0, true);//valor declarado
				if (!msjes.isEmpty()) {
					msje = msjes.get("desMsje").toString();
					msjes.put("desMsje", "VALOR DECLARADO. "+msje);
					msjes.put("cveMsje", "WB0031_"+msjes.get("cveMsje").toString());
				}
			}
			
			if (msje.isEmpty()) {
				msjes = validAtribute(arrayRows.getCobertura(), true, 15, false);//TIPO DE SEGURO
				if (!msjes.isEmpty()) {
					msje = msjes.get("desMsje").toString();
					msjes.put("desMsje", "COBERTURA. "+msje);
					msjes.put("cveMsje", "WB0031_"+msjes.get("cveMsje").toString());
				}
			}
			if (msje.isEmpty()) {
				msjes = validAtribute(arrayRows.getComentarios(), false, 255, false);//Comentarios
				if (!msjes.isEmpty()) {
					msje = msjes.get("desMsje").toString();
					msjes.put("desMsje", "COMENTARIOS. "+msje);
					msjes.put("cveMsje", "WB0032_"+msjes.get("cveMsje").toString());
				}
			}
			if (msje.isEmpty()) {
				msjes = validAtribute(arrayRows.getReferencia(), false, 65, false);//Referencia
				if (!msjes.isEmpty()) {
					msje = msjes.get("desMsje").toString();
					msjes.put("desMsje", "REFERENCIA. "+msje);
					msjes.put("cveMsje", "WB0033_"+msjes.get("cveMsje").toString());
				}
			}
			if (msje.isEmpty()) {
				msjes = validAtribute(arrayRows.getAgenteAduanal(), false, 50, false);//agente aduanal
				if (!msjes.isEmpty()) {
					msje = msjes.get("desMsje").toString();
					msjes.put("desMsje", "AGENTE ADUANAL. "+msje);
					msjes.put("cveMsje", "WB0034_"+msjes.get("cveMsje").toString());
				}
			}
			if (msje.isEmpty()) {
				msjes = validAtribute(arrayRows.getPedimento(), false, 50, false);//Pedimento
				if (!msjes.isEmpty()) {
					msje = msjes.get("desMsje").toString();
					msjes.put("desMsje", "PEDIMENTO. "+msje);
					msjes.put("cveMsje", "WB0035_"+msjes.get("cveMsje").toString());
				}
			}
			if (msje.isEmpty()) {
				msjes = validAtribute(arrayRows.getCentroCosto(), false, 15, false);//clave de centro de costo
				if (!msjes.isEmpty()) {
					msje = msjes.get("desMsje").toString();
					msjes.put("desMsje", "CLAVE CENTRO DE COSTO. "+msje);
					msjes.put("cveMsje", "WB0036_"+msjes.get("cveMsje").toString());
				}
			}
			if (msje.isEmpty()) {//AAP25
				msjes = validAtribute(arrayRows.getSegId(), false, 15, false);//clave servicio express//AAP25
				if (!msjes.isEmpty()) {//AAP25
					msje = msjes.get("desMsje").toString();//AAP25
					msjes.put("desMsje", "CLAVE SERVICIO EXPRESS. "+msje);//AAP25
					msjes.put("cveMsje", "WB0037_"+msjes.get("cveMsje").toString());//AAP25
				}//AAP25
			}//AAP25
			
			if (msje.isEmpty()) {
				msjes = validAtribute(arrayRows.getProductId(), false, 15, true);//productID sat
				if (!msjes.isEmpty()) {//AAP25
					msje = msjes.get("desMsje").toString();//AAP25
					msjes.put("desMsje", "ID PRODUCTO SAT. "+msje);//AAP25
					msjes.put("cveMsje", "WB0038_"+msjes.get("cveMsje").toString());//AAP25
				}//AAP25
			}//AAP25
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("validObject()_Error Exception:").append(e).toString());
			e.printStackTrace();
		}
		return msjes;
	}
	
	private HashMap<String, String> validAtribute(String value, boolean required, int length, boolean numOnly){
		HashMap<String, String> msjes = new HashMap<String, String>(3);
		try {
			if (required) {
				if (value.trim().length()==0) {
					msjes.put("cveMsje", "01");
					msjes.put("desMsje", "VALOR REQUERIDO");
				}
			}
			
			if (msjes.isEmpty()) {
				if (length>0 && value.trim().length()>length) {
					msjes.put("cveMsje", "02");
					msjes.put("desMsje", "LONGITUD NO VÁLIDA");
				}
			}
			
			if (msjes.isEmpty()) {
				if (numOnly) {
					try {
						if (!value.toString().trim().isEmpty()) {
							Double.parseDouble(value.toString().trim());
						}						
					}catch(Exception e) {
						msjes.put("cveMsje", "03");
						msjes.put("desMsje", "CAPTURAR SOLO NÚMEROS");
					}					
				}
			}
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("validAtribute()_Error Exception:").append(e).toString());
			e.printStackTrace();
		}
		return msjes;
	}
	
	private String getCadenaImpresion(Connection con, Global global, String rastreo) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		String cadenaEtiqueta = "";
		Clob myClob = null;
		
		try {
			String query = "SELECT CP_ETIQUETA_ZPL(?, ?, ?, ?) FROM DUAL";
			pst = con.prepareStatement(query);
			pst.setString(1, rastreo);
			pst.setString(2, "N");
			pst.setString(3, "0");
			pst.setString(4, "A");
			
			rs = pst.executeQuery();
			
			if (rs.next()) {
				myClob = rs.getClob(1);
				cadenaEtiqueta = ClobToString(myClob);
			}
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getCadenaImpresion()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.closeResources(rs, pst);
		}
		return cadenaEtiqueta;
	}
	private String getCadenaImpresionGuiaRet(Connection con, String rastreo) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		String cadenaEtiqueta = "";
		Clob myClob = null;
		
		try {
			String query = "SELECT CP_ETIQUETA_ZPL_RET(?, ?, ?, ?) FROM DUAL";
			pst = con.prepareStatement(query);
			pst.setString(1, rastreo);
			pst.setString(2, "N");
			pst.setString(3, "0");
			pst.setString(4, "A");
			
			rs = pst.executeQuery();
			
			if (rs.next()) {
				myClob = rs.getClob(1);
				cadenaEtiqueta = ClobToString(myClob);
			}
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getCadenaImpresion()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.closeResources(rs, pst);
		}
		return cadenaEtiqueta;
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private com.paquetexpress.www.webbooking.GuiasRetorno.retorno.DataResponse 
	getSavedGuiasReturn(Connection con, ReadxlDTO rastreoObj, int cantidadRastreos, String fname, Global global) {
		SheetRecovery sr = new SheetRecovery();
		PreparedStatement pst = null;
		ResultSet rs = null;
		String cadenaEtiqueta = "";
		ArrayList guiasRetornoList = new ArrayList();
		HashMap row = new HashMap(5);
		com.paquetexpress.www.webbooking.GuiasRetorno.retorno.DataResponse dataResponseGuiasRetorno = null;
		com.paquetexpress.www.webbooking.GuiasRetorno.retorno.DataResponse dataResponseGuiasRetornoFaltante = null;
		int cantidadRastreosFaltante = 0;
		String rastreoRet = "";
		String guiaRet = "";
		try {
			
			String query = "SELECT BR_GUIA_NO_RET, BR_FORM_NO_RET, BR_MSGE_ID, BR_MSGE_TYPE, BR_MSGE_DES FROM WEB_BACH_DETL_GUIA_RET WHERE BR_GUIA_NO = ?";
			pst = con.prepareStatement(query);
			pst.setString(1, rastreoObj.getRastreo());
			
			rs = pst.executeQuery();
			/*consulta los rastreos de retorno generados mediante el rastreo de envio*/
			while (rs.next()) {
				row.put("BR_GUIA_NO_RET", 	rs.getString(1) == null ? "" : rs.getString(1));
				row.put("BR_FORM_NO_RET",	rs.getString(2) == null ? "" : rs.getString(2));
				row.put("BR_MSGE_ID", 		rs.getString(3) == null ? "0" : rs.getString(3));
				row.put("BR_MSGE_TYPE", 	rs.getString(4) == null ? "" : rs.getString(4));
				row.put("BR_MSGE_DES", 		rs.getString(5) == null ? "" : rs.getString(5));
				guiasRetornoList.add(row.clone());
				row.clear();
			}
			
			/*si no encontró ninguno, genera la cantidad de rastreos de retorno solicitados por el rastreo de envio*/
			if (guiasRetornoList.isEmpty()) {
				dataResponseGuiasRetorno = genGuiasRetorno(con, rastreoObj, cantidadRastreos, global);
				sr.saveGuiasReturn(con, global, fname, rastreoObj, dataResponseGuiasRetorno);
			} else {
				/*calcula el faltante de rastreos de retorno para generarlos si es necesario*/
				cantidadRastreosFaltante = cantidadRastreos - guiasRetornoList.size();
				
				/*crea instancia de dataResponseGuiasRetorno con tamaño de rastreos de retorno para alimentar objeto*/
				dataResponseGuiasRetorno = new com.paquetexpress.www.webbooking.GuiasRetorno.retorno.DataResponse(
						new RetornoSolicitud(
								new RastreoRetorno[cantidadRastreos]
										)
						);
				
				int contAdd = 0;
				
				/*alimenta instancia de dataResponseGuiasRetorno en base a los rastreos de retorno ya generados, y les crea su cadena de impresion*/
				for (int indexRet = 0; indexRet<guiasRetornoList.size();indexRet++) {
					dataResponseGuiasRetorno.getRetornoSolicitud().getRastreosRetorno()[indexRet] = new RastreoRetorno();
					Mensaje[] mensajes = new Mensaje[1];
					Mensaje mensaje = new Mensaje();
					rastreoRet = ((HashMap)guiasRetornoList.get(indexRet)).get("BR_GUIA_NO_RET").toString();
					guiaRet = ((HashMap)guiasRetornoList.get(indexRet)).get("BR_FORM_NO_RET").toString();
					dataResponseGuiasRetorno.getRetornoSolicitud().getRastreosRetorno()[indexRet].setRastreoNo( rastreoRet );
					dataResponseGuiasRetorno.getRetornoSolicitud().getRastreosRetorno()[indexRet].setGuiaNo( guiaRet );
					mensaje.setCveMsjeRetorno(Integer.parseInt( ((HashMap)guiasRetornoList.get(indexRet)).get("BR_MSGE_ID").toString() ));
					mensaje.setTipoMsje( ((HashMap)guiasRetornoList.get(indexRet)).get("BR_MSGE_TYPE").toString() );
					mensaje.setDesMsjeRetorno( ((HashMap)guiasRetornoList.get(indexRet)).get("BR_MSGE_DES").toString() );
					mensajes[0] = mensaje;
					cadenaEtiqueta = getCadenaImpresionGuiaRet(con, rastreoRet);
					dataResponseGuiasRetorno.getRetornoSolicitud().getRastreosRetorno()[indexRet].setCadenaImpresion(cadenaEtiqueta);
					dataResponseGuiasRetorno.getRetornoSolicitud().getRastreosRetorno()[indexRet].setMensajes(mensajes);
					contAdd++;
				}
				
				/*si hubo rastreos de retorno faltantes, crea la cantidad de rastreos faltantes 
				 * mediante servicio web de generacion de GUIAS DE RETORNO, almacenandolo en dataResponseGuiasRetornoFaltante.
				 * 
				 * Mediante el objeto dataResponseGuiasRetornoFaltante termina de cargar los rastreos de retorno al objeto dataResponseGuiasRetorno*/				
				if (cantidadRastreosFaltante>0) {
					dataResponseGuiasRetornoFaltante = genGuiasRetorno(con, rastreoObj, cantidadRastreosFaltante, global);
					sr.saveGuiasReturn(con, global, fname, rastreoObj, dataResponseGuiasRetorno);
					for (int indexRet = 0; indexRet<guiasRetornoList.size(); indexRet++) {
						dataResponseGuiasRetorno.getRetornoSolicitud().getRastreosRetorno()[contAdd] = 
								dataResponseGuiasRetornoFaltante.getRetornoSolicitud().getRastreosRetorno()[indexRet];								
					}
				}				
			}
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getSavedGuiasReturn()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.closeResources(rs, pst);
		}
		return dataResponseGuiasRetorno;
	}
	private com.paquetexpress.www.webbooking.GuiasRetorno.retorno.DataResponse genGuiasRetorno(Connection con, ReadxlDTO rastreo, int cantidadRastreos, Global global){
		
		GuiasRetorno guiasRetorno = new GuiasRetorno();
		com.paquetexpress.www.webbooking.GuiasRetorno.retorno.DataResponse dataResponseGuiasRetorno = null;
		try {
			/* Se ejecuta servicio web de rastreos de retorno*/
			dataResponseGuiasRetorno = 
					guiasRetorno.getGuiaRetorno(
							rastreo.getRastreo(), 
							rastreo.getGuia(), 
							cantidadRastreos,null, null, null, global.getOrigenUserClave());
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("genGuiasRetorno()_Error:").append(e).toString());
			e.printStackTrace();
		}
		return dataResponseGuiasRetorno;
	}
	private String ClobToString(Clob cl) {
		BufferedReader br = null;
		if (cl == null)
			return "";
		StringBuffer strOut = new StringBuffer();
		String aux;
		try {
			br = new BufferedReader(cl.getCharacterStream());
			while ((aux = br.readLine()) != null) {
				strOut.append(aux).append("\n");
			}
			br.close();
			br = null;
		} catch (SQLException e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("ClobToString()SQLException_Error:").append(e).toString());
			e.printStackTrace();
		} catch (IOException e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("ClobToString()IOException_Error:").append(e).toString());
			e.printStackTrace();
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("ClobToString()Exception_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				if (br != null) {
					br.close();
				}
			} catch (Exception e2) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("ClobToString()_Error2:").append(e2).toString());
				e2.printStackTrace();
			}
		}
		return strOut.toString();
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList<Object> passArrayToDTO(ArrayList<Object> arrayRows, Global global, HttpServletRequest req, JavFileUploadForm2 fileUpForm) {
		ArrayList<Object> result = null;
		try {
			result = new ArrayList<Object>(arrayRows.size());
			ArrayList<Object> arrayRowsDet = null;
			for(int i=1;i<arrayRows.size();i++) {
				
				ReadxlDTO readxlDTO = new ReadxlDTO();				
				
				arrayRowsDet = (ArrayList) arrayRows.get(i);
				if (arrayRowsDet.isEmpty() || arrayRowsDet.size() <= 38) {
					continue;
				}
				readxlDTO.setIdClienteDest(	arrayRowsDet.get(0).toString());//id cliente destino
				readxlDTO.setNombre(		arrayRowsDet.get( 1).toString());//Nombre cliente destino
				readxlDTO.setRfc(			arrayRowsDet.get( 2).toString());//RFC
				readxlDTO.setPais(			arrayRowsDet.get( 3).toString());//Pais
				readxlDTO.setEstado(		arrayRowsDet.get( 4).toString());//Estado
				readxlDTO.setCiudad(		arrayRowsDet.get( 5).toString());//Ciudad
				readxlDTO.setCalle(			arrayRowsDet.get( 6).toString());//calle
				readxlDTO.setNumero(		arrayRowsDet.get( 7).toString());//Numero
				readxlDTO.setColonia(		arrayRowsDet.get( 8).toString());//Colonia
				readxlDTO.setCp(			arrayRowsDet.get( 9).toString());//Código postal
				readxlDTO.seteMail(			arrayRowsDet.get(10).toString());//Email
				readxlDTO.setTelefono(		arrayRowsDet.get(11).toString());//Telefono
				readxlDTO.setReferenciaDomicilio(arrayRowsDet.get(12).toString());//Referencia domicilio
				readxlDTO.setCantidad(		arrayRowsDet.get(13).toString());//Cantidad
				readxlDTO.setCodigoBulto(	arrayRowsDet.get(14).toString());//clave tipo de bulto
				readxlDTO.setContenido(		arrayRowsDet.get(15).toString());//Contenido
				readxlDTO.setTipoTarifa(	arrayRowsDet.get(16).toString());//tipo tarifa
				readxlDTO.setPeso(			arrayRowsDet.get(17).toString());//peso
				readxlDTO.setVolumen(		arrayRowsDet.get(18).toString());//Volumen
				readxlDTO.setLargo(			arrayRowsDet.get(19).toString());//Largo
				readxlDTO.setAncho(			arrayRowsDet.get(20).toString());//Ancho
				readxlDTO.setAlto(			arrayRowsDet.get(21).toString());//Alto
				readxlDTO.setTipoEntrega(	arrayRowsDet.get(22).toString());//tipo entrega
				readxlDTO.setFormaPago(		arrayRowsDet.get(23).toString());//Forma de pago
				readxlDTO.setAcuse(			arrayRowsDet.get(24).toString());//tipo acuse
				readxlDTO.setCod(			arrayRowsDet.get(25).toString());//cod
				readxlDTO.setValorDeclarado(arrayRowsDet.get(26).toString());//valor declarado
				readxlDTO.setCobertura(		arrayRowsDet.get(27).toString());//Cobertura (TIPO DE SEGURO)
				readxlDTO.setComentarios(	arrayRowsDet.get(28).toString());//Comentarios
				readxlDTO.setReferencia(	arrayRowsDet.get(29).toString());//Referencia
				readxlDTO.setAgenteAduanal(	arrayRowsDet.get(30).toString());//agente aduanal
				readxlDTO.setPedimento(		arrayRowsDet.get(31).toString());//Pedimento
				readxlDTO.setCentroCosto(	arrayRowsDet.get(32).toString());//clave de centro de costo
				
				readxlDTO.setCveMensaje(	arrayRowsDet.get(33).toString());//clave de mensaje
				readxlDTO.setTipoMensaje(	arrayRowsDet.get(34).toString());//tipo de mensaje
				readxlDTO.setDesMensaje(	arrayRowsDet.get(35).toString());//descripcion de mensaje
				readxlDTO.setRastreo(		arrayRowsDet.get(36).toString());//rastreo
				readxlDTO.setGuia(			arrayRowsDet.get(37).toString());//guia
				readxlDTO.setImporte(		arrayRowsDet.get(38).toString());//importe
				readxlDTO.setLinkPDF(		arrayRowsDet.get(39).toString());//link PDF
				
				if (arrayRowsDet.size() >= 41) {
					if (global.getAllowRRFromXLS().equals("Y")) {//AAP25 Se agrega validacion para permitir generar guias RR
						readxlDTO.setCantidadRastreos(arrayRowsDet.get(40).toString());//cantidad de rastreos de retorno
					} else {//AAP25
						readxlDTO.setCantidadRastreos("0");//cantidad de rastreos de retorno	
					}//AAP25
				}
				
				if (arrayRowsDet.size() >= 42) {//AAP25
					readxlDTO.setSegId(arrayRowsDet.get(41).toString().trim());//tipo de servicio STD / SEG
				}
				
				if (arrayRowsDet.size() == 43) {
					readxlDTO.setProductId(arrayRowsDet.get(42).toString().trim());//productID -- clave catalogo SAT
				}
				result.add(readxlDTO);
			}
		} catch (Exception e) {
			req.setAttribute("errormsgprint", "El archivo ingresado no contiene los registros o formato de cuadricula correctos. Por favor, verifica el archivo.");
			fileUpForm.setFlagGuiaProcess("N");//permite ver la visualizacion de los datos 
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("passArrayToDTO()Exception_Error:").append(e).toString());
			e.printStackTrace();
		}
		return result;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList<Object> passDTOToArray(ArrayList<Object> arrayRows, ArrayList<Object> arrayRowsDTO){		
		
		try {			
			for(int i=0;i<arrayRowsDTO.size();i++) {
				
				ReadxlDTO readxlDTO = (ReadxlDTO) arrayRowsDTO.get(i);	
				((ArrayList)arrayRows.get(i)).set(33, readxlDTO.getCveMensaje());//clave de mensaje
				((ArrayList)arrayRows.get(i)).set(34, readxlDTO.getTipoMensaje());//tipo de mensaje
				((ArrayList)arrayRows.get(i)).set(35, readxlDTO.getDesMensaje());//descripcion de mensaje
				((ArrayList)arrayRows.get(i)).set(36, readxlDTO.getRastreo());//rastreo
				((ArrayList)arrayRows.get(i)).set(37, readxlDTO.getGuia());//guia
				((ArrayList)arrayRows.get(i)).set(38, readxlDTO.getImporte());//importe
				((ArrayList)arrayRows.get(i)).set(39, readxlDTO.getLinkPDF());//link PDF
			}
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("passDTOToArray()Exception_Error:").append(e).toString());
			e.printStackTrace();
		}
		return arrayRows;
	}
}