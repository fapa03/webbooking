/**
 * @author: Abraham Daniel Arjona Peraza
 * Fecha de Creación: 29/07/2015
 * Compañía: PAQUETEXPRESS.
 * Descripción del programa: Bean para proceso de registro y recuperacion de dato de Hoja de excel.
 * FileName: 
 * SessionVariables:
 * Other Used Classes:
 * Function Names: 
 * ----------------------------------------------------------------- 
 * MODIFICACIONES:  
 * -----------------------------------------------------------------
 * Clave: 
 * Autor: 
 * Fecha: 
 * Descripción:  
 * ------------------------------------------------------------------
 * Clave: 
 * Autor: 
 * Fecha: 
 * Descripción:  
 * ------------------------------------------------------------------
 */
package bean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.paquetexpress.www.webbooking.GuiasRetorno.retorno.DataResponse;
import com.paquetexpress.www.webbooking.GuiasRetorno.retorno.RastreoRetorno;

import beanUtil.ConnectDB;
import logger.AccessLog;

public class SheetRecovery {
	private StringBuffer concatena = new StringBuffer();
	private final String msgErr = new StringBuffer("MSERR_B_").append(this.getClass().getName()).append(".").toString();
	private Resources resources = new Resources();
	
	public int setSheetMstr(Connection con, Global global, String fileName) {
		PreparedStatement pst = null;
		String query = null;
		int reg = 0;
		try {
			query = concatena.delete(0, concatena.length())
				.append("INSERT INTO WEB_BACH_MSTR ")
			   	.append("(BM_CLNT_ID, BM_USER_ID, BM_FILE_NAME, BM_STUS_FLAG, BM_PRNT_FNAME, CRTD_ON, CRTD_BY, MDFD_ON, MDFD_BY) ")
			   	.append("VALUES ")
			 	.append("(?, ?, ?, ?, null, SYSDATE, ?, SYSDATE, ?)").toString();
			
			pst = con.prepareStatement(query);
			pst.setString(1, global.getClientId());
			pst.setString(2, global.getOrigenUserClave());
			pst.setString(3, fileName);
			pst.setString(4, "C");
			pst.setString(5, global.getOrigenUserClave());
			pst.setString(6, global.getOrigenUserClave());

			reg = pst.executeUpdate();

			//AccessLog.Log("setSheetMstr() reg "+reg);
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0, concatena.length()).append(msgErr).append("setSheetMstr()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.cerrarPreparedStatement(pst);
		}
		return reg;
	}
	
