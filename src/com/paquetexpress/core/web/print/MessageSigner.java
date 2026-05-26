package com.paquetexpress.core.web.print;

//import javax.servlet.annotation.WebServlet;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.lowagie.text.pdf.codec.Base64;

//@WebServlet("/MessageSigner")
public class MessageSigner extends HttpServlet {
	  private static final long serialVersionUID = 1L;
	  private Signature sig;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = null;
        try {
            String data = request.getParameter("request");
            String signature = InitPropertiesPrinter.getInstance(data);
            response.setContentType("text/plain");
            out = response.getWriter();
            out.write(signature);
            out.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
    
   public String processRequestSimple(String dataReq, String dataResp) throws ServletException, IOException {
        PrintWriter out = null;
        String f = "";
        try {
            String signature = InitPropertiesPrinter.getInstance(dataReq);
            f = signature;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
        return f;
    }    

    public void getSigner(String keyPath) throws Exception {
        byte[] keyData = cleanseKeyData(readData(keyPath));
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyData);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PrivateKey key = kf.generatePrivate(keySpec);
        sig = Signature.getInstance("SHA1withRSA");
        sig.initSign(key);
    }

    /**
     * Parses an X509 PEM formatted base64 encoded private key, returns the
     * decoded private key byte data
     *
     * @param keyData PEM file contents, a X509 base64 encoded private key
     * @return Private key data
     * @throws IOException
     */
    private static byte[] cleanseKeyData(byte[] keyData) throws IOException {
        StringBuilder sb = new StringBuilder();
        String[] lines = new String(keyData).split("\n");
        String[] skips = new String[]{"-----BEGIN", "-----END", ": "};
        for (String line : lines) {
            boolean skipLine = false;
            for (String skip : skips) {
                if (line.contains(skip)) {
                    skipLine = true;
                }
            }
            if (!skipLine) {
                sb.append(line.trim());
            }
        }
        return Base64.decode(sb.toString());
    }

    /**
     * Reads the raw byte[] data from a file resource
     *
     * @param resourcePath
     * @return the raw byte data from a resource file
     * @throws IOException
     */
    public static byte[] readData(String resourcePath) {
        InputStream is = null;
        DataInputStream dis = null;
        byte[] data = null;
        try {
            is = new FileInputStream(new File(resourcePath));//MessageSigner.class.getResourceAsStream(resourcePath);
//            if (is == null) {
//                throw new IOException(String.format("Can't open resource \"%s\"", resourcePath));
//            }
            dis = new DataInputStream(is);
            data = new byte[dis.available()];
            dis.readFully(data);
            dis.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                    is = null;
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            if (dis != null) {
                try {
                    dis.close();
                    dis = null;
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return data;
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

	
}
