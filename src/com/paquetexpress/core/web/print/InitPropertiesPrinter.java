package com.paquetexpress.core.web.print;

import java.io.*;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import com.lowagie.text.pdf.codec.Base64;

import beanUtil.ConnectDB;

public class InitPropertiesPrinter {

    private static byte[] signature = null;

    /**
     * Constructor sin parametros
     */
    private InitPropertiesPrinter() {
    }

    /**
     * Método que regresa un objeto properties singleton con todos los querys de
     * la aplicacion RAD
     *
     * @return Objeto properties con querys de aplicacion RAD
     */
    public static String getInstance(String data) {
        try {
            return initSessionFactory(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Método que que regresa un objeto properties no singleton con todos los
     * querys de la aplicacion RAD
     *
     * @return Objeto properties con querys de aplicacion RAD
     */
    public static String getReloadInstance(String data) {
        try {
            signature = null;
            return initSessionFactory(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Signature sig;
    /**
     * Método que lee el archivo querys.Properties y los almacena en una
     * variable singleton
     */
    private static synchronized String initSessionFactory(String data) throws Exception {
        try {
            if (signature == null) {
            	
//            	java.io.InputStream is = null = ConnectDB.class.getClassLoader().getResourceAsStream("private-key-2019-2022.pem");
//				java.util.Properties p=new java.util.Properties();
//				if(is!=null) {
//					p.load(is);    
//					is.close();
//					is = null;
//				}
//				driver = p.getProperty("driver");
            	
            	//String ruta = "private-key-2019-2022.pem";
            	String ruta = "private-key-2022-2025.pem";
                signature = readData(ruta);
                System.out.println(signature);
            }
            getSigner();
            return sign(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
		return data;
    }
    
    public static void getSigner() throws Exception {
        byte[] keyData = cleanseKeyData(signature);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyData);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PrivateKey key = kf.generatePrivate(keySpec);
        sig = Signature.getInstance("SHA1withRSA");
        sig.initSign(key);
    }
    
        /**
     * Signs the specified data with the provided private key, returning the RSA
     * SHA1 signature
     *
     * @param data
     * @return
     * @throws Exception
     */
    private static String sign(String data) throws Exception {
        sig.update(data.getBytes());
        return Base64.encodeBytes(sig.sign());
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
        	
			is = InitPropertiesPrinter.class.getClassLoader().getResourceAsStream(resourcePath);
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

}