	public int setRowSheetDtl(Connection con, Global global, String fileName, ReadxlDTO row, int rowIndex) {
		PreparedStatement pst = null;
		String query = null;
		int reg = 0;
		String cadenaImpresion = "";
		try {
			query = concatena.delete(0, concatena.length())
					.append("Insert into WEB_BACH_DETL_GUIA")
				    .append("(BD_CLNT_ID, BD_USER_ID, BD_FILE_NAME, BD_TEXT_ROW_NO, BD_DEST_CUST_CLNT_ID, BD_DEST_CLNT_NAME, BD_DEST_CLNT_RFC, BD_DEST_COUNTRY, ")
				    .append("BD_DEST_CITY, BD_DEST_ESTATE, BD_DEST_STRT_NAME, BD_DEST_DRNR, BD_DEST_CLNY, BD_DEST_ZIP, BD_DEST_EMAIL, BD_DEST_PHONE, BD_DEST_REFDOM, ")
				    .append("BD_DEST_QUNT, BD_DEST_TIPBULTO, BD_DEST_CONT, BD_DEST_SLAB_NO, BD_DEST_WEIGHT, BD_DEST_VOLUME, BD_DEST_LONG, BD_DEST_WIDTH, BD_DEST_HIGH, ") 
				    .append("BD_SRVC_DLVY_TYPE, BD_SRVC_PAID_MODE, BD_SRVC_ACK, BD_SRVC_COD, BD_SRVC_DCLR_VAL, BD_SRVC_COVERAGE, BD_SRVC_COMT, BD_SRVC_REFR, BD_CUST_AGNT, ") 
				    .append("BD_PEDI_NUM, BD_CCOSTO, BD_MSGE_ID, BD_MSGE_TYPE, BD_MSGE_DES, BD_GUIA_NO, BD_FORM_NO, BD_TOTAL, BD_PRINT_CODE, BD_STUS_FLAG, CTRD_ON, CRTD_BY, ")
				    .append("MDFD_ON, MDFD_BY, BD_GUIAS_RETURN, BD_SHIP_TYPE, BD_PRODUCT_ID) ")//AAP25 se agrega BD_SHIP_TYPE
				 .append("Values ")
				   .append("(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ")
				   .append("?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, SYSDATE, ?, SYSDATE, ?, ?, ?, ?)").toString();				
			
			pst = con.prepareStatement(query);
			
			String BD_DEST_CUST_CLNT_ID = row.getIdClienteDest();
			if (row.getIdClienteDest().length()>15)
				BD_DEST_CUST_CLNT_ID = row.getIdClienteDest().substring(0,15);
			
			String BD_DEST_CLNT_NAME = row.getNombre();
			if (row.getNombre().length()>80)
				BD_DEST_CLNT_NAME = row.getNombre().substring(0,80);
			
			String BD_DEST_CLNT_RFC = row.getRfc();
			if (row.getRfc().length()>13)
				BD_DEST_CLNT_RFC = row.getRfc().substring(0,13);
			
			String BD_DEST_COUNTRY = row.getPais();
			if (row.getPais().length()>60)
				BD_DEST_COUNTRY = row.getPais().substring(0,60);
			
			String BD_DEST_ESTATE = row.getEstado();
			if (row.getEstado().length()>60)
				BD_DEST_ESTATE = row.getEstado().substring(0,60);
			
			String BD_DEST_CITY = row.getCiudad();
			if (row.getCiudad().length()>60)
				BD_DEST_CITY = row.getCiudad().substring(0,60);
			
			String BD_DEST_STRT_NAME = row.getCalle();
			if (row.getCalle().length()>80)
				BD_DEST_STRT_NAME = row.getCalle().substring(0,80);
			
			String BD_DEST_DRNR = row.getNumero();
			if (row.getNumero().length()>15)
				BD_DEST_DRNR = row.getNumero().substring(0,15);

			String BD_DEST_CLNY = row.getColonia();
			if (row.getColonia().length()>60)
				BD_DEST_CLNY = row.getColonia().substring(0,60);
			
			String BD_DEST_ZIP = row.getCp();
			if (row.getCp().length()>15)
				BD_DEST_ZIP = row.getCp().substring(0,15);
			
			String BD_DEST_EMAIL = row.geteMail();
			if (row.geteMail().length()>80)
				BD_DEST_EMAIL = row.geteMail().substring(0,80);
			
			String BD_DEST_PHONE = row.getTelefono();
			if (row.getTelefono().length()>20)
				BD_DEST_PHONE = row.getTelefono().substring(0,20);
			
			String BD_DEST_REFDOM = row.getReferenciaDomicilio();
			if (row.getReferenciaDomicilio().length()>50)
				BD_DEST_REFDOM = row.getReferenciaDomicilio().substring(0,50);
			
			int BD_DEST_QUNT = 0;
			try {BD_DEST_QUNT = Integer.parseInt(row.getCantidad());} catch (Exception e) {BD_DEST_QUNT = 0;}
			
			String BD_DEST_TIPBULTO = row.getCodigoBulto();
			if (row.getCodigoBulto().length()>15)
				BD_DEST_TIPBULTO = row.getCodigoBulto().substring(0,15);
			
			String BD_DEST_CONT = row.getContenido();
			if (row.getContenido().length()>100)
				BD_DEST_CONT = row.getContenido().substring(0,100);
			
			String BD_DEST_SLAB_NO = row.getTipoTarifa();
			if (row.getTipoTarifa().length()>15)
				BD_DEST_SLAB_NO = row.getTipoTarifa().substring(0,15);
			
			String BD_SRVC_DLVY_TYPE = row.getTipoEntrega();
			if (row.getTipoEntrega().length()>15)
				BD_SRVC_DLVY_TYPE = row.getTipoEntrega().substring(0,15);
			
			String BD_SRVC_PAID_MODE = row.getFormaPago();
			if (row.getFormaPago().length()>10) {
				BD_SRVC_PAID_MODE = row.getFormaPago().substring(0,10);}
			
			if (row.getFormaPago().length() == 0) {
				BD_SRVC_PAID_MODE = "PAID";
			}

			String BD_SRVC_ACK = row.getAcuse();
			if (row.getAcuse().length()>15)
				BD_SRVC_ACK = row.getAcuse().substring(0,15);
			
			String BD_SRVC_COVERAGE = row.getCobertura();
			if (row.getCobertura().length()>15)
				BD_SRVC_COVERAGE = row.getCobertura().substring(0,15);
			
			String BD_SRVC_COMT = row.getComentarios();
			if (row.getComentarios().length()>255)
				BD_SRVC_COMT = row.getComentarios().substring(0,255);
			
			String BD_SRVC_REFR = row.getReferencia();
			if (row.getReferencia().length()>65)
				BD_SRVC_REFR = row.getReferencia().substring(0,65);
			
			String BD_CUST_AGNT = row.getAgenteAduanal();
			if (row.getAgenteAduanal().length()>50)
				BD_CUST_AGNT = row.getAgenteAduanal().substring(0,50);
			
			String BD_PEDI_NUM = row.getPedimento();
			if (row.getPedimento().length()>50)
				BD_CUST_AGNT = row.getPedimento().substring(0,50);
			
			String BD_CCOSTO = row.getCentroCosto().toString();
			if (row.getCentroCosto().length()>15)
				BD_CCOSTO = row.getCentroCosto().substring(0,15);
			
			String BD_GUIAS_RETURN = row.getCantidadRastreos().toString();
			if (row.getCantidadRastreos().length()>5)
				BD_GUIAS_RETURN = row.getCantidadRastreos().substring(0,5);
			
			String BD_SHIP_TYPE = row.getSegId().toString();//AAP25
			if (row.getSegId().length()>15)//AAP25
				BD_SHIP_TYPE = row.getSegId().substring(0,15);//AAP25
			
			String BD_PRODUCT_ID = row.getProductId().toString().trim();
			if (row.getProductId().length()>15)
				BD_SHIP_TYPE = row.getProductId().substring(0,15);
			
			pst.setString( 1, global.getClientId());//BD_CLNT_ID
			pst.setString( 2, global.getOrigenUserClave());//BD_USER_ID
			pst.setString( 3, fileName);	//BD_FILE_NAME
			pst.setInt   ( 4, rowIndex); //BD_TEXT_ROW_NO
			pst.setString( 5, BD_DEST_CUST_CLNT_ID);//BD_DEST_CUST_CLNT_ID
			pst.setString( 6, BD_DEST_CLNT_NAME);//BD_DEST_CLNT_NAME
			pst.setString( 7, BD_DEST_CLNT_RFC);//BD_DEST_CLNT_RFC
			pst.setString( 8, BD_DEST_COUNTRY);//BD_DEST_COUNTRY
			pst.setString( 9, BD_DEST_CITY);//BD_DEST_CITY
			pst.setString(10, BD_DEST_ESTATE);//BD_DEST_ESTATE
			pst.setString(11, BD_DEST_STRT_NAME);//BD_DEST_STRT_NAME
			pst.setString(12, BD_DEST_DRNR);//BD_DEST_DRNR
			pst.setString(13, BD_DEST_CLNY);//BD_DEST_CLNY
			pst.setString(14, BD_DEST_ZIP);//BD_DEST_ZIP
			pst.setString(15, BD_DEST_EMAIL);//BD_DEST_EMAIL
			pst.setString(16, BD_DEST_PHONE);//BD_DEST_PHONE
			pst.setString(17, BD_DEST_REFDOM);//BD_DEST_REFDOM
			pst.setInt   (18, BD_DEST_QUNT);//BD_DEST_QUNT
			pst.setString(19, BD_DEST_TIPBULTO);//BD_DEST_TIPBULTO
			pst.setString(20, BD_DEST_CONT);//BD_DEST_CONT
			pst.setString(21, BD_DEST_SLAB_NO);//BD_DEST_SLAB_NO
			pst.setDouble(22, Double.parseDouble(row.getPeso().trim()));//BD_DEST_WEIGHT
			pst.setDouble(23, Double.parseDouble(row.getVolumen().trim()));//BD_DEST_VOLUME
			pst.setDouble(24, Double.parseDouble(row.getLargo().trim()));//BD_DEST_LONG
			pst.setDouble(25, Double.parseDouble(row.getAncho().trim()));//BD_DEST_WIDTH
			pst.setDouble(26, Double.parseDouble(row.getAlto().trim()));//BD_DEST_HIGH
			pst.setString(27, BD_SRVC_DLVY_TYPE);//BD_SRVC_DLVY_TYPE
			pst.setString(28, BD_SRVC_PAID_MODE);//BD_SRVC_PAID_MODE
			pst.setString(29, BD_SRVC_ACK);//BD_SRVC_ACK
			pst.setDouble(30, Double.parseDouble(row.getCod().trim()));//BD_SRVC_COD
			pst.setDouble(31, Double.parseDouble(row.getValorDeclarado().trim()));//BD_SRVC_DCLR_VAL
			pst.setString(32, BD_SRVC_COVERAGE);//BD_SRVC_COVERAGE
			pst.setString(33, BD_SRVC_COMT);//BD_SRVC_COMT
			pst.setString(34, BD_SRVC_REFR);//BD_SRVC_REFR
			pst.setString(35, BD_CUST_AGNT);// BD_CUST_AGNT
			pst.setString(36, BD_PEDI_NUM);//BD_PEDI_NUM
			pst.setString(37, BD_CCOSTO);//BD_CCOSTO
			pst.setString(38, row.getCveMensaje());//BD_MSGE_ID
			pst.setString(39, row.getTipoMensaje());//BD_MSGE_TYPE
			pst.setString(40, row.getDesMensaje());//BD_MSGE_DES
			pst.setString(41, row.getRastreo());//BD_GUIA_NO
			pst.setString(42, row.getGuia());//BD_FORM_NO
			double importe =0;
			try {importe = Double.parseDouble(row.getImporte());} catch(Exception e) {importe = 0;}
			pst.setDouble(43, importe);//BD_TOTAL
			cadenaImpresion = (String) row.getCadeImpresion();
			pst.setString(44, cadenaImpresion);//BD_PRINT_CODE							
			pst.setString(45, "C");					  //BD_STUS_FLAG
			pst.setString(46, global.getOrigenUserClave());//CRTD_BY
			pst.setString(47, global.getOrigenUserClave());//MDFD_BY
			pst.setString(48, BD_GUIAS_RETURN);//BD_GUIAS_RETURN
			pst.setString(49, BD_SHIP_TYPE);//BD_SHIP_TYPE//AAP25
			pst.setString(50, BD_PRODUCT_ID);//CLAVE DE PRODUCTO SAT

			reg = pst.executeUpdate();
			
			con.commit();
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0, concatena.length()).append(msgErr).append("setRowSheetDtl()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.cerrarPreparedStatement(pst);
		}
		return reg;
	}	
	
