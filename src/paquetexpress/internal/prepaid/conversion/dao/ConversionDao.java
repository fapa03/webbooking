
/**
 * 
 * Kits-Id			Date				Purpose
 * 
 * 69949	  		26-Mar-2010: 		To include Package type like sobre, paquete, caja.
 * 
 */


package paquetexpress.internal.prepaid.conversion.dao;

import java.io.BufferedReader;
import java.io.IOException;
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

import com.paquetexpress.www.webbooking.Documentacion.ShipTypeSEG;

import bean.CentrosCosto;
import bean.Global;
import bean.JavAddressLovRecords;
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
import paquetexpress.internal.prepaid.action.valueobject.PrepaidValueObject;
import paquetexpress.internal.prepaid.conversion.form.ConversionForm;
import paquetexpress.internal.prepaidcajas.conversion.action.guiamstr.PrepaidGuiaMstr;


public class ConversionDao {
	private StringBuffer cnct = new StringBuffer();
	private final String msgErr = new StringBuffer("MSERR_B_").append(this.getClass().getName()).append(".").toString();	
	private Resources resources = new Resources();
	Connection con=null;
	
	PreparedStatement pstmt=null;
	Statement st=null;
	String tarifType="";
	CallableStatement cst=null;
	ResultSet rs=null;ResultSet rs1=null;
	HttpSession session=null;
	String convertguia="{ call pack_ppg_web_2012.pro_convert_ppg_nrml_ww_2017(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) }";
	//String convertguia="{ call pack_ppg_web_2012.pro_convert_ppg_nrml(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) }";
	
	String zoneverification="select pack_ppg_web_2012.fun_zone_verfication(?,?,?,?) from dual";
	String checkGuiaExist="select BGM_CLINT_ID,BGM_ORGN_BRNC_ID,BGD_REF_NO,BGD_GH_AMT,BGM_ZONE,BGM_WGHT,BGM_VLUM,D.BGM_DEST_SITE_ID,D.BGM_DEST_CLNT_ID,PACK_WEB.fun_ftch_clnt_name(D.BGM_DEST_CLNT_ID),Pack_Web.fun_ftch_site_name(D.BGM_DEST_SITE_ID),PACK_WEB.fun_ftch_clnt_name(BGM_CLINT_ID),D.BGD_GH_CONTENT, M.BGM_EXT_SERVICE, M.BGM_REF_NO_OLD, BGM_ACK_SERVICE, BGM_ACK_AMT, BGM_SHIP_TYPE, BGM_LOC_TYPE " +
			"from PPG_BOK_GUIA_DTL D,PPG_BOK_GUIA_MSTR M WHERE D.BGD_TRAC_NO=? AND M.BGM_REF_NO=D.BGD_REF_NO AND M.BGM_VALID_FLG='A' AND D.BGD_CONV_DATE IS NULL and D.BGD_ACTV_FLAG='A'";
	String fetchShip="select PM_VLUE1_DESC FROM sys_parm_mstr WHERE PM_MDUL_ID ='PPG' AND PM_PARM_TYPE ='TRANS_MODE' and PM_PARM_CODE1 in (select SD_SHIP_TYPE from PPG_SHIP_DET WHERE SD_SHIP_GUIA_REF_NO=?)  AND PM_PARM_CODE1 NOT IN (SELECT PM_PARM_CODE1 FROM sys_parm_mstr WHERE PM_MDUL_ID ='PPG' AND PM_PARM_TYPE ='SRVC_SEG_PPG')";
	// Added on 14/07/2010 - For kitsId : 70454 Starts
	String fetchDelvryType = "SELECT PM_VLUE1_DESC,PM_PARM_CODE1 FROM sys_parm_mstr where pm_parm_type = 'DELIVERY'"; 
//	String destClientCity = "SELECT ge_code FROM sys_gety_mstr	 WHERE (ge_code, ge_levl, ge_type) IN ( SELECT gr_code_r, gr_levl_r, gr_type_r "+
//							" FROM sys_gety_resp WHERE (gr_code, gr_levl, gr_type) IN (SELECT gr_code_r, gr_levl_r, gr_type_r "+
//							" FROM sys_gety_resp WHERE (gr_code, gr_type, gr_levl) IN ( SELECT am_gety_code, am_gety_type, am_gety_levl "+
//							" FROM sys_addr_mstr  WHERE am_enty_id = ? AND AM_ADDR_CODE = ?)))";
	String destClientColonia = "SELECT am_gety_code FROM sys_addr_mstr WHERE am_enty_id = ? AND AM_ADDR_CODE = ?";
	// Added on 14/07/2010 - For kitsId : 70454 Ends	
	String chkEadAvl	=	"SELECT CM_EAD_FLAG FROM SYS_CLNT_MSTR WHERE " +
			"CM_CLNT_ID=?"; // Added on 28/07/2010 - For kitsId : 70454 Starts
	String fetchAddress="select BGA_ADDR_TYPE,BGA_ADDR_CODE," +
			"BGA_ADDR_LIN4,BGA_ADDR_LIN6,BGA_STRT_NAME,BGA_DRNR,BGA_PHNO_1,BGA_ZIP_CODE FROM PPG_BOK_GUIA_ADDR  WHERE BGA_REF_NO=?";
	String getname="select PACK_WEB.fun_ftch_clnt_name(?) from dual ";
	//String getzones="select PZM_ZONE_NAME from PPG_ZONE_MSTR WHERE PZM_ZONE_ACTIVE='Y' order by PZM_ZONE_NAME";
	String getShipTypes="select PM_PARM_CODE1 FROM sys_parm_mstr WHERE PM_MDUL_ID ='PPG' AND PM_PARM_TYPE ='TRANS_MODE'";
	//String getzoneIds="select PZM_ZONE_ID,PZM_ZONE_NAME from PPG_ZONE_MSTR WHERE PZM_ZONE_ACTIVE='Y' order by PZM_ZONE_NAME";
	String getrfc="select PACK_WEB.fun_ftch_rfc(?) from dual";
	String insertmaster="insert into PPG_BOK_GUIA_MSTR(BGM_REF_NO,BGM_BRANCH_ID,BGM_REF_DT,BGM_ORGN_BRNC_ID,BGM_CLINT_ID"
		+",BGM_NO_OF_GUIS,BGM_ZONE,BGM_WGHT,BGM_VLUM,BGM_GH_AMT,BGM_VALID_FLG) VALUES(?,?,?,?,?,?,?,?,?,?,?) ";
	String seqmaster = "{ call GEN_SEQN_NUM_WEB(?) }";
	String insertrecord="{call PRO_PPG_INS_GUIA(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	String print="{ call PACK_PPG_GUIA_PRINT.PRO_PPG_GUIA_PRINT(?,?)}";
	String prepareInvoice="INSERT INTO PPG_INV_PMT_HDR(IPH_INV_NO,IPH_BRANCH_ID,IPH_SITE_ID,IPH_INV_DATE,IPH_CLINT_ID,"+
							"IPH_INV_ACT_FLG,IPH_INV_GROSS_AMT,IPH_INV_TAX_PRTAGE,IPH_INV_TAX_AMT,IPH_INV_RET_TAX_PRTAGE,"+
							"IPH_INV_RET_TAX_AMT,IPH_INV_NET_AMT,IPH_INV_PAID_DT,IPH_INV_PMT_MODE,IPH_INV_PMT_TYPE,IPH_INV_CANCEL_DT,"+
							"CRTD_ON,CRTD_BY,MDFD_ON,MDFD_BY) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	String invoiceDetail="INSERT INTO PPG_INV_PMT_DTL(IPD_INV_NO,IPD_REF_NO,IPD_INV_AMT,CRTD_ON,CRTD_BY,MDFD_ON,MDFD_BY)"+
			"VALUES(?,?,?,?,?,?,?)";

	String removeguia="update PPG_BOK_GUIA_MSTR set BGM_VALID_FLG='N',MDFD_ON=(SELECT SYSDATE FROM DUAL),MDFD_BY=? where  BGM_REF_NO =?";
	String removeguias="{call pack_ppg_web_2012.PRO_PPG_REM_GUIA(?,?) }";
	String invoiceSeq="SELECT pack_ppg_web_2012.FUN_INV_SQNUM,sysdate  FROM DUAL";
	String invoiceprint="{ call PRO_PPG_INV_PRINT(?,?,?) }";
	String selectTax="select SM_TAX_PERC,SM_RETN_TAX_PERC from SYS_SITE_MSTR where SM_SITE_ID=?";
	String misSave="{ call pack_ppg_web_2012.PRO_PPG_INSERTINTO_ACT_FTT(?,?,?,?,?,?) }";
	String removeInvoiceLessCredit="{ call pack_ppg_web_2012.PRO_REMOVE_INVOICE_NOCRLT(?,?) }";
	// 
	// Agregado 04-01-2012 CenitSoft
	String getGuiaMstr="SELECT BGM_REF_NO,BGM_PREP_BRNC_ID,BGM_REF_DT,BGM_ORGN_BRNC_ID,BGM_CLINT_ID,BGM_NO_OF_GUIS,BGM_ZONE,BGM_WGHT,BGM_VLUM,BGM_GH_AMT,BGM_VALID_FLG,BGM_ORG_SITE_ID,BGM_SHIP_TYPE,M.CRTD_ON,M.CRTD_BY,M.MDFD_ON,M.MDFD_BY,BGM_SWINGCD_NO,BGM_ACK_SERVICE,BGM_ACK_AMT,BGM_ACK_TAX,BGM_INSUR_SERVICE,BGM_INSUR_AMT,BGM_INSUR_TAX,BGM_DSC_PERCENT,BGM_DSC_AMT,BGM_EAD_SERVICE,BGM_EAD_AMT,BGM_RAD_SERVICE,BGM_RAD_AMT, BGM_EXT_SERVICE, BGM_EXT_AMT, BGM_LOC_TYPE,  NVL(M.Bgm_Cmpy_Id, FUN_GET_CPNY_BY_WEIGHT_CLIENT(BGM_WGHT, BGM_CLINT_ID, 'SHP-G')) CPNY_ID"+
	"  FROM PPG_BOK_GUIA_MSTR M, PPG_BOK_GUIA_DTL D   " +
	"  WHERE M.BGM_REF_NO=D.BGD_REF_NO AND D.BGD_TRAC_NO=?";
	String reinsertrecord="{call pack_ppg_web_2012.pro_ppg_modify_guia_cajas(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	//String fetchFormNo="begin ? := pack_ppg_web_2012.FUN_PPG_GEN_FORM_NO('CFD_NO',?,'9'); end;";
	String fetchFormNo="Select FUN_GET_CFD_NO_BRNC(?,9) FROM DUAL";
	String misSaveCredit="{ call pack_ppg_web_2012.proppgReinsertActFttBox_Fact33(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,'+1',?,?,?,?,?) }";
	//String misSavecash="{ call pack_ppg_web_2012.pro_ppg_reinsert_act_ftt_box(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,'-1',?,?,?) }";
	String updateFormNo="update sys_parm_mstr set pm_vlue1_id=?,MDFD_ON=sysdate,MDFD_BY=? where pm_parm_type='CFD_NO' and pm_parm_code1=? and pm_vlue2_id='9'";
	String fetchFormCtr="select PM_PARM_CODE2 FROM  sys_parm_mstr WHERE pm_parm_type = 'CFD_NO' AND pm_parm_code1 = ? AND pm_vlue2_id = '9'";
	String getClientType="select cm_clnt_type from sys_clnt_mstr where cm_clnt_id=?";
	// Obtener los valores si el cliente es de Zona Extendida
	String verificazonaextendida="select A.BC_BRNC_ID, A.BC_OL_ID " +
		"FROM er_brnc_coly_mstr A, SYS_CLNT_VIEW B " + 
		"WHERE B.cm_clnt_id = ? " + 
		//"AND ROWNUM = 1 " +
		"AND SUBSTR(A.BC_BRNC_ID,4,2) = ? "+//AAP02
		"AND B.AM_ADDR_CODE = ? " +//AAP02
		"AND A.BC_GETY_LEVL_6 = B.AM_GETY_LEVL " +
		"AND A.BC_GETY_CODE_6 = B.AM_GETY_CODE " +
		"AND A.BC_GETY_TYPE_6 = B.AM_GETY_TYPE ";
	// Fin verificar zona extendida

