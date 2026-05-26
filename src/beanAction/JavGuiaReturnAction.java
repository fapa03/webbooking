package beanAction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.paquetexpress.core.web.util.MessageApi;
import com.paquetexpress.www.webbooking.GuiasRetorno.retorno.Mensaje;
import com.paquetexpress.www.webbooking.GuiasRetorno.retorno.RastreoRetorno;

import bean.Global;
import bean.GuiasRetorno;
import bean.Resources;
import beanForm.JavGuiaReturnForm;
import beanUtil.ConnectDB;
import beanUtil.InitPropertiesInfoNotificaciones;
import logger.AccessLog;
import mx.com.paquetexpress.dto.EmailNotificationDTO;
import mx.com.paquetexpress.dto.SmsNotificationDTO;
import mx.com.paquetexpress.dto.message.body.response.Response;

public class JavGuiaReturnAction extends Action {

	private StringBuffer cnct = new StringBuffer();
	private final String msgError = cnct.append("MSERR_B_").append(this.getClass().getName()).append(".").toString();
	private Resources resources = new Resources();
	/**
	 * This is the method called on by ActionServlet
	 * when a request is made.
	 */
	public ActionForward perform(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		JavGuiaReturnForm gcnclFrm = (JavGuiaReturnForm) form;
		
		try {
			if (request.getSession(false) == null) {
				mapping.findForward("nosession");
			}
			if (gcnclFrm.getUseraction() != null) {
				if (gcnclFrm.getUseraction().equals("validate_form")) {
					invokeFormNumValidityCheck(gcnclFrm, request);
				} else if (gcnclFrm.getUseraction().equals("GenerateGuideRR")) {
					invokeGuiaRR(gcnclFrm, request);
				}else if(gcnclFrm.getUseraction().equals("notificacion_form")){
					seterRequest(request, gcnclFrm);
					boolean continuar = true;
					if((gcnclFrm.getEmailDest() == null || (gcnclFrm.getEmailDest() != null && gcnclFrm.getEmailDest().isEmpty() ))
							&& (gcnclFrm.getPhoneDest()== null || (gcnclFrm.getPhoneDest() != null && gcnclFrm.getPhoneDest().isEmpty() ))){
						continuar = false;
					}
					
					if (continuar) {
						String cad = "";
						if (gcnclFrm.getEmailDest() != null && !gcnclFrm.getEmailDest().isEmpty()) {
							sendToEmailSolitudes(gcnclFrm);
							cad = gcnclFrm.getEmailDest();
						}
						if (gcnclFrm.getPhoneDest() != null && !gcnclFrm.getPhoneDest().isEmpty()) {
							sendToSMS(gcnclFrm);
							if (!cad.isEmpty()) {
								cad += ", ";
							}
							cad += gcnclFrm.getPhoneDest();
						}
						request.setAttribute("errorMessageText", "SE ENVIO UNA NOTIFICACION DE LA GUIA DE RETORNO CON LA INFORMACION INGRESADA EMAIL/TELEFONO: " + cad);
					} else {
						request.setAttribute("errorMessageText", "Favor de ingresar un email o teléfono celular");
					}
				}
			}
				
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgError).append("ActionForward()Error:").append(e).toString());
			e.printStackTrace();
		}
		return mapping.findForward("thispage");
	} // End perform

	public String invokeFormNumValidityCheck( JavGuiaReturnForm gcnclFrm, HttpServletRequest request){
		Connection conn = null;
		CallableStatement cstmt = null;
		PreparedStatement pstmt = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		
		try{
			conn = ConnectDB.getConnection();
			cstmt = conn.prepareCall("call pack_web.pro_ftch_guia_for_generate_RR (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

			String strTemp1 = request.getSession(false).getAttribute("sClientId").toString();
			String strTemp2 = request.getSession(false).getAttribute("sAssignedBranch").toString();
			
			boolean isFrmNoNull = true;

			cstmt.setString(1, strTemp1);										//orgn_client_id
			cstmt.setString(2, strTemp2);											//orgn_brnc_id
			if(isFrmNoNull) {
				cstmt.setNull(3,Types.VARCHAR);
			} else {
				cstmt.setString(3, "");						//form_no
			}
			cstmt.setString(4, gcnclFrm.getGuiaNumber());							//guia_no


			cstmt.registerOutParameter(2, java.sql.Types.VARCHAR); //orgn_brnc_id
			cstmt.registerOutParameter(3, java.sql.Types.VARCHAR); //form_no
			cstmt.registerOutParameter(4, java.sql.Types.VARCHAR); //guia_no
			cstmt.registerOutParameter(5, java.sql.Types.VARCHAR); //brnc_name
			cstmt.registerOutParameter(6, java.sql.Types.VARCHAR); //dest_clnt_name
			cstmt.registerOutParameter(7, java.sql.Types.VARCHAR); //isse_date
			cstmt.registerOutParameter(8, java.sql.Types.NUMERIC); //guia_amnt
			cstmt.registerOutParameter(9, java.sql.Types.VARCHAR); //mesg_type
			cstmt.registerOutParameter(10,java.sql.Types.VARCHAR); //mesg_text
			cstmt.registerOutParameter(11,java.sql.Types.VARCHAR); //mesg_text
			cstmt.registerOutParameter(12,java.sql.Types.VARCHAR); //DocuType
			cstmt.registerOutParameter(13, java.sql.Types.NUMERIC); // mesg_id
			cstmt.registerOutParameter(14, java.sql.Types.VARCHAR); // orgn_site_name
			cstmt.registerOutParameter(15, java.sql.Types.VARCHAR); // orgn_brnc_name
			cstmt.registerOutParameter(16, java.sql.Types.VARCHAR); // orgn_clnt_name
			cstmt.registerOutParameter(17, java.sql.Types.VARCHAR); // guia_no_return
			cstmt.registerOutParameter(18, java.sql.Types.VARCHAR); // dest_brnc_id
			cstmt.registerOutParameter(19, java.sql.Types.VARCHAR); // radFlag
			
			cstmt.executeQuery();
			
			request.setAttribute("errorMessageText", cstmt.getString(10));

			if(cstmt.getString(10) == null) {
				String radFlag ="0", destBrncId, destClntName, orgnClntName;
				radFlag = cstmt.getString(19);
				destBrncId = cstmt.getString(18);
				destClntName = cstmt.getString(6);
				orgnClntName = cstmt.getString(16);
				if(destBrncId != null && destBrncId.contains("70") || radFlag != null && radFlag.equalsIgnoreCase("0")){
					gcnclFrm.setTypeRecoleccion("1");
				}
				//VALIDAR QUE TENGAN EL SERVICIO RAD.
				
				//VALIDAR QUE NO SEA ZONA 70
				gcnclFrm.setPrepBrncId(cstmt.getString(2));
				gcnclFrm.setGuiaNumber(cstmt.getString(4));
				gcnclFrm.setDestBrncName(cstmt.getString(5));
				gcnclFrm.setIssueDate(cstmt.getString(7));
				gcnclFrm.setGuiaAmount("$"+cstmt.getDouble(8));
				gcnclFrm.setDestSiteName(cstmt.getString(11));
				gcnclFrm.setOrgnBrncName(cstmt.getString(15));
				gcnclFrm.setOrgnSiteName(cstmt.getString(14));
				gcnclFrm.setDestBrncId(destBrncId);
				gcnclFrm.setDocuType(cstmt.getString(12));
				gcnclFrm.setGuiaNumberReturn(cstmt.getString(17));
				gcnclFrm.setRadFlag(radFlag);
				gcnclFrm.setOrgnClntName(orgnClntName);
				gcnclFrm.setDestClntName(destClntName);
				
				String queryString = cnct.delete(0,cnct.length()).append("select '<TR><TD align=center	CLASS=\"mrbtd\">'||GL_QUNT||'</TD><TD CLASS=\"mrbtd\">'||pack_web.fun_ftch_ship_desc(gl_desc, gl_refr_srvc_id,gl_srvc_id) ")
									 .append("||'</TD><TD CLASS=\"mrbtd\">'||GL_CONT ||'</TD></TR>' FROM BOK_GUIA_SRVC_ITEM WHERE GL_GUIA_NO =? AND GL_REFR_SRVC_ID IN (SELECT GS_SRVC_ID ")
									 .append("FROM BOK_GUIA_SRVC WHERE GS_SRVC_TYPE=? AND GS_GUIA_NO=?)").toString();
				String mrbServiceInfo = "";
				pstmt = conn.prepareStatement(queryString);
				pstmt.setString(1,gcnclFrm.getGuiaNumber());
				pstmt.setString(2,"S");
				pstmt.setString(3,gcnclFrm.getGuiaNumber());
				
				rs2 = pstmt.executeQuery();
				int reccount = 0;
				while(rs2.next()) {
					mrbServiceInfo += rs2.getString(1);  
					reccount++;
					if(reccount > 5 ) {
						break;
					}
				}
				while( reccount < 6 ) {
					mrbServiceInfo += "<TR><TD CLASS=\"mrbtd\">&nbsp;</TD>       <TD CLASS=\"mrbtd\">&nbsp;</TD>          <TD CLASS=\"mrbtd\">&nbsp;</TD></TR>";
					reccount++;
				}

				gcnclFrm.setMrbServiceInfo(mrbServiceInfo);
				gcnclFrm.setValidado("1");
				if(gcnclFrm.getGuiaNumberReturn() != null && !gcnclFrm.getGuiaNumberReturn().isEmpty()){
					request.setAttribute("errorMessageText", "ESTA GUÍA YA CUENTA CON GUIA DE RETORNO: "+gcnclFrm.getGuiaNumberReturn());
					gcnclFrm.setValidado("0");
				}
				gcnclFrm.setUseraction(null);
				seterRequest(request, gcnclFrm);
			}		
		}catch(Exception e){
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgError).append("invokeFormNumValidityCheck()Error:").append(e).toString());
			e.printStackTrace();
			request.setAttribute("errorMessageText", e.getMessage());
		}finally{
			resources.closeResources(rs1, pstmt, cstmt);
			resources.cerrarResultSet(rs2);
			resources.cerrarConexion(conn);
		}		
		return "";		
	}

	private void seterRequest(HttpServletRequest request, JavGuiaReturnForm gcnclFrm){
		request.setAttribute("PrepBrncId", gcnclFrm.getPrepBrncId());
		gcnclFrm.setGuiaNumber(gcnclFrm.getGuiaNumber());
		request.setAttribute("DestBrncName", gcnclFrm.getDestBrncName());
		request.setAttribute("DestClntName", gcnclFrm.getDestClntName());
		request.setAttribute("IssueDate", gcnclFrm.getIssueDate());
		request.setAttribute("GuiaAmount", gcnclFrm.getGuiaAmount());
		request.setAttribute("DestSiteName", gcnclFrm.getDestSiteName());
		request.setAttribute("OrgnBrncName", gcnclFrm.getOrgnBrncName());
		request.setAttribute("OrgnClntName", gcnclFrm.getOrgnClntName());
		request.setAttribute("OrgnSiteName", gcnclFrm.getOrgnSiteName());
		request.setAttribute("guiaNumberReturn", gcnclFrm.getGuiaNumberReturn());
		request.setAttribute("mrbServiceInfo", gcnclFrm.getMrbServiceInfo());
	}
	
	public void invokeGuiaRR( JavGuiaReturnForm gcnclFrm, HttpServletRequest request) {
		HttpSession session = request.getSession();
		String msje = "";
		try {
			GuiasRetorno guiasRetorno = new GuiasRetorno();
			Global global = (Global)session.getAttribute("sGlobal");
			com.paquetexpress.www.webbooking.GuiasRetorno.retorno.DataResponse dataResponseGuiasRetorno = 
					guiasRetorno.getGuiaRetorno(gcnclFrm.getGuiaNumber(), "", 1, gcnclFrm.getTypeRecoleccion(), gcnclFrm.getHoraInicio(), gcnclFrm.getHoraFinal(), global.getOrigenUserClave());
			RastreoRetorno rastreoRetorno = dataResponseGuiasRetorno.getRetornoSolicitud().getRastreosRetorno()[0];
			if (rastreoRetorno.getMensajes() != null && rastreoRetorno.getMensajes().length > 0) {
				for (Mensaje msj : rastreoRetorno.getMensajes()) {
					if(msj.getCveMsjeRetorno() != 0) {
						msje+=msj.getDesMsjeRetorno();
					}
				}
			}
			if (!msje.isEmpty()) {
				request.setAttribute("errorMessageText", msje);
			} else {
				gcnclFrm.setGuiaNumberReturn(rastreoRetorno.getRastreoNo());
				request.setAttribute("guiaNumberReturn", rastreoRetorno.getRastreoNo());
				request.setAttribute("errorMessageText", "SE GENERO LA GUIA DE RETORNO:"+ rastreoRetorno.getRastreoNo());
				boolean continuar = true;
				if((gcnclFrm.getEmailDest() == null || (gcnclFrm.getEmailDest() != null && gcnclFrm.getEmailDest().isEmpty() ))
						&& (gcnclFrm.getPhoneDest()== null || (gcnclFrm.getPhoneDest() != null && gcnclFrm.getPhoneDest().isEmpty() ))){
					continuar = false;
				}
				
				if(continuar){
					if(gcnclFrm.getEmailDest() != null && !gcnclFrm.getEmailDest().isEmpty()) {
						sendToEmailSolitudes(gcnclFrm);
					}
					if(gcnclFrm.getPhoneDest() != null && !gcnclFrm.getPhoneDest().isEmpty()) {
						sendToSMS(gcnclFrm);
					}
				}
			}
			gcnclFrm.setGuiaNumber("");
			gcnclFrm.setUseraction(null);
			
			
		}catch(Exception e){
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgError).append("invokeGuiaCancellation()Error:").append(e).toString());
			e.printStackTrace();
		}finally{
		}
	}

	private void sendNotification(JavGuiaReturnForm gcnclFrm) {
		try {
		List<List<StructNotification>>  list = new ArrayList<List<StructNotification>>();
		List<StructNotification> listTMP = new ArrayList<StructNotification>();
		StructNotification struct = new StructNotification();
		struct.setRastreo(gcnclFrm.getGuiaNumberReturn());
		struct.setClienteOrigen(gcnclFrm.getDestClntName());
		struct.setClienteDestino(gcnclFrm.getOrgnClntName());
		listTMP.add(struct);
		list.add(listTMP);
		String subject = InitPropertiesInfoNotificaciones.getInstance().getProperty("estructuraEmailParteAsunto");
		subject = subject.replace("@cliente", struct.getClienteOrigen());
        String structMail = buildStructBodyEmail(list, Integer.parseInt(gcnclFrm.getTypeRecoleccion()));
        EmailNotificationDTO dTO = new EmailNotificationDTO();
        dTO.setEmailDestination(gcnclFrm.getEmailDest());
        dTO.setEmailSubject(subject);
        dTO.setEmailMensaje(structMail);
		String urlServer = InitPropertiesInfoNotificaciones.getInstance().getProperty("endPointNotificacion");
		MessageApi messageApi = new MessageApi(urlServer);
		messageApi.executeMessageApi(dTO, "/TypeNotifications/api/notifications/v1/sendEmailNotification", "");
    	Response responseSend =messageApi.getResponse();

        if (!responseSend.getSuccess()) {
        	System.out.println("Error en /TypeNotifications/../sendEmailNotification Response  ");
        	AccessLog.Log(cnct.delete(0,cnct.length()).append(msgError).append("sendNotification()Error: Success = false").append(dTO.toString()).toString());
        }
		} catch (Exception e) {
			// TODO Auto-generated catch block			
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgError).append("sendNotification()Error:").append(e).toString());
			e.printStackTrace();
		}
	}
	
	private void sendNotificationSMS(final JavGuiaReturnForm gcnclFrm) {
		try {
			List<SmsNotificationDTO> list = new ArrayList<SmsNotificationDTO>();
			SmsNotificationDTO struct = null;
			String[] strPhone = null;
			if(gcnclFrm.getPhoneDest().contains(";")) {
				strPhone = gcnclFrm.getPhoneDest().split(";");
			} else {
				strPhone = new String[] {gcnclFrm.getPhoneDest()};
			}
			String mensajeHeader;
			if (gcnclFrm.getTypeRecoleccion().equalsIgnoreCase("1")) {// ENTREGA EN PISO
				mensajeHeader = InitPropertiesInfoNotificaciones.getInstance().getProperty("notificacionSMSPiso");
			} else {// SOLICITUD DE RECOLECCION
				mensajeHeader = InitPropertiesInfoNotificaciones.getInstance().getProperty("notificacionSMSRecoleccion");
			}
			mensajeHeader = mensajeHeader.replace("@guiaRetorno", gcnclFrm.getGuiaNumberReturn());
			for (int i = 0; i < strPhone.length; i++) {
				if (!strPhone[i].isEmpty()) {
					struct = new SmsNotificationDTO();
					struct.setMessage(mensajeHeader);
					struct.setPhone(strPhone[i]);
					list.add(struct);
				}
			}

			String urlServer = InitPropertiesInfoNotificaciones.getInstance().getProperty("endPointNotificacion");
			MessageApi messageApi = new MessageApi(urlServer);
			messageApi.executeMessageApi(list,"/TypeNotifications/api/notifications/v3/sendSMSNotification/ALT","");
			Response responseSend = messageApi.getResponse();

			if (!responseSend.getSuccess()) {
				System.out.println("Error en /TypeNotifications/../sendNotificationSMS Response  ");
				AccessLog.Log(cnct.delete(0, cnct.length()).append(msgError).append("sendNotificationSMS()Error: Success = false").append(list.get(0).toString()).toString());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block			
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgError).append("sendNotificationSMS()Error:").append(e).toString());
			e.printStackTrace();
		}
	}

    /**
    *
    * @param ruta donde se encuentra el HTML a obtener su informacion.
    * @return
    */
   private String getStructHTMLNotification(String ruta) {
       BufferedReader rd = null;
       StringBuilder msjHTML = null;
       InputStreamReader inputStreamReader = null;
       try {
           msjHTML = new StringBuilder();
           inputStreamReader = new InputStreamReader(getClass().getResourceAsStream(ruta), "UTF-8");
           rd = new BufferedReader(inputStreamReader);
           String strLine;
           while ((strLine = rd.readLine()) != null) {
               msjHTML.append(strLine);
           }
           rd.close();
           rd = null;
           inputStreamReader.close();
           inputStreamReader = null;
       } catch (UnsupportedEncodingException ex) {           
           AccessLog.Log(cnct.delete(0,cnct.length()).append(msgError).append("getStructHTMLNotification()Error:").append(ex).toString());
           ex.printStackTrace();
       } catch (IOException ex) {           
           AccessLog.Log(cnct.delete(0,cnct.length()).append(msgError).append("getStructHTMLNotification()Error:").append(ex).toString());
           ex.printStackTrace();
       } finally {
           if (rd != null) {
               try {
                   rd.close();
                   rd = null;
               } catch (IOException ex) {
                   ex.printStackTrace();
               }
           }
           if (inputStreamReader != null) {
               try {
                   inputStreamReader.close();
                   inputStreamReader = null;
               } catch (IOException ex) {
                   ex.printStackTrace();
               }
           }
       }
       return msjHTML.toString();
   }

   public void sendToEmailSolitudes(final JavGuiaReturnForm gcnclFrm) {
       Runnable task = new Runnable() {
           public void run() {
               try {
            	   sendNotification(gcnclFrm);
               } catch (Exception ex) {
                   ex.printStackTrace();
               }
           }
       };
       new Thread(task, "sendToEmailSolitudes").start();
   }
   
   public void sendToSMS(final JavGuiaReturnForm gcnclFrm) {
       Runnable task = new Runnable() {
           public void run() {
               try {
            	   sendNotificationSMS(gcnclFrm);
               } catch (Exception ex) {
                   ex.printStackTrace();
               }
           }
       };
       new Thread(task, "sendToEmailSolitudes").start();
   }
   
   private String buildStructBodyEmail(final List<List<StructNotification>> listStructNotification, final int type) {
		String mensaje = "", mensajeHeader = "";
		if (type == 1) {// ENTREGA EN PISO
			mensajeHeader = "Estimado Cliente, se ha solicitado una guía de retorno:";
		} else {// SOLICITUD DE RECOLECCION
			mensajeHeader = "Estimado Cliente, se ha solicitado una guía de retorno con una solicitud de recolecci�n:";
		}

		StringBuilder body = new StringBuilder(), bodyNotification = new StringBuilder();
		for (int x = 0; x < listStructNotification.size(); x++) {
			String bodyString = "";
			body.delete(0, body.length());
			bodyString = getStructHTMLNotification("/mx/com/paquetexpress/utileria/notification/msjEnvioNotificationRAD.html");
			bodyString = bodyString.replace("@mensajeHeader", mensajeHeader);
			body.append(bodyString);
			bodyNotification.delete(0, bodyNotification.length());
			boolean zebra = true;
			bodyNotification.append(getStructHTMLNotification("/mx/com/paquetexpress/utileria/notification/msjSendNotificationBodyByBatchHeaderRAD.html"));
			String rastreo = "";
			for (StructNotification StructNotification : listStructNotification.get(x)) {
				String bodySendByEvent = getStructHTMLNotification("/mx/com/paquetexpress/utileria/notification/msjSendNotificationBodyByBatchRAD.html");
				bodySendByEvent = bodySendByEvent.replace("@Rastreo", StructNotification.getRastreo());
				bodySendByEvent = bodySendByEvent.replace("@ClienteOrigen", StructNotification.getClienteOrigen());
				bodySendByEvent = bodySendByEvent.replace("@ClienteDestino", StructNotification.getClienteDestino() == null ? "" : StructNotification.getClienteDestino());
				if (rastreo.compareTo("") == 0) {
					rastreo = StructNotification.getRastreo();
				}

				if (zebra) {
					bodySendByEvent = bodySendByEvent.replace("@bgColor", "bgcolor=\"#E4F3FA\"");
				} else {
					bodySendByEvent = bodySendByEvent.replace("@bgColor", "");
				}
				zebra = !zebra;
				bodyNotification.append(bodySendByEvent);
			}

			mensaje = body.toString().replace("@bodyComplet", bodyNotification.toString());
			mensaje = mensaje.replace("@guiaNO", rastreo);
		}
		return mensaje;
   }	// By Susheel for getting the Errror message by calling the procedure

	public class StructNotification implements Serializable {

        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String rastreo;

        private String clienteOrigen;

        private String clienteDestino;


        public String getRastreo() {
            return rastreo;
        }

        public void setRastreo(String rastreo) {
            this.rastreo = rastreo;
        }

        public String getClienteOrigen() {
            return clienteOrigen;
        }

        public void setClienteOrigen(String clienteOrigen) {
            this.clienteOrigen = clienteOrigen;
        }

        public String getClienteDestino() {
            return clienteDestino;
        }

        public void setClienteDestino(String clienteDestino) {
            this.clienteDestino = clienteDestino;
        }
	}
}
