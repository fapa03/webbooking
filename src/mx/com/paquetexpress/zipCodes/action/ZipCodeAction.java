package mx.com.paquetexpress.zipCodes.action;

import java.io.IOException;
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

import mx.com.paquetexpress.zipCodes.dao.ZipCodeDao;
import mx.com.paquetexpress.zipCodes.form.ZipCodeForm;

public class ZipCodeAction extends Action {
	private StringBuffer concatena = new StringBuffer();
	//private final String msgAvi  = concatena.delete(0, concatena.length()).append("MSAVI_B_").append(this.getClass().getName()).append(".").toString();
	private final String msgErr  = concatena.delete(0, concatena.length()).append("MSERR_B_").append(this.getClass().getName()).append(".").toString();
	
	@SuppressWarnings("rawtypes")
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		HttpSession session = request.getSession(false);
		ZipCodeDao dao = new ZipCodeDao();
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
			if (form instanceof ZipCodeForm) {
				HashMap resultCombo = null;
				ArrayList result = new ArrayList(0);
				
				ZipCodeForm zipCodeForm = (ZipCodeForm) form;
				//System.out.println("monitorForm.getCurrentTask() " +monitorForm.getCurrentTask());
				if (request.getParameter("currentTask")!= null) {
					zipCodeForm.setCurrentTask(request.getParameter("currentTask"));
					zipCodeForm.setFind(request.getParameter("find"));
					zipCodeForm.setSearchType(request.getParameter("searchType") != null ?  request.getParameter("searchType") : "");
				}
				forward = zipCodeForm.getCurrentTask();
				
				cargaCombos(session, zipCodeForm);
				
				if (zipCodeForm.getCurrentTask().equals("start")) {
					if (zipCodeForm.getFind() != null && zipCodeForm.getFind().length()>0) {
						result = dao.getRowsByZipCode(zipCodeForm.getFind(), zipCodeForm.getSearchType());
						
						if (result.size()>=1000) {
							zipCodeForm.setMsjeErr("Se encontraron demasiados resultados. Por favor afine su búsqueda agregando otro parámetro de búsqueda con el operador \"+\".");
							result.clear();
						} else if (result.size()<=0) {
							zipCodeForm.setMsjeErr("No se encontraron resultados con este criterio de busqueda.");
						}
					}
					
					request.setAttribute("conjuntoGuias", result);
					
					//obtiene pais
					if (session.getAttribute("zcPaisValue") == null) {						
						resultCombo = dao.fillComboPaises();
						if (resultCombo!= null && !resultCombo.isEmpty()) {
							zipCodeForm.setZcPaisValue((ArrayList)resultCombo.get("value"));
							zipCodeForm.setZcPaisLabel((ArrayList)resultCombo.get("label"));
							session.setAttribute("zcPaisValue", (ArrayList) resultCombo.get("value"));
							session.setAttribute("zcPaisLabel", (ArrayList) resultCombo.get("label"));
						}
					} else {						
						zipCodeForm.setZcPaisValue((ArrayList)session.getAttribute("zcPaisValue"));
						zipCodeForm.setZcPaisLabel((ArrayList)session.getAttribute("zcPaisLabel"));
					}
					
					//obtiene zona
					if(zipCodeForm.getZcPaisValue().size()>0){
						String paisDataValues[] = zipCodeForm.getZcPaisValue().get(0).toString().split("\\|");
						
						if (paisDataValues!=null) {
							if (session.getAttribute("zcZonaValue") == null) {										
								resultCombo = dao.fillComboZonas(paisDataValues[0],paisDataValues[1],paisDataValues[2]);
								if (resultCombo!= null && !resultCombo.isEmpty()) {
									zipCodeForm.setZcZonaValue((ArrayList)resultCombo.get("value"));
									zipCodeForm.setZcZonaLabel((ArrayList)resultCombo.get("label"));
									session.setAttribute("zcZonaValue", (ArrayList) resultCombo.get("value"));
									session.setAttribute("zcZonaLabel", (ArrayList) resultCombo.get("label"));
								}
							} else {								
								zipCodeForm.setZcZonaValue((ArrayList)session.getAttribute("zcZonaValue"));
								zipCodeForm.setZcZonaLabel((ArrayList)session.getAttribute("zcZonaLabel"));
							}
						}
					}
					
					//obtiene estados
					if(zipCodeForm.getZcZonaValue().size()>0){
						String zonaDataValues[] = zipCodeForm.getZcZonaValue().get(0).toString().split("\\|");
						
						if (zonaDataValues!=null) {
							if (session.getAttribute("zcEstadoValue") == null) {								
								resultCombo = dao.fillComboEstados(zonaDataValues[0],zonaDataValues[1],zonaDataValues[2]);
								if (resultCombo!= null && !resultCombo.isEmpty()) {
									zipCodeForm.setZcEstadoValue((ArrayList)resultCombo.get("value"));
									zipCodeForm.setZcEstadoLabel((ArrayList)resultCombo.get("label"));
									session.setAttribute("zcEstadoValue", (ArrayList) resultCombo.get("value"));
									session.setAttribute("zcEstadoLabel", (ArrayList) resultCombo.get("label"));
								}
							} else {
								
								zipCodeForm.setZcEstadoValue((ArrayList)session.getAttribute("zcEstadoValue"));
								zipCodeForm.setZcEstadoLabel((ArrayList)session.getAttribute("zcEstadoLabel"));
							}
						}
					}
				} else if (zipCodeForm.getCurrentTask().equals("selectEstado")) {					
					
					//obtiene municipio
					if(zipCodeForm.getZcEstadoValue().size()>0) {
						String estadoDataValues[] = zipCodeForm.getZcEstado().split("\\|");
						
						if (zipCodeForm.getZcEstado().equals("SEL")) {
							if (zipCodeForm.getZcDelMunValue().size()>0) {
								zipCodeForm.getZcDelMunValue().clear();
								zipCodeForm.getZcDelMunLabel().clear();
								zipCodeForm.setZcDelMunValue("SEL");
								zipCodeForm.setZcDelMunLabel("BUSQUE ESTADO");
								session.setAttribute("zcDelMunValue", zipCodeForm.getZcDelMunValue());
								session.setAttribute("zcDelMunLabel", zipCodeForm.getZcDelMunLabel());
							}
							
							if (zipCodeForm.getZcCiuPobValue().size()>0) {
								zipCodeForm.getZcCiuPobValue().clear();
								zipCodeForm.getZcCiuPobLabel().clear();
								zipCodeForm.setZcCiuPobValue("SEL");
								zipCodeForm.setZcCiuPobLabel("BUSQUE DELEGACION / MUNICIPIO");	
								session.setAttribute("zcCiuPobValue", zipCodeForm.getZcCiuPobValue());
								session.setAttribute("zcCiuPobLabel", zipCodeForm.getZcCiuPobLabel());
							}
							
						} else {
							if (estadoDataValues!=null) {
								resultCombo = dao.fillComboDeleMuni(estadoDataValues[0],estadoDataValues[1],estadoDataValues[2]);
								if (resultCombo!= null && !resultCombo.isEmpty()) {
									zipCodeForm.setZcDelMunValue((ArrayList)resultCombo.get("value"));
									zipCodeForm.setZcDelMunLabel((ArrayList)resultCombo.get("label"));
									session.setAttribute("zcDelMunValue", (ArrayList) resultCombo.get("value"));
									session.setAttribute("zcDelMunLabel", (ArrayList) resultCombo.get("label"));
								}
							}
							//alimenta combo ciudades de todo el estado
							resultCombo = dao.fillComboCiuMunByEdo(estadoDataValues[0],estadoDataValues[1],estadoDataValues[2]);
							
							if (resultCombo!= null && !resultCombo.isEmpty()) {
								zipCodeForm.setZcCiuPobValue((ArrayList)resultCombo.get("value"));
								zipCodeForm.setZcCiuPobLabel((ArrayList)resultCombo.get("label"));
								session.setAttribute("zcCiuPobValue", (ArrayList) resultCombo.get("value"));
								session.setAttribute("zcCiuPobLabel", (ArrayList) resultCombo.get("label"));
							}	
						}						
					}					
					request.setAttribute("conjuntoGuias", result);					
				} else if (zipCodeForm.getCurrentTask().equals("selectDelMun")) {
					if (zipCodeForm.getZcDelMun().equals("SEL")) {
						//alimenta combo ciudades de todo el estado
						String estadoDataValues[] = zipCodeForm.getZcEstado().split("\\|");
						resultCombo = dao.fillComboCiuMunByEdo(estadoDataValues[0], estadoDataValues[1], estadoDataValues[2]);
						
						if (resultCombo!= null && !resultCombo.isEmpty()) {
							zipCodeForm.setZcCiuPobValue((ArrayList)resultCombo.get("value"));
							zipCodeForm.setZcCiuPobLabel((ArrayList)resultCombo.get("label"));
							session.setAttribute("zcCiuPobValue", (ArrayList) resultCombo.get("value"));
							session.setAttribute("zcCiuPobLabel", (ArrayList) resultCombo.get("label"));
						}
					} else {
						if(zipCodeForm.getZcDelMunValue().size()>0){
							String delMunDataValues[] = zipCodeForm.getZcDelMun().split("\\|");
							
							if (delMunDataValues!=null) {
								resultCombo = dao.fillComboCiuPob(delMunDataValues[0],delMunDataValues[1],delMunDataValues[2]);
								if (resultCombo!= null && !resultCombo.isEmpty()) {
									zipCodeForm.setZcCiuPobValue((ArrayList)resultCombo.get("value"));
									zipCodeForm.setZcCiuPobLabel((ArrayList)resultCombo.get("label"));
									session.setAttribute("zcCiuPobValue", (ArrayList) resultCombo.get("value"));
									session.setAttribute("zcCiuPobLabel", (ArrayList) resultCombo.get("label"));
								}
							}
						}
					}
										
					request.setAttribute("conjuntoGuias", result);
				} else if (zipCodeForm.getCurrentTask().equals("fastFind")) {
					String col = "";
					String loc = "";
					String edo = "";
					String findArray[] = null;
					int busqueda = 0;
					
					String find = zipCodeForm.getFind() == null ? "" : zipCodeForm.getFind().toUpperCase();
					if (find.length()>0 && find.length()==5  ) {
						int zipCodeNum = 0;
						try {
							zipCodeNum = Integer.parseInt(find);
						} catch (Exception e) {
							zipCodeNum = 0;
						}
						if (zipCodeNum>0) {							
							busqueda = 1;
						} 					
					} 
					
					//no pudo validar CODIGO POSTAL
					if (busqueda==0) {
						findArray = find.split("\\+");
					
						if (findArray.length == 1) {
							busqueda = 2;//busqueda localidad
						} else if (findArray.length == 2) {
							busqueda = 3;//busqueda localidad + Estado
							loc = findArray[0] == null ? "" : findArray[0].trim();
							edo = findArray[1] == null ? "" : findArray[1].trim();
						} else if (findArray.length >= 3) {
							busqueda = 4;//busqueda colonia + localidad + Estado
							col = findArray[0] == null ? "" : findArray[0].trim();
							loc = findArray[1] == null ? "" : findArray[1].trim();
							edo = findArray[2] == null ? "" : findArray[2].trim();
						}
					}
					
					if (busqueda==1) {//busqueda por codigo postal
						result = dao.getRowsByZipCode(find, zipCodeForm.getSearchType());
					} else if (busqueda==2) {//busqueda localidad + Estado
						result = dao.getRowsByLocalidad(find);
					} else if (busqueda==3) {//busqueda localidad						
						result = dao.getRowsByColLocEst(col, loc, edo);
					} else {
						result = dao.getRowsByColLocEst(col, loc, edo);
					}
					if (result.size()>=1000) {
						zipCodeForm.setMsjeErr("Se encontraron demasiados resultados. Por favor afine su búsqueda agregando otro parámetro de búsqueda con el operador \"+\".");
						result.clear();
					} else if (result.size()<=0) {
						zipCodeForm.setMsjeErr("No se encontraron resultados con este criterio de busqueda.");
					}
					request.setAttribute("conjuntoGuias", result);
				} else if (zipCodeForm.getCurrentTask().equals("findByDetail")) {				
					zipCodeForm.setColonia(zipCodeForm.getColonia().toUpperCase());
					if (!zipCodeForm.getZcCiuPob().equals("SEL")) {
						String valuesDataArray[] = zipCodeForm.getZcCiuPob().split("\\|");
						if (valuesDataArray[0].equals("ALL")) {//BUSCA LA COLONIA POR DESCRIPCION DE CIUDAD EN TODO EL ESTADO
							result = dao.getRowsByCiuEdo(zipCodeForm.getColonia().trim(), valuesDataArray[1], zipCodeForm.getZcEstado());	
						} else {//BUSCA LA COLONIA POR CODIGO ESPECIFICO DE CIUDAD
							result = dao.getRowsByCiudad(zipCodeForm.getColonia().trim(), zipCodeForm.getZcCiuPob());
						}						
					} else if (!zipCodeForm.getZcDelMun().equals("SEL")) {
						result = dao.getRowsByMunicipio(zipCodeForm.getColonia().trim(), zipCodeForm.getZcDelMun());
					} else if (!zipCodeForm.getZcEstado().equals("SEL")) {
						result = dao.getRowsByEstado(zipCodeForm.getColonia().trim(), zipCodeForm.getZcEstado());
					}
					if (result.size()>=1000) {
						zipCodeForm.setMsjeErr("Se encontraron demasiados resultados. Por favor afine su búsqueda agregando otro parámetro de búsqueda con el operador \"+\".");
						result.clear();
					} else if (result.size()<=0) {
						zipCodeForm.setMsjeErr("No se encontraron resultados con este criterio de busqueda.");
					}
					request.setAttribute("conjuntoGuias", result);
				}
			}			
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0,concatena.length()).append(msgErr).append("perform()_Error:").append(e).append("\nCausa:").append(e.getCause()).toString());
			e.printStackTrace();
		}
		return mapping.findForward(forward);	
	}
	
	@SuppressWarnings("rawtypes")
	private void cargaCombos(HttpSession session, ZipCodeForm zipCodeForm){
		try {
			if (session.getAttribute("zcPaisValue") == null) {
				zipCodeForm.setZcPaisValue("SEL");
				zipCodeForm.setZcPaisLabel("SELECCIONE");
			} else {
				zipCodeForm.setZcPaisValue((ArrayList)session.getAttribute("zcPaisValue"));
				zipCodeForm.setZcPaisLabel((ArrayList)session.getAttribute("zcPaisLabel"));
			}
			if (session.getAttribute("zcZonaValue") == null) {
				zipCodeForm.setZcZonaValue("SEL");
				zipCodeForm.setZcZonaLabel("BUSQUE PAIS");
			} else {
				zipCodeForm.setZcZonaValue((ArrayList)session.getAttribute("zcZonaValue"));
				zipCodeForm.setZcZonaLabel((ArrayList)session.getAttribute("zcZonaLabel"));
			}
			
			if (session.getAttribute("zcEstadoValue") == null) {
				zipCodeForm.setZcEstadoValue("SEL");
				zipCodeForm.setZcEstadoLabel("BUSQUE ZONA");
			} else {
				zipCodeForm.setZcEstadoValue((ArrayList)session.getAttribute("zcEstadoValue"));
				zipCodeForm.setZcEstadoLabel((ArrayList)session.getAttribute("zcEstadoLabel"));
			}
			
			if (session.getAttribute("zcDelMunValue") == null) {
				zipCodeForm.setZcDelMunValue("SEL");
				zipCodeForm.setZcDelMunLabel("BUSQUE ESTADO");				
			} else {
				zipCodeForm.setZcDelMunValue((ArrayList)session.getAttribute("zcDelMunValue"));
				zipCodeForm.setZcDelMunLabel((ArrayList)session.getAttribute("zcDelMunLabel"));
			}
			
			if (session.getAttribute("zcCiuPobValue") == null) {
				zipCodeForm.setZcCiuPobValue("SEL");
				zipCodeForm.setZcCiuPobLabel("BUSQUE DELEGACION / MUNICIPIO");
			} else {
				zipCodeForm.setZcCiuPobValue((ArrayList)session.getAttribute("zcCiuPobValue"));
				zipCodeForm.setZcCiuPobLabel((ArrayList)session.getAttribute("zcCiuPobLabel"));
			}
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0,concatena.length()).append(msgErr).append("cargaCombos()_Error:").append(e).append("\nCausa:").append(e.getCause()).toString());
			e.printStackTrace();
		}
	}
}