	String fetchShipSEG="select PM_VLUE1_DESC, PM_PARM_CODE1,PM_PARM_CODE2 FROM sys_parm_mstr WHERE PM_MDUL_ID ='BOK' AND PM_PARM_TYPE ='SRVC_LOG' and PM_PARM_CODE2 in (select SD_SHIP_TYPE from PPG_SHIP_DET WHERE SD_SHIP_GUIA_REF_NO=?)";
	String fetchShipSEGByParmCode2="select PM_VLUE1_DESC, PM_PARM_CODE1,PM_PARM_CODE2 FROM sys_parm_mstr WHERE PM_MDUL_ID ='BOK' AND PM_PARM_TYPE ='SRVC_LOG' and PM_PARM_CODE2 = ?";
	//2017-07-21 esta consulta regresa los servicios disponible para el cliente ya validado su tiempo de corte o cobertura
	//String fetchShipSEGALL="SELECT PM_VLUE1_DESC,PM_PARM_CODE1 ,PM_PARM_CODE2 ,sactivos from (SELECT PM_VLUE1_DESC,PM_PARM_CODE1, PM_PARM_CODE2, nvl(CASE WHEN PM_PARM_CODE1 = 'STD-T'  THEN 1 WHEN PM_PARM_CODE1 = 'SEG-2D'  AND FUN_IS_CORTE_SEG_CUSTOMER( 'SEG-DS' ,:GH_ORGN_BRNC_ID , :GH_DEST_BRNC_ID, :CLIENTEID, :ADDRCODE)  = 0 THEN 1 WHEN PM_PARM_CODE1 NOT IN  ( 'SEG-2D' , 'STD-T' ) THEN FUN_IS_CORTE_SEG_CUSTOMER(PM_PARM_CODE1 , :GH_ORGN_BRNC_ID, :GH_DEST_BRNC_ID, :CLIENTEID, :ADDRCODE) END,0) sactivos FROM SYS_PARM_MSTR WHERE PM_PARM_TYPE =  'SRVC_LOG' ) where sactivos = 1 ";
	String fetchShipSEGALL="SELECT PM_VLUE1_DESC,PM_PARM_CODE1 ,PM_PARM_CODE2 ,sactivos from (SELECT PM_VLUE1_DESC,PM_PARM_CODE1, PM_PARM_CODE2, nvl(CASE WHEN PM_PARM_CODE1 = 'STD-T' THEN 1 WHEN PM_PARM_CODE1 NOT IN ('STD-T') THEN FUN_IS_CORTE_SEG_CUSTOMER(PM_PARM_CODE1, :GH_ORGN_BRNC_ID, :GH_DEST_BRNC_ID, :CLIENTEID, :ADDRCODE) END, 0) sactivos FROM SYS_PARM_MSTR WHERE PM_PARM_TYPE = 'SRVC_LOG' ) where sactivos = 1 ";
	String getCobertura="SELECT FUN_GET_COVER_SEGxSER(?,?) FROM DUAL";
	//2017-01-07 Para obtener la hora de corte de la sucursal para el SEG. 
	String getInsideTimeCourt="select FUN_IS_CORTE_SEG_CUSTOMER(?,?,?,?,?) from dual";
	//2017-07-21 Se agrego esta consulta para saber cuales servicios seran tratados como servicio express garantizado.
	String getExclusiveTypeShipSEG ="SELECT PM_PARM_CODE1 FROM sys_parm_mstr WHERE PM_MDUL_ID ='PPG' AND PM_PARM_TYPE ='SRVC_SEG_PPG'";

