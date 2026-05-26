package paquetexpress.internal.prepaid.monitor.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

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
import paquetexpress.internal.prepaid.monitor.dao.ConversionMonitorDao;
import paquetexpress.internal.prepaid.monitor.form.ConversionMonitorForm;

public class ConversionMonitorAction extends Action {
	private StringBuffer concatena = new StringBuffer();
	//private final String msgAvi  = concatena.delete(0, concatena.length()).append("MSAVI_B_").append(this.getClass().getName()).append(".").toString();
	private final String msgErr  = concatena.delete(0, concatena.length()).append("MSERR_B_").append(this.getClass().getName()).append(".").toString();
	
	@SuppressWarnings("rawtypes")
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		HttpSession session = request.getSession(false);
		ConversionMonitorDao dao = new ConversionMonitorDao();
		String forward = "start";
		try {
			//System.out.println("entró a accion" );
			if(session==null){
				return mapping.findForward("nosession");
				// return mapping.findForward("nosession");
			}
			String clientId = (String)session.getAttribute("sClientId");
			//For Nosession
			if (clientId==null) {
				return mapping.findForward("nosession");
			}
			if (form instanceof ConversionMonitorForm) {
				ArrayList result = null;
				HashMap resultCombo = null;
				ConversionMonitorForm monitorForm = (ConversionMonitorForm) form;
				monitorForm.setBanCerrar("true");//bandera para validar cuando se cierre la ventana desde "x" de navegador
				//System.out.println("monitorForm.getCurrentTask() " +monitorForm.getCurrentTask());
				
				forward = monitorForm.getCurrentTask();
				
				if (monitorForm.getCurrentTask().equals("start")) {
					monitorForm.setBranchId((String)session.getAttribute("branchid"));
					monitorForm.setEsPropietario("");
					result = dao.obtieneSetRegistros(session);
					request.setAttribute("conjuntoGuias", result);
				} else if (monitorForm.getCurrentTask().equals("loadSetDetail")) {
					//System.out.println("monitorForm.getIdSetSelOld() "+monitorForm.getIdSetSelOld());
					boolean idSetSelOld = false;
					if (monitorForm.getIdSetSelOld().length()>0) {
						limpiarGuiasSelect(monitorForm, session, dao );
						monitorForm.setClickedItems("");
						monitorForm.setCurrentGuia("");
						monitorForm.setIdSetSelOld("");
						idSetSelOld = true;
					}					
					
					result = dao.obtieneSetRegistros(session);
					request.setAttribute("conjuntoGuias", result.clone());
					
					String esPropietario = "";
					if (((Global) session.getAttribute("sGlobal")).getOrigenUserLevel().equals("0")) {
						esPropietario = dao.esClientePropietario(monitorForm.getIdSetSel(), session);
						monitorForm.setEsPropietario(esPropietario);
						if (esPropietario.equals("Y")) {
							resultCombo = dao.getFiltrosCombo(monitorForm.getIdSetSel(), session);
							
							String valueString = ((ArrayList)resultCombo.get("value")).get(0).toString();
							boolean guiasLibres = false;
							if (monitorForm.getFiltrarPor().length() == 0 || idSetSelOld) {
								for (int i=0;i<((ArrayList)resultCombo.get("value")).size();i++) {
									valueString = ((ArrayList)resultCombo.get("value")).get(i).toString();
									if (valueString.substring(0,valueString.indexOf('|')).equals(
											((Global) session.getAttribute("sGlobal")).getClientId()
											)) {
										monitorForm.setFiltrarPor(valueString);
										guiasLibres = true;
										break;
									}
								}
								
								if (monitorForm.getFiltrarPor().length() == 0 || !guiasLibres) {
									monitorForm.setFiltrarPor(((ArrayList)resultCombo.get("value")).get(0).toString());
								}
							}							
							
							/*valueString = ((ArrayList)resultCombo.get("value")).get(2).toString();
							System.out.println("valueString ini indexof "+valueString.substring(0,valueString.indexOf('|')));
							System.out.println("valueString indexof fin "+valueString.substring(valueString.indexOf('|')+1,valueString.length()));*/
//							String[] values = valueString.split("|");
//					        for (int i = 0; i < values.length; i++) {
//					            System.out.println(values[i]);
//					        }

							monitorForm.setFiltrarPorValue((ArrayList)resultCombo.get("value"));
							monitorForm.setFiltrarPorLabel((ArrayList)resultCombo.get("label"));
							session.setAttribute("filtrarPorValue", resultCombo.get("value"));
							session.setAttribute("filtrarPorLabel", resultCombo.get("label"));
						} else {
							monitorForm.setFiltrarPorValue(new ArrayList(0));
							monitorForm.setFiltrarPorLabel(new ArrayList(0));
						}
						
					}
					
					result = dao.obtieneDetalleRegistro(monitorForm.getIdSetSel(),monitorForm.getClickedItems(), session, esPropietario, monitorForm.getFiltrarPor());
					
					if (!result.isEmpty()) {
						monitorForm.setClientePropietario(
								((ArrayList)result.get(0)).get(2).toString()
						);
					}
					/*define tipo de asignacion por default*/
					if (monitorForm.getTipoAsignacion().length() == 0) {
						monitorForm.setTipoAsignacion("sin");
					}
					
					request.setAttribute("detalleGuias", result);
					
				} else if (monitorForm.getCurrentTask().equals("controlGuia")) {
					//System.out.println("monitorForm.getCurrentGuia() "+monitorForm.getCurrentGuia());
					if (monitorForm.getCurrentGuia().trim().length()>0) {
						String error = "";
						if (monitorForm.getCurrentGuia().indexOf(" ")==-1) {
							//System.out.println("sencillo");
							if (monitorForm.getClickedItems().indexOf(monitorForm.getCurrentGuia())==-1) {
								//System.out.println("desbloquear guia");
								error = dao.unlockGuia(monitorForm.getIdSetSel(), monitorForm.getCurrentGuia(), session);
							} else {
								//System.out.println("bloquear guia");
								error = dao.lockGuia(monitorForm.getIdSetSel(), monitorForm.getCurrentGuia(), session);	
								
								//System.out.println("monitorForm.getClickedItems() "+monitorForm.getClickedItems());
								
								if (error.length()>0) {
									/*elimina guia seleccionada de lista de guias.*/
									StringTokenizer st = null;
									String gnoset = "";
									String guiaSelect = "";
									if (monitorForm.getClickedItems() != null || monitorForm.getClickedItems().equals("")) {
										st = new StringTokenizer(monitorForm.getClickedItems());
										while (st.hasMoreElements()) {
											guiaSelect = st.nextToken();
											//System.out.println("guiaSelect "+guiaSelect);
											if (!monitorForm.getCurrentGuia().trim().equals(guiaSelect.trim()))
												gnoset = concatena.delete(0,concatena.length()).append( gnoset ).append( guiaSelect ).append(" ").toString();
										}
									}
									//System.out.println("gnoset "+gnoset);
									monitorForm.setClickedItems(gnoset);
									request.setAttribute("clickedItems", gnoset);
								}
							}
						} else {
							HashMap resultado = null;
							ArrayList listaFinal = null;
							String gnoset = "";
							//System.out.println("multiple");
							/*multiples guias*/						
							if (monitorForm.getCbAll().equals("off")) {
								//System.out.println("desbloquear todas las guias");
								resultado = dao.unlockGuias(monitorForm.getIdSetSel(), monitorForm.getCurrentGuia(), session);
							} else {
								//System.out.println("bloquear todas las guias");
								resultado = dao.lockGuias(monitorForm.getIdSetSel(), monitorForm.getCurrentGuia(), session);	
							}
							//System.out.println("monitorForm.getCbAll() "+monitorForm.getCbAll());
							if (resultado != null) {
								error = resultado.get("error").toString();
								listaFinal = (ArrayList) resultado.get("listaFinal");
								for (int i=0; i<listaFinal.size();i++) {
									gnoset = concatena.delete(0,concatena.length()).append( gnoset ).append( listaFinal.get(i).toString() ).append(" ").toString();
								}
							}
							//System.out.println("gnoset "+gnoset);
							monitorForm.setClickedItems(gnoset);
							request.setAttribute("clickedItems", gnoset);
						}
						
						//System.out.println("error "+error);
						if (error.length()>0) {
							request.setAttribute("error", error);
						}
						/*se limpia variable para la siguiente iteraccion*/
						monitorForm.setCurrentGuia("");
					} /*else {
						
						limpiarGuiasSelect(monitorForm, session, dao );
						monitorForm.setClickedItems("");
					}*/
					
					/*result = dao.obtieneSetRegistros(session);		
					request.setAttribute("conjuntoGuias", result.clone());					
					
					result = dao.obtieneDetalleRegistro(monitorForm.getIdSetSel(),monitorForm.getClickedItems(), session, monitorForm.getEsPropietario(), monitorForm.getFiltrarPor());
					request.setAttribute("detalleGuias", result);
					
					if (session.getAttribute("filtrarPorValue") != null) {
						monitorForm.setFiltrarPorValue((ArrayList) session.getAttribute("filtrarPorValue"));
						monitorForm.setFiltrarPorLabel((ArrayList) session.getAttribute("filtrarPorLabel"));							
					}*/
				} else if (monitorForm.getCurrentTask().equals("mainpage") ||
						monitorForm.getCurrentTask().equals("clearGuias")) {

					limpiarGuiasSelect(monitorForm, session, dao );
					monitorForm.setClickedItems("");
					if (session.getAttribute("filtrarPorValue") != null) {
						session.removeAttribute("filtrarPorValue");
						session.removeAttribute("filtrarPorLabel");
					}
				} else if (monitorForm.getCurrentTask().equals("asignaGuia")) {
					String error = "";
					
					/*asigna guias por set cuando se genera PDF de todo el set de guias*/
					if (monitorForm.getIdSetSelPDF().trim().length()>0) {
						dao.asignaGuiaSetPDF(monitorForm, session);
					} else {/*asigna guias seleccionadas (tanto de PDF como las asignaciones a clientes o usuarios)*/
						error = dao.asignaGuia(monitorForm, session);
					}
					
					if (error.length()>0) {
						request.setAttribute("error", error);
						
						if (monitorForm.getGenPDF().equals("Y")) {
							monitorForm.setGenPDF("E");
						}
					}
					
					if (monitorForm.getGenPDF().equals("Y")) {
						String urlRastreoPDF = ConnectDB.getCartaPorteExt();
						if (request.getRemoteAddr().trim().substring(0,3).equals("0:0") || request.getRemoteAddr().trim().substring(0,8).equals("192.168.")) {
							urlRastreoPDF =ConnectDB.getCartaPorteInt();
						}
						monitorForm.setUrlRastreoPDF(urlRastreoPDF);
						
					}
					
					result = dao.obtieneSetRegistros(session);
					request.setAttribute("conjuntoGuias", result.clone());					
					
					/* **************************************
					 * preparacion de combo filtro          *
					 * **************************************/
					resultCombo = dao.getFiltrosCombo(monitorForm.getIdSetSel(), session);
					String valueString = "";
					boolean encontroFiltro = false;
					boolean guiasLibres = false;
					
					/*busca opcion en combo previamente seleccionada*/
					for (int i=0;i<((ArrayList)resultCombo.get("value")).size();i++) {
						valueString = ((ArrayList)resultCombo.get("value")).get(i).toString();
						if (valueString.equals(
								monitorForm.getFiltrarPor()
								)) {
							monitorForm.setFiltrarPor(valueString);
							encontroFiltro = true;
							break;
						}
					}
					
					/*si no encontró filtro, busca guias sin asignar*/
					if (!encontroFiltro) {
						for (int i=0;i<((ArrayList)resultCombo.get("value")).size();i++) {
							valueString = ((ArrayList)resultCombo.get("value")).get(i).toString();
							if (valueString.substring(0,valueString.indexOf('|')).equals(
									((Global) session.getAttribute("sGlobal")).getClientId()
									)) {
								monitorForm.setFiltrarPor(valueString);
								guiasLibres = true;
								break;
							}
						}
						
						if (monitorForm.getFiltrarPor().length() == 0 || !guiasLibres) {
							monitorForm.setFiltrarPor(((ArrayList)resultCombo.get("value")).get(0).toString());
						}					
					}					
					
					monitorForm.setFiltrarPorValue((ArrayList)resultCombo.get("value"));
					monitorForm.setFiltrarPorLabel((ArrayList)resultCombo.get("label"));
					session.setAttribute("filtrarPorValue", resultCombo.get("value"));
					session.setAttribute("filtrarPorLabel", resultCombo.get("label"));					
					/* **************************************
					 * fin preparacion de combo filtro      *
					 * **************************************/
					
					
					result = dao.obtieneDetalleRegistro(monitorForm.getIdSetSel(),monitorForm.getClickedItems(), session, monitorForm.getEsPropietario(), monitorForm.getFiltrarPor());
					request.setAttribute("detalleGuias", result);
					
					limpiarGuiasSelect(monitorForm, session, dao );
					monitorForm.setClickedItems("");
					monitorForm.setCveSucursalAsig("");
					monitorForm.setDesSucursalAsig("");
					monitorForm.setCveClienteAsig("");
					monitorForm.setDesClienteAsig("");
					monitorForm.setCveUserAsig("");
					monitorForm.setDesUserAsig("");
					monitorForm.setCbAll("off");
					monitorForm.setContRastreos("0");
									
				} else if (monitorForm.getCurrentTask().equals("cambioFiltro")) {
					limpiarGuiasSelect(monitorForm, session, dao );
					monitorForm.setClickedItems("");
					monitorForm.setCurrentGuia("");
					monitorForm.setIdSetSelOld("");
					monitorForm.setCbAll("off");
					monitorForm.setContRastreos("0");
						
					result = dao.obtieneSetRegistros(session);
					request.setAttribute("conjuntoGuias", result.clone());					
					
					result = dao.obtieneDetalleRegistro(monitorForm.getIdSetSel(),monitorForm.getClickedItems(), session, monitorForm.getEsPropietario(), monitorForm.getFiltrarPor());
					request.setAttribute("detalleGuias", result);
					if (session.getAttribute("filtrarPorValue") != null) {
						monitorForm.setFiltrarPorValue((ArrayList) session.getAttribute("filtrarPorValue"));
						monitorForm.setFiltrarPorLabel((ArrayList) session.getAttribute("filtrarPorLabel"));							
					}
				}
			}			
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0,concatena.length()).append(msgErr).append("perform()_Error:").append(e).append("\nCausa:").append(e.getCause()).toString());
			e.printStackTrace();
		}
		return mapping.findForward(forward);	
	}
	
	private void limpiarGuiasSelect(ConversionMonitorForm monitorForm, HttpSession session, ConversionMonitorDao dao ){
		if (monitorForm.getClickedItems().length()>0) {
			//System.out.println("mainpage desbloquear guia");
			/*elimina guia seleccionadas*/
			StringTokenizer st = null;						
			String guiaSelect = "";
			String idSetSel = "";
			//System.out.println("monitorForm.getClickedItems() "+monitorForm.getClickedItems());
			if (monitorForm.getClickedItems() != null || monitorForm.getClickedItems().equals("")) {
				if (monitorForm.getIdSetSelOld().length() == 0) {
					idSetSel = monitorForm.getIdSetSel();
				} else {
					idSetSel = monitorForm.getIdSetSelOld();
				}
				//System.out.println("entro a desbloquear");
				st = new StringTokenizer(monitorForm.getClickedItems());
				while (st.hasMoreElements()) {
					//System.out.println("ciclo desbloqueo");
					guiaSelect = st.nextToken();
					dao.unlockGuia(idSetSel, guiaSelect, session);
				}
			}						
		}
	}

}
