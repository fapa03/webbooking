/**
 * @author: Jose Manuel Armenta
 * Fecha de Creación: 10/07/2017
 * Compañía: PAQUETEXPRESS.
 * Descripción del programa: Bean DAO para pantalla reimpresion de guias.
 * FileName: JavReprintGuiaDao.java
 * SessionVariables:
 * Other Used Classes:
 * Function Names: 
 * ----------------------------------------------------------------- 
 * MODIFICACIONES:  
 * -----------------------------------------------------------------
 * Clave: AAP01
 * Autor: ABRAHAM DANIEL ARJONA PERAZA.
 * Fecha: 03/12/2018
 * Descripción: Se agregan columas de fecha (ISSE_DATE) y referencias. 
 * Funcionalidad para marcar si quiere imprimir la cantidad de CP configuradas por cliente
 * ------------------------------------------------------------------
 * Clave: 
 * Autor: 
 * Fecha: 
 * Descripción: 
 * ------------------------------------------------------------------
 * Clave: 
 * Autor: 
 * Fecha: 
 * Descripción: 
 * 
 * ------------------------------------------------------------------ 
 * Clave: 
 * Autor: 
 * Fecha: 
 * Descripción: 
 * 
 * ------------------------------------------------------------------ 
 */
package mx.com.paquetexpress.reprintGuia.dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import bean.Resources;
import beanUtil.ConnectDB;
import logger.AccessLog;
import mx.com.paquetexpress.reprintGuia.dto.JavReprintEtiquetaDTO;
import mx.com.paquetexpress.reprintGuia.dto.JavReprintGuiaDTO;
import mx.com.paquetexpress.reprintGuia.form.JavReprintGuiaForm;

