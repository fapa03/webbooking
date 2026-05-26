/**
 * File Name    : LoginValidate.java
 * Description  : This used to Access the DataBase Values.
 * Date Written :  25-Feb-2003
 * @author 	    :  D.SivaKumar
 * -----------------------------------------------------------------
 * MODIFICACIONES:
 * -----------------------------------------------------------------
 * Clave: AAP01
 * Autor: ABRAHAM DANIEL ARJONA PERAZA.
 * Fecha: 01/07/2013
 * Descripci?n: Se agreg? obtencion de dato WC_ALLOWED_FXC en consulta de informacion de cliente origen.
 * ------------------------------------------------------------------
 * Clave: AAP19
 * Autor: ABRAHAM DANIEL ARJONA PERAZA.
 * Fecha: 03/12/2018
 * Descripci?n: Se agreg? obtencion de dato WC_SHIPMENT_LETTER_QTY (global.setSStotal(rs.getInt("WC_SHIPMENT_LETTER_QTY")) en consulta de informacion de cliente origen.
 * Para uso en m?dulo reimpresion de guias.
 * ------------------------------------------------------------------
 * ------------------------------------------------------------------
 * Clave: AAP20
 * Autor: MARTIN ALEJANDRO DIMAS CALVO
 * Fecha: 09/01/2020
 * Descripci?n: Se agrego obtenicio de parametro para identificar si el cliente imprime con QZ Tray
 * ---------------------------------------------------------------- --
 */

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;

import bean.ConsultaParametros;
import bean.Global;
import bean.Resources;
import beanUtil.ConnectDB;
import beanUtil.LoginLdap;
import beanUtil.MainProperties;
import logger.AccessLog;
import mx.com.paquetexpress.general.model.dto.LgnUserMstrResponseDTO;

public class LoginValidate {
	private static final String FAILED = "failed";
	private static final String SUCCESS = "success";
	private StringBuffer concatena = new StringBuffer();
	private final String msgErr  = concatena.delete(0, concatena.length()).append("MSERR_B_").append(this.getClass().getName()).append(".").toString();
	private Resources resources = new Resources();
	PreparedStatement pst = null;
	CallableStatement cst = null;
	ResultSet rs = null;
	Global global = null;
	// Constructor
	public LoginValidate() {
	}

