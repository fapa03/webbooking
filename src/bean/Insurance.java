/**
 * @author: Kumaran
 * Fecha de Creaci�n: 
 * Compa��a: PAQUETEXPRESS.
 * Descripci�n del programa: Bean para obtencion de informacion de seguro.
 * FileName: GenerateGuia.java
 * SessionVariables:
 * Other Used Classes:
 * Function Names: 
 * ----------------------------------------------------------------- 
 * MODIFICACIONES:  
 * -----------------------------------------------------------------
 * Clave: AAP12
 * Autor: ABRAHAM ARJONA
 * Fecha: 23/06/2015
 * Descripci�n: Se elimin� l�gica para omitir cobro de seguro. Ahora se bloquea captura de monto en valor declarado
 * ------------------------------------------------------------------ 
 */
package bean;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import logger.AccessLog;
import beanForm.JavWebBookingGeneralMainForm;

public class Insurance {

	private StringBuffer concatena = new StringBuffer();
	//private final String msgAvi = new StringBuffer("MSAVI_B_").append(this.getClass().getName()).append(".").toString();
	private final String msgErr = new StringBuffer("MSERR_B_").append(this.getClass().getName()).append(".").toString();
	private Resources resources = new Resources();
	
	public String getInsuranceAmount(Connection con, HttpSession session,
			JavWebBookingGeneralMainForm aform, boolean sinMonto) {
		String valorDeclarado = "";
		CallableStatement cst = null;
		Global global = (Global) session.getAttribute("sGlobal");
		try {
			String groupClientId = getGroupClientId(con, aform);
			String query = "{ call pack_web.PRO_CLNT_INSU_FLAG(?,?,?,?,?) }";

			cst = con.prepareCall(query);

			cst.setString(1, groupClientId);
			cst.registerOutParameter(2, Types.VARCHAR);
			cst.registerOutParameter(3, Types.VARCHAR);
			cst.registerOutParameter(4, Types.DATE);
			cst.registerOutParameter(5, Types.DATE);

			cst.executeQuery();

			String insuranceFlag = cst.getString(2);
			String policyNumber = cst.getString(3);
			Date expiryDate = cst.getDate(4);
			Date sysDate = cst.getDate(5);

			String lc_inv_vlue = "";
			ConsultaParametros topeValorDeclarado = new ConsultaParametros();
			Double tope = Double.parseDouble(((ArrayList<?>) topeValorDeclarado.QryMdulTypeParm1(con,"BOK","MAX_DECL_AMNT", "AMOUNT").get(0)).get(2).toString());
			if (aform.getValordeclarado() != null && !aform.getValordeclarado().isEmpty()) {
				valorDeclarado = aform.getValordeclarado().replace(",", "");
				if (Double.parseDouble(valorDeclarado) > tope) {
					aform.setErrMsgAdditional("Monto Máximo a Capturar de Valor Declarado para este Envio: "+tope);
				}
			}
			
			valorDeclarado = aform.getValordeclarado() != null ? aform.getValordeclarado().trim() : "";
			
			double intValorDeclarado = 0.0;
			
			String omitirCobroSeguro = getInsuranceViewFlag(con, global.getClientIdAgreement());//AAP12
			if (valorDeclarado != null && valorDeclarado.length() > 0) {
				intValorDeclarado = Double.parseDouble(valorDeclarado);
			}

			if (policyNumber != null && ((expiryDate != null && expiryDate.after(sysDate)) || (expiryDate != null && expiryDate.equals(sysDate)))) {
				valorDeclarado = null;
				lc_inv_vlue = "CLN";
				aform.setValordeclarado("");
				session.setAttribute("valorvalue", "N");
			} else if ((policyNumber != null && (expiryDate != null && expiryDate.before(sysDate)) && insuranceFlag.equalsIgnoreCase("N")) || insuranceFlag.equalsIgnoreCase("N")) {
				valorDeclarado = null;
				lc_inv_vlue = "NO";
				aform.setValordeclarado("");
				session.setAttribute("valorvalue", "N");
			} else if (policyNumber != null && (expiryDate != null && expiryDate.before(sysDate)) && insuranceFlag.equalsIgnoreCase("Y")) {
				if (valorDeclarado == null || valorDeclarado.length() == 0) {
					lc_inv_vlue = "NO";
				} else if (intValorDeclarado <= 1000) {
					lc_inv_vlue = "LTH";
				} else if (intValorDeclarado > 1000 && intValorDeclarado <= 5000) {
					lc_inv_vlue = "GTH";
				} else if (intValorDeclarado > 5000 && intValorDeclarado <= tope) {
					lc_inv_vlue = "GTF";
				}
			} else if (omitirCobroSeguro.equalsIgnoreCase("Y") && sinMonto) {/*AAP12*/
				valorDeclarado = "";
				lc_inv_vlue = "NO";
				aform.setValordeclarado("");
			} else if (insuranceFlag.equalsIgnoreCase("Y")) {
				if (valorDeclarado == null || valorDeclarado.length() == 0) {
					lc_inv_vlue = "NO";
				} else if (intValorDeclarado <= 1000) {
					lc_inv_vlue = "LTH";
				} else if (intValorDeclarado > 1000 && intValorDeclarado <= 5000) {
					lc_inv_vlue = "GTH";
				} else if (intValorDeclarado > 5000 && intValorDeclarado <= tope) {
					lc_inv_vlue = "GTF";
				}
			}

			// For Insurance Type
			getInsuranceType(con, lc_inv_vlue, aform);

			// For Insurance Infomation Display
			String insuranceInformationDisp = getInsuranceInformation(con, valorDeclarado, groupClientId, aform);
			aform.setSeguro(insuranceInformationDisp);
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0, concatena.length()).append(msgErr).append("getInsuranceAmount()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.cerrarCallableStatement(cst);
		}
		return valorDeclarado;
	}

