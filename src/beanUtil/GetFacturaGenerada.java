package beanUtil;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import logger.AccessLog;

/**
 * Servlet implementation class GetFacturaGenerada
 */
public class GetFacturaGenerada extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private StringBuffer cnct = new StringBuffer();
	private final String msgErr = new StringBuffer("MSERR_B_").append(this.getClass().getName()).append(".").toString();
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GetFacturaGenerada() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		obtieneFactura(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		obtieneFactura(request, response);
	}

	private void obtieneFactura(HttpServletRequest request,
			HttpServletResponse response) {
		FileInputStream fin = null;
		FileOutputStream fout = null;
            BufferedInputStream is = null;
            OutputStream os = null;
            FileInputStream fis = null;
		try {
			String d1 = "0";
			d1 = request.getParameter("seleccionaDia");
			String m1 = "0";
			m1 = request.getParameter("seleccionaMes");
			String an1 = "0";
			an1 = request.getParameter("seleccionaAnyo");

			String codfactura = "0";
			if (request.getParameter("fact") != null) {
				codfactura = request.getParameter("fact");
			}
			String dirdestino = "";
			String dirorigen = "";
			int dint = Integer.parseInt(d1);
			int mint = Integer.parseInt(m1);
			int anint = Integer.parseInt(an1);

			String cod1 = codfactura.substring(3, 4);
			String cod2 = codfactura.substring(0, 3);
			String cod3 = codfactura.substring(3);

			if (cod1.equals("k") || cod1.equals("K")) {

				dirorigen = cnct.delete(0, cnct.length()).append("Y:\\").append(cod2).append("\\").append(cod3).append(".pdf").toString();
				dirdestino = cnct.delete(0, cnct.length()).append("F:\\JRUN\\servers\\default\\default-ear\\default-war\\facturas\\fe\\")
						.append(codfactura).append(".pdf").toString();			

			} else {

				if (anint <= 2012) {
					if (anint == 2012) {
						if (mint >= 10) {
							if (mint == 10) {
								if (dint > 20) {
									dirorigen = cnct.delete(0, cnct.length()).append("x:\\").append( an1).append(m1).append("\\").append(d1)
											.append("\\pdf\\ITM8012013N0_CFD_")
											.append( codfactura ).append( "_").append( an1 ).append( m1 ).append( d1 )
											.append( ".pdf").toString();									
								} else {
									dirorigen = cnct.delete(0, cnct.length()).append("x:\\").append( an1 ).append( m1 )
											.append( "\\pdf\\ITM8012013N0_CFD_" )
											.append( codfactura ).append( "_" ).append( an1 ).append( m1 ).append( d1 )
											.append( ".pdf" ).toString();									
								}
							} else {
								dirorigen = cnct.delete(0, cnct.length()).append("x:\\").append( an1 ).append( m1 ).append( "\\" ).append( d1 )
										.append( "\\pdf\\ITM8012013N0_CFD_")
										.append( codfactura ).append( "_" ).append( an1 ).append( m1 ).append( d1 )
										.append(".pdf").toString();								
							}
						} else {
							dirorigen = cnct.delete(0, cnct.length()).append("x:\\").append( an1 ).append( m1 )
									.append( "\\pdf\\ITM8012013N0_CFD_" ).append( codfactura )
									.append( "_" ).append( an1 ).append( m1 ).append( d1 ).append(".pdf").toString();							
						}
					} else {
						dirorigen = cnct.delete(0, cnct.length()).append("x:\\").append( an1 ).append( m1)
								.append( "\\pdf\\ITM8012013N0_CFD_" ).append( codfactura ).append( "_" )
								.append( an1 ).append( m1 ).append( d1 ).append(".pdf").toString();						
					}
				} else {
					/* CFDI FACTURA NUEVA (AJUSTAR UNIDAD A MAPEAR)*/
					dirorigen = cnct.delete(0, cnct.length()).append("y:\\").append( an1 ).append( m1 ).append( "\\" ).append( d1 )
							.append( "\\pdf\\ITM8012013N0_CFDI_").append( codfactura ).append( "_")
							.append( an1 ).append( m1 ).append( d1 ).append( ".pdf").toString();
					
					/*dirorigen = cnct.delete(0, cnct.length()).append("x:\\").append( an1 ).append( m1 ).append( "\\" ).append( d1 )
							.append( "\\pdf\\ITM8012013N0_CFD_").append( codfactura ).append( "_")
							.append( an1 ).append( m1 ).append( d1 ).append( ".pdf").toString();*/
				}

				// dirdestino =
				// "F:\\JRUN\\servers\\default\\default-ear\\default-war\\facturas\\fe\\ITM8012013N0_CFD_"
				// + codfactura + "_" + an1 + m1 + d1 + ".pdf";
				dirdestino = cnct.delete(0, cnct.length()).append("C:\\facturas\\fe\\ITM8012013N0_CFDI_").append( codfactura )
						.append("_").append( an1 ).append( m1 ).append( d1 ).append(".pdf").toString();
			
			}

			if (new java.io.File(dirorigen).exists()) {
				//System.out.println("->");
				int i;
				
				fin = new FileInputStream(dirorigen);
				fout = new FileOutputStream(dirdestino);
				do {
					i = fin.read();
					if (i != -1)
						fout.write(i);
				} while (i != -1);

				fin.close();
				fin = null;
				fout.flush();
				fout.close();
				fout = null;
				//System.out.println("Generando...");
				
				

				String filename = dirdestino;
				try {
					fis = new FileInputStream(filename);
					is = new BufferedInputStream(fis);
					os = response.getOutputStream();

					int z = 0;
					int y = 0;
					while (is.available() > 0) {
						byte b[] = new byte[1024];
						if (is.available() > 1024)
							b = new byte[1024];
						else
							b = new byte[is.available()];
						y = is.read(b);
						os.write(b);

						z = z + y;
						os.flush();
					}
					fis.close();
					fis = null;
					is.close();
					is = null;
					os.flush();
					os.close();
					os = null;
				} catch (Exception e) {
					AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("obtieneFactura()_Error2:").append(e).toString());
					e.printStackTrace();
				} finally {
					try {
						if (fis != null)
							fis.close();
					} catch (Exception ex) {
						AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("obtieneFactura()_Error4:").append(ex).toString());
						ex.printStackTrace();
					}
					try {
						if (is != null)
							is.close();	
					} catch (Exception ex) {
						AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("obtieneFactura()_Error5:").append(ex).toString());
						ex.printStackTrace();
					}
					try {
						if (os != null) {
							os.flush();
							os.close();
						}
					} catch (Exception ex) {
						AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("obtieneFactura()_Error6:").append(ex).toString());
						ex.printStackTrace();
					}
					try {
						File f = new File(filename); if(f.exists()){ f.delete(); }
					} catch (Exception ex) {
						AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("obtieneFactura()_Error7:").append(ex).toString());
						ex.printStackTrace();
					}
				}				
			}			
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("obtieneFactura()_Error1:").append(e).toString());
			e.printStackTrace();
		}
		finally {
                    
			try {
				if (fin != null) {
					fin.close();
				}
			} catch (Exception e) {
				AccessLog.Log("obtieneFactura()Exception_Error:"+e);
				e.printStackTrace();
			}
			try {
				if (is != null) {
					is.close();
				}
			} catch (Exception e) {
				AccessLog.Log("obtieneFactura()Exception_Error:"+e);
				e.printStackTrace();
			}
			try {
				if (os != null) {
                                    os.flush();
					os.close();
				}
			} catch (Exception e) {
				AccessLog.Log("obtieneFactura()Exception_Error:"+e);
				e.printStackTrace();
			}
			try {
				if (fis != null) {
					fis.close();
				}
			} catch (Exception e) {
				AccessLog.Log("obtieneFactura()Exception_Error:"+e);
				e.printStackTrace();
			}
			try {
				if (fout != null) {
					fout.flush();
					fout.close();
				}
			} catch (Exception e) {
				AccessLog.Log("obtieneFactura()Exception_Error2:"+e);
				e.printStackTrace();
			}
		}
	}
}
