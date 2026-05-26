/**
 * @author: Abraham Daniel Arjona Peraza
 * Fecha de Creaci�n: 22/11/2011
 * Compa��a: PAQUETEXPRESS.
 * Descripci�n del programa: Bean con m�todos para catalogo de clientes
 * FileName: JavCatalogoClientes.java
 * SessionVariables:
 * Other Used Classes:
 * Function Names: 
 * ----------------------------------------------------------------- 
 * MODIFICACIONES:  
 * -----------------------------------------------------------------
 * Clave:  AAP01
 * Autor: Abraham Daniel Arjona Peraza
 * Fecha:  22/11/2011
 * Descripci�n: Se le di� formato al codigo, se borraron algunos system.out de pruebas, se reacomodaron algunas concatenaciones 
 * ------------------------------------------------------------------
 * Clave: AAP03
 * Autor: Abraham Daniel Arjona Peraza
 * Fecha: 26/07/2012
 * Descripci�n: Se agreg� l�gica para obtener informacion de cliente cuando el resultado de la consulta sea de un cliente.
 * Si el resultado de la consulta es de mas de un cliente, solo se presenta listado para posteriormente seleccionar el deseado 
 * y obtenerle informacion la informacion de la direcci�n.
 * ------------------------------------------------------------------
 * Clave: AAP04
 * Autor: Abraham Daniel Arjona Peraza
 * Fecha: 01/07/2013
 * Descripci�n: Se obtiene de cliente destino, bandera de verificacion si acepta flete por cobrar.
 * ------------------------------------------------------------------ 
 */
package bean;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpSession;

import logger.AccessLog;

