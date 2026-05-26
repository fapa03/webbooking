
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.annotation.processing.Filer;

import logger.AccessLog;

/**
 * Command line program to count lines, words and characters
 * in files or from standard input, similar to the wc
 * utility.
 * Run like that: java WordCount FILE1 FILE2 ... or
 * like that: java WordCount < FILENAME.
 * @author Marco Schmidt
 */
public class WordCount {
	/**
	 * Count lines, words and characters in given input stream
	 * and print stream name and those numbers to standard output.
	 * @param name name of input source
	 * @param input stream to be processed
	 * @throws IOException if there were I/O errors
	 */
	
	ArrayList listofForms=new ArrayList();
	private  ArrayList count(String name, BufferedReader in) throws
	IOException {
		
		long numLines = 0;
		long numWords = 0;
		long numChars = 0;
		int numColumns=0;
		String line;
		do {
			line = in.readLine();
			if (line != null)
			{
				numLines++;
				//numChars += line.length();
				//numWords += countWords(line);
				numColumns=extractColumns(line,numLines+"");
				//AAP//System.out.println("No of columns"+numColumns);
				

			}
		}
		while (line != null);
		
		
		//System.out.println(name + "\t" + numLines + "\t" + 
		//	numWords + "\t" + numChars);
		return listofForms;
	}

