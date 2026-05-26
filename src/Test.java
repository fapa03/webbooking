
import java.sql.*;
public class Test {
	
	
	public static void main(String args[])
	{
		System.out.println("Inside ");
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
		//	Connection con=DriverManager.getConnection("jdbc:oracle:thin:@172.24.0.102:1521:sipweb","sipweb","o");
			Connection con=DriverManager.getConnection("jdbc:oracle:thin:@192.168.10.20:1521:sipweb","sipweb","klnd9bc2");
			
			if(con==null)
			{
				System.out.println("conection is null");
			}
			double cdrlmt=0.0;
			String groupClientId="";
			String clientType="";
			String prequery = "SELECT CM_CLNT_ID,NVL(cm_cred_limt,0) cm_cred_limt, cm_grup_clnt_id, cm_clnt_type FROM SYS_CLNT_MSTR  "+
				            " WHERE  CM_CLNT_ID = ? ";
				
				PreparedStatement pst = con.prepareStatement(prequery);
				pst.setString(1,"6844");
				
				ResultSet rs = pst.executeQuery();
				
				System.out.println("executed");
				while(rs.next())
				{
					cdrlmt=rs.getDouble("cm_cred_limt");
					groupClientId=rs.getString("cm_grup_clnt_id");
					clientType=rs.getString("cm_clnt_type");
				}
				System.out.println("retrived");
				
				if(rs!=null)
				rs.close();
				if(pst!=null)
				pst.close();
				/*String query2 ="";
				System.out.println("second");
				if((clientType.equalsIgnoreCase("I"))||(clientType.equalsIgnoreCase("C")))
				{
					query2 = " SELECT NVL(gh_guia_amnt,0) FROM BOK_GUIA_HEAD WHERE gh_bill_clnt_id = ? AND" +
								" gh_pymt_type = 'C' AND gh_paid_date IS NULL AND "+ 
								" gh_guia_type IN ('N','S','G','T','H') AND gh_docu_type IN ('G','D','Q') AND "+
								" gh_used_date IS NULL AND gh_actv_flag = 'A'";
				}
				else if((clientType.equalsIgnoreCase("N"))||(clientType.equalsIgnoreCase("G")))
				{
								query2 = "SELECT NVL(gh_guia_amnt,0) FROM BOK_GUIA_HEAD WHERE gh_bill_clnt_id IN "+
							" (SELECT cm_clnt_id FROM SYS_CLNT_MSTR	WHERE cm_grup_clnt_id = ?) "+
					 	" AND	gh_pymt_type = 'C'	AND gh_paid_date IS NULL "+
					 	" AND gh_guia_type IN ('N','S','G','T','H') AND gh_docu_type IN ('G','D','Q') "+
					 	" AND gh_used_date IS NULL 	AND gh_actv_flag = 'A' ";
				}
				
				System.out.println("second query formed");
			PreparedStatement pst1 = con.prepareStatement(query2);
			if((clientType.equalsIgnoreCase("N"))||(clientType.equalsIgnoreCase("G")))
			pst1.setString(1,groupClientId);
			else
				pst1.setString(1,"6844");	
			
			ResultSet rs1 = pst1.executeQuery();
			double sumGuia=0.0;
			while(rs1.next())
			{
				sumGuia=sumGuia+ rs1.getDouble(1);
			}
			
			if(rs1!=null)
				rs1.close();
			if(pst1!=null)
				pst1.close();
			System.out.println("Limit"+(cdrlmt-sumGuia));*/
				con.close();
			
		} catch (Exception e) {
		
			e.printStackTrace();
		}
		
	}

}