	public String validate(Connection con, JavLoginForm lf) {
		String strUserId;
		String strClientGpoId = "";//AAP02
		String strClientOrigId = "";//AAP
		String strAssignedSite = "";
		String strclientName = "";
		String strRFC = "";
		String strPassword;
		String strResult = FAILED;
		String strAssignedSiteName = "";

		String strWeb = "";
		String strPpg = "";
		String query = "";
		try {
			global = new Global();
			strUserId = lf.getLoginName();
			strPassword = lf.getPassword();
			if (lf.getMilliSeconds().length()<=0) {
				LoginLdap loginLdap = new LoginLdap();
				con.close();
				LgnUserMstrResponseDTO lgnUserMstrResponseDTO = loginLdap.login(strUserId, strPassword, "CUS", "ES",lf.getUseUrl());
				strResult = valLdapLogin(lgnUserMstrResponseDTO);
				if (FAILED.equals(strResult)) {
					lgnUserMstrResponseDTO = loginLdap.login(strUserId, strPassword.toUpperCase(), "CUS", "ES",lf.getUseUrl());
				}
				con = ConnectDB.getConnection();

			} else {
				if (validateToken(lf.getToken(),lf.getLoginName())){
					strResult = SUCCESS;//viene de customer central
				}

			}
			if (strResult.equalsIgnoreCase(SUCCESS)) {
				query = "SELECT CU_USER_ID, CU_USER_NAME, CU_WEB_FLAG, CU_PPG_FLAG, CU_PPG_LVEL FROM SYS_CLNT_USER WHERE CU_USER_ID = ?";//AAP02
				pst = con.prepareStatement(query);
				pst.setString(1, strUserId);

				rs = pst.executeQuery();
				if (rs.next()) {
					strResult = SUCCESS;
					global.setOrigenUserClave(rs.getString(1));//AAP02
					global.setOrigenUserNombre(rs.getString(2));//AAP02

					strWeb = rs.getString(3)==null?"N":rs.getString(3);
					strPpg = rs.getString(4)==null?"N":rs.getString(4);

					global.setOrigenUserLevel(rs.getString(5)==null?"1":rs.getString(5));
				} else {
					strResult = FAILED;
				}
			}
			resources.closeResources(rs, pst);

			 // added by Bala user validation for Entrada del cliente
			if (strResult.equalsIgnoreCase(SUCCESS)) {
				query = "SELECT CU_CLNT_ID, CU_USER_ID FROM SYS_CLNT_USER_WEB WHERE CU_USER_ID = ? AND CU_DFLT_FLAG = ?";//RRR
				pst = con.prepareStatement(query);
				pst.setString(1, strUserId);
				pst.setString(2, "Y");
				rs = pst.executeQuery();
				if (rs.next()) {
					strResult = SUCCESS;
					strClientOrigId = rs.getString(1);
				} else {
					strResult = FAILED;
					lf.setMsjeVal("001");// USUARIO SIN CLIENTE ASOCIADO
				}

				resources.closeResources(rs, pst);
			}

			if (strResult.equalsIgnoreCase(SUCCESS)) {
				query = "SELECT WC_CLNT_ID, WC_SITE_ID, PACK_WEB.fun_ftch_clnt_name(WC_MBR_ID), PACK_WEB.fun_ftch_rfc(WC_MBR_ID), Pack_Web.fun_ftch_site_name(WC_SITE_ID) FROM WEB_CLNT_MBR WHERE WC_MBR_ID = ?";//RRR
				pst = con.prepareStatement(query);
				pst.setString(1, strClientOrigId);
				rs = pst.executeQuery();
				if (rs.next()) {
					strResult = SUCCESS;
					strClientGpoId = rs.getString(1);
					strAssignedSite = rs.getString(2);
					strclientName = rs.getString(3);
					strRFC = rs.getString(4);
					strAssignedSiteName = rs.getString(5);
				} else {
					strResult = FAILED;
					lf.setMsjeVal("002");//CLIENTE SIN CONVENIO ASOCIADO
				}
				resources.closeResources(rs, pst);
			}

			if (strResult.equalsIgnoreCase(SUCCESS)) {
				String entradaQuery = "SELECT WC_CLIENTE_DESTINO FROM web_clnt_mstr WHERE WC_CLNT_ID= ?";
				String user = null;
				pst = con.prepareStatement(entradaQuery);
				pst.setString(1, strClientGpoId);//AAP02
				rs = pst.executeQuery();
				if (rs.next()) {
					user = rs.getString(1);
				}
				if (user != null && user.equals("Y")) {
					lf.setUserValidate("validUser");
				} else {
					lf.setUserValidate("invalid");
				}

				resources.closeResources(rs, pst);

				String ccosto = "";
				entradaQuery = "SELECT CU_CCOSTO_ID FROM SYS_CLNT_CCOSTO_USER WHERE CU_CLNT_ID = ? AND CU_USER_ID = ?";
				pst = con.prepareStatement(entradaQuery);
				pst.setString(1, strClientOrigId);
				pst.setString(2, strUserId);
				rs = pst.executeQuery();
				if (rs.next()) {
					ccosto = rs.getString(1);
				}
				if (ccosto.length()<=0) {
					lf.setMsjeVal("cc");//USUARIO SIN CENTRO DE COSTO RELACIONADO
					strResult = FAILED;
				}
				resources.closeResources(rs, pst);
			}


			/**
			 * Ended by user validation for Entrada del cliente Ended by Bala
			 * Get the vaLues From WEB_CLNT_MSTR Table and put it in Session..
			 */


			// System.out.println("Qury1...."+query1);
			if (strResult.equalsIgnoreCase("success")) {
				String query1 = "SELECT WC_CLNT_ID,WC_ASSN_SITE,WC_TRIF_TYPE,WC_ADD_SRVC_DISP,WC_DISPLAY_AMOUNTS,PACK_WEB.fun_ftch_clnt_name(?),PACK_WEB.fun_ftch_rfc(?), WC_NEW_BOOKING, WC_SEND_EMAIL, WC_ALLOWED_FXC, WC_AGREEMENT_KEY, WC_ACCEPT_ZE_T7, WC_SHIPMENT_LETTER_QTY, WC_ACCEPT_RAD_ZE, WC_ACCEPT_RAD_ZE_T7, WC_APRV_BRNC FROM WEB_CLNT_MSTR WHERE WC_CLNT_ID = ?";
				pst = con.prepareStatement(query1);
				pst.setString(1, strClientGpoId);//AAP02
				pst.setString(2, strClientGpoId);//AAP02
				pst.setString(3, strClientGpoId);//AAP02
				rs = pst.executeQuery();

				// set the Values in Hidden fileds Which is need to be put in
				// Session..
				if (rs.next()) {
					lf.setClientId(strClientOrigId);
					lf.setAssignedSite(strAssignedSite);
					global.setSiteName(strAssignedSiteName);
					lf.setTarifType(rs.getString("WC_TRIF_TYPE"));
					lf.setAdditionalServiceFlag(rs.getString("WC_ADD_SRVC_DISP"));
					lf.setDisplayAmountFlag(rs.getString("WC_DISPLAY_AMOUNTS"));
					/*Si flag de mostrar importes, esta deshabilitada (N). se busca flag por excepcion
					 * para habilitar los importes en pantalla solamente (Y) (navegacion webbooking)*/
					if (lf.getDisplayAmountFlag()!= null && lf.getDisplayAmountFlag().equals("N")) {
						lf.setDisplayAmountFlag(getParmCustomerGLP(con, strClientGpoId, "AMNTSCREEN"));//se valida flag excepcion para mostrar importes (solo para pantallas en webbooking)
					}
					lf.setNewBooking(rs.getString("WC_NEW_BOOKING"));
					lf.setShowWeb(strWeb);
					lf.setShowPpg(strPpg);
					lf.setSendEmail(rs.getString("WC_SEND_EMAIL") == null ? "" : rs.getString("WC_SEND_EMAIL"));
					global.setClientName(strclientName);
					global.setRfc(strRFC);
					global.setAllowedFXC(rs.getString(10) == null ? "N" : rs.getString(10));// WC_ALLOWED_FXC//AAP01
					global.setAgreementKey(rs.getString(11) == null ? "N/A" : rs.getString(11));// agreetment key //AAP04
					global.setAcceptZeT7(rs.getString(12) == null ? "N" : rs.getString(12));// WC_ACCEPT_ZE_T7//AAP05
					global.setAcceptRadZp(rs.getString(14) == null ? "N" : rs.getString(14));// WC_ACCEPT_RAD_ZE
					global.setAcceptRadZpT7(rs.getString(15) == null ? "N" : rs.getString(15));// WC_ACCEPT_RAD_ZE_t7
					global.setPassword(strPassword);
					global.setClientIdAgreement(strClientGpoId);//cliente de convenios y configuraciones
					global.setSStotal(rs.getInt("WC_SHIPMENT_LETTER_QTY")); // WC_SHIPMENT_LETTER_QTY //AAP197

					lf.setBrncIdWeb(rs.getString("WC_APRV_BRNC"));

				}
				lf.setAssignedBranch( getBranchID(con, strClientOrigId, strUserId) );
				//obtiene bandera para indicar cobro de servicio local
				global.setLocalService(getBillLocalServices(con, strClientGpoId));//AAP17
				global.setCatalogMbr(getCatalogMbrFlag(con, strClientGpoId));//AAP18
				global.setDisableMFGen(getDisableMFGenFlag(con, strClientGpoId));//AAP18
				global.setShowReprintGuia(getShowReprintGuia(con, strClientGpoId));
				global.setShowGuiasRR(getShowElaboracionGuiasRR(con, strClientOrigId));
				global.setForceCaptureDimensions(getFlagForceCaptureDimensions(con, strClientGpoId));
				global.setAllowRRFromXLS(getAllowRRFromXLS(con, strClientGpoId));//AAP25
				global.setAllowPrintQZ(getAllowPrintQZ(con, strClientGpoId));//AAP20
				global.setPrintWwPdfXls((getParmModMstr(con, "WEB", "FLAGMSTRMOD", "WWPRINTPDFFIX") || getPrintWwPdfXls(con, strClientGpoId).equals("Y")) ? "Y" : "N");
				global.setFactorDivisorPesoVol(getConfigPesoVolumetrico(con));
				global.setManifestTypeWE((getParmModMstr(con, "WEB", "FLAGMSTRMOD", "WEMANFIXED") || getParmCustomerGLP(con, strClientGpoId, "WEMANIFEST").equals("Y")) ? "Y" : "N");
				global.setAllowCancelGuiaMult(getCancelGuiaMult(con, strClientOrigId));
				lf.setShowGuiasRR(global.getShowGuiasRR());
				global.setServerName(InetAddress.getLocalHost().getHostName());
				resources.closeResources(rs, pst);
				lf.setShowPopup(getShowPopup(con));
				global.setDaysCancelGMult(getCancelGuiaMultRangeDays(con));
				global.setPdfFormat(hasPdfFormatDefined(con, strClientOrigId, "4X6"));
				global.setShowGlobalMultCncl(getShowGlobalMultGuideCncl(con));
				global.setShowGlobalRR(getShowGlobalGuiasRR(con));
			}

			resources.closeResources(rs, pst, cst);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			resources.closeResources(rs, pst, cst);
			resources.cerrarConexion(con);
		}
		return strResult;
	}

