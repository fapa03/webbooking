/**
 * 
 * Kits-Id			Date				Purpose
 * 
 * 69949	  		26-Mar-2010: 		To include Package type like sobre, paquete, caja.
 * 
 */

package paquetexpress.internal.prepaidcajas.conversion.dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;

import com.paquetexpress.core.web.util.Rfc;
import com.paquetexpress.www.webbooking.Documentacion.ShipTypeSEG;

import bean.CentrosCosto;
import bean.ConsultaParametros;
import bean.Global;
import bean.JavAddressLovRecords;
import bean.JavTariff;
import bean.PrintFileExport;
import bean.Resources;
import bean.SucursalesConfiguradas;
import beanUtil.ConnectDB;
import logger.AccessLog;
import mx.com.paquetexpress.general.model.dto.BranchDetailDTOResponse;
import paquetexpress.internal.common.DateUtil;
import paquetexpress.internal.common.JavBranchRecords;
import paquetexpress.internal.common.JavControlMail;
import paquetexpress.internal.common.JavDeliveryHours;
import paquetexpress.internal.prepaidcajas.action.valueobject.PrepaidValueObject;
import paquetexpress.internal.prepaidcajas.conversion.action.guiamstr.PrepaidGuiaMstr;
import paquetexpress.internal.prepaidcajas.conversion.form.ConversionForm;

public class ConversionDao {
	private StringBuffer cnct = new StringBuffer();
	private final String msgErr = new StringBuffer("MSERR_B_").append(this.getClass().getName()).append(".").toString();
	private Resources resources = new Resources();
	Connection con = null;

	PreparedStatement pstmt = null;
	Statement st = null;
	String tarifType = "";
	CallableStatement cst = null;
	ResultSet rs = null;
	ResultSet rs1 = null;
	HttpSession session = null;
	// Agregado 19-Oct-2011 CenitSoft
	/*String getGuiaMstr = "SELECT BGM_REF_NO,BGM_PREP_BRNC_ID, BGM_REF_DT, BGM_ORGN_BRNC_ID, BGM_CLINT_ID, BGM_NO_OF_GUIS, BGM_ZONE, BGM_WGHT, BGM_VLUM, BGM_GH_AMT, BGM_VALID_FLG, BGM_ORG_SITE_ID, BGM_SHIP_TYPE, M.CRTD_ON, M.CRTD_BY, M.MDFD_ON, M.MDFD_BY, BGM_SWINGCD_NO, BGM_ACK_SERVICE, BGM_ACK_AMT, BGM_ACK_TAX, BGM_INSUR_SERVICE, BGM_INSUR_AMT, BGM_INSUR_TAX, BGM_DSC_PERCENT, BGM_DSC_AMT, BGM_EAD_SERVICE, BGM_EAD_AMT, BGM_RAD_SERVICE, BGM_RAD_AMT, BGM_EXT_SERVICE, BGM_EXT_AMT, BGM_TARIFA"
			+ " FROM PPG_BOK_GUIA_MSTR M, PPG_BOK_GUIA_DTL D WHERE M.BGM_REF_NO = D.BGD_REF_NO AND D.BGD_TRAC_NO = ?";*/
	String reinsertrecord = "{call pack_ppg_web_2012.pro_ppg_modify_guia_cajas(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	String convertguia = "{ call pack_ppg_web_2012.proConvertPpgNrmlCajasWW2017(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) }";	
	String getzones="select SCZ_ZONE_NAME_PPG as zonas, SCZ_END_KM from SYS_COVER_ZONE where SCZ_ZONE_ACTIVE ='A' and DELETE_FLAG = 'N' order by scz_zone_id";//aap04 cambio de tabla //JSA01 cambio por tabla SEG.
	//String getzonesTo = "select dt_to_vlue as zonas from act_dstn_tarif where dt_refr_srvc_id = ? and dt_trif_slab = ?";//AAP06 CAMBIO DE TABLA act_dstn_trif POR act_dstn_tarif
	String getGuianumbers = "select PTM_VOL_FROM,PTM_VOL_TO from ppg_trf_mstr  where PTM_ZONE_ID = ? AND PTM_RATE_FACTOR_FLG = ?";
	String gettarifas = "select distinct(dt_trif_slab) as tarifas from act_dstn_tarif where dt_refr_srvc_id = ?";//AAP06 CAMBIO DE TABLA act_dstn_trif POR act_dstn_tarif
	String gettarifasIds = "select distinct(dt_trif_slab) as tarifas from act_dstn_tarif where dt_refr_srvc_id='PACKETS'";//AAP06 CAMBIO DE TABLA act_dstn_trif POR act_dstn_tarif

	String zoneverification = "select pack_ppg_web_2012.fun_zone_verfication_cajas(?,?,?,?) from dual";
	String checkGuiaExist = "select BGM_CLINT_ID,BGM_ORGN_BRNC_ID,BGD_REF_NO,BGD_GH_AMT,BGM_ZONE,BGM_WGHT,BGM_VLUM,D.BGM_DEST_SITE_ID,D.BGM_DEST_CLNT_ID,PACK_WEB.fun_ftch_clnt_name(D.BGM_DEST_CLNT_ID),Pack_Web.fun_ftch_site_name(D.BGM_DEST_SITE_ID),PACK_WEB.fun_ftch_clnt_name(BGM_CLINT_ID),D.BGD_GH_CONTENT,  "
			+ " BGM_EAD_SERVICE, BGM_RAD_SERVICE, BGM_EXT_SERVICE, BGM_REF_NO_OLD,BGM_ACK_SERVICE, BGM_ACK_AMT, BGM_INSUR_SERVICE, BGM_INSUR_AMT, BGM_TARIFA, BGM_DSC_PERCENT, BGM_SHIP_TYPE, BGM_CMPY_ID, "
			+ "BGM_LOC_TYPE "//AAP20
			+ "from PPG_BOK_GUIA_DTL D,PPG_BOK_GUIA_MSTR M WHERE D.BGD_TRAC_NO=? AND M.BGM_REF_NO=D.BGD_REF_NO AND M.BGM_VALID_FLG='A' AND D.BGD_CONV_DATE IS NULL and D.BGD_ACTV_FLAG='A'";
	String fetchShip="select PM_VLUE1_DESC FROM sys_parm_mstr WHERE PM_MDUL_ID ='PPG' AND PM_PARM_TYPE ='TRANS_MODE' and PM_PARM_CODE1 in (select SD_SHIP_TYPE from PPG_SHIP_DET WHERE SD_SHIP_GUIA_REF_NO=?)  AND PM_PARM_CODE1 NOT IN (SELECT PM_PARM_CODE1 FROM sys_parm_mstr WHERE PM_MDUL_ID ='PPG' AND PM_PARM_TYPE ='SRVC_SEG_PPG')";
	// Added on 14/07/2010 - For kitsId : 70454 Starts
	String fetchDelvryType = "SELECT PM_VLUE1_DESC,PM_PARM_CODE1 FROM sys_parm_mstr where pm_parm_type = ?";
//	String destClientCity = "SELECT ge_code FROM sys_gety_mstr WHERE (ge_code, ge_levl, ge_type) IN ( SELECT gr_code_r, gr_levl_r, gr_type_r "
//			+ " FROM sys_gety_resp WHERE (gr_code, gr_levl, gr_type) IN (SELECT gr_code_r, gr_levl_r, gr_type_r "
//			+ " FROM sys_gety_resp WHERE (gr_code, gr_type, gr_levl) IN ( SELECT am_gety_code, am_gety_type, am_gety_levl "
//			+ " FROM sys_addr_mstr  WHERE am_enty_id = ? AND AM_ADDR_CODE = ?)))";
	String destClientColonia = "SELECT am_gety_code FROM sys_addr_mstr WHERE am_enty_id = ? AND AM_ADDR_CODE = ?";
	// Added on 14/07/2010 - For kitsId : 70454 Ends
	String chkEadAvl = "SELECT CM_EAD_FLAG FROM SYS_CLNT_MSTR WHERE "
			+ "CM_CLNT_ID=?"; // Added on 28/07/2010 - For kitsId : 70454 Starts
	String fetchAddress = "select BGA_ADDR_TYPE,BGA_ADDR_CODE,"
			+ "BGA_ADDR_LIN4,BGA_ADDR_LIN6,BGA_STRT_NAME,BGA_DRNR,BGA_PHNO_1,BGA_ZIP_CODE FROM PPG_BOK_GUIA_ADDR  WHERE BGA_REF_NO=?";
	String getname = "select PACK_WEB.fun_ftch_clnt_name(?) from dual ";
	// MODIFICACION CENITSOFT
	// Anterior
	// String
	// getzones="select PZM_ZONE_NAME from PPG_ZONE_MSTR WHERE PZM_ZONE_ACTIVE='Y' order by PZM_ZONE_NAME";
	// Nuevo
	// FIN MODIFICACION
	// CENITSOFT
	// Obtener el peso o volumen maximo de la tarifa
	String getPesoMaximo = "select ti_to_vlue,ti_trif_id from act_trif_id where ti_fact = ? order by ti_trif_id";
	// Fin obtener maximos de tarifas
	// Modifica La guia Existente
	//String modifyrecord = "{call pro_ppg_modify_guia_cajas(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) }";
	// Fin modifica la guia existente
	// Obtener los valores si el cliente es de Zona Extendida
	String verificazonaextendida = "select A.BC_BRNC_ID, A.BC_OL_ID "
			+ "FROM er_brnc_coly_mstr A, SYS_CLNT_VIEW B "
			+ "WHERE B.cm_clnt_id = ? "
			+
			// "AND ROWNUM=1 " +
			"AND SUBSTR(A.BC_BRNC_ID,4,2) = ?"
			+ // AAP02
			"AND B.AM_ADDR_CODE = ? "
			+ // AAP02
			"AND A.BC_GETY_LEVL_6 = B.AM_GETY_LEVL "
			+ "AND A.BC_GETY_CODE_6 = B.AM_GETY_CODE "
			+ "AND A.BC_GETY_TYPE_6 = B.AM_GETY_TYPE ";
	// Fin verificar zona extendida

	String getShipTypes = "select PM_PARM_CODE1 FROM sys_parm_mstr WHERE PM_MDUL_ID ='PPG' AND PM_PARM_TYPE ='TRANS_MODE'";
	String getzoneIds = "select distinct(dt_trif_slab) as tarifas from act_dstn_tarif where dt_refr_srvc_id='PACKETS' order by tarifas asc";//AAP06 CAMBIO DE TABLA act_dstn_trif POR act_dstn_tarif
	String getrfc = "select PACK_WEB.fun_ftch_rfc(?) from dual";
	String insertmaster = "insert into PPG_BOK_GUIA_MSTR(BGM_REF_NO,BGM_BRANCH_ID,BGM_REF_DT,BGM_ORGN_BRNC_ID,BGM_CLINT_ID"
			+ ",BGM_NO_OF_GUIS,BGM_ZONE,BGM_WGHT,BGM_VLUM,BGM_GH_AMT,BGM_VALID_FLG) VALUES(?,?,?,?,?,?,?,?,?,?,?) ";
	String seqmaster = "{ call GEN_SEQN_NUM_WEB(?) }";
	String insertrecord = "{call PRO_PPG_INS_GUIA(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

	String print = "{ call PACK_PPG_GUIA_PRINT.PRO_PPG_GUIA_PRINT(?,?)}";
	
	String removeguia = "update PPG_BOK_GUIA_MSTR set BGM_VALID_FLG='N',MDFD_ON=(SELECT SYSDATE FROM DUAL),MDFD_BY=? where  BGM_REF_NO =?";
	String removeguias = "{call pack_ppg_web_2012.PRO_PPG_REM_GUIA(?,?) }";
	//String invoiceSeq = "SELECT pack_ppg_web_2012.FUN_INV_SQNUM,sysdate FROM DUAL";
	String invoiceprint = "{ call PACK_PPG_GUIA_PRINT.PRO_PPG_INV_PRINT(?,?,?) }";
	String selectTax = "select SM_TAX_PERC,SM_RETN_TAX_PERC from SYS_SITE_MSTR where SM_SITE_ID=?";
	//String misSave = "{ call pack_ppg_web_2012.PRO_PPG_INSERTINTO_ACT_FTT(?,?,?,?,?,?) }";
	//String removeInvoiceLessCredit = "{ call pack_ppg_web_2012.PRO_REMOVE_INVOICE_NOCRLT(?,?) }";

	String misSaveCredit="{ call pack_ppg_web_2012.proppgReinsertActFttBox_Fact33(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,'+1',?,?,?,?,?) }";
	//String misSavecash = "{ call pack_ppg_web_2012.pro_ppg_reinsert_act_ftt_box(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,'-1',?,?,?) }";
	//String updateFormNo = "update sys_parm_mstr set pm_vlue1_id=?,MDFD_ON=sysdate,MDFD_BY=? where pm_parm_type='CFD_NO' and pm_parm_code1=? and pm_vlue2_id='4'";
	String fetchFormCtr = "select PM_PARM_CODE2 FROM  sys_parm_mstr WHERE pm_parm_type = 'CFD_NO' AND pm_parm_code1 = ? AND pm_vlue2_id = '9'";
	//String fetchFormNo = "begin ? := pack_ppg_web_2012.FUN_PPG_GEN_FORM_NO('CFD_NO',?,'4'); end;";
	String fetchFormNo="Select FUN_GET_CFD_NO_BRNC(?,9) FROM DUAL";
	//String getClientType = "select cm_clnt_type from sys_clnt_mstr where cm_clnt_id=?";
	// CenitSoft
	// Obtener los maximo de la tarifa
	// Uso: valor=getPexoMaximo("T2","CUM");
	// KG para Kilos, CUM para Volumen

	String getPesoMaximoT7 = "SELECT PM_VLUE1_ID FROM SYS_PARM_MSTR WHERE PM_MDUL_ID='BOK' AND PM_PARM_TYPE='LIM_TOP_PESO_BG' AND PM_PARM_CODE1='G'";
	String fetchShipSEG="select PM_VLUE1_DESC, PM_PARM_CODE1,PM_PARM_CODE2 FROM sys_parm_mstr WHERE PM_MDUL_ID ='BOK' AND PM_PARM_TYPE ='SRVC_LOG' and PM_PARM_CODE2 in (select SD_SHIP_TYPE from PPG_SHIP_DET WHERE SD_SHIP_GUIA_REF_NO=?)";
	String fetchShipSEGByParmCode2="select PM_VLUE1_DESC, PM_PARM_CODE1,PM_PARM_CODE2 FROM sys_parm_mstr WHERE PM_MDUL_ID ='BOK' AND PM_PARM_TYPE ='SRVC_LOG' and PM_PARM_CODE2 = ?";
	//2017-07-21 esta consulta regresa los servicios disponible para el cliente ya validado su tiempo de corte o cobertura
	//String fetchShipSEGALL="SELECT PM_VLUE1_DESC,PM_PARM_CODE1 ,PM_PARM_CODE2 ,sactivos from (SELECT PM_VLUE1_DESC,PM_PARM_CODE1, PM_PARM_CODE2, nvl(CASE WHEN PM_PARM_CODE1 = 'STD-T'  THEN 1 WHEN PM_PARM_CODE1 = 'SEG-2D'  AND FUN_IS_CORTE_SEG_CUSTOMER( 'SEG-DS' ,:GH_ORGN_BRNC_ID , :GH_DEST_BRNC_ID, :CLIENTEID, :ADDRCODE)  = 0 THEN 1 WHEN PM_PARM_CODE1 NOT IN  ( 'SEG-2D' , 'STD-T' ) THEN FUN_IS_CORTE_SEG_CUSTOMER(PM_PARM_CODE1 , :GH_ORGN_BRNC_ID, :GH_DEST_BRNC_ID, :CLIENTEID, :ADDRCODE) END,0) sactivos FROM SYS_PARM_MSTR WHERE PM_PARM_TYPE =  'SRVC_LOG' ) where sactivos = 1 ";
	String fetchShipSEGALL="SELECT PM_VLUE1_DESC, PM_PARM_CODE1, PM_PARM_CODE2, sactivos from (SELECT PM_VLUE1_DESC,PM_PARM_CODE1, PM_PARM_CODE2, nvl(CASE WHEN PM_PARM_CODE1 = 'STD-T' THEN 1 WHEN PM_PARM_CODE1 NOT IN ('STD-T') THEN FUN_IS_CORTE_SEG_CUSTOMER(PM_PARM_CODE1, :GH_ORGN_BRNC_ID, :GH_DEST_BRNC_ID, :CLIENTEID, :ADDRCODE) END, 0) sactivos FROM SYS_PARM_MSTR WHERE PM_PARM_TYPE = 'SRVC_LOG') where sactivos = 1";
	String getCobertura="SELECT FUN_GET_COVER_SEGxSER(?,?) FROM DUAL";
	//2017-01-07 Para obtener la hora de corte de la sucursal para el SEG. 
	String getInsideTimeCourt="select FUN_IS_CORTE_SEG_CUSTOMER(?,?,?,?,?) from dual";
	//2017-07-21 Se agrego esta consulta para saber cuales servicios seran tratados como servicio express garantizado.
	String getExclusiveTypeShipSEG ="SELECT PM_PARM_CODE1 FROM sys_parm_mstr WHERE PM_MDUL_ID ='PPG' AND PM_PARM_TYPE ='SRVC_SEG_PPG'";