public class JavCatalogoClientes {
	private final String msgError  = new StringBuffer("MSERR_B_").append(this.getClass().getName()).append(".").toString();
	private StringBuffer concatena = new StringBuffer();
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList getLovRecordsNew(Connection con, String addressType1,
			String destClientId, String clntName, String branchId,
			HttpSession session,  Global global)  {
	
		ResultSet rs = null;
		PreparedStatement pst = null;
		HashMap values = new HashMap(32);

		ArrayList result = new ArrayList();
		String clientId = global.getClientIdAgreement();
		destClientId = destClientId == null ? "" : destClientId;
		//int fun_chk_phy_dc_exist = 0;
		//int fun_chk_no_of_br = 0;
		
		String addrType = "CLNT", UclntName = "", deliveryType, clientIdDest, clientName, PQId, siteName, destSiteId, branch, brname, 
				AM_ADDR_CODE = null, AM_DRNR = null, AM_STRT_NAME = null, AM_PHNO1 = null, AM_SUIT_NO = null, AM_FLOR_NO = null, AM_ADDR_STYP = null, 
				AM_ADDR_DEFN_TYPE = null, AM_ADDR_REF_NO = null, AM_GETY_LEVL = null, AM_GETY_TYPE = null, AM_GETY_CODE = null, AM_COMT = null, CM_DEST_PAID_FLAG = null,//AAP04
				AM_MAIL_ID = null, CM_TAX_ID = null;//AAP05
		String CM_CLNT_TYPE, CM_CREDIT_STATUS;//AAPXXXX

		SucursalesConfiguradas suc = new SucursalesConfiguradas();//AAP02
		
		String whereClntOrign = "WODC.WO_ORGN_CLNT_ID";
		try {
			
			if (global.getCatalogMbr().equals("1")) {
				whereClntOrign = "WODC.WO_MBR_CLNT_ID";
				clientId = global.getClientId();
			}
			
			String query = concatena.delete(0,concatena.length())
					.append("SELECT AM_ADDR_CODE, AM_DRNR, AM_STRT_NAME,")
					.append(" AM_PHNO1, AM_SUIT_NO, AM_FLOR_NO,")
					.append(" AM_ADDR_STYP, AM_ADDR_DEFN_TYPE,")
					.append(" AM_ADDR_REF_NO, AM_GETY_LEVL,")
					.append(" AM_GETY_TYPE, AM_GETY_CODE,")
					.append(" wodc.wo_dest_clnt_id,  clnt.cm_clnt_name,  wodc.wo_dest_cust_clnt_id,")
					.append(" pack_web.fun_ftch_site_name (wodc.wo_dest_site_id) , wodc.wo_dest_site_id, sam.AM_COMT, clnt.CM_DEST_PAID_FLAG,")//AAP04
					.append(" sam.AM_MAIL_ID, clnt.CM_TAX_ID,")//AAP05
					.append(" pack_web.Fun_ftch_desc_client_type (clnt.CM_CLNT_TYPE),")//AAPXXXX
					.append(" pack_web.Fun_ftch_desc_credit_status (clnt.CM_CRED_STUS_ID)")//AAPXXXX
					.append(" FROM").toString();


			if (addressType1.equalsIgnoreCase("DC") && (clntName.length() > 0) && destClientId.length() > 0) {
				UclntName = clntName.toUpperCase();

				query = concatena.delete(0,concatena.length()).append(query)
						.append(" WEB_ORGN_DEST_CLNT wodc, SYS_CLNT_MSTR clnt, SYS_ADDR_MSTR sam WHERE ") 
						.append(" "+whereClntOrign+" = ? AND wodc.WO_DEST_CLNT_ID = clnt.CM_CLNT_ID AND clnt.CM_CLNT_ID = sam.AM_ENTY_ID ")
						.append(" AND (wodc.WO_DEST_CLNT_ID = ? OR WODC.WO_DEST_CUST_CLNT_ID = ? OR (clnt.cm_clnt_name LIKE ?))")
						.append(" AND AM_ADDR_TYPE = ? AND CM_ACTV_FLAG = ?").toString();//AND sam.AM_DEFA_FLAG = ?
			} else if (addressType1.equalsIgnoreCase("DC") && (clntName.length() > 0)) {
				UclntName = clntName.toUpperCase();
							
				query = concatena.delete(0,concatena.length()).append(query)
						.append(" WEB_ORGN_DEST_CLNT wodc, SYS_CLNT_MSTR clnt, SYS_ADDR_MSTR sam WHERE ") 
						.append(" "+whereClntOrign+" = ? AND wodc.WO_DEST_CLNT_ID = clnt.CM_CLNT_ID AND clnt.CM_CLNT_ID = sam.AM_ENTY_ID ")
						.append(" AND clnt.cm_clnt_name LIKE ?")
						.append(" AND AM_ADDR_TYPE = ? AND CM_ACTV_FLAG = ?").toString();//AND sam.AM_DEFA_FLAG = ?
			}else if (destClientId.length() > 0) {
				query = concatena.delete(0,concatena.length()).append(query)
						.append(" WEB_ORGN_DEST_CLNT wodc, SYS_CLNT_MSTR clnt, SYS_ADDR_MSTR sam WHERE ") 
						.append(" "+whereClntOrign+" = ? AND wodc.WO_DEST_CLNT_ID = clnt.CM_CLNT_ID AND clnt.CM_CLNT_ID = sam.AM_ENTY_ID ")
						.append(" AND (wodc.WO_DEST_CLNT_ID = ? OR WODC.WO_DEST_CUST_CLNT_ID = ?)")
						.append(" AND AM_ADDR_TYPE = ? AND CM_ACTV_FLAG = ?").toString(); //AND sam.AM_DEFA_FLAG = ?
			} else {
				query = concatena.delete(0,concatena.length()).append(query)
						.append(" WEB_ORGN_DEST_CLNT wodc, SYS_CLNT_MSTR clnt, SYS_ADDR_MSTR sam WHERE ") 
						.append(" "+whereClntOrign+" = ? AND wodc.WO_DEST_CLNT_ID = clnt.CM_CLNT_ID AND clnt.CM_CLNT_ID = sam.AM_ENTY_ID ")					
						.append(" AND AM_ADDR_TYPE = ? AND CM_ACTV_FLAG = ?").toString();//AND sam.AM_DEFA_FLAG = ?
			}
			
			pst = con.prepareStatement(query,ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			
			destClientId = destClientId.toUpperCase();
			if (addressType1.equalsIgnoreCase("DC") && (clntName.length() > 0) && destClientId.length() > 0) {
				UclntName = concatena.delete(0,concatena.length()).append(UclntName).append("%").toString();	
				pst.setString(1, clientId);
				pst.setString(2, destClientId);
				pst.setString(3, destClientId);
				pst.setString(4, UclntName);
				pst.setString(5, addrType);			
				pst.setString(6, "A");
			} else if (addressType1.equalsIgnoreCase("DC") && (clntName.length() > 0)) {
				UclntName = concatena.delete(0,concatena.length()).append(UclntName).append("%").toString();
				pst.setString(1, clientId);
				pst.setString(2, UclntName);
				pst.setString(3, addrType);
				pst.setString(4, "A");			
			} else if (destClientId.length() > 0) {
				pst.setString(1, clientId);
				pst.setString(2, destClientId);
				pst.setString(3, destClientId);
				pst.setString(4, addrType);
				pst.setString(5, "A");
			} else {
				pst.setString(1, clientId);
				pst.setString(2, addrType);
				pst.setString(3, "A");	
			}

			rs = pst.executeQuery();

			deliveryType = getDeliveryType(con, session);
			
			String entrega = "";//AAP02
			String nuevaSucursal = "";//AAP02
			
//			/*Se posiciona el cursor del resulset al final de los registros para sacar la cantidad de registros obtenidos*///AAP03
//			rs.last();
//			int rowCount = rs.getRow();
//			rs.beforeFirst();
//			
//			/*Si la obtencion de la informacion es de un solo registro, se le obtiene la informacion extra del cliente destino*/
//			if (rowCount == 1) {//AAP02
				while (rs.next()) {
					
					//AAP//AccessLog.Log("inside while loop");
					AM_ADDR_CODE = rs.getString(1) == null ? "" : rs.getString(1);//AM_ADDR_CODE
					AM_DRNR = rs.getString(2) == null ? "" : rs.getString(2);//AM_DRNR
					AM_STRT_NAME = rs.getString(3) == null ? "" : rs.getString(3);//AM_STRT_NAME
					AM_PHNO1 = rs.getString(4) == null ? "" : rs.getString(4);//AM_PHNO1
					AM_SUIT_NO = rs.getString(5) == null ? "" : rs.getString(5);//AM_SUIT_NO
					AM_FLOR_NO = rs.getString(6) == null ? "" : rs.getString(6);//AM_FLOR_NO
					AM_ADDR_STYP = rs.getString(7) == null ? "" : rs.getString(7);//AM_ADDR_STYP
					AM_ADDR_DEFN_TYPE = rs.getString(8) == null ? "" : rs.getString(8);//AM_ADDR_DEFN_TYPE
					AM_ADDR_REF_NO = rs.getString(9) == null ? "" : rs.getString(9);//AM_ADDR_REF_NO
					AM_GETY_LEVL = rs.getString(10) == null ? "" : rs.getString(10);//AM_GETY_LEVL
					AM_GETY_TYPE = rs.getString(11) == null ? "" : rs.getString(11);//AM_GETY_TYPE
					AM_GETY_CODE = rs.getString(12) == null ? "" : rs.getString(12);//AM_GETY_CODE				
					clientIdDest = rs.getString(13) == null ? "" : rs.getString(13);//wo_dest_clnt_id
					clientName = rs.getString(14) == null ? "" : rs.getString(14);//cm_clnt_name
					PQId = rs.getString(15) == null ? "" : rs.getString(15);//wo_dest_cust_clnt_id
					siteName = rs.getString(16) == null ? "" : rs.getString(16);//pack_web.fun_ftch_site_name (wodc.wo_dest_site_id)
					destSiteId = rs.getString(17) == null ? "" : rs.getString(17);//wo_dest_site_id
					AM_COMT = rs.getString(18) == null ? "" : rs.getString(18);//AM_COMT comentario de direccion
					CM_DEST_PAID_FLAG = rs.getString(19) == null ? "N" : rs.getString(19);//CM_DEST_PAID_FLAG indicador si acepta flete por cobrar//AAP04
					AM_MAIL_ID = rs.getString(20) == null ? "" : rs.getString(20);//AM_MAIL_ID correo electronico destinatario//AAP05
					CM_TAX_ID = rs.getString(21) == null ? "" : rs.getString(21);//CM_TAX_ID rfc destinatario//AAP05
					CM_CLNT_TYPE = rs.getString(22) == null ? "" : rs.getString(22);//CM_CLNT_TYPE descripcion de tipo de cliente//AAPXXXX
					CM_CREDIT_STATUS = rs.getString(23) == null ? "" : rs.getString(23);//CM_TAX_ID descripcion de estatus de cr�dito//AAPXXXX
					
					values.put("AM_ADDR_CODE", AM_ADDR_CODE);
					values.put("AM_DRNR", AM_DRNR);
					values.put("AM_STRT_NAME", AM_STRT_NAME);
					values.put("AM_PHNO1", AM_PHNO1);
					values.put("AM_SUIT_NO", AM_SUIT_NO);
					values.put("AM_FLOR_NO", AM_FLOR_NO);
					values.put("AM_ADDR_STYP", AM_ADDR_STYP);
					values.put("AM_ADDR_DEFN_TYPE", AM_ADDR_DEFN_TYPE);
					values.put("AM_GETY_LEVL", AM_GETY_LEVL);
					values.put("AM_GETY_TYPE", AM_GETY_TYPE);				
					values.put("AM_GETY_CODE", AM_GETY_CODE);
					values.put("clientId", clientIdDest);
					values.put("clientName", clientName);
					values.put("PQId", PQId);				
					values.put("siteName", siteName);				
					values.put("siteId", destSiteId);
					values.put("AM_COMT", AM_COMT);
					values.put("CM_DEST_PAID_FLAG", CM_DEST_PAID_FLAG);//AAP04
					values.put("AM_MAIL_ID", AM_MAIL_ID);//AAP05
					values.put("CM_TAX_ID", CM_TAX_ID);//AAP05
					values.put("CM_CLNT_TYPE", CM_CLNT_TYPE);//AAPXXXX
					values.put("CM_CREDIT_STATUS", CM_CREDIT_STATUS);//AAPXXXX
					
					/*obtiene informacion adicional de la direccion por cada cliente*/
					values = ejectPro_ftch_addr(con, values, AM_ADDR_DEFN_TYPE, AM_ADDR_REF_NO, AM_GETY_CODE, AM_GETY_LEVL, AM_GETY_TYPE);
					
					/*se limpian las variables branch y branche name para la iteraccion*/
					branch = "";
					brname = "";
					if (destSiteId.length() > 0) {					
						
						//AAP02 SE ELIMIN� CONDICION PORQUE AHORA SE REALIZA EN BASE A LA CONFIGURACION
						/*if (destSiteId.equalsIgnoreCase("GDL")) {

							if (deliveryType.equalsIgnoreCase("H")){
								branch = "GDL02";
							}else{
								branch = "GDL01";
							}
							
							//obtiene clave y descripcion de sucursal
							//values = getDescrGDLBranch(con, pst, rset, values, destSiteId, branch, brname);

						}*/ //else {
						//callFunction = "PRO_ASSIGN_DEST_BRNC";
						/* obtiene banderas */
						
//						fun_chk_phy_dc_exist = ejectFun_chk_phy_dc_exist(con, pst, rset, destSiteId);
//						
//						fun_chk_no_of_br = ejectFun_chk_no_of_br(con, pst, rset, destSiteId);
//						
//						if (deliveryType.equalsIgnoreCase("H")) {
//							callFunction = "PRO_ASSIGN_DEST_BRNC";
//						} else if (deliveryType.equalsIgnoreCase("O")
//								&& (fun_chk_phy_dc_exist == 1)
//								&& (fun_chk_no_of_br > 1)) {
//							callFunction = "PRO_ASSIGN_DEST_BRNC";
//						} else {
//							callFunction = "DIRECTBRANCH";
//						}

//						if (callFunction.equalsIgnoreCase("PRO_ASSIGN_DEST_BRNC")) {
//							values = ejectPRO_ASSIGN_DEST_BRNC(con, values, AM_GETY_CODE, AM_GETY_LEVL, AM_GETY_TYPE, destSiteId);						
//						} else {
//							values = getDescrSingleBranch(con, values, destSiteId, branch, brname);						
//						}
						values = ejectPRO_ASSIGN_DEST_BRNC(con, values, AM_GETY_CODE, AM_GETY_LEVL, AM_GETY_TYPE, destSiteId);	
						// }//AAP02
						branch = values.get("branch").toString();
						brname = values.get("branchname").toString();
						
						/*
						 * se asigna el tipo de b�squeda de configuracion en base al
						 * tipo de entrega
						 */
						if (deliveryType.equalsIgnoreCase("O")) {// AAP02
							entrega = "DEST_OCURRE";
						} else {
							if (!branch.substring(3).equals("70")) {
								//entrega = "DEST_EAD_YA_NO";//AAP08
								entrega = "";//AAP08
							}
						}
						
						if (entrega.length()>0) {
							/* se busca configuracion */					
							nuevaSucursal = suc.obtieneConfigSucursal(con, "BOK", entrega, branch);// AAP02
							
							if (nuevaSucursal.length() > 0) {// AAP02
								if (entrega.equals("DEST_OCURRE")) {//AAP07
									/*valida sucursal de entrega ocurre de cliente por excepcion*///AAP07
									String nuevaSucursalOcu = suc.obtieneConfigSucursalOcurre(con, destClientId, AM_ADDR_CODE);//AAP07							
									
									if (!nuevaSucursalOcu.equals("")){//AAP07
										/*si coinciden los sites de la sucursa de excepcion y la sucursal destino original, 
										 * se asigna la nueva sucursal excepcion de ocurre*///AAP07
										if (nuevaSucursalOcu.substring(0,2).equals(nuevaSucursal.substring(0,2))) {//AAP07
											nuevaSucursal = nuevaSucursalOcu;//AAP07
										}//AAP07
									}//AAP07
								}//AAP07
								
								branch = nuevaSucursal;
								// obtiene clave y descripcion de sucursal
								values = getDescrGDLBranch(con, values, destSiteId, branch, brname);
							}
						}
					}// destSiteId.legth >0 if part

					result.add(values.clone());
					values.clear();
				}// while ends here
				
//				/*Si el resultado de la consulta de clientes, es de mas de un registro, solo se 
//				 * envia el resultado de la consulta para presentar el listado de clientes*///AAP03
//			} else {//AAP03
//				while (rs.next()) {
//					AM_ADDR_CODE = rs.getString(1) == null ? "" : rs.getString(1);//AM_ADDR_CODE
//					AM_DRNR = rs.getString(2) == null ? "" : rs.getString(2);//AM_DRNR
//					AM_STRT_NAME = rs.getString(3) == null ? "" : rs.getString(3);//AM_STRT_NAME
//					AM_PHNO1 = rs.getString(4) == null ? "" : rs.getString(4);//AM_PHNO1
//					AM_SUIT_NO = rs.getString(5) == null ? "" : rs.getString(5);//AM_SUIT_NO
//					AM_FLOR_NO = rs.getString(6) == null ? "" : rs.getString(6);//AM_FLOR_NO
//					AM_ADDR_STYP = rs.getString(7) == null ? "" : rs.getString(7);//AM_ADDR_STYP
//					AM_ADDR_DEFN_TYPE = rs.getString(8) == null ? "" : rs.getString(8);//AM_ADDR_DEFN_TYPE
//					AM_ADDR_REF_NO = rs.getString(9) == null ? "" : rs.getString(9);//AM_ADDR_REF_NO
//					AM_GETY_LEVL = rs.getString(10) == null ? "" : rs.getString(10);//AM_GETY_LEVL
//					AM_GETY_TYPE = rs.getString(11) == null ? "" : rs.getString(11);//AM_GETY_TYPE
//					AM_GETY_CODE = rs.getString(12) == null ? "" : rs.getString(12);//AM_GETY_CODE				
//					clientIdDest = rs.getString(13) == null ? "" : rs.getString(13);//wo_dest_clnt_id
//					clientName = rs.getString(14) == null ? "" : rs.getString(14);//cm_clnt_name
//					PQId = rs.getString(15) == null ? "" : rs.getString(15);//wo_dest_cust_clnt_id
//					siteName = rs.getString(16) == null ? "" : rs.getString(16);//pack_web.fun_ftch_site_name (wodc.wo_dest_site_id)
//					destSiteId = rs.getString(17) == null ? "" : rs.getString(17);//wo_dest_site_id
//					
//					values.put("AM_ADDR_CODE", AM_ADDR_CODE);
//					values.put("AM_DRNR", AM_DRNR);
//					values.put("AM_STRT_NAME", AM_STRT_NAME);
//					values.put("AM_PHNO1", AM_PHNO1);
//					values.put("AM_SUIT_NO", AM_SUIT_NO);
//					values.put("AM_FLOR_NO", AM_FLOR_NO);
//					values.put("AM_ADDR_STYP", AM_ADDR_STYP);
//					values.put("AM_ADDR_DEFN_TYPE", AM_ADDR_DEFN_TYPE);
//					values.put("AM_GETY_LEVL", AM_GETY_LEVL);
//					values.put("AM_GETY_TYPE", AM_GETY_TYPE);				
//					values.put("AM_GETY_CODE", AM_GETY_CODE);
//					values.put("clientId", clientIdDest);
//					values.put("clientName", clientName);
//					values.put("PQId", PQId);
//					values.put("siteName", siteName);				
//					values.put("siteId", destSiteId);
//					
//					result.add(values.clone());
//					values.clear();
//				}
//			}//AAP03
			
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0,concatena.length()).append(msgError).append("getLovRecordsNew()Error_1:").append(e).toString());
			e.printStackTrace();
		}finally{
			try {
				if (rs != null){
					rs.close();
				}
				if (pst != null){
					pst.close();
				}
			} catch (Exception e2) {
				AccessLog.Log(concatena.delete(0,concatena.length()).append(msgError).append("getLovRecordsNew()Error_2:").append(e2).toString());
				e2.printStackTrace();
			}
		}
		return result;
	}

	private String getDeliveryType(Connection con, HttpSession session) {
		String deliveryType = "";
		Global global = (Global) session.getAttribute("sGlobal");
		PreparedStatement pst = null;
		ResultSet rs = null; 
		CallableStatement cst = null;
		try {
			String groupClientId = getGroupClientId(con, session);
			String query = "{call pack_web.pro_ftch_srvs_covd(?,?,?,?,?) }";
			cst = con.prepareCall(query);
			//AAP//AccessLog.Log("getDeliveryType()_global.tarifType "+global.tarifType);
			cst.setString(1, global.tarifType);
			cst.registerOutParameter(2, Types.VARCHAR);
			cst.registerOutParameter(3, Types.VARCHAR);
			cst.registerOutParameter(4, Types.VARCHAR);
			cst.registerOutParameter(5, Types.VARCHAR);

			cst.executeQuery();
			
			String ead = null;

			ead = cst.getString(2);

						
			if (cst != null)
				cst.close();

			//AAP//AccessLog.Log("EAD VALUE " + ead);

			if (ead == null) {
				//AAP//AccessLog.Log("Inside ead null");
				//String query1 = "select pack_web.fun_dflt_dlvy(?,?) from dual";
				String query1 = "SELECT CS_DEFA_SRVC_ITEM FROM SYS_CLNT_SRVC WHERE CS_CLNT_ID = ? AND CS_SRVC_ID = ?";
				
				pst = con.prepareStatement(query1);
				pst.setString(1, groupClientId);
				pst.setString(2, ead);
				rs = pst.executeQuery();
				if (rs.next()) {
					deliveryType = "H";
//					if (rs.getString(1) != null && rs.getString(1).equalsIgnoreCase("TRUE"))
//						deliveryType = "H";
//					else
//						deliveryType = "O";
					//AAP//AccessLog.Log("Inside deliveryType deliveryType " + deliveryType);
				} else {
					deliveryType = "O";
				}

				if (rs != null)
					rs.close();
				if (pst != null)
					pst.close();
			} else
				deliveryType = "H";
		} catch (Exception e) {
			AccessLog.Log("exception " + e);
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				
				if (cst != null)
					cst.close();
				
				if (pst != null)
					pst.close();
			} catch (Exception e2) {
				AccessLog.Log("exception2 " + e2);
				e2.printStackTrace();
			}
		}
		return deliveryType;
	}

	private String getGroupClientId(Connection con, HttpSession session)
			throws Exception {
		Global global = (Global) session.getAttribute("sGlobal");
		String groupIdQuery = "select pack_web.FUN_CHCK_GRUP_CLNT(?)as groupid from dual";
		PreparedStatement pst = con.prepareStatement(groupIdQuery);
		pst.setString(1, global.clientId);
		ResultSet rs = pst.executeQuery();
		String groupClientId = "";
		while (rs.next()) {
			groupClientId = rs.getString("groupid");
		}
		if (rs != null)
			rs.close();
		if (pst != null)
			pst.close();
		//AAP//AccessLog.Log("GROUPCLIENTID" + groupClientId);

		return groupClientId;
	}
	
	/*ejecuta funcion pro_ftch_addr para obtener valores adicionales de la direccion por cada cliente destino*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private HashMap ejectPro_ftch_addr(Connection con,  HashMap values, String AM_ADDR_DEFN_TYPE, 
			String AM_ADDR_REF_NO, String AM_GETY_CODE, String AM_GETY_LEVL, String AM_GETY_TYPE){
		CallableStatement cst = null;
		try {
			cst = con.prepareCall("{call pack_web.pro_ftch_addr(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) }");

			cst.setString(1, AM_ADDR_DEFN_TYPE);
			cst.setString(2, AM_ADDR_REF_NO);
			cst.setString(3, AM_GETY_CODE);
			cst.setString(4, AM_GETY_LEVL);
			cst.setString(5, AM_GETY_TYPE);
			
			cst.registerOutParameter(6, Types.VARCHAR);
			cst.registerOutParameter(7, Types.VARCHAR);
			cst.registerOutParameter(8, Types.VARCHAR);
			cst.registerOutParameter(9, Types.VARCHAR);
			cst.registerOutParameter(10, Types.VARCHAR);
			cst.registerOutParameter(11, Types.VARCHAR);
			cst.registerOutParameter(12, Types.VARCHAR);
			cst.registerOutParameter(13, Types.VARCHAR);
			cst.registerOutParameter(14, Types.VARCHAR);
			cst.registerOutParameter(15, Types.VARCHAR);
			cst.registerOutParameter(16, Types.VARCHAR);
			cst.registerOutParameter(17, Types.VARCHAR);
			cst.registerOutParameter(18, Types.VARCHAR);
			cst.registerOutParameter(19, Types.VARCHAR);			
			
			cst.executeQuery();

			values.put("u11", cst.getString(6) == null ? "" : cst.getString(6));
			values.put("u12", cst.getString(7) == null ? "" : cst.getString(7));
			values.put("u13", cst.getString(8) == null ? "" : cst.getString(8));
			values.put("u14", cst.getString(9) == null ? "" : cst.getString(9));
			values.put("u15", cst.getString(10) == null ? "" : cst.getString(10));
			values.put("u16", cst.getString(11) == null ? "" : cst.getString(11));
			values.put("u17", cst.getString(12) == null ? "" : cst.getString(12));
			values.put("Zipcode", cst.getString(13) == null ? "" : cst.getString(13));
			values.put("c11", cst.getString(14) == null ? "" : cst.getString(14));
			values.put("c12", cst.getString(15) == null ? "" : cst.getString(15));
			values.put("c13", cst.getString(16) == null ? "" : cst.getString(16));
			values.put("c14", cst.getString(17) == null ? "" : cst.getString(17));
			values.put("c15", cst.getString(18) == null ? "" : cst.getString(18));
			values.put("c16", cst.getString(19) == null ? "" : cst.getString(19));			
			
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0,concatena.length()).append(msgError).append("ejectPro_ftch_addr()Error_1:").append(e).toString());
			e.printStackTrace();
		}finally{
			try {
				if (cst != null) {
					cst.close();
				}
			} catch (Exception e2) {
				AccessLog.Log(concatena.delete(0,concatena.length()).append(msgError).append("ejectPro_ftch_addr()Error_2:").append(e2).toString());
				e2.printStackTrace();
			}			
		}
		return values;
	}
	
	/*obtiene clave y descripcion de sucursales de guadalajara*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private HashMap getDescrGDLBranch(Connection con,  HashMap values, String destSiteId, String branch, String brname){
		PreparedStatement pst = null; 
		ResultSet rset = null;
		try {
			pst = con.prepareStatement("select NVL(BM_BRNC_NAME,'') from sys_brnc_mstr where BM_BRNC_SITE_ID=?  AND BM_BRNC_ID=?");
			pst.setString(1, destSiteId);
			pst.setString(2, branch);
			rset = pst.executeQuery();
			if (rset.next()) {
				brname = rset.getString(1);							
			}
			values.put("branch", branch);
			values.put("branchname", brname);	
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0,concatena.length()).append(msgError).append("getDescrGDLBranch()Error_1:").append(e).toString());
			e.printStackTrace();
		}finally{
			try {
				if (rset != null)
					rset.close();
				if (pst != null) {
					pst.close();
				}
			} catch (Exception e2) {
				AccessLog.Log(concatena.delete(0,concatena.length()).append(msgError).append("getDescrGDLBranch()Error_2:").append(e2).toString());
				e2.printStackTrace();
			}
		}
		return values;
	}
	
	@SuppressWarnings("unused")
	private int ejectFun_chk_phy_dc_exist(Connection con, PreparedStatement pst, ResultSet rset, String destSiteId){
		int fun_chk_phy_dc_exist = 0;
		try {
			pst = con.prepareStatement("select pack_web.fun_chk_phy_dc_exist(?) from dual");
			pst.setString(1, destSiteId);
			rset = pst.executeQuery();
			if (rset.next()) {
				fun_chk_phy_dc_exist = rset.getInt(1);
			}

		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0,concatena.length()).append(msgError).append("ejectFun_chk_phy_dc_exist()Error_1:").append(e).toString());
			e.printStackTrace();
		}finally{
			try {
				if (rset != null)
					rset.close();
				if (pst != null) {
					pst.close();
				}
			} catch (Exception e2) {
				AccessLog.Log(concatena.delete(0,concatena.length()).append(msgError).append("ejectFun_chk_phy_dc_exist()Error_2:").append(e2).toString());
				e2.printStackTrace();
			}
		}
		return fun_chk_phy_dc_exist;
	}
	
	@SuppressWarnings("unused")
	private int ejectFun_chk_no_of_br(Connection con, PreparedStatement pst, ResultSet rset, String destSiteId){
		int fun_chk_no_of_br = 0;
		try {
			pst = con.prepareStatement("select pack_web.fun_chk_no_of_br(?) from dual");
			pst.setString(1, destSiteId);
			rset = pst.executeQuery();
			if (rset.next()) {
				fun_chk_no_of_br = rset.getInt(1);
			}
			if (rset != null)
				rset.close();
			if (pst != null)
				pst.close();

		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0,concatena.length()).append(msgError).append("ejectFun_chk_phy_dc_exist()Error_1:").append(e).toString());
			e.printStackTrace();
		}finally{
			try {
				if (rset != null)
					rset.close();
				if (pst != null) {
					pst.close();
				}
			} catch (Exception e2) {
				AccessLog.Log(concatena.delete(0,concatena.length()).append(msgError).append("ejectFun_chk_phy_dc_exist()Error_2:").append(e2).toString());
				e2.printStackTrace();
			}
		}
		return fun_chk_no_of_br;
	}
	

	/*ejecuta procedimiento pro_ftch_addr para obtener clave y descripcion de sucursal para site que tiene mas de una sucursal*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private HashMap ejectPRO_ASSIGN_DEST_BRNC(Connection con,  HashMap values, 
			String AM_GETY_CODE, String AM_GETY_LEVL, String AM_GETY_TYPE, String destSiteId){
		CallableStatement cst = null;
		try {
			cst = con.prepareCall("{call pack_web.PRO_ASSIGN_DEST_BRNC(?,?,?,?,?,?)}");
			
			cst.setString(1, AM_GETY_CODE);
			cst.setString(2, AM_GETY_TYPE);
			cst.setString(3, AM_GETY_LEVL);			
			cst.registerOutParameter(4, Types.VARCHAR);
			cst.registerOutParameter(5, Types.VARCHAR);			
			cst.setString(6, destSiteId);
					
			cst.executeQuery();			
			
			values.put("branch", cst.getString(4) == null ? "null" : cst.getString(4));//AAP03
			values.put("branchname", cst.getString(5) == null ? "null" : cst.getString(5));//AAP03
			
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0,concatena.length()).append(msgError).append("ejectPRO_ASSIGN_DEST_BRNC()Error_1:").append(e).toString());
			e.printStackTrace();
		}finally{
			try {
				if (cst != null) {
					cst.close();
				}
			} catch (Exception e2) {
				AccessLog.Log(concatena.delete(0,concatena.length()).append(msgError).append("ejectPRO_ASSIGN_DEST_BRNC()Error_2:").append(e2).toString());
				e2.printStackTrace();
			}			
		}
		return values;
	}	
	
	/*obtiene clave y descripcion de sucursales simples*/