	private String valLdapLogin(LgnUserMstrResponseDTO lgnUserMstrResponseDTO) {
		if (lgnUserMstrResponseDTO == null || lgnUserMstrResponseDTO.getSuccess() == null
				|| lgnUserMstrResponseDTO.getSuccess().equals("false")) {
			return FAILED;
		} else if (lgnUserMstrResponseDTO.getSuccess().equals("true")){
			return SUCCESS;

		}
		return FAILED;
	}

	public boolean validate(Connection con, String username, String oldpassword, String userlog) {

		String query = "select count(*) from sys_clnt_user where cu_user_id=? AND UPPER(CU_USER_PWRD)=?";
		boolean validateFlag = false;
		try {
			cst = con.prepareCall("begin ? := UPPER(pack_web.FUN_ENCR_DECR_PSWD(?,?)); end;");
			cst.registerOutParameter(1, Types.VARCHAR);
			cst.setString(2, oldpassword.trim());
			cst.setString(3, "E");
			cst.execute();
			String strDecrPassword = cst.getString(1);
			if (cst != null)
				cst.close();

			pst = con.prepareStatement(query);
			pst.setString(1, userlog.trim());
			pst.setString(2, strDecrPassword);
			pst.executeQuery();
			rs = pst.executeQuery();
			if (rs != null && rs.next() && rs.getInt(1) == 1) {
				validateFlag = true;
			}
			if (rs != null)
				rs.close();
			if (pst != null)
				pst.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pst != null)
					pst.close();
				if (cst != null)
					cst.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return validateFlag;
	}
	public Global getGlobal() {
		return global;
	}