	public int updateRowSheetDtl(Connection con, Global global, String fileName, ReadxlDTO row, int rowIndex) {
		PreparedStatement pst = null;
		String query = null;
		int reg = 0;
		try {
			query = concatena.delete(0, concatena.length())
					.append("UPDATE WEB_BACH_DETL_GUIA ")
					.append("SET BD_MSGE_ID = ?, BD_MSGE_TYPE = ?, BD_MSGE_DES = ?, BD_GUIA_NO = ?, BD_FORM_NO = ?, ")
					.append("BD_TOTAL = ?, BD_PRINT_CODE = ?, BD_STUS_FLAG = ?, MDFD_ON = SYSDATE, MDFD_BY = ? ")
				    .append("WHERE BD_CLNT_ID = ? AND BD_USER_ID = ? AND BD_FILE_NAME = ? AND BD_TEXT_ROW_NO = ? ").toString();			
			pst = con.prepareStatement(query);

			pst.setString(1, row.getCveMensaje());//BD_MSGE_ID
			pst.setString(2, row.getTipoMensaje());//BD_MSGE_TYPE
			pst.setString(3, row.getDesMensaje());//BD_MSGE_DES
			pst.setString(4, row.getRastreo());//BD_GUIA_NO
			pst.setString(5, row.getGuia());//BD_FORM_NO
			double importe =0;
			try {importe = Double.parseDouble(row.getImporte());} catch(Exception e) {importe = 0;}
			pst.setDouble(6, importe);//BD_TOTAL
			pst.setString(7, "");
			pst.setString(8, "P");					  //BD_STUS_FLAG
			pst.setString(9, global.getOrigenUserClave());//MDFD_BY
			pst.setString( 10, global.getClientId());//BD_CLNT_ID
			pst.setString( 11, global.getOrigenUserClave());//BD_USER_ID
			pst.setString( 12, fileName);	//BD_FILE_NAME
			pst.setInt   ( 13, rowIndex); //BD_TEXT_ROW_NO
			reg = pst.executeUpdate();
			
			con.commit();
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0, concatena.length()).append(msgErr).append("setRowSheetDtl()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.cerrarPreparedStatement(pst);
		}
		return reg;
	}	
	
