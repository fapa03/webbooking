
import java.sql.*;
import java.util.*;

import logger.AccessLog;

public class FetchReportRecords{
    public FetchReportRecords(){}
    public ArrayList fetchRecords(Connection con){
    	ArrayList values  = new ArrayList();
    	try{
    		PreparedStatement psmt = con.prepareStatement("select * from emp");
			ResultSet rs = psmt.executeQuery();
        	HashMap result = null;
        	while(rs.next()){
        		result = new HashMap();
            	result.put("EMPNO",rs.getString(1));
            	result.put("ENAME",rs.getString(2));
            	result.put("SAL",rs.getString(3));
            	result.put("DEPTNO",rs.getString(4));
            	result.put("JOB",rs.getString(5));
            	result.put("MGR",rs.getString(6));
            	result.put("HIREDATE",rs.getString(7));
            	result.put("COMM",rs.getString(8));
            	values.add(result);
        	}
        }
        catch(Exception e){
        	AccessLog.Log(e);
        }
        return values;
    }
}