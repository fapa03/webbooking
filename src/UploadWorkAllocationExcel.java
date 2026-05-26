

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.http.HttpServletRequest;

import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;
import logger.AccessLog;

// Referenced classes of package com:
//            TestDBConnection

public class UploadWorkAllocationExcel {

    public UploadWorkAllocationExcel() {
     
    }

    public static void main(String[] args) {
    	try {
    		//AAP//System.out.println("inside main");
    	//UploadWorkAllocationExcel ex=new UploadWorkAllocationExcel();
    	//ex.get();
    	//ex.writeToDataBase();
    	//readGuiaContent_WriteFile();
    	// ex.writeExcel();
    	//ex.readexel("c:\\Book1.xls");
    	String filename="Book1.xls";
    	String fileext=filename.substring(filename.length()-3,filename.length());
    	//AAP//System.out.println(fileext);
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
	}
    public void readGuiaContent_WriteFile(HttpServletRequest request)
    {
    	java.sql.CallableStatement cst=null;
		Connection con=null;
		ResultSet rs = null;
		Statement st = null;
		//HttpSession session =request.getSession(false);
		try {
		//	con=ConnectDB.getConnection();
			try {
				Class.forName("oracle.jdbc.driver.OracleDriver");
			} catch (ClassNotFoundException e) {
				
				e.printStackTrace();
			}
			con=DriverManager.getConnection("jdbc:oracle:thin:sipweb/o@172.24.48.149:1521:siptest");
			//AAP//System.out.println("inside main");
			st=con.createStatement();
			rs=st.executeQuery("select guia_no,content from web_bach_guia where session_id=");
			while(rs.next()) {
				//AAP//System.out.println("the content :"+rs.getString(2));
			}
			rs.close();
			rs = null;
			st.close();
			st = null;
		}
		catch(Exception e) {
			AccessLog.Log(e.getMessage());
		}
		 finally {
			 try {
					if (rs != null && !rs.isClosed())
						rs.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				try {
					if (st != null && !st.isClosed())
						st.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				try {
					if (cst != null && !cst.isClosed())
						cst.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				try {
					if (con != null)
						con.close();
				} catch (Exception ex) {
					AccessLog.Log(ex);
				}
		}
    }
    public void writeToDataBase() {
    	//AAP//System.out.println("inside main");
		java.sql.CallableStatement cst=null;
		Connection con=null;
		PreparedStatement pst = null;
		//HttpSession session =request.getSession(false);
		try {
		//	con=ConnectDB.getConnection();
			try {
				Class.forName("oracle.jdbc.driver.OracleDriver");
			} catch (ClassNotFoundException e) {

				e.printStackTrace();
			}
			con=DriverManager.getConnection("jdbc:oracle:thin:sipweb/o@172.24.48.149:1521:siptest");
			//AAP//System.out.println("inside main");
			pst=con.prepareStatement("insert into web_bach_guia values(?,?,?)");
			
			pst.setString(1,"1223");
			pst.setString(2,"12");
			pst.setString(3,"123234234");
			int result=pst.executeUpdate();
			//AAP//System.out.println("inside main");
			/*//AAPif(result<0)
				System.out.println("Failure");
			else
				System.out.println("Inserted");*/
			pst.close();
			
			
		} catch (SQLException e) {
			AccessLog.Log("Error while creating Statement");
			e.printStackTrace();
		} finally {
			try {
				if (cst != null && !cst.isClosed())
					cst.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			try {
				if (con != null)
					con.close();
			} catch (Exception ex) {
				AccessLog.Log(ex);
			}
		}
	}
    
    
    public void writeExcel()
    {
    	String exportdirectory="";
    	BufferedOutputStream buff = null;
    	FileOutputStream stream = null;
    	try{
    		byte [] files="sdsdsdsd".getBytes();
    		String path="c:/karthi";
    		File f=new File(path+File.separator+"Amal");
    		if(!f.exists()) 
    			f.mkdirs();
    		exportdirectory=f.toString();
    		stream = new FileOutputStream(exportdirectory+File.separator+"amal.xls");
    		buff = new BufferedOutputStream(stream);
    		buff.write(files);
    		stream.flush();
    		stream.close();
    		stream = null;
    		buff.flush();
    		buff.close();
    		buff = null;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stream != null) {
					stream.flush();
					stream.close();
				}
			} catch (Exception e) {
				AccessLog.Log("writeExcel()Exception_Error:"+e);
				e.printStackTrace();
			}
			try {
				if (buff != null) {
					buff.flush();
					buff.close();
				}
			} catch (Exception e) {
				AccessLog.Log("writeExcel()Exception_Error:"+e);
				e.printStackTrace();
			}
		}
    }
    
    	public void readexel(String filename)


    	{
    	Connection c = null;
    	Statement stmnt = null;
    	ResultSet rs=null;
    	try
    	{
    	Class.forName( "sun.jdbc.odbc.JdbcOdbcDriver" );
    	c = DriverManager.getConnection("jdbc:odbc:Driver={Microsoft Excel Driver (*.xls)};DBQ=" + filename);
    	stmnt = c.createStatement();
    	String query = "select * from [Sheet1$]" ;
    	 rs = stmnt.executeQuery( query );

    	while( rs.next() ) {
    		System.out.println( rs.getString(1) );
    	}
    	//AAP//System.out.println("after result set");
    	}
    	catch( Exception e ) {
    		AccessLog.Log( e );
    	}
    finally{
    	try{
    		rs.close();
    	stmnt.close();
    	c.close();
    	System.out.println(new File(filename).delete());
    	}catch(Exception e)
    	{
    		System.out.println( e );	
    	}
    }

    	} 
    
    
    public void readXLS(String s)
    {
        Connection connection = null;
        PreparedStatement preparedstatement = null;
        try
        {
            //connection = TestDBConnection.getConnection();
          //  connection.setAutoCommit(false);
            Object obj = null;
            Object obj1 = null;
            if(s != null)
                strExcelFilePath = "C:\\coreJava\\source\\com\\TestSheat.xls";
            else
                strExcelFilePath = s;
            String s1 = "";
            String s3 = "";
            File file = new File(strExcelFilePath);
            Workbook workbook = Workbook.getWorkbook(file);
            Sheet asheet[] = workbook.getSheets();
            int i = asheet.length;
            for(int j = 0; j < i; j++)
            {
                Sheet sheet = asheet[j];
                String s5 = sheet.getName();
                int k = sheet.getColumns();
                int l = sheet.getRows();
                if(l <= 0)
                    continue;
                Cell acell1[] = sheet.getRow(0);
                if(l <= 1)
                    continue;
                for(int i1 = 1; i1 < l; i1++)
                {
                    String s2 = "(";
                    String s4 = "(";
                    Cell acell[] = sheet.getRow(i1);
                    for(int j1 = 0; j1 < acell.length; j1++)
                    {
                        if(acell[j1].getType() == CellType.EMPTY)
                            continue;
                        if(j1 != acell.length - 1)
                        {
                            if(!isNull(acell[j1].getContents()))
                            {
                                s4 = s4 + "'" + acell[j1].getContents() + "'" + ",";
                                s2 = s2 + acell1[j1].getContents() + ",";
                            }
                            continue;
                        }
                        if(!isNull(acell[j1].getContents()))
                        {
                            s4 = s4 + "'" + acell[j1].getContents() + "'" + ")";
                            s2 = s2 + acell1[j1].getContents() + ")";
                        }
                    }

                    if(!s4.equals("(") || !s2.equals("("))
                    {
                        String s6 = "INSERT INTO " + s5 + s2 + " VALUES" + s4;
                        preparedstatement = connection.prepareStatement(s6);
                        preparedstatement.executeUpdate();
                        //AAP//System.out.println("");
                        //AAP//System.out.println(s6);
                        //AAP//System.out.println("");
                    }
                }

            }

            if(connection != null)
                connection.commit();
        }
        catch(Exception exception)
        {
            try
            {
                if(connection != null)
                    connection.rollback();
            }
            catch(Exception exception1) {
	            System.out.println(" Exception : " + exception.getMessage());
	            exception.printStackTrace();
            }
        }
        finally
        {
            PreparedStatement apreparedstatement[] = {
                preparedstatement
            };
            try
            {
               // TestDBConnection.closeDBResources(connection, null, apreparedstatement, null, null);
            }
            catch(Exception exception3) { }
        }
    }

    public boolean isNull(String s)
    {
        return s == null || s.trim().length() == 0 || s.trim().equals("");
    }

    String strEmp_Id;
    String strExcelFilePath;
    
    public void get() throws Exception
    {
    	//System.out.println("coming");
    	JavImportWebbookingForm form=new JavImportWebbookingForm();
    	
    	File file = new File("E:\\Book1.xls");
        Workbook workbook = Workbook.getWorkbook(file);
        Sheet asheet[] = workbook.getSheets();
        int i = asheet.length;
        for(int j = 0; j < i; j++)
        {
            Sheet sheet = asheet[j];
            
            String s5 = sheet.getName();
            int k = sheet.getColumns();        	
            int l = sheet.getRows();
           // System.out.println(k);System.out.println(l);
           Cell cells1[]=sheet.getRow(0);
            	
           
            if(l <= 0)
                continue;	
            for(int norows=1;norows<l;norows++)
            {
            	Cell cells[]=sheet.getRow(norows);
            	for(int cellcount=0;cellcount<cells.length;cellcount++)
            	{
            		form.assign(cells[cellcount].getContents(),cells1[cellcount].getContents());
            	}
            }
        	//System.out.println("coming");
            //AAP//System.out.println(form.getDestClient());
            //AAP//System.out.println(form.getDestClientBranch());
            //AAP//System.out.println(form.getDestClientBranch());
        }
        
    }
}
