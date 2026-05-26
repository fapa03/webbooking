package beanAction;

/*
 * Created on Aug 24, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import logger.AccessLog;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import com.paquetexpress.www.webbooking.GuiasRetorno.retorno.DataResponse;
import com.paquetexpress.www.webbooking.GuiasRetorno.retorno.RastreoRetorno;
import bean.Global;
import bean.PrintFileExport;
import bean.Readxl;
import bean.ReadxlDTO;
import bean.SheetRecovery;
import beanForm.JavFileUploadForm2;
import beanForm.JavImportWebbookingForm;
import beanUtil.ConnectDB;


public class JavFileUploadAction2 extends Action {
	private StringBuffer cnct = new StringBuffer();
	private final String msgErr = new StringBuffer("MSERR_B_").append(this.getClass().getName()).append(".").toString();

	Time sysTime = null;
	JavImportWebbookingForm WebbookingForm = new JavImportWebbookingForm();
	@SuppressWarnings("rawtypes")
	ArrayList listofNames = new ArrayList();
	@SuppressWarnings("rawtypes")
	ArrayList listofForms = new ArrayList();
	HashMap<String, Object> result = null;
	String fileName = "";
	String processfilename = "";
	String guiasGenDocuments = ""; 

	@SuppressWarnings({ "unchecked"})
	public ActionForward perform(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws FileNotFoundException, IOException {
		Readxl xlsFile = new Readxl();
		SheetRecovery sr = new SheetRecovery();
		Connection con = null;
		String resultExtract = "";
		String flow = "";
		HttpSession session = request.getSession(false);
		boolean isXlsxFile = false;
		// if session expired ask him to login once again
		if (session == null || session.isNew()) {
			return mapping.findForward("nosession");
		}

		Global glo = (Global) session.getAttribute("sGlobal");

		if (glo == null)
			return mapping.findForward("nosession");

		JavFileUploadForm2 fileUpForm = new JavFileUploadForm2();
		if (request.getParameter("action") != null)
			flow = request.getParameter("action");		
		
		FormFile file = null;
		// Check user pressed confirm button after taking printout
		fileUpForm = (JavFileUploadForm2) form;
		if (fileUpForm != null)
			file = fileUpForm.getTheFile();
		
		fileUpForm.setCurrentTask(flow);
		try {
			con = ConnectDB.getConnection();
			con.setAutoCommit(false);
		} catch (SQLException e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("perform()_Error3:").append(e).toString());
			e.printStackTrace();
		}
		
		try {
			// check user uploads file
			String cadenaWW = "";
			String cadenaRet = "";
			
			if (flow.trim().equals("")) {
				String fileNameP = sr.findFilesP(con, glo, "P");				
				fileUpForm.setFileName(fileNameP);
				  
				
				
			} else if (flow.equalsIgnoreCase("reinit")) {				
				sr.updateFinSheetMstr(con, glo, fileUpForm.getFileName(), "F");
				sr.updateFinRowSheetDtl(con, glo, fileUpForm.getFileName(),"F");
				
				
			} else if (flow.equalsIgnoreCase("upload")) {
				String fname = "";
				fname = file.getFileName();
				fname = fname.substring(fname.indexOf('.') +1, fname.length());
				// check type of the file as txt
				if (fname.equalsIgnoreCase("xls") || fname.equalsIgnoreCase("xlsx")) {
					// storing the content as a file
					resultExtract = saveFile(fileUpForm, request, con);
					
					if (fname.toLowerCase().equals("xlsx")) {
						isXlsxFile = true;
					} else {
						isXlsxFile = false;
					}
				} else {

					request.setAttribute("errormsgprint", "Cargar solo archivos de Excel");
					return mapping.findForward("UploadPage");
				}			

				if (resultExtract.equalsIgnoreCase("UploadPage")) {
					return mapping.findForward("UploadPage");
				} else if (resultExtract.equalsIgnoreCase("success")) {
					fileUpForm.setFileName(fileName);
					// before storing the content of the file check the file
					// already exists
					//pendiente validar
					String pathFileXl = sr.getXlsMainServerPath(con);
					String pathFile = pathFileXl + File.separator + glo.getClientId() + File.separator + glo.getOrigenUserClave()+ File.separator;
					fileUpForm.setPathFile(pathFile);
					String archivo = pathFile + fileName;
					File fWorkbook = new File(archivo);
					result = xlsFile.getDataSheet(fWorkbook, isXlsxFile);
					
					if (result==null) {
						request.setAttribute("errormsgprint", "Error al cargar datos de archivo, verificar Archivo.");
						return mapping.findForward("UploadPage");
					} else {
						if (result.get("cveMsje").equals("WB0000")){
							if (result.get("matriz")==null) {
								request.setAttribute("errormsgprint", "Error al generar matriz de datos, verificar Archivo.");
								return mapping.findForward("UploadPage");
							} else {
								ArrayList<Object> arrayRows = (ArrayList<Object>) result.get("matriz");
								fileUpForm.setFlagGuiaProcess("Y"); //permite ver la visualizacion de los datos 
								ArrayList<Object> arrayRowsDTOs = xlsFile.passArrayToDTO(arrayRows, glo, request, fileUpForm/*AAP25*/);
								if (arrayRowsDTOs == null || arrayRowsDTOs.isEmpty()) {
									request.setAttribute("errormsgprint", "Error al generar matriz de datos, verificar Archivo.");
									return mapping.findForward("UploadPage");
								}
								fileUpForm.setCountGuiaError(0);
								fileUpForm.setCountGuiaInfo(0);
								
								arrayRows = xlsFile.cotizarGenerar(con, arrayRowsDTOs, fileName, glo, "Calcular", request, flow, fileUpForm);							

								if (isXlsxFile) {
									xlsFile.updateWorkBookXSSFW(arrayRows, fWorkbook);
								} else {
									xlsFile.updateWorkBookHSSFW(arrayRows, fWorkbook);
								}
								request.setAttribute("matriz", arrayRows);
							}
						} else {
							request.setAttribute("errormsgprint", result.get("desMsje").toString());
							return mapping.findForward("UploadPage");
						}
					}			
				}
			}
			// check the user pressed the process
			else if (flow.equalsIgnoreCase("process")) {
				if (fileUpForm.getFileName().trim().length()<=0) {
					request.setAttribute("errormsgprint", "No se ha cargado archivo para procesar.");
				} else {
					sr.updateFinSheetMstr(con, glo, fileUpForm.getFileName(), "P");
					String pathFileXl = sr.getXlsMainServerPath(con);
					String pathFile = pathFileXl + File.separator + glo.getClientId() + File.separator + glo.getOrigenUserClave()+ File.separator;
					String fname = "";
					fname = fileUpForm.getFileName();
					fname = fname.substring(fname.indexOf('.') +1, fname.length());
					if (fname.toLowerCase().equals("xlsx")) {
						isXlsxFile = true;
					} else {
						isXlsxFile = false;
					}
					
					String archivo = pathFile + fileUpForm.getFileName();
					File fWorkbook = new File(archivo);
					result = xlsFile.getDataSheet(fWorkbook, isXlsxFile);
					
					if (result==null) {
						request.setAttribute("errormsgprint", "Error al cargar datos de archivo, verificar Archivo.");
						return mapping.findForward("UploadPage");
					} else {
						if (result.get("cveMsje").equals("WB0000")){
							if (result.get("matriz")==null) {
								request.setAttribute("errormsgprint", "Error al generar matriz de datos, verificar Archivo.");
								return mapping.findForward("UploadPage");
							} else {
								ArrayList<Object> arrayRows = (ArrayList<Object>) result.get("matriz");
								fileUpForm.setFlagGuiaProcess("Y");//permite ver la visualizacion de los datos 
								ArrayList<Object> arrayRowsDTOs = xlsFile.passArrayToDTO(arrayRows, glo, request, fileUpForm/*AAP25*/);
								fileUpForm.setCountGuiaError(0);
								fileUpForm.setCountGuiaInfo(0);
								arrayRows = xlsFile.cotizarGenerar(con, arrayRowsDTOs, fileName, glo, "Generar", request, flow, fileUpForm);
								if (isXlsxFile) {
									xlsFile.updateWorkBookXSSFW(arrayRowsDTOs, fWorkbook);
								} else {
									xlsFile.updateWorkBookHSSFW(arrayRowsDTOs, fWorkbook);
								}
								
								String cadenaUnitaria = "";
								String cadenaUnitariaGuiaRet = "";
								StringBuffer cadenaAcumulada = new StringBuffer();
								StringBuffer cadenaAcumuladaGuiaRet = new StringBuffer();
								DataResponse dataResponse = null;
								int cantidadRastreos = 0;
								int creacionGuiasRet = 0;
								RastreoRetorno[] rastreosRetorno = null;
								RastreoRetorno rastreoRetorno = null;
								for (int i = 0; i <arrayRows.size();i++) {
									if (((ReadxlDTO)arrayRowsDTOs.get(i)).getCadeImpresion()== null) {
										cadenaUnitaria = "";
									} else {
										cadenaUnitaria = ((ReadxlDTO)arrayRowsDTOs.get(i)).getCadeImpresion();
									}
									if (cadenaUnitaria.trim().length()>0) {
										cadenaAcumulada.append(cadenaUnitaria);										
										
										/*Identifica que contenga 42 elementos para generacion de sobres de retorno*/								
										       
										try {cantidadRastreos = Integer.parseInt(((ReadxlDTO)arrayRows.get(i)).getCantidadRastreos());} catch (Exception e) {cantidadRastreos = 0; }
										if (cantidadRastreos>0) {
											dataResponse = ((ReadxlDTO)arrayRows.get(i)).getDataResponseGuiasRetorno();
											
											if (dataResponse!= null ){
												if (dataResponse.getRetornoSolicitud() != null) {
													if (dataResponse.getRetornoSolicitud().getRastreosRetorno()!= null){
														for (int indexGuia = 0; indexGuia<dataResponse.getRetornoSolicitud().getRastreosRetorno().length;indexGuia++) {
															rastreosRetorno = dataResponse.getRetornoSolicitud().getRastreosRetorno();
															rastreoRetorno = rastreosRetorno[indexGuia];
															
															if (rastreoRetorno.getCadenaImpresion() == null) {
																cadenaUnitariaGuiaRet = "";
															} else {
																cadenaUnitariaGuiaRet = rastreoRetorno.getCadenaImpresion();
															}
															
															if (cadenaUnitariaGuiaRet.trim().length()>0) {
																cadenaAcumuladaGuiaRet.append(cadenaUnitariaGuiaRet);
																creacionGuiasRet++;
															}
														}
													}
												}
											}
										}
									}
								}

								if (!glo.getAllowPrintQZ().equalsIgnoreCase("Y")) {
									new PrintFileExport(request.getSession().getServletContext().getRealPath("/")).writeToGuiaFile(request, cadenaAcumulada.toString(), getTimeStamp(con).getTime());
								}
								
								cadenaWW = cadenaAcumulada.toString();
								
								if (creacionGuiasRet>0) {
									/*habilita bandera para mostrar boton de descarga de guias de retorno*/
									fileUpForm.setFlagGuiaReturn("Y");
									//crear archivo de impresion para guias de retorno
									
									if (!glo.getAllowPrintQZ().equalsIgnoreCase("Y")) {
										new PrintFileExport(request.getSession().getServletContext().getRealPath("/")).writeToGuiaReturnFile(request, cadenaAcumuladaGuiaRet.toString(), getTimeStamp(con).getTime());
									}
									cadenaRet = cadenaAcumuladaGuiaRet.toString();
								}
								
								//Se activa el apartado para mostrar la descarga de etiquetas 4X6 masivas en formato PDF 
								guiasGenDocuments = sr.countGuiaDocumentsxls(con, fileName);
								if (Integer.valueOf(guiasGenDocuments) > 0 && glo.getPrintWwPdfXls().equalsIgnoreCase("Y")) {
									fileUpForm.setFlagGuiaEtiPdf("Y");
									fileUpForm.setLinkGuiaEtiPdf(ConnectDB.getCartaPorteInt() +"GenCartaPorteFile?fileName="+fileName + "&measure=4x6" );
								}
								
								request.setAttribute("matriz", arrayRows);
							}
						} else {
							request.setAttribute("errormsgprint", result.get("desMsje").toString());
							return mapping.findForward("UploadPage");
						}
					}
				}
			} else if (flow.equalsIgnoreCase("reload")) { 
				String pathFileXl = sr.getXlsMainServerPath(con);
				String pathFile = pathFileXl + File.separator + glo.getClientId() + File.separator + glo.getOrigenUserClave()+ File.separator;
				fileUpForm.setPathFile(pathFile);
				String archivo = pathFile + fileUpForm.getFileName();
				File fWorkbook = new File(archivo);
				String fname = "";
				fname = fileUpForm.getFileName();
				fname = fname.substring(fname.indexOf('.')+1, fname.length());
				
				if (fname.toLowerCase().equals("xlsx")) {
					isXlsxFile = true;
				} else {
					isXlsxFile = false;
				}
				result = xlsFile.getDataSheet(fWorkbook, isXlsxFile);
				
				ArrayList<Object> arrayRows = (ArrayList<Object>) result.get("matriz");
				fileUpForm.setFlagGuiaProcess("Y"); //permite ver la visualizacion de los datos 
				ArrayList<Object> arrayRowsDTOs = xlsFile.passArrayToDTO(arrayRows, glo, request, fileUpForm/*AAP25*/);
				fileUpForm.setCountGuiaError(0);
				fileUpForm.setCountGuiaInfo(0);
				
				arrayRows = xlsFile.cotizarGenerar(con, arrayRowsDTOs, fileUpForm.getFileName(), glo, "Generar", request, flow, fileUpForm);
				
				if (isXlsxFile) {
					xlsFile.updateWorkBookXSSFW(arrayRowsDTOs, fWorkbook);
				} else {
					xlsFile.updateWorkBookHSSFW(arrayRowsDTOs, fWorkbook);
				}
				
				String cadenaUnitaria = "";
				String cadenaUnitariaGuiaRet = "";
				StringBuffer cadenaAcumulada = new StringBuffer();
				StringBuffer cadenaAcumuladaGuiaRet = new StringBuffer();
				DataResponse dataResponse = null;
				int cantidadRastreos = 0;
				int creacionGuiasRet = 0;
				RastreoRetorno[] rastreosRetorno = null;
				RastreoRetorno rastreoRetorno = null;
				for (int i = 0; i <arrayRowsDTOs.size();i++) {
					if (((ReadxlDTO)arrayRowsDTOs.get(i)).getCadeImpresion()== null) {
						cadenaUnitaria = "";
					} else {
						cadenaUnitaria = ((ReadxlDTO)arrayRowsDTOs.get(i)).getCadeImpresion();
					}
					
					if (cadenaUnitaria.trim().length()>0) {
						cadenaAcumulada.append(cadenaUnitaria);										
						
						/*Identifica que contenga 42 elementos para generacion de sobres de retorno*/
						try {cantidadRastreos = Integer.parseInt(((ReadxlDTO)arrayRows.get(i)).getCantidadRastreos());} catch (Exception e) {cantidadRastreos = 0; }
						if (cantidadRastreos>0) {
							dataResponse = ((ReadxlDTO)arrayRows.get(i)).getDataResponseGuiasRetorno();
							
							if (dataResponse!= null ){
								if (dataResponse.getRetornoSolicitud() != null) {
									if (dataResponse.getRetornoSolicitud().getRastreosRetorno()!= null){
										for (int indexGuia = 0; indexGuia<dataResponse.getRetornoSolicitud().getRastreosRetorno().length;indexGuia++) {
											rastreosRetorno = dataResponse.getRetornoSolicitud().getRastreosRetorno();
											rastreoRetorno = rastreosRetorno[indexGuia];
											
											if (rastreoRetorno.getCadenaImpresion() == null) {
												cadenaUnitariaGuiaRet = "";
											} else {
												cadenaUnitariaGuiaRet = rastreoRetorno.getCadenaImpresion();
											}
											
											if (cadenaUnitariaGuiaRet.trim().length()>0) {
												cadenaAcumuladaGuiaRet.append(cadenaUnitariaGuiaRet);
												creacionGuiasRet++;
											}
										}
									}
								}
							}
						}
					}
					
				}
				cadenaWW = cadenaAcumulada.toString();
				if (!glo.getAllowPrintQZ().equalsIgnoreCase("Y")) {
					new PrintFileExport(request.getSession().getServletContext().getRealPath("/")).writeToGuiaFile(request, cadenaAcumulada.toString(), getTimeStamp(con).getTime());
				}
				if (creacionGuiasRet>0) {
					/*habilita bandera para mostrar boton de descarga de guias de retorno*/
					fileUpForm.setFlagGuiaReturn("Y");
					//crear archivo de impresion para guias de retorno
					if (!glo.getAllowPrintQZ().equalsIgnoreCase("Y")) {
						new PrintFileExport(request.getSession().getServletContext().getRealPath("/")).writeToGuiaReturnFile(request, cadenaAcumuladaGuiaRet.toString(), getTimeStamp(con).getTime());
					}
					cadenaRet = cadenaAcumuladaGuiaRet.toString();
				}
				
				//Se activa el apartado para mostrar la descarga de etiquetas 4X6 masivas en formato PDF 
				guiasGenDocuments = sr.countGuiaDocumentsxls(con, fileUpForm.getFileName());
				if (Integer.valueOf(guiasGenDocuments) > 0 && glo.getPrintWwPdfXls().equalsIgnoreCase("Y")) {
					fileUpForm.setFlagGuiaEtiPdf("Y");
					fileUpForm.setLinkGuiaEtiPdf(ConnectDB.getCartaPorteInt() +"GenCartaPorteFile?fileName="+fileUpForm.getFileName() + "&measure=4x6" );
				}
				
				request.setAttribute("matriz", arrayRows);
			}
			request.setAttribute("cadenaWW", cadenaWW);
			request.setAttribute("cadenaRet", cadenaRet);			

		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("perform()_Error1:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("perform()_Error2:").append(e).toString());
				e.printStackTrace();
			}
		}
		return mapping.findForward("thispage");
	}
	
	public java.sql.Timestamp getTimeStamp(Connection con) throws Exception {
		PreparedStatement pst = con.prepareStatement("select sysdate from dual");// Checked
		ResultSet rs = pst.executeQuery();
		java.sql.Timestamp sysTimeStamp = null;

		if (rs.next())
			sysTime = rs.getTime(1);
		sysTimeStamp = rs.getTimestamp(1);

		if (rs != null)
			rs.close();
		if (pst != null)
			pst.close();
		return sysTimeStamp;
	}

	public String saveFile(JavFileUploadForm2 form, HttpServletRequest request,
			Connection con) {
		String ext = "";
		HttpSession session = request.getSession(false);
		Global glo = (Global) session.getAttribute("sGlobal");
		FormFile myFile = form.getTheFile();
		try {
			if (myFile == null)
				return "UploadPage";
			String filname = "";
			String fileName2 = "";
			filname = myFile.getFileName();

			fileName2 = filname.substring(0, filname.indexOf('.'));
			
			ext=filname.substring(filname.indexOf('.'));

			byte[] fileData = null;

			try {
				fileData = myFile.getFileData();
			} catch (FileNotFoundException e1) {
				AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("saveFile()_Error1 File Not Found :").append(e1).toString());
				e1.printStackTrace();
			} catch (IOException e1) {
				AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("saveFile()_Error2 Input Output Error :").append(e1).toString());
				e1.printStackTrace();
			}
			SheetRecovery sr = new SheetRecovery();
			String pathFileXl = sr.getXlsMainServerPath(con);
			File f = new File(pathFileXl + File.separator + glo.getClientId() + File.separator + glo.getOrigenUserClave());
			if (!f.exists()) {
				f.mkdirs();
			}
			BufferedOutputStream buff = null;
			try {

				long syDate;		

				java.sql.Timestamp sysDate = getTimeStamp(con);
				syDate = sysDate.getTime();
				fileName = glo.getClientId() + "-" + glo.getOrigenUserClave() + "-" + syDate + "-" + fileName2 + ext;
				buff = new BufferedOutputStream(new FileOutputStream(f.toString() + File.separator + fileName));
				buff.write(fileData);

				buff.flush();
				buff.close(); // /Writing text file is over
				buff = null;
				sr.setSheetMstr(con, glo, fileName);
				con.commit();
			} catch (Exception e) {
				AccessLog.Log("Data Processing Error in Extract Method" + e.getMessage());
				AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("saveFile()_Error3:").append(e).toString());
				e.printStackTrace();
			} finally {
				try {
					if (buff != null) {
						buff.flush();
						buff.close();
					}
				} catch (Exception e) {
					AccessLog.Log("saveFile()Exception_Error:"+e);
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			AccessLog.Log("Data Processing Error in Extract Method 2" + e.getMessage());
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("saveFile()_Error4:").append(e).toString());
			e.printStackTrace();
		}		
		return "success";
	}	
}