	private String getGroupClientId(Connection con,
			JavWebBookingGeneralMainForm aform) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		String groupIdQuery = "";
		String groupClientId = "";
		try {
			groupIdQuery = "select pack_web.FUN_CHCK_GRUP_CLNT(?)as groupid from dual";
			pst = con.prepareStatement(groupIdQuery);
			pst.setString(1, aform.getOrgienclave());

			rs = pst.executeQuery();

			while (rs.next()) {
				groupClientId = rs.getString("groupid");
			}
			//AAP//AccessLog.Log(concatena.delete(0, concatena.length()).append(msgAvi).append("getGroupClientId()GROUPCLIENTID").append(groupClientId).toString());
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0, concatena.length()).append(msgErr).append("getGroupClientId()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.closeResources(rs, pst);
		}
		return groupClientId;
	}

	@SuppressWarnings("rawtypes")
	private void getInsuranceType(Connection con, String lc_inv_vlue,
			JavWebBookingGeneralMainForm aform) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		int i = 0;
		String label = "";
		String value = "";
		//AAP//AccessLog.Log(concatena.delete(0, concatena.length()).append(msgAvi).append("getInsuranceType()LC_INV_VALUE ").append(lc_inv_vlue).toString());
		try {
			String query = "SELECT pm_vlue1_desc,pm_parm_code1 FROM sys_parm_mstr Where pm_mdul_id = ? and pm_parm_type = ? and pm_parm_code2 = ?";

			pst = con.prepareStatement(query);

			pst.setString(1, "BOK");
			pst.setString(2, "COVERAGE");
			pst.setString(3, lc_inv_vlue);

			rs = pst.executeQuery();

			aform.getInsurancetype().clear();
			aform.getInsurancetypelabel().clear();

			while (rs.next()) {
				label = rs.getString(1);
				value = rs.getString(2);
				//AAP//AccessLog.Log(concatena.delete(0, concatena.length()).append(msgAvi).append("getInsuranceType()VALUE OF OPTION ").append(value).toString());
				aform.setInsurancetypelabel(label);
				aform.setInsurancetype(value);
			}

			// Default Selection
			ArrayList temp = aform.getInsurancetype();
			String tempValue = "";
			if (lc_inv_vlue.length() == 0) {
				for (i = 0; i < temp.size(); i++) {
					tempValue = (String) temp.get(i);
					if (tempValue.equalsIgnoreCase("INV-1")) {
						aform.setCobertura("INV-1");
					}
				}
			} else if (lc_inv_vlue.equalsIgnoreCase("LTH")) {
				for (i = 0; i < temp.size(); i++) {
					tempValue = (String) temp.get(i);
					if (tempValue.equalsIgnoreCase("INV-PF")) {
						aform.setCobertura("INV-PF");
					}
				}
			} else if (lc_inv_vlue.equalsIgnoreCase("GTH")) {
				for (i = 0; i < temp.size(); i++) {
					tempValue = (String) temp.get(i);
					if (tempValue.equalsIgnoreCase("IV-PF"))
						aform.setCobertura("IV-PF");
				}
			} else if (lc_inv_vlue.equalsIgnoreCase("GTF")) {
				for (i = 0; i < temp.size(); i++) {
					tempValue = (String) temp.get(i);
					if (tempValue.equalsIgnoreCase("IV-XF")) {
						aform.setCobertura("IV-XF");
					}
				}
			} else if (lc_inv_vlue.equalsIgnoreCase("NO")) {
				for (i = 0; i < temp.size(); i++) {
					tempValue = (String) temp.get(i);
					if (tempValue.equalsIgnoreCase("INV-1")) {
						aform.setCobertura("INV-1");
					}
				}
			} else if (lc_inv_vlue.equalsIgnoreCase("CLN")) {
				for (i = 0; i < temp.size(); i++) {
					tempValue = (String) temp.get(i);
					if (tempValue.equalsIgnoreCase("INV-CX")) {
						aform.setCobertura("INV-CX");
					}
				}
			}
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0, concatena.length()).append(msgErr).append("getInsuranceType()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.closeResources(rs, pst);
		}
	}

	private String getInsuranceInformation(Connection con,
			String insuranceAmount, String groupClientId,
			JavWebBookingGeneralMainForm aform) {
		return getInsuranceInformation(con, insuranceAmount, groupClientId, aform, false);		
	}
	
	@SuppressWarnings("rawtypes")
	public String getInsuranceInformation(Connection con,
			String insuranceAmount, String groupClientId,
			JavWebBookingGeneralMainForm aform, boolean onchange) {
		CallableStatement cst = null;
		String insInfo = "";
		try {
			ArrayList insuranceTypeValue = aform.getInsurancetype();
			//AAP//ArrayList insuranceTypeLabel = aform.getInsurancetypelabel();

			String value = "";
			//AAP//String label = "";
			
			if(onchange){
				value= aform.getCobertura();
			}else{
				if (!insuranceTypeValue.isEmpty()) {
					value= (String)insuranceTypeValue.get(0);
				}
				//AAP//label= (String)insuranceTypeLabel.get(0);				
			}

			//AAP//AccessLog.Log(concatena.delete(0, concatena.length()).append(msgAvi).append("getInsuranceInformation()VALUE ").append(value).toString());
			//AAP//AccessLog.Log(concatena.delete(0, concatena.length()).append(msgAvi).append("getInsuranceInformation()LABEL ").append(label).toString());

			String query = "{ call pack_web.pro_srvc_info(?,?,?,?) }";
			cst = con.prepareCall(query);
			cst.setString(1, value);

			if (insuranceAmount == null || (insuranceAmount.length() == 0)) {
				cst.setNull(2, Types.NUMERIC);
			} else {
				cst.setDouble(2, Double.parseDouble(insuranceAmount));
			}

			cst.setString(3, groupClientId);
			cst.registerOutParameter(4, Types.VARCHAR);

			cst.executeQuery();

			insInfo = cst.getString(4);

		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0, concatena.length()).append(msgErr).append("getInsuranceInformation()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.cerrarCallableStatement(cst);
		}
		return insInfo;
	}
	
	public void fetchAcknowledgementTypeDesc(Connection con,
			JavWebBookingGeneralMainForm aform, Global global) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		String query = null;
		int checkTariffType = 0;
		String ackType = null;
		try {			
			if (aform.getTarifType().equalsIgnoreCase("G")) {
				/*PM_MDUL_ID  = 'WEB' AND PM_PARM_TYPE  = 'G' AND PM_PARM_CODE1 = 'ACK'*/ 
				//query = "SELECT pm_vlue1_desc,pm_parm_code1 FROM sys_parm_mstr WHERE pm_parm_type = ? AND pm_parm_code1 = ?";
				query = "SELECT PM_VLUE2_DESC, PM_PARM_CODE2 FROM SYS_PARM_MSTR WHERE PM_MDUL_ID  = ? AND PM_PARM_TYPE = ? AND PM_PARM_CODE1 = ? AND PM_PARM_CODE2 = ?";
				/*PM_PARM_CODE2, PM_VLUE2_DESC*/
				ackType = getAcknowledgementType(con, global);
				
				/*configuracion para tarifa nueva*///AAP01
			} else if (aform.getClasifTarif().equals("1") && (aform.getDefaultservicescreen()!= null && aform.getDefaultservicescreen().equalsIgnoreCase("yes"))) {
				if (aform.getDefaultservicescreenKm().equals("Y")) {
					checkTariffType = 3;	
					query = "SELECT PM_VLUE2_DESC, PM_PARM_CODE2 FROM SYS_PARM_MSTR WHERE PM_MDUL_ID  = ? AND PM_PARM_TYPE  = ? AND PM_PARM_CODE1 = ? AND PM_PARM_CODE2 IN ( " +
							"SELECT WT_SRVC_ID FROM WEB_CLNT_SRVC_TRIF_KM WHERE WT_ORGN_CLNT_ID = ? AND ? between WT_DSTN_FROM_KM and WT_DSTN_TO_KM AND WT_TRIF_SLAB = ? AND (WT_SRVC_ID = ? OR WT_TRIF_AMNT > ?))";
				} else {
					checkTariffType = 1;	
					/*SELECT * FROM WEB_CLNT_SRVC_TRIF WHERE WT_ORGN_CLNT_ID = '1920555' AND WT_ORGN_BRNC_ID ='LMM' AND WT_DEST_BRNC_ID = 'TIJ' AND WT_TRIF_SLAB='A' AND WT_SRVC_ID IN (
							SELECT PM_PARM_CODE2 FROM SYS_PARM_MSTR WHERE PM_MDUL_ID  = 'WEB' AND PM_PARM_TYPE  = 'A' AND PM_PARM_CODE1 = 'ACK')*/
					query = "SELECT PM_VLUE2_DESC, PM_PARM_CODE2 FROM SYS_PARM_MSTR WHERE PM_MDUL_ID  = ? AND PM_PARM_TYPE  = ? AND PM_PARM_CODE1 = ? AND PM_PARM_CODE2 IN ( " +
							"SELECT WT_SRVC_ID FROM WEB_CLNT_SRVC_TRIF WHERE WT_ORGN_CLNT_ID = ? AND WT_ORGN_BRNC_ID = ? AND WT_DEST_BRNC_ID = ? AND WT_TRIF_SLAB = ? AND (WT_SRVC_ID = ? OR WT_TRIF_AMNT > ?))";	
				}
				
				
			} else {
				query = "SELECT PM_VLUE2_DESC, PM_PARM_CODE2 FROM SYS_PARM_MSTR WHERE PM_MDUL_ID  = ? AND PM_PARM_TYPE = ? AND PM_PARM_CODE1 = ?";
				checkTariffType = 2;
			}
			
			pst = con.prepareStatement(query);
			
			if (checkTariffType == 0) {//tarifa G
				pst.setString(1, "WEB");
				pst.setString(2, aform.getTarifType());
				pst.setString(3, "ACK");
				pst.setString(4, ackType);
			} else if (checkTariffType == 1) {//tarifa nueva
				pst.setString(1, "WEB");
				pst.setString(2, aform.getTarifType());
				pst.setString(3, "ACK");
				//pst.setString(4, aform.getOrgienclave());
				pst.setString(4, global.getClientIdAgreement());
				pst.setString(5, aform.getOrgioncode());
				pst.setString(6, aform.getDestinationsitecode());				
				pst.setString(7, aform.getTarifType());
				pst.setString(8, "ACK-N");
				pst.setInt   (9, 0);
			} else if (checkTariffType == 3) {//tarifa nueva Km
				pst.setString(1, "WEB");
				pst.setString(2, aform.getTarifType());
				pst.setString(3, "ACK");
				//pst.setString(4, aform.getOrgienclave());
				pst.setString(4, global.getClientIdAgreement());				
				pst.setString(5, global.getKmTarifType());
				pst.setString(6, aform.getTarifType());
				pst.setString(7, "ACK-N");
				pst.setInt   (8, 0);
			} else {//otras tarifas
				pst.setString(1, "WEB");
				pst.setString(2, aform.getTarifType());
				pst.setString(3, "ACK");
			}

			rs = pst.executeQuery();

			aform.getAcklabel().clear();
			aform.getAckvalue().clear();
			
			while (rs.next()) {
				aform.setAcklabel(rs.getString(1));
				aform.setAckvalue(rs.getString(2));
			}
			rs.close();
			pst.close();
			//resources.closeResources(rs, pst);

			String ack = "ACK";
			String groupClientId = getGroupClientId(con, aform);
			//AAP//AccessLog.Log(concatena.delete(0, concatena.length()).append(msgAvi).append("fetchAcknowledgementTypeDesc()GROUP CLIENT ID IN SIDE ").append(groupClientId).toString());
			query = "SELECT CS_DEFA_SRVC_item FROM SYS_CLNT_SRVC WHERE CS_CLNT_ID = ? AND CS_SRVC_ID =? ";
			pst = con.prepareStatement(query);
			pst.setString(1, groupClientId);
			pst.setString(2, ack);
			rs = pst.executeQuery();

			if (rs.next()) {
				aform.setAcusederecibo(rs.getString(1) == null ? "ACK-N" : rs.getString(1));
			} else {
				aform.setAcusederecibo("ACK-N");
			}

		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0, concatena.length()).append(msgErr).append("fetchAcknowledgementTypeDesc()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.closeResources(rs, pst);
		}
	}	
	
	private String getAcknowledgementType(Connection con, Global global) {
		PreparedStatement pst = null;		
		ResultSet rs = null;
		String ackType = null;
		try {
			pst = con.prepareStatement("select wc_ack_type from WEB_CLNT_MSTR where wc_clnt_id =?");
									    
			pst.setString(1, global.getClientIdAgreement());
			
			rs = pst.executeQuery();
			
			if(rs.next()) {
				ackType=rs.getString(1);
			}			
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0,concatena.length()).append(msgErr).append("getAcknowledgementType()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.closeResources(rs,pst);
		}		
		return ackType;
	}	
	
	public void getServiceCovered(Connection con, HttpSession session,
			JavWebBookingGeneralMainForm aform) {
		CallableStatement cst = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String ead = null, cod = null, ack = null, inv = null;
		String deliveryType = "";
		String query = "";
		String groupClientId = "";
		try {
			
			groupClientId = getGroupClientId(con,aform);
			query = "{call pack_web.pro_ftch_srvs_covd(?,?,?,?,?) }";
			cst = con.prepareCall(query);
			cst.setString(1,aform.getTarifType());
			cst.registerOutParameter(2,Types.VARCHAR);
			cst.registerOutParameter(3,Types.VARCHAR);
			cst.registerOutParameter(4,Types.VARCHAR);
			cst.registerOutParameter(5,Types.VARCHAR);
			
			cst.executeQuery();			
			
			ead=cst.getString(2);
			ack=cst.getString(3);
			cod=cst.getString(4);
			inv=cst.getString(5);
			
			//AAP//AccessLog.Log(concatena.delete(0,concatena.length()).append(msgAvi).append("getServiceCovered()EAD VALUE ").append(ead).toString());
			
			if(ead==null) {
				query = "select pack_web.fun_dflt_dlvy(?,?) from dual";
				pst = con.prepareStatement(query);
				pst.setString(1,groupClientId);
				pst.setString(2,ead);
				rs = pst.executeQuery();
				
				if(rs.next()){
					if(rs.getString(1)!=null && rs.getString(1).equalsIgnoreCase("TRUE")) {
						deliveryType="H";
					} else {
						deliveryType="O";
					}
				}
				aform.setEntrega("1");//For default selection				
			} else {
				deliveryType="H";
				aform.setEntrega("2");//For default selection
			}
			
			ServicesGlobal servicesGlobal = (ServicesGlobal)session.getAttribute("servicesGlobal");
			if (servicesGlobal==null) {
				servicesGlobal=new ServicesGlobal();
				servicesGlobal.deliveryType=deliveryType;
				servicesGlobal.ead=ead;
				servicesGlobal.cod=cod;
				servicesGlobal.ack=ack;
				servicesGlobal.inv=inv;
			} else {				
				servicesGlobal.deliveryType=deliveryType;
				servicesGlobal.ead=ead;
				servicesGlobal.cod=cod;
				servicesGlobal.ack=ack;
				servicesGlobal.inv=inv;
			}
			
			session.setAttribute("servicesGlobal",servicesGlobal);
					
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0,concatena.length()).append(msgErr).append("getServiceCovered()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.closeResources(rs, pst, cst);
		}
	}
	
	/** ************************************************************************************************************************************
	 * Metodo para obtener bandera omision de cobro de seguro. Nueva funcionalidad, ahora no permite capturar seguro si esta con valor "Y" *
	 ** *************************************************************************************************************************************/
	private String getInsuranceViewFlag(Connection con, String clientId) {//AAP12

		String query = "";
		String band = "";
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			query = "select nvl(WC_OMITIR_COBRO_SEGURO,'N') WC_OMITIR_COBRO_SEGURO from web_clnt_mstr where wc_clnt_id =?";
			pst = con.prepareStatement(query);
			pst.setString(1, clientId);
			rs = pst.executeQuery();
			while (rs.next()) {
				band = rs.getString(1);
			}
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0,concatena.length()).append(msgErr).append("getInsuranceViewFlag()_Error:").append(e).toString());
			e.printStackTrace();
		}finally{
			resources.closeResources(rs, pst);
		}
		//return "N";
		return band;
	}
}