	public boolean zoneverify(String tracno, String orginbrnc, String destbrnc,
			String destsite) {
		try {

			//String orgin = orginbrnc.substring(0, 3);
			//AccessLog.Log(orgin);
			//String dest = destbrnc.substring(0, 3);
			//AccessLog.Log(dest);

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
			//AccessLog.Log("after 1stwhl in ConD");
		} catch (Exception e) {
			AccessLog.Log(e.getMessage());
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (rs != null)
					rs.close();
				if (con != null)
					con.close();

			} catch (Exception e) {
				AccessLog.Log(e.getMessage());
			}
		}
		return false;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PrepaidValueObject getDetail(String trackingno) {
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
				pObject.setExt(rs.getString(14));
				pObject.setGuiaOld(rs.getString(15));
				pObject.setAcklabels(rs.getString(16) == null ? "" : rs.getString(16));// AAP02
				pObject.setAckAmt(rs.getString(17) == null ? "0" : rs.getString(17));// AAP02
				pObject.setShipType(rs.getString(18) == null ? "":rs.getString(18));//JSA01
				pObject.setLocationType(rs.getString(19) == null ? "":rs.getString(19));//AAP20
			}
			//AccessLog.Log("after 2ndwhl in ConD");
			if (pObject.getClientId().length() > 0) {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();

				 ArrayList list=new ArrayList();
				 boolean validateExclusive1D=false;
				 if(!pObject.getShipType().equalsIgnoreCase("1D")) {
				     pstmt=con.prepareStatement(fetchShip);
				     pstmt.setString(1,pObject.getRefNo());
				     rs=pstmt.executeQuery();
				     
				     //String local="";
				     //int i=0;
				     while(rs.next()){		
				    	 list.add(rs.getString(1));
					 //i++;						
					 }
				     //AccessLog.Log("after 3rdwhl in ConD");
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
				 	//Cuando no encontro nada de los tipos de envio (antes de los cambios del SEG) se procede a buscar en los nuevos tipos de envios
					if (list.isEmpty()) {
					    if(validateExclusive1D){
							pstmt = con.prepareStatement(fetchShipSEGByParmCode2);
							pstmt.setString(1, "DS");
					    } else {
							pstmt = con.prepareStatement(fetchShipSEG);
							pstmt.setString(1, pObject.getRefNo());
					    }
						rs = pstmt.executeQuery();
						List<ShipTypeSEG>  listSEG = new ArrayList<ShipTypeSEG>();
						ShipTypeSEG shipTypeSEG=null;
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
				//AccessLog.Log("after 4thwhl in ConD");
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				pstmt = con.prepareStatement(fetchAddress);

				pstmt.setString(1, trackingno);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					if (rs.getString(1).equalsIgnoreCase("DESTINATION")) {
						//AccessLog.Log("fetch destination");
						pObject.setDestcode(rs.getString(2));
						//AccessLog.Log("After fetch destination pObject.setDestcode"+ pObject.getDestcode());
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
				}
				
				cst=con.prepareCall("{ call PRO_GET_MAX_SEG(?,?,?,?,?)}");
				cst.setString(1,"ENV");
				cst.registerOutParameter(2,Types.NUMERIC);
				cst.registerOutParameter(3,Types.NUMERIC);
				cst.registerOutParameter(4,Types.NUMERIC);
				cst.registerOutParameter(5,Types.NUMERIC);
				cst.execute();			
				pObject.setVolLMAX(cst.getDouble(2));
				pObject.setVolWMAX(cst.getDouble(3));
				pObject.setVolHMAX(cst.getDouble(4));
				pObject.setPesoMAX(cst.getDouble(5));	

				//Obtiene medidas mínimas
				getMinMeasures(con, pObject);
				resources.cerrarCallableStatement(cst);
				
			} else
				return null;
		} catch (Exception e) {
			AccessLog.Log(e.getMessage());
			e.printStackTrace();
			return null;
		} finally {
			resources.closeResources(rs, pstmt, cst);
			resources.cerrarConexion(con);
		}
		return pObject;
	}

	
	public String convert(PrepaidValueObject object,String TracNo,HttpSession sess,HttpServletRequest request, String serieCaja, ConversionForm conversionform) {//AAP02 
		String eadServie = null;		
		String modulo = "BOK";
		String result = "";
		SucursalesConfiguradas suc = new SucursalesConfiguradas();
		String entrega = "";
		try {
			
			String formNumber = getFormNumber((String) sess.getAttribute("branchid"), modulo, serieCaja);
			//AccessLog.Log("!!!!!!!!!!!!!!!!!!!!!!#######***********************formNumber "+ formNumber+"----- RASTREO "+TracNo);
			if(formNumber !=null && formNumber.length()>0) {
				conversionform.setGuiaNo(formNumber);
			}
			con = ConnectDB.getConnection();
			
			cst=con.prepareCall("{ call pack_ppg_web_2012.PRO_ASSIGN_ORIGIN_BRNC(?,?) }");
			cst.setString(1,object.getClientId());
			cst.registerOutParameter(2,Types.VARCHAR);
			cst.execute();
			
			cst.execute();
			object.setBranchId(cst.getString(2));
			if(cst!=null)
				cst.close();
			cst=con.prepareCall("{ call pack_ppg_web_2012.PRO_ASSIGN_DEST_BRNC(?,?,?,?) }");
			//AccessLog.Log("object.getDestcode()***********************"+object.getDestclave());
			//AccessLog.Log(object.getDestcode());
			//AccessLog.Log(object.getDestclave());
			cst.setString(1,object.getDestclave());
			cst.setString(2,object.getDestcode());
			cst.registerOutParameter(3,Types.VARCHAR);
			cst.registerOutParameter(4,Types.VARCHAR);
			cst.execute();
			String nuevaSucursal = cst.getString(3);
			/* Valida si el env�o es ocurre */
			if (conversionform.getDeliveryType().equalsIgnoreCase("O")) {
				/*se busca configuracion*/	
				entrega = "DEST_OCURRE";
				String nuevaSucursalDefault = suc.obtieneConfigSucursal(con, "BOK", entrega, object.getDestSite());//AAP02
				
				/*se busca configuracion*/
				String nuevaSucursalOcu = suc.obtieneConfigSucursalOcurre(con, conversionform.getDestclave(), conversionform.getDestcode());
				String nuevaSucursalEspec = suc.obtieneConfigSucursalEspec(con, modulo, "DEST_OCURRE_ESP", object.getDestSite());
				
				/* El cliente eligi� sucursal ocurre */
				String[] brnchClave = conversionform.getBrnchOcurre().split("\\|");
				if (Boolean.TRUE.equals(conversionform.getOpcOcurre()) && !brnchClave[0].equals("")) {
					nuevaSucursal = brnchClave[0];
				}else if (!nuevaSucursalOcu.isEmpty()){//AAP07
					/*si coinciden los sites de la sucursa de excepcion y la sucursal destino original, 
					 * se asigna la nueva sucursal excepcion de ocurre*///AAP07
					/* Valida si la sucursal por excepci�n cumple con los topes */
					for (BranchDetailDTOResponse s : conversionform.getFilteredBrnch()) {
						if (s.getClave().equalsIgnoreCase(nuevaSucursalOcu)) {
							nuevaSucursal = nuevaSucursalOcu;
						}
					}
					if (!nuevaSucursal.equalsIgnoreCase(nuevaSucursalOcu)) {
						if (nuevaSucursalEspec.equalsIgnoreCase(nuevaSucursalOcu)) {
							/* Asigna sucursal especial */
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
			if(cst!=null)
				cst.close();
			if (validaGH_FORM_NO(con, conversionform, "P")) {
				result = "EL NUMERO DE FORMA YA EXISTE";
			} else {	
				String desc="";
				cst = con.prepareCall(convertguia);
				cst.setString(1,TracNo);
				cst.setString(2,(String)sess.getAttribute("branchid"));
				cst.setString(3,object.getOrgioncode());
				cst.setDouble(4, Double.parseDouble(object.getValordeclarado()));
				cst.setString(5,object.getCobertura());
				cst.setDouble(6, 0);//AAP20 No se usa en la logica
				cst.setDouble(7, Double.parseDouble(object.getInsTaxRet()));
				cst.setDouble(8, Double.parseDouble(object.getInsuranceSubTotal()));
				cst.setString(9,desc);
				//AccessLog.Log("object.getDestbranch**************************"+object.getDestbranch());
				cst.setString(10,object.getDestbranch());
				cst.setString(11,object.getDestSite());
				cst.setString(12,object.getDestclave());
				cst.setString(13,object.getDestcode());
				cst.registerOutParameter(14,Types.VARCHAR);
				cst.setString(15,((Global)sess.getAttribute("sGlobal")).getOrigenUserClave());
				cst.registerOutParameter(16,Types.VARCHAR);
				cst.setString(17,object.getContent());
				cst.setString(18,object.getInsTax());
				cst.setString(19,object.getClientId());
				cst.setString(20,object.getPackType());					
				if ((object.getDeliveryType().equalsIgnoreCase("H")) || (object.getDeliveryType().equalsIgnoreCase("X"))) {
					eadServie = "1";
				} else {
					eadServie = "0";
				}
				cst.setString(21,eadServie);
				// Added on 14/07/2010 - For kitsId : 70454 Ends
				String colonyerror="";
				colonyerror=(String)sess.getAttribute("colonyerror");
				//AccessLog.Log("before coloneyerror");
							
				String comt = "";
				if ((colonyerror != null) && (colonyerror.length() > 0)) {//AAP02
					//cst.setString(22,"Entregar Ocurre, no existe Zona de EAD para esa colonia"); 
					comt = "Entregar Ocurre, no existe Zona de EAD para esa colonia";
				}//AAP02
				
				if (conversionform.getCheckRefDir().equals("V")) {//AAP02
					if (comt.length()>0) {
						comt = cnct.delete(0, cnct.length()).append(comt).append(". ").toString();
					}
					comt = cnct.delete(0, cnct.length()).append(comt).append(conversionform.getDestRefDom()).append(". Telefono: ").append(conversionform.getDesttelefono()).toString();
				}//AAP02
				
				if (conversionform.getComments().length()>0) {
					if (comt.length()>0) {
						comt = cnct.delete(0, cnct.length()).append(comt).append(". ").toString();
					}
					comt = cnct.delete(0, cnct.length()).append(comt).append(conversionform.getComments()).toString();
				}
				
				if (comt.length()>255) {
					comt = comt.substring(0,255);
				}
				cst.setString(22, comt);//AAP02
				cst.setString(23, serieCaja);
				cst.setString(24, conversionform.getGuiaNo());
				//AccessLog.Log("before coloneyerror nextttttttttttttt");
				cst.setString(25, conversionform.isConfirmationService2D()?conversionform.getShipTypeSEGPPChange():null);//JAS01
			    cst.setDouble(26, conversionform.getVolL() == 0 ? conversionform.getVolLMAX() : conversionform.getVolL());//JAS01
			    cst.setDouble(27, conversionform.getVolH() == 0 ? conversionform.getVolHMAX() : conversionform.getVolH());//JAS01
			    cst.setDouble(28, conversionform.getVolW() == 0 ? conversionform.getVolWMAX() : conversionform.getVolW());//JAS01
			    cst.setString(29, conversionform.getProductIdSat());
			    cst.setString(30, conversionform.getPedinumber().trim().toUpperCase());
			    cst.setString(31, conversionform.getCustagent().trim().toUpperCase());
				cst.executeQuery();
				
				result = cst.getString(14);
				
				if(result!=null) {
					return result; // + "Resultado de operacion de package";
				}	
			}
								
			// return regresa;
			//new PrintFileExport(request.getRealPath("/")).writeToGuiaFile(request,cst.getString(15),getTimeStamp(con).getTime(),object.getClientId());
			//return "downloadpage";		
		} catch(Exception e) {
			AccessLog.Log(e.getMessage());
			return (e.getMessage() + " Error de parametros o procedimiento");			
		} finally {
			try {
				if(cst!=null)
					cst.close();
				if(rs!=null)
					rs.close();
				if(con!=null)
					con.close();
				
			} catch(Exception e) {		
				AccessLog.Log(e.getMessage());
			}
		}
		return "";
	}
	
	private String getRFC(Connection con, String clientID) {
		String rfc = "";
		try {
			
			pstmt = con.prepareStatement(getrfc);
			pstmt.setString(1, clientID);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				rfc = rs.getString(1);
				if (rfc == null) {
					rfc = "";
				}
			}
			//AccessLog.Log("after 6thwhl in ConD");
		} catch (Exception e) {

			AccessLog.Log(e.getMessage());
			return "";
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (rs != null)
					rs.close();

			} catch (Exception e) {
				AccessLog.Log(e.getMessage());
			}
		}
		return rfc;
	}	

	private String getGroupClientId(String clientId, Connection con)
			throws Exception {

		String groupIdQuery = "select pack_web.FUN_CHCK_GRUP_CLNT(?)as groupid from dual";// Checked
		PreparedStatement pst = con.prepareStatement(groupIdQuery);
		pst.setString(1, clientId);
		ResultSet rs = pst.executeQuery();
		String groupClientId = "";
		while (rs.next()) {
			groupClientId = rs.getString("groupid");
		}
		//AccessLog.Log("after 12thwhl in ConD");
		if (rs != null)
			rs.close();
		if (pst != null)
			pst.close();
		//AccessLog.Log("GROUPCLIENTID" + groupClientId);

		return groupClientId.toUpperCase();
	}
	

	
	public void calculateInsuranceAmount(Connection con,
			PrepaidValueObject vObject) {
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

			/*
			 * if(isTariffSlabExists(con,vObject.getClientId(),vObject.getBranchId
			 * (),vObject.getDestbranch())){ subTotalInv=
			 * getAditionalServicesTotal
			 * (con,vObject.getCobertura(),"INV",vObject
			 * .getClientId(),vObject.getBranchId
			 * (),vObject.getDestbranch(),tarifType); discountInv[0] = null;
			 * discountInv[1] = "0"; subtotal=
			 * df.format(Double.parseDouble(subTotalInv));
			 * insuranceView=getInsuranceViewFlag(vObject.getClientId());
			 * taxableAmountInv= Double.parseDouble(subtotal); }else
			 */// {
			amount_totalInv = getAditionalServicesTotal(con, "", vObject
					.getValordeclarado().trim(), vObject.getCobertura().trim(),
					"INV", vObject.getClientId(), vObject.getBranchId(),
					vObject.getDestbranch());
			subtotal = amount_totalInv[0];
			if (Double.parseDouble(subtotal) > 0)
				discountInv = calculateQuantity(con,
						getGroupClientId(vObject.getClientId(), con), "INV",
						amount_totalInv[0], vObject.getValordeclarado().trim(),
						"0");
			else {
				discountInv[0] = null;
				discountInv[1] = "0.00";
			}
			// discountInv =
			// calculateQuantity(con,groupClientId,aform.getCobertura(),amount_totalInv[0],aform.getValordeclarado().trim(),"0");
			// commented and added prev line by amal

			//insuranceView = getInsuranceViewFlag(vObject.getClientId(), con);

			taxableAmountInv = Double.parseDouble(subtotal)
					- Double.parseDouble(discountInv[1]);
			// }

			String taxForInv[] = getTax(con, "INV",
					df.format(taxableAmountInv), vObject.getBranchId(),
					vObject.getDestbranch(), vObject.getClientId());
			vObject.setInsuranceSubTotal(Double.toString(taxableAmountInv));
			//AccessLog.Log("----------------------------taxableAmountInv" + taxableAmountInv);
			//AccessLog.Log("----------------------------taxForInv[0]" + taxForInv[0]);
			vObject.setInsTax(taxForInv[0]);
			vObject.setInsTaxRet(taxForInv[1]);
			vObject.setDiscount(discountInv[1]);

		} catch (Exception e) {
			AccessLog.Log("hai" + e.getMessage());
		}
	}
	
	private String[] getAditionalServicesTotal(Connection con, String lcZone,
			String totalAmount, String serviceId, String referenceServiceId,
			String clientId, String branch, String destbranch) throws Exception {

		String query = "Begin ? := pack_web.FUN_FTCH_ADD_SRVC(?,?,?,?,?,?,?,?); End;";// Checked
		CallableStatement cst = con.prepareCall(query);
		cst.registerOutParameter(1, Types.NUMERIC);
		cst.setString(2, serviceId);
		//AccessLog.Log("serviceId in getAditionalServicesTotal" + serviceId);
		cst.setString(3, referenceServiceId);
		//AccessLog.Log(referenceServiceId);
		cst.setString(4, branch);
		//AccessLog.Log(branch);
		cst.setString(5, destbranch);
		//AccessLog.Log(destbranch);
		cst.setString(6, lcZone);
		//AccessLog.Log(totalAmount);
		if (totalAmount != null && totalAmount.length() == 0)
			// cst.setDouble(7,0.0d);//Empty
			cst.setNull(7, Types.NUMERIC);
		else
			cst.setDouble(7, Double.parseDouble(totalAmount));
		cst.setString(8, "NON");

		cst.registerOutParameter(9, Types.NUMERIC);

		cst.executeQuery();
		java.text.DecimalFormat df = new java.text.DecimalFormat("0.00");
		String[] amount_total = new String[2];
		amount_total[0] = df.format(cst.getDouble(1));
		amount_total[1] = df.format(cst.getDouble(9));
		//AccessLog.Log("amount_total[0]" + amount_total[0]);
		//AccessLog.Log("amount_total[1]" + amount_total[1]);

		if (cst != null)
			cst.close();
		return amount_total;
	}
	
	private String[] calculateQuantity(Connection con, String grpclientId,
			String serviceId, String qty, String amount, String minAmount)
			throws Exception {

//		AccessLog.Log("----------------INSIDE CALCULATE QUANTITY ------------------");
//		AccessLog.Log("----------------inputs are grpclientId-" + grpclientId
//				+ "--serviceId" + serviceId + "--qty" + qty + "amount" + amount
//				+ "minAmount" + minAmount);

		java.text.DecimalFormat df = new java.text.DecimalFormat("0.00");
		String query = "Begin ? := pack_web.fun_calc_disc(?,?,?,?,?,?); End; ";
		//AccessLog.Log("BEFORE PREPARING CALCULATE QUANTITY'S CALLABLE STATEMENT ");
		CallableStatement cst = con.prepareCall(query);
		//AccessLog.Log("AFTER PREPARING CALCULATE QUANTITY'S CALLABLE STATEMENT ");
		cst.registerOutParameter(1, Types.NUMERIC);
		cst.setString(2, serviceId);
		cst.setString(3, grpclientId);

		if (amount != null && amount.length() == 0)
			// cst.setDouble(4,0.0d);//Empty
			cst.setNull(4, Types.NUMERIC);
		else
			cst.setDouble(4, Double.parseDouble(amount));
		if (qty != null && qty.length() == 0)
			// cst.setDouble(5,0.0d);//Empty
			cst.setDouble(5, Types.NUMERIC);
		else
			cst.setDouble(5, Double.parseDouble(qty));
		if (minAmount != null && minAmount.length() == 0)
			// cst.setDouble(6,0.0d);//Empty
			cst.setNull(6, Types.NUMERIC);
		else
			cst.setDouble(6, Double.parseDouble(minAmount));
		cst.registerOutParameter(7, Types.VARCHAR);

		cst.executeQuery();
		String a[] = new String[2];
		a[0] = cst.getString(7);
		a[1] = df.format(cst.getDouble(1));

		//AccessLog.Log(" DISCOUNT SLAB IN SERVICE IN CALCULATE QUANTIY " + a[0]);
		//AccessLog.Log(" DISCOUNT AMOUNT IN SERVICE IN CALCULATE QUANTIY "+ a[1]);

		if (cst != null)
			cst.close();
		return a;
	}

	private String[] getTax(Connection con,
		String referenceServiceId, String taxableAmount, String branch,
		String destbranch, String clientId) {

		String taxQuery = "{call pack_sipweb.PRO_CALC_TAX_AMT(?,?,?,?,?)}";// Checked
		CallableStatement cst = null;
		try {

			//AccessLog.Log("referenceServiceId" + referenceServiceId);
			//AccessLog.Log("taxableAmount" + taxableAmount);
			//AccessLog.Log("branch" + branch);
			//AccessLog.Log("destbranch" + destbranch);
			//AccessLog.Log("clientId" + clientId);

			boolean isOrgionBorderBranch = isBorderBranch(con, branch);
			boolean isDestinationBorderBranch = isBorderBranch(con, destbranch);

			cst = con.prepareCall(taxQuery);
			if (isOrgionBorderBranch && !isDestinationBorderBranch) {
				cst.setString(1, destbranch);

			} else {
				cst.setString(1, branch);

			}

			cst.setString(2, referenceServiceId);
			cst.setDouble(3, Double.parseDouble(taxableAmount));
			cst.registerOutParameter(4, Types.NUMERIC);
			cst.registerOutParameter(5, Types.NUMERIC);
			cst.executeQuery();

			java.text.DecimalFormat df = new java.text.DecimalFormat("0.00");
			String tax[] = new String[2];
			tax[0] = df.format(cst.getDouble(4));

			String clientType = "";
			clientType = getClientType(con, clientId);

			String rfc = getRFC(con, clientId);

			if (clientType != null && clientType.equalsIgnoreCase("C")
					|| clientType != null && clientType.equalsIgnoreCase("G")) {

				if (rfc == null || rfc.length() != 14)// added by Emerson on
														// 05/02/2004
					tax[1] = "0";
				else
					tax[1] = df.format(cst.getDouble(5));
			} else {

				tax[1] = "0";
			}
			if (cst != null)
				cst.close();
			return tax;
		} catch (Exception e) {
			AccessLog.Log(e);

		} finally {
			try {
				if (cst != null)
					cst.close();
			} catch (Exception e) {
				AccessLog.Log(e.getMessage());
			}
		}
		return null;
	}

	private boolean isBorderBranch(Connection con, String branchId)
			throws Exception {
		ResultSet rs = null;
		PreparedStatement pst = null;
		boolean border = true;
		try {
			// Global global = (Global)session.getAttribute("sGlobal");
			String query = "select pack_web.fun_brdr_brnc(?) from dual ";
			pst = con.prepareStatement(query);
			pst.setString(1, branchId);
			rs = pst.executeQuery();
			if (rs.next()) {
				if (rs.getString(1).equalsIgnoreCase("BR")) {
					border = true;
				} else {
					border = false;
				}
			} else {
				border = false;
			}
		} catch (Exception e) {
			AccessLog.Log(e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if (rs!=null) {
					rs.close();
				}
				if (pst!=null) {
					pst.close();
				}
			} catch (Exception e) {
				AccessLog.Log(e.getMessage());
				e.printStackTrace();
			}
		}
		
		return border;
	}

	private String getClientType(Connection con, String clientId)
			throws Exception {
		String query = "	SELECT cm_clnt_type" + "	FROM SYS_CLNT_MSTR"
				+ "	WHERE cm_clnt_id = ?";
		PreparedStatement pst = con.prepareStatement(query);
		pst.setString(1, clientId);

		ResultSet rs = pst.executeQuery();
		String clientType = null;
		if (rs.next()) {
			clientType = rs.getString(1);
		}

		if (rs != null)
			rs.close();
		if (pst != null)
			pst.close();
		return clientType;

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList fetchPackageType() // Added on 26/03/2010
	{
		ArrayList alPackType = new ArrayList();

		ArrayList alPackTypeLable = new ArrayList();
		ArrayList alPackTypeName = new ArrayList();

		ResultSet rs = null;
		try {
			String query = null;
			con = ConnectDB.getConnection();
			st = con.createStatement();
			query = "SELECT	SS_CODE, SS_DESC FROM SYS_SHP_DESC_MSTR WHERE SS_CODE IN ('1') ORDER BY SS_CODE";
			rs = st.executeQuery(query);

			while (rs.next()) {
				alPackTypeLable.add(rs.getString(1));
				alPackTypeName.add(rs.getString(2));
			}
			//AccessLog.Log("after 16thwhl in ConD");
			if (rs != null)
				rs.close();
			if (st != null)
				st.close();

			alPackType.add(alPackTypeLable);
			alPackType.add(alPackTypeName);
		} catch (Exception e) {
			AccessLog.Log(e.getMessage());
		} finally {

			try {
				if (st != null)
					st.close();
				if (rs != null)
					rs.close();
				if (con != null)
					con.close();

			} catch (Exception e) {
				AccessLog.Log(e.getMessage());
			}
		}
		return alPackType;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList fetchDeliveryType() // Added on 14/07/2010 - Starts For
											// kitsId : 70454
	{
		ArrayList delvryType = new ArrayList();
		ArrayList delvryDesc = new ArrayList();
		ArrayList delvryCode = new ArrayList();
		ResultSet rs = null;
		try {
			con = ConnectDB.getConnection();
			st = con.createStatement();
			rs = st.executeQuery(fetchDelvryType);
			while (rs.next()) {
				delvryDesc.add(rs.getString(1));
				delvryCode.add(rs.getString(2));
			}
			//AccessLog.Log("after 17thwhl in ConD");
			if (rs != null)
				rs.close();
			if (st != null)
				st.close();

			delvryType.add(delvryDesc);
			delvryType.add(delvryCode);
		} catch (Exception e) {
			AccessLog.Log(e.getMessage());
		} finally {

			try {
				if (st != null)
					st.close();
				if (rs != null)
					rs.close();
				if (con != null)
					con.close();

			} catch (Exception e) {
				AccessLog.Log(e.getMessage());
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
			//AccessLog.Log("after 18thwhl in ConD");
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();

//			pstmt = con.prepareStatement(destClientCity);
//			pstmt.setString(1, pObject.getDestclave());
//			pstmt.setString(2, pObject.getDestcode());
//
//			rs = pstmt.executeQuery();
//			while (rs.next()) {
//				clientCity = rs.getString(1);
//			}
//			//AccessLog.Log("after 19thwhl in ConD");
//			if (rs != null)
//				rs.close();
//			if (pstmt != null)
//				pstmt.close();
			st = con.createStatement();
//			String destEadSrvc = "SELECT bc_brnc_id FROM er_brnc_coly_mstr  "
//					+ "WHERE bc_gety_code_4 = '" + clientCity
//					+ "' AND bc_gety_code_6 = '" + clientColonia + "' AND "
//					+ "bc_brnc_id like '" + pObject.getDestSite()
//					+ "%' AND bc_ead_rad_apply = 'Y'";
			
			String destEadSrvc = "SELECT bc_brnc_id FROM er_brnc_coly_mstr  "
					+ "WHERE bc_gety_code_6 = '" + clientColonia + "' AND "
					+ "bc_brnc_id like '" + pObject.getDestSite()
					+ "%' AND bc_ead_rad_apply = 'Y'";

			rs = st.executeQuery(destEadSrvc);

			while (rs.next()) {
				result = true;
			}
			//AccessLog.Log("after 20thwhl in ConD");
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();

		} catch (Exception e) {
			AccessLog.Log(e.getMessage());
		} finally {
			try {
				if (st != null)
					st.close();
				if (rs != null)
					rs.close();
				if (con != null)
					con.close();

			} catch (Exception e) {
				AccessLog.Log(e.getMessage());
			}
		}
		return result;
	}

	// Added on 14/07/2010 - Ends For kitsId : 70454

	// Added on 28/07/2010 - Starts For kitsId : 70454
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
			//AccessLog.Log("after 21thwhl in ConD");
			if (eadValue != null && eadValue.equalsIgnoreCase("y")) {
				result = true;
			}
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();

		} catch (Exception e) {
			AccessLog.Log(e.getMessage());
		} finally {			
			resources.closeResources(rs, pstmt);
			resources.cerrarConexion(con);
		}
		return result;
	}

	// Added on 28/07/2010 - Ends For kitsId : 70454

	public String creditStatus(String clientId, HttpSession session) {
		//AccessLog.Log("Revisando credito cliente :" + clientId);
		String credit = "N";
		try {
			con = ConnectDB.getConnection();
			String branchID = (String) session.getAttribute("branchid");
			branchID = branchID.toLowerCase();
			pstmt = con
					.prepareStatement("select CM_CRED_STUS_ID,CM_BRNC_ID from SYS_CLNT_MSTR where CM_CLNT_ID=?");
			pstmt.setString(1, clientId);
			rs = pstmt.executeQuery();
			// session.removeAttribute("status");
			String creditEnabled = "";
			String creditBranchId = "";
			//AccessLog.Log("Credit Branch: " + branchID);

			while (rs.next()) {
				creditEnabled = rs.getString(1);
				creditBranchId = rs.getString(2);
				//AccessLog.Log("Credit Branch: " + creditBranchId);
				if (creditEnabled.equalsIgnoreCase("ena")) {
					if (creditBranchId != null) {
						creditBranchId = creditBranchId.toLowerCase();
						if (branchID.equalsIgnoreCase(creditBranchId)) {
							session.setAttribute("status", "ena");
							credit = "Y";
						}
					} else {
						session.setAttribute("status", "ena");
						credit = "Y";
					}
				}
			}
			//AccessLog.Log("after 12thwhl in PreD");

		} catch (Exception e) {
			AccessLog.Log("Error al revisar credito cliente:" + e.getMessage());
		} finally {
			resources.closeResources(rs, pstmt);
			resources.cerrarConexion(con);
		}
		//AccessLog.Log("Terminado Revisar Credito Cliente");
		//AccessLog.Log("return credit "+credit);
		return credit;
	}

	// Funciones para reconvertir y agregar el valor de Zona Extendida para
	// Sobres
	// 04/01/2012
	public PrepaidGuiaMstr getPrepaidGuiaMaster(String guiaNo) {
		PrepaidGuiaMstr obj = new PrepaidGuiaMstr();
		try {
			con = ConnectDB.getConnection();
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
				obj.setLocationType(rs.getString(33) == null ? "":rs.getString(33));//AAP20
				obj.setCompanyId(rs.getString(34));
			}
		} catch (Exception e) {
			return null;
		} finally {
			resources.closeResources(rs, pstmt);
			resources.cerrarConexion(con);
		}
		return obj;
	}

	public String getTarifaEXT() {
		String tarifaEXT = "";
		String minimo = "";
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
			AccessLog.Log(e.getMessage());
			return null;
		} finally {
			resources.closeResources(rs, pstmt);
			resources.cerrarConexion(con);
		}

		return tarifaEXT;
	}

