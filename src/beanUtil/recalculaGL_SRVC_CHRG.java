package beanUtil;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;




public class recalculaGL_SRVC_CHRG {

	/**
	 * @param args
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void main(String[] args) {
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;	
				
		
		ArrayList result = new ArrayList();		
		ArrayList mainList = new ArrayList();
		try {
			String rastreo = "";
			String rastreo_bk = "";
			String totalValue = "";
			
			con = ConnectDB.getConnection();
			
			//String query = "SELECT D_RASTREO FROM EXT_RESPALDO where d_rastreo='01134977083' GROUP BY D_RASTREO";21135008003
			//String query = "SELECT D_RASTREO FROM EXT_RESPALDO GROUP BY D_RASTREO";
			//String query = "SELECT GL_GUIA_NO FROM RECOVER_DATAT GROUP BY GL_GUIA_NO";
			//String query = "SELECT GL_GUIA_NO FROM RECOVER_DATAT_neg WHERE GL_GUIA_NO = '23134991244' GROUP BY GL_GUIA_NO";
			String query = "SELECT GL_GUIA_NO FROM RECOVER_DATAT_neg GROUP BY GL_GUIA_NO";
			//String query = "SELECT GL_GUIA_NO FROM RECOVER_DATAT_SHP_E GROUP BY GL_GUIA_NO";
			
			
			pst =  con.prepareStatement(query);
			rs =  pst.executeQuery();
			while (rs.next()) {
				rastreo_bk =  rs.getString(1) == null ? "" : rs.getString(1);				
				mainList.add(rastreo_bk);			
			}
			
			rs.close();
			pst.close();
			System.out.println("mainList "+mainList);
			int tarifat7 = 0;
			int portalWeb = 0;
			int servicioWeb = 0;
			String kmTarifType = "";
			for (int i=0;i<mainList.size();i++){
				rastreo = mainList.get(i).toString();
				if (isPortalWB(con, rastreo)) {
					portalWeb ++;
					//System.out.println(rastreo+" SI es de portal WB");
					//if (validaT7(con, rastreo)) {
						//System.out.println(rastreo+" SI tiene tarifa T7");
						//tarifat7++;
					//} else {
						//System.out.println(rastreo+" NO tiene tarifa T7");
						kmTarifType = calculateKMTarifType(con,rastreo);
						System.out.println("kmTarifType "+kmTarifType);
						result = getInfoBokGuiaSrvcItem(con, rastreo);
						System.out.println("result "+result);				
						for (int j =0; j<result.size();j++){
							
							totalValue = new java.text.DecimalFormat("0.00").format(
									
							
									getServiceAmount(con, 
											rastreo, 
											((HashMap)result.get(j)).get("GL_REFR_SRVC_ID").toString(), 
											((HashMap)result.get(j)).get("GL_SRVC_ID").toString(),
											((HashMap)result.get(j)).get("GL_QUNT").toString(), 
											kmTarifType, 
											((HashMap)result.get(j)).get("GL_SLAB_NO").toString(), 
											((HashMap)result.get(j)).get("peso").toString(), 
											((HashMap)result.get(j)).get("volumen").toString())
									
									);
							
							System.out.println("totalValue "+totalValue);
							
							recalculateGuiasSrvcChrg(con, rastreo, 
									((HashMap)result.get(j)).get("GL_SRVC_ID").toString(),
									totalValue, 
									((HashMap)result.get(j)).get("GL_REFR_SRVC_ID").toString(), 
									((HashMap)result.get(j)).get("GL_DESC").toString(), 
									((HashMap)result.get(j)).get("GL_SLAB_NO").toString());
							
							
						}
						result.clear();
						
						
					//}FINAL VALIDACION T7
				} else {
					//System.out.println(rastreo+" NO es de portal WB");
					servicioWeb++;
				}
			}
			System.out.println("total rastreos tarifa t7 "+tarifat7);
			System.out.println("total rastreos portalWeb "+portalWeb);
			System.out.println("total rastreos servicioWeb "+servicioWeb);
			System.out.println("mainList.size() "+mainList.size());
			

			/*rs.close();
			pst.close();*/
			con.commit();
			//con.rollback();
			//con.close();
		} catch (Exception e) {
			System.out.println("main()_Error1: "+e);
			e.printStackTrace();
			try {
				con.rollback();
			} catch (Exception e2) {
				System.out.println("main()_Error2: "+e2);
				e2.printStackTrace();
			}
			
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pst != null)
					pst.close();
				if (con!= null)
					con.close();
			} catch (Exception e2) {
				System.out.println("main()_Error3: "+e2);
				e2.printStackTrace();
			}
			
		}
	}	
	
	private static int recalculateGuiasSrvcChrg(Connection con, String rastreo, String GS_SRVC_ID, String totalValue, String GL_REFR_SRVC_ID, String GL_DESC, String GL_SLAB_NO) {
		int reg = 0;
		PreparedStatement pst = null;
		PreparedStatement pst2 = null;
		ResultSet rs = null;
		try {
			String query = 
					"UPDATE BOK_GUIA_SRVC_ITEM "+
					"SET GL_SRVC_CHRG = ? "+
					"WHERE GL_GUIA_NO = ? "+
					"AND GL_REFR_SRVC_ID = ?" +
					"AND GL_DESC = ?" +
					"AND GL_SLAB_NO = ?";
			
			System.out.println(
					"actualiza\n totalValue: "+totalValue+
					"\n rastreo: "+rastreo+
					"\n GS_SRVC_ID: "+GS_SRVC_ID+
					"\n GL_REFR_SRVC_ID: "+GL_REFR_SRVC_ID +
					"\n GL_DESC: "+GL_DESC +
					"\n GL_SLAB_NO: "+GL_SLAB_NO
					);
			

			pst2 = con.prepareStatement(query);
			
			pst2.setString(1, totalValue);
			pst2.setString(2, rastreo);
			
			//pst2.setString(3, rowId);
			
			pst2.setString(3, GL_REFR_SRVC_ID);
			pst2.setString(4, GL_DESC);
			pst2.setString(5, GL_SLAB_NO);
		
			reg = pst2.executeUpdate();
			
			pst2.close();
			//System.out.println("numero de actualizaciones "+reg);
			
		} catch (Exception e) {
			System.out.println("recalculateGuiasSrvcChrg()_Error: "+e);
			e.printStackTrace();
			try {
				con.rollback();				
			} catch (Exception e2) {
				System.out.println("recalculateGuiasSrvcChrg()_Error: "+e2);
			}
			
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pst != null)
					pst.close();
				if (pst2 != null)
					pst2.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}			
		}
		return reg;		
	}	
	
	private static boolean validaT7(Connection con, String rastreo) {
		
		PreparedStatement pst = null;
		PreparedStatement pst2 = null;
		ResultSet rs = null;
		boolean valT7 = false;
		try {
			String query = 
					"SELECT GL_GUIA_NO FROM BOK_GUIA_SRVC_ITEM WHERE GL_GUIA_NO = ? and GL_SLAB_NO = ?";	

			pst2 = con.prepareStatement(query);
			
			pst2.setString(1, rastreo);
			pst2.setString(2,"T7");
		
			rs = pst2.executeQuery();
			
			if (rs.next()) {
				valT7 = true;
			}			
						
		} catch (Exception e) {
			System.out.println("validaT7()_Error: "+e);
			e.printStackTrace();						
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pst != null)
					pst.close();
				if (pst2 != null)
					pst2.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}			
		}
		return valT7;		
	}
