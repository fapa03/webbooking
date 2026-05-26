import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beanUtil.ConnectDB;

/**
 * Servlet implementation class GenFirma
 */
public class GenFirma extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private StringBuffer cnct = new StringBuffer();
	private final String msgErr = new StringBuffer("MSERR_B_").append(this.getClass().getName()).append(".").toString();
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GenFirma() {
		super();		
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		perform(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		perform(request, response);
	}

	private void perform(HttpServletRequest request,
			HttpServletResponse response) {
		String Rastfirm = "0";
		Rastfirm = request.getParameter("RASTREO").toString();
		FileOutputStream fos = null;
		File f = null;
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		InputStream is = null;
		try {
			String ruta = request.getSession().getServletContext().getRealPath("/");
			//Class.forName("oracle.jdbc.driver.OracleDriver");
			//con = DriverManager.getConnection("jdbc:oracle:thin:@192.168.10.20:1521:SIPWEB", "SIPWEB2","WMOY0412P");
			con = ConnectDB.getConnection();
			st = con.prepareStatement("select DL_SIGNATURE from dlvry_sign where DL_GUIA_NO = ?");
			st.setString(1, Rastfirm);
			//f = new File("F:\\JRUN\\servers\\default\\default-ear\\default-war\\sign\\sign" + Rastfirm + ".bmp");
			//f = new File("C:\\paso\\firmas\\sign" + Rastfirm + ".bmp");			
			f = new File(cnct.delete(0, cnct.length()).append(ruta).append("\\firmas\\sign").append(Rastfirm).append(".bmp").toString());
			
			fos = new FileOutputStream(f);
			rs = st.executeQuery();
			if (rs.next()) {
				is = rs.getBinaryStream(1);
				byte b[] = new byte[255];
				while (is.read(b) != -1) {
					fos.write(b);
					if (is.available() >= 255)
						b = new byte[255];
					else
						b = new byte[is.available()];
				}
				is.close();
				is = null;
			}

//			fos.flush();
//			fos.close();
//			fos = null;
		} catch (Exception e) {			
			System.out.println(cnct.delete(0,cnct.length()).append(msgErr).append("perform()_Error_update:").append(e).toString());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e2) {
				System.out.println(cnct.delete(0,cnct.length()).append(msgErr).append("perform()_Error_update_02:").append(e2).toString());
			}
			try {
				if (st != null) {
					st.close();
				}
			} catch (Exception e2) {
				System.out.println(cnct.delete(0,cnct.length()).append(msgErr).append("perform()_Error_update_02:").append(e2).toString());
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e2) {
				System.out.println(cnct.delete(0,cnct.length()).append(msgErr).append("perform()_Error_update_02:").append(e2).toString());
			}
			try {
				if (is != null){
					is.close();
				}
			} catch (Exception e2) {
				System.out.println(cnct.delete(0,cnct.length()).append(msgErr).append("perform()_Error_update_02:").append(e2).toString());
			}
			try {
				if (fos != null) {
					fos.flush();
					fos.close();
				}
			} catch (Exception e2) {
				System.out.println(cnct.delete(0,cnct.length()).append(msgErr).append("perform()_Error_update_02:").append(e2).toString());
			}
		}
	}
}