	public String reInsertRecord(PrepaidGuiaMstr object, HttpSession sess,
			String guiaNo, String tipoTarifa, String fiscalAddrCode,
			String origenAddrCode) {
		//CommonFunction commonobject = new CommonFunction();
		//String seq = "";
		String result = null;
		Date today = null;
		try {
			con = ConnectDB.getConnection();
			// inserta el registro de la guia prepagada
			cst = con.prepareCall(reinsertrecord);

			cst.setString(1, object.getBgm_ref_no());
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

			// Falta el 34 y 35
			cst.setString(34, fiscalAddrCode);
			cst.setString(35, origenAddrCode);
			cst.setString(36, object.getLocationType());//AAP20	

			//AccessLog.Log("after ack dewsc");
			cst.executeQuery();
			result = cst.getString(29);
			//seq = cst.getString(30);
		} catch (Exception e) {
			AccessLog.Log(e);
			AccessLog.Log(e.getMessage());
			result = e.getMessage() + " error de procedimiento";
		} finally {
			resources.cerrarCallableStatement(cst);
			resources.cerrarConexion(con);
		}
		return result;
	}

	public String reFactura(PrepaidGuiaMstr object, HttpSession session,
			HttpServletRequest request, String guiaNo, String payMode,
			String formNo, String formNoOld, String taxBrnc/*AAP20*/) {
		//String result = "";
		Date today = null;
		float taxPerc = 0;
		float taxReturnPerc = 0;
		float taxAmt = 0;
		float taxRetAmt = 0;
		float ackAmt = 0;
		float amount = 0;
		float netAmount = 0;
		float clintdiscount = 0;
		float discountAmount = 0;
		float currentAmount = 0;
		double currentTotalDiscount = 0;
		DecimalFormat dc = new DecimalFormat("0.00");
		// Agregado CenitSoft
		double totalEnvio = 0;
		double descuentoEnvio = 0;
		double totalACK = 0;
		double descuentoACK = 0;
		double totalInsurance = 0;
		double totalEAD = 0;
		double descuentoEAD = 0;
		double totalRAD = 0;
		double totalEXT = 0;
		double descuentoRAD = 0;
		double totalGlobal = 0;
		double descuentoGlobal = 0;
		String seqNum = "";

		totalEnvio = object.getBgm_gh_amt();
		descuentoEnvio = (totalEnvio * object.getBgm_dsc_percent()) / 100;
		totalACK = 0;
		if (object.getBgm_ack_service() != null)
			totalACK = object.getBgm_ack_amt();
		descuentoACK = (totalACK * object.getBgm_dsc_percent()) / 100;
		totalInsurance = object.getBgm_insur_amt();
		totalEAD = object.getBgm_ead_amt();
		descuentoEAD = (totalEAD * object.getBgm_dsc_percent()) / 100;
		totalRAD = object.getBgm_rad_amt();
		descuentoRAD = (totalRAD * object.getBgm_dsc_percent()) / 100;
		totalEXT = totalEXT + (object.getBgm_ext_amt() * 1);
		totalGlobal = totalEnvio + totalACK + totalEAD + totalRAD
				+ totalInsurance + totalEXT;
		descuentoGlobal = descuentoEnvio + descuentoACK + descuentoEAD
				+ descuentoRAD;
		double porcentajeImpuesto = 0;
		amount = Float.parseFloat(Double.toString(totalGlobal)
				.replace(',', '.'));
		try {

			con = ConnectDB.getConnection();
			con.setAutoCommit(false);
			st = con.createStatement();
			rs = st.executeQuery(invoiceSeq);
			while (rs.next()) {
				seqNum = rs.getString(1);
				today = rs.getDate(2);
			}
			//AccessLog.Log("after 16thwhl in PreD");
			if (rs != null)
				rs.close();
			if (st != null)
				st.close();
			//AccessLog.Log("After Sequnce Number" + seqNum);
			pstmt = con.prepareStatement(selectTax);
			//pstmt.setString(1, object.getBgm_prep_brnc_id().substring(0, 3));//AAP20
			pstmt.setString(1, taxBrnc.substring(0, 3));//AAP20
			rs = pstmt.executeQuery();
			while (rs.next()) {
				taxPerc = rs.getFloat(1);
				taxReturnPerc = rs.getFloat(2);
			}
			//AccessLog.Log("after 17thwhl in PreD");
			taxAmt = (amount * taxPerc) / 100;
			// taxRetAmt=(amount*taxReturnPerc)/100;
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
			String buscaCliente = "select cm_clnt_type from sys_clnt_mstr where cm_clnt_id=?";
			String tipoCliente = "";
			pstmt = con.prepareStatement(buscaCliente);
			pstmt.setString(1, object.getBgm_clint_id());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				tipoCliente = rs.getString(1);
			}
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
			if (tipoCliente.indexOf("I") > -1) {
				//taxReturnPerc = 0;//AAP20
				taxRetAmt = 0;
			} else {
				taxRetAmt = (Float.parseFloat(Double.toString(totalEnvio)) * taxReturnPerc) / 100;
			}

			HashMap<String, ArrayList<Object>> mapService = getServiceConfig(con, object.getBgm_clint_id(), object.getCompanyId(), "PREPAID", true);//AAP23
			
			amount = (float)Math.round(amount * 100) / 100;//REDONDEA A 2 DIGITOS
			taxAmt = (float)Math.round(taxAmt * 100) / 100;//REDONDEA A 2 DIGITOS
			taxRetAmt = (float)Math.round(taxRetAmt * 100) / 100;//REDONDEA A 2 DIGITOS
			netAmount = (amount+taxAmt);//-taxRetAmt;
			netAmount = (float)Math.round(netAmount * 100) / 100;//REDONDEA A 2 DIGITOS
			
			
			if (mapService != null && !mapService.isEmpty()) {//AAP23
				
				//actualiza tasas de iva retenido y empresas para servicios por set de pp.
				serviceRecalc(con, object.getCompanyId(), object.getBgm_clint_id(), object.getBgm_ref_no(), mapService);

				float shpGTaxRet = (float) totalEnvio * ((float) getTaxRetPercConfig(mapService.get("SHP-E")) / 100);
				float EADTaxRet = (float) totalEAD * ((float) getTaxRetPercConfig(mapService.get("EAD")) / 100);
				float RADTaxRet = (float) totalRAD * ((float) getTaxRetPercConfig(mapService.get("RAD")) / 100);
				float INVTaxRet = (float) totalInsurance * ((float) getTaxRetPercConfig(mapService.get("INV")) / 100);
				float ACKTaxRet = (float) totalACK * ((float) getTaxRetPercConfig(mapService.get("ACK")) / 100);
				float EXTTaxRet = (float) totalEXT * ((float) getTaxRetPercConfig(mapService.get("EXT")) / 100);

				taxRetAmt = shpGTaxRet + EADTaxRet + RADTaxRet + INVTaxRet + ACKTaxRet + EXTTaxRet;
				taxRetAmt = (float) Math.round(taxRetAmt * 100) / 100;// REDONDEA A 2 DIGITOS
			}
			
			float aux = (float)Math.round(((amount+taxAmt)-taxRetAmt) *100) / 100;
			// Insertar en PPG INV PMT HDR
			//CommonFunction commonobject = new CommonFunction();
			pstmt = con.prepareStatement(prepareInvoice);
			pstmt.setString(1, seqNum);
			pstmt.setString(2, object.getBgm_orgn_brnc_id());
			pstmt.setString(3, object.getBgm_prep_brnc_id().substring(0, 3));
			today = DateUtil.getDateValue("");
			pstmt.setDate(4, today);
			pstmt.setString(5, object.getBgm_clint_id());
			pstmt.setString(6, "A");
			pstmt.setFloat(7, amount);
			pstmt.setFloat(8, taxPerc);
			pstmt.setFloat(9, taxAmt);
			pstmt.setFloat(10, taxReturnPerc);
			pstmt.setFloat(11, taxRetAmt);
			pstmt.setFloat(12, aux);
			pstmt.setDate(13, null);
			pstmt.setString(14, payMode);
			pstmt.setString(15, "C");
			pstmt.setString(16, "");
			pstmt.setDate(17, today);
			pstmt.setString(18, ((Global) session.getAttribute("sGlobal")).getOrigenUserClave());
			pstmt.setDate(19, today);
			pstmt.setString(20, ((Global) session.getAttribute("sGlobal")).getOrigenUserClave());
			pstmt.executeQuery();
            pstmt.close();

			// Insertar en PPG INV PMT DTL
			pstmt = con.prepareStatement(invoiceDetail);
			pstmt.setString(1, seqNum);
			pstmt.setString(2, object.getBgm_ref_no());
			pstmt.setFloat(3, Float.parseFloat(Double.toString(object.getBgm_gh_amt()).replace(',', '.')));
			pstmt.setDate(4, today);
			pstmt.setString(5, ((Global) session.getAttribute("sGlobal")).getOrigenUserClave());
			pstmt.setDate(6, today);
			pstmt.setString(7, ((Global) session.getAttribute("sGlobal")).getOrigenUserClave());
			pstmt.executeQuery();
			if (pstmt != null) {
				pstmt.close();
				if (payMode.equalsIgnoreCase("PAID")) {
					pstmt = con.prepareStatement("update PPG_BOK_GUIA_MSTR set BGM_VALID_FLG='A',MDFD_ON=sysdate,MDFD_BY=? where BGM_REF_NO =?");
					pstmt.setString(1, ((Global) session.getAttribute("sGlobal")).getOrigenUserClave());
					pstmt.setString(2, object.getBgm_ref_no());
					pstmt.executeQuery();
				}
			}
			if (pstmt != null) {
				pstmt.close();
			}
			con.commit();
			//AccessLog.Log("after commit");
			if (payMode.equalsIgnoreCase("PAID")) {
				double doubleavailCred = 0;
				String query2 = "select FUN_CLNT_CRDT_AMT_WW(?) from dual";// AAP04
				pstmt = con.prepareStatement(query2);
				pstmt.setString(1, object.getBgm_clint_id());// vObject.getClientId());
				rs1 = pstmt.executeQuery();
				if (rs1.next())
					doubleavailCred = rs1.getDouble(1);
                                rs1.close();
				if (pstmt != null)
					pstmt.close();

				doubleavailCred = doubleavailCred + clintdiscount - taxAmt;
				//AccessLog.Log("doubleavailCred" + doubleavailCred);
				if (doubleavailCred <= 0) {
					request.setAttribute("errors",
							"No tiene cr�dito disponible");
					// request.setAttribute("errors","No credit limit available");
					cst = con.prepareCall(removeInvoiceLessCredit);
					cst.setString(1, seqNum);
					cst.setString(2, object.getBgm_clint_id());// vObject.getClientId());
					cst.executeQuery();
					return null;
				}
			}
			//AccessLog.Log("before invoice print");
			//AccessLog.Log("amount" + taxAmt);
			//AccessLog.Log("taxAmttaxAmt" + taxAmt);
			if (cst != null)
				cst.close();
			String resultoutput = "";
			//AccessLog.Log("AccessLog.Log(netAmount);" + netAmount);
			netAmount = Float.parseFloat(dc.format(netAmount));
			//AccessLog.Log("AccessLog.Log(netAmount);" + netAmount);
			String formCtr = fetchFormCounter(session, con);
			//AccessLog.Log("formCtr------" + formCtr);
//			if (!payMode.equalsIgnoreCase("PAID")) {//AAP20
//			if (payMode.equalsIgnoreCase("PAID")) {
//				cst = con.prepareCall(misSaveCredit);
//				cst.setString(1, object.getBgm_clint_id());// vObject.getClientId());
//				cst.setString(2, object.getBgm_prep_brnc_id());
//				cst.setString(3, session.getId());
//				cst.setFloat(4, netAmount);
//				cst.setString(5, ((Global) session.getAttribute("sGlobal")).getOrigenUserClave());
//				cst.setString(6, seqNum);
//				cst.setFloat(7, taxAmt);
//				cst.registerOutParameter(8, Types.VARCHAR);
//				cst.setString(9, formCtr + formNo);
//				cst.setString(10, "");
//				cst.setDouble(11, (totalEnvio + totalACK + totalInsurance
//						+ totalEAD + totalRAD));
//				cst.setDouble(12, totalEnvio);
//				cst.setDouble(13, totalACK);
//				cst.setDouble(14, totalInsurance);
//				cst.setDouble(15, totalEAD);
//				cst.setDouble(16, totalRAD);
//				cst.setDouble(17, 0); // Se manda 0 para no incluir el descuento
//				cst.setDouble(18, totalEXT);
//				cst.setFloat(19, taxRetAmt);
//				cst.setString(20, guiaNo);
//				cst.setString(21, taxBrnc);
//				cst.setString(22, "SHP-E");
//				cst.executeQuery();
//				resultoutput = cst.getString(8);
//				cst.close();
//				cst = null;
//				//AccessLog.Log("return................. " + resultoutput);
//			} else {
			//AAP20--NO SE REQUIERE ESTE CODIGO YA QUE NO HAY PAGO DE CONTADO EN MODIFICACIONES DE RASTREOS PREPAGO DL
//				cst = con.prepareCall(misSavecash);
//				cst.setString(1, object.getBgm_clint_id());// vObject.getClientId());
//				cst.setString(2, object.getBgm_prep_brnc_id());
//				cst.setString(3, session.getId());
//				// cst.setFloat(4,((amount+taxAmt)-taxRetAmt));
//				cst.setFloat(4, netAmount);
//				cst.setString(5, ((Global) session.getAttribute("sGlobal")).getOrigenUserClave());
//				cst.setString(6, seqNum);
//				cst.setFloat(7, taxAmt);
//				cst.registerOutParameter(8, Types.VARCHAR);
//				cst.setString(9, formCtr + formNo);
//				cst.setString(10, " ");
//				cst.setDouble(11, (totalEnvio + totalACK + totalInsurance
//						+ totalEAD + totalRAD));
//				cst.setDouble(12, totalEnvio);
//				cst.setDouble(13, totalACK);
//				cst.setDouble(14, totalInsurance);
//				cst.setDouble(15, totalEAD);
//				cst.setDouble(16, totalRAD);
//				cst.setDouble(17, 0); // no mandes nada
//				cst.setDouble(18, totalEXT);
//				cst.setFloat(19, taxRetAmt);
//				cst.setString(20, guiaNo);
//				
//				cst.executeQuery();
//				resultoutput = cst.getString(8);
//                                cst.close();
//				//AccessLog.Log("return................. " + resultoutput);
//			}
			if ((resultoutput != null) && (resultoutput.length() > 0)) {
				if (resultoutput.indexOf("ERROR") > -1) {
					if (resultoutput.indexOf("GH_BOOK_CONT_ID") > -1)
						request.setAttribute(
								"error",
								"El cliente no tiene asignado un contacto en su direcci�n, no es posible generar la factura");
					// request.setAttribute("errors","THE INPUTS ARE WRONG OR TARIFF IS DISABLED");
					else
						request.setAttribute("error", "Falla de inserci�n");
					con.rollback();
					cst = con.prepareCall(removeInvoiceLessCredit);
					cst.setString(1, seqNum);
					cst.setString(2, object.getBgm_clint_id());// vObject.getClientId());
					cst.executeQuery();
					return null;
				}
			}
			//AccessLog.Log("after over");
			/*
			 * cst = con.prepareCall(invoiceprint); cst.setString(1,seqNum);
			 * cst.setString(2,object.getBgm_prep_brnc_id());
			 * cst.registerOutParameter(3,Types.LONGVARCHAR); String
			 * toReturn=""; cst.executeQuery(); toReturn=cst.getString(3);
			 */
			//AccessLog.Log("After invoice pinted");
			//AccessLog.Log("FORM no inside UPDATE" + formNo);
			if (pstmt != null)
				pstmt.close();
			if (!payMode.equalsIgnoreCase("PAID")) {//AAPXX
				pstmt = con.prepareStatement(updateFormNo);
				pstmt.setString(1, formNo);
				pstmt.setString(2, ((Global) session.getAttribute("sGlobal")).getOrigenUserClave());
				pstmt.setString(3, (String) session.getAttribute("branchid"));
				pstmt.executeQuery();
				if (pstmt != null)
					pstmt.close();
			}
			// Cambias la referencia del ppg_inv_pmt_dtl para que te respete los
			// nuevos cambios
			String getRef = "select bgd_ref_no from ppg_bok_guia_dtl where bgd_trac_no= ? ";
			String referencia = "";
			pstmt = con.prepareStatement(getRef);
			pstmt.setString(1, guiaNo);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				referencia = rs.getString(1);
			}
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
			// Actualizas la referencia a la nueva
			String cambiaRef = "update ppg_inv_pmt_dtl set ipd_ref_no=? where ipd_inv_no=?";
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
			
