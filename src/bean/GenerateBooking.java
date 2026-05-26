package bean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import logger.AccessLog;
import beanForm.JavWebBookingGeneralMainForm;

public class GenerateBooking {
	private StringBuffer cnct = new StringBuffer();
	private final String msgAvi = new StringBuffer("MSAVI_B_").append(this.getClass().getName()).append(".").toString();
	private final String msgErr = new StringBuffer("MSERR_B_").append(this.getClass().getName()).append(".").toString();
	
	@SuppressWarnings("rawtypes")
	public String generate(JavWebBookingGeneralMainForm aform, Connection con,
			HttpServletRequest req, HttpSession session, Global global) {
		String returnPage = "";
		String clientId = aform.getOrgienclave();
		GenerateGuia genGuia = new GenerateGuia();
		try {
			//AAP//AccessLog.Log(cnct.delete(0,cnct.length()).append(msgAvi).append("generate()_").append(clientId).append("..................................................").toString());
			ArrayList servicesDetailArray = (ArrayList) session.getAttribute("servicesDetail");
			String desc = "";
			String cont = "";
			String productIdSat = "";
			if (servicesDetailArray != null){
				ShipmentServiceDetail ssd = (ShipmentServiceDetail) servicesDetailArray.get(0);
				desc = ssd.descripcion;
				cont = ssd.contenido;
				productIdSat = ssd.getProductIdSat();
			}			
		
			if(updateDescCont(con, global.getClientId(), desc, cont, global.getOrigenUserClave(), productIdSat)) {
				//AAP//AccessLog.Log(cnct.delete(0,cnct.length()).append(msgAvi).append("generate()_").append(clientId).append("*****value updated successfully******").toString());						
			}
			
			/*CalculateServices calcServ = new CalculateServices();
			calcServ.calculateServices(con, aform, req, global);*/

			if(genGuia.generateGuia(con, aform, req)){
				con.commit();
				//AccessLog.Log(cnct.delete(0,cnct.length()).append(msgAvi).append("generate()_").append(clientId).append("*****LA GUIA HA SIDO DOCUMENTADA CON ÉXITO!******").toString());
				//aform.setSuccessmessage("SU GUIA HA SIDO DOCUMENTADA CON ÉXITO!");
				//req.setAttribute("successmessage","SU GUIA HA SIDO DOCUMENTADA CON ÉXITO!");
				returnPage = "downloadpage";
			} else {
				con.rollback();
				returnPage = "thispage";
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgAvi).append("generate()_").append(clientId).append("*****FALLO EN LA GENERACION DE LA GUIA!******").toString());
			}
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("generate()_Error:").append(e).toString());
			e.printStackTrace();
		}
		return returnPage;
	}

	//code added by sundar on 29/10/2003 for displaying description,content by default
    private boolean updateDescCont(Connection con,String clientid,String desc,String cont, String user, String productIdSat) {
		PreparedStatement pst = null;
		try {
			//AAP//AccessLog.Log(cnct.delete(0,cnct.length()).append(msgAvi).append("updateDescCont()_").append(clientid).append("------------------- INSIDE updateDescCont").toString());
			//String query="update web_clnt_mstr set wc_desc = ?, wc_cont = ? where wc_clnt_id = ?";
			String query="update sys_clnt_user_web set cu_desc = ?, cu_cont = ?, cu_product_id = ? where cu_clnt_id = ? and cu_user_id = ?";
			pst=con.prepareStatement(query);
			pst.setString(1,desc);
			pst.setString(2,cont);
			pst.setString(3,productIdSat);
			pst.setString(4,clientid);
			pst.setString(5,user);
			
			pst.executeUpdate();
			//AAP//AccessLog.Log(cnct.delete(0,cnct.length()).append(msgAvi).append("updateDescCont()_").append(clientid).append("------------------- INSIDE 2nd updateDescCont").toString());			
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("updateDescCont()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			 new Resources().cerrarPreparedStatement(pst);			
		}
		return true;
	}
}