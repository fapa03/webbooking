package beanAction;


/**
 * Source code
 * Author Angshuman Debnath
 * Created 2/3/2003
 */



import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import logger.AccessLog;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import bean.Global;
import bean.Resources;
import beanForm.JavGuiaCancellationForm;
import beanUtil.ConnectDB;


public class JavGuiaCancellationAction extends Action {
	private StringBuffer cnct = new StringBuffer();
	private final String msgAvi   = cnct.append("MSAVI_B_").append(this.getClass().getName()).append(".").toString();
	private final String msgError = cnct.append("MSERR_B_").append(this.getClass().getName()).append(".").toString();
	private Resources resources = new Resources();
	/**
	 * This is the method called on by ActionServlet
	 * when a request is made.
	 */
	public ActionForward perform(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		JavGuiaCancellationForm gcnclFrm = (JavGuiaCancellationForm) form;
		
		try {
			if (request.getSession(false) == null) {
				mapping.findForward("nosession");
			}

			if (gcnclFrm.getUseraction() != null && gcnclFrm.getUseraction().equals("validate_form")) {
				invokeFormNumValidityCheck(gcnclFrm, request);
			} else if (gcnclFrm.getUseraction() != null && gcnclFrm.getUseraction().equals("Cancellation")) {// Confirm Cancellation
				invokeGuiaCancellation(gcnclFrm, request);
			}
				
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgError).append("ActionForward()Error:").append(e).toString());
			e.printStackTrace();
		}
		return mapping.findForward("thispage");
	} // End perform
	
	
	/**
	 * This is the method called on by perform method
	 * when a request is made.ie if 
	 * JavGuiaCancellationForm.getUseraction().equals("validate_form"))
	 * 
	 */
	
	public String invokeFormNumValidityCheck( JavGuiaCancellationForm gcnclFrm, 
											  HttpServletRequest request){
		Connection conn = null;
		CallableStatement cstmt = null;
		PreparedStatement pstmt = null;
		ResultSet rs2 = null;
		
		try{
			conn = ConnectDB.getConnection();
			cstmt = conn.prepareCall("call pack_web.pro_ftch_guia_for_canc_DE (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

			String strTemp1 = request.getSession(false).getAttribute("sClientId") == null ? "" : request.getSession(false).getAttribute("sClientId").toString();
			String strTemp2 = request.getSession(false).getAttribute("sAssignedBranch") == null ? "" : request.getSession(false).getAttribute("sAssignedBranch").toString();
			//String strTemp3 = request.getSession(false).getAttribute("sSiteId").toString();
			boolean isFrmNoNull = gcnclFrm.getFormnumber().equals("");

			cstmt.setString(1, strTemp1);											//orgn_client_id
			cstmt.setString(2, strTemp2);											//orgn_brnc_id
			if(isFrmNoNull) {
				cstmt.setNull(3,Types.VARCHAR);
			} else {
				cstmt.setString(3, gcnclFrm.getFormnumber());						//form_no
			}
			cstmt.setString(4, gcnclFrm.getGuiaNumber());							//guia_no

			//AccessLog.Log("GUIA CANCELLATION CLIENT ID :------"+strTemp1);
			//AccessLog.Log("GUIA CANCELLATION BRANCH ID :------"+request.getSession(false).getAttribute("sAssignedBranch"));
			//AccessLog.Log("GUIA CANCELLATION FORMID ID :------"+gcnclFrm.getFormnumber());

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
			
			cstmt.executeQuery();			
				
			request.setAttribute("errorMessageText", cstmt.getString(10));

			if( cstmt.getString(10) == null) {	
				request.setAttribute("PrepBrncId",  cstmt.getString(2));
				gcnclFrm.setFormnumber(				cstmt.getString(3));
				gcnclFrm.setGuiaNumber(				cstmt.getString(4));
				request.setAttribute("DestBrncName",cstmt.getString(5));
				request.setAttribute("DestClntName",cstmt.getString(6));
				request.setAttribute("IssueDate",	cstmt.getString(7));
				request.setAttribute("GuiaAmount",	"$"+cstmt.getDouble(8));
				request.setAttribute("DestSiteName",cstmt.getString(11));
				gcnclFrm.setDocuType(				cstmt.getString(12));
				
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
				request.setAttribute("mrbServiceInfo", mrbServiceInfo);					
			}
			
			
		}catch(Exception e){
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgError).append("invokeFormNumValidityCheck()Error:").append(e).toString());
			e.printStackTrace();
			request.setAttribute("errorMessageText", e.getMessage());
		}finally{
			resources.closeResources(rs2, pstmt, cstmt);
			resources.cerrarResultSet(rs2);
			resources.cerrarConexion(conn);
		}		
		return "";		
	}
	
	
	/**
	 * This is the method called on by perform method
	 * when a request is made.ie if 
	 * JavGuiaCancellationForm.getUseraction().equals("validate_form"))
	 * 
	 */
	public void invokeGuiaCancellation( JavGuiaCancellationForm gcnclFrm, HttpServletRequest request) {
		Connection conn = null;
		CallableStatement cstmt = null;
		HttpSession session = request.getSession();
		String msje = "";
		try {
			String clienteOrigen = (String) session.getAttribute("sClientId");
			Global global = (Global) session.getAttribute("sGlobal");
			conn = ConnectDB.getConnection();
			
//			cstmt = conn.prepareCall("call pack_web.pro_del_guia (?, ?, ?)");
//			cstmt.setString(1, gcnclFrm.getGuiaNumber());			//guia_no
//			cstmt.registerOutParameter(2, java.sql.Types.VARCHAR);  //mesg_type
//			cstmt.registerOutParameter(3, java.sql.Types.VARCHAR);  //mesg_text
//			
//			cstmt.executeQuery();
//			
//			request.setAttribute("errorMessageText", cstmt.getString(3));
			
			if (gcnclFrm.getDocuType().equals("P")){
				if (cancelarGUIA_PP(conn, gcnclFrm)>0) {
					msje = getError(conn,"BOK", 900177, "");
				} else {
					msje = "Fallo en la Cancelaci�n de la Gu�a";
				}
			} else {
				cancelarBOK_GUIA_SRVC(conn, gcnclFrm);
				insertToBokGuiaStatus(conn,gcnclFrm,clienteOrigen);
				cancelarBOK_GUIA_HEAD(conn, gcnclFrm);
				
				/*busca clave de autorizacion para zona extendida en BOK_EXT_DETL.
				 * Si encuentra registro, vuelve a poner disponible la clave para volverla a utilizar*/
				activateEspExtSrvc(conn, gcnclFrm, global);
				
				//BORRAR REFERENCIA DE ACUSE XT
				//System.out.println("SE borra la referencia de la guia XT");
				String desactiva = "{call PACK_ACUSES_XT.PRO_BORRA_GUI_RET(?)}";
				cstmt = conn.prepareCall(desactiva);
				cstmt.setString(1, gcnclFrm.getGuiaNumber());
				cstmt.executeQuery();
				cstmt.close();
				
//			   if (cancelarBOK_GUIA_SRVC(conn, gcnclFrm)>0) {
//				   if (insertToBokGuiaStatus(conn,gcnclFrm,clienteOrigen)>0) {
//					   if (cancelarBOK_GUIA_HEAD(conn, gcnclFrm)>0) {
//				   		   doCommit = true;
//						   msje = getError(conn,"BOK", 900177, "");
//					   } else {
//						   //msje = "Fallo en la Actualizacion del nuevo estatus para la guia a cancelar";						   
//						   AccessLog.Log(cnct.delete(0,cnct.length()).append(msgAvi)
//								   .append("invokeGuiaCancellation()Rastreo:")
//								   .append(gcnclFrm)
//								   .append("Fallo en la Actualizacion del nuevo estatus para la guia a cancelar").toString());
//						   doCommit = true;
//					   }
//				   } else {
//					   //msje = "Fallo en la inserci�n del nuevo estatus";
//					   AccessLog.Log(cnct.delete(0,cnct.length()).append(msgAvi)
//							   .append("invokeGuiaCancellation()Rastreo:")
//							   .append(gcnclFrm)
//							   .append("Fallo en la inserci�n del nuevo estatus").toString());
//					   doCommit = true;
//				   }
//			   } else {
//				  //msje = "Fallo en la cancelaci�n de los servicios";
//				   AccessLog.Log(cnct.delete(0,cnct.length()).append(msgAvi)
//						   .append("invokeGuiaCancellation()Rastreo:")
//						   .append(gcnclFrm)
//						   .append("Fallo en la cancelaci�n de los servicios").toString());
//				   doCommit = true;
//			   }
			}
			conn.commit();	
//			if (doCommit) {
//				conn.commit();	
//			} else {
//				conn.rollback();
//			}
			request.setAttribute("errorMessageText", msje);
			gcnclFrm.setFormnumber("");
			gcnclFrm.setGuiaNumber("");
			gcnclFrm.setUseraction("");
			
			
		}catch(Exception e){
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgError).append("invokeGuiaCancellation()Error:").append(e).toString());
			e.printStackTrace();
		}finally{
			resources.cerrarCallableStatement(cstmt);
			resources.cerrarConexion(conn);
		}
	}

	public int cancelarGUIA_PP(Connection conn, JavGuiaCancellationForm gcnclFrm) {
		PreparedStatement pst = null;
		int update = 0;
		CallableStatement cstmt = null;
		try {
			pst = conn.prepareStatement("DELETE FROM BOK_GUIA_HEAD WHERE GH_GUIA_NO = ?");
			pst.setString(1,gcnclFrm.getGuiaNumber());
			pst.executeUpdate();
			pst.close();
			
			// BORRAR BOK_GUIA_ADDR
			pst = conn.prepareStatement("DELETE FROM BOK_GUIA_ADDR WHERE GA_GUIA_NO = ?");			
			pst.setString(1,gcnclFrm.getGuiaNumber());
			pst.executeUpdate();
			pst.close();

			// BORRAR BOK_GUIA_SRVC
			pst = conn.prepareStatement("DELETE FROM BOK_GUIA_SRVC WHERE GS_GUIA_NO = ?");			
			pst.setString(1,gcnclFrm.getGuiaNumber());
			pst.executeUpdate();
			pst.close();
			
			// BORRAR BOK_GUIA_SRVC_ITEM
			pst = conn.prepareStatement("DELETE FROM BOK_GUIA_SRVC_ITEM WHERE GL_GUIA_NO = ?");			
			pst.setString(1,gcnclFrm.getGuiaNumber());
			pst.executeUpdate();
			pst.close();
			
			// BORRAR TABLA DE REQUERIMIENTOS   BOK_GUIA_SERV_ITEM_REQ
			pst = conn.prepareStatement("DELETE FROM BOK_GUIA_SRVC_REQM WHERE GR_GUIA_NO = ?");			
			pst.setString(1,gcnclFrm.getGuiaNumber());
			pst.executeUpdate();
			pst.close();
			
			// BORRAR BOK_GUIA_STUS
			pst = conn.prepareStatement("DELETE FROM BOK_GUIA_STUS WHERE GS_GUIA_NO = ?");			
			pst.setString(1,gcnclFrm.getGuiaNumber());
			pst.executeUpdate();
			pst.close();
			
			// BORRAR BOK_EXT_DETL
			pst = conn.prepareStatement("DELETE FROM BOK_EXT_DETL WHERE GZ_GUIA_NO = ?");			
			pst.setString(1,gcnclFrm.getGuiaNumber());
			pst.executeUpdate();
			pst.close();
			
			// BORRAR WEB_GUIA_REFR
			pst = conn.prepareStatement("DELETE FROM WEB_GUIA_REFR WHERE GR_GUIA_NO = ?");			
			pst.setString(1,gcnclFrm.getGuiaNumber());
			pst.executeUpdate();
			pst.close();
			
			// BORRAR WEB_CONTROL_EMAIL
			pst = conn.prepareStatement("DELETE FROM WEB_CTRL_EMAIL WHERE CE_GUIA_NO = ?");			
			pst.setString(1,gcnclFrm.getGuiaNumber());
			pst.executeUpdate();
			pst.close();
						
			//BORRAR PPG_BOK_GUIA_ADDR
			pst = conn.prepareStatement("DELETE FROM PPG_BOK_GUIA_ADDR WHERE BGA_REF_NO = ?");
			pst.setString(1,gcnclFrm.getGuiaNumber());
			pst.executeUpdate();
			pst.close();

			// ACTUALIZAR PPG_BOK_GUIA_DTL (FECHA DE CONVERSION NULL) GUIAS SECUNDARIAS, GUIA PRINCIPAL
			pst = conn.prepareStatement("UPDATE PPG_BOK_GUIA_DTL SET BGD_CONV_DATE = NULL, BGM_DEST_BRN_ID = NULL, BGM_DEST_SITE_ID = NULL, BGM_DEST_CLNT_ID = NULL, BGD_GH_CONTENT = NULL WHERE BGD_TRAC_NO IN (SELECT GR_GUIA_REL FROM BOK_GUIA_REL WHERE GR_GUIA_NO = ?)");			
			pst.setString(1,gcnclFrm.getGuiaNumber());
			pst.executeUpdate();
			pst.close();
			
			// ACTUALIZAR PPG_BOK_GUIA_DTL (FECHA DE CONVERSION NULL) GUIA PRINCIPAL
			pst = conn.prepareStatement("UPDATE PPG_BOK_GUIA_DTL SET BGD_CONV_DATE = NULL, BGM_DEST_BRN_ID = NULL, BGM_DEST_SITE_ID = NULL, BGM_DEST_CLNT_ID = NULL, BGD_GH_CONTENT = NULL WHERE BGD_TRAC_NO = ?");			
			pst.setString(1,gcnclFrm.getGuiaNumber());
			pst.executeUpdate();
			pst.close();
			
			// BORRAR BOK_GUIA_HEAD_EXTRA
			pst = conn.prepareStatement("DELETE FROM BOK_GUIA_HEAD_EXTRA WHERE GE_GUIA_NO = ?");			
			pst.setString(1,gcnclFrm.getGuiaNumber());
			pst.executeUpdate();
			pst.close();

			// BORRAR CTRL_LABEL_PRN
			pst = conn.prepareStatement("DELETE FROM CTRL_LABEL_PRN WHERE CL_GUIA_NO = ?");			
			pst.setString(1,gcnclFrm.getGuiaNumber());
			pst.executeUpdate();
			pst.close();
			
			// BORRAR CTRL_LABEL_PRN
			pst = conn.prepareStatement("DELETE FROM BOK_GUIA_REL WHERE GR_GUIA_NO = ?");			
			pst.setString(1,gcnclFrm.getGuiaNumber());
			pst.executeUpdate();
			pst.close();
			
			//BORRAR REFERENCIA DE ACUSE XT
			//System.out.println("XT CANCELADA");
			String desactiva = "{call PACK_ACUSES_XT.PRO_BORRA_GUI_RET(?)}";
			cstmt = conn.prepareCall(desactiva);
			cstmt.setString(1, gcnclFrm.getGuiaNumber());
			cstmt.executeQuery();
			cstmt.close();
			
			update = 1;
			
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgError).append("cancelarGUIA_PP()Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.cerrarPreparedStatement(pst);
			resources.cerrarCallableStatement(cstmt);
		}
		return update;
	}
	
	public int cancelarBOK_GUIA_SRVC(Connection conn, JavGuiaCancellationForm gcnclFrm) {
		PreparedStatement pst = null;
		int update = 0;
		try {
			pst = conn.prepareStatement("UPDATE BOK_GUIA_SRVC SET GS_STUS_FLAG = ? WHERE GS_GUIA_NO = ? AND GS_GUIA_TYPE = ? AND GS_DOCU_TYPE = ?");
			pst.setString(1,"I");
			pst.setString(2, gcnclFrm.getGuiaNumber());
			pst.setString(3, "H");
			pst.setString(4, "Q");
			
			update = pst.executeUpdate();			

		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgError).append("cancelarBOK_GUIA_SRVC()Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.cerrarPreparedStatement(pst);
		}
		return update;
	}
	
	public int cancelarBOK_GUIA_HEAD(Connection conn, JavGuiaCancellationForm gcnclFrm) {
		PreparedStatement pst = null;
		int update = 0;
		try {
			pst = conn.prepareStatement("UPDATE BOK_GUIA_HEAD SET GH_GUIA_STUS = ?, GH_ACTV_FLAG = ? WHERE GH_GUIA_NO = ? AND GH_GUIA_TYPE = ? AND GH_DOCU_TYPE = ?");
			pst.setString(1,"CWB");
			pst.setString(2,"I");
			pst.setString(3, gcnclFrm.getGuiaNumber());
			pst.setString(4, "H");
			pst.setString(5, "Q");
			
			update = pst.executeUpdate();
			
			if (pst!=null)
				pst.close();
			
			pst = conn.prepareStatement("DELETE FROM WEB_CTRL_EMAIL WHERE CE_GUIA_NO = ?");
			pst.setString(1,gcnclFrm.getGuiaNumber());
			
			pst.executeUpdate();
			
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgError).append("cancelarBOK_GUIA_HEAD()Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.cerrarPreparedStatement(pst);
		}
		return update;
	}
	
	public int activateEspExtSrvc(Connection conn, JavGuiaCancellationForm gcnclFrm, Global global) {
		PreparedStatement pst = null;
		ResultSet rst = null;
		int update = 0;
		String cveAutserv = "";
		try {
			pst = conn.prepareStatement("SELECT GZ_REFER_AUT_T7 FROM BOK_EXT_DETL WHERE GZ_GUIA_NO = ? AND GZ_REFER_AUT_T7 IS NOT NULL");
			pst.setString(1, gcnclFrm.getGuiaNumber());
			
			rst = pst.executeQuery();
			
			if (rst.next()) {
				cveAutserv = rst.getString(1) == null ? "" : rst.getString(1).trim(); 
			}
			
			resources.closeResources(rst, pst);
			
			if (cveAutserv.trim().length()>0) {
				pst = conn.prepareStatement("UPDATE OL_REFER_AUT SET RA_REFER_STUS = ?, MDFD_BY = ?, MDFD_ON = SYSDATE WHERE RA_REFER_ID = ?");
				pst.setString(1, "A");
				pst.setString(2, global.getOrigenUserClave());
				pst.setString(3, cveAutserv);				
				
				update = pst.executeUpdate();	
			}			
			
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgError).append("activateEspExtSrvc()Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.closeResources(rst, pst);
		}
		return update;
	}
	
	public int insertToBokGuiaStatus(Connection con,
			JavGuiaCancellationForm gcnclFrm, String webClientId) {
		String query = "";
		PreparedStatement pst = null;
		ResultSet rs = null;
		int guisinsCount = 0;
		int serialNumber = 0;
		String strGuiaLocation = null;
		String strGuiaDest = null;
		
		try {
			query = "select GH_GUIA_NO,GH_CURR_LOCA,GH_CURR_DEST FROM BOK_GUIA_HEAD WHERE GH_GUIA_NO = ? AND GH_GUIA_TYPE = ? AND GH_ACTV_FLAG = ?";
			
			pst = con.prepareStatement(query);
			pst.setString(1, gcnclFrm.getGuiaNumber());
			pst.setString(2, "H");
			pst.setString(3, "A");
			
			rs = pst.executeQuery();
			
			if (rs.next()) {
				strGuiaLocation = rs.getString("GH_CURR_LOCA") == null ? "" : rs.getString("GH_CURR_LOCA");
				strGuiaDest = rs.getString("GH_CURR_DEST") == null ? "" : rs.getString("GH_CURR_DEST");
			}
			
			rs.close();
			pst.close();

			String q = "";
			String serialNumberQuery = "";

			q = cnct.delete(0,cnct.length())
					.append("INSERT INTO BOK_GUIA_STUS(GS_GUIA_NO, ")
					.append("GS_STUS_CODE, GS_SERL_NO, ")
					.append("CRTD_ON, CRTD_BY, ")
					.append("MDFD_ON, MDFD_BY, GS_CTT_TRNS_TYPE, ")
					.append("GS_CURR_LOCA, GS_CURR_DEST)")
					.append("VALUES(?,?,?,SYSDATE,?,SYSDATE,?,?,?,?)").toString();
			
			// get Max of Serial Number....from Bok Guia Status...
			serialNumberQuery = "SELECT NVL(MAX(GS_SERL_NO),0)+1 FROM BOK_GUIA_STUS WHERE GS_GUIA_NO = ?";
			
			pst = con.prepareStatement(serialNumberQuery);
			pst.setString(1, gcnclFrm.getGuiaNumber());
			rs = pst.executeQuery();

			if (rs != null && rs.next()) {
				serialNumber = rs.getInt(1);
			}
			
			if (rs != null) {
				rs.close();	
			}			
			pst.close();

			pst = con.prepareStatement(q);

			pst.setString(1, gcnclFrm.getGuiaNumber());
			pst.setString(2, "CWB");
			pst.setInt(3, serialNumber);
			pst.setString(4, webClientId);
			pst.setString(5, webClientId);
			pst.setString(6, "CWB");
			pst.setString(7, strGuiaLocation);
			pst.setString(8, strGuiaDest);

			guisinsCount = pst.executeUpdate();
			
			pst.close();			

		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgError).append("insertToBokGuiaStatus()Error:").append(e).toString());
			e.printStackTrace();		
		} finally {
			resources.closeResources(rs, pst);
		}
		return guisinsCount;
	}
	
	// By Susheel for getting the Errror message by calling the procedure
	private String getError(Connection con, String modulo, int msjeID, String tagID) {
		CallableStatement cst = null;
		String msgtext = "";

		try {
			cst = con.prepareCall("{ call pack_web.PRO_SHOW_MESG(?,pack_web.language_id,?,1,?,NULL,NULL,?,?) }	");
			//PRO_SHOW_MESG('BOK',LANGUAGE_ID,900177,1,NULL,NULL,NULL,mesg_type,mesg_text);
			cst.setString(1, modulo);//WEB
			cst.setInt(2, msjeID);//900177
			cst.setString(3, tagID);//complemento de mensaje			
			cst.registerOutParameter(4, Types.VARCHAR);
			cst.registerOutParameter(5, Types.VARCHAR);

			cst.executeQuery();
			
			msgtext = (cst.getString(5) != null ? cst.getString(5) : "");
			
		} catch (Exception exe) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgError).append("getError()Error:").append(exe).toString());
			exe.printStackTrace();
		} finally {
			resources.cerrarCallableStatement(cst);
		}
		return msgtext;
	}
} //End class