			// Cambia el valor de la factura recien insertada para que te
			// respete los valores
			// Insertas un nuevo valor en ppg_bok_guia_addr para que copies los
			// datos de la direccion anterior
			// String
			// actualizaaddr="{call pack_ppg_web_2012.pro_ppg_re_ins_guia_box(?,?)}";
			// pstmt=con.prepareStatement(actualizaaddr);
			// pstmt.setString(1,formNoOld); // <-- Referencia Vieja p.eje. 4231
			// pstmt.setString(2,referencia); // <-- nueva referencia 4332
			// pstmt.executeQuery();
			if (!payMode.equalsIgnoreCase("PAID")) {//AAPXX
				String branchID = (String) session.getAttribute("branchid");
				String actualizaFinInvGuia = "update fin_invc_guia set IG_GUIA_NO=? WHERE IG_INVC_FORM_NO=? AND IG_INVC_BRNC_ID=?";
				pstmt = con.prepareStatement(actualizaFinInvGuia);
				pstmt.setString(1, guiaNo);
				pstmt.setString(2, formCtr + formNo);
				pstmt.setString(3, branchID);
				pstmt.executeQuery();
				if (pstmt != null)
					pstmt.close();
				// terminas la reinsercion de la factura
			}
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
			AccessLog.Log(e.getMessage());
			return (e.getMessage() + " <-- Error Try Catch DAO -->");
		} finally {
                    try {
                        if (rs != null) {
                            rs.close();
                        }
                    } catch (Exception e) {
                        AccessLog.Log(e.getMessage());
                    }
                    try {
                        if (rs1 != null) {
                            rs1.close();
                        }
                    } catch (Exception e) {
                        AccessLog.Log(e.getMessage());
                    }
                    try {
                        if (st != null) {
                            st.close();
                        }
                    } catch (Exception e) {
                        AccessLog.Log(e.getMessage());
                    }
                    try {
                        if (pstmt != null) {
                            pstmt.close();
                        }
                    } catch (Exception e) {
                        AccessLog.Log(e.getMessage());
                    }
                    try {
                        if (cst != null) {
                            cst.close();
                        }
                    } catch (Exception e) {
                        AccessLog.Log(e.getMessage());
                    }
                    try {
                        if (con != null) {
                            con.close();
                        }

                    } catch (Exception e) {
                        AccessLog.Log(e.getMessage());
                    }
		}
		// return result;
	}

	public String calcRetAmount(PrepaidGuiaMstr object, String payMode, String clntId, String taxBrnc) {
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
	    ResultSet rst1 = null;
	    try {
	        con = ConnectDB.getConnection();
	        
	        pstmt = con.prepareStatement(selectTax);
	        pstmt.setString(1, taxBrnc.substring(0, 3));//AAP20
	        ResultSet rst = pstmt.executeQuery();
	        while (rst.next()) {
	            taxPerc = rst.getFloat(1);
	            taxReturnPerc = rst.getFloat(2);
	        }
	        
	        taxAmt = (amount * taxPerc) / 100;
	        
	        if (rst != null)
	        	rst.close();
			if (pstmt != null)
				pstmt.close();
	        
	        String buscaCliente = "select cm_clnt_type from sys_clnt_mstr where cm_clnt_id=?";
	        String tipoCliente = "";
	        pstmt = con.prepareStatement(buscaCliente);
	        pstmt.setString(1, clntId);
	        rst = pstmt.executeQuery();
	        while (rst.next()) {
	            tipoCliente = rst.getString(1);
	        }
	        
	        if (rst != null)
	        	rst.close();
			if (pstmt != null)
				pstmt.close();
	        
	        if (tipoCliente.indexOf("I") > -1) {
	            taxRetAmt = 0;
	        } else {
	            taxRetAmt = (Float.parseFloat(Double.toString(totalEnvio)) * taxReturnPerc) / 100;
	        }
	        
	        HashMap<String, ArrayList<Object>> mapService = getServiceConfig(con, object.getBgm_clint_id(), object.getCompanyId(), "PREPAID", true);//AAP23

	        amount = (float)Math.round(amount * 100) / 100;//REDONDEA A 2 DIGITOS
	        taxAmt = (float)Math.round(taxAmt * 100) / 100;//REDONDEA A 2 DIGITOS
	        taxRetAmt = (float)Math.round(taxRetAmt * 100) / 100;//REDONDEA A 2 DIGITOS
	        netAmount = (amount+taxAmt);//-taxRetAmt;
	        netAmount = (float)Math.round(netAmount * 100) / 100;//REDONDEA A 2 DIGITOS
	        
	        
	        if (mapService != null && !mapService.isEmpty()) {
	            float shpGTaxRet = (float) totalEnvio * ((float) getTaxRetPercConfig(mapService.get("SHP-E")) / 100);
				float EADTaxRet = (float) totalEAD * ((float) getTaxRetPercConfig(mapService.get("EAD")) / 100);
				float RADTaxRet = (float) totalRAD * ((float) getTaxRetPercConfig(mapService.get("RAD")) / 100);
				float INVTaxRet = (float) totalInsurance * ((float) getTaxRetPercConfig(mapService.get("INV")) / 100);
				float ACKTaxRet = (float) totalACK * ((float) getTaxRetPercConfig(mapService.get("ACK")) / 100);
				float EXTTaxRet = (float) totalEXT * ((float) getTaxRetPercConfig(mapService.get("EXT")) / 100);

				taxRetAmt = shpGTaxRet + EADTaxRet + RADTaxRet + INVTaxRet + ACKTaxRet + EXTTaxRet;
				taxRetAmt = (float) Math.round(taxRetAmt * 100) / 100;// REDONDEA A 2 DIGITOS
	        }
	    } catch (Exception e) {
	        AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("reFactura()_Error1:").append(e).toString());
	        e.printStackTrace();
	    } finally {
	        try {
	            if(rst1 != null)
	                rst1.close();
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
	    
	    return Float.toString(taxRetAmt);
	}
	
	public int fetchFormNumber(HttpSession session) {
		String FormNo = "";
		int formnumber = 0;
		try {
			con = ConnectDB.getConnection();
			//Se cabia cst por pstmt
			pstmt = con.prepareStatement(fetchFormNo);
			pstmt.setString(1, (String) session.getAttribute("branchid"));
			//cst.registerOutParameter(1, Types.VARCHAR);
			//cst.setString(2, (String) sessionIn.getAttribute("branchid"));
			//cst.execute();
			rs = pstmt.executeQuery();
			while(rs.next()) {
				FormNo = rs.getString(1);
			}
			formnumber = Integer.parseInt(FormNo.substring(2, FormNo.length()));
			//formnumber = Integer.parseInt(FormNo) + 1;

		} catch (Exception e) {
			AccessLog.Log(e.getMessage());
			return 0;
		} finally {
			resources.cerrarPreparedStatement(pstmt);
			resources.cerrarResultSet(rs);
			resources.cerrarConexion(con);
		}
		return formnumber;
	}

	private String fetchFormCounter(HttpSession session, Connection con) {
		String formCtr = "";

		try {
			//con = ConnectDB.getConnection();
			pstmt = con.prepareStatement(fetchFormCtr);
			pstmt.setString(1, (String) session.getAttribute("branchid"));
			rs = pstmt.executeQuery();
			while (rs.next()) {
				formCtr = rs.getString(1);
			}
			//AccessLog.Log("after 2ndwhl in PreD");
		} catch (Exception e) {
			AccessLog.Log(e.getMessage());
			return "";
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (rs != null)
					rs.close();
				/*if (con != null)
					con.close();*/
			} catch (Exception e) {
				AccessLog.Log(e.getMessage());
			}
		}
		return formCtr;
	}
	
	public String clientType(String clientId) {
		String returnValue = "";
		String getClientType = "select cm_clnt_type from sys_clnt_mstr where cm_clnt_id=?";
		try {
			con = ConnectDB.getConnection();
			pstmt = con.prepareStatement(getClientType);
			pstmt.setString(1, clientId);
			rs = pstmt.executeQuery();
			// session.removeAttribute("status");
			while (rs.next()) {
				returnValue = rs.getString(1);
			}
			// AccessLog.Log("after 12thwhl in PreD");
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("clientType()_Error1:").append(e).toString());
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
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("clientType()_Error2:").append(e).toString());
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
			//AccessLog.Log("after 12thwhl in PreD");

		} catch (Exception e) {
			AccessLog.Log(e.getMessage());
		} finally {
			resources.closeResources(rs, pstmt);
			resources.cerrarConexion(con);
		}

		return returnValue;
	}

	// Verificar si el cliente asignado pertenece a una zona extendida
	public boolean isextendedzone(ConversionForm conversionform) {
		//String branchID = "";
		String olID = "";
		boolean returnValue = false;
		String clientNo = conversionform.getDestclave();
		try {
			con = ConnectDB.getConnection();
			pstmt = con.prepareStatement(verificazonaextendida);
			pstmt.setString(1, clientNo);
			pstmt.setString(2, "70");// AAP02
			pstmt.setString(3, conversionform.getDestcode());// AAP02
			//System.out.println("verificazonaextendida " + verificazonaextendida);
			//System.out.println("clientNo " + clientNo);
			//System.out.println("conversionform.getDestcode() " + conversionform.getDestcode());
			rs = pstmt.executeQuery();

			while (rs.next()) {
				//branchID = rs.getString(1);
				olID = rs.getString(2) == null ? "" : rs.getString(2);
				conversionform.setOperadorLogistico(olID);
				returnValue = true;
			}
		} catch (Exception e) {
			AccessLog.Log(e.getMessage());
		} finally {
			resources.closeResources(rs, pstmt);
			resources.cerrarConexion(con);
		}
		return returnValue;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList fetchFormNo(String branch_id) {
		ArrayList hold = new ArrayList();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			String query = null;
			con = ConnectDB.getConnection();
			//String ackType = null;
			query = "	SELECT pm_parm_code2 FROM sys_parm_mstr "
					+ "	WHERE pm_parm_type = 'FORM_NO' AND PM_MDUL_ID='BOK' AND PM_PARM_CODE1=?"
					+ // Checked
					" AND PM_PARM_CODE2<>'FX' AND PM_PARM_CODE2<>'PP' AND PM_PARM_CODE2<>'WW'";
			pst = con.prepareStatement(query);
			pst.setString(1, branch_id);
			rs = pst.executeQuery();
			ArrayList listaCajasType = new ArrayList();
			ArrayList listaCajasValue = new ArrayList();
			//int i = 0;
			String caja = "";
			while (rs.next()) {
				//AccessLog.Log("inside fetchAcknowledgementTypeDesc");
				caja = rs.getString(1);
				listaCajasType.add(caja);
				listaCajasValue.add(caja);
				// AccessLog.Log("ackTypeValue"+ackTypeValue.get(i));
				//i++;
			}
			//AccessLog.Log("after 5thwhl in PreD");
			hold.add(listaCajasType);
			hold.add(listaCajasValue);
			//AccessLog.Log("out of fetchAcknowledgementTypeDesc");
		} catch (Exception e) {
			AccessLog.Log(e.getMessage());
		} finally {
			resources.closeResources(rs, pst);
			resources.cerrarConexion(con);
		}
		return hold;
	}

	public String getDeliveryHours(String sucOrigen, String sucDest, String ocuEadFlag, String codCol, String srvcType, String clntId) {
		String ssSrvcId = "SHP-E";
		String horasEntrega = "";
		JavDeliveryHours horas = new JavDeliveryHours();
		try {
			con=ConnectDB.getConnection();
		
			//horasEntrega = horas.getDeliveryHoursRecord(con, sucOrigen, sucDest, ocuEadFlag, codCol, getSysDateFormat(con,"'HH24:MI'"), srvcType, clntId);
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
	public boolean obtieneInfRequerimientosACK(String groupClientId, String tipo_acuse) {

		PreparedStatement pst = null;
		ResultSet rs = null;
		boolean exist = false;

		try {
			con = ConnectDB.getConnection();
			pst = con.prepareStatement("SELECT CR_CLNT_ID FROM SYS_CLNT_SRVC_REQM_MSTR WHERE CR_CLNT_ID = ? AND CR_SRVC_ID = ? AND CR_REFR_SRVC_ID = ?");
			pst.setString(1, groupClientId);
			pst.setString(2, tipo_acuse);
			pst.setString(3, "ACK");
			rs = pst.executeQuery();

			if (rs.next()) {
				exist = true;
			}
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("obtieneInfRequerimientosACK()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
                    try {
                        if (rs != null) {
                            rs.close();
                        }
                    } catch (Exception e2) {
                        AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("obtieneInfRequerimientosACK()_Error2:").append(e2).toString());
                        e2.printStackTrace();
                    }
                    try {
                        if (pst != null) {
                            pst.close();
                        }
                    } catch (Exception e2) {
                        AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("obtieneInfRequerimientosACK()_Error2:").append(e2).toString());
                        e2.printStackTrace();
                    }
                    try {
                        if (con != null) {
                            con.close();
                        }
                    } catch (Exception e2) {
                        AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("obtieneInfRequerimientosACK()_Error2:").append(e2).toString());
                        e2.printStackTrace();
                    }
		}
		return exist;
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
			/*
			 * cst.setString(1, genGuiaNumber) cst.setString(2,
			 * conversionform.getClientId()); cst.setString(3,
			 * conversionform.getDestclave()); cst.setString(4,
			 * conversionform.geteMailOrigText()); cst.setString(5,
			 * conversionform.geteMailDestText()); cst.setString(6, "P");
			 * cst.setString(7, documentador); cst.registerOutParameter(8,
			 * Types.VARCHAR);
			 */
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("insertWebCntrlMail()_Error:").append(e).toString());
			e.printStackTrace();
		}
	}// AAP04

	private String getFormNumber(String branchId, String modulo,
			String serieCaja) {
		CallableStatement clst = null;
		String formNumber="A0";
		Connection localconn = null;
		try {
			localconn = ConnectDB.getConnection();
			
			String sql = "declare\n"
                    + "pragma Autonomous_Transaction;\n"
                    + "begin\n"
                    + "? := fun_ftch_form_no_v2(?, ?, ?, ?); \n"
                    + "commit;\n"
                    + "end; ";
			
			clst = localconn.prepareCall(sql);
			clst.registerOutParameter(1, Types.VARCHAR);
			clst.setString(2,branchId);
			clst.setString(3,branchId);
			clst.setString(4, serieCaja);
			clst.setString(5, modulo);
			clst.executeQuery();
			formNumber = clst.getString(1);
			
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("getFormNumber()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.cerrarCallableStatement(clst);
			resources.cerrarConexion(localconn);
		}
		return cnct.delete(0, cnct.length()).append(serieCaja).append(formNumber).toString();
	}

	/****************************************************************
	 * Metodo para validar si el numero de forma (guia) ya existe *
	 ****************************************************************/
	private boolean validaGH_FORM_NO(Connection conn, ConversionForm serForm,
			String docuType) {
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
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("validaGH_FORM_NO()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.closeResources(rst, pst);
		}
		return existe;
	}

	public String verificaRastreo(String tracno, String moduloOrigen,
			String OpcOrigen) {
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
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("verificaRastreo()_Error1:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				resources.closeResources(rs, pstmt);
				resources.cerrarConexion(con);
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("verificaRastreo()_Error2:").append(e).toString());
				e.printStackTrace();
			}
		}
		return result;
	}
	@SuppressWarnings("rawtypes")
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
			
			serForm.setOrgien1(result.get("orgien1").toString());
			serForm.setOrgien2(result.get("orgien2").toString());
			serForm.setOrgientelefono(result.get("orgientelefono").toString());
			serForm.setOrgioncode(result.get("orgioncode").toString());
			serForm.setOrgiencolonia1(result.get("orgiencolonia1").toString());
			serForm.setOrgiencolonia2(result.get("orgiencolonia2").toString());
			
			//AccessLog.Log("result "+result);
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
			st.append("INSERT INTO BOK_GUIA_HEAD_EXTRA(GE_GUIA_NO, GE_CLNT_ID, GE_CLNT_CCOSTO_ID, GE_CLNT_USER_ID, GE_PROMESA_HRS");
			if(!shipTypeSEG.isEmpty()){
			    st.append(", SRVC_ID ");
			}
			st.append(")");
			st.append(" VALUES(?, ?, ?, ?, ?");
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
			if(!shipTypeSEG.isEmpty()){
				pst.setString(6, shipTypeSEG);
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
			resources.closeResources(rs, pstmt);
			resources.cerrarConexion(con);
		}
		return localCredit;
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
	public String getCadenaImpresion(HttpServletRequest request, String rastreo, boolean acuseXT, String rastreo_de_retorno) {
		PreparedStatement pst = null;
		ResultSet rsCad = null;
		String cadenaEtiqueta = "";
		Clob myClob = null;
		try {
			con = ConnectDB.getConnection();
			//String query = getFuncionEti(con, );
			//String query = "SELECT CP_ETIQUETA(?) FROM DUAL";
			//String query = "SELECT CP_ETIQUETA_V1(?, ?, ?) FROM DUAL";
			String query = "SELECT CP_ETIQUETA_ZPL(?, ?, ?, ?) FROM DUAL";
			pst = con.prepareStatement(query);
			pst.setString(1, rastreo);
			pst.setString(2, "N");
			pst.setString(3, "0");
			pst.setString(4, "A");
			
			rsCad = pst.executeQuery();
			
			if (rsCad.next()) {
				myClob = rsCad.getClob(1);
				cadenaEtiqueta = ClobToString(myClob);
			}
			
			//System.out.println("Parametro: acuseXT= " + acuseXT);
			if(acuseXT==true) {
				//Cerramos el Statement y el Resultset
				resources.closeResources(rs, pst);
				//Ejecutmos el procedimiento que generar� las guias de retorno
				/*String query_retorno = "{call PACK_ACUSES_XT.PRO_GEN_GUI_RET_XT(?,?,?)}";
				System.out.println("RASTREO ORIGINAL: " + rastreo);
				
				cst = con.prepareCall(query_retorno);
				cst.setString(1, rastreo);
				cst.setString(2, "2");//2 por que es de tipo WW
				cst.registerOutParameter(3, Types.VARCHAR);
				cst.executeQuery();
				//Obtenemos la salida del Procedimiento
				rastreo_de_retorno = cst.getString(3);
				System.out.println("Rastreo de retorno: " + rastreo_de_retorno);
				cst.close();*/
				if(!rastreo_de_retorno.isEmpty()) {
					//Si se genero el rastreo correctamente, debemos obtener la cadena de impresion
					pst = con.prepareStatement(query);
					pst.setString(1, rastreo_de_retorno);
					pst.setString(2, "N");
					pst.setString(3, "0");
					pst.setString(4, "A");
					//Ejecutamos la consulta
					rs = pst.executeQuery();
					//Si obtenemos resultados debemos concatenar a la cadena de etiqueta el resultado
					if (rs.next()) {
						myClob = rs.getClob(1);
						cadenaEtiqueta += "\n" + ClobToString(myClob);
					}
					//Ahora desactivamos el rastreo de retorno
					/*if(pst!=null)
						pst.close();
					String desactiva = "{call PACK_ACUSES_XT.PRO_INAC_GUI_RET(?)}";
					pst = con.prepareStatement(desactiva);
					pst.setString(1, rastreo);
					pst.executeQuery();
					String etiquetas_a_imprimir = "{call PACK_ACUSES_XT.PRO_ETIQ_PRN(?)}";
					if(cst!=null)
						cst.close();
					cst = con.prepareCall(etiquetas_a_imprimir);
					cst.setString(1, rastreo_de_retorno);
					cst.execute();
					cst.close();
					System.out.println("Etiquetas generadas");*/
				}
				
			}
			new PrintFileExport(request.getSession().getServletContext().getRealPath("/")).writeToGuiaFile(request, cadenaEtiqueta, getTimeStamp(con).getTime());
			//AccessLog.Log(cnct.delete(0,cnct.length()).append(msgAvi).append("getCadenaImpresion()_cadenaEtiqueta INFORMACION PARA IMPRESION ").append(cadenaEtiqueta).toString());		
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getCadenaImpresion()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.closeResources(rsCad, pst);
			resources.cerrarConexion(con);
		}
		return cadenaEtiqueta;
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
				AccessLog.Log("writeToGuiaFile()Exception_Error:"+e);
				e.printStackTrace();
			}
		}
		return strOut.toString();
	}
	
