package bean;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import logger.AccessLog;

public class JavTariff{
	private StringBuffer cnct = new StringBuffer();
	private final String msgError = cnct.append("MSERR_B_").append(this.getClass().getName()).append(".").toString();
	private Resources resources = new Resources();
    
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList getLovRecords(Connection con, String serviceId) {

		HashMap values = new HashMap(7);
		ArrayList result = new ArrayList();
		PreparedStatement psmt = null;
		ResultSet rs = null;
		String query = null;
		try {
			if (serviceId.equalsIgnoreCase("SHP-E")) {

				query =  cnct.delete(0, cnct.length())
						.append("SELECT TI_TRIF_ID TRIF1, 'Por Sobre' FACT1, ")
						.append("NULL WT2, 0 WT, NULL FACT2, NULL VOL2,")
						.append("0 VOL, 0 WT1, 0 VOL1 FROM ACT_TRIF_ID ")
						.append("WHERE TI_SRVC_ID = 'SHP-E'").toString();
			} else {
				query = cnct.delete(0, cnct.length())
						.append("SELECT a.TI_TRIF_ID TRIF1, ")
						.append("a.TI_FACT FACT1, ")
						.append("a.TI_FROM_VLUE||'-'||a.TI_TO_VLUE WT2, ")
						.append("(a.TI_FROM_VLUE+ a.TI_TO_VLUE)/2 wt, ")
						.append("b.TI_FACT FACT2, ")
						.append("b.TI_FROM_VLUE||'-'|| b.TI_TO_VLUE VOL2, ")
						.append("b.TI_TO_VLUE vol, ")
						.append("(a.TI_FROM_VLUE+ a.TI_TO_VLUE)/2 wt1, ")
						.append("b.TI_TO_VLUE vol1 ")
						.append("FROM ACT_TRIF_ID a, ACT_TRIF_ID b where ")
						.append("a.TI_SRVC_ID = 'SHP-G' and a.TI_TRIF_ID = b.ti_trif_id AND a.TI_FACT = 'KG' and b.TI_FACT ='CUM' ")
						//.append("AND a.TI_TRIF_ID != 'T7' ")
						.append("Union ")
						.append("SELECT SUBSTR(a.TI_TRIF_ID,1,2) TRIF1, a.TI_FACT ")
						.append("FACT1, a.TI_FROM_VLUE||'-'||a.TI_TO_VLUE WT2, ")
						.append("a.TI_FROM_VLUE WT, b.TI_FACT FACT2, b.TI_FROM_VLUE||'-'|| ")
						.append("b.TI_TO_VLUE VOL2, ")
						.append("b.TI_FROM_VLUE vol, a.TI_FROM_VLUE WT1, ")
						.append("b.TI_FROM_VLUE vol1 ")
						.append("FROM (SELECT TI_TRIF_ID, TI_FACT, TI_FROM_VLUE, TI_TO_VLUE ")
						.append("FROM ACT_TRIF_ID WHERE TI_TRIF_ID = 'T7P') A, ")
						.append("(SELECT TI_TRIF_ID, TI_FACT, TI_FROM_VLUE, ")
						.append("TI_TO_VLUE FROM ACT_TRIF_ID ")
						.append("WHERE TI_TRIF_ID = 'T7V') B").toString();
			}
			
			psmt = con.prepareStatement(query);
			rs = psmt.executeQuery();

			while (rs.next()) {

				if (serviceId.equalsIgnoreCase("SHP-E")) {
					values.put("TI_TRIF_ID", rs.getString(1));
					values.put("FACT1", rs.getString(2));
					result.add(values.clone());
				} else {
					values.put("TRIF1", rs.getString("TRIF1"));
					values.put("FACT1", rs.getString("FACT1"));
					values.put("WT2", rs.getString("WT2"));
					values.put("FACT2", rs.getString("FACT2"));
					values.put("VOL2", rs.getString("VOL2"));
					values.put("WT", rs.getString("WT"));
					values.put("VOL", rs.getString("VOL"));
					result.add(values.clone());
				}
				values.clear();
			}
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgError).append("getLovRecords()Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (psmt != null) {
					psmt.close();
				}
			} catch (Exception e2) {
				AccessLog.Log(cnct.delete(0, cnct.length()).append(msgError).append("getLovRecords()Error2:").append(e2).toString());
				e2.printStackTrace();
			}
		}
		return result;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList getLovNewTariff(Connection con, String serviceId, String clientId, String desBranchId, String orgBranchId) {

		HashMap values = new HashMap(7);
		ArrayList result = new ArrayList();
		PreparedStatement psmt = null;
		ResultSet rs = null;
		String query = null;
		try {
			if (serviceId.equalsIgnoreCase("SHP-E")) {

				query =  cnct.delete(0, cnct.length())
						.append("SELECT TI_TRIF_ID TRIF1, 'Por Sobre' FACT1, ")
						.append("NULL WT2, 0 WT, NULL FACT2, NULL VOL2,")
						.append("0 VOL, 0 WT1, 0 VOL1 FROM ACT_TRIF_ID ")
						.append("WHERE TI_SRVC_ID = ?").toString();
			} else {
				query = cnct.delete(0, cnct.length())
						.append("SELECT a.TI_TRIF_ID TRIF1, ")
						.append("a.TI_FACT FACT1, ")
						.append("a.TI_FROM_VLUE||'-'||a.TI_TO_VLUE WT2, ")
						.append("(a.TI_FROM_VLUE+ a.TI_TO_VLUE)/2 wt, ")
						.append("b.TI_FACT FACT2, ")
						.append("b.TI_FROM_VLUE||'-'|| b.TI_TO_VLUE VOL2, ")
						.append("b.TI_TO_VLUE vol, ")
						.append("(a.TI_FROM_VLUE+ a.TI_TO_VLUE)/2 wt1, ")
						.append("b.TI_TO_VLUE vol1 ")
						.append("FROM ACT_TRIF_ID a, ACT_TRIF_ID b where ")
						.append("a.TI_SRVC_ID = ? and a.TI_TRIF_ID = b.ti_trif_id ")
						.append("AND a.TI_TRIF_ID IN (SELECT TD_TRIF_ID FROM WEB_CLNT_SRVC_TRIF_DTL WHERE TD_ORGN_CLNT_ID = ? AND TD_ORGN_BRNC_ID = ? AND TD_DEST_BRNC_ID = ?) ")
						.append("AND a.TI_FACT = ? and b.TI_FACT = ? ")
						.append("Union ")
						.append("SELECT SUBSTR(a.TI_TRIF_ID,1,2) TRIF1, a.TI_FACT ")
						.append("FACT1, a.TI_FROM_VLUE||'-'||a.TI_TO_VLUE WT2, ")
						.append("a.TI_FROM_VLUE WT, b.TI_FACT FACT2, b.TI_FROM_VLUE||'-'|| ")
						.append("b.TI_TO_VLUE VOL2, ")
						.append("b.TI_FROM_VLUE vol, a.TI_FROM_VLUE WT1, ")
						.append("b.TI_FROM_VLUE vol1 ")
						.append("FROM (SELECT TI_TRIF_ID, TI_FACT, TI_FROM_VLUE, TI_TO_VLUE ")
						.append("FROM ACT_TRIF_ID WHERE TI_TRIF_ID = ? AND TI_TRIF_ID IN (SELECT TD_TRIF_ID FROM WEB_CLNT_SRVC_TRIF_DTL WHERE TD_ORGN_CLNT_ID = ? AND TD_ORGN_BRNC_ID = ? AND TD_DEST_BRNC_ID = ?)) A, ")
						.append("(SELECT TI_TRIF_ID, TI_FACT, TI_FROM_VLUE, ")
						.append("TI_TO_VLUE FROM ACT_TRIF_ID ")
						.append("WHERE TI_TRIF_ID = ? AND TI_TRIF_ID IN (SELECT TD_TRIF_ID FROM WEB_CLNT_SRVC_TRIF_DTL WHERE TD_ORGN_CLNT_ID = ? AND TD_ORGN_BRNC_ID = ? AND TD_DEST_BRNC_ID = ?)) B").toString();
			}
			
			psmt = con.prepareStatement(query);
			if (serviceId.equalsIgnoreCase("SHP-E")) {
				psmt.setString(1, "SHP-E");
			} else {
				psmt.setString(1, "SHP-G");
				psmt.setString(2, clientId);
				psmt.setString(3, orgBranchId);
				psmt.setString(4, desBranchId);
				psmt.setString(5, "KG");
				psmt.setString(6, "CUM");
				psmt.setString(7, "T7P");
				psmt.setString(8, clientId);
				psmt.setString(9, orgBranchId);
				psmt.setString(10, desBranchId);
				psmt.setString(11, "T7V");
				psmt.setString(12, clientId);
				psmt.setString(13, orgBranchId);
				psmt.setString(14, desBranchId);
			}
			rs = psmt.executeQuery();

			while (rs.next()) {

				if (serviceId.equalsIgnoreCase("SHP-E")) {
					values.put("TI_TRIF_ID", rs.getString(1));
					values.put("FACT1", rs.getString(2));
					result.add(values.clone());
				} else {
					values.put("TRIF1", rs.getString("TRIF1"));
					values.put("FACT1", rs.getString("FACT1"));
					values.put("WT2", rs.getString("WT2"));
					values.put("FACT2", rs.getString("FACT2"));
					values.put("VOL2", rs.getString("VOL2"));
					values.put("WT", rs.getString("WT"));
					values.put("VOL", rs.getString("VOL"));
					result.add(values.clone());
				}
				values.clear();
			}
			
			/*si no obtuvo listado de tarifas de convenio, muestra tarifario de piso*/
			if (result.isEmpty()) {
				result = getLovRecords(con, serviceId);
			}
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgError).append("getLovNewTariff()Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (psmt != null) {
					psmt.close();
				}
			} catch (Exception e2) {
				AccessLog.Log(cnct.delete(0, cnct.length()).append(msgError).append("getLovNewTariff()Error2:").append(e2).toString());
				e2.printStackTrace();
			}
		}
		return result;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList getLovNewTariffKm(Connection con, String serviceId, String clientId, String kmDist) {

		HashMap values = new HashMap(7);
		ArrayList result = new ArrayList();
		PreparedStatement psmt = null;
		ResultSet rs = null;
		String query = null;
		try {
			if (serviceId.equalsIgnoreCase("SHP-E")) {

				query =  cnct.delete(0, cnct.length())
						.append("SELECT TI_TRIF_ID TRIF1, 'Por Sobre' FACT1, ")
						.append("NULL WT2, 0 WT, NULL FACT2, NULL VOL2,")
						.append("0 VOL, 0 WT1, 0 VOL1 FROM ACT_TRIF_ID ")
						.append("WHERE TI_SRVC_ID = ?").toString();
			} else {
				query = cnct.delete(0, cnct.length())
						.append("SELECT a.TI_TRIF_ID TRIF1, ")
						.append("a.TI_FACT FACT1, ")
						.append("a.TI_FROM_VLUE||'-'||a.TI_TO_VLUE WT2, ")
						.append("(a.TI_FROM_VLUE+ a.TI_TO_VLUE)/2 wt, ")
						.append("b.TI_FACT FACT2, ")
						.append("b.TI_FROM_VLUE||'-'|| b.TI_TO_VLUE VOL2, ")
						.append("b.TI_TO_VLUE vol, ")
						.append("(a.TI_FROM_VLUE+ a.TI_TO_VLUE)/2 wt1, ")
						.append("b.TI_TO_VLUE vol1 ")
						.append("FROM ACT_TRIF_ID a, ACT_TRIF_ID b where ")
						.append("a.TI_SRVC_ID = ? and a.TI_TRIF_ID = b.ti_trif_id ")
						.append("AND a.TI_TRIF_ID IN (SELECT TD_TRIF_ID FROM WEB_CLNT_SRVC_TRIF_DTL_KM WHERE TD_ORGN_CLNT_ID = ? AND ? BETWEEN TD_DSTN_FROM_KM AND TD_DSTN_TO_KM) ")
						.append("AND a.TI_FACT = ? and b.TI_FACT = ? ")
						.append("Union ")
						.append("SELECT SUBSTR(a.TI_TRIF_ID,1,2) TRIF1, a.TI_FACT ")
						.append("FACT1, a.TI_FROM_VLUE||'-'||a.TI_TO_VLUE WT2, ")
						.append("a.TI_FROM_VLUE WT, b.TI_FACT FACT2, b.TI_FROM_VLUE||'-'|| ")
						.append("b.TI_TO_VLUE VOL2, ")
						.append("b.TI_FROM_VLUE vol, a.TI_FROM_VLUE WT1, ")
						.append("b.TI_FROM_VLUE vol1 ")
						.append("FROM (SELECT TI_TRIF_ID, TI_FACT, TI_FROM_VLUE, TI_TO_VLUE ")
						.append("FROM ACT_TRIF_ID WHERE TI_TRIF_ID = ? AND TI_TRIF_ID IN (SELECT TD_TRIF_ID FROM WEB_CLNT_SRVC_TRIF_DTL_KM WHERE TD_ORGN_CLNT_ID = ? AND ? BETWEEN TD_DSTN_FROM_KM AND TD_DSTN_TO_KM)) A, ")
						.append("(SELECT TI_TRIF_ID, TI_FACT, TI_FROM_VLUE, ")
						.append("TI_TO_VLUE FROM ACT_TRIF_ID ")
						.append("WHERE TI_TRIF_ID = ? AND TI_TRIF_ID IN (SELECT TD_TRIF_ID FROM WEB_CLNT_SRVC_TRIF_DTL_KM WHERE TD_ORGN_CLNT_ID = ? AND ? BETWEEN TD_DSTN_FROM_KM AND TD_DSTN_TO_KM)) B").toString();
			}
			
			psmt = con.prepareStatement(query);
			if (serviceId.equalsIgnoreCase("SHP-E")) {
				psmt.setString(1, "SHP-E");
			} else {
				psmt.setString(1, "SHP-G");
				psmt.setString(2, clientId);
				psmt.setString(3, kmDist);				
				psmt.setString(4, "KG");
				psmt.setString(5, "CUM");
				psmt.setString(6, "T7P");
				psmt.setString(7, clientId);
				psmt.setString(8, kmDist);
				psmt.setString(9, "T7V");
				psmt.setString(10, clientId);
				psmt.setString(11, kmDist);		
			}
			rs = psmt.executeQuery();

			while (rs.next()) {

				if (serviceId.equalsIgnoreCase("SHP-E")) {
					values.put("TI_TRIF_ID", rs.getString(1));
					values.put("FACT1", rs.getString(2));
					result.add(values.clone());
				} else {
					values.put("TRIF1", rs.getString("TRIF1"));
					values.put("FACT1", rs.getString("FACT1"));
					values.put("WT2", rs.getString("WT2"));
					values.put("FACT2", rs.getString("FACT2"));
					values.put("VOL2", rs.getString("VOL2"));
					values.put("WT", rs.getString("WT"));
					values.put("VOL", rs.getString("VOL"));
					result.add(values.clone());
				}
				values.clear();
			}
			
			/*si no obtuvo listado de tarifas de convenio, muestra tarifario de piso*/
			if (result.isEmpty()) {
				result = getLovRecords(con, serviceId);
			}
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgError).append("getLovNewTariffKm()Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (psmt != null) {
					psmt.close();
				}
			} catch (Exception e2) {
				AccessLog.Log(cnct.delete(0, cnct.length()).append(msgError).append("getLovNewTariffKm()Error2:").append(e2).toString());
				e2.printStackTrace();
			}
		}
		return result;
	}
	
   	/****************************************************************************************
   	 * valida las tarifas de los servicios seleccionados para validar si es tarifa invalida *
   	 ****************************************************************************************/
	@SuppressWarnings("rawtypes")
	public boolean isTarifaInvalida(Connection con, HttpSession session){//AAP02
		boolean invalida = false;
		
		ShipmentServiceDetail ssd = null;		
		ArrayList tarifaInvalida = null;
		double pesoInv = 0;
		double volumenInv = 0;
		double peso = 0;
		double volumen = 0;

		String defaultScreen = (String)session.getAttribute("defaultservicescreen");
		ArrayList servicesDetailArray = null;
		
		try {
			/* **********************************************************
			 * obtiene el arraylist de servicios agregados				*
			 * tarifa fija, defaultScreen = yes							*
			 * tarifa de piso, defaultScreen = no						*
			 * **********************************************************/
			if(defaultScreen!=null && defaultScreen.equalsIgnoreCase("yes")){
				servicesDetailArray= (ArrayList)session.getAttribute("servicesDetailDefault");
			} else{
				servicesDetailArray= (ArrayList)session.getAttribute("servicesDetail");
			}
			
			/*obtiene la tarifa considerada como invalida*/
			tarifaInvalida = getTarifaInvalida(con);
			try {pesoInv = Double.parseDouble(tarifaInvalida.get(1).toString());} catch (Exception e) {pesoInv = 0;}
			try {volumenInv = Double.parseDouble(tarifaInvalida.get(2).toString());} catch (Exception e) {volumenInv = 0;}
			for(int i=0;i<servicesDetailArray.size();i++){
				ssd = (ShipmentServiceDetail)servicesDetailArray.get(i);
				
				if(defaultScreen!=null && defaultScreen.equalsIgnoreCase("yes")){
					/*compara contra peso y volumen (servicios default)*/
					try {peso = Double.parseDouble(ssd.peso);} catch (Exception e) {peso = 0;}
					try {volumen = Double.parseDouble(ssd.volumen);} catch (Exception e) {volumen = 0;}
					//AccessLog.Log(cnct.delete(0, cnct.length()).append(msgError).append("isTarifaInvalida()peso:").append(peso).toString());
					//AccessLog.Log(cnct.delete(0, cnct.length()).append(msgError).append("isTarifaInvalida()volumen:").append(volumen).toString());
					//AccessLog.Log(cnct.delete(0, cnct.length()).append(msgError).append("isTarifaInvalida()pesoInv:").append(pesoInv).toString());
					//AccessLog.Log(cnct.delete(0, cnct.length()).append(msgError).append("isTarifaInvalida()volumenInv:").append(volumenInv).toString());
					if (peso >= pesoInv || volumen >= volumenInv ){						
						invalida = true;
						break;
					}
				}else{
					/*compara contra tipo de tarifa (servicios de piso)*/
					if (tarifaInvalida.get(0).toString().equals( ssd.tarifa )){
						invalida = true;
						break;					
					} 
				}
			}
		} catch (Exception e) {			
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgError).append("isTarifaInvalida()Error:").append(e).toString());
			e.printStackTrace();
		}
		return invalida;
	}	   
	
	/****************************************************************************************
   	 * valida las tarifas de los servicios seleccionados para validar si es tarifa invalida *
   	 ****************************************************************************************/
	@SuppressWarnings("rawtypes")
	public boolean isTarifaInvalidaNew(Connection con, HttpSession session, String defaultScreen, String orgnClntId, String destSite, String defaultScreenKm){//AAP
		boolean invalida = false;
		boolean acceptT7 = false;
		ShipmentServiceDetail ssd = null;		
		ArrayList tarifaInvalida = null;
		double pesoInv = 0;
		double volumenInv = 0;
		double peso = 0;
		double volumen = 0;
		double total_amount = 0;
		String totalAmount = "0";
		//String defaultScreen = (String)session.getAttribute("defaultservicescreen");
		ArrayList servicesDetailArray = null;
		
		try {			
			servicesDetailArray= (ArrayList)session.getAttribute("servicesDetail");
			Global global = (Global) session.getAttribute("sGlobal");
			
			/*obtiene la tarifa considerada como invalida*/
			tarifaInvalida = getTarifaInvalida(con);
			try {pesoInv = Double.parseDouble(tarifaInvalida.get(1).toString());} catch (Exception e) {pesoInv = 0;}
			try {volumenInv = Double.parseDouble(tarifaInvalida.get(2).toString());} catch (Exception e) {volumenInv = 0;}
			//AccessLog.Log(msgError+"isTarifaInvalidaNew() global.getTarifType() "+global.getTarifType());
			//AccessLog.Log(msgError+"isTarifaInvalidaNew() global.getAcceptZeT7() "+global.getAcceptZeT7());
//			if (global.getTarifType().equals("A") && global.getAcceptZeT7().equals("Y")) {//AAP24
//				acceptT7 = true;//AAP24
//			}//AAP24
			if (global.getAcceptZeT7().equals("Y")) {//AAP24
				acceptT7 = true;//AAP24
			}//AAP24
			for(int i=0;i<servicesDetailArray.size();i++){
				ssd = (ShipmentServiceDetail)servicesDetailArray.get(i);
				
				if(defaultScreen!=null && defaultScreen.equalsIgnoreCase("yes")){
					/*compara contra peso y volumen (servicios default)*/
					try {peso = Double.parseDouble(ssd.getPeso());} catch (Exception e) {peso = 0;}
					try {volumen = Double.parseDouble(ssd.getVolumen());} catch (Exception e) {volumen = 0;}
					//AccessLog.Log(cnct.delete(0, cnct.length()).append(msgError).append("isTarifaInvalida()peso:").append(peso).toString());
					//AccessLog.Log(cnct.delete(0, cnct.length()).append(msgError).append("isTarifaInvalida()volumen:").append(volumen).toString());
					//AccessLog.Log(cnct.delete(0, cnct.length()).append(msgError).append("isTarifaInvalida()pesoInv:").append(pesoInv).toString());
					//AccessLog.Log(cnct.delete(0, cnct.length()).append(msgError).append("isTarifaInvalida()volumenInv:").append(volumenInv).toString());
					if (acceptT7 && ssd.getTarifa().equals("T7")) {
						//AccessLog.Log(msgError+"isTarifaInvalidaNew() entro a obtener tarifa de convenio EXT");
						if (global.getTarifType().equals("A")) {
							/*si acepta envios a EXT con T7, verifica que exista convenio.*/
							if(defaultScreenKm!=null && defaultScreenKm.equalsIgnoreCase("Y")){
								totalAmount = getImporteEXTT7convenioKm(con, global.getClientIdAgreement(), destSite, ssd.getTarifa(), "EXT-1", ssd.getCantidad(), ssd.getPeso(), ssd.getVolumen(), global.getKmTarifType());
							} else {
								totalAmount = getImporteEXTT7convenio(con, global.getClientIdAgreement(), destSite, ssd.getTarifa(), "EXT-1", ssd.getCantidad(), ssd.getPeso(), ssd.getVolumen(), global.getAssignedSite());	
							}
							
							try {total_amount = Double.parseDouble(totalAmount);} catch (Exception e) {total_amount = 0;}
						} else {
							/*verifica si existe convenio para EXT-1 en cliente tipo C*/
							if(defaultScreenKm!=null && defaultScreenKm.equalsIgnoreCase("Y")){
                                //AccessLog.Log(msgError+"isTarifaInvalidaNew() entro por kilometraje ");
                                total_amount = existTarifCtypeKm(con, global, "EXT-1", "EXT");
                            } else {
                                total_amount = existTarifCtype(con, global, "EXT-1", "EXT");
                            }
						}
					}
					
					//AccessLog.Log(msgError+"isTarifaInvalidaNew() totalAmount "+totalAmount);
					//if (global.getTarifType().equals("A")) {
						if ((peso >= pesoInv || volumen >= volumenInv) && /*valida tarifa T7*/
								(!acceptT7 									/*no acepta Envios T7 a EXT*/
										|| (acceptT7 && total_amount<=0))) 	/*Acepta Envios T7 a EXT pero no tiene convenio configurado*/ {			
							invalida = true;
							break;
						}
					/*} else {
						if (peso >= pesoInv || volumen >= volumenInv) {
							if (!acceptT7) {//AAP24
								invalida = true;
							}//AAP24			
							break;
						}
					}*/

				}else{
					/*compara contra tipo de tarifa (servicios de piso)*/
					if (tarifaInvalida.get(0).toString().equals( ssd.getTarifa() )){
						invalida = true;
						break;
					} 
				}
			}
		} catch (Exception e) {			
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgError).append("isTarifaInvalida()Error:").append(e).toString());
			e.printStackTrace();
		}
		return invalida;
	}	   
	
	/****************************************************************************************
	 * valida las tarifas de los servicios seleccionados para validar si es tarifa
	 * invalida *
	 ****************************************************************************************/
	@SuppressWarnings("rawtypes")
	public boolean isTarifaInvalidaZP(Connection con, HttpSession session, String defaultScreen, String orgnClntId,
			String destSite, String defaultScreenKm) {// AAP
		boolean invalida = false;
		boolean acceptT7 = false;
		ShipmentServiceDetail ssd = null;
		ArrayList tarifaInvalida = null;
		double pesoInv = 0;
		double volumenInv = 0;
		double peso = 0;
		double volumen = 0;
		double totalAmountD = 0;
		String totalAmount = "0";
		ArrayList servicesDetailArray = null;

		try {
			servicesDetailArray = (ArrayList) session.getAttribute("servicesDetail");
			Global global = (Global) session.getAttribute("sGlobal");

			/* obtiene la tarifa considerada como invalida */
			tarifaInvalida = getTarifaInvalida(con);
			try {pesoInv = Double.parseDouble(tarifaInvalida.get(1).toString());} catch (Exception e) {pesoInv = 0;}
			try {volumenInv = Double.parseDouble(tarifaInvalida.get(2).toString());} catch (Exception e) {volumenInv = 0;}
			if (global.getAcceptRadZpT7().equals("Y")) {// AAP24
				acceptT7 = true;// AAP24
			} // AAP24
			for (int i = 0; i < servicesDetailArray.size(); i++) {
				ssd = (ShipmentServiceDetail) servicesDetailArray.get(i);

				if (defaultScreen != null && defaultScreen.equalsIgnoreCase("yes")) {
					/* compara contra peso y volumen (servicios default) */
					try {peso = Double.parseDouble(ssd.getPeso());} catch (Exception e) {peso = 0;}
					try {volumen = Double.parseDouble(ssd.getVolumen());} catch (Exception e) {volumen = 0;}
					
					if (acceptT7 && ssd.getTarifa().equals("T7")) {
						if (global.getTarifType().equals("A")) {
							/* si acepta envios a EXT con T7, verifica que exista convenio. */
							if (defaultScreenKm != null && defaultScreenKm.equalsIgnoreCase("Y")) {
								totalAmount = getImporteEXTT7convenioKm(con, global.getClientIdAgreement(), destSite,
										ssd.getTarifa(), "RAD-ZP-1", ssd.getCantidad(), ssd.getPeso(), ssd.getVolumen(),
										global.getKmTarifType());
							} else {
								totalAmount = getImporteEXTT7convenio(con, global.getClientIdAgreement(), destSite,
										ssd.getTarifa(), "RAD-ZP-1", ssd.getCantidad(), ssd.getPeso(), ssd.getVolumen(),
										global.getAssignedSite());
							}

							try {
								totalAmountD = Double.parseDouble(totalAmount);
							} catch (Exception e) {
								totalAmountD = 0;
							}
						} else {
							/* verifica si existe convenio para EXT-1 en cliente tipo C */
							if (defaultScreenKm != null && defaultScreenKm.equalsIgnoreCase("Y")) {
								totalAmountD = existTarifCtypeKm(con, global, "RAD-ZP-1", "RAD-ZP");
							} else {
								totalAmountD = existTarifCtype(con, global, "RAD-ZP-1", "RAD-ZP");
							}
						}
					}
					if ((peso >= pesoInv || volumen >= volumenInv) && /* valida tarifa T7 */
							(!acceptT7 /* no acepta Envios T7 a EXT */
									|| (acceptT7 && totalAmountD <= 0))) /* Acepta Envios T7 a EXT pero no tiene convenio configurado*/ {
						invalida = true;
						break;
					}
				} else {
					/* compara contra tipo de tarifa (servicios de piso) */
					if (tarifaInvalida.get(0).toString().equals(ssd.getTarifa())) {
						invalida = true;
						break;
					}
				}
			}
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgError).append("isTarifaInvalida()Error:").append(e)
					.toString());
			e.printStackTrace();
		}
		return invalida;
	}
	
	
	/*************************************************************************************************************
	 * Metodo para obtener Tarifa, Volumen y Peso invalidos para zonas
	 * extendidas *
	 *************************************************************************************************************/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList getTarifaInvalida(Connection con){
		PreparedStatement pst = null;
		ResultSet rs = null;

		ArrayList datos = new ArrayList(3);
		StringBuffer query = new StringBuffer();

		try {

			query.append("SELECT SUBSTR(a.TI_TRIF_ID,1,2) TRIF1, a.TI_FROM_VLUE WT1, b.TI_FROM_VLUE vol1 FROM") 
					.append(" (SELECT TI_TRIF_ID, TI_FROM_VLUE FROM ACT_TRIF_ID WHERE TI_TRIF_ID=?) A,") 
					.append(" (SELECT TI_TRIF_ID, TI_FROM_VLUE FROM ACT_TRIF_ID WHERE TI_TRIF_ID=?) B");
			//AAP//System.out.println("query TARIFAS "+query);
			pst = con.prepareStatement(query.toString());
			
			pst.setString(1, "T7P");
			pst.setString(2, "T7V");
			
			rs = pst.executeQuery();

			if (rs.next()) {
				// TARIFA
				if (rs.getString(1) == null) {
					datos.add("");
				} else {
					datos.add(rs.getString(1));
				}

				// PESO
				if (rs.getString(2) == null) {
					datos.add("");
				} else {
					datos.add(rs.getString(2));
				}
				// VOLUMEN
				if (rs.getString(3) == null) {
					datos.add("");
				} else {
					datos.add(rs.getString(3));
				}				
			}
		} catch (Exception e) {			
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgError).append("getTarifaInvalida()Error1:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pst != null) {
					pst.close();
				}
			} catch (Exception e2) {
				AccessLog.Log(cnct.delete(0, cnct.length()).append(msgError).append("getTarifaInvalida()Error2:").append(e2).toString());
				e2.printStackTrace();				
			}
		}
		return datos;
	}
	
	/****************************************************************************************
	 * metodo que ejecuta funcion para obtener tipo de tarifa (T01, T1, T2 .. T7)          	*
	 * **************************************************************************************/
	public String getTipoTarifa(Connection con, double peso, double volumen) {
		//Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String tipoTarifa = "";//monto de cobro tarifa normal.
		try {
			//con = ConnectDB.getConection();
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
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgError).append("getTipoTarifa()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pst != null) {
					pst.close();
				}
				
			} catch (Exception e2) {
				AccessLog.Log(cnct.delete(0, cnct.length()).append(msgError).append("getTipoTarifa()_Error2: ").append(e2).toString());
				e2.printStackTrace();
			}
		}
		return tipoTarifa;
	}
	
	/****************************************************************************************
	 * Obtiene peso y volumen configuracion en convenio para T7 en WEB_CLNT_MSTR         	*
	 * **************************************************************************************/
	public HashMap<String, String> getPesoVolConvenio(Connection con, String clientId) {
		//Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String peso = "61";
		String volu = ".32";
		HashMap<String, String> result = new HashMap<String, String>(2);
		try {
			//con = ConnectDB.getConection();
			String query = "";		
			
			query = "SELECT WC_WEIGHT, WC_VOLUME FROM WEB_CLNT_MSTR WHERE WC_CLNT_ID = ?";
			pst = con.prepareStatement(query);
			
			pst.setString(1, clientId);
			
			rs = pst.executeQuery();
			
			if (rs.next()) {
				peso = rs.getString(1) == null ? "61" : rs.getString(1);
				volu = rs.getString(2) == null ? ".32" : rs.getString(2);				
			}
			
			result.put("pesoConvenio", peso);
			result.put("voluConvenio", volu);
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgError).append("getPesoVolConvenio()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pst != null) {
					pst.close();
				}
				
			} catch (Exception e2) {
				AccessLog.Log(cnct.delete(0, cnct.length()).append(msgError).append("getPesoVolConvenio()_Error2: ").append(e2).toString());
				e2.printStackTrace();
			}
		}
		return result;
	}
	
	/*************************************************************************************************************
	 * Metodo para obtener importe de zona extendida de convenio para T7.
	 *************************************************************************************************************/	
	public String getImporteEXTT7convenio(Connection con, String clientIdAgreement, String destSite, String tipoTarifa, 
			String serviceId, String sCantidad, String sPeso, String sVolumen, String orgnSite){
		PreparedStatement pst = null;
		ResultSet rs = null;
		double total_amount = 0;//monto total de los renglones capturados
		double amount = 0;//monto de cobro tarifa normal o T7V (segun condicion)
		double amountp = 0;//monto de cobro tarifa T7P
		double factorv = 0;//tipo de calculo (cada cuantos metros cubicos)
		double factorp = 0;// tipo de calculo (cada cuantos kilos)
		double divvolu = 0;//division de calculo (cada cuantos metros cubicos)
		double divpeso = 0;// division de calculo (cada cuantos kilos)
		double totalv = 0;//total en volumen
		double totalp = 0;//total en peso
		//int cantidad = 0;//cantidad de bultos
		double peso = 0;
		double volumen = 0;
		try {
			
			//String query = "select pack_web.FUN_FTCH_TRIF_AMNT_NEW(?,?,?,?) from dual";
			
			String query = cnct.delete(0,cnct.length())
					.append("SELECT TD_TRIF_AMNT, NVL(TD_FACTOR_VALUE,0) FROM WEB_CLNT_SRVC_TRIF_DTL ")
					.append("WHERE TD_ORGN_CLNT_ID = ? ")
					.append("AND substr(TD_ORGN_BRNC_ID,1,3) = substr(?,1,3) ")
					.append("AND substr(TD_DEST_BRNC_ID,1,3) = substr(?,1,3) ")
					.append("AND TD_SRVC_ID = ? ")
					.append("AND TD_TRIF_ID = ?")
					.append("AND TD_TRIF_AMNT <> 0").toString();        
			
			pst = con.prepareStatement(query);

			pst.setString(1, clientIdAgreement);
			pst.setString(2, orgnSite);	
			pst.setString(3, destSite);	
			pst.setString(4, serviceId);
			
			//try {cantidad = Integer.parseInt(sCantidad);	} catch (Exception e) { cantidad = 0;}
			
			if (tipoTarifa.equals("T7")) {
				/*obtiene tarifa por peso*/
				pst.setString(5, "T7P");
				
				rs = pst.executeQuery();
				
				if (rs.next()) {
					amountp = rs.getDouble(1);
					factorp = rs.getDouble(2);
				}
				
				if (rs!=null)
					rs.close();
				
				/*obtiene tarifa por volumen*/
				pst.setString(5, "T7V");
				
				rs = pst.executeQuery();
				
				if (rs.next()) {
					amount = rs.getDouble(1);
					factorv = rs.getDouble(2);
				}
				
				/* se convierten los datos de la forma a valores enteros y dobles*/					
				try {peso = Double.parseDouble(sPeso); 		} catch (Exception e) { peso = 0;}
				try {volumen = Double.parseDouble(sVolumen);	} catch (Exception e) { volumen = 0;}
				
				/*se realiza calculo de precio en base a formulas*/
				
				/*calculos para peso*/
				if (factorp==0) {
					totalp = amountp;
				}  else if (factorp==1) {
					totalp = (amountp * peso);
				} else if (factorp>1) {
					divpeso = peso / factorp;
					divpeso = Math.ceil(divpeso);
					
					totalp = amountp * divpeso;
				}
				
				/*calculos para volumen*/
				if (factorv==0) {
					totalv = amount;
				}  else if (factorv==1) {
					totalv = (amount * volumen);
				} else if (factorv>1) {
					divvolu = volumen / factorv;
					divvolu = Math.ceil(divvolu);
					
					totalv = amount * divvolu;
				}			

				if (totalp > totalv) {
					amount = totalp;
				} else {
					amount = totalv;
				}
			} else {
				pst.setString(5, tipoTarifa);
				rs = pst.executeQuery();
				
				if (rs.next()) {
					amount = rs.getDouble(1);						
				}
			}
			
			/*suma al importe total, el cobro obtenido por la cantidad de bultos del renglon actual*/ 
			//total_amount = total_amount + amount;
			total_amount = amount;
			//amount = 0;
		
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgError).append("getImporteEXTT7convenio()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pst != null) {
					pst.close();
				}
				
			} catch (Exception e2) {
				AccessLog.Log(cnct.delete(0, cnct.length()).append(msgError).append("getImporteEXTT7convenio()_Error2: ").append(e2).toString());
				e2.printStackTrace();
			}
		}
		return new java.text.DecimalFormat("0.00").format(total_amount);
	}
	
	/*************************************************************************************************************
	 * Metodo para obtener importe de zona extendida de convenio para T7 por Kilometraje
	 * 
	 *************************************************************************************************************/	
	public String getImporteEXTT7convenioKm(Connection con, String clientIdAgreement, String destSite, String tipoTarifa, 
			String serviceId, String sCantidad, String sPeso, String sVolumen, String KmTarifType){
		PreparedStatement pst = null;
		ResultSet rs = null;
		double total_amount = 0;//monto total de los renglones capturados
		double amount = 0;//monto de cobro tarifa normal o T7V (segun condicion)
		double amountp = 0;//monto de cobro tarifa T7P
		double factorv = 0;//tipo de calculo (cada cuantos metros cubicos)
		double factorp = 0;// tipo de calculo (cada cuantos kilos)
		double divvolu = 0;//division de calculo (cada cuantos metros cubicos)
		double divpeso = 0;// division de calculo (cada cuantos kilos)
		double totalv = 0;//total en volumen
		double totalp = 0;//total en peso
		//int cantidad = 0;//cantidad de bultos
		double peso = 0;
		double volumen = 0;
		try {
			
			//String query = "select pack_web.FUN_FTCH_TRIF_AMNT_NEW(?,?,?,?) from dual";
			
			String query = cnct.delete(0,cnct.length())
					.append("SELECT TD_TRIF_AMNT, NVL(TD_FACTOR_VALUE,0) FROM WEB_CLNT_SRVC_TRIF_DTL_KM ")
					.append("WHERE TD_ORGN_CLNT_ID = ? ")
					.append("AND ? between TD_DSTN_FROM_KM and TD_DSTN_TO_KM ")
					.append("AND TD_SRVC_ID = ? ")
					.append("AND TD_TRIF_ID = ?")
					.append("AND TD_TRIF_AMNT <> 0").toString();        
			
			pst = con.prepareStatement(query);

			//pst.setString(1, idOrgnClnt);
			pst.setString(1, clientIdAgreement);			
			pst.setString(2, KmTarifType);	
			pst.setString(3, serviceId);
			
			//try {cantidad = Integer.parseInt(sCantidad);	} catch (Exception e) { cantidad = 0;}
			
			if (tipoTarifa.equals("T7")) {
				/*obtiene tarifa por peso*/
				pst.setString(4, "T7P");
				
				rs = pst.executeQuery();
				
				if (rs.next()) {
					amountp = rs.getDouble(1);
					factorp = rs.getDouble(2);
				}
				
				if (rs!=null)
					rs.close();
				
				/*obtiene tarifa por volumen*/
				pst.setString(4, "T7V");
				
				rs = pst.executeQuery();
				
				if (rs.next()) {
					amount = rs.getDouble(1);
					factorv = rs.getDouble(2);
				}
				
				/* se convierten los datos de la forma a valores enteros y dobles*/					
				try {peso = Double.parseDouble(sPeso); 		} catch (Exception e) { peso = 0;}
				try {volumen = Double.parseDouble(sVolumen);	} catch (Exception e) { volumen = 0;}
				
				/*se realiza calculo de precio en base a formulas*/
				
				/*calculos para peso*/
				if (factorp==0) {
					totalp = amountp;
				}  else if (factorp==1) {
					totalp = (amountp * peso);
				} else if (factorp>1) {
					divpeso = peso / factorp;
					divpeso = Math.ceil(divpeso);
					
					totalp = amountp * divpeso;
				}
				
				/*calculos para volumen*/
				if (factorv==0) {
					totalv = amount;
				}  else if (factorv==1) {
					totalv = (amount * volumen);
				} else if (factorv>1) {
					divvolu = volumen / factorv;
					divvolu = Math.ceil(divvolu);
					
					totalv = amount * divvolu;
				}			

				if (totalp > totalv) {
					amount = totalp;
				} else {
					amount = totalv;
				}
			} else {
				pst.setString(4, tipoTarifa);
				rs = pst.executeQuery();
				
				if (rs.next()) {
					amount = rs.getDouble(1);						
				}
			}
			
			/*suma al importe total, el cobro obtenido por la cantidad de bultos del renglon actual*/ 
			//total_amount = total_amount + amount;
			total_amount = amount;
			//amount = 0;
		
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgError).append("getImporteEXTT7convenioKm()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pst != null) {
					pst.close();
				}
				
			} catch (Exception e2) {
				AccessLog.Log(cnct.delete(0, cnct.length()).append(msgError).append("getImporteEXTT7convenioKm()_Error2: ").append(e2).toString());
				e2.printStackTrace();
			}
		}
		return new java.text.DecimalFormat("0.00").format(total_amount);
	}
	
    /*verifica si existe configuracion en tabla WEB_CLNT_SRVC_TRIF_FACTOR_KM, para servicio EXT-1 */
    private double existTarifCtypeKm(Connection con, Global global, String serviceId, String referenceServiceId) {//ADAP26
        double amount = 0.0;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {

            String query =  "SELECT WCK_TRIF_AMNT " +
                            "FROM WEB_CLNT_SRVC_TRIF_FACTOR_KM " +
                            "WHERE WCK_ORGN_CLNT_ID = ? AND "+
                                "? between WCK_DSTN_FROM_KM and WCK_DSTN_TO_KM AND " +
                                "WCK_TRIF_SLAB = ? AND " +
                                "WCK_REFR_SRVC_ID = :refr_serv_id AND " +
                                "WCK_SRVC_ID = ? " +
                                "AND ROWNUM <= 1 " +
                            "ORDER BY WCK_FACTOR_VALUE";
            pst = con.prepareStatement(query);
            pst.setString(1, global.getClientIdAgreement());
            pst.setString(2, global.getKmTarifType());
            pst.setString(3, global.getTarifType());
            pst.setString(4, referenceServiceId);
            pst.setString(5, serviceId);
            //pst.setDouble(7, totalWeight);
            //pst.setDouble(8, totalVolume);
            rs = pst.executeQuery();

            if (rs.next()) {
                amount = rs.getDouble(1);
            }					
        } catch (Exception e) {
            AccessLog.Log(cnct.delete(0, cnct.length()).append(msgError).append("existTarifCtypeKm()_Error2: ").append(e).toString());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pst != null) {
                    pst.close();
                }

            } catch (Exception e2) {
                AccessLog.Log(cnct.delete(0, cnct.length()).append(msgError).append("existTarifCtypeKm()_Error2: ").append(e2).toString());
                e2.printStackTrace();
            }
        }
        return amount;
    }        
            
    /*verifica si existe configuracion en tabla WEB_CLNT_SRVC_TRIF_FACTOR, para servicio EXT-1 */
    private double existTarifCtype(Connection con, Global global, String serviceId, String referenceServiceId) {//ADAP26
        double amount = 0.0;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            String query = "SELECT WCP_TRIF_AMNT " +
                    "FROM WEB_CLNT_SRVC_TRIF_FACTOR " +
                    "WHERE WCP_ORGN_CLNT_ID = ? AND " +
                        "substr(WCP_ORGN_BRNC_ID,1,3) = substr(?,1,3) AND " +
                        "substr(WCP_DEST_BRNC_ID,1,3) = substr(?,1,3) AND " +
                        "WCP_TRIF_SLAB = ? AND " +
                        "WCP_REFR_SRVC_ID = ? AND " +
                        "WCP_SRVC_ID = ? AND " +
                        "ROWNUM <= 1 " +
                    "ORDER BY WCP_FACTOR_VALUE";

            pst = con.prepareStatement(query);
            pst.setString(1, global.getClientIdAgreement());
            pst.setString(2, global.getAssignedBranch());
            pst.setString(3, global.getDestinationBranchId());
            pst.setString(4, global.getTarifType());
            pst.setString(5, referenceServiceId);
            pst.setString(6, serviceId);
            //pst.setDouble(7, totalWeight);
            //pst.setDouble(8, totalVolume);

            rs = pst.executeQuery();

            if (rs.next()) {
                amount = rs.getDouble(1);
            }					
         } catch (Exception e) {
            AccessLog.Log(cnct.delete(0, cnct.length()).append(msgError).append("existTarifCtype()_Error2: ").append(e).toString());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pst != null) {
                    pst.close();
                }

            } catch (Exception e2) {
                AccessLog.Log(cnct.delete(0, cnct.length()).append(msgError).append("existTarifCtype()_Error2: ").append(e2).toString());
                e2.printStackTrace();
            }
        }
        return amount;
    }

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList getLovRecordsByTarrif(Connection con, String tarrifID) {

		HashMap values = new HashMap(7);
		ArrayList result = new ArrayList();
		PreparedStatement psmt = null;
		ResultSet rs = null;
		String query = null;
		try {
				query = cnct.delete(0, cnct.length())
						.append("SELECT a.TI_TRIF_ID TRIF1, ")
						.append("a.TI_FACT FACT1, ")
						.append("a.TI_FROM_VLUE||'-'||a.TI_TO_VLUE WT2, ")
						.append("(a.TI_FROM_VLUE+ a.TI_TO_VLUE)/2 wt, ")
						.append("b.TI_FACT FACT2, ")
						.append("b.TI_FROM_VLUE||'-'|| b.TI_TO_VLUE VOL2, ")
						.append("b.TI_TO_VLUE vol, ")
						.append("(a.TI_FROM_VLUE+ a.TI_TO_VLUE)/2 wt1, ")
						.append("b.TI_TO_VLUE vol1 ")
						.append("FROM ACT_TRIF_ID a, ACT_TRIF_ID b where ")
						.append("a.TI_SRVC_ID = ? and a.TI_TRIF_ID = b.ti_trif_id AND a.TI_FACT = ? and b.TI_FACT = ? and a.TI_TRIF_ID = ?").toString();
			
			psmt = con.prepareStatement(query);
			psmt.setString(1, "SHP-G");
			psmt.setString(2, "KG");
			psmt.setString(3, "CUM");
			psmt.setString(4, tarrifID);
			rs = psmt.executeQuery();

			while (rs.next()) {
					values.put("TRIF1", rs.getString("TRIF1"));
					values.put("FACT1", rs.getString("FACT1"));
					values.put("WT2", rs.getString("WT2"));
					values.put("FACT2", rs.getString("FACT2"));
					values.put("VOL2", rs.getString("VOL2"));
					values.put("WT", rs.getString("WT"));
					values.put("VOL", rs.getString("VOL"));
					result.add(values.clone());
					values.clear();
			}
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgError).append("getLovRecordsByTarrif()Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (psmt != null) {
					psmt.close();
				}
			} catch (Exception e2) {
				AccessLog.Log(cnct.delete(0, cnct.length()).append(msgError).append("getLovRecordsByTarrif()Error2:").append(e2).toString());
				e2.printStackTrace();
			}
		}
		return result;
	}
	
	public String getTarrifDefaultByTarrifType_C(Connection con, String clntId) {
		String result = "";
		PreparedStatement psmt = null;
		ResultSet rs = null;
		String query = null;
		try {
			query = cnct.delete(0, cnct.length()).append("SELECT PM_PARM_CODE2 PARMCODE FROM SYS_PARM_MSTR where PM_MDUL_ID= 'WEB' and PM_PARM_TYPE = 'ASSIGN_TARIFF' and PM_PARM_CODE1 = ? ").toString();
			
			psmt = con.prepareStatement(query);
			psmt.setString(1, clntId);
			rs = psmt.executeQuery();

			if (rs.next()) {
				result = rs.getString("PARMCODE");
			}
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgError).append("getTarrifDefaultByTarrifType_C()Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (psmt != null) {
					psmt.close();
				}
			} catch (Exception e2) {
				AccessLog.Log(cnct.delete(0, cnct.length()).append(msgError).append("getTarrifDefaultByTarrifType_C()Error2:").append(e2).toString());
				e2.printStackTrace();
			}
		}
		return result;
	}
	
	//Obtiene compa�ia de servicio calculada en base a peso, cliente, servicio.//AAP23
	public String getCompanyId(Connection con, String peso, String clntId, String srvcId) {//AAP23
		PreparedStatement pst = null;
		ResultSet rs = null;
		String companyId =null;
		try {

			String query = "select FUN_GET_CPNY_BY_WEIGHT_CLIENT(?, ?, ?) from dual";
			pst = con.prepareStatement(query);
			pst.setDouble(1, Double.parseDouble(peso));
			pst.setString(2, clntId);
			pst.setString(3, srvcId);
			
			rs = pst.executeQuery();
			
			if (rs.next()) {
				companyId = rs.getString(1);
			}
			
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgError).append("getCompanyId()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.closeResources(rs, pst);
		}
		return companyId;
	}
	
	public List<Object> getTaxRenEspecial(Connection con, String clntRet, String serviceRefId, String companySrvcCalculado, String serviceId) {
		CallableStatement cst = null;
		List<Object> datos = new ArrayList<Object>();
		try {
			String query = "{ call PRO_GET_SET_SRVC(?,?,?,?,?,?,?) }";
			cst = con.prepareCall(query);
			cst.setString(1, clntRet);
			cst.setString(2, "BOOKING");
			String srvcId = serviceRefId;
			if (srvcId.equalsIgnoreCase("PACKETS") || srvcId.equalsIgnoreCase("ENVELOPES")) {
				srvcId = serviceId;
			}
			cst.setString(3, srvcId);
			cst.setString(4, companySrvcCalculado);
			cst.registerOutParameter(5, Types.INTEGER);//retension
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
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgError).append("getTaxRenEspecial()2_Error:").append(e)
					.toString());
			e.printStackTrace();
		} finally {
			resources.cerrarCallableStatement(cst);
		}
		return datos;
	}
}