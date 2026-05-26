import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import logger.AccessLog;
import bean.AddServRecord;
import bean.ShipmentRecord;

public class ShipmentBean{
	private StringBuffer cnct = new StringBuffer();
	private final String msgError = cnct.append("MSERR_B_").append(this.getClass().getName()).append(".").toString();
	
  //This function returns a arraylist of values for the Shipment Fields
	public ArrayList fecthShipmentRecords(String guiaNumber, Connection con) {
		ArrayList arrRecords = new ArrayList();
		PreparedStatement psmt = null;
		ResultSet rs = null;
		try {
			String query = cnct.delete(0, cnct.length())
					.append("select GL_QUNT,pack_web.fun_ftch_ship_desc(gl_desc, gl_refr_srvc_id,gl_srvc_id)as DESCR,GL_SRVC_ID,GL_REFR_SRVC_ID,")
					.append("GL_CONT,GL_VLUE_1,GL_VLUE_2,GL_SRVC_CHRG FROM ")
					.append("BOK_GUIA_SRVC_ITEM WHERE GL_GUIA_NO =? AND GL_REFR_SRVC_ID IN (SELECT GS_SRVC_ID ")
					.append("FROM BOK_GUIA_SRVC WHERE GS_SRVC_TYPE='S' AND GS_GUIA_NO=?)")
					.toString();

			psmt = con.prepareStatement(query);
			psmt.setString(1, guiaNumber);
			psmt.setString(2, guiaNumber);
			
			rs = psmt.executeQuery();
			
			ShipmentRecord shipRecords = null;
			java.text.DecimalFormat df = new java.text.DecimalFormat("0.00");
			while (rs.next()) {
				shipRecords = new ShipmentRecord();
				shipRecords.setQuantity(rs.getString("GL_QUNT"));
				shipRecords.setDescription(rs.getString("DESCR"));
				shipRecords.setContent(rs.getString("GL_CONT"));
				
				shipRecords.setPeso(df.format(rs.getDouble("GL_VLUE_1")));// Changed by rama
				shipRecords.setVolume(df.format(rs.getDouble("GL_VLUE_2")));// Changed by rama
				shipRecords.setCharges(df.format(rs.getDouble("GL_SRVC_CHRG")));// Changed by rama
				arrRecords.add(shipRecords);
			}
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgError).append("fecthShipmentRecords()Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (psmt != null) {
					psmt.close();
				}
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0, cnct.length()).append(msgError).append("fecthShipmentRecords()Error2:").append(e).toString());
				e.printStackTrace();				
			}
		}
		return arrRecords;
	}
	
	// this function returns a arraylist of values for the Additional Service fields
	public ArrayList fetchAdtitionalServiceRecords(String guiaNumber, Connection con) {

		ArrayList arrAddServRecords = new ArrayList();
		PreparedStatement psmt = null;
		ResultSet rs = null;
		try {
			String query = cnct.delete(0, cnct.length())
					.append("SELECT GL_DESC, pack_web.fun_ftch_add_srvc_disc_guia ('")
					.append(guiaNumber)
					.append("',gl_Refr_Srvc_Id) as TOTAL from bok_guia_srvc_item ")
					.append("where GL_GUIA_NO = ? AND GL_REFR_SRVC_ID in (SELECT GS_SRVC_ID FROM BOK_GUIA_SRVC ")
					.append("WHERE GS_SRVC_TYPE !='S' AND GS_GUIA_NO = ?)").toString();
			
			psmt = con.prepareStatement(query);
			psmt.setString(1, guiaNumber);
			psmt.setString(2, guiaNumber);
			
			rs = psmt.executeQuery();
			AddServRecord addServRecord = null;
			while (rs.next()) {
				addServRecord = new AddServRecord();
				addServRecord.setDescription(rs.getString("GL_DESC"));
				
				addServRecord.setTotal(new java.text.DecimalFormat("0.00").format(rs.getDouble("TOTAL")));// Changed by rama
				arrAddServRecords.add(addServRecord);
			}

		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgError).append("fetchAdtitionalServiceRecords()Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (psmt != null) {
					psmt.close();
				}
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0, cnct.length()).append(msgError).append("fetchAdtitionalServiceRecords()Error2:").append(e).toString());
				e.printStackTrace();				
			}
		}
		return arrAddServRecords;
	}
	
  //this function returns an ArrayList of values for Requirement fields
	public ArrayList fetchRequirement(String guiaNumber, Connection con) {
		ArrayList arrRequirement = new ArrayList();
		PreparedStatement psmt = null;
		ResultSet rs = null;
		try {
			String query = "select GI_REQM_TEXT from BOK_GUIA_SRVC_ITEM_REQM where GI_GUIA_NO = ?";
			psmt = con.prepareStatement(query);
			psmt.setString(1, guiaNumber);
			rs = psmt.executeQuery();
			while (rs.next()) {
				arrRequirement.add(rs.getString("GI_REQM_TEXT"));
			}
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgError).append("fetchRequirement()Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (psmt != null) {
					psmt.close();
				}
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0, cnct.length()).append(msgError).append("fetchRequirement()Error2:").append(e).toString());
				e.printStackTrace();
			}
		}
		return arrRequirement;
	}
  
  /* this method returns the comment and returns a string for confirmation
  the values are directly set in the FormClass */
	public String fetchContactInfo(String guiaNumber, JavServicesForm form, Connection con) {
		JavServicesForm serFrm = form;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		String addComment = null;
		String addInfo = null;
		try {
			String query = "SELECT GH_COMT, GH_CONC_IDEN_NO FROM BOK_GUIA_HEAD WHERE GH_GUIA_NO = ?";
			psmt = con.prepareStatement(query);
			psmt.setString(1, guiaNumber);
			rs = psmt.executeQuery();
			while (rs.next()) {
				addComment = rs.getString("GH_COMT");
				addInfo = rs.getString("GH_CONC_IDEN_NO");
				if (addComment == null) {
					addComment = "";
				}
				if (addInfo == null) {
					addInfo = "";
				}
				serFrm.setComment(addComment);
				serFrm.setAddInfo(addInfo);
			}
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgError).append("fetchContactInfo()Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (psmt != null) {
					psmt.close();
				}
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0, cnct.length()).append(msgError).append("fetchContactInfo()Error2:").append(e).toString());
				e.printStackTrace();
			}
		}
		return "success";
	}
}