	public int updateFinRowSheetDtl(Connection con, Global global, String fileName, String status) {
		PreparedStatement pst = null;
		String query = null;
		int reg = 0;
		try {

			query = concatena.delete(0, concatena.length())
					.append("UPDATE WEB_BACH_DETL_GUIA ")
					.append("SET BD_STUS_FLAG = ?, MDFD_ON = SYSDATE, MDFD_BY = ? ")
				    .append("WHERE BD_CLNT_ID = ? AND BD_USER_ID = ? AND BD_FILE_NAME = ?")
				    .toString();
			pst = con.prepareStatement(query);
			pst.setString(1, status);					  //BD_STUS_FLAG
			pst.setString(2, global.getOrigenUserClave());//MDFD_BY
			pst.setString(3, global.getClientId());//BD_CLNT_ID
			pst.setString(4, global.getOrigenUserClave());//BD_USER_ID
			pst.setString(5, fileName);	//BD_FILE_NAME
			reg = pst.executeUpdate();
			con.commit();
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0, concatena.length()).append(msgErr).append("setRowSheetDtl()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.cerrarPreparedStatement(pst);
		}
		return reg;
	}
	
	public int updateFinSheetMstr(Connection con, Global global, String fileName, String status) {
		PreparedStatement pst = null;
		String query = null;
		int reg = 0;
		try {
			query = concatena.delete(0, concatena.length())
			.append("UPDATE WEB_BACH_MSTR ")
			.append("SET BM_STUS_FLAG = ?, MDFD_ON = SYSDATE, MDFD_BY = ? ")
			.append("WHERE BM_CLNT_ID = ? AND BM_USER_ID = ? AND BM_FILE_NAME = ?").toString();			
			pst = con.prepareStatement(query);
			
			pst.setString(1, status);
			pst.setString(2, global.getOrigenUserClave());
			pst.setString(3, global.getClientId());
			pst.setString(4, global.getOrigenUserClave());
			pst.setString(5, fileName);
			reg = pst.executeUpdate();
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0, concatena.length()).append(msgErr).append("setSheetMstr()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.cerrarPreparedStatement(pst);
		}
		return reg;
	}
	