	private String fetchFormCounter(HttpSession sesion, Connection conn) {
		String formCtr = "";

		try {
			//con = ConnectDB.getConnection();
			pstmt = conn.prepareStatement(fetchFormCtr);
			pstmt.setString(1, (String) sesion.getAttribute("branchid"));
			rs = pstmt.executeQuery();
			while (rs.next()) {
				formCtr = rs.getString(1);
			}			
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("fetchFormCounter()_Error:").append(e).toString());
			e.printStackTrace();
			return "";
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (rs != null)
					rs.close();
				//if (conn != null)
					//conn.close();
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("fetchFormCounter()_Error:").append(e).toString());
				e.printStackTrace();
			}
		}
		return formCtr;
	}
	
	/*obtiene parametro para obtener peso volumetric configurado*/
	@SuppressWarnings("rawtypes")
	public double getConfigPesoVolumetrico() {
		double result = 5000;
		Connection con = null;
		try {
			con = ConnectDB.getConnection();
			ConsultaParametros cons = new ConsultaParametros();
			ArrayList temp = cons.QryMdulType(con, "BOK", "FACTOR_PESO_VOL");
			if (!temp.isEmpty()) {
				result = temp.isEmpty() ? 5000 :(Double) Double.valueOf(((ArrayList) temp .get(0)).get(2).toString());// obteniendo PM_VLUE1_ID	
			}
			
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getConfigPesoVolumetrico()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.cerrarConexion(con);
		}
		return result;
	}

	public String getPesoMaximoTarifa7() {
		String maximo = "";
		try {			
			con = ConnectDB.getConnection();
			pstmt = con.prepareStatement(getPesoMaximoT7);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				maximo = rs.getString(1);
				if (maximo == null) {
					maximo = "";
				}
			}			
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("getPesoMaximoTarifa7()_Error:").append(e).toString());
			e.printStackTrace();
			return "0";
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();

			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("getPesoMaximoTarifa7()_Error:").append(e).toString());
				e.printStackTrace();
			}
		}
		return maximo;
	}

	// Verificar si el cliente asignado pertenece a una zona extendida
	public boolean isextendedzone(ConversionForm conversionform) {
		//String branchID = "";
		String olID = "";

		String clientNo = conversionform.getDestclave();
		boolean returnValue = false;
		try {
			con = ConnectDB.getConnection();
			pstmt = con.prepareStatement(verificazonaextendida);
			pstmt.setString(1, clientNo);
			pstmt.setString(2, "70");// AAP02
			pstmt.setString(3, conversionform.getDestcode());// AAP02

			rs = pstmt.executeQuery();
			while (rs.next()) {
				//branchID = rs.getString(1);
				olID = rs.getString(2) == null ? "" : rs.getString(2);
				conversionform.setOperadorLogistico(olID);
				returnValue = true;
			}
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("isextendedzone()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (rs != null)
					rs.close();
				if (con != null)
					con.close();
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("isextendedzone()_Error:").append(e).toString());
				e.printStackTrace();
			}
		}
		return returnValue;
	}

	// Fin verificar zona extendida

	public boolean zoneverify(String tracno, String orginbrnc, String destbrnc, String destsite) {
		
		try {			
			if (orginbrnc.equalsIgnoreCase(destbrnc)) {
				return true;
			}
			con = ConnectDB.getConnection();
			pstmt = con.prepareStatement(zoneverification);
			pstmt.setString(1, tracno);
			pstmt.setString(2, orginbrnc);
			pstmt.setString(3, destbrnc);
			pstmt.setString(4, destsite);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				if (rs.getString(1).equalsIgnoreCase("true")) {
					return true;
				} 
			}
			
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("zoneverify()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (rs != null)
					rs.close();
				if (con != null)
					con.close();

			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("zoneverify()_Error:").append(e).toString());
				e.printStackTrace();
			}
		}
		return false;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public PrepaidValueObject getDetail(String trackingno, String idClient) {
		PrepaidValueObject pObject = new PrepaidValueObject();
		try {
			con = ConnectDB.getConnection();
			pstmt = con.prepareStatement(checkGuiaExist);
			pstmt.setString(1, trackingno);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				pObject.setClientId(rs.getString(1));
				pObject.setBranchId(rs.getString(2));
				pObject.setRefNo(rs.getString(3));
				pObject.setTotalGuiaAmount(rs.getString(4));
				pObject.setZone(rs.getString(5));
				pObject.setWeight(rs.getString(6));
				pObject.setVolume(rs.getString(7));

				pObject.setDestSite(rs.getString(8));
				pObject.setDestclave(rs.getString(9));
				//AccessLog.Log("pObject.setDestclave" + pObject.getDestclave());
				pObject.setDestnombre(rs.getString(10));
				pObject.setDestSiteName(rs.getString(11));
				pObject.setClientName(rs.getString(12));
				pObject.setContent(rs.getString(13));
				pObject.setEad(rs.getString(14));
				pObject.setRad(rs.getString(15));
				pObject.setExt(rs.getString(16));
				pObject.setOldRefNo(rs.getString(17));
				// BGM_ACK_SERVICE, BGM_ACK_AMT, BGM_INSUR_SERVICE,
				// BGM_INSUR_AMT
				pObject.setAcklabels(rs.getString(18));
				pObject.setAckAmt(rs.getString(19));
				pObject.setValordeclarado(rs.getString(20));
				pObject.setTarifa(rs.getString(22));
				pObject.setDiscount(rs.getString(23));
				pObject.setShipType(rs.getString(24));//JSA01
				pObject.setCmpyId(rs.getString(25));
				pObject.setLocationType(rs.getString(26) == null ? "":rs.getString(26));//AAP20

			}
			
			if (pObject.getClientId().length() > 0) {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				ArrayList list = new ArrayList();
				boolean validateExclusive1D = false;
				if (!pObject.getShipType().equalsIgnoreCase("1D")) {
					pstmt = con.prepareStatement(fetchShip);
					pstmt.setString(1, pObject.getRefNo());
					rs = pstmt.executeQuery();

					while (rs.next()) {
						list.add(rs.getString(1));
					}
					// AccessLog.Log("after 3rdwhl in ConD");
					if (!list.isEmpty()) {
						String shipping[] = new String[list.size()];
						shipping = (String[]) list.toArray(shipping);
						pObject.setShippingType(shipping);
					}
					if (rs != null)
						rs.close();
					if (pstmt != null)
						pstmt.close();
				} else {
					validateExclusive1D = true;
				}
				// Cuando no encontro nada de los tipos de envio (antes de los
				// cambios del SEG) se procede a buscar en los nuevos tipos de
				// envios
				if (list.isEmpty()) {
					if (validateExclusive1D) {
						pstmt = con.prepareStatement(fetchShipSEGByParmCode2);
						pstmt.setString(1, "DS");
					} else {
						pstmt = con.prepareStatement(fetchShipSEG);
						pstmt.setString(1, pObject.getRefNo());
					}
					rs = pstmt.executeQuery();
					List<ShipTypeSEG> listSEG = new ArrayList<ShipTypeSEG>();
					ShipTypeSEG shipTypeSEG = null;
					while (rs.next()) {
						list.add(rs.getString(1));
						shipTypeSEG = new ShipTypeSEG();
						shipTypeSEG.setShipTypeSEGSrvcDesc(rs.getString(1));
						shipTypeSEG.setShipTypeSEGSrvc(rs.getString(2));
						shipTypeSEG.setShipTypeSEGSrvcPP(rs.getString(3));
						listSEG.add(shipTypeSEG);
					}
					// AccessLog.Log("after 3rdwhl in ConD");

					String shipping[] = new String[list.size()];
					shipping = (String[]) list.toArray(shipping);
					pObject.setShippingType(shipping);
					pObject.setShippingTypeSEG(listSEG);

					if (rs != null)
						rs.close();
					if (pstmt != null)
						pstmt.close();
				}

				pstmt = con.prepareStatement(fetchAddress);
				pstmt.setString(1, pObject.getRefNo());
				rs = pstmt.executeQuery();
				while (rs.next()) {
					if (rs.getString(1).equalsIgnoreCase("FISCAL")) {
						pObject.setFiscaladdresscode(rs.getString(2));
						pObject.setFiscalcolonia1(rs.getString(3));
						pObject.setFiscalcolonia2(rs.getString(4));
						pObject.setFiscal1(rs.getString(5));
						pObject.setFiscal2(rs.getString(6));
						pObject.setFiscaltelefono(rs.getString(7));
					} else if (rs.getString(1).equalsIgnoreCase("ORIGIN")) {
						pObject.setOrgioncode(rs.getString(2));
						pObject.setOrgiencolonia1(rs.getString(3));
						pObject.setOrgiencolonia2(rs.getString(4));
						pObject.setOrgien1(rs.getString(5));
						pObject.setOrgien2(rs.getString(6));
						pObject.setOrgientelefono(rs.getString(7));
					}
				}

				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();

				pstmt = con.prepareStatement(fetchAddress);
				pstmt.setString(1, trackingno);
				rs = pstmt.executeQuery();
				
				while (rs.next()) {
					if (rs.getString(1).equalsIgnoreCase("DESTINATION")) {
						pObject.setDestcode(rs.getString(2));						
						pObject.setDestcolonia1(rs.getString(3));
						pObject.setDestcolonia2(rs.getString(4));
						pObject.setDest1(rs.getString(5));
						pObject.setDest2(rs.getString(6));
						pObject.setDesttelefono(rs.getString(7));
					} else if (rs.getString(1).equalsIgnoreCase("ORIGIN")) {
						pObject.setOrgioncode(rs.getString(2));
						pObject.setOrgiencolonia1(rs.getString(3));
						pObject.setOrgiencolonia2(rs.getString(4));
						pObject.setOrgien1(rs.getString(5));
						pObject.setOrgien2(rs.getString(6));
						pObject.setOrgientelefono(rs.getString(7));
					}

					pstmt = con.prepareStatement("select CM_TAX_ID from SYS_CLNT_MSTR where CM_CLNT_ID=?");

					pstmt.setString(1, pObject.getClientId());
					rs = pstmt.executeQuery();
					while (rs.next()) {
						pObject.setRefRFC(rs.getString(1));
					}					
				}
				if(validateIsTypeSEG(pObject.getShipType())) {
					cst=con.prepareCall("{ call PRO_GET_MAX_SEG(?,?,?,?,?)}");
					cst.setString(1,"PAQ");
					cst.registerOutParameter(2,Types.NUMERIC);
					cst.registerOutParameter(3,Types.NUMERIC);
					cst.registerOutParameter(4,Types.NUMERIC);
					cst.registerOutParameter(5,Types.NUMERIC);
					cst.execute();			
					pObject.setVolLMAX(cst.getDouble(2));
					pObject.setVolWMAX(cst.getDouble(3));
					pObject.setVolHMAX(cst.getDouble(4));
					pObject.setPesoMAX(cst.getDouble(5));
				} else {
					cst=con.prepareCall("{ call PRO_GET_MAX_STD_V2(?,?,?,?,?,?,?)}");
					cst.setString(1,"PAQ");
					cst.registerOutParameter(2,Types.NUMERIC);
					cst.registerOutParameter(3,Types.NUMERIC);
					cst.registerOutParameter(4,Types.NUMERIC);
					cst.registerOutParameter(5,Types.NUMERIC);
					cst.registerOutParameter(6, Types.NUMERIC);
					cst.setString(7, idClient);
					cst.execute();			
					pObject.setVolLMAX(cst.getDouble(2));
					pObject.setVolWMAX(cst.getDouble(3));
					pObject.setVolHMAX(cst.getDouble(4));
					pObject.setPesoMAX(cst.getDouble(5));
					pObject.setVolMAX(cst.getDouble(6));
				}
				
				//Obtiene medidas mínimas
				getMinMeasures(con, pObject);
				resources.cerrarCallableStatement(cst);
			} else
				return null;
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("PrepaidValueObject()_Error:").append(e).toString());
			e.printStackTrace();
			return null;
		} finally {
			resources.closeResources(rs, pstmt, cst);
			resources.cerrarConexion(con);
		}
		return pObject;
	}
	
	public String convert(PrepaidValueObject object, String TracNo,
			HttpSession sess, String serieCaja,
			ConversionForm conversionform, int numBultos) {
		String eadServie = null;
		String result = "";
		String modulo = "BOK";
		SucursalesConfiguradas suc = new SucursalesConfiguradas();
		String entrega = "";
		
		try {
			
			String formNumber = getFormNumber((String) sess.getAttribute("branchid"), modulo, serieCaja);
			if(formNumber !=null && formNumber.length()>0) {
				conversionform.setGuiaNo(formNumber);
			}
			
			con = ConnectDB.getConnection();
			cst = con.prepareCall("{ call pack_ppg_web_2012.PRO_ASSIGN_ORIGIN_BRNC(?,?) }");
			cst.setString(1, object.getClientId());
			cst.registerOutParameter(2, Types.VARCHAR);
			cst.execute();
			object.setBranchId(cst.getString(2));
			if (cst != null)
				cst.close();
			cst = con.prepareCall("{ call pack_ppg_web_2012.PRO_ASSIGN_DEST_BRNC(?,?,?,?) }");
			cst.setString(1, object.getDestclave());
			cst.setString(2, object.getDestcode());
			cst.registerOutParameter(3, Types.VARCHAR);
			cst.registerOutParameter(4, Types.VARCHAR);
			cst.execute();
			String nuevaSucursal = cst.getString(3);
			/* Valida si el env?o es ocurre */
			if (conversionform.getDeliveryType().equalsIgnoreCase("O")) {
				// Obtaining default delivery configuration
				entrega = "DEST_OCURRE";
				String nuevaSucursalDefault = suc.obtieneConfigSucursal(con, "BOK", entrega, object.getDestSite());

				// Obtaining specific delivery configurations
				String nuevaSucursalOcu = suc.obtieneConfigSucursalOcurre(con, conversionform.getDestclave(), conversionform.getDestcode());
				String nuevaSucursalEspec = suc.obtieneConfigSucursalEspec(con, modulo, "DEST_OCURRE_ESP", object.getDestSite());

				// Check if the client chose ocurre branch
				String[] brnchClave = conversionform.getBrnchOcurre().split("\\|");
				if (Boolean.TRUE.equals(conversionform.getOpcOcurre()) && !brnchClave[0].equals("")) {
				    nuevaSucursal = brnchClave[0];
				} else if (!nuevaSucursalOcu.isEmpty()) {
				    // Check if the exception branch site matches the original destination site
				    for (BranchDetailDTOResponse s : conversionform.getFilteredBrnch()) {
				        if (s.getClave().equalsIgnoreCase(nuevaSucursalOcu)) {
				            nuevaSucursal = nuevaSucursalOcu;
				        }
				    }
				    // Check if the exception branch meets the criteria
				    if (!nuevaSucursal.equalsIgnoreCase(nuevaSucursalOcu)) {
				        if (nuevaSucursalEspec.equalsIgnoreCase(nuevaSucursalOcu)) {
				            nuevaSucursal = nuevaSucursalEspec;
				        } else if (!nuevaSucursalDefault.isEmpty()){
				            nuevaSucursal = nuevaSucursalDefault;
				        }
				    }
				} else if (!nuevaSucursalOcu.isEmpty() && nuevaSucursalEspec.equals(nuevaSucursalOcu)) {
					/* Asigna sucursal especial */
					nuevaSucursal = nuevaSucursalEspec;
				} else if (!nuevaSucursalDefault.isEmpty()) {
				    nuevaSucursal = nuevaSucursalDefault;
				}

			}
			object.setDestbranch(nuevaSucursal);
			
			if (cst != null)
				cst.close();

			
			if (validaGH_FORM_NO(con, conversionform, "P")) {
				result = "EL NUMERO DE FORMA YA EXISTE";
			} else {
				String desc = "";
				cst = con.prepareCall(convertguia);			

				cst.setString(1, TracNo); //AccessLog.Log("1.convert()TracNo: " + TracNo);
				cst.setString(2, (String) sess.getAttribute("branchid")); //AccessLog.Log("2.convert()sess.getAttribute(\"branchid\"): "+ (String) sess.getAttribute("branchid"));
				cst.setString(3, object.getOrgioncode()); //AccessLog.Log("3.convert()object.getOrgioncode(): " + object.getOrgioncode());AccessLog.Log("4.ANTESS=====>convert()object.getValordeclarado(): " + object.getValordeclarado());
				String valorDeclarado = object.getValordeclarado() == null?"0.0": (object.getValordeclarado().isEmpty()?"0.0":object.getValordeclarado());//JSA01
				cst.setDouble(4, Double.parseDouble(valorDeclarado)); //AccessLog.Log("4.convert()object.getValordeclarado(): " + object.getValordeclarado());//JSA01
				cst.setString(5, object.getCobertura()); //AccessLog.Log("5.convert()object.getCobertura(): " + object.getCobertura());
				//cst.setDouble(6, Double.parseDouble(tax[0]));//AAP20 No se esta usando
				cst.setDouble(6, 0);//AAP20
				cst.setDouble(7, Double.parseDouble(object.getInsTaxRet())); //AccessLog.Log("7.convert()object.getInsTaxRet(): " + object.getInsTaxRet());
				cst.setDouble(8, Double.parseDouble(object.getInsuranceSubTotal())); //AccessLog.Log("8.convert()object.getInsuranceSubTotal(): " + object.getInsuranceSubTotal());
				cst.setString(9, desc); //AccessLog.Log("9.convert()desc: " + desc);
				//AccessLog.Log("object.getDestbranch**************************" + object.getDestbranch());
				cst.setString(10, object.getDestbranch()); //AccessLog.Log("10.convert()object.getDestbranch(): " + object.getDestbranch());
				cst.setString(11, object.getDestSite()); //AccessLog.Log("11.convert()object.getDestSite(): " + object.getDestSite());
				cst.setString(12, object.getDestclave()); //AccessLog.Log("12.convert()object.getDestclave(): " + object.getDestclave());
				cst.setString(13, object.getDestcode()); //AccessLog.Log("13.convert()object.getDestcode(): " + object.getDestcode());
				//AccessLog.Log("beforeOUT VAR");
				cst.registerOutParameter(14, Types.VARCHAR);
				//AccessLog.Log("14.convert()Types.VARCHAR out: ");
				cst.setString(15, ((Global) sess.getAttribute("sGlobal")).getOrigenUserClave()); //AccessLog.Log("15.convert()((Global) sess.getAttribute(\"sGlobal\")).getOrigenUserClave(): " + ((Global) sess.getAttribute("sGlobal")).getOrigenUserClave());
				cst.registerOutParameter(16, Types.VARCHAR);//AccessLog.Log("16.convert()Types.VARCHAR out: ");
				cst.setString(17, object.getContent());//AccessLog.Log("17.convert()object.getContent(): "+ object.getContent());
				cst.setString(18, object.getInsTax()); //AccessLog.Log("18.convert()object.getInsTax(): " + object.getInsTax());
				cst.setString(19, object.getClientId()); //AccessLog.Log("19.convert()object.getClientId(): " + object.getClientId());
				cst.setString(20, object.getPackType()); //AccessLog.Log("20.convert()object.getPackType(): " + object.getPackType());
				if ((object.getDeliveryType().equalsIgnoreCase("H"))
						|| (object.getDeliveryType().equalsIgnoreCase("X"))) {
					eadServie = "1";
				} else {
					eadServie = "0";
				}
				cst.setString(21, eadServie); //AccessLog.Log("21.convert()eadServie: " + eadServie);
				// Added on 14/07/2010 - For kitsId : 70454 Ends
				String colonyerror = (String) sess.getAttribute("colonyerror");
				//AccessLog.Log("before coloneyerror");
				String comt = "";
				if ((colonyerror != null) && (colonyerror.length() > 0)) {
					// cst.setString(22,"Entregar Ocurre, no existe Zona de EAD para esa colonia");
					comt = "Entregar Ocurre, no existe Zona de EAD para esa colonia";
				}

				if (conversionform.getCheckRefDir().equals("V")) {
					if (comt.length()>0) {
						comt = cnct.delete(0, cnct.length()).append(comt).append(". ").toString();
					}
					comt = cnct.delete(0, cnct.length()).append(comt).append(conversionform.getDestRefDom()).append(". Telefono: ").append(conversionform.getDesttelefono()).toString();
				}
				
				if (conversionform.getComments().length()>0) {
					if (comt.length()>0) {
						comt = cnct.delete(0, cnct.length()).append(comt).append(". ").toString();
					}
					comt = cnct.delete(0, cnct.length()).append(comt).append(conversionform.getComments()).toString();
				}
				
				if (comt.length()>255) {
					comt = comt.substring(0,255);
				}
				
				cst.setString(22, comt); //AccessLog.Log("22.convert() comt: " + comt);
				cst.setString(23, serieCaja); //AccessLog.Log("23.convert()serieCaja: " + serieCaja);
				cst.registerOutParameter(24, Types.VARCHAR);
				cst.setString(25, conversionform.getGuiaNo());
				cst.setString(26, String.valueOf(numBultos));
			    cst.setString(27, conversionform.isConfirmationService2D()?conversionform.getShipTypeSEGPPChange():null);//JAS01
			    Double volL, volH, volW, weightVolumetric;
			    volL = Double.valueOf(conversionform.getVolL() == null? "0.0" : (conversionform.getVolL().isEmpty()? "0.0" : conversionform.getVolL()));
			    volH = Double.valueOf(conversionform.getVolH() == null? "0.0" : (conversionform.getVolH().isEmpty()? "0.0" : conversionform.getVolH()));
			    volW = Double.valueOf(conversionform.getVolW() == null? "0.0" : (conversionform.getVolW().isEmpty()? "0.0" : conversionform.getVolW()));
			    weightVolumetric = Double.valueOf(conversionform.getWeightVolumetric() == null? "0.0" : (conversionform.getWeightVolumetric().isEmpty()? "0.0" : conversionform.getWeightVolumetric()));
			    if(volL > 0 && volH > 0 && volW > 0)
			    {
				    cst.setDouble(28, volL);//JAS01
				    cst.setDouble(29, volH);//JAS01
				    cst.setDouble(30, volW);//JAS01
			    } else {
				    cst.setNull(28, Types.NUMERIC);
				    cst.setNull(29, Types.NUMERIC);
				    cst.setNull(30, Types.NUMERIC);
			    }
			    if(weightVolumetric > 0 && conversionform.getShipType().equalsIgnoreCase("ST")) {
					JavTariff tariff = new JavTariff();
					String tarif = tariff.getTipoTarifa(con, Double.valueOf(conversionform.getWeight()), Double.valueOf(conversionform.getVolume().replace(',', '.')));
					conversionform.setTarifa(tarif);
				}
			    cst.setString(31, conversionform.getTarifa());//JAS02
			    Double pesoReal = Double.valueOf(conversionform.getWeight()), pesoVol = 0D;
			    if(conversionform.getWeightVolumetric() != null && !conversionform.getWeightVolumetric().isEmpty()) {
			    	pesoVol = Double.valueOf(conversionform.getWeightVolumetric());
			    }
			    cst.setString(32, conversionform.getShipType().equalsIgnoreCase("ST") ? pesoReal.toString() : (pesoReal > pesoVol  ? pesoReal.toString() : pesoVol.toString()));//JAS01
			    cst.setString(33, conversionform.getVolume().replace(",", "."));//JAS02
			    cst.setString(34, conversionform.getProductIdSat());
			    cst.setString(35, conversionform.getPedinumber().trim().toUpperCase());
			    cst.setString(36, conversionform.getCustagent().trim().toUpperCase());
			    cst.executeQuery();
				
				result = cst.getString(14);
				//AccessLog.Log("#$#$#$#$#$#$#$#$#$#$# cst.getString(24): " + cst.getString(24));
				
				if (result != null)
					return result;

			}
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("convert()_Error:").append(e).toString());
			e.printStackTrace();
			return (cnct.delete(0, cnct.length()).append(msgErr).append("convert()_Error:").append(e).toString());
		} finally {
			try {
				resources.cerrarCallableStatement(cst);
				resources.cerrarConexion(con);				
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("convert()_Error:").append(e).toString());
				e.printStackTrace();
				return (e.getMessage());
			}
		}
		return result;
	}

	private String getRFC(Connection conn, String clientID) {
		String rfc = "";
		try {
//			if (con == null)
//				con = ConnectDB.getConnection();
			pstmt = conn.prepareStatement(getrfc);
			pstmt.setString(1, clientID);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				rfc = rs.getString(1);
				if (rfc == null) {
					rfc = "";
				}
			}
			
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("getRFC()_Error:").append(e).toString());
			e.printStackTrace();
			return "";
		} finally {
			try {
				resources.closeResources(rs, pstmt);
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("getRFC()_Error:").append(e).toString());
				e.printStackTrace();
			}
		}
		return rfc;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ArrayList getZones() {
		ArrayList list = new ArrayList();
		ArrayList mainlist = new ArrayList();
		ArrayList list1 = new ArrayList();
		try {
			con = ConnectDB.getConnection();
			pstmt = con.prepareStatement(getzones);

			rs = pstmt.executeQuery();
			mainlist.add("Sel zona");
			list1.add("-1");
			while (rs.next()) {
				mainlist.add(rs.getString(1));
				list1.add(rs.getString(2));
			}
			
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("getZones()_Error:").append(e).toString());
			e.printStackTrace();
			return null;
		} finally {
			try {
				resources.closeResources(rs, pstmt);
				resources.cerrarConexion(con);				
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("getZones()_Error:").append(e).toString());
				e.printStackTrace();
			}
		}

		list.add(mainlist);
		list.add(list1);
		return list;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList getZonesIds() {
		ArrayList list = new ArrayList();
		try {
			con = ConnectDB.getConnection();
			pstmt = con.prepareStatement(getzoneIds);

			rs = pstmt.executeQuery();
			list.add("Sel tarifa");
			while (rs.next()) {
				list.add(rs.getString(1));
			}			
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("getZonesIds()_Error:").append(e).toString());
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (rs != null)
					rs.close();
				if (con != null)
					con.close();

			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("getZonesIds()_Error:").append(e).toString());
				e.printStackTrace();
			}
		}
		return list;
	}

	public String print(String number) {
		String result = "";
		try {
			con = ConnectDB.getConnection();
			cst = con.prepareCall(print);
			cst.setString(1, number);
			cst.registerOutParameter(2, Types.VARCHAR);
			cst.executeQuery();
			result = cst.getString(2);

		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("print()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				if (cst != null)
					cst.close();
				if (rs != null)
					rs.close();
				if (con != null)
					con.close();
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("print()_Error:").append(e).toString());
				e.printStackTrace();
			}
		}
		return result;
	}

	private String getGroupClientId(String clientId, Connection conn) {
		String groupClientId = "";
		PreparedStatement pst = null;
		ResultSet rst = null;
		try {
			String groupIdQuery = "select pack_web.FUN_CHCK_GRUP_CLNT(?)as groupid from dual";// Checked
			pst = conn.prepareStatement(groupIdQuery);
			pst.setString(1, clientId);
			rst = pst.executeQuery();
			
			while (rst.next()) {
				groupClientId = rst.getString("groupid");
			}			
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("getGroupClientId()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				if (rst != null)
					rst.close();
				if (pst != null)
					pst.close();
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("getGroupClientId()_Error:").append(e).toString());
				e.printStackTrace();
			}
		}
		return groupClientId.toUpperCase();
	}


	public void calculateInsuranceAmount(Connection conn, PrepaidValueObject vObject) {
		
		try {
			//String clientId;
			//String subTotalInv = null;
			String subtotal = "";
			String[] amount_totalInv = null;// 0--- subTotal, 1---- Minamount
			String discountInv[] = new String[2];
			double taxableAmountInv = 0.0;
			//String insuranceView = "";
			//double totalQty = 0.0;
			//String valor = "";
			//String cober = "";
			java.text.DecimalFormat df = new java.text.DecimalFormat("0.00");

			{
				amount_totalInv = getAditionalServicesTotal(conn, "", vObject
						.getValordeclarado().trim(), vObject.getCobertura().trim(), 
						"INV", //vObject.getClientId(),
						vObject.getBranchId(), vObject.getDestbranch());
				subtotal = amount_totalInv[0];
				if (Double.parseDouble(subtotal) > 0)
					discountInv = calculateQuantity(conn,
							getGroupClientId(vObject.getClientId(), conn),
							"INV", amount_totalInv[0], 
							vObject.getValordeclarado().trim(), "0");
				else {
					discountInv[0] = null;
					discountInv[1] = "0.00";
				}
				// discountInv =
				// calculateQuantity(con,groupClientId,aform.getCobertura(),amount_totalInv[0],aform.getValordeclarado().trim(),"0");
				// commented and added prev line by amal
				//insuranceView = getInsuranceViewFlag(vObject.getClientId(), conn);
				taxableAmountInv = Double.parseDouble(subtotal) - Double.parseDouble(discountInv[1]);
			}

			String taxForInv[] = getTax(conn, "INV", 
					df.format(taxableAmountInv), vObject.getBranchId(),
					vObject.getDestbranch(), vObject.getClientId());
			vObject.setInsuranceSubTotal(Double.toString(taxableAmountInv));
			
			vObject.setInsTax(taxForInv[0]);
			vObject.setInsTaxRet(taxForInv[1]);
			vObject.setDiscount(discountInv[1]);

		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("calculateInsuranceAmount()_Error:").append(e).toString());
			e.printStackTrace();
		}
	}


	public String[] getAditionalServicesTotal(Connection conn, String lcZone,
			String totalAmount, String serviceId, String referenceServiceId,
			String branch, String destbranch) {

		String[] amount_total = new String[2];
		CallableStatement cstt = null;
		try {
			String query = "Begin ? := pack_web.FUN_FTCH_ADD_SRVC(?,?,?,?,?,?,?,?); End;";// Checked
			cstt = conn.prepareCall(query);
			cstt.registerOutParameter(1, Types.NUMERIC);
			cstt.setString(2, serviceId);
			cstt.setString(3, referenceServiceId);
			cstt.setString(4, branch);
			cstt.setString(5, destbranch);
			cstt.setString(6, lcZone);
			
			if (totalAmount != null && totalAmount.length() == 0)
				// cst.setDouble(7,0.0d);//Empty
				cstt.setNull(7, Types.NUMERIC);
			else
				cstt.setDouble(7, Double.parseDouble(totalAmount));
			cstt.setString(8, "NON");

			cstt.registerOutParameter(9, Types.NUMERIC);

			cstt.executeQuery();
			java.text.DecimalFormat df = new java.text.DecimalFormat("0.00");
			
			amount_total[0] = df.format(cstt.getDouble(1));
			amount_total[1] = df.format(cstt.getDouble(9));			
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("getAditionalServicesTotal()_Error1:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				if (cstt != null)
					cstt.close();
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("getAditionalServicesTotal()_Error2:").append(e).toString());
				e.printStackTrace();
			}
		}
		return amount_total;
	}

	private String[] calculateQuantity(Connection conn, String grpclientId,
			String serviceId, String qty, String amount, String minAmount) {			
		String a[] = new String[2];
		CallableStatement cstt = null;
		try {
			java.text.DecimalFormat df = new java.text.DecimalFormat("0.00");
			String query = "Begin ? := pack_web.fun_calc_disc(?,?,?,?,?,?); End; ";
			
			cstt = conn.prepareCall(query);
			
			cstt.registerOutParameter(1, Types.NUMERIC);
			cstt.setString(2, serviceId);
			cstt.setString(3, grpclientId);

			if (amount != null && amount.length() == 0)
				// cst.setDouble(4,0.0d);//Empty
				cstt.setNull(4, Types.NUMERIC);
			else
				cstt.setDouble(4, Double.parseDouble(amount));
			if (qty != null && qty.length() == 0)
				// cst.setDouble(5,0.0d);//Empty
				cstt.setDouble(5, Types.NUMERIC);
			else
				cstt.setDouble(5, Double.parseDouble(qty));
			if (minAmount != null && minAmount.length() == 0)
				// cst.setDouble(6,0.0d);//Empty
				cstt.setNull(6, Types.NUMERIC);
			else
				cstt.setDouble(6, Double.parseDouble(minAmount));
			cstt.registerOutParameter(7, Types.VARCHAR);

			cstt.executeQuery();
			
			a[0] = cstt.getString(7);
			a[1] = df.format(cstt.getDouble(1));		
		
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("calculateQuantity()_Error1:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				if (cstt != null)
					cstt.close();
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("calculateQuantity()_Error2:").append(e).toString());
				e.printStackTrace();
			}
		}
		return a;
	}

	private String[] getTax(Connection conn,
	String referenceServiceId, String taxableAmount, String branch,
			String destbranch, String clientId) {

		String taxQuery = "{call pack_sipweb.PRO_CALC_TAX_AMT(?,?,?,?,?)}";// Checked
		CallableStatement cstt = null;
		try {			

			boolean isOrgionBorderBranch = isBorderBranch(conn, branch);
			boolean isDestinationBorderBranch = isBorderBranch(conn, destbranch);

			cstt = conn.prepareCall(taxQuery);
			if (isOrgionBorderBranch && !isDestinationBorderBranch) {
				cstt.setString(1, destbranch);
			} else {
				cstt.setString(1, branch);
			}

			cstt.setString(2, referenceServiceId);
			cstt.setDouble(3, Double.parseDouble(taxableAmount));
			cstt.registerOutParameter(4, Types.NUMERIC);
			cstt.registerOutParameter(5, Types.NUMERIC);
			cstt.executeQuery();

			java.text.DecimalFormat df = new java.text.DecimalFormat("0.00");
			String tax[] = new String[2];
			tax[0] = df.format(cstt.getDouble(4));

			String clientType = "";
			clientType = getClientType(conn, clientId);

			String rfc = getRFC(conn, clientId);

			if (clientType != null && clientType.equalsIgnoreCase("C")
					|| clientType != null && clientType.equalsIgnoreCase("G")) {

				if (rfc == null || rfc.length() != 14)// added by Emerson on 05/02/2004
					tax[1] = "0";
				else
					tax[1] = df.format(cstt.getDouble(5));
			} else {
				tax[1] = "0";
			}
			if (cstt != null)
				cstt.close();
			return tax;
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("[]getTax()_Error1:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				if (cstt != null)
					cstt.close();
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("[]getTax()_Error2:").append(e).toString());
				e.printStackTrace();
			}	
		}		
		return null;
	}

	private boolean isBorderBranch(Connection conn, String branchId) {
		// Global global = (Global)session.getAttribute("sGlobal");
		PreparedStatement pst = null;
		ResultSet rst = null;
		boolean band = false;
		try {
			String query = "select pack_web.fun_brdr_brnc(?) from dual ";
			pst = conn.prepareStatement(query);
			pst.setString(1, branchId);
			rst = pst.executeQuery();
			if (rst.next()) {
				if (rst.getString(1).equalsIgnoreCase("BR")) {
					band = true;
				} 
			}	
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("isBorderBranch()_Error1:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				if (rst != null) {
					rst.close();
				}
				if (pst != null) {
					pst.close();
				}
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("isBorderBranch()_Error2:").append(e).toString());
				e.printStackTrace();
			}
		}
		return band;
	}

	private String getClientType(Connection conn, String clientId) {
		String clientType = null;
		PreparedStatement pst = null;
		ResultSet rst = null; 
		try {
			String query = "SELECT cm_clnt_type FROM SYS_CLNT_MSTR WHERE cm_clnt_id = ?";
			pst = conn.prepareStatement(query);
			pst.setString(1, clientId);

			rst = pst.executeQuery();
			
			if (rst.next()) {
				clientType = rst.getString(1);
			}			
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("getClientType()_Error1:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				if (rst != null)
					rst.close();
				if (pst != null)
					pst.close();	
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("getClientType()_Error2:").append(e).toString());
				e.printStackTrace();
			}			
		}
		return clientType;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList fetchPackageType() {
		ArrayList alPackType = new ArrayList(50);

		ArrayList alPackTypeLable = new ArrayList(50);
		ArrayList alPackTypeName = new ArrayList(50);

		try {
			String query = "SELECT SS_CODE,SS_DESC FROM SYS_SHP_DESC_MSTR WHERE SS_SRVC_ID = ? ORDER BY TO_NUMBER(SS_CODE)";
			con = ConnectDB.getConnection();
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, "SHP-G");
			
			rs = pstmt.executeQuery();

			while (rs.next()) {
				alPackTypeLable.add(rs.getString(1));
				alPackTypeName.add(rs.getString(2));
			}

			alPackType.add(alPackTypeLable);
			alPackType.add(alPackTypeName);
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("fetchPackageType()_Error1:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("fetchPackageType()_Error2:").append(e).toString());
				e.printStackTrace();
			}
		}
		return alPackType;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList fetchDeliveryType() {
		ArrayList delvryType = new ArrayList();
		ArrayList delvryDesc = new ArrayList();
		ArrayList delvryCode = new ArrayList();
		
		try {
			con = ConnectDB.getConnection();
			pstmt = con.prepareStatement(fetchDelvryType);
			pstmt.setString(1, "DELIVERY");
			rs = pstmt.executeQuery();			
			while (rs.next()) {
				delvryDesc.add(rs.getString(1));
				delvryCode.add(rs.getString(2));
			}
			delvryType.add(delvryDesc);
			delvryType.add(delvryCode);
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("fetchDeliveryType()_Error1:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();				
				if (con != null)
					con.close();
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("fetchDeliveryType()_Error2:").append(e).toString());
				e.printStackTrace();
			}
		}
		return delvryType;
	}

	public boolean delvryVerify(PrepaidValueObject pObject) {
		boolean result = false;
		//String clientCity = null;
		String clientColonia = null;
		try {

			con = ConnectDB.getConnection();
			pstmt = con.prepareStatement(destClientColonia);
			pstmt.setString(1, pObject.getDestclave());
			pstmt.setString(2, pObject.getDestcode());

			rs = pstmt.executeQuery();
			while (rs.next()) {
				clientColonia = rs.getString(1);
			}
			
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();

			//String destEadSrvc = "SELECT bc_brnc_id FROM er_brnc_coly_mstr WHERE bc_gety_code_4 = ? AND bc_gety_code_6 = ? AND bc_brnc_id like ? AND bc_ead_rad_apply = ?";
			String destEadSrvc = "SELECT bc_brnc_id FROM er_brnc_coly_mstr WHERE bc_gety_code_6 = ? AND bc_brnc_id like ? AND bc_ead_rad_apply = ?";
			pstmt = con.prepareStatement(destEadSrvc);
			//pstmt.setString(1, clientCity);
			pstmt.setString(1, clientColonia);
			pstmt.setString(2,
					cnct.delete(0, cnct.length()).append(pObject.getDestSite())
							.append("%").toString());
			pstmt.setString(3, "Y");
			
			rs = pstmt.executeQuery();

			while (rs.next()) {
				result = true;
			}
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("delvryVerify()_Error1:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();			
				if (con != null)
					con.close();
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("delvryVerify()_Error2:").append(e).toString());
				e.printStackTrace();
			}
		}
		return result;
	}

	public boolean eadVerify(PrepaidValueObject pObject) {
		boolean result = false;
		String eadValue = null;
		try {
			con = ConnectDB.getConnection();
			pstmt = con.prepareStatement(chkEadAvl);
			pstmt.setString(1, pObject.getDestclave());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				eadValue = rs.getString(1);
			}
			
			if (eadValue!=null && eadValue.equalsIgnoreCase("y")) {
				result = true;
			}			
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("eadVerify()_Error1:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();				
				if (con != null)
					con.close();
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("eadVerify()_Error2:").append(e).toString());
				e.printStackTrace();
			}
		}
		return result;
	}
	

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList getTarifas() {
		ArrayList list = new ArrayList(8);
		ArrayList mainlist = new ArrayList(1);
		//ArrayList list1 = new ArrayList();
		try {
			con = ConnectDB.getConnection();
			
			pstmt = con.prepareStatement(gettarifas);
			pstmt.setString(1, "PACKETS");
			rs = pstmt.executeQuery();
			while (rs.next()) {
				list.add(rs.getString(1));
			}
			
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("getTarifas()_Error1:").append(e).toString());
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();				
				if (con != null)
					con.close();
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("getTarifas()_Error2:").append(e).toString());
				e.printStackTrace();
			}
		}
		mainlist.add(list);
		// mainlist.add(list1);
		return mainlist;
	}

	// CenitSoft
	// Obtener los maximo de la tarifa
	// Uso: valor=getPexoMaximo("T2","CUM");
	// KG para Kilos, CUM para Volumen
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList getMaximos(String tipo) {
		ArrayList lista = new ArrayList();
		String maximo = "";
		try {			
			con = ConnectDB.getConnection();
			pstmt = con.prepareStatement(getPesoMaximo);
			pstmt.setString(1, tipo);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				maximo = rs.getString(1);
				if (maximo == null) {
					maximo = "";
				}
				lista.add(maximo);
			}			
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("getMaximos()_Error1:").append(e).toString());
			e.printStackTrace();
			lista.add(e.getMessage());
			return lista;
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("getMaximos()_Error2:").append(e).toString());
				e.printStackTrace();
			}
		}
		lista.add("ALGO");
		return lista;
	}

	// Fin obtener los maximos de la tarifa

	// Agregado 18-Oct-2011
	// Reconvertir la guia
	public String reInsertRecord(PrepaidGuiaMstr object, //HttpSession sess,
			String guiaNo, //String tipoTarifa, 
			String fiscalAddrCode, String origenAddrCode) {
		//CommonFunction commonobject = new CommonFunction();
		//String seq = "";
		String result = null;
		Date today = null;
		try {
			con = ConnectDB.getConnection();
			// inserta el registro de la guia prepagada
			cst = con.prepareCall(reinsertrecord);

			cst.setString(1, object.getBgm_ref_no());
			// cst.setString(2, (String)sess.getAttribute("branchid"));
			cst.setString(2, object.getBgm_prep_brnc_id());
			today = DateUtil.getDateValue("");
			cst.setDate(3, today);
			cst.setString(4, object.getBgm_orgn_brnc_id());
			cst.setString(5, object.getBgm_clint_id());
			cst.setDouble(6, object.getBgm_no_of_guis());
			cst.setString(7, object.getBgm_zone());
			cst.setDouble(8, object.getBgm_wght());
			cst.setString(9, object.getBgm_vlum());
			cst.setDouble(10, object.getBgm_gh_amt());
			cst.setString(11, object.getBgm_valid_flg());
			cst.setString(12, object.getBgm_org_site_id());
			cst.setString(13, object.getBgm_ship_type());
			today = DateUtil.getDateValue("");
			cst.setDate(14, today);
			cst.setString(15, object.getCrtd_by());
			cst.setString(16, object.getBgm_ack_service());
			cst.setDouble(17, object.getBgm_ack_amt());
			cst.setDouble(18, object.getBgm_ack_tax());
			cst.setString(19, object.getBgm_insur_service());
			cst.setDouble(20, object.getBgm_insur_amt());
			cst.setDouble(21, object.getBgm_insur_tax());
			cst.setDouble(22, object.getBgm_dsc_percent());
			cst.setDouble(23, object.getBgm_dsc_amt());
			cst.setString(24, object.getBgm_ead_service());
			cst.setDouble(25, object.getBgm_ead_amt());
			cst.setString(26, object.getBgm_rad_service());
			cst.setDouble(27, object.getBgm_rad_amt());
			cst.setString(28, guiaNo);
			cst.registerOutParameter(29, Types.VARCHAR);
			cst.registerOutParameter(30, Types.VARCHAR);
			cst.setString(31, object.getBgm_ext_service());
			cst.setDouble(32, object.getBgm_ext_amt());
			cst.setString(33, object.getBgm_tarifa());

			cst.setString(34, fiscalAddrCode);
			cst.setString(35, origenAddrCode);
			cst.setString(36, object.getLocationType());//AAP20
			
			cst.executeQuery();
			result = cst.getString(29);
			//seq = cst.getString(30);
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("reInsertRecord()_Error1:").append(e).toString());
			e.printStackTrace();
			result = cnct.delete(0, cnct.length()).append(e.getMessage()).append(" error de procedimiento").toString();
		} finally {
			try {				
				if (cst != null)
					cst.close();				
				if (con != null)
					con.close();
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("reInsertRecord()_Error2:").append(e).toString());
				e.printStackTrace();
			}
		}
		return result;
	}

	// Agregado 25-Oct-2011
	// ReFacturar la guia
	public String reFactura(PrepaidGuiaMstr object, HttpSession sesion,
			HttpServletRequest request, String guiaNo, String payMode,
			String formNo, String newClientID, String taxBrnc/*AAP20*/) {
		
		Date today = null;
		float taxPerc = 0;
		float taxReturnPerc = 0;
		float taxAmt = 0;
		float taxRetAmt = 0;
		float amount = 0;
		float netAmount = 0;
		DecimalFormat dc = new DecimalFormat("0.00");
		// Agregado CenitSoft
		double totalEnvio = 0;
		double totalACK = 0;
		double totalInsurance = 0;
		double totalEAD = 0;
		double totalRAD = 0;
		double totalEXT = 0;
		double totalGlobal = 0;
		String seqNum = "";
		totalEnvio = object.getBgm_gh_amt();
		totalACK = 0;
		if (object.getBgm_ack_service() != null)
			totalACK = object.getBgm_ack_amt();
		totalInsurance = object.getBgm_insur_amt();
		totalEAD = object.getBgm_ead_amt();
		totalRAD = object.getBgm_rad_amt();
		totalEXT = totalEXT + (object.getBgm_ext_amt() * 1);
		totalGlobal = totalEnvio + totalACK + totalEAD + totalRAD + totalInsurance + totalEXT;
		amount = Float.parseFloat(Double.toString(totalGlobal).replace(',', '.'));
		ResultSet rsRef = null;
		try {

			con = ConnectDB.getConnection();
			con.setAutoCommit(false);

			// cambiado al principio para evitar folios duplicados			
			if (pstmt != null)
				pstmt.close();
			if (!payMode.equals("PAID")) {//Solo cuando es de contado vendr? folio de factura
				String updateFormNo = "update sys_parm_mstr set pm_vlue1_id = ?, MDFD_ON = sysdate, MDFD_BY = ? where pm_parm_type = ? and pm_parm_code1 = ? and pm_vlue2_id = ?";
				pstmt = con.prepareStatement(updateFormNo);
				pstmt.setString(1, formNo);
				pstmt.setString(2, ((Global) sesion.getAttribute("sGlobal")).getOrigenUserClave());
				pstmt.setString(3, "CFD_NO");
				pstmt.setString(4, (String) sesion.getAttribute("branchid"));
				pstmt.setString(5, "9");
				pstmt.executeQuery();
				if (pstmt != null)
					pstmt.close();
			}
			// cambiado al inicio para evitar folios duplicados
			String invoiceSeq = "SELECT pack_ppg_web_2012.FUN_INV_SQNUM,sysdate FROM DUAL";
			pstmt = con.prepareStatement(invoiceSeq);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				seqNum = rs.getString(1);
				today = rs.getDate(2);
			}
			
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
			
			pstmt = con.prepareStatement(selectTax);

			pstmt.setString(1, taxBrnc.substring(0, 3));//AAP20
			rs = pstmt.executeQuery();
			while (rs.next()) {
				taxPerc = rs.getFloat(1);
				taxReturnPerc = rs.getFloat(2);
			}
			
			taxAmt = (amount * taxPerc) / 100;

			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();

			String buscaRFCCliente = "select cm_tax_id, cm_ret_flag from sys_clnt_mstr where cm_clnt_id = ?";
			String rfcCliente = "";
			
			pstmt = con.prepareStatement(buscaRFCCliente);
			pstmt.setString(1, newClientID);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				rfcCliente = rs.getString(1) == null ? "" : rs.getString(1);
			}
			
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
			
			if (rfcCliente.indexOf("-") > -1)
				rfcCliente = rfcCliente.replace("-", "");
			String rfcNuevo = "";
			String strCaracter;
			if (rfcCliente.length() > 1) {
				for (int i = 0; i <= rfcCliente.length() - 1; i++) {
					if (rfcCliente.charAt(i) != ' ') {
						strCaracter = rfcCliente.substring(i, i + 1);
						rfcNuevo = rfcNuevo.concat(strCaracter);
					}
				}
			} else {
				rfcNuevo = "XAXX010101000";
			}
			rfcCliente = rfcNuevo;

			String buscaCliente = "select cm_clnt_type from sys_clnt_mstr where cm_clnt_id = ?";
			String tipoCliente = "";
			pstmt = con.prepareStatement(buscaCliente);

			pstmt.setString(1, newClientID);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				tipoCliente = rs.getString(1);
			}
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
			if (tipoCliente.indexOf("I") > -1) {
				taxRetAmt = 0;
			} else {
				Rfc r = new Rfc();
				boolean rfcGenerico = r.isRfcGenerico(rfcCliente);
				if (!rfcGenerico) {
					taxRetAmt = (Float.parseFloat(Double.toString(totalEnvio)) * taxReturnPerc) / 100;
				} else {
					taxRetAmt = 0;
				}
			}
			
			
			amount = (float)Math.round(amount * 100) / 100;//REDONDEA A 2 DIGITOS
			taxAmt = (float)Math.round(taxAmt * 100) / 100;//REDONDEA A 2 DIGITOS
			taxRetAmt = (float)Math.round(taxRetAmt * 100) / 100;//REDONDEA A 2 DIGITOS
			netAmount = (amount+taxAmt);//-taxRetAmt;
			netAmount = (float)Math.round(netAmount * 100) / 100;//REDONDEA A 2 DIGITOS
			
			HashMap<String, ArrayList<Object>> mapService = getServiceConfig(con, newClientID, object.getCompanyId(), "BOOKING", true);//AAP23
			
			if (mapService != null && !mapService.isEmpty()) {//AAP23
				//actualiza tasas de iva retenido y empresas para servicios por set de pp.
				serviceRecalc(con, object.getCompanyId(), newClientID, "BOOKING", object.getBgm_ref_no(), mapService);

				float shpGTaxRet = (float)totalEnvio * ((float)getTaxRetPercConfig(mapService.get("SHP-G")) /100);
				float EADTaxRet = (float) totalEAD * ((float)getTaxRetPercConfig(mapService.get("EAD"))/100);
				float RADTaxRet = (float) totalRAD * ((float)getTaxRetPercConfig(mapService.get("RAD"))/100);
				float INVTaxRet = (float) totalInsurance * ((float)getTaxRetPercConfig(mapService.get("INV"))/100);
				float ACKTaxRet = (float) totalACK * ((float)getTaxRetPercConfig(mapService.get("ACK"))/100);
				float EXTTaxRet = (float) totalEXT * ((float)getTaxRetPercConfig(mapService.get("EXT"))/100);
				
				taxRetAmt = shpGTaxRet + EADTaxRet + RADTaxRet + INVTaxRet + ACKTaxRet + EXTTaxRet;
				taxRetAmt = (float)Math.round(taxRetAmt * 100) / 100;//REDONDEA A 2 DIGITOS
			}
			
			float aux = (float)Math.round(((amount+taxAmt)-taxRetAmt) *100) / 100;
			// Insertar en PPG INV PMT HDR
			String prepareInvoice = cnct.delete(0, cnct.length()).append("INSERT INTO PPG_INV_PMT_HDR(IPH_INV_NO,IPH_BRANCH_ID,IPH_SITE_ID,IPH_INV_DATE,IPH_CLINT_ID,")
					.append("IPH_INV_ACT_FLG,IPH_INV_GROSS_AMT,IPH_INV_TAX_PRTAGE,IPH_INV_TAX_AMT,IPH_INV_RET_TAX_PRTAGE,")
					.append("IPH_INV_RET_TAX_AMT,IPH_INV_NET_AMT,IPH_INV_PAID_DT,IPH_INV_PMT_MODE,IPH_INV_PMT_TYPE,IPH_INV_CANCEL_DT,")
					.append("CRTD_ON,CRTD_BY,MDFD_ON,MDFD_BY) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)").toString();
			
			pstmt = con.prepareStatement(prepareInvoice);
			pstmt.setString(1, seqNum);
			pstmt.setString(2, object.getBgm_orgn_brnc_id());
			pstmt.setString(3, object.getBgm_prep_brnc_id().substring(0, 3));
			
			today = DateUtil.getDateValue("");
			pstmt.setDate(4, today);			
			pstmt.setString(5, newClientID);
			pstmt.setString(6, "A");
			amount = Float.parseFloat(dc.format(amount));
			pstmt.setFloat(7, amount);
			pstmt.setFloat(8, taxPerc);
			pstmt.setFloat(9, taxAmt);
			pstmt.setFloat(10, taxReturnPerc);
			pstmt.setFloat(11, taxRetAmt);			
			netAmount = (amount + taxAmt) - taxRetAmt;
			pstmt.setFloat(12, aux);
			pstmt.setDate(13, null);
			pstmt.setString(14, payMode);
			pstmt.setString(15, "C");
			pstmt.setString(16, "");
			pstmt.setDate(17, today);
			pstmt.setString(18, ((Global) sesion.getAttribute("sGlobal")).getOrigenUserClave());
			pstmt.setDate(19, today);
			pstmt.setString(20, ((Global) sesion.getAttribute("sGlobal")).getOrigenUserClave());

			pstmt.executeQuery();
			
			
			// Insertar en PPG INV PMT DTL
			String invoiceDetail = "INSERT INTO PPG_INV_PMT_DTL(IPD_INV_NO,IPD_REF_NO,IPD_INV_AMT,CRTD_ON,CRTD_BY,MDFD_ON,MDFD_BY) VALUES(?,?,?,?,?,?,?)";
			pstmt = con.prepareStatement(invoiceDetail);
			pstmt.setString(1, seqNum);
			pstmt.setString(2, object.getBgm_ref_no());
			pstmt.setFloat(3, Float.parseFloat(Double.toString(object.getBgm_gh_amt()).replace(',', '.')));
			pstmt.setDate(4, today);
			pstmt.setString(5, ((Global) sesion.getAttribute("sGlobal")).getOrigenUserClave());
			pstmt.setDate(6, today);
			pstmt.setString(7, ((Global) sesion.getAttribute("sGlobal")).getOrigenUserClave());
			pstmt.executeQuery();
			if (pstmt != null) {
				pstmt.close();
				if (payMode.equalsIgnoreCase("PAID")) {
					pstmt = con.prepareStatement("update PPG_BOK_GUIA_MSTR set BGM_VALID_FLG = ?, MDFD_ON = sysdate, MDFD_BY = ? where BGM_REF_NO = ?");
					pstmt.setString(1, "A");
					pstmt.setString(2, ((Global) sesion.getAttribute("sGlobal")).getOrigenUserClave());
					pstmt.setString(3, object.getBgm_ref_no());
					pstmt.executeQuery();
				}
			}
			if (pstmt != null) {
				pstmt.close();
			}
			
			if (cst != null)
				cst.close();
			String resultoutput = "";
			netAmount = Float.parseFloat(dc.format(netAmount));
			String formCtr = fetchFormCounter(sesion, con);
			
			if ((resultoutput != null) && (resultoutput.length() > 0)) {
				if (resultoutput.indexOf("ERROR") > -1) {
					if (resultoutput.indexOf("GH_BOOK_CONT_ID") > -1)
						request.setAttribute("error", "El cliente no tiene asignado un contacto en su direcci?n, no es posible generar la factura");
					// request.setAttribute("errors","THE INPUTS ARE WRONG OR TARIFF IS DISABLED");
					else
						request.setAttribute("error", "Falla de inserci?n");
					con.rollback();
					//String removeInvoiceLessCredit = "{ call pack_ppg_web_2012.PRO_REMOVE_INVOICE_NOCRLT(?,?) }";
					cst = con.prepareCall("{ call pack_ppg_web_2012.PRO_REMOVE_INVOICE_NOCRLT(?,?) }");
					cst.setString(1, seqNum);
					// cst.setString(2,object.getBgm_clint_id());//
					// vObject.getClientId());
					cst.setString(2, newClientID);
					cst.executeQuery();
					return resultoutput;
				}
			}
			
			// Cambias la referencia del ppg_inv_pmt_dtl para que te respete los
			// nuevos cambios
			String getRef = "select bgd_ref_no from ppg_bok_guia_dtl where bgd_trac_no = ? ";
			String referencia = "";
			pstmt = con.prepareStatement(getRef);
			pstmt.setString(1, guiaNo);
			rsRef = pstmt.executeQuery();
			while (rsRef.next()) {
				referencia = rsRef.getString(1);
			}			
			
			if (rsRef != null)
				rsRef.close();
			
			if (pstmt != null)
				pstmt.close();
			// Actualizas la referencia a la nueva
			String cambiaRef = "update ppg_inv_pmt_dtl set ipd_ref_no = ? where ipd_inv_no = ?";
			pstmt = con.prepareStatement(cambiaRef);
			pstmt.setString(1, referencia);
			pstmt.setString(2, seqNum);
			pstmt.executeQuery();
			if (pstmt != null)
				pstmt.close();
			
			cambiaRef = "update PPG_BOK_GUIA_ASIG set BGAS_CLNT_MSTR = ?, BGAS_USER_MSTR = null where BGAS_TRAC_NO = ?";
			pstmt = con.prepareStatement(cambiaRef);
			pstmt.setString(1, object.getBgm_clint_id());
			pstmt.setString(2, guiaNo);
			pstmt.executeQuery();
			if (pstmt != null)
				pstmt.close();
			
			//solo cuando sea de contado 'ToPay' vendr? factura.
			if (!payMode.equalsIgnoreCase("PAID")) {
				String branchID = (String) sesion.getAttribute("branchid");
				String actualizaFinInvGuia = "update fin_invc_guia set IG_GUIA_NO = ? WHERE IG_INVC_FORM_NO = ? AND IG_INVC_BRNC_ID = ?";
				pstmt = con.prepareStatement(actualizaFinInvGuia);
				pstmt.setString(1, guiaNo);
				pstmt.setString(2, formCtr + formNo);
				pstmt.setString(3, branchID);
				pstmt.executeQuery();
				if (pstmt != null)
					pstmt.close();				
			}
			// terminas la reinsercion de la factura
			con.commit();
			// if (toReturn!=null && toReturn.length()>0)
			// return toReturn;
			// else
			if (pstmt != null)
				pstmt.close();
			if (cst != null)
				cst.close();
			return formCtr + formNo;
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("reFactura()_Error1:").append(e).toString());
			e.printStackTrace();
			return (cnct.delete(0, cnct.length()).append(msgErr).append("reFactura()_Error1:").append(e.getMessage()).toString());
		} finally {
			try {
				if (rsRef != null)
					rsRef.close();
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (cst != null)
					cst.close();
				if (con != null)
					con.close();
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("reInsertRecord()_Error2:").append(e).toString());
				e.printStackTrace();
			}
		}
		// return result;
	}
	
	public String[] calcRetAmount(PrepaidGuiaMstr object, String payMode, String clntId, String taxBrnc) {
		float taxRetAmt = 0;
		
		float taxPerc = 0;
		float taxReturnPerc = 0;
		float taxAmt = 0;
		float amount = 0;
		float netAmount = 0;
		// Agregado CenitSoft
		double totalEnvio = 0;
		double totalACK = 0;
		double totalInsurance = 0;
		double totalEAD = 0;
		double totalRAD = 0;
		double totalEXT = 0;
		double totalGlobal = 0;
		
		String ivaRetTemplate = "N";

		totalEnvio = object.getBgm_gh_amt();
		totalACK = 0;
		totalACK = object.getBgm_ack_amt();
		
		totalInsurance = object.getBgm_insur_amt();
		totalEAD = object.getBgm_ead_amt();
		totalRAD = object.getBgm_rad_amt();
		totalEXT = totalEXT + (object.getBgm_ext_amt() * 1);
		totalGlobal = totalEnvio + totalACK + totalEAD + totalRAD + totalInsurance + totalEXT;
		amount = Float.parseFloat(Double.toString(totalGlobal).replace(',', '.'));
		
		ResultSet rsIn = null;
		try {
			con = ConnectDB.getConnection();
			
			pstmt = con.prepareStatement(selectTax);
			pstmt.setString(1, taxBrnc.substring(0, 3));//AAP20
			rsIn = pstmt.executeQuery();
			while (rsIn.next()) {
				taxPerc = rsIn.getFloat(1);
				taxReturnPerc = rsIn.getFloat(2);
			}

			taxAmt = (amount * taxPerc) / 100;

			if (rsIn != null)
				rsIn.close();
			if (pstmt != null)
				pstmt.close();

			String buscaRFCCliente = "select cm_tax_id, cm_ret_flag from sys_clnt_mstr where cm_clnt_id=?";
			String rfcCliente = "";
			String retFlag = "";
			// Buscando Cliente
			pstmt = con.prepareStatement(buscaRFCCliente);

			pstmt.setString(1, clntId);
			rsIn = pstmt.executeQuery();
			while (rsIn.next()) {
				rfcCliente = rsIn.getString(1) == null ? "" : rsIn.getString(1);
				retFlag = rsIn.getString(2) == null ? "" : rsIn.getString(2);
			}

			if (rsIn != null)
				rsIn.close();
			if (pstmt != null)
				pstmt.close();
			
			// Verificando Tipo de Cliente
			if (rfcCliente.indexOf("-") > -1)
				rfcCliente = rfcCliente.replace('-', ' ');
			String rfcNuevo = "";
			String strCaracter;
			if (rfcCliente.length() > 1) {
				for (int i = 0; i <= rfcCliente.length() - 1; i++) {
					if (rfcCliente.charAt(i) != ' ') {
						strCaracter = rfcCliente.substring(i, i + 1);
						rfcNuevo = rfcNuevo.concat(strCaracter);
					}
				}
			} else {
				rfcNuevo = "XAXX010101000";
			}
			rfcCliente = rfcNuevo;

			String buscaCliente = "select cm_clnt_type from sys_clnt_mstr where cm_clnt_id=?";
			String tipoCliente = "";
			
			pstmt = con.prepareStatement(buscaCliente);
			pstmt.setString(1, clntId);
			rsIn = pstmt.executeQuery();
			while (rsIn.next()) {
				tipoCliente = rsIn.getString(1);
			}

			if (rs != null)
	            rs.close();
	        if (pstmt != null)
	            pstmt.close();
	        
	        if (tipoCliente.indexOf("I") > -1) {
	            taxRetAmt = 0;
	        } else {
	        	Rfc r = new Rfc();
	            boolean rfcGenerico = r.isRfcGenerico(rfcCliente);
	            
	            if (!rfcGenerico) {
	                taxRetAmt = (Float.parseFloat(Double.toString(totalEnvio)) * taxReturnPerc) / 100;
	            } else {
	                taxRetAmt = 0;
	            }
			}
			
			amount = (float)Math.round(amount * 100) / 100;//REDONDEA A 2 DIGITOS
			taxAmt = (float)Math.round(taxAmt * 100) / 100;//REDONDEA A 2 DIGITOS
			taxRetAmt = (float)Math.round(taxRetAmt * 100) / 100;//REDONDEA A 2 DIGITOS
			netAmount = (amount+taxAmt);//-taxRetAmt;
			netAmount = (float)Math.round(netAmount * 100) / 100;//REDONDEA A 2 DIGITOS
			
			
			//Si es una modificaci?n a cr?dito buscan configuraciones BOOKING, si es al contado, PREPAID
			HashMap<String, ArrayList<Object>> mapService = getServiceConfig(con, clntId, object.getCompanyId(), "BOOKING", true);//AAP23
			
			if (mapService != null && !mapService.isEmpty()) {				
				float shpGTaxRet = (float)totalEnvio * ((float)getTaxRetPercConfig(mapService.get("SHP-G")) /100);
				float EADTaxRet = (float) totalEAD * ((float)getTaxRetPercConfig(mapService.get("EAD"))/100);
				float RADTaxRet = (float) totalRAD * ((float)getTaxRetPercConfig(mapService.get("RAD"))/100);
				float INVTaxRet = (float) totalInsurance * ((float)getTaxRetPercConfig(mapService.get("INV"))/100);
				float ACKTaxRet = (float) totalACK * ((float)getTaxRetPercConfig(mapService.get("ACK"))/100);
				float EXTTaxRet = (float) totalEXT * ((float)getTaxRetPercConfig(mapService.get("EXT"))/100);
				
				taxRetAmt = shpGTaxRet + EADTaxRet + RADTaxRet + INVTaxRet + ACKTaxRet + EXTTaxRet;
				taxRetAmt = (float)Math.round(taxRetAmt * 100) / 100;//REDONDEA A 2 DIGITOS
				
				ivaRetTemplate = "Y";
			}
		
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("reFactura()_Error1:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				if(rsIn != null)
					rsIn.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			try {
				if (pstmt != null)
					pstmt.close();
				if (cst != null)
					cst.close();
				if (con != null)
					con.close();

			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("reFactura()_Error2:").append(e).toString());
				e.printStackTrace();				
			}
		}
		
		String[] results = new String[2];
		results[0] = Float.toString(taxRetAmt);
		results[1] = ivaRetTemplate;
		return results;
	}

	public int fetchFormNumber(HttpSession sesion) {
		String FormNo = "";
		int formnumber = 0;
		try {
			con = ConnectDB.getConnection();
			//String fetchFormNo = "begin ? := pack_ppg_web_2012.FUN_PPG_GEN_FORM_NO('CFD_NO',?,'4'); end;";
			//cst = con.prepareCall("begin ? := pack_ppg_web_2012.FUN_PPG_GEN_FORM_NO(?,?,?); end;");
			pstmt = con.prepareStatement(fetchFormNo);
			pstmt.setString(1, (String) session.getAttribute("branchid"));
			//cst.registerOutParameter(1, Types.VARCHAR);
			//cst.setString(2, "CFD_NO");
			//cst.setString(3, (String) sesion.getAttribute("branchid"));
			//cst.setString(4, "9");
			//cst.execute();
			rs = pstmt.executeQuery();
			while(rs.next()) {
				FormNo = rs.getString(1);
			}
			//FormNo = cst.getString(1);
			formnumber = Integer.parseInt(FormNo.substring(2, FormNo.length()));
			//formnumber = Integer.parseInt(FormNo) + 1;

		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("fetchFormNumber()_Error1:").append(e).toString());
			e.printStackTrace();
			return 0;
		} finally {
			try {
				if (cst != null)
					cst.close();
				if (con != null)
					con.close();
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("fetchFormNumber()_Error2:").append(e).toString());
				e.printStackTrace();
			}
		}
		return formnumber;
	}

	public String getAckTotal(String ackdesc) {
		String total = "0";
		try {
			con = ConnectDB.getConnection();
			pstmt = con.prepareStatement("SELECT TR_TRIF_VLUE FROM ACT_TRIF WHERE TR_SRVC_ID = ? AND TR_TRIF_ID = ( select TI_TRIF_ID FROM act_trif_id where TI_FACT = ? AND TI_SRVC_ID = ?)");
			pstmt.setString(1, ackdesc);
			pstmt.setString(2, "NON");
			pstmt.setString(3, ackdesc);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				total = rs.getString(1);
			}
			
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("getAckTotal()_Error1:").append(e).toString());
			e.printStackTrace();
			return "";
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();				
				if (con != null)
					con.close();
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("getAckTotal()_Error2:").append(e).toString());
				e.printStackTrace();
			}
		}
		return total;
	}

	public String getImpuesto(String referenceServiceID, String taxableAmount, String branch/*, String destbranch*/) {
		String returnValue = "";
		try {
			con = ConnectDB.getConnection();
			//returnValue = this.getTax(con, referenceServiceID, taxableAmount, branch, destbranch);
			returnValue = this.getTax(con, referenceServiceID, taxableAmount, branch);
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("getImpuesto()_Error1:").append(e).toString());
			e.printStackTrace();
			return "";
		} finally {
			try {				
				if (con != null)
					con.close();
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("getImpuesto()_Error2:").append(e).toString());
				e.printStackTrace();
			}
		}
		return returnValue;
	}

	private String getTax(Connection conn, String referenceServiceId,
			String taxableAmount, String branch) {
		// Regresa el valor del impuesto dependiendo de la zona origen y destino
		String taxQuery = "{call pack_sipweb.PRO_CALC_TAX_AMT(?,?,?,?,?)}";// Checked
		try {
			
			cst = conn.prepareCall(taxQuery);
			cst.setString(1, branch);
			cst.setString(2, referenceServiceId);
			cst.setDouble(3, Double.parseDouble(taxableAmount));
			cst.registerOutParameter(4, Types.NUMERIC);
			cst.registerOutParameter(5, Types.NUMERIC);
			cst.executeQuery();
			java.text.DecimalFormat df = new java.text.DecimalFormat("0.00");
			String tax = "0";
			tax = df.format(cst.getDouble(4));
			//String clientType = "";
			return tax;
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("getTax()_Error1:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				if (cst != null)
					cst.close();
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("getTax()_Error2:").append(e).toString());
				e.printStackTrace();
			}
		}
		return null;
	}

	public PrepaidGuiaMstr getPrepaidGuiaMaster(String guiaNo) {
		PrepaidGuiaMstr obj = new PrepaidGuiaMstr();
		try {
			con = ConnectDB.getConnection();
			String getGuiaMstr = cnct
					.delete(0, cnct.length())
					.append("SELECT BGM_REF_NO,BGM_PREP_BRNC_ID, BGM_REF_DT, BGM_ORGN_BRNC_ID, BGM_CLINT_ID, BGM_NO_OF_GUIS, BGM_ZONE, BGM_WGHT, ")
					.append("BGM_VLUM, BGM_GH_AMT, BGM_VALID_FLG, BGM_ORG_SITE_ID, BGM_SHIP_TYPE, M.CRTD_ON, M.CRTD_BY, M.MDFD_ON, M.MDFD_BY, ")
					.append("BGM_SWINGCD_NO, BGM_ACK_SERVICE, BGM_ACK_AMT, BGM_ACK_TAX, BGM_INSUR_SERVICE, BGM_INSUR_AMT, BGM_INSUR_TAX, BGM_DSC_PERCENT, ")
					.append("BGM_DSC_AMT, BGM_EAD_SERVICE, BGM_EAD_AMT, BGM_RAD_SERVICE, BGM_RAD_AMT, BGM_EXT_SERVICE, BGM_EXT_AMT, BGM_TARIFA, ")
					.append("NVL(M.Bgm_Cmpy_Id, FUN_GET_CPNY_BY_WEIGHT_CLIENT(BGM_WGHT, BGM_CLINT_ID, 'SHP-G')) CPNY_ID, ")
					.append("BGM_LOC_TYPE ")//AAP20
					.append("FROM PPG_BOK_GUIA_MSTR M, PPG_BOK_GUIA_DTL D WHERE M.BGM_REF_NO = D.BGD_REF_NO AND D.BGD_TRAC_NO = ?")
					.toString();
			
			pstmt = con.prepareStatement(getGuiaMstr);
			pstmt.setString(1, guiaNo);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				obj.setBgm_ref_no(rs.getString(1));
				obj.setBgm_prep_brnc_id(rs.getString(2));
				obj.setBgm_ref_dt(rs.getDate(3));
				obj.setBgm_orgn_brnc_id(rs.getString(4));
				obj.setBgm_clint_id(rs.getString(5));
				obj.setBgm_no_of_guis(rs.getDouble(6));
				obj.setBgm_zone(rs.getString(7));
				obj.setBgm_wght(rs.getDouble(8));
				obj.setBgm_vlum(rs.getString(9));
				obj.setBgm_gh_amt(rs.getDouble(10));
				obj.setBgm_valid_flg(rs.getString(11));
				obj.setBgm_org_site_id(rs.getString(12));
				obj.setBgm_ship_type(rs.getString(13));
				obj.setCrtd_on(rs.getDate(14));
				obj.setCrtd_by(rs.getString(15));
				obj.setMdfd_on(rs.getDate(16));
				obj.setMdfd_by(rs.getString(17));
				obj.setBgm_swingcd_no(rs.getString(18));
				obj.setBgm_ack_service(rs.getString(19));
				obj.setBgm_ack_amt(rs.getDouble(20));
				obj.setBgm_ack_tax(rs.getDouble(21));
				obj.setBgm_insur_service(rs.getString(22));
				obj.setBgm_insur_amt(rs.getDouble(23));
				obj.setBgm_insur_tax(rs.getDouble(24));
				obj.setBgm_dsc_percent(rs.getDouble(25));
				obj.setBgm_dsc_amt(rs.getDouble(26));
				obj.setBgm_ead_service(rs.getString(27));
				obj.setBgm_ead_amt(rs.getDouble(28));
				obj.setBgm_rad_service(rs.getString(29));
				obj.setBgm_rad_amt(rs.getDouble(30));
				obj.setBgm_ext_service(rs.getString(31));
				obj.setBgm_ext_amt(rs.getDouble(32));
				obj.setBgm_tarifa(rs.getString(33));// AAP01
				obj.setCompanyId(rs.getString(34));
				obj.setLocationType(rs.getString(35) == null ? "":rs.getString(35));//AAP20
			}
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("getPrepaidGuiaMaster()_Error1:").append(e).toString());			
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();				
				if (con != null)
					con.close();
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("getPrepaidGuiaMaster()_Error2:").append(e).toString());
				e.printStackTrace();
			}
		}
		return obj;
	}

	public String[] getTarifaEnvioSaltoTarif(PrepaidGuiaMstr guiaOld, ConversionForm conversionform) {
		String returnValue[] = new String[2];				
		int countZones = 0;
		int countTariff = 0;
		double importeTariff = 0;
		double totalTariff = 0;
		try {
			con = ConnectDB.getConnection();
			
			/*contar zonas*/
			//select count (dt_to_vlue) from act_dstn_tarif where dt_to_vlue > 400 and DT_TO_VLUE <= 2400 and dt_srvc_id='SHP-G' and dt_trif_slab = 'T0'			
			
			String query = "select count (dt_to_vlue) from act_dstn_tarif where dt_to_vlue > ? and DT_TO_VLUE <= ? and dt_srvc_id = ? and dt_trif_slab = ?";
			pstmt = con.prepareStatement(query);
			
			pstmt.setInt(1, Integer.parseInt(guiaOld.getBgm_zone()));
			pstmt.setInt(2, Integer.parseInt(conversionform.getZone()));
			pstmt.setString(3, "SHP-G");
			pstmt.setString(4, "T0");
			
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				countZones = rs.getInt(1);
			}
			
			if (rs != null) {
				rs.close();
			}
			
			if (pstmt != null) {
				pstmt.close();
			}
			
			/*contar tarifas*/
			//select count (dt_trif_slab) from act_dstn_tarif where dt_trif_slab > 'T2' and dt_trif_slab <= 'T6' and dt_srvc_id='SHP-G' and dt_to_vlue = 400
			query = "select count (dt_trif_slab) from act_dstn_tarif where dt_trif_slab > ? and dt_trif_slab <= ? and dt_srvc_id = ? and dt_to_vlue = ?";
			pstmt = con.prepareStatement(query);
			
			pstmt.setString(1, guiaOld.getBgm_tarifa());
			pstmt.setString(2, conversionform.getTarifa());
			pstmt.setString(3, "SHP-G");
			pstmt.setInt(4, 400);
			
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				countTariff = rs.getInt(1);
			}
			
			if (rs != null) {
				rs.close();
			}
			
			if (pstmt != null) {
				pstmt.close();
			}			
			
			/*obtener costo por incremento de zonas / tarifas*/
			//select count (dt_trif_slab) from act_dstn_tarif where dt_trif_slab > 'T2' and dt_trif_slab <= 'T6' and dt_srvc_id='SHP-G' and dt_to_vlue = 400
			query = "select pm_vlue2_id from sys_parm_mstr where pm_mdul_id = ? and pm_parm_type = ? and pm_parm_code1 = ?";
			pstmt = con.prepareStatement(query);
			
			pstmt.setString(1, "PPG");
			pstmt.setString(2, "PPGMODIF");
			pstmt.setString(3, "SHP-G");			
			
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				importeTariff = rs.getDouble(1);
			}
			
			if (rs != null) {
				rs.close();
			}
			
			if (pstmt != null) {
				pstmt.close();
			}
			
			totalTariff = (countTariff + countZones) * importeTariff;
			//AccessLog.Log(msgAvi + "getTarifaEnvioSaltoTarif() totalTariff "+totalTariff);
		}  catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getTarifaEnvioSaltoTarif()_Error1:").append(e).toString());
			e.printStackTrace();			
			return returnValue;
		} finally {
			try {
				if (rs != null)
					rs.close();
				
				if (pstmt != null)
					pstmt.close();
				
				if (con != null)
					con.close();

			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getTarifaEnvioSaltoTarif()_Error2:").append(e).toString());
				e.printStackTrace();
			}
		}
		returnValue[0] = String.valueOf(totalTariff);
		returnValue[1] = "";
		return returnValue;
	}
	
	public String[] getTarifaEnvio(String zone, String tarifa, String peso,
			String volumen, String tarifaT7) {
		String returnValue[] = new String[2];
		String tarifaEnvio = "";
		String tarifaT7V = "";
		String tarifaT7P = "";
		//ArrayList list = new ArrayList();
		String identificador = tarifa.substring(0, 2);
		double valorTarifa = 0;
		volumen = volumen.replace(',', '.');
		volumen = "0" + volumen;
		double valorVolumen = Double.parseDouble(volumen);
		double valorPeso = Double.parseDouble(peso);
		double valorTarifaT7V = 0;
		double valorTarifaT7P = 0;
		try {
			con = ConnectDB.getConnection();
			if (identificador.indexOf("T7") == -1) {
				String getTarifa = "select dt_trif_amnt from act_dstn_tarif where ? between dt_from_vlue and dt_to_vlue and dt_trif_slab = ? and dt_srvc_id = ?";//AAP06 CAMBIO DE TABLA act_dstn_trif POR act_dstn_tarif
				pstmt = con.prepareStatement(getTarifa);
				pstmt.setInt(1, Integer.parseInt(zone));
				pstmt.setString(2, tarifa);
				pstmt.setString(3, "SHP-G");
				rs = pstmt.executeQuery();
				while (rs.next()) {
					tarifaEnvio = rs.getString(1);
				}
			} else {
				String getTarifa = "select dt_trif_amnt from act_dstn_tarif where ? between dt_from_vlue and dt_to_vlue and dt_trif_slab = ? and dt_srvc_id = ?";//AAP06 CAMBIO DE TABLA act_dstn_trif POR act_dstn_tarif
				pstmt = con.prepareStatement(getTarifa);
				pstmt.setInt(1, Integer.parseInt(zone));
				pstmt.setString(2, "T7V");
				pstmt.setString(3, "SHP-G");
				
				rs = pstmt.executeQuery();
				while (rs.next()) {
					tarifaT7V = rs.getString(1);
				}
				valorTarifaT7V = Double.parseDouble(tarifaT7V) * valorVolumen;
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				
				getTarifa = "select dt_trif_amnt from act_dstn_tarif where ? between dt_from_vlue and dt_to_vlue and dt_trif_slab = ? and dt_srvc_id = ?";//AAP06 CAMBIO DE TABLA act_dstn_trif POR act_dstn_tarif
				pstmt = con.prepareStatement(getTarifa);
				pstmt.setInt(1, Integer.parseInt(zone));
				pstmt.setString(2, "T7P");
				pstmt.setString(3, "SHP-G");
				
				rs = pstmt.executeQuery();
				while (rs.next()) {
					tarifaT7P = rs.getString(1);
				}
				valorTarifaT7P = Double.parseDouble(tarifaT7P) * valorPeso;
				if (valorTarifaT7P > valorTarifaT7V) {
					tarifaT7 = "T7P";
					valorTarifa = valorTarifaT7P;
				} else {
					tarifaT7 = "T7V";
					valorTarifa = valorTarifaT7V;
				}
				tarifaEnvio = Double.toString(valorTarifa);
			}
		}  catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("getTarifaEnvio()_Error1:").append(e).toString());
			e.printStackTrace();
			return returnValue;
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();				
				if (con != null)
					con.close();
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("getTarifaEnvio()_Error2:").append(e).toString());
				e.printStackTrace();
			}
		}
		returnValue[0] = tarifaEnvio;
		returnValue[1] = tarifaT7;
		return returnValue;
	}

	public String getTarifaEXT() {
		String tarifaEXT = "";
		//String porcentaje = "";
		String minimo = "";
		//ArrayList list = new ArrayList();
		String rateConfig = "PPGSERVICE2017";
		try {
			con = ConnectDB.getConnection();
			/*obtiene configuracion de descuentos a usar*/
			pstmt = con.prepareStatement("SELECT PM_PARM_CODE1 FROM sys_parm_mstr WHERE pm_mdul_id = ? AND pm_parm_type = ?");
			pstmt.setString(1, "PPG");
			pstmt.setString(2, "PPGSERVICEUSE");
			
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				rateConfig = rs.getString(1)==null ?"PPGSERVICE2017":rs.getString(1);
			}
			rs.close();
			pstmt.close();
			
			String getTarifa = "select pm_vlue1_id, pm_vlue2_id from sys_parm_mstr where pm_mdul_id = ? and pm_parm_type = ? and pm_parm_code1 = ? ";
			pstmt = con.prepareStatement(getTarifa);
			pstmt.setString(1, "PPG");
			pstmt.setString(2, rateConfig);
			pstmt.setString(3, "EXT");
			rs = pstmt.executeQuery();
			while (rs.next()) {
				//porcentaje = rs.getString(1);
				minimo = rs.getString(2);
			}
			tarifaEXT = minimo;
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("getTarifaEXT()_Error1:").append(e).toString());
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();				
				if (con != null)
					con.close();
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("getTarifaEXT()_Error2:").append(e).toString());
				e.printStackTrace();
			}
		}
		return tarifaEXT;
	}

	public String getTariffService(String tarifaEnvio, String modul, String pCode1, String pType, boolean valMinimo) {
		String serviceTariff = "";
		String porcentaje = "";
		String minimo = "";
		//ArrayList list = new ArrayList();
		try {
			con = ConnectDB.getConnection();
			String getTarifa = "select pm_vlue1_id, pm_vlue2_id from sys_parm_mstr where pm_mdul_id = ? and pm_parm_code1 = ? and pm_parm_type = ?";
			pstmt = con.prepareStatement(getTarifa);
			pstmt.setString(1, modul);
			pstmt.setString(2, pCode1);
			pstmt.setString(3, pType);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				porcentaje = rs.getString(1);
				minimo = rs.getString(2);
			}
			
			double valorPorcentaje = 0;
			valorPorcentaje = Double.parseDouble(tarifaEnvio) * (Double.parseDouble(porcentaje) / 100);
			double valorMinimo = Double.parseDouble(minimo);
			if (valMinimo && valorMinimo > valorPorcentaje)
				serviceTariff = minimo;
			else
				serviceTariff = Double.toString(valorPorcentaje);			
			
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getTariffService()_Error1:").append(e).toString());
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (rs != null)
					rs.close();
				if (con != null)
					con.close();

			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getTariffService()_Error2:").append(e).toString());
				e.printStackTrace();
			}
		}
		return serviceTariff;
	}

	public String getTariffServiceSEG(String tarifaEnvio, String modul, String pCode1, String pType, boolean valMinimo) {
		String serviceTariff = "";
		String porcentaje = "";
		String minimo = "";
		//ArrayList list = new ArrayList();
		try {
			con = ConnectDB.getConnection();
			String getTarifa = "SELECT  pm_vlue1_id,pm_vlue2_id FROM SYS_PARM_MSTR WHERE PM_MDUL_ID = ? AND PM_PARM_CODE1 = ? AND PM_PARM_TYPE = ?";
			pstmt = con.prepareStatement(getTarifa);
			pstmt.setString(1, modul);
			pstmt.setString(2, pCode1);
			pstmt.setString(3, pType);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				porcentaje = rs.getString(1);
				minimo = rs.getString(2);
			}
			
			double valorPorcentaje = 0;
			valorPorcentaje = Double.parseDouble(tarifaEnvio) * (Double.parseDouble(porcentaje) / 100);
			double valorMinimo = Double.parseDouble(minimo);
			if (valMinimo && valorMinimo > valorPorcentaje)
				serviceTariff = minimo;
			else
				serviceTariff = Double.toString(valorPorcentaje);			
			
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getTariffServiceSEG()_Error1:").append(e).toString());
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (rs != null)
					rs.close();
				if (con != null)
					con.close();

			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getTariffService()_Error2:").append(e).toString());
				e.printStackTrace();
			}
		}
		return serviceTariff;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList fetchAcknowledgementTypeDesc() {
		ArrayList hold = new ArrayList(1);
		PreparedStatement pst = null;		
		try {
			String query = null;
			con = ConnectDB.getConnection();
			//String ackType = null;

			query = "SELECT pm_vlue1_desc,pm_parm_code1 FROM sys_parm_mstr WHERE pm_parm_type = ?";// Checked

			pst = con.prepareStatement(query);
			pst.setString(1, "ACK_TYPE");
			rs = pst.executeQuery();
			ArrayList ackTypeLabel = new ArrayList(3);
			ArrayList ackTypeValue = new ArrayList(3);
			ArrayList ackTypeLabel1 = new ArrayList(3);
			ArrayList ackTypeValue1 = new ArrayList(3);
			//int i = 0;
			String ack = "";
			while (rs.next()) {				
				ack = rs.getString(2);
				if (ack.equalsIgnoreCase("N"))
					ackTypeLabel1.add(rs.getString(1));
				else
					ackTypeLabel.add(rs.getString(1));

				if (ack.equalsIgnoreCase("I"))
					ackTypeValue.add("ACK-P");
				else if (ack.equalsIgnoreCase("N"))
					ackTypeValue1.add("ACK-N");
				else if (ack.equalsIgnoreCase("C"))
					ackTypeValue.add("ACK-C");
				else if (ack.equalsIgnoreCase("X"))
					ackTypeValue.add("ACK-X");

				// AccessLog.Log("ackTypeValue"+ackTypeValue.get(i));
				//i++;

			}	

			for (int count = 0; count < ackTypeLabel.size(); count++) {
				ackTypeLabel1.add(ackTypeLabel.get(count));
				ackTypeValue1.add(ackTypeValue.get(count));
			}
			hold.add(ackTypeLabel1);
			hold.add(ackTypeValue1);
			
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("fetchAcknowledgementTypeDesc()_Error1:").append(e).toString());
			e.printStackTrace();
            } finally {
                try {
                    if (rs != null) {
                        rs.close();
                    }
                } catch (Exception e) {
                    AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("fetchAcknowledgementTypeDesc()_Error2:").append(e).toString());
                    e.printStackTrace();
                }
                try {
                    if (pst != null) {
                        pst.close();
                    }
                } catch (Exception e) {
                    AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("fetchAcknowledgementTypeDesc()_Error2:").append(e).toString());
                    e.printStackTrace();
                }
                try {
                    if (con != null) {
                        con.close();
                    }
                } catch (Exception e) {
                    AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("fetchAcknowledgementTypeDesc()_Error2:").append(e).toString());
                    e.printStackTrace();
                }
            }
		return hold;
	}

	public String creditStatus(String clientId, HttpSession sesion) {
		String credit = "N";
		try {
			con = ConnectDB.getConnection();
			String branchID = (String) sesion.getAttribute("branchid");
			branchID = branchID.toLowerCase();
			pstmt = con.prepareStatement("select CM_CRED_STUS_ID, CM_BRNC_ID from SYS_CLNT_MSTR where CM_CLNT_ID = ?");
			pstmt.setString(1, clientId);
			rs = pstmt.executeQuery();
			// session.removeAttribute("status");
			String creditEnabled = "";
			String creditBranchId = "";		

			while (rs.next()) {
				creditEnabled = rs.getString(1);
				creditBranchId = rs.getString(2);
				
				if (creditEnabled.equalsIgnoreCase("ena")) {
					if (creditBranchId != null) {
						creditBranchId = creditBranchId.toLowerCase();
						if (branchID.equalsIgnoreCase(creditBranchId)) {
							sesion.setAttribute("status", "ena");
							credit = "Y";
						}
					} else {
						sesion.setAttribute("status", "ena");
						credit = "Y";
					}
				}
			}
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("creditStatus()_Error1:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				resources.closeResources(rs, pstmt);
				resources.cerrarConexion(con);				
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("creditStatus()_Error2:").append(e).toString());
				e.printStackTrace();
			}
		}
		return credit;
	}
	
	public boolean clntCreditStatusEnabled(String clientId, String branchID) {
		boolean isEnabled = false;
		try {
			con = ConnectDB.getConnection();
			branchID = branchID.toLowerCase();
			pstmt = con.prepareStatement("select CM_CRED_STUS_ID, CM_BRNC_ID from SYS_CLNT_MSTR where CM_CLNT_ID = ?");
			pstmt.setString(1, clientId);
			rs = pstmt.executeQuery();
			String creditEnabled = "";
			String creditBranchId = "";

			while (rs.next()) {
				creditEnabled = rs.getString(1);
				creditBranchId = rs.getString(2);
				if (creditEnabled.equalsIgnoreCase("ena")) {
					if (creditBranchId != null) {
						creditBranchId = creditBranchId.toLowerCase();
						if (branchID.equalsIgnoreCase(creditBranchId)) {
							isEnabled = true;
						}
					} else {
						isEnabled = true;
					}
				}
			}
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("creditStatus()_Error1:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();				
				if (con != null)
					con.close();
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("creditStatus()_Error2:").append(e).toString());
				e.printStackTrace();
			}
		}
		
		return isEnabled;
	}

	public String clientType(String clientId) {
		String returnValue = "";
		try {
			con = ConnectDB.getConnection();
			//String getClientType = "select cm_clnt_type from sys_clnt_mstr where cm_clnt_id = ?";
			pstmt = con.prepareStatement("select cm_clnt_type from sys_clnt_mstr where cm_clnt_id = ?");
			pstmt.setString(1, clientId);
			rs = pstmt.executeQuery();
			// session.removeAttribute("status");
			while (rs.next()) {
				returnValue = rs.getString(1);
			}
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("clientType()_Error1:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();				
				if (con != null)
					con.close();
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("clientType()_Error2:").append(e).toString());
				e.printStackTrace();
			}
		}
		return returnValue;
	}

	public String[] getTaxes(String branchID) {
		String returnValue[] = new String[2];
		try {

			con = ConnectDB.getConnection();
			pstmt = con.prepareStatement(selectTax);
			pstmt.setString(1, branchID);
			rs = pstmt.executeQuery();
			// session.removeAttribute("status");
			while (rs.next()) {
				returnValue[0] = rs.getString(1);
				returnValue[1] = rs.getString(2);
			}

		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("getTaxes()_Error1:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();		
				if (con != null)
					con.close();
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("getTaxes()_Error2:").append(e).toString());
				e.printStackTrace();
			}
		}
		return returnValue;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList fetchFormNo(String branch_id) {
		ArrayList hold = new ArrayList(2);
		PreparedStatement pst = null;		
		try {
			String query = null;
			con = ConnectDB.getConnection();
			//String ackType = null;
			query = "SELECT pm_parm_code2 FROM sys_parm_mstr WHERE pm_parm_type = ? AND PM_MDUL_ID = ? AND PM_PARM_CODE1 = ? AND PM_PARM_CODE2 <> ? AND PM_PARM_CODE2 <> ? AND PM_PARM_CODE2 <> ?";
			pst = con.prepareStatement(query);
			pst.setString(1, "FORM_NO");
			pst.setString(2, "BOK");
			pst.setString(3, branch_id);
			pst.setString(4, "FX");
			pst.setString(5, "PP");
			pst.setString(6, "WW");
			rs = pst.executeQuery();
			ArrayList listaCajasType = new ArrayList(6);
			ArrayList listaCajasValue = new ArrayList(6);
			//int i = 0;
			String caja = "";
			while (rs.next()) {				
				caja = rs.getString(1);
				listaCajasType.add(caja);
				listaCajasValue.add(caja);
				//i++;
			}
			
			hold.add(listaCajasType);
			hold.add(listaCajasValue);
			
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("fetchFormNo()_Error1:").append(e).toString());
			e.printStackTrace();
		} finally {
                    try {
                        if (rs != null) {
                            rs.close();
                        }
                    } catch (Exception e) {
                        AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("fetchFormNo()_Error2:").append(e).toString());
                        e.printStackTrace();
                    }
                    try {
                        if (pst != null) {
                            pst.close();
                        }
                    } catch (Exception e) {
                        AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("fetchFormNo()_Error2:").append(e).toString());
                        e.printStackTrace();
                    }
                    try {
                        if (con != null) {
                            con.close();
                        }
                    } catch (Exception e) {
                        AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("fetchFormNo()_Error2:").append(e).toString());
                        e.printStackTrace();
                    }
		}
		return hold;
	}

	public double clientCredit(String clientId) {
		double localCredit = 0;
		try {
			con = ConnectDB.getConnection();
			// pstmt=con.prepareStatement("select pack_web.FUN_CLNT_CRDT_AMT(?) from dual");//AAP05
			pstmt = con.prepareStatement("select FUN_CLNT_CRDT_AMT_WW(?) from dual");// AAP05
			pstmt.setString(1, clientId);
			rs = pstmt.executeQuery();
			if (rs.next())
				localCredit = rs.getDouble(1);
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();			
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("clientCredit()_Error1:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();				
				if (con != null)
					con.close();
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("clientCredit()_Error1:").append(e).toString());
				e.printStackTrace();
			}
		}
		return localCredit;
	}

	public String getDeliveryHours(String sucOrigen, String sucDest, String ocuEadFlag, String codCol, String srvcType, String fechaDocum, String clntId) {
		String ssSrvcId = "SHP-G";
		String horasEntrega = "";
		JavDeliveryHours horas = new JavDeliveryHours();
		try {
			con=ConnectDB.getConnection();
			//horasEntrega = horas.getDeliveryHoursRecord(con, sucOrigen, sucDest, ocuEadFlag, codCol, getSysDateFormat(con,"'HH24:MI'"), srvcType, clntId );
			horasEntrega = horas.getDeliveryHoursRecordNew(con, sucOrigen, sucDest, ocuEadFlag, codCol, srvcType, clntId, ssSrvcId);
		} catch (Exception e) {
			AccessLog.Log(e.getMessage());
			e.printStackTrace();
		} finally {
			try {				
				if (con != null)
					con.close();
			} catch (Exception e) {
				AccessLog.Log(e.getMessage());
				e.printStackTrace();
			}
		}
		return horasEntrega;
	}
	
	public String getSysDateFormat(Connection conn, String format){

		PreparedStatement pst = null;
		ResultSet rst = null;
		String sysDate =null;
		Connection connTMP = null;
		try {//'HH24:MI'
			cnct.delete(0, cnct.length()).append("select to_char(sysdate, ").append(format).append(") from dual");
			if (conn == null) {
				connTMP = ConnectDB.getConnection();
				pst = connTMP.prepareStatement(cnct.toString());// Checked
			} else {
				pst = conn.prepareStatement(cnct.toString());// Checked
			}
			rst = pst.executeQuery();
			if(rst.next()) {
				//sysDate = rs.getString(1);
				sysDate = rst.getString(1);
			}			
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getSysDateFormat()_Error3:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				if (rst != null) {
					rst.close();
				}
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getSysDateFormat()_Error3:").append(e).toString());
				e.printStackTrace();
			}
                        try {
				if (pst != null) {
					pst.close();	
				}
				
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getSysDateFormat()_Error3:").append(e).toString());
				e.printStackTrace();
			}
			try {				
				if (connTMP != null)
					connTMP.close();
			} catch (Exception e) {
				AccessLog.Log(e.getMessage());
				e.printStackTrace();
			}
		}
		
		//AccessLog.Log("SYSDATE WITH TIME STAMP IN SERVICES ACTION "+sysTimeStamp);
		return sysDate;
	}

	/*************************************************************************************************************
	 * Metodo para obtener la informacion del operador logistico para ser
	 * almacenada en la generacion de la guia *
	 *************************************************************************************************************/
	// AAP02
	public boolean obtieneInfRequerimientosACK(String groupClientId) {

		PreparedStatement pst = null;
		ResultSet rst = null;
		boolean exist = false;

		try {
			con = ConnectDB.getConnection();
			pst = con.prepareStatement("SELECT CR_CLNT_ID FROM SYS_CLNT_SRVC_REQM_MSTR WHERE CR_CLNT_ID = ? AND CR_SRVC_ID = ? AND CR_REFR_SRVC_ID = ?");
			pst.setString(1, groupClientId);
			pst.setString(2, "ACK-C");
			pst.setString(3, "ACK");
			rst = pst.executeQuery();

			if (rst.next()) {
				exist = true;
			}
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("obtieneInfRequerimientosACK()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				if (rst != null)
					rst.close();
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("obtieneInfRequerimientosACK()_Error3:").append(e).toString());
				e.printStackTrace();
			}
                        try {
				if (pst != null)
					pst.close();
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("obtieneInfRequerimientosACK()_Error3:").append(e).toString());
				e.printStackTrace();
			}
                        try {
				if (con != null)
					con.close();
			} catch (Exception e2) {
				AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("obtieneInfRequerimientosACK()_Error2:").append(e2).toString());
				e2.printStackTrace();
			}
		}
		return exist;
	}

	/*************************************************************************************************************
	 * Metodo para obtener la informacion del operador logistico para ser
	 * almacenada en la generacion de la guia *
	 *************************************************************************************************************/
	// AAP03
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ArrayList obtieneInfOperadorLogistico(ConversionForm serForm) {

		PreparedStatement pst = null;
		 ResultSet rst = null;
		ArrayList datos = new ArrayList(2);

		try {
			con = ConnectDB.getConnection();
			pst = con.prepareStatement("SELECT OM_OL_FLAG, OM_OLREFRUTA, OM_OL_ID, OM_MAX_VAL_DECL FROM SYS_OL_MSTR WHERE OM_OL_ID = ?");
			pst.setString(1, serForm.getOperadorLogistico());
			rst = pst.executeQuery();

			if (rst.next()) {
				// bandera operador logistico
				if (rst.getString(1) == null) {
					datos.add("");
				} else {
					datos.add(rst.getString(1));
				}
				// ruta de operador logistico
				if (rst.getString(2) == null) {
					datos.add("");
				} else {
					datos.add(rst.getString(2));
				}

				// ID operador logistico
				if (rst.getString(3) == null) {
					datos.add("");
				} else {
					datos.add(rst.getString(3));
				}

				// valor maximo a declarar por operador logistico
				if (rst.getString(4) == null) {
					datos.add("");
				} else {
					datos.add(rst.getString(4));
				}
			}
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("obtieneInfOperadorLogistico()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				if (rst != null)
					rst.close();
			} catch (Exception e2) {
				AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("obtieneInfOperadorLogistico()_Error2:").append(e2).toString());
				e2.printStackTrace();
			}
                        try {
				if (pst != null)
					pst.close();
			} catch (Exception e2) {
				AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("obtieneInfOperadorLogistico()_Error2:").append(e2).toString());
				e2.printStackTrace();
			}
                        try {
				if (con != null)
					con.close();
			} catch (Exception e2) {
				AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("obtieneInfOperadorLogistico()_Error2:").append(e2).toString());
				e2.printStackTrace();
			}
		}
		return datos;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void insertWebCntrlMail(ConversionForm conversionform,
			String genGuiaNumber, String documentador) {// AAP04
		JavControlMail javControlMail = new JavControlMail();
		Hashtable datos = new Hashtable(8);

		try {
			datos.put("genGuiaNumber", genGuiaNumber);
			datos.put("clientId", conversionform.getClientId());
			datos.put("destclave", conversionform.getDestclave());
			datos.put("eMailOrigText", conversionform.geteMailOrigText());
			datos.put("eMailDestText", conversionform.geteMailDestText());
			datos.put("statusMail", "P");
			datos.put("documentador", documentador);

			javControlMail.insertWebCntrlMail(datos);
			
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("insertWebCntrlMail()_Error:").append(e).toString());
			e.printStackTrace();
		}
	}// AAP04
	
	private String getFormNumber(String branchId, String modulo, String serieCaja) {
		CallableStatement clst = null;
		String formNumber="A0";
		Connection localconn =null;
		try {			
			localconn = ConnectDB.getConnection();
			String sql = "declare\n"
                    + "pragma Autonomous_Transaction;\n"
                    + "begin\n"
                    + "? := fun_ftch_form_no_v2(?, ?, ?, ?); \n"
                    + "commit;\n"
                    + "end; ";
			clst = localconn.prepareCall(sql);
			clst.registerOutParameter(1,Types.VARCHAR);
			clst.setString(2,branchId);
			clst.setString(3,branchId);
			clst.setString(4,serieCaja);
			clst.setString(5,modulo);
			
			clst.executeQuery();
			
			formNumber = clst.getString(1);			
					
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getFormNumber()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.cerrarCallableStatement(clst);
			resources.cerrarConexion(localconn);
		}		
		return cnct.delete(0,cnct.length()).append(serieCaja).append(formNumber).toString();		
	}
	
	/****************************************************************
	 * Metodo para validar si el numero de forma (guia) ya existe	*
	 ****************************************************************/
	private boolean validaGH_FORM_NO(Connection conn, ConversionForm serForm, String docuType) {
		PreparedStatement pst = null;
		ResultSet rst = null;
		boolean existe = false;
		try {
			String checkFormQuery = "select 1 from bok_guia_head where GH_FORM_NO = ? and GH_PREP_BRNC_ID = ? and GH_DOCU_TYPE = ?";
			pst = conn.prepareStatement(checkFormQuery);
			pst.setString(1, serForm.getGuiaNo());
			pst.setString(2, serForm.getBranchId());
			pst.setString(3, docuType);
			rst = pst.executeQuery();
			if (rst.next()) {
				existe = true;
			}
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("validaGH_FORM_NO()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.closeResources(rst, pst);
		}
		return existe;
	}

	public String verificaRastreo(String tracno, String moduloOrigen, String OpcOrigen) {
		String result = "OK";
		try {

			con = ConnectDB.getConnection();
			pstmt = con.prepareStatement("select pack_ppg_web_2012.fun_guia_verfication(?,?,?) from dual");
			pstmt.setString(1, tracno);
			pstmt.setString(2, moduloOrigen);
			pstmt.setString(3, OpcOrigen);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				result = rs.getString(1) == null ? "OK" : rs.getString(1);
			}
			//AccessLog.Log("after 1stwhl in ConD");
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("verificaRastreo()_Error1:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {				
				resources.closeResources(rs, pstmt);
				resources.cerrarConexion(con);
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("verificaRastreo()_Error2:").append(e).toString());
				e.printStackTrace();
			}
		}
		return result;
	}
	
	@SuppressWarnings({ "rawtypes"})
	public void getCentrosCosto(ConversionForm serForm, Global global) {
		
		try {
			con = ConnectDB.getConnection();
			CentrosCosto centrosCosto =  new CentrosCosto();
			ArrayList result = centrosCosto.fetchCentrosCosto(con, global);
			serForm.getCentrosCostoLabel().clear();
			serForm.getCentrosCostoValue().clear();		
			
			if (!result.isEmpty()) {
				for (int i=0;i<result.size();i++) {
					serForm.setCentrosCostoValue(((ArrayList)result.get(i)).get(0).toString());
					serForm.setCentrosCostoLabel(((ArrayList)result.get(i)).get(1).toString());
					if (((ArrayList)result.get(i)).get(2).toString().equals("Y")) {
						serForm.setCentrosCosto(((ArrayList)result.get(i)).get(0).toString());
					}
					
					if (serForm.getCentrosCosto().trim().length()>0){
						if (((ArrayList)result.get(i)).get(0).toString().equals(serForm.getCentrosCosto())) {
							serForm.setCentrosCosto(((ArrayList)result.get(i)).get(0).toString());
						}
					} else {
						if (((ArrayList)result.get(i)).get(2).toString().equals("Y")) {
							serForm.setCentrosCosto(((ArrayList)result.get(i)).get(0).toString());
						}
					}
					
				}
			}
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getCentrosCosto()_Error1:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				resources.cerrarConexion(con);
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getCentrosCosto()_Error2:").append(e).toString());
				e.printStackTrace();
			}
		}		
	}
	
	@SuppressWarnings({ "rawtypes"})
	public void getDirCentrosCosto(ConversionForm serForm, HttpSession session) {
		
		try {
			con = ConnectDB.getConnection();
			JavAddressLovRecords addressLovRecords = new JavAddressLovRecords();
			HashMap result = addressLovRecords.getOrgionClientAddressCC(con, session, serForm.getCentrosCosto());
			
			//result.put("orignCityCode", orignCityCode);
			//result.put("originColinaCode", AM_GETY_CODE);
			if (!result.get("orgien1").toString().equalsIgnoreCase("")) {
				serForm.setOrgien1(result.get("orgien1").toString());
				serForm.setOrgien2(result.get("orgien2").toString());
				serForm.setOrgientelefono(result.get("orgientelefono").toString());
				serForm.setOrgioncode(result.get("orgioncode").toString());
				serForm.setOrgiencolonia1(result.get("orgiencolonia1").toString());
				serForm.setOrgiencolonia2(result.get("orgiencolonia2").toString());	
			}
					
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getDirCentrosCosto()_Error1:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				resources.cerrarConexion(con);
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getDirCentrosCosto()_Error2:").append(e).toString());
				e.printStackTrace();
			}
		}		
	}
	
	public int insertBookGuiaExtra(ConversionForm generalForm, String trackingNo, Global global) {
		PreparedStatement pst = null;
		int insertCount = 0;
		Long tiempoEntregaL = 0L;
		try {	
			con = ConnectDB.getConnection();
			String shipTypeSEG="STD-T";
			String tiempoEntrega = "";
			if ((generalForm.getDeliveryType().equalsIgnoreCase("H")) || (generalForm.getDeliveryType().equalsIgnoreCase("X"))) {
				tiempoEntrega = generalForm.getHorasEntregaEad();
			} else {
				tiempoEntrega = generalForm.getHorasEntregaOcu();
			}
			
			try { tiempoEntregaL = Long.parseLong(tiempoEntrega);} catch (Exception e) {tiempoEntregaL = 0L;}
			
			if (validateIsTypeSEG(generalForm.getShipType())) {
				int i = 0;
				String shipType = generalForm.getShipType();
				if (generalForm.getShipType().equals("1D")) {
					shipType = "DS";
				}
				for (i = 0; i < generalForm.getShippingTypeSEG().size(); i++) {
					if (generalForm.getShippingTypeSEG().get(i).getShipTypeSEGSrvcPP().equals(shipType)) {
						break;
					}
				}
				shipTypeSEG = generalForm.getShippingTypeSEG().get(i).getShipTypeSEGSrvc();
				if (generalForm.isConfirmationService2D()) {
					shipTypeSEG = generalForm.getShipTypeSEGChange();// JAS01
				}
			}
			//Timestamp fechaDLVY = getDeliveryDateRecord(getTimeStamp(con), Long.parseLong(tiempoEntrega));
			StringBuilder st= new StringBuilder();
			st.append("INSERT INTO BOK_GUIA_HEAD_EXTRA(GE_GUIA_NO, GE_CLNT_ID, GE_CLNT_CCOSTO_ID, GE_CLNT_USER_ID, GE_PROMESA_HRS, GE_MAND_RET");
			if(!shipTypeSEG.isEmpty()){
			    st.append(", SRVC_ID ");
			}
			st.append(")");
			st.append(" VALUES(?, ?, ?, ?, ?, FUN_GET_MAND_IVARET(?)");
			if(!shipTypeSEG.isEmpty()){
			    st.append(", ? ");
			}
			st.append(")");
			//String insertQuery = "INSERT INTO BOK_GUIA_HEAD_EXTRA(GE_GUIA_NO, GE_CLNT_ID, GE_CLNT_CCOSTO_ID, GE_CLNT_USER_ID) VALUES(?, ?, ?, ?)";

			pst = con.prepareStatement(st.toString());
			pst.setString(1, trackingNo);
			pst.setString(2, generalForm.getClientId());
			pst.setString(3, generalForm.getCentrosCosto());
			pst.setString(4, global.getOrigenUserClave());
			pst.setLong(5, tiempoEntregaL);
			//pst.setLong(5, Long.parseLong(tiempoEntrega));			
			//pst.setTimestamp(5, fechaDLVY);
			pst.setString(6, generalForm.getClientId());
			if(!shipTypeSEG.isEmpty()){
				pst.setString(7, shipTypeSEG);
			}
			insertCount = pst.executeUpdate();

//			if (insertCount > 0) {
//				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgAvi).append("insertBookGuiaExtra()_").append(generalForm.getOrgienclave()).append("WEB_GUIA_REFR INSERT SCUCESS").toString());
//			} else {
//				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgAvi).append("insertBookGuiaExtra()_").append(generalForm.getOrgienclave()).append("WEB GUIA REFR INSERT FAILURE").toString());
//			}
			con.commit();
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("insertBookGuiaExtra()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.cerrarPreparedStatement(pst);
			resources.cerrarConexion(con);
		}
		return insertCount;
	}
	
	public int insertCtrlLabelPrn(String genGuiaNumber) {
		
		int insertCount = 0;
		
		try {
			con = ConnectDB.getConnection();
			String query = "{call PACK_CP_PDF.PRO_LABEL_PRN(?)}";
			cst = con.prepareCall(query);
			
			cst.setString(1, genGuiaNumber);
			
			cst.executeQuery();
			
			con.commit();
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("insertCtrlLabelPrn()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.cerrarCallableStatement(cst);
			resources.cerrarConexion(con);
		}
		return insertCount;
	}
	public String getCadenaImpresion(HttpServletRequest request, String rastreo, boolean multicaja, boolean acuseXT, String rastreo_retorno) {
		PreparedStatement pst = null;
		ResultSet rsCad = null;
		StringBuilder cadenaEtiqueta = null;
		Clob myClob = null;
		String rastreos[] = null;
		InputStream is = null;
		try {
			con = ConnectDB.getConnection();
			
			//String query = getFuncionEti(con, );
			//String query = "SELECT CP_ETIQUETA(?) FROM DUAL";
			String query = "";
			
			if (rastreo.indexOf("|")==-1 | multicaja){
				query = "SELECT CP_ETIQUETA_ZPL(?, ?, ?, ?) FROM DUAL";
				pst = con.prepareStatement(query);
				pst.setString(1, rastreo);
				pst.setString(2, "N");
				pst.setString(3, "0");
				pst.setString(4, "A");
			} else {
				//System.out.println("RastreosXT: " + rastreo_retorno);
				rastreos = rastreo_retorno.split("\\|");
				query = "SELECT CP_ETIQUETA_MULTIPLE(?, ?, ?) FROM DUAL";
				pst = con.prepareStatement(query);
				pst.setString(1, rastreo);
				pst.setString(2, "N");
				pst.setString(3, "0");
			}
						
			rsCad = pst.executeQuery();
			
			if (rsCad.next()) {
				myClob = rsCad.getClob(1);
				cadenaEtiqueta = new StringBuilder();
				cadenaEtiqueta.append(ClobToString(myClob));
			}
			
			//System.out.println("Parametro: acuseXT= " + acuseXT);
			if(acuseXT==true) {
				if(rastreos==null) {
					cadenaEtiqueta.append("\n").append(guiaRetorno(con, rastreo,rastreo_retorno).toString());
					is = IOUtils.toInputStream(cadenaEtiqueta, "UTF-8");
					//is = new ByteArrayInputStream(cadenaEtiqueta.toString().getBytes());
				}
				else {
					cadenaEtiqueta.append("\n").append(guiaRetorno(con, rastreos).toString());
					//is = new ByteArrayInputStream(cadenaEtiqueta.toString().getBytes());
					is = IOUtils.toInputStream(cadenaEtiqueta, "UTF-8");
				}
			}
			else {
				is = myClob.getAsciiStream();
			}
			
			
			//new PrintFileExport(request.getSession().getServletContext().getRealPath("/")).writeToGuiaFile(request, cadenaEtiqueta, getTimeStamp(con).getTime());
			//new PrintFileExport(request.getSession().getServletContext().getRealPath("/")).writeToGuiaFile(request, myClob, getTimeStamp(con).getTime());
			new PrintFileExport(request.getSession().getServletContext().getRealPath("/")).writeToGuiaFile(request, is, getTimeStamp(con).getTime());
			//AccessLog.Log(cnct.delete(0,cnct.length()).append(msgAvi).append("getCadenaImpresion()_cadenaEtiqueta INFORMACION PARA IMPRESION ").append(cadenaEtiqueta).toString());
			is.close();
			is = null;
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getCadenaImpresion()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.closeResources(rsCad, pst);
			resources.cerrarConexion(con);
			try {
				if (is != null) {
					is.close();
				}
			} catch (Exception e) {
				AccessLog.Log("getCadenaImpresion()Exception_Error:"+e);
				e.printStackTrace();
			}
		}
		return cadenaEtiqueta.toString();
	}
    private StringBuilder guiaRetorno(Connection conecta2, String p_rastreo,String rastreo_de_retorno) {
    	PreparedStatement pst = null;
		ResultSet rs = null;
		Clob myClob = null;
    	StringBuilder cadena_impresion = new StringBuilder();
    	try {
    		
    		if(!rastreo_de_retorno.isEmpty()) {
    			//Si se genero el rastreo correctamente, debemos obtener la cadena de impresion
    			pst = conecta2.prepareStatement("SELECT CP_ETIQUETA_ZPL(?, ?, ?, ?) FROM DUAL");
    			pst.setString(1, rastreo_de_retorno);
    			pst.setString(2, "N");
    			pst.setString(3, "0");
    			pst.setString(4, "A");
    			//Ejecutamos la consulta
    			rs = pst.executeQuery();
    			//Si obtenemos resultados debemos concatenar a la cadena de etiqueta el resultado
    			if (rs.next()) {
    				myClob = rs.getClob(1);
    				cadena_impresion.append(ClobToString(myClob));
    			}
    			
    		}
    	}catch(Exception ex) {
    		ex.printStackTrace();
    	}
    	finally {
    		resources.closeResources(rs, pst);
    	}
		
    	return cadena_impresion;
    }
    private StringBuilder guiaRetorno(Connection conecta2, String[] rastreosXT) {
    	PreparedStatement pst = null;
		ResultSet rs = null;
		Clob myClob = null;
    	StringBuilder cadena_impresion = new StringBuilder(); 
    	try {
    		for(String p_rastreo:rastreosXT) {
    			
        		if(!p_rastreo.isEmpty()) {
        			//Si se genero el rastreo correctamente, debemos obtener la cadena de impresion
        			pst = conecta2.prepareStatement("SELECT CP_ETIQUETA_ZPL(?, ?, ?, ?) FROM DUAL");
        			pst.setString(1, p_rastreo);
        			pst.setString(2, "N");
        			pst.setString(3, "0");
        			pst.setString(4, "A");
        			//Ejecutamos la consulta
        			rs = pst.executeQuery();
        			//Si obtenemos resultados debemos concatenar a la cadena de etiqueta el resultado
        			if (rs.next()) {
        				myClob = rs.getClob(1);
        				cadena_impresion.append("\n").append(ClobToString(myClob));
        			}
        			if(rs!=null)
        				rs.close();
        			if(pst!=null)
        				pst.close();
        			
        		}
    		}
    	}catch(Exception ex) {
    		ex.printStackTrace();
    	}
    	finally {
    		resources.closeResources(rs, pst);
    	}
    	return cadena_impresion;
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
			} catch (Exception e) {
				AccessLog.Log("ClobToString()Exception_Error:"+e);
				e.printStackTrace();
			}
		}
		return strOut.toString();
	}
	
	/****************************************************************
	 * Metodo para obtener TimeStamp								*
	 ****************************************************************/
	private Timestamp getTimeStamp(Connection conn) {
		PreparedStatement pst = null;
		ResultSet rsTime1 = null;
		Timestamp sysTimeStamp =null;
		try {
			pst = conn.prepareStatement("select sysdate from dual");
			rsTime1 = pst.executeQuery();
			if(rsTime1.next()) {
				sysTimeStamp=rsTime1.getTimestamp(1);
			}
			//AAP//AccessLog.Log(cnct.delete(0,cnct.length()).append(msgAvi).append("getTimeStamp()_").append("SYSDATE WITH TIME STAMP IN SERVICES ACTION ").append(sysTimeStamp).toString());			
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getTimeStamp()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.closeResources(rsTime1, pst);
		}
		return sysTimeStamp;
	}
	public String valBranchVrtl(String destBrnch) {
		String result = "0";
		try {
			con = ConnectDB.getConnection();
			pstmt = con.prepareStatement("SELECT BM_FLAG2 FROM SYS_BRNC_MSTR WHERE BM_BRNC_ID = ?");
			pstmt.setString(1, destBrnch);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				result = rs.getString(1) == null ? "0" : rs.getString(1);
			}
			//System.out.println("sucursal virtual "+result);
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("valBranchVrtl()_Error1:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				resources.closeResources(rs, pstmt);
				resources.cerrarConexion(con);
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("valBranchVrtl()_Error2:").append(e).toString());
				e.printStackTrace();
			}
		}
		return result;
	}
	@SuppressWarnings("rawtypes")
	public int insertBookGuiaRel(ConversionForm generalForm, String trackingNo, Global global, ArrayList guias) {//AAP07 REGISTRO MULTICAJA
		PreparedStatement pst = null;		
		int insertCount = 0;
		String genMultiCaja = "N";
		try {			
			con = ConnectDB.getConnection();

			String insertQuery = "INSERT INTO BOK_GUIA_REL(GR_GUIA_NO, GR_GUIA_REL, GR_FLAG_1, CRTD_ON, CRTD_BY, MDFD_ON, MDFD_BY) VALUES(?, ?, ?, SYSDATE, ?, SYSDATE, ?)";
			if (generalForm.isGenMultiCaja()) {
				for (int i=0;i<guias.size();i++) {
					pst = con.prepareStatement(insertQuery);
					pst.setString(1, trackingNo);
					pst.setString(2, guias.get(i).toString());
					pst.setString(3, "P");
					pst.setString(4, global.getOrigenUserClave());
					pst.setString(5, global.getOrigenUserClave());
				
					insertCount = pst.executeUpdate();
					
					pst.close();
					
					if (!guias.get(i).toString().equals(trackingNo)) {
						pst = con.prepareStatement("UPDATE PPG_BOK_GUIA_DTL SET BGD_CONV_DATE = SYSDATE, MDFD_ON = SYSDATE, MDFD_BY = ?, BGM_DEST_BRN_ID = ?, BGM_DEST_SITE_ID = ?, BGM_DEST_CLNT_ID = ?, BGD_GH_CONTENT = ? WHERE BGD_TRAC_NO = ?");
						pst.setString(1, global.getOrigenUserClave());
						pst.setString(2, generalForm.getDestBranch());//System.out.println("generalForm.getDestbranch() "+generalForm.getDestbranch());System.out.println("generalForm.getDestBranch() "+generalForm.getDestBranch());
						pst.setString(3, generalForm.getDestSite());
						pst.setString(4, generalForm.getDestclave());
						pst.setString(5, generalForm.getContent());
						pst.setString(6, guias.get(i).toString());

						pst.executeUpdate();
						
						pst.close();
					}				
				}
				genMultiCaja = "Y";
			} else {
				pst = con.prepareStatement(insertQuery);
				pst.setString(1, trackingNo);
				pst.setString(2, trackingNo);				
				pst.setString(3, "P");
				pst.setString(4, global.getOrigenUserClave());
				pst.setString(5, global.getOrigenUserClave());
			
				insertCount = pst.executeUpdate();
				
				pst.close();
				
				genMultiCaja = "N";
			}			
			
			insertQuery = "UPDATE SYS_CLNT_USER SET CU_GEN_MULTICAJA = ? WHERE CU_CLNT_ID = ? AND CU_USER_ID = ?";
			pst = con.prepareStatement(insertQuery);
			pst.setString(1, genMultiCaja);
			pst.setString(2, global.getClientIdAgreement());
			pst.setString(3, global.getOrigenUserClave());
			
			pst.executeUpdate();
			con.commit();
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("insertBookGuiaRel()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {			
			resources.cerrarPreparedStatement(pst);
			resources.cerrarConexion(con);
		}
		return insertCount;
	}
	
	public boolean getBanderaMulticaja(Global global) {
		boolean result = false;
		try {
			con = ConnectDB.getConnection();
			pstmt = con.prepareStatement("SELECT NVL(CU_GEN_MULTICAJA,'N') FROM SYS_CLNT_USER WHERE CU_CLNT_ID = ? AND CU_USER_ID = ?");
			pstmt.setString(1, global.getClientIdAgreement());
			pstmt.setString(2, global.getOrigenUserClave());			

			rs = pstmt.executeQuery();

			while (rs.next()) {				
				if (rs.getString(1).equals("Y")) {
					result = true;
				}
			}
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getBanderaMulticaja()_Error1:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {				
				resources.closeResources(rs, pstmt);
				resources.cerrarConexion(con);
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getBanderaMulticaja()_Error2:").append(e).toString());
				e.printStackTrace();
			}
		}
		return result;
	}
	public String valGuiaModificada(String trackingNo) {		
		String guiaRefNo = "";
		String facturaMod = "";
		try {
			con = ConnectDB.getConnection();
			pstmt = con.prepareStatement("SELECT BGM_REF_NO FROM PPG_BOK_GUIA_MSTR M, PPG_BOK_GUIA_DTL D WHERE M.BGM_REF_NO = D.BGD_REF_NO AND D.BGD_TRAC_NO = ? AND BGM_REF_NO_OLD IS NOT NULL");
			pstmt.setString(1, trackingNo);						
			
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				guiaRefNo = rs.getString(1);
			}			
			
			rs.close();
			pstmt.close();
			
			pstmt = con.prepareStatement("SELECT DECODE(S_BRANCH_ID, NULL, IPH_BRANCH_ID||IPH_BOK_INV_REF_NO, S_BRANCH_ID||S_INV_REF_NO) FACTURA FROM PPG_INV_PMT_HDR WHERE IPH_INV_NO IN (SELECT IPD_INV_NO FROM PPG_INV_PMT_DTL WHERE IPD_REF_NO = ?)");
			pstmt.setString(1, guiaRefNo);
			
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				facturaMod = rs.getString(1);				
			}			
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("valGuiaModificada()_Error1:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {	
				if (rs != null) {
					rs.close();
				}				
				if (pstmt !=null) {
					pstmt.close();
				}				
				if (con!= null){
					con.close();
				}
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("valGuiaModificada()_Error2:").append(e).toString());
				e.printStackTrace();
			}
		}
		return facturaMod;
	}
	public int insertwebGuiaRefr(String referencia, String trackingNo, Global global, String mainRefr) {
		PreparedStatement pst = null;
		int insertCount = 0;
		try {	
			con = ConnectDB.getConnection();
			String insertQuery = "INSERT INTO WEB_GUIA_REFR(GR_GUIA_NO, GR_GUIA_TYPE, GR_DOCU_TYPE, GR_GUIA_REFR, CRTD_ON, CRTD_BY, MDFD_ON, MDFD_BY, GR_MAIN_REFR) VALUES (?, ?, ?, ?, SYSDATE, ?, SYSDATE, ?, ?)";

			pst = con.prepareStatement(insertQuery);
			pst.setString(1, trackingNo);
			pst.setString(2, "H");
			pst.setString(3, "P");
			pst.setString(4, referencia);
			pst.setString(5, global.getOrigenUserClave());
			pst.setString(6, global.getOrigenUserClave());
			pst.setString(7, mainRefr);

			insertCount = pst.executeUpdate();

//			if (insertCount > 0) {
//				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgAvi).append("insertBookGuiaExtra()_").append(generalForm.getOrgienclave()).append("WEB_GUIA_REFR INSERT SCUCESS").toString());
//			} else {
//				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgAvi).append("insertBookGuiaExtra()_").append(generalForm.getOrgienclave()).append("WEB GUIA REFR INSERT FAILURE").toString());
//			}
			con.commit();
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("insertwebGuiaRefr()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.cerrarPreparedStatement(pst);
			resources.cerrarConexion(con);
		}
		return insertCount;
	}
	
	public String getRateConfig() {
		//String porcentaje = "";
		//ArrayList list = new ArrayList();
		String rateConfig = "PPGSERVICE2017";
		try {
			con = ConnectDB.getConnection();
			
			/*obtiene configuracion de descuentos a usar*/
			pstmt = con.prepareStatement("SELECT PM_PARM_CODE1 FROM sys_parm_mstr WHERE pm_mdul_id = ? AND pm_parm_type = ?");
			pstmt.setString(1, "PPG");
			pstmt.setString(2, "PPGSERVICEUSE");
			
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				rateConfig = rs.getString(1)==null ?"PPGSERVICE2017":rs.getString(1);
			}
			
		} catch (Exception e) {
			AccessLog.Log(e.getMessage());
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();				
				if (con != null)
					con.close();
			} catch (Exception e) {
				AccessLog.Log(e.getMessage());
				e.printStackTrace();
			}
		}
		return rateConfig;
	}
	public String isInsideTimeCourtSEG(String orgnBrnc, String destBrnc, String srvcTypeId, String clienID, String addrCode){
    	PreparedStatement pst = null;
		ResultSet rst = null;
		String isInsideTimeCourt ="OUT";//Fuera de horario de corte pero si se tiene servicio.
		try {
			con = ConnectDB.getConnection();
			//CommonFunction commonobject = new CommonFunction();
			pst = con.prepareStatement(getInsideTimeCourt);
			pst.setString(1, srvcTypeId);
			pst.setString(2, orgnBrnc);
			pst.setString(3, destBrnc);
			pst.setString(4, clienID);
			pst.setString(5, addrCode);
			rst = pst.executeQuery();
			if(rst.next()) {
				String valor = rst.getString(1);
				if(valor.equalsIgnoreCase("1")){//Dentro del horario de corte
					isInsideTimeCourt = "INSIDE";
				}else if(valor.equalsIgnoreCase("3")){//Sin horario de corte, es decir, sin servicio
					isInsideTimeCourt = "WITHOUTSERVICE";
				}
			}			
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("isInsideTimeCourtSEG()_Error1:").append(e).toString());
			e.printStackTrace();
			isInsideTimeCourt = "WITHOUTSERVICE";
		} finally {
			try {
				if (rst != null) {
					rst.close();
				}
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("isInsideTimeCourtSEG()_Error2:").append(e).toString());
				e.printStackTrace();
			}
			try {
				if (pst != null) {
					pst.close();	
				}
				
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("isInsideTimeCourtSEG()_Error3:").append(e).toString());
				e.printStackTrace();
			}
			
			try {
				if (con != null) {
					con.close();	
				}				
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("isInsideTimeCourtSEG()_Error4:").append(e).toString());
				e.printStackTrace();
			}
		}
		return isInsideTimeCourt;
	}

	public boolean getCoverageSEG(String codColo, String destClient, String destAddrCode, String srvcType) {
		ResultSet rst = null;
		boolean iscobertura =false;
		try {
			con = ConnectDB.getConnection();
		    if(codColo == null ) {
			codColo ="";
		    }
		    if(codColo.isEmpty()){
		    	
				pstmt = con.prepareStatement(destClientColonia);
				pstmt.setString(1, destClient);
				pstmt.setString(2, destAddrCode);
	
				rs = pstmt.executeQuery();
				while (rs.next()) {
				    codColo = rs.getString(1);
				}
				// AccessLog.Log("after 18thwhl in ConD");
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
		    }
			//CommonFunction commonobject = new CommonFunction();
		    pstmt = con.prepareStatement(getCobertura);

		    pstmt.setString(1, srvcType);
		    pstmt.setString(2, codColo);
			//System.out.println("clave colonia: "+codColo);

		    rst = pstmt.executeQuery();
			if(rst.next()) {
				String valor = rst.getString(1);
				iscobertura=valor.equalsIgnoreCase("1");
			}			
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getCoverageSEG()_Error1:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				if (rst != null) {
					rst.close();
				}
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getCoverageSEG()_Error2:").append(e).toString());
				e.printStackTrace();
			}
			
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getCoverageSEG()_Error4:").append(e).toString());
				e.printStackTrace();
			}
			
			try {
				if (pstmt != null) {
					pstmt.close();
				}
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getCoverageSEG()_Error5:").append(e).toString());
				e.printStackTrace();
			}
			
			try {
				if (con != null) {
					con.close();	
				}
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getCoverageSEG()_Error6:").append(e).toString());
				e.printStackTrace();
			}
		}
		return iscobertura;
	}

	public Double[] getTariffSEG(String srvcTypeId, String zoneEndKm){
		PreparedStatement pst = null;
		ResultSet rst = null;
		Double[] tarifa = new Double[2];//Fuera de horario de corte pero si se tiene servicio.
		Double tarifaAmnt= 0.0, tarifaAmntExen =0.0;
		try {
			String zoneID=getZoneIDSEG(zoneEndKm);
			con = ConnectDB.getConnection();
			//CommonFunction commonobject = new CommonFunction();
			pst = con.prepareStatement("SELECT  TRIF_AMNT, TRIF_AMNT_EXED FROM SEG_TARIF_MSTR where SRVC_ID = ? and ZONE_ID = ? ");
			pst.setString(1, srvcTypeId);
			pst.setString(2, zoneID);
			rst = pst.executeQuery();
			if(rst.next()) {
				tarifaAmnt= rst.getDouble(1);
				tarifaAmntExen= rst.getDouble(2);
			}			
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getTariffSEG()_Error1:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				if (rst != null) {
					rst.close();
				}
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getTariffSEG()_Error2:").append(e).toString());
				e.printStackTrace();
			}
			try {
				if (pst != null) {
					pst.close();	
				}
				
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getTariffSEG()_Error3:").append(e).toString());
				e.printStackTrace();
			}
			
			try {
				if (con != null) {
					con.close();	
				}				
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getTariffSEG()_Error4:").append(e).toString());
				e.printStackTrace();
			}
		}
		tarifa[0]=tarifaAmnt;
		tarifa[1]=tarifaAmntExen;
		return tarifa;
	}

	public String getZoneIDSEG(String zoneEndKm){
		PreparedStatement pst = null;
		ResultSet rst = null;
		String zoneID = null;
		try {
			con = ConnectDB.getConnection();
			//CommonFunction commonobject = new CommonFunction();
			pst = con.prepareStatement("select SCZ_ZONE_ID from SYS_COVER_ZONE where SCZ_END_KM =? AND SCZ_ZONE_ACTIVE='A' and DELETE_FLAG = 'N'");
			pst.setString(1, zoneEndKm);
			rst = pst.executeQuery();
			if(rst.next()) {
				zoneID= rst.getString(1);
			}			
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getZoneIDSEG()_Error1:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				if (rst != null) {
					rst.close();
				}
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getZoneIDSEG()_Error2:").append(e).toString());
				e.printStackTrace();
			}
			try {
				if (pst != null) {
					pst.close();	
				}
				
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getZoneIDSEG()_Error3:").append(e).toString());
				e.printStackTrace();
			}
			
			try {
				if (con != null) {
					con.close();	
				}				
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getZoneIDSEG()_Error4:").append(e).toString());
				e.printStackTrace();
			}
		}
		return zoneID;
	}

	public Timestamp getDeliveryDateRecord(Timestamp FECHA_DOC , Long diaPromesa) {
		Timestamp horasEntrega = null;
		JavDeliveryHours horas = new JavDeliveryHours();
		Connection con = null;
		try {
			con = ConnectDB.getConnection();
			horasEntrega = horas.getDeliveryDateRecord(con, FECHA_DOC,diaPromesa);
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("getDeliveryDateRecord()_Error1:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				if (con != null)
					con.close();
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("getDeliveryDateRecord()_Error2:").append(e).toString());
				e.printStackTrace();
			}
		}
		return horasEntrega;
	}

	public List<String> getExclusiveTypeShipSEG(){
    	PreparedStatement pst = null;
		ResultSet rst = null;
		List<String> typeShipSEG = new ArrayList<String>();
		Connection con = null;
		try {
			con = ConnectDB.getConnection();
			pst = con.prepareStatement(getExclusiveTypeShipSEG);
			rst = pst.executeQuery();
			while(rst.next()) {
				typeShipSEG.add(rst.getString(1));
			}			
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getExclusiveTypeShipSEG()_Error1:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				if (rst != null) {
					rst.close();
				}
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getExclusiveTypeShipSEG()_Error2:").append(e).toString());
				e.printStackTrace();
			}
			try {
				if (pst != null) {
					pst.close();	
				}
				
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getExclusiveTypeShipSEG()_Error3:").append(e).toString());
				e.printStackTrace();
			}
			
			try {
				if (con != null) {
					con.close();	
				}				
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getExclusiveTypeShipSEG()_Error4:").append(e).toString());
				e.printStackTrace();
			}
		}
		return typeShipSEG;
	}
	
	public boolean validateIsTypeSEG(String typeService){
		 List<String> list = getExclusiveTypeShipSEG();
		 if(typeService.equalsIgnoreCase("1D")) {
			 typeService = "DS";
		 }
		for (String string : list) {
			if(typeService.equalsIgnoreCase(string)){
				return true;
			}
		}
		
		return false;
	}
	
	public List<ShipTypeSEG> getFetchShipSEGALLActive(String orgnBrncId, String destBrncID, String clienID, String addrCode) {
		PreparedStatement pst = null;
		ResultSet rst = null;
		List<ShipTypeSEG> listSEG = null;
		Connection con = null;
		try {
			con = ConnectDB.getConnection();
			String str = fetchShipSEGALL;
			str = str.replace(":GH_ORGN_BRNC_ID", "'"+orgnBrncId+"'");
			str = str.replace(":GH_DEST_BRNC_ID", "'"+destBrncID+"'");
			str = str.replace(":CLIENTEID", "'"+clienID+"'");
			str = str.replace(":ADDRCODE", "'"+addrCode+"'");
			pst = con.prepareStatement(str);
			rst = pst.executeQuery();
			listSEG = new ArrayList<ShipTypeSEG>();
			ShipTypeSEG shipTypeSEG = null;
			while (rst.next()) {
				shipTypeSEG = new ShipTypeSEG();
				shipTypeSEG.setShipTypeSEGSrvcDesc(rst.getString(1));
				shipTypeSEG.setShipTypeSEGSrvc(rst.getString(2));
				shipTypeSEG.setShipTypeSEGSrvcPP(rst.getString(3));
				listSEG.add(shipTypeSEG);
			}
			if (rst != null) {
				rst.close();
				rst = null;
			}
			if (pst != null) {
				pst.close();
				pst = null;
			}
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("getFetchShipSEGALLActive()_Error1:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				if (rst != null) {
					rst.close();
				}
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("getFetchShipSEGALLActive()_Error2:").append(e).toString());
				e.printStackTrace();
			}
			try {
				if (pst != null) {
					pst.close();
				}

			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("getFetchShipSEGALLActive()_Error3:").append(e).toString());
				e.printStackTrace();
			}

			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("getFetchShipSEGALLActive()_Error4:").append(e).toString());
				e.printStackTrace();
			}
		}
		return listSEG;
	}
	public String getTipoTarifa(double peso, double volumen) {
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		String tipoTarifa = "";//monto de cobro tarifa normal.
		try {
			con = ConnectDB.getConnection();
			String query = "";		
			
			query = "select FUN_GET_TARIFF(?, ?) from dual";
			pst = con.prepareStatement(query);		
			
			pst.setDouble(1, peso);
			pst.setDouble(2, volumen);
			
			rs = pst.executeQuery();
			
			if (rs.next()) {
				tipoTarifa = rs.getString(1) == null ? "" : rs.getString(1).substring(0,2);
			}
		} catch (Exception e) {
			System.out.println(cnct.delete(0,cnct.length()).append(msgErr).append("getTipoTarifa()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e2) {
				System.out.println(cnct.delete(0, cnct.length()).append(msgErr).append("getTipoTarifa()_Error2: ").append(e2).toString());
				e2.printStackTrace();
			}
                        try {  
				if (pst != null) {
					pst.close();
				}
			} catch (Exception e2) {
				System.out.println(cnct.delete(0, cnct.length()).append(msgErr).append("getTipoTarifa()_Error2: ").append(e2).toString());
				e2.printStackTrace();
			}
                        try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e2) {
				System.out.println(cnct.delete(0, cnct.length()).append(msgErr).append("getTipoTarifa()_Error2: ").append(e2).toString());
				e2.printStackTrace();
			}
		}
		return tipoTarifa;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList getClientrecords(String siteId,
			String clientId) throws Exception {
		Connection cone = null;
		HashMap values = null;
		ArrayList result = new ArrayList();
		CallableStatement cst = null;
		PreparedStatement psmt = null;
		PreparedStatement psmt1 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		
		String AM_ADDR_CODE = null, AM_DRNR = null, AM_STRT_NAME = null, AM_PHNO1 = null, AM_ADDR_STYP = null, AM_ADDR_DEFN_TYPE = null, AM_ADDR_REF_NO = null, AM_GETY_LEVL = null, AM_GETY_TYPE = null, AM_GETY_CODE = null;
		try {
			cone = ConnectDB.getConnection();
			String query = "SELECT CM_CLNT_ID, CM_CLNT_NAME,AM_STRT_NAME,"
					+ "AM_GETY_CODE, AM_GETY_LEVL, AM_GETY_TYPE, AM_ADDR_CODE, AM_PHNO1,"
					+ "AM_DRNR, AM_ADDR_STYP, AM_ADDR_REF_NO, AM_ADDR_DEFN_TYPE FROM SYS_CLNT_VIEW WHERE "
					+ "AM_PE_BRNC_ID = ? AND CM_CLNT_ID = ?";
			psmt = cone.prepareStatement(query);
			
			psmt.setString(1, siteId);

			clientId = clientId.trim();

			psmt.setString(2, clientId);
			rs = psmt.executeQuery();
			String clientName1 = "";

			String u11 = null, u12 = null, u13 = null, u14 = null, u15 = null, u16 = null, u17 = null;
			String c11 = null, c12 = null, c13 = null, c14 = null, c15 = null, c16 = null, zipcode = null;
			
			while (rs.next()) {
				clientId = rs.getString(1);
				clientName1 = rs.getString(2);
				AM_STRT_NAME = rs.getString(3);
				AM_GETY_CODE = rs.getString(4);
				AM_GETY_LEVL = rs.getString(5);
				AM_GETY_TYPE = rs.getString(6);
				AM_ADDR_CODE = rs.getString(7);
				AM_PHNO1 = rs.getString(8);
			
				AM_DRNR = rs.getString(9);
				AM_ADDR_STYP = rs.getString(10);
				AM_ADDR_REF_NO = rs.getString(11);
				AM_ADDR_DEFN_TYPE = rs.getString(12);


				AM_ADDR_CODE = (AM_ADDR_CODE != null ? AM_ADDR_CODE : "");
				AM_DRNR = (AM_DRNR != null ? AM_DRNR : "");
				AM_STRT_NAME = (AM_STRT_NAME != null ? AM_STRT_NAME : "");
				AM_PHNO1 = (AM_PHNO1 != null ? AM_PHNO1 : "");
				AM_ADDR_STYP = (AM_ADDR_STYP != null ? AM_ADDR_STYP : "");
				AM_ADDR_DEFN_TYPE = (AM_ADDR_DEFN_TYPE != null ? AM_ADDR_DEFN_TYPE : "");
				AM_ADDR_REF_NO = (AM_ADDR_REF_NO != null ? AM_ADDR_REF_NO : "");
				AM_GETY_LEVL = (AM_GETY_LEVL != null ? AM_GETY_LEVL : "");
				AM_GETY_TYPE = (AM_GETY_TYPE != null ? AM_GETY_TYPE : "");
				AM_GETY_CODE = (AM_GETY_CODE != null ? AM_GETY_CODE : "");

				cst = cone.prepareCall("{call pack_web.pro_ftch_addr(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) }");

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

				u11 = (cst.getString(6) != null ? cst.getString(6) : "");
				u12 = (cst.getString(7) != null ? cst.getString(7) : "");
				u13 = (cst.getString(8) != null ? cst.getString(8) : "");
				u14 = (cst.getString(9) != null ? cst.getString(9) : "");
				u15 = (cst.getString(10) != null ? cst.getString(10) : "");
				u16 = (cst.getString(11) != null ? cst.getString(11) : "");
				u17 = (cst.getString(12) != null ? cst.getString(12) : "");
				zipcode = (cst.getString(13) != null ? cst.getString(13) : "");
				c11 = (cst.getString(14) != null ? cst.getString(14) : "");
				c12 = (cst.getString(15) != null ? cst.getString(15) : "");
				c13 = (cst.getString(16) != null ? cst.getString(16) : "");
				c14 = (cst.getString(17) != null ? cst.getString(17) : "");
				c15 = (cst.getString(18) != null ? cst.getString(18) : "");
				c16 = (cst.getString(19) != null ? cst.getString(19) : "");

				if (cst != null)
					cst.close();
				String rfc = "";

				psmt1 = cone.prepareStatement("select PACK_WEB.fun_ftch_rfc(?) FROM dual");
				psmt1.setString(1, clientId);
				rs1 = psmt1.executeQuery();
				while (rs1.next()) {
					rfc = rs1.getString(1);
					if (rfc == null) {
						rfc = "";
					}
				}

				if (rs1 != null)
					rs1.close();
				if (psmt1 != null)
					psmt1.close();

				values = new HashMap();
				values.put("clientName", clientName1);
				values.put("clientId", clientId);
				values.put("AM_ADDR_CODE", AM_ADDR_CODE);
				values.put("AM_DRNR", AM_DRNR);
				values.put("AM_STRT_NAME", AM_STRT_NAME);
				values.put("AM_PHNO1", AM_PHNO1);
				values.put("AM_ADDR_STYP", AM_ADDR_STYP);
				values.put("AM_ADDR_DEFN_TYPE", AM_ADDR_DEFN_TYPE);
				values.put("AM_GETY_LEVL", AM_GETY_LEVL);
				values.put("AM_GETY_TYPE", AM_GETY_TYPE);
				values.put("AM_GETY_CODE", AM_GETY_CODE);
				values.put("u11", u11);
				values.put("u12", u12);
				values.put("u13", u13);
				values.put("u14", u14);
				values.put("u15", u15);
				values.put("u16", u16);
				values.put("u17", u17);
				values.put("Zipcode", zipcode);
				values.put("c11", c11);
				values.put("c12", c12);
				values.put("c13", c13);
				values.put("c14", c14);
				values.put("c15", c15);
				values.put("c16", c16);
				values.put("rfc", rfc);
				
				result.add(values);
			}
			
			if (rs != null)
				rs.close();
			
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("getClientrecords()Error:").append(e).toString());
			e.printStackTrace();
            } finally {
                try {
                    if (rs1 != null) {
                        rs1.close();
                    }
                } catch (Exception e2) {
                    AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("getClientrecords()Error:").append(e2).toString());
                    e2.printStackTrace();
                }
                try {
                    if (rs != null) {
                        rs.close();
                    }
                } catch (Exception e2) {
                    AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("getClientrecords()Error:").append(e2).toString());
                    e2.printStackTrace();
                }
                try {
                    if (psmt1 != null) {
                        psmt1.close();
                    }
                } catch (Exception e2) {
                    AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("getClientrecords()Error:").append(e2).toString());
                    e2.printStackTrace();
                }
                try {
                    if (psmt != null) {
                        psmt.close();
                    }
                } catch (Exception e2) {
                    AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("getClientrecords()Error:").append(e2).toString());
                    e2.printStackTrace();
                }
                try {
                    if (cst != null) {
                        cst.close();
                    }
                } catch (Exception e2) {
                    AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("getClientrecords()Error:").append(e2).toString());
                    e2.printStackTrace();
                }
                try {
                    if (cone != null) {
                        cone.close();
                    }
                } catch (Exception e2) {
                    AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("getClientrecords()Error:").append(e2).toString());
                    e2.printStackTrace();
                }
            }
		return result;
	}
	
	/****************************************************************************
	* Metodo para obtener bandera de validacion de referencia de cliente        *
	****************************************************************************/
	public String getFlagValidRefrClnt(Global global) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		String flagRefer ="0";
		Connection con = null;
		try {			
			con = ConnectDB.getConnection();
			pst = con.prepareStatement("select NVL(FUN_ISREQ_REFER(?),'0') FROM DUAL");
			
			pst.setString(1, global.getClientIdAgreement());
			
			rs = pst.executeQuery();
			
			if(rs.next()){
				flagRefer=rs.getString(1);
			}			
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getFlagValidRefrClnt()_Error:").append(e).toString());
			e.printStackTrace();
		}finally{
			resources.closeResources(rs, pst);
			resources.cerrarConexion(con);
		}
		return flagRefer.trim();
	}
	
	/****************************************************************************
	* Metodo para validacion de referencia de cliente        					*
	****************************************************************************/
	public HashMap<String, String> validRefrClnt(ConversionForm aform, Global global) {
		CallableStatement cst = null;
		String msgeRefr = "OK";
		String valResponse = "1";
		int posicion = 0;
		HashMap<String, String> valuesReturn = new HashMap<>(2);
		Connection con = null;
		try {
			con = ConnectDB.getConnection();
			posicion = getTotalReferences(aform)+1;
			
			String query = "Begin ? := FUN_VALID_REFER(?, ?, ?, ?); End;";			
			
			cst = con.prepareCall(query);

			cst.registerOutParameter(1, Types.VARCHAR);
			cst.setString(2, global.getClientIdAgreement());
			cst.setString(3, aform.getReference());
			cst.setInt(4, posicion);
			cst.registerOutParameter(5, Types.VARCHAR);
			
			cst.executeQuery();
			
			valResponse = cst.getString(1) == null ? "" : cst.getString(1);
			msgeRefr = cst.getString(5) == null ? "" : cst.getString(5);
			
			valuesReturn.put("valResponse", valResponse);
			valuesReturn.put("msgeRefr", msgeRefr);
			
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("validRefrClnt()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.cerrarCallableStatement(cst);
			resources.cerrarConexion(con);
		}
		return valuesReturn;
	}
	
	private int getTotalReferences(ConversionForm aform) {
		int length = 0;
		String[] listRefers = null;
		try {
			if (aform.getListReferences().trim().length()>0) {
				listRefers = aform.getListReferences().split("\\|");
				length = listRefers.length;				
			}			
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getTotalReferences()_Error:").append(e).toString());
			e.printStackTrace();
		}
		return length;
	}

	public String getCompanyId(String peso, String clntId, String srvcId) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		String companyId =null;
		Connection con = null;
		try {
			con = ConnectDB.getConnection();
			String query = "select FUN_GET_CPNY_BY_WEIGHT_CLIENT(?, ?, ?) from dual";
			pst = con.prepareStatement(query);
			pst.setDouble(1,Double.parseDouble(peso));
			pst.setString(2, clntId);
			pst.setString(3, srvcId);
			
			rs = pst.executeQuery();
			
			if (rs.next()) {
				companyId = rs.getString(1);
			}
			
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("getCompanyId()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.closeResources(rs, pst);
			resources.cerrarConexion(con);
		}
		return companyId;
	}

	public String getRetIvaByCompany(String companyID) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		String retiene = "N";
		Connection con = null;
		try {
			con = ConnectDB.getConnection();
			pst = con.prepareStatement("SELECT FUN_GET_RET_FLAG_BY_CPNY(?) FROM DUAL");
			pst.setString(1, companyID);

			rs = pst.executeQuery();

			if (rs.next()) {
				retiene = rs.getString(1) == null ? "" : rs.getString(1);
			}
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("getRetIvaByCompany()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.closeResources(rs, pst);
			resources.cerrarConexion(con);
		}
		return retiene;
	}
	@SuppressWarnings("rawtypes")
	public String getMsgDimensionesIngresar() {
		String result = "";
		Connection con = null;
		try {
			con = ConnectDB.getConnection();
			ConsultaParametros cons = new ConsultaParametros();
			ArrayList temp = cons.QryMdulTypeParm1(con, "WEB", "TMP_MSGE", "MSGDIMINGR");
			if (!temp.isEmpty()) {
				result = temp.isEmpty() ? "" : ((ArrayList) temp .get(0)).get(4).toString();// obteniendo PM_VLUE1_DESC	
			}
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("getMsgDimensionesIngresar()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.cerrarConexion(con);
		}
		return result;
	}
	public String branchLocationType(String brncId){//AAP20
		String result = "";
		JavBranchRecords brncRec = new JavBranchRecords();
		result = brncRec.branchLocationType(brncId);
		return result;
	}
	
	public String getTaxesBranch(String locationType) {//AAP20
		
		String result = "";
		try {
			con = ConnectDB.getConnection();
			pstmt = con.prepareStatement("SELECT PM_PARM_CODE2 FROM SYS_PARM_MSTR WHERE PM_MDUL_ID = ? AND PM_PARM_TYPE = ? AND PM_PARM_CODE1 = ?");
			pstmt.setString(1, "SYS");
			pstmt.setString(2, "BRANCH_LOCATION");
			pstmt.setString(3, locationType);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				result = rs.getString(1);
			}
			
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("getInvBase()_Error:").append(e).toString());
			e.printStackTrace();
			return "";
		} finally {
			resources.closeResources(rs, pstmt);
			resources.cerrarConexion(con);
		}
		return result;
	}
	
	public float getInvBase() {//AAP20
		float total = 0;
		try {
			con = ConnectDB.getConnection();
			pstmt = con.prepareStatement("SELECT SRVC_CONF.PM_VLUE2_ID FROM SYS_PARM_MSTR CONF_USE " + 
					"LEFT join SYS_PARM_MSTR SRVC_CONF ON CONF_USE.PM_MDUL_ID = SRVC_CONF.PM_MDUL_ID AND CONF_USE.PM_PARM_CODE1 = SRVC_CONF.PM_PARM_TYPE AND SRVC_CONF.PM_PARM_CODE1 = ?" + 
					"WHERE CONF_USE.PM_MDUL_ID = ? AND CONF_USE.PM_PARM_TYPE = ?");
			
			pstmt.setString(1, "INV");
			pstmt.setString(2, "PPG");
			pstmt.setString(3, "PPGSERVICEUSE");
			
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				total = rs.getFloat(1);
			}
			
			if (total <= 0) {
				total = 9.8f;
			}
			
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("getInvBase()_Error:").append(e).toString());
			e.printStackTrace();
			return 9;
		} finally {
			resources.closeResources(rs, pstmt);
			resources.cerrarConexion(con);
		}
		return total;
	}
	
	private void serviceRecalc(Connection conn, String companyIdOriginal, String clienteID, String bsnsLine, String refNo, HashMap<String, ArrayList<Object>> mapService) {

		int shpGTaxRetPerc = 0;
		int EADTaxRetPerc = 0;
		int RADTaxRetPerc = 0;
		int INVTaxRetPerc = 0;
		int ACKTaxRetPerc = 0;
		int EXTTaxRetPerc = 0;

//		HashMap<String, ArrayList<Object>> mapService = null;

		String shpGCpnyId = "";
		String EADCpnyId = "";
		String RADCpnyId = "";
		String INVCpnyId = "";
		String ACKCpnyId = "";
		String EXTCpnyId = "";
		try {

			/*
			 * envia a actualizar ppg_bok_guia_mstr para asignacion de tasas de iva retenido
			 * y empresas
			 */
			mapService = getServiceConfig(con, clienteID, companyIdOriginal, bsnsLine, true);

			shpGTaxRetPerc = getTaxRetPercConfig(mapService.get("SHP-G"));
			EADTaxRetPerc = getTaxRetPercConfig(mapService.get("EAD"));
			RADTaxRetPerc = getTaxRetPercConfig(mapService.get("RAD"));
			INVTaxRetPerc = getTaxRetPercConfig(mapService.get("INV"));
			ACKTaxRetPerc = getTaxRetPercConfig(mapService.get("ACK"));
			EXTTaxRetPerc = getTaxRetPercConfig(mapService.get("EXT"));

			shpGCpnyId = getCpnyConfig(mapService.get("SHP-G"), companyIdOriginal);
			EADCpnyId = getCpnyConfig(mapService.get("EAD"), companyIdOriginal);
			RADCpnyId = getCpnyConfig(mapService.get("RAD"), companyIdOriginal);
			INVCpnyId = getCpnyConfig(mapService.get("INV"), companyIdOriginal);
			ACKCpnyId = getCpnyConfig(mapService.get("ACK"), companyIdOriginal);
			EXTCpnyId = getCpnyConfig(mapService.get("EXT"), companyIdOriginal);

			updatePpgBokGuiaMstr(conn, refNo, shpGTaxRetPerc, EADTaxRetPerc, RADTaxRetPerc, INVTaxRetPerc,
					ACKTaxRetPerc, EXTTaxRetPerc, shpGCpnyId, EADCpnyId, RADCpnyId, INVCpnyId, ACKCpnyId, EXTCpnyId);
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("serviceRecalc()1_Error:").append(e)
					.toString());
			e.printStackTrace();
		}
	}
	public HashMap<String, ArrayList<Object>> getServiceConfig(Connection conn, String clntRet, String companySrvcCalculado, String module, boolean getAllSrvc){//AAP23
		HashMap<String, ArrayList<Object>> mapService = null;
		
		ArrayList<Object> shpGConfig = null;
		ArrayList<Object> EADConfig = null;
		ArrayList<Object> RADConfig = null;
		ArrayList<Object> INVConfig = null;
		ArrayList<Object> ACKConfig = null;
		ArrayList<Object> EXTConfig = null;
		
		try {
			shpGConfig = getTaxRenEspecial(conn, clntRet, "SHP-G", companySrvcCalculado, module);
			
			//no hay configuracion
			if (shpGConfig.isEmpty()) {
				mapService = new HashMap<>(0);
			} else {
				//no hay configuracion
				if ((int)shpGConfig.get(0)== -1) {
					mapService = new HashMap<>(0);
				} else {
					mapService = new HashMap<>(6);
					mapService.put("SHP-G", shpGConfig);
					
					if (getAllSrvc) {
						EADConfig = getTaxRenEspecial(conn, clntRet, "EAD", companySrvcCalculado, module);
						RADConfig = getTaxRenEspecial(conn, clntRet, "RAD", companySrvcCalculado, module);
						INVConfig = getTaxRenEspecial(conn, clntRet, "INV", companySrvcCalculado, module);
						ACKConfig = getTaxRenEspecial(conn, clntRet, "ACK", companySrvcCalculado, module);
						EXTConfig = getTaxRenEspecial(conn, clntRet, "EXT", companySrvcCalculado, module);
						
						mapService.put("EAD", EADConfig);
						mapService.put("RAD", RADConfig);
						mapService.put("INV", INVConfig);
						mapService.put("ACK", ACKConfig);
						mapService.put("EXT", EXTConfig);
					}					
				}
			}
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("getServiceConfig()1_Error:").append(e).toString());
			e.printStackTrace();
		}
		return mapService;
	}
	
	private int getTaxRetPercConfig(ArrayList<Object> srvcConfig) {
		int taxRetPerc = 0;
		try {
			taxRetPerc = (Integer) srvcConfig.get(0);
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("getTaxRetPercConfig()1_Error:").append(e).toString());
			e.printStackTrace();
		}
		return taxRetPerc;
	}
	
	private String getCpnyConfig(ArrayList<Object> cpnyConfig, String defaultCpny) {
		String cpnyIdConfig = "";
		try {
			if (cpnyConfig.size()>0) {
				cpnyIdConfig = (String) cpnyConfig.get(2);
			} else {
				cpnyIdConfig = defaultCpny;
			}
			
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("getCpnyConfig()1_Error:").append(e).toString());
			e.printStackTrace();
		}
		return cpnyIdConfig;
	}
	
	private void updatePpgBokGuiaMstr(Connection conn, String refNo,
			int shpGTaxRetPerc, int EADTaxRetPerc, int RADTaxRetPerc, int INVTaxRetPerc,int ACKTaxRetPerc, int EXTTaxRetPerc,
			String shpGCpnyId, String EADCpnyId, String RADCpnyId, String INVCpnyId, String ACKCpnyId, String EXTCpnyId) {//AAP23
		
		PreparedStatement pst = null;
		try {
			pst=conn.prepareStatement(
					"update PPG_BOK_GUIA_MSTR set "+
					"BGM_SHP_RETN_TAX_PERC = ?, BGM_ACK_RETN_TAX_PERC = ?, "+
				    "BGM_INV_RETN_TAX_PERC = ?, BGM_EAD_RETN_TAX_PERC = ?, "+
				    "BGM_RAD_RETN_TAX_PERC = ?, BGM_EXT_RETN_TAX_PERC = ?, "+
				    "BGM_CMPY_ID = ?, "+
				    "BGM_ACK_CMPY_ID = ?, BGM_INV_CMPY_ID = ?, "+
				    "BGM_EAD_CMPY_ID = ?, BGM_RAD_CMPY_ID = ?, "+
				    "BGM_EXT_CMPY_ID = ? where BGM_REF_NO = ?"
				    );
			
			pst.setInt(1, shpGTaxRetPerc);
			pst.setInt(2, ACKTaxRetPerc);
			pst.setInt(3, INVTaxRetPerc);
			pst.setInt(4, EADTaxRetPerc);
			pst.setInt(5, RADTaxRetPerc);
			pst.setInt(6, EXTTaxRetPerc);
			pst.setString(7, shpGCpnyId);
			pst.setString(8, ACKCpnyId);
			pst.setString(9, INVCpnyId);
			pst.setString(10, EADCpnyId);
			pst.setString(11, RADCpnyId);
			pst.setString(12, EXTCpnyId);
			pst.setString(13, refNo);
			pst.executeQuery();			
			
			
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("updatePpgBokGuiaMstr()1_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.cerrarPreparedStatement(pst);
		}
	}
	
	private ArrayList<Object> getTaxRenEspecial(Connection conn, String clntRet, String serviceId, String companySrvcCalculado, String module) {//AAP23
		CallableStatement cst = null;
		ArrayList<Object> datos = new ArrayList<Object>();
		
		try {
			String query = "{ call PRO_GET_SET_SRVC(?,?,?,?,?,?,?) }";
			cst = conn.prepareCall(query);
			cst.setString(1, clntRet);
			cst.setString(2, module);
			cst.setString(3, serviceId);
			cst.setString(4, companySrvcCalculado);
			cst.registerOutParameter(5, Types.INTEGER);//retencion
			cst.registerOutParameter(6, Types.VARCHAR);//servicio factura
			cst.registerOutParameter(7, Types.VARCHAR);//compa?ia retencion
			cst.executeQuery();

			int amount = cst.getInt(5);
			datos.add(amount);
			if (amount > -1) {
				datos.add(cst.getString(6));
				datos.add(cst.getString(7));
			}
			
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("getTaxRenEspecial()1_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.cerrarCallableStatement(cst);			
		}
		return datos;
	}
	
	/********************************************************************************************************
	 * m?todo para actualizar total de gu?a		*
	 ********************************************************************************************************/
	public void updateFinalAmnt(Connection con, String guiaNo) {
		ResultSet rs = null;
		PreparedStatement pst = null;
		String query = "";
		String totalSrvc = "";
		try {
			if (!guiaNo.equals("")) {
				query = cnct.delete(0, cnct.length())
						.append("Select SUM(((GS_SUB_TOTL - GS_DISC) +  GS_TAX - GS_TAX_RET))")
						.append(" FROM BOK_GUIA_SRVC")
						.append(" WHERE GS_GUIA_NO = ?").toString();
				pst = con.prepareStatement(query);
				pst.setString(1, guiaNo);
				rs = pst.executeQuery();
				
				while(rs.next()) {
					totalSrvc = rs.getString(1);
				}
				pst.close();
				rs.close();
				if (!totalSrvc.equals("")) {
					query = cnct.delete(0, cnct.length())
							.append("UPDATE BOK_GUIA_HEAD SET GH_GUIA_AMNT = ? WHERE GH_GUIA_NO = ?").toString();
					pst = con.prepareStatement(query);
					pst.setString(1, totalSrvc);
					pst.setString(2, guiaNo);
					pst.executeQuery();
					pst.close();
				}
				if (!totalSrvc.equals("")) {
					query = cnct.delete(0, cnct.length())
							.append("UPDATE BOK_INVC_HEAD SET IH_GUIA_AMNT = ? WHERE IH_GUIA_NO = ?").toString();
					pst = con.prepareStatement(query);
					pst.setString(1, totalSrvc);
					pst.setString(2, guiaNo);
					pst.executeQuery(); 
					pst.close();
				}
			}
		} catch (Exception e){
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("updateFinalAmnt()_Error:").append(e).toString());
			e.printStackTrace();
			resources.closeResources(rs,pst);
		} finally {
			resources.closeResources(rs, pst);
		}
	}
	
	public void insertBokHistErrorPediNumb(Connection con, String pediNumb, String guiaNo) {
		PreparedStatement pst = null;
		try {
			String insertQuery = "INSERT INTO BOK_HIST_ERROR_PEDI_NUMB(GUIA_NO,PEDI_NUMB) VALUES(?,?)";

			pst = con.prepareStatement(insertQuery);
			pst.setString(1, guiaNo);
			pst.setString(2, pediNumb);
			
			pst.executeUpdate();		
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("insertBokHistErrorPediNumb()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.cerrarPreparedStatement(pst);
		}
		
	}
	
	// Este metodo obtiene los valores mínimos para largo, ancho, alto, peso tanto
	// para sobre como para caja
	private void getMinMeasures(Connection con, PrepaidValueObject pObject) {
		CallableStatement cst = null;
		try {
			String str = "{ call PRO_GET_MIN(?,?,?,?,?,?)}";

			cst = con.prepareCall(str);
			cst.setString(1, "PAQ");
			cst.registerOutParameter(2, Types.NUMERIC);
			cst.registerOutParameter(3, Types.NUMERIC);
			cst.registerOutParameter(4, Types.NUMERIC);
			cst.registerOutParameter(5, Types.NUMERIC);
			cst.registerOutParameter(6, Types.NUMERIC);

			cst.execute();
			pObject.setVolLMin(cst.getDouble(2));
			pObject.setVolWMin(cst.getDouble(3));
			pObject.setVolHMin(cst.getDouble(4));
			pObject.setWghtMin(cst.getDouble(5));
			pObject.setVolMin(cst.getDouble(6));
			resources.cerrarCallableStatement(cst);
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("getMinMeasures()_Error1:").append(e)
					.toString());
			e.printStackTrace();
		} finally {
			resources.cerrarCallableStatement(cst);
		}
	}
}
