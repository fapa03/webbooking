package beanUtil;

import java.sql.Connection;

import javax.naming.Context;
//import javax.naming.InitialContext;
import javax.sql.DataSource;

public class DataSourceConexion {
	public DataSourceConexion() {		
		super();
		getConexion();
	}

	private static DataSourceConexion laConexion = null;

	public static DataSourceConexion getConexion() {
		if (laConexion == null) {
			synchronized (DataSourceConexion.class) {
				if (laConexion == null) {
					laConexion = new DataSourceConexion();
				}
			}
		}
		return laConexion;
	}

	public static Connection Conecta(String jndiDataSource) {
		Connection con = null;
		
		try {
			Context initContext = new javax.naming.InitialContext();
			Context envContext = (Context) initContext.lookup("java:/comp/env");
			DataSource ds = (DataSource) envContext.lookup(jndiDataSource);
			con = ds.getConnection();
			
//			InitialContext initialContext = new InitialContext();
//          DataSource datasource = (DataSource) initialContext.lookup(jndiDataSource);
//          con = datasource.getConnection();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return con;
	}

}