//	private String getFuncionEti(Connection conn, Global global) {
//		String funcion = "CP_ETIQUETA";
//		ArrayList result = null;
//		
//		try {
//			//SELECT CP_ETIQUETA_DELIMITED(?) FROM DUAL. CADENA DELIMITADA
//			//SELECT CP_ETIQUETA(?) FROM DUAL. CADENA ZPL PARA ENVIAR A PUERTO DE IMPRESION
//			ConsultaParametros parametros = new ConsultaParametros();
//			result = parametros.QryMdulTypeParm1(conn, "WEB", "PRINTGUIAWS", global.getClientId());
//			
//			if (!result.isEmpty()) {
//				funcion = ((ArrayList)result.get(0)).get(4).toString();				
//			}
//		} catch (Exception e) {
//			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getFuncionEti()Exception_Error:").append(e).toString());
//			e.printStackTrace();
//		}
//		return cnct.delete(0,cnct.length()).append("SELECT ").append(funcion).append("(?) FROM DUAL").toString();
//	}
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
			resources.closeResources(rs, pstmt);
			resources.cerrarConexion(con);
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
	public int insertBookGuiaRel(ConversionForm generalForm, String trackingNo, Global global) {//AAP07 REGISTRO MULTICAJA
		PreparedStatement pst = null;		
		int insertCount = 0;
		//String genMultiCaja = "N";
		try {			
			con = ConnectDB.getConnection();

			String insertQuery = "INSERT INTO BOK_GUIA_REL(GR_GUIA_NO, GR_GUIA_REL, GR_FLAG_1, CRTD_ON, CRTD_BY, MDFD_ON, MDFD_BY) VALUES(?, ?, ?, SYSDATE, ?, SYSDATE, ?)";
			
			pst = con.prepareStatement(insertQuery);
			pst.setString(1, trackingNo);
			pst.setString(2, trackingNo);				
			pst.setString(3, "P");
			pst.setString(4, global.getOrigenUserClave());
			pst.setString(5, global.getOrigenUserClave());
		
			insertCount = pst.executeUpdate();
			
			pst.close();			
			
			/*insertQuery = "UPDATE SYS_CLNT_USER SET CU_GEN_MULTICAJA = ? WHERE CU_CLNT_ID = ? AND CU_USER_ID = ?";
			pst = con.prepareStatement(insertQuery);
			pst.setString(1, genMultiCaja);
			pst.setString(2, global.getClientId());
			pst.setString(3, global.getOrigenUserClave());
			
			pst.executeUpdate();*/
			
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

	public String isInsideTimeCourtSEG(String orgnBrnc, String destBrnc, String srvcTypeId, String clienteID, String addrCode){
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
			pst.setString(4, clienteID);
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
	
	public List<ShipTypeSEG> getFetchShipSEGALLActive(String orgnBrncId, String destBrncID, String clienteID, String addrCode) {
		PreparedStatement pst = null;
		ResultSet rst = null;
		List<ShipTypeSEG> listSEG = null;
		Connection con = null;
		try {
			con = ConnectDB.getConnection();
			String str = fetchShipSEGALL;
			str = str.replace(":GH_ORGN_BRNC_ID", "'"+orgnBrncId+"'");
			str = str.replace(":GH_DEST_BRNC_ID", "'"+destBrncID+"'");
			str = str.replace(":CLIENTEID", "'"+clienteID+"'");
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
	
private void serviceRecalc(Connection conn, String companyIdOriginal, String clienteID, String refNo, HashMap<String, ArrayList<Object>> mapService) {//AAP23
		
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
			
			/*envia a actualizar ppg_bok_guia_mstr para asignacion de tasas de iva retenido y empresas*/
								
				mapService = getServiceConfig(con, clienteID, companyIdOriginal, "BOOKING", true);//AAP23
				
				shpGTaxRetPerc = getTaxRetPercConfig(mapService.get("SHP-E"));
				EADTaxRetPerc = getTaxRetPercConfig(mapService.get("EAD"));
				RADTaxRetPerc = getTaxRetPercConfig(mapService.get("RAD"));
				INVTaxRetPerc = getTaxRetPercConfig(mapService.get("INV"));
				ACKTaxRetPerc = getTaxRetPercConfig(mapService.get("ACK"));
				EXTTaxRetPerc = getTaxRetPercConfig(mapService.get("EXT"));
				
				shpGCpnyId = getCpnyConfig(mapService.get("SHP-E"), companyIdOriginal);
				EADCpnyId = getCpnyConfig(mapService.get("EAD"), companyIdOriginal);
				RADCpnyId = getCpnyConfig(mapService.get("RAD"), companyIdOriginal);
				INVCpnyId = getCpnyConfig(mapService.get("INV"), companyIdOriginal);
				ACKCpnyId = getCpnyConfig(mapService.get("ACK"), companyIdOriginal);
				EXTCpnyId = getCpnyConfig(mapService.get("EXT"), companyIdOriginal);
				
				updatePpgBokGuiaMstr(conn, refNo, shpGTaxRetPerc, EADTaxRetPerc, RADTaxRetPerc, INVTaxRetPerc, ACKTaxRetPerc, EXTTaxRetPerc, 
						shpGCpnyId, EADCpnyId, RADCpnyId, INVCpnyId, ACKCpnyId, EXTCpnyId);				
									
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("serviceRecalc()1_Error:").append(e).toString());
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
			shpGConfig = getTaxRenEspecial(conn, clntRet, "SHP-E", companySrvcCalculado, module);
			
			//no hay configuracion
			if (shpGConfig.isEmpty()) {
				mapService = new HashMap<>(0);
			} else {
				//no hay configuracion
				if ((int)shpGConfig.get(0)== -1) {
					mapService = new HashMap<>(0);
				} else {
					mapService = new HashMap<>(6);
					mapService.put("SHP-E", shpGConfig);
					
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
			cst.registerOutParameter(7, Types.VARCHAR);//compa�ia retencion
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
	 * m�todo para actualizar total de gu�a		*
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
			cst.setString(1, "ENV");
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
			AccessLog.Log(
					cnct.delete(0, cnct.length()).append(msgErr).append("getMinMeasures()_Error1:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.cerrarCallableStatement(cst);
		}
	}

}