	public void setGlobal(Global global) {
		this.global = global;
	}

	private String getBranchID(Connection con, String strClientOrigId, String strUserId) {
		String branch = "";
		String addrCode = "";
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			String query = "SELECT CC_ADDR_CODE, CC_BRNC_ORGN FROM SYS_CLNT_CCOSTO, SYS_CLNT_CCOSTO_USER WHERE CC_CLNT_ID = ? AND CC_CLNT_ID = CU_CLNT_ID and CC_CCOSTO_ID = CU_CCOSTO_ID AND CU_USER_ID = ? AND CU_DFLT_FLAG = ?";
			pst = con.prepareStatement(query);

			pst.setString(1, strClientOrigId);
			pst.setString(2, strUserId);
			pst.setString(3, "Y");

			rs = pst.executeQuery();

			if (rs.next()) {
				addrCode = rs.getString(1) == null ? "" : rs.getString(1);
				branch = rs.getString(2) == null ? "" : rs.getString(2);
			}
			rs.close();
			pst.close();

			if (branch.length()==0) {
				query = "SELECT BC_BRNC_ID FROM SYS_ADDR_MSTR, ER_BRNC_COLY_MSTR WHERE AM_ADDR_CODE = ? AND AM_GETY_LEVL = BC_GETY_LEVL_6 AND AM_GETY_TYPE = BC_GETY_TYPE_6 AND AM_GETY_CODE = BC_GETY_CODE_6";

				pst = con.prepareStatement(query);

				pst.setString(1, addrCode);

				rs = pst.executeQuery();
				if (rs.next()) {
					branch = rs.getString(1);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pst != null)
					pst.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return branch;
	}

	/*obtiene bandera de cliente si cobrar? servicio local*///AAP17
	@SuppressWarnings("rawtypes")
	private String getBillLocalServices(Connection con, String strClientOrigId) {//AAP17
		String result = "0";
		try {
			ConsultaParametros cons = new ConsultaParametros();
			ArrayList temp = cons.QryMdulTypeParm1(con, "WEB", "LOCAL_SERVICE", strClientOrigId);
			if (!temp.isEmpty()) {
				result = temp.isEmpty() ? "0" : ((ArrayList) temp .get(0)).get(3).toString();// obteniendo PM_VLUE2_ID
			}

		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0,concatena.length()).append(msgErr).append("getBillLocalServices()_Error:").append(e).toString());
			e.printStackTrace();
		}
		return result;
	}

	/*obtiene bandera de cliente si si muestra cat?logo de clientes destino por miembro*///AAP18
	@SuppressWarnings("rawtypes")
	private String getCatalogMbrFlag(Connection con, String strClientOrigId) {//AAP17
		String result = "0";
		try {
			ConsultaParametros cons = new ConsultaParametros();
			ArrayList temp = cons.QryMdulTypeParm1(con, "WEB", "CATALOG_MBR", strClientOrigId);
			if (!temp.isEmpty()) {
				result = temp.isEmpty() ? "0" : ((ArrayList) temp .get(0)).get(3).toString();// obteniendo PM_VLUE2_ID
			}

		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0,concatena.length()).append(msgErr).append("getCatalogMbrFlag()_Error:").append(e).toString());
			e.printStackTrace();
		}
		return result;
	}
	/*obtiene bandera de cliente si si muestra cat?logo de clientes destino por miembro*///AAP18
	@SuppressWarnings("rawtypes")
	private String getDisableMFGenFlag(Connection con, String strClientOrigId) {//AAP17
		String result = "0";
		try {
			ConsultaParametros cons = new ConsultaParametros();
			ArrayList temp = cons.QryMdulTypeParm1(con, "WEB", "DISABLE_MF_GEN", strClientOrigId);
			if (!temp.isEmpty()) {
				result = temp.isEmpty() ? "0" : ((ArrayList) temp .get(0)).get(3).toString();// obteniendo PM_VLUE2_ID
			}

		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0,concatena.length()).append(msgErr).append("getDisableMFGenFlag()_Error:").append(e).toString());
			e.printStackTrace();
		}
		return result;
	}

	/*obtiene parametro para mostrar u ocultar opcion de modulo reimpresion de guias*/
	@SuppressWarnings("rawtypes")
	private String getShowReprintGuia(Connection con, String strClientOrigId) {
		String result = "0";
		try {
			ConsultaParametros cons = new ConsultaParametros();
			ArrayList temp = cons.QryMdulTypeParm1(con, "WEB", "SHW_RPRNT_GUIA", strClientOrigId);
			if (!temp.isEmpty()) {
				result = temp.isEmpty() ? "0" : ((ArrayList) temp .get(0)).get(3).toString();// obteniendo PM_VLUE2_ID
			}

		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0,concatena.length()).append(msgErr).append("getShowReprintGuia()_Error:").append(e).toString());
			e.printStackTrace();
		}
		return result;
	}

	/*obtiene parametro para mostrar u ocultar opcion de modulo elaboracion de guias RR*/
	@SuppressWarnings("rawtypes")
	private String getShowElaboracionGuiasRR(Connection con, String strClientOrigId) {
		String result = "0";
		try {
			ConsultaParametros cons = new ConsultaParametros();
			ArrayList temp = cons.QryMdulTypeParm1(con, "WEB", "SHW_GGUIA_RR", strClientOrigId);
			if (!temp.isEmpty()) {
				result = temp.isEmpty() ? "0" : ((ArrayList) temp .get(0)).get(3).toString();// obteniendo PM_VLUE2_ID
			}

		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0,concatena.length()).append(msgErr).append("getShowElaboracionGuiasRR()_Error:").append(e).toString());
			e.printStackTrace();
		}
		return result;
	}

	private String getFlagForceCaptureDimensions(Connection con, String strClientOrigId) {
		String result = "N";
		try {
			ConsultaParametros cons = new ConsultaParametros();
			boolean temp = cons.QryParmCustomerGLP(con, strClientOrigId, "OBINDI");
			if (temp) {
				result = "Y";
			}

		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0,concatena.length()).append(msgErr).append("getFlagForceCaptureDimensions()_Error:").append(e).toString());
			e.printStackTrace();
		}
		return result;
	}

	private String getAllowRRFromXLS(Connection con, String strClientOrigId) {
		String result = "N";
		try {
			ConsultaParametros cons = new ConsultaParametros();
			boolean temp = cons.QryParmCustomerGLP(con, strClientOrigId, "ALLOWRRXLS");
			if (temp) {
				result = "Y";
			}

		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0,concatena.length()).append(msgErr).append("getAllowRRFromXLS()_Error:").append(e).toString());
			e.printStackTrace();
		}
		return result;
	}

	private String getParmCustomerGLP(Connection con, String strClientId, String parm) {
		String result = "N";
		try {
			ConsultaParametros cons = new ConsultaParametros();
			boolean temp = cons.QryParmCustomerGLP(con, strClientId, parm);
			if (temp) {
				result = "Y";
			}

		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0,concatena.length()).append(msgErr).append("getAllowRRFromXLS()_Error:").append(e).toString());
			e.printStackTrace();
		}
		return result;
	}

	private String getAllowPrintQZ(Connection con, String clntId) {
		String result = "N";
		try {
			ConsultaParametros cons = new ConsultaParametros();
			boolean temp = cons.QryParmCustomerGLP(con, clntId, "USEQZTRAY");
			if (temp) {
				result = "Y";
			}
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0,concatena.length()).append(msgErr).append("getAllowPrintQZ()_Error:").append(e).toString());
			e.printStackTrace();
		}
		return result;
	}
	/*obtiene parametro para obtener peso volumetric configurado*/
	@SuppressWarnings("rawtypes")
	private double getConfigPesoVolumetrico(Connection con) {
		double result = 5000;
		try {
			ConsultaParametros cons = new ConsultaParametros();
			ArrayList temp = cons.QryMdulType(con, "BOK", "FACTOR_PESO_VOL");
			if (!temp.isEmpty()) {
				result = temp.isEmpty() ? 5000 : Double.parseDouble( ((ArrayList) temp .get(0)).get(2).toString() );// obteniendo PM_VLUE1_ID
			}

		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0,concatena.length()).append(msgErr).append("getConfigPesoVolumetrico()_Error:").append(e).toString());
			e.printStackTrace();
		}
		return result;
	}
	private boolean validateToken(String token, String user) {
        boolean isValid = true;
        try {
			String secretJWT = MainProperties.getProperty("secretJWT");
			String validateToken = MainProperties.getProperty("validateToken");
            long timeLive = Long.parseLong(MainProperties.getProperty("timeExpire")) / 1000;
            if(validateToken.equalsIgnoreCase("true")){
                Algorithm algorithm = Algorithm.HMAC256(secretJWT);
                JWTVerifier verifier = JWT.require(algorithm)
                        .acceptLeeway(1) //1 sec for nbf and iat
                        .acceptExpiresAt(timeLive) //1 secs for exp
                        .withIssuer(user)
                        .build(); //Reusable verifier instance
                verifier.verify(token);
            }
        } catch (TokenExpiredException exception) {
            exception.printStackTrace();
            isValid = false;
        } catch (JWTVerificationException|UnsupportedEncodingException exception) {
            isValid = false;
        }
        return isValid;
    }

	private String getPrintWwPdfXls(Connection con, String clntId) {
		String result = "N";
		try {
			ConsultaParametros cons = new ConsultaParametros();
			boolean temp = cons.QryParmCustomerGLP(con, clntId, "WWPRINTSSPDF");
			if (temp) {
				result = "Y";
			}
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0,concatena.length()).append(msgErr).append("getPrintWwPdfXls()_Error:").append(e).toString());
			e.printStackTrace();
		}
		return result;
	}

	public String getShowPopup(Connection con) {
		String result = "false";
		try {
			ConsultaParametros cons = new ConsultaParametros();
			ArrayList temp = cons.QryMdulTypeParm1(con, "WEB", "SHW_POPUP", "ALL");
			if (!temp.isEmpty()) {
				result = temp.isEmpty() ? "0" : ((ArrayList) temp .get(0)).get(3).toString();
			}

		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0,concatena.length()).append(msgErr).append("getShowElaboracionGuiasRR()_Error:").append(e).toString());
			e.printStackTrace();
		}
		return result;
	}

	private String getCancelGuiaMult(Connection con, String clntId) {
		String result = "N";
		try {
			ConsultaParametros cons = new ConsultaParametros();
			boolean temp = cons.QryParmCustomerGLP(con, clntId, "ALLOWCNCLGMULT");
			if (temp) {
				result = "Y";
			}
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0,concatena.length()).append(msgErr).append("getCancelGuiaMult()_Error:").append(e).toString());
			e.printStackTrace();
		}
		return result;
	}
	private String getCancelGuiaMultRangeDays(Connection con) {
		ArrayList result = null;
		String resultRange = "";
		try {
			ConsultaParametros cons = new ConsultaParametros();
			result = cons.QryMdulType(con, "WEB", "RANG_CANCELGM");
			if (!result.isEmpty()) {
				result = (ArrayList) result.get(0); //extrae el array con el resultado en general
				resultRange = (String) result.get(2);
			}else {
				resultRange = "0";
			}
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0,concatena.length()).append(msgErr).append("getCancelGuiaMultRangeDays()_Error:").append(e).toString());
			e.printStackTrace();
		}
		return resultRange;
	}

	private String hasPdfFormatDefined(Connection con, String strClientOrigId, String size) {
		String result = "N";
		try {
			ConsultaParametros cons = new ConsultaParametros();
			boolean temp = cons.QryParmCustomerGLP(con, strClientOrigId, "PDF_SIZE_"+size);
			if (temp) {
				result = "Y";
			}

		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0,concatena.length()).append(msgErr).append("hasPdfFormatDefined()_Error:").append(e).toString());
			e.printStackTrace();
		}
		return result;
	}

	private String getShowGlobalMultGuideCncl(Connection con) {
		String result = "0";
		try {
			ConsultaParametros cons = new ConsultaParametros();
			ArrayList<ArrayList<String>> rows = cons.QryMdulTypeParm1(con, "WEB", "SHW_CNCL_MULT_G", "GLOBAL");
			if (!rows.isEmpty()) {
				result = rows.get(0).get(2);
			}
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0, concatena.length()).append(msgErr)
					.append("getShowGlobalGuiasRR()_Error:").append(e).toString());
			e.printStackTrace();
		}
		return result;
	}

	private String getShowGlobalGuiasRR(Connection con) {
		String result = "0";
		try {
			ConsultaParametros cons = new ConsultaParametros();
			ArrayList<ArrayList<String>> rows = cons.QryMdulTypeParm1(con, "WEB", "SHW_GLOBAL_RR", "GLOBAL");
			if (!rows.isEmpty()) {
				result = rows.get(0).get(2);
			}
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0, concatena.length()).append(msgErr)
					.append("getShowGlobalGuiasRR()_Error:").append(e).toString());
			e.printStackTrace();
		}
		return result;
	}
	
	
	private Boolean getParmModMstr(Connection con, String moduleID, String parmType, String parmCode) {
		ArrayList result = null;
		String resultRange = ""; 
		try {
			ConsultaParametros cons = new ConsultaParametros();
			result = cons.QryMdulTypeParm1(con, moduleID, parmType, parmCode);
			if (!result.isEmpty()) {
				result = (ArrayList) result.get(0); //extrae el array con el resultado en general
				resultRange = (String) result.get(2);
			}else {
				resultRange = "0"; 
			}
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0,concatena.length()).append(msgErr).append("getParmModMstr()_Error:").append(e).toString());
			e.printStackTrace();
		}
		return resultRange.equals("1");
	}
	
	
	
	
}