package beanUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ActualizaPromesaComercial {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			String rastreo = "";
			String promesa = "";
			System.out.println("inicia consulta");
			Connection con = ConnectDBDirect.getConection();
			//String query = "SELECT WC_CLNT_ID, WC_PSWD, WC_APRV_BRNC FROM WEB_CLNT_MSTR WHERE WC_CLNT_ID='18025'";
			String query = "SELECT GH_GUIA_NO, GH_ISSE_DATE,  DECODE(GH_EAD_FLAG,'0','OCU','EAD') ENTREGA,to_char(GH_ISSE_DATE,'HH24:MM')  TIEMPO, " +
							"FUN_GET_PROMESA( 'TERRESTRE',GH_ORGN_BRNC_ID, GH_DEST_BRNC_ID, DECODE(GH_EAD_FLAG,'0','OCU','EAD'), '34289',to_char(GH_ISSE_DATE,'HH24:MM')) PROMESA "+
							"FROM BOK_GUIA_HEAD, BOK_GUIA_STUS "+
							"WHERE GH_ISSE_DATE >= TO_DATE('01/jul/2015') " + 
							//"AND SUBSTR(GH_FORM_NO,1,2)='WW' AND GH_GUIA_TYPE='N' AND GH_gUIA_NO = GS_GUIA_NO AND GS_STUS_CODE IN ('WBK','SWB')";
							"AND SUBSTR(GH_FORM_NO,1,2)='PP' AND GH_GUIA_TYPE='N' AND GH_gUIA_NO = GS_GUIA_NO AND GS_STUS_CODE IN ('WBK')";
			pst =  con.prepareStatement(query);
			rs =  pst.executeQuery();
			System.out.println("inicia proceso");
			double recorridos = 0;
			double actualizados = 0;
			int contador =1;
			int reg = 0;
			while (rs.next()) {
				rastreo =  rs.getString(1) == null ? "" : rs.getString(1);
				promesa =  rs.getString(5) == null ? "" : rs.getString(5);			

				/*inserta SYS_CLNT_USER*/
				reg = updateBokGuiaHeadExtra(con, rastreo, promesa);
				if (reg<=0){
					System.out.println("rastreo no actualizado "+rastreo+" promesa "+promesa);
				}
				actualizados = actualizados + reg;
				recorridos = recorridos + 1;
				if (contador == 500) {
					System.out.println("Registros recorridos "+recorridos);
					System.out.println("Registros actualizados "+actualizados);
					System.out.println("Ultimo rastreo "+rastreo);
					contador = 0;
					//break;
				}
				contador++;
			}
			System.out.println("termina proceso");
			rs.close();
			pst.close();
			con.commit();
			con.close();
		} catch (Exception e) {
			System.out.println("main()_Error: "+e);
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
	}
	
	
	private static int updateBokGuiaHeadExtra(Connection con, String rastreo, String promesa) {
		int reg = 0;
		
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			String query = "UPDATE BOK_GUIA_HEAD_EXTRA SET GE_PROMESA_HRS = ? WHERE GE_GUIA_NO = ?";
			pst = con.prepareStatement(query);
			pst.setString(1, promesa);
			pst.setString(2, rastreo);
			reg = pst.executeUpdate();
			pst.close();
			
			
		} catch (Exception e) {
			System.out.println("updateBokGuiaHeadExtra()_Error: "+e);
			e.printStackTrace();
		}finally {
			try {
				if (rs != null)
					rs.close();
				if (pst != null)
					pst.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			
		}
		return reg;		
	}

}