	/**
	 * Open file, count its words, lines and characters 
	 * and print them to standard output.
	 * @param fileName name of file to be processed
	 */
	public  ArrayList count(String fileName) {
		BufferedReader in = null;
		ArrayList list=new ArrayList();
		FileReader fileReader = null;
		try {
			fileReader = new FileReader(fileName);
			in = new BufferedReader(fileReader);
			list=count(fileName, in);
			fileReader.close();
			fileReader = null;
			in.close();
			in = null;
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			if (fileReader != null) {
				try {
					fileReader.close();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		}
		return list;
	}

	/**
	 * Count words, lines and characters of given input stream
	 * and print them to standard output.
	 * @param streamName name of input stream (to print it to stdout)
	 * @param input InputStream to read from
	 */
	/*private  void count(String streamName, InputStream input) {
		InputStreamReader inputStreamReader = null;
		BufferedReader in = null;
		try {
			inputStreamReader = new InputStreamReader(input);
			in = new BufferedReader(inputStreamReader);
			count(streamName, in);
			inputStreamReader.close();
			inputStreamReader = null;
			in.close();
			in = null;
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			if (inputStreamReader != null) {
				try {
					inputStreamReader.close();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		}
	}*/

	/**
	 * Determine the number of words in the argument line.
	 * @param line String to be examined, must be non-null
	 * @return number of words, 0 or higher
	 */
	private  long countWords(String line) {
		long numWords = 0;
		int index = 0;
		boolean prevWhitespace = true;
		while (index < line.length()) {
			char c = line.charAt(index++);
			boolean currWhitespace = Character.isWhitespace(c);
			if (prevWhitespace && !currWhitespace) {
				numWords++;
			}
			prevWhitespace = currWhitespace;
		}
		return numWords;
	}

	private  int extractColumns(String line,String lineNo) {
		
		JavImportWebbookingForm form=new JavImportWebbookingForm();
		long numWords = 0;
		int index = 0;
		int incre=0;
		String strColumn=null;
		char columnValue[]=new char[300];
		int i=0;
		int colCount=0;
		while (index < line.length()) {
			
			char c = line.charAt(index++);
			if(c=='|')
			{
			columnValue[incre]='\0';
			incre=0;
			i=0;
			for(;columnValue[i]!='\0';i++)
			{			
				//System.out.print(columnValue[i]);
			}
			strColumn=new String(columnValue,0,i);
			//System.out.println(strColumn);
			colCount++;
			//System.out.println(colCount);
			form.setRowNum(lineNo);
			switch(colCount)
			{
			case 1:form.assign(strColumn,"DESTINATION CITY");
					break;
			case 2:form.assign(strColumn,"CLIENT ID");
					break;
			case 3:form.assign(strColumn,"CLIENT NAME");
					break;
			case 4:form.assign(strColumn,"STREET");
					break;
			case 5:form.assign(strColumn,"DOOR NUMBER");
					break;
			case 6:form.assign(strColumn,"COLONY");
					break;
			case 7:
					form.assign(strColumn,"ZIP CODE");
					break;
			case 8:form.assign(strColumn,"CITY");
					break;
			case 9:form.assign(strColumn,"PHONE");
					break;
			case 10:form.assign(strColumn,"QUANTITY");
					break;
			case 11:form.assign(strColumn,"DESCRIPTION");
					break;
			case 12:form.assign(strColumn,"CONTENT");
					break;
			case 13:form.assign(strColumn,"WEIGHT");
					break;
			case 14:form.assign(strColumn,"VOLUME");
					break;
			case 15:form.assign(strColumn,"DELIVERY TYPE");
					break;
			case 16:form.assign(strColumn,"ACKNOWLEDGEMENT");
					break;
			case 17:form.assign(strColumn,"COD VALUE");
					break;
			case 18:form.assign(strColumn,"DECLARED VALUE");
					break;
			case 19:form.assign(strColumn,"COMMENTARIES");
					break;
			case 20:form.assign(strColumn,"REFERENCE");
					break;
			case 21:form.assign(strColumn,"NUMBERDEPEDIMENTO");
					break;
			case 22:form.assign(strColumn,"AGENTSADUANAL");
					break;
			case 23:form.assign(strColumn,"SEQNUMBER");
					break;
			}			
			}
			else
			{				
			columnValue[incre]=c;
			incre++;
			}
							}
				columnValue[incre]='\0';
				i=0;
				for(;columnValue[i]!='\0';i++)
				{			
					//System.out.print(columnValue[i]);
				}
				strColumn=new String(columnValue,0,i);
				colCount++;
				switch(colCount)
				{
				case 1:form.assign(strColumn,"DESTINATION CITY");
						break;
				case 2:form.assign(strColumn,"CLIENT ID");
						break;
				case 3:form.assign(strColumn,"CLIENT NAME");
						break;
				case 4:form.assign(strColumn,"STREET");
						break;
				case 5:form.assign(strColumn,"DOOR NUMBER");
						break;
				case 6:form.assign(strColumn,"COLONY");
						break;
				case 7:form.assign(strColumn,"ZIP CODE");
						break;
				case 8:form.assign(strColumn,"CITY");
						break;
				case 9:form.assign(strColumn,"PHONE");
						break;
				case 10:form.assign(strColumn,"QUANTITY");
						break;
				case 11:form.assign(strColumn,"DESCRIPTION");
						break;
				case 12:form.assign(strColumn,"CONTENT");
						break;
				case 13:form.assign(strColumn,"WEIGHT");
						break;
				case 14:form.assign(strColumn,"VOLUME");
						break;
				case 15:form.assign(strColumn,"DELIVERY TYPE");
						break;
				case 16:form.assign(strColumn,"ACKNOWLEDGEMENT");
						break;
				case 17:form.assign(strColumn,"COD VALUE");
						break;
				case 18:form.assign(strColumn,"DECLARED VALUE");
						break;
				case 19:form.assign(strColumn,"COMMENTARIES");
						break;
				case 20:form.assign(strColumn,"REFERENCE");
						break;
				case 21:form.assign(strColumn,"NUMBERDEPEDIMENTO");
				break;
				case 22:form.assign(strColumn,"AGENTSADUANAL");
				break;
				case 23:form.assign(strColumn,"SEQNUMBER");
				break;
				}	
				listofForms.add(form);
			//	System.out.println(strColumn);
				
		return colCount;
	}
	
	
	public String insertToTempDB(Connection con,String nameFile,String clientId,ArrayList listofForms1)
	{
		//AAP//System.out.println("inside the db");
		try {
			int result=0;
			for(int i=0;i<listofForms1.size();i++)
			{
				//AAP//AccessLogLog.Log("inside"+i);
				JavImportWebbookingForm WebbookingFormverify=null;
				
				JavImportWebbookingForm Form=(JavImportWebbookingForm)listofForms.get(i);
				if(i>0)
				{
					WebbookingFormverify=(JavImportWebbookingForm)listofForms.get(i-1);
				}
			
			if(i==0)
			{
				//AAP//System.out.println("guia head ");
				//AAP//System.out.println("row number:"+Form.getRowNum());
				//AAP//System.out.println("client:"+Form.getDestClient());
				PreparedStatement pst=con.prepareStatement("insert into web_bach_guia values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
				pst.setString(1,nameFile);
				pst.setString(2,Form.getRowNum());
				pst.setString(3,clientId);
				pst.setString(4,null);
				pst.setString(5,null);
				pst.setString(6,Form.getDestClient());
				pst.setString(7,Form.getDestClientBranch());
				pst.setString(8,Form.getDestClientName());
				pst.setString(9,Form.getDestStreet());
				pst.setString(10,Form.getDestDoorNo());
				pst.setString(11,Form.getDestColony());
				pst.setString(12,Form.getDestZipCode());
				pst.setString(13,Form.getDestClientCity());
				pst.setString(14,Form.getDestClientPhone());
				pst.setString(15,Form.getServicesDeliveryType());
				pst.setString(16,Form.getServicesAck());
				pst.setString(17,Form.getServicesCodValue());
				pst.setString(18,Form.getServicesDeclaredValue());
				pst.setString(19,Form.getServicesCommentaries());
				pst.setString(20,Form.getServicesReference());
				pst.setString(21,null);
				pst.setString(22,null);
				pst.setString(23,null);
				pst.setString(24,null);
				pst.setString(25,null);
				pst.setString(26,null);
				pst.setString(27,null);
				pst.setString(28,null);
				pst.setString(29,null);
				pst.setString(30,null);
				
				result=pst.executeUpdate();
				//if(result==0)
						
				pst.close();
				con.commit();
			}
			if(i>0)
			{
				if(Form.getDestClientBranch().equalsIgnoreCase(WebbookingFormverify.getDestClientBranch()))
			{
			
				//AAP//System.out.println("Shipment details");
				PreparedStatement pst=con.prepareStatement("insert into web_bach_ship_srvc values(?,?,?,?,?,?,?,?,?,?,?)");
				pst.setString(1,nameFile);
				pst.setString(2,Form.getRowNum());
				pst.setString(3,null);
				pst.setString(4,null);
				pst.setString(5,null);
				pst.setString(6,null);
				pst.setString(7,Form.getShipQuantity());
				pst.setString(8,Form.getShipDescription());
				pst.setString(9,Form.getShipContent());
				pst.setString(10,Form.getShipWeight());
				pst.setString(11,Form.getShipVolume());
				result=pst.executeUpdate();
				pst.close();
			  //AAP//System.out.println("after inserting into ship");
				
			}
			
			else
			{
				
				//AAP//System.out.println("guia head ");
				PreparedStatement pst=con.prepareStatement("insert into web_bach_guia values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
				pst.setString(1,nameFile);
				pst.setString(2,Form.getRowNum());
				pst.setString(3,clientId);
				pst.setString(4,null);
				pst.setString(5,null);
				pst.setString(6,Form.getDestClient());
				pst.setString(7,Form.getDestClientBranch());
				pst.setString(8,Form.getDestClientName());
				pst.setString(9,Form.getDestStreet());
				pst.setString(10,Form.getDestDoorNo());
				pst.setString(11,Form.getDestColony());
				pst.setString(12,Form.getDestZipCode());
				pst.setString(13,Form.getDestClientCity());
				pst.setString(14,Form.getDestClientPhone());
				pst.setString(15,Form.getServicesDeliveryType());
				pst.setString(16,Form.getServicesAck());
				pst.setString(17,Form.getServicesCodValue());
				pst.setString(18,Form.getServicesDeclaredValue());
				pst.setString(19,Form.getServicesCommentaries());
				pst.setString(20,Form.getServicesReference());
				pst.setString(21,null);
				pst.setString(22,null);
				pst.setString(23,null);
				pst.setString(24,null);
				pst.setString(25,null);
				pst.setString(26,null);
				pst.setString(27,null);
				pst.setString(28,null);
				pst.setString(29,null);
				pst.setString(30,null);
				
				
				result=pst.executeUpdate();
				//AAP//AccessLogLog.Log(""+result);
				pst.close();
			}
			}
			
			}			
		} catch (SQLException e) {
			AccessLog.Log("Cannot create Prepared statement"+e.getMessage());
			e.printStackTrace();
		}
		return "success";
	}	
	
//	public static void main(String[] args) {
//		WordCount wc =new WordCount();
//		
//	ArrayList list=	wc.count("c:\\6844-20060826-1.txt");
//	Connection con=null;
//	try {
//		//	con=ConnectDB.getConnection();
//			try {
//				Class.forName("oracle.jdbc.driver.OracleDriver");
//			} catch (ClassNotFoundException e) {
//				
//				e.printStackTrace();
//			}
//			con=DriverManager.getConnection("jdbc:oracle:thin:sipweb/o@172.24.48.149:1521:siptest");
//	}
//	catch(Exception e)
//	{
//		
//	}
//	wc.insertToTempDB(con,"c:\\6844-20060826-1.txt","11176",list);
//		
//	}
}
