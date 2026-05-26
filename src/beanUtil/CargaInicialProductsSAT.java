package beanUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import logger.AccessLog;
import mx.com.paquetexpress.general.model.dto.SysSspDesMstrDTO;

public class CargaInicialProductsSAT {
	private final static String msgError  = new StringBuffer("MSERR_B_").append(CargaInicialProductsSAT.class.getName()).append(".").toString();
	private static StringBuffer concatena = new StringBuffer();
	@SuppressWarnings("rawtypes")
	private static ArrayList productsSAT;
	
	/**
     * Constructor sin parametros
     */
	private CargaInicialProductsSAT() {
	}
	
	@SuppressWarnings("rawtypes")
	public static ArrayList getInstance() {
        try {
            initSessionFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return productsSAT;
    }
	
	/**
     * Método que lee el archivo querys.Properties y los almacena en una
     * variable singleton
     */
    @SuppressWarnings("unchecked")
	private static synchronized void initSessionFactory() throws Exception {
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            if (productsSAT == null) {
                conn = ConnectDB.getConnection();                
                String query = concatena.delete(0, concatena.length())
        				.append("SELECT  PRODUCT_ID, DESCRIPTION, SIMILAR, SIMILAR_PQTX ")
        				.append("FROM    BOK_CAT_PRODUCTS "
        						+ "WHERE ACTV_FLAG = 'Y'").toString();
                pst = conn.prepareStatement(query);
                pst.setFetchSize(29000);
                rs = pst.executeQuery();
                productsSAT = new ArrayList<SysSspDesMstrDTO>();
                while (rs.next()) {
                    productsSAT.add(new SysSspDesMstrDTO(rs.getString("PRODUCT_ID"), rs.getString("DESCRIPTION"), rs.getString("SIMILAR"), rs.getString("SIMILAR_PQTX")));
                }
            }
        } catch (Exception e) {
        	AccessLog.Log(concatena.delete(0,concatena.length()).append(msgError).append("getLovRecords()Error_:").append(e).toString());
            e.printStackTrace();
        } finally {
            try{
                if (rs != null) {
                    rs.close();
                }                
            }catch(Exception ex){
                ex.printStackTrace();
            }
            try{
                if (pst != null) {
                    pst.close();
                }                
            }catch(Exception ex){
                ex.printStackTrace();
            }            
            try{
                if (conn != null) {
                    conn.close();
                }                
            }catch(Exception ex){
                ex.printStackTrace();
            }                        
        }
    }
}