//	@SuppressWarnings({ "rawtypes", "unchecked" })
//	private HashMap getDescrSingleBranch(Connection con, HashMap values, String destSiteId, String branch, String brname){
//		PreparedStatement pst=null;
//		ResultSet rset = null;
//		try {
//			pst = con.prepareStatement("select NVL(BM_BRNC_ID,''),NVL(BM_BRNC_NAME,'') from sys_brnc_mstr where BM_BRNC_SITE_ID=?  AND BM_FLAG1='G'");
//			pst.setString(1, destSiteId);
//			rset = pst.executeQuery();
//			if (rset.next()) {
//				branch = rset.getString(1);
//				brname = rset.getString(2);
//			}
//			values.put("branch", branch);
//			values.put("branchname", brname);
//			
//		} catch (Exception e) {
//			AccessLog.Log(concatena.delete(0,concatena.length()).append(msgError).append("getDescrSingleBranch()Error_1:").append(e).toString());
//			e.printStackTrace();
//		} finally {
//			try {
//				if (rset != null)
//					rset.close();
//				if (pst != null) {
//					pst.close();
//				}
//			} catch (Exception e2) {
//				AccessLog.Log(concatena.delete(0,concatena.length()).append(msgError).append("getDescrSingleBranch()Error_2:").append(e2).toString());
//				e2.printStackTrace();
//			}
//		}
//		return values;
//	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList getListaClientesDestino(Connection con, Global global, String nombreDest) {
		ArrayList datos = new ArrayList();
		ArrayList datosTemp = new ArrayList(4);
		PreparedStatement psmt = null;
		ResultSet rs = null;		
		String clienteOrigen = global.getClientIdAgreement();//AAP18
		String whereClntOrign = "WO_ORGN_CLNT_ID";//AAP18
		String query = "";//AAP06
		
		try {
			if (global.getCatalogMbr().equals("1")) {//AAP18
				whereClntOrign = "WO_MBR_CLNT_ID";//AAP18
				clienteOrigen = global.getClientId();//AAP18
			}//AAP18
			
			query = concatena.delete(0,concatena.length()).append("SELECT WO_DEST_CLNT_ID, CM_CLNT_NAME as NOMBRE,  WO_DEST_CUST_CLNT_ID, PACK_WEB.Fun_ftch_site_name(WO_DEST_SITE_ID) AS SITE, WO_DEST_SITE_ID ")//AAP06
					.append("FROM WEB_ORGN_DEST_CLNT, SYS_CLNT_MSTR ")//AAP06
					.append("WHERE "+whereClntOrign+" = ? AND WO_DEST_CLNT_ID = CM_CLNT_ID AND CM_CLNT_NAME LIKE ? AND CM_ACTV_FLAG = ?  AND ROWNUM <= ? ORDER BY NOMBRE").toString();//AAP06
			
			psmt = con.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			psmt.setString(1, clienteOrigen);
			psmt.setString(2, concatena.delete(0,concatena.length()).append("%").append(nombreDest).append("%").toString());
			psmt.setString(3, "A");//AAP06
			int maxQtyRslt = getMaxQtyRslt(con, "DEST_CLNT");
			psmt.setInt(4, maxQtyRslt+1);
			
			rs = psmt.executeQuery();
			
			if (!isValidQtyResults(con, rs, maxQtyRslt)){
				datosTemp.add("ERROR");
				datosTemp.add("Favor de detallar más el nombre en la búsqueda para mayor precisión.");
				datosTemp.add("");
				datosTemp.add("");
				datosTemp.add("");
				datos.add(datosTemp.clone());
				datosTemp.clear();
				return datos;
			}
			
			while(rs.next()) {
				datosTemp.add(rs.getString(1) == null ? "" : rs.getString(1));
				datosTemp.add(rs.getString(2) == null ? "" : rs.getString(2));
				datosTemp.add(rs.getString(3) == null ? "" : rs.getString(3));
				datosTemp.add(rs.getString(4) == null ? "" : rs.getString(4));
				datosTemp.add(rs.getString(5) == null ? "" : rs.getString(5));
				
				datos.add(datosTemp.clone());
				datosTemp.clear();
			}
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0,concatena.length()).append(msgError).append("getListaClientesDestino()Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			Resources resources = new Resources();
			resources.closeResources(rs, psmt);
		}
		return datos;
	}

	private boolean isValidQtyResults(Connection con, ResultSet rs, int maxQtyRslt) {
	    int rowQty = 0;

	    try {
	        if (!rs.next()) {
	            return true;
	        }

	        rs.last();
	        rowQty = rs.getRow();
	        rs.beforeFirst();
	        
	        if (rowQty > maxQtyRslt) {
	            return false;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return true;
	}

	private int getMaxQtyRslt(Connection con, String srchType) {
	    ConsultaParametros consulta = new ConsultaParametros();
		ArrayList conf = consulta.QryMdulTypeParm1(con, "BOK", "MAX_QTY_RSLT", srchType);
        if (conf.isEmpty()) {
            return 0;
        }
        String maxTmp = ((ArrayList<?>) conf.get(0)).get(2).toString();
        return Integer.parseInt(maxTmp);
	}
}