private static boolean isPortalWB(Connection con, String rastreo) {
		
		PreparedStatement pst = null;
		PreparedStatement pst2 = null;
		ResultSet rs = null;
		boolean valT7 = false;
		try {
			String query = 
					"SELECT GS_GUIA_NO FROM BOK_GUIA_STUS WHERE GS_GUIA_NO = ? AND GS_STUS_CODE = ?";	

			pst2 = con.prepareStatement(query);
			
			pst2.setString(1, rastreo);
			pst2.setString(2,"WBK");
		
			rs = pst2.executeQuery();
			
			if (rs.next()) {
				valT7 = true;
			}			
						
		} catch (Exception e) {
			System.out.println("isPortalWB()_Error: "+e);
			e.printStackTrace();						
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pst != null)
					pst.close();
				if (pst2 != null)
					pst2.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}			
		}
		return valT7;		
	}

//private static boolean isDescuentosPortalWB(Connection con, String rastreo) {
//	
//	PreparedStatement pst = null;
//	PreparedStatement pst2 = null;
//	ResultSet rs = null;
//	boolean valT7 = false;
//	try {
//		String query = 
//				"SELECT GS_GUIA_NO FROM BOK_GUIA_STUS WHERE GS_GUIA_NO = ? AND GS_STUS_CODE = ?";	
//
//		pst2 = con.prepareStatement(query);
//		
//		pst2.setString(1, rastreo);
//		pst2.setString(2,"WBK");
//	
//		rs = pst2.executeQuery();
//		
//		if (rs.next()) {
//			valT7 = true;
//		}			
//					
//	} catch (Exception e) {
//		System.out.println("isDescuentosPortalWB()_Error: "+e);
//		e.printStackTrace();						
//	} finally {
//		try {
//			if (rs != null)
//				rs.close();
//			if (pst != null)
//				pst.close();
//			if (pst2 != null)
//				pst2.close();
//		} catch (Exception e2) {
//			e2.printStackTrace();
//		}			
//	}
//	return valT7;		
//}
@SuppressWarnings({ "rawtypes", "unchecked" })
private static ArrayList getInfoBokGuiaSrvcItem(Connection con, String rastreo) {
		
		PreparedStatement pst = null;
		PreparedStatement pst2 = null;
		ResultSet rs = null;
		
		HashMap row = new HashMap(4);
		ArrayList result = new ArrayList();
		try {
			String query = "SELECT GL_REFR_SRVC_ID, GL_SLAB_NO, GL_SRVC_CHRG, GL_QUNT, GL_SRVC_ID, GL_VLUE_1, GL_VLUE_2, GL_DESC FROM BOK_GUIA_SRVC_ITEM WHERE GL_GUIA_NO = ? and GL_REFR_SRVC_ID = ? ";

			pst2 = con.prepareStatement(query);
			
			pst2.setString(1, rastreo);
			pst2.setString(2,"SHP-G");
		
			rs = pst2.executeQuery();
			
			while (rs.next()) {				
				row.put("rastreo", rastreo);
				row.put("GL_REFR_SRVC_ID", rs.getString(1) == null ? "" : rs.getString(1));
				row.put("GL_SLAB_NO", rs.getString(2) == null ? "" : rs.getString(2));
				row.put("GL_SRVC_CHRG", rs.getString(3) == null ? "" : rs.getString(3));
				row.put("GL_QUNT", rs.getString(4) == null ? "" : rs.getString(4));
				row.put("GL_SRVC_ID", rs.getString(5) == null ? "" : rs.getString(5));
				row.put("peso", rs.getString(6) == null ? "" : rs.getString(6));
				row.put("volumen", rs.getString(7) == null ? "" : rs.getString(7));
				row.put("GL_DESC", rs.getString(8) == null ? "" : rs.getString(8));
				
				result.add(row.clone());
				row.clear();
			}			
						
		} catch (Exception e) {
			System.out.println("getInfoBokGuiaSrvcItem()_Error: "+e);
			e.printStackTrace();						
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pst != null)
					pst.close();
				if (pst2 != null)
					pst2.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}			
		}
		return result;		
	}

	private static String calculateKMTarifType(Connection con, String rastreo) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		String query = "";
		String kmTarifType="";
		String siteOrigen = "";
		String siteDestino = "";
		
		try {
			query = "select SUBSTR(GH_ORGN_BRNC_ID,1,3), SUBSTR(GH_DEST_BRNC_ID,1,3) from bok_guia_head where gh_guia_no = ?";
			pst = con.prepareStatement(query);
			pst.setString(1,rastreo);
			//pst.setString(2,aform.getDestinationcode());--commented and below line added by B.Emerson on 06/04/2004
			
			rs = pst.executeQuery();
			
			if (rs.next()){
				siteOrigen = rs.getString(1) == null ? "" : rs.getString(1);
				siteDestino = rs.getString(2) == null ? "" : rs.getString(2);
			}
			//System.out.println("siteOrigen "+siteOrigen);
			//System.out.println("siteDestino "+siteDestino);
			rs.close();
			pst.close();
			
			query = "select pack_web.FUN_CHK_ACTV_TRIF(?,?) from dual";
			pst = con.prepareStatement(query);
			pst.setString(1,siteOrigen);
			//pst.setString(2,aform.getDestinationcode());--commented and below line added by B.Emerson on 06/04/2004
			pst.setString(2,siteDestino);
			rs = pst.executeQuery();
						
			if(rs.next()){	
				//AAP//System.out.println("entro a registro kmtarif");
				kmTarifType = rs.getString(1);
			}		
			//AAP//AccessLog.Log(concatena.delete(0,concatena.length()).append(msgAvi).append("calculateKMTarifType()Inside km tarif type ==> ").append(kmTarifType).toString());
			//System.out.println("kmTarifType "+kmTarifType);
			//session.setAttribute("sGlobal",global);			
		} catch (Exception e) {
			System.out.println("calculateKMTarifType()_Error:"+e);
			e.printStackTrace();
		}finally{
			try {
				if (rs != null)
					rs.close();
				if (pst != null)
					pst.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}	
		}
		return kmTarifType;
	}
	
	
	/****************************************************************************************
	 * metodo que ejecuta funcion que retorna tarifa de piso								*
	 * **************************************************************************************/
	private static double getServiceAmount(Connection con, String rastreo, String Ss_srvc_id, String refr_srvc_id, String cantidadBultos, 
			String kmTarifType, String tarifa, String peso, String volumen) {
		double returnvalue = 0.0f;
		CallableStatement cst = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String siteOrigen = "";
		String siteDestino = "";
		String clienteOrigen = "";
		
		try {
			
			String query = "select SUBSTR(GH_ORGN_BRNC_ID,1,3), SUBSTR(GH_DEST_BRNC_ID,1,3), GH_ORGN_CLNT_ID from bok_guia_head where gh_guia_no = ?";
			pst = con.prepareStatement(query);
			pst.setString(1,rastreo);
			//pst.setString(2,aform.getDestinationcode());--commented and below line added by B.Emerson on 06/04/2004
			
			rs = pst.executeQuery();
			
			if (rs.next()){
				siteOrigen = rs.getString(1) == null ? "" : rs.getString(1);
				siteDestino = rs.getString(2) == null ? "" : rs.getString(2);
				clienteOrigen = rs.getString(3) == null ? "" : rs.getString(3);
			}
			System.out.println("getServiceAmount() siteOrigen "+siteOrigen);
			System.out.println("getServiceAmount() siteDestino "+siteDestino);
			rs.close();
			pst.close();
			
			//String query = "Begin ? := pack_sipweb.ftch_servc_tarif(?,?,?,?,?,?,?,?,?,?); End; ";//AAP03
			 query = "Begin ? := PACK_TARIFAS.FTCH_SERVC_TARIF_PISO(?,?,?,?,?,?,?,?,?,?); End; ";//AAP03
			
			cst = con.prepareCall(query);
			
			cst.registerOutParameter(1, Types.NUMERIC);
			/*cst.setString(2, aform.getSs_srvc_id());
			cst.setString(3, aform.getSs_refr_srvc_id());*/
			cst.setString(2, Ss_srvc_id);
			cst.setString(3, refr_srvc_id);
			cst.setInt(6, Integer.parseInt(cantidadBultos));
			cst.setString(7, siteOrigen);
			cst.setString(8, siteDestino);
			cst.registerOutParameter(10, Types.NUMERIC);
			
			//AAP//AccessLog.Log(concatena.delete(0,concatena.length()).append(msgAvi).append("getServiceAmount()THE VALUE OF KM TARIF TYPE ").append(global.kmTarifType).toString());
			
			if (kmTarifType == null) {
				cst.setNull(11, Types.NUMERIC);
			} else {
				cst.setDouble(11, Double.parseDouble(kmTarifType));
			}

			if (Ss_srvc_id.equalsIgnoreCase("SHP-E")) {
				cst.setString(4, "NON");
				// cst.setDouble(5,0.0d);//Empty
				cst.setNull(5, Types.NUMERIC);
				cst.setString(9, tarifa);
			} else {
				cst.setString(4, "KG");
				cst.setDouble(5, Double.parseDouble(peso));
				if (tarifa.equalsIgnoreCase("T7")) {
					cst.setString(9, "T7P");
				} else {
					cst.setString(9, tarifa);
				}
			}
			
			cst.executeQuery();

			double weight = cst.getDouble(1);
			returnvalue = weight;
			double volume = 0.0f;
			if (Ss_srvc_id.equalsIgnoreCase("SHP-G") && tarifa.equalsIgnoreCase("T7")) {
				cst.setString(4, "CUM");
				cst.setDouble(5, Double.parseDouble(volumen));
				cst.setString(9, "T7V");
				cst.executeQuery();
				volume = cst.getDouble(1);

				if (weight > volume) {
					returnvalue = weight;
					//aform.setCalculationdone("weight");
				} else {
					returnvalue = volume;
					//aform.setCalculationdone("volume");
				}
			} else if (Ss_srvc_id.equalsIgnoreCase("SHP-G")) {
				/*aplica regla para descontar el 3% al flete SHP-G cuando el cliente haya sido creado antes del 2015 (NO APLICA PARA T7)*///AAP03
				query = "SELECT CRTD_ON FROM SYS_CLNT_MSTR WHERE CM_CLNT_ID = ?";
				
				pst = con.prepareStatement(query);
				pst.setString(1, clienteOrigen);
				
				rs = pst.executeQuery();
				
				Calendar fechaCreacion = Calendar.getInstance();
				if (rs.next()) {
					fechaCreacion.setTime(rs.getDate(1));
				}
				
				rs.close();
				pst.close();
				
				/*verifica que tenga fecha de creacion antes del 2015*///AAP03
				if (fechaCreacion!= null && fechaCreacion.get(fechaCreacion.YEAR)<2015) {//AAP03
					
					query = "SELECT * FROM SYS_CLNT_SRVC WHERE CS_CLNT_ID = ? AND CS_SRVC_ID = ?";
					
					pst = con.prepareStatement(query);
					pst.setString(1, clienteOrigen);
					pst.setString(2, "PACKETS");
					
					rs = pst.executeQuery();
					
					/*verifica que tenga descuentos registrados para aplicar un descuento del 3% adicional (incremento de 5% a tarifario 2014)*///AAP03
					if (rs.next()) {//AAP03
						Double descuento = returnvalue * .03;
						returnvalue = returnvalue - descuento;						
					}
				}				
			}			
			//AAP//AccessLog.Log(concatena.delete(0,concatena.length()).append(msgAvi).append("getServiceAmount()THE SERVICE AMOUNT RETURNED ").append(cst.getDouble(10)).toString());
			//AAP//AccessLog.Log(concatena.delete(0,concatena.length()).append(msgAvi).append("getServiceAmount()THE VALUE RETURNED FOR SERVICE AMOUNT ").append(returnvalue).toString());
		} catch (Exception e) {
			System.out.println("getServiceAmount()_Error:"+e);
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
		return returnvalue;
	}		
}