public class JavReprintGuiaDao {
	private StringBuffer cnct = new StringBuffer();
	private Connection con;
	private final String msgErr = new StringBuffer("MSERR_B_").append(this.getClass().getName()).append(".").toString();
	private Resources resources = new Resources();
	//private final String msgAvi = new StringBuffer("MSAVI_B_").append(this.getClass().getName()).append(".").toString();
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public JavReprintGuiaForm getGuiasByClient(String webClientId) {		
		String strSqlQuery = "";
		PreparedStatement pst = null;
		CallableStatement cst = null;
		ResultSet rs = null;
		ArrayList listReprintGuiaDTO = new ArrayList();
		JavReprintGuiaForm reprintGuiaForm = new JavReprintGuiaForm();
		JavReprintGuiaDTO reprintGuiaDTO = new JavReprintGuiaDTO();
		try {
			con = ConnectDB.getConnection();
			strSqlQuery = cnct.delete(0, cnct.length())
					.append("SELECT GH_FORM_NO, GH_GUIA_NO, GH_DEST_BRNC_ID, GH_DEST_CLNT_NAME, to_char(GH_GUIA_AMNT, '$999990.99') GH_GUIA_AMNT, GH_NUMB_PACK, GH_ORGN_BRNC_ID, (to_char (GH_ISSE_DATE + NVL(FUN_GET_TIME_SITE(GH_ORGN_BRNC_ID),0)/86400 , 'DD/MM/YYYY HH24:MI:SS')) ISSE_DATE, FUN_GET_REFRS_GUIA(GH_GUIA_NO) REFERS ")
					.append("FROM BOK_GUIA_HEAD A ").append("WHERE A.GH_ORGN_CLNT_ID = ? ")
					.append("AND A.GH_GUIA_TYPE = ? ").append("AND A.GH_ACTV_FLAG = ? ")
					.append("AND A.GH_RAD_FLAG IN (?, ?) ")
					.append("ORDER BY GH_ISSE_DATE, GH_GUIA_NO").toString();

			pst = con.prepareStatement(strSqlQuery);
			pst.setString(1, webClientId);
			pst.setString(2, "H");
			pst.setString(3, "A");
			pst.setString(4, "1");
			pst.setString(5, "6");
			rs = pst.executeQuery();

			String strDestBranchName = "";
			while (rs.next()) {
				reprintGuiaDTO = new JavReprintGuiaDTO();
				reprintGuiaDTO.setFormNumber((rs.getString("GH_FORM_NO") == null ? "" : (rs.getString("GH_FORM_NO"))));
				reprintGuiaDTO.setGuiaNumber((rs.getString("GH_GUIA_NO") == null ? "" : (rs.getString("GH_GUIA_NO"))));
				reprintGuiaDTO
						.setGuiaAmount((rs.getString("GH_GUIA_AMNT") == null ? "" : (rs.getString("GH_GUIA_AMNT"))));
				reprintGuiaDTO.setNumPack((rs.getString("GH_NUMB_PACK") == null ? "" : (rs.getString("GH_NUMB_PACK"))));
				reprintGuiaDTO.setDestClientName(
						(rs.getString("GH_DEST_CLNT_NAME") == null ? "" : (rs.getString("GH_DEST_CLNT_NAME"))));
				reprintGuiaDTO.setOrigBranch(
						(rs.getString("GH_ORGN_BRNC_ID") == null ? "" : (rs.getString("GH_ORGN_BRNC_ID"))));

				reprintGuiaDTO.setIsseDate( rs.getString("ISSE_DATE") == null ? "" : rs.getString("ISSE_DATE") );//AAP01
				reprintGuiaDTO.setRefers( rs.getString("REFERS") == null ? "" : rs.getString("REFERS") );//AAP01
				
				String StrDestBrnachId = rs.getString("GH_DEST_BRNC_ID");

				cst = con.prepareCall("begin ? := pack_web.Fun_ftch_brnc_name(?); end;");
				cst.registerOutParameter(1, Types.VARCHAR);
				cst.setString(2, StrDestBrnachId);
				cst.execute();

				strDestBranchName = (cst.getString(1) == null ? "" : cst.getString(1));
				reprintGuiaDTO.setDestBranch(strDestBranchName);
				reprintGuiaDTO.setChecked(true);
				listReprintGuiaDTO.add(reprintGuiaDTO);
				cst.close();
			}			
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("getGuiasByClient()_Error:").append(e)
					.toString());
			e.printStackTrace();
		} finally {
			resources.closeResources(rs, pst);
			resources.cerrarCallableStatement(cst);
			resources.cerrarConexion(con);
		}
		listReprintGuiaDTO = getEtiquetasByGuia(listReprintGuiaDTO);
		reprintGuiaForm.setListGuias(listReprintGuiaDTO);
		return reprintGuiaForm;
	}

	@SuppressWarnings("rawtypes")
	private ArrayList getEtiquetasByGuia(ArrayList<JavReprintGuiaDTO> listReprintGuiaDTO) {		
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			con = ConnectDB.getConnection();

			for (int i = 0; i < listReprintGuiaDTO.size(); i++) {
				ArrayList<JavReprintEtiquetaDTO> listEtiquetas = new ArrayList<JavReprintEtiquetaDTO>();
				JavReprintGuiaDTO reprintGuiaDTO = (JavReprintGuiaDTO) listReprintGuiaDTO.get(i);

				String strSqlQuery = cnct.delete(0, cnct.length())
						.append("select GL_GUIA_NO, GL_DESC, SS_DESC, GL_CONT, GL_QUNT ")
						.append("from BOK_GUIA_SRVC_ITEM a,SYS_SHP_DESC_MSTR ").append("where GL_GUIA_NO = ? ")
						.append("AND SYS_SHP_DESC_MSTR.SS_CODE =GL_DESC ")
						.append("AND GL_REFR_SRVC_ID IN ('SHP-G','SHP-E')").toString();
				pst = con.prepareStatement(strSqlQuery);
				pst.setString(1, reprintGuiaDTO.getGuiaNumber());
				rs = pst.executeQuery();

				while (rs.next()) {
					JavReprintEtiquetaDTO etiqueta = new JavReprintEtiquetaDTO();
					etiqueta.setGuiaNo((rs.getString("GL_GUIA_NO") == null ? "" : (rs.getString("GL_GUIA_NO"))));
					etiqueta.setGuiaDescCode((rs.getString("GL_DESC") == null ? "" : (rs.getString("GL_DESC"))));
					etiqueta.setGuiaDesc((rs.getString("SS_DESC") == null ? "" : (rs.getString("SS_DESC"))));
					etiqueta.setContent((rs.getString("GL_CONT") == null ? "" : (rs.getString("GL_CONT"))));
					etiqueta.setQuantity((rs.getString("GL_QUNT") == null ? "" : (rs.getString("GL_QUNT"))));
					listEtiquetas.add(etiqueta);
					etiqueta = null;
				}
				reprintGuiaDTO.setListEtiquetas(separarEtiquetas(listEtiquetas));
				listReprintGuiaDTO.set(i, reprintGuiaDTO);
				
				if (rs!= null) {
					rs.close();
				}
				
				if (pst!= null) {
					pst.close();
				}
			}			
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("getEtiquetasByGuia()_Error:").append(e)
					.toString());
			e.printStackTrace();
		} finally {
			resources.closeResources(rs, pst);
			resources.cerrarConexion(con);
		}
		return listReprintGuiaDTO;
	}

	private ArrayList<JavReprintEtiquetaDTO> separarEtiquetas(ArrayList<JavReprintEtiquetaDTO> listEtiquetas) {

		ArrayList<JavReprintEtiquetaDTO> listAux = new ArrayList<JavReprintEtiquetaDTO>();

		int counter = 0;
		for (int i = 0; i < listEtiquetas.size(); i++) {
			JavReprintEtiquetaDTO dtoAux = (JavReprintEtiquetaDTO) listEtiquetas.get(i);
			for (int j = 1; j <= Integer.parseInt(dtoAux.getQuantity()); j++) {
				counter++;
				JavReprintEtiquetaDTO newDTO = new JavReprintEtiquetaDTO();
				newDTO.setGuiaNo(dtoAux.getGuiaNo());
				newDTO.setContent(dtoAux.getContent());
				newDTO.setGuiaDesc(dtoAux.getGuiaDesc());
				newDTO.setGuiaDescCode(dtoAux.getGuiaDescCode());
				newDTO.setQuantity(String.valueOf(counter));
				newDTO.setChecked(true);
				listAux.add(newDTO);
			}
		}
		return listAux;
	}

	public String print(String[] guias, String[] allEtiquetas, String[] checkedGuias, String[] checkedEtiquetas,
			String idClientAgreement, int SSTotal) {
		
		String cadenaImpresion = "";
		int guiasImpresas = 0;
		int etiqImpresas = 0;
		for (String guia : guias) {
			int numEtiqGuia = 0;
			boolean printAll = false;
			boolean isCheckedGuia = false;
			boolean isCheckedEtiqueta = false;
			ArrayList<String> etiquetas = new ArrayList<>();

			for (String checkedGuia : checkedGuias) {
				if (checkedGuia.equalsIgnoreCase(guia)) {
					isCheckedGuia = true;
					guiasImpresas++;
					break;
				}
			}

			if (isCheckedGuia) {
				for (String infoEtiqueta : allEtiquetas) {
					String[] info = infoEtiqueta.split("-");
					String rastreo = info[0];
					if (rastreo.equalsIgnoreCase(guia)) {
						numEtiqGuia++;
					}
				}
			}

			for (String checkedEtiqueta : checkedEtiquetas) {
				String[] etiqueta = checkedEtiqueta.split("-");
				String rastreo = etiqueta[0];
				String numEtiq = etiqueta[1];
				if (rastreo.equalsIgnoreCase(guia)) {
					isCheckedEtiqueta = true;
					etiquetas.add(numEtiq);
					etiqImpresas++;
					if (etiquetas.size() == numEtiqGuia) {
						break;
					}
				}
			}
			printAll = (isCheckedGuia && numEtiqGuia == etiquetas.size()) ? true : false;
			if (isCheckedGuia || isCheckedEtiqueta) {
				cadenaImpresion += getCadenaImpresion(guia, printAll, isCheckedGuia, etiquetas, SSTotal);
			}

			if (guiasImpresas == checkedGuias.length && etiqImpresas == checkedEtiquetas.length)
				break;
		}
		
		cadenaImpresion = addPortToPrintString(cadenaImpresion, idClientAgreement);
		return cadenaImpresion;
	}

	private String getCadenaImpresion(String rastreo, boolean printAll, boolean printGuia,
			ArrayList<String> etiquetas, int SSTotal) {
		PreparedStatement pst = null;
		ResultSet rsCad = null;
		String cadena = "";
		String cadenaSS = "";//AAP01
		Clob myClob = null;
		String query = "SELECT CP_ETIQUETA_ZPL(?, ?, ?, ?) FROM DUAL";
		try {
			con = ConnectDB.getConnection();
//			if (printAll) {
//				pst = con.prepareStatement(query);
//				pst.setString(1, rastreo);
//				pst.setString(2, "R");
//				pst.setString(3, "0");
//				pst.setString(4, "A");
//
//				rsCad = pst.executeQuery();
//
//				if (rsCad.next()) {
//					myClob = rsCad.getClob(1);
//					cadena += ClobToString(myClob);
//				}
//			} else {
				if (printGuia) {

					pst = con.prepareStatement(query);
					pst.setString(1, rastreo);
					pst.setString(2, "R");
					pst.setString(3, "0");
					pst.setString(4, "C");

					rsCad = pst.executeQuery();

					if (rsCad.next()) {
						myClob = rsCad.getClob(1);
						//cadena += ClobToString(myClob);//AAP01
						cadenaSS += ClobToString(myClob);//AAP01
					}
					
					if (rsCad!= null) {
						rsCad.close();
					}
					
					if (pst!= null) {
						pst.close();
					}
					//suma el total de copias de cartas porte
					for (int jj = 1; jj<=SSTotal; jj++) {//AAP01
						cadena += cadenaSS;//AAP01
					}//AAP01
				}
				
				
				for (String etiqueta : etiquetas) {
					pst = con.prepareStatement(query);
					pst.setString(1, rastreo);
					pst.setString(2, "R");
					pst.setString(3, etiqueta);
					pst.setString(4, "E");

					rsCad = pst.executeQuery();

					if (rsCad.next()) {
						myClob = rsCad.getClob(1);
						cadena += ClobToString(myClob);
					}
					
					if (rsCad!= null) {
						rsCad.close();
					}
					
					if (pst!= null) {
						pst.close();
					}
				}
//			}

		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("getCadenaImpresion()_Error:").append(e)
					.toString());
			e.printStackTrace();
		} finally {
			resources.closeResources(rsCad, pst);
			resources.cerrarConexion(con);
		}
		return cadena;
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
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("ClobToString()SQLException_Error:")
					.append(e).toString());
			e.printStackTrace();
		} catch (IOException e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("ClobToString()IOException_Error:")
					.append(e).toString());
			e.printStackTrace();
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("ClobToString()Exception_Error:")
					.append(e).toString());
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

	private String addPortToPrintString(String cadena, String idClientAgreement) {
		
		int x = cadena.indexOf("\n") + 1;
		String comparacion = cadena.substring(x, x + 3);
		boolean portAlreadyExists = comparacion.equalsIgnoreCase("^FX");

		PreparedStatement pst = null;
		ResultSet rs = null;
		String port = "";
		if (!portAlreadyExists) {
			try {
				con = ConnectDB.getConnection();

				String query = "SELECT WC_BAR_PORT FROM WEB_CLNT_MSTR WHERE WC_CLNT_ID= ?";
				pst = con.prepareStatement(query);
				pst.setString(1, idClientAgreement);

				rs = pst.executeQuery();

				if (rs.next()) {
					port = rs.getString(1);
				}
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("addPortToPrintString()_Error:")
						.append(e).toString());
				e.printStackTrace();
			} finally {
				resources.closeResources(rs, pst);
				resources.cerrarConexion(con);
			}
			cadena = new StringBuilder(cadena).insert(x, "^FX" + port + "^FS\n").toString();
		}
		
		return cadena;
	}
}