	public String findFilesP(Connection con, Global global, String status) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		String query = null;
		//int reg = 0;
		String fileName = "";
		try {
			query = concatena.delete(0, concatena.length())
			.append("SELECT BM_FILE_NAME, BM_PRNT_FNAME FROM WEB_BACH_MSTR  ")			
			.append("WHERE BM_CLNT_ID = ? AND BM_USER_ID = ? AND BM_STUS_FLAG = ? AND CRTD_ON >= SYSDATE - 10").toString();	

			pst = con.prepareStatement(query);			
			pst.setString(1, global.getClientId());
			pst.setString(2, global.getOrigenUserClave());
			pst.setString(3, status);			

			rs = pst.executeQuery();
			
			if (rs.next()) {
				fileName = rs.getString(1);
			}
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0, concatena.length()).append(msgErr).append("findFilesP()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.closeResources(rs, pst);
		}
		return fileName;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public HashMap getFilesPInfo(Connection con, Global global, String fileName, int index) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		String query = null;
		String acuse = ""; 
		HashMap result = new HashMap();
		try {
			query = concatena.delete(0, concatena.length())
			.append("SELECT BD_MSGE_ID, BD_MSGE_TYPE, BD_MSGE_DES, BD_GUIA_NO, BD_FORM_NO, BD_GUIAS_RETURN, BD_SRVC_ACK FROM WEB_BACH_DETL_GUIA ")			
			.append("WHERE BD_CLNT_ID = ? AND BD_USER_ID = ? AND BD_FILE_NAME = ? AND BD_TEXT_ROW_NO = ?").toString();	
			
			pst = con.prepareStatement(query);			
			
			pst.setString(1, global.getClientId());
			pst.setString(2, global.getOrigenUserClave());
			pst.setString(3, fileName);
			pst.setInt(4, index);

			rs = pst.executeQuery();
			
			if (rs.next()) {
				result.put("msgeId", rs.getString(1) == null ? "" : rs.getString(1));
				result.put("msgeType", rs.getString(2) == null ? "" : rs.getString(2));
				result.put("msgeDes", rs.getString(3) == null ? "" : rs.getString(3));
				result.put("guiaNo", rs.getString(4) == null ? "" : rs.getString(4));
				result.put("formNo", rs.getString(5) == null ? "" : rs.getString(5));
				result.put("guiasRetorno", rs.getString(6) == null ? "" : rs.getString(6));
				acuse = rs.getString(7) == null ? "" : rs.getString(7);
				
				if (acuse.equals("X") && (result.get("guiaNo")!= null && result.get("guiaNo").toString().length()>0)) {
							result.put("guiaNoXT", noGuiaXT(con, result.get("guiaNo").toString())); 	
				}else {result.put("guiaNoXT", ""); }
			}
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0, concatena.length()).append(msgErr).append("getFilesPInfo()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.closeResources(rs, pst);
		}
		return result;
	}
	
	public String noGuiaXT (Connection con, String noGuia) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		String acuse = ""; 
		try {
			String query = "SELECT GX_GUIA_RET FROM BOK_GUIA_ACK_XT WHERE GX_GUIA_NO = ?";
			pst = con.prepareStatement(query);			
			pst.setString(1, noGuia);//Se inserta numero de guia
			rs = pst.executeQuery();
			if (rs.next()) {acuse = rs.getString(1) == null ? "" : rs.getString(1);}	
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0, concatena.length()).append(msgErr).append("getFilesPInfo()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.closeResources(rs, pst);
		}
		return acuse; 
	}
	
	public int saveGuiasReturn(Connection con, Global global, String fileName, ReadxlDTO rowDTO, DataResponse dataResponseGuiasRetorno) {
		int result = 0;
		RastreoRetorno[] rastreosRetorno = null;
		RastreoRetorno rastreoRetorno = null;
		try {
			if (dataResponseGuiasRetorno!= null ){
				if (dataResponseGuiasRetorno.getRetornoSolicitud() != null) {
					if (dataResponseGuiasRetorno.getRetornoSolicitud().getRastreosRetorno()!= null){
						for (int indexGuia = 0; indexGuia<dataResponseGuiasRetorno.getRetornoSolicitud().getRastreosRetorno().length;indexGuia++) {
							rastreosRetorno = dataResponseGuiasRetorno.getRetornoSolicitud().getRastreosRetorno();
							rastreoRetorno = rastreosRetorno[indexGuia];						
							
							if (rastreoRetorno.getCadenaImpresion() != null) {
								saveGuiaReturn(con, global, fileName, rowDTO, rastreoRetorno);	
							}													
						}
					}
				}
			}
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0, concatena.length()).append(msgErr).append("saveGuiasReturn()_Error:").append(e).toString());
			e.printStackTrace();
		}
		return result;
	}
	
	public int saveGuiaReturn(Connection con, Global global, String fileName, ReadxlDTO row, RastreoRetorno rastreoRetorno) {
		PreparedStatement pst = null;
		String query = null;
		int reg = 0;	
		try {
			query = concatena.delete(0, concatena.length())
				.append("Insert into WEB_BACH_DETL_GUIA_RET")
				.append("(BR_GUIA_NO, BR_GUIA_NO_RET, BR_FORM_NO, BR_FORM_NO_RET, BR_CLNT_ID, BR_USER_ID, BR_FILE_NAME, ")
				.append("BR_MSGE_ID, BR_MSGE_TYPE, BR_MSGE_DES, BR_PRINT_CODE, BR_STUS_FLAG, CTRD_ON, CRTD_BY, MDFD_ON, MDFD_BY)")				    
				.append("Values ")
				.append("(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, SYSDATE, ?, SYSDATE, ?)").toString();				
			
			pst = con.prepareStatement(query);			
			
			pst.setString( 1, row.getRastreo());//BD_CLNT_ID 
			pst.setString( 2, rastreoRetorno.getRastreoNo());//BD_USER_ID 
			pst.setString( 3, row.getGuia());	//BD_FILE_NAME 
			pst.setString( 4, rastreoRetorno.getGuiaNo()); //BD_TEXT_ROW_NO
			pst.setString( 5, global.getClientId());//BD_DEST_CUST_CLNT_ID
			pst.setString( 6, global.getOrigenUserClave());//BD_DEST_CLNT_NAME
			pst.setString( 7, fileName);//BD_DEST_CLNT_RFC
			pst.setInt   ( 8, rastreoRetorno.getMensajes()[0].getCveMsjeRetorno());//BD_DEST_COUNTRY
			pst.setString( 9, rastreoRetorno.getMensajes()[0].getTipoMsje());//BD_DEST_CITY
			pst.setString(10, rastreoRetorno.getMensajes()[0].getDesMsjeRetorno());//BD_DEST_ESTATE
			pst.setString(11, "");//BD_DEST_STRT_NAME
			pst.setString(12, "P");//BD_DEST_DRNR
			pst.setString(13, global.getOrigenUserClave());//BD_DEST_CLNY
			pst.setString(14, global.getOrigenUserClave());//BD_DEST_ZIP
			
			reg = pst.executeUpdate();
			con.commit();
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0, concatena.length()).append(msgErr).append("saveGuiaReturn()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.cerrarPreparedStatement(pst);
		}
		return reg;
	}	
	
	public String countGuiaDocumentsxls(Connection con, String file) {
		String guias ="" ; 
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			String query =  "SELECT COUNT(*) FROM WEB_BACH_DETL_GUIA WHERE BD_FILE_NAME = ? and BD_GUIA_NO is not null";
			ps = con.prepareStatement(query);
			ps.setString(1, file);
			
			rs = ps.executeQuery();
			while(rs.next()){
				guias = rs.getString(1) ;	
			}
		} catch (Exception e) {
			AccessLog.Log(e);
			e.printStackTrace();
		} finally {
			resources.closeResources(rs, ps);
		}
		return guias;
	}
	
	@SuppressWarnings("unchecked")
	public String getXlsMainServerPath(Connection con) {
		boolean isLocalConn = false;
		try {
			if (con == null) {
				con = ConnectDB.getConnection();
				isLocalConn = true;
			}
			ConsultaParametros consulta = new ConsultaParametros();
			List<ArrayList<String>> result = consulta.QryMdulTypeParm1(con, "SYS", "URL", "XLS_PATH_SERVER");
			return result.get(0).get(4); // File path with ip
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (isLocalConn) {
				resources.cerrarConexion(con);
			}
		}
		return ConnectDB.getPathFileXls();
	}
}
