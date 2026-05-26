package paquetexpress.internal.common;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import logger.AccessLog;

public class CommonFunction {
	
	public java.sql.Date getSysdate(Connection con)throws Exception{
		PreparedStatement pst = con.prepareStatement("select sysdate from dual");//Checked
		ResultSet rs = pst.executeQuery();
		java.sql.Date sysDate =null;
		if(rs.next())
			//sysDate = rs.getString(1);
			sysDate=rs.getDate(1);

		if(rs!=null)
			rs.close();
		if(pst!=null)
			pst.close();
		AccessLog.Log("SYSDATE IN SERVICES ACTION "+sysDate);
		return sysDate;
	}
	
	public ArrayList getLovRecords(Connection con)throws Exception{
		

        HashMap values = null;
        ArrayList result = new ArrayList();
        //String bmFlag = "T";--commented by B.Emerson on 20/06/2003
        PreparedStatement psmt = null;
        //String query = "select BM_BRNC_NAME, BM_BRNC_ID  from sys_brnc_mstr where BM_FLAG1 <> ?	and BM_BRNC_ID  <> ? order by 1";--commented by B.Emerson on 20/06/2003

		/*added by B.Emerson on 20/06/2003 - 
		only available destination branch for the web client should be displayed*/

		//String query = "select pack_web.Fun_ftch_site_name(WO_DEST_SITE_ID) BM_SITE_NAME, WO_DEST_SITE_ID BM_SITE_ID from web_orgn_dest_clnt where WO_ORGN_CLNT_ID = ? and WO_DEST_SITE_ID <> ? group by WO_DEST_SITE_ID order by 1";
        String query1="SELECT ALL SYS_BANK_MSTR.BA_BANK_ID, SYS_BANK_MSTR.BA_BANK_NAME FROM SYS_BANK_MSTR ";
		psmt = con.prepareStatement(query1);
        //psmt.setString(1,clientId); //psmt.setString(1,bmFlag);--passed client Id - by B.Emerson on 20/06/2003
        //psmt.setString(2,siteId);
	//	AccessLog.Log("Client Id "+clientId);
        ResultSet rs = psmt.executeQuery();
	//	AccessLog.Log("Site Id "+siteId);
        while(rs.next()){
            values = new HashMap();
            values.put("BANKID",rs.getString(1));
            values.put("BANKNAME",rs.getString(2));
            result.add(values);
                    }
        AccessLog.Log("after whl in CommonFun");
        if (rs!=null)
        	rs.close();
        if (psmt!=null)
        	psmt.close();
        return result;
    }
}
